/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.AbstractQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class AbstractQueueTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return AbstractQueue.class;
	}

	// Test implementations


	// put() and get() not functional.
	// Used to test the semantics of open, close, terminate
	class ConcreteQueue<T> extends AbstractQueue<T> {
		
		ConcreteQueue() { super(); }

		@Override protected boolean putImpl(Object elt) { return false; }

		@Override protected T getImpl() { return null; }

		@Override public boolean isEmpty() { return false; }
		
	}

	
	private Queue<String> queue;
	
	@Override public Procedure beforeEach() {
		return () -> this.queue = new ConcreteQueue<>();
	}
	
	@Override public Procedure afterEach() {
		return () -> this.queue = null;
	}
	

	@TestOrig.Impl( src = "protected AbstractQueue()", desc = "Queues are created open" )
	public void AbstractQueue_QueuesAreCreatedOpen( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
	}

	@TestOrig.Impl( src = "public void AbstractQueue.close()", desc = "Can close if open" )
	public void close_CanCloseIfOpen( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
	}

	@TestOrig.Impl( src = "public void AbstractQueue.close()", desc = "Close on terminated ignored" )
	public void close_CloseOnTerminatedIgnored( TestCase tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		this.queue.close();
		tc.assertFalse( this.queue.isClosed() );
	}

	@TestOrig.Impl( src = "public void AbstractQueue.terminate()", desc = "Can terminate if closed" )
	public void terminate_CanTerminateIfClosed( TestCase tc ) {
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
	}

	@TestOrig.Impl( src = "public void AbstractQueue.terminate()", desc = "Can terminate if open" )
	public void terminate_CanTerminateIfOpen( TestCase tc ) {
		tc.assertTrue( this.queue.isOpen() );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
	}

	
	
	
	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new TestOrig(AbstractQueueTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
