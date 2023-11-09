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
import sog.core.Procedure;
import sog.core.Test;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class Reload extends AdminRoute {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Check for new Routes and register them with the server.
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
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Reload() {
	}

	@Override 
	public Procedure makeResponse( HttpExchange exchange, JsonValue requestBody, Params params ) throws Exception {
		this.sendHtml( exchange, "Reloading routes..." );
		return () -> Server.get().load();
	}

	@Override
	public Category getCategory() {
		return Category.Administration;
	}

	@Override
	public int getSequence() {
		return 50;
	}

	@Override
	public String getPath() {
		return "/admin/reload";
	}

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}

}
