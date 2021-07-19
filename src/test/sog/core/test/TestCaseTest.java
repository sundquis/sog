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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.TestCase;
import sog.core.test.TestImpl;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

public class TestCaseTest extends Test.Container {
	
	private final SomeContainer container;
	private final Map<String,TestImpl> impls;
	
	private TestCase test;

	public TestCaseTest() {
		super( TestCase.class );
		
		this.container = new SomeContainer();
		this.impls = Arrays.stream( this.container.getClass().getDeclaredMethods() )
			.collect( Collectors.toMap( Method::getName, TestImpl::forMethod ) );
	}
	
	@Override
	public Procedure beforeEach() {
		return () -> { TestCaseTest.this.test = TestCaseTest.this.getTestCase( "method1" ); };
	}
	
	@Override
	public Procedure afterEach() {
		return () -> { TestCaseTest.this.test = null; };
	}
	
	
	private TestCase getTestCase( String methodName ) {
		return new TestCase( impls.get(  methodName ), this.container );
	}
	
	private TestCase getTimed( long time ) {
		this.container.sleepTime = time;
		return new TestCase( impls.get( "timed" ), this.container );
	}
	
	private int messageCount( TestCase tc ) {
		List<?> list = null;
		return this.getSubjectField( tc, "messages", list ).size();
	}
	

	
	public static class SomeContainer extends Test.Container {
		static final int WT = 42;
		public long sleepTime = 0L;

		protected SomeContainer() { super( TestCase.class ); }
				
		@Test.Impl( member = "MEMBER NAME", description = "DESCRIPTION" ) public void method1() {}
		@Test.Impl( member = "OTHER MEMBER", description = "DESCR" ) public void method2() {}
		
		@Test.Impl( member = "A", description = "D", priority = 0 ) public void ordered0() {}
		@Test.Impl( member = "A", description = "D", priority = 1 ) public void ordered1() {}
		@Test.Impl( member = "B", description = "D", priority = 0 ) public void ordered2() {}
		@Test.Impl( member = "A", description = "E", priority = 0 ) public void ordered3() {}
		
		@Test.Impl( member = "MEMBER", description = "descr", weight = SomeContainer.WT ) public void weighted() {}

		@Test.Impl( member = "M", description = "D" ) 
		public void timed( Test.Case tc ) { 
			try { Thread.sleep( this.sleepTime ); } catch ( InterruptedException e ) {}
		}
		
		@Test.Impl( member = "M", description = "d" )
		public void error( Test.Case tc ) {
			try { Thread.sleep( 10L ); } catch ( InterruptedException e ) {}
			throw new AssertionError();
		}
	}
	

	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Label includes member name and description", weight = 2 )
	public void TestCase_LabelIncludesMemberNameAndDescription( Test.Case tc ) {
		tc.assertTrue( this.test.toString().contains( "MEMBER NAME" ) );
		tc.assertTrue( this.test.toString().contains( "DESCRIPTION" ) );
	}

	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Marked as passed at creation" )
	public void TestCase_MarkedAsPassedAtCreation( Test.Case tc ) {
		tc.assertTrue( this.getSubjectField( this.test, "passed", Boolean.TRUE ) );
	}

	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Throws AssertionError for null Test.Container" )
	public void TestCase_ThrowsAssertionerrorForNullTestContainer( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestCase( this.impls.get( "method1" ), null );
	}

	@Test.Impl( member = "constructor: TestCase(TestImpl, Test.Container)", description = "Throws AssertionError for null TestImpl" )
	public void TestCase_ThrowsAssertionerrorForNullTestimpl( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestCase( null, this.container );
	}

	@Test.Impl( member = "method: String TestCase.toString()", description = "Starts with FAIL if failed" )
	public void toString_StartsWithFailIfFailed( Test.Case tc ) {
		tc.assertTrue( this.test.fail( "Reason" ).toString().startsWith( "FAIL" ) );
	}

	@Test.Impl( member = "method: String TestCase.toString()", description = "Starts with PASS if passed" )
	public void toString_StartsWithPassIfPassed( Test.Case tc ) {
		tc.assertTrue( this.test.toString().startsWith( "PASS" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Does not alter pass/fail status", weight = 2 )
	public void addMessage_DoesNotAlterPassFailStatus( Test.Case tc ) {
		tc.assertEqual( this.test.toString(), this.test.addMessage( "foo" ).toString() );
		this.test.fail( "Reason" );
		tc.assertEqual( this.test.toString(), this.test.addMessage( "foo" ).toString() );
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Message is included in details." )
	public void addMessage_MessageIsIncludedInDetails( Test.Case tc ) {
		//this.test.addMessage( "One" ).addMessage( "FOO" ).addMessage( "Two" );
		StringOutputStream sos = new StringOutputStream();
		this.test.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( "FOO" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Return is this" )
	public void addMessage_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.addMessage( "FOO" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Throws AssertionError for empty message" )
	public void addMessage_ThrowsAssertionerrorForEmptyMessage( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.test.addMessage( "" );
	}

	@Test.Impl( member = "method: Test.Case TestCase.addMessage(String)", description = "Throws AssertionError for null message" )
	public void addMessage_ThrowsAssertionerrorForNullMessage( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.test.addMessage( null );
	}

	@Test.Impl( member = "method: Test.Case TestCase.afterThis(Procedure)", description = "Return is this" )
	public void afterThis_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.afterThis( Procedure.NOOP ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.afterThis(Procedure)", description = "Throws AssertionError for null procedure" )
	public void afterThis_ThrowsAssertionerrorForNullProcedure( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.test.afterThis( null );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Message added on failure" )
	public void assertEqual_MessageAddedOnFailure( Test.Case tc ) {
		int before = this.messageCount( this.test );
		this.test.assertEqual( 1, 2 );
		int after = this.messageCount( this.test );
		tc.assertTrue( after > before );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Return is this" )
	public void assertEqual_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.assertEqual( 1, 2 ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Test fails for inequivalent" )
	public void assertEqual_TestFailsForInequivalent( Test.Case tc ) {
		this.test.assertEqual( "fourty-two", "42" );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}
	
	@Test.Impl( member = "method: Test.Case TestCase.assertEqual(Object, Object)", description = "Test passes for equivalent objects" )
	public void assertEqual_TestPassesForEquivalentObjects( Test.Case tc ) {
		this.test.assertEqual( new String[] {"A", "B", "C"}, new String[] {"A", "B", "C"} );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Case fails for true" )
	public void assertFalse_CaseFailsForTrue( Test.Case tc ) {
		this.test.assertFalse( true );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Case passes for false" )
	public void assertFalse_CasePassesForFalse( Test.Case tc ) {
		this.test.assertFalse( false );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Message added on failure" )
	public void assertFalse_MessageAddedOnFailure( Test.Case tc ) {
		int before = this.messageCount( this.test );
		this.test.assertFalse( true );
		int after = this.messageCount( this.test );
		tc.assertTrue( after > before );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertFalse(boolean)", description = "Return is this", weight = 2 )
	public void assertFalse_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.assertFalse( false ) );
		tc.assertTrue( this.test == this.test.assertFalse( true ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Case fails for false" )
	public void assertTrue_CaseFailsForFalse( Test.Case tc ) {
		this.test.assertTrue( false );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Case passes for true" )
	public void assertTrue_CasePassesForTrue( Test.Case tc ) {
		this.test.assertTrue( true );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Message added on failure" )
	public void assertTrue_MessageAddedOnFailure( Test.Case tc ) {
		int before = this.messageCount( this.test );
		this.test.assertTrue( false );
		int after = this.messageCount( this.test );
		tc.assertTrue( after > before );
	}

	@Test.Impl( member = "method: Test.Case TestCase.assertTrue(boolean)", description = "Return is this", weight = 2 )
	public void assertTrue_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.assertTrue( true ) );
		tc.assertTrue( this.test == this.test.assertTrue( false ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Does not alter pass/fail status", weight = 2 )
	public void expectError_DoesNotAlterPassFailStatus( Test.Case tc ) {
		tc.assertEqual( this.test.toString(), this.test.expectError( Exception.class ).toString() );
		this.beforeEach().exec();
		this.test.fail( "Reason" );
		tc.assertEqual( this.test.toString(), this.test.expectError( Exception.class ).toString() );
	}

	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Return is this" )
	public void expectError_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.expectError( Exception.class ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Throws AssertionError for null error" )
	public void expectError_ThrowsAssertionerrorForNullError( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.test.expectError( null );
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Marks failed case as failed" )
	public void fail_MarksFailedCaseAsFailed( Test.Case tc ) {
		tc.assertTrue( this.test.getFailCount() == 0 );
		this.test.fail( "Reason" );
		tc.assertTrue( this.test.getFailCount() > 0 );
		this.test.fail( "Reason" );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Marks location of failure", weight = 2 )
	public void fail_MarksLocationOfFailure( Test.Case tc ) {
		tc.isNull( this.container.getSubjectField( this.test, "fileLocation", "" ) );
		this.test.fail( "Reason" );
		tc.notNull( this.container.getSubjectField( this.test, "fileLocation", "" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Marks passed case as failed" )
	public void fail_MarksPassedCaseAsFailed( Test.Case tc ) {
		tc.assertTrue( this.test.getFailCount() == 0 );
		this.test.fail( "Reason" );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.fail()", description = "Return is this" )
	public void fail_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.fail( "Reason" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.getTestCase()", description = "Physically equal to this" )
	public void getTestCase_PhysicallyEqualToThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.getTestCase() );
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Case fails for non-null object" )
	public void isNull_CaseFailsForNonNullObject( Test.Case tc ) {
		this.test.isNull( "" );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Case passes for null object" )
	public void isNull_CasePassesForNullObject( Test.Case tc ) {
		this.test.isNull( null );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Message added on failure" )
	public void isNull_MessageAddedOnFailure( Test.Case tc ) {
		this.test.isNull( "" );
		tc.assertTrue( this.messageCount( this.test )> 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.isNull(Object)", description = "Return is this" )
	public void isNull_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.isNull( "" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case fails for empty string" )
	public void notEmpty_CaseFailsForEmptyString( Test.Case tc ) {
		this.test.notEmpty( "" );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case fails for null string" )
	public void notEmpty_CaseFailsForNullString( Test.Case tc ) {
		this.test.notEmpty( null );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Case passes for non-empty string" )
	public void notEmpty_CasePassesForNonEmptyString( Test.Case tc ) {
		this.test.notEmpty( "FOO" );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Message added on failure" )
	public void notEmpty_MessageAddedOnFailure( Test.Case tc ) {
		this.test.notEmpty( "" );
		tc.assertTrue( this.messageCount( this.test )> 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notEmpty(String)", description = "Return is this" )
	public void notEmpty_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.notEmpty( "" ) );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Case fails for null" )
	public void notNull_CaseFailsForNull( Test.Case tc ) {
		this.test.notNull( null );
		tc.assertTrue( this.test.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Case passes for non-null" )
	public void notNull_CasePassesForNonNull( Test.Case tc ) {
		this.test.notNull( "" );
		tc.assertTrue( this.test.getPassCount() > 0 );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Message added on failure" )
	public void notNull_MessageAddedOnFailure( Test.Case tc ) {
		this.test.notNull( null );
		tc.assertTrue( this.messageCount( this.test )> 0  );
	}

	@Test.Impl( member = "method: Test.Case TestCase.notNull(Object)", description = "Return is this" )
	public void notNull_ReturnIsThis( Test.Case tc ) {
		tc.assertTrue( this.test == this.test.notNull( null ) );
	}

	@Test.Impl( member = "method: boolean TestCase.equals(Object)", description = "If compareTo not zero then not equal" )
	public void equals_IfComparetoNotZeroThenNotEqual( Test.Case tc ) {
		TestCase other = this.getTestCase( "method1" );
		tc.assertTrue( this.test.compareTo( other ) == 0 || !this.test.equals( other ) );
		other = this.getTestCase( "method2" );
		tc.assertTrue( this.test.compareTo( other ) == 0 || !this.test.equals( other ) );
	}

	@Test.Impl( member = "method: boolean TestCase.equals(Object)", description = "If compareTo zero then equal" )
	public void equals_IfComparetoZeroThenEqual( Test.Case tc ) {
		TestCase other = this.getTestCase( "method1" );
		tc.assertTrue( this.test.compareTo( other ) != 0 || this.test.equals( other ) );
		other = this.getTestCase( "method2" );
		tc.assertTrue( this.test.compareTo( other ) != 0 || this.test.equals( other ) );
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "For equal priority and member ordered by description" )
	public void compareTo_ForEqualPriorityAndMemberOrderedByDescription( Test.Case tc ) {
		TestCase test0 = this.getTestCase( "ordered0" );
		TestCase test3 = this.getTestCase( "ordered3" );
		tc.assertTrue( test0.compareTo( test3 ) < 0 );
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "For equal priority ordered by member" )
	public void compareTo_ForEqualPriorityOrderedByMember( Test.Case tc ) {
		TestCase test0 = this.getTestCase( "ordered0" );
		TestCase test2 = this.getTestCase( "ordered2" );
		tc.assertTrue( test0.compareTo( test2 ) < 0 );
	}

	@Test.Impl( member = "method: int TestCase.compareTo(TestCase)", description = "Respects Test.Impl.priority" )
	public void compareTo_RespectsTestImplPriority( Test.Case tc ) {
		TestCase test0 = this.getTestCase( "ordered0" );
		TestCase test1 = this.getTestCase( "ordered1" );
		tc.assertTrue( test0.compareTo( test1 ) < 0 );
	}

	@Test.Impl( member = "method: int TestCase.getFailCount()", description = "Equal to Test.Impl.weight if case fails" )
	public void getFailCount_EqualToTestImplWeightIfCaseFails( Test.Case tc ) {
		TestCase weighted = this.getTestCase( "weighted" );
		weighted.fail( "Reason" );
		tc.assertEqual( weighted.getFailCount(), SomeContainer.WT );
	}

	@Test.Impl( member = "method: int TestCase.getFailCount()", description = "Zero if case passes" )
	public void getFailCount_ZeroIfCasePasses( Test.Case tc ) {
		TestCase weighted = this.getTestCase( "weighted" );
		tc.assertEqual( weighted.getFailCount(), 0 );
	}

	@Test.Impl( member = "method: int TestCase.getPassCount()", description = "Equal to Test.Impl.weight if case passes" )
	public void getPassCount_EqualToTestImplWeightIfCasePasses( Test.Case tc ) {
		TestCase weighted = this.getTestCase( "weighted" );
		tc.assertEqual( weighted.getPassCount(), SomeContainer.WT );
	}

	@Test.Impl( member = "method: int TestCase.getPassCount()", description = "Zero if case fails" )
	public void getPassCount_ZeroIfCaseFails( Test.Case tc ) {
		TestCase weighted = this.getTestCase( "weighted" );
		weighted.fail( "Reason" );
		tc.assertEqual( weighted.getPassCount(), 0 );
	}

	@Test.Impl( member = "method: int TestCase.hashCode()", description = "If equal then same hashCode" )
	public void hashCode_IfEqualThenSameHashcode( Test.Case tc ) {
		TestCase other = this.getTestCase( "method1" );
		tc.assertTrue( !this.test.equals( other ) || this.test.hashCode() == other.hashCode() );
		other = this.getTestCase( "method2" );
		tc.assertTrue( !this.test.equals( other ) || this.test.hashCode() == other.hashCode() );
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Elapsed time is consistent with execution time", weight = 3 )
	public void getElapsedTime_ElapsedTimeIsConsistentWithExecutionTime( Test.Case tc ) {
		TestCase timed = this.getTestCase( "timed" );
		long[] times = new long[] { 40L, 120L, 580L };
		for ( long t : times ) {
			this.container.sleepTime = t;
			timed.run();
			tc.assertTrue( timed.getElapsedTime() >= t );
		}
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Set after expected exception" )
	public void getElapsedTime_SetAfterExpectedException( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( AssertionError.class );
		error.run();
		tc.assertTrue( error.getElapsedTime() > 0L );
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Set after unexpected exception" )
	public void getElapsedTime_SetAfterUnexpectedException( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( Error.class );
		error.run();
		tc.assertTrue( error.getElapsedTime() > 0L );
	}

	@Test.Impl( member = "method: long TestCase.getElapsedTime()", description = "Set if case fails" )
	public void getElapsedTime_SetIfCaseFails( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( AssertionError.class );
		error.fail( "Reason" );
		error.run();
		tc.assertTrue( error.getElapsedTime() > 0L );
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes error information on failure" )
	public void print_IncludesErrorInformationOnFailure( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( Error.class );
		error.run();
		StringOutputStream sos = new StringOutputStream();
		error.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( "AssertionError" ) );
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes file location on failure" )
	public void print_IncludesFileLocationOnFailure( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.run();
		StringOutputStream sos = new StringOutputStream();
		error.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( "Location: " ) );
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Includes messages on failure" )
	public void print_IncludesMessagesOnFailure( Test.Case tc ) {
		String message = "This is a random dfkjsfhkf";
		TestCase error = this.getTestCase( "error" );
		error.run();
		error.addMessage( message );
		StringOutputStream sos = new StringOutputStream();
		error.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( message ) );
	}

	@Test.Impl( member = "method: void TestCase.print(IndentWriter)", description = "Prints summary line" )
	public void print_PrintsSummaryLine( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		this.test.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().startsWith( this.test.toString() ) );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Graceful exit on an expected error" )
	public void run_GracefulExitOnAnExpectedError( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( AssertionError.class );
		error.run();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Graceful exit on an unexpected error" )
	public void run_GracefulExitOnAnUnexpectedError( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( Error.class );
		error.run();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Graceful exit on an post processing error" )
	public void tm_044A50FEA( Test.Case tc ) {
		SomeContainer sc = new SomeContainer() {
			@Override public Procedure afterEach() {
				return () -> { throw new AssertionError(); };
			}
		};
		TestCase bad = new TestCase( this.impls.get( "timed" ), sc );
		bad.run();
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added for exception in afterEach" )
	public void tm_0AF8838FB( Test.Case tc ) {
		SomeContainer sc = new SomeContainer() {
			@Override public Procedure afterEach() {
				return () -> { throw new AssertionError(); };
			}
		};
		TestCase bad = new TestCase( this.impls.get( "timed" ), sc );
		int before = this.messageCount( bad );
		bad.run();
		int after = this.messageCount( bad );
		tc.assertTrue( before < after );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added for exception in afterThis" )
	public void tm_0AF8F2598( Test.Case tc ) {
		TestCase bad = this.getTimed( 0L );
		bad.afterThis( () -> { throw new AssertionError();} );
		int before = this.messageCount( bad );
		bad.run();
		int after = this.messageCount( bad );
		tc.assertTrue( before < after );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added for no exception when expected" )
	public void tm_07866A976( Test.Case tc ) {
		TestCase timed = this.getTimed( 0L );
		timed.expectError( AssertionError.class );
		int before = this.messageCount( timed );
		timed.run();
		int after = this.messageCount( timed );
		tc.assertTrue( before < after );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added for unexpected exception" )
	public void tm_059E13D48( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		int before = this.messageCount( error );
		error.run();
		int after = this.messageCount( error );
		tc.assertTrue( before < after );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Message added for wrong exception when expected" )
	public void tm_0D162D852( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		error.expectError( Error.class );
		int before = this.messageCount( error );
		error.run();
		int after = this.messageCount( error );
		tc.assertTrue( before < after );
	}
        
    @Test.Impl( member = "method: void TestCase.run()", description = "Test fails if expected error is not thrown" )
	public void run_TestFailsIfExpectedErrorIsNotThrown( Test.Case tc ) {
    	TestCase timed = this.getTimed( 0L );
    	timed.expectError( Error.class );
    	timed.run();
    	tc.assertTrue( timed.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if unexpected error is thrown" )
	public void run_TestFailsIfUnexpectedErrorIsThrown( Test.Case tc ) {
    	TestCase error = this.getTestCase( "error" );
    	error.run();
    	tc.assertTrue( error.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if wrong error is thrown" )
	public void run_TestFailsIfWrongErrorIsThrown( Test.Case tc ) {
    	TestCase error = this.getTestCase( "error" );
    	error.expectError( Error.class );
    	error.run();
    	tc.assertTrue( error.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test passes if expected error is thrown" )
	public void run_TestPassesIfExpectedErrorIsThrown( Test.Case tc ) {
    	TestCase error = this.getTestCase( "error" );
    	error.expectError( AssertionError.class );
    	error.run();
    	tc.assertTrue( error.getFailCount() == 0 );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "afterEach always executed" )
	public void run_AftereachAlwaysExecuted( Test.Case tc ) {
		TestCase error = this.getTestCase( "error" );
		SomeContainer sc = new SomeContainer() {
			private String state = "initial";
			@Override public String toString() { return state; }
			@Override public Procedure afterEach() { return () -> { this.state = "executed"; }; }
		};
		this.container.setSubjectField( error, "container", sc );
		error.run();
		tc.assertEqual( "executed", sc.toString() );
	}

	/* THIS SHOULD BE 5 SEPARATE CASES */
	@Test.Impl( member = "method: void TestCase.run()", description = "afterThis always executed", weight = 5 )
	public void run_AfterthisAlwaysExecuted( Test.Case tc ) {
		Procedure proc = new Procedure() {
			private int executionCount = 0;
			@Override public void exec() { this.executionCount++; }
			@Override public int hashCode() { return this.executionCount; }
		};
		tc.assertEqual( 0,  proc.hashCode() );
		
		// No error
		TestCase timed = this.getTimed( 0L );
		timed.afterThis( proc );
		timed.run();
		tc.assertEqual( 1, proc.hashCode() );
		
		// Expected occurs
		TestCase error = this.getTestCase( "error" );
		error.afterThis( proc );
		error.expectError( AssertionError.class );
		error.run();
		tc.assertEqual( 2, proc.hashCode() );
		
		// Expected does not occur
		timed = this.getTimed( 0L );
		timed.afterThis( proc );
		timed.expectError( AssertionError.class );
		timed.run();
		tc.assertEqual( 3, proc.hashCode() );
		
		// Wrong error occurs
		error = this.getTestCase( "error" );
		error.afterThis( proc );
		error.expectError( Error.class );
		error.run();
		tc.assertEqual( 4, proc.hashCode() );
		
		// Unexpected occurs
		error = this.getTestCase( "error" );
		error.afterThis( proc );
		error.run();
		tc.assertEqual( 5, proc.hashCode() );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "elapsedTime greater than zero" )
	public void run_ElapsedtimeGreaterThanZero( Test.Case tc ) {
		TestCase timed = this.getTimed( 10L );
		timed.run();
		tc.assertTrue( timed.getElapsedTime() > 0 );
	}	
	
	@Test.Impl( member = "method: Test.Case TestCase.afterThis(Procedure)", description = "Default is NOOP" )
	public void tm_003F07311( Test.Case tc ) {
		TestCase timed = this.getTimed( 0L );
		Procedure proc = null;
		tc.assertEqual( this.container.getSubjectField( timed, "afterThis", proc ), Procedure.NOOP );
	}
    
	@Test.Impl( member = "method: Test.Case TestCase.expectError(Class)", description = "Throws  AssertionError if expected error already set" )
	public void tm_0D9C6AF12( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestCase error = this.getTestCase( "error" );
		error.expectError( Error.class );
		error.expectError( AssertionError.class );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if afterEach throws exception" )
	public void tm_0F6A88741( Test.Case tc ) {
		SomeContainer container = new SomeContainer() {
			@Override public Procedure afterEach() {
				return () -> { throw new AssertionError(); };
			}
		};
		TestCase timed = this.getTimed( 0L );
		this.container.setSubjectField( timed, "container", container );
		timed.run();
		tc.assertTrue( timed.getFailCount() > 0 );
	}

	@Test.Impl( member = "method: void TestCase.run()", description = "Test fails if afterThis throws exception" )
	public void tm_04D4EC844( Test.Case tc ) {
		TestCase timed = this.getTimed( 0L );
		timed.afterThis( () -> { throw new AssertionError(); } );
		timed.run();
		tc.assertTrue( timed.getFailCount() > 0 );
	}	

	
	
	public static void main( String[] args ) {
		Test.eval( TestCase.class );
		//Test.evalPackage( TestCase.class );
	}
	
}
