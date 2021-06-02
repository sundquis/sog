/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Assert;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.Test.Case;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestCase extends Result implements Test.Case, Comparable<TestCase>, Runnable {
	
		

	private final TestImpl impl;
	private final Test.Container container;

	public TestCase( TestImpl impl, Test.Container container ) {
		super( Assert.nonNull( impl ).getDescription() );
		this.impl = Assert.nonNull( impl );
		this.container = Assert.nonNull( container );
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo( TestCase o ) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getElapsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void incElapsedTime( long time ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPassCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void incPassCount( int pass ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFailCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void incFailCount( int fail ) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void print( IndentWriter out ) {
		// TODO Auto-generated method stub
		
	}

}
