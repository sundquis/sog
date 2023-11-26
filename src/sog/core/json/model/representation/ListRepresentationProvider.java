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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Representation;
import sog.core.json.model.RepresentationProvider;
import sog.core.json.model.Representations;
import sog.core.json.model.ModelException;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class ListRepresentationProvider implements RepresentationProvider {

	/*
	 * Instances cached by the type the list element
	 */
	private final Map<Type, Representation<?>> elementTypeToRepresentation;

	public ListRepresentationProvider() {
		this.elementTypeToRepresentation = new HashMap<>();
	}

	@Override
	public Class<?> getRawType() {
		return List.class;
	}

	@Override
	public synchronized Representation<?> getRepresentationFor( Type... params ) {
		if ( params == null || params.length != 1 ) {
			throw new ModelException( "Expect one parameter for List: " + params );
		}
		
		Representation<?> result = this.elementTypeToRepresentation.get( params[0] );
		if ( result == null ) {
			result = new ListRepresentation<>( Representations.get().forType( params[0] ) );
			this.elementTypeToRepresentation.put( params[0], result );
		}
		
		return result;
	}


	private static class ListRepresentation<E> implements Representation<List<E>> {
		
		private final Representation<E> elementRep;
		
		private ListRepresentation( Representation<E> elementRep ) {
			this.elementRep = elementRep;
		}

		@Override
		public List<E> read( PrimitiveReader reader ) throws IOException {
			List<E> result = new ArrayList<>();
			reader.skipWhiteSpace().consume( '[' );
			
			boolean first = true;
			while ( reader.skipWhiteSpace().curChar() != ']' ) {
				if ( first ) {
					first = false;
				} else {
					reader.consume( ',' );
				}
				
				result.add( this.elementRep.read( reader ) );
			}
			
			reader.consume( ']' );
			return result;
		}

		@Override
		public void write( List<E> element, PrimitiveWriter writer ) throws IOException {
			writer.append( '[' );
			
			boolean first = true;
			for ( E elt : element ) {
				if ( first ) {
					first = false;
				} else {
					writer.append( ',' );
				}
				
				this.elementRep.write( elt, writer );
			}
			
			writer.append( ']' );
		}
		
	}
	

	private static List<Integer> int1 = List.of(1, 2, 3);
	private static List<Integer> int2 = List.of(4, 5, 6);
	
	@SuppressWarnings( "unchecked" )
	public static void main( String[] args ) {
		try ( 
			PrimitiveWriter out = new PrimitiveWriter( System.out );
			PrimitiveReader in = new PrimitiveReader( "[0, 1, 2, 3, 42]" );
		) {
			Representation<List<Integer>> rep1 = (Representation<List<Integer>>) 
				Representations.get().forField( ListRepresentationProvider.class.getDeclaredField( "int1" ) );
			Representation<List<Integer>> rep2 = (Representation<List<Integer>>) 
				Representations.get().forField( ListRepresentationProvider.class.getDeclaredField( "int2" ) );
			sog.core.App.get().msg( "Same?: " + (rep1 == rep2) );
			rep1.write( int1, out );
			rep2.write( int2, out );
			out.flush();
			sog.core.App.get().msg( rep1.read( in ) );
			sog.core.App.get().done();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}
