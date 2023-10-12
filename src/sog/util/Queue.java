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

import sog.core.Test;

/**
 * Abstraction of the queue operations.
 * 
 * The Queue contract:
 * 
 * Queues have three states: open, closed, and terminated.
 * 
 * Queues are created open.
 * 
 * Allowed transitions: close(), terminate()
 * 		O -> C
 * 		O -> T
 * 		C -> T
 * 
 * State detected by predicates
 * 		isOpen()
 * 		isClosed()
 *		isTerminated()
 *
 * Queues are created empty
 * 		isEmpty()
 * 
 * Queues may optionally specify a "full" property
 * 
 * Successful put/get operations alter empty status and, optionally, full status
 * 
 * Elements can be added to the queue: put( E elt )
 * 		OPEN and not-full: accept the given element
 * 		OPEN and full: unspecified
 * 		CLOSED: Ignore the element, return false.
 * 		TERMINATED: Ignore the element, return false.
 * 
 * Elements can be retrieved from the queue: E get()
 * 		OPEN and non-empty: The next non-null element
 * 		OPEN and empty: unspecified
 * 		CLOSED: The next element or null if empty
 * 		TERMINATED: null
 * 		
 * Implementations:
 * 	1. Determine behavior for put( E elt ) when the queue is open and full
 * 	2. Determine behavior for E get() when the queue is open and empty
 * 	3. Determine the retrieval order policy
 * 	4. Test the contract
 * 
 * The OPEN/CLOSED/TERMINATED semantics are enforced by the AbstractQueue.
 * 
 * @see AbstractQueue
 * @see FifoQueue
 * @see MultiQueue
 * @see PriorityQueue
 */
@Test.Skip( "Abstract" )
public interface Queue<E> extends AutoCloseable {

	/**
	 * Tells if the queue is empty.
	 *
	 * @return
	 *      <tt>true</tt> if the queue contains no elements.
	 */
	public boolean isEmpty();

	/**
	 * Tells if the queue is open.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is open and accepting inputs and producing outputs.
	 */
	public boolean isOpen();

	/**
	 * Tells if the queue is closed.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is closed and not accepting inputs.
	 */
	public boolean isClosed();

	/**
	 * Tells if the queue has been terminated.
	 *
	 * @return
	 *      <tt>true</tt> if the queue is no longer accepting inputs or producing outputs.
	 */
	public boolean isTerminated();

	/**
	 * Response that this queue be closed, blocking further input.
	 */
	public void close();

	/**
	 * Response that this queue be terminated. After this call the queue ignores calls 
	 * to get, put, and close.
	 */
	public void terminate();
	
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
	public boolean put( E elt );
	
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
	 *   Return empty
	 *
	 * @return
	 *       The next element of the queue or null if the queue is done producing elements.
	 */
	public E get();
	
	
}
