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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.AppRuntime;
import sog.core.Stored;
import sog.core.Strings;
import sog.core.Test;
import sog.core.xml.representation.IntegerRep;
import sog.core.xml.representation.ListRep;
import sog.core.xml.representation.StringRep;

@Test.Subject( "test." )
public abstract class XMLRepresentation<T> {
	
	public static enum Registered {
		STRING( String.class ) {
			@Override protected XMLRepresentation<?> toRep( XMLRepresentation<?>... comps ) {
				return new StringRep();
			}
		},
		
		INTEGER( Integer.class ) {
			@Override protected XMLRepresentation<?> toRep( XMLRepresentation<?>... comps ) {
				return new IntegerRep();
			}
		},
		
		LIST( List.class ) {
			@Override protected XMLRepresentation<?> toRep( XMLRepresentation<?>... comps ) {
				return ListRep.getRep( comps[0] );
			}
		}
		;
		
		private Class<?> targetType;
		
		private Registered( Class<?> targetType ) {
			this.targetType = targetType;
		}
		
		public Class<?> getRawType() {
			return this.targetType;
		}
		
		protected abstract XMLRepresentation<?> toRep( XMLRepresentation<?>... comps );
	}
	
	public static final Map<Class<?>, Registered> REGISTERED = 
		Arrays.stream( Registered.values() ).collect( Collectors.toMap( Registered::getRawType, Function.identity() ) );


	// The cast in the return statement is unchecked. Class design should ensure it succeeds.
	@SuppressWarnings( "unchecked" )
	public static <S> XMLRepresentation<S> forType( Type type ) {

		XMLRepresentation<?> result = null;
		
		if ( type instanceof ParameterizedType ) {
			ParameterizedType pt = (ParameterizedType) type;
			Registered reg = getRegistered( pt.getRawType() );
			Type[] args = pt.getActualTypeArguments();
			XMLRepresentation<?>[] comps = new XMLRepresentation[ args.length ];
			for ( int  i = 0; i < args.length; i++ ) {
				comps[i] = XMLRepresentation.forType( args[i] );
			}
			result = reg.toRep( comps );
		} else if ( type instanceof Class && ((Class) type).isArray() ) {
			System.out.println( ">>> ARRAY" );
		} else if ( type instanceof Class ) {
			result = getRegistered( type ).toRep();
		} else {
			throw new AppRuntime( "Unsupported field type: " + type );
		}
		

		return (XMLRepresentation<S>) result;
	}

	private static Registered getRegistered( Type type ) {
		Registered result = REGISTERED.get( type );

		if ( result == null ) {
			throw new AppRuntime( "Unsupported field type: " + type );
		}

		return result;
	}
	
	
	public abstract T fromXML( XMLReader in );
	
	public abstract void toXML( T t, XMLWriter out );
	
	public static class Foo<X> {
		
		
		public void bar( Object obj ) {
			java.lang.reflect.Type type = obj.getClass().getGenericSuperclass();
			System.out.println( "TYPE: " + type );
			ParameterizedType pt = (ParameterizedType) type;
			System.out.println( "getTypeName: " + pt.getTypeName() );
			System.out.println( "getRawType: " + pt.getRawType() );
			System.out.println( "getActualTypeArguments: " + pt.getActualTypeArguments()[0] );
		}
		@Override public String toString() {
			return this.getClass().toGenericString();
		}
		public void accept( X myX ) {
			System.out.println( myX );
		}
	}
	
	public static <T> Foo<T> getFoo() {
		return new Foo<>();
	}
	
	public static Stored<List<String>> myStringList;
	
	public static Stored<String> myString;
	
	public static Stored<Integer> myInteger;
	
	public static Stored<String[]> myStringArray;
	
	public static String someString;
	
	public static List<String> someList;
	
	public static Stored<List<List<Integer>>> myIntegerListList;
	public static void main( String[] args ) {
		try {
//			Field field = XMLRepresentation.class.getDeclaredField( "someString" );
//			System.out.println( "getGenericType: " + field.getGenericType().getClass() );
//			field = XMLRepresentation.class.getDeclaredField( "myString" );
//			System.out.println( "getGenericType: " + field.getGenericType().getClass() );

			myString = Stored.get( "myString", "<open>&hi</open>" );
			System.out.println( myString.get() );
			
			myInteger = Stored.get( "myInteger", 0 );
			System.out.println( myInteger.get() );
			myInteger.set( myInteger.get() + 1 );
			System.out.println( myInteger.get() );
			
			myStringList = Stored.get( "myStringList", new ArrayList<>() );
			System.out.println( Strings.toString( myStringList.get() ) );
			myStringList.get().add( "'<>'" );
			System.out.println( Strings.toString( myStringList.get() ) );
			
			List<Integer> list = new ArrayList<>();
			list.add( 1 );
			List<List<Integer>> llist = new ArrayList<>();
			llist.add( list );
			list.add( 2 );
			llist.add( list );
			myIntegerListList = Stored.get( "myIntegerListList", llist );
			System.out.println( Strings.toString( myIntegerListList.get() ) );
			list.add( 3 );
			myIntegerListList.get().add( list );
			System.out.println( Strings.toString( myIntegerListList.get() ) );
			
			//myStringArray = Stored.get( "myStringArray", new String[] {"hi"} );
			
//			myStringList = Stored.get( "myStringList", new ArrayList<>() );
//			System.out.println( "Value is " + Strings.toString( myStringList.get() ) );
//			myStringList.get().add( "Another string" );
//			myStringList.get().add( "And another string" );
			
//			Stored<String> local = Stored.get( "myString", "init" );
//			System.out.println( "Local value is " + local.get() );
//			local.set( "Set from local" );
//			System.out.println( "Value is " + Strings.toString( myStringList.get() ) );
//			Supplier<Foo<?>> p = Foo::new;
//			System.out.println( p.get().toString() );
//			Object arg = "hi";
//			getFoo().accept( "hi" );
		} catch ( Throwable e ) {
			e.printStackTrace();
		}
//		Object[] objs = new Object[] {
//			"A",
//			new Object(),
//			new ListRep(),
//			new ArrayList<String>(),
//			new Integer[] {1, 2}
//		};
//		Arrays.stream( objs ).forEach( obj -> System.out.println( "OBJ: " + obj + ", TYPE: " + obj.getClass().getTypeName() ) );
		System.out.println( "\nDone!" );
	}
	
}