/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.test;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.test.CaseResult;

/**
 * @author sundquis
 *
 */
public class CaseResultTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return CaseResult.class;
	}

	// Test implementations
	
	public CaseResult cr = null;
	
	@Override public Procedure beforeEach() { 
		return () -> cr = new CaseResult( "member", "description", "subject" );
	}
	
	@Override public Procedure afterEach() { return () -> cr = null; }
	

	@Test.Impl( src = "public String CaseResult.getKey()", desc = "Is not empty" )
	public void getKey_IsNotEmpty( TestCase tc ) {
		tc.assertFalse( cr.getKey().isEmpty() );
	}

	@Test.Impl( src = "public String CaseResult.toString()", desc = "Is not empty" )
	public void toString_IsNotEmpty( TestCase tc ) {
		tc.assertFalse( cr.toString().isEmpty() );
	}

	@Test.Impl( src = "public void CaseResult.addMessage(String)", desc = "Throws assertion eror for empty message" )
	public void addMessage_ThrowsAssertionErorForEmptyMessage( TestCase tc ) {
		tc.expectError( AssertionError.class );
		cr.addMessage( "" );
	}

	@Test.Impl( src = "public void CaseResult.print(String)", desc = "Adds warnings for unexecuted cases" )
	public void print_AddsWarningsForUnexecutedCases( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( src = "public void CaseResult.print(String)", desc = "Prints messages for non pass cases" )
	public void print_PrintsMessagesForNonPassCases( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( src = "public void CaseResult.print(String)", desc = "Shows stack trace for unexpected errors" )
	public void print_ShowsStackTraceForUnexpectedErrors( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( src = "public void CaseResult.print(String)", desc = "Throws assertion error for null prefix" )
	public void print_ThrowsAssertionErrorForNullPrefix( TestCase tc ) {
		tc.expectError( AssertionError.class );
		cr.print( null );
	}

	@Test.Impl( src = "public void CaseResult.setError(Throwable)", desc = "Throws assertion error for null error" )
	public void setError_ThrowsAssertionErrorForNullError( TestCase tc ) {
		tc.expectError( AssertionError.class );
		cr.setError( null );
	}

	@Test.Impl( src = "public void CaseResult.setWeight(int)", desc = "Throws assertion eror for non-positve weight" )
	public void setWeight_ThrowsAssertionErorForNonPositveWeight( TestCase tc ) {
		tc.expectError( AssertionError.class );
		cr.setWeight( 0 );
	}

	
	
	public static void main(String[] args) {

		System.out.println();

		new Test(CaseResultTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
