/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import sog.core.App;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class StringsTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Strings.class;
	}

	// Test implementations
	
	// These objects are various arrays
	private List<Object> testArrays;
	
	// Carefully worked out string representations
	private List<String> testArrayStrings;
	
	private List<Collection<Object>> testLists;
	
	private List<String> testListStrings;
	
	private void init() {
		this.testArrays = new ArrayList<Object>();
		this.testArrays.add( new int[] { -1, 42, 43, 44, 45, 50, Integer.MAX_VALUE } );
		this.testArrays.add( new long[] { Long.MIN_VALUE, 42, 0, 0, 0, 0, 0, 0, 0, 0, 0} );
		this.testArrays.add( new double[] { -1.234, 5.678, Double.MIN_VALUE, Double.MAX_VALUE } );
		this.testArrays.add( new boolean[] { true, false } );
		this.testArrays.add( new String[] { "hello", "world", "of", "course" } );
		this.testArrays.add( new Object[] { 
			new Object() {public String toString() {return "Hi";}}, null, "Foo" } );
		
		this.testArrayStrings = new ArrayList<String>();
		this.testArrayStrings.add( "[-1, 42, ...<4 more>... 2147483647]" );
		this.testArrayStrings.add( "[-9223372036854775808, 42, ...<8 more>... 0]" );
		this.testArrayStrings.add( "[-1.234, 5.678, 4.9E-324, 1.7976931348623157E308]" );
		this.testArrayStrings.add( "[true, false]" );
		this.testArrayStrings.add( "[hello, world, of, course]" );
		this.testArrayStrings.add( "[Hi, null, Foo]" );
		
		this.testLists = new ArrayList<Collection<Object>>();
		for ( Object obj : testArrays ) {
			ArrayList<Object> newList = new ArrayList<Object>();
			for ( int i = 0; i < Array.getLength( obj ); i++ ) {
				newList.add( Array.get( obj,  i) );
			}
			testLists.add( newList );
		}
		
		this.testListStrings = new ArrayList<String>();
		for ( String s : testArrayStrings ) {
			this.testListStrings.add( s.replace( '[',  '{' ).replace( ']', '}' ) );
		}
	}
	
	
	public Procedure beforeAll() {
		return () -> init();
	}
	
	
	// arrayToString
	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)", 
		desc = "Empty array allowed" )
	public void arrayToString_EmptyArrayAllowed( TestCase tc ) {
		tc.assertEqual( "[]" , Strings.arrayToString( new String[] {} ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)", 
		desc = "Enclosed in set brackets" )
	public void arrayToString_EnclosedInSetBrackets( TestCase tc ) {
		String result = Strings.arrayToString( testArrays.get(0) );
		tc.assertEqual( '[', result.charAt(0) );
		tc.assertEqual( ']', result.charAt( result.length()-1 ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)",
		desc = "Omitted elements are indicated" )
	public void arrayToString_OmittedElementsAreIndicated( TestCase tc ) {
		String result = Strings.arrayToString( testArrays.get(0) );
		tc.assertTrue( result.contains( "more>" ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)",
		desc = "Sample cases for arrays of arrays" )
	public void arrayToString_SampleCasesForArraysOfArrays( TestCase tc ) {
		Object[] arrayOfArray = {
			new int[] { 1 },
			new boolean[] { true, false },
			new char[] { 'a', 'b', 'c' }
		};
		tc.assertEqual( "[[1], [true, false], [a, b, c]]", Strings.arrayToString( arrayOfArray ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)",
		desc = "Sample cases for arrays of collections" )
	public void arrayToString_SampleCasesForArraysOfCollections( TestCase tc ) {
		Object[] arrayOfCollection = {
				Arrays.asList( 1 ),
				Arrays.asList( true, false ),
				Arrays.asList( 'a', 'b', 'c' )
			};
		tc.assertEqual( "[{1}, {true, false}, {a, b, c}]", Strings.arrayToString( arrayOfCollection ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)",
		desc = "Sample cases for arrays of primitive" )
	public void arrayToString_SampleCasesForArraysOfPrimitive( TestCase tc ) {
		Iterator<Object> args = testArrays.iterator();
		for ( String s : testArrayStrings ) {
			tc.assertEqual( s,  Strings.arrayToString( args.next() ) );
		}
	}

	@TestOrig.Impl(
		src = "public String Strings.arrayToString(Object)", 
		desc = "Throws assertion error on null arrays" )
	public void arrayToString_ThrowsAssertionErrorOnNullArrays( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.arrayToString( null );
	}

	
	// collectionToString
	@TestOrig.Impl( 
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Empty collection allowed" )
	public void collectionToString_EmptyCollectionAllowed( TestCase tc ) {
		tc.assertEqual( "{}",  Strings.collectionToString( new ArrayList<Object>() ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Enclosed in set braces" )
	public void collectionToString_EnclosedInSetBraces( TestCase tc ) {
		String result = Strings.collectionToString( testLists.get(0) );
		tc.assertEqual( '{', result.charAt(0) );
		tc.assertEqual( '}', result.charAt( result.length()-1 ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Omitted elements are indicated" )
	public void collectionToString_OmittedElementsAreIndicated( TestCase tc ) {
		String result = Strings.collectionToString( testLists.get(0) );
		tc.assertTrue( result.contains( "more>" ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Sample cases for collections of arrays" )
	public void collectionToString_SampleCasesForCollectionsOfArrays( TestCase tc ) {
		List<Object> lo = new ArrayList<>();
		lo.add( new int[] { 1 } );
		lo.add( new boolean[] { true, false } );
		lo.add( new char[] { 'a', 'b', 'c' } );
		tc.assertEqual( "{[1], [true, false], [a, b, c]}", Strings.collectionToString( lo ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Sample cases for collections of collections" )
	public void collectionToString_SampleCasesForCollectionsOfCollections( TestCase tc ) {
		List<Object> collectionOfCollections = Arrays.asList(
			Arrays.asList( 1 ),
			Arrays.asList( true, false ),
			Arrays.asList( 'a', 'b', 'c' )
		);
		tc.assertEqual( "{{1}, {true, false}, {a, b, c}}", Strings.collectionToString( collectionOfCollections ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Sample cases for collections of primitive" )
	public void collectionToString_SampleCasesForCollectionsOfPrimitive( TestCase tc ) {
		Iterator<Collection<Object>> args = testLists.iterator();
		for ( String s : testListStrings ) {
			tc.assertEqual( s,  Strings.collectionToString( args.next() ) );
		}
	}

	@TestOrig.Impl(
		src = "public String Strings.collectionToString(Collection)", 
		desc = "Throws assertion error on null collections" )
	public void collectionToString_ThrowsAssertionErrorOnNullCollections( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.collectionToString( null );
	}

	
	// justify
	@TestOrig.Impl( 
		src = "public String Strings.justify(String, int, char)", 
		desc = "Justify empty with neg width is not empty" )
	public void justify_JustifyEmptyWithNegWidthIsNotEmpty( TestCase tc ) {
		tc.assertEqual( "X",  Strings.justify( "",  -1,  'X' ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.justify(String, int, char)", 
		desc = "Justify empty with pos width is not empty" )
	public void justify_JustifyEmptyWithPosWidthIsNotEmpty( TestCase tc ) {
		tc.assertEqual( "X",  Strings.justify( "",  1,  'X' ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.justify(String, int, char)", 
		desc = "Throws assertion error for null string" )
	public void justify_ThrowsAssertionErrorForNullString( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.justify( null,  1,  ' ' );
	}

	@TestOrig.Impl( 
		src = "public String Strings.justify(String, int, char)", 
		desc = "Throws assertion error for zero width" )
	public void justify_ThrowsAssertionErrorForZeroWidth( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.justify( "Foo",  0,  ' ' );
	}

	
	// leftJustify
	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Long string truncated" )
	public void leftJustify_LongStringTruncated( TestCase tc ) {
		tc.assertEqual( "Hell",  Strings.leftJustify( "Hello world",  4,  '_' ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Result has specified length" )
	public void leftJustify_ResultHasSpecifiedLength( TestCase tc ) {
		for ( int i = 1; i < 10; i++ ) {
			tc.assertEqual( i,  Strings.leftJustify( "Foo", i,  'x' ).length() );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Sample cases" )
	public void leftJustify_SampleCases( TestCase tc ) {
		String[] expected = { "H", "He", "Hel", "Hell", "Hello", "Hello_", "Hello__" };
		for ( int i = 0; i < 7; i++ ) {
			tc.assertEqual( expected[i],  Strings.leftJustify( "Hello",  i+1,  '_' ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Short string padded with given character" )
	public void leftJustify_ShortStringPaddedWithGivenCharacter( TestCase tc ) {
		for ( int i = 5; i < 10; i++ ) {
			tc.assertEqual( 'x',  Strings.leftJustify( "Fooo",  10,  'x' ).charAt(i) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Throws assertion error for non positive width" )
	public void leftJustify_ThrowsAssertionErrorForNonPositiveWidth( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.leftJustify( "Foo",  -2,  'c' );
	}

	@TestOrig.Impl( 
		src = "public String Strings.leftJustify(String, int, char)", 
		desc = "Throws assertion error for null string" )
	public void leftJustify_ThrowsAssertionErrorForNullString( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.leftJustify( null,  2,  'c' );
	}

	
	// rightJustify
	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Long string truncated" )
	public void rightJustify_LongStringTruncated( TestCase tc ) {
		tc.assertEqual( "Hell",  Strings.rightJustify( "Hello world",  4,  '_' ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Result has specified length" )
	public void rightJustify_ResultHasSpecifiedLength( TestCase tc ) {
		for ( int i = 1; i < 10; i++ ) {
			tc.assertEqual( i,  Strings.rightJustify( "Foo", i,  'x' ).length() );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Sample cases" )
	public void rightJustify_SampleCases( TestCase tc ) {
		String[] expected = { "H", "He", "Hel", "Hell", "Hello", "_Hello", "__Hello" };
		for ( int i = 0; i < 7; i++ ) {
			tc.assertEqual( expected[i],  Strings.rightJustify( "Hello",  i+1,  '_' ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Short string padded with given character" )
	public void rightJustify_ShortStringPaddedWithGivenCharacter( TestCase tc ) {
		for ( int i = 0; i < 5; i++ ) {
			tc.assertEqual( 'x',  Strings.rightJustify( "Fooo",  10,  'x' ).charAt(i) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Throws assertion error for non positive width" )
	public void rightJustify_ThrowsAssertionErrorForNonPositiveWidth( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.rightJustify( "Foo",  -2,  'c' );
	}

	@TestOrig.Impl( 
		src = "public String Strings.rightJustify(String, int, char)", 
		desc = "Throws assertion error for null string" )
	public void rightJustify_ThrowsAssertionErrorForNullString( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.rightJustify( null,  2,  'c' );
	}

	
	
	// strip
	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Identity for non quoted trimmed strings" )
	public void strip_IdentityForNonQuotedTrimmedStrings( TestCase tc ) {
		String[] expected = { 
			"Unquoted strings", 
			"1, 2, 3, 4, and 5",
			"A string with \" embedded",
			"A string with \' embedded",
			"A string with   \t\ninternal white space"
		};
		for ( String s : expected ) {
			tc.assertEqual( s, Strings.strip( s ) );
		}
	}

	@TestOrig.Impl(
		src = "public String Strings.strip(String)", 
		desc = "Identity on empty" )
	public void strip_IdentityOnEmpty( TestCase tc ) {
		tc.assertEqual( "",  Strings.strip( "" ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Ignores unmatched quotes" )
	public void strip_IgnoresUnmatchedQuotes( TestCase tc ) {
		String[] expected = {
			"\"Leading double",
			"\'Leading single",
			"Trailing doule\"",
			"Trainling single\'",
			"\"Double single\'",
			"\'Single double\""
		};
		for ( String s : expected ) {
			tc.assertEqual( s, Strings.strip( s ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Is idempotent" )
	public void strip_IsIdempotent( TestCase tc ) throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<String>();
		
		String line;
		// Read every line in this file as a string
		Path file = App.get().sourceFile( Strings.class );
		try ( BufferedReader in = new BufferedReader( new FileReader( file.toFile() ) ) ) {
			while ( (line = in.readLine()) != null ) {
				// Create lots of quoted strings
				lines.add( line.replaceAll( "@Test\\.Decl\\( ", "" ).replaceAll( "\\)",  "" ) );
			}
		}
		
		String expect;
		for ( String s : lines ) {
			expect = Strings.strip( s );
			tc.assertEqual( expect,  Strings.strip( expect ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Removes double quotes" )
	public void strip_RemovesDoubleQuotes( TestCase tc ) {
		tc.assertEqual( "Hello world",  Strings.strip( "\"Hello world\"" ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Removes nested quotes" )
	public void strip_RemovesNestedQuotes( TestCase tc ) {
		tc.assertEqual( "Hello world",  Strings.strip( "\"\'Hello world\'\"" ) );
	}

	@TestOrig.Impl(
		src = "public String Strings.strip(String)", 
		desc = "Removes single quotes" )
	public void strip_RemovesSingleQuotes( TestCase tc ) {
		tc.assertEqual( "Hello world",  Strings.strip( "\'Hello world\'" ) );
	}

	@TestOrig.Impl( src = "public String Strings.strip(String)", desc = "Result is trimmed" )
	public void strip_ResultIsTrimmed( TestCase tc ) {
		tc.assertEqual( "Hello world",  Strings.strip( "\t\n  Hello world   " ) );
	}

	@TestOrig.Impl( src = "public String Strings.strip(String)", desc = "Sample cases" )
	public void strip_SampleCases( TestCase tc ) {
		String[] args = {
			"No quotes",
			"\"Double\"",
			"\'Single\'",
			"\"   Double space\t\"",
			"\'\n Single space      \'",
			"     \"Space double\"     ",
			"   \'Space single\'   ",
			"   \"    Mixed Double   \"   ",
			"   \'   Mixed single   \'   ",
			"   \"   \'   Nested   \'   \"   "
		};

		String[] expected = {
				"No quotes",
				"Double",
				"Single",
				"Double space",
				"Single space",
				"Space double",
				"Space single",
				"Mixed Double",
				"Mixed single",
				"Nested"
		};
		
		// Equivalent to assertListEqual...
		for ( int i = 0; i < args.length; i++ ) {
			tc.assertEqual( expected[i],  Strings.strip( args[i] ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.strip(String)", 
		desc = "Throws assertion error for null string" )
	public void strip_ThrowsAssertionErrorForNullString( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.strip( null );
	}

	
	
	// toCamelCase
	@TestOrig.Impl( src = "public String Strings.toCamelCase(String)", desc = "Does not start with a digit" )
	public void toCamelCase_DoesNotStartWithADigit( TestCase tc ) {
		String[] args = {
			"A 2-normal subgroup",
			"2-3 cisethylene",
			"1 a 2 b 0 0 0 destruct 0",
			"1 2 3 4",
			"1234",
			"1, 2, 3, a, b, c",
			"1 2 3 a b c",
			"",
			"1234 abcd"
		};
		for ( String s : args ) {
			tc.assertFalse( Strings.toCamelCase( s ).matches( "^\\d" ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Identity on empty" )
	public void toCamelCase_IdentityOnEmpty( TestCase tc ) {
		tc.assertEqual( "",  Strings.toCamelCase( "" ) );
	}
	
	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Result contains no white space" )
	public void toCamelCase_ResultContainsNoWhiteSpace( TestCase tc ) {
		String[] args = {
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"Enclosed in set brackets",
			"Empty array allowed",
			"Sample cases for arrays of primitive",
			"Sample cases for arrays of collections",
			"Sample cases for arrays of arrays",
			"Omitted elements are indicated"
		};
		
		for ( String s : args ) {
			tc.assertFalse( Strings.toCamelCase( s ).matches( ".*\\s+.*" ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Result contains only letters" )
	public void toCamelCase_ResultContainsOnlyLetters( TestCase tc ) {
		String[] args = {
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"Enclosed in set brackets",
			"Empty array allowed",
			"Sample cases for arrays of primitive",
			"Sample cases for arrays of collections",
			"Sample cases for arrays of arrays",
			"Omitted elements are indicated"
		};
		
		for ( String s : args ) {
			tc.assertTrue( Strings.toCamelCase( s ).matches( "^\\w*$" ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Sample cases" )
	public void toCamelCase_SampleCases( TestCase tc ) {
		String[] args = {
			"Hello, world!",
			"a e i o u and sometimes y",
			"3.14159",
			"A, E, I, O, U",
			"onereallylongstring",
			"A 2-normal subgroup",
			"2-3 cisethylene",
			"1 a 2 b 0 0 0 destruct 0",
			"1 2 3 4",
			"1234",
			"1, 2, 3, a, b, c",
			"1 2 3 a b c",
			"",
			"1234 abcd"
		};
		
		String[] expected = {
			"HelloWorld",
			"AEIOUAndSometimesY",
			"",
			"AEIOU",
			"Onereallylongstring",
			"A2NormalSubgroup",
			"Cisethylene",
			"A2B000Destruct0",
			"",
			"",
			"ABC",
			"ABC",
			"",
			"Abcd"
		};
		
		for ( int i = 0; i < args.length; i++ ) {
			tc.assertEqual( expected[i], Strings.toCamelCase( args[i] ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Starts with uppercase" )
	public void toCamelCase_StartsWithUppercase( TestCase tc ) {
		String[] args = {
			"Hello, world!",
			"a e i o u and sometimes y",
			"3.14159",
			"A, E, I, O, U",
			"onereallylongstring",
			"A 2-normal subgroup",
			"2-3 cisethylene",
			"1 a 2 b 0 0 0 destruct 0",
			"1 2 3 4",
			"1234",
			"1, 2, 3, a, b, c",
			"1 2 3 a b c",
			"",
			"1234 abcd"
		};
		
		for ( String s : args ) {
			String result = Strings.toCamelCase( s );
			tc.assertTrue( result.length() == 0 || result.matches( "^[A-Z].*" ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toCamelCase(String)", 
		desc = "Throws assertion error for null string" )
	public void toCamelCase_ThrowsAssertionErrorForNullString( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Strings.toCamelCase( null );
	}

	@TestOrig.Impl( src = "public String Strings.toCamelCase(String)", desc = "Underscore removed" )
	public void toCamelCase_UnderscoreRemoved( TestCase tc ) {
		String[] args = {
			"Hello_world!",
			"a_e_i_o_u and sometimes y",
			"3.14159",
			"_____A__E_I_ O U___",
			"_onereallylongstring_",
			"A 2_normal subgroup",
			"2_3_cisethylene",
			"1 a 2 b 0 0 0_destruct_0",
			"1_2_3_4",
			"_1234_",
			"1_2_3_a_b_c",
			"1234_abcd"
		};
		
		String[] expected = {
			"HelloWorld",
			"AEIOUAndSometimesY",
			"",
			"AEIOU",
			"Onereallylongstring",
			"A2NormalSubgroup",
			"Cisethylene",
			"A2B000Destruct0",
			"",
			"",
			"ABC",
			"Abcd"
		};
		
		for ( int i = 0; i < args.length; i++ ) {
			tc.assertEqual( expected[i],  Strings.toCamelCase( args[i] ) );
		}
	}

	
	// toString
	@TestOrig.Impl( 
		src = "public String Strings.toString(Object)", 
		desc = "Agrees with object to string for non array non collection" )
	public void toString_AgreesWithObjectToStringForNonArrayNonCollection( TestCase tc ) {
		Object[] args = {
			null,
			"null",
			new Object(){},
			42,
			true,
			Boolean.TRUE,
			new Runnable() { public void run(){} public String toString(){ return "foo";} },
			(Supplier<Integer>) () -> 42,
			123456789L,
			new java.util.Date()
		};
		
		for ( Object obj : args ) {
			tc.assertEqual( "" + obj,  Strings.toString( obj ) );
		}
	}

	@TestOrig.Impl( 
		src = "public String Strings.toString(Object)", 
		desc = "Identity on empty" )
	public void toString_IdentityOnEmpty( TestCase tc ) {
		tc.assertEqual( "",  Strings.toString( "" ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.toString(Object)", 
		desc = "Provides alternate string representation for arrays" )
	public void toString_ProvidesAlternateStringRepresentationForArrays( TestCase tc ) {
		String[] arg = { "a", "b" };
		tc.assertFalse( arg.toString() == Strings.toString( arg ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.toString(Object)", 
		desc = "Provides alternate string representation for collections" )
	public void toString_ProvidesAlternateStringRepresentationForCollections( TestCase tc ) {
		List<String> arg = Arrays.asList( "a", "b" );
		tc.assertFalse( arg.toString() == Strings.toString( arg ) );
	}

	@TestOrig.Impl( 
		src = "public String Strings.toString(Object)", 
		desc = "String representation of null is null" )
	public void toString_StringRepresentationOfNullIsNull( TestCase tc ) {
		tc.assertEqual( "null",  Strings.toString( null ) );
	}
				
	@TestOrig.Impl( src = "public String Strings.relativePathToClassname(Path)", desc = "Class to relative path to classname correct" )
	public void relativePathToClassname_ClassToRelativePathToClassnameCorrect( TestCase tc ) {
		Class<?>[] classes = new Class<?>[] { 
			App.class,
			Procedure.class,
			Strings.class,
			TestOrig.class,
			TestCase.class,
			TestContainer.class
		};
		
		for ( int i = 0; i < classes.length; i++ ) {
			Class<?> clazz = classes[i];
			Path sourceRoot = App.get().sourceDir( clazz );
			Path sourceFile = App.get().sourceFile( clazz );
			Path relativePath = sourceRoot.relativize( sourceFile );
			String className = Strings.relativePathToClassname( relativePath );
			tc.assertEqual( clazz.getName(), className );
		}
	}
	
	
	

	public static void main(String[] args) {

		System.out.println();

		new TestOrig(StringsTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
