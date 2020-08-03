/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 */

package sog.util;

import java.util.LinkedList;

import sog.core.TestOrig;

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
@TestOrig.Decl( "Elements retrieved in FIFO order" )
public class FifoQueue<E> extends AbstractQueue<E> {


	private LinkedList<E> elements;

	/**
	 * Constructs an empty FIFO queue. The queue is open and accepting input.
	 */
	@TestOrig.Decl( "FifoQueues are created empty" )
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
	@TestOrig.Decl( "Put on empty is not empty" )
	@TestOrig.Decl( "Put on non empty is not empty" )
	@TestOrig.Decl( "Put then get on empty is empty" )
	@TestOrig.Decl( "Put then put then get is not empty" )
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	@TestOrig.Decl( "Put on open is accepted" )
	@TestOrig.Decl( "Put on closed is ignored" )
	@TestOrig.Decl( "Put on terminated is ignored" )
	protected boolean putImpl( E elt ) {
		this.elements.addLast( elt );
		return true;
	}


	@Override
	@TestOrig.Decl( "Get on open non empty returns non null" )
	@TestOrig.Decl( "Get on open empty returns null" )
	@TestOrig.Decl( "Get on closed non empty returns non null" )
	@TestOrig.Decl( "Get on closed empty returns null" )
	@TestOrig.Decl( "Get on terminated non empty returns null" )
	@TestOrig.Decl( "Get on terminated empty returns null" )
	protected E getImpl() {
		return this.isEmpty() ? null : this.elements.removeFirst();
	}

	
}
