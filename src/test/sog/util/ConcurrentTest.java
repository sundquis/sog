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

import java.util.function.Function;

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
    	description = "If the function application throws an exception it is thrown by the Supplier" 
    )
    public void tm_04C3CBFBE( Test.Case tc ) {
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
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Function Concurrent.wrapGetLater(FunctionWithException)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0CF50F24F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0850DE000( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "If the function application throws an exception it is thrown" 
    )
    public void tm_0AC2CE619( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_0F2DFFE69( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: FunctionWithException Concurrent.wrap(FunctionWithException)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_00E8BC3D0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_089D5B2E4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_04264FE05( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(Function, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_071CC046C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_0DB886559( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "If the function application throws an exception it is thrown" 
    )
    public void tm_0DA6044F2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_03280B1B0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Concurrent.apply(FunctionWithException, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0746EAF57( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Call blocks until all results are ready" 
    )
    public void tm_019717F99( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Results are consistent with the given function and arguments" 
    )
    public void tm_038FB3B8A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Results are presented in the order of the original steram of inputs" 
    )
    public void tm_056FD3568( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Return is not terminated" 
    )
    public void tm_092D50707( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_004C82A98( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Concurrent.map(Function, Stream)", 
    	description = "Throws AssertionError for null stream" 
    )
    public void tm_002550980( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Includes constructed label" 
    )
    public void tm_0D1E0B947( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Reports number of Worker threads" 
    )
    public void tm_06202128C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Concurrent.toString()", 
    	description = "Reports when procedures are pending" 
    )
    public void tm_017CAA9B4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_01293843B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_068C2520E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Supplier Concurrent.applyGetLater(Function, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_06DF32C35( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Function is evaluated by a Worker thread" 
    )
    public void tm_020640ED5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "If the function application throws an exception it is thrown by the Supplier" 
    )
    public void tm_010BBA332( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Result is consistent with given function and argument" 
    )
    public void tm_0BB0D51B4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: SupplierWithException Concurrent.applyGetLater(FunctionWithException, Object)", 
    	description = "Throws AssertionError for null function" 
    )
    public void tm_0C0F9E65B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.Worker.run()", 
    	description = "Worker evaluates procedures" 
    )
    public void tm_0B6C754FE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "No effect if Concurrent already terminated" 
    )
    public void tm_0D9180615( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "Requests are not acceppted after terminate()" 
    )
    public void tm_09E50264C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Concurrent.terminate()", 
    	description = "Worker threads are stopped after terminate()" 
    )
    public void tm_0DECDA05A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
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
