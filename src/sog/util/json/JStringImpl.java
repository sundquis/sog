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

import java.util.Arrays;

import sog.core.App;
import sog.core.Test;
import sog.util.json.JSON.JArray;
import sog.util.json.JSON.JBoolean;
import sog.util.json.JSON.JNumber;
import sog.util.json.JSON.JObject;
import sog.util.json.JSON.JString;

/**
 * 
 */
@Test.Subject( "test." )
public class JStringImpl implements JString {
	
	private final String value;
	
	JStringImpl( String value ) {
		this.value = value;
	}
	
	/*
	 * NOTE:
	 * This does not handle unicode escapes
	 */
	// FIXME: Needs an exhaustive set of test cases.
	@Override
	public String toJSON() {
		StringBuilder buf = new StringBuilder().append( '"' );

		int start = 0;
		int current = 0;
		char c = 0;
		int length = this.value.length();
		
		while ( current < length ) {
			c = this.value.charAt( current );
			switch (c) {
			case '\\':
			case '"':
			case '/':
				buf.append( this.value, start, current ).append( '\\' );
				start = current++;
				break;
			case '\b':
				buf.append( this.value, start, current ).append( "\\b" );
				start = ++current;
				break;
			case '\f':
				buf.append( this.value, start, current ).append( "\\f" );
				start = ++current;
				break;
			case '\n':
				buf.append( this.value, start, current ).append( "\\n" );
				start = ++current;
				break;
			case '\r':
				buf.append( this.value, start, current ).append( "\\r" );
				start = ++current;
				break;
			case '\t':
				buf.append( this.value, start, current ).append( "\\t" );
				start = ++current;
				break;
			default:
				current++;
			}
		}
		buf.append( this.value, start, length );
		
		return buf.append( '"' ).toString();
	}

	@Override
	public String toJavaString() {
		return this.value;
	}

	@Override
	public JObject toJObject() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON String", "JSON Object" );
	}

	@Override
	public JArray toJArray() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON String", "JSON Array" );
	}

	@Override
	public JString toJString() throws JsonIllegalCast {
		return this;
	}

	@Override
	public JNumber toJNumber() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON String", "JSON Number" );
	}

	@Override
	public JBoolean toJBoolean() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON String", "JSON Boolean" );
	}

	
	
	
	public static void main( String[] args ) {
		Arrays.stream( args ).forEach( JStringImpl::print );
		//JString js = JSON.str( null );
		//App.get().msg( "Java: " + js.toJavaString() );
		//App.get().msg( "JSON: " + js.toJSON() );
	}
	
	// For testing
	private static void print( String s ) {
		JString js = JSON.str( s );
		App.get().msg( "Java: " + js.toJavaString() );
		App.get().msg( "JSON: " + js.toJSON() );
	}
	
}
