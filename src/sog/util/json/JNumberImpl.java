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
public class JNumberImpl implements JNumber {
	
	private final int integer;
	private final int fraction;
	private final int exponent;

	JNumberImpl( int integer, int fraction, int exponent ) {
		this.integer = integer;
		this.fraction = fraction;
		this.exponent = exponent;
	}

	@Override
	public String toJSON() {
		return "" + this.integer 
			+ (this.fraction > 0 ? "." + this.fraction : "") 
			+ (this.exponent == 0 ? "" : "E" + this.exponent );
	}

	@Override
	public Integer toJavaInteger() {
		return this.integer;
	}

	@Override
	public Float toJavaFloat() {
		return Float.parseFloat( this.toJSON() );
	}

	@Override
	public Double toJavaDouble() {
		return Double.parseDouble( this.toJSON() );
	}

	@Override
	public Number toJavaNumber() {
		return this.fraction == 0 ? this.toJavaInteger()
			: this.exponent == 0 ? this.toJavaFloat() : this.toJavaDouble();
	}

	@Override
	public JObject toJObject() throws IllegalCast {
		throw new IllegalCast( "JSON Number", "JSON Object" );
	}

	@Override
	public JArray toJArray() throws IllegalCast {
		throw new IllegalCast( "JSON Number", "JSON Array" );
	}

	@Override
	public JString toJString() throws IllegalCast {
		throw new IllegalCast( "JSON Number", "JSON String" );
	}

	@Override
	public JNumber toJNumber() throws IllegalCast {
		return this;
	}

	@Override
	public JBoolean toJBoolean() throws IllegalCast {
		throw new IllegalCast( "JSON Number", "JSON Boolean" );
	}


}
