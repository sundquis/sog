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


import sog.core.Parser;
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
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Last value for repeated elements" 
	)
	public void tm_0559203CB( Test.Case tc ) {
		tc.assertEqual( "bar", Property.get( "repeated", "foo", Parser.STRING ) );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
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
		
	public static class Inner {
		
		public static final String MSG = Property.get( "message", "foo", Parser.STRING );
		
		public static final String TEXT = Property.getText( "message" );
		
		public static class Nested {
			public static final String MSG = Property.get( "message", "foo", Parser.STRING );
			public static final String TEXT = Property.getText( "message" );
		}
	
	}
	
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Retrieves properties for double nested classes" 
	)
	public void tm_082AA0C45( Test.Case tc ) {
		tc.assertEqual( "bar", Inner.Nested.MSG );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Retrieves properties for nested classes" 
	)
	public void tm_09B5D985A( Test.Case tc ) {
		tc.assertEqual( "bar", Inner.MSG );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Retrieves properties for top level classes" 
	)
	public void tm_02FAD2C58( Test.Case tc ) {
		tc.assertEqual( "bar", Property.get( "message", "foo", Parser.STRING ) );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Retrieves properties for anonymous classes" 
	)
	public void tm_0E5D30F77( Test.Case tc ) {
		String s = new Object() { 
			@Override public String toString() { return Property.get( "message", "foo", Parser.STRING ); }
		}.toString();
		tc.assertEqual( "bar", s );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Throws AssertionError for empty name" 
	)
	public void tm_05D390A50( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.get( "", "foo", Parser.STRING );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Retrieves properties for local classes" 
	)
	public void tm_0E024C6E2( Test.Case tc ) {
		class Local {
			@Override public String toString() { return Property.get( "message", "foo", Parser.STRING ); }
		}
		String s = new Local().toString();
		tc.assertEqual( "bar", s );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Throws AssertionError for null name" 
	)
	public void tm_0BBEC9E48( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.get( null, "foo", Parser.STRING );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Throws AssertionError for null parser" 
	)
	public void tm_04C6CD25C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Parser<String> p = null;
		Property.get( "message", "foo", p );
	}
		
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Uses default for missing" 
	)
	public void tm_096581170( Test.Case tc ) {
		// TOGGLE:
		/* */ tc.assertPass(); /*
		tc.addMessage( "This should pass, but it prints an error on stderr" );
		tc.assertEqual( "default", Property.get( "bogus", "default", Property.STRING ) );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Can retrieve empty" 
	)
	public void tm_0ADC0F21B( Test.Case tc ) {
		tc.assertEqual( "", Property.getText( "empty message" ) );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Can use property name" 
	)
	public void tm_0A642ED81( Test.Case tc ) {
		tc.assertEqual( "Bar", Property.getText( "message" ) );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Last value for multiple elements" 
	)
	public void tm_0A2D74919( Test.Case tc ) {
		tc.assertEqual( "Bar", Property.getText( "repeated" ) );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Prints declaration for missing property" 
	)
	public void tm_060042E5A( Test.Case tc ) {
		// TOGGLE:
		/* */ tc.assertPass(); /*
		tc.assertFail( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Text not found:" );
		tc.addMessage( "<class fullname=\"test.sog.core.PropertyTest\">" );
		tc.addMessage( "<text name=\"bogus\">Text value</text>" );
		tc.addMessage( "</class>" );
		Property.getText( "bogus" );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Retrieves text for double nested classes" 
	)
	public void tm_07F2FF20D( Test.Case tc ) {
		tc.assertEqual( "Message.", Inner.Nested.TEXT );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Retrieves text for nested classes" 
	)
	public void tm_0A1572392( Test.Case tc ) {
		tc.assertEqual( "Message.", Inner.TEXT );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Retrieves text for top level classes" 
	)
	public void tm_07A5D3620( Test.Case tc ) {
		tc.assertEqual( "Bar", Property.getText( "message" ) );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Retrieves text for anonymous classes" 
	)
	public void tm_045210D69( Test.Case tc ) {
		String s = new Object() {
			@Override public String toString() { return Property.getText( "message" ); }
		}.toString();
		tc.assertEqual( "Bar", s );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Throws AssertionError for empty name" 
	)
	public void tm_0509EF89E( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( "" );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Retrieves text for local classes" 
	)
	public void tm_091C859B0( Test.Case tc ) {
		class Local {
			@Override public String toString() { return Property.getText( "message" ); }
		}
		String s = new Local().toString();
		tc.assertEqual( "Bar", s );
	}
		
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Throws AssertionError for null name" 
	)
	public void tm_0CC08AE3A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( null );
	}
	
	@Test.Impl( 
		member = "method: Object Property.get(String, Object, Parser)", 
		description = "Prints instructions when system property file not found" 
	)
	public void tm_0682795E3( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: String Property.getText(String)", 
		description = "Prints instructions when system property file not found" 
	)
	public void tm_0D9EF3EE4( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}

	@Test.Impl( 
		member = "method: void Property.characters(char[], int, int)", 
		description = "Characters added" 
	)
	public void tm_0289A2691( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: void Property.endElement(String)", 
		description = "Text elements terminated" 
	)
	public void tm_0318718C2( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: void Property.startElement(String, Map)", 
		description = "Buffer reset on text elements" 
	)
	public void tm_07DAACEB8( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: void Property.startElement(String, Map)", 
		description = "Class name set on class elements" 
	)
	public void tm_0AFD78ACD( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: void Property.startElement(String, Map)", 
		description = "Key set on text elements" 
	)
	public void tm_0BBD761A0( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
	
	@Test.Impl( 
		member = "method: void Property.startElement(String, Map)", 
		description = "Property added on property elements" 
	)
	public void tm_0C1872E8A( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}

	
	
	

	public static void main( String[] args ) {
		Test.eval( Property.class );
		//Test.evalPackage( Property.class );
		//Test.evalDir( Property.class, "sog" );
	}
}
