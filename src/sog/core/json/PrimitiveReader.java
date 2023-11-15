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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class PrimitiveReader implements AutoCloseable {
	
	private final BufferedReader buf;
	
	private int current = -1;
	
	private int charCount = 0;
	
	public PrimitiveReader( BufferedReader buf ) throws IOException {
		this.buf = buf;
		this.advance();
	}
	
	public PrimitiveReader( Reader reader ) throws IOException {
		this( new BufferedReader( reader ) );
	}
	
	public PrimitiveReader( InputStream input ) throws IOException {
		this( new InputStreamReader( input, Charset.forName( "UTF-8" ) ) );
	}
	
	public PrimitiveReader( Path path ) throws IOException {
		this( Files.newBufferedReader( path ) );
	}
	
	public PrimitiveReader( String jsonValue ) throws IOException {
		this( new StringReader( Assert.nonNull( jsonValue ) ) );
	}
	
	
	
	@Override
	public void close() throws IOException {
		this.buf.close();
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
	
	public PrimitiveReader consume( char c ) throws IOException, JsonException {
		if ( c == this.curChar() ) {
			this.advance();
		} else {
			throw new JsonException( "Expected '" + c + "' but got '" + this.curChar() + "'", this.charCount );
		}
		return this;
	}
	
	public void consume( String s ) throws IOException, JsonException {
		for ( int i = 0; i < s.length(); i++ ) {
			this.consume( s.charAt( i ) );
		}
	}
	
	public PrimitiveReader skipWhiteSpace() throws IOException {
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
					return this;
			}
		}
		return this;
	}
	
	public String readString() throws IOException, JsonException {
		StringBuilder java = new StringBuilder();
		
		this.skipWhiteSpace().consume( '"' );
		
		boolean escape = false;
		char cur;
		while ( this.hasChar() ) {
			cur = this.curChar();
			if ( escape ) {
				java.append( this.getEscape( cur ) );
				escape = false;
			} else if ( cur == '\\' ) {
				escape = true;
			} else if ( cur == '"' ) {
				break;
			} else {
				java.append( cur );
			}
			this.advance();
		}
		this.consume( '"' );
		
		return java.toString();
	}
	
	private char getEscape( char c ) throws JsonException {
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
				throw new JsonException( "Illegal escape character: " + c, this.getColumn() );
		}
	}

	public BigDecimal readNumber() throws IOException, JsonException {
		StringBuilder buf = new StringBuilder();
		
		this.readInteger( buf );
		this.readFraction( buf );
		this.readExponent( buf );
		
		return new BigDecimal( buf.toString() );
	}
	
	private void readInteger( StringBuilder buf ) throws IOException, JsonException {
		if ( this.skipWhiteSpace().curChar() == '-' ) {
			buf.append( '-' );
			this.consume( '-' );
		}
		
		if ( this.curChar() == '0' ) {
			buf.append( '0' );
			this.consume( '0' );
			return;
		}
		
		this.readOneNine( buf );
		this.readDigits( buf );
	}
	
	private void readOneNine( StringBuilder buf ) throws IOException, JsonException {
		char c = this.curChar();
		switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				buf.append( c );
				this.advance();
				break;
			default: 
				throw new JsonException( "Non-zero digit expected", this.getColumn() );
		}
	}
	
	private void readDigit( StringBuilder buf ) throws IOException, JsonException {
		char c = this.curChar();
		switch (c) {
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
				buf.append( c );
				this.advance();
				break;
			default: 
				throw new JsonException( "Digit expected", this.getColumn() );
		}
	}
	
	/*
	 * Note: In https://www.json.org/json-en.html, "digits" refers to one or more digits.
	 * This implementation is for zero or more digits.
	 */
	private void readDigits( StringBuilder buf ) throws IOException, JsonException {
		while ( this.hasChar() ) {
			char c = this.curChar();
			switch (c) {
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
					buf.append( c );
					this.advance();
					break;
				default: return;
			}
		}
	}
	
	private void readFraction( StringBuilder buf ) throws IOException, JsonException {
		if ( this.curChar() == '.' ) {
			buf.append( '.' );
			this.advance();
			this.readDigit( buf );
			this.readDigits( buf );
		}
	}
	
	private void readExponent( StringBuilder buf ) throws IOException, JsonException {
		if ( this.curChar() == 'E' || this.curChar() == 'e' ) {
			buf.append( this.curChar() );
			this.advance();
			
			if ( this.curChar() == '+' || this.curChar() == '-' ) {
				buf.append( this.curChar() );
				this.advance();
			}

			this.readDigit( buf );
			this.readDigits( buf );
		}
	}

	public Boolean readBoolean() throws IOException, JsonException {
		char c = this.skipWhiteSpace().curChar();
		switch (c) {
			case 't':
				this.consume( "true" );
				return Boolean.TRUE;
			case 'f':
				this.consume( "false" );
				return Boolean.FALSE;
			default:
				throw new JsonException( "Illegal character for boolean value", this.getColumn() );
		}
	}
	
	
	public void readNull() throws IOException, JsonException {
		this.skipWhiteSpace().consume( "null" );
	}
	

}
