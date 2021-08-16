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

import java.util.Map;
import java.util.stream.Stream;

import sog.core.AppException;
import sog.core.Cache;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.Test;

/**
 * A number of test cases for Cache provoke garbage collection which contributes to execution time.
 */
@Test.Skip( "Container" )
public class CacheTest extends Test.Container {
	
	
	public CacheTest() {
		super( Cache.class );
	}
	
	
	public static class Value {
		// Chosen to minimize the total execution time.
		private final static int SIZE = 1_000_000;
		private static int NEXT_ID = 0;
		
		private final int ID;
		private final String label;
		@SuppressWarnings( "unused" ) 
		private final byte[] payload;
		
		private Value( String label ) {
			this.ID = Value.NEXT_ID++;
			this.label = label;
			this.payload = new byte[ Value.SIZE ];
		}
		
		public boolean equivalent( Value other ) { return this.label.equals( other.label ); }
		public boolean identical( Value other ) { return this.ID == other.ID; }

		@Override public String toString() { return "[" + this.ID + "]" + this.label; }
		@Override public int hashCode() { return this.ID; }
	}
	
	public static final Cache.Builder<Integer, CacheTest.Value> BUILDER = n -> {
		return new CacheTest.Value( Strings.rightJustify( n.toString(), 20, '_' ) );
	};
	
	private Cache<Integer, Value> cache;
	
	@Override public Procedure beforeEach() {
		return () -> {
			this.cache = new Cache<>( CacheTest.BUILDER );
		};
	}
	
	@Override public Procedure afterEach() {
		return () -> {
			this.cache.clear();
			this.cache = null;
		};
	}
	
	public boolean cacheHasKey( int n ) {
		Map<Integer, Value> map = this.getSubjectField( this.cache, "map", null );
		return map.containsKey( n );
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
		description = "After clear() produces equivalent object" 
	)
	public void tm_043EC5F55( Test.Case tc ) {
		Value value = this.cache.get( 42 );
		this.cache.clear();
		tc.assertTrue( value.equivalent( this.cache.get( 42 ) ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "After clear() produces non-identical object" 
	)
	public void tm_0C28C7D80( Test.Case tc ) {
		Value value = this.cache.get( 42 );
		this.cache.clear();
		tc.assertFalse( value.identical( this.cache.get( 42 ) ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "After garbage collection produces valid object" 
	)
	public void tm_0E649AFB4( Test.Case tc ) {
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		tc.assertEqual( this.cache.get( 42 ), this.cache.get( 42 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "From empty cache returns valid object" 
	)
	public void tm_0465B6646( Test.Case tc ) {
		tc.assertEqual( this.cache.get( 42 ), this.cache.get( 42 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Produces identical values for identical keys after collection" 
	)
	public void tm_05CCE4EE5( Test.Case tc ) {
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		tc.assertTrue( this.cache.get( n ).identical( this.cache.get( n ) ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Produces identical values for identical keys before collection" 
	)
	public void tm_0F01D5012( Test.Case tc ) {
		int n = 55;
		tc.assertTrue( this.cache.get( n ).identical( this.cache.get( n ) ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Stored uncolllectable object returns identical object" 
	)
	public void tm_030EFD0AD( Test.Case tc ) {
		Value value = this.cache.get( 0 );
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		tc.assertTrue( value.identical( this.cache.get( 0 ) ) );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Throws AppException if Builder throws exception" 
	)
	public void tm_0E55DE864( Test.Case tc ) {
		Cache.Builder<Integer, String> builder = (n) -> {
			throw new AssertionError();
		};
		Cache<Integer, String> cache = new Cache<>( builder );
		tc.expectError( AppException.class );
		cache.get( 0 );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Throws AssertionError for null key" 
	)
	public void tm_09F53E05B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.cache.get( null );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Throws AssertionError if Builder produces null" 
	)
	public void tm_0783D71AA( Test.Case tc ) {
		Cache.Builder<Integer, String> builder = (n) -> {
			return null;
		};
		Cache<Integer, String> cache = new Cache<>( builder );
		tc.expectError( AssertionError.class );
		cache.get( 0 );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Values before and after collection are equivalent" 
	)
	public void tm_045F979A7( Test.Case tc ) {
		String label = this.cache.get( 0 ).label;
		int n = 0;
		while ( this.cacheHasKey( 0 ) ) {
			this.cache.get( n++ );
		}
		tc.assertEqual( label, this.cache.get( 0 ).label );
	}
		
	@Test.Impl( 
		member = "method: Object Cache.get(Comparable)", 
		description = "Values before and after collection are not identical" 
	)
	public void tm_05C060DA1( Test.Case tc ) {
		String label = this.cache.get( 0 ).toString();
		int n = 0;
		while ( this.cacheHasKey( 0 ) ) {
			this.cache.get( n++ );
		}
		tc.assertFalse( label.equals( this.cache.get( 0 ).toString() ) );
	}
		
	@Test.Impl( 
		member = "method: String Cache.toString()", 
		description = "Result is not empty" 
	)
	public void tm_0D1FB8F40( Test.Case tc ) {
		tc.assertNotEmpty( this.cache.toString() );
		this.cache.get( 0 );
		tc.assertNotEmpty( this.cache.toString() );
		this.cache.clear();
		tc.assertNotEmpty( this.cache.toString() );
	}
		
	@Test.Impl( 
		member = "method: int Cache.collected()", 
		description = "Greater than zero when the size of the map decreases" 
	)
	public void tm_0BAF8A0A0( Test.Case tc ) {
		int n = 0;
		int previous = this.cache.size();
		while ( this.cache.size() >= previous ) {
			previous = this.cache.size();
			this.cache.get( n++ );
		}
		tc.assertTrue( this.cache.collected() > 0 );
	}
		
	@Test.Impl( 
		member = "method: int Cache.collected()", 
		description = "Unchanged after clear()" 
	)
	public void tm_02FCB933D( Test.Case tc ) {
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		int collected = this.cache.collected();
		this.cache.clear();
		tc.assertEqual( collected, this.cache.collected() );
	}
		
	@Test.Impl( 
		member = "method: int Cache.collected()", 
		description = "Zero at creation" 
	)
	public void tm_0B3F7DD32( Test.Case tc ) {
		tc.assertEqual( 0, this.cache.collected() );
	}
		
	@Test.Impl( 
		member = "method: int Cache.collected()", 
		description = "collected() + size() is invariant across collections" 
	)
	public void tm_0B29392B5( Test.Case tc ) {
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		int collected = this.cache.collected();
		int previousSize = 0;
		while ( this.cache.collected() == collected ) {
			previousSize = this.cache.size();
			this.cache.get( n++ );
		}
		// GC occurs before new item is inserted in map so:
		int newTotal = this.cache.collected() + this.cache.size() - 1;
		tc.assertEqual( collected + previousSize, newTotal );
	}
		
	@Test.Impl( 
		member = "method: int Cache.size()", 
		description = "Created empty" 
	)
	public void tm_0ACE6B283( Test.Case tc ) {
		tc.assertTrue( this.cache.size() == 0 );
	}
		
	@Test.Impl( 
		member = "method: int Cache.size()", 
		description = "Not empty after get" 
	)
	public void tm_0C706A3A0( Test.Case tc ) {
		this.cache.get( 0 );
		tc.assertTrue( this.cache.size() > 0 );
	}
		
	@Test.Impl( 
		member = "method: int Cache.size()", 
		description = "Zero after clear()" 
	)
	public void tm_0C70038FA( Test.Case tc ) {
		Stream.of( 0, 1, 2, 3 ).forEach( this.cache::get );
		tc.assertTrue( this.cache.size() > 0 );
		this.cache.clear();
		tc.assertTrue( this.cache.size() == 0 );
	}
		
	@Test.Impl( 
		member = "method: void Cache.clear()", 
		description = "Idempotent" 
	)
	public void tm_0A7ED3A2A( Test.Case tc ) {
		int n = 0;
		while ( this.cache.collected() == 0 ) {
			this.cache.get( n++ );
		}
		this.cache.clear();
		int oldSize = this.cache.size();
		int oldCollected = this.cache.collected();
		String oldString = this.cache.toString();
		this.cache.clear();
		tc.assertEqual( oldSize, this.cache.size() );
		tc.assertEqual( oldCollected, this.cache.collected() );
		tc.assertEqual( oldString, this.cache.toString() );
	}

	
	
	

	public static void main( String[] args ) {
		Test.eval( Cache.class );
		//Test.evalPackage( Cache.class );
		//Test.evalAll();
	}
}
