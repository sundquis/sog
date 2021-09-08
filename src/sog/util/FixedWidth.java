/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class FixedWidth {
	
	
	@FunctionalInterface
	public interface Field {
		
		/** Return a string of the given width (length), based on a given string and padding character. */
		public String format( String s, int width, char pad );
		
		public static Field LEFT = Strings::leftJustify;
		
		public static Field RIGHT = Strings::rightJustify;
	}
	

	
	/* Field functions act on arguments from the array, separator functions return a separator. */
	private final List<Function<Object[], String>> formatters;
	
	/* The list of field names, used to build the header. */
	private final List<String> fieldNames;
	
	/* The list of underlines, used to build header2. */
	private final List<String> underlines;
	
	/* Formatted fields that depend on an array of objects, not separators. */
	private int fieldCount;
	
	/* Total width, in characters, of a formatted string. */
	private int width;
	
	/* The field names as a formatted string. */
	private String header;
	
	/* An optional second row, underlining the field names. */
	private String header2;

	
	/**
	 * Construct an empty {@code FixedWidth} formatter.
	 */
	@Test.Decl( "Empty formatter produces empty formatted string" )
	public FixedWidth() {
		this.formatters = new ArrayList<>();
		this.fieldNames = new ArrayList<>();
		this.underlines = new ArrayList<>();
		this.fieldCount = 0;
		this.width = 0;
		this.header = null;
		this.header2 = null;
	}
	
	
	public FixedWidth field( String name, int width, char pad, Field formatter ) {
		final int index = this.fieldCount++;
		this.formatters.add( args -> {
			return formatter.format( Strings.toString( args[index] ), width, pad );
		});
		
		this.fieldNames.add( formatter.format( name, width, ' ' ) );
		this.underlines.add( formatter.format( "", width, '=' ) );
		this.width += width;
		this.header = null;
		this.header2 = null;
		
		return this;
	}

	
	@Test.Decl( "Can not be empty" )
	@Test.Decl( "Throws assertion error for null separator" )
	@Test.Decl( "Overrides default" )
	@Test.Decl( "Does not affect subsequent separators" )
	@Test.Decl( "Multiple separators allowed" )
	@Test.Decl( "Appends if last" )
	public FixedWidth sep( String separator ) {
		this.formatters.add( args -> separator );

		this.width += separator.length();
		this.header = null;
		this.header2 = null;
		
		return this;
	}

	
	/**
	 * Append a left-justified field with given positive width and padding character.
	 * 
	 * @param width
	 * @return
	 */
	@Test.Decl( "Width must be positive" )
	@Test.Decl( "Appends left justified field" )
	@Test.Decl( "Padding character is used" )
	@Test.Decl( "Width is correct for short" )
	@Test.Decl( "Truncated for long" )
	public FixedWidth left( String name, int width, char pad ) {
		return this.field( name, width, pad, Field.LEFT );
	}
	
	/**
	 * Append a right-justified field with given positive width and padding character.
	 * 
	 * @param width
	 * @return
	 */
	@Test.Decl( "Width must be positive" )
	@Test.Decl( "Appends right justified field" )
	@Test.Decl( "Padding character is used" )
	@Test.Decl( "Width is correct for short" )
	@Test.Decl( "Truncated for long" )
	public FixedWidth right( String name, int width, char pad ) {
		return this.field( name, width, pad, Field.RIGHT );
	}

	@Test.Decl( "Includes fields" )
	@Test.Decl( "Includes separators" )
	@Test.Decl( "Increases when field appended" )
	@Test.Decl( "Increses when separator appended" )
	public int width() {
		return this.width;
	}
	
	/**
	 * Apply the specified field formats to the given fields and return the string representation.
	 * 
	 * @param args
	 * @return
	 */
	@Test.Decl( "Throws illegal argument exception for too few fields" )
	@Test.Decl( "Throws illegal argument exception for too many fields" )
	@Test.Decl( "Null fields use null representation" )
	@Test.Decl( "Non null fields represented appropriately" )
	@Test.Decl( "Fields formatted in the order specified" )
	@Test.Decl( "Fields are separated as specified" )
	@Test.Decl( "Fields can be added after formatting" )
	@Test.Decl( "Throws class cast exception for wrong argument type" )
	public String format( Object ... fields ) {
		if ( this.fieldCount != fields.length ) {
			throw new IllegalArgumentException( "Wrong number of fields. Expected " + this.fieldCount + ", got " + fields.length );
		}
		
		return this.formatters.stream().map( f -> f.apply( fields ) ).collect( Collectors.joining() );
	}
	
	
	public String header() {
		return null;
	}
	
	
	public String header2() {
		return null;
	}
	
	
	/**
	 * Return basic info about this formatter
	 */
	@Test.Decl( "Includes field count" )
	@Test.Decl( "Includes length" )
	@Override
	public String toString() {
		return "FixedWidth(" + this.fieldCount + " fields, width = " + this.width + ")";
	}


}
