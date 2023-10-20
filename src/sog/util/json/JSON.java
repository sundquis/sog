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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import sog.core.AppRuntime;
import sog.core.Test;

/**
 * Static types and helpers
 */
@Test.Subject( "test." )
public class JSON {
	
	private JSON() {}
	
	
	public static JsonValue read( Reader reader ) throws IOException, JsonParseException {
		try ( 
			BufferedReader buf = new BufferedReader( reader );
			JsonReader json = new JsonReader( buf )
		) {
			return json.readValue();
		}
	}
	
	public static JsonValue read( InputStream input ) throws IOException, JsonParseException {
		return JSON.read( new InputStreamReader( input, Charset.forName( "UTF-8" ) ) );
	}
	
	public static JsonValue fromJsonString( String s ) throws IOException, JsonParseException {
		return JSON.read( new StringReader( s ) );
	}

	
	
	public interface JsonValue {
		
		public JsonValue read( JsonReader reader ) throws IOException, JsonParseException;
		
		public void write( BufferedWriter writer ) throws IOException;
		
		public default void write( Writer writer, boolean formatted ) throws IOException {
			try ( 
				BufferedWriter buf = new BufferedWriter( writer )
			) {
				this.write( buf );
			}
		}
		
		public default void write( OutputStream output ) throws IOException {
			this.write( new OutputStreamWriter( output, Charset.forName( "UTF-8" ) ), false );
		}
		
		/**
		 * Produce the canonical JSON string representation for the element.
		 * See: "https://www.json.org/json-en.html"
		 * 
		 * 'Primitive' JSON types override this using internal state.
		 * 
		 * @return
		 * @throws IOException 
		 */
		public default String toJsonString() {
			try ( Writer sw = new StringWriter() ) {
				this.write( sw, true );
				return sw.toString();
			} catch ( IOException ex ) {
				// Probably only happens if we are out of resources, for large object?
				throw new AppRuntime( "Unable to construct string representation" );
			}
		}
		
	}

	
	
	public static interface JsonObject extends JsonValue {
		
		public JsonObject add( JsonString key, JsonValue value );
		
		public JsonObject add( String key, JsonValue value );
		
		public Map<String, JsonValue> toJavaMap();
		
		
		
	}
	
	public static JsonObject obj() {
		return new JsonObjectImpl();
	}
	
	
	
	public static interface JsonArray extends JsonValue {
		
		public JsonArray add( JsonValue element );
		
		public List<JsonValue> toJavaList();
		
	}
	
	public static JsonArray arr() {
		return new JsonArrayImpl();
	}
	
	
	
	public static interface JsonString extends JsonValue {
		
		public String toJavaString();
		
	}
	
	public static JsonString str( String s ) {
		return s == null ? JSON.NULL : new JsonStringImpl( s );
	}
	
	
	
	public static interface JsonNumber extends JsonValue {
		
		public Integer toJavaInteger();
		
		public Float toJavaFloat();
		
		public Double toJavaDouble();
		
		public Number toJavaNumber();
		
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
	
	public static final JsonBoolean TRUE = new JsonBooleanImpl( true );
	
	public static final JsonBoolean FALSE = new JsonBooleanImpl( false );
	
	
	
	public static interface JsonNull extends JsonObject, JsonArray, JsonString, JsonNumber, JsonBoolean {}
	
	public static final JsonNull NULL = new JsonNullImpl();
	
	
}
