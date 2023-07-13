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
	
	@Test.Decl( "Default OutputStream" )
	public IndentWriter( String indent ) {
		this( System.out, indent);
	}
	
	@Test.Decl( "No-arg uses both defaults" )
	public IndentWriter() {
		this( "    " );
	}
	
	@Test.Decl( "Can increase indent" )
	@Test.Decl( "Increase indent increases indent" )
	@Test.Decl( "Increase empty indent is noop" )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter increaseIndent() {
		this.prefix.push( this.prefix.peek() + this.indent );
		return this;
	}
	
	@Test.Decl( "Custom indent used" )
	@Test.Decl( "Custom indent used after default" )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter increaseIndent( String in ) {
		this.prefix.push( this.prefix.peek() + in );
		return this;
	}
	
	@Test.Decl( "Can decrease after increase" )
	@Test.Decl( "Illegal state for decrease before increase" )
	@Test.Decl( "Illegal state for more decreases than increases" )
	@Test.Decl( "Custom indent removed" )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter decreaseIndent() throws IllegalStateException {
		if ( this.prefix.size() == 1 ) {
			throw new IllegalStateException( "Indent already at minimum" );
		}
		this.prefix.pop();
		return this;
	}
	
	@Test.Decl( "Prints prefix" )
	@Test.Decl( "Before increase no prefix" )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter println( String s ) {
		this.out.println( this.prefix.peek() + s );
		return this;
	}
	
	// WARNING: A Printable class cannot implement its Printable.print( out ) method using
	// out.println( this ) since this results in a recursive loop. What is probably intended
	// is something like out.println( this.toString() )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter println( Printable p ) {
		p.print( this );
		return this;
	}
	
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	@Test.Decl( "Throws Assertion Error for null error" )
	@Test.Decl( "Details of the error are included" )
	@Test.Decl( "Elements have classes matching the given regexp" )
	@Test.Decl( "Throws AssertionError for null regexp" )
	@Test.Decl( "regexp can be empty" )
	public IndentWriter printErr( Throwable error, String regexp ) {
		this.println( "Error: " + error );
		this.increaseIndent();
		App.get().getLocationMatching( error, regexp ).forEach( this::println );
		this.decreaseIndent();
		
		Throwable cause = error.getCause();
		while ( cause != null ) {
			this.println( "Caused By: " + cause );
			this.increaseIndent();
			App.get().getLocation( cause ).forEach( this::println );
			this.decreaseIndent();
			cause = cause.getCause();
		}
		
		return this;
	}

	@Test.Decl( "Default shows all locations" )
	@Test.Decl( "Throws AssertionError for null error" )
	@Test.Decl( "Details of the error are included" )
	@Test.Decl( "Default shows elements from all classes" )
	public IndentWriter printErr( Throwable error ) {
		return this.printErr( error, "" );
	}

	@Test.Decl( "Null object is allowed" )
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter println( Object obj ) {
		this.println( Strings.toString( obj ) );
		return this;
	}
	
	
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	public IndentWriter println() {
		this.out.println();
		return this;
	}
	
	@Test.Decl( "Return this IndentWriter to allow chaining" )
	@Test.Decl( "Write fails after close" )
	public IndentWriter close() {
		if ( this.out != null ) {
			this.out.close();
		}
		return this;
	}
	
	
	/**
	 * Returns a IndentWriter using a StringOutputStream. The toString method closes the OutputStream
	 * and returns the concatenation of written strings.
	 * 
	 * @return
	 */
	@Test.Decl( "After toString write calls are ignored" )
	public static IndentWriter stringIndentWriter() {
		final StringOutputStream sos = new StringOutputStream();
		IndentWriter result = new IndentWriter( sos ) {
			@Override public String toString() { return sos.toString(); }
		};
		return result;
	}
	
	
}
