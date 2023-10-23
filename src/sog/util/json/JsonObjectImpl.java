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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import sog.core.Test;
import sog.util.json.JSON.JsonValue;
import sog.util.json.JSON.JsonObject;
import sog.util.json.JSON.JsonString;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonObjectImpl implements JSON.JsonObject {
	
	
	private final Map<JsonString, JsonValue> members;
	
	JsonObjectImpl() {
		this.members = new TreeMap<>();
	}
	
	@Override
	public Map<JsonString, JsonValue> getMembers() {
		return this.members;
	}
	
	@Override
	public JsonObject add( String key, JsonValue value ) {
		this.members.put( JSON.str( key ), value );
		return this;
	}

	@Override
	public JsonObject add( JsonString key, JsonValue value ) {
		this.members.put( key, value );
		return this;
	}

	@Override
	public Map<String, JsonValue> toJavaMap() {
		Map<String, JsonValue> result = new TreeMap<>();
		for ( Entry<JsonString, JsonValue> entry : this.members.entrySet() ) {
			result.put( entry.getKey().toJavaString(), entry.getValue() );
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "JSON.Object";
	}

	@Override
	public void write( JsonWriter writer ) throws IOException {
		writer.writeObject( this );
	}

}
