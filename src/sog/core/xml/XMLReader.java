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

package sog.core.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.LocalDir;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLReader implements AutoCloseable, XML.Helpers {
	
	/* Keep a reference. Closing the stream closes the underlying file. */
	private final Stream<String> stream;
	
	/* File broken into strings delimited by newline characters. */
	private final Iterator<String> lines;

	/* The current line of input. */
	private String currentLine;
	
	/* Used to identify location, for diagnosing errors in the file. */
	private int lineIndex;
	
	/* 
	 * The position in the current line of the current character.
	 * Also used to identify location, for diagnosing errors in the file. 
	 */
	private int colIndex;

	/* Used for accumulating characters into strings. */
	private final StringBuilder buffer;

	/**
	 * Open the given xml file for reading.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public XMLReader( Path path ) throws IOException {
		this.stream = Files.lines( Assert.nonNull( path ) );
		this.lines = this.stream.iterator();
		this.currentLine = null;
		this.lineIndex = 0;
		this.colIndex = 0;
		this.buffer = new StringBuilder();
	}


	// CHARACTER ITERATION
	
	/**
	 * Returns true if there is a character to be read.
	 * 
	 * @return
	 */
	public boolean hasChar() {
		if ( this.currentLine == null || this.currentLine.length() <= this.colIndex ) {
			if ( this.lines.hasNext() ) {
				this.currentLine = this.lines.next() + "\n";
				this.lineIndex++;
				this.colIndex = 0;
			}
		}
		
		return this.currentLine != null && this.currentLine.length() > this.colIndex;
	}


	/**
	 * Return the current character but do not advance the reader.
	 * 
	 * @return
	 */
	public char peekChar() {
		return this.currentLine.charAt( this.colIndex );
	}
	

	/**
	 * Return the current character and advance the reader.
	 * 
	 * @return
	 */
	public char nextChar() {
		return this.currentLine.charAt( this.colIndex++ );
	}
	

	/**
	 * Advance the reader beyond any whitespace characters.
	 * This uses the java whitespace definition from Character, which is not identical to
	 * the xml specification.
	 * 
	 * @return
	 */
	public XMLReader skipWhiteSpace() {
		while ( this.hasChar() && Character.isWhitespace( this.peekChar() ) ) {
			this.nextChar();
		}
		return this;
	}

	
	/**
	 * Advance the reader beyond the given expected sequence of characters.
	 * If the expected sequence is not found, an exception is thrown.
	 * 
	 * @param expected
	 * @return
	 * @throws XMLRuntime
	 */
	public XMLReader expectString( String expected ) throws XMLRuntime {
		Assert.nonNull( expected );
		
		for ( int i = 0; i < expected.length(); i++ ) {
			this.wellFormed( 
				this.hasChar() && this.nextChar() == expected.charAt( i ), 
				"Expected " + expected + " here." 
			);
		}
		
		return this;
	}

	

	// NAME PARSING. Used for element names and attribute names.
	
	/* More restrictive than the xml specification. */
	private static final Pattern NAME_START = Pattern.compile( "[a-zA-Z:_]" );
	
	/* More restrictive than the xml specification. */
	private static final Pattern NAME_CHAR = Pattern.compile( "[-0-9.a-zA-Z:_]" );

	/* True if the current character can be the start of a name. Does not advance the reader. */
	private boolean nameStart() {
		return this.hasChar() && NAME_START.matcher( String.valueOf( this.peekChar() ) ).matches();
	}
	
	/* True if the current character can be part of a name. Does not advance the reader. */
	private boolean nameChar() {
		return this.hasChar() && NAME_CHAR.matcher( String.valueOf( this.peekChar() ) ).matches();
	}
	
	/**
	 * Read and return the name at the current position, advancing the reader to the first
	 * non-name character. 
	 * 
	 * Throws an XMLRuntime if the current position does not represent a legal name.
	 * 
	 * @return
	 * @throws XMLRuntime
	 */
	public String readName() throws XMLRuntime {
		this.buffer.setLength( 0 );

		this.wellFormed( this.nameStart(), "Illegal name." );
		
		this.buffer.append( this.nextChar() );
		while ( this.nameChar() ) {
			this.buffer.append( this.nextChar() );
		}
		
		return this.buffer.toString();
	}
	

	// CONTENT PARSING. Used for attribute values and element content.

	/* 
	 * The xml specification has two categories, one for attribute values and
	 * one for content. This simplified version ignores certain details, for example
	 * CDATA sections.
	 * Also, the only type of attribute is a CDATA attribute.
	 */
	private static final Pattern CONTENT_CHAR = Pattern.compile( "[^<\"]" );

	/* True if the current character can be part of an attribute value. Does not advance the reader. */
	private boolean contentChar() {
		return this.hasChar() && CONTENT_CHAR.matcher( String.valueOf( this.peekChar() ) ).matches();
	}

	/**
	 * Read and return the attribute value at the current position, advance the reader
	 * beyond the closing double quote.
	 * 
	 * Throws an XMLRuntime if the current position does not hold a legal attribute value.
	 * 
	 * @return
	 */
	public String readValue() throws XMLRuntime {
		this.buffer.setLength( 0 );
		
		this.expectString( "\"" );
		while ( this.contentChar() ) {
			this.buffer.append( this.nextChar() );
		}
		this.expectString( "\"" );
		
		return this.decodeEntities( this.buffer.toString() );
	}


	
	/**
	 * Read the element open tag at the current position, advancing the reader beyond the closing '>'.
	 * 
	 * @return
	 * @throws XMLRuntime		If the current position does hold an element open tag.
	 */
	public XMLTag readOpenTag() throws XMLRuntime {
		this.expectString( "<" );
		XMLTag result = new XMLTag( this.readName() );
		
		String name, value;
		this.skipWhiteSpace();
		while ( this.nameStart() ) {
			name = this.readName();
			this.skipWhiteSpace().expectString( "=" ).skipWhiteSpace();
			value = this.readValue();
			result.addAttribute( name, value );
			this.skipWhiteSpace();
		}
		
		// Not a name character or whitespace. Must be '>' or '/>'
		if ( this.hasChar() && this.peekChar() == '/' ) {
			result.markEmpty();
			this.nextChar();
		}
		this.expectString( ">" );
		
		return result;
	}
	
	
	/**
	 * Read the element open tag at the current position, advancing the reader beyond the closing '>'.
	 * 
	 * @param name
	 * @return
	 * @throws XMLRuntime		If the current position does hold an element open tag with the given name.
	 */
	public XMLTag expectOpenTag( String name ) throws XMLRuntime {
		Assert.nonEmpty( name );
		XMLTag result = this.readOpenTag();
		this.wellFormed( 
			result.getName().equals( name ), 
			"Expected open tag: " + name + ", Got: " + result.getName() 
		);
		return result;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String readContent() {
		this.buffer.setLength( 0 );
		
		while ( this.contentChar() ) {
			this.buffer.append( this.nextChar() );
		}
		
		return this.decodeEntities( this.buffer.toString() );
	}
	
	
	/**
	 * Read the element close tag at the current position.
	 * 
	 * @return				The name of the tag.
	 * @throws XMLRuntime	If the current position does not hold an element close tag.
	 */
	public String readCloseTag() throws XMLRuntime {
		this.expectString( "</" );
		String result = this.readName();
		this.skipWhiteSpace().expectString( ">" );
		
		return result;
	}
	

	/**
	 * Read the element close tag at the current position.
	 * 
	 * @param name
	 * @throws XMLRuntime	If the current position does not hold an element close tag with the given name.
	 */
	public void expectCloseTag( String name ) throws XMLRuntime {
		Assert.nonEmpty( name );
		String close = this.readCloseTag();
		this.wellFormed( close.equals( name ), "Expected close tag: " + name + ", Got: " + close );
	}
	
	
	/**
	 * Convenience method to make assertions and produce an exception with the current position and 
	 * diagnostic message when the assertion fails.
	 * 
	 * @param message
	 * @throws XMLRuntime
	 */
	public void wellFormed( boolean assertion, String message ) {
		if ( !assertion ) {
			throw new XMLRuntime( "Malformed xml at " + this.getLocation() + ": " + message );
		}
	}
	
	
	@Override
	public void close() throws Exception {
		this.stream.close();
	}
	
	
	/**
	 * The location where we start looking for the next character.
	 * @return
	 */
	public String getLocation() {
		return "(" + (this.lineIndex +1) + ", "  + (this.colIndex + 1) + ")";
	}
	
	
	public static void main( String[] args ) {
		Path path = new LocalDir().sub( "tmp" ).getFile( "TEST", LocalDir.Type.TEXT );
		try ( XMLReader in = new XMLReader( path ) ) {
			System.out.println( in.skipWhiteSpace().readOpenTag() );
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		System.out.println( "\nDone!" );
	}

}
