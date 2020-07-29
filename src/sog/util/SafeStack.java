/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.util.Stack;
import java.util.function.Supplier;

/**
 * @author sundquis
 *
 */
public class SafeStack<E> {

	private final Stack<E> stack;
	private final Supplier<E> emptySupplier;
	
	/**
	 * Not thread safe
	 */
	public SafeStack( Supplier<E> emptySupplier ) {
		this.stack = new Stack<>();
		this.emptySupplier = emptySupplier;
	}
	
	/**
	 * Return null when empty
	 */
	public SafeStack() {
		this( () -> null );
	}
	
	/**
	 * No previously stored values available.
	 * Sources, pop and peek, would return value obtained from the Supplier
	 */
	public boolean isEmpty() {
		return this.stack.empty();
	}

	/**
	 * Throw away any previously stored values
	 */
	public void clear() {
		this.stack.clear();
	}
	
	/**
	 * Conservative sink
	 */
	public void push( E e ) {
		this.stack.push( e );
	}
	
	/**
	 * Destructive sink
	 */
	public void replace( E e ) {
		this.pop();
		this.stack.push( e );
	}
	
	/**
	 * Conservative source
	 */
	public E peek() {
		return this.isEmpty() ? this.emptySupplier.get() : this.stack.peek();
	}
	
	/**
	 * Destructive source
	 */
	public E pop() {
		return this.isEmpty() ? this.emptySupplier.get() : this.stack.pop();
	}

}
