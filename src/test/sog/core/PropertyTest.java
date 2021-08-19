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

import java.util.List;

import sog.core.Objects;
import sog.core.Property;
import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class PropertyTest extends Test.Container {
	
	public PropertyTest() {
		super( Property.class );
	}
	

	private <T> void test( Test.Case tc, Property.Parser<T> p, String s, T target ) {
		T value = p.value( s );
		if ( Objects.deepEquals( target, value ) ) {
			tc.assertPass();
		} else {
			tc.assertFail( "String = " + s + ", Target = " + Strings.toString( target ) + ", Value = " + Strings.toString( value ) );
		}
	}
	
	
	
	// TEST CASES

	@Test.Impl( 
		member = "field: Property.Parser Property.BOOLEAN", 
		description = "Correct for sample cases" 
	)
	public void tm_07C8234C7( Test.Case tc ) {
		this.test( tc, Property.BOOLEAN, "true", true );
		this.test( tc, Property.BOOLEAN, "TRUE", true );
		this.test( tc, Property.BOOLEAN, "True", true );
		this.test( tc, Property.BOOLEAN, "false", false );
		this.test( tc, Property.BOOLEAN, "FALSE", false );
		this.test( tc, Property.BOOLEAN, "False", false );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.BOOLEAN", 
		description = "Returns false for mal-formed string" 
	)
	public void tm_0F106679B( Test.Case tc ) {
		this.test( tc, Property.BOOLEAN, "malformed", false );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.CSV", 
		description = "Array of length one allowed" 
	)
	public void tm_01D9A6D1D( Test.Case tc ) {
		this.test( tc, Property.CSV, "foo", new String[] { "foo" } );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.CSV", 
		description = "Collection of common cases" 
	)
	public void tm_0A0B0ACA4( Test.Case tc ) {
		this.test( tc, Property.CSV, "1, 2, 3", new String[] { "1", "2", "3" } );
		this.test( tc, Property.CSV, "1, , 3", new String[] { "1", "", "3" } );
		this.test( tc, Property.CSV, "1,2,3", new String[] { "1", "2", "3" } );
		//this.test( tc, Property.CSV, ",,", new String[] { "", "", "" } );
		//this.test( tc, Property.CSV, " , , ", new String[] { "", "", "" } );
		this.test( tc, Property.CSV, " a string with spaces, another string ", new String[] { " a string with spaces", "another string " } );
		this.test( tc, Property.CSV, "!@#,$%^,&*(", new String[] { "!@#", "$%^", "&*(" } );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.CSV", 
		description = "Empty array not allowed" 
	)
	public void tm_0A31EFA55( Test.Case tc ) {
		this.test( tc, Property.CSV, "", new String[] { "" } );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.CSV", 
		description = "White space after comma ignored" 
	)
	public void tm_09DB525F9( Test.Case tc ) {
		this.test( tc, Property.CSV, "one,      spaces", new String[]  { "one", "spaces" } );
		this.test( tc, Property.CSV, "one,\ttab", new String[] { "one", "tab" } );
		this.test( tc, Property.CSV, "one,  \t\t  \tmultiple", new String[] { "one", "multiple" } );
		this.test( tc, Property.CSV, "one  ,      before", new String[]  { "one  ", "before" } );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.INTEGER", 
		description = "Correct for sample cases" 
	)
	public void tm_06AD2FF71( Test.Case tc ) {
		this.test( tc, Property.INTEGER, "1", 1 );
		this.test( tc, Property.INTEGER, "-1", -1 );
		this.test( tc, Property.INTEGER, "1000000", 1000000 );
		this.test( tc, Property.INTEGER, "0001000", 1000 );
		this.test( tc, Property.INTEGER, "-123456789", -123456789 );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.INTEGER", 
		description = "Throws NumberFormatException for mal-formed string" 
	)
	public void tm_0045EE845( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		this.test( tc, Property.INTEGER, "123.456", 123 );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LIST", 
		description = "List of length one allowed" 
	)
	public void tm_0199DBCED( Test.Case tc ) {
		this.test( tc, Property.LIST, "foo", List.of( "foo" ) );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LIST", 
		description = "Collection of common cases" 
	)
	public void tm_0D21C22D4( Test.Case tc ) {
		this.test( tc, Property.LIST, "A, B, C", List.of( "A", "B", "C" ) );
		this.test( tc, Property.LIST, "A, , C", List.of( "A", "", "C" ) );
		this.test( tc, Property.LIST, "A,B,C", List.of( "A", "B", "C" ) );
		this.test( tc, Property.LIST, "A B C, A B C, A B C", List.of( "A B C", "A B C", "A B C" ) );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LIST", 
		description = "Empty list not allowed" 
	)
	public void tm_056171A25( Test.Case tc ) {
		this.test( tc, Property.LIST, "", List.of( "" ) );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LIST", 
		description = "White space after comma ignored" 
	)
	public void tm_044EC0DC9( Test.Case tc ) {
		this.test( tc, Property.LIST, "one,      spaces", List.of( "one", "spaces" ) );
		this.test( tc, Property.LIST, "one,\ttab", List.of( "one", "tab" ) );
		this.test( tc, Property.LIST, "one,  \t\t  \tmultiple", List.of( "one", "multiple" ) );
		this.test( tc, Property.LIST, "one  ,      before", List.of( "one  ", "before" ) );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LONG", 
		description = "Correct for sample cases" 
	)
	public void tm_06A6902BB( Test.Case tc ) {
		this.test( tc, Property.LONG, "1", 1L );
		this.test( tc, Property.LONG, "0", 0L );
		this.test( tc, Property.LONG, "000", 0L );
		this.test( tc, Property.LONG, "1234567890123456789", 1234567890123456789L );
		this.test( tc, Property.LONG, "-1234567890123456789", -1234567890123456789L );
		this.test( tc, Property.LONG, "0101010", 101010L );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.LONG", 
		description = "Throws NumberFormatException for mal-formed string" 
	)
	public void tm_0B70A018F( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		this.test( tc, Property.LONG, "0xABC", 0L );
	}
		
	@Test.Impl( 
		member = "field: Property.Parser Property.STRING", 
		description = "Correct for sample cases" 
	)
	public void tm_06446D446( Test.Case tc ) {
		this.test( tc, Property.STRING, "this parser is the identity function", "this parser is the identity function" );
		this.test( tc, Property.STRING, "", "" );
		this.test( tc, Property.STRING, " ", " " );
		this.test( tc, Property.STRING, "'a b'", "'a b'" );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Property.Parser)", 
		description = "Last value for repeated elements" 
	)
	public void tm_0559203CB( Test.Case tc ) {
		tc.assertEqual( "bar", Property.get( "repeated", "foo", Property.STRING ) );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Property.Parser)", 
		description = "Prints declaration for missing property" 
	)
	public void tm_0DD356768( Test.Case tc ) {
		// TOGGLE:
		/* */ tc.assertPass(); /*
		tc.assertFail( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Property not found:" );
		tc.addMessage( "..." );
		Property.get( "bogus", "missing", Property.STRING );
		/* */
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Property.Parser)", 
		description = "Retrieves properties for double nested classes" 
	)
	public void tm_082AA0C45( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Retrieves properties for nested classes" 
		)
		public void tm_09B5D985A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Retrieves properties for top level classes" 
		)
		public void tm_02FAD2C58( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Throws AssertionError for anonymous classes" 
		)
		public void tm_0E5D30F77( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Throws AssertionError for empty name" 
		)
		public void tm_05D390A50( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Throws AssertionError for local classs" 
		)
		public void tm_0E024C6E2( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Throws AssertionError for null name" 
		)
		public void tm_0BBEC9E48( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Throws AssertionError for null parser" 
		)
		public void tm_04C6CD25C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Property.Parser)", 
			description = "Uses default for missing" 
		)
		public void tm_096581170( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Can retrieve empty" 
		)
		public void tm_0ADC0F21B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Can use property name" 
		)
		public void tm_0A642ED81( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Last value for multiple elements" 
		)
		public void tm_0A2D74919( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Prints declaration for missing property" 
		)
		public void tm_060042E5A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Retrieves text for double nested classes" 
		)
		public void tm_07F2FF20D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Retrieves text for nested classes" 
		)
		public void tm_0A1572392( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Retrieves text for top level classes" 
		)
		public void tm_07A5D3620( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Throws AssertionError for anonymous classes" 
		)
		public void tm_045210D69( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Throws AssertionError for empty name" 
		)
		public void tm_0509EF89E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Throws AssertionError for local classs" 
		)
		public void tm_091C859B0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Property.getText(String)", 
			description = "Throws AssertionError for null name" 
		)
		public void tm_0CC08AE3A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

	
	
	

	public static void main( String[] args ) {
		Test.eval( Property.class );
		//Test.evalPackage( Property.class );
		//Test.evalDir( Property.class, "sog" );
	}
}
