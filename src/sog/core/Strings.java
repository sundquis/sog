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
import java.util.Collection;
import java.util.stream.Collectors;

@Test.Subject(  "test." )
public final class Strings {
	

	/* If a collection has more than this many elements then toString() omits some elements. */
	private static final int COLLECTION_MAX_LENGTH = Property.get( "collection.max.length", 7, Parser.INTEGER );
	
	/* If an array has more than this many elements then toString() omits some elements. */
	private static final int ARRAY_MAX_LENGTH = Property.get( "array.max.length", 7, Parser.INTEGER );


	/** Not intended to be instantiated. */
	private Strings() {}

	/**
	 * If the width is non-zero, the argument is truncated
	 * Truncate or pad to the non-zero length as necessary.
	 * If the width is negative, the string is left-justified.
	 * If the width is positive, the string is right-justified.
	 */
	@Test.Decl( "Throws AssertionError for null string" )
	@Test.Decl( "Return is empty for zero width" )
	@Test.Decl( "Length of return is specified width" )
	@Test.Decl( "For negative width equal to opposite of length return is the given string" )
	@Test.Decl( "For large negative width return value starts with given string" )
	@Test.Decl( "For large negative width return value ends with the pad character" )
	@Test.Decl( "For small negative width given string starts with return value" )
	@Test.Decl( "For positive width equal to length return is the given string" )
	@Test.Decl( "For large positive width return value ends with given string" )
	@Test.Decl( "For large positive width return value starts with the pad character" )
	@Test.Decl( "For small positive width given string ends with return value" )
	public static String justify( String s, int w, char pad ) {
		Assert.nonNull( s );
		
		return w == 0 ? "" :
			w > 0 ? Strings.rightJustify( s, w, pad ) : Strings.leftJustify( s, -1 * w, pad );
	}

	/** Right-truncate to given width. If shorter left justify by padding on the right. */
	@Test.Decl( "Throws AssertionError for null string" )
	@Test.Decl( "Return is empty for zero width" )
	@Test.Decl( "Length of return is specified width" )
	@Test.Decl( "For width equal to length return is the given string" )
	@Test.Decl( "For large width return value starts with given string" )
	@Test.Decl( "For large width return value ends with the pad character" )
	@Test.Decl( "For small width given string starts with return value" )
	public static String leftJustify( String s, int w, char pad ) {
		Assert.nonNull( s );
		Assert.nonNeg( w );

		StringBuilder sb = new StringBuilder( s );
		while ( sb.length() < w ) {
			sb.append( pad );
		}
		sb.setLength( w );
		
		return sb.toString();
	}

	/** Truncate to given width. If shorter right justify by padding on the left. */
	@Test.Decl( "Throws AssertionError for null string" )
	@Test.Decl( "Return is empty for zero width" )
	@Test.Decl( "Length of return is specified width" )
	@Test.Decl( "For width equal to length return is the given string" )
	@Test.Decl( "For large width return value ends with given string" )
	@Test.Decl( "For large width return value starts with the pad character" )
	@Test.Decl( "For small width given string ends with return value" )
	public static String rightJustify( String s, int w, char pad ) {
		Assert.nonNull( s );
		Assert.nonNeg( w );

		StringBuilder sb = new StringBuilder( s );
		sb.reverse();
		while ( sb.length() < w ) {
			sb.append( pad );
		}
		sb.setLength( w );
		sb.reverse();

		return sb.toString();
	}

	/**
	 * Remove any enclosing quotes and/or whitespace.
	 */
	@Test.Decl( "Throws AssertionError for null string" )
	@Test.Decl( "Identity on empty" )
	@Test.Decl( "Identity for non quoted trimmed strings" )
	@Test.Decl( "Ignores unmatched quotes" )
	@Test.Decl( "Removes single quotes" )
	@Test.Decl( "Removes double quotes" )
	@Test.Decl( "Removes nested quotes" )
	@Test.Decl( "Result is trimmed" )
	@Test.Decl( "Is idempotent" )
	@Test.Decl( "Sample cases" )
	public static String strip( String s ) {
		Assert.nonNull( s );
		
		String result = s.trim();
		while ( quoted( result ) ) {
			result = result.substring( 1,  result.length() - 1).trim();
		}

		return Assert.nonNull( result );
	}
	
	private static boolean quoted( String s ) {
		return (s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("\'") && s.endsWith("\'"));
	}
	
	@Test.Decl( "Throws AssertionError for null string" )
	@Test.Decl( "Identity on empty" )
	@Test.Decl( "Result contains no white space" )
	@Test.Decl( "Does not start with a digit" )
	@Test.Decl( "Starts with uppercase" )
	@Test.Decl( "Result contains only letters" )
	@Test.Decl( "Sample cases" )
	@Test.Decl( "Underscore removed" )
	@Test.Decl( "If no alphabetic characters in input then return is empty" )
	@Test.Decl( "Result is trimmed" )
	public static String toCamelCase( String s ) {
		Assert.nonNull( s );
		
		// Non-alpha-numeric characters to whitespace
		String result = s.replaceAll( "[^a-zA-Z0-9]", " " );
		
		// White space sequences to single space
		result = result.replaceAll( "\\s+", " " );

		// Initial capitalization and concatenation
		result = Arrays.stream( result.split( " " ) ).map( Strings::initCap ).collect( Collectors.joining( "" ) );

		// Remove leading digits
		result = result.replaceAll( "^\\d*",  "" );
		
		return result;
	}
	
	private static String initCap( String s ) {
		return s.length() == 0 ? "" : s.substring( 0, 1 ).toUpperCase() + s.substring( 1 ).toLowerCase();
	}
	
	/**
	 * Simple string representations for various types
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Test.Decl( "String representation of null is null" )
	@Test.Decl( "Identity on strings" )
	@Test.Decl( "Agrees with object to string for non array non collection" )
	@Test.Decl( "Provides alternate string representation for arrays" )
	@Test.Decl( "Provides alternate string representation for collections" )
	public static String toString( Object obj ) {
		if ( obj == null ) {
			return "null";
		}
		
		if ( obj.getClass().isArray() ) {
			return Strings.arrayToString( (Object[])obj );
		}
		
		if ( Collection.class.isAssignableFrom( obj.getClass() ) ) {
			return Strings.collectionToString( (Collection<Object>) obj );
		}
		
		return obj.toString();
	}
	
	@Test.Decl( "Throws AssertionError on null collections" )
	@Test.Decl( "Enclosed in set braces" )
	@Test.Decl( "Empty collection allowed" )
	@Test.Decl( "Sample cases for collections of primitive" )
	@Test.Decl( "Sample cases for collections of collections" )
	@Test.Decl( "Sample cases for collections of arrays" )
	@Test.Decl( "Omitted elements are indicated with 'more'" )
	@Test.Decl( "For long collections first element is shown" )
	@Test.Decl( "For long collections last element is shown" )
	public static String collectionToString( Collection<?> objects ) {
		Assert.nonNull( objects );
		int length = objects.size();

		StringBuilder sb = new StringBuilder();
		sb.append( "{" );
		if ( length <= Strings.COLLECTION_MAX_LENGTH ) {
			sb.append( objects.stream().map( Strings::toString ).collect( Collectors.joining( ", " ) ) );
		} else {
			sb.append( objects.stream().limit( 4L ).map( Strings::toString ).collect( Collectors.joining( ", " ) ) );
			sb.append( ", ...<" ).append( length - 5 ).append( " more>... " );
			sb.append( objects.stream().skip( length - 1 ).findFirst().get() );
		}
		sb.append( "}" );
		
		return sb.toString();
	}
	
	@Test.Decl( "Throws AssertionError on null arrays" )
	@Test.Decl( "Enclosed in square brackets" )
	@Test.Decl( "Empty array allowed" )
	@Test.Decl( "Sample cases for arrays of primitive" )
	@Test.Decl( "Sample cases for arrays of collections" )
	@Test.Decl( "Sample cases for arrays of arrays" )
	@Test.Decl( "Omitted elements are indicated with 'more'" )
	@Test.Decl( "For long arrays first element is shown" )
	@Test.Decl( "For long arrays last element is shown" )
	public static String arrayToString( Object[] objects ) {
		Assert.nonNull( objects );
		int length = objects.length;
		
		StringBuilder sb = new StringBuilder();
		sb.append( "[" );
		if ( length <= Strings.ARRAY_MAX_LENGTH ) {
			sb.append( Arrays.stream( objects ).map( Strings::toString ).collect( Collectors.joining( ", ") ) );
		} else {
			sb.append( Arrays.stream( objects ).limit( 4L ).map( Strings::toString ).collect( Collectors.joining( ", " ) ) );
			sb.append( ", ...<" ).append( length - 5 ).append( " more>... " );
			sb.append( Arrays.stream( objects ).skip( length - 1 ).findFirst().get() );
		}
		sb.append( "]" );
		
		return sb.toString();
	}
	
	
	@Test.Decl( "Result is non-empty" )
	@Test.Decl( "Hex digits have natural representation" )
	@Test.Decl( "Positive integers up to 2^16 - 1 have representation" )
	@Test.Decl( "Integers greater than 2^16 - 1 wrap around" )
	public static String toHex( int n ) {
		return "0x" + Strings.rightJustify( Integer.toHexString(n).toUpperCase(), 4, '0' );
	}

	
}