/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;


import java.util.function.Function;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.FixedWidth;

/**
 * @author sundquis
 *
 */
public class FixedWidthTest extends Test.Container {

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
	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "Applied to remaining fields" )
	public void defaultFieldSeparator_AppliedToRemainingFields( Test.Case tc ) {
		this.formatter.right( 3,  '0' ).sep( " " ).right( 3,  '0' )
			.defaultFieldSeparator("__").right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001 002__003__004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "Can be emtpty" )
	public void defaultFieldSeparator_CanBeEmtpty( Test.Case tc ) {
		this.formatter.right( 3,  '0' ).sep( " " ).right( 3,  '0' )
			.defaultFieldSeparator("").right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001 002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "If not specified, default is empty string" )
	public void defaultFieldSeparator_IfNotSpecifiedDefaultIsEmptyString( Test.Case tc ) {
		this.formatter.right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "Overrides default empty" )
	public void defaultFieldSeparator_OverridesDefaultEmpty( Test.Case tc ) {
		this.formatter.defaultFieldSeparator("X").right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' );
		tc.assertEqual( "001X002X003X004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "Previous fields unaffected" )
	public void defaultFieldSeparator_PreviousFieldsUnaffected( Test.Case tc ) {
		this.formatter.right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).right( 3,  '0' ).defaultFieldSeparator("X");
		tc.assertEqual( "001002003004",  this.formatter.format( 1, 2, 3, 4) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.defaultFieldSeparator(String)", description = "Throws assertion error for null" )
	public void defaultFieldSeparator_ThrowsAssertionErrorForNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.defaultFieldSeparator( null );
	}

	
	
	
	// left(int, char)
	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char)", description = "Appends left justified field" )
	public void left_AppendsLeftJustifiedField( Test.Case tc ) {
		this.formatter.right( 3,  '0' );
		String before = this.formatter.format( "FOO" );
		this.formatter.left( 3,  ' ' );
		String after = formatter.format( "FOO", 2 );
		tc.assertEqual( before + "2  " , after );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char)", description = "Padding character is used" )
	public void left_PaddingCharacterIsUsed( Test.Case tc ) {
		this.formatter.left( 5,  'X' );
		tc.assertEqual( "FOOXX",  this.formatter.format( "FOO" ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char)", description = "Width must be positive" )
	public void left_WidthMustBePositive( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.left( -1,  '0' );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char)", description = "Truncated for long" )
	public void left_TruncatedForLong( Test.Case tc ) {
		tc.assertEqual( 10,  this.formatter.left( 10,  '0' ).format( "A string that is too long" ).length() );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char)", description = "Width is correct for short" )
	public void left_WidthIsCorrectForShort( Test.Case tc ) {
		tc.assertEqual( 10,  this.formatter.left( 10,  '0' ).format( "short" ).length() );
	}


	
	// left(int, char, Function)
	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char, Function)", description = "Other fields unaffected" )
	public void left_OtherFieldsUnaffected( Test.Case tc ) {
		Function<Integer, String> inc = (x) -> ((x+1) + "");
		this.formatter.left( 3,  '0' ).left( 3, '0', inc ).left( 3,  '0' );
		tc.assertEqual( "100200100",  this.formatter.format( 1, 1, 1 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char, Function)", description = "Representation overrides toString() for this field" )
	public void left_RepresentationOverridesTostringForThisField( Test.Case tc ) {
		Function<Integer, String> foo = (x) -> "FOO";
		this.formatter.left( 3,  'X', foo );
		tc.assertEqual( "FOO",  this.formatter.format(42) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.left(int, char, Function)", description = "Throws assertion error for null representation" )
	public void left_ThrowsAssertionErrorForNullRepresentation( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.left( 3, '0', null );
	}

	
	

	// nullFields(String)
	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "Applied to remaining fields" )
	public void nullFields_AppliedToRemainingFields( Test.Case tc ) {
		this.formatter.left( 4,  '0' ).nullFields( "null" ).left( 4,  '0' ).left( 4,  '0' );
		tc.assertEqual( "0000nullnull", this.formatter.format( null, null, null ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "Can be empty" )
	public void nullFields_CanBeEmpty( Test.Case tc ) {
		this.formatter.nullFields( "" ).left( 4,  '0' );
		tc.assertEqual( "0000",  this.formatter.format( new Object[] {null} ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "If not specified, default is empty string" )
	public void nullFields_IfNotSpecifiedDefaultIsEmptyString( Test.Case tc ) {
		this.formatter.left( 4,  ' ' );
		tc.assertEqual( "    ",  this.formatter.format( new Object[] {null} ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "Overrides default empty" )
	public void nullFields_OverridesDefaultEmpty( Test.Case tc ) {
		this.formatter.nullFields( "FOO" ).left( 3,  '0' );
		tc.assertEqual( "FOO",  this.formatter.format( new Object[] {null} ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "Previous fields unaffected" )
	public void nullFields_PreviousFieldsUnaffected( Test.Case tc ) {
		this.formatter.left( 3,  '0' ).nullFields( "FOO" ).left( 3,  '0' );
		tc.assertEqual( "000FOO",  this.formatter.format( null, null ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.nullFields(String)", description = "Throws assertion error for null" )
	public void nullFields_ThrowsAssertionErrorForNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.nullFields( null );
	}

	
	
	
	// right(int, char)
	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char)", description = "Appends right justified field" )
	public void right_AppendsRightJustifiedField( Test.Case tc ) {
		this.formatter.left( 3,  '0' );
		String before = this.formatter.format( "FOO" );
		this.formatter.right( 3,  ' ' );
		String after = formatter.format( "FOO", 2 );
		tc.assertEqual( before + "  2" , after );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char)", description = "Padding character is used" )
	public void right_PaddingCharacterIsUsed( Test.Case tc ) {
		this.formatter.right( 5,  'X' );
		tc.assertEqual( "XXFOO",  this.formatter.format( "FOO" ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char)", description = "Width must be positive" )
	public void right_WidthMustBePositive( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.right( -1,  '0' );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char)", description = "Truncated for long" )
	public void right_TruncatedForLong( Test.Case tc ) {
		tc.assertEqual( 10,  this.formatter.right( 10,  '0' ).format( "A string that is too long" ).length() );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char)", description = "Width is correct for short" )
	public void right_WidthIsCorrectForShort( Test.Case tc ) {
		tc.assertEqual( 10,  this.formatter.right( 10,  '0' ).format( "short" ).length() );
	}


	
	
	
	// right(int, char, Function)
	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char, Function)", description = "Other fields unaffected" )
	public void right_OtherFieldsUnaffected( Test.Case tc ) {
		Function<Integer, String> inc = (x) -> ((x+1) + "");
		this.formatter.right( 3,  '0' ).right( 3, '0', inc ).right( 3,  '0' );
		tc.assertEqual( "001002001",  this.formatter.format( 1, 1, 1 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char, Function)", description = "Representation overrides toString() for this field" )
	public void right_RepresentationOverridesTostringForThisField( Test.Case tc ) {
		Function<Integer, String> foo = (x) -> "FOO";
		this.formatter.right( 3,  'X', foo );
		tc.assertEqual( "FOO",  this.formatter.format(42) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.right(int, char, Function)", description = "Throws assertion error for null representation" )
	public void right_ThrowsAssertionErrorForNullRepresentation( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.right( 3, '0', null );
	}

	
	
	
	
	// sep(String)
	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Appends if last" )
	public void sep_AppendsIfLast( Test.Case tc ) {
		this.formatter.right( 4,  '0' ).sep( "FOO" );
		tc.assertEqual( "0001FOO",  this.formatter.format( 1 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Can be empty" )
	public void sep_CanBeEmpty( Test.Case tc ) {
		this.formatter.right( 4,  '0' ).sep( "" ).right( 4,  '0' );
		tc.assertEqual( "00010002",  this.formatter.format( 1, 2 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Does not affect subsequent separators" )
	public void sep_DoesNotAffectSubsequentSeparators( Test.Case tc ) {
		this.formatter.right( 4,  '0' ).sep( "BAR" ).right( 4,  '0' ).right( 4,  '0' ).right( 4,  '0' );
		tc.assertEqual( "0001BAR000200030004", this.formatter.format( 1, 2, 3, 4 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Multiple separators allowed" )
	public void sep_MultipleSeparatorsAllowed( Test.Case tc ) {
		this.formatter.right( 4,  ' ' ).sep( "FOO" ).sep( " " ).sep( "BAR" ).right( 4,  ' ' );
		tc.assertEqual( "   1FOO BAR   2",  this.formatter.format( 1, 2 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Overrides default" )
	public void sep_OverridesDefault( Test.Case tc ) {
		this.formatter.defaultFieldSeparator( "FOO" )
			.right( 3,  '_' ).sep( "BAR" ).right( 3,  '_' );
		tc.assertEqual( "__1BAR__2",  this.formatter.format( 1, 2 ) );
	}

	@Test.Impl( member = "public FixedWidth FixedWidth.sep(String)", description = "Throws assertion error for null separator" )
	public void sep_ThrowsAssertionErrorForNullSeparator( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.formatter.sep( null );
	}

	
	
	
	// FixedWidth()
	@Test.Impl( member = "public FixedWidth()", description = "Empty formatter produces empty formatted string" )
	public void FixedWidth_EmptyFormatterProducesEmptyFormattedString( Test.Case tc ) {
		tc.assertEqual( "",  this.formatter.format() );
	}

	
	
	
	// format(Object[])
	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Fields are separated as specified" )
	public void format_FieldsAreSeparatedAsSpecified( Test.Case tc ) {
		this.formatter.right( 5,  ' ' ).sep( "---" ).left( 5,  ' ' );
		tc.assertEqual( "    >---<    ",  this.formatter.format( ">", "<" ) );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Fields can be added after formatting" )
	public void format_FieldsCanBeAddedAfterFormatting( Test.Case tc ) {
		String before = this.formatter.right( 3,  'x' ).format( ">>>" );
		String after = this.formatter.left( 3,  'x' ).format( ">>>", "<<<" );
		tc.assertEqual( before + "<<<",  after );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Fields formatted in the order specified" )
	public void format_FieldsFormattedInTheOrderSpecified( Test.Case tc ) {
		this.formatter.defaultFieldSeparator(" ").right( 1,  '0' ).right( 2,  '0' ).right( 3,  '0' );
		tc.assertEqual( "1 02 003", this.formatter.format( 1, 2, 3 ) );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Non null fields represented appropriately" )
	public void format_NonNullFieldsRepresentedAppropriately( Test.Case tc ) {
		this.formatter.defaultFieldSeparator( " " )
			.nullFields( "n" ).right( 4, '0' )
			.nullFields( "ni" ).right( 4,  '0' )
			.nullFields( "nil" ).right( 4,  '0' );
		tc.assertEqual( "000n 00ni 0nil",  this.formatter.format( null, null, null ) );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Null fields use null representation" )
	public void format_NullFieldsUseNullRepresentation( Test.Case tc ) {
		this.formatter.nullFields( "use null" ).right( 8,  ' ' );
		tc.assertEqual( "use null",  this.formatter.format( new Object[] {null} ) );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Throws illegal argument exception for too few fields" )
	public void format_ThrowsIllegalArgumentExceptionForTooFewFields( Test.Case tc ) {
		this.formatter.right( 1,  ' ' ).left( 2,  ' ' );
		this.formatter.format( 1, 2 );
		tc.expectError( IllegalArgumentException.class );
		this.formatter.format( 1 );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Throws illegal argument exception for too many fields" )
	public void format_ThrowsIllegalArgumentExceptionForTooManyFields( Test.Case tc ) {
		this.formatter.right( 1,  ' ' ).left( 2,  ' ' );
		this.formatter.format( 1, 2 );
		tc.expectError( IllegalArgumentException.class );
		this.formatter.format( 1, 2, 3 );
	}

	@Test.Impl( member = "public String FixedWidth.format(Object[])", description = "Throws class cast exception for wron argument type" )
	public void format_ThrowsClassCastExceptionForWronArgumentType( Test.Case tc ) {
		Function<String, String> rep = s -> s + "rep";
		this.formatter.left( 10,  ' ', rep );
		tc.expectError( ClassCastException.class );
		this.formatter.format( 1 );
	}


	
	
	
	

	// length()
	@Test.Impl( member = "public int FixedWidth.length()", description = "Includes fields" )
	public void length_IncludesFields( Test.Case tc ) {
		this.formatter.right( 14,  ' ' ).left( 14,  ' ' ).right( 14,  ' ' );
		tc.assertEqual( 42,  this.formatter.length() );
	}

	@Test.Impl( member = "public int FixedWidth.length()", description = "Includes separators" )
	public void length_IncludesSeparators( Test.Case tc ) {
		this.formatter.right( 20,  ' ' ).sep( "12" ).left( 20,  ' ' );
		tc.assertEqual( 42,  this.formatter.length() );
	}

	@Test.Impl( member = "public int FixedWidth.length()", description = "Increases when field appended" )
	public void length_IncreasesWhenFieldAppended( Test.Case tc ) {
		int before = this.formatter.right( 5,  'c' ).length();
		int after = this.formatter.left( 5,  'c' ).length();
		tc.assertEqual( before + 5,  after );
	}

	@Test.Impl( member = "public int FixedWidth.length()", description = "Increses when separator appended" )
	public void length_IncresesWhenSeparatorAppended( Test.Case tc ) {
		int before = this.formatter.right( 5,  'c' ).length();
		int after = this.formatter.sep( "12345" ).length();
		tc.assertEqual( before + 5,  after );
	}
	
	
	
	// toString()
	@Test.Impl( member = "public String FixedWidth.toString()", description = "Includes field count" )
	public void toString_IncludesFieldCount( Test.Case tc ) {
		for ( int i = 0; i < 42; i++ ) {
			this.formatter.right( 1,  '.' );
		}
		tc.assertTrue( this.formatter.toString().contains( "42 fields" ) );
	}

	@Test.Impl( member = "public String FixedWidth.toString()", description = "Includes length" )
	public void toString_IncludesLength( Test.Case tc ) {
		for ( int i = 0; i < 21; i++ ) {
			this.formatter.right( 1,  '.' ).sep( " " );
		}
		tc.assertTrue( this.formatter.toString().contains( "length = 42" ) );
	}


}
