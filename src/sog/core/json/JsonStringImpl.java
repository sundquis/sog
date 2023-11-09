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

package sog.core.json;

import sog.core.Test;
import sog.core.json.JSON.JsonString;

/**
 * 
 */
@Test.Subject( "test." )
public final class JsonStringImpl implements JsonString {
	
	private final String javaValue;
	
	JsonStringImpl( String javaValue ) {
		this.javaValue = javaValue;
	}

	@Override
	public String toJavaString() {
		return this.javaValue;
	}

	@Override
	public int compareTo( JsonString other ) {
		return this.toJavaString().compareTo( other.toJavaString() );
	}
	
	@Override
	public int hashCode() {
		return this.javaValue.hashCode();
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( other instanceof JsonString js ) {
			return this.toJavaString().equals( js.toJavaString() );
		} else {
			return false;
		}
	}

	
}
