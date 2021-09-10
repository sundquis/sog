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

package test.sog.core;

import java.io.PrintWriter;
import java.lang.reflect.Field;

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

	
	private StringOutputStream sos;
	private PrintWriter out;
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.sos = new StringOutputStream();
			this.out = new PrintWriter( this.sos, true );
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.sos = null;
			this.out = null;
		};
	}

	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: boolean Trace.isEnabled()", 
		description = "After enable(false) returns false" 
	)
	public void tm_0A611564F( Test.Case tc ) {
		Trace.enable( false );
		tc.assertFalse( Trace.isEnabled() );
	}
		
	@Test.Impl( 
		member = "method: boolean Trace.isEnabled()", 
		description = "After enable(true) returns true" 
	)
	public void tm_073CA6073( Test.Case tc ) {
		Trace.enable( true );
		tc.assertTrue( Trace.isEnabled() );
	}
		
	@Test.Impl( 
		member = "method: void Trace.enable(boolean)", 
		description = "After enable(false) messages are ignored" 
	)
	public void tm_0E392F861( Test.Case tc ) {
		Trace.enable( true );
		Trace.write( "Test", "BEFORE", this.out );
		Trace.enable( false );
		Trace.write( "Test", "AFTER", this.out );
		tc.assertFalse( this.sos.toString().contains( "AFTER" ) );
	}
		
	@Test.Impl( 
		member = "method: void Trace.enable(boolean)", 
		description = "After enable(true) message are logged in the trace file" 
	)
	public void tm_0EF1563EE( Test.Case tc ) {
		Trace.enable( true );
		Trace.write( "Test", "BEFORE", this.out );
		Trace.enable( false );
		Trace.write( "Test", "AFTER", this.out );
		tc.assertTrue( this.sos.toString().contains( "BEFORE" ) );
	}
		
	@Test.Impl( 
		member = "method: void Trace.enable(boolean)", 
		description = "Does not affect pending messages" 
	)
	public void tm_0E6B239C2( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
		
		@Test.Impl( 
			member = "method: void Trace.enable(boolean)", 
			description = "Is idempotent" 
		)
		public void tm_0B9EB102F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.run()", 
			description = "Inserts header for new files" 
		)
		public void tm_0F5173CE3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.run()", 
			description = "Throws AppException when called from an external thread" 
		)
		public void tm_0DBED2B2C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.terminate()", 
			description = "After termination, messages are ignored" 
		)
		public void tm_01AFC3B42( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.terminate()", 
			description = "Pending messages are written to the trace file" 
		)
		public void tm_09E508AC5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Message is written to file before application exits" 
		)
		public void tm_033B9403F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Throws AssertionError for empty message" 
		)
		public void tm_0EFE80A0C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Throws AssertionError for null source" 
		)
		public void tm_07023E5EC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Trace message includes details on the calling class" 
		)
		public void tm_01D86EC21( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Trace message includes details on the calling method" 
		)
		public void tm_04FA6EAAE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Trace message includes details on the calling thread" 
		)
		public void tm_0C1E78197( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Trace message includes details on the source object" 
		)
		public void tm_04995C5E9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String)", 
			description = "Trace message includes the given message" 
		)
		public void tm_014A86C54( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Message is echoed to the given PrintWriter immediately" 
		)
		public void tm_066FF07D2( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Message is written to file before application exits" 
		)
		public void tm_0A3B3B4D3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Multi-thread stress test" 
		)
		public void tm_0F59A5A68( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Throws AssertionError for empty message" 
		)
		public void tm_0891DBCA0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Throws AssertionError for null PrintWriter" 
		)
		public void tm_0329A0071( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Throws AssertionError for null source" 
		)
		public void tm_0FE8D2D80( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Trace message includes details on the calling class" 
		)
		public void tm_08D8160B5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Trace message includes details on the calling method" 
		)
		public void tm_0DEFB089A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Trace message includes details on the calling thread" 
		)
		public void tm_0513B9F83( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Trace message includes details on the source object" 
		)
		public void tm_0B9903A7D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(Object, String, PrintWriter)", 
			description = "Trace message includes the given message" 
		)
		public void tm_0A2290C40( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Trace.class );
		//Test.evalPackage( Trace.class );
	}
}
