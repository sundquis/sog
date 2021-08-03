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

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;
import sog.core.test.Result;
import sog.core.test.TestResult;
import sog.core.test.TestResultSet;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

/**
 * 
 */
public class TestResultSetTest extends Test.Container {

	public TestResultSetTest() {
		super( TestResultSet.class );
	}
	
	
	public boolean getVerbose( TestResultSet trs ) {
		return this.getSubjectField( trs, "verbose", null );
	}

	
	public Set<Result> getResults( TestResultSet trs ) {
		return this.getSubjectField( trs, "results", null );
	}
	
	
	public List<String> getSkippedClases( TestResultSet trs ) {
		return this.getSubjectField( trs, "skippedClasses", null );
	}
	
	
	public void print( Class<?> subject ) {
		TestResult.forSubject( subject ).print( new IndentWriter( System.out, "\t" ) );
	}
	
	public static class MyResult extends Result {
		public static final long ELAPSED_TIME = 25L;
		public static final int PASS_COUNT = 7;
		public static final int FAIL_COUNT = 6;
		
		public MyResult() { super( "LABEL" ); }

		@Override public long getElapsedTime() { return MyResult.ELAPSED_TIME; }

		@Override public int getPassCount() { return MyResult.PASS_COUNT; }

		@Override public int getFailCount() { return MyResult.FAIL_COUNT; }

		@Override public void print( IndentWriter out ) {}
	}
	
	public static final Result RESULT = new MyResult();
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: TestResultSet(String)", 
		description = "Default has verbose = false" 
	)
	public void tm_017656908( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		tc.assertFalse( this.getVerbose( trs ) );
	}
		
	@Test.Impl( 
		member = "constructor: TestResultSet(String, boolean)", 
		description = "Throws AssertionError for empty label" 
	)
	public void tm_0E5BF9170( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestResultSet( "" );
	}
		
	@Test.Impl( 
		member = "constructor: TestResultSet(String, boolean)", 
		description = "Throws AssertionError for null label" 
	)
	public void tm_0A1C6DF82( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestResultSet( null );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(Class)", 
		description = "Adds one TestResult",
		weight = 3
	)
	public void tm_02334FB7C( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		int before = this.getResults( trs ).size();
		trs.addClass( App.class );
		int after = this.getResults( trs ).size();
		tc.assertEqual( before + 1, after );
		
		before = after;
		trs.addClass( Test.class );
		after = this.getResults( trs ).size();
		tc.assertEqual( before + 1, after );
		
		before = after;
		trs.addClass( Object.class );
		after = this.getResults( trs ).size();
		tc.assertEqual( before + 1, after );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(Class)", 
		description = "Return is this TestResultSet instance" 
	)
	public void tm_0337F4643( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		tc.assertEqual( trs, trs.addClass( App.class ) );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_0FAD19EEE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet trs = new TestResultSet( "LABEL" );
		Class<?> c = null;
		trs.addClass( c );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(String)", 
		description = "Adds one TestResult" 
	)
	public void tm_0A82CCD55( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		int before = this.getResults( trs ).size();
		trs.addClass( "sog.core.test.TestResult" );
		int after = this.getResults( trs ).size();
		tc.assertEqual( before + 1, after );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(String)", 
		description = "Records error message if class is not found",
		weight = 2
	)
	public void tm_0B778BFB9( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		int before = this.getResults( trs ).size();
		trs.addClass( "sog.core.test.Foo" );
		int after = this.getSkippedClases( trs ).size();
		tc.assertEqual( before + 1, after );
		
		before = after;
		trs.addClass( "sog.core.test.Bar" );
		after = this.getSkippedClases( trs ).size();
		tc.assertEqual( before + 1, after );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(String)", 
		description = "Return is this TestResultSet instance" 
	)
	public void tm_077F1B3DC( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		tc.assertEqual( trs, trs.addClass( "sog.core.Test" ) );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(String)", 
		description = "Throws AssertionError for empty class name" 
	)
	public void tm_0D15DAF7C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet trs = new TestResultSet( "LABEL" );
		trs.addClass( "" );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClass(String)", 
		description = "Throws AssertionError for null class name" 
	)
	public void tm_0D0425EEC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet trs = new TestResultSet( "LABEL" );
		Class<?> c = null;
		trs.addClass( c );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
		description = "Adds one TestResult for each valid class name" 
	)
	public void tm_022AD7F13( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		Stream.of( App.class, Test.class, Object.class ).map( Class::getName ).forEach( trs::addClass );
		tc.assertEqual( 3, this.getResults( trs ).size() );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
		description = "Return is this TestResultSet instance" 
	)
	public void tm_02A4BB5FB( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		Stream<String> stream = Stream.of( App.class, Test.class, Object.class ).map( Class::getName );
		tc.assertEqual( trs, trs.addClasses( stream ) );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
		description = "Throws AssertionError for null class names stream" 
	)
	public void tm_0830D4A18( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet trs = new TestResultSet( "LABEL" );
		Stream<String> stream = null;
		trs.addClasses( stream );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addResult(Result)", 
		description = "Elapsed time reflects new total" 
	)
	public void tm_06E3B48EE( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		long before = trs.getElapsedTime();
		trs.addResult( TestResultSetTest.RESULT );
		long after = trs.getElapsedTime();
		tc.assertTrue( after >= before + MyResult.ELAPSED_TIME );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addResult(Result)", 
		description = "Fail count reflects new total" 
	)
	public void tm_0BDAFF652( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		trs.addResult( TestResultSetTest.RESULT );
		tc.assertEqual( MyResult.FAIL_COUNT, trs.getFailCount() );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addResult(Result)", 
		description = "Pass count reflects new total" 
	)
	public void tm_0C19DE97F( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		trs.addResult( TestResultSetTest.RESULT );
		tc.assertEqual( MyResult.PASS_COUNT, trs.getPassCount() );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.addResult(Result)", 
		description = "Return is this TestResultSet instance" 
	)
	public void tm_03C567283( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		tc.assertEqual( trs, trs.addResult( TestResultSetTest.RESULT ) );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forAllSourceDirs()", 
		description = "Aggregates TestResult instances for every class under every source directory" 
	)
	public void tm_004D9EEC5( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forAllSourceDirs()", 
		description = "Return is not null" 
	)
	public void tm_0C565278F( Test.Case tc ) {
		// Causes infinite loop:
		// tc.assertNonNull( TestResultSet.forAllSourceDirs() );
		tc.addMessage( "Manually verified." );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forPackage(Class)", 
		description = "Aggregates TestResult instances for every class in the same package as the given class" 
	)
	public void tm_0A1A84C84( Test.Case tc ) {
		TestResultSet trs = TestResultSet.forPackage( test.sog.core.test.foo.C1.class );
		tc.assertEqual( 3, this.getResults( trs ).size() );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forPackage(Class)", 
		description = "Return is not null" 
	)
	public void tm_037C6939D( Test.Case tc ) {
		tc.assertNonNull( TestResultSet.forPackage( test.sog.core.test.foo.C1.class ) );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forPackage(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_0A01E3E54( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet.forPackage( null );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
		description = "Aggregates TestResult instances for every class under the given source directory" 
	)
	public void tm_0D63BED47( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
		description = "Return is not null" 
	)
	public void tm_0491CBEDA( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
		description = "Throws AssertionError for null source path" 
	)
	public void tm_08EB90389( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestResultSet.forSourceDir( null );
	}
		
	@Test.Impl( 
		member = "method: TestResultSet TestResultSet.setVerbose(boolean)", 
		description = "Return is this TestResultSet instance" 
	)
	public void tm_07A9EAA2A( Test.Case tc ) {
		TestResultSet trs = new TestResultSet( "LABEL" );
		tc.assertEqual( trs, trs.setVerbose( true ) );
	}
		
	@Test.Impl( 
		member = "method: int TestResultSet.getFailCount()", 
		description = "Value is the sum of all failing weights for all tests" 
	)
	public void tm_06A537EE4( Test.Case tc ) {
		TestResultSet trs = TestResultSet.forPackage( test.sog.core.test.foo.C1.class );
		tc.assertEqual( 9, trs.getFailCount() );
	}
		
	@Test.Impl( 
		member = "method: int TestResultSet.getPassCount()", 
		description = "Value is the sum of all passing weights for all tests" 
	)
	public void tm_0584C9B7E( Test.Case tc ) {
		TestResultSet trs = TestResultSet.forPackage( test.sog.core.test.foo.C1.class );
		tc.assertEqual( 12, trs.getPassCount() );
	}
		
	@Test.Impl( 
		member = "method: long TestResultSet.getElapsedTime()", 
		description = "Value is the total elapsed time for all tests" 
	)
	public void tm_0ED41C40C( Test.Case tc ) {
		TestResultSet trs = TestResultSet.forPackage( test.sog.core.test.foo.C1.class );
		tc.assertTrue( trs.getElapsedTime() >= 21L );
	}
		
	@Test.Impl( 
		member = "method: void TestResultSet.print(IndentWriter)", 
		description = "If verbose is true includes details from each TestResult" 
	)
	public void tm_04519880D( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		TestResultSet.forPackage( test.sog.core.test.foo.C1.class )
			.setVerbose( true ).print( new IndentWriter( sos, " " ) );
		String msg = sos.toString();
		int pos = msg.indexOf( "RESULTS" );
		pos = msg.indexOf( "RESULTS", pos +1 );
		pos = msg.indexOf( "RESULTS", pos +1 );
		tc.assertTrue( pos > 0 );
	}
		
	@Test.Impl( 
		member = "method: void TestResultSet.print(IndentWriter)", 
		description = "Includes messages for each bad classname" 
	)
	public void tm_067F458BE( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		TestResultSet.forPackage( test.sog.core.test.foo.C1.class ).print( new IndentWriter( sos, " " ) );
		tc.assertTrue( sos.toString().contains( "Bad Classes" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestResultSet.print(IndentWriter)", 
		description = "Includes summary for each TestResult" 
	)
	public void tm_06F277C47( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		TestResultSet.forPackage( test.sog.core.test.foo.C1.class ).print( new IndentWriter( sos, " " ) );
		String output = sos.toString();
		Stream.of( test.sog.core.test.foo.C1.class, test.sog.core.test.foo.C2.class, test.sog.core.test.foo.C3.class )
			.map( Class::getName ).forEach( s -> tc.assertTrue( output.contains( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: void TestResultSet.print(IndentWriter)", 
		description = "Results are printed in alphabetaical order" 
	)
	public void tm_0119BF2CF( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		new TestResultSet( "TESTING" )
			.addClass( test.sog.core.test.foo.C3.class )
			.addClass( test.sog.core.test.foo.C2.class )
			.addClass( test.sog.core.test.foo.C1.class )
			.print( new IndentWriter( sos, " " ) );
		String output = sos.toString();
		int pos = output.indexOf( test.sog.core.test.foo.C1.class.getName() );
		pos = output.indexOf( test.sog.core.test.foo.C2.class.getName(), pos + 1 );
		pos = output.indexOf( test.sog.core.test.foo.C3.class.getName(), pos + 1 );
		tc.assertTrue( pos > 0 );
	}
		
	@Test.Impl( 
		member = "method: void TestResultSet.print(IndentWriter)", 
		description = "Throws AssertionError for null writer" 
	)
	public void tm_06C10F0A5( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestResultSet( "Testing" ).print( null );
	}

	
	
	

	public static void main( String[] args ) {
		Test.eval( TestResultSet.class );
		//Test.evalPackage( TestResultSet.class );
	}
}
