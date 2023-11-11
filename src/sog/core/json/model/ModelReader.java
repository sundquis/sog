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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sog.core.Test;
import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;

/**
 * To extend model types:
 * 
 * For new primitive types add to Representation
 * For new parameterized types, enhance ModelReader.getReader( Type )
 */
@Test.Subject( "test." )
public class ModelReader extends PrimitiveReader {

	
	public ModelReader( BufferedReader buf ) throws IOException {
		super( buf );
	}
	
	public ModelReader( Reader reader ) throws IOException {
		super( reader );
	}
	
	public ModelReader( InputStream input ) throws IOException {
		super( input );
	}
	
	public ModelReader( String jsonValue ) throws IOException {
		super( jsonValue );
	}

	
	
	public <S extends Model.Structure> S readStructure( Class<S> clazz ) throws IOException, JsonException, ModelException {
		S result = this.getInstance( clazz );
		this.skipWhiteSpace().consume( '{' );
		
		boolean first = true;
		while ( this.skipWhiteSpace().curChar() != '}' ) {
			if ( first ) {
				first = false;
			} else {
				this.consume( ',' );
			}
			
			String memberName = this.readString();
			this.skipWhiteSpace().consume( ':' );
			this.setMemberValue( clazz, memberName, result ); 
		}
		
		this.consume( '}' );
		return result;
	}
	
	private <S> S getInstance( Class<S> clazz ) throws ModelException {
		try {
			Constructor<S> cons = clazz.getDeclaredConstructor();
			cons.setAccessible( true );
			return cons.newInstance();
		} catch ( Exception e ) {
			throw new ModelException( clazz + " must declare an accessible, no-arg constructor.", e );
		}
		
	}
	
	private void setMemberValue( Class<?> clazz, String memberName, Object instance ) throws ModelException {
		try {
			Field field = clazz.getDeclaredField( memberName );
			if ( field.getDeclaredAnnotation( Model.Member.class ) == null ) {
				throw new ModelException( clazz + " does not annotate member field: " + memberName );
			}
			field.setAccessible( true );
			Object value = this.getReader( field.getGenericType() ).read();
			field.set( instance, value );
		} catch ( NoSuchFieldException e ) {
			throw new ModelException( clazz + " does not define member field: " + memberName, e );
		} catch ( Exception e ) {
			throw new ModelException( "Problem setting value on " + clazz + "." + memberName, e );
		}
	}
	
	@FunctionalInterface
	private static interface ValReader {
		Object read() throws IOException, JsonException, ModelException;
	}
	
	@SuppressWarnings( "unchecked" )
	private ValReader getReader( Type type ) throws ModelException {
		return switch ( type ) {
			case ParameterizedType pt when ( pt.getRawType() instanceof Class c ) && List.class.isAssignableFrom( c ) -> 
				() -> this.readList( this.getReader( pt.getActualTypeArguments()[0] ) );
			case ParameterizedType pt -> 
				throw new ModelException( "Unsupported parameterized type: " + pt );
			case Class<?> c when Model.Entity.class.isAssignableFrom( c ) ->
				throw new ModelException( "Reference required for entity: " + c );
			case Class<?> c when Model.Structure.class.isAssignableFrom( c ) ->
				() -> this.readStructure( (Class<? extends Model.Structure>) c );
			case Class<?> c ->
				() -> Model.repForClass( c ).read( this );
			default -> throw new ModelException( "Illegal Type: " + type );
		};
	}
	
	private List<?> readList( ValReader reader ) throws IOException, JsonException, ModelException {
		List<Object> result = new ArrayList<>();
		this.skipWhiteSpace().consume( '[' );
		
		boolean first = true;
		while ( this.skipWhiteSpace().curChar() != ']' ) {
			if ( first ) {
				first = false;
			} else {
				this.consume( ',' );
			}
			result.add( reader.read() );
		}

		this.consume( ']' );
		return result;
	}


//	public static class TEST implements Model.Structure {
//		
//		@Model.Member private String name;
//		@Model.Member private Integer age;
//		@Model.Member private java.util.Date date;
//		@Model.Member private TEST parent;
//		@Model.Member private Model.Entity illegal;
//		@Model.Member private List<String> children;
//		@Model.Member private List<List<Integer>> square;
//		@Model.Member private Boolean profp;
//		
//		@Override public String toString() {
//			return """
//				TEST:
//				    name: %s
//				    age: %d
//				    date: %s
//				    parent: %s
//				    children: %s
//				    square: %s
//				    profp: %s
//				""".formatted( this.name, this.age, "" + this.date, "" + this.parent, sog.core.Strings.toString( this.children ), sog.core.Strings.toString( this.square ), this.profp );
//		}
//	}
//	
//	public static void main( String[] args ) {
//		try {
//			JSON.JsonValue square = JSON.arr()
//				.add( JSON.arr().add( JSON.num( 1 ) ).add( JSON.num( 2 ) ).add( JSON.num( 3 ) ) )
//				.add( JSON.arr().add( JSON.num( 4 ) ).add( JSON.num( 5 ) ).add( JSON.num( 6 ) ) )
//				.add( JSON.arr().add( JSON.num( 7 ) ).add( JSON.num( 8 ) ).add( JSON.num( 9 ) ) )
//			;
//			JSON.JsonValue json = JSON.obj()
//				.add( "name", JSON.str( "Tom") )
//				.add( "age", JSON.num( 42 ) )
//				.add( "date", JSON.big( new java.math.BigDecimal( new java.util.Date().getTime() ) ) )
//				.add( "parent", JSON.obj().add( "name", JSON.str( "Larry" ) ) )
//				//.add( "illegal", JSON.NULL )
//				.add( "children", JSON.arr().add( JSON.str( "Patrick" ) ).add( JSON.str( "Andrew" ) ) )
//				.add( "square", square )
//				.add( "profp", JSON.TRUE )
//			;
//			App.get().msg( JSON.toString( json ) );
//			ModelReader  mr = new ModelReader( JSON.toString( json ) );
//			TEST test = mr.readStructure( TEST.class );
//			App.get().msg( test.toString() );
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}
	
}
