/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.Test;
import sog.core.TestContainer;
import sog.util.XStream;

/**
 * @author sundquis
 *
 */
public class XStreamTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return XStream.class;
	}

	// Test implementations
	
	

	public static void main(String[] args) {

		System.out.println();

		Test.verbose();
		new Test(XStreamTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
