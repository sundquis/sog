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

import java.util.List;

import sog.core.Test;
import sog.core.xml.XMLRepresentation;

/**
 * 
 */
@Test.Subject( "test." )
public class ListRep<E> extends XMLRepresentation<List<E>> {

	private XMLRepresentation<E> elementRep;
	
	public ListRep( XMLRepresentation<E> elementRep ) {
		this.elementRep = elementRep;
	}

	@Override
	public String toString( List<E> t ) {
		return null;
	}

	
	@Override
	public List<E> fromString( String rep ) {
		return null;
	}

//	@Override
//	public String toXML( List<E> t ) {
//		final StringBuilder buf = new StringBuilder();
//		buf.append( "<list><size>" )
//			.append( t.size() )
//			.append( "</size>\n" );
//		t.stream().forEach( (e) -> buf.append( this.elementRep.toXML( e ) + "\n" ) );
//		buf.append(  "</list>" );
//		return buf.toString();
//	}

	
	public static void main( String[] args ) {
		IntegerRep eRep = new IntegerRep();
		ListRep<Integer> rep = new ListRep<>( eRep );
		List<Integer> list = List.of( 0, 0, -245763, Integer.MAX_VALUE, Integer.MIN_VALUE, +42 );
		//rep.testMappings( list );
	}

	
}
