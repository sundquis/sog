/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package test.core.test;

import sog.core.test.Container;
import sog.core.Test;

/**
 * 
 */
public class TestResult extends Container {

	/**
	 * 
	 */
	public TestResult() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<?> subjectClass() {
		// TODO Auto-generated method stub
		return sog.core.test.TestResult.class;
	}
	
	@Test.Impl( description = "Description for an orphan", member = "Some lost member" )
	public void someMethod( String s ) {
		
	}

}
