/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.test;

import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.test.MemberResultOrig;

/**
 * @author sundquis
 *
 */
public class MemberResultTest implements TestContainer {

	@Override
	public Class<?> subjectClass() { return MemberResultOrig.class; }

	// Test implementations

	
	
	private MemberResultOrig instance;;
	
	public Procedure beforeEach() { return () -> this.instance = new MemberResultOrig( TestOrig.class ); }
	
	public Procedure afterEach() { return () -> instance = null; }


	@TestOrig.Impl( src = "public CaseResult MemberResult.addCase(Test.Decl)", desc = "Throws assertion error for null declaration" )
	public void addCase_ThrowsAssertionErrorForNullDeclaration( TestCase tc ) {
		tc.expectError( AssertionError.class );
		instance.addCase( null );
	}

	
	
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(MemberResultTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
