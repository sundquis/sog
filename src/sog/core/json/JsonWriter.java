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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map.Entry;

import sog.core.Test;
import sog.core.json.JSON.JsonArray;
import sog.core.json.JSON.JsonBoolean;
import sog.core.json.JSON.JsonNumber;
import sog.core.json.JSON.JsonObject;
import sog.core.json.JSON.JsonString;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonWriter extends PrimitiveWriter {

	
	public JsonWriter( BufferedWriter buf ) {
		super( buf );
	}
	
	public JsonWriter( Writer writer ) {
		super( writer );
	}
	
	public JsonWriter( OutputStream out ) {
		super( out );
	}

	
	public void writeValue( JsonValue value ) throws IOException {
		switch ( value ) {
			case JSON.JsonNull x -> this.writeJsonNull();
			case JSON.JsonObject x -> this.writeJsonObject( x );
			case JSON.JsonArray x -> this.writeJsonArray( x );
			case JSON.JsonString x -> this.writeJsonString( x );
			case JSON.JsonNumber x -> this.writeJsonNumber( x );
			case JSON.JsonBoolean x -> this.writeJsonBoolean( x );
		}
	}
	

	public void writeJsonObject( JsonObject object ) throws IOException {
		this.append( '{' );
		
		boolean first = true;
		for ( Entry<String, JsonValue> entry : object.toJavaMap().entrySet() ) {
			if ( first ) {
				first = false;
			} else {
				this.append( ',' );
			}
			this.writeString( entry.getKey() );
			this.append( ':' );
			this.writeValue( entry.getValue() );
		}
		
		this.append( '}' );
	}

	public void writeJsonArray( JsonArray array ) throws IOException {
		this.append( '[' );
		
		boolean first = true;
		for ( JsonValue value : array.toJavaList() ) {
			if ( first ) {
				first = false;
			} else {
				this.append( ',' );
			}
			this.writeValue( value );
		}
		
		this.append( ']' );
	}
	

	public void writeJsonString( JsonString string ) throws IOException {
		this.writeString( string.toJavaString() );
	}


	public void writeJsonNumber( JsonNumber number ) throws IOException {
		this.writeNumber( number.toJavaBigDecimal() );
	}

	
	public void writeJsonBoolean( JsonBoolean bool ) throws IOException {
		this.writeBoolean( bool.toJavaBoolean() );
	}
	
	
	public void writeJsonNull() throws IOException {
		this.writeNull();
	}
	
}
