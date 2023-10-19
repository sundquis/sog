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

import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;

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
public class Pull extends Route {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Pull the current MegaEmpires repo, exposing assets in ./static
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
	public Pull() {
	}

	@Override
	public Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception {
		int timeout = params.getInt( "timeout", 5 );
		
		String cmd = new LocalDir().sub( "tool" ).sub( "bin" ).getFile( "MCIV_PULL", LocalDir.Type.BASH ).toString();
		Process proc = Runtime.getRuntime().exec( cmd );
		proc.waitFor( timeout, TimeUnit.SECONDS );
		String msg = new String( proc.getInputStream().readAllBytes() );

		return Response.forMessage( msg, exchange );
	}

	@Override
	public Category getCategory() {
		return Category.Administration;
	}

	@Override
	public int getSequence() {
		return 210;
	}

	@Override
	public String getPath() {
		return "/admin/pull";
	}

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "timeout", "Seconds to wait for command to complete; default is 5." ).integer();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}

	
}
