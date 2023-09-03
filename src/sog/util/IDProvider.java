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
package sog.util;


import sog.core.Assert;
import sog.core.Test;

/**
 * @author sundquis
 *
 * Class containing the algorithm for generating integer ids from fully
 * qualified string names.
 * 
 * Usage:
 * 		ID.get( name )
 * 
 */
@Test.Subject( "test." )
public class IDProvider {
	
	private IDProvider() {}

	/**
	 * Based on the algorithm in java.lang.String.hashCode()
	 * A very good chance to get distinct integers for distinct names.
	 * 
	 * @param name
	 * 		Fully qualified, non-empty string name of an entity
	 * @return
	 * 		Integer encoding of name. 
	 */
	@Test.Decl( "Throws assertion error for empty name" )
	@Test.Decl( "Short names distinct" )
	@Test.Decl( "Case sensitive" )
	@Test.Decl( "No collision dict test" )
	@Test.Decl( "Qualified name stress test" )
	@Test.Decl( "Long strings have id" )
	public static int get( String name ) {
		Assert.nonEmpty( name ) ;
		
		int id = 0;
		for ( int i = 0; i < name.length(); i++ ) {
			// String algorithm uses *= 31, Many length 2 strings collide
			// Many collisions with *= 255
			// Hilarious! With *= 127 get collision on "milfs" and "Brno's"
			// += i resolves this collision
			id *= 127;
			id += i;
			id += name.charAt(i);
		}
		
		return id;
	}
	
	
}
