/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 */

package sog.util;

import sog.core.Assert;
import sog.core.Test;

/**
 * Partial implementation of queue operations. Enforces open/close/terminate semantics.
 * 
 * Concrete implementations address the following details:
 * 		get when empty: block vs null
 * 		put when full: block vs exception
 * 		retrieval order: FIFO, LIFO, etc
 * 		
 * @see FifoQueue
 * @see MultiQueue
 * @see PriorityQueue
 */
public abstract class AbstractQueue<E> implements Queue<E> {

	/**
	 * The legal states for a Queue:
	 */
	@Test.Skip
	private enum State {

		/** Accepting input and providing output */
		OPEN {
			@Override
			<E> boolean put( AbstractQueue<E> q, E elt ) {
				return q.putImpl( elt );
			}

			@Override
			<E> E get( AbstractQueue<E> q ) {
				return q.getImpl();
			}
		},

		/** Providing output but not accepting input */
		CLOSED {
			@Override
			<E> boolean put( AbstractQueue<E> q, E elt ) {
				return false;
			}

			@Override
			<E> E get( AbstractQueue<E> q ) {
				return q.getImpl();
			}
		},

		/** Not accepting input and not providing output */
		TERMINATED {
			@Override
			<E> boolean put( AbstractQueue<E> q, E elt ) {
				return false;
			}

			@Override
			<E> E get( AbstractQueue<E> q ) {
				return null;
			}
		};
		
		abstract <E> boolean put( AbstractQueue<E> q, E elt );
		
		abstract <E> E get( AbstractQueue<E> q );
		
	}

	/** The current state */
	private volatile State state;
	
	/** All queues are created open */
	@Test.Decl( "Queues are created open" )
	protected AbstractQueue() {
		this.state = State.OPEN;
	}

	/**
	 * Request to add a non-null element to the queue.
	 * The behavior depends on the state of the queue and capacity:
	 *
	 * OPEN and not-full: accept the given element
	 * OPEN and full: unspecified
	 * CLOSED: Ignore the element, return false.
	 * TERMINATED: Ignore the element, return false.
	 *
	 * Two choices in the unspecified case, OPEN and full, include:
	 *   Block, waiting for available space
	 *   Throw appropriate exception
	 *
	 * @param elt
	 * 		The non-null element to add to the queue.
	 * 
	 * @return
	 * 		false if the queue is closed or terminated and the call has been ignored,
	 * 		true if the element has been accepted
	 */
	@Test.Skip( "The beavior depends on putImpl so implementations test this." )
	public boolean put( E elt ) {
		Assert.nonNull( elt );  // Queue cannot accept null elements.
		
		return this.state.put( this,  elt );
	}
	
	/**
	 * Accept the non-null element.
	 *
	 * @param elt
	 * 		The non-null element to add to the queue.
	 * @return
	 * 		true if the element is accepted
	 */
	protected abstract boolean putImpl( E elt );

	/**
	 * Get the next element from the queue. The return value depends on the
	 * state of the queue and on the empty/non-empty status of the queue:
	 *
	 * OPEN and non-empty: The next non-null element
	 * OPEN and empty: unspecified
	 * CLOSED: The next element or null if empty
	 * TERMINATED: null
	 *
	 * Two choices in the unspecified case, OPEN and empty, include:
	 *   Block, waiting for an available element
	 *   Throw NoSuchElementException
	 *
	 * @return
	 *       The next element of the queue or null if the queue is done producing elements.
	 */
	@Test.Skip( "The beavior depends on getImpl so implementations test this." )
	public E get() {
		return this.state.get( this );
	}
	
	/** 
	 * Get the next element, or null if empty
	 * 
	 * @return
	 *       The next element of the queue or null if the queue is done producing elements.
	 */
	protected abstract E getImpl();
	
	/**
	 * Tells if the queue is empty.
	 *
	 * @return
	 *      <tt>true</tt> if the queue contains no elements.
	 */
	@Test.Skip
	public abstract boolean isEmpty();

	/**
	 * Tells if the queue is open.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is open and accepting input.
	 */
	@Test.Skip
	public boolean isOpen() {
		return this.state == State.OPEN;
	}

	/**
	 * Tells if the queue is closed.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is closed and not accepting inputs.
	 */
	@Test.Skip
	public boolean isClosed() {
		return this.state == State.CLOSED;
	}

	/**
	 * Tells if the queue has been terminated.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is no longer accepting inputs or
	 *      producing outputs.
	 */
	@Test.Skip
	public boolean isTerminated() {
		return this.state == State.TERMINATED;
	}

	/**
	 * Request that this queue be closed, blocking further input. This call
	 * has no effect unless the queue is open.
	 */
	@Test.Decl( "Can close if open" )
	@Test.Decl( "Close on terminated ignored" )
	public void close() {
		if ( this.state == State.OPEN ) {
			this.state = State.CLOSED;
		}
	}

	/**
	 * Request that this queue be terminated. This causes the current contents
	 * to be discarded. After this call the queue ignores calls to get, put, and
	 * close.
	 */
	@Test.Decl( "Can terminate if open" )
	@Test.Decl( "Can terminate if closed" )
	public void terminate() {
		this.state = State.TERMINATED;
	}
	

}
