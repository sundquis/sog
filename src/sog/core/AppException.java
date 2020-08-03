/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

/**
 * @author sundquis
 * 
 * The base application exception type
 *
 * Depends on:
 * 		NONE
 */
@TestOrig.Skip
public class AppException extends RuntimeException {

	private static final long serialVersionUID = -2314875945481995828L;

	/** Constructs an exception with empty detail message. */
	public AppException() {
		super();
	}

	/** Constructs an exception with specified detail message. */
	public AppException( String msg ) {
		super( msg );
	}

	/** Constructs an exception with specified cause. */
	public AppException( Throwable cause ) {
		super( cause );
	}

	/** Constructs an exception with specified detail message and cause. */
	public AppException( String msg, Throwable cause ) {
		super( msg, cause );
	}
	
}