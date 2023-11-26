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

import java.lang.reflect.Type;

import sog.core.Test;

/**
 * Concrete implementations must provide an accessible no-arg constructor.
 */
@Test.Subject( "test." )
public interface RepresentationProvider {
	
	public Class<?> getRawType();

	/**
	 * Providers may cache and reuse representations, depending on the state required.
	 * If the return has type Representation<E>, then the raw type of E must match getRawType()
	 * and if E is generic its type parameters must match the supplied parameters.
	 * 
	 * FIXME: Is there a way to statically declare the type relationship?
	 * 
	 * @param params
	 * @return
	 */
	public Representation<?> getRepresentationFor( Type... params );

}
