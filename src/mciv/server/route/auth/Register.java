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

package mciv.server.route.auth;



import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.Route;
import sog.core.Test;
import sog.util.JSON;

/**
 * 
 */
@Test.Subject( "test." )
public class Register extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Use supplied credentials to create a new user.
	 *   The required email address is used as the login name.
	 *   The required password must have at least 8 characters.
	 *   The required handle is used to identify the user in application messaging.
	 *   Insert description of potential state transitions.
	 * 	
	 * REQUEST BODY:
	 *   Request: {
	 *     "email": str,
	 *     "password": str,
	 *     "handle": str
	 *   }
	 * 	
	 * RESPONSE BODY:
	 *   Response: {
	 *     "status": int( <values enumerated in mciv.server.route.Codes> ),
	 *     "data": Data,
	 *     "error": Error
	 *   }
	 * 	
	 *   Where Data: {
	 *     "token": str
	 *   }
	 * 
	 *   Where Error: [ str ]
	 *   When status = 400, error is a stack trace corresponding to a programmatic error.
	 * 
	 * EXCEPTIONS:
	 *   Status
	 *   1: Email address does not parse
	 *   2: Password does not meet requirements
	 *   3: User exists
	 * 
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Register() {}

	@Override
	public void handle( HttpExchange exchange ) throws IOException {
		String requestBody = new String( exchange.getRequestBody().readAllBytes() );

		if ( exchange.getRequestHeaders().containsKey( "Origin" ) ) {
			String origin = exchange.getRequestHeaders().getFirst( "Origin" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", origin );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "POST, GET" );
			exchange.getResponseHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );
		}
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );

		String responseBody = JSON.obj()
			.add( "status", JSON.num( -1 ) )
			.add( "data", JSON.obj().add( "token", JSON.str( "authenticated-token" ) ) )
			.add( "error", JSON.arr() )
			.toString();
		
		Log.get().accept( exchange, requestBody, responseBody );

		exchange.sendResponseHeaders( 200, responseBody.getBytes().length );
		exchange.getResponseBody().write( responseBody.getBytes() );
		exchange.close();
	}
	

	@Override
	public String getPath() {
		return "/auth/register";
	}
	
	@Override
	public Category getCategory() {
		return Route.Category.Authorization;
	}

	@Override
	public int getSequence() {
		return 20;
	}


}
