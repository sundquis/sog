/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.LinkedList;
import java.util.List;

import sog.core.Assert;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * Base class for various results associated with testing.
 */
public abstract class Result implements Printable {
	
	private final String label;
	
	private final List<Result> children;
	
	private boolean notSet;
	
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
		
		this.notSet = true;
		this.time = 0L;
		this.totalCases = 0;
		this.passCases = 0;
		this.failCases = 0;
	}
	
	protected void addChild( Result child ) {
		this.children.add( Assert.nonNull( child ) );
	}
	
	protected long getTime() {
		if ( this.notSet ) {
			this.setStats();
		}
		return this.time;
	}
	
	protected int getTotalCases() {
		if ( this.notSet ) {
			this.setStats();
		}
		return this.totalCases;
	}
	
	protected int getPassCases() {
		if ( this.notSet ) {
			this.setStats();
		}
		return this.passCases;
	}
	
	protected int getFailCases() {
		if ( this.notSet ) {
			this.setStats();
		}
		return this.failCases;
	}
	
	@Override
	public String toString() {
		int tot = this.getTotalCases();
		int pass = this.getPassCases();
		int fail = this.getFailCases();
		int unx = tot - pass - fail;
		double success = (double) 100 * pass / (tot == 0 ? 1 : tot);
		double seconds = (double) this.time / 1000.0;
		return String.format( "[S = %.1f%%, T = %.1fs, N = %d, P = %d, F = %d, U = %d]: %s", success, seconds, tot, pass, fail, unx, this.label );
		//return String.format( "[%s] Success = %.1f%%, Time = %.1fs, Count = %d (P = %d, F = %d, U = %d)", this.label, success, seconds, tot, pass, fail, unx );
	}
	
	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out );
		
		out.println( this.toString() );
		
		out.increaseIndent();
		this.children.stream().forEach( (c) -> c.print( out ) );
		out.decreaseIndent();
	}

	private void setStats() {
		this.children.stream().forEach( this::combine );
	}

	private void combine( Result other ) {
		Assert.nonNull( other );
		
		this.time += other.getTime();
		this.totalCases += other.getTotalCases();
		this.passCases += other.getPassCases();
		this.failCases += other.getFailCases();
	}
	
}
