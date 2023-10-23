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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import sog.core.AppRuntime;
import sog.core.Test;
import sog.util.json.JSON.JsonArray;
import sog.util.json.JSON.JsonBoolean;
import sog.util.json.JSON.JsonNumber;
import sog.util.json.JSON.JsonObject;
import sog.util.json.JSON.JsonString;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class JsonValueImpl {
	
	protected JsonValueImpl() {}

	protected abstract void write( BufferedWriter writer ) throws IOException;
	
	/**
	 * Produce the canonical JSON string representation for the element.
	 * See: "https://www.json.org/json-en.html"
	 */
	@Override
	public abstract String toString();
	
	/* Composite types can use this to implement their toString */
	protected String toStringImpl() {
		try (
			StringWriter out = new StringWriter()
		) {
			this.write( out );
			return out.toString();
		} catch ( IOException ex ) {
			ex.printStackTrace();
			throw new AppRuntime( "Should not happen.", ex );
		}
	}

	public void write( Writer writer ) throws IOException {
		try ( 
			BufferedWriter buf = new BufferedWriter( writer )
		) {
			this.write( buf );
		}
	}
	
	public void write( OutputStream output ) throws IOException {
		this.write( new OutputStreamWriter( output, Charset.forName( "UTF-8" ) ) );
	}
		
	public JsonObject castToJsonObject() throws ClassCastException {
		return (JsonObject) this;
	}
	
	public JsonArray castToJsonArray() throws ClassCastException {
		return (JsonArray) this;
	}
	
	public JsonString castToJsonString() throws ClassCastException {
		return (JsonString) this;
	}
	
	public JsonNumber castToJsonNumber() throws ClassCastException {
		return (JsonNumber) this;
	}
	
	public JsonBoolean castToJsonBoolean() throws ClassCastException {
		return (JsonBoolean) this;
	}
	

}
