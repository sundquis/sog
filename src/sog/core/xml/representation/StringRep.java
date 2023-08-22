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

package sog.core.xml.representation;


import java.lang.reflect.Type;

import sog.core.Test;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class StringRep extends XMLRepresentation<String> {
	
	/**
	 * Represent a list of elements of type E.
	 * 
	 * @param comps		
	 */
	@Test.Decl( "Array of component types is ignored" )
	public StringRep( Type... comps ) {}
	
	@Override
	@Test.Decl( "Result is not empty" )
	@Test.Decl( "Result does not contain entity characters" )
	public String getName() {
		return "string";
	}

	@Override
	@Test.Decl( "Throws AssertionError for null reader" )
	@Test.Decl( "Throws XMLRuntime for malformed content" )
	@Test.Decl( "Write followed by read produces the original instance" )
	public String fromXML( XMLReader in ) {
		in.readOpenTag( this.getName() );
		String content = in.readContent();
		in.readCloseTag( this.getName() );
		
		return content;
	}

	@Override
	@Test.Decl( "Throws AssertionError for null element" )
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Read followed by write produces an equivalent representation" )
	public void toXML( String t, XMLWriter out ) {
		out.writeTag( this.getName(), t );
	}


}
