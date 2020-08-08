/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;

import sog.core.Assert;
import sog.core.Test;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * Base class for various results associated with testing.
 */
public abstract class Result implements Printable {
	
	private final String label;
	
	private final List<Result> children;
	
	private volatile long time;
	
	private volatile int totalCases;
	
	private volatile int passCases;
	
	private volatile int failCases;

	/**
	 * Base result construction. Must supply a text label for the type of result.
	 * 
	 * Concrete subclasses must provide the logic for finding and adding children.
	 */
	protected Result( String label ) {
		this.label = Assert.nonEmpty( label );
		this.children = new LinkedList<Result>();
		
		this.time = 0L;
		this.totalCases = 0;
		this.passCases = 0;
		this.failCases = 0;
	}
	
	// Find, construct, and add children
	protected abstract void load();
	
	protected void addChild( Result child ) {
		Assert.nonNull( child );
		
		child.load();
		
		this.time += child.time;
		this.totalCases += child.totalCases;
		this.passCases += child.passCases;
		this.failCases += child.failCases;
		
		this.children.add( child );
	}
	

	public String getStats() {
		int unx = this.totalCases - this.passCases - this.failCases;
		double success = (double) 100 * this.passCases / (this.totalCases == 0 ? 1 : this.totalCases);
		double seconds = (double) this.time / 1000.0;
		return String.format( 
			"[S = %.1f%%, T = %.1fs, N = %d, P = %d, F = %d, U = %d]", 
			success, seconds, this.totalCases, this.passCases, this.failCases, unx );
	}
	
	@Override public String toString() {
		return this.label;
	}
		
	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out );
		
		out.println( this.getStats() + ": " + this.toString() );
		
		out.increaseIndent();
		this.children.stream().forEach( (c) -> c.print( out ) );
		out.decreaseIndent();
	}
	
	public void print() {
		this.print( new IndentWriter( System.out ) );
	}
	
	
}
