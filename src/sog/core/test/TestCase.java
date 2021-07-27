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
	 * can be determined. This happens in Container code when one of the Test.Case.assertXXXX() methods is
	 * called, directly signaling a pass or fail, or it can happen in TestCase.run() after
	 * Container code has called Test.Case.expectError().
	 * 
	 * When detailed results are displayed using Printable.print(...), the name of the State is used
	 * to tag the toString() result.
	 * 
	 * The Test.Case is considered to have passed if and only if the ending State is PASS.
	 * 
	 * The allowed State transitions are:
	 * 		After State.fail(), the new State must be FAIL.
	 * 		After State.pass(), if the old State was FAIL it remains FAIL, otherwise the new State is PASS.
	 */
	public enum State {
		@Test.Decl( "OPEN cases are test fails" )
		OPEN { 
			public boolean passed() { return false; } 
			public State pass() { return PASS; }
			public State fail() { return FAIL; }
		},

		@Test.Decl( "PASS cases are test passes" )
		PASS { 
			public boolean passed() { return true; } 
			public State pass() { return PASS; }
			public State fail() { return FAIL; }
		},
		
		@Test.Decl( "FAIL cases are test fails" )
		FAIL { 
			public boolean passed() { return false; } 
			public State pass() { return FAIL; }
			public State fail() { return FAIL; }
		};

		public abstract boolean passed();
		public abstract State pass();
		public abstract State fail();
	}

	
	/* 
	 * Holds the Method and its corresponding Test.Impl annotation. The label associated
	 * with this test case is taken from the TestImpl member and description.
	 */
	@Test.Decl( "Not null" )
	private final TestImpl impl;
	
	/* The container instance that holds the test Method. */
	@Test.Decl( "Not null" )
	private final Test.Container container;
	
	/*
	 * When test failure occurs a message is added to this list. Messages are printed
	 * if Printable.print() is called to give details.
	 */
	@Test.Decl( "Not null" )
	private final List<String> messages;
	
	/*
	 * Test implementations that need to trigger an exception can register the expected
	 * type of exception that is thrown. When an exception is encountered, the type is
	 * checked against this registered type.
	 */
	@Test.Decl( "Initially null" )
	private Class<? extends Throwable> expectedError;
	
	/* If an unexpected error occurs it is saved for Printable.print() details. */
	@Test.Decl( "Initially null" )
	private Throwable unexpectedError;
	
	/* 
	 * Guaranteed to be executed after the Method runs. 
	 * Initialized as Procedure.NOOP, but Container test methods 
	 * may specify a custom Procedure using Test.Case.afterThis(...)
	 */
	@Test.Decl( "Initially NOOP" )
	private Procedure afterThis;
	
	/*
	 * A TestCase is created with OPEN State, indicating that the test method has
	 * not called pass() or fail(...).
	 * 
	 * When fail() is called the State transitions to FAIL regardless of the current State.
	 * 
	 * When pass() is called, if the current State is not FAIL (State is OPEN or PASS)
	 * then the new State is PASS. If the current State is FAIL, the call to pass() is ignored.
	 */
	@Test.Decl( "Initially OPEN" )
	private State state;
	
	
	/* 
	 * A printable link to the file location of this test case. Filled in by setFileLocation 
	 * when a Container test method calls any Test.Case method.
	 */
	@Test.Decl( "Initially null" )
	private String fileLocation;
	
	/* Total execution time for the test method and test framework monitoring. */
	@Test.Decl( "Initially zero" )
	private long elapsedTime;

	
	@Test.Decl( "Throws AssertionError for null TestImpl" )
	@Test.Decl( "Throws AssertionError for null Test.Container" )
	public TestCase( TestImpl impl, Test.Container container ) {
		super( Assert.nonNull( impl ).toString() );
		this.impl = impl;
		this.container = Assert.nonNull( container );
		this.messages = new LinkedList<String>();
		this.expectedError = null;
		this.unexpectedError = null;
		this.afterThis = Procedure.NOOP;
		this.state = State.OPEN;
		this.fileLocation = null;
		this.elapsedTime = 0L;
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
	 * 
	 * The execution of a Container test method falls into one of the following categories:
	 *   + Error in beforeEach: Initialization code generates an error. The Test.Case fails.
	 *   + No error: Method executes as expected, with no exception. The Test.Case passes or fails, depending on assertions.
	 *   + Got expected error: Method generates an anticipated exception. The Test.Case passes.
	 *   + Got wrong error: Method generates an exception but not of the type expected. The Test.Case fails.
	 *   + Got unexpected error: Method generates an unanticipated exception. The Test.Case fails.
	 *   + No expected error: Method completes, but an exception was expected. The Test.Case fails.
	 * 
	 * Additionally, the finally clause may alter the final status:
	 *   + Error afterThis: Exception generated by the afterThis Procedure. The Test.Case fails.
	 *   + Error afterEach: Exception generated by the afterEach Procedure. The Test.Case fails.
	 */
	@Test.Decl( "Error in beforeEach: State is FAIL" )
	@Test.Decl( "Error in beforeEach: elapsedTime recorded" )
	@Test.Decl( "Error in beforeEach: unexpectedError is not null" )
	@Test.Decl( "Error in beforeEach: afterThis called" )
	// Potential issue: afterEach might assume beforeEach completed
	@Test.Decl( "Error in beforeEach: afterEach called" ) 

	@Test.Decl( "No error: State is PASS if assertion succeeds" )
	@Test.Decl( "No error: State is FAIL if assertion fails" )
	@Test.Decl( "No error: State is OPEN if no assertions" )
	@Test.Decl( "No error: elapsedTime recorded" )
	@Test.Decl( "No error: unexpectedError is null" )
	@Test.Decl( "No error: afterThis called" )
	@Test.Decl( "No error: afterEach called" )

	@Test.Decl( "Got expected error: State is PASS" )
	@Test.Decl( "Got expected error: elapsedTime recorded" )
	@Test.Decl( "Got expected error: unexpectedError is null" )
	@Test.Decl( "Got expected error: afterThis called" )
	@Test.Decl( "Got expected error: afterEach called" )
	
	@Test.Decl( "Got wrong error: State is FAIL" )
	@Test.Decl( "Got wrong error: elapsedTime recorded" )
	@Test.Decl( "Got wrong error: unexpectedError is not null" )
	@Test.Decl( "Got wrong error: unexpectedError is different from expected" )
	@Test.Decl( "Got wrong error: afterThis called" )
	@Test.Decl( "Got wrong error: afterEach called" )
	
	@Test.Decl( "Got unexpected error: State is FAIL" )
	@Test.Decl( "Got unexpected error: elapsedTime recorded" )
	@Test.Decl( "Got unexpected error: unexpectedError is not null" )
	@Test.Decl( "Got unexpected error: afterThis called" )
	@Test.Decl( "Got unexpected error: afterEach called" )
	
	@Test.Decl( "No expected error: State is FAIL" )
	@Test.Decl( "No expected error: elapsedTime recorded" )
	@Test.Decl( "No expected error: unexpectedError is null" )
	@Test.Decl( "No expected error: afterThis called" )
	@Test.Decl( "No expected error: afterEach called" )

	@Test.Decl( "Error in afterThis: State is FAIL" )
	@Test.Decl( "Error in afterThis: elapsedTime recorded" )
	@Test.Decl( "Error in afterThis: unexpectedError is not null" )
	@Test.Decl( "Error in afterThis: afterEach called" )

	@Test.Decl( "Error in afterEach: State is FAIL" )
	@Test.Decl( "Error in afterEach: elapsedTime recorded" )
	@Test.Decl( "Error in afterEach: unexpectedError is not null" )
	@Test.Decl( "Error in afterEach: afterThis called" )

	@Override
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
				this.unexpectedError = ex.getCause();
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
	 * Mark the current case as failed.
	 * 
	 * If a test has multiple parts, the failure of any single part means the Test.Case fails.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	@Test.Decl( "The message must not be empty" )
	@Test.Decl( "The message must not be null" )
	@Test.Decl( "The message is retained" )
	@Test.Decl( "If old state is OPEN, new state is FAIL" )
	@Test.Decl( "If old state is PASS, new state is FAIL" )
	@Test.Decl( "If old state is FAIL, new state is FAIL" )
	private void fail( String message ) {
		this.state = this.state.fail();
		this.addMessage( Assert.nonEmpty( message ) + ": " + this.getFileLocation() );
	}
	

	/**
	 * Mark the current case as passed.
	 * 
	 * If a test has multiple parts, all parts must pass for the Test.Case to pass.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	@Test.Decl( "If old state is OPEN, new state is PASS" )
	@Test.Decl( "If old state is PASS, new state is PASS" )
	@Test.Decl( "If old state is FAIL, new state is FAIL" )
	private void pass() {
		this.state = this.state.pass();
	}
	

	/**
	 * When called from a test method in a Test.Container this method will store a printable
	 * link to the location of the current test case.
	 * 
	 * @return
	 * 		this Test.Case
	 */
	private String getFileLocation() {
		Predicate<StackWalker.StackFrame> sfp = 
			sf -> Test.Container.class.isAssignableFrom( sf.getDeclaringClass() );
		Function<StackWalker.StackFrame, String> sfm = 
			sf -> new App.Location( sf ).toString();
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
			s -> s.filter( sfp ).map( sfm ).findFirst().orElse( null )
		);
	}
	
	private void setFileLocation() {
		if ( this.fileLocation == null ) {
			this.fileLocation = this.getFileLocation();
		}
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
	@Test.Decl( "File location is set" )
	public Case addMessage( String message ) {
		this.setFileLocation();
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
	@Test.Decl( "File location is set" )
	@Test.Decl( "Test.Case fails if expected error already set" )
	public Case expectError( Class<? extends Throwable> expectedError ) {
		this.setFileLocation();
		if ( this.expectedError != null ) {
			this.fail( "expectedError already set" + this.expectedError );
		}
		this.expectedError = Assert.nonNull( expectedError );
		return this;
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "Does not alter State" )
	@Test.Decl( "File location is set" )
	public Case afterThis( Procedure afterThis ) {
		this.setFileLocation();
		this.afterThis = Assert.nonNull( afterThis );
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public Case assertNonNull( Object obj ) {
		this.setFileLocation();
		if ( obj != null ) {
			this.pass();
		} else {
			this.fail( "Expected non-null object but got null" );
		}
		return this;
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public Case assertNotEmpty( String s ) {
		this.setFileLocation();
		if ( s == null || s.isEmpty() ) {
			this.fail( "Expected non-empty string" );
		} else {
			this.pass();
		}
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
	@Test.Decl( "Case passes for null object" )
	@Test.Decl( "Case fails for non-null object" )
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public Case assertIsNull( Object obj ) {
		this.setFileLocation();
		if ( obj == null ) {
			this.pass();
		} else {
			this.fail( "Expected null, got: " + Strings.toString( obj ) );
		}
		return this;
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public Case assertTrue( boolean passIfTrue ) {
		this.setFileLocation();
		if ( passIfTrue ) {
			this.pass();
		} else {
			this.fail( "Expected true" );
		}
		return this;
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public Case assertFalse( boolean passIfFalse ) {
		this.setFileLocation();
		if ( passIfFalse ) {
			this.fail( "Expected false" );
		} else {
			this.pass();
		}
		return this;
	}

	/**
	 * Test for equality using Object.equals().
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
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public <T> Case assertEqual( T expected, T actual ) {
		this.setFileLocation();
		if ( Objects.shallowEquals( expected, actual ) ) {
			this.pass();
		} else {
			this.fail( "Expected: " + Strings.toString( expected ) + ", Got: " + Strings.toString( actual ) );
		}
		return this;
	}

	/**
	 * Test for non-equality using Object.equals().
	 * If T is a compound type (array or collection) then the components are shallowly tested.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 * 		this Test.Case
	 */
	@Override
	@Test.Decl( "Test fails for equivalent objects" )
	@Test.Decl( "Test passes for inequivalent" )
	@Test.Decl( "Test passes for one null and one not null" )
	@Test.Decl( "Test fails when both null" )
	@Test.Decl( "Return is this" )
	@Test.Decl( "File location is set" )
	public <T> Test.Case assertNotEqual( T first, T second ) {
		this.setFileLocation();
		if ( Objects.shallowEquals( first, second ) ) {
			this.fail( "First: " + Strings.toString( first ) + ", Second: " + Strings.toString( second ) );
		} else {
			this.pass();
		}
		return this;
	}
	
	/** Total execution time for Method and framework monitoring. */
	@Override
	@Test.Decl( "Elapsed time is consistent with execution time" )
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	/** The total weight of all components if the case passed, otherwise zero. */
	@Override
	@Test.Decl( "Return is zero when State is OPEN" )
	@Test.Decl( "Return is zero when State is FAIL" )
	@Test.Decl( "Return is Test.Impl.weight when State is PASS" )
	public int getPassCount() {
		return this.state.passed() ? this.impl.getWeight() : 0;
	}

	/** The total weight of all components if the case failed, otherwise zero. */
	@Override
	@Test.Decl( "Return is Test.Impl.weight when State is OPEN" )
	@Test.Decl( "Return is Test.Impl.weight when State is FAIL" )
	@Test.Decl( "Return is zero when State is PASS" )
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
		return this.state + " [" + this.fileLocation +"]: " + super.toString();
	}


	/** 
	 * Implementations first print this instance, then indent for details. 
	 */
	@Override
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Prints summary line" )
	@Test.Decl( "Includes file location on failure" )
	@Test.Decl( "Includes fail messages on failure" )
	@Test.Decl( "Includes additional messages on failure" )
	public void print( IndentWriter out ) {
		Assert.nonNull( out ).println( this.toString() );
		
		out.increaseIndent();
		
		if ( this.getFailCount() > 0 && this.messages.size() > 0 ) {
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
