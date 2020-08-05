/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.Arrays;

import sog.core.Assert;
import sog.util.IndentWriter;

/**
 * Signal an error encountered while processing tests.
 * 
 * Instances represent serious defects that must be fixed, as opposed to warnings, which
 * can be ignored.
 * 
 * May Include:
 * 	+ No container specified for a subject class.
 * 	+ Container cannot be created, or container does not confirm subject class.
 * 	+ No test method found for a declared test case.
 * 	+ Test case fails.
 * 	+ Public non-skipped method has no declared test cases.
 * 	+ Orphaned container test methods.
 * 
 * 
 * Error and Warning Policy:
 * 
 * On any type of member (Type, Constructor, Field, Method) if Test.Case is present:
 * 		Case, !Impl: Error - Unimplemented (generates a stub)
 * 		Case,  Impl, Fail: Error - Test Fail
 * 		Case,  Impl, Pass: Ok
 * 
 * The strictest policy is applied to public methods:
 * 		!Case, !Skip: Error - Untested
 * 		!Case,  Skip: Warning - Skipping public method...
 * 
 * For protected or package methods:
 * 		!Case, !Skip: Warning - Unstated test policy
 * 		!Case,  Skip: Ok
 * 
 * For private methods, no annotation is required:
 * 		!Case, !Skip: Ok
 * 
 * For public types, constructors, or fields the policy is the same as protected methods:
 * 		!Case, !Skip: Warning - Unstated test policy
 * 		!Case,  Skip: Ok
 * 
 * For other protection levels, no annotation is required:
 * 		!Case, !Skip: Ok
 * 
 */
public class Error {
	
	private static final IndentWriter err = new IndentWriter( System.err );

	/**
	 * 
	 */
	public static void log( String description, String... details) {
		Assert.nonEmpty( description );
		
		// FIXME Capture frame data
		
		Error.err.println( description );
		
		Error.err.increaseIndent();
		Arrays.stream( details ).forEach( Error.err::println );
		Error.err.decreaseIndent();
	}

}
