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
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * Static types and helpers
 */
@Test.Subject( "test." )
public class JSON {
	
	private JSON() {}
	
	public static JsonValue read( Reader reader ) throws IOException, JsonParseException {
		try ( 
			JsonReader json = new JsonReader( reader )
		) {
			return json.readValue();
		}
	}
	
	public static JsonValue read( InputStream input ) throws IOException, JsonParseException {
		try ( 
			JsonReader json = new JsonReader( input )
		) {
			return json.readValue();
		}
	}
	
	public static JsonValue fromString( String s ) throws IOException, JsonParseException {
		try ( 
			JsonReader json = new JsonReader( s )
		) {
			return json.readValue();
		}
	}

	
	
	public interface JsonValue {
		
		public void write( Writer writer ) throws IOException;
		
		public void write( OutputStream output ) throws IOException;
		
		
		/**
		 * Produce the canonical JSON string representation for the element.
		 * See: "https://www.json.org/json-en.html"
		 */
		@Override
		public String toString();
		
		public JsonObject castToJsonObject() throws ClassCastException;
		
		public JsonArray castToJsonArray() throws ClassCastException;
		
		public JsonString castToJsonString() throws ClassCastException;
		
		public JsonNumber castToJsonNumber() throws ClassCastException;
		
		public JsonBoolean castToJsonBoolean() throws ClassCastException;
		
	}

	
	
	public static interface JsonObject extends JsonValue {
		
		public JsonObject add( JsonString key, JsonValue value );
		
		public JsonObject add( String key, JsonValue value );
		
		public Map<String, JsonValue> toJavaMap();
		
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
	
	
	
	public static interface JsonString extends JsonValue {
		
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
	
	
	
	public static interface JsonNumber extends JsonValue {
		
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
	
	
	
	public static interface JsonBoolean extends JsonValue {
		public Boolean toJavaBoolean();
	}
	
	public static final JsonBoolean TRUE = JsonBooleanImpl.TRUE;
	
	public static final JsonBoolean FALSE = JsonBooleanImpl.FALSE;
	
	
	
	public static interface JsonNull extends JsonObject, JsonArray {}
	
	public static final JsonNull NULL = JsonNullImpl.NULL;
	
	
	public static void main (String[] args ) {
		App.get().msg(); // int.int double or float?
	}
	
}
