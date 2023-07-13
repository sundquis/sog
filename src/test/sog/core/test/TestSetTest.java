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

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;
import sog.core.test.Result;
import sog.core.test.TestSubject;
import sog.core.test.TestSet;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;
import test.sog.core.test.bar.ConcurrentTests;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestSetTest extends Test.Container {
	
	public TestSetTest() {
		super( TestSet.class );
	}
	
	
	
	public Set<Result> getResults( TestSet trs ) {
		return this.getSubjectField( trs, "testSubjects", null );
	}
	
	
	public List<String> getSkippedClases( TestSet trs ) {
		return this.getSubjectField( trs, "skippedClasses", null );
	}
	
	
	public void print( Class<?> subject ) {
		TestSubject.forSubject( subject ).print( new IndentWriter( System.out, "\t" ) );
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
		member = "constructor: TestSet(String)", 
		description = "Throws AssertionError for empty label" 
	)
	public void tm_0E5BF9170( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestSet( "" );
	}
		
	@Test.Impl( 
		member = "constructor: TestSet(String)", 
		description = "Throws AssertionError for null label" 
	)
	public void tm_0A1C6DF82( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestSet( null );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(Class)", 
		description = "Adds one TestSubject if not skipped",
		weight = 3
	)
	public void tm_02334FB7C( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
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
		member = "method: TestSet TestSet.addClass(Class)", 
		description = "Return is this TestSet instance to allow chaining" 
	)
	public void tm_0337F4643( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		tc.assertEqual( trs, trs.addClass( App.class ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_0FAD19EEE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet trs = new TestSet( "LABEL" );
		Class<?> c = null;
		trs.addClass( c );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(String)", 
		description = "Adds one TestSubject" 
	)
	public void tm_0A82CCD55( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		int before = this.getResults( trs ).size();
		trs.addClass( "sog.core.test.TestSubject" );
		int after = this.getResults( trs ).size();
		tc.assertEqual( before + 1, after );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(String)", 
		description = "Records error message if class is not found",
		weight = 2
	)
	public void tm_0B778BFB9( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
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
		member = "method: TestSet TestSet.addClass(String)", 
		description = "Return is this TestSet instance to allow chaining" 
	)
	public void tm_077F1B3DC( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		tc.assertEqual( trs, trs.addClass( "sog.core.Test" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(String)", 
		description = "Throws AssertionError for empty class name" 
	)
	public void tm_0D15DAF7C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet trs = new TestSet( "LABEL" );
		trs.addClass( "" );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClass(String)", 
		description = "Throws AssertionError for null class name" 
	)
	public void tm_0D0425EEC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet trs = new TestSet( "LABEL" );
		Class<?> c = null;
		trs.addClass( c );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClasses(Stream)", 
		description = "Adds one TestSubject for each valid class name" 
	)
	public void tm_022AD7F13( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		Stream.of( App.class, Test.class, Object.class ).map( Class::getName ).forEach( trs::addClass );
		tc.assertEqual( 3, this.getResults( trs ).size() );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClasses(Stream)", 
		description = "Return is this TestSet instance to allow chaining" 
	)
	public void tm_02A4BB5FB( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		Stream<String> stream = Stream.of( App.class, Test.class, Object.class ).map( Class::getName );
		tc.assertEqual( trs, trs.addClasses( stream ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.addClasses(Stream)", 
		description = "Throws AssertionError for null class names stream" 
	)
	public void tm_0830D4A18( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet trs = new TestSet( "LABEL" );
		Stream<String> stream = null;
		trs.addClasses( stream );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forAllSourceDirs()", 
		description = "Aggregates TestSubject instances for every class under every source directory" 
	)
	public void tm_004D9EEC5( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forAllSourceDirs()", 
		description = "Return is not null" 
	)
	public void tm_0C565278F( Test.Case tc ) {
		// Causes infinite loop:
		// tc.assertNonNull( TestSet.forAllSourceDirs() );
		tc.addMessage( "Manually verified." );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackage(Class)", 
		description = "Aggregates TestSubject instances for every class in the same package as the given class" 
	)
	public void tm_0A1A84C84( Test.Case tc ) {
		TestSet trs = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		tc.assertEqual( 3, this.getResults( trs ).size() );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackage(Class)", 
		description = "Return is not null" 
	)
	public void tm_037C6939D( Test.Case tc ) {
		tc.assertNonNull( TestSet.forPackage( test.sog.core.test.foo.C1.class ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackage(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_0A01E3E54( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet.forPackage( null );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forSourceDir(Path)", 
		description = "Aggregates TestSubject instances for every class under the given source directory" 
	)
	public void tm_0D63BED47( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forSourceDir(Path)", 
		description = "Return is not null" 
	)
	public void tm_0491CBEDA( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forSourceDir(Path)", 
		description = "Throws AssertionError for null source path" 
	)
	public void tm_08EB90389( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet.forSourceDir( null );
	}
			
	@Test.Impl( 
		member = "method: int TestSet.getFailCount()", 
		description = "Value is the sum of all failing weights for all tests" 
	)
	public void tm_06A537EE4( Test.Case tc ) {
		TestSet trs = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		trs.print( IndentWriter.stringIndentWriter() );
		tc.assertEqual( 9, trs.getFailCount() );
	}
		
	@Test.Impl( 
		member = "method: int TestSet.getPassCount()", 
		description = "Value is the sum of all passing weights for all tests" 
	)
	public void tm_0584C9B7E( Test.Case tc ) {
		TestSet trs = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		trs.print( IndentWriter.stringIndentWriter() );
		tc.assertEqual( 12, trs.getPassCount() );
	}
		
	@Test.Impl( 
		member = "method: long TestSet.getElapsedTime()", 
		description = "Value is the total elapsed time for all tests" 
	)
	public void tm_0ED41C40C( Test.Case tc ) {
		TestSet trs = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		trs.print( IndentWriter.stringIndentWriter() );
		tc.assertTrue( trs.getElapsedTime() >= 21L );
	}
		
	@Test.Impl( 
		member = "method: void TestSet.print(IndentWriter)", 
		description = "Includes messages for each bad classname" 
	)
	public void tm_067F458BE( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		TestSet.forPackage( test.sog.core.test.foo.C1.class ).addClass( "foo.bar" ).print( new IndentWriter( sos, " " ) );
		tc.assertTrue( sos.toString().contains( "SKIPPED" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSet.print(IndentWriter)", 
		description = "Includes summary for each TestSubject" 
	)
	public void tm_06F277C47( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		TestSet.forPackage( test.sog.core.test.foo.C1.class ).print( new IndentWriter( sos, " " ) );
		String output = sos.toString();
		Stream.of( test.sog.core.test.foo.C1.class, test.sog.core.test.foo.C2.class, test.sog.core.test.foo.C3.class )
			.map( Class::getName ).forEach( s -> tc.assertTrue( output.contains( s ) ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSet.print(IndentWriter)", 
		description = "Results are printed in alphabetaical order" 
	)
	public void tm_0119BF2CF( Test.Case tc ) {
		StringOutputStream sos = new StringOutputStream();
		new TestSet( "TESTING" )
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
		member = "method: void TestSet.print(IndentWriter)", 
		description = "Throws AssertionError for null writer" 
	)
	public void tm_06C10F0A5( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new TestSet( "Testing" ).print( null );
	}

	@Test.Impl( 
		member = "method: TestSet TestSet.forPackages(Path, Path)", 
		description = "Aggregates TestSubject instances for every class under the given directory" 
	)
	public void tm_0FBB7A244( Test.Case tc ) {
		Path sourceDir = App.get().sourceDir( test.sog.core.test.PolicyTest.class );
		Path sub = Path.of( "test", "sog", "core", "foo" );
		TestSet ts = TestSet.forPackages( sourceDir, sub );
		String skips = this.getSkippedClases( ts ).stream().collect( Collectors.joining( "," ) );
		tc.assertTrue( skips.contains( "A" ) );
		tc.assertTrue( skips.contains( "B" ) );
		tc.assertTrue( skips.contains( "C" ) );
		tc.assertTrue( skips.contains( "Sub1" ) );
		tc.assertTrue( skips.contains( "Sub2" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackages(Path, Path)", 
		description = "Return is not null" 
	)
	public void tm_0984A45D4( Test.Case tc ) {
		Path sourceDir = App.get().sourceDir( test.sog.core.test.PolicyTest.class );
		Path sub = Path.of( "test", "sog", "core", "foo" );
		tc.assertNonNull( TestSet.forPackages( sourceDir, sub ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackages(Path, Path)", 
		description = "Throws AssertionError for null source directory" 
	)
	public void tm_0046B215F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet.forPackages( null, Path.of( "sog" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSet TestSet.forPackages(Path, Path)", 
		description = "Throws AssertionError for null sub-directory" 
	)
	public void tm_00B5612DF( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSet.forPackages( App.get().sourceDir( App.class ), null );
	}
	
	@Test.Skip( "Testing" )
	private class SkippedClass {}
	
    @Test.Impl( 
    	member = "method: TestSet TestSet.addClass(Class)", 
    	description = "Skipped classes not added" 
    )
    public void tm_0267DADA8( Test.Case tc ) {
		TestSet trs = new TestSet( "LABEL" );
		int before = this.getResults( trs ).size();
		trs.addClass( SkippedClass.class );
		int after = this.getResults( trs ).size();
		tc.assertEqual( before, after );
    }
    
    @Test.Impl( 
    	member = "method: void TestSet.print(IndentWriter)", 
    	description = "Includes messages for each skipped class" 
    )
    public void tm_0C6120AE1( Test.Case tc ) {
		TestSet set = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		set.addClass( SkippedClass.class ).addClass( Object.class ).addClass( "bogus" );
		tc.assertEqual( 2, this.getSkippedClases( set ).size() );
    }
    
    @Test.Impl( 
        	member = "method: TestSet TestSet.run()", 
        	description = "Ignored after first call" 
        )
        public void tm_0AD0FCFED( Test.Case tc ) {
		TestSet set = TestSet.forPackage( test.sog.core.test.foo.C1.class );
		this.evalSubjectMethod( set, "run", null );
		int before = set.getPassCount();
		this.evalSubjectMethod( set, "run", null );
		tc.assertEqual( before, set.getPassCount() );
    }

    @Test.Impl( 
    	member = "method: void TestSet.print(IndentWriter)", 
    	description = "Includes details when Result.showDetails is true" 
    )
    public void tm_0C480A0ED( Test.Case tc ) {
    	TestSet set = TestSet.forPackage( test.sog.core.test.foo.C1.class ).showDetails( true );
    	StringOutputStream sos = new StringOutputStream();
    	set.print( new IndentWriter( sos ) );
    	tc.assertTrue( sos.toString().contains( "SKIPPED" ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.concurrent(boolean)", 
    	description = "Returns this TestSubject instance to allow chaining"
    )
    public void tm_02807334C( Test.Case tc ) {
    	TestSet set = new TestSet( "testing" );
    	tc.assertEqual( set, set.concurrent( true ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.concurrent(boolean)", 
    	description = "When concurrent is true TestSubject instances use worker threads to run tests",
    	threadsafe = false 
    )
    public void tm_064B69EAF( Test.Case tc ) {
    	TestSet set = TestSet.forPackage( test.sog.core.test.bar.ConcurrentTests.class ).concurrent( true );
    	this.evalSubjectMethod( set, "run", null );
    	// We can't actually guarantee that more than one thread gets scheduled, but there are 20
    	// tests so it is likely...
    	// In concurrent mode our thread must be different from the thread(s) used by these cases,,
    	// but if we are also running concurrently our thread and these threads will
    	// all be Worker threads. NOTE: In safeMode, this will result in error messages warning
    	// about potential deadlock printed to std.err.
    	Set<Thread> threads = ConcurrentTests.TEST.getThreads();
    	tc.assertTrue( threads.size() > 1 );
    	threads.stream().forEach( (t) -> tc.assertFalse( Thread.currentThread().equals( t ) ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.showDetails(boolean)", 
    	description = "Returns this TestSubject instance to allow chaining" 
    )
    public void tm_0ED6A5DDE( Test.Case tc ) {
    	TestSet set = new TestSet( "testing" );
    	tc.assertEqual( set, set.showDetails( true ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.showDetails(boolean)", 
    	description = "When showDetails is true contained TestSubjects include their details" 
    )
    public void tm_07ADA1208( Test.Case tc ) {
    	TestSet set = TestSet.forPackage( test.sog.core.test.foo.C1.class ).showDetails( true );
    	StringOutputStream sos = new StringOutputStream();
    	set.print( new IndentWriter( sos ) );
    	tc.assertTrue( sos.toString().contains( "RESULTS" ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.showProgress(boolean)", 
    	description = "Returns this TestSubject instance to allow chaining" 
    )
    public void tm_03B37C319( Test.Case tc ) {
    	TestSet set = new TestSet( "test" );
    	tc.assertEqual( set, set.showProgress( true ) );
    }
    
    @Test.Impl( 
    	member = "method: TestSet TestSet.showProgress(boolean)", 
    	description = "When showProgress is true contained TestCase instances show progress as tests are run" 
    )
    public void tm_0D4397305( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertTrue( true );
    }
    
    @Test.Impl( 
    	member = "method: void TestSet.print()", 
    	description = "Indicates if tests were run concurrently" 
    )
    public void tm_0ADAFAF1F( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertTrue( true );
    }
    
    @Test.Impl( 
    	member = "method: void TestSet.print()", 
    	description = "Prints the total elapsed time" 
    )
    public void tm_08D817329( Test.Case tc ) {
    	tc.addMessage( "Manually verified" );
    	tc.assertTrue( true );
    }


	
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( TestSet.class )
			.concurrent( true )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( TestSet.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
	}
}
