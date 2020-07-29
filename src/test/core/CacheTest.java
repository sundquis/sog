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
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.Cache.Builder;

/**
 * @author sundquis
 *
 */
public class CacheTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Cache.class;
	}

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
	
	@Test.Impl( src = "public Cache(Cache.Builder)", desc = "Null Builder throws Assertion Error" )
	public void Cache_NullBuilderThrowsAssertionError( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new Cache<String, String>( null );
	}

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "From empty cache returns valid object" )
	public void get_FromEmptyCacheReturnsValidObject( TestCase tc ) {
		tc.assertTrue( cache.size() == 0 );
		Value v1 = this.builder.make(0);
		Value v2 = this.cache.get(0);
		tc.assertEqual( v1.toString(), v2.toString() );
		tc.assertFalse( v1.getId() == v2.getId() );
	}

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "Multi thread stress test", weight = 10 )
	public void get_MultiThreadStressTest( TestCase tc ) {
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

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "Null key throws Assertion Error" )
	public void get_NullKeyThrowsAssertionError( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.cache.get( null );
	}

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "Get stress test", weight = 10 )
	public void get_GetStressTest( TestCase tc ) {
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

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "Stored uncolllectable object returns same object", weight = 10 )
	public void get_StoredUncolllectableObjectReturnsSameObject( TestCase tc ) {
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

	@Test.Impl( src = "public Object Cache.get(Comparable)", desc = "Values are not null" )
	public void get_ValuesAreNotNull( TestCase tc ) {
		tc.assertTrue( this.cache.get(42) != null );
	}

	@Test.Impl( src = "public void Cache.flush()", desc = "Cache empty after" )
	public void flush_CacheEmptyAfter( TestCase tc ) {
		for ( int i = 0; i < 1000; i++ ) {
			this.cache.get(i);
		}
		tc.assertTrue( this.cache.size() > 0 );
		cache.flush();
		tc.assertEqual( this.cache.size(), 0 );
	}

	@Test.Impl( src = "public void Cache.flush()", desc = "Then get() retrieves equivalent value" )
	public void flush_ThenGetRetrievesEquivalentValue( TestCase tc ) {
		Value orig = cache.get( 42 );
		cache.flush();
		tc.assertEqual( orig.toString() , this.cache.get(42).toString() );
	}

	@Test.Impl( src = "public void Cache.flush()", desc = "Then get() retrieves distinct instance" )
	public void flush_ThenGetRetrievesDistinctInstance( TestCase tc ) {
		Value orig = cache.get( 42 );
		int id  = orig.getId();
		cache.flush();
		tc.assertFalse( id == this.cache.get(42).getId() );
	}
	
	@Test.Impl( src = "public String Cache.toString()", desc = "Result is not empty" )
	public void toString_ResultIsNotEmpty( TestCase tc ) {
		tc.assertFalse( this.cache.toString().isEmpty() );
	}

	@Test.Impl( src = "public String Cache.toString()", desc = "Result is not null" )
	public void toString_ResultIsNotNull( TestCase tc ) {
		tc.notNull( this.cache.toString() );
	}

	@Test.Impl( src = "public int Cache.size()", desc = "Created empty" )
	public void size_CreatedEmpty( TestCase tc ) {
		tc.assertEqual( 0,  this.cache.size() );
	}

	@Test.Impl( src = "public int Cache.size()", desc = "Not empty after get" )
	public void size_NotEmptyAfterGet( TestCase tc ) {
		this.cache.get( 0 );
		tc.assertEqual( 1,  this.cache.size() );
	}

	@Test.Impl( src = "public boolean Cache.collected()", desc = "False at creation" )
	public void collected_FalseAtCreation( TestCase tc ) {
		tc.assertFalse( this.cache.collected() );
	}

	
	
	

	public static void main(String[] args) {

		System.out.println();

		new Test(CacheTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
