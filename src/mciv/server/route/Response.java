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

package mciv.server.route;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.sun.net.httpserver.HttpExchange;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.json.JSON;

/**
 * Responsible for:
 *  + Setting the Content-Type header
 *  + Determining the length of the response
 *  + Providing the InputStream representing the response
 */
@Test.Subject( "test." )
public interface Response {

	/* The String for the response body */
	public InputStream getBody() throws IOException;

	/* Total length in bytes */
	public long getLength();
	
	/* A Procedure to execute after the Exchange is closed */
	public Procedure afterClose();
	
	/* Add builders for each type of response. */
	
	private static InputStream forString( String s ) {
		return new ByteArrayInputStream( s.getBytes() );
	}
	
	/* Serve a file from the web directory */
	public static Response forFile( final Path file, HttpExchange exchange ) throws IOException {
		final long length = file.toFile().length();

		String content = Files.probeContentType( file );
		if ( content == null ) {
			exchange.getResponseHeaders().add( "Content-Type", "text/plain" );
		} else {
			exchange.getResponseHeaders().add( "Content-Type", content );
		}

		return new Response() {
			@Override public InputStream getBody() throws IOException {
				return Files.newInputStream( file, StandardOpenOption.READ );
			}
			
			@Override
			public long getLength() {
				return length;
			}

			@Override
			public Procedure afterClose() {
				return Procedure.NOOP;
			}
			
		};
	}

	/* Simple message, with closing operation */
	public static Response forMessage( String msg, HttpExchange exchange, Procedure afterClose ) {
		String body = "<!DOCTYPE html><html><body><h1>MCIV Server:</h1><pre>" + msg + "</pre></body></html>";
		
		return Response.forHtml( body, exchange, afterClose );
	}
	
	/* Simple message, non closing operation */
	public static Response forMessage( String msg, HttpExchange exchange ) {
		return Response.forMessage( msg, exchange, Procedure.NOOP );
	}
	
	/* HTML response, with closing operation */
	public static Response forHtml( String body, HttpExchange exchange, Procedure afterClose ) {
		exchange.getResponseHeaders().add( "Content-Type", "text/html" );
		return new Response() {
			@Override public long getLength() { return body.getBytes().length; }
			@Override public InputStream getBody() { return Response.forString( body ); }
			@Override public Procedure afterClose() { return afterClose; }
		};
	}
	
	/* HTML response, no closing operation */
	public static Response forHtml( String body, HttpExchange exchange ) {
		return Response.forHtml( body, exchange, Procedure.NOOP );
	}
	
	/* JSON response, with closing operation. */
	public static Response forJSON( HttpExchange exchange, JSON.JElement json, Procedure afterClose ) {
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );
		String body = json.toJSON();
		return new Response() {
			@Override public long getLength() { return body.getBytes().length; }
			@Override public InputStream getBody() { return Response.forString( body ); }
			@Override public Procedure afterClose() { return afterClose; }
		};
	}

	/* JSON response, no closing operation. */
	public static Response forJSON( HttpExchange exchange, JSON.JElement json) {
		return Response.forJSON( exchange, json, Procedure.NOOP );
	}


}
