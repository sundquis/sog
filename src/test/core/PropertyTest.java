/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import sog.core.App;
import sog.core.Property;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.xml.XML;

/**
 * @author sundquis
 *
 */
public class PropertyTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Property.class;
	}

	// Test implementations

	
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Prints declaration for missing property" )
	public void get_PrintsDeclarationForMissingProperty( TestCase tc ) {
		// TOGGLE
		/* */ tc.addMessage( "Manually verified" ).pass(); /*
		tc.addMessage( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Property not found:" );
		tc.addMessage( "<class fullname=\"test.core.PropertyTest\">" );
		tc.addMessage( "<text name=\"FOO\" value=\"Foo\" />" );
		tc.addMessage( "</class>" );
		Property.get( "FOO",  "Foo",  Property.STRING );
		tc.fail();
		// */
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Retrieves properties for nested classes" )
	public void get_RetrievesPropertiesForNestedClasses( TestCase tc ) {
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
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Retrieves properties for double nested classes" )
	public void get_RetrievesPropertiesForDoubleNestedClasses( TestCase tc ) {
		tc.assertTrue( Inner.getTestProp() );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Retrieves properties for top level classes" )
	public void get_RetrievesPropertiesForTopLevelClasses( TestCase tc ) {
		tc.assertTrue( App.get().description() != null );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws assertion error for anonymous classes" )
	public void get_ThrowsAssertionErrorForAnonymousClasses( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Object anon = new Object() {
			@Override public String toString() {
				return Property.get( "foo",  "foo",  Property.STRING );
			}
		};
		tc.assertEqual( "bar",  anon.toString() );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws assertion error for empty name" )
	public void get_ThrowsAssertionErrorForEmptyName( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Property.get( "",  "foo",  Property.STRING );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws assertion error for local classs" )
	public void get_ThrowsAssertionErrorForLocalClasss( TestCase tc ) {
		tc.expectError( AssertionError.class );
		class Local {
			boolean getProp() {
				return Property.get( "foo",  false,  Property.BOOLEAN );
			}
		}
		Local local = new Local();
		tc.assertEqual( "bar",  local.getProp() );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws assertion error for null name" )
	public void get_ThrowsAssertionErrorForNullName( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Property.get( null,  "foo",  Property.STRING );
	}
	
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "throws assertion error for null parser" )
	public void get_ThrowsAssertionErrorForNullParser( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Property.get( "foo",  "foo",  null );
	}
	
	@TestOrig.Impl( src = "public Function Property.CSV", desc = "Array of length one allowed" )
	public void CSV_ArrayOfLengthOneAllowed( TestCase tc ) {
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
	@TestOrig.Impl( src = "public Function Property.CSV", desc = "Collection of common cases", weight = 5 )
	public void CSV_CollectionOfCommonCases( TestCase tc ) {
		for ( int i = 0; i < ARRAYS.length; i++ ) {
			tc.assertEqual( ARRAYS[i],  Property.CSV.apply( ARGS[i] ) );
		}
	}

	@TestOrig.Impl( src = "public Function Property.CSV", desc = "Empty array allowed" )
	public void CSV_EmptyArrayAllowed( TestCase tc ) {
		String arg = "";
		String[] array = { "" };
		tc.assertEqual( array, Property.CSV.apply( arg ) );
	}
	
	@TestOrig.Impl( src = "public Function Property.CSV", desc = "White space after comman ignored" )
	public void CSV_WhiteSpaceAfterCommanIgnored( TestCase tc ) {
		String arg = "Spaces ignored,        Tabs ignored,\t\tNewlines ignored,\n\n Done";
		String[] array = { "Spaces ignored", "Tabs ignored", "Newlines ignored", "Done" };
		tc.assertEqual( array, Property.CSV.apply( arg ) );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Prints declaration for missing property" )
	public void getText_PrintsDeclarationForMissingProperty( TestCase tc ) {
		// TOGGLE
		/* */ tc.addMessage( "Manually verified" ).pass(); /*
		tc.addMessage( "SHOULD SEE:" );
		tc.addMessage( "WARNING: Text not found:" );
		tc.addMessage( "<class fullname=\"test.core.PropertyTest\">" );
		tc.addMessage( "<text name=\"FOO\">Text value</text>" );
		tc.addMessage( "</class>" );
		Property.getText( "FOO" );
		tc.fail();
		// */
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Retrieves text for double nested classes" )
	public void getText_RetrievesTextForDoubleNestedClasses( TestCase tc ) {
		tc.assertTrue( Inner.getTestText().length() > 0 );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Retrieves text for nested classes" )
	public void getText_RetrievesTextForNestedClasses( TestCase tc ) {
		tc.assertEqual( "Nested",  Property.getText( "nested" ) );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Retrieves text for top level classes" )
	public void getText_RetrievesTextForTopLevelClasses( TestCase tc ) {
		tc.assertTrue( XML.get().getDeclaration().length() > 0 );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Throws assertion error for anonymous classes" )
	public void getText_ThrowsAssertionErrorForAnonymousClasses( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Object anon = new Object() {
			@Override public String toString() {
				return Property.getText( "foo" );
			}
		};
		tc.assertEqual( "foo",  anon.toString() );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Throws assertion error for empty name" )
	public void getText_ThrowsAssertionErrorForEmptyName( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( "" );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Throws assertion error for local classs" )
	public void getText_ThrowsAssertionErrorForLocalClasss( TestCase tc ) {
		tc.expectError( AssertionError.class );
		class Local {
			String getText() {
				return Property.getText( "foo" );
			}
		}
		Local local = new Local();
		tc.assertEqual( "foo",  local.getText() );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Throws assertion error for null name" )
	public void getText_ThrowsAssertionErrorForNullName( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Property.getText( null );
	}
	
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Last value for multiple elements" )
	public void get_LastValueForMultipleElements( TestCase tc ) {
		tc.assertTrue( Property.get( "duplicate",  false,  Property.BOOLEAN ) );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Can retrieve empty" )
	public void getText_CanRetrieveEmpty( TestCase tc ) {
		tc.assertTrue( "".equals( Property.getText( "empty" ) ) );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Can use property name" )
	public void getText_CanUsePropertyName( TestCase tc ) {
		tc.assertEqual( "Duplicate", Property.getText( "duplicate" ) );
	}

	@TestOrig.Impl( src = "public String Property.getText(String)", desc = "Last value for multiple elements" )
	public void getText_LastValueForMultipleElements( TestCase tc ) {
		tc.assertEqual( "Repeated", Property.getText( "repeated" ) );
	}
	
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Uses default for missing" )
	public void get_UsesDefaultForMissing( TestCase tc ) {
		// This results in a warning that can be ignored
		// TODO: Suppress warning?
		System.err.println( "Can ignore: WARNING: Property not found:..." );
		tc.assertEqual( "Default",  Property.get( "bogus",  "Default",  Property.STRING ) );
	}
	
	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws exception for malformed integer" )
	public void get_ThrowsExceptionForMalformedInteger( TestCase tc ) {
		tc.expectError( NumberFormatException.class );
		tc.assertEqual( 10,  Property.get( "malformed", 10, Property.INTEGER ) );
	}

	@TestOrig.Impl( src = "public Object Property.get(String, Object, Function)", desc = "Throws exception for malformed long" )
	public void get_ThrowsExceptionForMalformedLong( TestCase tc ) {
		tc.expectError( NumberFormatException.class );
		tc.assertEqual( 10L,  Property.get( "malformed", 10L, Property.LONG ) );
	}

	
	
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(PropertyTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
