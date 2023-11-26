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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Representations {
	
	private static Representations INSTANCE = null;
	
	public static Representations get() {
		if ( Representations.INSTANCE == null ) {
			synchronized ( Representations.class ) {
				if ( Representations.INSTANCE == null ) {
					Representations.INSTANCE = new Representations();
				}
			}
		}
		
		return Representations.INSTANCE;
	}
	

	
	private final Map<Class<?>, RepresentationProvider> rawTypeToProvider;
	
	private Representations() {
		this.rawTypeToProvider = App.get().classesUnderDir( 
				App.get().sourceDir( Representations.class ), 
				Path.of( "sog", "core", "json", "model", "representation" ) )
			.map( this::providerForName )
			.filter( Objects::nonNull )
			.collect( Collectors.toMap( RepresentationProvider::getRawType, Function.identity() ) );
	}
	
	private RepresentationProvider providerForName( String className ) {
		Class<?> clazz;
		try {
			clazz = Class.forName( className );
		} catch ( ClassNotFoundException ex ) {
			throw new ModelException( ex );
		}
		
		if ( Modifier.isAbstract( clazz.getModifiers() ) || ! RepresentationProvider.class.isAssignableFrom( clazz ) ) {
			return null;
		}
		
		RepresentationProvider provider;
		try {
			provider = (RepresentationProvider) clazz.getDeclaredConstructor().newInstance();
		} catch ( Exception ex ) {
			throw new ModelException( ex );
		}
		
		return provider;
	}
	
	
	public Representation<?> forClass( Class<?> rawType, Type... params ) {
		RepresentationProvider provider = this.rawTypeToProvider.get( Assert.nonNull( rawType ) );
		
		if ( provider == null ) {
			throw new ModelException( "No Provider registered for raw type: " + rawType );
		}
		
		return provider.getRepresentationFor( params );
	}

	
	public Representation<?> forType( Type type ) {
		return switch ( type ) {
			case ParameterizedType pt when ( pt.getRawType() instanceof Class c ) -> 
				this.forClass( c, pt.getActualTypeArguments() );
			
			case ParameterizedType pt -> 
				throw new ModelException( "Unsupported parameterized type: " + pt );
			
			case Class<?> c when Structure.class.isAssignableFrom( c ) ->
				this.forClass( Structure.class, c );
			
			case Class<?> c -> 
				this.forClass( c );
			
			default -> 
				throw new ModelException( "Illegal Type: " + type );
		};
	}
	
	public Representation<?> forField( Field field ) {
		Assert.nonNull( field );
		
		return this.forType( field.getGenericType() );
	}
	


}
