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

package sog.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sog.core.App.OnShutdown;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * USAGE: Declare a field of the form
 * 		Stored<String> myString = Stored.get( "myString", "init" );
 * 
 * All instances of the containing class share the same instance of Stored<> so the field
 * may as well be static.
 */
@Test.Subject( "test." )
public class Stored<T> {
	
	private static final Map<Path, Stored<?>> LOADED = new HashMap<>();
	
	private static final Object LOCK = new Object() {};

	
	public static <S> Stored<S> get( String name, S initial ) {
		Assert.nonEmpty( name );
		
		final Class<?> caller = App.get().getCallingClass( 2 );
		Assert.isTrue( ! caller.isAnonymousClass(), "Stored properties not allowed in anonymous classes." );
		
		String fileName = caller.getSimpleName() + "." + name;
		Class<?> enclosing = caller;
		while ( (enclosing = enclosing.getEnclosingClass()) != null ) {
			fileName = enclosing.getSimpleName() + "." + fileName;
		}
		
		LocalDir dir = new LocalDir().sub( "data" );
		Arrays.stream( caller.getPackageName().split( "\\." ) ).forEach( dir::sub );
		final Path path = dir.getFile( fileName, LocalDir.Type.XML );

		synchronized ( Stored.LOCK ) {
			@SuppressWarnings( "unchecked" )
			Stored<S> result = (Stored<S>) Stored.LOADED.get( path );

			if ( result == null ) {
				Type type;
				try {
					Field field = caller.getDeclaredField( name );
					Assert.isTrue( field.getGenericType() instanceof ParameterizedType, "Field must be parameterized." );

					ParameterizedType paramType = (ParameterizedType) field.getGenericType();
					Assert.isTrue( Stored.class.equals( paramType.getRawType() ), "Can only use with a Stored field." );
					
					type = paramType.getActualTypeArguments()[0];
				} catch ( NoSuchFieldException | SecurityException e ) {
					throw new AssertionError( "Stored properites must correspond to a field in the calling class.", e );
				}

				final XMLRepresentation<S> rep = XMLRepresentation.forType( type );
				final Stored<S> stored = new Stored<>( initial );
				if ( path.toFile().exists() ) {
					try ( XMLReader in = new XMLReader( path ) ) {
						stored.set( rep.fromXML( in ) );
					} catch ( Exception e ) {
						throw new AppRuntime( "Unable to read Stored<" + type + ">", e );
					}
				}
				
				App.get().terminateOnShutdown( new OnShutdown() {
					@Override public void terminate() {
						try ( XMLWriter out = new XMLWriter( path ) ) {
							rep.toXML( stored.get(), out );
						} catch ( IOException e ) {
							throw new AppRuntime( "Unable to write Stored<" + type + ">", e );
						}
					}
				} );
				
				result = stored;
				Stored.LOADED.put( path, result );
			}
			
			return result;
		}
	}
	

	private T value;
	
	private Stored( T value ) {
		this.value = value;
	}

	public T get() {
		return this.value;
	}
	
	public void set( T t ) {
		this.value = t;
	}

}
