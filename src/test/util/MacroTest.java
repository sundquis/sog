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
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.Macro;

/**
 * @author sundquis
 *
 */
public class MacroTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Macro.class;
	}

	// Test implementations


	@TestOrig.Impl( src = "public Macro Macro.expand(String, List)", desc = "Any value may be empty" )
	public void expand_AnyValueMayBeEmpty( TestCase tc ) {
		new Macro().expand( "X",  Arrays.asList( "A", "", "B" ) );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, List)", desc = "Empty value collection allowed" )
	public void expand_EmptyValueCollectionAllowed( TestCase tc ) {
		new Macro().expand( "A",  new ArrayList<String>() );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, List)", desc = "Returns this instance" )
	public void expand_ReturnsThisInstance( TestCase tc ) {
		Macro macro = new Macro();
		tc.assertEqual( macro,  macro.expand( "A",  new ArrayList<String>() ) );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, List)", desc = "Throws assertion error for a null value" )
	public void expand_ThrowsAssertionErrorForANullValue( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "A",  Arrays.asList( "A", null, "B" ) );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, List)", desc = "Throws assertion error for null collection of values" )
	public void expand_ThrowsAssertionErrorForNullCollectionOfValues( TestCase tc ) {
		tc.expectError( AssertionError.class );
		List<String> list = null;
		new Macro().expand( "A", list );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Any value may be empty" )
	public void expand_AnyValueMayBeEmpty_( TestCase tc ) {
		new Macro().expand( "X",  new String[] { "A", "", "B" } );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Dollar wihtin key not allowed" )
	public void expand_DollarWihtinKeyNotAllowed( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "$hi",  "hi" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Empty value sequence allowed" )
	public void expand_EmptyValueSequenceAllowed( TestCase tc ) {
		new Macro().expand( "A",  new String[] {} );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Key does not end with white space" )
	public void expand_KeyDoesNotEndWithWhiteSpace( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "foo ",  "" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Key does not start with white space" )
	public void expand_KeyDoesNotStartWithWhiteSpace( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( " foo",  "" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Left brace in key not allowed" )
	public void expand_LeftBraceInKeyNotAllowed( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "f{o",  "" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Replacement rules can be over-written" )
	public void expand_ReplacementRulesCanBeOverWritten( TestCase tc ) {
		Macro macro = new Macro().expand( "A",  "B" );
		String before = macro.apply( "${A}" ).findFirst().orElse( "" );
		macro.expand( "A",  "C" );
		String after = macro.apply( "${A}" ).findFirst().orElse( "" );
		tc.assertFalse( before.equals( after ) );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Returns this instance" )
	public void expand_ReturnsThisInstance_( TestCase tc ) {
		Macro macro = new Macro();
		tc.assertEqual( macro,  macro.expand( "A",  "" ) );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Right brace in key not allowed" )
	public void expand_RightBraceInKeyNotAllowed( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "a}a",  "a", "b" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Special characters in key allowed" )
	public void expand_SpecialCharactersInKeyAllowed( TestCase tc ) {
		new Macro().expand( "a!@##%^&*()-_=+[];:',<.>/?",  "a", "b" );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Throws AssertionError for empty key" )
	public void expand_ThrowsAssertionerrorForEmptyKey( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Macro().expand( "",  "A", "S" );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Throws assertion error for a null value" )
	public void expand_ThrowsAssertionErrorForANullValue_( TestCase tc ) {
		String[] values = null;
		tc.expectError( AssertionError.class );
		new Macro().expand( "A",  values );
	}

	@TestOrig.Impl( src = "public Macro Macro.expand(String, String[])", desc = "Whitespace in key is allowed" )
	public void expand_WhitespaceInKeyIsAllowed( TestCase tc ) {
		new Macro().expand( "SPA  CE",  "A", "A" );
		tc.pass();
	}

	@TestOrig.Impl( src = "public Macro()", desc = "Stateless instance acts as identity function" )
	public void Macro_StatelessInstanceActsAsIdentityFunction( TestCase tc ) {
		String line = "This is the input line.";
		tc.assertEqual( line, new Macro().apply( line ).findFirst().orElse( "FAIL" ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(List)", desc = "An empty collection produces an empty stream" )
	public void apply_AnEmptyCollectionProducesAnEmptyStream( TestCase tc ) {
		List<String> empty = new ArrayList<>();
		tc.assertEqual( 0L, new Macro().expand( "A",  "A" ).apply( empty ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(List)", desc = "An empty expansion rule removes line from output stream" )
	public void apply_AnEmptyExpansionRuleRemovesLineFromOutputStream( TestCase tc ) {
		tc.assertEqual( 0L, new Macro().expand( "x",  new String[] {} ).apply( "A line ${x}." ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Can trigger recursion exception through excessive replacements" )
	public void apply_CanTriggerRecursionExceptionThroughExcessiveReplacements( TestCase tc ) {
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

	@TestOrig.Impl( src = "public Stream Macro.apply(List)", desc = "Multiple lines in output for multiple input lines" )
	public void apply_MultipleLinesInOutputForMultipleInputLines( TestCase tc ) {
		List<String> lines = Arrays.asList( "First ${x}", "Second ${x}" );
		tc.assertEqual( (long) lines.size(),  new Macro().expand( "x",  "text" ).apply( lines ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(List)", desc = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion( TestCase tc ) {
		Macro macro = new Macro().expand( "X",  "${Y}" ).expand( "Y",  "${X}" );
		tc.expectError( AppException.class );
		macro.apply( Arrays.asList( "{X}", "Good", "${X}", "Good" ) ).count();
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(List)", desc = "Throws Assertion Error for null lines" )
	public void apply_ThrowsAssertionErrorForNullLines( TestCase tc ) {
		List<String> lines = null;
		tc.expectError( AssertionError.class );
		new Macro().apply( lines );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "All keys replaced in output stream" )
	public void apply_AllKeysReplacedInOutputStream( TestCase tc ) {
		String result = new Macro().expand( "A",  "plain" ).apply( "${A}  ${A}  ${A}" ).findFirst().orElse("FAIL");
		tc.assertFalse( result.contains( "${A}" ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "An empty expansion rule produces an empty stream" )
	public void apply_AnEmptyExpansionRuleProducesAnEmptyStream( TestCase tc ) {
		tc.assertEqual( 0L, new Macro().expand( "X",  new String[] {} ).apply( "${X} ${X}" ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Input line without keys is unchanged" )
	public void apply_InputLineWithoutKeysIsUnchanged( TestCase tc ) {
		String input = "A random string &!#(@*&$!&@%*@!#)(*";
		tc.assertEqual( input,  new Macro().apply( input ).findFirst().orElse( "FAIL" ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Mal-formed patterns are ignored" )
	public void apply_MalFormedPatternsAreIgnored( TestCase tc ) {
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

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Multiple keys replaced" )
	public void apply_MultipleKeysReplaced( TestCase tc ) {
		Macro macro = new Macro().expand( "key1", "value1" ).expand( "key2", "value2" );
		Stream<String> stream = Stream.of( "${key1}  ${key2}", "No subs", "${key2}", "${key1}", "${key2}  ${key1}")
			.flatMap( macro );
		tc.assertTrue( stream.allMatch( s -> !s.contains( "$" ) ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Multiple lines in output for multiple replacements" )
	public void apply_MultipleLinesInOutputForMultipleReplacements( TestCase tc ) {
		tc.assertEqual( 6L,  new Macro()
			.expand( "A",  "2", "2" ).expand( "B",  "3", "3", "3" ).apply( "${A}${B}" ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "One letter keys work" )
	public void apply_OneLetterKeysWork( TestCase tc ) {
		tc.assertEqual( 6,  new Macro().expand( "X",  "XXX" ).apply( "${X}${X}" ).findFirst().orElse( "" ).length() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Replacement within replacement allowed" )
	public void apply_ReplacementWithinReplacementAllowed( TestCase tc ) {
		Macro macro = new Macro().expand( "OUTER", "Done" ).expand( "INNER",  "OUT" );
		tc.assertEqual( "Done", macro.apply( "${${INNER}ER}" ).findFirst().orElse( "FAIL" )  );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Simple replacement works" )
	public void apply_SimpleReplacementWorks( TestCase tc ) {
		Macro macro = new Macro().expand( "simple replacement", "Done" );
		tc.assertEqual( "Done", macro.apply( "${simple replacement}" ).findFirst().orElse( "FAIL" ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion_( TestCase tc ) {
		Macro macro = new Macro().expand( "X",  "${Y}" ).expand( "Y",  "${X}" );
		tc.expectError( AppException.class );
		macro.apply( "Start: ${X}" );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Throws AppExcpetion for missing key" )
	public void apply_ThrowsAppexcpetionForMissingKey( TestCase tc ) {
		tc.expectError( AppException.class );
		new Macro().apply( "${missing}" );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Throws AssertionError for null line" )
	public void apply_ThrowsAssertionerrorForNullLine( TestCase tc ) {
		tc.expectError( AssertionError.class );
		String line = null;
		new Macro().apply( line );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String)", desc = "Works with flatMap to expand a stream" )
	public void apply_WorksWithFlatmapToExpandAStream( TestCase tc ) {
		Macro macro = new Macro().expand( "x",  "replaced" );
		tc.assertEqual( 4L, Stream.of( "${x}", "${x}", "${x}", "${x}" ).flatMap( macro ).count() );
		tc.assertTrue( Stream.of( "${x}", "${x}", "${x}", "${x}" ).flatMap( macro ).allMatch( s -> "replaced".equals(s) ) );
		macro.expand( "x",  "replaced", "replaced" );
		tc.assertEqual( 4L, Stream.of( "${x}", "${x}" ).flatMap( macro ).count() );
		tc.assertTrue( Stream.of( "${x}", "${x}" ).flatMap( macro ).allMatch( s -> "replaced".equals(s) ) );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String[])", desc = "An empty expansion rule removes line from output stream" )
	public void apply_AnEmptyExpansionRuleRemovesLineFromOutputStream_( TestCase tc ) {
		Macro macro = new Macro().expand( "x",  "replaced" ).expand( "y",  new String[] {} );
		tc.assertEqual( 3L, macro.apply( "${x}", "${y}", "${x}", "${x}" ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String[])", desc = "An empty sequence of lines produces an empty stream" )
	public void apply_AnEmptySequenceOfLinesProducesAnEmptyStream( TestCase tc ) {
		tc.assertEqual( 0L,  new Macro().apply( new String[] {} ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String[])", desc = "Multiple lines in output for multiple input lines" )
	public void apply_MultipleLinesInOutputForMultipleInputLines_( TestCase tc ) {
		tc.assertEqual( 3L,  new Macro().apply( "1", "2", "3" ).count() );
	}

	@TestOrig.Impl( src = "public Stream Macro.apply(String[])", desc = "Throws AppException for excessive recursion" )
	public void apply_ThrowsAppexceptionForExcessiveRecursion__( TestCase tc ) {
		tc.expectError( AppException.class );
		new Macro().expand( "x",  "${x}" ).apply( "v", "w", "${x}" ).count();
	}

	
	
	
	
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(MacroTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
