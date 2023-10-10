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
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpHandler;

import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;


/**
 * Base class for all application routes.
 * 
 * Concrete subclasses are required to support a no-arg constructor so that the Registrar can discover
 * and construct instances.
 * 
 * Concrete routes are located in sub-packages under mciv.server.route so that the Registrar can find them.
 */
@Test.Subject( "test." )
public abstract class Route implements HttpHandler, Comparable<Route> {
	

	/*
	 * Each Route is assigned to a category.
	 * Categories correspond roughly to turn phases and are implemented by Routes in a corresponding
	 * sub-package of mciv.server.route.
	 */
	public static enum Category {
		Administration,
		Authorization
	}
	
	
	// This template goes in each concrete route.
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Insert description of potential state transitions.
	 * 	
	 * REQUEST BODY:
	 *   Request: {
	 *   }
	 * 	
	 * RESPONSE BODY:
	 *   Response: {
	 *     "status": int( <values enumerated in mciv.server.route.Codes> ),
	 *     "data": Data,
	 *     "error": Error
	 *   }
	 * 
	 *   Where Data: {
	 *   }
	 * 
	 *   Where Error: [ str ]
	 * 
	 * EXCEPTIONS:
	 *   Description of non-programmatic exceptions.
	 *   Exceptions have status codes enumerated in mciv.server.route.Status
	 *   The application must recover from these.
	 * 
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */

	
	private boolean logging;
	
	protected Route() {
		this.logging = true;
	}
	
	/* Corresponds to turn phase and package. */
	public abstract Category getCategory();
	
	/* Approximate sequence in turn phase. */
	public abstract int getSequence();
	
	/* End point for the route. */
	public abstract String getPath();
		
			
	public Stream<String> getDocunmentation() {
		return this.getTaggedLines( "API" ).flatMap(  new Macro().expand( "path", this.getPath() ) );
	}
	
	protected Stream<String> getTaggedLines( String tag ) {
		try {
			return new Commented( this.getClass() ).getTaggedBlock( tag );
		} catch ( IOException e ) {
			e.printStackTrace();
			return Stream.of();
		}
	}
	
	protected Stream<String> getCommentedLines( String label ) {
		try {
			return new Commented( this.getClass() ).getCommentedLines( label );
		} catch ( IOException e ) {
			e.printStackTrace();
			return Stream.of();
		}
	}
		
	public boolean isLogging() {
		return this.logging;
	}
	
	public void setLogging( boolean logging ) {
		this.logging = logging;
	}
	
	@Override
	public int compareTo( Route other ) {
		int cat = this.getCategory().compareTo( other.getCategory() );
		return cat == 0 ? this.getSequence() - other.getSequence() : cat;
	}
	
	@Override
	public String toString() {
		return this.getPath();
	}
	
}
