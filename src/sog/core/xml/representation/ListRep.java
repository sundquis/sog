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
import java.util.ArrayList;
import java.util.List;

import sog.core.Test;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class ListRep<E> extends XMLRepresentation<List<E>> {

	
	private XMLRepresentation<E> componentRep;

	/**
	 * Represent a list of elements of type E. There should be a single element in the
	 * Type[], identifying the type of the components.
	 * 
	 * @param comps		
	 */
	@Test.Decl( "Throws AssertionError for null array of component types" )
	@Test.Decl( "Throws AssertionError if not exactly one component" )
	public ListRep( Type... comps ) {
		this.componentRep = XMLRepresentation.forType( comps[0] );
	}
	
	@Override
	@Test.Decl( "Result is not empty" )
	@Test.Decl( "Result does not contain entity characters" )
	public String getName() {
		return "List(" + this.componentRep.getName() + ")";
	}


	@Override
	@Test.Decl( "Throws AssertionError for null reader" )
	@Test.Decl( "Throws AppRuntime for malformed content" )
	@Test.Decl( "Throws AppRuntime if an IOException occurs" )
	@Test.Decl( "Returns null if element is not present" )
	@Test.Decl( "If element not present then the reader has not advanced" )
	@Test.Decl( "Write followed by read produces the original instance" )
	public List<E> fromXML( XMLReader in ) {
		List<E> result = new ArrayList<>();
		if ( ! in.readTagOpen( this.getName() ) ) {
			return null;
		}

		E elt = null;
		while ( (elt = this.componentRep.fromXML( in )) != null ) {
			result.add( elt );
		}
		
		in.readTagClose( this.getName() );
		return result;
	}

	@Override
	@Test.Decl( "Throws AssertionError for null element" )
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Throws AppRuntime if an IOException occurs" )
	@Test.Decl( "Read followed by write produces an equivalent representation" )
	public void toXML( List<E> t, XMLWriter out ) {
		out.writeTagOpen( this.getName() );
		t.forEach( e -> this.componentRep.toXML( e, out ) );
		out.writeTagClose( this.getName() );
	}

}
