/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import sog.core.App;
import sog.core.Property;
import sog.core.Test;
import sog.core.xml.XML;

/**
 * @author sundquis
 *
 */
public class PropertyTest extends Test.Implementation {

	// Test implementations

	
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Prints declaration for missing property" )
	public void get_PrintsDeclarationForMissingProperty( Test.Case tc ) {
		// TOGGLE
		/* */ tc.addMessage( "Manually verified" ); /*
		tc.addMessage( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Property not found:" );
		tc.addMessage( "<class fullname=\"test.core.PropertyTest\">" );
		tc.addMessage( "<text name=\"FOO\" value=\"Foo\" />" );
		tc.addMessage( "</class>" );
		Property.get( "FOO",  "Foo",  Property.STRING );
		tc.fail();
		// */
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Retrieves properties for nested classes" )
	public void get_RetrievesPropertiesForNestedClasses( Test.Case tc ) {
		// Should use a mock-up. Instead put a fake entry in system.xml
		tc.assertTrue( Property.get( "nested.test",  false,  Property.BOOLEAN ) );
	}

	public static class Inner {
		public static boolean getTestProp() {
			return Property.get( "double.nested.test",  false,  Property.BOOLEAN );
		}
		public static String getTestText() {
			return Property.getText( "innertest" );
		}
	}
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Retrieves properties for double nested classes" )
	public void get_RetrievesPropertiesForDoubleNestedClasses( Test.Case tc ) {
		tc.assertTrue( Inner.getTestProp() );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Retrieves properties for top level classes" )
	public void get_RetrievesPropertiesForTopLevelClasses( Test.Case tc ) {
		tc.assertTrue( App.get().description() != null );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws assertion error for anonymous classes" )
	public void get_ThrowsAssertionErrorForAnonymousClasses( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Object anon = new Object() {
			@Override public String toString() {
				return Property.get( "foo",  "foo",  Property.STRING );
			}
		};
		tc.assertEqual( "bar",  anon.toString() );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws assertion error for empty name" )
	public void get_ThrowsAssertionErrorForEmptyName( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.get( "",  "foo",  Property.STRING );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws assertion error for local classs" )
	public void get_ThrowsAssertionErrorForLocalClasss( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		class Local {
			boolean getProp() {
				return Property.get( "foo",  false,  Property.BOOLEAN );
			}
		}
		Local local = new Local();
		tc.assertEqual( "bar",  local.getProp() );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws assertion error for null name" )
	public void get_ThrowsAssertionErrorForNullName( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.get( null,  "foo",  Property.STRING );
	}
	
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "throws assertion error for null parser" )
	public void get_ThrowsAssertionErrorForNullParser( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.get( "foo",  "foo",  null );
	}
	
	@Test.Impl( member = "public Function Property.CSV", description = "Array of length one allowed" )
	public void CSV_ArrayOfLengthOneAllowed( Test.Case tc ) {
		String arg = "A single string.";
		String[] array = { "A single string." };
		tc.assertEqual( array, Property.CSV.apply( arg ) );
	}

	private static String[][] ARRAYS = {
		{ "a", "b", "c" }, 
		{ "This time I mean it", "You know what that means", "Somewhat", "longer", "array", "here" },
		{ "Rememeber", "to", "add", "cases", "here", "for", "newly", "discovered", "failure", "modes." },
		{ "" },
		{ "Singleton" }
	};
	private static String[] ARGS = {
		"a, b, c",
		"This time I mean it, You know what that means, Somewhat, longer, array, here",
		"Rememeber, to, add, cases, here, for, newly, discovered, failure, modes.",
		"",
		"Singleton"
	};
	@Test.Impl( member = "public Function Property.CSV", description = "Collection of common cases", weight = 5 )
	public void CSV_CollectionOfCommonCases( Test.Case tc ) {
		for ( int i = 0; i < ARRAYS.length; i++ ) {
			tc.assertEqual( ARRAYS[i],  Property.CSV.apply( ARGS[i] ) );
		}
	}

	@Test.Impl( member = "public Function Property.CSV", description = "Empty array allowed" )
	public void CSV_EmptyArrayAllowed( Test.Case tc ) {
		String arg = "";
		String[] array = { "" };
		tc.assertEqual( array, Property.CSV.apply( arg ) );
	}
	
	@Test.Impl( member = "public Function Property.CSV", description = "White space after comman ignored" )
	public void CSV_WhiteSpaceAfterCommanIgnored( Test.Case tc ) {
		String arg = "Spaces ignored,        Tabs ignored,\t\tNewlines ignored,\n\n Done";
		String[] array = { "Spaces ignored", "Tabs ignored", "Newlines ignored", "Done" };
		tc.assertEqual( array, Property.CSV.apply( arg ) );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Prints declaration for missing property" )
	public void getText_PrintsDeclarationForMissingProperty( Test.Case tc ) {
		// TOGGLE
		/* */ tc.addMessage( "Manually verified" ); /*
		tc.addMessage( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Text not found:" );
		tc.addMessage( "<class fullname=\"test.core.PropertyTest\">" );
		tc.addMessage( "<text name=\"FOO\">Text value</text>" );
		tc.addMessage( "</class>" );
		Property.getText( "FOO" );
		tc.fail();
		// */
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Retrieves text for double nested classes" )
	public void getText_RetrievesTextForDoubleNestedClasses( Test.Case tc ) {
		tc.assertTrue( Inner.getTestText().length() > 0 );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Retrieves text for nested classes" )
	public void getText_RetrievesTextForNestedClasses( Test.Case tc ) {
		tc.assertEqual( "Nested",  Property.getText( "nested" ) );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Retrieves text for top level classes" )
	public void getText_RetrievesTextForTopLevelClasses( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().length() > 0 );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Throws assertion error for anonymous classes" )
	public void getText_ThrowsAssertionErrorForAnonymousClasses( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Object anon = new Object() {
			@Override public String toString() {
				return Property.getText( "foo" );
			}
		};
		tc.assertEqual( "foo",  anon.toString() );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Throws assertion error for empty name" )
	public void getText_ThrowsAssertionErrorForEmptyName( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( "" );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Throws assertion error for local classs" )
	public void getText_ThrowsAssertionErrorForLocalClasss( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		class Local {
			String getText() {
				return Property.getText( "foo" );
			}
		}
		Local local = new Local();
		tc.assertEqual( "foo",  local.getText() );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Throws assertion error for null name" )
	public void getText_ThrowsAssertionErrorForNullName( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( null );
	}
	
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Last value for multiple elements" )
	public void get_LastValueForMultipleElements( Test.Case tc ) {
		tc.assertTrue( Property.get( "duplicate",  false,  Property.BOOLEAN ) );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Can retrieve empty" )
	public void getText_CanRetrieveEmpty( Test.Case tc ) {
		tc.assertTrue( "".equals( Property.getText( "empty" ) ) );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Can use property name" )
	public void getText_CanUsePropertyName( Test.Case tc ) {
		tc.assertEqual( "Duplicate", Property.getText( "duplicate" ) );
	}

	@Test.Impl( member = "public String Property.getText(String)", description = "Last value for multiple elements" )
	public void getText_LastValueForMultipleElements( Test.Case tc ) {
		tc.assertEqual( "Repeated", Property.getText( "repeated" ) );
	}
	
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Uses default for missing" )
	public void get_UsesDefaultForMissing( Test.Case tc ) {
		// This results in a warning that can be ignored
		// TODO: Suppress warning?
		System.err.println( "Can ignore: WARNING: Property not found:..." );
		tc.assertEqual( "Default",  Property.get( "bogus",  "Default",  Property.STRING ) );
	}
	
	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws exception for malformed integer" )
	public void get_ThrowsExceptionForMalformedInteger( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		tc.assertEqual( 10,  Property.get( "malformed", 10, Property.INTEGER ) );
	}

	@Test.Impl( member = "public Object Property.get(String, Object, Function)", description = "Throws exception for malformed long" )
	public void get_ThrowsExceptionForMalformedLong( Test.Case tc ) {
		tc.expectError( NumberFormatException.class );
		tc.assertEqual( 10L,  Property.get( "malformed", 10L, Property.LONG ) );
	}

	
}
