/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.util.List;

/**
 * @author sundquis
 *
 */
@FunctionalInterface
public interface Procedure {

	/**
	 * No arguments, no return, no Exception.
	 */
	public void exec();
	
	public static final Procedure NOOP = new Procedure() { public void exec() {} };

	/**
	 * Form a composite {@code Procedure} that first executes this {@code Procedure} and then
	 * executes the given other {@code Procedure}.
	 *  
	 * @param after
	 * @return
	 */
	default public Procedure andThen( Procedure after ) {
		return () -> { this.exec(); after.exec(); };
	}
	
	/**
	 * Form a composite {@code Procedure} that first executes this {@code Procedure} and then
	 * executes the given list of additional {@code Procedure}s.
	 *  
	 * @param after
	 * @return
	 */
	default public Procedure andThen( List<Procedure> more ) {
		return () -> { this.exec(); more.forEach( Procedure::exec ); };
	}
	
}
