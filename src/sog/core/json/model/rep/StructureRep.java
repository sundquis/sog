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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.TreeMap;

import sog.core.Test;
import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.ModelException;
import sog.core.json.model.ModelRep;
import sog.core.json.model.Structure;

/**
 * 
 */
@Test.Subject( "test." )
public class StructureRep<S extends Structure> implements ModelRep<S> {
	
	/*
	 * We only need one instance per concrete Structure class. Save instances here:
	 */
	private static final Map<String, StructureRep<?>> typeToRep = new TreeMap<>();
	
	@SuppressWarnings( "unchecked" )
	public static synchronized StructureRep<?> forType( Type... params ) throws ModelException {
		if ( params == null || params.length != 1 ) {
			throw new ModelException( "Illegal parameters for StructureRep: " + params );
		}
		
		if ( params[0] instanceof Class c && Structure.class.isAssignableFrom( c ) ) {
			String typeName = c.getCanonicalName();
			StructureRep<?> result = StructureRep.typeToRep.get( typeName );
			if ( result == null ) {
				result = new StructureRep<>( c );
				StructureRep.typeToRep.put( typeName, result );
			}
			return result;
		} else {
			throw new ModelException( "Illegal type for StructureRep: " + params[0] );
		}
	}
	
	
	private final Class<S> structureClass;
	
	private final Map<String, MemberRep<?>> nameToMemberRep;
	
	private SortedSet<MemberRep<?>> members = null;
	
	private StructureRep( Class<S> clazz ) throws ModelException {
		this.structureClass = clazz;
		
		this.nameToMemberRep = new TreeMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for ( Field field : fields ) {
			MemberRep<?> mr = new MemberRep<>( field );
			if ( mr.isEntity() ) {
				throw new ModelException( "Structure " + clazz + " declares an Enitity Member: " + mr.getName() );
			}
			if ( mr.isMember() ) {
				mr.setRep();
				this.nameToMemberRep.put( mr.getName(), mr );
			}
		}
	}

	@Override
	public S read( PrimitiveReader reader ) throws IOException, JsonException, ModelException {
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
			MemberRep<?> mr = this.nameToMemberRep.get( memberName );
			if ( mr == null ) {
				throw new ModelException( this.structureClass + " does not declare Member " + memberName );
			}
			mr.readAndSet( reader, result );
		}
		
		reader.consume( '}' );
		return result;
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
	
	public SortedSet<MemberRep<?>> getMembers() {
		if ( this.members == null ) {
			this.members = new TreeSet<>( this.nameToMemberRep.values() );
		}
		return this.members;
	}

	@Override
	public void write( S structure, PrimitiveWriter writer ) throws IOException, ModelException {
		writer.append( '{' );
		
		boolean first = true;
		for ( MemberRep<?> mr : this.nameToMemberRep.values() ) {
			first = mr.write( first, writer, structure );
		}
		
		writer.append( '}' );
	}

	
//	private static class CHILD implements Structure {
//		//@Model.Member private Model.Entity illegal;
//		@Model.Member private String name = "Tom";
//		private CHILD( String name ) {
//			this.name = name;
//		}
//		private CHILD() {}
//	}
//	
//	private static class TEST implements Structure {
//		@Model.Member private String aString = "Hello world!";
//		@Model.Member private Integer anInteger = 42;
//		@Model.Member private Double aDouble = -12345.6789;
//		@Model.Member private Long aLong = Long.MAX_VALUE;
//		@Model.Member private Boolean aBoolean = true;
//		@Model.Member private java.util.Date aDate = new java.util.Date();
//		@Model.Member private java.util.List<String> aList = java.util.List.of( "foo", "bar" );
//		@Model.Member private CHILD aChild = new CHILD();
//		@Model.Member private java.util.List<CHILD> children = java.util.List.of( new CHILD("Patrick"),  new CHILD("Andrew"));
//		@Model.Member private List<List<List<List<String>>>> aNestedList = List.of( List.of(List.of(List.of("ha!"))));
//		
//		//@Model.Member private Model.Entity illegal;
//		
//		private String ignored = "Ignored";
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
//			ModelRep<TEST> rep = (ModelRep<TEST>) Model.get().repForType( StructureRep.class.getDeclaredField( "foo" ).getGenericType() );
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
