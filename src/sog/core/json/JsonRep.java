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

package sog.core.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Test;

/**
 * JSON representation for fields of type T.
 * 
 * Responsible for writing the object state as JSON and constructing new instances
 * of T from JSON.
 * 
 * If a representation holds only immutable state then a factory method should
 * arrange to share a singleton instance.
 * 
 * To define a new representation:
 *   + Extend JsonRep<>. Suggested location is in or under sog.util.json.model.rep
 * 	 + Implement the two abstract methods.
 *   + Implement a static factory method that implements RepCons
 * 	 + Add an entry to the CLASS_TO_CONS Map below.
 * 	     The map key is the class of the target type modeled by the representation.
 * 	     The value is a static reference to the fatory method.
 * 
 */
@Test.Subject( "test." )
public interface JsonRep<T> {

	
	/**
	 * Consume data from the given reader and use it to construct a non-null instance of type T.
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 * @throws JsonException
	 */
	public T read( JsonReader reader ) throws IOException, JsonException;
	
	
	/**
	 * Convert the given non-null instance of T to its JSON representation and write to the given writer.
	 * 
	 * @param t
	 * @param writer
	 * @throws IOException
	 * @throws JsonException
	 */
	//public abstract void write( T t, JsonWriter writer ) throws IOException, JsonException;

	

//	@FunctionalInterface
//	private static interface RepCons extends Function<Type[], JsonRep<?>> {}
//
//	
//	/* Register representations here. */
//	private static final Map<Class<?>, RepCons> CLASS_TO_CONS = Map.of(
//		String.class, StringRep::get,
//		Integer.class, IntegerRep::get,
//		Double.class, DoubleRep::get,
//		BigDecimal.class, DecimalRep::get,
//		Boolean.class, BooleanRep::get,
//		List.class, ListRep::new
//	);
	

	/**
	 * Find a representation for the given Type of a member Field.
	 * Type comes from Field.getGenericType(); it could be a Class or a ParameterizedType instance
	 * 
	 * This method uses the Map of registered representations.
	 * 
	 * Algorithm: Check if Type is
	 *   + A Collection
	 *   + Another parameterized type
	 *   + A Class
	 *   
	 * @param <S>
	 * @param type
	 * @return
	 */
	@SuppressWarnings( "unchecked" )
	public static <S> JsonRep<S> forType( Type type ) {
		JsonRep<?> result = null;
//		// FIXME
//		result = switch ( type ) {
//			case ParameterizedType pt when List.class.isAssignableFrom( Class.class.cast( pt.getRawType() ) )
//				-> JsonRep.forClass( Class.class.cast( pt.getRawType() ), pt.getActualTypeArguments() );
//			case Class<?> clazz when clazz.isArray() -> new ArrayRep<>( clazz.getComponentType() );
//			case Class<?> clazz -> JsonRep.forClass( clazz );
//			default -> throw new AppRuntime( "No representation for " + type );
//		};
		
//		if ( type instanceof ParameterizedType pt ) {
//			result = JsonRep.forClass( Class.class.cast( pt.getRawType() ), pt.getActualTypeArguments() );
//		} else if ( type instanceof Class clazz && clazz.isArray() ) {
//			return new ArrayRep<>( clazz.getComponentType() );
//		} else if ( type instanceof Class clazz ) {
//			result = JsonRep.forClass( clazz );
//		} else if ( type instanceof List list ) {
//			
//		}
		
		return (JsonRep<S>) result;
	}
	
	
//	private static JsonRep<?> forClass( Class<?> target, Type... params ) {
//		RepCons rc = CLASS_TO_CONS.get( Assert.nonNull( target ) );
//		if ( rc == null ) {
//			throw new AppRuntime( "No JSON representation for " + target );
//		}
//		return rc.apply( params );
//	}
	
	
//public static void foo( Object obj ) {
//	switch ( obj ) {
//		case Integer x when x > 0 			-> System.out.println( "Positive: " + x );
//		case null 							-> System.out.println( "NULL" );
//		case Integer x when x < 0 			-> System.out.println( "Negative: " + x );
//		case Integer x 						-> System.out.println( "Integer: " + x );
//		case String x when x.length() > 4	-> System.out.println( "Long: " + x );
//		case String x 						-> System.out.println( "String: " + x );
//		default 							-> System.out.println( "Other: " + obj );
//	}
//}
//	
//	
//public static void main( String[] args ) {
//	Stream.of( 0, 42, -42, "hello", "hi" ).forEach( obj -> foo( obj ) );
//}
//

//Stream.of( new int[] {1, 2, 3}, List.of( "a", "b" ), "", 0, 42, -42, Integer.MAX_VALUE, Integer.MIN_VALUE, null, 1.2, "hello", "hi", -123.456, Boolean.TRUE, new Object() {} ).forEach( JsonRep::foo );
	
	
//	class TEST {
//	java.util.ArrayList<String> myField;
//}
//	try {
//		Field field = TEST.class.getDeclaredField( "myField" );
//		App.get().msg( "GenericType: " + field.getGenericType() );
//		App.get().msg( "Class: " + field.getGenericType().getClass() );
//		App.get().msg( "Type: " + field.getType() );
//		App.get().msg( "Class: " + field.getType().getClass() );
//		App.get().msg( "Assignable: " + java.util.Collection.class.isAssignableFrom( field.getType() ) );
//	} catch ( Exception ex ) {
//		ex.printStackTrace();
//	}
	
//	App.get().done();
	
}
