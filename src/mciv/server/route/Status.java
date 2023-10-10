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

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Status {
	
	public static enum Code {
		
		UNIMPLEMENTED( -1, "The route has not yet been implemented. No state has changed." ),
		OK( 0, "Processing completed normally." ),
		PARSE( 1, "Encountered a parse error when processing string data." ),
		INVALID( 2, "Supplied data violates published constraints." ),
		DUPLICATE( 3, "Attempt to create a resource that already exists." ),
		AUTH( 4, "Supplied credentials do not match stored values" ),
		MAL_FORMED( 100, "Improper JSON request body." ),
		ERROR( 400, "Programmatic exception" ),
		;
		
		int value;
		
		String description;
		
		Code( int value, String description ) {
			this.value = value;
			this.description = description;
		}
		
		public int getValue() { 
			return this.value; 
		}
		
		public String getDescription() { 
			return this.description; 
		}
		
	}
	
	private Status() {}


}
