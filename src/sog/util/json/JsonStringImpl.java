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

import sog.core.Test;
import sog.util.json.JSON.JsonString;
import sog.util.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonStringImpl implements JsonString {
	
	private String javaValue;
	
	private String jsonValue;
	
	JsonStringImpl( String value ) {
		this.javaValue = value;
		// FIXME set the JSON value
		this.jsonValue = null;
	}
	
	@Override
	public String toJavaString() {
		return this.javaValue;
	}
	
	@Override
	public String toJsonString() {
		return this.jsonValue;
	}

	/*
	 * NOTE:
	 * This does not handle unicode escapes
	 */
	// FIXME: Needs an exhaustive set of test cases.
//	@Override
//	public String toJSON() {
//		StringBuilder buf = new StringBuilder().append( '"' );
//
//		int start = 0;
//		int current = 0;
//		char c = 0;
//		int length = this.value.length();
//		
//		while ( current < length ) {
//			c = this.value.charAt( current );
//			switch (c) {
//			case '\\':
//			case '"':
//			case '/':
//				buf.append( this.value, start, current ).append( '\\' );
//				start = current++;
//				break;
//			case '\b':
//				buf.append( this.value, start, current ).append( "\\b" );
//				start = ++current;
//				break;
//			case '\f':
//				buf.append( this.value, start, current ).append( "\\f" );
//				start = ++current;
//				break;
//			case '\n':
//				buf.append( this.value, start, current ).append( "\\n" );
//				start = ++current;
//				break;
//			case '\r':
//				buf.append( this.value, start, current ).append( "\\r" );
//				start = ++current;
//				break;
//			case '\t':
//				buf.append( this.value, start, current ).append( "\\t" );
//				start = ++current;
//				break;
//			default:
//				current++;
//			}
//		}
//		buf.append( this.value, start, length );
//		
//		return buf.append( '"' ).toString();
//	}

	@Override
	public JsonValue read( JsonReader reader ) throws IOException, JsonParseException {
		StringBuilder java = new StringBuilder();
		StringBuilder json = new StringBuilder();
		
		reader.skipWhiteSpace().consume( '"' );
		json.append( '"' );
		
		boolean escape = false;
		char cur;
		while ( reader.hasChar() ) {
			cur = reader.curChar();
			json.append( cur );
			if ( escape ) {
				java.append( this.getEscape( cur, reader.getColumn() ) );
				escape = false;
			} else if ( cur == '\\' ) {
				escape = true;
			} else if ( cur == '"' ) {
				break;
			} else {
				java.append( cur );
			}
		}

		this.javaValue = java.toString();
		this.jsonValue = json.toString();
		return this;
	}
//	public JsonString readString() throws IOException, JsonParseException {
//		this.skipWhiteSpace();
//		this.consume( '"' );
//		
//		StringBuilder buf = new StringBuilder();
//		boolean escape = false;
//		char cur;
//		while ( this.hasChar() ) {
//			cur = this.curChar();
//			if ( escape ) {
//				buf.append( this.getEscape( cur ) );
//				escape = false;
//			} else if ( cur == '\\' ) {
//				escape = true;
//			} else if ( cur == '"' ) {
//				break;
//			} else {
//				buf.append( cur );
//			}
//			this.advance();
//		}
//		
//		this.consume( '"' );
//		return JSON.str( buf.toString() );
//	}
	
	private char getEscape( char c, int location ) throws JsonParseException {
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
			throw new JsonParseException( "Illegal escape character: " + c, location );
		}
	}
	
	

	@Override
	public void write( BufferedWriter writer ) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
