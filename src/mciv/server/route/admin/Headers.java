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

import java.util.Map.Entry;
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Log;
import mciv.server.route.Params;
import mciv.server.route.Registrar;
import mciv.server.route.API;
import sog.core.Procedure;
import sog.core.Test;
import sog.util.json.JSON.JsonObject;

/**
 * 
 */
@Test.Subject( "test." )
public class Headers extends AdminRoute {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Retrieve header information from recent transactions.
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
	public Headers() {
	}

	@Override 
	public Procedure makeResponse( HttpExchange exchange, JsonObject requestBody, Params params ) throws Exception {
		int trunc = params.getInt( "trunc", 100 );
		Log.get().truncate( trunc );
		
		int count = params.getInt( "count", 10 );
		
		params.getEntries().forEach( this::enableDisable );

		this.sendHtml( exchange, Stream.concat( Stream.of( "<h1>Headers:</h1>" ), Log.get().getHeaders( count ) ) );

		return Procedure.NOOP;
	}
	
	private void enableDisable( Entry<String, String> entry ) {
		Boolean enable = "enable".equals( entry.getKey() ) ? Boolean.TRUE
			: "disable".equals( entry.getKey() ) ? Boolean.FALSE : null;
		
		if ( enable == null ) {
			return;
		}
		
		Registrar.get().getRoutes()
			.filter( r -> r.getPath().startsWith( entry.getValue() ) )
			.forEach( r -> r.setLogging( enable ) );
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
	
	@Override
	public API getRequestAPI() {
		return super.getRequestAPI()
			.member( "trunc", "Delete all but the given number of headers." ).integer()
			.member( "enable", "Enable logging for the given path and any descendants" ).string()
			.member( "disable", "Disable logging for the given path and any descendants" ).string()
			.member( "count", "The number of headers to return; default is 10." ).integer();
	}

	@Override
	public API getResponseAPI() {
		return super.getResponseAPI();
	}


}
