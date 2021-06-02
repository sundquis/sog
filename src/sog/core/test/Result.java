/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Assert;
import sog.util.IndentWriter;
import sog.util.Printable;

public abstract class Result implements Printable {

	private final String label;

	protected Result( String label ) {
		this.label = Assert.nonEmpty( label );
	}

	public abstract long getElapsedTime();

	public abstract int getPassCount();

	public abstract int getFailCount();


	@Override
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