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

import sog.core.Assert;
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
		return String.format( "%s: Success = %.1f%%, Time = %.1fs, Count = %d (P = %d, F = %d)", this.label,
				success, seconds, totalCount, this.getPassCount(), this.getFailCount() );
	}

	/** Implementations first print this instance, then indent for details. */
	@Override
	public abstract void print( IndentWriter out );
	
	

}