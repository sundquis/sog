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

import java.util.Iterator;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class IterableIterator<T> implements Iterable<T> {
	
	private final Iterator<T> iterator;
	
	private boolean done;

	@Test.Decl( "Throws AssertionError for null iterator" )
	public IterableIterator( Iterator<T> iterator ) {
		this.iterator = iterator;
		this.done = false;
	}

	@Override
	@Test.Decl( "Return is not null" )
	@Test.Decl( "Thorws IllegalStateException when called twice" )
	public Iterator<T> iterator() {
		if ( this.done ) {
			throw new IllegalStateException( "Multiple traversals not supported." );
		}
		this.done = true;
		return this.iterator;
	}


}
