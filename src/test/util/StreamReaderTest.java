/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.stream.Stream;

import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.StreamReader;

/**
 * @author sundquis
 *
 */
public class StreamReaderTest implements TestContainer {

	private static final String[] lines = {
		"First line.",
		"Second line",
		"Last line"
	};
	
	private StreamReader reader;
	
	private char[] buffer = new char[100];

	@Override public Class<?> subjectClass() { return StreamReader.class; }
	
	@Override public Procedure beforeEach() { return () -> reader = new StreamReader( Stream.of( lines ) ); }
	
	@Override public Procedure afterEach() { return () -> reader = null; }
	
	public int fillBuffer() { return this.reader.read( this.buffer,  0,  this.buffer.length ); }

	
	// Test implementations
	
	@SuppressWarnings( "resource" )
	@TestOrig.Impl( src = "public StreamReader(Stream)", desc = "Throws Assertion Error for null stream" )
	public void StreamReader_ThrowsAssertionErrorForNullStream( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new StreamReader( null );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Returns -1 after close()" )
	public void read_Returns1AfterClose( TestCase tc ) {
		this.reader.close();
		tc.assertEqual( -1,  this.fillBuffer() );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Returns -1 at end of stream" )
	public void read_Returns1AtEndOfStream( TestCase tc ) {
		int iterations = 0;  // To guard against infinite while on bad implementation
		while ( iterations < 100 && this.fillBuffer() != -1 ) {
			iterations++;
		}
		tc.assertTrue( iterations < 100 );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Returns lines terminated with LF character" )
	public void read_ReturnsLinesTerminatedWithLfCharacter( TestCase tc ) {
		String line = new String( buffer, 0, this.fillBuffer() );
		tc.assertTrue( line.endsWith( "\n" ) );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Return is at most count" )
	public void read_ReturnIsAtMostCount( TestCase tc ) {
		for ( int i = 0; i < 10; i++ ) {
			tc.assertTrue( this.reader.read( buffer, 0, 5) <= 5 );
		}
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Throws Assertion Error for negative offset" )
	public void read_ThrowsAssertionErrorForNegativeOffset( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( buffer,  -1,  10 );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Throws Assertion Error for null buffer" )
	public void read_ThrowsAssertionErrorForNullBuffer( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( null,  0,  10 );
	}

	@TestOrig.Impl( src = "public int StreamReader.read(char[], int, int)", desc = "Throws Assertion Error for offset + count > length" )
	public void read_ThrowsAssertionErrorForOffsetCountLength( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.reader.read( buffer,  10,  buffer.length );
	}

	@TestOrig.Impl( src = "public void StreamReader.close()", desc = "Close after close is no op" )
	public void close_CloseAfterCloseIsNoOp( TestCase tc ) {
		this.reader.close();
		this.reader.close();
		tc.pass();
	}


	

	public static void main(String[] args) {

		System.out.println();

		new TestOrig(StreamReaderTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
