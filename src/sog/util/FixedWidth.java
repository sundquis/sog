/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;


import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class FixedWidth {
	
	// Field functions pull args out of array, separator functions return string
	private List<Function<Object[], String>> functions;
	
	// Left/Right justified fields, not separators
	private int fieldCount;
	
	// Current total length
	private int length;
	
	// Default separator currently in effect
	private String defaultSeparator;
	
	// Null representation currently in effect
	private String nullRepresentation;
	
	private boolean previousIsField;

	/**
	 * Construct an empty {@code FixedWidth} formatter.
	 */
	@Test.Decl( "Empty formatter produces empty formatted string" )
	public FixedWidth() {
		super();
		
		this.functions = new LinkedList<>();
		this.fieldCount = 0;
		this.length = 0;
		this.defaultSeparator = "";
		this.nullRepresentation = "";
		this.previousIsField = false;
	}
	
	/**
	 * Set the default field separator. This string is inserted between consecutive fields.
	 * 
	 * @param defaultFieldSeparator
	 * @return
	 */
	@Test.Decl( "Can be emtpty" )
	@Test.Decl( "Throws assertion error for null" )
	@Test.Decl( "Applied to remaining fields" )
	@Test.Decl( "Previous fields unaffected" )
	@Test.Decl( "If not specified, default is empty string" )
	@Test.Decl( "Overrides default empty" )
	public FixedWidth defaultFieldSeparator( String defaultFieldSeparator ) {
		this.defaultSeparator = Assert.nonNull( defaultFieldSeparator );
		return this;
	}
	
	/**
	 * Use the given string to represent {@code null} fields.
	 * 
	 * @param nullRepresentation
	 * @return
	 */
	@Test.Decl( "Can be empty" )
	@Test.Decl( "Throws assertion error for null" )
	@Test.Decl( "Applied to remaining fields" )
	@Test.Decl( "Previous fields unaffected" )
	@Test.Decl( "If not specified, default is empty string" )
	@Test.Decl( "Overrides default empty" )
	public FixedWidth nullFields( String nullRepresentation ) {
		this.nullRepresentation = Assert.nonNull( nullRepresentation );
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
	public FixedWidth left( int width, char pad ) {
		Assert.positive( width );
		this.addField( width,  pad,  Strings::leftJustify );
		return this;
	}
	
	/**
	 * Append a left-justified field with given positive width, padding character, and representation.
	 * 
	 * @param width
	 * @return
	 */
	@Test.Decl( "Throws assertion error for null representation" )
	@Test.Decl( "Representation overrides toString() for this field" )
	@Test.Decl( "Other fields unaffected" )
	public <T> FixedWidth left( int width, char pad, Function<T, String> representation ) {
		Assert.positive( width );
		this.addField( width,  pad,  Strings::leftJustify, Assert.nonNull( representation ) );
		return this;
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
	public FixedWidth right( int width, char pad ) {
		Assert.positive( width );
		this.addField( width,  pad,  Strings::rightJustify );
		return this;
	}

	/**
	 * Append a right-justified field with given positive width, padding character, and representation.
	 * 
	 * @param width
	 * @return
	 */
	@Test.Decl( "Throws assertion error for null representation" )
	@Test.Decl( "Representation overrides toString() for this field" )
	@Test.Decl( "Other fields unaffected" )
	public <T> FixedWidth right( int width, char pad, Function<T, String> representation ) {
		Assert.positive( width );
		this.addField( width,  pad,  Strings::rightJustify, Assert.nonNull( representation ) );
		return this;
	}
	
	/**
	 * Append a field separator string. This overrides the default separator between this
	 * field and the next. If there is no subsequent field the separator is appended to the
	 * end of the formatted string.
	 * 
	 * @param separator
	 * @return
	 */
	@Test.Decl( "Can be empty" )
	@Test.Decl( "Throws assertion error for null separator" )
	@Test.Decl( "Overrides default" )
	@Test.Decl( "Does not affect subsequent separators" )
	@Test.Decl( "Multiple separators allowed" )
	@Test.Decl( "Appends if last" )
	public FixedWidth sep( String separator ) {
		this.addSep( Assert.nonNull( separator ) );
		return this;
	}
	
	/**
	 * Returns the current length of a formatted string
	 * 
	 * @return
	 */
	@Test.Decl( "Includes fields" )
	@Test.Decl( "Includes separators" )
	@Test.Decl( "Increases when field appended" )
	@Test.Decl( "Increses when separator appended" )
	public int length() {
		return this.length;
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
	@Test.Decl( "Throws class cast exception for wron argument type" )
	public String format( Object ... fields ) {
		if ( this.fieldCount != fields.length ) {
			throw new IllegalArgumentException( "Wrong number of fields. Expected " + this.fieldCount + ", got " + fields.length );
		}
		
		StringBuilder buf = new StringBuilder();
		for ( Function<Object[], String> f : this.functions ) {
			buf.append( f.apply( fields ) );
		}
		
		return buf.toString();
	}
	
	/**
	 * Return basic info about this formatter
	 */
	@Test.Decl( "Includes field count" )
	@Test.Decl( "Includes length" )
	@Override
	public String toString() {
		return "FixedWidth(" + this.fieldCount + " fields, length = " + this.length + ")";
	}

	@Test.Skip
	@FunctionalInterface
	private static interface Justifier {
		public String fmt( String s, int width, char pad );
	}
	
	private void addSep( String sep ) {
		this.functions.add( args -> sep );
		this.length += sep.length();
		this.previousIsField = false;
	}
	
	private void addField( int width, char pad, Justifier justifier ) {
		this.addField( width,  pad,  justifier, (x) -> x.toString() );
	}
	
	private <T> void addField( int width, char pad, Justifier justifier, Function<T, String> rep ) {
		if ( this.previousIsField ) {
			String sep = this.defaultSeparator;
			this.functions.add( (args) -> sep );
			this.length += sep.length();
		}
		
		int index = this.fieldCount++;
		String nrep = this.nullRepresentation;
		this.functions.add( args -> {
			@SuppressWarnings("unchecked")
			T val = (T) args[index];
			String s = val == null ? nrep : rep.apply( val );
			return justifier.fmt( s,  width,  pad );
		});

		this.length += width;
		this.previousIsField = true;
	}

}
