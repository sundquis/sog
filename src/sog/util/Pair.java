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

package sog.util;

import sog.core.Strings;
import sog.core.Test;

/**
 * Models a simple ordered pair, consisting of an x-coordinate and a y-coordinate.
 */
@Test.Subject( "test." )
public class Pair <X, Y> {
	
	private final X myX;
	private final Y myY;

	/**
	 * Construct an instance representing the given pair of elements.
	 * 
	 * @param myX
	 * @param myY
	 */
	@Test.Decl( "First coordinate can be null" )
	@Test.Decl( "Second coordinate can be null" )
	public Pair( X myX, Y myY ) {
		this.myX = myX;
		this.myY = myY;
	}

	/**
	 * Return the first coordinate.
	 * 
	 * @return
	 */
	@Test.Decl( "Returns original value" )
	public X getX() {
		return this.myX;
	}

	/**
	 * Return the second coordinate.
	 * 
	 * @return
	 */
	@Test.Decl( "Returns original value" )
	public Y getY() {
		return this.myY;
	}
	
	@Override
	@Test.Decl( "Uses canonical string representations for first coordinate" )
	@Test.Decl( "Uses canonical string representations for second coordinate" )
	public String toString() {
		return "(" + Strings.toString( this.myX ) + ", " + Strings.toString( this.myY ) + ")";
	}

}
