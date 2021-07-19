/**
 * Copyright (C) 2021
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */
package sog.core.test;

import java.lang.StackWalker.Option;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import sog.core.App;
import sog.core.Assert;
import sog.core.Objects;
import sog.core.Procedure;
import sog.core.Strings;
import sog.core.Test;
import sog.core.Test.Case;
import sog.util.IndentWriter;

/**
 * This class has two responsibilities. First, it implements the Test.Case interface and is
 * passed to test method implementations. Second, it extends Result and serves
 * as the holder of test results for this test case. The implementation of Comparable is
 * based on the priority, if defined, in the Test.Impl annotation. The Runnable.run()
 * method executes the test.
 */
@Test.Subject( "test." )
public class TestCase extends Result implements Test.Case, Comparable<TestCase>, Runnable {
	
	
	/***
	 * A TestCase is created in the OPEN state and remains OPEN until the PASS/FAIL status
	 * can be determined. This happens in Container code when one of the Tst.Case.assertXXXX() methods is
	 * called, directly signaling a pass or fail, or it can happen in TestCase.run() after
	 * Container code has called Test.Case.expectError().
	 */
	private enum State {
		OPEN { 
			boolean passed() { return false; } 
			State pass() { return PASS; }
			State fail() { return FAIL; }
		},
		PASS { 
			boolean passed() { return true; } 
			State pass() { return PASS; }
			State fail() { return FAIL; }
		},
		FAIL { 
			boolean passed() { return false; } 
			State pass() { return FAIL; }
			State fail() { return FAIL; }
		};
		
		abstract boolean passed();
		abstract State pass();
		abstract State fail();
	}

	
	/* 
	 * Holds the Method and its corresponding Test.Impl annotation. The label associated
	 * with this test case is taken from the TestImpl member and description.
	 */
	private final TestImpl impl;
	
	/* The container instance that holds the test Method. */
	private final Test.Container container;
	
	/*
	 * When test failure occurs a message is added to this list. Messages are printed
	 * if Printable.print() is called to give details.
	 */
	private final List<String> messages = new LinkedList<String>();
	
	/*
	 * Test implementations that need to trigger an exception can register the expected
	 * type of exception that is thrown. When an exception is encountered, the type is
	 * checked against this registered type.
	 */
	private Class<? extends Throwable> expectedError = null;
	
	/* If an unexpected error occurs it is saved for Printable.print() details. */
	private Throwable unexpectedError = null;
	
	/* Guaranteed to be executed after the Method runs. */
	private Procedure afterThis = Procedure.NOOP;
	
	/*
	 * A TestCase is created with indeterminate State, indicating that the test method has
	 * not called pass() or fail(...).
	 * 
	 * When fail() is called the state transitions to FAIL regardless of the current state.
	 * 
	 * When pass() is called, if the current state is not FAIL (state is INDETERMINATE or PASS)
	 * then the new stat is PASS. If the current state is FAIL, the call to pass() is ignored.
	 */
	private State state = State.OPEN;
	
	
	/* 
	 * A printable link to the file location of this test case. Filled in by setFileLocation 
	 * when expectError or one of the assert methods is called.
	 */
	private String fileLocation = null;
	
	/* Total execution time for the test method and test framework monitoring. */
	private long elapsedTime = 0L;

	
	@Test.Decl( "Throws AssertionError for null TestImpl" )
	@Test.Decl( "Throws AssertionError for null Test.Container" )
	@Test.Decl( "Marked as passed at creation" )
	@Test.Decl( "Label includes member name and description" )
	@Test.Decl( "Created with OPEN State" )
	public TestCase( TestImpl impl, Test.Container container ) {
		super( Assert.nonNull( impl ).toString() );
		this.impl = impl;
		this.container = Assert.nonNull( container );
	}
	
	
	/**
	 * Downcast this TestCase as a Test.Case for passing to the test Method.
	 * @return
	 */
	@Test.Decl( "Physically equal to this" )
	public Test.Case getTestCase() {
		return this;
	}
	
	
	/**
	 * Executes the test Method and monitors the results.
	 * 
	 * NOTE: Should not be run by multi-threaded workers since Test.Container implementations
	 * generally will not be thread-safe. BUT: Could be run in a separate thread and monitored 
	 * for timely termination. In that case the implementation should consult Test.Impl.timeout.
	 */
	@Override
	@Test.Decl( "Marks file location of test method" )
	@Test.Decl( "Graceful exit on an expected error" )
	@Test.Decl( "Graceful exit on an unexpected error" )
	@Test.Decl( "Graceful exit on an post processing error" )
	@Test.Decl( "Test fails if expected error is not thrown" )
	@Test.Decl( "Test passes if expected error is thrown" )
	@Test.Decl( "Test fails if unexpected error is thrown" )
	@Test.Decl( "Test fails if wrong error is thrown" )
	@Test.Decl( "Message added for no exception when expected" )
	@Test.Decl( "Message added for wrong exception when expected" )
	@Test.Decl( "Message added for unexpected exception" )
	@Test.Decl( "Message added for exception in afterThis" )
	@Test.Decl( "Message added for exception in afterEach" )
	@Test.Decl( "afterThis always executed" )
	@Test.Decl( "afterEach always executed" )
	@Test.Decl( "Test fails if afterThis throws exception" )
	@Test.Decl( "Test fails if afterEach throws exception" )
	@Test.Decl( "elapsedTime greater than zero" )
	public void run() {
		long start = System.currentTimeMillis();
		try {
			this.container.beforeEach().exec();
			this.impl.getMethod().invoke( this.container, this.getTestCase() );
			if ( this.expectedError != null ) {
				this.fail( "No exception thrown, expected " + this.expectedError );
			} 
		} catch ( InvocationTargetException ex ) {
			if ( this.expectedError == null || !this.expectedError.equals( ex.getCause().getClass() ) ) {
				this.unexpectedError = ex;
				this.fail( "Expected " + this.expectedError + " but got " + ex.getCause() );
			} else {
				this.pass();
			}
		} catch ( Throwable err ) {
			this.unexpectedError = err;
			this.fail( "Unexpected error" );
		} finally {
			try {
				this.afterThis.exec();
			} catch ( Throwable err ) {
				this.unexpectedError = err;
				this.fail( "Exception in afterThis." );
			}
			try {
				this.container.afterEach().exec();
			} catch ( Throwable err ) {
				this.unexpectedError = err;
				this.fail( "Exception in afterEach." );
			}
			this.elapsedTime = System.currentTimeMillis() - start;
		}
	}


	/**
	 * When called from a test method in a Test.Container this method will store a printable
	 * link to the location of the current test case.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	public Test.Case setFileLocation() {
		if ( this.fileLocation == null ) {
			Predicate<StackWalker.StackFrame> sfp = 
				sf -> Test.Container.class.isAssignableFrom( sf.getDeclaringClass() );
			Function<StackWalker.StackFrame, String> sfm = 
				sf -> new App.Location( sf ).toString();
			this.fileLocation = StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
				s -> s.filter( sfp ).map( sfm ).findFirst().orElse( null )
			);
		}
		
		return this;
	}
	
	
	/**
	 * Only displayed for non-passing cases
	 * 
	 * @param message
	 * 		Failure message
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Throws AssertionError for empty message" )
	@Test.Decl( "Throws AssertionError for null message" )
	@Test.Decl( "Message is included in details." )
	@Test.Decl( "Return is this" )
	@Test.Decl( "Does not alter State" )
	public Case addMessage( String message ) {
		this.messages.add( Assert.nonEmpty( message ) );
		return this;
	}

	/**
	 * Type of Throwable that subsequent code is expected to throw
	 * 
	 * @param expectedError
	 * 		Throwable type
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Throws AssertionError for null error" )
	@Test.Decl( "Return is this" )
	@Test.Decl( "Does not alter State" )
	@Test.Decl( "Throws  AssertionError if expected error already set" )
	public Case expectError( Class<? extends Throwable> expectedError ) {
		if ( this.expectedError != null ) {
			this.fail( "expectedError already set" + this.expectedError );
		}
		this.expectedError = Assert.nonNull( expectedError );
		return this.setFileLocation();
	}

	/**
	 * Procedure to call after the current method (in a Test.Container) completes
	 * Will be called even if the method throws an exception.
	 * 
	 * @param callafter
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Throws AssertionError for null procedure" )
	@Test.Decl( "Default is NOOP" )
	@Test.Decl( "Return is this" )
	public Case afterThis( Procedure afterThis ) {
		this.afterThis = Assert.nonNull( afterThis );
		return this;
	}

	

	/**
	 * Mark the current case as failed.
	 * 
	 * If a test has multiple parts, the failure of any single part means the Test.Case fails.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	@Test.Decl( "The message must not be empty" )
	@Test.Decl( "The message must not be null" )
	@Test.Decl( "Marks passed case as failed" )
	@Test.Decl( "Marks failed case as failed" )
	@Test.Decl( "Marks indeterminate case as failed" )
	@Test.Decl( "Marks location of failure" )
	@Test.Decl( "Return is this" )
	public Test.Case fail( String message ) {
		this.state = this.state.fail();
		this.addMessage( message );
		return this;
	}
	

	/**
	 * Mark the current case as passed.
	 * 
	 * If a test has multiple parts, all parts must pass for the Test.Case to pass.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	@Test.Decl( "If old State is INDETERMINATE then new state is PASS" )
	@Test.Decl( "If old State is PASS then new state is PASS" )
	@Test.Decl( "If old State is FAIL then new state is FAIL" )
	@Test.Decl( "Return is this" )
	public Test.Case pass() {
		this.state = this.state.pass();
		return this;
	}
	

	/**
	 * Assert that the given object is not null.
	 * 
	 * @param obj
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Case fails for null" )
	@Test.Decl( "Case passes for non-null" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public Case notNull( Object obj ) {
		if ( obj != null ) {
			this.pass();
		} else {
			this.fail( "Expected non-null object" );
		}
		return this.setFileLocation();
	}

	/**
	 * Assert that the given string is non-null and not empty
	 * 
	 * @param s
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Case fails for null string" )
	@Test.Decl( "Case fails for empty string" )
	@Test.Decl( "Case passes for non-empty string" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public Case notEmpty( String s ) {
		if ( s == null || s.isEmpty() ) {
			this.fail( "Expected non-empty string" );
		} else {
			this.pass();
		}
		return this.setFileLocation();
	}

	/**
	 * Assert that the given object is not null.
	 * 
	 * @param obj
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Case passes for null object" )
	@Test.Decl( "Case fails for non-null object" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public Case isNull( Object obj ) {
		if ( obj == null ) {
			this.pass();
		} else {
			this.fail( "Expected null, got: " + Strings.toString( obj ) );
		}
		return this.setFileLocation();
	}

	/**
	 * Conditionally mark the case as passed/failed.
	 *  
	 * @param passIfTrue
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Case passes for true" )
	@Test.Decl( "Case fails for false" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public Case assertTrue( boolean passIfTrue ) {
		if ( passIfTrue ) {
			this.pass();
		} else {
			this.fail( "Expected true" );
		}
		return this.setFileLocation();
	}

	/**
	 * Conditionally mark the case as passed/failed.
	 *  
	 * @param passIfFalse
	 * @return
	 * 		this TestCase
	 */
	@Override
	@Test.Decl( "Case fails for true" )
	@Test.Decl( "Case passes for false" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public Case assertFalse( boolean passIfFalse ) {
		if ( passIfFalse ) {
			this.fail( "Expected false" );
		} else {
			this.pass();
		}
		return this.setFileLocation();
	}

	/**
	 * Test for equality using Objobject.equals().
	 * If T is a compound type (array or collection) then the components are shallowly tested.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Test passes for equivalent objects" )
	@Test.Decl( "Test fails for inequivalent" )
	@Test.Decl( "Message added on failure" )
	@Test.Decl( "Return is this" )
	public <T> Case assertEqual( T expected, T actual ) {
		if ( Objects.shallowEquals( expected, actual ) ) {
			this.pass();
		} else {
			this.fail( "Expected: " + Strings.toString( expected ) + ", Got: " + Strings.toString( actual ) );
		}
		return this.setFileLocation();
	}

	/** Total execution time for Method and framework monitoring. */
	@Override
	@Test.Decl( "Elapsed time is consistent with execution time" )
	@Test.Decl( "Set if case fails" )
	@Test.Decl( "Set after expected exception" )
	@Test.Decl( "Set after unexpected exception" )
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	/** The total weight of all components if the case passed, otherwise zero. */
	@Override
	@Test.Decl( "Return is zero when State is INDETERMINATE" )
	@Test.Decl( "Return is zero when State is FAIL" )
	@Test.Decl( "Return is Test.Impl.weight when State is PASS" )
	public int getPassCount() {
		return this.state.passed() ? this.impl.getWeight() : 0;
	}

	/** The total weight of all components if the case failed, otherwise zero. */
	@Override
	@Test.Decl( "Zero if case passes" )
	@Test.Decl( "Equal to Test.Impl.weight if case fails" )
	public int getFailCount() {
		return this.state.passed() ? 0 : this.impl.getWeight();
	}
	

	/**
	 * Extending Result.toString() to add PASS/FAIL keyword.
	 */
	@Override
	@Test.Decl( "Starts with PASS if passed" )
	@Test.Decl( "Starts with FAIL if failed" )
	@Test.Decl( "Statrs with OPEN if pass/fail is unknown" )
	public String toString() {
		return this.state + ": " + super.toString();
	}


	/** 
	 * Implementations first print this instance, then indent for details. 
	 */
	@Override
	@Test.Decl( "Prints summary line" )
	@Test.Decl( "Includes file location on failure" )
	@Test.Decl( "Includes messages on failure" )
	@Test.Decl( "Includes error information on failure" )
	public void print( IndentWriter out ) {
		out.println( this.toString() );
		
		out.increaseIndent();
		
		if ( !this.state.passed() ) {
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
	@Test.Decl( "Respects Test.Impl.priority" )
	@Test.Decl( "For equal priority ordered by member" )
	@Test.Decl( "For equal priority and member ordered by description" )
	public int compareTo( TestCase other ) {
		Assert.nonNull( other );
		
		int result = this.impl.getPriority() - other.impl.getPriority();
		if ( result == 0 ) {
			result = this.impl.toString().compareTo( other.impl.toString() );
		}
		
		return result;
	}
	
	
	@Override
	@Test.Decl( "If compareTo not zero then not equal" )
	@Test.Decl( "If compareTo zero then equal" )
	public boolean equals( Object other ) {
		if ( other == null || !(other instanceof TestCase) ) {
			return false;
		}
		
		return this.compareTo( (TestCase) other ) == 0;
	}
	
	
	@Override
	@Test.Decl( "If equal then same hashCode" )
	public int hashCode() {
		return this.impl.toString().hashCode();
	}

}
