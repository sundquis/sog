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
@Test.Subject( "test." )
public class FixedWidth {
	
	
	@FunctionalInterface
	public interface Field {
		
		/** Return a string of the given width (length), based on a given string and padding character. */
		public String format( String s, int width, char pad );

		@Test.Skip
		public static Field LEFT = Strings::leftJustify;

		@Test.Skip
		public static Field RIGHT = Strings::rightJustify;
	}
	

	
	/* Field functions act on arguments from the array, separator functions return a separator. */
	private final List<Function<Object[], String>> formatters;
	
	/* The list of field names, used to build the header. */
	private final List<String> fieldNames;
	
	/* Formatted fields that depend on an array of objects, not separators. */
	private int fieldCount;
	
	/* Total width, in characters, of a formatted string. */
	private int width;
	
	/* The field names as a formatted string. */
	private String header;
	
	
	/**
	 * Construct an empty {@code FixedWidth} formatter.
	 */
	@Test.Decl( "Empty formatter produces empty formatted string" )
	public FixedWidth() {
		this.formatters = new ArrayList<>();
		this.fieldNames = new ArrayList<>();
		this.fieldCount = 0;
		this.width = 0;
		this.header = null;
	}
	
	
	public FixedWidth field( String name, int width, char pad, Field formatter ) {
		final int w = width > 0 ? width : name.length();
		final int index = this.fieldCount++;
		this.formatters.add( args -> formatter.format( Strings.toString( args[index] ), w, pad ) );
		this.fieldNames.add( name );
		this.width += w;
		this.header = null;
		
		return this;
	}

	
	@Test.Decl( "Throws AssertionError for empty separator" )
	@Test.Decl( "Throws AssertionError for null separator" )
	@Test.Decl( "Overrides default" )
	@Test.Decl( "Does not affect subsequent separators" )
	@Test.Decl( "Multiple separators allowed" )
	@Test.Decl( "Appends if last" )
	public FixedWidth sep( String separator ) {
		Assert.nonNull( separator );
		
		this.formatters.add( args -> separator );
		this.width += separator.length();
		this.header = null;
		
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
		if ( this.header == null ) {
			this.header = this.format( this.fieldNames.toArray() );
		}
		
		return this.header;
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
