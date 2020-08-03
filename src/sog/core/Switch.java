/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.util.Map;
import java.util.HashMap;
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
public class Switch<T, A, R> {

	/** Holds the registered handlers */
	private Map<T, Function<? super A, ? extends R>> handlers;
	
	/** The default case */
	private Function<? super A, ? extends R> defaultHandler;
	
	/** Builds a switch object associated with the given type T. */
	public Switch() {
		this.handlers = new HashMap<T, Function<? super A, ? extends R>>();
		this.defaultHandler = null;
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
	@TestOrig.Decl( "Throws assertion error for null key" )
	@TestOrig.Decl( "Throws assertion error for null handler" )
	@TestOrig.Decl( "Returns non null" )
	@TestOrig.Decl( "Returns this Switch instance" )
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
	@TestOrig.Decl( "Throws assertion error for null handler" )
	@TestOrig.Decl( "Returns non null" )
	@TestOrig.Decl( "Returns this Switch instance" )
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
	@TestOrig.Decl( "Throws assertion error for null key" )
	@TestOrig.Decl( "Throws App exception when no handler found" )
	@TestOrig.Decl( "Throws App exception when handler raises exception" )
	public R apply( T key, A arg ) {
		Assert.nonNull( key );
		
		Function<? super A, ? extends R> handler = this.handlers.get( key );
		if ( handler == null ) {
			handler = this.defaultHandler;
		}
		
		if ( handler == null ) {
			Fatal.error( "No handler found" );
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
