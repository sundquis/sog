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
	
	private long elapsedTime = 0L;
	private int passCount = 0;
	private int failCount = 0;
	private int unimplementedCount = 0;
	
	private final String label;
	
	protected Result( String label ) {
		this.label = Assert.nonEmpty( label );
	}
	
	public long getElapsedTime() { return this.elapsedTime; }
	
	public void incElapsedTime( long time ) { this.elapsedTime += time; }
	
	public int getPassCount() { return this.passCount; }
	
	public void incPassCount( int pass ) { this.passCount += pass; }
	
	public int getFailCount() { return this.failCount; }
	
	public void incFailCount( int fail ) { this.failCount += fail; }
	
	public int getUnimplementedCount() { return this.unimplementedCount; }
	
	public void incUnimplementedCount( int unimpl ) { this.unimplementedCount += unimpl; }
	
	@Override
	public String toString() {
		int totalCount = this.passCount + this.failCount + this.unimplementedCount;
		double success = (double) 100 * passCount / (totalCount == 0 ? 1 : totalCount);
		double seconds = (double) this.elapsedTime / 1000.0;
		return String.format( "%s: Success = %.1f%%, Time = %.1fs, Count = %d (P = %d, F = %d, U = %d)", 
			this.label, success, seconds, totalCount, this.passCount, this.failCount, this.unimplementedCount );
	}
	
	/** Implementations first print this instance, then indent for details. */
	@Override
	public abstract void print( IndentWriter out );
	
}