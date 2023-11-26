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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sog.core.App;
import sog.core.LocalDir;
import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;

/**
 * Singleton classes must either declare a no-arg constructor that initializes member values,
 * or use initializers. This establishes the initial state, the first time the Singleton is
 * created. When the JVM shuts down, the current state is preserved for the next execution.
 * 
 * To get the persistent singleton instance use
 *   SomeSingleton s = Singleton.getInstance( SomeSingleton.class );
 */
@Test.Subject( "test." )
public abstract class Singleton implements Structure {


	/*
	 * Instances cached by concrete Singleton type.
	 */
	private static final Map<Class<?>, Singleton> classToSingleton = new HashMap<>();
	
	/**
	 * Get the unique instance of the given Singleton class.
	 * 
	 * @param <S>
	 * @param clazz
	 * @return
	 */
	public synchronized static <S extends Singleton> S getInstance( Class<S> clazz ) {
		@SuppressWarnings( "unchecked" )
		S result = (S) Singleton.classToSingleton.get( clazz );
		if ( result != null ) {
			return result;
		}
		
		LocalDir dir = new LocalDir().sub( "json" ).sub( "model" );
		Arrays.stream( clazz.getCanonicalName().split( "\\." ) ).forEach( dir::sub );
		final Path path = dir.getFile( "Instance", LocalDir.Type.JSON );
		
		@SuppressWarnings( "unchecked" )
		final Representation<S> rep = (Representation<S>) Representations.get().forType( clazz );
		
		try (
			PrimitiveReader in = path.toFile().exists() ? new PrimitiveReader( path ) : new PrimitiveReader( "{}" );
		) {
			result = rep.read( in );
		} catch ( Exception ex ) {
			throw new ModelException( "Unable to initialize Singleton instance: " + clazz, ex );
		}
		Singleton.classToSingleton.put( clazz, result );

		final S value = result;
		App.get().terminateOnShutdown( new App.OnShutdown() {
			@Override public void terminate() {
				try ( PrimitiveWriter out = new PrimitiveWriter( path ) ) {
					rep.write( value, out );
				} catch ( Exception ex ) {
					ex.printStackTrace();
				}
			}
		});
		
		return result;
	}
	
	protected Singleton() {
		// Check if constructor called from factory
		Boolean good = App.get().getLocation().filter( (s) -> s.startsWith( "sog.core.json.model." ) )
			.filter( (s) -> s.contains( "Singleton" ) )
			.filter( (s) -> s.contains( "in getInstance" ) )
			.findAny().isPresent();
		if ( !good ) {
			throw new ModelException( "Use Singleton.getInstance to construct Singleton instances" );
		}
	}

	
//	public static class SomeSingleton extends Singleton {
//		@Member private Integer myInt = 0;
//		@Member private String myString = "hi";
//	}
//
//	public static void main( String[] args ) {
//		try {
//			SomeSingleton ss = Singleton.getInstance( SomeSingleton.class );
//			App.get().msg( "Int = " + ss.myInt + ", Str = " + ss.myString );
//			ss.myInt++;
//			ss.myString += " and hi";
//			//SomeSingleton error = new SomeSingleton();
//			SomeSingleton s2 = Singleton.getInstance( SomeSingleton.class );
//			App.get().msg( "Same instance: " + (ss == s2) );
//		} catch ( Exception ex ) {
//			ex.printStackTrace();
//		}
//	}

}
