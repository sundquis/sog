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

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class StringsTest extends Test.Container {
	
	private final int arrayMaxLength;
	
	
	private final String[] shortArray = new String[] { "A", "B", "C" };
	
	private final Collection<String> shortCollection = List.of( "A", "B", "C" );
	
	private final int FIRST = 4213;
	
	private final int LAST;
	
	private final Collection<Integer> longCollection;
	
	private final Integer[] longArray;
	
	public StringsTest() {
		super( Strings.class );
		
		this.arrayMaxLength = this.getSubjectField( null, "ARRAY_MAX_LENGTH", null );
		
		this.longCollection = IntStream.range( this.FIRST,  this.FIRST + 2 * this.arrayMaxLength )
			.mapToObj( Integer::valueOf ).collect( Collectors.toList() );
		this.longArray = this.longCollection.toArray( new Integer[] {} );
		this.LAST = this.longArray[ this.longArray.length  -1 ];
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Empty array allowed" 
	)
	public void tm_014AFD1A7( Test.Case tc ) {
		tc.assertNotEmpty( Strings.arrayToString( new Object[] {} ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Enclosed in square brackets" 
	)
	public void tm_0B270C477( Test.Case tc ) {
		String s = Strings.arrayToString( this.shortArray );
		tc.assertTrue( s.startsWith( "[" ) );
		tc.assertTrue( s.endsWith( "]" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "For long arrays first element is shown" 
	)
	public void tm_0CA58A2C5( Test.Case tc ) {
		String s = Strings.arrayToString( this.longArray );
		tc.assertTrue( s.contains( this.FIRST + ", " ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "For long arrays last element is shown" 
	)
	public void tm_096276709( Test.Case tc ) {
		String s = Strings.arrayToString( this.longArray );
		tc.assertTrue( s.contains( " " + this.LAST ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Omitted elements are indicated with 'more'" 
	)
	public void tm_06312C4CE( Test.Case tc ) {
		String s = Strings.arrayToString( this.longArray );
		tc.assertTrue( s.contains( "more" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Sample cases for arrays of arrays" 
	)
	public void tm_00A1A7140( Test.Case tc ) {
		Object[] empty = new String[] {};
		String emptyS = Strings.arrayToString( empty );

		String longS = Strings.arrayToString( this.longArray );
		
		Object[] object = new Object[] { new Object(), new Object() };
		String objectS = Strings.arrayToString( object );
		
		Object[] tf = new Boolean[] { true, false };
		String tfS = Strings.arrayToString( tf  );
		
		Object[] arg = new Object[] { empty, this.longArray, object, tf };
		String argS = Strings.arrayToString( arg );

		tc.assertTrue( argS.contains( emptyS ) );
		tc.assertTrue( argS.contains( longS ) );
		tc.assertTrue( argS.contains( objectS ) );
		tc.assertTrue( argS.contains( tfS ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Sample cases for arrays of collections" 
	)
	public void tm_037D4ADA5( Test.Case tc ) {
		Collection<Object> empty = List.of();
		String emptyS = Strings.collectionToString( empty );

		String longS = Strings.collectionToString( this.longCollection );
		
		Collection<Object> object = List.of( new Object(), new Object() );
		String objectS = Strings.collectionToString( object );
		
		Collection<Object> tf = Set.of( true, false );
		String tfS = Strings.collectionToString( tf  );
		
		Object[] arg = new Object[] { empty, this.longCollection, object, tf };
		String argS = Strings.arrayToString( arg );

		tc.assertTrue( argS.contains( emptyS ) );
		tc.assertTrue( argS.contains( longS ) );
		tc.assertTrue( argS.contains( objectS ) );
		tc.assertTrue( argS.contains( tfS ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Sample cases for arrays of primitive" 
	)
	public void tm_0D3F439B7( Test.Case tc ) {
		Integer[] ints = new Integer[] { 1, 34567, 2 };
		Boolean[] booleans = new Boolean[] { true, false };
		Character[] chars = new Character[] { '_', '#', '?' };
		
		tc.assertTrue( Strings.arrayToString( ints ).contains( "34567") );
		tc.assertTrue( Strings.arrayToString( booleans ).contains( "true") );
		tc.assertTrue( Strings.arrayToString( chars ).contains( "#") );
	}
		
	@Test.Impl( 
		member = "method: String Strings.arrayToString(Object[])", 
		description = "Throws AssertionError on null arrays" 
	)
	public void tm_079C8F674( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.arrayToString( null );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Empty collection allowed" 
	)
	public void tm_03309717C( Test.Case tc ) {
		tc.assertNotEmpty( Strings.collectionToString( List.of() ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Enclosed in set braces" 
	)
	public void tm_0D635ACD1( Test.Case tc ) {
		String s = Strings.collectionToString( this.shortCollection );
		tc.assertTrue( s.startsWith( "{" ) );
		tc.assertTrue( s.endsWith( "}" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "For long collections first element is shown" 
	)
	public void tm_0E2492FBA( Test.Case tc ) {
		String s = Strings.collectionToString( this.longCollection );
		tc.assertTrue( s.contains( this.FIRST + ", " ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "For long collections last element is shown" 
	)
	public void tm_07E26E774( Test.Case tc ) {
		String s = Strings.collectionToString( this.longCollection );
		tc.assertTrue( s.contains( " " + this.LAST ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Omitted elements are indicated with 'more'" 
	)
	public void tm_009DE1B54( Test.Case tc ) {
		String s = Strings.collectionToString( this.longCollection );
		tc.assertTrue( s.contains( "more" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Sample cases for collections of arrays" 
	)
	public void tm_033792411( Test.Case tc ) {
		Object[] empty = new String[] {};
		String emptyS = Strings.arrayToString( empty );

		String longS = Strings.arrayToString( this.longArray );
		
		Object[] object = new Object[] { new Object(), new Object() };
		String objectS = Strings.arrayToString( object );
		
		Object[] tf = new Boolean[] { true, false };
		String tfS = Strings.arrayToString( tf  );
		
		Collection<Object> arg = List.of( empty, this.longArray, object, tf );
		String argS = Strings.collectionToString( arg );

		tc.assertTrue( argS.contains( emptyS ) );
		tc.assertTrue( argS.contains( longS ) );
		tc.assertTrue( argS.contains( objectS ) );
		tc.assertTrue( argS.contains( tfS ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Sample cases for collections of collections" 
	)
	public void tm_05C7A1574( Test.Case tc ) {
		Collection<Object> empty = List.of();
		String emptyS = Strings.collectionToString( empty );

		String longS = Strings.collectionToString( this.longCollection );
		
		Collection<Object> object = List.of( new Object(), new Object() );
		String objectS = Strings.collectionToString( object );
		
		Collection<Object> tf = Set.of( true, false );
		String tfS = Strings.collectionToString( tf  );
		
		Collection<Object> arg = List.of( empty, this.longCollection, object, tf );
		String argS = Strings.collectionToString( arg );

		tc.assertTrue( argS.contains( emptyS ) );
		tc.assertTrue( argS.contains( longS ) );
		tc.assertTrue( argS.contains( objectS ) );
		tc.assertTrue( argS.contains( tfS ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Sample cases for collections of primitive" 
	)
	public void tm_0171F4946( Test.Case tc ) {
		Collection<Integer> ints = Set.of( 1, 34567, 2 );
		Collection<Boolean> booleans = List.of( true, false );
		Collection<Character> chars = Set.of( '_', '#', '?' );
		
		tc.assertTrue( Strings.collectionToString( ints ).contains( "34567") );
		tc.assertTrue( Strings.collectionToString( booleans ).contains( "true") );
		tc.assertTrue( Strings.collectionToString( chars ).contains( "#") );
	}
		
	@Test.Impl( 
		member = "method: String Strings.collectionToString(Collection)", 
		description = "Throws AssertionError on null collections" 
	)
	public void tm_058F89F77( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.collectionToString( null );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For large negative width return value ends with the pad character" 
	)
	public void tm_0250365B8( Test.Case tc ) {
		tc.assertTrue( Strings.justify( "Hello", -100, '#' ).endsWith( "#" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For large negative width return value starts with given string" 
	)
	public void tm_09195CCD8( Test.Case tc ) {
		tc.assertTrue( Strings.justify( "Hello", -100, '#' ).startsWith( "Hello" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For large positive width return value ends with given string" 
	)
	public void tm_0AD535D43( Test.Case tc ) {
		tc.assertTrue( Strings.justify( "Hello", 100, '$' ).endsWith( "Hello" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For large positive width return value starts with the pad character" 
	)
	public void tm_0899931BB( Test.Case tc ) {
		tc.assertTrue( Strings.justify( "Hello", 100, '$' ).startsWith( "$" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For negative width equal to opposite of length return is the given string" 
	)
	public void tm_0A05F0EBB( Test.Case tc ) {
		String arg = "This is the arbitray string with a tpyo";
		tc.assertEqual( arg,  Strings.justify( arg, -1 * arg.length(), '@' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For positive width equal to length return is the given string" 
	)
	public void tm_028E3D867( Test.Case tc ) {
		String arg = "This is the other arbitray string with a tpyo";
		tc.assertEqual( arg,  Strings.justify( arg, arg.length(), '*' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For small negative width given string starts with return value" 
	)
	public void tm_02C2F9B46( Test.Case tc ) {
		String arg = "Hellow world";
		tc.assertTrue( arg.startsWith( Strings.justify( arg, -3, ' ' ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "For small positive width given string ends with return value" 
	)
	public void tm_034C287F1( Test.Case tc ) {
		String arg = "Hellow world";
		tc.assertTrue( arg.endsWith( Strings.justify( arg, 3, ' ' ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "Length of return is specified width" 
	)
	public void tm_098599BF4( Test.Case tc ) {
		final String s = "A string of length 21";
		Stream.of( 10, 21, 42 ).forEach( n -> tc.assertEqual( n, Strings.justify( s, n, '@' ).length() ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "Return is empty for zero width" 
	)
	public void tm_03BD32501( Test.Case tc ) {
		tc.assertEqual( "", Strings.justify( "Hello world", 0, ' ' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.justify(String, int, char)", 
		description = "Throws AssertionError for null string" 
	)
	public void tm_02FFBBBED( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.justify( null, 10, '!' );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "For large width return value ends with the pad character" 
	)
	public void tm_02BA6292E( Test.Case tc ) {
		tc.assertTrue( Strings.leftJustify( "Hello", 20, '%' ).endsWith( "%" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "For large width return value starts with given string" 
	)
	public void tm_05FA8AD22( Test.Case tc ) {
		String s = "Hello world";
		tc.assertTrue( Strings.leftJustify( s, 40, '#' ).startsWith( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "For small width given string starts with return value" 
	)
	public void tm_02AEAFFA8( Test.Case tc ) {
		String s = "Hello world";
		tc.assertTrue( s.startsWith( Strings.leftJustify( s, 5, '$' ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "For width equal to length return is the given string" 
	)
	public void tm_0874C1377( Test.Case tc ) {
		String s = "Hello world";
		tc.assertEqual( s, Strings.leftJustify( s, s.length(),'1' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "Length of return is specified width" 
	)
	public void tm_09D2235DB( Test.Case tc ) {
		String s = "Length = 11";
		Stream.of( 4, 11, 15 ).forEach( n -> tc.assertEqual( n, Strings.leftJustify( s, n, '1' ).length() ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "Return is empty for zero width" 
	)
	public void tm_079ADB6BA( Test.Case tc ) {
		tc.assertEqual( "", Strings.leftJustify( "ha!", 0, '2' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.leftJustify(String, int, char)", 
		description = "Throws AssertionError for null string" 
	)
	public void tm_0DC2984A6( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.leftJustify( null, 2, '3' );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "For large width return value ends with given string" 
	)
	public void tm_0B6B1284C( Test.Case tc ) {
		String s = "Some string";
		tc.assertTrue( Strings.rightJustify( s, 100, '4' ).endsWith( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "For large width return value starts with the pad character" 
	)
	public void tm_0E31B9A92( Test.Case tc ) {
		tc.assertTrue( Strings.rightJustify( "Hello", 42, '~' ).startsWith( "~" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "For small width given string ends with return value" 
	)
	public void tm_0EECAFD12( Test.Case tc ) {
		String s = "Hello world";
		tc.assertTrue( s.endsWith( Strings.rightJustify( s, 5, '$' ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "For width equal to length return is the given string" 
	)
	public void tm_0AB1ABBD4( Test.Case tc ) {
		String s = "Some other string";
		tc.assertEqual( s, Strings.rightJustify( s, s.length(), '#' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "Length of return is specified width" 
	)
	public void tm_0201CF3DE( Test.Case tc ) {
		String s = "This string has length 25";
		Stream.of( 20, 25, 30 ).forEach( n -> tc.assertEqual( n, Strings.rightJustify( s, n, '3' ).length() ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "Return is empty for zero width" 
	)
	public void tm_0971F02D7( Test.Case tc ) {
		tc.assertEqual( "", Strings.rightJustify( "Hi", 0, '2' ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.rightJustify(String, int, char)", 
		description = "Throws AssertionError for null string" 
	)
	public void tm_0154F63C3( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.rightJustify( null, 10, '3' );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Identity for non quoted trimmed strings" 
	)
	public void tm_0A2E6B97C( Test.Case tc ) {
		Stream.of( 
			"Hello world",
			"fjds fsdkj fdkja ewir ufds j",
			"&@^ )*@&^(*^# 187987 SKJJ",
			"Internal ' \" quotes",
			"Internal  \t \n white space"
		).forEach( s -> tc.assertEqual( s, Strings.strip( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Identity on empty" 
	)
	public void tm_083F3318A( Test.Case tc ) {
		tc.assertEqual( "", Strings.strip( "" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Ignores unmatched quotes" 
	)
	public void tm_08C3E2725( Test.Case tc ) {
		Stream.of(
			"Ends with single quote'",
			"Ends with double quote\"",
			"\"Starts with double quote",
			"'Starts with single quote",
			"\"Double single'",
			"'Single double\""
		).forEach( s -> tc.assertEqual( s, Strings.strip( s ) ) );
	}
		
	private String quoteFilter( String s ) {
		return s.replaceAll( "@Test\\.Decl\\( ", "" ).replaceAll( "\\)",  "" );
	}
	
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Is idempotent" 
	)
	public void tm_00FE2BCCB( Test.Case tc ) throws IOException {
		Files.lines( App.get().sourceFile( Strings.class ) )
			.map( this::quoteFilter )
			.forEach( s -> tc.assertEqual( Strings.strip( s ), Strings.strip( Strings.strip( s ) ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Removes double quotes" 
	)
	public void tm_03BBF5911( Test.Case tc ) {
		String s = "Hello world";
		String q = "\"" + s + "\"";
		tc.assertEqual( s, Strings.strip( q ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Removes nested quotes" 
	)
	public void tm_09294DEAB( Test.Case tc ) {
		String s = "Hello world";
		String q = "\"'\"" + s + "\"'\"";
		tc.assertEqual( s, Strings.strip( q ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Removes single quotes" 
	)
	public void tm_0D54AE67A( Test.Case tc ) {
		String s = "Hello world";
		String q = "'" + s + "'";
		tc.assertEqual( s, Strings.strip( q ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Result is trimmed" 
	)
	public void tm_0B5FC23B3( Test.Case tc ) {
		String s = "Hello world";
		String w = " \t \n" + s + "  \t\n\t\t ";
		tc.assertEqual( s, Strings.strip( w ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Sample cases" 
	)
	public void tm_0F3CE2A47( Test.Case tc ) {
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
			
		for ( int i = 0; i < args.length; i++ ) {
			tc.assertEqual( expected[i],  Strings.strip( args[i] ) );
		}
	}
		
	@Test.Impl( 
		member = "method: String Strings.strip(String)", 
		description = "Throws AssertionError for null string" 
	)
	public void tm_0E8E94264( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.strip( null );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Does not start with a digit" 
	)
	public void tm_007645515( Test.Case tc ) {
		Stream.of( 
			"A 2-normal subgroup",
			"2-3 cisethylene",
			"1 a 2 b 0 0 0 destruct 0",
			"1 2 3 4",
			"1234",
			"1, 2, 3, a, b, c",
			"1 2 3 a b c",
			"",
			"1234 abcd"
		).forEach( s -> tc.assertFalse( Strings.toCamelCase( s ).matches( "^\\d" ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Identity on empty" 
	)
	public void tm_0D272FEBD( Test.Case tc ) {
		tc.assertEqual( "", Strings.toCamelCase( "" ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "If no alphabetic characters in input then return is empty" 
	)
	public void tm_01894C845( Test.Case tc ) {
		Stream.of( "1 2 3", "$ 7____2 &", "3215786123987" )
			.forEach( s -> tc.assertEqual( "", Strings.toCamelCase( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Result contains no white space" 
	)
	public void tm_066FCF835( Test.Case tc ) {
		Stream.of( 
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"\t\nLots         of\t\t\t\t\t\t\twhite\n\n\n\n\n\n\nspace    "
		)
			.map( Strings::toCamelCase )
			.forEach( s -> tc.assertFalse( Strings.toCamelCase( s ).matches( ".*\\s+.*" ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Result contains only letters" 
	)
	public void tm_04B201E9E( Test.Case tc ) {
		Stream.of( 
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"\t\nLots         of\t\t\t\t\t\t\twhite\n\n\n\n\n\n\nspace    ",
			"A 1 and a 2"
		)
			.map( Strings::toCamelCase )
			.forEach( s -> tc.assertTrue( Strings.toCamelCase( s ).matches( "^[A-Za-z0-9]*$" ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Result is trimmed" 
	)
	public void tm_0047BF0E6( Test.Case tc ) {
		Stream.of( 
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"\t\nLots         of\t\t\t\t\t\t\twhite\n\n\n\n\n\n\nspace    "
		)
			.map( Strings::toCamelCase )
			.forEach( s -> tc.assertEqual( s, s.trim() ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Sample cases" 
	)
	public void tm_0A739A934( Test.Case tc ) {
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
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Starts with uppercase" 
	)
	public void tm_000443D36( Test.Case tc ) {
		Stream.of( 
			"Gibberish!t\t dfk\tljg sv;lrjtl a;sljt;lA\n\nSEJ r;slej  l;krsetj tkl ",
			"Hello, world!",
			"Throws assertion error on null arrays",
			"\t\nLots         of\t\t\t\t\t\t\twhite\n\n\n\n\n\n\nspace    ",
			"A 1 and a 2",
			"Hello, world!",
			"a e i o u and sometimes y",
			"A, E, I, O, U",
			"onereallylongstring",
			"A 2-normal subgroup",
			"2-3 cisethylene",
			"1 a 2 b 0 0 0 destruct 0",
			"1, 2, 3, a, b, c",
			"1 2 3 a b c",
			"1234 abcd"
		).map( Strings::toCamelCase ).forEach( s -> tc.assertTrue( s.matches( "^[A-Z].*" ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Throws AssertionError for null string" 
	)
	public void tm_0D2DFC311( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Strings.toCamelCase( null );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toCamelCase(String)", 
		description = "Underscore removed" 
	)
	public void tm_0E81810A1( Test.Case tc ) {
		Stream.of(
			"Hello_world!",
			"a_e_i_o_u and sometimes y",
			"3.141_59",
			"_____A__E_I_ O U___",
			"_onereallylongstring_",
			"A 2_normal subgroup",
			"2_3_cisethylene",
			"1 a 2 b 0 0 0_destruct_0",
			"1_2_3_4",
			"_1234_",
			"1_2_3_a_b_c",
			"1234_abcd"
		).map( Strings::toCamelCase ).forEach( s -> tc.assertFalse( s.contains( "_" ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toHex(int)", 
		description = "Hex digits have natural representation" 
	)
	public void tm_05D0EB955( Test.Case tc ) {
		tc.assertEqual( "0x0000", Strings.toHex( 0 ) );
		tc.assertEqual( "0x0001", Strings.toHex( 1 ) );
		tc.assertEqual( "0x0002", Strings.toHex( 2 ) );
		tc.assertEqual( "0x0003", Strings.toHex( 3 ) );
		tc.assertEqual( "0x0004", Strings.toHex( 4 ) );
		tc.assertEqual( "0x0005", Strings.toHex( 5 ) );
		tc.assertEqual( "0x0006", Strings.toHex( 6 ) );
		tc.assertEqual( "0x0007", Strings.toHex( 7 ) );
		tc.assertEqual( "0x0008", Strings.toHex( 8 ) );
		tc.assertEqual( "0x0009", Strings.toHex( 9 ) );
		tc.assertEqual( "0x000A", Strings.toHex( 10 ) );
		tc.assertEqual( "0x000B", Strings.toHex( 11 ) );
		tc.assertEqual( "0x000C", Strings.toHex( 12 ) );
		tc.assertEqual( "0x000D", Strings.toHex( 13 ) );
		tc.assertEqual( "0x000E", Strings.toHex( 14 ) );
		tc.assertEqual( "0x000F", Strings.toHex( 15 ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toHex(int)", 
		description = "Positive integers up to 2^16 - 1 have representation" 
	)
	public void tm_0892C0999( Test.Case tc ) {
		int big = (int) Math.pow( 2, 16 ) - 1;
		tc.assertEqual( "0xFFFD", Strings.toHex( big - 2 ) );
		tc.assertEqual( "0xFFFE", Strings.toHex( big - 1) );
		tc.assertEqual( "0xFFFF", Strings.toHex( big ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toHex(int)", 
		description = "Integers greater than 2^16 - 1 wrap around" 
	)
	public void tm_09255570E( Test.Case tc ) {
		int big = (int) Math.pow( 2, 16 );
		tc.assertEqual( "0x0000", Strings.toHex( big ) );
		tc.assertEqual( "0x0001", Strings.toHex( big + 1 ) );
		tc.assertEqual( "0x0002", Strings.toHex( big + 2 ) );
	}

	@Test.Impl( 
		member = "method: String Strings.toHex(int)", 
		description = "Result is non-empty" 
	)
	public void tm_018FDAE82( Test.Case tc ) {
		tc.assertNotEmpty( Strings.toHex( 549841398 ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toString(Object)", 
		description = "Agrees with object to string for non array non collection" 
	)
	public void tm_0035E8C60( Test.Case tc ) {
		Stream.of(
			new Object(),
			new Object() { @Override public String toString() { return "FOO"; } },
			"ABC",
			42
		).forEach( s -> tc.assertEqual( s.toString(), Strings.toString( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toString(Object)", 
		description = "Identity on strings" 
	)
	public void tm_034B843B7( Test.Case tc ) throws IOException {
		Files.lines( App.get().sourceFile( StringsTest.class ) )
			.forEach( s -> tc.assertEqual( s, Strings.toString( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toString(Object)", 
		description = "Provides alternate string representation for arrays" 
	)
	public void tm_03422487D( Test.Case tc ) {
		String[] arg = new String[] { "A", "B" };
		tc.assertNotEqual( arg.toString(), Strings.toString( arg ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toString(Object)", 
		description = "Provides alternate string representation for collections" 
	)
	public void tm_08432D488( Test.Case tc ) {
		List<?> arg = List.of( 1, 2, 3 );
		tc.assertNotEqual( arg.toString(), Strings.toString( arg ) );
	}
		
	@Test.Impl( 
		member = "method: String Strings.toString(Object)", 
		description = "String representation of null is null" 
	)
	public void tm_0BC98197D( Test.Case tc ) {
		tc.assertEqual( "null", Strings.toString( null ) );
	}

    @Test.Impl( 
    	member = "method: String Strings.entryToString(Map.Entry)", 
    	description = "Includes string representations for the key and value" 
    )
    public void tm_093C974C6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Strings.entryToString(Map.Entry)", 
    	description = "Throws AssertionError for null entry" 
    )
    public void tm_002E52DB4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	

	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( Strings.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( Strings.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/

		App.get().done();
	}
}
