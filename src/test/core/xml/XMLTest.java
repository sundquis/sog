/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.xml;


import sog.core.Test;
import sog.core.xml.XML;

/**
 * @author sundquis
 *
 */
public class XMLTest extends Test.Implementation {

	// Test implementations
	
	@Test.Impl( member = "public String XML.getDeclaration()", description = "Not empty" )
	public void getDeclaration_NotEmpty( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().length() > 0 );
	}

	@Test.Impl( member = "public XML XML.get()", description = "Is not null" )
	public void get_IsNotNull( Test.Case tc ) {
		tc.notNull( XML.get() );
	}

	@Test.Impl( member = "public String XML.getDeclaration()", description = "starts correct" )
	public void getDeclaration_StartsCorrect( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().startsWith( "<?xml" ) );
	}
	
}
