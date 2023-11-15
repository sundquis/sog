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

package sog.core.json.model.rep;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import sog.core.Test;
import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Model;
import sog.core.json.model.ModelException;
import sog.core.json.model.ModelRep;

/**
 * 
 */
@Test.Subject( "test." )
public class MapRep<K, V> implements ModelRep<Map<K, V>> {
	
	public static MapRep<?, ?> forType( boolean sorted, Type... params ) throws ModelException {
		if ( params == null || params.length != 2 ) {
			throw new ModelException( "Illegal parameters for MapRep: " + params );
		}
		
		MapRep<?, ?> result = new MapRep<>( sorted,
			Model.get().repForType( params[0] ), 
			Model.get().repForType( params[1] ) 
		);
		
		return result;
	}

	
	private final boolean sorted;
	
	private final ModelRep<K> keyRep;
	
	private final ModelRep<V> valRep;
	
	private MapRep( boolean sorted, ModelRep<K> keyRep, ModelRep<V> valRep ) {
		this.sorted = sorted;
		this.keyRep = keyRep;
		this.valRep = valRep;
	}
	
	private Map<K, V> newMap() {
		return this.sorted ? new TreeMap<>() : new HashMap<>();
	}
	
	/*
	 * Represent as a list of two-element lists
	 * [ [ key1, val1 ], ... ]
	 */

	@Override
	public Map<K, V> read( PrimitiveReader reader ) throws IOException, JsonException, ModelException {
		Map<K, V> result = this.newMap();
		reader.skipWhiteSpace().consume( '[' );
		
		boolean first = true;
		while ( reader.skipWhiteSpace().curChar() != ']' ) {
			if ( first ) {
				first = false;
			} else {
				reader.consume( ',' );
			}
			
			reader.skipWhiteSpace().consume( '[' );
			
			K key = this.keyRep.read( reader );
			reader.skipWhiteSpace().consume( ',' );
			V val = this.valRep.read( reader );
			
			reader.skipWhiteSpace().consume( ']' );
			
			result.put( key, val );
		}
		
		reader.consume( ']' );
		return result;
	}

	@Override
	public void write( Map<K, V> t, PrimitiveWriter writer ) throws IOException, ModelException {
		writer.append( '[' );
		
		boolean first = true;
		for ( Map.Entry<K,V> entry : t.entrySet() ) {
			if ( first ) {
				first = false;
			} else {
				writer.append( ',' );
			}
			
			writer.append( '[' );
			
			this.keyRep.write( entry.getKey(), writer );
			writer.append( ',' );
			this.valRep.write( entry.getValue(), writer );

			writer.append( ']' );
		}
		
		writer.append( ']' );
	}
	

//	private static java.util.SortedMap<Integer, String> aMap = new TreeMap<>( Map.of(3, "C", 2, "B", 1, "A") );
//	
//	public static void main( String[] args ) {
//		try (
//			java.io.StringWriter sw = new java.io.StringWriter();
//			PrimitiveWriter out = new PrimitiveWriter( sw );
//		) {
//			ModelRep<Map<Integer, String>> rep = (ModelRep<Map<Integer, String>>) Model.get().repForType( MapRep.class.getDeclaredField( "aMap" ).getGenericType() );
//			rep.write( aMap, out );
//			out.flush();
//			String s = sw.toString();
//			sog.core.App.get().msg( s );
//
//			PrimitiveReader in = new PrimitiveReader( s );
//			Map<Integer, String> map = rep.read( in );
//			map.put( -42, "foo" );
//			sog.core.App.get().msg( sog.core.Strings.toString( map ) );
//			
//			sw.getBuffer().setLength( 0 );
//			rep.write( map, out );
//			out.flush();
//			sog.core.App.get().msg( sw.toString() );
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}

}
