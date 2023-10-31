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
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import sog.core.Assert;
import sog.core.Test;
import sog.util.json.JSON.JsonArray;
import sog.util.json.JSON.JsonBoolean;
import sog.util.json.JSON.JsonNumber;
import sog.util.json.JSON.JsonObject;
import sog.util.json.JSON.JsonString;
import sog.util.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonWriter implements AutoCloseable {
	
	private final BufferedWriter buf;
	
	public JsonWriter( BufferedWriter buf ) {
		this.buf = Assert.nonNull( buf );
	}
	
	public JsonWriter( Writer writer ) {
		this( new BufferedWriter( Assert.nonNull( writer ) ) );
	}
	
	public JsonWriter( OutputStream out ) {
		this( new OutputStreamWriter( out, Charset.forName( "UTF-8" ) ) );
	}
	
	public void writeValue( JsonValue value ) throws IOException {
		value.write( this );
	}
	
	public void writeObject( JsonObject object ) throws IOException {
		this.buf.append( '{' );
		boolean first = true;
		for ( Entry<JsonString, JsonValue> entry : object.getMembers().entrySet() ) {
			if ( first ) {
				first = false;
			} else {
				this.buf.append( ',' );
			}
			this.writeString( entry.getKey() );
			this.buf.append( ':' );
			this.writeValue( entry.getValue() );
		}
		this.buf.append( '}' );
	}

	public void writeArray( JsonArray array ) throws IOException {
		this.buf.append( '[' );
		boolean first = true;
		for ( JsonValue value : array.toJavaList() ) {
			if ( first ) {
				first = false;
			} else {
				this.buf.append( ',' );
			}
			this.writeValue( value );
		}
		this.buf.append( ']' );
	}

	public void writeString( JsonString string ) throws IOException {
		this.buf.append( string.toString() );
	}

	public void writeNumber( JsonNumber number ) throws IOException {
		this.buf.append( number.toString() );
	}

	public void writeBoolean( JsonBoolean bool ) throws IOException {
		this.buf.append( bool.toString() );
	}
	
	public void writeNull() throws IOException {
		this.buf.append( JSON.NULL.toString() );
	}
	
	public void flush() throws IOException {
		this.buf.flush();
	}

	@Override
	public void close() throws IOException {
		this.buf.flush();
		this.buf.close();
	}
	
	
}
