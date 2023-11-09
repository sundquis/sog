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
import java.nio.file.StandardOpenOption;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.McivException;
import mciv.server.route.Params;
import mciv.server.route.Route;
import sog.core.LocalDir;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.json.JSON.JsonValue;

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
	public Root() {}
	
	private static final Path ROOT = new LocalDir().sub( "web" ).getDir();

	@Override
	public Procedure makeResponse( HttpExchange exchange, JsonValue requestBody, Params params ) throws Exception {
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
		
		long length = path.toFile().length();

		String content = Files.probeContentType( path );
		if ( content == null ) {
			exchange.getResponseHeaders().add( "Content-Type", "text/plain" );
		} else {
			exchange.getResponseHeaders().add( "Content-Type", content );
		}

		exchange.sendResponseHeaders( 200, length );
		Files.newInputStream( path, StandardOpenOption.READ ).transferTo( exchange.getResponseBody() );
		
		return Procedure.NOOP;
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

}
