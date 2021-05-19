/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;


/**
 * @author sundquis
 *
 */
@FunctionalInterface
public interface Printable {

	/**
	 * Pretty print in the given IndentWriter
	 * 
	 * Write immediate state
	 * For nested Printable components,
	 * 		Increase indent
	 * 		print nested components
	 * 		Decrease indent
	 * @param out
	 */
	public void print( IndentWriter out );
	
}
