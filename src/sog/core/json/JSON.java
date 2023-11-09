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
	
	public static JsonValue read( Reader reader ) throws IOException, JsonException {
		try ( JsonReader json = new JsonReader( reader ) ) {
			return json.readJsonValue();
		}
	}
	
	public static JsonValue read( InputStream input ) throws IOException, JsonException {
		try ( JsonReader json = new JsonReader( input ) ) {
			return json.readJsonValue();
		}
	}
	
	public static JsonValue fromString( String s ) throws IOException, JsonException {
		try ( JsonReader json = new JsonReader( s ) ) {
			return json.readJsonValue();
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

	/**
	 * Use with caution. JsonValue instances may be arbitrarily complex.
	 * 
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public static String toString( JsonValue value ) throws IOException {
		try ( StringWriter sw = new StringWriter(); JsonWriter json = new JsonWriter( sw ); ) {
			json.writeValue( value );
			json.flush();
			return sw.toString();
		}
	}

	
	
	public sealed interface JsonValue permits JsonObject, JsonArray, JsonString, JsonNumber, JsonBoolean {}

	
	
	public static non-sealed interface JsonObject extends JsonValue {
		
		public Map<String, JsonValue> toJavaMap();
		
	}
	
	/**
	 * Return an empty JSON Object.
	 * 
	 * @return
	 */
	public static JsonObjectImpl obj() {
		return new JsonObjectImpl();
	}
	
	
	
	public static non-sealed interface JsonArray extends JsonValue {
		
		public List<JsonValue> toJavaList();
		
	}

	/**
	 * Return an empty JSON Array
	 * @return
	 */
	public static JsonArrayImpl arr() {
		return new JsonArrayImpl();
	}
	
	
	
	public static non-sealed interface JsonString extends JsonValue, Comparable<JsonString> {
		
		public String toJavaString();
		
	}

	/**
	 * Return an immutable JSON String
	 * @param s
	 * @return
	 */
	public static JsonStringImpl str( String s ) {
		return new JsonStringImpl( Assert.nonNull( s ) );
	}
	
	
	
	public static non-sealed interface JsonNumber extends JsonValue, Comparable<JsonNumber> {
		
		public Integer toJavaInteger() throws ArithmeticException;
		
		public Double toJavaDouble() throws ArithmeticException;
		
		public BigDecimal toJavaBigDecimal();
		
	}
	
	public static JsonNumberImpl num( int num ) {
		return new JsonNumberImpl( num );
	}
		
	public static JsonNumberImpl dec( double num ) {
		return new JsonNumberImpl( num );
	}
	
	public static JsonNumberImpl big( BigDecimal big ) {
		return new JsonNumberImpl( Assert.nonNull( big ) );
	}
	
	
	
	public static non-sealed interface JsonBoolean extends JsonValue {
		public Boolean toJavaBoolean();
	}
	
	public static final JsonBoolean TRUE = JsonBooleanImpl.TRUE;
	
	public static final JsonBoolean FALSE = JsonBooleanImpl.FALSE;
	

	
	
	public static sealed interface JsonNull extends JsonObject, JsonArray permits JsonNullImpl {}
	
	public static final JsonNull NULL = JsonNullImpl.NULL;
	
	
}
