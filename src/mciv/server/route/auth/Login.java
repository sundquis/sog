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

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.Test;
import sog.util.json.JSON;
import sog.util.json.JSON.JObject;

/**
 * 
 */
@Test.Subject( "test." )
public class Login extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Use supplied credentials to log in a user.
	 *   The required email address is the login name.
	 *   The required password must match stored value.
	 *   The optional handle is used to identify the user in application messaging, overriding
	 *   the handle used to create the user.
	 * 	
	 * REQUEST BODY:
	 *   Response: {
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
	 * 	
	 * EXCEPTIONS:
	 *   Status
	 *   4: Authentication failure
	 * 	
	 * 	</pre>
	 * <a href="#">Top</a>
	 * 	
	 */
	
	public Login() {}

	@Override 
	public Response getResponse( HttpExchange exchange, String requestBody, JObject params ) throws Exception {
		return Response.build( exchange, JSON.obj()
			.add( "status", JSON.num( -1 ) )
			.add( "data", JSON.obj().add( "token", JSON.str( "authenticated-token" ) ) )
			.add( "error", JSON.arr() )
			.add( "(REMOVE) Response", JSON.str( requestBody ) ) );
	}


	@Override
	public String getPath() {
		return "/auth/login";
	}

	@Override
	public Category getCategory() {
		return Route.Category.Authorization;
	}

	@Override
	public int getSequence() {
		return 10;
	}

}
