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

package sog.core.xml.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

import sog.core.AppRuntime;
import sog.core.Test;
import sog.core.xml.XML;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLWriter implements AutoCloseable {
	
	final private PrintWriter out;
	
	final private Deque<String> indent;
	
	XMLWriter( Path dataFile ) throws IOException {
		this.out = new PrintWriter( Files.newBufferedWriter( dataFile ) );
		this.indent = new ArrayDeque<>();
		this.indent.push( "" );
	}
	
	private void increaseIndent() {
		this.indent.push( this.indent.peek() + "    " );
	}
	
	private void decreaseIndent() {
		if ( this.indent.size() == 1 ) {
			throw new AppRuntime( "Already at minimum indent" );
		}
		this.indent.pop();
	}
	
	private void indent() {
		this.out.append( this.indent.peek() );
	}
	
	XMLWriter declaration() {
		this.indent();
		this.out.println( XML.get().getDeclaration() );
		return this;
	}
	
	XMLWriter startClass( Class<?> clazz ) {
		this.indent();
		this.out.append( "<class name = \"" )
			.append( clazz.getName() )
			.println( "\">" );
		this.increaseIndent();
		return this;
	}
	
	XMLWriter endClass() {
		this.decreaseIndent();
		this.indent();
		this.out.println( "</class>" );
		return this;
	}
	
	XMLWriter startField( XMLField field ) {
		this.indent();
		this.out.append( "<field name = \"" )
			.append( field.getName() )
			.append( "\" representation = \"REP\">" )
			.append( "STUFF" )
			.println( "</field>" );
		return this;
	}

	@Override
	public void close() {
		this.out.close();
	}


}
