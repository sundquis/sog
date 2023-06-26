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

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Static helper methods for testing normal conditions.
 * 
 * All methods return their argument when the test passes to allow in-line testing.
 * Tests are implemented in pairs, one allowing a custom error message the other with a default error message.
 * All methods use the private fail( msg ) method to throw an AsserionError. 
 * 
 * Depends on:
 * 		NONE
 *
 */
@Test.Subject( "test." )
public class Assert {
	
	/**
	 * Not intended to be instantiated.
	 */
	private Assert() {}

	
	@Test.Decl( "Throws AssertionError if object is null" )
	@Test.Decl( "Returns object if not null" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T nonNull( T obj, String errMsg ) {
		if ( obj == null ) {
			Assert.fail( errMsg );
		}
		return obj;
	}

	@Test.Decl( "Throws AssertionError if object is null" )
	@Test.Decl( "Returns object if not null" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T nonNull( T obj ) {
		if ( obj == null ) {
			Assert.fail( "Argument is null" );
		}
		return obj;
	}
	
	
	@Test.Decl( "Throws AssertionError if object is not null" )
	@Test.Decl( "Returns object if null" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T isNull( T obj, String errMsg ) {
		if ( obj != null ) {
			Assert.fail( errMsg );
		}
		return obj;
	}
	
	@Test.Decl( "Throws AssertionError if object is not null" )
	@Test.Decl( "Returns object if null" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T isNull( T obj ) {
		if ( obj != null ) {
			Assert.fail( "Should be null: " + Strings.toString( obj ) );
		}
		return obj;
	}
	

	@Test.Decl( "Throws AssertionError if array is empty" )
	@Test.Decl( "Returns array if not empty" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T[] nonEmpty( T[] array, String errMsg ) {
		if ( array == null || array.length == 0 ) {
			Assert.fail( errMsg );
		}
		return array;
	}
	
	@Test.Decl( "Throws AssertionError if array is empty" )
	@Test.Decl( "Returns array if not empty" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T[] nonEmpty( T[] array ) {
		if ( array == null || array.length == 0 ) {
			Assert.fail( "Array is " + (array == null ? "null" : "empty" ) );
		}
		return array;
	}


	@Test.Decl( "Throws AssertionError if string is empty" )
	@Test.Decl( "Returns string if not empty" )
	@Test.Decl( "Includes diagnostic message" )
	public static String nonEmpty( String s, String errMsg ) {
		if ( s == null || s.isEmpty() ) {
			Assert.fail( errMsg );
		}
		return s;
	}
	
	@Test.Decl( "Throws AssertionError if string is empty" )
	@Test.Decl( "Returns string if not empty" )
	@Test.Decl( "Includes diagnostic message" )
	public static String nonEmpty( String s ) {
		if ( s == null || s.isEmpty() ) {
			Assert.fail( "String is " + (s == null ? "null" : "empty") );
		}
		return s;
	}
	

	@Test.Decl( "Throws AssertionError if string length is out of bounds" )
	@Test.Decl( "Returns string if length is in bounds" )
	@Test.Decl( "Includes diagnostic message" )
	public static String boundedString( String s, int minLength, int maxLength, String errMsg ) {
		if ( s == null || s.length() < minLength || s.length() > maxLength ) {
			Assert.fail( errMsg );
		}
		return s;
	}
	
	@Test.Decl( "Throws AssertionError if string length is out of bounds" )
	@Test.Decl( "Returns string if length is in bounds" )
	@Test.Decl( "Includes diagnostic message" )
	public static String boundedString( String s, int minLength, int maxLength ) {
		if ( s == null || s.length() < minLength || s.length() > maxLength ) {
			String errMsg = s == null ? "null" :
				s.length() < minLength ? "too short" : "too long";
			Assert.fail( "String '" + s + "' is " + errMsg );
		}
		return s;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a readable directory" )
	@Test.Decl( "Returns path if it represents a readable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path readableDirectory( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a readable directory" )
	@Test.Decl( "Returns path if it represents a readable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path readableDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					!Files.isDirectory( p ) ? ": is not a directory" : ": is not readable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a writeable directory" )
	@Test.Decl( "Returns path if it represents a writeable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path writeableDirectory( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isWritable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a writeable directory" )
	@Test.Decl( "Returns path if it represents a writeable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path writeableDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isWritable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					!Files.isDirectory( p ) ? ": is not a directory" : ": is not writeable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a readable and writeable directory" )
	@Test.Decl( "Returns path if it represents a readable and writeable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path rwDirectory( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a readable and writeable directory" )
	@Test.Decl( "Returns path if it represents a readable and writeable directory" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path rwDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					!Files.isDirectory( p ) ? ": is not a directory" : 
						!Files.isReadable( p ) ? ": is not readable" : ": is not writeable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a readable file" )
	@Test.Decl( "Returns path if it represents a readable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path readableFile( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a readable file" )
	@Test.Decl( "Returns path if it represents a readable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path readableFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					Files.isDirectory( p ) ? ": is a directory" : ": is not readable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a writeable file" )
	@Test.Decl( "Returns path if it represents a writeable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path writeableFile( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isWritable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a writeable file" )
	@Test.Decl( "Returns path if it represents a writeable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path writeableFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isWritable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					Files.isDirectory( p ) ? ": is a directory" : ": is not writeable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if path does not represent a readable and writeable file" )
	@Test.Decl( "Returns path if it represents a readable and writeable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path rwFile( Path p, String errMsg ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			Assert.fail( errMsg );
		}
		return p;
	}
	
	@Test.Decl( "Throws AssertionError if path does not represent a readable and writeable file" )
	@Test.Decl( "Returns path if it represents a readable and writeable file" )
	@Test.Decl( "Includes diagnostic message" )
	public static Path rwFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			String errMsg = p == null ? ": is null" :
				!Files.exists( p ) ? ": does not exist" :
					Files.isDirectory( p ) ? ": is a directory" : 
						!Files.isReadable( p ) ? ": is not readable" : ": is not writeable";
			Assert.fail( p + errMsg );
		}
		return p;
	}
	

	@Test.Decl( "Throws AssertionError if integer is zero" )
	@Test.Decl( "Returns integer if it is not zero" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer nonZero( Integer n, String errMsg ) {
		if ( n == 0 ) {
			Assert.fail( errMsg );
		}
		return n;
	}
	
	@Test.Decl( "Throws AssertionError if integer is zero" )
	@Test.Decl( "Returns integer if it is not zero" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer nonZero( Integer n ) {
		if ( n == 0 ) {
			Assert.fail( "Argument is zero" );
		}
		return n;
	}
	
	@Test.Decl( "Throws AssertionError if integer is not positive" )
	@Test.Decl( "Returns integer if it is positive" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer positive( Integer n, String errMsg ) {
		if ( n <= 0 ) {
			Assert.fail( errMsg );
		}
		return n;		
	}
	
	@Test.Decl( "Throws AssertionError if integer is not positive" )
	@Test.Decl( "Returns integer if it is positive" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer positive( Integer n ) {
		if ( n <= 0 ) {
			Assert.fail( "Should be positive: " + n );
		}
		return n;		
	}
	

	@Test.Decl( "Throws AssertionError if integer is negative" )
	@Test.Decl( "Returns integer if it is nonnegative" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer nonNeg( Integer n, String errMsg ) {
		if ( n < 0 ) {
			Assert.fail( errMsg );
		}
		return n;		
	}
	
	@Test.Decl( "Throws AssertionError if integer is negative" )
	@Test.Decl( "Returns integer if it is nonnegative" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer nonNeg( Integer n ) {
		if ( n < 0 ) {
			Assert.fail( "Should be nonnegative: " + n );
		}
		return n;		
	}
	

	@Test.Decl( "Throws AssertionError if integer is less than or equal to the minimum" )
	@Test.Decl( "Returns integer if it is greater than the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer greaterThan( Integer n, Integer min, String errMsg ) {
		if ( n <= min ) {
			Assert.fail( errMsg );
		}
		return n;
	}
	
	@Test.Decl( "Throws AssertionError if integer is less than or equal to the minimum" )
	@Test.Decl( "Returns integer if it is greater than the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer greaterThan( Integer n, Integer min ) {
		if ( n <= min ) {
			Assert.fail( "Should be greater than " + min + ": " + n );
		}
		return n;
	}
	

	@Test.Decl( "Throws AssertionError if integer is greater than or equal to the minimum" )
	@Test.Decl( "Returns integer if it is less than the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer lessThan( Integer n, Integer max, String errMsg ) {
		if ( n >= max ) {
			Assert.fail( errMsg );
		}
		return n;
	}
	
	@Test.Decl( "Throws AssertionError if integer is greater than or equal to the minimum" )
	@Test.Decl( "Returns integer if it is less than the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer lessThan( Integer n, Integer max ) {
		if ( n >= max ) {
			Assert.fail( "Should be less than " + max + ": " + n );
		}
		return n;
	}
	

	@Test.Decl( "Throws AssertionError if integer is greater than the minimum" )
	@Test.Decl( "Returns integer if it is less than or equal to the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer lessThanOrEqual( Integer n, Integer max, String errMsg ) {
		if ( n > max ) {
			Assert.fail( errMsg );
		}
		return n;
	}

	@Test.Decl( "Throws AssertionError if integer is greater than the minimum" )
	@Test.Decl( "Returns integer if it is less than or equal to the minimum" )
	@Test.Decl( "Includes diagnostic message" )
	public static Integer lessThanOrEqual( Integer n, Integer max ) {
		if ( n > max ) {
			Assert.fail( "Should be less or equal to " + max + ": " + n );
		}
		return n;
	}

	
	@Test.Decl( "Throws AssertionError if predicate is false" )
	@Test.Decl( "Returns predicate if it is true" )
	@Test.Decl( "Includes diagnostic message" )
	public static boolean isTrue( boolean predicate, String errMsg ) {
		if ( ! predicate ) {
			Assert.fail( errMsg );
		}
		return predicate;
	}
	
	@Test.Decl( "Throws AssertionError if predicate is false" )
	@Test.Decl( "Returns predicate if it is true" )
	@Test.Decl( "Includes diagnostic message" )
	public static boolean isTrue( boolean predicate ) {
		if ( ! predicate ) {
			Assert.fail( "Should be true" );
		}
		return predicate;
	}
	

	@Test.Decl( "Throws AssertionError if objects are not equal" )
	@Test.Decl( "Returns first argument if objects are equal" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T equal( T arg1, Object arg2, String errMsg ) {
		if ( ! Objects.equals( arg1,  arg2 ) ) {
			Assert.fail( errMsg );
		}
		return arg1;
	}
	
	@Test.Decl( "Throws AssertionError if objects are not equal" )
	@Test.Decl( "Returns first argument if objects are equal" )
	@Test.Decl( "Includes diagnostic message" )
	public static <T> T equal( T arg1, Object arg2 ) {
		if ( ! Objects.equals( arg1,  arg2 ) ) {
			Assert.fail( "Arg1: " + Strings.toString( arg1 ) + ", Arg2: " + Strings.toString( arg2 ) );
		}
		return arg1;
	}


	private static void fail( String errMsg ) {
		throw new AssertionError( errMsg );
	}
	


}
