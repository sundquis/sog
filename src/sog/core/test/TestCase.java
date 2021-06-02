/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.LinkedList;
import java.util.List;

import sog.core.Assert;
import sog.core.Objects;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.Test;
import sog.core.Test.Case;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestCase extends Result implements Test.Case, Comparable<TestCase>, Runnable {
	
		

	private final TestImpl impl;
	private final Test.Container container;
	private final List<String> messages = new LinkedList<String>();
	private Class<? extends Throwable> expectedError = null;
	private Procedure callAfter = Procedure.NOOP;
	private boolean passed = true;
	private String failLocation = null;
	private long elapsedTime = 0L;

	public TestCase( TestImpl impl, Test.Container container ) {
		super( Assert.nonNull( impl ).getDescription() );
		this.impl = Assert.nonNull( impl );
		this.container = Assert.nonNull( container );
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Case addMessage( String message ) {
		this.messages.add( Assert.nonEmpty( message ) );
		return this;
	}

	@Override
	public Case expectError( Class<? extends Throwable> expectedError ) {
		this.expectedError = Assert.nonNull( expectedError );
		return this;
	}

	@Override
	public Case afterThis( Procedure callafter ) {
		this.callAfter = Assert.nonNull( callAfter );
		return this;
	}

	@Override
	public Case fail() {
		this.failLocation = this.container.getFileLocation();
		this.passed = false;
		return this;
	}

	@Override
	public Case notNull( Object obj ) {
		if ( obj == null ) {
			this.addMessage( "Expected non-null object" );
		}
		return this.fail();
	}

	@Override
	public Case notEmpty( String s ) {
		if ( s == null || s.isEmpty() ) {
			this.addMessage( "Expected non-empty string" );
		}
		return this.fail();
	}

	@Override
	public Case isNull( Object obj ) {
		if ( obj != null ) {
			this.addMessage( "Expected null, got: " + obj  );
		}
		return this.fail();
	}

	@Override
	public Case assertTrue( boolean passIfTrue ) {
		if ( !passIfTrue ) {
			this.addMessage( "Expected true" );
		}
		return this.fail();
	}

	@Override
	public Case assertFalse( boolean passIfFalse ) {
		if ( passIfFalse ) {
			this.addMessage( "Expected false" );
		}
		return this.fail();
	}

	@Override
	public <T> Case assertEqual( T expected, T actual ) {
		if ( !Objects.shallowEquals( expected, actual ) ) {
			this.addMessage( "Expected: " + Strings.toString( expected ) + ", Got: " + Strings.toString( actual ) );
		}
		return this.fail();
	}

	@Override
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	@Override
	public int getPassCount() {
		return this.passed ? this.impl.getWeight() : 0;
	}

	@Override
	public int getFailCount() {
		return this.passed ? 0 : this.impl.getWeight();
	}


	@Override
	public void print( IndentWriter out ) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public int compareTo( TestCase o ) {
		// TODO Auto-generated method stub
		return 0;
	}



}
