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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Registrar;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.Test;
import sog.util.Macro;
import sog.util.json.JSON.JObject;

/**
 * 
 */
@Test.Subject( "test." )
public class RouteInfo extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Display information about all registered routes.
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
	public RouteInfo() {}

	@Override 
	public Response getResponse( HttpExchange exchange, String requestBody, JObject params ) throws Exception {
		StringWriter sw = new StringWriter();
		final PrintWriter out = new PrintWriter( sw );

		// HTML <html>
		// HTML <head><meta charset="utf-8"></head>
		// HTML <body>
		// HTML <H1>Routes</h1>
		// HTML <ul>
		// HTML ${route links}
		// HTML </ul>
		// HTML ${route apis}
		// HTML </body>
		// HTML </html>
		
		final String host = "/23.88.147.138:1104"; //exchange.getLocalAddress().toString();
		
		Function<Route, String> map = (r) -> "<li><a href='#" + r.getPath() + "'>" + r.getPath() + "</a></li>";
		Macro mapper = new Macro()
			.expand( "route links", 
				Registrar.get().getRoutes().map( map ).collect( Collectors.toList() ) )
			.expand( "route apis", 
				Registrar.get().getRoutes().flatMap( r -> r.getDocunmentation( host ) ).collect( Collectors.toList() ) );
		
		this.getCommentedLines( "HTML" ).flatMap( mapper ).forEach( out::println );

		return Response.build( sw.toString() );
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
	
	
}
