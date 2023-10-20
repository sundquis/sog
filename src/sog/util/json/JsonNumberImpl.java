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
import sog.util.json.JSON.JsonNumber;
import sog.util.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonNumberImpl implements JsonNumber {
	
	private final int integer;
	private final int fraction;
	private final int exponent;

	JsonNumberImpl( int integer, int fraction, int exponent ) {
		this.integer = integer;
		this.fraction = fraction;
		this.exponent = exponent;
	}

//	@Override
//	public String toJSON() {
//		return "" + this.integer 
//			+ (this.fraction > 0 ? "." + this.fraction : "") 
//			+ (this.exponent == 0 ? "" : "E" + this.exponent );
//	}

	@Override
	public Integer toJavaInteger() {
		return this.integer;
	}

	@Override
	public Float toJavaFloat() {
		return Float.parseFloat( "FIXME" );
	}

	@Override
	public Double toJavaDouble() {
		return Double.parseDouble( "FIXME" );
	}

	@Override
	public Number toJavaNumber() {
		return this.fraction == 0 ? this.toJavaInteger()
			: this.exponent == 0 ? this.toJavaFloat() : this.toJavaDouble();
	}

	@Override
	public JsonValue read( JsonReader reader ) throws IOException, JsonParseException {
		// TODO Auto-generated method stub
		return this;
	}
//	public JsonNumber readNumber() throws IOException, JsonParseException {
//		this.skipWhiteSpace();
//
//		int integer;
//		if ( this.curChar() == '-' ) {
//			integer = -1;
//		} else {
//			integer = 1;
//		}
//		integer *= this.readDigits();
//		
//		int fraction = 0;
//		if ( this.curChar() == '.' ) {
//			fraction = this.readDigits();
//		}
//		
//		int exponent = 0;
//		
//		
//		
//		return JSON.exp( integer, fraction, exponent );
//	}
//	
//	private int readDigits() throws IOException, JsonParseException {
//		StringBuilder buf = new StringBuilder();
//		return 0;
//	}

	@Override
	public void write( BufferedWriter writer ) throws IOException {
		// TODO Auto-generated method stub
		
	}


}
