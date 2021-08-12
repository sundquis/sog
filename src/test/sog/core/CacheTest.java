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

import sog.core.AppException;
import sog.core.Cache;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class CacheTest extends Test.Container {
	
	public CacheTest() {
		super( Cache.class );
	}
	
	
	
	public static class Value {
		
		private static final int SIZE = 10000;
		
		private static int NEXT_ID = 0;
		
		
		private final int ID;
		private final String label;
		@SuppressWarnings( "unused" ) private final byte[] payload;
		
		private Value( String label ) {
			this.ID = Value.NEXT_ID++;
			this.label = label;
			this.payload = new byte[ Value.SIZE ];
		}
		
		public boolean identical( Value other ) { return this.ID == other.ID; }
		
		public boolean equivalent( Value other ) { return this.label.equals( other.label ); }
		
		@Override public String toString() { return this.label; }
		
		@Override public int hashCode() { return this.ID; }
		
	}
	
	
	public static class MyBuilder implements Cache.Builder<Integer, Value> {

		@Override
		public Value make( Integer key ) throws AppException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: Cache(Cache.Builder)", 
		description = "Throws AssertionError for null builder" 
	)
	public void tm_06B011624( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Cache<Integer, String>( null );
	}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "After garbage collection produces valid object" 
		)
		public void tm_0E649AFB4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "From empty cache returns valid object" 
		)
		public void tm_0465B6646( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "Get stress test" 
		)
		public void tm_0CAA94DD3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "Multi thread stress test" 
		)
		public void tm_09F0BB146( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "Stored uncolllectable object returns same object" 
		)
		public void tm_06EA017CC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "Throws AssertionError for null key" 
		)
		public void tm_09F53E05B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Object Cache.get(Comparable)", 
			description = "Throws AssertionError if Builder produces null" 
		)
		public void tm_0783D71AA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Cache.toString()", 
			description = "Result is not empty" 
		)
		public void tm_0D1FB8F40( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String Cache.toString()", 
			description = "Result is not null" 
		)
		public void tm_0D47FE88A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Cache.collected()", 
			description = "False at creation" 
		)
		public void tm_0B6B65700( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int Cache.size()", 
			description = "Created empty" 
		)
		public void tm_0ACE6B283( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int Cache.size()", 
			description = "Not empty after get" 
		)
		public void tm_0C706A3A0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Cache.flush()", 
			description = "Cache empty after" 
		)
		public void tm_03254E1A7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Cache.flush()", 
			description = "Then get() retrieves distinct instance" 
		)
		public void tm_07577F5DE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void Cache.flush()", 
			description = "Then get() retrieves equivalent value" 
		)
		public void tm_02488724C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

	
	
	

	public static void main( String[] args ) {
		Test.eval( Cache.class );
		//Test.evalPackage( Cache.class );
		//Test.evalAll();
	}
}
