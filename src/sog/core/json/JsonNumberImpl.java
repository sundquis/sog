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

import java.math.BigDecimal;

import sog.core.Test;
import sog.core.json.JSON.JsonNumber;

/**
 * 
 */
@Test.Subject( "test." )
public final class JsonNumberImpl implements JsonNumber {
	
	private final BigDecimal javaValue;
	
	JsonNumberImpl( String jsonValue ) {
		this.javaValue = new BigDecimal( jsonValue );
	}

	JsonNumberImpl( int integer ) {
		this.javaValue = new BigDecimal( integer );
	}
	
	JsonNumberImpl( double num ) {
		this.javaValue = new BigDecimal( num );
	}
	
	JsonNumberImpl( BigDecimal javaValue ) {
		this.javaValue = javaValue;
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
	public String toString() {
		return this.javaValue.toString();
	}
	
	@Override
	public int compareTo( JsonNumber other ) {
		return this.toJavaBigDecimal().compareTo( other.toJavaBigDecimal() );
	}

	@Override
	public int hashCode() {
		return this.javaValue.hashCode();
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( other instanceof JsonNumber num ) {
			return this.toJavaBigDecimal().equals( num.toJavaBigDecimal() );
		} else {
			return false;
		}
	}
	
//	public static void main( String[] args ) {
//		JsonNumber[] nums = new JsonNumber[] {
//			JSON.num( 42 ),
//			JSON.num( -42 ),
//			JSON.dec( 42.0 ),
//			JSON.dec( 42.35 ),
//			JSON.dec( -42.6 ),
//			JSON.dec( -42.0 ),
//			JSON.dec( 0.0 ),
//			JSON.dec( 0.2 ),
//			JSON.dec( -0.000000001 ),
//			JSON.dec( 0.0000000000000000000000000000000000000001 ),
//			JSON.dec( 12743574398759843754370598093875093838957298742598276871.1942878974 ),
//			JSON.big( new BigDecimal( "12476287364287.83297498274298E-5324" ) )
//		};
//		
//		Arrays.stream( nums ).forEach( App.get()::msg );
//	}
	

}
