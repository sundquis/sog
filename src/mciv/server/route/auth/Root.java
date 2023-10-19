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

import mciv.server.McivException;
import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Response;
import mciv.server.route.Route;
import sog.core.LocalDir;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Root extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   This route serves files from the root of the mciv root directory,
	 *   Any unimplemented routes will resolve to this handler, and attempt to serve
	 *   the corresponding file, resulting in a file not found error.
	 * 	
	 * REQUEST BODY:
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
	 * 
	 * EXCEPTIONS:
	 * 
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */
	public Root() {
	}
	
	private static final Path ROOT = new LocalDir().sub( "web" ).getDir();

	@Override
	public Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception {
		String fileName = exchange.getRequestURI().getPath();
		if ( fileName.startsWith( "/" ) ) {
			fileName = fileName.substring( 1 );
		}
		if ( fileName.isEmpty() ) {
			fileName = "index.html";
		}
		
		Path path = ROOT.resolve( fileName );

		if ( ! Files.exists( path ) ) {
			throw new McivException( "File not found: " + path );
		}
		
		if ( ! Files.isRegularFile( path ) ) {
			throw new McivException( "Not a regular file: " + path );
		}
		
		return Response.forFile( path, exchange );
	}

	@Override
	public Category getCategory() {
		return Category.Root;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public String getPath() {
		return "/";
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
