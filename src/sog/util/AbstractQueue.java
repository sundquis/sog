/**
 * Copyright (C) 2021, 2023
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
@Test.Subject( "test." )
public abstract class AbstractQueue<E> implements Queue<E> {

	/**
	 * The legal states for a Queue:
	 */
	private enum State {

		/** Accepting input and providing output */
		@Test.Skip( "Enumerated" )
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
		@Test.Skip( "Enumerated" )
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
		@Test.Skip( "Enumerated" )
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
	 * Response to add a non-null element to the queue.
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
	@Test.Decl( "Returns true when OPEN" )
	@Test.Decl( "Returns false when CLOSED" )
	@Test.Decl( "Returns false when TERMINATED" )
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
	@Test.Decl( "Consistent with getImpl when OPEN and non-empty" )
	@Test.Decl( "Consistent with getImpl when CLOSED" )
	@Test.Decl( "Returns null when TERMINATED" )
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
	public abstract boolean isEmpty();

	/**
	 * Tells if the queue is open.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is open and accepting input.
	 */
	@Test.Decl( "True when constructed" )
	@Test.Decl( "False after close()" )
	@Test.Decl( "False after terminate()" )
	public boolean isOpen() {
		return this.state == State.OPEN;
	}

	/**
	 * Tells if the queue is closed.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is closed and not accepting inputs.
	 */
	@Test.Decl( "False when constructed" )
	@Test.Decl( "True after close()" )
	@Test.Decl( "False after terminate()" )
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
	@Test.Decl( "False when constructed" )
	@Test.Decl( "False after close()" )
	@Test.Decl( "True after terminate()" )
	public boolean isTerminated() {
		return this.state == State.TERMINATED;
	}

	/**
	 * Response that this queue be closed, blocking further input. This call
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
	 * Response that this queue be terminated. This causes the current contents
	 * to be discarded. After this call the queue ignores calls to get, put, and
	 * close.
	 */
	@Test.Decl( "Can terminate if open" )
	@Test.Decl( "Can terminate if closed" )
	public void terminate() {
		this.state = State.TERMINATED;
	}
	

}
