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
import java.util.HashMap;
import java.util.Map;

import sog.core.Test;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class MapRep<K, V> extends XMLRepresentation<Map<K, V>> {
	
	private XMLRepresentation<K> keyRep;
	
	private XMLRepresentation<V> valueRep;

	
	@Test.Decl( "Throws AssertionError for null array of component types" )
	@Test.Decl( "Throws AssertionError if not exactly two components" )
	public MapRep( Type... components ) {
		this.keyRep = XMLRepresentation.forType( components[0] );
		this.valueRep = XMLRepresentation.forType( components[1] );
	}

	@Override
	@Test.Decl( "Result is not empty" )
	@Test.Decl( "Result does not contain entity characters" )
	public String getName() {
		return "map";
	}

	@Override
	@Test.Decl( "Throws AssertionError for null reader" )
	@Test.Decl( "Throws XMLRuntime for malformed content" )
	@Test.Decl( "Write followed by read produces the original instance" )
	public Map<K, V> fromXML( XMLReader in ) {
		in.readOpenTag( this.getName() );
		
		in.readOpenTag( "size" );
		int size = Integer.valueOf( in.readContent() );
		in.readCloseTag( "size" );
		
		Map<K, V> result = new HashMap<>();
		for ( int i = 0; i < size; i++ ) {
			result.put( this.keyRep.fromXML( in ), this.valueRep.fromXML( in ) );
		}
		
		in.readCloseTag( this.getName() );
		
		return result;
	}

	@Override
	@Test.Decl( "Throws AssertionError for null map" )
	@Test.Decl( "Throws AssertionError for null key" )
	@Test.Decl( "Throws AssertionError for null value" )
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Read followed by write produces an equivalent representation" )
	public void toXML( Map<K, V> t, XMLWriter out ) {
		out.writeOpenTag( this.getName() );

		out.writeTag( "size", String.valueOf( t.size() ) );
		t.entrySet().forEach( (entry) -> {
			MapRep.this.keyRep.toXML( entry.getKey(), out );
			MapRep.this.valueRep.toXML( entry.getValue(), out );
		});  
		
		out.writeCloseTag( this.getName() );
	}
	
}
