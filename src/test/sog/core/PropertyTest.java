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

import sog.core.Property;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class PropertyTest extends Test.Container {
	
	public PropertyTest() {
		super( Property.class );
	}
	
	
	
	// TEST CASES

	@Test.Impl( 
			member = "field: Function Property.BOOLEAN", 
			description = "Correct for sample cases" 
		)
		public void tm_0590C4851( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.BOOLEAN", 
			description = "Throws ??? for mal-formed string" 
		)
		public void tm_0A4B15125( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.CSV", 
			description = "Array of length one allowed" 
		)
		public void tm_0B11AC053( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.CSV", 
			description = "Collection of common cases" 
		)
		public void tm_052DE1AAE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.CSV", 
			description = "Empty array allowed" 
		)
		public void tm_0C028E38B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.CSV", 
			description = "White space after comma ignored" 
		)
		public void tm_005CCEE2F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.INTEGER", 
			description = "Correct for sample cases" 
		)
		public void tm_0475D12FB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.INTEGER", 
			description = "Throws ??? for mal-formed string" 
		)
		public void tm_0B809D1CF( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LIST", 
			description = "Array of length one allowed" 
		)
		public void tm_0F627D077( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LIST", 
			description = "Collection of common cases" 
		)
		public void tm_0659C760A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LIST", 
			description = "Empty array allowed" 
		)
		public void tm_0DA4A57AF( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LIST", 
			description = "White space after comma ignored" 
		)
		public void tm_0DFCD4C53( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LONG", 
			description = "Correct for sample cases" 
		)
		public void tm_09130AB71( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.LONG", 
			description = "Throws ??? for mal-formed string" 
		)
		public void tm_078509445( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.STRING", 
			description = "Correct for sample cases" 
		)
		public void tm_0F7C7277C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "field: Function Property.STRING", 
			description = "Throws ??? for mal-formed string" 
		)
		public void tm_06D9BD550( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Last value for multiple elements" 
		)
		public void tm_093A030F3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Prints declaration for missing property" 
		)
		public void tm_023799D40( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Retrieves properties for double nested classes" 
		)
		public void tm_0AC2E136D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Retrieves properties for nested classes" 
		)
		public void tm_0E1A1CE32( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Retrieves properties for top level classes" 
		)
		public void tm_02B630780( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Throws AssertionError for anonymous classes" 
		)
		public void tm_060D8994F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Throws AssertionError for empty name" 
		)
		public void tm_0609C6378( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Throws AssertionError for local classs" 
		)
		public void tm_09816760A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Throws AssertionError for null name" 
		)
		public void tm_02FA58020( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Throws AssertionError for null parser" 
		)
		public void tm_0B5749E34( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Property.get(String, Object, Function)", 
			description = "Uses default for missing" 
		)
		public void tm_04A59E698( Test.Case tc ) {
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
