/**
 * Copyright (C) 2021, 2023
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */
package test.sog.util;


import sog.core.App;
import sog.core.Test;
import sog.util.StreamReader;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class StreamReaderTest extends Test.Container {

	public StreamReaderTest() {
		super( StreamReader.class );
	}
	

//	private static final String[] lines = {
//		"First line.",
//		"Second line",
//		"Last line"
//	};
//	
//	private StreamReader reader;
//	
//	private char[] buffer = new char[100];
//	
//	@Override public Procedure beforeEach() { return () -> reader = new StreamReader( Stream.of( lines ) ); }
//	
//	@Override public Procedure afterEach() { return () -> reader = null; }
//	
//	public int fillBuffer() { return this.reader.read( this.buffer,  0,  this.buffer.length ); }
//
//	
//	// Test implementations
//	
//	@SuppressWarnings( "resource" )
//	@Test.Impl( member = "public StreamReader(Stream)", description = "Throws Assertion Error for null stream" )
//	public void StreamReader_ThrowsAssertionErrorForNullStream( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		new StreamReader( null );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns -1 after close()" )
//	public void read_Returns1AfterClose( Test.Case tc ) {
//		this.reader.close();
//		tc.assertEqual( -1,  this.fillBuffer() );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns -1 at end of stream" )
//	public void read_Returns1AtEndOfStream( Test.Case tc ) {
//		int iterations = 0;  // To guard against infinite while on bad implementation
//		while ( iterations < 100 && this.fillBuffer() != -1 ) {
//			iterations++;
//		}
//		tc.assertTrue( iterations < 100 );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Returns lines terminated with LF character" )
//	public void read_ReturnsLinesTerminatedWithLfCharacter( Test.Case tc ) {
//		String line = new String( buffer, 0, this.fillBuffer() );
//		tc.assertTrue( line.endsWith( "\n" ) );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Return is at most count" )
//	public void read_ReturnIsAtMostCount( Test.Case tc ) {
//		for ( int i = 0; i < 10; i++ ) {
//			tc.assertTrue( this.reader.read( buffer, 0, 5) <= 5 );
//		}
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for negative offset" )
//	public void read_ThrowsAssertionErrorForNegativeOffset( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		this.reader.read( buffer,  -1,  10 );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for null buffer" )
//	public void read_ThrowsAssertionErrorForNullBuffer( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		this.reader.read( null,  0,  10 );
//	}
//
//	@Test.Impl( member = "public int StreamReader.read(char[], int, int)", description = "Throws Assertion Error for offset + count > length" )
//	public void read_ThrowsAssertionErrorForOffsetCountLength( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		this.reader.read( buffer,  10,  buffer.length );
//	}
//
//	@Test.Impl( member = "public void StreamReader.close()", description = "Close after close is no op" )
//	public void close_CloseAfterCloseIsNoOp( Test.Case tc ) {
//		this.reader.close();
//		this.reader.close();
//	}

	
	
	
	// TEST CASES

    @Test.Impl( 
    	member = "constructor: StreamReader(Stream)", 
    	description = "Throws AssertionError for null stream" 
    )
    public void tm_06BDBCDB1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Return is at most count" 
    )
    public void tm_05CBF339D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Returns -1 after close()" 
    )
    public void tm_008303DE8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Returns -1 at end of stream" 
    )
    public void tm_01412437A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Returns lines terminated with LF character" 
    )
    public void tm_0CDF8B790( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Throws Assertion Error for negative offset" 
    )
    public void tm_07962B0F0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Throws Assertion Error for null buffer" 
    )
    public void tm_073456FCB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int StreamReader.read(char[], int, int)", 
    	description = "Throws Assertion Error for offset + count > length" 
    )
    public void tm_0DEC8FD4D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StreamReader.close()", 
    	description = "Close after close is no op" 
    )
    public void tm_094B736CF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( StreamReader.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( StreamReader.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

	
}
