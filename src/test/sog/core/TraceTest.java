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

package test.sog.core;

import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sog.core.AppRuntime;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.Trace;
import sog.util.StringOutputStream;

/**
 * 
 */
@Test.Skip( "Container" )
public class TraceTest extends Test.Container {
	
	public TraceTest() {
		super( Trace.class );
	}

	// These tests use shared instance values and are not concurrent in thier present version.
	// FIXME: Could rewrite to be concurrent and test thread-safety of the Trace interface
	
	private StringOutputStream sos;
	private PrintWriter out;
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.sos = new StringOutputStream();
			this.out = new PrintWriter( this.sos, true );
			Trace.enable( true );
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.sos = null;
			this.out = null;
		};
	}
	
	@Override
	public String toString() {
		return "TRACE TEST";
	}
	
	private static class Agent extends Thread {
		
		private final int ID;
		
		private Agent( int ID ) {
			this.ID = ID;
		}
		
		private Agent begin() {
			this.start();
			return this;
		}
		
		public void join0() {
			try {
				this.join();
			} catch ( InterruptedException e ) {
			}
		}
		
		@Override
		public String toString() {
			return "Agent(" + this.ID + ")";
		}
		
		@Override
		public void run() {
			IntStream.range( 0, 50 ).forEach( n -> { Trace.write( "Multi-tread test", "Message #" + n ); Thread.yield(); } );
		}
	}
	

	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: boolean Trace.isEnabled()", 
		description = "After enable(false) returns false",
		threadsafe = false
	)
	public void tm_0A611564F( Test.Case tc ) {
		Trace.enable( false );
		tc.assertFalse( Trace.isEnabled() );
	}
		
	@Test.Impl( 
		member = "method: boolean Trace.isEnabled()", 
		description = "After enable(true) returns true",
		threadsafe = false
	)
	public void tm_073CA6073( Test.Case tc ) {
		Trace.enable( true );
		tc.assertTrue( Trace.isEnabled() );
	}
		
	@Test.Impl( 
		member = "method: void Trace.enable(boolean)", 
		description = "After enable(false) messages are ignored",
		threadsafe = false
	)
	public void tm_0E392F861( Test.Case tc ) {
		Trace.enable( false );
		Trace.write( "Test", "DISABLED", this.out );
		tc.assertFalse( this.sos.toString().contains( "DISABLED" ) );
	}
		
	@Test.Impl( 
		member = "method: void Trace.enable(boolean)", 
		description = "After enable(true) message are enqueued for logging",
		threadsafe = false
	)
	public void tm_0EF1563EE( Test.Case tc ) {
		Trace.enable( true );
		Trace.write( "Test", "ENABLED", this.out );
		// Cannot access queue/buffer directly so we check the PrintWriter
		// This relies on the implementation of Trace.write(...)
		tc.assertTrue( this.sos.toString().contains( "ENABLED" ) );
	}
		
	@Test.Impl( 
		member = "method: void Trace.run()", 
		description = "Throws AppRuntime when called from an external thread",
		threadsafe = false
	)
	public void tm_0DBED2B2C( Test.Case tc ) {
		Trace INSTANCE = this.getSubjectField( null, "INSTANCE", null );
		tc.expectError( AppRuntime.class );
		INSTANCE.run();
	}
		
	@Test.Impl( 
		member = "method: void Trace.terminate()", 
		description = "After termination, messages are ignored",
		threadsafe = false
	)
	public void tm_01AFC3B42( Test.Case tc ) {
		tc.addMessage( "Manually verified" ).assertPass();
	}
		
	@Test.Impl( 
		member = "method: void Trace.terminate()", 
		description = "Pending messages are written to the trace file",
		threadsafe = false
	)
	public void tm_09E508AC5( Test.Case tc ) {
		tc.addMessage( "Manually verified" ).assertPass();
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Message is written to file before application exits",
		threadsafe = false
	)
	public void tm_033B9403F( Test.Case tc ) {
		tc.addMessage( "Manually verified" ).assertPass();
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Throws AssertionError for empty message",
		threadsafe = false
	)
	public void tm_0EFE80A0C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Trace.write( this, "" );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Throws AssertionError for null source",
		threadsafe = false
	)
	public void tm_07023E5EC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Trace.write( null, "hi" );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Trace message includes details on the calling class",
		threadsafe = false
	)
	public void tm_01D86EC21( Test.Case tc ) {
		String s = this.getClass().getName();
		tc.assertTrue( Trace.write( this, "hi" ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Trace message includes details on the calling method",
		threadsafe = false
	)
	public void tm_04FA6EAAE( Test.Case tc ) {
		String s = "tm_04FA6EAAE";
		tc.assertTrue( Trace.write( this, "hi" ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Trace message includes details on the calling thread",
		threadsafe = false
	)
	public void tm_0C1E78197( Test.Case tc ) {
		String s = Thread.currentThread().toString();
		tc.assertTrue( Trace.write( this, "hi" ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Trace message includes details on the source object",
		threadsafe = false
	)
	public void tm_04995C5E9( Test.Case tc ) {
		String s = this.toString();
		tc.assertTrue( Trace.write( this, "hi" ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Trace message includes the given message",
		threadsafe = false
	)
	public void tm_014A86C54( Test.Case tc ) {
		String s = "Some random message... 42";
		tc.assertTrue( Trace.write( this, s ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Return is non-empty when disabled",
		threadsafe = false
	)
	public void tm_01374793C( Test.Case tc ) {
		Trace.enable( false );
		tc.assertNotEmpty( Trace.write( this, "hi" ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String)", 
		description = "Return is non-empty when enabled",
		threadsafe = false
	)
	public void tm_049721D97( Test.Case tc ) {
		Trace.enable( true );
		tc.assertNotEmpty( Trace.write( this, "hi" ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Message is echoed to the given PrintWriter immediately",
		threadsafe = false
	)
	public void tm_066FF07D2( Test.Case tc ) {
		Trace.write( this, "IMMEDIATE", this.out );
		tc.assertTrue( this.sos.toString().contains( "IMMEDIATE") );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Message is written to file before application exits",
		threadsafe = false
	)
	public void tm_0A3B3B4D3( Test.Case tc ) {
		tc.addMessage( "Manually verified." ).assertPass();
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Multi-thread stress test",
		threadsafe = false
	)
	public void tm_0F59A5A68( Test.Case tc ) throws InterruptedException {
		Set<Agent> agents = IntStream.range( 0, 40 ).boxed()
			.map( Agent::new ).map( Agent::begin ).collect( Collectors.toSet() );
		agents.forEach( Agent::join0 );
		// No deadlock
		tc.assertPass();
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Throws AssertionError for empty message",
		threadsafe = false
	)
	public void tm_0891DBCA0( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Trace.write( this, "", this.out );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Throws AssertionError for null PrintWriter",
		threadsafe = false
	)
	public void tm_0329A0071( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Trace.write( this, "hi", null );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Throws AssertionError for null source",
		threadsafe = false
	)
	public void tm_0FE8D2D80( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Trace.write( null, "hi", this.out );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Trace message includes details on the calling class",
		threadsafe = false
	)
	public void tm_08D8160B5( Test.Case tc ) {
		String s = this.getSubjectClass().getName();
		tc.assertTrue( Trace.write( this, "hi", this.out ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Trace message includes details on the calling method",
		threadsafe = false
	)
	public void tm_0DEFB089A( Test.Case tc ) {
		String s = "tm_0DEFB089A";
		tc.assertTrue( Trace.write( this, "hi", this.out ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Trace message includes details on the calling thread",
		threadsafe = false
	)
	public void tm_0513B9F83( Test.Case tc ) {
		String s = Thread.currentThread().toString();
		tc.assertTrue( Trace.write( this, "hi", this.out ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Trace message includes details on the source object",
		threadsafe = false
	)
	public void tm_0B9903A7D( Test.Case tc ) {
		String s = this.toString();
		tc.assertTrue( Trace.write( this, "hi", this.out ).contains( s ) );
	}
		
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Trace message includes the given message",
		threadsafe = false
	)
	public void tm_0A2290C40( Test.Case tc ) {
		String s = "Some bogus and random message";
		tc.assertTrue( Trace.write( this, s, this.out ).contains( s ) );
	}
	
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Return is non-empty when disabled",
		threadsafe = false
	)
	public void tm_04EE87C36( Test.Case tc ) {
		Trace.enable( false );
		tc.assertNonNull( Trace.write( this, "hi", this.out ) );
	}
			
	@Test.Impl( 
		member = "method: String Trace.write(Object, String, PrintWriter)", 
		description = "Return is non-empty when enabled",
		threadsafe = false
	)
	public void tm_02A54D35D( Test.Case tc ) {
		Trace.enable( true );
		tc.assertNonNull( Trace.write( this, "hi", this.out ) );
	}
	
	

	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( Trace.class )
			.concurrent( true )
			.showDetails( true )
			.showProgress( true )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( Trace.class )
			.concurrent( false )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
}
