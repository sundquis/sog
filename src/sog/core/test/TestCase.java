/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import sog.core.App;
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
	private Throwable unexpectedError = null;
	private Procedure afterThis = Procedure.NOOP;
	private boolean passed = true;
	private String fileLocation = null;
	private long elapsedTime = 0L;

	public TestCase( TestImpl impl, Test.Container container ) {
		super( Assert.nonNull( impl ).getDescription() );
		this.impl = Assert.nonNull( impl );
		this.container = Assert.nonNull( container );
	}
	
	
	
	private Test.Case getTestCase() {
		return this;
	}
	
	
	/*
	 * NOTE: Should not be run by multi-threaded workers since Test.Container implementations
	 * generally will not be thread-safe.
	 * BUT: Could be run in a separate thread and monitored for timely termination. In that
	 * case the implementation should consult Test.Impl.timeout.
	 */
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			this.container.beforeEach().exec();
			this.impl.getMethod().invoke( this.container, this.getTestCase() );
			if ( this.expectedError != null ) {
				this.addMessage( "No exception thrown, expected " + this.expectedError );
				this.fail();
			}
		} catch ( InvocationTargetException ex ) {
			if ( this.expectedError == null || !this.expectedError.equals( ex.getCause().getCause() ) ) {
				this.addMessage( "Expected " + this.expectedError + " but got " + ex.getCause() );
				this.unexpectedError = ex;
				this.fail();
			}
		} catch ( Throwable err ) {
			this.addMessage( "Unexpected error" );
			this.unexpectedError = err;
			this.fail();
		} finally {
			this.afterThis.exec();
			this.container.afterEach().exec();
			this.elapsedTime = System.currentTimeMillis() - start;
		}
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
	public Case afterThis( Procedure afterThis ) {
		this.afterThis = Assert.nonNull( afterThis );
		return this;
	}

	@Override
	public Case fail() {
		this.fileLocation = this.container.getFileLocation();
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
			this.addMessage( "Expected null, got: " + Strings.toString( obj ) );
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
	public String toString() {
		return (this.passed ? "PASS: " : "FAIL: ") + super.toString();
	}


	@Override
	public void print( IndentWriter out ) {
		out.println( this.toString() );
		
		out.increaseIndent();
		
		if ( this.fileLocation != null ) {
			out.println( "Location: " + this.fileLocation );
		}
		
		if ( this.messages.size() > 0 ) {
			out.println( "Messages:" );
			out.increaseIndent();
			this.messages.forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.unexpectedError != null ) {
			out.println( "Error:" + this.unexpectedError );
			out.increaseIndent();
			App.get().getLocation( this.unexpectedError ).forEach( out::println );
			out.decreaseIndent();
		}
		
		out.decreaseIndent();
	}
	
	
	@Override
	public int compareTo( TestCase other ) {
		Assert.nonNull( other );
		
		int result = this.impl.getPriority() - other.impl.getPriority();
		if ( result == 0 ) {
			result = this.impl.toString().compareTo( other.impl.toString() );
		}
		
		return result;
	}
	
	
	@Override
	public boolean equals( Object other ) {
		if ( other == null || !(other instanceof TestCase) ) {
			return false;
		}
		
		return this.compareTo( (TestCase) other ) == 0;
	}
	
	
	@Override
	public int hashCode() {
		return this.impl.toString().hashCode();
	}



}
