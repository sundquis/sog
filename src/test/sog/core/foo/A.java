/**
 * Copyright (C) 2021
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

package test.sog.core.foo;

import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "For testing" )
public class A {
	
	public static Stream<String> classesInPackage() {
		return App.get().classesInPackage( Secondary.class );
	}
	
	public static String classname() {
		return "test.sog.core.foo.Secondary";
	}
	
	public static Stream<String> getLocation() {
		return Secondary.getLocationSecondary();
	}
	
	public static Stream<String> getLocation( String prefix ) {
		return Secondary.getLocationSecondary( prefix );
	}
	
	public static Stream<String> getLocationException() {
		return Secondary.getLocationExceptionSecondary();
	}
	
	public A() {}
	
	static public Object getSecondary() {
		return new Secondary();
	}

}

@Test.Skip( "For testing" )
class Secondary {
	
	static Stream<String> getLocationSecondary() {
		return App.get().getLocation();
	}

	static Stream<String> getLocationSecondary( String prefix ) {
		return App.get().getLocation( prefix );
	}

	static Stream<String> getLocationExceptionSecondary() {
		return App.get().getLocation( new Exception() );
	}
	
	@Override public String toString() {
		return App.get().getCallingMethod( 1 );
	}
}