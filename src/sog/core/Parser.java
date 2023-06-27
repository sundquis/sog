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

package sog.core;

import java.util.Arrays;
import java.util.List;

/**
 * 
 */
@Test.Subject( "test." )
@FunctionalInterface
public interface Parser<T> {

	/**
	 * Convert the canonical String representation to an instance of type T
	 * 
	 * @param s
	 * @return
	 */
	public T fromString( String s );
	

	
	/**
	 * A marker interface for identifying a parser as being a faithful string representation.
	 * 
	 * To be faithful, a Parser p must satisfy the following two properties:
	 * 		F1. For each non-null object t of type T, p.fromString( t.toString() ).equals( t )
	 * 		F2. For each String s in the domain of fromString, p.fromString( s ).toString().equals( s )
	 */
	@FunctionalInterface
	public static interface Faithful<T> extends Parser<T> {
		
		@Override
		public String toString();
		
		@Override
		public T fromString( String s );
		
	}
	
	

	@Test.Decl( "Throws NumberFormatException for mal-formed string" )
	@Test.Decl( "Correct for sample cases" )
	@Test.Decl( "Parser.INTEGER satisfies F1" )
	@Test.Decl( "Parser.INTEGER does not satisfy F2" )
	public static final Parser<Integer> INTEGER = Integer::parseInt;

	@Test.Decl( "Throws NumberFormatException for mal-formed string" )
	@Test.Decl( "Correct for sample cases" )
	@Test.Decl( "Parser.LONG satisfies F1" )
	@Test.Decl( "Parser.LONG does not satisfy F2" )
	public static final Parser<Long> LONG = Long::parseLong;
	
	@Test.Decl( "Returns false for mal-formed string" )
	@Test.Decl( "Correct for sample cases" )
	@Test.Decl( "Parser.BOOLEAN satisfies F1" )
	@Test.Decl( "Parser.BOOLEAN does not satisfy F2" )
	public static final Parser<Boolean> BOOLEAN = Boolean::parseBoolean;
	
	@Test.Decl( "Correct for sample cases" )
	@Test.Decl( "Parser.STRING satisfies F1" )
	@Test.Decl( "Parser.STRING satisfies F2" )
	public static final Parser<String> STRING = String::toString;

	@Test.Decl( "Collection of common cases" )
	@Test.Decl( "Array of length one allowed" )
	@Test.Decl( "Empty array not allowed" )
	@Test.Decl( "White space after comma ignored" )
	@Test.Decl( "Parser.CSV does not satisfy F1" )
	@Test.Decl( "Parser.CSV does not satisfy F2" )
	public static final Parser<String[]> CSV = (s) -> s.split( ",\\s*" );
	
	@Test.Decl( "Collection of common cases" )
	@Test.Decl( "List of length one allowed" )
	@Test.Decl( "Empty list not allowed" )
	@Test.Decl( "White space after comma ignored" )
	@Test.Decl( "Parser.LIST does not satisfy F1" )
	@Test.Decl( "Parser.LSIT does not satisfy F2" )
	public static final Parser<List<String>> LIST = (s) -> Arrays.asList( CSV.fromString(s) );
	

}
