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

package sog.util.json;

import sog.core.Test;
import sog.util.json.JSON.JArray;
import sog.util.json.JSON.JBoolean;
import sog.util.json.JSON.JNumber;
import sog.util.json.JSON.JObject;
import sog.util.json.JSON.JString;

/**
 * 
 */
@Test.Subject( "test." )
public class JBooleanImpl implements JBoolean {
	
	private final boolean value;
	
	JBooleanImpl( boolean value ) {
		this.value = value;
	}

	@Override
	public String toJSON() {
		return this.value ? "true" : "false";
	}

	@Override
	public Boolean toJavaObject() {
		return this.value;
	}

	@Override
	public JObject toJObject() throws IllegalCast {
		throw new IllegalCast( "JSON Boolean", "JSON Object" );
	}

	@Override
	public JArray toJArray() throws IllegalCast {
		throw new IllegalCast( "JSON Boolean", "JSON Array" );
	}

	@Override
	public JString toJString() throws IllegalCast {
		throw new IllegalCast( "JSON Boolean", "JSON String" );
	}

	@Override
	public JNumber toJNumber() throws IllegalCast {
		throw new IllegalCast( "JSON Boolean", "JSON Number" );
	}

	@Override
	public JBoolean toJBoolean() throws IllegalCast {
		return this;
	}


}
