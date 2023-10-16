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

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.API;
import mciv.server.route.Error;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.Test;
import sog.util.json.JSON.JElement;
import sog.util.json.JSON.JObject;

/**
 * 
 */
@Test.Subject( "test." )
public class Headers extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Retrieve header information from recent transactions.
	 * 	
	 * REQUEST BODY:
	 *   Request: {
	 *     : int
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
	public Response getResponse( HttpExchange exchange, String requestBody, JObject params ) throws Exception {
		int count = 10;
		JElement countElt = params.toJavaMap().get( "n" );
		if ( countElt != null ) {
			try {
				count = Integer.parseInt( countElt.toJString().toJavaString() );
			} catch ( NumberFormatException nfe ) {
				Error.get().accept( nfe );
			}
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
	
	public API getRequestAPI() {
		return API.obj( "Request", "JSON request object" )
			.member( "count", "The number of headers to return" ).integer(  );
	}


}
