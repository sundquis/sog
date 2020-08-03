/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;


import sog.core.Assert;
import sog.core.TestOrig;

/**
 * @author sundquis
 *
 * Class containing the algorithm for generating integer ids from fully
 * qualified string names.
 * 
 * Usage:
 * 		ID.get( name )
 * 
 */
public class IDProvider {
	
	private IDProvider() {}

	/**
	 * Based on the algorithm in java.lang.String.hashCode()
	 * A very good chance to get distinct integers for distinct names.
	 * 
	 * @param name
	 * 		Fully qualified, non-empty string name of an entity
	 * @return
	 * 		Integer encoding of name. 
	 */
	@TestOrig.Decl( "Throws assertion error for empty name" )
	@TestOrig.Decl( "Short names distinct" )
	@TestOrig.Decl( "Case sensitive" )
	@TestOrig.Decl( "No collision dict test" )
	@TestOrig.Decl( "Qualified name stress test" )
	@TestOrig.Decl( "Long strings have id" )
	public static int get( String name ) {
		Assert.nonEmpty( name ) ;
		
		int id = 0;
		for ( int i = 0; i < name.length(); i++ ) {
			// String algorithm uses *= 31, Many length 2 strings collide
			// Many collisions with *= 255
			// Hilarious! With *= 127 get collision on "milfs" and "Brno's"
			// += i resolves this collision
			id *= 127;
			id += i;
			id += name.charAt(i);
		}
		
		return id;
	}
	
	
}
