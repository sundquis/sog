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

import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.LocalDir;
import sog.core.Test;
import sog.util.json.JSON.JObject;

/**
 * 
 */
@Test.Subject( "test." )
public class Static  extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}/index.html">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Retrieve asset from the /static directory.
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
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Static() {
	}

	@Override
	public Response getResponse( HttpExchange exchange, String requestBody, JObject params ) throws Exception {
		String file = exchange.getRequestURI().getPath();
		Path path = new LocalDir().sub( "ext" ).getDir().resolve( file.replaceAll( "^/", "" ) );

		return Response.build( path );
	}

	@Override
	public Category getCategory() {
		return Category.Authorization;
	}

	@Override
	public int getSequence() {
		return 9;
	}

	@Override
	public String getPath() {
		return "/static";
	}


}
