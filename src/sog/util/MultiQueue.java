/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 */

package sog.util;


import sog.core.Assert;
import sog.core.Test;

/**
 * Decorates a queue by adding thread-safe synchronization and blocking
 * semantics to the accessors.
 */
@Test.Decl( "If backed by priority queue elements retrieved in priority order" )
@Test.Decl( "If backed by FIFO queue elements retrieved in FIFO order" )
@Test.Decl( "Multi thread stress test" )
public class MultiQueue<E> implements Queue<E> {


	// The backing Queue
	private Queue<E> q;

	/**
	 * Constructs a queue containing the elements of the given Queue.
	 * The queue has the same state as the wrapped queue.
	 *
	 * <p>
	 * <b>Note:</b> Direct operations on the underlying <tt>Queue</tt> are
	 * not thread-safe. Safe access is available through the <tt>MultiQueue</tt>
	 * wrapper only.
	 *
	 * @param elements
	 *      A queue of elements.
	 */
	@Test.Decl( "is empty if queue is empty" )
	@Test.Decl( "is non empty if queue is non empty" )
	@Test.Decl( "is open if queue is open" )
	@Test.Decl( "is closed if queue is closed" )
	@Test.Decl( "is terminated if queue is terminated" )
	public MultiQueue( Queue<E> q ) {
		this.q = Assert.nonNull( q );
	}

	/**
	 * Tells if the queue is open.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is open and accepting inputs and producing outputs.
	 */
	@Override
	@Test.Decl( "is open if queue is open" )
	public synchronized boolean isOpen() {
		return this.q.isOpen();
	}

	/**
	 * Tells if the queue is closed.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is closed and not accepting inputs.
	 */
	@Override
	@Test.Decl( "is closed if queue is closed" )
	public synchronized boolean isClosed() {
		return this.q.isClosed();
	}

	/**
	 * Tells if the queue has been terminated.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is no longer accepting inputs or
	 *      producing outputs.
	 */
	@Override
	@Test.Decl( "is terminated if queue is terminated" )
	public synchronized boolean isTerminated() {
		return this.q.isTerminated();
	}

	/**
	 * Request that this queue be closed, blocking further input.
	 */
	@Override
	@Test.Decl( "Can close if open" )
	@Test.Decl( "Close on terminated ignored" )
	public synchronized void close() {
		this.q.close();
		this.notifyAll();
	}

	/**
	 * Request that this queue be terminated. After this call the queue ignores calls 
	 * to get, put, and close.
	 */
	@Override
	@Test.Decl( "Can terminate if open" )
	@Test.Decl( "Can terminate if closed" )
	public synchronized void terminate() {
		this.q.terminate();
		this.notifyAll();
	}
	
	/**
	 * Request to add a non-null element to the queue.
	 *
	 * @param elt
	 * 		The non-null element to add to the queue.
	 * 
	 * @return
	 * 		false if the queue is closed or terminated and the call has been ignored,
	 * 		true if the element has been accepted
	 */
	@Override
	@Test.Decl( "Put on open is accepted" )
	@Test.Decl( "Put on closed is ignored" )
	@Test.Decl( "Put on terminated is ignored" )
	public synchronized boolean put( E elt ) {
		boolean result = this.q.put( elt );
		this.notifyAll();
		return result;
	}
	
	/**
	 * Get the next element from the queue. The return value depends on the
	 * state of the queue and on the empty/non-empty status of the queue:
	 *
	 * OPEN and non-empty: The next non-null element
	 * OPEN and empty: block, awaiting an element
	 * CLOSED: The next element or null if empty
	 * TERMINATED: null
	 *
	 * @return
	 *       The next element of the queue or null if the queue is done
	 *       producing elements.
	 */
	@Override
	@Test.Decl( "Get on open non empty returns non null" )
	@Test.Decl( "Get on open empty blocks awaiting notification" )
	@Test.Decl( "Get on closed non empty returns non null" )
	@Test.Decl( "Get on closed empty returns null" )
	@Test.Decl( "Get on terminated non empty returns null" )
	@Test.Decl( "Get on terminated empty returns null" )
	public synchronized E get() {
		while ( true ) {
			if ( this.q.isTerminated() ) {
				return null;
			}
			
			if ( this.q.isClosed() ) {
				return this.q.get();
			}
			
			if ( ! this.q.isEmpty() ) {
				return this.q.get();
			}
			
			try {
				this.wait();
			} catch ( InterruptedException ex ) {}
		}
	}

	
	/**
	 * Tells if the queue is empty.
	 *
	 * @return
	 *      <tt>true</tt> if the queue contains no elements.
	 */
	@Override
	@Test.Decl( "Put on empty is not empty" )
	@Test.Decl( "Put on non empty is not empty" )
	@Test.Decl( "Put then get on empty is empty" )
	@Test.Decl( "Put then put then get is not empty" )
	public synchronized boolean isEmpty() {
		return this.q.isEmpty();
	}
	
	

}
