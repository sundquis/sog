/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;



import sog.core.AppException;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class TestTest extends Test.Implementation{

	// Test implementations
	
	@Test.Impl( member = "Test", description = "Loads correctly" )
	public void Test_LoadsCorrectly( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
	}

	@Test.Impl( member = "private void Test.addAnnotations(MemberResult)", description = "Can change name of generated method" )
	public void addAnnotations_CanChangeNameOfGeneratedMethod( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
	}

	@Test.Impl( member = "private void Test.addAnnotations(MemberResult)", description = "Description's with; puc-tuation! handled... right?" )
	public void addAnnotations_DescriptionSWithPucTuationHandledRight( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
	}
	
	@Test.Impl( member = "public Test(Class)", description = "Fails for containers with illegal constructors" )
	public void Test_FailsForContainersWithIllegalConstructors( Test.Case tc ) {
//		final class BadContainer extends Test.Container {
//			@SuppressWarnings("unused")
//			BadContainer() {}
//			
//		}
		tc.expectError( AppException.class );
		//new Test( BadContainer.class );
	}

	@Test.Impl( member = "public Test(Class)", description = "Throws assertion error for null container" )
	public void Test_ThrowsAssertionErrorForNullContainer( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//new Test( null );
	}

	@Test.Impl( member = "public void Test.addStub(String, String, String)", description = "Throws assertion error if descrption is empty" )
	public void addStub_ThrowsAssertionErrorIfDescrptionIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//Test.addStub( "", "Hello", "World" );
	}

	@Test.Impl( member = "public void Test.addStub(String, String, String)", description = "Throws assertion error if member is empty" )
	public void addStub_ThrowsAssertionErrorIfMemberIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//Test.addStub( "Hello", "", "World" );
	}

	@Test.Impl( member = "public void Test.addStub(String, String, String)", description = "Throws assertion error if subject is empty" )
	public void addStub_ThrowsAssertionErrorIfSubjectIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//Test.addStub( "Hello", "World", "" );
	}

	@Test.Impl( member = "public void Test.addWarning(String)", description = "Throws assertion error if warning is empty" )
	public void addWarning_ThrowsAssertionErrorIfWarningIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//Test.addWarning( "" );
	}

	@Test.Impl( member = "public void Test.addWarning(String)", description = "Throws assertion error if warning is null" )
	public void addWarning_ThrowsAssertionErrorIfWarningIsNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		//Test.addWarning( null );
	}

	@Test.Impl( member = "private void Test.evaluateTests()", description = "Handles cases in priority order." )
	public void evaluateTests_HandlesCasesInPriorityOrder( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
	}

	
	
}
