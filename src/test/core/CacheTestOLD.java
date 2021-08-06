/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import sog.core.AppException;
import sog.core.Cache;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.Test;
import sog.core.Cache.Builder;

/**
 * @author sundquis
 *
 */
public class CacheTestOLD extends Test.Container {

	// Test implementations
	
	public static class Value {
		private static int ID = 0;
		
		private int id;
		private String text;
		@SuppressWarnings("unused")
		private byte[] payload;
		
		public Value( String text ) {
			this.id = ID++;
			this.text = text;
			this.payload = new byte[10000];
		}
		
		public int getId() { return this.id; }

		@Override public String toString() { return this.text; }
	}

	// Construct heavy values:
	public static class MyBuilder implements Builder<Integer, Value> {
		@Override
		public Value make( Integer key ) throws AppException {
			return new Value( Strings.rightJustify( key.toString(), 30, '_' ) );
		}
	}
	
	public static class Agent extends Thread {

		private static int ITERATIONS = 1000;		// Stress: 1000
		private static Cache<Integer, Value> cache = new Cache<>( new MyBuilder() );

		private static void dispose() {
			Agent.cache.flush();
			Agent.cache = null;
		}
		
		private boolean consistent = true;
		
		@Override
		public void run() {
			int key;
			int code;
			Value v;
			for ( int i = 0; i < ITERATIONS; i++ ) {
				key = 209 * i + 101010;
				v = Agent.cache.get( key );
				code = v.toString().hashCode();
				v = null;
				Thread.yield();
				v = Agent.cache.get( key );
				this.consistent &= code == v.toString().hashCode();
			}
		}

		public boolean isConsitent() {
			return this.consistent;
		}
		
		public Agent init() {
			this.start();
			return this;
		}
	}
	
	private Cache<Integer, Value> cache;
	private Builder<Integer, Value> builder = new MyBuilder();
	
	public CacheTestOLD() {
		super( Cache.class );
	}
	
	@Override
	public Procedure beforeEach() {
		return () -> cache = new Cache<>( this.builder );
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			cache.flush();
			cache = null;
		};
	}
	
	@Test.Impl( member = "public Cache(Cache.Builder)", description = "Null Builder throws Assertion Error" )
	public void Cache_NullBuilderThrowsAssertionError( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Cache<String, String>( null );
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "From empty cache returns valid object" )
	public void get_FromEmptyCacheReturnsValidObject( Test.Case tc ) {
		tc.assertTrue( cache.size() == 0 );
		Value v1 = this.builder.make(0);
		Value v2 = this.cache.get(0);
		tc.assertEqual( v1.toString(), v2.toString() );
		tc.assertFalse( v1.getId() == v2.getId() );
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "Multi thread stress test", weight = 10 )
	public void get_MultiThreadStressTest( Test.Case tc ) {
		tc.afterThis( () -> Agent.dispose() );
		ArrayList<Agent> agents = new ArrayList<>();
		for ( int i = 0; i < 5; i++ ) {
			agents.add( new Agent().init() );
		}
		for ( Agent agent : agents ) {
			try {
				agent.join();
				tc.assertTrue( agent.isConsitent() );
				// Not really a test of concurrency...
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "Null key throws Assertion Error" )
	public void get_NullKeyThrowsAssertionError( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.cache.get( null );
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "Get stress test", weight = 10 )
	public void get_GetStressTest( Test.Case tc ) {
		boolean consistent = true;
		Set<Integer> codes = new HashSet<>();
		int M = 100;
		Function<Integer, Integer> f = (x) -> 713 * x - 12345;
		for ( int i = 0; i < M; i++ ) {
			codes.add( this.cache.get( f.apply(i) ).toString().hashCode() );
		}
		this.cache.flush();
		for ( int i = 0; i < M; i++ ) {
			consistent &= codes.contains( this.cache.get( f.apply(i) ).toString().hashCode() );
		}
		tc.assertTrue( consistent );
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "Stored uncolllectable object returns same object", weight = 10 )
	public void get_StoredUncolllectableObjectReturnsSameObject( Test.Case tc ) {
		int curSize = this.cache.size();
		tc.assertTrue( curSize == 0 );
		
		Value strongReference = this.cache.get( 42 );  // This Value can't by collected
		tc.assertTrue( this.cache.size() == 1 );

		int i = 43;
		while ( ! this.cache.collected() ) {
			this.cache.get( i++ );
		}
		
		// Some objects have been GC'd, but not our strongly held value
		// If it had been collected then its id would have changed
		tc.assertEqual( strongReference.getId(), this.cache.get( 42 ).getId() );
	}

	@Test.Impl( member = "public Object Cache.get(Comparable)", description = "Values are not null" )
	public void get_ValuesAreNotNull( Test.Case tc ) {
		tc.assertTrue( this.cache.get(42) != null );
	}

	@Test.Impl( member = "public void Cache.flush()", description = "Cache empty after" )
	public void flush_CacheEmptyAfter( Test.Case tc ) {
		for ( int i = 0; i < 1000; i++ ) {
			this.cache.get(i);
		}
		tc.assertTrue( this.cache.size() > 0 );
		cache.flush();
		tc.assertEqual( this.cache.size(), 0 );
	}

	@Test.Impl( member = "public void Cache.flush()", description = "Then get() retrieves equivalent value" )
	public void flush_ThenGetRetrievesEquivalentValue( Test.Case tc ) {
		Value orig = cache.get( 42 );
		cache.flush();
		tc.assertEqual( orig.toString() , this.cache.get(42).toString() );
	}

	@Test.Impl( member = "public void Cache.flush()", description = "Then get() retrieves distinct instance" )
	public void flush_ThenGetRetrievesDistinctInstance( Test.Case tc ) {
		Value orig = cache.get( 42 );
		int id  = orig.getId();
		cache.flush();
		tc.assertFalse( id == this.cache.get(42).getId() );
	}
	
	@Test.Impl( member = "public String Cache.toString()", description = "Result is not empty" )
	public void toString_ResultIsNotEmpty( Test.Case tc ) {
		tc.assertFalse( this.cache.toString().isEmpty() );
	}

	@Test.Impl( member = "public String Cache.toString()", description = "Result is not null" )
	public void toString_ResultIsNotNull( Test.Case tc ) {
		tc.assertNonNull( this.cache.toString() );
	}

	@Test.Impl( member = "public int Cache.size()", description = "Created empty" )
	public void size_CreatedEmpty( Test.Case tc ) {
		tc.assertEqual( 0,  this.cache.size() );
	}

	@Test.Impl( member = "public int Cache.size()", description = "Not empty after get" )
	public void size_NotEmptyAfterGet( Test.Case tc ) {
		this.cache.get( 0 );
		tc.assertEqual( 1,  this.cache.size() );
	}

	@Test.Impl( member = "public boolean Cache.collected()", description = "False at creation" )
	public void collected_FalseAtCreation( Test.Case tc ) {
		tc.assertFalse( this.cache.collected() );
	}

	
	
	
}
