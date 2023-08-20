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
	
	public XMLWriter( Path dataFile ) throws IOException {
		this.out = new PrintWriter( Files.newBufferedWriter( dataFile ) );
		this.prefix = new ArrayDeque<>();
		this.prefix.push( "" );
	}
	
	
	public XMLWriter increaseIndent() {
		this.prefix.push( this.prefix.peek() + INDENT );
		return this;
	}
	
	
	public XMLWriter decreaseIndent() {
		if ( this.prefix.size() == 1 ) {
			throw new IllegalStateException( "Indent already at minimum" );
		}
		this.prefix.pop();
		return this;
	}
	
	
	@Deprecated
	public void writeTagXXX( String tag, String content ) {
		this.out.append( this.tagStart( tag ) )
			.append( this.encodeEntities( content ) )
			.append( this.tagEnd( tag ) );
		this.newlineXXX();
	}
	
	@Deprecated
	public void newlineXXX() {
		out.println();
	}
	
	@Deprecated
	public void writeTagOpenXXX( String tag ) {
		this.out.append( this.tagStart( tag ) );
		this.newlineXXX();
	}
	
	@Deprecated
	public void writeTagCloseXXX( String tag ) {
		this.out.append( this.tagEnd( tag ) );
		this.newlineXXX();
	}

	@Override
	public void close() {
		this.out.close();
	}


}
