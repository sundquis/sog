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

package mciv.server.route;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;

import sog.core.App;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Log {
	
	private static final Log INSTANCE = new Log();
	
	public static Log get() {
		return Log.INSTANCE;
	}
	
	public static class Entry {
		
	}
	
	private Log() {
	}
	
	public void accept( HttpExchange exchange, String requestBody, String responseBody ) {
		// FIXME
		App.get()
			.msg()
			.msg()
			.msg( new Date() )
			.msg( "URI: " + exchange.getRequestURI() )
			.msg( "REMOTE: " + exchange.getRemoteAddress() )
			.msg( "METHOD: " + exchange.getRequestMethod() )
			.msg( "HEADERS:" );
		
		Map<String, List<String>> hdrs = exchange.getRequestHeaders();
		for ( Map.Entry<String, List<String>> hdr : hdrs.entrySet() ) {
			App.get().msg( "\t" + hdr.getKey() + ": " + hdr.getValue().stream().collect( Collectors.joining( ", " ) ) );
		}
		
		App.get()
			.msg( "REQUEST BODY LENGTH: " + requestBody.length() )
			.msg( "RESPONSE BODY LENGTH: " + responseBody.length() );
	
	}


}
