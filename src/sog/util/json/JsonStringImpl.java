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

import sog.core.Test;
import sog.util.json.JSON.JsonString;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonStringImpl implements JsonString {
	
	static JsonString forJavaValue( String javaValue ) {
		return new JsonStringImpl( javaValue, javaToJson( javaValue ) );
	}
	
	private final String javaValue;
	
	private final String jsonValue;
	
	JsonStringImpl( String javaValue, String jsonValue ) {
		this.javaValue = javaValue;
		this.jsonValue = jsonValue;
	}

	@Override
	public String toString() {
		return this.jsonValue;
	}
	
	@Override
	public String toJavaString() {
		return this.javaValue;
	}

	/*
	 * NOTE:
	 * This does not escape unicode characters.
	 */
	private static String javaToJson( String javaValue ) {
		StringBuilder buf = new StringBuilder().append( '"' );

		int start = 0;
		int current = 0;
		char c = 0;
		int length = javaValue.length();
		
		while ( current < length ) {
			c = javaValue.charAt( current );
			switch (c) {
			case '\\':
			case '"':
			case '/':
				buf.append( javaValue, start, current ).append( '\\' );
				start = current++;
				break;
			case '\b':
				buf.append( javaValue, start, current ).append( "\\b" );
				start = ++current;
				break;
			case '\f':
				buf.append( javaValue, start, current ).append( "\\f" );
				start = ++current;
				break;
			case '\n':
				buf.append( javaValue, start, current ).append( "\\n" );
				start = ++current;
				break;
			case '\r':
				buf.append( javaValue, start, current ).append( "\\r" );
				start = ++current;
				break;
			case '\t':
				buf.append( javaValue, start, current ).append( "\\t" );
				start = ++current;
				break;
			default:
				current++;
			}
		}
		buf.append( javaValue, start, length );
		
		return buf.append( '"' ).toString();
	}

	@Override
	public void write( JsonWriter writer ) throws IOException {
		writer.writeString( this );
	}

	@Override
	public int compareTo( JsonString other ) {
		return this.toJavaString().compareTo( other.toJavaString() );
	}

	
}
