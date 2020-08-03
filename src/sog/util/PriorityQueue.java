/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 */

package sog.util;

import java.util.SortedSet;
import java.util.TreeSet;

import sog.core.Assert;
import sog.core.TestOrig;


/**
 * Implements Queue behavior. The element returned by <code>get</code>
 * is determined by the priority ranking of elements currently in the queue,
 * which in turn is derived from the <code>Comparable</code> order of
 * elements.
 *
 * <p>
 * <b>Note:</b> This implies that all elements added to a priority queue
 * must implement <code>Comparable</code> and in fact must all be
 * mutually comparable.
 *
 * <p>
 * <b>Important:</b> When implementing <code>Comparable</code> it is required
 * that the order be <i>total</i> and consistent with <tt>equals</tt>.
 * 
 * 
 * Queue Implementation:
 * 	1. Determine behavior for put( E elt ) when the queue is open and full
 * 		No full property.
 * 
 * 	2. Determine behavior for E get() when the queue is open and empty
 * 		Return null if open and empty.
 * 
 * 	3. Determine the order retrieval policy
 * 		Smallest element with respect to the Comparable property
 * 
 */
@TestOrig.Decl( "Elements retrieved in comparable order" )
public class PriorityQueue<E extends Comparable<E>> extends AbstractQueue<E> {


	private SortedSet<E> elements;

	/**
	 * Constructs an empty priority queue. The queue is open and
	 * accepting input.
	 */
	@TestOrig.Decl( "Can be created empty" )
	public PriorityQueue() {
		this.elements = new TreeSet<E>();
	}

	/**
	 * Constructs a priority queue containing the elements of the given
	 * TreeSet. Note: Operations on the set will affect
	 * the behavior of the queue. The queue is open and accepting input.
	 *
	 * @param elements
	 *      A sorted set of elements. The minimal element( with respect to
	 *      the implementation of <code>Comparable</code> ) is considered the
	 *      "head" of the queue and will be the first to be produced by a
	 *      call to <code>get</code>.
	 */
	@TestOrig.Decl( "Can be created non empty" )
	public PriorityQueue( SortedSet<E> elements ) {
		this.elements = Assert.nonNull( elements );
	}

	@Override
	@TestOrig.Decl( "Put on open is accepted" )
	@TestOrig.Decl( "Put on closed is ignored" )
	@TestOrig.Decl( "Put on terminated is ignored" )
	protected boolean putImpl( E elt ) {
		return this.elements.add( elt );
	}

	@Override
	@TestOrig.Decl( "Get on open non empty returns non null" )
	@TestOrig.Decl( "Get on open empty returns null" )
	@TestOrig.Decl( "Get on closed non empty returns non null" )
	@TestOrig.Decl( "Get on closed empty returns null" )
	@TestOrig.Decl( "Get on terminated non empty returns null" )
	@TestOrig.Decl( "Get on terminated empty returns null" )
	protected E getImpl() {
		if ( this.isEmpty() ) {
			return null;
		}
		
		E result = this.elements.first();
		this.elements.remove( result );
		return result;
	}

	@Override
	@TestOrig.Decl( "Put on empty is not empty" )
	@TestOrig.Decl( "Put on non empty is not empty" )
	@TestOrig.Decl( "Put then get on empty is empty" )
	@TestOrig.Decl( "Put then put then get is not empty" )
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}
	
	
}
