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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.Registrar;
import mciv.server.route.Route;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class RouteInfo extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
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
	public void handle( HttpExchange exchange ) throws IOException {
		String requestBody = new String( exchange.getRequestBody().readAllBytes() );

		StringWriter sw = new StringWriter();
		final PrintWriter out = new PrintWriter( sw );
		
		// PRE	<html>
		// PRE	<head><meta charset="utf-8"></head>
		// PRE	<body>
		// PRE	<h1>Routes</h1>
		// PRE	<ul>
		this.getCommentedLines( "PRE" ).forEach( out::println );
		
		Function<Route, String> map = (r) -> "<li><a href='#" + r.getPath() + "'>" + r.getPath() + "</a></li>";
		Registrar.get().getRoutes().map( map ).forEach( out::println );
		out.println( "</ul>" );
		
		Registrar.get().getRoutes().flatMap( Route::getDocunmentation ).forEach( out::println );

		// POST	</body>
		// POST	</html>
		this.getCommentedLines( "POST" ).forEach( out::println );
		
		String responseBody = sw.toString();
		
		Log.get().accept( exchange, requestBody, responseBody );

		exchange.sendResponseHeaders( 200, responseBody.getBytes().length );
		exchange.getResponseBody().write( responseBody.getBytes() );
		exchange.close();
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
