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

import java.util.LinkedList;

import sog.core.Test;

/**
 * Implements the Queue behavior and specifies a FIFO property on elements.
 * 
 * Queue Implementation:
 * 	1. Determine behavior for put( E elt ) when the queue is open and full
 * 		No full property.
 * 
 * 	2. Determine behavior for E get() when the queue is open and empty
 * 		Return null if open and empty.
 * 
 * 	3. Determine the order retrieval policy
 * 		"First in first out"
 * 
 */
@Test.Subject( "test." )
public class FifoQueue<E> extends AbstractQueue<E> {


	private LinkedList<E> elements;

	/**
	 * Constructs an empty FIFO queue. The queue is open and accepting input.
	 */
	@Test.Decl( "FifoQueues are created empty" )
	public FifoQueue() {
		this.elements = new LinkedList<E>();
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
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	@Test.Decl( "Put on open is accepted" )
	@Test.Decl( "Put on closed is ignored" )
	@Test.Decl( "Put on terminated is ignored" )
	protected boolean putImpl( E elt ) {
		this.elements.addLast( elt );
		return true;
	}


	@Override
	@Test.Decl( "Get on open non empty returns non null" )
	@Test.Decl( "Get on open empty returns null" )
	@Test.Decl( "Get on closed non empty returns non null" )
	@Test.Decl( "Get on closed empty returns null" )
	@Test.Decl( "Get on terminated non empty returns null" )
	@Test.Decl( "Get on terminated empty returns null" )
	@Test.Decl( "Elements retrieved in FIFO order" )
	protected E getImpl() {
		return this.isEmpty() ? null : this.elements.removeFirst();
	}

	
}
