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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;
import sog.util.json.JSON.JsonArray;
import sog.util.json.JSON.JsonBoolean;
import sog.util.json.JSON.JsonNull;
import sog.util.json.JSON.JsonNumber;
import sog.util.json.JSON.JsonObject;
import sog.util.json.JSON.JsonString;
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
	
	public JsonReader( Reader reader ) throws IOException {
		this( new BufferedReader( reader ) );
	}
	
	public JsonReader( InputStream input ) throws IOException {
		this( new InputStreamReader( input, Charset.forName( "UTF-8" ) ) );
	}
	
	public JsonReader( String jsonValue ) throws IOException {
		this( new StringReader( Assert.nonNull( jsonValue ) ) );
	}
	
	private boolean hasChar() {
		return this.current != -1;
	}
	
	private char curChar() {
		return (char) this.current;
	}
	
	private void advance() throws IOException {
		this.current = this.buf.read();
		this.charCount++;
	}
	
	private int getColumn() {
		return this.charCount;
	}
	
	@Override
	public void close() throws IOException {
		this.buf.close();
	}
	
	private JsonReader consume( char c ) throws IOException, JsonParseException {
		if ( c == this.curChar() ) {
			this.advance();
		} else {
			throw new JsonParseException( "Expected '" + c + "' but got '" + this.curChar() + "'", this.charCount );
		}
		return this;
	}
	
	private void consume( String s ) throws IOException, JsonParseException {
		for ( int i = 0; i < s.length(); i++ ) {
			this.consume( s.charAt( i ) );
		}
	}
	
	private JsonReader skipWhiteSpace() throws IOException {
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
	
	private static final char EOF = (char)-1;
	
	/*
	 * NOTE: This is non-standard:
	 * If the stream is empty at the point where we expect to read a value, we return JSON null.
	 */
	public JsonValue readValue() throws IOException, JsonParseException {
		char c = this.skipWhiteSpace().curChar();
		switch (c) {
		case '{': return this.readObject();
		case '[': return this.readArray();
		case '"': return this.readString();
		case 't': return this.readBoolean();
		case 'f': return this.readBoolean();
		case 'n': return this.readNull();
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
		case '9': return this.readNumber();
		case EOF: return JSON.NULL;  // Not sure about this.
		default: throw new JsonParseException( "Illegal character: " + c, this.charCount );
		}
	}
	
	public JsonObject readObject() throws IOException, JsonParseException {
		this.skipWhiteSpace().consume( '{' );
		JsonObject result = new JsonObjectImpl();

		if ( this.skipWhiteSpace().curChar() != '}' ) {
			result.add( this.readString(), this.skipWhiteSpace().consume( ':' ).readValue() );
		}

		while ( this.skipWhiteSpace().curChar() != '}' ) {
			this.consume( ',' );
			result.add( this.readString(), this.skipWhiteSpace().consume( ':' ).readValue() );
		}
		
		this.consume( '}' );
		return result;
	}

	public JsonArray readArray() throws IOException, JsonParseException {
		this.skipWhiteSpace().consume( '[' );
		JsonArray result = new JsonArrayImpl();
		
		if ( this.skipWhiteSpace().curChar() != ']' ) {
			result.add( this.readValue() );
		}
		
		while ( this.skipWhiteSpace().curChar() != ']' ) {
			this.consume( ',' );
			result.add( this.readValue() );
		}

		this.consume( ']' );
		return result;
	}

	public JsonString readString() throws IOException, JsonParseException {
		StringBuilder java = new StringBuilder();
		StringBuilder json = new StringBuilder();
		
		this.skipWhiteSpace().consume( '"' );
		json.append( '"' );
		
		boolean escape = false;
		char cur;
		while ( this.hasChar() ) {
			cur = this.curChar();
			json.append( cur );
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

		return new JsonStringImpl( java.toString(), json.toString() );
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
			throw new JsonParseException( "Illegal escape character: " + c, this.getColumn() );
		}
	}

	
	public JsonNumber readNumber() throws IOException, JsonParseException {
		StringBuilder buf = new StringBuilder();
		
		this.readInteger( buf );
		this.readFraction( buf );
		this.readExponent( buf );
		
		return new JsonNumberImpl( buf.toString() );
	}
	
	private void readInteger( StringBuilder buf ) throws IOException, JsonParseException {
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
	
	private void readOneNine( StringBuilder buf ) throws IOException, JsonParseException {
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
			throw new JsonParseException( "Non-zero digit expected", this.getColumn() );
		}
	}
	
	private void readDigit( StringBuilder buf ) throws IOException, JsonParseException {
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
			throw new JsonParseException( "Digit expected", this.getColumn() );
		}
	}
	
	/*
	 * Note: In https://www.json.org/json-en.html, "digits" refers to one or more digits.
	 * This implementation is for zero or more digits.
	 */
	private void readDigits( StringBuilder buf ) throws IOException, JsonParseException {
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
	
	private void readFraction( StringBuilder buf ) throws IOException, JsonParseException {
		if ( this.curChar() == '.' ) {
			buf.append( '.' );
			this.advance();
			this.readDigit( buf );
			this.readDigits( buf );
		}
	}
	
	private void readExponent( StringBuilder buf ) throws IOException, JsonParseException {
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

	
	public JsonBoolean readBoolean() throws IOException, JsonParseException {
		char c = this.skipWhiteSpace().curChar();
		switch (c) {
		case 't':
			this.consume( "true" );
			return JSON.TRUE;
		case 'f':
			this.consume( "false" );
			return JSON.FALSE;
		default:
			throw new JsonParseException( "Illegal character for boolean value", this.getColumn() );
		}
	}
	
	
	public JsonNull readNull() throws IOException, JsonParseException {
		this.skipWhiteSpace().consume( "null" );
		return JSON.NULL;
	}
	

	private static Consumer<String> nullTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: [" + s + "]" );
			JsonNull json = in.readNull();
			App.get().msg( "JSON: " + json.toString() );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] nullTestCases = {
		"null", "", "nu ll", "     null", "null       ", null, "\tnull", "Null", "123", "\rnull", "\nnull", "\"null\""
	};
	
	private static Consumer<String> booleanTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: [" + s + "]" );
			//JsonBoolean json = in.readBoolean();
			JsonBoolean json = in.readValue().castToJsonBoolean();
			App.get().msg( "JSON: " + json.toString() );
			App.get().msg( "JAVA: " + json.toJavaBoolean() );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] booleanTestCases = {
		"true", "false", 
		"     true", "  false", 
		"true     ", "false    ", 
		"\t\r \n true", "  false\t", 
		"True", "fal se", 
		null, "null", 
		"12", "\"false\"", 
	};
	
	private static Consumer<String> stringTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: [" + s + "]" );
			JsonString json = in.readString();
			//JsonString json = in.readValue().castToJsonString();
			App.get().msg( "JSON: [" + json.toString() + "]" );
			App.get().msg( "JAVA: [" + json.toJavaString() + "]" );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] stringTestCases = {
		"\"A perfectly fine JSON string\"",	
		"\"\"",	
		"\"A string with \" quote inside\"",	
		"\"A string with escaped \\\" quote inside\"",	
		"\"A string with \\ escape inside\"",	
		"\"A string with escaped \\\\ escape inside\"",	
		"\"A string with / solidus inside\"",	
		"\"A string with escaped \\/ solidus inside\"",	
		"\"A string with backspace \b inside\"",	
		"\"A string with escaped backspace \\b inside\"",	
		"\"A string with formfeed \f inside\"",	
		"\"A string with escaped formfeed \\f inside\"",	
		"\"A string with linefeed \n inside\"",	
		"\"A string with escaped linefeed \\n inside\"",	
		"\"A string with carriage return \r inside\"",	
		"\"A string with escaped carriage return \\r inside\"",	
		"\"A string with horizontal tab \t inside\"",	
		"\"A string with escaped horizontal tab \\t inside\"",	
		"\"A string with unicode \u42AB inside\"",	
		"\"A string with escaped unicode \\\u42AB inside\"",
		"\"Consecutive tabs: \t\t inside\"",
		"\"Consecutive escaped tabs: \\t\\t inside\"",
		"Not a JSON string",
		"\"Not a JSON string",
		"Not a JSON string\"",
		null,
	};
	
	private static Consumer<String> integerTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: [" + s + "]" );
			JsonNumber json = in.readNumber();
			//JsonNumber json = in.readValue().castToJsonNumber();
			App.get().msg( "JSON: [" + json.toString() + "]" );
			App.get().msg( "BIG: [" + json.toJavaBigDecimal() + "]" );
			App.get().msg( "INT: [" + json.toJavaInteger() + "]" );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] goodIntegerTestCases = {
		"1", "0", "42", "-1", "-0", "1234567890", "" + Integer.MAX_VALUE, "" + Integer.MIN_VALUE,
		"1.0", "12.3E2", "42000E-3", "420000.0e-4", "-123E0", "-34e00", "450e0001", "67.0000"
	};
	
	private static String[] badIntegerTestCases = {
			".1", "00", "42.00000000001", "12345678900", "-12345678900",
			"1.1", "12.34E1", "42000E-4", "420000.0e-5", "42AF", "+1", "- 1",
			"", "123E40", "1e-1.1", "-1E40", "-1e-1", "420000000000e-12", "1e-4000"
		};
		
	private static Consumer<String> doubleTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: [" + s + "]" );
			//JsonNumber json = in.readNumber();
			JsonNumber json = in.readValue().castToJsonNumber();
			App.get().msg( "JSON: [" + json.toString() + "]" );
			App.get().msg( "BIGDEC: [" + json.toJavaBigDecimal() + "]" );
			App.get().msg( "DOUBLE: [" + json.toJavaDouble() + "]" );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] goodDoubleTestCases = {
		"1", "0", "42", "-1", "-0", "1234567890", "" + Integer.MAX_VALUE, "" + Integer.MIN_VALUE,
		"1.0", "12.3E2", "42000E-3", "420000.0e-4", "-123E0", "-34e00", "450e0001", "67.0000",
		"0.05", "-0.1234567890", 
		"1.0000000000000000000000000000000001", "-3.141595879843769874e5", "420000000000e-12", 
		"" + Double.MAX_VALUE, "" + Double.MIN_VALUE, "1e42", "1E-42", "-1E+42", "-1e-42"
	};
		
	private static String[] badDoubleTestCases = {
		"+1", "- 1", "", "123E400", "-1E400", "-1e-400", "1e-4000", "x0.9", "10.01E--1",
	};
	
	private static Consumer<String> arrayTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: " + s );
			//JsonArray json = in.readArray();
			JsonArray json = in.readValue().castToJsonArray();
			App.get().msg( "JSON: " + JSON.toString( json ) );
			App.get().msg( "ELEMENT TYPES: " );
			json.toJavaList().stream().map( Object::getClass ).map( Class::getName )
				.map( n -> "\t" + n ).forEach( System.out::println );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	public static String[] arrayTestCases = {
		"[]",
		"[1, 2, 3]",
		"[1, \"A\", [], \"foo\", [1, 2, 3, 4], null, 0.1234567, \"Hello\\tWorld\", 42E42]",
		"[{}, [{}], {\"arr\": []}, []]",
		"[  null, \t false, \ntrue   ]",
		"[ [ ] , [[]], [[], []], { \"key_1\" : \"value_1\", \"\" : null }]",
		"[ 1, 2, 3, 4, 5, 6, \"...\", 42 ]", 
	};
	
	private static Consumer<String> objectTest = (s) -> {
		try (
			JsonReader in = new JsonReader( s );
		) {
			App.get().msg();
			App.get().msg( "CASE: " + s );
			JsonObject json = in.readObject();
			//JsonObject json = in.readValue().castToJsonObject();
			App.get().msg( "JSON: " + JSON.toString( json ) );
			App.get().msg( "MEMBERSS: " );
			json.toJavaMap().entrySet().stream()
				.map( e -> "    " + e.getKey().toString() + " -> " + e.getValue().toString() ).forEach( System.out::println );
		} catch ( Throwable ex ) {
			App.get().msg( "ERROR: " + ex.toString() );
			App.get().getLocation( ex ).map( (l) -> "\t" + l ).forEach( System.out::println );
		}
	};
	
	private static String[] objectTestCases = {
		"{}",
		"{ \"arr\": [], \"obj\": {}, \"string\": \"x\", \"number\": 123.456, \"true\":true, \"false\": false, \"&null\" : null}",
		
	};
	
	public static void main( String[] args ) {
		App.get().msg( "TEST CASES:" );

		//Arrays.stream( stringTestCases ).forEach( stringTest );

		try {
			//"quote \", esc \\, tab \\t, consecutive \\n\\nDone!"
			StringBuilder buf = new StringBuilder();
			buf.append( "Quote " ).append( '"' )
				.append( " Escape " ).append( '\\' )
				.append( " Tab " ).append( '\\' ).append( '\t' )
				.append( " Consecutive newlines " ).append( '\n' ).append( '\n' )
				.append( " Done!" );
			JsonValue value = JSON.obj().add( "Nul", JSON.NULL ).add( "True", JSON.TRUE )
					.add( "False", JSON.FALSE ).add( "Integer", JSON.num( 1234567890 ) )
					.add( "Decimal", JSON.dec( 123, 456 ) ).add( "Exponential", JSON.exp( 123, 456, 789 ) )
					.add( "Array", JSON.arr().add( JSON.TRUE ).add( JSON.FALSE ) )
					.add( "Empty-Object", JSON.obj() )
					.add( "Wild String", JSON.str( buf.toString() ) );
				String rep = JSON.toString( value );
				JsonValue value2 = JSON.fromString( rep );
				App.get().msg( "REP1: " + rep );
				App.get().msg( "REP2: " + JSON.toString( value2 ) );
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		App.get().done();
	}


}
