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
import java.io.IOException;

import sog.core.Test;
import sog.util.json.JSON.JsonValue;

/**
 * When reading elements, always start by skipping whitespace, but allow trailing whitespace.
 */
@Test.Subject( "test." )
public class JsonReader implements AutoCloseable {
	
	private final BufferedReader buf;
	
	private int current = -1;
	
	private int charCount = 0;
	
	public JsonReader( BufferedReader buf ) throws IOException {
		this.buf = buf;
		this.advance();
	}
	
	public boolean hasChar() {
		return this.current != -1;
	}
	
	public char curChar() {
		return (char) this.current;
	}
	
	public void advance() throws IOException {
		this.current = this.buf.read();
		this.charCount++;
	}
	
	public int getColumn() {
		return this.charCount;
	}
	
	@Override
	public void close() throws IOException {
		this.buf.close();
	}
	
	public void consume( char c ) throws IOException, JsonParseException {
		if ( c == this.curChar() ) {
			this.advance();
		} else {
			throw new JsonParseException( "Expected '" + c + "' but got '" + this.curChar() + "'", this.charCount );
		}
	}
	
	public void consume( String s ) throws IOException, JsonParseException {
		for ( int i = 0; i < s.length(); i++ ) {
			this.consume( s.charAt( i ) );
		}
	}
	
	public JsonReader skipWhiteSpace() throws IOException {
		while ( this.hasChar() ) {
			char c = this.curChar();
			switch (c) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				this.advance();
				break;
			default:
				break;
			}
		}
		return this;
	}
	
	private static final char EOF = (char)-1;
	
	/*
	 * If the stream is empty at the point where we expect to read a value, we return JSON null.
	 */
	public JsonValue readValue() throws IOException, JsonParseException {
		char c = this.skipWhiteSpace().curChar();
		switch (c) {
		case '{': return JSON.obj().read( this );
		case '[': return JSON.arr().read( this );
		case '"': return JSON.str( "" ).read( this );
		case 't': return JSON.TRUE.read( this );
		case 'f': return JSON.FALSE.read( this );
		case 'n': return JSON.NULL.read( this );
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
		case '9': return JSON.num( 0 ).read( this );
		case EOF: return JSON.NULL;  // Not sure about this.
		default: throw new JsonParseException( "Illegal character: " + c, this.charCount );
		}
	}

}
