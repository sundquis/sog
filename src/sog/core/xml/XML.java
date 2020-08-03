/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.xml;


import sog.core.Assert;
import sog.core.Property;
import sog.core.TestOrig;

/**
 * Static help with xml
 */
public class XML {

	private static XML instance = null;

	/** Retrieve the singleton instance */
	@TestOrig.Decl( "Is not null" )
	public static XML get() {
		if ( XML.instance == null ) {
			synchronized ( XML.class ) {
				if ( XML.instance == null ) {
					XML.instance = new XML();
				}
			}
		}
		
		return Assert.nonNull( XML.instance );
	}
	
	private final String declaration;
	
	private XML() {
		this.declaration = Property.getText( "declaration" );
	}
	
	@TestOrig.Decl( "Not empty" )
	@TestOrig.Decl( "starts correct" )
	public String getDeclaration() {
		return this.declaration;
	}

	
}
