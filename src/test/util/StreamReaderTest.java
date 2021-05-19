/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.stream.Stream;

import sog.core.Procedure;
import sog.core.Test;
import sog.util.StreamReader;

/**
 * @author sundquis
 *
 */
public class StreamReaderTest extends Test.Implementation {

	private static final String[] lines = {
		"First line.",
		"Second line",
		"Last line"
	};
	
	private StreamReader reader;
	
	private char[] buffer = new char[100];

	@Override public Procedure beforeEach() { return () -> reader = new StreamReader( Stream.of( lines ) ); }
	
	@Override public Procedure afterEach() { return () -> reader = null; }
	
	public int fillBuffer() { return this.reader.read( this.buffer,  0,  this.buffer.length ); }

	
	// Test implementations
	
	@SuppressWarnings( "resource" )
	@Test.Impl( member = "public StreamReader(Stream)", description = "Throws Assertion Error for null stream" )
	public void StreamReader_ThrowsAssertionErrorForNullStream( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new StreamReader( null );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns -1 after close()" )
	public void read_Returns1AfterClose( Test.Case tc ) {
		this.reader.close();
		tc.assertEqual( -1,  this.fillBuffer() );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns -1 at end of stream" )
	public void read_Returns1AtEndOfStream( Test.Case tc ) {
		int iterations = 0;  // To guard against infinite while on bad implementation
		while ( iterations < 100 && this.fillBuffer() != -1 ) {
			iterations++;
		}
		tc.assertTrue( iterations < 100 );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns lines terminated with LF character" )
	public void read_ReturnsLinesTerminatedWithLfCharacter( Test.Case tc ) {
		String line = new String( buffer, 0, this.fillBuffer() );
		tc.assertTrue( line.endsWith( "\n" ) );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Return is at most count" )
	public void read_ReturnIsAtMostCount( Test.Case tc ) {
		for ( int i = 0; i < 10; i++ ) {
			tc.assertTrue( this.reader.read( buffer, 0, 5) <= 5 );
		}
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for negative offset" )
	public void read_ThrowsAssertionErrorForNegativeOffset( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( buffer,  -1,  10 );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for null buffer" )
	public void read_ThrowsAssertionErrorForNullBuffer( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( null,  0,  10 );
	}

	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for offset + count > length" )
	public void read_ThrowsAssertionErrorForOffsetCountLength( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( buffer,  10,  buffer.length );
	}

	@Test.Impl( member = "public void StreamReader.close()", description = "Close after close is no op" )
	public void close_CloseAfterCloseIsNoOp( Test.Case tc ) {
		this.reader.close();
		this.reader.close();
		tc.pass();
	}


	
}
