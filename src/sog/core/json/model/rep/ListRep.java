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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Model;
import sog.core.json.model.Model.Rep;
import sog.core.json.model.ModelException;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class ListRep<E> implements Rep<List<E>>{

	/*
	 * The only state is the representation for the element type so we can
	 * reuse instances based on element type.
	 */
	private final static Map<String, ListRep<?>> typeToRep = new TreeMap<>();
	
	public static synchronized ListRep<?> forType( Type... params ) throws ModelException {
		if ( params == null || params.length != 1 ) {
			throw new ModelException( "Illegal parameters for ListRep: " + params );
		}
		
		String typeName = params[0].getTypeName();
		ListRep<?> result = ListRep.typeToRep.get( typeName );
		if ( result == null ) {
			result = new ListRep<>( Model.get().repForType( params[0] ) );
			ListRep.typeToRep.put( typeName, result );
		}
		
		return result;
	}
	
	
	private final Rep<E> elementRep;
	
	private ListRep( Rep<E> elementRep ) {
		this.elementRep = elementRep;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<E> read( PrimitiveReader reader ) throws IOException, JsonException, ModelException {
		List<Object> result = new ArrayList<>();
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
		return (List<E>) result;
	}

	@Override
	public void write( List<E> list, PrimitiveWriter writer ) throws IOException, ModelException {
		writer.append( '[' );
		
		boolean first = true;
		for ( E elt : list ) {
			if ( first ) {
				first = false;
			} else {
				writer.append( ',' );
			}
			this.elementRep.write( elt, writer );
		}
		
		writer.append( ']' );
	}

//	private static List<Integer> int1 = List.of(1, 2, 3);
//	private static List<Integer> int2 = List.of(4, 5, 6);
//	
//	public static void main( String[] args ) {
//		try ( 
//			PrimitiveWriter out = new PrimitiveWriter( System.out );
//			PrimitiveReader in = new PrimitiveReader( "[0, 1, 2, 3, 42]" );
//		) {
//			Rep<List<Integer>> rep1 = (Rep<List<Integer>>) Model.get().repForType( ListRep.class.getDeclaredField( "int1" ).getGenericType() );
//			Rep<List<Integer>> rep2 = (Rep<List<Integer>>) Model.get().repForType( ListRep.class.getDeclaredField( "int2" ).getGenericType() );
//			App.get().msg( "Same?: " + (rep1 == rep2) );
//			rep1.write( int1, out );
//			rep2.write( int2, out );
//			out.flush();
//			App.get().msg( rep1.read( in ) );
//			App.get().done();
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}

}
