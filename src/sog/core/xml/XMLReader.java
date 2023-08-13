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
import java.util.stream.Stream;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLReader implements AutoCloseable, XML.Helpers {

	private Stream<String> stream;
	private Iterator<String> lines;
	private String currentLine;
	
	public XMLReader( Path path ) throws IOException {
		this.stream = Files.lines( path );
		this.lines = this.stream.iterator();
		this.currentLine = this.lines.hasNext() ? this.lines.next() : null;
	}
	
	// Return null if the current line does not match
	public String readTag( String tag ) {
		String result = this.currentLine;
		
		if ( result != null && result.startsWith( this.tagStart( tag ) ) && result.endsWith( this.tagEnd( tag ) ) ) {
			result = result.substring( this.tagStart( tag ).length() );
			result = result.substring( 0, result.length() - this.tagEnd( tag ).length() );
			result = this.decodeEntities( result );
			this.advance();
		} else {
			result = null;
		}
		
		return result;
	}

	public boolean readTagOpen( String tag ) {
		if ( this.currentLine != null && this.currentLine.startsWith( this.tagStart( tag ) ) ) {
			this.advance();
			return true;
		} else {
			return false;
		}
	}
	
	public void readTagClose( String tag ) {
		if ( this.currentLine != null && this.currentLine.endsWith( this.tagEnd( tag ) ) ) {
			this.advance();
		}
	}
	
	private void advance() {
		this.currentLine = this.lines.hasNext() ? this.lines.next() : null;
	}
	
	@Override
	public void close() throws Exception {
		this.stream.close();
	}
	

}
