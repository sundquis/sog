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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import sog.core.App;
import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class SimpleServer {
	
	private static final int MCIV_PORT = 1104;
	
	private final HttpServer server;
	
	private SimpleServer() throws IOException {
		this.server = HttpServer.create( new InetSocketAddress( MCIV_PORT ), 0 );
		this.server.setExecutor( null );
		
		this.server.createContext( "/", new RootHandler() );
		this.server.createContext( "/stop", new ShutdownHandler() );
		this.server.createContext( "/data", new DataHandler() );
		this.server.createContext( "/corstest", new CorsHandler() );
		
		this.server.start();
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
	
	
	
	private class DataHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			//exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "POST,GET,HEAD,OPTIONS" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", "*" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Headers", "Arg1, Arg2" );
			exchange.getResponseHeaders().add( "Content-Type", "application/json" );
			
			String array = "[1, 2, 3]";
			String response = "[ " + array
				+ ", " + exchange.getRequestHeaders().get( "Arg1" ) 
				+ ", " + exchange.getRequestHeaders().get( "Arg2" )
				+ "]";

			exchange.sendResponseHeaders( 200, response.getBytes().length );
			exchange.getResponseBody().write( response.getBytes() );
			exchange.close();
		}
		
	}
	
	
	private static String logRequest( HttpExchange exchange ) throws IOException {
		App.get().msg( "URI: " + exchange.getRequestURI() );
		App.get().msg( "REMOTE: " + exchange.getRemoteAddress() );
		App.get().msg( "METHOD: " + exchange.getRequestMethod() );
		App.get().msg( "HEADERS:" );
		Map<String, List<String>> hdrs = exchange.getRequestHeaders();
		for ( Map.Entry<String, List<String>> hdr : hdrs.entrySet() ) {
			System.out.print( "\t" + hdr.getKey() + ": " );
			System.out.println( hdr.getValue().stream().collect( Collectors.joining( ", " ) ) );
		}
		byte[] bytes = exchange.getRequestBody().readAllBytes();
		App.get().msg( "BODY LENGTH: " + bytes.length );
		App.get().msg( "START BODY" );
		String body = new String( bytes );
		System.out.println( body );
		App.get().msg( "END BODY" );
		return body;
	}
	
	
	private class ShutdownHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			logRequest( exchange );
			
			String response = "Shutting down...";

			exchange.sendResponseHeaders( 200, response.getBytes().length );
			exchange.getResponseBody().write( response.getBytes() );
			
			exchange.close();
			
			SimpleServer.this.server.stop( 1 );
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
	
	private class XRootHandler implements HttpHandler {

		@Override
		public void handle( HttpExchange exchange ) throws IOException {
			StringBuilder sb = new StringBuilder();
			sb.append( "<html>" );
			sb.append( "<H1>Hello World!</H1>" ).append( "</p>" );

			//exchange.getRequestHeaders().add( "Vary", "Origin" );
			//exchange.getRequestHeaders().add( "Origin", "http://normandale.edu" );
			addMap( sb, "Request Headers", exchange.getRequestHeaders() );
			sb.append( "<H2>URI</H2>" ).append( exchange.getRequestURI() );
			sb.append( "<H2>Method</H2>" ).append( exchange.getRequestMethod() );
			sb.append( "<H2>Protocol</H2>" ).append( exchange.getProtocol() );
			sb.append( "<H2>Remote</H2>" ).append( exchange.getRemoteAddress() );
			sb.append( "<H2>Local</H2>" ).append( exchange.getLocalAddress() );
			sb.append( "<H2>Request Body</H2>" );
			InputStreamReader isr;
			BufferedReader br = new BufferedReader( new InputStreamReader( exchange.getRequestBody(), "utf-8" ) );
			char[] cbuf = new char[512];
			int len = 0;
			while ( (len = br.read( cbuf, 0, 512 )) != -1 ) {
				sb.append( cbuf, 0, len );
			}
			sb.append( "</p>Done." );
			
			sb.append( "</html>" );
			
			App.get().msg( "Token: " + exchange.getAttribute( "token" ) );
			
			exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "GET,POST,PUT" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", "*" );
			
			addMap( sb, "Response Headers", exchange.getResponseHeaders() );
			
			String response = sb.toString();
			exchange.sendResponseHeaders( 200, response.getBytes().length );
			exchange.getResponseBody().write( response.getBytes() );

			

			App.get().msg( "From: " + exchange.getRemoteAddress() + "\n" );
			App.get().msg( "Time: " + exchange.getResponseHeaders().get( "Date" ) + "\n" );
			
			exchange.close();
		}
		
	}
	
	private static void addMap( StringBuilder sb, String label, Map<String, List<String>> hdrs ) {
		sb.append( "<H2>" ).append( label ).append( "</H2>" );
		sb.append( "<ul>" );
		for ( Map.Entry<String, List<String>> entry : hdrs.entrySet() ) {
			sb.append( "<li><b>" ).append( entry.getKey() ).append( ": " );
			sb.append( entry.getValue().stream().collect( Collectors.joining( ", " ) ) ).append( "</i>" );
		}
		sb.append( "</ul>" );		
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
