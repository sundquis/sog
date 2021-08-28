/**
 * Copyright (C) 2021
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

package test.sog.core;

import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class StringsTest extends Test.Container {
	
	public StringsTest() {
		super( Strings.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Empty array allowed" 
	)
	public void tm_014AFD1A7( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Enclosed in set brackets" 
		)
		public void tm_032E318B6( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Omitted elements are indicated" 
		)
		public void tm_01D3226E9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Sample cases for arrays of arrays" 
		)
		public void tm_00A1A7140( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Sample cases for arrays of collections" 
		)
		public void tm_037D4ADA5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Sample cases for arrays of primitive" 
		)
		public void tm_0D3F439B7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.arrayToString(Object[])", 
			description = "Throws assertion error on null arrays" 
		)
		public void tm_079C8F674( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Empty collection allowed" 
		)
		public void tm_03309717C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Enclosed in set braces" 
		)
		public void tm_0D635ACD1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Omitted elements are indicated" 
		)
		public void tm_09196966F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Sample cases for collections of arrays" 
		)
		public void tm_033792411( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Sample cases for collections of collections" 
		)
		public void tm_05C7A1574( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Sample cases for collections of primitive" 
		)
		public void tm_0171F4946( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.collectionToString(Collection)", 
			description = "Throws assertion error on null collections" 
		)
		public void tm_058F89F77( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For large negative width return value ends with the pad character" 
		)
		public void tm_0250365B8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For large negative width return value starts with given string" 
		)
		public void tm_09195CCD8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For large positive width return value ends with given string" 
		)
		public void tm_0AD535D43( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For large positive width return value starts with the pad character" 
		)
		public void tm_0899931BB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For negative width equal to opposite of length return is the given string" 
		)
		public void tm_0A05F0EBB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For positive width equal to length return is the given string" 
		)
		public void tm_028E3D867( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For small negative width given string starts with return value" 
		)
		public void tm_02C2F9B46( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "For small positive width given string ends with return value" 
		)
		public void tm_034C287F1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "Length of return is specified width" 
		)
		public void tm_098599BF4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "Return is empty for zero width" 
		)
		public void tm_03BD32501( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.justify(String, int, char)", 
			description = "Throws assertion error for null string" 
		)
		public void tm_02FFBBBED( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "For large width return value ends with the pad character" 
		)
		public void tm_02BA6292E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "For large width return value starts with given string" 
		)
		public void tm_05FA8AD22( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "For small width given string starts with return value" 
		)
		public void tm_02AEAFFA8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "For width equal to length return is the given string" 
		)
		public void tm_0874C1377( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "Length of return is specified width" 
		)
		public void tm_09D2235DB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "Return is empty for zero width" 
		)
		public void tm_079ADB6BA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.leftJustify(String, int, char)", 
			description = "Throws assertion error for null string" 
		)
		public void tm_0DC2984A6( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "For large width return value ends with given string" 
		)
		public void tm_0B6B1284C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "For large width return value starts with the pad character" 
		)
		public void tm_0E31B9A92( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "For small width given string ends with return value" 
		)
		public void tm_0EECAFD12( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "For width equal to length return is the given string" 
		)
		public void tm_0AB1ABBD4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "Length of return is specified width" 
		)
		public void tm_0201CF3DE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "Return is empty for zero width" 
		)
		public void tm_0971F02D7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.rightJustify(String, int, char)", 
			description = "Throws assertion error for null string" 
		)
		public void tm_0154F63C3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Identity for non quoted trimmed strings" 
		)
		public void tm_0A2E6B97C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Identity on empty" 
		)
		public void tm_083F3318A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Ignores unmatched quotes" 
		)
		public void tm_08C3E2725( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Is idempotent" 
		)
		public void tm_00FE2BCCB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Removes double quotes" 
		)
		public void tm_03BBF5911( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Removes nested quotes" 
		)
		public void tm_09294DEAB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Removes single quotes" 
		)
		public void tm_0D54AE67A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Result is trimmed" 
		)
		public void tm_0B5FC23B3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Sample cases" 
		)
		public void tm_0F3CE2A47( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.strip(String)", 
			description = "Throws assertion error for null string" 
		)
		public void tm_0E8E94264( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Does not start with a digit" 
		)
		public void tm_007645515( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Identity on empty" 
		)
		public void tm_0D272FEBD( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "If no alphabetic characters in input then return is empty" 
		)
		public void tm_01894C845( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Result contains no white space" 
		)
		public void tm_066FCF835( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Result contains only letters" 
		)
		public void tm_04B201E9E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Result is trimmed" 
		)
		public void tm_0047BF0E6( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Sample cases" 
		)
		public void tm_0A739A934( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Starts with uppercase" 
		)
		public void tm_000443D36( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Throws assertion error for null string" 
		)
		public void tm_0D2DFC311( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toCamelCase(String)", 
			description = "Underscore removed" 
		)
		public void tm_0E81810A1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toHex(int)", 
			description = "Hex digits have natural representation" 
		)
		public void tm_05D0EB955( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toHex(int)", 
			description = "Integer.MAX_VALUE is represented" 
		)
		public void tm_0892C0999( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toHex(int)", 
			description = "Result is non-empty" 
		)
		public void tm_018FDAE82( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toHex(int)", 
			description = "Throws Assertionrror for negative" 
		)
		public void tm_016987B62( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toString(Object)", 
			description = "Agrees with object to string for non array non collection" 
		)
		public void tm_0035E8C60( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toString(Object)", 
			description = "Identity on strings" 
		)
		public void tm_034B843B7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toString(Object)", 
			description = "Provides alternate string representation for arrays" 
		)
		public void tm_03422487D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toString(Object)", 
			description = "Provides alternate string representation for collections" 
		)
		public void tm_08432D488( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Strings.toString(Object)", 
			description = "String representation of null is null" 
		)
		public void tm_0BC98197D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Strings.class );
		//Test.evalPackage( Strings.class );
	}
}
