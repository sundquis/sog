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
	
	private static final Registrar REGISTRAR = new Registrar();
	
	public static Registrar get() {
		App.get().classesUnderDir( App.get().sourceDir( Route.class ) ).forEach( REGISTRAR::addRoute );
		
		return Registrar.REGISTRAR;
	}
	
	private final SortedSet<Route> routes;
	
	private Registrar() {
		this.routes = new TreeSet<>();
	}
	
	
	private void addRoute( String name ) {
		Class<?> clazz;
		try {
			clazz = Class.forName( name );
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			return;
		}
		
		if ( Modifier.isAbstract( clazz.getModifiers() ) || ! Route.class.isAssignableFrom( clazz ) ) {
			return;
		}
		
		Route route;
		try {
			route = (Route) clazz.getDeclaredConstructor().newInstance();
		} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e ) {
			e.printStackTrace();
			return;
		}

		this.routes.add( route );
	}

	public Stream<Route> getRoutes() {
		return this.routes.stream();
	}
	

}
