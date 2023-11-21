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
import sog.core.json.JsonRuntime;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;

/**
 * Singleton classes must either declare a no-arg constructor that initializes member values,
 * or use initializers. This establishes the initial state, the first time the Singleton is
 * created. When the JVM shuts down, the current state is preserved for the next execution.
 */
@Test.Subject( "test." )
public abstract class Singleton implements Structure {

	
	private static final Map<Class<?>, Singleton> instances = new HashMap<>();
	
	/**
	 * Get the unique instance of the given Singleton class.
	 * 
	 * @param <S>
	 * @param clazz
	 * @return
	 */
	public static <S extends Singleton> S getInstance( Class<S> clazz ) {
		LocalDir dir = new LocalDir().sub( "json" ).sub( "model" );
		Arrays.stream( clazz.getCanonicalName().split( "\\." ) ).forEach( dir::sub );
		final Path path = dir.getFile( "Instance", LocalDir.Type.JSON );
		
		@SuppressWarnings( "unchecked" )
		final ModelRep<S> rep = (ModelRep<S>) Singleton.getRep( clazz );
		
		S result = null;
		try (
			PrimitiveReader in = path.toFile().exists() ? new PrimitiveReader( path ) : new PrimitiveReader( "{}" );
		) {
			result = rep.read( in );
		} catch ( Exception ex ) {
			throw new JsonRuntime( "Unable to initialize Singleton instance: " + clazz, ex );
		}

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
	
	private static ModelRep<?> getRep( Class<?> clazz ) {
		try {
			return Model.get().repForClass( Structure.class, clazz );
		} catch ( ModelException ex ) {
			throw new JsonRuntime( "Illegal Structure definition.", ex );
		}
	}
	
	protected Singleton() {
		// Check if constructor called from factory
		Boolean good = App.get().getLocation().filter( (s) -> s.startsWith( "sog.core.json.model." ) )
			.filter( (s) -> s.contains( "Singleton" ) )
			.filter( (s) -> s.contains( "in getInstance" ) )
			.findAny().isPresent();
		if ( !good ) {
			throw new JsonRuntime( "Use Singleton.getInstance to construct Singleton instances" );
		}
	}

	
//	public static class SomeSingleton extends Singleton {
//		@Member private Integer myInt = 0;
//		@Member private String myString = "hi";
//	}
//
	public static void main( String[] args ) {
		try {
//			SomeSingleton ss = Singleton.getInstance( SomeSingleton.class );
//			App.get().msg( "Int = " + ss.myInt + ", Str = " + ss.myString );
//			ss.myInt++;
//			ss.myString += " and hi";
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}
