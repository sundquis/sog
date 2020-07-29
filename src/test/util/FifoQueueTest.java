/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.FifoQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class FifoQueueTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return FifoQueue.class;
	}

	// Test implementations
	
	
	
	private Queue<String> queue;

	@Override
	public Procedure beforeEach() {
		return () -> this.queue = new FifoQueue<String>();
	}
	
	@Override
	public Procedure afterEach() {
		return () -> this.queue = null;
	}
	
	

	@Test.Impl( src = "FifoQueue", desc = "Elements retrieved in FIFO order" )
	public void FifoQueue_ElementsRetrievedInFifoOrder( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.put( "B" );
		this.queue.put( "C" );
		tc.assertEqual( "A",  this.queue.get() );
		tc.assertEqual( "B",  this.queue.get() );
		tc.assertEqual( "C",  this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on closed empty returns null" )
	public void getImpl_GetOnClosedEmptyReturnsNull( TestCase tc ) {
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on closed non empty returns non null" )
	public void getImpl_GetOnClosedNonEmptyReturnsNonNull( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on open empty returns null" )
	public void getImpl_GetOnOpenEmptyReturnsNull( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on open non empty returns non null" )
	public void getImpl_GetOnOpenNonEmptyReturnsNonNull( TestCase tc ) {
		this.queue.put( "A" );
		tc.assertTrue( this.queue.isOpen() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.notNull( this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on terminated empty returns null" )
	public void getImpl_GetOnTerminatedEmptyReturnsNull( TestCase tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertTrue( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( src = "protected Object FifoQueue.getImpl()", desc = "Get on terminated non empty returns null" )
	public void getImpl_GetOnTerminatedNonEmptyReturnsNull( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.isEmpty() );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( src = "protected boolean FifoQueue.putImpl(Object)", desc = "Put on closed is ignored" )
	public void putImpl_PutOnClosedIsIgnored( TestCase tc ) {
		this.queue.put( "A" );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		tc.assertFalse( this.queue.put( "B" ) );
		tc.assertEqual( "A", this.queue.get() );
	}

	@Test.Impl( src = "protected boolean FifoQueue.putImpl(Object)", desc = "Put on open is accepted" )
	public void putImpl_PutOnOpenIsAccepted( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		tc.assertTrue( this.queue.put( "B" ) );
		tc.assertEqual( "B", this.queue.get() );
	}

	@Test.Impl( src = "protected boolean FifoQueue.putImpl(Object)", desc = "Put on terminated is ignored" )
	public void putImpl_PutOnTerminatedIsIgnored( TestCase tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		tc.assertFalse( this.queue.put( "A" ) );
		tc.isNull( this.queue.get() );
	}

	@Test.Impl( src = "public FifoQueue()", desc = "FifoQueues are created empty" )
	public void FifoQueue_FifoQueuesAreCreatedEmpty( TestCase tc ) {
		tc.assertTrue ( this.queue.isEmpty() );
	}

	@Test.Impl( src = "public boolean FifoQueue.isEmpty()", desc = "Put on empty is not empty" )
	public void isEmpty_PutOnEmptyIsNotEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@Test.Impl( src = "public boolean FifoQueue.isEmpty()", desc = "Put on non empty is not empty" )
	public void isEmpty_PutOnNonEmptyIsNotEmpty( TestCase tc ) {
		this.queue.put( "A" );
		tc.assertFalse( this.queue.isEmpty() );
		this.queue.put( "B" );
		tc.assertFalse( this.queue.isEmpty() );
	}

	@Test.Impl( src = "public boolean FifoQueue.isEmpty()", desc = "Put then get on empty is empty" )
	public void isEmpty_PutThenGetOnEmptyIsEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.get();
		tc.assertTrue( this.queue.isEmpty() );
	}

	@Test.Impl( src = "public boolean FifoQueue.isEmpty()", desc = "Put then put then get is not empty" )
	public void isEmpty_PutThenPutThenGetIsNotEmpty( TestCase tc ) {
		tc.assertTrue( this.queue.isEmpty() );
		this.queue.put( "A" );
		this.queue.put( "A" );
		this.queue.get();
		tc.assertFalse( this.queue.isEmpty() );
	}


	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new Test(FifoQueueTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
