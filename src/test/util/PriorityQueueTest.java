/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.TreeSet;

import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.PriorityQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class PriorityQueueTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return PriorityQueue.class;
	}

	// Test implementations

	
	private Queue<String> queue;

	@Override
	public Procedure beforeEach() {
		return new Procedure() {
			public void exec() {
				queue = new PriorityQueue<String>();
			}
		};
	}
	
	@Override
	public Procedure afterEach() {
		return new Procedure() {
			public void exec() {
				queue = null;
			}
		};
	}
	

	@TestOrig.Impl( src = "PriorityQueue", desc = "Elements retrieved in comparable order" )
	public void PriorityQueue_ElementsRetrievedInComparableOrder( TestCase tc ) {
		this.queue.put( "C" );
		this.queue.put( "B2" );
		this.queue.put( "A" );
		this.queue.put( "B1" );
		tc.assertEqual( "A",  this.queue.get() );
		tc.assertEqual( "B1",  this.queue.get() );
		tc.assertEqual( "B2",  this.queue.get() );
		tc.assertEqual( "C",  this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on closed empty returns null" )
	public void getImpl_GetOnClosedEmptyReturnsNull( TestCase tc ) {
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on closed non empty returns non null" )
	public void getImpl_GetOnClosedNonEmptyReturnsNonNull( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on open empty returns null" )
	public void getImpl_GetOnOpenEmptyReturnsNull( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on open non empty returns non null" )
	public void getImpl_GetOnOpenNonEmptyReturnsNonNull( TestCase tc ) {
		this.queue.put( "A" );
		tc.assertTrue( this.queue.isOpen() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on terminated empty returns null" )
	public void getImpl_GetOnTerminatedEmptyReturnsNull( TestCase tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected Comparable PriorityQueue.getImpl()", desc = "Get on terminated non empty returns null" )
	public void getImpl_GetOnTerminatedNonEmptyReturnsNull( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "protected boolean PriorityQueue.putImpl(Comparable)", desc = "Put on closed is ignored" )
	public void putImpl_PutOnClosedIsIgnored( TestCase tc ) {
		this.queue.put( "B" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.put( "A" ) );
		tc.assertEqual( "B", this.queue.get() );
	}

	@TestOrig.Impl( src = "protected boolean PriorityQueue.putImpl(Comparable)", desc = "Put on open is accepted" )
	public void putImpl_PutOnOpenIsAccepted( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.put( "B" ) );
		tc.assertEqual( "B", this.queue.get() );
	}

	@TestOrig.Impl( src = "protected boolean PriorityQueue.putImpl(Comparable)", desc = "Put on terminated is ignored" )
	public void putImpl_PutOnTerminatedIsIgnored( TestCase tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.put( "A" ) );
		tc.isNull( this.queue.get() );
	}

	@TestOrig.Impl( src = "public PriorityQueue()", desc = "Can be created empty" )
	public void PriorityQueue_CanBeCreatedEmpty( TestCase tc ) {
		tc.assertTrue ( this.queue.isEmpty() );
	}

	@TestOrig.Impl( src = "public PriorityQueue(SortedSet)", desc = "Can be created non empty" )
	public void PriorityQueue_CanBeCreatedNonEmpty( TestCase tc ) {
		TreeSet<String> set = new TreeSet<String>();
		set.add( "B" );
		set.add( "A" );
		queue = new PriorityQueue<String>( set );
		tc.assertFalse( queue.isEmpty() );
		tc.assertEqual( "A",  queue.get() );
	}

	@TestOrig.Impl( src = "public boolean PriorityQueue.isEmpty()", desc = "Put on empty is not empty" )
	public void isEmpty_PutOnEmptyIsNotEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@TestOrig.Impl( src = "public boolean PriorityQueue.isEmpty()", desc = "Put on non empty is not empty" )
	public void isEmpty_PutOnNonEmptyIsNotEmpty( TestCase tc ) {
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
		this.queue.put( "B" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@TestOrig.Impl( src = "public boolean PriorityQueue.isEmpty()", desc = "Put then get on empty is empty" )
	public void isEmpty_PutThenGetOnEmptyIsEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.get();
		tc.assertTrue( this.queue.isEmpty() );
	}

	@TestOrig.Impl( src = "public boolean PriorityQueue.isEmpty()", desc = "Put then put then get is not empty" )
	public void isEmpty_PutThenPutThenGetIsNotEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.put( "B" );
		this.queue.get();
		tc.assertFalse( this.queue.isEmpty() );
	}
	

	
	
	
	
	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new TestOrig(PriorityQueueTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
