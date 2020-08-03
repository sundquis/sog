/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.test;

import java.util.LinkedList;
import java.util.List;

import sog.core.Assert;
import sog.core.Fatal;
import sog.core.TestOrig;

/**
 * Represents the result status of a single test case
 *
 */
public class CaseResultOrig extends ResultOrig {

	/*
	 * Case created in state UNEXECUTED
	 * Allowed transitions:
	 * 	U -> P
	 * 	U -> F
	 * 	P -> F	If the case is a compound case with multiple conditions
	 */
	@TestOrig.Skip
	private static enum Status {
		UNEXECUTED,	// Test case has been declared but not executed
		PASSED,		// Test case passed
		FAILED		// Test case failed
	}

	
	private Status status;
	
	private long time;	
	
	private List<String> messages;
	
	private Throwable error;
	
	private int weight;
	
	private final String member;
	
	private final String subject;
	
	private String location;
	
	public CaseResultOrig( String member, String description, String subject ) {
		super( description );
		this.status = Status.UNEXECUTED;
		this.time = 0L;
		this.messages = new LinkedList<String>();
		this.error = null;
		this.weight = 1;
		this.member = Assert.nonEmpty( member );
		this.subject = Assert.nonEmpty( subject );
		this.location = null;
	}
	

	/**
	 * Pretty-print with indented format
	 * 
	 * @see sog.core.test.ResultOrig#print(java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Throws assertion error for null prefix" )
	@TestOrig.Decl( "Adds warnings for unexecuted cases" )
	@TestOrig.Decl( "Prints messages for non pass cases" )
	@TestOrig.Decl( "Shows stack trace for unexpected errors" )
	public void print( String prefix ) {
		Assert.nonNull( prefix );
		
		if ( this.status == Status.UNEXECUTED ) {
			TestOrig.addStub( this.member, this.getLabel(), this.subject );
		}
		
		if ( this.showResults() ) {
			System.err.print( prefix + this );
			if ( this.location != null && this.status != Status.PASSED ) {
				System.err.print( this.location );
			}
			System.err.println();

			if ( this.status != Status.PASSED ) {
				for ( String s : this.messages ) {
					System.err.println( "\t" + prefix + s );
				}
			}
			
			if ( this.error != null ) {
				this.error.printStackTrace();
			}
		}
	}

	/**
	 * Shows status and test description
	 * @see sog.core.test.ResultOrig#toString()
	 */
	@Override
	@TestOrig.Decl( "Is not empty" )
	public String toString() {
		return this.status + " [" + this.getLabel() + "] ";
	}

	/**
	 * Unique identifier among test cases for the subject class
	 * 
	 * @return
	 */
	@TestOrig.Decl( "Is not empty" )
	public String getKey() {
		return this.member + "." + this.getLabel();
	}
	

	/**
	 * Set the execution time for this test case
	 * @param time
	 */
	@TestOrig.Skip
	public void setTime ( long time ) {
		this.time = time;
	}

	/**
	 * If an unexpected error occurs in TestCaseImpl
	 * @param err
	 */
	@TestOrig.Decl( "Throws assertion error for null error" )
	public void setError( Throwable err ) {
		this.error = Assert.nonNull( err );
	}

	/**
	 * Set the location (file and line number) of the test method.
	 * This is best-effort based on StackTraceElement
	 * @param location
	 */
	@TestOrig.Skip
	public void setLocation( String location ) {
		if ( this.location == null && location != null ) {
			this.location = location;
		}
	}

	/**
	 * Set the relative weight. Default weight is 1.
	 * @param weight
	 */
	@TestOrig.Decl( "Throws assertion eror for non-positve weight" )
	public void setWeight( int weight ) {
		this.weight = Assert.positive( weight );
	}

	@Override
	@TestOrig.Skip
	public int getPassCount() {
		return this.status == Status.PASSED ? this.weight : 0;
	}
	
	@Override
	@TestOrig.Skip
	public int getFailCount() {
		return this.status == Status.FAILED ? this.weight : 0;
	}
	
	@Override
	@TestOrig.Skip
	public int getTotalCount() {
		return this.weight;
	}
	
	@Override
	@TestOrig.Skip
	public long getTime() {
		return this.time;
	}
	
	@Override
	@TestOrig.Skip
	public ResultOrig addChild( ResultOrig child ) {
		Fatal.error( "Case elements do not have children" );
		return null;
	}

	@Override
	@TestOrig.Skip
	public boolean showResults() {
		return ResultOrig.SHOW_CASE_RESULTS && (
			(this.status == Status.PASSED && ResultOrig.SHOW_PASS_CASES)
			|| (this.status == Status.FAILED && ResultOrig.SHOW_FAIL_CASES)
			|| (this.status == Status.UNEXECUTED && ResultOrig.SHOW_UNEXECUTED_CASES)
		);
	}

	/**
	 * Add a message to be displayed for non-pass cases
	 * @param message
	 */
	@TestOrig.Decl( "Throws assertion eror for empty message" )
	public void addMessage( String message ) {
		this.messages.add( Assert.nonEmpty( message ) );
	}

	/**
	 * Mark the case passed. Can later fail if this is a compound case.
	 */
	@TestOrig.Skip
	public void pass() {
		if ( this.status == Status.UNEXECUTED ) {
			this.status = Status.PASSED;
		}
	}
	
	/**
	 * Once a case fails it cannot change status
	 */
	@TestOrig.Skip
	public void fail() {
		this.status = Status.FAILED;
	}
	
			
	
}
