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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import sog.core.Test;
import sog.core.json.JSON.JsonArray;
import sog.core.json.JSON.JsonBoolean;
import sog.core.json.JSON.JsonNull;
import sog.core.json.JSON.JsonNumber;
import sog.core.json.JSON.JsonObject;
import sog.core.json.JSON.JsonString;
import sog.core.json.JSON.JsonValue;

/**
 * See: https://www.json.org/json-en.html
 */
@Test.Subject( "test." )
public class JsonReader extends PrimitiveReader {

	
	public JsonReader( BufferedReader buf ) throws IOException {
		super( buf );
	}
	
	public JsonReader( Reader reader ) throws IOException {
		super( reader );
	}
	
	public JsonReader( InputStream input ) throws IOException {
		super( input );
	}
	
	public JsonReader( String jsonValue ) throws IOException {
		super( jsonValue );
	}
	
	
	private static final char EOF = (char)-1;
	
	/*
	 * NOTE: This is non-standard:
	 * If the stream is empty at the point where we expect to read a value, we return JSON null.
	 */
	public JsonValue readJsonValue() throws IOException, JsonException {
		char c = this.skipWhiteSpace().curChar();
		switch (c) {
			case '{': return this.readJsonObject();
			case '[': return this.readJsonArray();
			case '"': return this.readJsonString();
			case 't': return this.readJsonBoolean();
			case 'f': return this.readJsonBoolean();
			case 'n': return this.readJsonNull();
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9': return this.readJsonNumber();
			case EOF: return JSON.NULL;  // Not sure about this.
			default: throw new JsonException( "Illegal character: " + c, this.getColumn() );
		}
	}
	
	
	public JsonObject readJsonObject() throws IOException, JsonException {
		JsonObjectImpl result = new JsonObjectImpl();
		this.skipWhiteSpace().consume( '{' );

		boolean first = true;
		while ( this.skipWhiteSpace().curChar() != '}' ) {
			if ( first ) {
				first = false;
			} else {
				this.consume( ',' );
			}
			String memberName = this.readString();
			this.skipWhiteSpace().consume( ':' );
			JsonValue value = this.readJsonValue();
			result.add( memberName, value );
		}
		
		this.consume( '}' );
		return result;
	}

	public JsonArray readJsonArray() throws IOException, JsonException {
		this.skipWhiteSpace().consume( '[' );
		JsonArrayImpl result = new JsonArrayImpl();
		
		boolean first = true;
		while ( this.skipWhiteSpace().curChar() != ']' ) {
			if ( first ) {
				first = false;
			} else {
				this.consume( ',' );
			}
			result.add( this.readJsonValue() );
		}

		this.consume( ']' );
		return result;
	}

	
	public JsonString readJsonString() throws IOException, JsonException {
		return new JsonStringImpl( this.readString() );
	}

	
	public JsonNumber readJsonNumber() throws IOException, JsonException {
		return new JsonNumberImpl( this.readNumber() );
	}

	
	public JsonBoolean readJsonBoolean() throws IOException, JsonException {
		return this.readBoolean() ? JSON.TRUE : JSON.FALSE;
	}
	
	
	public JsonNull readJsonNull() throws IOException, JsonException {
		this.readNull();
		return JSON.NULL;
	}
	
}
