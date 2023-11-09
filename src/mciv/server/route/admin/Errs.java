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

import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.API;
import mciv.server.route.Error;
import mciv.server.route.Params;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class Errs extends AdminRoute {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Retrieve information regarding recent errors.
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
	public Errs() {}

	@Override
	public Procedure makeResponse( HttpExchange exchange, JsonValue requestBody, Params params ) throws Exception {
		int trunc = params.getInt( "trunc", 100 );
		Error.get().truncate( trunc );
		
		int count = params.getInt( "count", 10 );

		this.sendHtml( exchange, Stream.concat( Stream.of( "<h1>Errors:</h1>" ), Error.get().getErrors( count ) ) );

		return Procedure.NOOP;
	}

	@Override
	public Category getCategory() {
		return Category.Administration;
	}

	@Override
	public int getSequence() {
		return 36;
	}

	@Override
	public String getPath() {
		return "/admin/errs";
	}

	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "trunc", "Delete all but the given number of errors." ).integer()
			.member( "count", "The number of errors to return; default is 10." ).integer();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}


}
