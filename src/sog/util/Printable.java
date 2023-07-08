/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import sog.core.Test;

/**
 * @author sundquis
 *
 */
@FunctionalInterface
@Test.Subject( "test." )
public interface Printable {

	/**
	 * Pretty print on the given IndentWriter
	 * 
	 * Write immediate state
	 * For nested Printable components,
	 * 		Increase indent
	 * 		print nested components
	 * 		Decrease indent
	 * 
	 * @param out
	 * @param showDetails
	 */
	public void print( IndentWriter out );

	@Test.Decl( "Default uses System.out" )
	default public void print() {
		this.print( new IndentWriter() );
	}

	
}
