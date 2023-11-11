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
import java.util.Map;

import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;

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
	
	private Model() {}
	
	public static interface Structure {}
	
	public static interface Entity extends Structure {}
	
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD } )
	@Documented
	public @interface Member {}


	
	
	public interface Rep<T> {
		
		public T read( PrimitiveReader reader ) throws IOException, JsonException;
		
		public void write( T t, PrimitiveWriter writer ) throws IOException;
		
	}
	
	public static Rep<?> repForType( Type type ) throws ModelException {
		return switch ( type ) {
			case ParameterizedType pt when ( pt.getRawType() instanceof Class c ) -> 
				Model.repForClass( c, pt.getActualTypeArguments() );
			case ParameterizedType pt -> 
				throw new ModelException( "Unsupported parameterized type: " + pt );
			case Class<?> c when Entity.class.isAssignableFrom( c ) ->
				Model.repForClass( Entity.class, c );
			case Class<?> c when Structure.class.isAssignableFrom( c ) ->
				Model.repForClass( Structure.class, c );
			case Class<?> c -> Model.repForClass( c );
			default -> throw new ModelException( "Illegal Type: " + type );
		};
	}
	
	public static Rep<?> repForClass( Class<?> rawType, Type... params ) throws ModelException {
		return null;
	}
	
	

	private static final Map<String, Builder> BUILDER_MAP = null;
	
	@FunctionalInterface
	public static interface Builder {
		public Rep<?> build( Type[] params );
	}

	
}
