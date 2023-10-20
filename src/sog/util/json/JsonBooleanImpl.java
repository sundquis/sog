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

import sog.core.Test;
import sog.util.json.JSON.JsonBoolean;
import sog.util.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonBooleanImpl implements JsonBoolean {
	
	private final boolean value;
	
	JsonBooleanImpl( boolean value ) {
		this.value = value;
	}

	@Override
	public Boolean toJavaBoolean() {
		return this.value;
	}
	
	@Override
	public String toJsonString() {
		return this.value ? "true" : "false";
	}

	@Override
	public JsonValue read( JsonReader reader ) throws IOException, JsonParseException {
		char c = reader.skipWhiteSpace().curChar();
		switch (c) {
		case 't':
			reader.consume( "true" );
			return JSON.TRUE;
		case 'f':
			reader.consume( "false" );
			return JSON.FALSE;
		default:
			throw new JsonParseException( "Illegal character for boolean value", reader.getColumn() );
		}
	}
	
	
	@Override
	public void write( BufferedWriter writer ) throws IOException {
		writer.append( this.value ? "true" : "false" );
	}

}
