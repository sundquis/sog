/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.core.TestOrig;

/**
 * @author sundquis
 *
 */
public class Fault implements Printable {
	
	
	private static final Set<Consumer<Fault>> listeners = new HashSet<>();

	/**
	 * Register the given listener to receive notification of application faults.
	 * 
	 * @param listener
	 */
	@TestOrig.Decl( "Subsequent faults are deleivered to listener" )
	public static void addListener( Consumer<Fault> listener ) {
		Fault.listeners.add( listener );
	}
	
	/**
	 * Unsubscribe the given listener.
	 * 
	 * @param listener
	 */
	@TestOrig.Decl( "Subsequent faults are deleivered to listener" )
	public static void removeListener( Consumer<Fault> listener ) {
		Fault.listeners.remove( listener );
	}


	
	
	// Description of the nature of the defect
	private final String description;
	
	// Optional list of cause(s)
	private final List<String> sources;
	
	// File and line number where fault was generated
	private final String faultLocation;
	
	/**
	 * Construct a {@code Fault} representing a application defect. The required
	 * non-empty description explains the nature of the fault. The optional
	 * sources provide additional context.
	 * 
	 * @param description
	 * @param sources
	 */
	@TestOrig.Decl( "Throws assertion error for enpty string" )
	@TestOrig.Decl( "Fault location is recorded" )
	public Fault( String description, Object ... sources ) {
		this.description = Assert.nonEmpty( description );
		this.sources = new ArrayList<>();
		for ( Object obj : sources ) {
			this.sources.add( Strings.toString( obj ) );
		}
		this.faultLocation = getFaultLocation();
	}

	
	/**
	 * Broadcasts an application {@code Fault} to all registered listeners.
	 * 
	 * Similar to throwing an exception but "gentler". Useful in compilation-style
	 * processes where it's useful to gather as much feedback as possible.
	 */
	@TestOrig.Decl( "Fault is delivered to listeners" )
	public void toss() {
		Fault.listeners.stream().forEach( l -> l.accept(this) );
	}
	

	/**
	 * Add an additional source to this {@code Fault}.
	 * Mutator pattern allows chaining.
	 * 
	 * @param source
	 * @return
	 */
	@TestOrig.Decl( "Source is appended to previous sources" )
	@TestOrig.Decl( "Returns this" )
	public Fault addSource( String source ) {			
		this.sources.add( source );
		return this;
	}
	
	
	/**
	 * Description of the fault
	 */
	@Override
	@TestOrig.Decl( "Returns non-empty description" )
	public String toString() {
		return "Fault(" + this.description + ")";
	}
	

	/**
	 * Write a "pretty-print" representation on the given writer
	 * 
	 * @see sog.util.Printable#print(sog.util.IndentWriter)
	 */
	@Override
	@TestOrig.Decl( "Description printed" )
	@TestOrig.Decl( "Fault location printed" )
	@TestOrig.Decl( "Model location printed when possible" )
	@TestOrig.Decl( "All provided sources printed" )
	public void print( IndentWriter out ) {
		out.println( "FAULT: " + this.description );
		out.increaseIndent();

		out.println( "FAULT LOCATION: " + this.faultLocation );

		out.println( "SOURCE(S):" );
		out.increaseIndent();
		for ( String s : this.sources ) {
			out.println( s );
		}
		out.decreaseIndent();

		out.decreaseIndent();
		out.println("");
	}


	// 0: StackWalker.getInstance
	// 1: App.getFileLocation
	// 2: Fault.getFileLocation
	// 3: Originating source
	private String getFaultLocation() {
		return App.get().getFileLocation( 3, 3 );
	}
	
	
}
