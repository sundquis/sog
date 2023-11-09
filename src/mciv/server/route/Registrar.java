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

package mciv.server.route;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;

/**
 * Find and register all routes
 */
@Test.Subject( "test." )
public class Registrar {
	
	private final static Registrar INSTANCE = new Registrar();
	
	public static Registrar get() {
		return Registrar.INSTANCE;
	}
	
	
	private final SortedSet<Route> routes;
	
	private final SortedSet<String> classNames;
	
	private Registrar() {
		this.routes = new TreeSet<>();
		this.classNames = new TreeSet<>();
	}
	
	/*
	 * Return the unique Route instance for a given classname.
	 * We return null if:
	 *   + This className has already been loaded.
	 *   + The class fails to load (should not happen)
	 *   + The class is abstract or is not a subclass of Route
	 *   + It is not possible to construct an instance
	 */
	private Route newRouteForName( String className ) {
		if ( this.classNames.contains( className ) ) {
			return null;
		}
		
		Class<?> clazz;
		try {
			clazz = Class.forName( className );
		} catch ( ClassNotFoundException ex ) {
			Error.get().accept( ex, "Loading " + className );
			return null;
		}
		
		if ( Modifier.isAbstract( clazz.getModifiers() ) || ! Route.class.isAssignableFrom( clazz ) ) {
			return null;
		}
		
		Route route;
		try {
			route = (Route) clazz.getDeclaredConstructor().newInstance();
		} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex ) {
			Error.get().accept( ex, "Constructing " + className );
			return null;
		}

		this.routes.add( route );
		this.classNames.add( route.getClass().getCanonicalName() );
		
		return route;
	}
	
	// FIXME: We only need to search Route sub-packages
	private static String FIXME;
	
	public synchronized Stream<Route> getNewRoutes() {
		return App.get().classesUnderDir( App.get().sourceDir( Route.class ) )
			.map( this::newRouteForName )
			.filter( Objects::nonNull )
			.sorted();
	}

	public Stream<Route> getRoutes() {
		return this.routes.stream();
	}
	

}
