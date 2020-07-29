/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;



import sog.core.AppException;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class TestTest implements TestContainer{

	@Override public Class<?> subjectClass() { return Test.class; }

	// Test implementations
	
	@Test.Impl( src = "Test", desc = "Loads correctly" )
	public void Test_LoadsCorrectly( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( src = "private void Test.addAnnotations(MemberResult)", desc = "Can change name of generated method" )
	public void addAnnotations_CanChangeNameOfGeneratedMethod( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( src = "private void Test.addAnnotations(MemberResult)", desc = "Description's with; puc-tuation! handled... right?" )
	public void addAnnotations_DescriptionSWithPucTuationHandledRight( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}
	
	@Test.Impl( src = "public Test(Class)", desc = "Fails for containers with illegal constructors" )
	public void Test_FailsForContainersWithIllegalConstructors( TestCase tc ) {
		final class BadContainer implements TestContainer {
			@SuppressWarnings("unused")
			BadContainer() {}
			
			@Override
			public Class<?> subjectClass() {
				return null;
			}
		}
		tc.expectError( AppException.class );
		new Test( BadContainer.class );
	}

	@Test.Impl( src = "public Test(Class)", desc = "Throws assertion error for null container" )
	public void Test_ThrowsAssertionErrorForNullContainer( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Test( null );
	}

	@Test.Impl( src = "public void Test.addStub(String, String, String)", desc = "Throws assertion error if descrption is empty" )
	public void addStub_TrhowsAssertionErrorIfDescrptionIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Test.addStub( "", "Hello", "World" );
	}

	@Test.Impl( src = "public void Test.addStub(String, String, String)", desc = "Throws assertion error if member is empty" )
	public void addStub_TrhowsAssertionErrorIfMemberIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Test.addStub( "Hello", "", "World" );
	}

	@Test.Impl( src = "public void Test.addStub(String, String, String)", desc = "Throws assertion error if subject is empty" )
	public void addStub_TrhowsAssertionErrorIfSubjectIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Test.addStub( "Hello", "World", "" );
	}

	@Test.Impl( src = "public void Test.addWarning(String)", desc = "Throws assertion error if warning is empty" )
	public void addWarning_ThrowsAssertionErrorIfWarningIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Test.addWarning( "" );
	}

	@Test.Impl( src = "public void Test.addWarning(String)", desc = "Throws assertion error if warning is null" )
	public void addWarning_ThrowsAssertionErrorIfWarningIsNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Test.addWarning( null );
	}

	@Test.Impl( src = "private void Test.evaluateTests()", desc = "Handles cases in priority order." )
	public void evaluateTests_HandlesCasesInPriorityOrder( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	
	
	

	public static void main(String[] args) {

		System.out.println();

		new Test(TestTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
