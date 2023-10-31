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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import sog.core.Assert;
import sog.core.Test;

/**
 * Static types and helpers
 */
@Test.Subject( "test." )
public class JSON {
	
	private JSON() {}
	
	public static JsonValue read( Reader reader ) throws IOException, JsonParseException {
		try ( JsonReader json = new JsonReader( reader ) ) {
			return json.readValue();
		}
	}
	
	public static JsonValue read( InputStream input ) throws IOException, JsonParseException {
		try ( JsonReader json = new JsonReader( input ) ) {
			return json.readValue();
		}
	}
	
	public static JsonValue fromString( String s ) throws IOException, JsonParseException {
		try ( JsonReader json = new JsonReader( s ) ) {
			return json.readValue();
		}
	}
	
	public static void write( JsonValue  value, Writer writer ) throws IOException {
		try ( JsonWriter json = new JsonWriter( writer ) ) {
			json.writeValue( value );
		}
	}

	public static void write( JsonValue  value, OutputStream output ) throws IOException {
		try ( JsonWriter json = new JsonWriter( output ) ) {
			json.writeValue( value );
		}
	}

	public static String toString( JsonValue value ) throws IOException {
		try ( StringWriter sw = new StringWriter(); JsonWriter json = new JsonWriter( sw ); ) {
			json.writeValue( value );
			json.flush();
			return sw.toString();
		}
	}

	
	
	public interface JsonValue {
		
		public void write( JsonWriter writer ) throws IOException;
		
		/**
		 * The implementation for simple JSON values (null, boolean, number, and string)
		 * returns the canonical JSON string representation for the element.
		 * (See: "https://www.json.org/json-en.html")
		 * 
		 * But the composite types (object and array) can represent arbitrarily large
		 * data structures and generally should not be converted to a String representation.
		 * Rather, one of the JSON.write methods can be used to write the representation
		 * to a Stream or Writer.
		 */
		@Override
		public String toString();
		
		public default JsonObject castToJsonObject() { return (JsonObject) this; }
		
		public default JsonArray castToJsonArray() { return (JsonArray) this; }
		
		public default JsonString castToJsonString() { return (JsonString) this; }
		
		public default JsonNumber castToJsonNumber() { return (JsonNumber) this; }
		
		public default JsonBoolean castToJsonBoolean() { return (JsonBoolean) this; }
		
	}

	
	
	public static interface JsonObject extends JsonValue {
		
		public JsonObject add( JsonString key, JsonValue value );
		
		public JsonObject add( String key, JsonValue value );
		
		public Map<String, JsonValue> toJavaMap();
		
		public Map<JsonString, JsonValue> getMembers();
		
	}
	
	/**
	 * Return an empty JSON Object.
	 * 
	 * @return
	 */
	public static JsonObject obj() {
		return new JsonObjectImpl();
	}
	
	
	
	public static interface JsonArray extends JsonValue {
		
		public JsonArray add( JsonValue element );
		
		public List<JsonValue> toJavaList();
		
	}

	/**
	 * Return an empty JSON Array
	 * @return
	 */
	public static JsonArray arr() {
		return new JsonArrayImpl();
	}
	
	
	
	public static interface JsonString extends JsonValue, Comparable<JsonString> {
		
		public String toJavaString();
		
	}

	/**
	 * Return an immutable JSON String
	 * @param s
	 * @return
	 */
	public static JsonString str( String s ) {
		return JsonStringImpl.forJavaValue( Assert.nonNull( s ) );
	}
	
	
	
	public static interface JsonNumber extends JsonValue, Comparable<JsonNumber> {
		
		public Integer toJavaInteger() throws ArithmeticException;
		
		public Double toJavaDouble() throws ArithmeticException;
		
		public BigDecimal toJavaBigDecimal();
		
	}
	
	public static JsonNumber num( int num ) {
		return new JsonNumberImpl( num, 0, 0 );
	}
		
	public static JsonNumber dec( int num, int frac ) {
		return new JsonNumberImpl( num, frac, 0 );
	}
	
	public static JsonNumber exp( int num, int frac, int exp ) {
		return new JsonNumberImpl( num, frac, exp );
	}
	
	public static JsonNumber big( BigDecimal big ) {
		return new JsonNumberImpl( Assert.nonNull( big ) );
	}
	
	
	
	public static interface JsonBoolean extends JsonValue {
		public Boolean toJavaBoolean();
	}
	
	public static final JsonBoolean TRUE = JsonBooleanImpl.TRUE;
	
	public static final JsonBoolean FALSE = JsonBooleanImpl.FALSE;
	
	
	
	public static interface JsonNull extends JsonObject, JsonArray {}
	
	public static final JsonNull NULL = JsonNullImpl.NULL;
	
	
}
