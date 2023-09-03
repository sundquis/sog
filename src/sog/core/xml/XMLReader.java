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
	@Test.Decl( "Throws AssertionError for null path" )
	public XMLReader( Path path ) throws IOException {
		this.stream = Files.lines( Assert.nonNull( path ) );
		this.lines = this.stream.iterator();
		this.currentLine = null;
		this.lineIndex = 0;
		this.colIndex = 0;
		this.buffer = new StringBuilder();
	}
	
	
	/**
	 * Read the element open tag at the current position, advancing the reader beyond the closing '>'.
	 * 
	 * @return
	 * @throws XMLRuntime		If the current position does not hold an open tag with the given name.
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AppRuntime if reader does not hold an open tag with the given name" )
	@Test.Decl( "Leading whitespace is skipped" )
	@Test.Decl( "Advances reader beyond the end of tag" )
	public XMLReader readOpenTag( String name ) throws XMLRuntime {
		this.skipWhiteSpace();
		this.expectString( this.tagStart( name ) );
		
		return this;
	}
	
	

	/**
	 * Read the element close tag at the current position.
	 * 
	 * @return				The name of the tag.
	 * @throws XMLRuntime	If the current position does not hold an element close tag.
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AppRuntime if reader does not hold a close tag with the given name" )
	@Test.Decl( "Leading whitespace is skipped" )
	@Test.Decl( "Advances reader beyond the end of tag" )
	public XMLReader readCloseTag( String name ) throws XMLRuntime {
		this.skipWhiteSpace();
		this.expectString( this.tagEnd( name ) );
		
		return this;
	}
	

	
	/* Simplified content model. */
	private static final Pattern CONTENT_CHAR = Pattern.compile( "[^<]" );

	/* True if the current character can be part of an attribute value. Does not advance the reader. */
	private boolean contentChar() {
		return this.hasChar() && CONTENT_CHAR.matcher( String.valueOf( this.peekChar() ) ).matches();
	}

	/**
	 * Read and return the text content at the current position, advancing the reader
	 * to the following '<'.
	 * 
	 * @return
	 */
	@Test.Decl( "Return can be empty" )
	@Test.Decl( "Return can contain entities" )
	@Test.Decl( "Return can contain whitespace" )
	public String readContent() {
		this.buffer.setLength( 0 );
		
		while ( this.contentChar() ) {
			this.buffer.append( this.nextChar() );
		}
		
		return this.decodeEntities( this.buffer.toString() );
	}


	
	// CHARACTER ITERATION
	
	/**
	 * Returns true if there is a character to be read.
	 * 
	 * @return
	 */
	private boolean hasChar() {
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
	private char peekChar() {
		return this.currentLine.charAt( this.colIndex );
	}
	

	/**
	 * Return the current character and advance the reader.
	 * 
	 * @return
	 */
	private char nextChar() {
		return this.currentLine.charAt( this.colIndex++ );
	}
	

	/**
	 * Advance the reader beyond any whitespace characters.
	 * 
	 * This uses the java whitespace definition from Character, which is not identical to
	 * the xml specification.
	 * 
	 * @return
	 */
	private void skipWhiteSpace() {
		while ( this.hasChar() && Character.isWhitespace( this.peekChar() ) ) {
			this.nextChar();
		}
	}

	
	/**
	 * Advance the reader beyond the given expected sequence of characters.
	 * If the expected sequence is not found, an exception is thrown.
	 * 
	 * @param expected
	 * @return
	 * @throws XMLRuntime
	 */
	private void expectString( String expected ) throws XMLRuntime {
		for ( int i = 0; i < Assert.nonEmpty( expected ).length(); i++ ) {
			if ( ! this.hasChar() || this.nextChar() != expected.charAt( i ) ) {
				throw new XMLRuntime( "Expected " + expected + " here." );
			}
		}
	}

	
	
	@Override
	@Test.Decl( "Characters not available after close" )
	@Test.Decl( "Idempotent" )
	public void close() {
		this.stream.close();
		this.currentLine = null;
	}
	
	
	/**
	 * The location where we start looking for the next character.
	 * @return
	 */
	@Test.Decl( "Location includes the current line number" )
	@Test.Decl( "Location includes the current column number" )
	public String getLocation() {
		return "(" + (this.lineIndex +1) + ", "  + (this.colIndex + 1) + ")";
	}
	

}
