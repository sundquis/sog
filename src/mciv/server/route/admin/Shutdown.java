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

package mciv.server.route.admin;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.Server;
import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Route;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class Shutdown extends AdminRoute {
	
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Halts the mciv server.
	 * 
	 * REQUEST BODY:
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
	 * 
	 * EXCEPTIONS:
	 *   None.
	 * 
	 * 	</pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Shutdown() {}

	@Override 
	public Procedure makeResponse( HttpExchange exchange, JsonValue requestBody, Params params ) throws Exception {
		int delay = params.getInt( "delay", 5 );

		this.sendHtml( exchange, "Shutting down the server in " + delay + " seconds." );
		
		return () -> Server.get().stop( delay );
	}


	@Override
	public String getPath() {
		return "/admin/shutdown";
	}

	@Override
	public Category getCategory() {
		return Route.Category.Administration;
	}

	@Override
	public int getSequence() {
		return 100;
	}

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "delay", "Number of seconds to wait before shutting down; default is 5." ).integer(  );
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}

}
