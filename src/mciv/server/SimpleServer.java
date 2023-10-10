/**
 * Copyright (C) 2021, 2023
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */

package mciv.server;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import sog.core.App;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class SimpleServer {
	
	private static final int MCIV_PORT = 1104;
	
	private final HttpServer server;
	
	
	private static final ExecutorService exec = Executors.newCachedThreadPool();
	
	private SimpleServer() throws IOException {
		this.server = HttpServer.create( new InetSocketAddress( MCIV_PORT ), 0 );
		this.server.setExecutor( exec );
		
		this.server.createContext( "/", new RootHandler() );
		this.server.createContext( "/log", new LogHandler() );
		this.server.createContext( "/logtrim", new LogTrimHandler() );
		this.server.createContext( "/stop", new ShutdownHandler() );
		this.server.createContext( "/corstest", new CorsHandler() );
		
		this.server.start();
	}
	
	
	private static String logRequest( HttpExchange exchange ) throws IOException {
		App.get()
			.msg()
			.msg()
			.msg( new Date() )
			.msg( "URI: " + exchange.getRequestURI() )
			.msg( "REMOTE: " + exchange.getRemoteAddress() )
			.msg( "METHOD: " + exchange.getRequestMethod() )
			.msg( "RESPONSE CODE: " + exchange.getResponseCode() )
			.msg( "HEADERS:" );
			
		Map<String, List<String>> hdrs = exchange.getRequestHeaders();
		for ( Map.Entry<String, List<String>> hdr : hdrs.entrySet() ) {
			App.get().msg( "\t" + hdr.getKey() + ": " + hdr.getValue().stream().collect( Collectors.joining( ", " ) ) );
		}
		
		byte[] bytes = exchange.getRequestBody().readAllBytes();
		String body = new String( bytes );
		
		App.get()
			.msg( "BODY LENGTH: " + bytes.length )
			.msg( "BODY: " + body );

		return body;
	}
	
	
	private class CorsHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			String body = logRequest( exchange );
			
			String origin = exchange.getRequestHeaders().getFirst( "Origin" );
			
			exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "POST, GET" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", origin );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Headers", "Content-Type, Arg1, Arg2" );
			exchange.getResponseHeaders().add( "Content-Type", "application/json" );
			
			String response = "{"
				+ "\"arg1\": \"" + exchange.getRequestHeaders().getFirst( "Arg1" ) + "\", "
				+ "\"arg2\": \"" + exchange.getRequestHeaders().getFirst( "Arg2" ) + "\", "
				+ "\"request-json\": " + body + ", "
				+ "\"response-json\": " + "{ \"gameid\": 123 }"
			+ "}";

			exchange.sendResponseHeaders( 200, response.getBytes().length );
			exchange.getResponseBody().write( response.getBytes() );
			exchange.close();
		}
		
	}
	
	private class ShutdownHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			String response = "Shutting down...";

			exchange.sendResponseHeaders( 200, response.getBytes().length );
			exchange.getResponseBody().write( response.getBytes() );
			
			exchange.close();
			exec.shutdown();
			exec.shutdownNow();
			
			SimpleServer.this.server.stop( 1 );
		}
		
	}
	
	private class LogHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			Path p = Path.of( "/", "home", "sundquis", "book", "sog", "bin", "RequestLog.txt" );

			String pre = "<html><head><meta charset='utf-8'></head><body><pre>";
			String post = "</pre></body></html>";
			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes( p );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			exchange.sendResponseHeaders( 200, bytes.length + pre.getBytes().length + post.getBytes().length );
			
			exchange.getResponseBody().write( pre.getBytes() );
			exchange.getResponseBody().write( bytes );
			exchange.getResponseBody().write( post.getBytes() );
			exchange.close();
		}
		
	}
	
	
	private class LogTrimHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			Path p = Path.of( "/", "home", "sundquis", "book", "sog", "bin", "RequestLog.txt" );
			RandomAccessFile raf = new RandomAccessFile( p.toFile(), "rw ");
			raf.setLength( 0L );
			raf.close();

			exchange.sendResponseHeaders( 200, 0 );
			
			exchange.close();
		}
		
	}
	
	
	private class RootHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			Path p = Path.of( "/", "home", "sundquis", "book", "MegaEmpires", "static", "bundle.js" );

			String pre = "<html><head><meta charset='utf-8'></head><body><div id='root'></div><script>";
			String post = "</script></body></html>";
			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes( p );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			exchange.sendResponseHeaders( 200, bytes.length + pre.getBytes().length + post.getBytes().length );
			
			exchange.getResponseBody().write( pre.getBytes() );
			exchange.getResponseBody().write( bytes );
			exchange.getResponseBody().write( post.getBytes() );
			exchange.close();
		}
		
	}
	
	public static void main( String[] args ) {
		try {
			new SimpleServer();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		App.get().done();
	}

}
