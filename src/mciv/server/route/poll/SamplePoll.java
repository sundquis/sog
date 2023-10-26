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

package mciv.server.route.poll;

import mciv.server.route.Route;
import sog.core.Test;
import sog.util.json.JSON;
import sog.util.json.JSON.JsonObject;

/**
 * 
 */
@Test.Subject( "test." )
public class SamplePoll extends PollRoute {
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Sample poll route.
	 * 	
	 * REQUEST BODY:
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
	 * 	
	 * EXCEPTIONS:
	 *   Status
	 * 	
	 * 	</pre>
	 * <a href="#">Top</a>
	 * 	
	 */
	public SamplePoll() {}

	@Override
	public JsonObject respond( JsonObject requestBody ) throws Exception {
		return JSON.obj()
			.add( "message", JSON.str( "Unimplemented" ) )
			.add( "status", JSON.num( -1 ) );
	}

	@Override
	public Category getCategory() {
		return Route.Category.Poll;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public String getPath() {
		return "/poll/sample";
	}


}