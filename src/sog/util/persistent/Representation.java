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

package sog.util.persistent;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public interface Representation<T> {
	
	
	/**
	 * Construct the instance of type T represented by the given string.
	 * 
	 * All representations should honor the following constraints:
	 * 		The returned value must not be null.
	 * 		The representation must not be null.
	 * 		The representation must not contain tab characters (\t).
	 * 		The representation must not contain newline characters (\n).
	 * 
	 * @param rep	The non-null string representation.
	 * @return		The corresponding non-null instance of target type T.
	 */
	public T fromString( String rep );

	
	/**
	 * Produce the string representation of a given instance of target type T.
	 * 
	 * All representations should honor the following constraints:
	 * 		The returned value must not be null.
	 * 		The representation must not be null.
	 * 		The representation must not contain tab characters (\t).
	 * 		The representation must not contain newline characters (\n).
	 * 
	 * @param t		A non-null instance of the target type T.
	 * @return		The corresponding string representation.
	 */
	public String toString( T t );
	
	
	/**
	 * Convenience predicate used for assertions regarding concrete representations.
	 * 
	 * @param rep
	 * @return
	 */
	@Test.Decl( "False if rep is null" )
	@Test.Decl( "False if rep contains a tab character" )
	@Test.Decl( "False if rep contains a newline character" )
	default public boolean isValid( String rep ) {
		return rep != null && !rep.contains( "\t" ) && ! rep.contains( "\n" );
	}
	

	/**
	 * The following composition must be equivalent to the identity function on the set of
	 * non-null instances of target type T:
	 * 		For all t != null, toThenFrom( t ).equals( t )
	 * 
	 * @param t
	 * @return
	 */
	@Test.Skip( "Convenience method" )
	default public T toThenFrom( T t ) {
		return this.fromString( this.toString( t ) );
	}

	
	/**
	 * The following composition must be equivalent to the identity function on the image of
	 * toString, that is
	 * 		For all strings s that represent some instance of target type T, fromThenTo( s ).equals( s )
	 * 
	 * @param rep
	 * @return
	 */
	@Test.Skip( "Convenience method" )
	default public String fromThenTo( String rep ) {
		return this.toString( this.fromString( rep ) );
	}
	

	@Test.Decl( "Throws AssertionError if rep is not valid" )
	@Test.Decl( "Throws AssertionError if instance t is null" )
	@Test.Decl( "toThenFrom is identity" )
	@Test.Decl( "fromThenTo is identity" )
	public static Representation<String> STRING = new Representation<String>() {
		@Override public String fromString( String rep ) { 
			Assert.isTrue( this.isValid( rep ) );
			return rep; 
		}
		@Override public String toString( String t ) { 
			Assert.nonNull( t );
			return t; 
		}
	};

	
}
