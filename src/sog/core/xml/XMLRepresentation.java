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

package sog.core.xml;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Test;
import sog.core.xml.representation.ArrayRep;
import sog.core.xml.representation.IntegerRep;
import sog.core.xml.representation.ListRep;
import sog.core.xml.representation.MapRep;
import sog.core.xml.representation.StringRep;

/**
 * To define a new representation:
 * 		+ Extend XMLRepresentation<>. Suggested location is in or under sog.core.xml.representation
 * 		+ Define the required constructor accepting an array of Type instances. See ListRep
 * 		+ Implement the three abstract methods.
 * 		+ Add an entry to the TARGET_TO_CONS Map below.
 * 			The map key is the target type modeled by the representation.
 * 			The value is a static reference to the constructor.
 */
@Test.Subject( "test." )
public abstract class XMLRepresentation<T> {

	/**
	 * A simple description of the target type that can be used as a tag name in xml
	 * Concrete implementations should use the following test cases:
	 * 		"Result is not empty"
	 * 		"Result does not contain entity characters"
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Consume lines from the given XMLReader to produce an instance of the target type.
	 * Concrete implementations should use the following test cases:
	 * 		"Throws AssertionError for null reader"
	 * 		"Throws AppRuntime for malformed content"
	 * 		"Throws AppRuntime if an IOException occurs"
	 * 		"Returns null if element is not present"
	 * 		"If element not present then the reader has not advanced"
	 * 		"Write followed by read produces the original instance"
	 * 
	 * @param in
	 * @return
	 */
	public abstract T fromXML( XMLReader in );

	/**
	 * Convert the given instance to an xml representation and writ on the given writer.
	 * Concrete implementations should use the following test cases:
	 * 		"Throws AssertionError for null element"
	 * 		"Throws AssertionError for null writer"
	 * 		"Throws AppRuntime if an IOException occurs"
	 * 		"Read followed by write produces an equivalent representation"
	 * 
	 * @param t
	 * @param out
	 */
	public abstract void toXML( T t, XMLWriter out );


	@FunctionalInterface
	private static interface RepConstructor extends Function<Type[], XMLRepresentation<?>> {}

	/*
	 * The mapping registering all known representations.
	 * The key entry is the class instance for the target type of a representation.
	 * The value entry is the constructor for the representation.
	 * 
	 * NOTE: Arrays are handled differently. See forType(...)
	 */
	private static final Map<Class<?>, RepConstructor> TARGET_TO_CONS = Map.of(
		String.class, StringRep::new,
		Integer.class, IntegerRep::new,
		List.class, ListRep::new,
		Map.class, MapRep::new
	);

	/*
	 * Use the table of registered representations to find a constructor corresponding
	 * to the given type, then construct an instance of the representation.
	 * Compound representations (for example representations with type parameters or
	 * representations for arrays) will use the Type[] of component types.
	 */
	@Test.Decl( "Throws AssertionError for null target type" )
	@Test.Decl( "Throws AppRuntime when no representation for the target type has been registered" )
	private static XMLRepresentation<?> forClass( Class<?> targetType, Type... comps ) {
		RepConstructor rc = TARGET_TO_CONS.get( Assert.nonNull( targetType ) );
		if ( rc == null ) {
			throw new AppRuntime( "No representation for " + targetType );
		}
		return rc.apply( comps );
	}
	


	/**
	 * Return a representation for the given target type. The given Type instance must correspond
	 * to the type parameter S.
	 * 
	 * @param <S>
	 * @param type
	 * @return
	 */
	@SuppressWarnings( "unchecked" ) // Class design should ensure that the cast in the return statement succeeds.
	@Test.Decl( "Throws AssertionError for null type" )
	@Test.Decl( "Throws ClassCastException for improper type" )
	@Test.Decl( "Throws AppRuntime for a type without registered representation" )
	public static <S> XMLRepresentation<S> forType( Type type ) {
		XMLRepresentation<?> result = null;
		
		if ( type instanceof ParameterizedType ) {
			ParameterizedType pt = ParameterizedType.class.cast( type );
			result = forClass( Class.class.cast( pt.getRawType() ), pt.getActualTypeArguments() );
		} else if ( type instanceof Class && Class.class.cast( type ).isArray() ) {
			// Arrays get special treatment:
			result = new ArrayRep<>( Class.class.cast( type ).getComponentType() );
		} else if ( type instanceof Class ) {
			result = forClass( Class.class.cast( type ) ); 
		} else {
			throw new AppRuntime( "Unsupported field type: " + type );
		}

		return (XMLRepresentation<S>) result;
	}


}