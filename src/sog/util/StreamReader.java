/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.io.Reader;
import java.util.Iterator;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.TestOrig;

/**
 * Wrap a {@code java.util.stream.Stream} of String to provide a {@code java.io.Reader}
 * 
 * @author sundquis
 *
 */
public class StreamReader extends Reader implements AutoCloseable {
	
	private Stream<String> stream;
	
	private Iterator<String> lines;
	
	private char[] currentLine;
	
	private int position;
	
	/**
	 * Create a reader that supplies characters by iterating over the given stream
	 * 
	 * @param stream
	 */
	@TestOrig.Decl( "Throws Assertion Error for null stream" )
	public StreamReader( Stream<String> stream ) {
		this.stream = Assert.nonNull( stream );
		this.lines = stream.iterator();
		this.currentLine = "".toCharArray();
		this.position = 0;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	@TestOrig.Decl( "Throws Assertion Error for null buffer" )
	@TestOrig.Decl( "Throws Assertion Error for negative offset" )
	@TestOrig.Decl( "Throws Assertion Error for offset + count > length" )
	@TestOrig.Decl( "Returns -1 at end of stream" )
	@TestOrig.Decl( "Returns -1 after close()" )
	@TestOrig.Decl( "Returns lines terminated with LF character" )
	@TestOrig.Decl( "Return is at most count" )
	public int read( char[] buffer, int offset, int count ) {
		Assert.nonNull( buffer );
		Assert.nonNeg( offset );
		Assert.isTrue( offset + count <= buffer.length );
		
		if ( this.position == this.currentLine.length ) {
			if ( this.lines != null && this.lines.hasNext() ) {
				this.currentLine = (this.lines.next() + "\n").toCharArray();
				this.position = 0;
			} else {
				return -1;
			}
		}
		
		int capacity = this.currentLine.length - this.position;
		int actual = Math.min( count,  capacity );
		System.arraycopy( this.currentLine,  this.position,  buffer,  offset,  actual );
		this.position += actual;
		return actual;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#close()
	 */
	@Override
	@TestOrig.Decl( "Close after close is no op" )
	public void close() {
		if ( stream != null ) {
			this.stream.close();
			this.stream = null;
			this.lines = null;
		}
	}

}
