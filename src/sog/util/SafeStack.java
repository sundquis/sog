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

import java.util.Stack;
import java.util.function.Supplier;

import sog.core.Test;

/**
 * @author sundquis
 *
 */
@Test.Subject( "test." )
public class SafeStack<E> {

	private final Stack<E> stack;
	private final Supplier<E> emptySupplier;
	
	/**
	 * Not thread safe
	 */
	@Test.Decl( "Throws AssertionError for null supplier" )
	public SafeStack( Supplier<E> emptySupplier ) {
		this.stack = new Stack<>();
		this.emptySupplier = emptySupplier;
	}
	
	/**
	 * Return null when empty
	 */
	@Test.Decl( "Default returns null when empty" )
	public SafeStack() {
		this( () -> null );
	}
	
	/**
	 * No previously stored values available.
	 * Sources, pop and peek, would return value obtained from the Supplier
	 */
	@Test.Decl( "True when constructed" )
	@Test.Decl( "False after push" )
	@Test.Decl( "False after replace" )
	public boolean isEmpty() {
		return this.stack.empty();
	}

	/**
	 * Throw away any previously stored values
	 */
	@Test.Decl( "Stack is empty after" )
	@Test.Decl( "Previously added elements are not available" )
	public void clear() {
		this.stack.clear();
	}
	
	/**
	 * Conservative sink
	 */
	@Test.Decl( "Null element allowed" )
	@Test.Decl( "New element retained" )
	@Test.Decl( "Previously added element retained" )
	public void push( E e ) {
		this.stack.push( e );
	}
	
	/**
	 * Destructive sink
	 */
	@Test.Decl( "Null element allowed" )
	@Test.Decl( "New element retained" )
	@Test.Decl( "Previously added element not retained" )
	public void replace( E e ) {
		this.pop();
		this.stack.push( e );
	}
	
	/**
	 * Conservative source
	 */
	@Test.Decl( "Configured supplier used before elements have been added" )
	@Test.Decl( "Configured supplier used when empty" )
	@Test.Decl( "Previously added element returned" )
	@Test.Decl( "Previously added element retained" )
	public E peek() {
		return this.isEmpty() ? this.emptySupplier.get() : this.stack.peek();
	}
	
	/**
	 * Destructive source
	 */
	@Test.Decl( "Configured supplier used before elements have been added" )
	@Test.Decl( "Configured supplier used when empty" )
	@Test.Decl( "Previously added element returned" )
	@Test.Decl( "Previously added element not retained" )
	public E pop() {
		return this.isEmpty() ? this.emptySupplier.get() : this.stack.pop();
	}

}
