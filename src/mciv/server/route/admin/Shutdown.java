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

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.Server;
import mciv.server.route.Log;
import mciv.server.route.Route;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Shutdown extends Route {
	
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Halts the mciv server.
	 * 
	 * REQUEST BODY:
	 *   None.
	 * 
	 * RESPONSE BODY:
	 *   None.
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
	public void handle( HttpExchange exchange ) throws IOException {
		String requestBody = new String( exchange.getRequestBody().readAllBytes() );

		String responseBody = "Shutting down the server...";
		
		Log.get().accept( exchange, requestBody, responseBody );

		exchange.sendResponseHeaders( 200, responseBody.getBytes().length );
		exchange.getResponseBody().write( responseBody.getBytes() );
		exchange.close();
		
		Server.stop( 5 );
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


}
