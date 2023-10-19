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
public class Register extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Use supplied credentials to create a new user.
	 *   The required email address is used as the login name.
	 *   The required password must have at least 8 characters.
	 *   The required handle is used to identify the user in application messaging.
	 *   Insert description of potential state transitions.
	 * 	
	 * REQUEST BODY:
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
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
	public Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception {
		return Response.forJSON( exchange, JSON.obj()
			.add( "status", JSON.num( -1 ) )
			.add( "(REMOVE) Request was", JSON.str( requestBody ) ) );
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
