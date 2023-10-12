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
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Headers extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Retrieve header information from recent transactions.
	 * 	
	 * REQUEST BODY:
	 *   Request: {
	 *     n: int
	 *   }
	 *   
	 *   Or supply n=count as a URL parameter.
	 * 	
	 * RESPONSE BODY:
	 *   None.
	 * 
	 * EXCEPTIONS:
	 *   None.
	 * 
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Headers() {
	}

	@Override
	public Response getResponse( HttpExchange exchange, String requestBody, Map<String, String> params ) throws Exception {
		int count = 0;
		try {
			count = Integer.valueOf( params.get( "n" ) );
		} catch ( Exception ex ) {
			count = 10;
		}
		
		// PRE <html>
		// PRE <head><meta charset="utf-8"></head>
		// PRE <body>
		// PRE <H1>Headers</h1>
		// PRE <pre>
		
		// POST </pre>
		// POST </body>
		// POST </html>
		
		StringWriter sw = new StringWriter();
		final PrintWriter out = new PrintWriter( sw );
		
		this.getCommentedLines( "PRE" ).forEach( out::println );
		Log.get().getHeaders( count ).forEach( out::println );
		this.getCommentedLines( "POST" ).forEach( out::println );
		
		return Response.build( sw.toString() );
	}

	@Override
	public Category getCategory() {
		return Category.Administration;
	}

	@Override
	public int getSequence() {
		return 35;
	}

	@Override
	public String getPath() {
		return "/admin/hdrs";
	}


}
