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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.Route;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Home extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Present the starting page of the application.
	 * 	
	 * REQUEST BODY:
	 *     None.
	 * 	
	 * RESPONSE BODY:
	 *     None.
	 * 	
	 * EXCEPTIONS:
	 *     None.
	 * 	
	 * </pre>
	 * <a href="#">Top</a>
	 * 	
	 */
	public Home() {}

	@Override
	public void handle( HttpExchange exchange ) throws IOException {
		String requestBody = new String( exchange.getRequestBody().readAllBytes() );

		// FIXME
		Path p = Path.of( "/", "home", "sundquis", "book", "MegaEmpires", "static", "bundle.js" );

		String pre = "<html><head><meta charset='utf-8'></head><body><div id='root'></div><script>";
		String content = "";
		try {
			content = new String( Files.readAllBytes( p ) );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		String post = "</script></body></html>";

		String responseBody = pre + content + post;
		
		Log.get().accept( exchange, requestBody, responseBody );

		exchange.sendResponseHeaders( 200, responseBody.getBytes().length );
		exchange.getResponseBody().write( responseBody.getBytes() );
		exchange.close();
	}

	@Override
	public Category getCategory() {
		return Route.Category.Authorization;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public String getPath() {
		return "/auth/home";
	}


}
