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
import java.math.BigDecimal;

import sog.core.Test;
import sog.util.json.JSON.JsonNumber;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonNumberImpl implements JsonNumber {
	
	private final BigDecimal javaValue;
	
	private final String jsonValue;
	
	JsonNumberImpl( String jsonValue ) {
		this.javaValue = new BigDecimal( jsonValue );
		this.jsonValue = jsonValue;
	}

	JsonNumberImpl( int integer, int fraction, int exponent ) {
		this( "" + integer + (fraction > 0 ? "." + fraction : "") + (exponent != 0 ? "E" + exponent : "") );
	}

	@Override
	public String toString() {
		return this.jsonValue;
	}

	@Override
	public Integer toJavaInteger() throws ArithmeticException {
		return this.javaValue.intValueExact();
	}

	@Override
	public Double toJavaDouble() throws ArithmeticException {
		Double result = this.javaValue.doubleValue();
		if ( result == Double.NEGATIVE_INFINITY || result == Double.POSITIVE_INFINITY ) {
			throw new ArithmeticException( "Magnitude too large");
		}
		return result;
	}

	@Override
	public BigDecimal toJavaBigDecimal() {
		return this.javaValue;
	}


	@Override
	public void write( JsonWriter writer ) throws IOException {
		writer.writeNumber( this );
	}

	@Override
	public int compareTo( JsonNumber other ) {
		return this.toJavaBigDecimal().compareTo( other.toJavaBigDecimal() );
	}


}
