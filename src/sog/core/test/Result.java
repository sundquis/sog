/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.ArrayList;
import java.util.List;

import sog.core.Assert;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * Base class for various results associated with testing.
 */
public abstract class Result implements Printable {
	
	private final String name;
	
	private final List<Result> children;
	
	private volatile long time;
	
	private volatile int passCount;
	
	private volatile int failCount;

	/**
	 * Base result construction. Must supply a text label for the type of result.
	 * 
	 * Concrete subclasses must provide the logic for finding and adding children.
	 */
	protected Result( String name ) {
		this.name = name;
		this.children = new ArrayList<Result>();
		
		this.time = 0L;
		this.passCount = 0;
		this.failCount = 0;
	}
	
	// Find and construct valid children, call addChild for each
	protected abstract void load();
	
	
	protected long getTime() {
		return this.time;
	}
	
	protected int getPassCount() {
		return this.passCount;
	}
	
	protected int getFailCount() {
		return this.failCount;
	}
	
	protected void addChild( Result child ) {
		Assert.nonNull( child );
		
		child.load();
		
		this.time += child.getTime();
		this.passCount += child.getPassCount();
		this.failCount += child.getFailCount();
		
		this.children.add( child );
	}
	

	public String getStats() {
		int totalCount = this.passCount + this.failCount;
		double success = (double) 100 * this.passCount / (totalCount == 0 ? 1 : totalCount);
		double seconds = (double) this.time / 1000.0;
		return String.format( 
			"[S = %.1f%%, T = %.1fs, N = %d, P = %d, F = %d] ", 
			success, seconds, totalCount, this.passCount, this.failCount );
	}
	
	@Override public String toString() {
		return this.name;
	}
		
	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out );
		
		out.println( this.getStats() + this.toString() );
		
		out.increaseIndent();
		this.children.stream().forEach( (c) -> c.print( out ) );
		out.decreaseIndent();
		
		out.println( "" );
	}
	
	public void print() {
		this.print( new IndentWriter( System.out ) );
		System.out.flush();
	}
	
	
}
