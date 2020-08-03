/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;


import java.util.function.Function;

import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.FixedWidth;

/**
 * @author sundquis
 *
 */
public class FixedWidthTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return FixedWidth.class;
	}
	
	private FixedWidth formatter;
	
	@Override
	public Procedure beforeEach() {
		return () -> formatter = new FixedWidth();
	}
	
	@Override
	public Procedure afterEach() {
		return () -> formatter = null;
	}

	// Test implementations
	
	
	// defaultFieldSeparator(String)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "Applied to remaining fields" )
	public void defaultFieldSeparator_AppliedToRemainingFields( TestCase tc ) {
		this.formatter.right( 3,  '0' ).sep( " " ).right( 3,  '0' )
			.defaultFieldSeparator("__").right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001 002__003__004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "Can be emtpty" )
	public void defaultFieldSeparator_CanBeEmtpty( TestCase tc ) {
		this.formatter.right( 3,  '0' ).sep( " " ).right( 3,  '0' )
			.defaultFieldSeparator("").right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001 002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "If not specified, default is empty string" )
	public void defaultFieldSeparator_IfNotSpecifiedDefaultIsEmptyString( TestCase tc ) {
		this.formatter.right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "Overrides default empty" )
	public void defaultFieldSeparator_OverridesDefaultEmpty( TestCase tc ) {
		this.formatter.defaultFieldSeparator("X").right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001X002X003X004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "Previous fields unaffected" )
	public void defaultFieldSeparator_PreviousFieldsUnaffected( TestCase tc ) {
		this.formatter.right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).defaultFieldSeparator("X");
		tc.assertEqual( "001002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", desc = "Throws assertion error for null" )
	public void defaultFieldSeparator_ThrowsAssertionErrorForNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.defaultFieldSeparator( null );
	}

	
	
	
	// left(int, char)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char)", desc = "Appends left justified field" )
	public void left_AppendsLeftJustifiedField( TestCase tc ) {
		this.formatter.right( 3,  '0' );
		String before = this.formatter.format( "FOO" );
		this.formatter.left( 3,  ' ' );
		String after = formatter.format( "FOO", 2 );
		tc.assertEqual( before + "2  " , after );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char)", desc = "Padding character is used" )
	public void left_PaddingCharacterIsUsed( TestCase tc ) {
		this.formatter.left( 5,  'X' );
		tc.assertEqual( "FOOXX",  this.formatter.format( "FOO" ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char)", desc = "Width must be positive" )
	public void left_WidthMustBePositive( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.left( -1,  '0' );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char)", desc = "Truncated for long" )
	public void left_TruncatedForLong( TestCase tc ) {
		tc.assertEqual( 10,  this.formatter.left( 10,  '0' ).format( "A string that is too long" ).length() );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char)", desc = "Width is correct for short" )
	public void left_WidthIsCorrectForShort( TestCase tc ) {
		tc.assertEqual( 10,  this.formatter.left( 10,  '0' ).format( "short" ).length() );
	}


	
	// left(int, char, Function)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char, Function)", desc = "Other fields unaffected" )
	public void left_OtherFieldsUnaffected( TestCase tc ) {
		Function<Integer, String> inc = (x) -> ((x+1) + "");
		this.formatter.left( 3,  '0' ).left( 3, '0', inc ).left( 3,  '0' );
		tc.assertEqual( "100200100",  this.formatter.format( 1, 1, 1 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char, Function)", desc = "Representation overrides toString() for this field" )
	public void left_RepresentationOverridesTostringForThisField( TestCase tc ) {
		Function<Integer, String> foo = (x) -> "FOO";
		this.formatter.left( 3,  'X', foo );
		tc.assertEqual( "FOO",  this.formatter.format(42) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.left(int, char, Function)", desc = "Throws assertion error for null representation" )
	public void left_ThrowsAssertionErrorForNullRepresentation( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.left( 3, '0', null );
	}

	
	

	// nullFields(String)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "Applied to remaining fields" )
	public void nullFields_AppliedToRemainingFields( TestCase tc ) {
		this.formatter.left( 4,  '0' ).nullFields( "null" ).left( 4,  '0' ).left( 4,  '0' );
		tc.assertEqual( "0000nullnull", this.formatter.format( null, null, null ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "Can be empty" )
	public void nullFields_CanBeEmpty( TestCase tc ) {
		this.formatter.nullFields( "" ).left( 4,  '0' );
		tc.assertEqual( "0000",  this.formatter.format( new Object[] {null} ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "If not specified, default is empty string" )
	public void nullFields_IfNotSpecifiedDefaultIsEmptyString( TestCase tc ) {
		this.formatter.left( 4,  ' ' );
		tc.assertEqual( "    ",  this.formatter.format( new Object[] {null} ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "Overrides default empty" )
	public void nullFields_OverridesDefaultEmpty( TestCase tc ) {
		this.formatter.nullFields( "FOO" ).left( 3,  '0' );
		tc.assertEqual( "FOO",  this.formatter.format( new Object[] {null} ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "Previous fields unaffected" )
	public void nullFields_PreviousFieldsUnaffected( TestCase tc ) {
		this.formatter.left( 3,  '0' ).nullFields( "FOO" ).left( 3,  '0' );
		tc.assertEqual( "000FOO",  this.formatter.format( null, null ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.nullFields(String)", desc = "Throws assertion error for null" )
	public void nullFields_ThrowsAssertionErrorForNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.nullFields( null );
	}

	
	
	
	// right(int, char)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char)", desc = "Appends right justified field" )
	public void right_AppendsRightJustifiedField( TestCase tc ) {
		this.formatter.left( 3,  '0' );
		String before = this.formatter.format( "FOO" );
		this.formatter.right( 3,  ' ' );
		String after = formatter.format( "FOO", 2 );
		tc.assertEqual( before + "  2" , after );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char)", desc = "Padding character is used" )
	public void right_PaddingCharacterIsUsed( TestCase tc ) {
		this.formatter.right( 5,  'X' );
		tc.assertEqual( "XXFOO",  this.formatter.format( "FOO" ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char)", desc = "Width must be positive" )
	public void right_WidthMustBePositive( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.right( -1,  '0' );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char)", desc = "Truncated for long" )
	public void right_TruncatedForLong( TestCase tc ) {
		tc.assertEqual( 10,  this.formatter.right( 10,  '0' ).format( "A string that is too long" ).length() );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char)", desc = "Width is correct for short" )
	public void right_WidthIsCorrectForShort( TestCase tc ) {
		tc.assertEqual( 10,  this.formatter.right( 10,  '0' ).format( "short" ).length() );
	}


	
	
	
	// right(int, char, Function)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char, Function)", desc = "Other fields unaffected" )
	public void right_OtherFieldsUnaffected( TestCase tc ) {
		Function<Integer, String> inc = (x) -> ((x+1) + "");
		this.formatter.right( 3,  '0' ).right( 3, '0', inc ).right( 3,  '0' );
		tc.assertEqual( "001002001",  this.formatter.format( 1, 1, 1 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char, Function)", desc = "Representation overrides toString() for this field" )
	public void right_RepresentationOverridesTostringForThisField( TestCase tc ) {
		Function<Integer, String> foo = (x) -> "FOO";
		this.formatter.right( 3,  'X', foo );
		tc.assertEqual( "FOO",  this.formatter.format(42) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.right(int, char, Function)", desc = "Throws assertion error for null representation" )
	public void right_ThrowsAssertionErrorForNullRepresentation( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.right( 3, '0', null );
	}

	
	
	
	
	// sep(String)
	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Appends if last" )
	public void sep_AppendsIfLast( TestCase tc ) {
		this.formatter.right( 4,  '0' ).sep( "FOO" );
		tc.assertEqual( "0001FOO",  this.formatter.format( 1 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Can be empty" )
	public void sep_CanBeEmpty( TestCase tc ) {
		this.formatter.right( 4,  '0' ).sep( "" ).right( 4,  '0' );
		tc.assertEqual( "00010002",  this.formatter.format( 1, 2 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Does not affect subsequent separators" )
	public void sep_DoesNotAffectSubsequentSeparators( TestCase tc ) {
		this.formatter.right( 4,  '0' ).sep( "BAR" ).right( 4,  '0' ).right( 4,  '0' ).right( 4,  '0' );
		tc.assertEqual( "0001BAR000200030004", this.formatter.format( 1, 2, 3, 4 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Multiple separators allowed" )
	public void sep_MultipleSeparatorsAllowed( TestCase tc ) {
		this.formatter.right( 4,  ' ' ).sep( "FOO" ).sep( " " ).sep( "BAR" ).right( 4,  ' ' );
		tc.assertEqual( "   1FOO BAR   2",  this.formatter.format( 1, 2 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Overrides default" )
	public void sep_OverridesDefault( TestCase tc ) {
		this.formatter.defaultFieldSeparator( "FOO" )
			.right( 3,  '_' ).sep( "BAR" ).right( 3,  '_' );
		tc.assertEqual( "__1BAR__2",  this.formatter.format( 1, 2 ) );
	}

	@TestOrig.Impl( src = "public FixedWidth FixedWidth.sep(String)", desc = "Throws assertion error for null separator" )
	public void sep_ThrowsAssertionErrorForNullSeparator( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.sep( null );
	}

	
	
	
	// FixedWidth()
	@TestOrig.Impl( src = "public FixedWidth()", desc = "Empty formatter produces empty formatted string" )
	public void FixedWidth_EmptyFormatterProducesEmptyFormattedString( TestCase tc ) {
		tc.assertEqual( "",  this.formatter.format() );
	}

	
	
	
	// format(Object[])
	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Fields are separated as specified" )
	public void format_FieldsAreSeparatedAsSpecified( TestCase tc ) {
		this.formatter.right( 5,  ' ' ).sep( "---" ).left( 5,  ' ' );
		tc.assertEqual( "    >---<    ",  this.formatter.format( ">", "<" ) );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Fields can be added after formatting" )
	public void format_FieldsCanBeAddedAfterFormatting( TestCase tc ) {
		String before = this.formatter.right( 3,  'x' ).format( ">>>" );
		String after = this.formatter.left( 3,  'x' ).format( ">>>", "<<<" );
		tc.assertEqual( before + "<<<",  after );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Fields formatted in the order specified" )
	public void format_FieldsFormattedInTheOrderSpecified( TestCase tc ) {
		this.formatter.defaultFieldSeparator(" ").right( 1,  '0' ).right( 2,  '0' ).right( 3,  '0' );
		tc.assertEqual( "1 02 003", this.formatter.format( 1, 2, 3 ) );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Non null fields represented appropriately" )
	public void format_NonNullFieldsRepresentedAppropriately( TestCase tc ) {
		this.formatter.defaultFieldSeparator( " " )
			.nullFields( "n" ).right( 4, '0' )
			.nullFields( "ni" ).right( 4,  '0' )
			.nullFields( "nil" ).right( 4,  '0' );
		tc.assertEqual( "000n 00ni 0nil",  this.formatter.format( null, null, null ) );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Null fields use null representation" )
	public void format_NullFieldsUseNullRepresentation( TestCase tc ) {
		this.formatter.nullFields( "use null" ).right( 8,  ' ' );
		tc.assertEqual( "use null",  this.formatter.format( new Object[] {null} ) );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Throws illegal argument exception for too few fields" )
	public void format_ThrowsIllegalArgumentExceptionForTooFewFields( TestCase tc ) {
		this.formatter.right( 1,  ' ' ).left( 2,  ' ' );
		this.formatter.format( 1, 2 );
		tc.expectError( IllegalArgumentException.class );
		this.formatter.format( 1 );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Throws illegal argument exception for too many fields" )
	public void format_ThrowsIllegalArgumentExceptionForTooManyFields( TestCase tc ) {
		this.formatter.right( 1,  ' ' ).left( 2,  ' ' );
		this.formatter.format( 1, 2 );
		tc.expectError( IllegalArgumentException.class );
		this.formatter.format( 1, 2, 3 );
	}

	@TestOrig.Impl( src = "public String FixedWidth.format(Object[])", desc = "Throws class cast exception for wron argument type" )
	public void format_ThrowsClassCastExceptionForWronArgumentType( TestCase tc ) {
		Function<String, String> rep = s -> s + "rep";
		this.formatter.left( 10,  ' ', rep );
		tc.expectError( ClassCastException.class );
		this.formatter.format( 1 );
	}


	
	
	
	

	// length()
	@TestOrig.Impl( src = "public int FixedWidth.length()", desc = "Includes fields" )
	public void length_IncludesFields( TestCase tc ) {
		this.formatter.right( 14,  ' ' ).left( 14,  ' ' ).right( 14,  ' ' );
		tc.assertEqual( 42,  this.formatter.length() );
	}

	@TestOrig.Impl( src = "public int FixedWidth.length()", desc = "Includes separators" )
	public void length_IncludesSeparators( TestCase tc ) {
		this.formatter.right( 20,  ' ' ).sep( "12" ).left( 20,  ' ' );
		tc.assertEqual( 42,  this.formatter.length() );
	}

	@TestOrig.Impl( src = "public int FixedWidth.length()", desc = "Increases when field appended" )
	public void length_IncreasesWhenFieldAppended( TestCase tc ) {
		int before = this.formatter.right( 5,  'c' ).length();
		int after = this.formatter.left( 5,  'c' ).length();
		tc.assertEqual( before + 5,  after );
	}

	@TestOrig.Impl( src = "public int FixedWidth.length()", desc = "Increses when separator appended" )
	public void length_IncresesWhenSeparatorAppended( TestCase tc ) {
		int before = this.formatter.right( 5,  'c' ).length();
		int after = this.formatter.sep( "12345" ).length();
		tc.assertEqual( before + 5,  after );
	}
	
	
	
	// toString()
	@TestOrig.Impl( src = "public String FixedWidth.toString()", desc = "Includes field count" )
	public void toString_IncludesFieldCount( TestCase tc ) {
		for ( int i = 0; i < 42; i++ ) {
			this.formatter.right( 1,  '.' );
		}
		tc.assertTrue( this.formatter.toString().contains( "42 fields" ) );
	}

	@TestOrig.Impl( src = "public String FixedWidth.toString()", desc = "Includes length" )
	public void toString_IncludesLength( TestCase tc ) {
		for ( int i = 0; i < 21; i++ ) {
			this.formatter.right( 1,  '.' ).sep( " " );
		}
		tc.assertTrue( this.formatter.toString().contains( "length = 42" ) );
	}


	

	public static void main(String[] args) {

		System.out.println();

		new TestOrig(FixedWidthTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
