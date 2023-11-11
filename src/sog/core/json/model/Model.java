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

package sog.core.json.model;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.rep.ListRep;
import sog.core.json.model.rep.StructureRep;

/**
 * Model.Structure marker interface
 * 
 * Structure instances have annotated Member fields.
 * 
 * Member fields can be
 * + Primitive
 * + Non-Entity Structure
 * + List<above>
 * 
 * Entity is persistent Structure
 * 
 * Entity instances can have references to Entity
 * 
 */
public class Model {

	/**
	 * Marker interface for classes that represent a JSON Object.
	 */
	public interface Structure {}
	
	/**
	 * Marker interface for Structure classes that are also persistent.
	 */
	public interface Entity extends Structure {}
	
	/**
	 * Structure classes use this annotation to mark persistent member fields.
	 * Unmarked fields are ignored.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD } )
	@Documented
	public @interface Member {}


	/**
	 * A Rep (representation) implements a bijection between instances of a given type
	 * a JSON representation.
	 */
	public interface Rep<T> {
		
		public T read( PrimitiveReader reader ) throws IOException, JsonException, ModelException;
		
		public void write( T t, PrimitiveWriter writer ) throws IOException, ModelException;
		
	}
	
	@FunctionalInterface
	public interface Builder {
		
		public Rep<?> build( Type[] params ) throws ModelException;
		
	}
	
	
	private static Model INSTANCE;
	
	public static Model get() {
		if ( Model.INSTANCE == null ) {
			synchronized ( Model.class ) {
				if ( Model.INSTANCE == null ) {
					Model.INSTANCE = new Model();
				}
			}
		}
		return Model.INSTANCE;
	}
	
	
	
	private final Map<String, Builder> nameToBuilder;
	
	/*
	 * Assemble the mappings from class names to Rep.Builder instances
	 */
	private Model() {
		this.nameToBuilder = new TreeMap<>();
		
		// PrimitiveRep instances are the shared, stateless representations
		PrimitiveRep[] preps = PrimitiveRep.values();
		for ( PrimitiveRep prep : preps ) {
			String name = prep.getRawType().getCanonicalName();
			final Rep<?> rep = prep.getRep();
			Builder builder = (params) -> rep;
			this.nameToBuilder.put( name, builder );
		}
		
		// Register stateful representations here. Implementations are in sog.core.json.model.rep
		this.nameToBuilder.put( List.class.getCanonicalName(), ListRep::forType );
		this.nameToBuilder.put( Structure.class.getCanonicalName(), StructureRep::forType );
	}

	public Rep<?> repForType( Type type ) throws ModelException {
		return switch ( type ) {
			case ParameterizedType pt when ( pt.getRawType() instanceof Class c ) -> 
				this.repForClass( c, pt.getActualTypeArguments() );
			case ParameterizedType pt -> 
				throw new ModelException( "Unsupported parameterized type: " + pt );
			case Class<?> c when Entity.class.isAssignableFrom( c ) ->
				this.repForClass( Entity.class, c );
			case Class<?> c when Structure.class.isAssignableFrom( c ) ->
				this.repForClass( Structure.class, c );
			case Class<?> c -> this.repForClass( c );
			default -> throw new ModelException( "Illegal Type: " + type );
		};
	}
	
	public Rep<?> repForClass( Class<?> rawType, Type... params ) throws ModelException {
		Builder builder = this.nameToBuilder.get( rawType.getCanonicalName() );
		if ( builder == null ) {
			throw new ModelException( "No registered representation for raw type: " + rawType );
		}
		return builder.build( params );
	}
	
	
//	private static List<String> foo;
//	
//	public static void main( String[] args ) {
//		try ( PrimitiveWriter out = new PrimitiveWriter( System.out ) ) {
//			Rep<String> rep = (Rep<String>) Model.get().repForType( Model.class.getDeclaredField( "foo" ).getGenericType() );
//			String s = """
//			String with newline
//				And tab
//				Quotes ' and "
//				Solidus /
//				Rev solidus \\
//			""";
//			rep.write( s, out );
//			Rep<java.util.Date> rep = (Rep<java.util.Date>) Model.get().repForType( java.util.Date.class );
//			rep.write( new java.util.Date(), out );
//			
//			App.get().done();
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}
//
	
	
}
