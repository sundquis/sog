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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import sog.core.json.model.rep.ListRep;
import sog.core.json.model.rep.MapRep;
import sog.core.json.model.rep.StructureRep;

/**
 * Manages JSON representations for various types.
 */
public class Model {

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
	
	
	/**
	 * In general, representations can depend on type parameters.
	 */
	@FunctionalInterface
	public interface Builder {
		
		public ModelRep<?> build( Type[] params ) throws ModelException;
		
	}
	
	/*
	 * Canonical name to Builder instance. 
	 */
	private final Map<String, Builder> nameToBuilder;
	
	/*
	 * Assemble the mappings from class names to Rep.Builder instances
	 */
	private Model() {
		this.nameToBuilder = new TreeMap<>();
		
		// Enumerated PrimitiveRep instances are the shared, stateless representations
		PrimitiveRep[] preps = PrimitiveRep.values();
		for ( PrimitiveRep prep : preps ) {
			String name = prep.getRawType().getCanonicalName();
			final ModelRep<?> rep = prep.getRep();
			Builder builder = (params) -> rep;
			this.nameToBuilder.put( name, builder );
		}
		
		// Register state-ful representations here. Implementations are in sog.core.json.model.rep
		this.nameToBuilder.put( List.class.getCanonicalName(), ListRep::forType );
		this.nameToBuilder.put( Structure.class.getCanonicalName(), StructureRep::forType );
		this.nameToBuilder.put( Map.class.getCanonicalName(), (params) -> MapRep.forType( false, params ) );
		this.nameToBuilder.put( SortedMap.class.getCanonicalName(), (params) -> MapRep.forType( true, params ) );
	}

	public ModelRep<?> repForType( Type type ) throws ModelException {
		return switch ( type ) {
			case ParameterizedType pt when ( pt.getRawType() instanceof Class c ) -> 
				this.repForClass( c, pt.getActualTypeArguments() );
			case ParameterizedType pt -> 
				throw new ModelException( "Unsupported parameterized type: " + pt );
//			case Class<?> c when Entity.class.isAssignableFrom( c ) ->
//				this.repForClass( Entity.class, c );
			case Class<?> c when Structure.class.isAssignableFrom( c ) ->
				this.repForClass( Structure.class, c );
			case Class<?> c -> this.repForClass( c );
			default -> throw new ModelException( "Illegal Type: " + type );
		};
	}
	
	public ModelRep<?> repForClass( Class<?> rawType, Type... params ) throws ModelException {
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
