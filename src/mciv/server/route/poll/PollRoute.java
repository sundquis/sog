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

package mciv.server.route.poll;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Route;
import sog.core.Procedure;
import sog.core.Test;
import sog.util.json.JSON;
import sog.util.json.JSON.JsonObject;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class PollRoute extends Route {

	public PollRoute() {}

	public abstract JsonObject respond( JsonObject requestBody ) throws Exception;

	@Override
	public Procedure makeResponse( HttpExchange exchange, JsonObject requestBody, Params params ) throws Exception {
		JsonObject responseBody = this.respond( requestBody );
		exchange.getResponseHeaders().add( "Content-Type", "application/json" );
		
		exchange.sendResponseHeaders( 200, 0 );
		JSON.write( responseBody, exchange.getResponseBody() );
		return Procedure.NOOP;
	}
	
	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "playerId", "Authentication token used to identify the player." ).string( )
		;
	}


}
