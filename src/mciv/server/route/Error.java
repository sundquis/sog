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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;

/**
 * 
 */
@Test.Subject( "test." )
public class Error {
	
	private static final int MAX_COUNT = 100;
	
	private static final Error INSTANCE = new Error();
	
	public static Error get() {
		return Error.INSTANCE;
	}
	
	
	private final LinkedList<List<String>> errors;
	
	private Error() {
		this.errors = new LinkedList<>();
	}
	
	
	public synchronized void truncate( int length ) {
		while ( this.errors.size() > length ) {
			this.errors.removeLast();
		}
	}
	
	public synchronized Stream<String> getErrors( int count ) {
		return this.errors.stream().limit( count ).flatMap( List::stream );
	}

	// ERR <pre>
	// ERR <hr>
	// ERR   EXCEPTION: ${Exception}
	// ERR   MESSAGE: ${Message}
	// ERR   CONTEXT: ${Context}
	// ERR   TRACE:
	// ERR     ${Trace}
	// ERR 
	// ERR </pre>
	
	public void accept( Exception ex, String context ) {
		Macro mapper = new Macro()
			.expand( "Context", context )
			.expand( "Message", ex.getMessage() )
			.expand( "Exception", ex.getClass().getName() )
			.expand( "Trace", App.get().getLocationMatching( ex, "^sog.*|^mciv.*"  ).collect( Collectors.toList() ) );
		
		try {
			this.errors.addFirst( new Commented( Error.class )
				.getCommentedLines( "ERR" ).flatMap( mapper ).collect(  Collectors.toList() ) );
		} catch ( IOException e ) {
			App.get().msg( "In Error.accept()" );
			e.printStackTrace();
		}
		
		while ( this.errors.size() > Error.MAX_COUNT ) {
			this.errors.removeLast();
		}
	}


}
