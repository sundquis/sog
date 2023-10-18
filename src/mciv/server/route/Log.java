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

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;

import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;

/**
 * 
 */
@Test.Subject( "test." )
public class Log {
	
	private static final int MAX_COUNT = 100;
	
	private static final Log INSTANCE = new Log();
	
	public static Log get() {
		return Log.INSTANCE;
	}
	
	
	private final LinkedList<List<String>> exchangeEntries;
	
	private Log() {
		this.exchangeEntries = new LinkedList<>();
	}
	
	public synchronized Stream<String> getHeaders( int count ) {
		return this.exchangeEntries.stream().limit( count ).flatMap( List::stream );
	}

	// ENTRY <hr>
	// ENTRY ${Date}
	// ENTRY   URI: ${URI}
	// ENTRY   REMOTE: ${Remote}
	// ENTRY   METHOD: ${Method}
	// ENTRY   PARAMS: 
	// ENTRY     ${Params}
	// ENTRY   REQUEST HEADERS: 
	// ENTRY     ${Request Headers}
	// ENTRY   RESPONSE HEADERS:
	// ENTRY     ${Response Headers}
	// ENTRY   REQUEST BODY: ${Request Body}
	// ENTRY   RESPONSE LENGTH: ${Response Length} bytes.
	// ENTRY   
	public synchronized void accept( HttpExchange exchange, String requestBody, long responseLength, String params ) {
		Macro mapper = new Macro()
			.expand( "Date", new Date().toString() )
			.expand( "URI", exchange.getRequestURI().toString() )
			.expand( "Remote", exchange.getRemoteAddress().toString() )
			.expand( "Method", exchange.getRequestMethod() )
			.expand( "Params", params )
			.expand( "Request Headers", 
				exchange.getRequestHeaders().entrySet().stream().map( Map.Entry.class::cast )
					.map( this::toString ).collect( Collectors.toList() ) )
			.expand( "Response Headers", 
				exchange.getResponseHeaders().entrySet().stream().map( Map.Entry.class::cast )
					.map( this::toString ).collect( Collectors.toList() ) )
			.expand( "Request Body", 
				requestBody.length() > 200 ? (requestBody.length() + " charcaters.") : requestBody )
			.expand( "Response Length", "" + responseLength );

		try {
			this.exchangeEntries.addFirst(
				new Commented( Log.class ).getCommentedLines( "ENTRY" )
				.flatMap( mapper ).collect(  Collectors.toList() ) );
		} catch ( IOException ex ) {
			Error.get().accept( ex );
		}
		
		while ( this.exchangeEntries.size() > Log.MAX_COUNT ) {
			this.exchangeEntries.removeLast();
		}
	}
	
	public String toString( Map.Entry<String, List<String>> entry ) {
		return entry.getKey() + ": " + entry.getValue().stream().collect( Collectors.joining( ", ") );
	}


}
