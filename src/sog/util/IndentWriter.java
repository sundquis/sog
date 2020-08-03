/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;



import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import sog.core.Assert;
import sog.core.TestOrig;

/**
 * 
 * @author sundquis
 *
 */
public class IndentWriter implements Closeable {

	private final PrintWriter out;
	private final String indent;
	private final Deque<String> prefix;

	@TestOrig.Decl( "Throws assertion error for null stream" )
	@TestOrig.Decl( "Throws assertion error for null indent" )
	@TestOrig.Decl( "Indent can be empty" )
	public IndentWriter( OutputStream os, String indent ) {
		this.out = new PrintWriter( Assert.nonNull( os ), true );
		this.indent = Assert.nonNull( indent );
		this.prefix = new ArrayDeque<>();
		this.prefix.push( "" );
	}

	@TestOrig.Decl( "Default indent" )
	public IndentWriter( OutputStream os ) {
		this( os, "    " );
	}
	
	@TestOrig.Decl( "Can increase indent" )
	@TestOrig.Decl( "Increase indent increases indent" )
	@TestOrig.Decl( "Increase empty indent is noop" )
	public void increaseIndent() {
		this.prefix.push( this.prefix.peek() + this.indent );
	}
	
	@TestOrig.Decl( "Custom indent used" )
	@TestOrig.Decl( "Custom indent used after default" )
	public void increaseIndent( String in ) {
		this.prefix.push( this.prefix.peek() + in );
	}
	
	@TestOrig.Decl( "Can decrease after increase" )
	@TestOrig.Decl( "Illegal state for decrease before increase" )
	@TestOrig.Decl( "Illegal state for more decreases than increases" )
	@TestOrig.Decl( "Custom indent removed" )
	public void decreaseIndent() throws IllegalStateException {
		if ( this.prefix.size() == 1 ) {
			throw new IllegalStateException( "Indent already at minimum" );
		}
		this.prefix.pop();
	}
	
	@TestOrig.Decl( "Prints prefix" )
	@TestOrig.Decl( "Before increase no prefix" )
	public void println( String s ) {
		this.out.println( this.prefix.peek() + s );
	}

	@TestOrig.Skip
	public void close() {
		if ( this.out != null ) {
			this.out.close();
		}
	}
	
	
}
