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

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;

import mciv.server.route.Route;
import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class AdminRoute extends Route {
	
	protected AdminRoute() {}
	
	/* Admin routes use this to send their html response */
	protected void sendHtml( HttpExchange exchange, Stream<String> htmlBody ) throws IOException {
		exchange.getResponseHeaders().add( "Content-Type", "text/html" );
		exchange.sendResponseHeaders( 200, 0 );

		Macro mapper = new Macro().expand( "body", htmlBody );
		Iterator<String> lines = HTML.getHtml().flatMap( mapper ).map( s -> s + "\n" ).iterator();
		
		while ( lines.hasNext() ) {
			exchange.getResponseBody().write( lines.next().getBytes() );
		}
		
		exchange.getResponseBody().close();
	}
	
	protected void sendHtml( HttpExchange exchange, String msg ) throws IOException {
		this.sendHtml( exchange, Stream.of( "<h1>MCIV Server:</h1><pre>" + msg + "</pre>" ) );
	}
	
	private static class HTML {
		
		// HTML <!DOCTYPE html>
		// HTML <html>
		// HTML <head><meta charset="utf-8"></head>
		// HTML <body>
		// HTML ${body}
		// HTML </body>
		// HTML </html>
		
		private static Stream<String> getHtml() throws IOException {
			return new Commented( HTML.class ).getCommentedLines( "HTML" );
		}

	}


}
