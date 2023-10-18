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

import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.App;
import sog.core.LocalDir;
import sog.core.Test;

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
	public Static() {
	}

	@Override
	public Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception {
		String file = exchange.getRequestURI().getPath();
		Path path = new LocalDir().sub( "ext" ).getDir().resolve( file.replaceFirst( "^/", "" ) );
		
		App.get().msg( "File: " + path.toString() );
		App.get().msg( "Content-Type: " + Files.probeContentType( path ) );

		// FIXME: Type will depend on file extension
		//exchange.getResponseHeaders().add( "Content-Type", "text/html" );
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

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}


}
