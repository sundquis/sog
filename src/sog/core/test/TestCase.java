/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.Test.Case;

/**
 * 
 */
public class TestCase implements Test.Case {

	/**
	 * 
	 */
	public TestCase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Case addMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case expectError(Class<? extends Throwable> expectedError) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case afterThis(Procedure callafter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case fail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case pass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case notNull(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case notEmpty(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case isNull(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case assertTrue(boolean passIfTrue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case assertFalse(boolean passIfFalse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Case assertEqual(T expected, T actual) {
		// TODO Auto-generated method stub
		return null;
	}

}
