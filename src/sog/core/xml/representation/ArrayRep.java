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

import java.lang.reflect.Array;
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
public class ArrayRep<E> extends XMLRepresentation<E[]> {
	
	private XMLRepresentation<E> componentRep;
	
	private Class<E> componentType;
	
	@Test.Decl( "Throws AssertionError for null component types" )
	public ArrayRep( Class<E> componentType ) {
		this.componentType = componentType;
		this.componentRep = XMLRepresentation.forType( componentType );
	}

	@Override
	@Test.Decl( "Result is not empty" )
	@Test.Decl( "Result does not contain entity characters" )
	public String getName() {
		return "Array[" + this.componentRep.getName() + "]";
	}

	@Override
	@Test.Decl( "Throws AssertionError for null reader" )
	@Test.Decl( "Throws AppRuntime for malformed content" )
	@Test.Decl( "Throws AppRuntime if an IOException occurs" )
	@Test.Decl( "Returns null if element is not present" )
	@Test.Decl( "If element not present then the reader has not advanced" )
	@Test.Decl( "Write followed by read produces the original instance" )
	public E[] fromXML( XMLReader in ) {
		List<E> list = new ArrayList<>();
		if ( ! in.readTagOpen( this.getName() ) ) {
			return null;
		}

		E elt = null;
		while ( (elt = this.componentRep.fromXML( in )) != null ) {
			list.add( elt );
		}
		
		in.readTagClose( this.getName() );
		
		@SuppressWarnings( "unchecked" )
		E[] result = (E[]) Array.newInstance( this.componentType, list.size() );
		int index = 0;
		for ( E e : list ) {
			result[index++] = e;
		}
		
		return result;
	}

	@Override
	@Test.Decl( "Throws AssertionError for null array" )
	@Test.Decl( "Throws AssertionError for null element" )
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Throws AppRuntime if an IOException occurs" )
	@Test.Decl( "Read followed by write produces an equivalent representation" )
	public void toXML( E[] t, XMLWriter out ) {
		out.writeTagOpen( this.getName() );
		for ( E e : t ) {
			this.componentRep.toXML( e, out );
		}
		out.writeTagClose( this.getName() );
	}
	
}
