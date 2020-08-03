/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.xml;


import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.xml.XML;

/**
 * @author sundquis
 *
 */
public class XMLTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return XML.class;
	}

	// Test implementations
	
	@TestOrig.Impl( src = "public String XML.getDeclaration()", desc = "Not empty" )
	public void getDeclaration_NotEmpty( TestCase tc ) {
		tc.assertTrue( XML.get().getDeclaration().length() > 0 );
	}

	@TestOrig.Impl( src = "public XML XML.get()", desc = "Is not null" )
	public void get_IsNotNull( TestCase tc ) {
		tc.notNull( XML.get() );
	}

	@TestOrig.Impl( src = "public String XML.getDeclaration()", desc = "starts correct" )
	public void getDeclaration_StartsCorrect( TestCase tc ) {
		tc.assertTrue( XML.get().getDeclaration().startsWith( "<?xml" ) );
	}
	

	public static void main(String[] args) {

		System.out.println();

		new TestOrig(XMLTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
