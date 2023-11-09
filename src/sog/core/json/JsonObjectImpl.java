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

import java.util.Map;
import java.util.TreeMap;

import sog.core.Test;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public final class JsonObjectImpl implements JSON.JsonObject {
	
	
	private final Map<String, JsonValue> members;
	
	JsonObjectImpl() {
		this.members = new TreeMap<>();
	}
	
	@Override
	public Map<String, JsonValue> toJavaMap() {
		return this.members;
	}
	
	public JsonObjectImpl add( String key, JsonValue value ) {
		this.members.put( key, value );
		return this;
	}


}
