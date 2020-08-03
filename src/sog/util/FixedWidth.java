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
import sog.core.TestOrig;

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
	@TestOrig.Decl( "Empty formatter produces empty formatted string" )
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
	@TestOrig.Decl( "Can be emtpty" )
	@TestOrig.Decl( "Throws assertion error for null" )
	@TestOrig.Decl( "Applied to remaining fields" )
	@TestOrig.Decl( "Previous fields unaffected" )
	@TestOrig.Decl( "If not specified, default is empty string" )
	@TestOrig.Decl( "Overrides default empty" )
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
	@TestOrig.Decl( "Can be empty" )
	@TestOrig.Decl( "Throws assertion error for null" )
	@TestOrig.Decl( "Applied to remaining fields" )
	@TestOrig.Decl( "Previous fields unaffected" )
	@TestOrig.Decl( "If not specified, default is empty string" )
	@TestOrig.Decl( "Overrides default empty" )
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
	@TestOrig.Decl( "Width must be positive" )
	@TestOrig.Decl( "Appends left justified field" )
	@TestOrig.Decl( "Padding character is used" )
	@TestOrig.Decl( "Width is correct for short" )
	@TestOrig.Decl( "Truncated for long" )
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
	@TestOrig.Decl( "Throws assertion error for null representation" )
	@TestOrig.Decl( "Representation overrides toString() for this field" )
	@TestOrig.Decl( "Other fields unaffected" )
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
	@TestOrig.Decl( "Width must be positive" )
	@TestOrig.Decl( "Appends right justified field" )
	@TestOrig.Decl( "Padding character is used" )
	@TestOrig.Decl( "Width is correct for short" )
	@TestOrig.Decl( "Truncated for long" )
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
	@TestOrig.Decl( "Throws assertion error for null representation" )
	@TestOrig.Decl( "Representation overrides toString() for this field" )
	@TestOrig.Decl( "Other fields unaffected" )
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
	@TestOrig.Decl( "Can be empty" )
	@TestOrig.Decl( "Throws assertion error for null separator" )
	@TestOrig.Decl( "Overrides default" )
	@TestOrig.Decl( "Does not affect subsequent separators" )
	@TestOrig.Decl( "Multiple separators allowed" )
	@TestOrig.Decl( "Appends if last" )
	public FixedWidth sep( String separator ) {
		this.addSep( Assert.nonNull( separator ) );
		return this;
	}
	
	/**
	 * Returns the current length of a formatted string
	 * 
	 * @return
	 */
	@TestOrig.Decl( "Includes fields" )
	@TestOrig.Decl( "Includes separators" )
	@TestOrig.Decl( "Increases when field appended" )
	@TestOrig.Decl( "Increses when separator appended" )
	public int length() {
		return this.length;
	}
	
	/**
	 * Apply the specified field formats to the given fields and return the string representation.
	 * 
	 * @param args
	 * @return
	 */
	@TestOrig.Decl( "Throws illegal argument exception for too few fields" )
	@TestOrig.Decl( "Throws illegal argument exception for too many fields" )
	@TestOrig.Decl( "Null fields use null representation" )
	@TestOrig.Decl( "Non null fields represented appropriately" )
	@TestOrig.Decl( "Fields formatted in the order specified" )
	@TestOrig.Decl( "Fields are separated as specified" )
	@TestOrig.Decl( "Fields can be added after formatting" )
	@TestOrig.Decl( "Throws class cast exception for wron argument type" )
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
	@TestOrig.Decl( "Includes field count" )
	@TestOrig.Decl( "Includes length" )
	@Override
	public String toString() {
		return "FixedWidth(" + this.fieldCount + " fields, length = " + this.length + ")";
	}

	@TestOrig.Skip
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
