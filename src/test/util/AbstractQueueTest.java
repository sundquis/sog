/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.AbstractQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class AbstractQueueTest extends Test.Container {

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
	
	public AbstractQueueTest() {
		super( AbstractQueue.class );
	}
	
	
	@Override public Procedure beforeEach() {
		return () -> this.queue = new ConcreteQueue<>();
	}
	
	@Override public Procedure afterEach() {
		return () -> this.queue = null;
	}
	

	@Test.Impl( member = "protected AbstractQueue()", description = "Queues are created open" )
	public void AbstractQueue_QueuesAreCreatedOpen( Test.Case tc ) {
		tc.assertTrue( this.queue.isOpen() );
	}

	@Test.Impl( member = "public void AbstractQueue.close()", description = "Can close if open" )
	public void close_CanCloseIfOpen( Test.Case tc ) {
		tc.assertTrue( this.queue.isOpen() );
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
	}

	@Test.Impl( member = "public void AbstractQueue.close()", description = "Close on terminated ignored" )
	public void close_CloseOnTerminatedIgnored( Test.Case tc ) {
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
		this.queue.close();
		tc.assertFalse( this.queue.isClosed() );
	}

	@Test.Impl( member = "public void AbstractQueue.terminate()", description = "Can terminate if closed" )
	public void terminate_CanTerminateIfClosed( Test.Case tc ) {
		this.queue.close();
		tc.assertTrue( this.queue.isClosed() );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
	}

	@Test.Impl( member = "public void AbstractQueue.terminate()", description = "Can terminate if open" )
	public void terminate_CanTerminateIfOpen( Test.Case tc ) {
		tc.assertTrue( this.queue.isOpen() );
		this.queue.terminate();
		tc.assertTrue( this.queue.isTerminated() );
	}

	
	
}
