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

import java.util.function.Function;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Registrar;
import mciv.server.route.Route;
import sog.core.Procedure;
import sog.core.Test;
import sog.util.Macro;
import sog.util.json.JSON.JsonObject;

/**
 * 
 */
@Test.Subject( "test." )
public class RouteInfo extends AdminRoute {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Display information about all registered routes.
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
	public RouteInfo() {}

	@Override 
	public Procedure makeResponse( HttpExchange exchange, JsonObject requestBody, Params params ) throws Exception {

		// BODY <H1>Routes</h1>
		// BODY <ul>
		// BODY ${route links}
		// BODY </ul>
		// BODY ${route apis}
		
		Function<Route, String> map = (r) -> "<li><a href='#" + r.getPath() + "'>" + r.getPath() + "</a></li>";
		Macro mapper = new Macro()
			.expand( "route links", Registrar.get().getRoutes().map( map ) )
			.expand( "route apis", Registrar.get().getRoutes().flatMap( Route::getDocunmentation ) );
		
		this.sendHtml( exchange, this.getCommentedLines( "BODY" ).flatMap( mapper ) );
		
		return Procedure.NOOP;
	}


	@Override
	public String getPath() {
		return "/admin/routes";
	}

	@Override
	public Category getCategory() {
		return Route.Category.Administration;
	}

	@Override
	public int getSequence() {
		return 0;
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
