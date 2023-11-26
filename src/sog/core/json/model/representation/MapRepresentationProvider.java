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

package sog.core.json.model.representation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import sog.core.Assert;
import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.ModelException;
import sog.core.json.model.Representation;
import sog.core.json.model.RepresentationProvider;
import sog.core.json.model.Representations;

/**
 * 
 */
@Test.Subject( "test." )
public class MapRepresentationProvider implements RepresentationProvider {
	
	private static class KVT {
		
		private final Type keyType;
		private final Type valType;
		
		private KVT( Type... params ) {
			this.keyType = Assert.nonNull( params[0] );
			this.valType = Assert.nonNull( params[1] );
		}
		
		@Override
		public int hashCode() {
			return Objects.hash( this.keyType, this.valType );
		}
		
		@Override
		public boolean equals( Object other ) {
			if ( other instanceof KVT kvt ) {
				return this.keyType.equals( kvt.keyType ) && this.valType.equals( kvt.valType );
			} else {
				return false;
			}
		}
	}
	
	/*
	 * Instances cached by the key and value element types
	 */
	private final Map<KVT, Representation<?>> kvtToRepresentation;
	
	public MapRepresentationProvider() {
		this.kvtToRepresentation = new HashMap<>();
	}
	
	@Override
	public Class<?> getRawType() {
		return Map.class;
	}

	@Override
	public synchronized Representation<?> getRepresentationFor( Type... params ) {
		if ( params == null || params.length != 2 ) {
			throw new ModelException( "Expect two parameters for Map: " + params );
		}
		
		KVT kvt = new KVT( params );
		Representation<?> result = this.kvtToRepresentation.get( kvt );
		if ( result == null ) {
			result = new MapRepresentation<>( 
				Representations.get().forType( params[0] ),
				Representations.get().forType( params[1] )
			);
			this.kvtToRepresentation.put( kvt, result );
		}
		
		return result;
	}
	
	protected <K, V> Map<K, V> newMap() {
		return new HashMap<>();
	}
	
	

	private class MapRepresentation<K, V> implements Representation<Map<K, V>> {

		private final Representation<K> keyRep;
		
		private final Representation<V> valRep;
		
		private MapRepresentation( Representation<K> keyRep, Representation<V> valRep ) {
			this.keyRep = keyRep;
			this.valRep = valRep;
		}
		
		/*
		 * Represent as a list of two-element lists
		 * [ [ key1, val1 ], ... ]
		 */

		@Override
		public Map<K, V> read( PrimitiveReader reader ) throws IOException {
			Map<K, V> result = MapRepresentationProvider.this.newMap();
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
		public void write( Map<K, V> element, PrimitiveWriter writer ) throws IOException {
			writer.append( '[' );
			
			boolean first = true;
			for ( Map.Entry<K,V> entry : element.entrySet() ) {
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
		
	}
	

//	private static Map<String, Integer> aMap = new HashMap<>( Map.of( "A", 3, "C", 4, "Random", 5, "Also random", 2, "B", 1 ) );
//	
//	@SuppressWarnings( "unchecked" )
//	public static void main( String[] args ) {
//		try (
//			java.io.StringWriter sw = new java.io.StringWriter();
//			PrimitiveWriter out = new PrimitiveWriter( sw );
//		) {
//			Representation<Map<String, Integer>> rep = (Representation<Map<String, Integer>>) Representations.get().forField( MapRepresentationProvider.class.getDeclaredField( "aMap" ) );
//			rep.write( aMap, out );
//			out.flush();
//			String s = sw.toString();
//			sog.core.App.get().msg( s );
//
//			PrimitiveReader in = new PrimitiveReader( s );
//			Map<String, Integer> map = rep.read( in );
//			sog.core.App.get().msg( "Map class: " + map.getClass() );
//			map.put( "foo", -42 );
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
