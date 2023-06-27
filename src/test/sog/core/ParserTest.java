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

package test.sog.core;

import java.util.List;

import sog.core.Objects;
import sog.core.Parser;
import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class ParserTest extends Test.Container {
	
	public ParserTest() {
		super( Parser.class );
	}
	

	private <T> void test( Test.Case tc, Parser<T> p, String s, T target ) {
		T value = p.fromString( s );
		if ( Objects.deepEquals( target, value ) ) {
			tc.assertPass();
		} else {
			tc.assertFail( "String = " + s + ", Target = " + Strings.toString( target ) + ", Value = " + Strings.toString( value ) );
		}
	}

	
	// TEST CASES
	
	@Test.Impl( 
		member = "field: Parser Parser.BOOLEAN", 
		description = "Correct for sample cases" 
	)
	public void tm_00B219A8E( Test.Case tc ) {
		this.test( tc, Parser.BOOLEAN, "true", true );
		this.test( tc, Parser.BOOLEAN, "TRUE", true );
		this.test( tc, Parser.BOOLEAN, "True", true );
		this.test( tc, Parser.BOOLEAN, "false", false );
		this.test( tc, Parser.BOOLEAN, "FALSE", false );
		this.test( tc, Parser.BOOLEAN, "False", false );
	}
				
	@Test.Impl( 
		member = "field: Parser Parser.BOOLEAN", 
		description = "Returns false for mal-formed string" 
	)
	public void tm_0126A770A( Test.Case tc ) {
		this.test( tc, Parser.BOOLEAN, "malformed", false );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "Array of length one allowed" 
	)
	public void tm_0E023B276( Test.Case tc ) {
		this.test( tc, Parser.CSV, "foo", new String[] { "foo" } );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "Collection of common cases" 
	)
	public void tm_07DACD82B( Test.Case tc ) {
		this.test( tc, Parser.CSV, "1, 2, 3", new String[] { "1", "2", "3" } );
		this.test( tc, Parser.CSV, "1, , 3", new String[] { "1", "", "3" } );
		this.test( tc, Parser.CSV, "1, , , , 3", new String[] { "1", "", "", "", "3" } );
		this.test( tc, Parser.CSV, "1,2,3", new String[] { "1", "2", "3" } );
		this.test( tc, Parser.CSV, "1,,2", new String[] { "1", "", "2" } );
		this.test( tc, Parser.CSV, "1, , , , 42", new String[] { "1", "", "", "", "42" } );
		this.test( tc, Parser.CSV, " a string with spaces, another string ", new String[] { " a string with spaces", "another string " } );
		this.test( tc, Parser.CSV, "!@#,$%^,&*(", new String[] { "!@#", "$%^", "&*(" } );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "Empty array not allowed" 
	)
	public void tm_03B9F0E21( Test.Case tc ) {
		this.test( tc, Parser.CSV, "", new String[] { "" } );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "White space after comma ignored" 
	)
	public void tm_0C4FA16D2( Test.Case tc ) {
		this.test( tc, Parser.CSV, "one,      spaces", new String[]  { "one", "spaces" } );
		this.test( tc, Parser.CSV, "one,\ttab", new String[] { "one", "tab" } );
		this.test( tc, Parser.CSV, "one,  \t\t  \tmultiple", new String[] { "one", "multiple" } );
		this.test( tc, Parser.CSV, "one  ,      before", new String[]  { "one  ", "before" } );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.INTEGER", 
		description = "Correct for sample cases" 
	)
	public void tm_0F9726538( Test.Case tc ) {
		this.test( tc, Parser.INTEGER, "1", 1 );
		this.test( tc, Parser.INTEGER, "-1", -1 );
		this.test( tc, Parser.INTEGER, "1000000", 1000000 );
		this.test( tc, Parser.INTEGER, "0001000", 1000 );
		this.test( tc, Parser.INTEGER, "-123456789", -123456789 );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.INTEGER", 
		description = "Throws NumberFormatException for mal-formed string" 
	)
	public void tm_0BDC1B61C( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		this.test( tc, Parser.INTEGER, "123.456", 123 );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "Collection of common cases" 
	)
	public void tm_094A5682D( Test.Case tc ) {
		this.test( tc, Parser.LIST, "A, B, C", List.of( "A", "B", "C" ) );
		this.test( tc, Parser.LIST, "A, , C", List.of( "A", "", "C" ) );
		this.test( tc, Parser.LIST, "A,B,C", List.of( "A", "B", "C" ) );
		this.test( tc, Parser.LIST, "A B C, A B C, A B C", List.of( "A B C", "A B C", "A B C" ) );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "Empty list not allowed" 
	)
	public void tm_00FF6C544( Test.Case tc ) {
		this.test( tc, Parser.LIST, "", List.of( "" ) );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "List of length one allowed" 
	)
	public void tm_0AAE09653( Test.Case tc ) {
		this.test( tc, Parser.LIST, "foo", List.of( "foo" ) );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "White space after comma ignored" 
	)
	public void tm_006453810( Test.Case tc ) {
		this.test( tc, Parser.LIST, "one,      spaces", List.of( "one", "spaces" ) );
		this.test( tc, Parser.LIST, "one,\ttab", List.of( "one", "tab" ) );
		this.test( tc, Parser.LIST, "one,  \t\t  \tmultiple", List.of( "one", "multiple" ) );
		this.test( tc, Parser.LIST, "one  ,      before", List.of( "one  ", "before" ) );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LONG", 
		description = "Correct for sample cases" 
	)
	public void tm_06105CA54( Test.Case tc ) {
		this.test( tc, Parser.LONG, "1", 1L );
		this.test( tc, Parser.LONG, "0", 0L );
		this.test( tc, Parser.LONG, "000", 0L );
		this.test( tc, Parser.LONG, "1234567890123456789", 1234567890123456789L );
		this.test( tc, Parser.LONG, "-1234567890123456789", -1234567890123456789L );
		this.test( tc, Parser.LONG, "0101010", 101010L );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.LONG", 
		description = "Throws NumberFormatException for mal-formed string" 
	)
	public void tm_0440EB038( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		this.test( tc, Parser.LONG, "0xABC", 0L );
	}
			
	
	@Test.Impl( 
		member = "field: Parser Parser.STRING", 
		description = "Correct for sample cases" 
	)
	public void tm_026D0199F( Test.Case tc ) {
		this.test( tc, Parser.STRING, "this parser is the identity function", "this parser is the identity function" );
		this.test( tc, Parser.STRING, "", "" );
		this.test( tc, Parser.STRING, " ", " " );
		this.test( tc, Parser.STRING, "'a b'", "'a b'" );
	}
			

	@Test.Impl( 
		member = "field: Parser Parser.BOOLEAN", 
		description = "Parser.BOOLEAN satisfies F1" 
	)
	public void tm_052AA55B3( Test.Case tc ) {
		Boolean[] values = { Boolean.TRUE, Boolean.FALSE };
		for ( Boolean b : values ) {
			tc.assertEqual( b, Parser.BOOLEAN.fromString( b.toString() ) );
		}
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.BOOLEAN", 
		description = "Parser.BOOLEAN does not satisfy F2" 
	)
	public void tm_052AA5974( Test.Case tc ) {
		String bad = "FALSE";
		tc.assertNotEqual( bad, Parser.BOOLEAN.fromString( bad ).toString() );
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.INTEGER", 
		description = "Parser.INTEGER satisfies F1" 
	)
	public void tm_0381CEEF3( Test.Case tc ) {
		Integer[] values = { 42, 0, -2, Integer.MAX_VALUE, Integer.MIN_VALUE, 1_000_000 };
		for ( Integer n : values ) {
			tc.assertEqual( n, Parser.INTEGER.fromString( n.toString() ) );
		}
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.INTEGER", 
		description = "Parser.INTEGER does not satisfy F2" 
	)
	public void tm_0381CF2B4( Test.Case tc ) {
		String bad = "+42";
		tc.assertNotEqual( bad, Parser.INTEGER.fromString( bad ).toString() );
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.LONG", 
		description = "Parser.LONG satisfies F1" 
	)
	public void tm_0DF13BCD9( Test.Case tc ) {
		Long[] values = { 42L, 0L, -213L, Long.MAX_VALUE, Long.MIN_VALUE, 1_000_000_000_000L };
		for ( Long n : values ) {
			tc.assertEqual( n, Parser.LONG.fromString( n.toString() ) );
		}
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.LONG", 
		description = "Parser.LONG does not satisfy F2" 
	)
	public void tm_0DF13C09A( Test.Case tc ) {
		String bad = "+42";
		tc.assertNotEqual( bad, Parser.LONG.fromString( bad ).toString() );
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.STRING", 
		description = "Parser.STRING satisfies F1" 
	)
	public void tm_07B279F2F( Test.Case tc ) {
		String[] values = {
			"some string",
			"Hello world!",
			"+42",
			"this rep is the identity"
		};
		for ( String s : values ) {
			tc.assertEqual( s, Parser.STRING.fromString( s.toString() ) );
		}
	}
		
	@Test.Impl( 
		member = "field: Parser Parser.STRING", 
		description = "Parser.STRING satisfies F2" 
	)
	public void tm_07B27A2F0( Test.Case tc ) {
		String[] values = {
			"some string",
			"Hello world!",
			"+42",
			"this rep is the identity"
		};
		for ( String s : values ) {
			tc.assertEqual( s, Parser.STRING.fromString( s ).toString() );
		}
	}

	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "Parser.CSV does not satisfy F1" 
	)
	public void tm_04B0717A9( Test.Case tc ) {
		String[] bad = { "Hello", "world!" };
		tc.assertNotEqual( bad, Parser.CSV.fromString( bad.toString() ) );
	}
	
	@Test.Impl( 
		member = "field: Parser Parser.CSV", 
		description = "Parser.CSV does not satisfy F2" 
	)
	public void tm_04B071B6A( Test.Case tc ) {
		String bad = "4,2";
		tc.assertNotEqual( bad, Parser.CSV.fromString( bad ).toString() );
	}
			
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "Parser.LIST does not satisfy F1" 
	)
	public void tm_051046047( Test.Case tc ) {
		List<Integer> bad = List.of( 1, 2, 3 );
		tc.assertNotEqual( bad, Parser.LIST.fromString( bad.toString() ) );
	}
	
	@Test.Impl( 
		member = "field: Parser Parser.LIST", 
		description = "Parser.LSIT does not satisfy F2" 
	)
	public void tm_0A2D7D15C( Test.Case tc ) {
		String bad = "1, 2, 3";
		tc.assertNotEqual( bad, Parser.LIST.fromString( bad ).toString() );
	}

	
	

	public static void main( String[] args ) {
		Test.eval( Parser.class );
		//Test.evalPackage( Parser.class );
	}
}
