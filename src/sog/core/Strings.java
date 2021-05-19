/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 */

package sog.core;



import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public final class Strings {


	/** Not intended to be instantiated. */
	private Strings() {}

	/**
	 * If the width is non-zero, the argument is truncated
	 * Truncate or pad to the non-zero length as necessary.
	 * If the width is negative, the string is left-justified.
	 * If the width is positive, the string is right-justified.
	 */
	@Test.Decl( "Throws assertion error for zero width" )
	@Test.Decl( "Throws assertion error for null string" )
	@Test.Decl( "Justify empty with neg width is not empty" )
	@Test.Decl( "Justify empty with pos width is not empty" )
	public static String justify( String s, int w, char pad ) {
		Assert.nonNull( s );
		Assert.nonZero( w );
		
		return Assert.nonEmpty( (w < 0) ? leftJustify( s, -1*w, pad ) : rightJustify( s, w, pad ) );
	}

	/** Truncate to given width. If shorter left justify by padding on the right. */
	@Test.Decl( "Throws assertion error for non positive width" )
	@Test.Decl( "Throws assertion error for null string" )
	@Test.Decl( "Result has specified length" )
	@Test.Decl( "Long string truncated" )
	@Test.Decl( "Short string padded with given character" )
	@Test.Decl( "Sample cases" )
	public static String leftJustify( String s, int w, char pad ) {
		Assert.nonNull( s );
		Assert.positive( w );
		
		StringBuffer padded = new StringBuffer( s );
		while ( padded.length() < w ) {
			padded.append( pad );
		}
		padded.setLength( w );
		return Assert.nonEmpty( padded.toString() );
	}

	/** Truncate to given width. If shorter right justify by padding on the left. */
	@Test.Decl( "Throws assertion error for non positive width" )
	@Test.Decl( "Throws assertion error for null string" )
	@Test.Decl( "Result has specified length" )
	@Test.Decl( "Long string truncated" )
	@Test.Decl( "Short string padded with given character" )
	@Test.Decl( "Sample cases" )
	public static String rightJustify( String s, int w, char pad ) {
		Assert.nonNull( s );
		Assert.positive( w );
		
		StringBuffer padded = new StringBuffer();
		while ( padded.length() < w - s.length() ) {
			padded.append( pad );
		}
		padded.append( s );
		padded.setLength( w );
		return Assert.nonEmpty( padded.toString() );
	}

	/**
	 * Remove any enclosing quotes and/or whitespace.
	 */
	@Test.Decl( "Throws assertion error for null string" )
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
	
	@Test.Decl( "Throws assertion error for null string" )
	@Test.Decl( "Identity on empty" )
	@Test.Decl( "Result contains no white space" )
	@Test.Decl( "Does not start with a digit" )
	@Test.Decl( "Starts with uppercase" )
	@Test.Decl( "Result contains only letters" )
	@Test.Decl( "Sample cases" )
	@Test.Decl( "Underscore removed" )
	public static String toCamelCase( String s ) {
		Assert.nonNull( s );
		
		// Non-alpha-numeric characters to whitespace
		String result = s.replaceAll( "[^a-zA-Z0-9]", " " );
		
		// White space sequences to single space
		result = result.replaceAll( "\\s+", " " );
		
		String[] words = result.split( " " );
		result = "";
		for ( String word : words ) {
			if ( word.length() > 0 ) {
				result += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
			}
		}

		// Remove leading digits
		result = result.replaceAll( "^\\d*",  "" );
		
		return result;
	}
	
	/**
	 * Simple string representations for various types
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Test.Decl( "String representation of null is null" )
	@Test.Decl( "Identity on empty" )
	@Test.Decl( "Agrees with object to string for non array non collection" )
	@Test.Decl( "Provides alternate string representation for arrays" )
	@Test.Decl( "Provides alternate string representation for collections" )
	public static String toString( Object obj ) {
		if ( obj == null ) {
			return "null";
		}
		
		if ( obj.getClass().isArray() ) {
			return Strings.arrayToString( obj );
		}
		
		if ( Collection.class.isAssignableFrom( obj.getClass() ) ) {
			return Strings.collectionToString( (Collection<Object>) obj );
		}
		
		return obj.toString();
	}
	
	@Test.Decl( "Throws assertion error on null collections" )
	@Test.Decl( "Enclosed in set braces" )
	@Test.Decl( "Empty collection allowed" )
	@Test.Decl( "Sample cases for collections of primitive" )
	@Test.Decl( "Sample cases for collections of collections" )
	@Test.Decl( "Sample cases for collections of arrays" )
	@Test.Decl( "Omitted elements are indicated" )
	public static String collectionToString( Collection<Object> objects ) {
		Assert.nonNull( objects );
		int length = objects.size();
		
		StringBuffer buf = new StringBuffer();
		buf.append( "{" );
		if ( length <= 5 ) {
			boolean notFirst  = false;
			for ( Object obj : objects ) {
				if ( notFirst ) buf.append( ", " );
				notFirst = true;
				buf.append( Strings.toString( obj ) );
			}
		} else {
			Iterator<Object> iter = objects.iterator();
			buf.append( Strings.toString( iter.next() ) ).append( ", " );
			buf.append( Strings.toString( iter.next() ) );
			buf.append( ", ...<" ).append( length - 3 ).append( " more>... " );
			for ( int i = 3; i < length; i++ ) iter.next();
			buf.append( Strings.toString( iter.next() ) );
		}
		buf.append( "}" );
		
		return buf.toString();
	}
	
	@Test.Decl( "Throws assertion error on null arrays" )
	@Test.Decl( "Enclosed in set brackets" )
	@Test.Decl( "Empty array allowed" )
	@Test.Decl( "Sample cases for arrays of primitive" )
	@Test.Decl( "Sample cases for arrays of collections" )
	@Test.Decl( "Sample cases for arrays of arrays" )
	@Test.Decl( "Omitted elements are indicated" )
	public static String arrayToString( Object obj ) {
		Assert.nonNull( obj );
		Assert.isTrue( obj.getClass().isArray() );
		int length = Array.getLength( obj );
		
		StringBuffer buf = new StringBuffer();
		buf.append( "[" );
		if ( length <= 6 ) {
			for ( int i = 0; i < length; i++ ) {
				if ( i > 0 ) buf.append( ", " );
				buf.append( Strings.toString( Array.get( obj,  i ) ) );
			}
		} else {
			buf.append( Strings.toString( Array.get( obj,  0 ) ) ).append( ", " );
			buf.append( Strings.toString( Array.get( obj,  1 ) ) );
			buf.append( ", ...<" ).append( length - 3 ).append( " more>... " );
			buf.append( Strings.toString( Array.get( obj,  length -1 ) ) );
		}
		buf.append( "]" );
		
		return buf.toString();
	}
	
	@Test.Decl( "Class to relative path to classname correct" )
	public static String relativePathToClassname( Path relativePath ) {
		String result = StreamSupport.stream( relativePath.spliterator(), false )
			.map( p -> p.toString() )
			.collect( Collectors.joining( "." ) );
		Assert.isTrue( result.endsWith( ".java" ) );
		return result.replace( ".java",  "" );
	}
	
	
	public static String toHex( int n ) {
		return "0x" + Strings.rightJustify( Integer.toHexString(n).toUpperCase(),  4,  '0' );
	}
		
	
	
}