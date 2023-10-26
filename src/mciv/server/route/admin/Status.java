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

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.Server;
import mciv.server.route.API;
import mciv.server.route.Params;
import mciv.server.route.Registrar;
import mciv.server.route.Route;
import sog.core.Procedure;
import sog.core.Test;
import sog.util.FixedWidth;
import sog.util.Macro;
import sog.util.json.JSON.JsonObject;

/**
 * 
 */
@Test.Subject( "test." )
public class Status extends AdminRoute {

	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Print statistics about the current mciv server instance.
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
	
	private final FixedWidth serviceStats;
	
	private final FixedWidth remoteStats;
	
	private final FixedWidth errorStats;
	
	public Status() {
		this.serviceStats = new FixedWidth()
			.right( "Count", 8, ' ' )
			.sep( "  " )
			.left( "Endpoint", 50, ' ' );
		this.remoteStats = new FixedWidth()
			.right( "Count", 8, ' ' )
			.sep( "  " )
			.left( "Remote Host", 50, ' ' );
		this.errorStats = new FixedWidth()
			.right( "Errors", 8, ' ' )
			.sep( "  " )
			.left( "Endpoint", 50, ' ' );
	}


	@Override 
	public Procedure makeResponse( HttpExchange exchange, JsonObject requestBody, Params params ) throws Exception {

		// BODY <h1>MCIV Server Status</h1>
		// BODY <pre>
		// BODY OPERATION:
		// BODY   Server Started: ${start time}
		// BODY   Uptime: ${up time}
		// BODY 
		// BODY SERVICES:
		// BODY   ${service header}
		// BODY   ${service rows}
		// BODY 
		// BODY CLIENTS:
		// BODY   ${remote header}
		// BODY   ${remote rows}
		// BODY 
		// BODY ERRORS:
		// BODY   ${error header}
		// BODY   ${error rows}
		// BODY 
		// BODY </pre>
		// BODY </body>
		
		Macro mapper = new Macro()
			.expand( "start time", Server.get().getStartTime() )
			.expand( "up time", Server.get().getUpTime() )
			.expand( "service header", this.serviceStats.header() )
			.expand( "service rows", Registrar.get().getRoutes().map( this::getServiceStats ) )
			.expand( "remote header", this.remoteStats.header() )
			.expand( "remote rows", this.getRemoteEntries().map( this::getRemoteStats ) )
			.expand( "error header", this.errorStats.header() )
			.expand( "error rows", Registrar.get().getRoutes().map( this::getErrorStats ) );
		
		this.sendHtml( exchange, this.getCommentedLines( "BODY" ).flatMap( mapper ) );
				
		return Procedure.NOOP;
	}
	
	private String getServiceStats( Route r ) {
		return this.serviceStats.format( r.getExecutionCount(), r.getPath() );
	}
	
	private String getErrorStats( Route r ) {
		return this.errorStats.format( r.getErrorCount(), r.getPath() );
	}
	
	private String getRemoteStats( Map.Entry<String, Integer> entry ) {
		return this.remoteStats.format( entry.getValue(), entry.getKey() );
	}

	@Override
	public Category getCategory() {
		return Category.Administration;
	}

	@Override
	public int getSequence() {
		return 10;
	}

	@Override
	public String getPath() {
		return "/admin/status";
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
