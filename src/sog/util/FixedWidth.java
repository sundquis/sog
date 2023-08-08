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
 * FixedWidth is a composite formatting object for producing tabular style string representations
 * for sequences of objects.
 * 
 * A FixedWidth instance is composed of a sequence of fields and separators using the various
 * mutator methods. Each field specifies a formatting strategy, represented by the FunctionalInterface 
 * Field, that will produce a fixed width representation of a given string. Separators are represented
 * by fixed strings. 
 * 
 * A simple use case:
 * 		FixedWidth fw = new FixedWidth()
 * 			.sep( "| " )
 * 			.left( "Label", 12, ' ' )
 * 			.sep( " | " )
 * 			.right( "Cost", 8, ' ' )
 * 			.sep( " |" );
 * 		Stream.of(
 * 			fw.header(),
 * 			fw.format( "Item 1", "$13.95" ),
 * 			fw.format( "Item 2", "$4.30" ),
 * 			fw.format( "Item 3", "$100.18" )
 * 		).forEach( System.out::println );
 * 
 * @author sundquis
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
	

	
	/* 
	 * Fields and separators are both represented by functions. The argument is a given
	 * array of objects to be formatted. Field functions correspond to a fixed column,
	 * and act on the corresponding object. Separator functions ignore the objects,
	 * and simply return the fixed separator string.
	 */
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
	

	/**
	 * Append a new field.
	 * 
	 * @param name			The column name for the field in this position, used in the header.
	 * @param width			The width for this column; if 0, the width is determined from the column name.
	 * @param pad			The character to use for padding short strings.
	 * @param formatter		The strategy for producing string representations.
	 * @return				This FixedWidth instance.
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "If width is zero, the length of the column name determines the width" )
	@Test.Decl( "Throws AssertionError for null formatter" )
	@Test.Decl( "Formatting strategy is applied to the corresponding column" )
	@Test.Decl( "String representations formed using Strings.toString(...)" )
	@Test.Decl( "Returns this FixedWidth instance to allow chaining" )
	public FixedWidth field( String name, int width, char pad, Field formatter ) {
		Assert.nonEmpty( name );
		Assert.nonNull( formatter );
		
		final int w = width > 0 ? width : name.length();
		final int index = this.fieldCount++;
		this.formatters.add( args -> formatter.format( Strings.toString( args[index] ), w, pad ) );
		this.fieldNames.add( name );
		this.width += w;
		this.header = null;
		
		return this;
	}


	/**
	 * Append a separator.
	 * 
	 * @param separator		The non-empty separator string.
	 * @return				This FixedWidth instance.
	 */
	@Test.Decl( "Throws AssertionError for empty separator" )
	@Test.Decl( "Multiple consecutive separators allowed" )
	@Test.Decl( "Included in formatted string for each row" )
	@Test.Decl( "Returns this FixedWidth instance to allow chaining" )
	public FixedWidth sep( String separator ) {
		Assert.nonEmpty( separator );
		
		this.formatters.add( args -> separator );
		this.width += separator.length();
		this.header = null;
		
		return this;
	}

	
	/**
	 * Append a left-justified field with given positive width and padding character.
	 * 
	 * @param name			The column name for the field in this position, used in the header.
	 * @param width			The positive width for this column.
	 * @param pad			The character to use for padding short strings.
	 * @return				This FixedWidth instance.
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AssertionError for negative width" )
	@Test.Decl( "Appends left justified field" )
	@Test.Decl( "Padding character is used" )
	@Test.Decl( "Width is correct for short" )
	@Test.Decl( "Truncated for long" )
	@Test.Decl( "Returns this FixedWidth instance to allow chaining" )
	public FixedWidth left( String name, int width, char pad ) {
		Assert.isTrue( width >= 0 );
		return this.field( name, width, pad, Field.LEFT );
	}

	
	/**
	 * Append a right-justified field with given positive width and padding character.
	 * 
	 * @param name			The column name for the field in this position, used in the header.
	 * @param width			The positive width for this column.
	 * @param pad			The character to use for padding short strings.
	 * @return				This FixedWidth instance.
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AssertionError for negative width" )
	@Test.Decl( "Appends right justified field" )
	@Test.Decl( "Padding character is used" )
	@Test.Decl( "Width is correct for short" )
	@Test.Decl( "Truncated for long" )
	@Test.Decl( "Returns this FixedWidth instance to allow chaining" )
	public FixedWidth right( String name, int width, char pad ) {
		Assert.isTrue( width >= 0 );
		return this.field( name, width, pad, Field.RIGHT );
	}


	/**
	 * The total width (length) of a formatted string.
	 * 
	 * @return		The length of strings produced by thiw FixedWidth formatter.
	 */
	@Test.Decl( "Width is zero when constructed" )
	@Test.Decl( "Increases by field width when field appended" )
	@Test.Decl( "Increses by separator length when separator appended" )
	public int width() {
		return this.width;
	}
	
	/**
	 * Apply the specified field formats to the given fields and return the string representation.
	 * 
	 * @param args		The array of objects to format.
	 * @return			The fixed width representation.
	 */
	@Test.Decl( "Throws IllegalArgumentException for too few fields" )
	@Test.Decl( "Throws IllegalArgumentException for too many fields" )
	@Test.Decl( "Null fields are allowed" )
	@Test.Decl( "Fields formatted in the order specified" )
	@Test.Decl( "Fields are separated as specified" )
	@Test.Decl( "Fields can be added after formatting" )
	public String format( Object ... fields ) {
		if ( this.fieldCount != fields.length ) {
			throw new IllegalArgumentException( "Wrong number of fields. Expected " + this.fieldCount + ", got " + fields.length );
		}
		
		return this.formatters.stream().map( f -> f.apply( fields ) ).collect( Collectors.joining() );
	}
	

	/**
	 * The formated string of column names.
	 * 
	 * @return	The fixed width representation.
	 */
	@Test.Decl( "Length of the header equals width()" )
	@Test.Decl( "Includes configured separators" )
	@Test.Decl( "Includes configured column names" )
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
