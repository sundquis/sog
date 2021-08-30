/**
 * Copyright (C) 2021
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

package sog.core;

import java.util.Map;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Used to implement switch behavior for arbitrary types, especially
 * enumerated types.
 *
 * <p>
 * A typical use looks like:
 * <pre><tt>
 *  public class MyClass {
 *      public final enum State {
 *          OPEN,
 *          CLOSED
 *      }
 *
 *      private myState = State.OPEN;
 *
 *      private Switch reactor = new Switch()
 *          .addCase( State.OPEN, (x) -> f(x) )
 *          .addCase( State.CLOSED, (x) -> g(x) )
 *          .addDefault( (x) -> h(x) );
 *
 *      void someMethod() {
 *          ...
 *          Object result = reactor.apply( myState, myArg );
 *          ...
 *      }
 *  }
 * </tt></pre>
 * 
 * Three type parameters:
 * 	T	Type to switch on
 * 	A	Argument type for Function.apply
 * 	R	Result type for Function.apply
 * 
 * Uses equals for comparisons
 */
@Test.Subject( "test." )
public class Switch<T, A, R> implements BiFunction<T, A, R> {
	
	/** Holds the registered handlers */
	private final Map<T, Function<? super A, ? extends R>> handlers;
	
	/** The default case */
	private Function<? super A, ? extends R> defaultHandler;
	
	/** Builds a switch object associated with the given type T. */
	@Test.Decl( "Default handler returns null" )
	public Switch() {
		this.handlers = new HashMap<T, Function<? super A, ? extends R>>();
		this.defaultHandler = a -> { return null; };
	}

	/**
	 * Add a case to this switch object. Proper behavior depends on the
	 * implementation of equals on the key objects. This case object is
	 * returned to allow chaining of addCase() methods.
	 *
	 * @param key
	 *        A non-null object of type T
	 *
	 * @param handler
	 *        A non-null implementation of the apply method
	 */
	@Test.Decl( "Throws AssertionError for null key" )
	@Test.Decl( "Throws AssertionError for null handler" )
	@Test.Decl( "Returns this Switch instance" )
	@Test.Decl( "Replaces previously added case" )
	public Switch<T, A, R> addCase( T key, Function<? super A, ? extends R> handler ) {
		Assert.nonNull( key );
		Assert.nonNull( handler );

		this.handlers.put( key, handler );

		return this;
	}

	/**
	 * Add a default handler to this switch object. If apply is called with an
	 * object that does not have an associated handler function this default handler is
	 * called. The return is this Case object to allow chaining with
	 * addCase() methods.
	 *
	 * @param handler
	 *        The non-null handler for the default case.
	 */
	@Test.Decl( "Throws AssertionError for null handler" )
	@Test.Decl( "Returns this Switch instance" )
	@Test.Decl( "Replaces previously set default" )
	public Switch<T, A, R> addDefault( Function<? super A, ? extends R> handler ) {
		this.defaultHandler = Assert.nonNull( handler );

		return this;
	}

	/**
	 * Called by clients to execute the corresponding apply operation.
	 *
	 * @param key
	 *		The switch value, which must be a non-null member of type T
	 *
	 * @param arg
	 *		Passed to the handler's apply method
	 *
	 * @return
	 * 		The result of the corresponding apply method
	 */
	@Override
	@Test.Decl( "Throws AssertionError for null key" )
	@Test.Decl( "Throws AppException when handler raises exception" )
	public R apply( T key, A arg ) {
		Assert.nonNull( key );
		
		Function<? super A, ? extends R> handler = this.handlers.get( key );
		if ( handler == null ) {
			handler = this.defaultHandler;
		}
		
		R result = null;
		try {
			result = handler.apply( arg );
		} catch ( Throwable e ) {
			throw new AppException( "Exception in case handler for Switch on " + key.getClass(), e );
		}

		return result;
	}
		
	
}
