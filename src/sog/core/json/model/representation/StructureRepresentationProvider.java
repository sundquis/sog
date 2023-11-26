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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.ModelException;
import sog.core.json.model.Representation;
import sog.core.json.model.RepresentationProvider;
import sog.core.json.model.Structure;

/**
 * 
 */
@Test.Subject( "test." )
public class StructureRepresentationProvider implements RepresentationProvider {

	/*
	 * Instances cached by concrete Structure class
	 */
	private final Map<Class<?>, Representation<?>> classToRepresentation;
	
	public StructureRepresentationProvider() {
		this.classToRepresentation = new HashMap<>();
	}
	
	@Override
	public Class<?> getRawType() {
		return Structure.class;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public synchronized Representation<?> getRepresentationFor( Type... params ) {
		if ( params == null || params.length != 1 ) {
			throw new ModelException( "Expect one parameter for Structure: " + params );
		}
		
		if ( params[0] instanceof Class c && Structure.class.isAssignableFrom( c ) ) {
			Representation<?> result = this.classToRepresentation.get( c );
			if ( result == null ) {
				result = new StructureRepresentation<>( c );
				this.classToRepresentation.put( c, result );
			}
			return result;
		} else {
			throw new ModelException( "Illegal type for Structure representation: " + params[0] );
		}
	}

	
	private static class StructureRepresentation<S> implements Representation<S> {
		
		private final Class<S> structureClass;
		
		private final Map<String, StructureMember<?>> nameToMember;
		
		private StructureRepresentation( Class<S> structureClass ) {
			this.structureClass = structureClass;
			
			List<Field> fields = new ArrayList<>();
			Class<?> clazz = this.structureClass;
			while ( clazz != null ) {
				if ( Structure.class.isAssignableFrom( clazz ) ) {
					Collections.addAll( fields, clazz.getDeclaredFields() );
				}
				clazz = clazz.getSuperclass();
			}
			this.nameToMember = fields.stream()
				.map( StructureMember::new )
				.filter( StructureMember::isMember )
				.collect( Collectors.toMap( StructureMember::getName, Function.identity() ) );
		}

		@Override
		public S read( PrimitiveReader reader ) throws IOException {
			S result = this.getInstance();
			reader.skipWhiteSpace().consume( '{' );

			boolean first = true;
			while ( reader.skipWhiteSpace().curChar() != '}' ) {
				if ( first ) {
					first = false;
				} else {
					reader.consume( ',' );
				}
				
				String memberName = reader.readString();
				reader.skipWhiteSpace().consume( ':' );
				StructureMember<?> sm = this.nameToMember.get( memberName );
				if ( sm == null ) {
					throw new ModelException( this.structureClass + " does not declare Member " + memberName );
				}
				sm.readAndSet( reader, result );
			}
				
			reader.consume( '}' );
			return result;
		}

		@Override
		public void write( S element, PrimitiveWriter writer ) throws IOException {
			writer.append( '{' );
			
			boolean first = true;
			for ( StructureMember<?> sm : this.nameToMember.values() ) {
				first = sm.write( first, writer, element );
			}
			
			writer.append( '}' );
		}
		
		private S getInstance() throws ModelException {
			try {
				Constructor<S> cons = this.structureClass.getDeclaredConstructor();
				cons.setAccessible( true );
				return cons.newInstance();
			} catch ( Exception ex ) {
				throw new ModelException( this.structureClass + " does not have an accessible no-arg constructor." );
			}
		}
		
	}
	
	
	
	
	
//	private static class CHILD implements Structure {
//		//@sog.core.json.model.Member private Entity illegal;
//		@sog.core.json.model.Member private String name = "Tom";
//		
//		private CHILD( String name ) {
//			this.name = name;
//		}
//		
//		private CHILD() {}
//	}
//	
//	public static class NON_STRUCTURE {
//		@sog.core.json.model.Member private Integer IGNORED = 0;
//	}
//	
//	public static class BASE extends NON_STRUCTURE implements Structure {
//		@sog.core.json.model.Member private Integer BASE_INTEGER = 100;
//		public Integer BASE_IGNORED = 0;
//	}
//	
//	public static class INTERMEDIATE extends BASE {
//		@sog.core.json.model.Member protected Integer INTERMEDIATE_INTEGER = 200;
//		public Integer INTERMEDIATE_IGNORED = 0;
//	}
//	
//	public static class TEST extends INTERMEDIATE {
//		@sog.core.json.model.Member private String aString = "Hello world!";
//		@sog.core.json.model.Member private Integer anInteger = 42;
//		@sog.core.json.model.Member private Double aDouble = -12345.6789;
//		@sog.core.json.model.Member private Long aLong = Long.MAX_VALUE;
//		@sog.core.json.model.Member private Boolean aBoolean = true;
//		@sog.core.json.model.Member private java.util.Date aDate = new java.util.Date();
//		@sog.core.json.model.Member private java.util.List<String> aList = java.util.List.of( "foo", "bar" );
//		@sog.core.json.model.Member private CHILD aChild = new CHILD();
//		@sog.core.json.model.Member private java.util.List<CHILD> children = java.util.List.of( new CHILD("Patrick"),  new CHILD("Andrew"));
//		@sog.core.json.model.Member private List<List<List<List<String>>>> aNestedList = List.of( List.of(List.of(List.of("ha!"))));
//		
//		//@sog.core.json.model.Member private Entity illegal;
//		
//		public String ignored = "Ignored";
//		
//		private TEST( int x ) {}
//		private TEST() {}
//	}
//	
//	private static TEST foo = new TEST(0);
//	
//	
//	public static void main( String[] args ) {
//		try (
//			java.io.StringWriter sw = new java.io.StringWriter();
//			PrimitiveWriter out = new PrimitiveWriter( sw );
//		){
//			@SuppressWarnings( "unchecked" )
//			Representation<TEST> rep = (Representation<TEST>) sog.core.json.model.Representations.get().forField( StructureRepresentationProvider.class.getDeclaredField( "foo" ) );
//			rep.write( foo, out );
//			out.flush();
//			String JSON = sw.toString();
//			sog.core.App.get().msg( JSON );
//			
//			PrimitiveReader pr = new PrimitiveReader( JSON );
//			TEST test = rep.read( pr );
//			sog.core.App.get().msg( test );
//			test.aBoolean = false;
//			test.aString = "New string";
//			test.aDate = null;
//			test.INTERMEDIATE_INTEGER = 150;
//			
//			sw.getBuffer().setLength( 0 );
//			rep.write( test, out );
//			out.flush();
//			sog.core.App.get().msg( sw.toString() );
//			
//			sog.core.App.get().done();
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}
	
}
