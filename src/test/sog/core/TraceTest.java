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

import java.io.IOException;
import java.nio.file.Files;

import sog.core.AppException;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.Trace;

/**
 * 
 */
@Test.Skip( "Container" )
public class TraceTest extends Test.Container {
	
	public TraceTest() {
		super( Trace.class );
	}
	
	
	private Trace trace;
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.trace = new Trace( "Testing" );
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			try {
				Files.deleteIfExists( Trace.close() );
			} catch ( IOException e ) {
				throw new AppException( e );
			}
			this.trace = null;
		};
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: Trace(String)", 
		description = "Not echoEnabled" 
	)
	public void tm_09A8AADCC( Test.Case tc ) {
		tc.assertFalse( this.trace.echoEnabled() );
	}
		
	@Test.Impl( 
		member = "constructor: Trace(String)", 
		description = "Throws AssertionError for empty topic" 
	)
	public void tm_071DC5D1B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Trace( "" );
	}
		
	@Test.Impl( 
		member = "constructor: Trace(String, boolean)", 
		description = "If echoMessages then echoEnabled" 
	)
	public void tm_0C9DBBE20( Test.Case tc ) {
		tc.assertTrue( new Trace( "x", true ).echoEnabled() );
	}
		
	@Test.Impl( 
		member = "constructor: Trace(String, boolean)", 
		description = "If not echoMessages then not echoEnabled" 
	)
	public void tm_04150D5C6( Test.Case tc ) {
		tc.assertFalse( new Trace( "x", false ).echoEnabled() );
	}
		
	@Test.Impl( 
		member = "constructor: Trace(String, boolean)", 
		description = "Throws AssertionError for empty topic" 
	)
	public void tm_0396493F9( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Trace( "", true );
	}
		
		@Test.Impl( 
			member = "method: Path Trace.MsgHandler.getPath()", 
			description = "Return is not null" 
		)
		public void tm_00D670DCA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.MsgHandler.getPath()", 
			description = "Returns path to file containing messages on this trace instance" 
		)
		public void tm_0E35EE116( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Can write after close" 
		)
		public void tm_0AA62E0C7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "File does not exist if disabled messages written" 
		)
		public void tm_04FF7342F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "File does not exist if no messages written" 
		)
		public void tm_04808168A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Opens new handler with different file" 
		)
		public void tm_0A5866674( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Returned file contains messages if written and enabled" 
		)
		public void tm_0849BE184( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Returns non null when disabled" 
		)
		public void tm_0AEC4446B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Returns non null when enabled" 
		)
		public void tm_04E74B088( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Path Trace.close()", 
			description = "Returns readable file if written and enabled" 
		)
		public void tm_0D5BA7A4F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Trace.MsgHandler.toString()", 
			description = "Return is non-empty" 
		)
		public void tm_06C5A51F0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Trace.toString()", 
			description = "Identifies state when disabled" 
		)
		public void tm_03A39B903( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Trace.toString()", 
			description = "Indicates state when enabled" 
		)
		public void tm_080A480B6( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Trace.toString()", 
			description = "Indicates topic when enabled" 
		)
		public void tm_0AE0E6858( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Trace.echoEnabled()", 
			description = "If echoEnabled then write(message) copies to standard out" 
		)
		public void tm_0E91ADF03( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Trace.isEnabled()", 
			description = "Enabled when initialized" 
		)
		public void tm_0D7CD7141( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.MsgHandler.accept(String)", 
			description = "Issues global warning when message exceeds line count warn limit" 
		)
		public void tm_098DDCE38( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.MsgHandler.accept(String)", 
			description = "Throws AppException when message exceeds line count fail limit" 
		)
		public void tm_0D663A554( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.MsgHandler.accept(String)", 
			description = "Throws AssertionError for null message" 
		)
		public void tm_06A65E5CB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.MsgHandler.run()", 
			description = "Throws AppExcpetion when started from a non-worker thread" 
		)
		public void tm_03536554E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.disable()", 
			description = "Messages ignored" 
		)
		public void tm_03AE16491( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.disable()", 
			description = "Not enabled after" 
		)
		public void tm_029C75393( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.enable()", 
			description = "Is enabled after" 
		)
		public void tm_03563EC99( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.enable()", 
			description = "Same file used when re-enabled without close" 
		)
		public void tm_09E398F66( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.enableEcho(boolean)", 
			description = "If enableEcho(false) then not echoEnabled" 
		)
		public void tm_0D5BC94E4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.enableEcho(boolean)", 
			description = "If enableEcho(true) then echoEnabled" 
		)
		public void tm_049222A36( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Message printed if echo enabled" 
		)
		public void tm_035D21085( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Multi thread stress test" 
		)
		public void tm_0C88F7B5C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Throws AppException when count exceeds fail limit" 
		)
		public void tm_06D8BB53B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Throws AssertionError for empty message" 
		)
		public void tm_0EF81F1FF( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Throws AssertionError for null message" 
		)
		public void tm_030205B19( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Trace.write(String)", 
			description = "Warning issued when count exceeds warn limit" 
		)
		public void tm_0968768B5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Trace.class );
		//Test.evalPackage( Trace.class );
	}
}
