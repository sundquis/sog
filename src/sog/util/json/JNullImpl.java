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

import java.util.List;
import java.util.Map;

import sog.core.Test;
import sog.util.json.JSON.JArray;
import sog.util.json.JSON.JBoolean;
import sog.util.json.JSON.JElement;
import sog.util.json.JSON.JNull;
import sog.util.json.JSON.JNumber;
import sog.util.json.JSON.JObject;
import sog.util.json.JSON.JString;

/**
 * 
 */
@Test.Subject( "test." )
public class JNullImpl implements JNull {
	
	JNullImpl() {}

	
	@Override
	public String toJSON() {
		return "null";
	}

	
	@Override
	public JObject toJObject() throws IllegalCast {
		return this;
	}

	@Override
	public JArray toJArray() throws IllegalCast {
		return this;
	}

	@Override
	public JString toJString() throws IllegalCast {
		return this;
	}

	@Override
	public JNumber toJNumber() throws IllegalCast {
		return this;
	}

	@Override
	public JBoolean toJBoolean() throws IllegalCast {
		return this;
	}

	
	
	@Override
	public Map<String, JElement> toJavaMap() {
		return null;
	}

	@Override
	public List<JElement> toJavaList() {
		return null;
	}

	@Override
	public String toJavaString() {
		return null;
	}

	@Override
	public Integer toJavaInteger() {
		return null;
	}

	@Override
	public Float toJavaFloat() {
		return null;
	}

	@Override
	public Double toJavaDouble() {
		return null;
	}

	@Override
	public Number toJavaNumber() {
		return null;
	}

	@Override
	public Boolean toJavaObject() {
		return null;
	}


	
	
	
	@Override
	public JObject add( JString key, JElement value ) {
		throw new IllegalOperation( "add" );
	}

	@Override
	public JObject add( String key, JElement value ) {
		throw new IllegalOperation( "add" );
	}

	@Override
	public JArray add( JElement element ) {
		throw new IllegalOperation( "add" );
	}


}
