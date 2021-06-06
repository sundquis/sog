/**
 * Copyright (C) 2021
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

package test.sog.core.test;

import sog.core.Test;
import sog.core.test.TestCase;

public class TestCaseTest extends Test.Container {

	public TestCaseTest() {
		super( TestCase.class );
	}

	
	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Throws AssertionError for null Test.Container" )
	public void TestCase_ThrowsAssertionerrorForNullTestContainer( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Throws AssertionError for null TestImpl" )
	public void TestCase_ThrowsAssertionerrorForNullTestimpl( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: String TestCase.toString()", description = "Starts with FAIL if failed" )
	public void toString_StartsWithFailIfFailed( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: String TestCase.toString()", description = "Starts with PASS if passed" )
	public void toString_StartsWithPassIfPassed( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Message is included in details." )
	public void addMessage_MessageIsIncludedInDetails( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Return is this" )
	public void addMessage_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Throws AssertionError for empty message" )
	public void addMessage_ThrowsAssertionerrorForEmptyMessage( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Throws AssertionError for null message" )
	public void addMessage_ThrowsAssertionerrorForNullMessage( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.afterThis(Procedure)", description = "Return is this" )
	public void afterThis_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.afterThis(Procedure)", description = "Throws AssertionError for null procedure" )
	public void afterThis_ThrowsAssertionerrorForNullProcedure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Message added on failure" )
	public void assertEqual_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Return is this" )
	public void assertEqual_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Test fails for inequivalent" )
	public void assertEqual_TestFailsForInequivalent( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Test passes for equivalent objects" )
	public void assertEqual_TestPassesForEquivalentObjects( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Test passes for shallow but not deep equal" )
	public void assertEqual_TestPassesForShallowButNotDeepEqual( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Case fails for true" )
	public void assertFalse_CaseFailsForTrue( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Case passes for false" )
	public void assertFalse_CasePassesForFalse( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Message added on failure" )
	public void assertFalse_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Return is this" )
	public void assertFalse_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Case fails for false" )
	public void assertTrue_CaseFailsForFalse( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Case passes for true" )
	public void assertTrue_CasePassesForTrue( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Message added on failure" )
	public void assertTrue_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Return is this" )
	public void assertTrue_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Return is this" )
	public void expectError_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Throws AssertionError for null error" )
	public void expectError_ThrowsAssertionerrorForNullError( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Marks case as failed" )
	public void fail_MarksCaseAsFailed( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Marks location of failure" )
	public void fail_MarksLocationOfFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Return is this" )
	public void fail_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.getTestCase()", description = "Physically equal to this" )
	public void getTestCase_PhysicallyEqualToThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Case fails for non-null object" )
	public void isNull_CaseFailsForNonNullObject( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Case passes for null object" )
	public void isNull_CasePassesForNullObject( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Message added on failure" )
	public void isNull_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Return is this" )
	public void isNull_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case fails for empty string" )
	public void notEmpty_CaseFailsForEmptyString( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case fails for null string" )
	public void notEmpty_CaseFailsForNullString( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case passes for non-empty string" )
	public void notEmpty_CasePassesForNonEmptyString( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Message added on failure" )
	public void notEmpty_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Return is this" )
	public void notEmpty_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Case fails for null" )
	public void notNull_CaseFailsForNull( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Case passes for non-null" )
	public void notNull_CasePassesForNonNull( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Message added on failure" )
	public void notNull_MessageAddedOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Return is this" )
	public void notNull_ReturnIsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean TestCase.equals(Object)", description = "If compareTo not zero then not equal" )
	public void equals_IfComparetoNotZeroThenNotEqual( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean TestCase.equals(Object)", description = "If compareTo zero then equal" )
	public void equals_IfComparetoZeroThenEqual( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "For equal priority and member ordered by description" )
	public void compareTo_ForEqualPriorityAndMemberOrderedByDescription( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "For equal priority ordered by member" )
	public void compareTo_ForEqualPriorityOrderedByMember( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "Respects Test.Impl.priority" )
	public void compareTo_RespectsTestImplPriority( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.getFailCount()", description = "Equal to Test.Impl.weight if case fails" )
	public void getFailCount_EqualToTestImplWeightIfCaseFails( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.getFailCount()", description = "Zero if case passes" )
	public void getFailCount_ZeroIfCasePasses( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.getPassCount()", description = "Equal to Test.Impl.weight if case passes" )
	public void getPassCount_EqualToTestImplWeightIfCasePasses( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.getPassCount()", description = "Zero if case fails" )
	public void getPassCount_ZeroIfCaseFails( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: int TestCase.hashCode()", description = "If equal then same hashCode" )
	public void hashCode_IfEqualThenSameHashcode( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Elapsed time is consistent with execution time" )
	public void getElapsedTime_ElapsedTimeIsConsistentWithExecutionTime( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Set if case fails" )
	public void getElapsedTime_SetIfCaseFails( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes error information on failure" )
	public void print_IncludesErrorInformationOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes file location on failure" )
	public void print_IncludesFileLocationOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes messages on failure" )
	public void print_IncludesMessagesOnFailure( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Prints summary line" )
	public void print_PrintsSummaryLine( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Graceful exit on an expected error" )
	public void run_GracefulExitOnAnExpectedError( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Graceful exit on an unexpected error" )
	public void run_GracefulExitOnAnUnexpectedError( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added if test fails" )
	public void run_MessageAddedIfTestFails( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if expected error is not thrown" )
	public void run_TestFailsIfExpectedErrorIsNotThrown( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if unexpected error is thrown" )
	public void run_TestFailsIfUnexpectedErrorIsThrown( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if wrong error is thrown" )
	public void run_TestFailsIfWrongErrorIsThrown( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test passes if expected error is thrown" )
	public void run_TestPassesIfExpectedErrorIsThrown( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "afterEach always executed" )
	public void run_AftereachAlwaysExecuted( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "afterThis always executed" )
	public void run_AfterthisAlwaysExecuted( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "elapsedTime greater than zero" )
	public void run_ElapsedtimeGreaterThanZero( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}	
	

	public static void main( String[] args ) {
		Test.eval( TestCase.class );
	}
	
}
