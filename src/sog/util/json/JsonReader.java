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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import sog.core.App;
import sog.core.Test;
import sog.util.json.JSON.JArray;
import sog.util.json.JSON.JBoolean;
import sog.util.json.JSON.JElement;
import sog.util.json.JSON.JNumber;
import sog.util.json.JSON.JObject;
import sog.util.json.JSON.JString;

/**
 * When reading elements, always start by skipping whitespace, but allow trailing whitespace.
 */
@Test.Subject( "test." )
public class JsonReader implements AutoCloseable {
	
	private final BufferedReader buf;
	
	private int current;
	
	private int charCount;
	
	JsonReader( BufferedReader buf ) {
		this.buf = buf;
	}
	
	public JElement readElement() throws IOException, JsonParseException {
		this.skipWhiteSpace();
		char c = this.peekChar();
		switch (c) {
		case '{': return this.readObject();
		case '[': return this.readArray();
		case '"': return this.readString();
		case 't':
		case 'f': return this.readBoolean();
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
		case '9':
			return this.readNumber();
		default: throw new JsonParseException( "Illegal character", this.charCount );
		}
	}
	
	public JObject readObject() throws IOException, JsonParseException {
		this.skipWhiteSpace();
		this.consume( '{' );
		JObject result = JSON.obj();
		
		this.skipWhiteSpace();
		while ( this.peekChar() == '"' ) {
			JString key = this.readString();
			this.skipWhiteSpace();
			this.consume( ':' );
			JElement value = this.readElement();
			result.add( key, value );
			this.skipWhiteSpace();
			if ( this.peekChar() == ',' ) {
				this.consume( ',' );
				this.skipWhiteSpace();
			}
		}

		this.consume( '}' );
		return result;
	}
	
	public JArray readArray() throws IOException, JsonParseException {
		this.skipWhiteSpace();
		this.consume( '[' );
		JArray result = JSON.arr();
		
		this.skipWhiteSpace();
		while ( this.peekChar() != ']' ) {
			JElement elt = this.readElement();
			result.add( elt );
			this.skipWhiteSpace();
			if ( this.peekChar() == ',' ) {
				this.consume( ',' );
				this.skipWhiteSpace();
			}
		}
		
		this.consume( ']' );
		return result;
	}
	
	public JString readString() throws IOException, JsonParseException {
		this.skipWhiteSpace();
		this.consume( '"' );
		
		StringBuilder buf = new StringBuilder();
		boolean escape = false;
		char cur;
		while ( this.hasChar() ) {
			cur = this.peekChar();
			if ( escape ) {
				buf.append( this.getEscape( cur ) );
				escape = false;
			} else if ( cur == '\\' ) {
				escape = true;
			} else if ( cur == '"' ) {
				break;
			} else {
				buf.append( cur );
			}
			this.nextChar();
		}
		
		this.consume( '"' );
		return JSON.str( buf.toString() );
	}

	public JBoolean readBoolean() throws IOException, JsonParseException {
		this.skipWhiteSpace();
		if ( this.peekChar() == 't' ) {
			this.consume( "true" );
			return JSON.TRUE;
		} else {
			this.consume( "false)" );
			return JSON.FALSE;
		}
	}
	
	public JNumber readNumber() throws IOException, JsonParseException {
		this.skipWhiteSpace();

		int integer;
		if ( this.peekChar() == '-' ) {
			integer = -1;
		} else {
			integer = 1;
		}
		integer *= this.readDigits();
		
		int fraction = 0;
		if ( this.peekChar() == '.' ) {
			fraction = this.readDigits();
		}
		
		int exponent = 0;
		
		
		
		return JSON.exp( integer, fraction, exponent );
	}
	
	private int readDigits() throws IOException, JsonParseException {
		StringBuilder buf = new StringBuilder();
		return 0;
	}
	
	@Override
	public void close() throws IOException {
		//in.close();
	}
	
	private char getEscape( char c ) throws JsonParseException {
		switch (c) {
		case '\\': return '\\';
		case '"': return '"';
		case '/': return '/';
		case 'b': return '\b';
		case 'f': return '\f';
		case 'n': return '\n';
		case 'r': return '\r';
		case 't': return '\t';
		default:
			throw new JsonParseException( "Illegal escape character: " + c, this.charCount );
		}
	}
	
	
	private void consume( char c ) throws IOException, JsonParseException {
		if ( c == this.peekChar() ) {
			this.nextChar();
		} else {
			throw new JsonParseException( "Expected '" + c + "' bt got '" + this.peekChar() + "'", this.charCount );
		}
	}
	
	private void consume( String s ) throws IOException, JsonParseException {
		for ( int i = 0; i < s.length(); i++ ) {
			this.consume( s.charAt( i ) );
		}
	}
	
	private void skipWhiteSpace() throws IOException {
		while ( this.hasChar() ) {
			char c = this.peekChar();
			switch (c) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				this.nextChar();
				break;
			default:
				return;
			}
		}
	}
	
	private boolean hasChar() {
		return this.current != -1;
	}
	
	private char peekChar() {
		return (char) this.current;
	}
	
	private char nextChar() throws IOException {
		this.current = this.buf.read();
		this.charCount++;
		return this.peekChar();
	}

	
}
