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
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLWriter implements AutoCloseable, XML.Helpers {
	
	private static final String INDENT = "    ";
	
	private final PrintWriter out;
	
	private final Deque<String> prefix;

	
	@Test.Decl( "Throws AssertionError for null path" )
	@Test.Decl( "Throws IOExcpetion for error openening file" )
	public XMLWriter( Path dataFile ) throws IOException {
		this.out = new PrintWriter( Files.newBufferedWriter( dataFile ) );
		this.prefix = new ArrayDeque<>();
		this.prefix.push( "" );
	}
	

	/**
	 * Write a tag with the given name and given string content.
	 * 
	 * @param name
	 * @param content
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AssertionError for null content" )
	@Test.Decl( "Content can be empty" )
	@Test.Decl( "Entities in content are encoded" )
	@Test.Decl( "Content can contain whitespace" )
	public void writeTag( String name, String content ) {
		this.indent();
		this.out.append( this.tagStart( name ) );
		this.out.append( this.encodeEntities( content ) );
		this.out.append( this.tagEnd( name ) );
		this.out.println();
	}

	

	/**
	 * Write the open tag for an element with mixed content.
	 * 
	 * @param name
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	public void writeOpenTag( String name ) {
		this.indent();
		this.out.println( this.tagStart( name ) );
		this.increaseIndent();
	}

	
	
	/**
	 * Write the close tag for an element with mixed content.
	 * 
	 * @param name
	 */
	@Test.Decl( "Throws AssertionError for empty name" )
	public void writeCloseTag( String name ) {
		this.decreaseIndent();
		this.indent();
		this.out.println( this.tagEnd( name ) );
	}

	

	private void increaseIndent() {
		this.prefix.push( this.prefix.peek() + INDENT );
	}
	
	
	private void decreaseIndent() {
		if ( this.prefix.size() == 1 ) {
			throw new IllegalStateException( "Indent already at minimum" );
		}
		this.prefix.pop();
	}
	
	
	private void indent() {
		this.out.append( this.prefix.peek() );
	}
	
	
	@Override
	@Test.Decl( "Idempotent" )
	public void close() {
		this.out.close();
	}


}
