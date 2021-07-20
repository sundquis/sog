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
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.Test;
import sog.core.test.TestCase;
import sog.core.test.TestImpl;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

/**
 * 
 */
public class TestCaseTest extends Test.Container {

	
	public final Test.Container container;
	public final Map<String, TestImpl> TEST_IMPLS;
	
	public TestCase noop = null;
	public TestImpl impl = null;
	
	public TestCaseTest() {
		super( TestCase.class );
		
		this.container = new MyContainer();

		this.TEST_IMPLS = Arrays.stream( MyContainer.class.getDeclaredMethods() )
			.collect( Collectors.toMap( Method::getName, TestImpl::forMethod ) );
		
		this.noop = this.getCase( "noopMethod" );
		this.impl = this.getSubjectField( this.noop, "impl", impl );
	}
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.noop = this.getCase( "noopMethod" );
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.noop = null;
		};
	}
	
	public TestCase getCase( String name ) {
		return new TestCase( this.TEST_IMPLS.get( name ), this.container );
	}
	
	public String getFileLocation() {
		return this.getSubjectField( this.noop, "fileLocation", "" );
	}

	public static class MyContainer extends Test.Container {
		
		public static final String MEMBER_NAME = "This is the member name";
		public static final String DESCRIPTION = "This is the descriptoin";
		public static final int WEIGHT = 123;
		public static final int PRIORITY = 7;
		public static final long TIMEOUT = 57L;

		public MyContainer() { super( TestCase.class ); }
		
		@Test.Impl( member = MEMBER_NAME, description = DESCRIPTION, weight = WEIGHT, priority = PRIORITY, timeout = TIMEOUT )
		public void noopMethod( Test.Case tc ) {}
		
		@Test.Impl( member = "member", description = "description" )
		public void openMethod( Test.Case tc ) { tc.addMessage( "No assertions made" ); }
		
		@Test.Impl( member = "member", description = "description" )
		public void passMethod( Test.Case tc ) { tc.assertTrue( true ); }
		
		@Test.Impl( member = "member", description = "description" )
		public void failMethod( Test.Case tc ) { tc.assertTrue( false  ); }
				
	}
	
	
	// TEST CASES

    @Test.Impl( 
    	member = "constructor: TestCase(TestImpl, Test.Container)", 
    	description = "Throws AssertionError for null Test.Container" 
    )
    public void tm_0CA6FAACA( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestCase( this.impl, null );
    }
        
    @Test.Impl( 
    	member = "constructor: TestCase(TestImpl, Test.Container)", 
    	description = "Throws AssertionError for null TestImpl" 
    )
    public void tm_0BA36E6D7( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestCase( null, this.container );
    }
        
    @Test.Impl( 
    	member = "field: Class TestCase.expectedError", 
    	description = "Initially null" 
    )
    public void tm_0345429AE( Test.Case tc ) {
    	Class<?> expectedError = null;
    	expectedError = this.getSubjectField( this.noop, "expectedError", expectedError );
    	tc.assertIsNull( expectedError );
    }
        
    @Test.Impl( 
    	member = "field: List TestCase.messages", 
    	description = "Not null" 
    )
    public void tm_034D5CAE2( Test.Case tc ) {
    	List<?> messages = null;
    	messages = this.getSubjectField( this.noop, "messages", messages );
    	tc.assertNonNull( messages );
    }
        
    @Test.Impl( 
    	member = "field: Procedure TestCase.afterThis", 
    	description = "Initially NOOP" 
    )
    public void tm_0C5DBD73A( Test.Case tc ) {
    	Procedure afterThis = null;
    	afterThis = this.getSubjectField( this.noop, "afterThis", afterThis );
    	tc.assertEqual( Procedure.NOOP, afterThis );
    }
        
    @Test.Impl( 
    	member = "field: String TestCase.fileLocation", 
    	description = "Initially null" 
    )
    public void tm_0CAEB0C6C( Test.Case tc ) {
    	String fileLocation = null;
    	fileLocation = this.getSubjectField( this.noop, "fileLocation", fileLocation );
    	tc.assertIsNull( fileLocation );
    }
        
    @Test.Impl( 
    	member = "field: Test.Container TestCase.container", 
    	description = "Not null" 
    )
    public void tm_0FF0D5DB2( Test.Case tc ) {
    	Test.Container container = null;
    	container = this.getSubjectField( this.noop, "container", container );
    	tc.assertNonNull( container );
    }
        
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.FAIL", 
    	description = "FAIL cases are test fails" 
    )
    public void tm_039A10C50( Test.Case tc ) {
    	tc.assertFalse( TestCase.State.FAIL.passed() );
    }
        
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.OPEN", 
    	description = "OPEN cases are test fails" 
    )
    public void tm_0CF4AE5D0( Test.Case tc ) {
    	tc.assertFalse( TestCase.State.OPEN.passed() );
    }
        
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.State.PASS", 
    	description = "PASS cases are test passes" 
    )
    public void tm_0DAEA5DDA( Test.Case tc ) {
    	tc.assertTrue( TestCase.State.PASS.passed() );
    }
        
    @Test.Impl( 
    	member = "field: TestCase.State TestCase.state", 
    	description = "Initially OPEN" 
    )
    public void tm_000E7B607( Test.Case tc ) {
    	TestCase.State state = null;
    	state = this.getSubjectField( this.noop, "state", state );
    	tc.assertEqual( TestCase.State.OPEN, state );
    }
        
    @Test.Impl( 
    	member = "field: TestImpl TestCase.impl", 
    	description = "Not null" 
    )
    public void tm_02C91CBDA( Test.Case tc ) {
    	TestImpl impl = null;
    	impl = this.getSubjectField( this.noop, "impl", impl );
    	tc.assertNonNull( impl );
    }
        
    @Test.Impl( 
    	member = "field: Throwable TestCase.unexpectedError", 
    	description = "Initially null" 
    )
    public void tm_0A35AA6CF( Test.Case tc ) {
    	Throwable unexpectedError = null;
    	unexpectedError = this.getSubjectField( this.noop, "unexpectedError", unexpectedError );
    	tc.assertIsNull( unexpectedError );
    }
        
    @Test.Impl( 
    	member = "field: long TestCase.elapsedTime", 
    	description = "Initially zero" 
    )
    public void tm_0B8A918C4( Test.Case tc ) {
    	long elapsedTime = 0L;
    	elapsedTime = this.getSubjectField( this.noop, "elapsedTime", elapsedTime );
    	tc.assertEqual( 0L, elapsedTime );
    }
        
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Starts with FAIL if failed" 
    )
    public void tm_06AED5866( Test.Case tc ) {
    	TestCase fail = this.getCase( "failMethod" );
    	tc.assertFalse( fail.toString().startsWith( "FAIL" ) );
    	fail.run();
    	tc.assertTrue( fail.toString().startsWith( "FAIL" ) );
    }
        
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Starts with PASS if passed" 
    )
    public void tm_0B31DEC4C( Test.Case tc ) {
    	TestCase pass = this.getCase( "passMethod" );
    	tc.assertFalse( pass.toString().startsWith( "PASS" ) );
    	pass.run();
    	tc.assertTrue( pass.toString().startsWith( "PASS" ) );
    }
        
    @Test.Impl( 
    	member = "method: String TestCase.toString()", 
    	description = "Statrs with OPEN if pass/fail is unknown" 
    )
    public void tm_03590B1F1( Test.Case tc ) {
    	TestCase open = this.getCase( "openMethod" );
    	tc.assertTrue( open.toString().startsWith( "OPEN" ) );
    	open.run();
    	tc.assertTrue( open.toString().startsWith( "OPEN" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Does not alter State",
    	weight = 3
    )
    public void tm_0928753F0( Test.Case tc ) {
    	TestCase tstCase = this.getCase( "openMethod" );
    	tstCase.run();
    	TestCase.State before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.addMessage( "Hello world!" );
    	TestCase.State after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    	
    	tstCase = this.getCase( "passMethod" );
    	tstCase.run();
    	before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.addMessage( "Hello world!" );
    	after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    	
    	tstCase = this.getCase( "failMethod" );
    	tstCase.run();
    	before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.addMessage( "Hello world!" );
    	after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "File location is set" 
    )
    public void tm_0A4F69A10( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.addMessage( "Hi" );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Message is included in details." 
    )
    public void tm_02A38A2F9( Test.Case tc ) {
    	StringOutputStream sos = new StringOutputStream();
    	String message = "Hello world!";
    	this.noop.addMessage( message );
    	this.noop.print( new IndentWriter( sos ) );
    	tc.assertTrue( sos.toString().contains( message ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Return is this" 
    )
    public void tm_0F908BBA1( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.addMessage( "hi" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Throws AssertionError for empty message" 
    )
    public void tm_0D3CF612F( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.noop.addMessage( "" );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.addMessage(String)", 
    	description = "Throws AssertionError for null message" 
    )
    public void tm_081D045E9( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.noop.addMessage( null );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Does not alter State",
    	weight = 3
    )
    public void tm_0BD36E122( Test.Case tc ) {
    	Procedure p = () -> {};
    	
    	TestCase tstCase = this.getCase( "openMethod" );
    	tstCase.run();
    	TestCase.State before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.afterThis( p );
    	TestCase.State after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    	
    	tstCase = this.getCase( "passMethod" );
    	tstCase.run();
    	before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.afterThis( p );
    	after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    	
    	tstCase = this.getCase( "failMethod" );
    	tstCase.run();
    	before = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tstCase.afterThis( p );
    	after = this.getSubjectField( tstCase, "state", TestCase.State.OPEN );
    	tc.assertEqual( before, after );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "File location is set" 
    )
    public void tm_0CFA62742( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.afterThis( () -> {} );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Return is this" 
    )
    public void tm_08CEE9653( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.afterThis( () -> {} ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.afterThis(Procedure)", 
    	description = "Throws AssertionError for null procedure" 
    )
    public void tm_027E21827( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.noop.afterThis( null );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "File location is set" 
    )
    public void tm_08D3ED115( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertEqual( "A", "B" );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Return is this" 
    )
    public void tm_06D73DA66( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertEqual( "A", "B" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Test fails for inequivalent" 
    )
    public void tm_0D8688053( Test.Case tc ) {
    	List<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toList() );
    	List<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toList() );
    	this.noop.assertEqual( abc, cba );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertEqual(Object, Object)", 
    	description = "Test passes for equivalent objects" 
    )
    public void tm_0A32ACECA( Test.Case tc ) {
    	Set<String> abc = Stream.of( "A", "B", "C" ).collect( Collectors.toSet() );
    	Set<String> cba = Stream.of( "C", "B", "A" ).collect( Collectors.toSet() );
    	this.noop.assertEqual( abc, cba );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Case fails for true" 
    )
    public void tm_0C3A7304F( Test.Case tc ) {
    	this.noop.assertFalse( true );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Case passes for false" 
    )
    public void tm_0FB86BC0A( Test.Case tc ) {
    	this.noop.assertFalse( false );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "File location is set" 
    )
    public void tm_086BBAB1A( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertFalse( false );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertFalse(boolean)", 
    	description = "Return is this" 
    )
    public void tm_09409DC2B( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertFalse( false ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Case fails for non-null object" 
    )
    public void tm_0C2F7CDE0( Test.Case tc ) {
    	this.noop.assertIsNull( "hi" );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Case passes for null object" 
    )
    public void tm_022C03D8C( Test.Case tc ) {
    	this.noop.assertIsNull( null );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "File location is set" 
    )
    public void tm_00032EECD( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertIsNull( "" );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertIsNull(Object)", 
    	description = "Return is this" 
    )
    public void tm_0BA5EA21E( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertIsNull( "" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Case fails for null" 
    )
    public void tm_0FE31D5E0( Test.Case tc ) {
    	this.noop.assertNonNull( null );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Case passes for non-null" 
    )
    public void tm_0E1846FBE( Test.Case tc ) {
    	this.noop.assertNonNull( "hi" );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "File location is set" 
    )
    public void tm_0D6483642( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertNonNull( "" );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNonNull(Object)", 
    	description = "Return is this" 
    )
    public void tm_07E07E553( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertNonNull( "hi" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case fails for empty string" 
    )
    public void tm_07F303449( Test.Case tc ) {
    	this.noop.assertNotEmpty( "" );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case fails for null string" 
    )
    public void tm_0A6C47EFB( Test.Case tc ) {
    	this.noop.assertNotEmpty( null );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Case passes for non-empty string" 
    )
    public void tm_095428DCF( Test.Case tc ) {
    	this.noop.assertNotEmpty( "hi" );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "File location is set" 
    )
    public void tm_0742FACD6( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertNotEmpty( "hi" );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertNotEmpty(String)", 
    	description = "Return is this" 
    )
    public void tm_0A220DAE7( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertNotEmpty( "" ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Case fails for false" 
    )
    public void tm_0A7538BBB( Test.Case tc ) {
    	this.noop.assertTrue( false );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Case passes for true" 
    )
    public void tm_0D709B1C0( Test.Case tc ) {
    	this.noop.assertTrue( true );
    	tc.assertTrue( this.noop.getPassCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "File location is set" 
    )
    public void tm_07EE17EFD( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.assertTrue( true );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.assertTrue(boolean)", 
    	description = "Return is this" 
    )
    public void tm_04289164E( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.assertTrue( true ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "File location is set" 
    )
    public void tm_0F32873BC( Test.Case tc ) {
    	tc.assertIsNull( this.getFileLocation() );
    	this.noop.expectError( Error.class );
    	tc.assertNonNull( this.getFileLocation() );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Return is this" 
    )
    public void tm_0E15A464D( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.expectError( Error.class ) );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Test.Case fails if expected error already set" 
    )
    public void tm_06699588B( Test.Case tc ) {
    	this.noop.expectError( Error.class );
    	this.noop.expectError( Error.class );
    	tc.assertTrue( this.noop.getFailCount() > 0 );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.expectError(Class)", 
    	description = "Throws AssertionError for null error" 
    )
    public void tm_0C09C3B16( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.noop.expectError( null );
    }
        
    @Test.Impl( 
    	member = "method: Test.Case TestCase.getTestCase()", 
    	description = "Physically equal to this" 
    )
    public void tm_00096BC01( Test.Case tc ) {
    	tc.assertEqual( this.noop, this.noop.getTestCase() );
    }
        
    @Test.Impl( 
    	member = "method: boolean TestCase.equals(Object)", 
    	description = "If compareTo not zero then not equal" 
    )
    public void tm_010BB0742( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
        
        @Test.Impl( 
        	member = "method: boolean TestCase.equals(Object)", 
        	description = "If compareTo zero then equal" 
        )
        public void tm_0130B2F9C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.compareTo(TestCase)", 
        	description = "For equal priority and member ordered by description" 
        )
        public void tm_0EC4EAD2D( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.compareTo(TestCase)", 
        	description = "For equal priority ordered by member" 
        )
        public void tm_0864954FA( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.compareTo(TestCase)", 
        	description = "Respects Test.Impl.priority" 
        )
        public void tm_0DF73B7DC( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getFailCount()", 
        	description = "Return is Test.Impl.weight when State is FAIL" 
        )
        public void tm_069B1FAD1( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getFailCount()", 
        	description = "Return is Test.Impl.weight when State is OPEN" 
        )
        public void tm_079DF2EFD( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getFailCount()", 
        	description = "Return is zero when State is PASS" 
        )
        public void tm_0EAD20B46( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getPassCount()", 
        	description = "Return is Test.Impl.weight when State is PASS" 
        )
        public void tm_044F4FD71( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getPassCount()", 
        	description = "Return is zero when State is FAIL" 
        )
        public void tm_07E275100( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.getPassCount()", 
        	description = "Return is zero when State is OPEN" 
        )
        public void tm_08E54852C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: int TestCase.hashCode()", 
        	description = "If equal then same hashCode" 
        )
        public void tm_08B7DB543( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: long TestCase.getElapsedTime()", 
        	description = "Elapsed time is consistent with execution time" 
        )
        public void tm_0F6E3A253( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "If old state is FAIL, new state is FAIL" 
        )
        public void tm_0EBA3BDE5( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "If old state is OPEN, new state is FAIL" 
        )
        public void tm_039B2E239( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "If old state is PASS, new state is FAIL" 
        )
        public void tm_0A91ED6D2( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "The message is retained" 
        )
        public void tm_0C8BE02CF( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "The message must not be empty" 
        )
        public void tm_0CCE2B4C9( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.fail(String)", 
        	description = "The message must not be null" 
        )
        public void tm_0A2C96DE1( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.pass()", 
        	description = "If old state is FAIL, new state is FAIL" 
        )
        public void tm_03BA40429( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.pass()", 
        	description = "If old state is OPEN, new state is PASS" 
        )
        public void tm_09AC844B0( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.pass()", 
        	description = "If old state is PASS, new state is PASS" 
        )
        public void tm_00A343949( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Includes additional messages on failure" 
        )
        public void tm_06503EAF3( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Includes error information on failure" 
        )
        public void tm_0F3370A64( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Includes fail messages on failure" 
        )
        public void tm_0DA55B5AA( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Includes file location on failure" 
        )
        public void tm_0144F6DBF( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Prints summary line" 
        )
        public void tm_0EC920742( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.print(IndentWriter)", 
        	description = "Throws AssertionError for null writer" 
        )
        public void tm_0AEE2C608( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterEach: State is FAIL" 
        )
        public void tm_0DAA1815C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterEach: afterThis called" 
        )
        public void tm_0614CCB82( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterEach: elapsedTime recorded" 
        )
        public void tm_02BC82946( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterEach: unexpectedError is null" 
        )
        public void tm_06CA682CB( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterThis: State is FAIL" 
        )
        public void tm_03147C25F( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterThis: afterEach called" 
        )
        public void tm_0B267539C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterThis: elapsedTime recorded" 
        )
        public void tm_0926A4EE3( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in afterThis: unexpectedError is null" 
        )
        public void tm_0FBDD9C0E( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in beforeEach: State is FAIL" 
        )
        public void tm_0F60D2DFB( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in beforeEach: afterEach called" 
        )
        public void tm_02C6CF280( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in beforeEach: afterThis called" 
        )
        public void tm_05879E683( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in beforeEach: elapsedTime recorded" 
        )
        public void tm_040FDDBC7( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Error in beforeEach: unexpectedError is not null" 
        )
        public void tm_0EFE23A97( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got expected error: State is PASS" 
        )
        public void tm_0C16BBBF5( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got expected error: afterEach called" 
        )
        public void tm_0953C5759( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got expected error: afterThis called" 
        )
        public void tm_0C1494B5C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got expected error: elapsedTime recorded" 
        )
        public void tm_0376D2C20( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got expected error: unexpectedError is null" 
        )
        public void tm_0842D59B1( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got unexpected error: State is FAIL" 
        )
        public void tm_035A5C409( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got unexpected error: afterEach called" 
        )
        public void tm_0EA0AF9B2( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got unexpected error: afterThis called" 
        )
        public void tm_01617EDB5( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got unexpected error: elapsedTime recorded" 
        )
        public void tm_06500F9F9( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got unexpected error: unexpectedError is not null" 
        )
        public void tm_07FBC2025( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: State is FAIL" 
        )
        public void tm_0116ED75D( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: afterEach called" 
        )
        public void tm_0966B35DE( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: afterThis called" 
        )
        public void tm_0C27829E1( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: elapsedTime recorded" 
        )
        public void tm_0314F4025( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: unexpectedError is different from expected" 
        )
        public void tm_046BDB9F8( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "Got wrong error: unexpectedError is not null" 
        )
        public void tm_04CA6D079( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: State is FAIL if assertion fails" 
        )
        public void tm_0BAA5AB1D( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: State is PASS if assertion succeeds" 
        )
        public void tm_00A82C0B2( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: afterEach called" 
        )
        public void tm_075818E0E( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: afterThis called" 
        )
        public void tm_0A18E8211( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: elapsedTime recorded" 
        )
        public void tm_0701E0055( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No error: unexpectedError is null" 
        )
        public void tm_0ADF41D5C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No expected error: State is FAIL" 
        )
        public void tm_0DD9A5B7F( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No expected error: afterEach called" 
        )
        public void tm_01270A67C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No expected error: afterThis called" 
        )
        public void tm_03E7D9A7F( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No expected error: elapsedTime recorded" 
        )
        public void tm_055CF31C3( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void TestCase.run()", 
        	description = "No expected error: unexpectedError is null" 
        )
        public void tm_0351B4D2E( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
	


	public static void main( String[] args ) {
		Test.eval( TestCase.class );
		//Test.evalPackage( TestCase.class );
		//TestCaseTest tct = new TestCaseTest();
		//System.out.println( Strings.toString( tct.TEST_CASES ));
	}
}
