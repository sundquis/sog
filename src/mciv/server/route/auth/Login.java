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

import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.Test;
import sog.util.json.JSON;

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
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
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
	public Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception {
		return Response.forJSON( exchange, JSON.obj()
			.add( "status", JSON.num( -1 ) )
			.add( "(REMOVE) Request was", JSON.str( requestBody ) ) );
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

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "FIXME", "Determine request structure" ).string( );
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI()
			.member( "FIXME", "Determine response structure" ).string( );
	}

}
