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
 * more details starting with the summary, followed by details for any components of the test.
 */
@Test.Subject( "test." )
public abstract class Result implements Printable {
	
	/* 
	 * Controls the behavior of the print methods.
	 * If true, a Result instance first prints its one-line toString() summary,
	 * then indents and prints any details. Otherwise, only the summary is printed.
	 */
	private static boolean SHOW_DETAILS = Property.get( "showDetails", false, Parser.BOOLEAN );
	
	/*
	 * Impacts the behavior of TestCase.run()
	 * If true, after each test case is run, TestCase.run() will print a progress indicator.
	 */
	private static boolean SHOW_PROGRESS = Property.get( "showProgress", false, Parser.BOOLEAN );

	/*
	 * Impacts the behavior of TestCase.run()
	 * When SHOW_PROGRESS is true, TestCase.run() will print a new line after this many
	 * progress indicators have been printed.
	 */
	private static int WRAP_PROGRESS = Property.get( "wrapProgress", 80, Parser.INTEGER );

	
	@Test.Decl( "Progress is included when true" )
	@Test.Decl( "Progress is excluded when false" )
	@Test.Decl( "Returns this Result instance to allow chaining" )
	public static void showProgress( boolean showProgress ) {
		Result.SHOW_PROGRESS = showProgress;
	}
	


	
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

	/** Total execution time for Method and framework monitoring. */
	public abstract long getElapsedTime();

	/** The total weight of all components if the case passed, otherwise zero. */
	public abstract int getPassCount();

	/** The total weight of all components if the case failed, otherwise zero. */
	public abstract int getFailCount();


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
		Result.SHOW_DETAILS = showDetails;
		return this;
	}

	/**
	 * Concrete subclasses use this to determine the level of detail in printed output.
	 * @return
	 */
	@Test.Decl( "True after showDetails(true)" )
	@Test.Decl( "False after showDetails(false)" )
	protected boolean showDetails() {
		return Result.SHOW_DETAILS;
	}
	
	/**
	 * TestCase checks this after every case is run.
	 * @return
	 */
	@Test.Decl( "True after showProgress(true)" )
	@Test.Decl( "False after showProgress(false)" )
	protected boolean showProgress() {
		return Result.SHOW_PROGRESS;
	}
	
	/**
	 * TestCase checks this after every case is run.
	 * @return
	 */
	@Test.Decl( "Throws Assertion Error if not positive" )
	protected int wrapProgress() {
		return Assert.positive( Result.WRAP_PROGRESS );
	}

	/**
	 * Implementations first print this instance using the toString() one-line summary.
	 * If SHOW_DETAILS is true the instance indents the writer and prints details for the test Result.
	 */
	@Override
	public abstract void print( IndentWriter out );
	

	@Test.Decl( "Defaults to standard out" )
	public void print() {
		this.print( new IndentWriter() );
	}


}