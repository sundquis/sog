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

import sog.core.Test;

/**
 * @author sundquis
 *
 */
@FunctionalInterface
@Test.Subject( "test." )
public interface Printable {

	/**
	 * Pretty print on the given IndentWriter
	 * 
	 * Write immediate state
	 * For nested Printable components,
	 * 		Increase indent
	 * 		print nested components
	 * 		Decrease indent
	 * Return the given IndentWriter to allow chaining.
	 * 
	 * @param out
	 * @param showDetails
	 */
	public void print( IndentWriter out );

	@Test.Decl( "Default uses System.out" )
	default public void print() {
		this.print( new IndentWriter() );
	}

	
}
