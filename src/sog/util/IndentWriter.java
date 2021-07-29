/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;



import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;

/**
 * 
 * @author sundquis
 *
 */
public class IndentWriter {

	private final PrintWriter out;
	private final String indent;
	private final Deque<String> prefix;

	@Test.Decl( "Throws assertion error for null stream" )
	@Test.Decl( "Throws assertion error for null indent" )
	@Test.Decl( "Indent can be empty" )
	public IndentWriter( OutputStream os, String indent ) {
		this.out = new PrintWriter( Assert.nonNull( os ), true );
		this.indent = Assert.nonNull( indent );
		this.prefix = new ArrayDeque<>();
		this.prefix.push( "" );
	}

	@Test.Decl( "Default indent" )
	public IndentWriter( OutputStream os ) {
		this( os, "    " );
	}
	
	@Test.Decl( "Can increase indent" )
	@Test.Decl( "Increase indent increases indent" )
	@Test.Decl( "Increase empty indent is noop" )
	public void increaseIndent() {
		this.prefix.push( this.prefix.peek() + this.indent );
	}
	
	@Test.Decl( "Custom indent used" )
	@Test.Decl( "Custom indent used after default" )
	public void increaseIndent( String in ) {
		this.prefix.push( this.prefix.peek() + in );
	}
	
	@Test.Decl( "Can decrease after increase" )
	@Test.Decl( "Illegal state for decrease before increase" )
	@Test.Decl( "Illegal state for more decreases than increases" )
	@Test.Decl( "Custom indent removed" )
	public void decreaseIndent() throws IllegalStateException {
		if ( this.prefix.size() == 1 ) {
			throw new IllegalStateException( "Indent already at minimum" );
		}
		this.prefix.pop();
	}
	
	@Test.Decl( "Prints prefix" )
	@Test.Decl( "Before increase no prefix" )
	public void println( String s ) {
		this.out.println( this.prefix.peek() + s );
	}
	
	// WARNING: A Printable class cannot implement its Printable.print( out ) method using
	// out.println( this ) since this results in a recursive loop. What is probably intended
	// is something like out.println( this.toString() )
	public void println( Printable p ) {
		p.print( this );
	}
	
	public void println( Object obj ) {
		this.println( Strings.toString( obj ) );
	}
	
	
	public void println() {
		this.out.println();
	}
	
	public void printErr( Throwable error ) {
		this.println( "Error: " + error );
		this.increaseIndent();
		App.get().getLocation( error ).forEach( this::println );
		this.decreaseIndent();
		
		Throwable cause = error.getCause();
		while ( cause != null ) {
			this.println( "Caused By: " + cause );
			this.increaseIndent();
			App.get().getLocation( cause ).forEach( this::println );
			this.decreaseIndent();
			cause = cause.getCause();
		}		
	}

	public void close() {
		if ( this.out != null ) {
			this.out.close();
		}
	}
	
	
}
