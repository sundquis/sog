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
import mciv.server.route.Route;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.json.JSON;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class AuthRoute extends Route {
	
	protected AuthRoute() {}
	
	public abstract JsonValue respond( JsonValue requestBody ) throws Exception;

	@Override
	public Procedure makeResponse( HttpExchange exchange, JsonValue requestBody, Params params ) throws Exception {
		JsonValue responseBody = this.respond( requestBody );
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );
		
		exchange.sendResponseHeaders( 200, 0 );
		JSON.write( responseBody, exchange.getResponseBody() );
		return Procedure.NOOP;
	}
	
	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "email", "The identifying email address of the player." ).string( )
			.member( "handle", "The optional screen name used to identify players in the game." ).string();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI()
			.member( "status", "Status code. See mciv.server.route.Status." ).integer( -1, 0, 1, 2, 3, 4, 5, 400 )
			.member( "message", "Descriptive erorr message when status > 0." ).string()
			.member( "playerId", "Authentication token used to identify the plater." ).string( );
	}
	

}
