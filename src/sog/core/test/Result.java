/**
 * Copyright (C) 2021, 2023
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

import sog.core.Assert;
import sog.core.Parser;
import sog.core.Property;
import sog.core.Test;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * The base class for classes that report results from testing. In particular, all
 * test results inherit the toString() implementation given here, setting the uniform
 * format for results.
 * 
 * There are two levels of detail for test results. The toString() method returns a printable
 * summary including elapsed time and pass/fail counts. The Printable.print() method gives
 * more details starting with the summary, followed by details for any components of the test, 
 * depending on the status of showDetails.
 * 
 * This package has three concrete Result classes covering three levels of testing. TestCase
 * represents a single test method exercising a single testable property as described by
 * a Test.Decl. TestSubject represents the collection of test cases for a single subject class.
 * TestSet aggregates tests for various sets of subject classes.
 * 
 * Result instances are Runnable. Concrete classes implement Runnable.run() by executing their
 * contained test(s) and tabulating the pass/fail results. 
 * 
 * Various mutators allow properties to be customized before the tests are executed.
 */
@Test.Subject( "test." )
public abstract class Result implements Printable {

	/*
	 * The following global properties are used to configure the behavior of tests. Each has:
	 * 		1. A configured default property from system.xml
	 * 		2. Instance mutator to alter values (for all instances)
	 * 		3. Static getter
	 */
	
	/* 
	 * Controls the behavior of the print methods.
	 * If true, a Result instance first prints its one-line toString() summary,
	 * then indents and prints any details. Otherwise, only the summary is printed.
	 */
	private static boolean showDetails = Property.get( "showDetails", false, Parser.BOOLEAN );
	
	/*
	 * TestSubject instances hold a collection of TestCase Result instances.
	 * This property determines whether to use concurrent processing for these test cases.
	 */
	private static boolean concurrentSubjects = Property.get( "concurrentSubjects", false, Parser.BOOLEAN );
	
	/*
	 * TestSet instances hold a collection of TestSubject Result instances.
	 * This property determines whether to use concurrent processing for these test cases.
	 */
	private static boolean concurrentSets = Property.get( "concurrentSets", false, Parser.BOOLEAN );

	
	
	/** Short descriptive string identifying the test */
	private final String label;

	/**
	 * Constructor requires a short descriptive string identifying the test.
	 * 
	 * @param label
	 */
	@Test.Decl( "Throws AssertionError for null label" )
	@Test.Decl( "Throws AssertionError for empty label" )
	@Test.Decl( "Label is included in the toString() value" )
	protected Result( String label ) {
		this.label = Assert.nonEmpty( label );
	}
	
	/*
	 * Subclass Obligations
	 */
	
	/** Execute the test(s) associated with this Result */
	protected abstract void run();

	/** Total execution time for Method and framework monitoring. */
	public abstract long getElapsedTime();

	/** The total weight of all components if the case passed, otherwise zero. */
	public abstract int getPassCount();

	/** The total weight of all components if the case failed, otherwise zero. */
	public abstract int getFailCount();

	/**
	 * Implementations first print this instance using the toString() one-line summary.
	 * If showDetails is true the instance indents the writer and prints details for the test Result.
	 */
	@Override
	public abstract void print( IndentWriter out );
	


	@Override
	@Test.Decl( "Includes elapsed time" )
	@Test.Decl( "Includes the pass count" )
	@Test.Decl( "Includes the fail count" )
	@Test.Decl( "Includes the total count" )
	public String toString() {
		int totalCount = this.getPassCount() + this.getFailCount();
		double success = (double) 100 * this.getPassCount() / (totalCount == 0 ? 1 : totalCount);
		double seconds = (double) this.getElapsedTime() / 1000.0;
		return String.format( "%s: Success = %.1f%%, Time = %.1fs, Count = %d (P = %d, F = %d)", 
			this.label, success, seconds, totalCount, this.getPassCount(), this.getFailCount() );
	}
	
	@Test.Decl( "Is not null" )
	@Test.Decl( "Is consistent with constructed value" )
	protected String getLabel() {
		return this.label;
	}
	
	/**
	 * Set the boolean flag for when instances should include test details.
	 * 
	 * @param details
	 * @return
	 */
	@Test.Decl( "Details are included when true" )
	@Test.Decl( "Details are excluded when false" )
	@Test.Decl( "Returns this Result instance to allow chaining" )
	public Result showDetails( boolean showDetails ) {
		Result.showDetails = showDetails;
		return this;
	}

	/**
	 * Concrete subclasses use this to determine the level of detail in printed output.
	 * @return
	 */
	@Test.Decl( "True after showDetails(true)" )
	@Test.Decl( "False after showDetails(false)" )
	protected static boolean showDetails() {
		return Result.showDetails;
	}

	/**
	 * Specify that TestSubject should use concurrent processing for the multiple
	 * contained TestCase instances.
	 */
	@Test.Decl( "Concurrent processing used when true" )
	@Test.Decl( "Concurrent processing not used when false" )
	@Test.Decl( "Returns this Result instance to allow chaining" )
	public Result concurrentSubjects( boolean concurrentSubjects ) {
		Result.concurrentSubjects = concurrentSubjects;
		return this;
	}
	
	/**
	 * TestSubject checks this when running its TestCase instances.
	 * 
	 * @return
	 */
	@Test.Decl( "Consistent with specified value" )
	protected static boolean concurrentSubjects() {
		return Result.concurrentSubjects;
	}
	
	/**
	 * Specify that TestSet should use concurrent processing for the multiple
	 * contained TestSubject instances.
	 */
	@Test.Decl( "Concurrent processing used when true" )
	@Test.Decl( "Concurrent processing not used when false" )
	@Test.Decl( "Returns this Result instance to allow chaining" )
	public Result concurrentSets( boolean concurrentSets ) {
		Result.concurrentSets = concurrentSets;
		return this;
	}
	
	/**
	 * TestSet checks this when running its TestSubject instances.
	 * 
	 * @return
	 */
	@Test.Decl( "Consistent with specified value" )
	protected static boolean concurrentSets() {
		return Result.concurrentSets;
	}
	

}