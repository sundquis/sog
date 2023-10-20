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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import sog.core.Test;
import sog.util.json.JSON.JsonArray;
import sog.util.json.JSON.JsonValue;
import sog.util.json.JSON.JsonNull;
import sog.util.json.JSON.JsonObject;
import sog.util.json.JSON.JsonString;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonNullImpl implements JsonNull {
	
	JsonNullImpl() {}

	
	@Override
	public Map<String, JsonValue> toJavaMap() {
		return null;
	}

	@Override
	public List<JsonValue> toJavaList() {
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
	public Boolean toJavaBoolean() {
		return null;
	}


	
	
	
	@Override
	public JsonObject add( JsonString key, JsonValue value ) {
		throw new JsonIllegalOperation( "add" );
	}

	@Override
	public JsonObject add( String key, JsonValue value ) {
		throw new JsonIllegalOperation( "add" );
	}

	@Override
	public JsonArray add( JsonValue element ) {
		throw new JsonIllegalOperation( "add" );
	}
	
	@Override
	public String toJsonString() {
		return "null";
	}


	@Override
	public JsonValue read( JsonReader reader ) throws IOException, JsonParseException {
		reader.skipWhiteSpace().consume( "null" );
		return JSON.NULL;
	}

	@Override
	public void write( BufferedWriter writer ) throws IOException {
		writer.append( "null" );
	}
	

}
