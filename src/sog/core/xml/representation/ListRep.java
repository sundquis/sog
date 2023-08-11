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

	//FIXME
	// Need a more general XMLReader 
	// Deal with whitespace and elements over multiple lines?
	
	public static <T> XMLRepresentation<List<T>> getRep( XMLRepresentation<T> comp ) {
		return new ListRep<>( comp );
	}
	
	
	private XMLRepresentation<E> componentRep;
	
	public ListRep( XMLRepresentation<E> comp ) {
		this.componentRep = comp;
	}
	
	@Override
	public List<E> fromXML( XMLReader in ) {
		List<E> result = new ArrayList<>();
		in.readTagOpen( "List" );

		E elt = null;
		while ( (elt = this.componentRep.fromXML( in )) != null ) {
			result.add( elt );
		}
		
		in.readTagClose( "List" );
		return result;
	}

	@Override
	public void toXML( List<E> t, XMLWriter out ) {
		out.writeTagOpen( "List" );
		t.forEach( e -> this.componentRep.toXML( e, out ) );
		out.writeTagClose( "List" );
	}


}
