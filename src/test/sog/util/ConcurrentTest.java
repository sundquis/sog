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

package test.sog.util;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.AppRuntime;
import sog.core.Test;
import sog.util.Concurrent;
import sog.util.FunctionWithException;
import sog.util.SupplierWithException;

/**
 * 
 */
@Test.Skip( "Container" )
public class ConcurrentTest extends Test.Container {
	
	public ConcurrentTest() {
		super( Concurrent.class );
	}
	
	private Concurrent getConcurrent() {
		return new Concurrent( "Test", 1 );
	}

	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: Concurrent(String, int)", 
    	description = "Terminates workers on shutdown" 
    )
    public void tm_073C29D1C( Test.Case tc ) {
    	tc.addMessage( "FIXME: How to test" );
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "constructor: Concurrent(String, int)", 
    	description = "Throws AssertionError for non-positive thread count" 
    )
    public void tm_0C9CF749A( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Concurrent( "Test", 0 );
    }
    
    @Test.Impl( 
    	member = "constructor: Concurrent(String, int)", 
    	description = "Throws AssertionError for null or empty label" 
    )
    public void tm_04BDFD8B7( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Concurrent( "", 1 );
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrap(Function)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0864BE58C( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, Thread> f = (s) -> Thread.currentThread();
    	tc.assertNotEqual( f.apply( "" ), c.wrap( f ).apply( "" ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrap(Function)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_0409C5C5D( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, String> f = (s) -> ">>" + s + "<<";
    	String arg = "hi";
    	tc.assertEqual( f.apply( arg ), c.wrap( f ).apply( arg ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrap(Function)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_079F0DCC4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	Function<String, String> f = null;
    	c.wrap( f );
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(Function)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_09879C6D6( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, Thread> f = (s) -> Thread.currentThread();
    	tc.assertNotEqual( f.apply( "" ), c.wrapGetLater( f ).apply( "" ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(Function)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_017E06353( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, String> f = (s) -> ">>" + s + "<<";
    	String arg = "hi";
    	tc.assertEqual( f.apply( arg ), c.wrapGetLater( f ).apply( arg ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(Function)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0513CAA3A( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	Function<String, String> f = null;
    	c.wrapGetLater( f );
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0DCEE8161( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, Thread, AppRuntime> f = (s) -> Thread.currentThread();
    	tc.assertNotEqual( f.apply( "" ), c.wrapGetLater( f ).apply( "" ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "If the function application throws a checked exception it is thrown by the Supplier" 
    )
    public void tm_04C3CBFBE( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> f = (s) -> {
    		if ( s == null ) { throw new IOException("null"); }
    		return s;
    	};
    	SupplierWithException<String, IOException> s = c.wrapGetLater( f ).apply( null );
    	tc.expectError( IOException.class );
    	s.get();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "If the function application throws an unchecked excpetion it is thrown by the Supplier" 
    )
    public void tm_025F5B8E6( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, AppRuntime> f = (s) -> {
    		if ( s == null ) { throw new AppRuntime("null"); }
    		return s;
    	};
    	SupplierWithException<String, AppRuntime> s = c.wrapGetLater( f ).apply( null );
    	tc.expectError( AppRuntime.class );
    	s.get();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_077D662A8( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, String, AppRuntime> f = (s) -> {
    		if ( s == null ) { throw new AppRuntime("null"); }
    		return ">>" + s + "<<";
    	};
    	String arg = "hi";
    	tc.assertEqual( f.apply( arg ), c.wrapGetLater( f ).apply( arg ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0CF50F24F( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, Exception> f = null;
    	tc.expectError( AssertionError.class );
    	c.wrapGetLater( f );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0850DE000( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, Thread, IOException> f = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return Thread.currentThread();
    	};
    	tc.assertNotEqual( f.apply( "" ), c.wrap( f ).apply( "" ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "If the function application throws a checked exception it is thrown" 
    )
    public void tm_0383E567C( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, Thread, IOException> f = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return Thread.currentThread();
    	};
    	tc.expectError( IOException.class );
    	f.apply( null );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "If the function application throws an unchecked excpetion it is thrown" 
    )
    public void tm_039439481( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, Thread, AppRuntime> f = (s) -> {
    		if ( s == null ) { throw new AppRuntime(); }
    		return Thread.currentThread();
    	};
    	tc.expectError( AppRuntime.class );
    	f.apply( null );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_0F2DFFE69( Test.Case tc ) {
		Concurrent c = this.getConcurrent();
		FunctionWithException<String, String, AppRuntime> f = (s) -> {
			if ( s == null ) { throw new AppRuntime("null"); }
			return ">>" + s + "<<";
		};
		String arg = "hi";
		tc.assertEqual( f.apply( arg ), c.wrap( f ).apply( arg ) );
		c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_00E8BC3D0( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> f = null;
    	tc.expectError( AssertionError.class );
    	c.wrap( f );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_089D5B2E4( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, Thread> f = (s) -> Thread.currentThread();
    	String arg = "bar";
    	tc.assertNotEqual( f.apply( arg ), c.apply( f, arg ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_04264FE05( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, String> f = (s) -> ">>" + s + "<<";
    	String arg = "foo";
    	tc.assertEqual( f.apply( arg ), c.apply( f, arg ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_071CC046C( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	Function<String, String> f = null;
    	tc.expectError( AssertionError.class );
    	c.apply( f, "" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0DB886559( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, Thread, IOException> f = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return Thread.currentThread();
    	};
    	String arg = "hi";
    	tc.assertNotEqual( f.apply( arg ), c.apply( f, arg ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "If the function application throws a checked exception it is thrown" 
    )
    public void tm_0F7044183( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> f = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return s;
    	};
    	tc.expectError( IOException.class );
    	c.apply( f, null );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "If the function application throws an unchecked excpetion it is thrown" 
    )
    public void tm_0B334F81A( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, AppRuntime> f = (s) -> {
    		if ( s == null ) { throw new AppRuntime(); }
    		return s;
    	};
    	tc.expectError( AppRuntime.class );
    	c.apply( f, null );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_03280B1B0( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, String, AppRuntime> f = (s) -> {
    		if ( s == null ) { throw new AppRuntime(); }
    		return ">>" + s + "<<";
    	};
    	String arg = "in";
    	tc.assertEqual( f.apply( arg ), c.apply( f, arg ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0746EAF57( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> f = null;
    	tc.expectError( AssertionError.class );
    	c.apply( f, "hi" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Call blocks until all results are ready" 
    )
    public void tm_019717F99( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, String> f = (s) -> {
    		try {
        		Thread.sleep( 2L );
			} catch ( InterruptedException e ) {
			}
    		return s;
    	};
    	long start = System.currentTimeMillis();
    	c.map( f, Stream.of( "1", "2", "3" ) ).count();
    	tc.assertTrue( System.currentTimeMillis() - start >= 1L );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Results are consistent with the given function and arguments" 
    )
    public void tm_038FB3B8A( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<Integer, Integer> f = (n) -> n*n;
    	tc.assertEqual( List.of(1, 4, 9), c.map( f, Stream.of( 1, 2, 3 ) ).collect( Collectors.toList() ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Results are presented in the order of the original steram of inputs" 
    )
    public void tm_056FD3568( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<Integer, Integer> f = (n) -> n - 1;
    	tc.assertEqual( List.of(0, 1, 2), c.map( f, Stream.of( 1, 2, 3 ) ).collect( Collectors.toList() ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Return is not terminated" 
    )
    public void tm_092D50707( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<Integer, Integer> f = (n) -> n * (n + 1);
    	tc.assertEqual( 3L, c.map( f, Stream.of( 4, 5, 6 ) ).count() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_004C82A98( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<Integer, Integer> f = null;
    	tc.expectError( AssertionError.class );
    	c.map( f, Stream.of( 1, 2, 3 ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Throws AssertionError for null stream" 
    )
    public void tm_002550980( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<Integer, Integer> f = (n) -> n+1;
    	Stream<Integer> stream =null;
    	tc.expectError( AssertionError.class );
    	c.map( f, stream );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Includes label" 
    )
    public void tm_0D1E0B947( Test.Case tc ) {
    	String label = "This is the label";
    	Concurrent c = new Concurrent( label, 3 );
    	c.terminate();
    	tc.assertTrue( c.toString().contains( label ) );
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Reports number of Worker threads" 
    )
    public void tm_06202128C( Test.Case tc ) {
    	Concurrent c = new Concurrent( "xxx", 1 );
    	c.terminate();
    	tc.assertTrue( c.toString().contains( "1 worker" ) );
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Reports when procedures are pending" 
    )
    public void tm_017CAA9B4( Test.Case tc ) {
    	Concurrent c = new Concurrent( "pending", 1 );
    	Function<String, String> function = (s) -> {
    		try {
    			Thread.sleep( 1L );
    		} catch ( InterruptedException ex ) {}
    		return s;
    	};
    	Function<String, Supplier<String>> delayed = c.wrapGetLater( function );
    	delayed.apply( "A" );
    	delayed.apply( "A" );
    	delayed.apply( "A" );
    	tc.assertTrue( c.toString().contains( "has procedures" ) );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_01293843B( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, Thread> function = (s) -> Thread.currentThread();
    	tc.assertNotEqual( function.apply( "" ), c.applyGetLater( function, "" ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_068C2520E( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, String> function = (s) -> ">>" + s + "<<";
    	String arg = "jks";
    	tc.assertEqual( function.apply( arg ), c.applyGetLater( function, arg ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_06DF32C35( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	Function<String, String> function = null;
    	tc.expectError( AssertionError.class );
    	c.applyGetLater( function, "hi" );
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_020640ED5( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	Function<String, Thread> function = (s) -> Thread.currentThread();
    	tc.assertNotEqual( function.apply( "" ), c.applyGetLater( function, "" ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "If the function application throws a checked exception it is thrown by the Supplier" 
    )
    public void tm_0C730834B( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> function = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return s;
    	};
    	tc.expectError( IOException.class );
    	c.applyGetLater( function, null ).get();
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "If the function application throws an unchecked excpetion it is thrown by the Supplier" 
    )
    public void tm_02715DB5A( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, AppRuntime> function = (s) -> {
    		if ( s == null ) { throw new AppRuntime(); }
    		return s;
    	};
    	tc.expectError( AppRuntime.class );
    	c.applyGetLater( function, null ).get();
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_0BB0D51B4( Test.Case tc ) throws IOException {
    	Concurrent c = this.getConcurrent();
    	FunctionWithException<String, String, IOException> function = (s) -> {
    		if ( s == null ) { throw new IOException(); }
    		return "++" + s + "++";
    	};
    	String arg = "brs";
    	tc.assertEqual( function.apply( arg ), c.applyGetLater( function, arg ).get() );
    	c.terminate();
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0C0F9E65B( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	tc.afterThis( () -> c.terminate() );
    	FunctionWithException<String, String, IOException> function = null;
    	tc.expectError( AssertionError.class );
    	c.applyGetLater( function, "a" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.Worker.run()", 
    	description = "Worker evaluates procedures" 
    )
    public void tm_0B6C754FE( Test.Case tc ) {
    	Concurrent c = new Concurrent( "Worker", 1 );
    	List<?> workers = this.getSubjectField( c, "workers", null );
    	String worker = workers.get( 0 ).toString();
    	Function<String, String> function = (s) -> Thread.currentThread().toString();
    	tc.assertEqual( worker, c.apply( function, "" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "No effect if Concurrent already terminated" 
    )
    public void tm_0D9180615( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	c.terminate();
    	String before = c.toString();
    	c.terminate();
    	tc.assertEqual( before, c.toString() );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "Throws AppRuntime for requests after terminated" 
    )
    public void tm_09E50264C( Test.Case tc ) {
    	Concurrent c = this.getConcurrent();
    	c.terminate();
    	Function<String, String> function = Function.identity();
    	tc.expectError( AppRuntime.class );
    	c.apply( function, "hi" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "Worker threads are stopped after terminate()" 
    )
    public void tm_0DECDA05A( Test.Case tc ) {
    	Concurrent c = new Concurrent( "stopped", 1 );
    	c.terminate();
    	List<?> workers = this.getSubjectField( c, "workers", null );
    	Thread worker = (Thread) workers.get( 0 );
    	tc.assertFalse( worker.isAlive() );
    }
    

	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Concurrent.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Concurrent.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}

	
}
