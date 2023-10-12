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

import com.sun.net.httpserver.HttpExchange;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.JSON;

/**
 * 
 */
@Test.Subject( "test." )
public interface Response {

	/* The String for the response body */
	public String getBody();
	
	/* A Procedure to execute after the Exchange is closed */
	public Procedure afterClose();
	
	/* Add builders for each type of response. */
	
	/* String response, no closing operation */
	public static Response build( final String body ) {
		return new Response() {
			@Override public String getBody() { return body; }
			@Override public Procedure afterClose() { return Procedure.NOOP; }
		};
	}

	/* String response, with closing operation */
	public static Response build( final String body, final Procedure afterClose ) {
		return new Response() {
			@Override public String getBody() { return body; }
			@Override public Procedure afterClose() { return afterClose; }
		};
	}
	
	/*
	 * JSON response, no closing operation.
	 * The correct response header is added to the exchange.
	 */
	public static Response build( HttpExchange exchange, JSON.Element json) {
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );
		return new Response() {
			@Override public String getBody() { return json.toString(); }
			@Override public Procedure afterClose() { return Procedure.NOOP; }
		};
	}

	/*
	 * JSON response, with closing operation.
	 * The correct response header is added to the exchange.
	 */
	public static Response build( HttpExchange exchange, JSON.Element json, Procedure afterClose ) {
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );
		return new Response() {
			@Override public String getBody() { return json.toString(); }
			@Override public Procedure afterClose() { return afterClose; }
		};
	}


}
