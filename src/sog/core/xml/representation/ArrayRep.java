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
		return "array";
	}

	@Override
	@Test.Decl( "Throws AssertionError for null reader" )
	@Test.Decl( "Throws XMLRuntime for malformed content" )
	@Test.Decl( "Write followed by read produces the original instance" )
	public E[] fromXML( XMLReader in ) {
		in.readOpenTag( this.getName() );
		
		in.readOpenTag( "length" );
		int length = Integer.parseInt( in.readContent() );
		in.readCloseTag( "length" );

		@SuppressWarnings( "unchecked" ) E[] result = (E[]) Array.newInstance( this.componentType, length );
		for ( int i = 0; i < length; i++ ) {
			result[i] = this.componentRep.fromXML( in );
		}

		in.readCloseTag( this.getName() );
		
		return result;
	}

	@Override
	@Test.Decl( "Throws AssertionError for null array" )
	@Test.Decl( "Throws AssertionError for null element" )
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Read followed by write produces an equivalent representation" )
	public void toXML( E[] t, XMLWriter out ) {
		out.writeOpenTag( this.getName() );
		
		out.writeTag( "length", String.valueOf( t.length ) );
		for ( E e : t ) {
			this.componentRep.toXML( e, out );
		}
		
		out.writeCloseTag( this.getName() );
	}
	
}
