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
package test.sog.core.test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.AppException;
import sog.core.Assert;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.TestCase;
import sog.core.test.TestImpl;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestCaseTest extends Test.Container {

	
	/*
	 * Potentially confusing!
	 * 
	 * Dealing with two levels of (Container, TestCase) pairs. 
	 * 
	 * TestCaseTest is the Container for the top-level test methods which deal only with
	 * Test.Case tc test cases.
	 * 
	 * OurContainer is the Container for TestCase as a subject class.
	 */
	
	
	public TestCaseTest() {
		super( TestCase.class );
	}
	
	public static class OurContainer extends Test.Container {

		private Procedure afterEach = () -> {};
		
		private Procedure beforeEach = () -> {};
		
		@Override public Procedure afterEach() { return this.afterEach; }
		
		@Override public Procedure beforeEach() { return this.beforeEach; }		
		
		public OurContainer() { super( TestCase.class ); }

		private long sleepTime = 0L;
		
		private void sleep() { try { Thread.sleep( this.sleepTime ); } catch ( InterruptedException ex ) {} }
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_NOOP( Test.Case tc ) {}

		private static final int FAIL_WT = 7;
		@Test.Impl( member = "member", description = "description", weight = FAIL_WT )
		public void tm_Fail( Test.Case tc ) { tc.assertFail( "forced fail" ); }
		
		private static final int OPEN_WT = 6;
		@Test.Impl( member = "member", description = "description", weight = OPEN_WT )
		public void tm_Open( Test.Case tc ) { tc.addMessage( "Open case" ); }
		
		private static final int PASS_WT = 5;
		@Test.Impl( member = "member", description = "description", weight = PASS_WT )
		public void tm_Pass( Test.Case tc ) { tc.assertPass(); }
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_ExpectedError( Test.Case tc ) { 
			tc.expectError( AppException.class );
			this.sleep();
			throw new AppException();
		}
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_UnexpectedError( Test.Case tc ) { 
			this.sleep();
			throw new AssertionError();
		}
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_WrongError( Test.Case tc ) { 
			tc.expectError( AppException.class );
			this.sleep();
			throw new AssertionError();
		}
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_NoError( Test.Case tc ) { 
			this.sleep();
		}
		
		@Test.Impl( member = "member", description = "description" )
		public void tm_NoExpectedError( Test.Case tc ) { 
			tc.expectError( AssertionError.class );
			this.sleep();
		}
		
		@Test.Impl( member = "A", description = "A", priority = 0 ) public void tm_OrderedAA0( Test.Case tc ) {}
		@Test.Impl( member = "A", description = "A", priority = 1 ) public void tm_OrderedAA1( Test.Case tc ) {}
		@Test.Impl( member = "A", description = "B", priority = 0 ) public void tm_OrderedAB0( Test.Case tc ) {}
		@Test.Impl( member = "A", description = "B", priority = 1 ) public void tm_OrderedAB1( Test.Case tc ) {}
		@Test.Impl( member = "B", description = "A", priority = 0 ) public void tm_OrderedBA0( Test.Case tc ) {}
		@Test.Impl( member = "B", description = "A", priority = 1 ) public void tm_OrderedBA1( Test.Case tc ) {}
		@Test.Impl( member = "B", description = "B", priority = 0 ) public void tm_OrderedBB0( Test.Case tc ) {}
		@Test.Impl( member = "B", description = "B", priority = 1 ) public void tm_OrderedBB1( Test.Case tc ) {}

		@Test.Impl( member = "member", description = "description", threadsafe = true )
		public void tm_ThreadSafe( Test.Case tc ) {}
		
		@Test.Impl( member = "member", description = "description", threadsafe = false )
		public void tm_NotThreadSafe( Test.Case tc ) {}
		
	}
	
	public static class TestCaseData {
		
		public static TestCaseData forMethod( String name ) {
			TestCaseData result = new TestCaseData( name );

			try {
				result.method = OurContainer.class.getDeclaredMethod( "tm_" + name, Test.Case.class );
			} catch ( NoSuchMethodException | SecurityException e ) {
				e.printStackTrace();
				result.setMessage( e.toString() );
				return result;
			}
			result.impl = TestImpl.forMethod( result.method );
			if ( ! result.impl.isValid() ) {
				result.setMessage( "Not a valid test method" );
				return result;
			}
			result.container = new OurContainer();
			result.testCase = new TestCase( result.impl, result.container );
			
			return result;
		}
		
		private String name;
		private Method method = null;
		private TestImpl impl = null;
		private OurContainer container = null;
		private TestCase testCase = null;
		private String message = null;
		
		private TestCaseData( String name ) {
			this.name = Assert.nonEmpty( name );
		}
		
		private void setMessage( String message ) {
			this.message = message;
		}
		
		private void setSleepTime( long sleepTime) { this.container.sleepTime = sleepTime; }
		
		private TestImpl getTestImpl() { return this.impl; }
		
		private Test.Container getContainer() { return this.container; }
		
		private TestCase getTestCase() { return this.testCase; }
		
		private Class<? extends Throwable> getExpectedError() { return this.container.getSubjectField( this.testCase, "expectedError", null ); }
		
		private Throwable getUnexpectedError() { return this.container.getSubjectField( this.testCase, "unexpectedError", null ); }
		
		private List<String> getMessages() { return this.container.getSubjectField( this.testCase, "messages", null ); }
		
		private Procedure getAfterThis() { return this.container.getSubjectField( this.testCase, "afterThis", null ); }
		
		private String getFileLocation() { return this.container.getSubjectField( this.testCase, "fileLocation", null ); }
		
		private TestCase.State getState() { return this.container.getSubjectField( this.testCase, "state", null ); }
		
		private TestImpl getTestCaseImpl() { return this.container.getSubjectField( this.testCase, "impl", null ); }
		
		private void setAfterEach( Procedure p ) { this.container.afterEach = p; }
		
		private void setBeforeEach( Procedure p ) { this.container.beforeEach = p; }

		private void run() { this.container.evalSubjectMethod( this.testCase, "run", null ); }
		
		private String print() {
			StringOutputStream sos = new StringOutputStream();
			this.testCase.print( new IndentWriter( sos ) );
			return sos.toString();
		}
		
		private void fail( String msg ) { this.container.evalSubjectMethod( this.testCase, "fail", null, msg ); }
		
		private void pass() { this.container.evalSubjectMethod( this.testCase, "pass", null ); }
		
		@Override public String toString() { return this.name + (this.message == null ? "" : ": " + this.message); }
	}


	public static class TestProcedure implements Procedure {
		
		private final long sleepTime;
		private final Error error;
		private boolean executed;
		
		private TestProcedure( long sleepTime, Error error ) {
			this.sleepTime = sleepTime;
			this.error = error;
			this.executed = false;
		}
		
		public boolean executed() { return this.executed; }
	
		@Override 
		public void exec() { 
			this.executed = true;
			if ( this.sleepTime > 0 ) {
				try { Thread.sleep( this.sleepTime ); } catch ( InterruptedException e ) {}
			}
			if ( this.error != null ) {
				throw this.error;
			}
		}
		
	}

	
	

	// TEST CASES:
	
    @Test.Impl( 
    	member = "constructor: TestCase(TestImpl, Test.Container)", 
    	description = "Throws AssertionError for null Test.Container" 
    )
    public void tm_0CA6FAACA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tcd.container = null;
    	tc.expectError( AssertionError.class );
    	new TestCase( tcd.getTestImpl(), tcd.getContainer() );
    }
    
    @Test.Impl( 
    	member = "constructor: TestCase(TestImpl, Test.Container)", 
    	description = "Throws AssertionError for null TestImpl" 
    )
    public void tm_0BA36E6D7( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tcd.impl = null;
    	tc.expectError( AssertionError.class );
    	new TestCase( tcd.getTestImpl(), tcd.getContainer() );
    }
    
    @Test.Impl( 
    	member = "field: Class TestCase.expectedError", 
    	description = "Initially null" 
    )
    public void tm_0345429AE( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getExpectedError() );
    }
    
    @Test.Impl( 
    	member = "field: List TestCase.messages", 
    	description = "Not null" 
    )
    public void tm_034D5CAE2( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertNonNull( tcd.getMessages() );
    }
    
    @Test.Impl( 
    	member = "field: Procedure TestCase.afterThis", 
    	description = "Initially NOOP" 
    )
    public void tm_0C5DBD73A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( Procedure.NOOP, tcd.getAfterThis() );
    }
    
    @Test.Impl( 
    	member = "field: String TestCase.fileLocation", 
    	description = "Initially null" 
    )
    public void tm_0CAEB0C6C( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "field: Test.Container TestCase.container", 
    	description = "Not null" 
    )
    public void tm_0FF0D5DB2( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertNonNull( tcd.getContainer() );
    }
    
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.FAIL", 
    	description = "FAIL cases are test fails" 
    )
    public void tm_039A10C50( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    	tc.assertTrue( tcd.getTestCase().getFailCount() > 0 );
    }
    
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.OPEN", 
    	description = "OPEN cases are test fails" 
    )
    public void tm_0CF4AE5D0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tc.assertTrue( tcd.getTestCase().getFailCount() > 0 );
    }
    
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.PASS", 
    	description = "PASS cases are test passes" 
    )
    public void tm_0DAEA5DDA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    	tc.assertTrue( tcd.getTestCase().getPassCount() > 0 );
    }
    
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.state", 
    	description = "Initially OPEN" 
    )
    public void tm_000E7B607( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "field: TestImpl TestCase.impl", 
    	description = "Not null" 
    )
    public void tm_02C91CBDA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertNonNull( tcd.getTestCaseImpl() );
    }
    
    @Test.Impl( 
    	member = "field: Throwable TestCase.unexpectedError", 
    	description = "Initially null" 
    )
    public void tm_0A35AA6CF( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "field: long TestCase.elapsedTime", 
    	description = "Initially zero" 
    )
    public void tm_0B8A918C4( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( 0L, tcd.getTestCase().getElapsedTime() );
    }
    
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Starts with FAIL if failed" 
    )
    public void tm_06AED5866( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().toString().startsWith( "FAIL" ) );
    }
    
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Starts with PASS if passed" 
    )
    public void tm_0B31DEC4C( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().toString().startsWith( "PASS" ) );
    }
    
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Statrs with OPEN if pass/fail is unknown" 
    )
    public void tm_03590B1F1( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().toString().startsWith( "OPEN" ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Does not alter State",
    	weight = 3
    )
    public void tm_0928753F0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	TestCase.State before = tcd.getState();
    	tcd.getTestCase().addMessage( "msg" );
    	tc.assertEqual( before, tcd.getState() );
    	
    	tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	before = tcd.getState();
    	tcd.getTestCase().addMessage( "msg" );
    	tc.assertEqual( before, tcd.getState() );
    	
    	tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	before = tcd.getState();
    	tcd.getTestCase().addMessage( "msg" );
    	tc.assertEqual( before, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "File location is set" 
    )
    public void tm_0A4F69A10( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().addMessage( "msg" );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Message is included in details." 
    )
    public void tm_02A38A2F9( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	String msg = "Some random message";
    	tc.assertFalse( tcd.print().contains( msg ) );
    	tcd.getTestCase().addMessage( msg );
    	tc.assertTrue( tcd.print().contains( msg ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Return is this" 
    )
    public void tm_0F908BBA1( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.addMessage( "hi" ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Throws AssertionError for empty message" 
    )
    public void tm_0D3CF612F( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	TestCaseData.forMethod( "NOOP" ).getTestCase().addMessage( "" );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Throws AssertionError for null message" 
    )
    public void tm_081D045E9( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	TestCaseData.forMethod( "NOOP" ).getTestCase().addMessage( null );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Does not alter State",
    	weight = 3
    )
    public void tm_0BD36E122( Test.Case tc ) {
    	Procedure p = () -> {};
    	
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	TestCase.State before = tcd.getState();
    	tcd.getTestCase().afterThis( p );
    	tc.assertEqual( before, tcd.getState() );
    	
    	tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	before = tcd.getState();
    	tcd.getTestCase().afterThis( p );
    	tc.assertEqual( before, tcd.getState() );
    	
    	tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	before = tcd.getState();
    	tcd.getTestCase().afterThis( p );
    	tc.assertEqual( before, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "File location is set" 
    )
    public void tm_0CFA62742( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().afterThis( () -> {} );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Return is this" 
    )
    public void tm_08CEE9653( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.afterThis( () -> {} ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Throws AssertionError for null procedure" 
    )
    public void tm_027E21827( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	TestCaseData.forMethod( "NOOP" ).getTestCase().afterThis( null );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "File location is set" 
    )
    public void tm_08D3ED115( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertEqual( 42, 42 );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Return is this" 
    )
    public void tm_06D73DA66( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertEqual( "hello", "world" ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Test fails for inequivalent" 
    )
    public void tm_0D8688053( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	List<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toList() );
    	List<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toList() );
    	tcd.getTestCase().assertEqual( abc, cba );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Test passes for equivalent objects" 
    )
    public void tm_0A32ACECA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	Set<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toSet() );
    	Set<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toSet() );
    	tcd.getTestCase().assertEqual( abc, cba );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFail(String)", 
    	description = "Case fails" 
    )
    public void tm_03E469684( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertFail( "fail" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFail(String)", 
    	description = "Failure message is included" 
    )
    public void tm_06A65EDDA( Test.Case tc ) {
    	String msg = "A unique failure message.";
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertFalse( tcd.print().contains( msg ) );
    	tcd.getTestCase().assertFail( msg );
    	tc.assertTrue( tcd.print().contains( msg ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFail(String)", 
    	description = "File location is set" 
    )
    public void tm_01784AA72( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertFail( "hi" );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFail(String)", 
    	description = "Return is this" 
    )
    public void tm_0B1FB3D83( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertFail( "hi" ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Case fails for true" 
    )
    public void tm_0C3A7304F( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertFalse( true );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Case passes for false" 
    )
    public void tm_0FB86BC0A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertFalse( false );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "File location is set" 
    )
    public void tm_086BBAB1A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertFalse( false );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Return is this" 
    )
    public void tm_09409DC2B( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertFalse( true ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Case fails for non-null object" 
    )
    public void tm_0C2F7CDE0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertIsNull( "" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Case passes for null object" 
    )
    public void tm_022C03D8C( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertIsNull( null );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "File location is set" 
    )
    public void tm_00032EECD( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertIsNull( null );
    	tc.assertNonNull( tcd.getFileLocation() );
     }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Return is this" 
    )
    public void tm_0BA5EA21E( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertIsNull( null ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Case fails for null" 
    )
    public void tm_0FE31D5E0( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNonNull( null );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
     }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Case passes for non-null" 
    )
    public void tm_0E1846FBE( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNonNull( "" );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "File location is set" 
    )
    public void tm_0D6483642( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertNonNull( null );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Return is this" 
    )
    public void tm_07E07E553( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertNonNull( null ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case fails for empty string" 
    )
    public void tm_07F303449( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNotEmpty( "" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case fails for null string" 
    )
    public void tm_0A6C47EFB( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNotEmpty( null );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case passes for non-empty string" 
    )
    public void tm_095428DCF( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNotEmpty( " " );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "File location is set" 
    )
    public void tm_0742FACD6( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertNotEmpty( "" );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Return is this" 
    )
    public void tm_0A220DAE7( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertNotEmpty( "" ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "File location is set" 
    )
    public void tm_0BEAD9B90( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertNotEqual( "", "" );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "Return is this" 
    )
    public void tm_0627EDD21( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertNotEqual( 1, 2 ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "Test fails for equivalent objects" 
    )
    public void tm_01E192747( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	Set<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toSet() );
    	Set<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toSet() );
    	tcd.getTestCase().assertNotEqual( abc, cba );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "Test fails when both null" 
    )
    public void tm_017A9D2D2( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNotEqual( null, null );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "Test passes for inequivalent" 
    )
    public void tm_084F36AB6( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	List<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toList() );
    	List<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toList() );
    	tcd.getTestCase().assertNotEqual( abc, cba );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEqual(Object, Object)", 
    	description = "Test passes for one null and one not null" 
    )
    public void tm_0E7A5D7E5( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertNotEqual( null, "" );
    	tcd.getTestCase().assertNotEqual( "", null );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertPass()", 
    	description = "Case passes" 
    )
    public void tm_03AC7014A( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertPass();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertPass()", 
    	description = "File location is set" 
    )
    public void tm_0E591072E( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertPass();
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertPass()", 
    	description = "Return is this" 
    )
    public void tm_0A4CAD73F( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertPass() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Case fails for false" 
    )
    public void tm_0A7538BBB( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertTrue( false );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Case passes for true" 
    )
    public void tm_0D709B1C0( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().assertTrue( true );
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "File location is set" 
    )
    public void tm_07EE17EFD( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().assertTrue( false );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Return is this" 
    )
    public void tm_04289164E( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.assertTrue( false ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "File location is set" 
    )
    public void tm_0F32873BC( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertIsNull( tcd.getFileLocation() );
    	tcd.getTestCase().expectError( Exception.class );
    	tc.assertNonNull( tcd.getFileLocation() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Return is this" 
    )
    public void tm_0E15A464D( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.expectError( Exception.class ) );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Test.Case fails if expected error already set" 
    )
    public void tm_06699588B( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().expectError( Exception.class );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.getTestCase().expectError( Exception.class );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Throws AssertionError for null error" 
    )
    public void tm_0C09C3B16( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
       	tc.expectError( AssertionError.class );
       	tcd.getTestCase().expectError( null );
    }
    
    @Test.Impl( 
    	member = "method: Test.Case TestCase.getTestCase()", 
    	description = "Physically equal to this" 
    )
    public void tm_00096BC01( Test.Case tc ) {
       	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
       	TestCase me = tcd.getTestCase();
       	tc.assertEqual( me, me.getTestCase() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterEach: State is FAIL" 
    )
    public void tm_03A759DAE( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setAfterEach( new TestProcedure( 0L, new Error() ) );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterEach: afterThis called" 
    )
    public void tm_00DD875F0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.setAfterEach( new TestProcedure( 0L, new Error() ) );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterEach: elapsedTime recorded" 
    )
    public void tm_0FC7CECB4( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.getTestCase().afterThis( new TestProcedure( 1L, null ) );
    	tcd.setAfterEach( new TestProcedure( 2L, new Error() ) );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 3L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterEach: unexpectedError is not null" 
    )
    public void tm_055C7B50A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setAfterEach( new TestProcedure( 0L, new Error() ) );
    	tcd.run();
    	tc.assertNonNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterThis: State is FAIL" 
    )
    public void tm_0911BDEB1( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.getTestCase().afterThis( new TestProcedure( 0L, new Error() ) );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterThis: afterEach called" 
    )
    public void tm_05EF2FE0A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.getTestCase().afterThis( new TestProcedure( 0l, new Error() ) );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterThis: elapsedTime recorded" 
    )
    public void tm_0631F1251( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.getTestCase().afterThis( new TestProcedure( 2L, new Error() ) );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 2L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in afterThis: unexpectedError is not null" 
    )
    public void tm_0D57A74CD( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.getTestCase().afterThis( new TestProcedure( 0L, new Error() ) );
    	tcd.run();
    	tc.assertNonNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in beforeEach: State is FAIL" 
    )
    public void tm_090BC9BE9( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setBeforeEach( new TestProcedure( 0L, new Error() ) );
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in beforeEach: afterEach called" 
    )
    public void tm_0115695D2( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setBeforeEach( new TestProcedure( 0L, new Error() ) );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in beforeEach: afterThis called" 
    )
    public void tm_03D6389D5( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setBeforeEach( new TestProcedure( 0L, new Error() ) );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in beforeEach: elapsedTime recorded" 
    )
    public void tm_086E18619( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setBeforeEach( new TestProcedure( 2L, new Error() ) );
    	tcd.setAfterEach( new TestProcedure( 1L, null ) );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 3L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Error in beforeEach: unexpectedError is not null" 
    )
    public void tm_0A28BB005( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.setBeforeEach( new TestProcedure( 0L, new Error() ) );
    	tcd.run();
    	tc.assertNonNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got expected error: State is PASS" 
    )
    public void tm_0213FD847( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ExpectedError" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got expected error: afterEach called" 
    )
    public void tm_041C801C7( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ExpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setBeforeEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got expected error: afterThis called" 
    )
    public void tm_06DD4F5CA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ExpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got expected error: elapsedTime recorded" 
    )
    public void tm_00821EF8E( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ExpectedError" );
    	tcd.setSleepTime( 4 );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 4 );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got expected error: unexpectedError is null" 
    )
    public void tm_0DFCFB783( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ExpectedError" );
    	tcd.run();
    	tc.assertIsNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got unexpected error: State is FAIL" 
    )
    public void tm_0F0E413DB( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "UnexpectedError" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got unexpected error: afterEach called" 
    )
    public void tm_0A255C0A0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "UnexpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got unexpected error: afterThis called" 
    )
    public void tm_0CE62B4A3( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "UnexpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got unexpected error: elapsedTime recorded" 
    )
    public void tm_0DB9299E7( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "UnexpectedError" );
    	tcd.setSleepTime( 2L );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 2L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got unexpected error: unexpectedError is not null" 
    )
    public void tm_022415877( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "UnexpectedError" );
    	tcd.run();
    	tc.assertNonNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: State is FAIL" 
    )
    public void tm_050EE6C4B( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: afterEach called" 
    )
    public void tm_0F63F5230( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: afterThis called" 
    )
    public void tm_0224C4633( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: elapsedTime recorded" 
    )
    public void tm_01638E377( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	tcd.setSleepTime( 5L );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 5L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: unexpectedError is different from expected" 
    )
    public void tm_0FF5E33CA( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	tcd.run();
    	tc.assertNotEqual( tcd.getExpectedError(), tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "Got wrong error: unexpectedError is not null" 
    )
    public void tm_065502CE7( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "WrongError" );
    	tcd.run();
    	tc.assertNonNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: State is FAIL if assertion fails" 
    )
    public void tm_031374B0B( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.getTestCase().assertTrue( false );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: State is OPEN if no assertions" 
    )
    public void tm_062006F58( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: State is PASS if assertion succeeds" 
    )
    public void tm_00704F204( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.getTestCase().assertFalse( false );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: afterEach called" 
    )
    public void tm_01DFCC9FC( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: afterThis called" 
    )
    public void tm_04A09BDFF( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: elapsedTime recorded" 
    )
    public void tm_0AF9D9543( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.setSleepTime( 3L );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 3L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No error: unexpectedError is null" 
    )
    public void tm_00DC839AE( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.run();
    	tc.assertIsNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No expected error: State is FAIL" 
    )
    public void tm_03B886CED( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoExpectedError" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No expected error: afterEach called" 
    )
    public void tm_0CDAEF64E( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoExpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.setAfterEach( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No expected error: afterThis called" 
    )
    public void tm_0F9BBEA51( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoExpectedError" );
    	TestProcedure tp = new TestProcedure( 0L, null );
    	tcd.getTestCase().afterThis( tp );
    	tcd.run();
    	tc.assertTrue( tp.executed() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No expected error: elapsedTime recorded" 
    )
    public void tm_0A6DD4895( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoExpectedError" );
    	tcd.setSleepTime( 2L );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 2L );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.run()", 
    	description = "No expected error: unexpectedError is null" 
    )
    public void tm_0ABACED1C( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoExpectedError" );
    	tcd.run();
    	tc.assertIsNull( tcd.getUnexpectedError() );
    }
    
    @Test.Impl( 
    	member = "method: TestCase TestCase.showProgress(boolean)", 
    	description = "Returns this TestCase instance to allow chaining" 
    )
    public void tm_0A347FAC5( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NOOP" );
    	TestCase me = tcd.getTestCase();
    	tc.assertEqual( me, me.showProgress( false ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean TestCase.equals(Object)", 
    	description = "If compareTo not zero then not equal",
    	weight = 4
    )
    public void tm_010BB0742( Test.Case tc ) {
    	// IF tc1.compareTo( tc2 ) != 0 THEN ! tc1.equals( tc2 )
    	// tc1.compareTo( tc2 ) == 0  ||  ! tc1.equals( tc2 )
    	
    	List<TestCaseData> tcds = Stream.of( "AA0", "AA1", "AB0", "AB1", "BA0", "BA1", "BB0", "BB1" )
    		.map( s -> "Ordered" + s )
    		.map( TestCaseData::forMethod )
    		.collect( Collectors.toList() );
    	
    	TestCase tc1, tc2;
    	for( TestCaseData tcd1 : tcds ) {
    		for( TestCaseData tcd2 : tcds ) {
    			tc1 = tcd1.getTestCase();
    			tc2 = tcd2.getTestCase();
    			tc.assertTrue( tc1.compareTo( tc2 ) == 0  ||  ! tc1.equals( tc2 ) );
    		}
    	}
    }
    
    @Test.Impl( 
    	member = "method: boolean TestCase.equals(Object)", 
    	description = "If compareTo zero then equal",
    	weight = 4
    )
    public void tm_0130B2F9C( Test.Case tc ) {
    	// IF tc1.compareTo( tc2 ) == 0 THEN tc1.equals( tc2 )
    	// tc1.compareTo( tc2 ) != 0  ||  tc1.equals( tc2 )
    	
    	List<TestCaseData> tcds = Stream.of( "AA0", "AA1", "AB0", "AB1", "BA0", "BA1", "BB0", "BB1" )
    		.map( s -> "Ordered" + s )
    		.map( TestCaseData::forMethod )
    		.collect( Collectors.toList() );
    	
    	TestCase tc1, tc2;
    	for( TestCaseData tcd1 : tcds ) {
    		for( TestCaseData tcd2 : tcds ) {
    			tc1 = tcd1.getTestCase();
    			tc2 = tcd2.getTestCase();
    			tc.assertTrue( tc1.compareTo( tc2 ) != 0  ||  tc1.equals( tc2 ) );
    		}
    	}
    }
    
    @Test.Impl( 
    	member = "method: boolean TestCase.threadsafe()", 
    	description = "Consistent with configured value" 
    )
    public void tm_0867D007A( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "ThreadSafe" );
    	tc.assertTrue( tcd.getTestCase().threadsafe() );
    	tcd = TestCaseData.forMethod( "NotThreadSafe" );
    	tc.assertFalse( tcd.getTestCase().threadsafe() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.compareTo(TestCase)", 
    	description = "For equal priority and member ordered by description" 
    )
    public void tm_0EC4EAD2D( Test.Case tc ) {
    	TestCase tc1 = TestCaseData.forMethod( "OrderedAA0" ).getTestCase();
    	TestCase tc2 = TestCaseData.forMethod( "OrderedAB0" ).getTestCase();
    	tc.assertTrue( tc1.compareTo( tc2 ) < 0 );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.compareTo(TestCase)", 
    	description = "For equal priority ordered by member" 
    )
    public void tm_0864954FA( Test.Case tc ) {
    	TestCase tc1 = TestCaseData.forMethod( "OrderedAB0" ).getTestCase();
    	TestCase tc2 = TestCaseData.forMethod( "OrderedBA0" ).getTestCase();
    	tc.assertTrue( tc1.compareTo( tc2 ) < 0 );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getFailCount()", 
    	description = "Return is Test.Impl.weight when State is FAIL" 
    )
    public void tm_069B1FAD1( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    	tc.assertEqual( OurContainer.FAIL_WT, tcd.getTestCase().getFailCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getFailCount()", 
    	description = "Return is Test.Impl.weight when State is OPEN" 
    )
    public void tm_079DF2EFD( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tc.assertEqual( OurContainer.OPEN_WT, tcd.getTestCase().getFailCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getFailCount()", 
    	description = "Return is zero when State is PASS" 
    )
    public void tm_0EAD20B46( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    	tc.assertEqual( 0, tcd.getTestCase().getFailCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getPassCount()", 
    	description = "Return is Test.Impl.weight when State is PASS" 
    )
    public void tm_044F4FD71( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    	tc.assertEqual( OurContainer.PASS_WT, tcd.getTestCase().getPassCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getPassCount()", 
    	description = "Return is zero when State is FAIL" 
    )
    public void tm_07E275100( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    	tc.assertEqual( 0, tcd.getTestCase().getPassCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.getPassCount()", 
    	description = "Return is zero when State is OPEN" 
    )
    public void tm_08E54852C( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tc.assertEqual( 0, tcd.getTestCase().getPassCount() );
    }
    
    @Test.Impl( 
    	member = "method: int TestCase.hashCode()", 
    	description = "If equal then same hashCode",
    	weight = 4
    )
    public void tm_08B7DB543( Test.Case tc ) {
    	// IF tc1.equals( tc2 ) THEN tc1.hashCode() == tc2.hashCode()
    	// ! tc1.equals( tc2 )  ||  tc1.hashCode() == tc2.hashCode()
    	
    	List<TestCaseData> tcds = Stream.of( "AA0", "AA1", "AB0", "AB1", "BA0", "BA1", "BB0", "BB1" )
    		.map( s -> "Ordered" + s )
    		.map( TestCaseData::forMethod )
    		.collect( Collectors.toList() );
    	
    	TestCase tc1, tc2;
    	for( TestCaseData tcd1 : tcds ) {
    		for( TestCaseData tcd2 : tcds ) {
    			tc1 = tcd1.getTestCase();
    			tc2 = tcd2.getTestCase();
    			tc.assertTrue( ! tc1.equals( tc2 )  ||  tc1.hashCode() == tc2.hashCode() );
    		}
    	}
    }
    
    @Test.Impl( 
    	member = "method: long TestCase.getElapsedTime()", 
    	description = "Elapsed time is consistent with execution time" 
    )
    public void tm_0F6E3A253( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "NoError" );
    	tcd.setSleepTime( 1L );
    	tcd.setBeforeEach( new TestProcedure( 2L, null ) );
    	tcd.setAfterEach( new TestProcedure( 3L, null ) );
    	tcd.getTestCase().afterThis( new TestProcedure( 4L, null )  );
    	tcd.run();
    	tc.assertTrue( tcd.getTestCase().getElapsedTime() >= 10L  );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "If old state is FAIL, new state is FAIL" 
    )
    public void tm_0EBA3BDE5( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    	tcd.fail( "hi" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "If old state is OPEN, new state is FAIL" 
    )
    public void tm_039B2E239( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.fail( "hi" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "If old state is PASS, new state is FAIL" 
    )
    public void tm_0A91ED6D2( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    	tcd.fail( "hi" );
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "The message is retained" 
    )
    public void tm_0C8BE02CF( Test.Case tc ) {
    	String msg = "An elaborate error message";
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tcd.fail( msg );
    	tc.assertTrue( tcd.print().contains( msg ) );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "Throws AssertionError for empty message" 
    )
    public void tm_0CCE2B4C9( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.expectError( AssertionError.class );
    	tcd.fail( "" );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.fail(String)", 
    	description = "Throws AssertionError for null message" 
    )
    public void tm_0A2C96DE1( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.expectError( AssertionError.class );
    	tcd.fail( null );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.pass()", 
    	description = "If old state is FAIL, new state is FAIL" 
    )
    public void tm_03BA40429( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Fail" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    	tcd.pass();
    	tc.assertEqual( TestCase.State.FAIL, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.pass()", 
    	description = "If old state is OPEN, new state is PASS" 
    )
    public void tm_09AC844B0( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Open" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.OPEN, tcd.getState() );
    	tcd.pass();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.pass()", 
    	description = "If old state is PASS, new state is PASS" 
    )
    public void tm_00A343949( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    	tcd.pass();
    	tc.assertEqual( TestCase.State.PASS, tcd.getState() );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Includes additional messages on failure" 
    )
    public void tm_06503EAF3( Test.Case tc ) {
    	String msg = "An elaborate error message";
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tcd.fail( msg );
    	tc.assertTrue( tcd.print().contains( msg ) );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Includes fail messages on failure" 
    )
    public void tm_0DA55B5AA( Test.Case tc ) {
    	String msg = "An elaborate error message";
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tcd.fail( msg );
    	tc.assertTrue( tcd.print().contains( msg ) );
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Includes file location on failure" 
    )
    public void tm_0144F6DBF( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Prints causes when excpetion is thrown" 
    )
    public void tm_0641DAB89( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Prints stack trace when excpetion is thrown" 
    )
    public void tm_05AA9AAF8( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Prints summary line" 
    )
    public void tm_0EC920742( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: void TestCase.print(IndentWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0AEE2C608( Test.Case tc ) {
    	TestCaseData tcd = TestCaseData.forMethod( "Pass" );
    	tcd.run();
    	tc.expectError( AssertionError.class );
    	tcd.getTestCase().print( null );
    }
	


    
        
    public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( TestCase.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( TestCase.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

    	//Stream.of( "NOOP" ).map( TestCaseData::forMethod ).forEach( System.out::println );
    	//System.out.println( TestCaseData.forMethod( "NOOP" ) );
    	//System.out.println( TestCaseData.forMethod( "bogus" ) );
    	
    	System.out.println("\nDone!");
	}
    
}
