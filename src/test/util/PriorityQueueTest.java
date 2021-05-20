/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.TreeSet;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.PriorityQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class PriorityQueueTest extends Test.Container {

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
	

	@Test.Impl( member = "PriorityQueue", description = "Elements retrieved in comparable order" )
	public void PriorityQueue_ElementsRetrievedInComparableOrder( Test.Case tc ) {
		this.queue.put( "C" );
		this.queue.put( "B2" );
		this.queue.put( "A" );
		this.queue.put( "B1" );
		tc.assertEqual( "A",  this.queue.get() );
		tc.assertEqual( "B1",  this.queue.get() );
		tc.assertEqual( "B2",  this.queue.get() );
		tc.assertEqual( "C",  this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on closed empty returns null" )
	public void getImpl_GetOnClosedEmptyReturnsNull( Test.Case tc ) {
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on closed non empty returns non null" )
	public void getImpl_GetOnClosedNonEmptyReturnsNonNull( Test.Case tc ) {
		this.queue.put( "A" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on open empty returns null" )
	public void getImpl_GetOnOpenEmptyReturnsNull( Test.Case tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on open non empty returns non null" )
	public void getImpl_GetOnOpenNonEmptyReturnsNonNull( Test.Case tc ) {
		this.queue.put( "A" );
		tc.assertTrue( this.queue.isOpen() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on terminated empty returns null" )
	public void getImpl_GetOnTerminatedEmptyReturnsNull( Test.Case tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on terminated non empty returns null" )
	public void getImpl_GetOnTerminatedNonEmptyReturnsNull( Test.Case tc ) {
		this.queue.put( "A" );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on closed is ignored" )
	public void putImpl_PutOnClosedIsIgnored( Test.Case tc ) {
		this.queue.put( "B" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.put( "A" ) );
		tc.assertEqual( "B", this.queue.get() );
	}

	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on open is accepted" )
	public void putImpl_PutOnOpenIsAccepted( Test.Case tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.put( "B" ) );
		tc.assertEqual( "B", this.queue.get() );
	}

	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on terminated is ignored" )
	public void putImpl_PutOnTerminatedIsIgnored( Test.Case tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.put( "A" ) );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( member = "public PriorityQueue()", description = "Can be created empty" )
	public void PriorityQueue_CanBeCreatedEmpty( Test.Case tc ) {
		tc.assertTrue ( this.queue.isEmpty() );
	}

	@Test.Impl( member = "public PriorityQueue(SortedSet)", description = "Can be created non empty" )
	public void PriorityQueue_CanBeCreatedNonEmpty( Test.Case tc ) {
		TreeSet<String> set = new TreeSet<String>();
		set.add( "B" );
		set.add( "A" );
		queue = new PriorityQueue<String>( set );
		tc.assertFalse( queue.isEmpty() );
		tc.assertEqual( "A",  queue.get() );
	}

	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put on empty is not empty" )
	public void isEmpty_PutOnEmptyIsNotEmpty( Test.Case tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put on non empty is not empty" )
	public void isEmpty_PutOnNonEmptyIsNotEmpty( Test.Case tc ) {
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
		this.queue.put( "B" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put then get on empty is empty" )
	public void isEmpty_PutThenGetOnEmptyIsEmpty( Test.Case tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.get();
		tc.assertTrue( this.queue.isEmpty() );
	}

	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put then put then get is not empty" )
	public void isEmpty_PutThenPutThenGetIsNotEmpty( Test.Case tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.put( "B" );
		this.queue.get();
		tc.assertFalse( this.queue.isEmpty() );
	}
	

	
	
}
