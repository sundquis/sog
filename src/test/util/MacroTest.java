/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import sog.core.AppException;
import sog.core.Test;
import sog.util.Macro;

/**
 * @author sundquis
 *
 */
public class MacroTest extends Test.Container {

	// Test implementations


	@Test.Impl( member = "public Macro Macro.expand(String, List)", description = "Any value may be empty" )
	public void expand_AnyValueMayBeEmpty( Test.Case tc ) {
		new Macro().expand( "X",  Arrays.asList( "A", "", "B" ) );
		tc.pass();
	}

	@Test.Impl( member = "public Macro Macro.expand(String, List)", description = "Empty value collection allowed" )
	public void expand_EmptyValueCollectionAllowed( Test.Case tc ) {
		new Macro().expand( "A",  new ArrayList<String>() );
		tc.pass();
	}

	@Test.Impl( member = "public Macro Macro.expand(String, List)", description = "Returns this instance" )
	public void expand_ReturnsThisInstance( Test.Case tc ) {
		Macro macro = new Macro();
		tc.assertEqual( macro,  macro.expand( "A",  new ArrayList<String>() ) );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, List)", description = "Throws assertion error for a null value" )
	public void expand_ThrowsAssertionErrorForANullValue( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "A",  Arrays.asList( "A", null, "B" ) );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, List)", description = "Throws assertion error for null collection of values" )
	public void expand_ThrowsAssertionErrorForNullCollectionOfValues( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		List<String> list = null;
		new Macro().expand( "A", list );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Any value may be empty" )
	public void expand_AnyValueMayBeEmpty_( Test.Case tc ) {
		new Macro().expand( "X",  new String[] { "A", "", "B" } );
		tc.pass();
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Dollar wihtin key not allowed" )
	public void expand_DollarWihtinKeyNotAllowed( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "$hi",  "hi" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Empty value sequence allowed" )
	public void expand_EmptyValueSequenceAllowed( Test.Case tc ) {
		new Macro().expand( "A",  new String[] {} );
		tc.pass();
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Key does not end with white space" )
	public void expand_KeyDoesNotEndWithWhiteSpace( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "foo ",  "" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Key does not start with white space" )
	public void expand_KeyDoesNotStartWithWhiteSpace( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( " foo",  "" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Left brace in key not allowed" )
	public void expand_LeftBraceInKeyNotAllowed( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "f{o",  "" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Replacement rules can be over-written" )
	public void expand_ReplacementRulesCanBeOverWritten( Test.Case tc ) {
		Macro macro = new Macro().expand( "A",  "B" );
		String before = macro.apply( "${A}" ).findFirst().orElse( "" );
		macro.expand( "A",  "C" );
		String after = macro.apply( "${A}" ).findFirst().orElse( "" );
		tc.assertFalse( before.equals( after ) );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Returns this instance" )
	public void expand_ReturnsThisInstance_( Test.Case tc ) {
		Macro macro = new Macro();
		tc.assertEqual( macro,  macro.expand( "A",  "" ) );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Right brace in key not allowed" )
	public void expand_RightBraceInKeyNotAllowed( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "a}a",  "a", "b" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Special characters in key allowed" )
	public void expand_SpecialCharactersInKeyAllowed( Test.Case tc ) {
		new Macro().expand( "a!@##%^&*()-_=+[];:',<.>/?",  "a", "b" );
		tc.pass();
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Throws AssertionError for empty key" )
	public void expand_ThrowsAssertionerrorForEmptyKey( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "",  "A", "S" );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Throws assertion error for a null value" )
	public void expand_ThrowsAssertionErrorForANullValue_( Test.Case tc ) {
		String[] values = null;
		tc.expectError( AssertionError.class );
		new Macro().expand( "A",  values );
	}

	@Test.Impl( member = "public Macro Macro.expand(String, String[])", description = "Whitespace in key is allowed" )
	public void expand_WhitespaceInKeyIsAllowed( Test.Case tc ) {
		new Macro().expand( "SPA  CE",  "A", "A" );
		tc.pass();
	}

	@Test.Impl( member = "public Macro()", description = "Stateless instance acts as identity function" )
	public void Macro_StatelessInstanceActsAsIdentityFunction( Test.Case tc ) {
		String line = "This is the input line.";
		tc.assertEqual( line, new Macro().apply( line ).findFirst().orElse( "FAIL" ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(List)", description = "An empty collection produces an empty stream" )
	public void apply_AnEmptyCollectionProducesAnEmptyStream( Test.Case tc ) {
		List<String> empty = new ArrayList<>();
		tc.assertEqual( 0L, new Macro().expand( "A",  "A" ).apply( empty ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(List)", description = "An empty expansion rule removes line from output stream" )
	public void apply_AnEmptyExpansionRuleRemovesLineFromOutputStream( Test.Case tc ) {
		tc.assertEqual( 0L, new Macro().expand( "x",  new String[] {} ).apply( "A line ${x}." ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Can trigger recursion exception through excessive replacements" )
	public void apply_CanTriggerRecursionExceptionThroughExcessiveReplacements( Test.Case tc ) {
		Macro macro = new Macro();
		int iterations = this.getSubjectField( macro, "MAX_ITERATIONS", 0 );
		for ( int i = 0; i < iterations; i++ ) {
			macro.expand( "x" + i,  "${x" + (i+1) + "}" );
		}
		macro.expand( "x" + iterations,  "Done" );
		tc.assertEqual( "Done", macro.apply( "${x1}" ).findFirst().orElse("FAIL") );
		tc.expectError( AppException.class );
		macro.apply( "${x0}" );
	}

	@Test.Impl( member = "public Stream Macro.apply(List)", description = "Multiple lines in output for multiple input lines" )
	public void apply_MultipleLinesInOutputForMultipleInputLines( Test.Case tc ) {
		List<String> lines = Arrays.asList( "First ${x}", "Second ${x}" );
		tc.assertEqual( (long) lines.size(),  new Macro().expand( "x",  "text" ).apply( lines ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(List)", description = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion( Test.Case tc ) {
		Macro macro = new Macro().expand( "X",  "${Y}" ).expand( "Y",  "${X}" );
		tc.expectError( AppException.class );
		macro.apply( Arrays.asList( "{X}", "Good", "${X}", "Good" ) ).count();
	}

	@Test.Impl( member = "public Stream Macro.apply(List)", description = "Throws Assertion Error for null lines" )
	public void apply_ThrowsAssertionErrorForNullLines( Test.Case tc ) {
		List<String> lines = null;
		tc.expectError( AssertionError.class );
		new Macro().apply( lines );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "All keys replaced in output stream" )
	public void apply_AllKeysReplacedInOutputStream( Test.Case tc ) {
		String result = new Macro().expand( "A",  "plain" ).apply( "${A}  ${A}  ${A}" ).findFirst().orElse("FAIL");
		tc.assertFalse( result.contains( "${A}" ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "An empty expansion rule produces an empty stream" )
	public void apply_AnEmptyExpansionRuleProducesAnEmptyStream( Test.Case tc ) {
		tc.assertEqual( 0L, new Macro().expand( "X",  new String[] {} ).apply( "${X} ${X}" ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Input line without keys is unchanged" )
	public void apply_InputLineWithoutKeysIsUnchanged( Test.Case tc ) {
		String input = "A random string &!#(@*&$!&@%*@!#)(*";
		tc.assertEqual( input,  new Macro().apply( input ).findFirst().orElse( "FAIL" ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Mal-formed patterns are ignored" )
	public void apply_MalFormedPatternsAreIgnored( Test.Case tc ) {
		String replacement = "DONE";
		Macro macro = new Macro().expand( "key", replacement );
		String good = "${key}";
		String[] malformed = new String[] {
			"{$key}",
			"{ke$y}",
			"{key$}",
			"{key}$",
			"$k{ey}",
			"$key{}",
			"$key}{",
			"$}{key",
			"${}"
		};
		for ( String bad : malformed ) {
			tc.assertEqual( bad + replacement, macro.apply( bad + good ).findFirst().orElse( "FAIL" ) );
		}
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Multiple keys replaced" )
	public void apply_MultipleKeysReplaced( Test.Case tc ) {
		Macro macro = new Macro().expand( "key1", "value1" ).expand( "key2", "value2" );
		Stream<String> stream = Stream.of( "${key1}  ${key2}", "No subs", "${key2}", "${key1}", "${key2}  ${key1}")
			.flatMap( macro );
		tc.assertTrue( stream.allMatch( s -> !s.contains( "$" ) ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Multiple lines in output for multiple replacements" )
	public void apply_MultipleLinesInOutputForMultipleReplacements( Test.Case tc ) {
		tc.assertEqual( 6L,  new Macro()
			.expand( "A",  "2", "2" ).expand( "B",  "3", "3", "3" ).apply( "${A}${B}" ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "One letter keys work" )
	public void apply_OneLetterKeysWork( Test.Case tc ) {
		tc.assertEqual( 6,  new Macro().expand( "X",  "XXX" ).apply( "${X}${X}" ).findFirst().orElse( "" ).length() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Replacement within replacement allowed" )
	public void apply_ReplacementWithinReplacementAllowed( Test.Case tc ) {
		Macro macro = new Macro().expand( "OUTER", "Done" ).expand( "INNER",  "OUT" );
		tc.assertEqual( "Done", macro.apply( "${${INNER}ER}" ).findFirst().orElse( "FAIL" )  );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Simple replacement works" )
	public void apply_SimpleReplacementWorks( Test.Case tc ) {
		Macro macro = new Macro().expand( "simple replacement", "Done" );
		tc.assertEqual( "Done", macro.apply( "${simple replacement}" ).findFirst().orElse( "FAIL" ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion_( Test.Case tc ) {
		Macro macro = new Macro().expand( "X",  "${Y}" ).expand( "Y",  "${X}" );
		tc.expectError( AppException.class );
		macro.apply( "Start: ${X}" );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Throws AppExcpetion for missing key" )
	public void apply_ThrowsAppexcpetionForMissingKey( Test.Case tc ) {
		tc.expectError( AppException.class );
		new Macro().apply( "${missing}" );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Throws AssertionError for null line" )
	public void apply_ThrowsAssertionerrorForNullLine( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		String line = null;
		new Macro().apply( line );
	}

	@Test.Impl( member = "public Stream Macro.apply(String)", description = "Works with flatMap to expand a stream" )
	public void apply_WorksWithFlatmapToExpandAStream( Test.Case tc ) {
		Macro macro = new Macro().expand( "x",  "replaced" );
		tc.assertEqual( 4L, Stream.of( "${x}", "${x}", "${x}", "${x}" ).flatMap( macro ).count() );
		tc.assertTrue( Stream.of( "${x}", "${x}", "${x}", "${x}" ).flatMap( macro ).allMatch( s -> "replaced".equals(s) ) );
		macro.expand( "x",  "replaced", "replaced" );
		tc.assertEqual( 4L, Stream.of( "${x}", "${x}" ).flatMap( macro ).count() );
		tc.assertTrue( Stream.of( "${x}", "${x}" ).flatMap( macro ).allMatch( s -> "replaced".equals(s) ) );
	}

	@Test.Impl( member = "public Stream Macro.apply(String[])", description = "An empty expansion rule removes line from output stream" )
	public void apply_AnEmptyExpansionRuleRemovesLineFromOutputStream_( Test.Case tc ) {
		Macro macro = new Macro().expand( "x",  "replaced" ).expand( "y",  new String[] {} );
		tc.assertEqual( 3L, macro.apply( "${x}", "${y}", "${x}", "${x}" ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String[])", description = "An empty sequence of lines produces an empty stream" )
	public void apply_AnEmptySequenceOfLinesProducesAnEmptyStream( Test.Case tc ) {
		tc.assertEqual( 0L,  new Macro().apply( new String[] {} ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String[])", description = "Multiple lines in output for multiple input lines" )
	public void apply_MultipleLinesInOutputForMultipleInputLines_( Test.Case tc ) {
		tc.assertEqual( 3L,  new Macro().apply( "1", "2", "3" ).count() );
	}

	@Test.Impl( member = "public Stream Macro.apply(String[])", description = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion__( Test.Case tc ) {
		tc.expectError( AppException.class );
		new Macro().expand( "x",  "${x}" ).apply( "v", "w", "${x}" ).count();
	}

	
	
}
