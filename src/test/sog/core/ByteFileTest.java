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
package test.sog.core;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.AppRuntime;
import sog.core.ByteFile;
import sog.core.Procedure;
import sog.core.Test;
import sog.util.Fault;

/**
 * 
 */
@Test.Skip( "Container" )
public class ByteFileTest extends Test.Container {
	
	private static final long LIMIT = 10_000;

	private long ORIG_MAX_LENGTH;
	private long ORIG_WARN_LIMIT;
	private long ORIG_FAIL_LIMIT;
	
	private final long NEW_MAX_LENGTH = LIMIT;
	private final long NEW_WARN_LIMIT = LIMIT * 2;
	private final long NEW_FAIL_LIMIT = LIMIT * 5;
	
	public ByteFileTest() {
		super( ByteFile.class );
	}
	
	public Procedure beforeAll() {
		return () -> {
			this.ORIG_MAX_LENGTH = this.getSubjectField( null, "MAX_LENGTH", null );
			this.ORIG_WARN_LIMIT = this.getSubjectField( null, "WARN_LIMIT", null );
			this.ORIG_FAIL_LIMIT = this.getSubjectField( null, "FAIL_LIMIT", null );

			// Reduce limits to make it easier to trigger exceptional cases
			this.setSubjectField( null, "MAX_LENGTH", this.NEW_MAX_LENGTH );
			this.setSubjectField( null, "WARN_LIMIT", this.NEW_WARN_LIMIT );
			this.setSubjectField( null, "FAIL_LIMIT", this.NEW_FAIL_LIMIT );
		};
	}
	
	@Override
	public Procedure afterAll() {
		return () -> {
			this.setSubjectField( null, "MAX_LENGTH", this.ORIG_MAX_LENGTH );
			this.setSubjectField( null, "WARN_LIMIT", this.ORIG_WARN_LIMIT );
			this.setSubjectField( null, "FAIL_LIMIT", this.ORIG_FAIL_LIMIT );
		};
	}
	
	public long getMaxLength() {
		return this.getSubjectField( null, "MAX_LENGTH", null );
	}
	
	public long getTotalBytes() {
		return this.getSubjectField( null, "TOTAL_BYTES", null );
	}

	public File getFile( ByteFile bf ) {
		return this.getSubjectField( bf, "file", null );
	}
	
	/* Test cases can assume all strings (and their corresponding byte arrays) have length at least 10. */
	public final String[] ARGS = new String[] { 
		"Hello world!",
		"The second string.",
		"Four score and seven years ago..",
		"A long time ago, in a alaxy far, far away...",
		"In a hole in, in the ground, there lived a Hobbit.",
		"The answer to the ultimate question of life, the universe, and everything."
	};

	/* Reusable byte array to read and append. */
	public final byte[] DATA = ARGS[ARGS.length - 1].getBytes();
	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: ByteFile()", 
		description = "Creates empty file" 
	)
	public void tm_014932A17( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertEqual( 0, bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "constructor: ByteFile()", 
		description = "Creates open file" 
	)
	public void tm_0DB0B5700( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.isOpen() );
		}
	}
		
	@Test.Impl( 
		member = "method: String ByteFile.toString()", 
		description = "Indicates length" 
	)
	public void tm_0A0B2B06B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "some bytes to write".getBytes() );
			tc.assertTrue( bf.toString().contains( "" + bf.getLength() ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canAppend(int)", 
		description = "False for disposed" 
	)
	public void tm_039DF6E7A( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.dispose();
			tc.assertFalse( bf.canAppend( 0 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canAppend(int)", 
		description = "False for large count" 
	)
	public void tm_0F7F0AA8B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertFalse( bf.canAppend( (int) this.NEW_MAX_LENGTH + 1 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canAppend(int)", 
		description = "Throws AssertionError for negative count" 
	)
	public void tm_0D13BDEE3( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.canAppend( 0 );
			bf.canAppend( 1 );
			tc.expectError( AssertionError.class );
			bf.canAppend( -1 );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canAppend(int)", 
		description = "True for small count" 
	)
	public void tm_0B52185CE( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.canAppend( (int) this.NEW_MAX_LENGTH ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canRead(int, int)", 
		description = "False for disposed" 
	)
	public void tm_033B5AF97( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.canRead( 0, 0 ) );
			bf.dispose();
			tc.assertFalse( bf.canRead( 0, 0 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canRead(int, int)", 
		description = "False for large count" 
	)
	public void tm_0C3F3F84E( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "a few bytes".getBytes() );
			tc.assertFalse( bf.canRead( 0, 100 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canRead(int, int)", 
		description = "False for large position" 
	)
	public void tm_0D7780E60( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "a few bytes".getBytes() );
			tc.assertFalse( bf.canRead( 100, 0 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canRead(int, int)", 
		description = "True for small count" 
	)
	public void tm_0926BF3AB( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "a few bytes".getBytes() );
			tc.assertTrue( bf.canRead( 0, 2 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canWrite(int, int)", 
		description = "False for disposed" 
	)
	public void tm_06B6A482E( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.canWrite( 0, 10 ) );
			bf.dispose();
			tc.assertFalse( bf.canWrite( 0, 10 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canWrite(int, int)", 
		description = "False for large count" 
	)
	public void tm_0451D0457( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertFalse( bf.canWrite( 1, (int) this.NEW_MAX_LENGTH ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canWrite(int, int)", 
		description = "False for large position" 
	)
	public void tm_05F2799B7( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertFalse( bf.canWrite( (int) this.NEW_MAX_LENGTH, 1 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.canWrite(int, int)", 
		description = "True for small count" 
	)
	public void tm_0AF5CC282( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.canWrite( 1, 10 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.isOpen()", 
		description = "False after dispose" 
	)
	public void tm_06F37E2B0( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.isOpen() );
			bf.dispose();
			tc.assertFalse( bf.isOpen() );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean ByteFile.isOpen()", 
		description = "True for new" 
	)
	public void tm_07BA1F9BB( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertTrue( bf.isOpen() );
		}
	}
		
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Read is idempotent" 
	)
	public void tm_0BB72BBCD( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "some random data hopefully at least 10 bytes".getBytes() );
			byte[] data = bf.read( 3, 10 );
			tc.assertEqual( data, bf.read( 3, 10 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Read is consistent with write" 
	)
	public void tm_09853AA15( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			List<Integer> positions = Stream.of( this.ARGS )
				.map( String::getBytes )
				.map( bf::append )
				.collect( Collectors.toList() );
			for ( int i = 0; i < this.ARGS.length; i++ ) {
				tc.assertEqual( this.ARGS[i], new String( bf.read( positions.get( i ), ARGS[i].length() ) ) );
			}
		}
	}
	
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_0C0731D4B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "some random data hopefully at least 10 bytes".getBytes() );
			bf.read( 0, 10 );
			tc.expectError( AssertionError.class );
			bf.dispose();
			bf.read( 0, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Throws AssertionError for negative count" 
	)
	public void tm_01EEE848A( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.read( 0, -1 );
		}
	}
		
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Throws AssertionError for negative position" 
	)
	public void tm_021FD48A4( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.read(-1, 0 );
		}
	}
		
	@Test.Impl( 
		member = "method: byte[] ByteFile.read(int, int)", 
		description = "Throws AssertionError for position + count > length" 
	)
	public void tm_0C0A28E55( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( "random data".getBytes() );
			tc.expectError( AssertionError.class );
			bf.read( 10, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "At fail limit throws AppException",
		threadsafe = false
	)
	public void tm_06694C1DC( Test.Case tc ) {
		// Fail limit is 5 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile(); 
			ByteFile bf4 = new ByteFile(); 
			ByteFile bf5 = new ByteFile(); 
			ByteFile bf6 = new ByteFile(); 
		) {
			int position = (int) this.NEW_MAX_LENGTH - this.DATA.length;
			bf1.write( position, this.DATA );
			bf2.write( position, this.DATA );
			bf3.write( position, this.DATA );
			bf4.write( position, this.DATA );
			bf5.write( position, this.DATA );
			tc.expectError( AppRuntime.class );
			bf6.append( this.DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "At warn limit issues warning",
		threadsafe = false
	)
	public void tm_084F9DB69( Test.Case tc ) {
		// Warn limit is 2 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile() 
		) {
			Consumer<Fault> listener = f -> tc.assertPass();
			tc.afterThis( () -> Fault.removeListener( listener ) );
			int position = (int) this.NEW_MAX_LENGTH - DATA.length;
			bf1.write( position, DATA );
			bf2.write( position, DATA );
			Fault.addListener( listener );
			bf3.write( 0, DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Can recover bytes" 
	)
	public void tm_0BC7914D4( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			String message = "String to append";
			bf.write( 100, "some data".getBytes() );
			int position = bf.append( message.getBytes() );
			tc.assertEqual( message, new String( bf.read( position, message.length() ) ) );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Increases length by source.length"
	)
	public void tm_0392704D0( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 100, "some random data".getBytes() );
			int length = bf.getLength();
			byte[] data = "message to append".getBytes();
			bf.append( data );
			tc.assertEqual( length + data.length, bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Increases total bytes by source.length",
		threadsafe = false
	)
	public void tm_0A0B685D1( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			long TOTAL_BYTES = this.getSubjectField( null, "TOTAL_BYTES", null );
			byte[] data = "message to append".getBytes();
			bf.append( data );
			tc.assertEqual( TOTAL_BYTES + data.length, this.getSubjectField( null, "TOTAL_BYTES", null ) );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_07F406F24( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( this.DATA );
			bf.dispose();
			tc.expectError( AssertionError.class );
			bf.append( this.DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Throws AssertionError for length + source.length > MAX_LENGTH",
		threadsafe = false
	)
	public void tm_09BF6C210( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( (int) this.NEW_MAX_LENGTH - this.DATA.length, this.DATA );
			tc.expectError( AssertionError.class );
			bf.append( this.DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[])", 
		description = "Throws AssertionError for null source" 
	)
	public void tm_0A69EEC0B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.append( null );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "At fail limit throws AppException",
		threadsafe = false
	)
	public void tm_0C770D63C( Test.Case tc ) {
		// Fail limit is 5 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile(); 
			ByteFile bf4 = new ByteFile(); 
			ByteFile bf5 = new ByteFile(); 
			ByteFile bf6 = new ByteFile(); 
		) {
			int position = (int) this.NEW_MAX_LENGTH - this.DATA.length;
			bf1.write( position, this.DATA );
			bf2.write( position, this.DATA );
			bf3.write( position, this.DATA );
			bf4.write( position, this.DATA );
			bf5.write( position, this.DATA );
			tc.expectError( AppRuntime.class );
			bf6.append( this.DATA, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "At warn limit issues warning",
		threadsafe = false
	)
	public void tm_0FD3A8B09( Test.Case tc ) {
		// Warn limit is 2 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile() 
		) {
			Consumer<Fault> listener = f -> tc.assertPass();
			tc.afterThis( () -> Fault.removeListener( listener ) );
			int position = (int) this.NEW_MAX_LENGTH - DATA.length;
			bf1.write( position, DATA );
			bf2.write( position, DATA );
			Fault.addListener( listener );
			bf3.append( DATA, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Can recover bytes written with positive offset" 
	)
	public void tm_0ED2DF4EB( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 100, this.DATA );
			int position = bf.append( this.DATA, 5, 7 );
			tc.assertEqual( new String( this.DATA, 5, 7 ), new String( bf.read( position, 7 ) ) );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Can recover bytes written with zero offset" 
	)
	public void tm_03021001C( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 123, this.DATA );
			int position = bf.append( this.DATA, 0, 7 );
			tc.assertEqual( new String( this.DATA, 0, 7 ), new String( bf.read( position, 7 ) ) );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Increases length by count"
	)
	public void tm_0E16E6CA6( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 37, this.DATA );
			int length = bf.getLength();
			bf.append( this.DATA, 3, 11 );
			tc.assertEqual( length + 11, bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Increases total bytes by count",
		threadsafe = false
	)
	public void tm_011D851E7( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 43, this.DATA );
			int count = 9;
			long TOTAL_BYTES = this.getTotalBytes();
			bf.append( this.DATA, 2, count );
			tc.assertEqual( TOTAL_BYTES + count, this.getTotalBytes() );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_039E6E6C4( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.append( this.DATA, 5, 9 );
			bf.dispose();
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, 5, 9 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for length + count > MAX_LENGTH",
		threadsafe = false
	)
	public void tm_05A8EB9BA( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			int count = 8;
			bf.write( (int) this.NEW_MAX_LENGTH - count, this.DATA, 0, 1 );
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, 1, count );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for negative count" 
	)
	public void tm_03B798F43( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, 1, -1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for negative offset" 
	)
	public void tm_01CD67DF5( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, -1, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for null source" 
	)
	public void tm_05899D06B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.append( null, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for offset + count > source.length" 
	)
	public void tm_022A4ADC3( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, 10, 100 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.append(byte[], int, int)", 
		description = "Throws AssertionError for offset greater or equal to length of source" 
	)
	public void tm_084B33E10( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			int length = this.DATA.length;
			tc.expectError( AssertionError.class );
			bf.append( this.DATA, length, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.getLength()", 
		description = "Agrees with length of underlying physical file" 
	)
	public void tm_0C681DE75( Test.Case tc ) {
		try ( 
			ByteFile bf = new ByteFile();
			RandomAccessFile raf = new RandomAccessFile( this.getFile( bf ), "r" )
		) {
			Stream.of( 27, 11, 92, 78, 76, 100 ).forEach( n -> bf.write( n, this.DATA ) );
			tc.assertEqual( bf.getLength(), (int) raf.length() );
		} catch ( Exception e ) {
			throw new AppRuntime(e);
		} 
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.length()", 
		description = "Length increases with write"
	)
	public void tm_0F4C54CF9( final Test.Case tc ) {
		try ( final ByteFile bf = new ByteFile() ) {
			Consumer<Integer> process = new Consumer<Integer>() {
				int previous = 0;
				@Override 
				public void accept( Integer p ) {
					bf.write( p, DATA );
					tc.assertTrue( this.previous <= bf.getLength() );
					this.previous = bf.getLength();
				}
			};
			Stream.of( 27, 11, 92, 78, 76, 100, 105, 130, 100 ).forEach( process );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.length()", 
		description = "Length is at most MAX_LENGTH",
		threadsafe = false
	)
	public void tm_07C049F58( Test.Case tc ) {
		try ( final ByteFile bf = new ByteFile() ) {
			Consumer<Integer> process = new Consumer<Integer>() {
				@Override
				public void accept( Integer p ) {
					try { bf.write( p, DATA ); } catch ( AssertionError e ) {}
					tc.assertTrue( bf.getLength() <= ByteFileTest.this.NEW_MAX_LENGTH );
				}
			};
			Stream.of( 27, 11, 92, 98, 105, 130, 100, (int) this.NEW_MAX_LENGTH - DATA.length ).forEach( process );
			tc.assertEqual( (int) this.NEW_MAX_LENGTH, bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "method: int ByteFile.length()", 
		description = "Length is non-negative",
		threadsafe = false
	)
	public void tm_0D0A12D7C( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			Stream.of( 0, 27, 11, 92, 78, 76, 98, (int) NEW_MAX_LENGTH - DATA.length )
				.map( p -> { bf.write( p, DATA ); return bf.getLength(); } )
				.forEach( l -> tc.assertTrue( l >= 0 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.close()", 
		description = "Idempotent" 
	)
	public void tm_058F727A9( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 1234, DATA );

			bf.close();
			int length = bf.getLength();
			boolean isOpen = bf.isOpen();
			String toString = bf.toString();

			bf.close();
			tc.assertEqual( length, bf.getLength() );
			tc.assertEqual( isOpen, bf.isOpen() );
			tc.assertEqual( toString, bf.toString() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.dispose()", 
		description = "Idempotent" 
	)
	public void tm_0AC8E5262( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 3962, DATA );

			bf.dispose();
			int length = bf.getLength();
			boolean isOpen = bf.isOpen();
			String toString = bf.toString();

			bf.dispose();
			tc.assertEqual( length, bf.getLength() );
			tc.assertEqual( isOpen, bf.isOpen() );
			tc.assertEqual( toString, bf.toString() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.dispose()", 
		description = "Releases resources" 
	)
	public void tm_0E39C3F7A( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.assertNonNull( this.getFile( bf ) );
			bf.dispose();
			tc.assertIsNull( this.getFile( bf ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Read is consistent with write" 
	)
	public void tm_04254FE82( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			List<Integer> positions = Stream.of( this.ARGS )
				.map( String::getBytes ).map( bf::append ).collect( Collectors.toList() );
			byte[] buffer = new byte[ 100 ];
			for ( int i = 0; i < this.ARGS.length; i++ ) {
				int length = ARGS[i].length();
				bf.read( positions.get( i ), buffer, 0, length );
				tc.assertEqual( this.ARGS[i], new String( buffer, 0, length ) );
			}
		}
	}

	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Read is idempotent" 
	)
	public void tm_0A659E980( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 17,  DATA );
			int length = bf.getLength();
			byte[] buffer1 = new byte[ length ];
			byte[] buffer2 = new byte[ length ];
			bf.read( 0, buffer1, 0, length );
			bf.read( 0, buffer2, 0, length );
			tc.assertEqual( buffer1, buffer2 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_0402684FE( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			bf.read( 0, buffer, 0, 10 );
			bf.dispose();
			tc.expectError( AssertionError.class );
			bf.read( 0, buffer, 0, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for negative count" 
	)
	public void tm_06B3419FD( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( 0, buffer, 0, -1 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for negative offset" 
	)
	public void tm_0E46D4A7B( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( 0, buffer, -1, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for negative position" 
	)
	public void tm_0F787DA51( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( -1, buffer, 0, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for null destination" 
	)
	public void tm_0BB7617CE( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.read( 0, null, 0, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for offset + count > destination.length" 
	)
	public void tm_0D760FF38( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( 0, buffer, 5, 6 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for offset greater than length of destination" 
	)
	public void tm_077990149( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( 0, buffer, 11, 0 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.read(int, byte[], int, int)", 
		description = "Throws AssertionError for position + count > length" 
	)
	public void tm_0614B2302( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			int length = bf.getLength();
			byte[] buffer = new byte[10];
			tc.expectError( AssertionError.class );
			bf.read( length - 9, buffer, 0, 10 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "At fail limit throws AppException",
		threadsafe = false
	)
	public void tm_0C420EAF9( Test.Case tc ) {
		// Fail limit is 5 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile(); 
			ByteFile bf4 = new ByteFile(); 
			ByteFile bf5 = new ByteFile(); 
			ByteFile bf6 = new ByteFile(); 
		) {
			int position = (int) this.NEW_MAX_LENGTH - this.DATA.length;
			bf1.write( position, this.DATA );
			bf2.write( position, this.DATA );
			bf3.write( position, this.DATA );
			bf4.write( position, this.DATA );
			bf5.write( position, this.DATA );
			tc.expectError( AppRuntime.class );
			bf6.write( 0, this.DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "At warn limit issues warning",
		threadsafe = false
	)
	public void tm_01BD5B42C( Test.Case tc ) {
		// Warn limit is 2 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile() 
		) {
			Consumer<Fault> listener = f -> tc.assertPass();
			tc.afterThis( () -> Fault.removeListener( listener ) );
			int position = (int) this.NEW_MAX_LENGTH - DATA.length;
			bf1.write( position, DATA );
			bf2.write( position, DATA );
			Fault.addListener( listener );
			bf3.write( 0, DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Can recover bytes written at position zero" 
	)
	public void tm_0AE9F4920( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA );
			tc.assertEqual( new String( DATA ), new String( bf.read( 0, DATA.length ) ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Can recover bytes written at positive position" 
	)
	public void tm_085005E11( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 31, DATA );
			tc.assertEqual( new String( DATA ), new String( bf.read( 31, DATA.length ) ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Does not increase length if position + source.length <= length" 
	)
	public void tm_0E9047889( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 111, DATA );
			int length = bf.getLength();
			bf.write( 100, DATA );
			tc.assertEqual( length, bf.length() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Does not increase total bytes if position + source.length <= length",
		threadsafe = false
	)
	public void tm_05DB95FAC( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 97, DATA );
			long length = this.getTotalBytes();
			bf.write( 95, DATA );
			tc.assertEqual( length, this.getTotalBytes() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Increases length if position + source.length > length" 
	)
	public void tm_066AE3875( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 111, DATA );
			int length = bf.getLength();
			bf.write( 123, DATA );
			tc.assertTrue( length < bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Increases total bytes if position + source.length > length",
		threadsafe = false
	)
	public void tm_040BD17DC( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 97, DATA );
			long length = this.getTotalBytes();
			bf.write( 195, DATA );
			tc.assertTrue( length < this.getTotalBytes() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_0D33969A7( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 97, DATA );
			bf.dispose();
			tc.expectError( AssertionError.class );
			bf.write( 97, DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Throws AssertionError for negative position" 
	)
	public void tm_065F123C8( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( -1, DATA );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Throws AssertionError for null source" 
	)
	public void tm_0A1873EA8( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 0, null );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[])", 
		description = "Throws AssertionError for position + source.length > MAX_LENGTH",
		threadsafe = false
	)
	public void tm_042C274CA( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( (int) NEW_MAX_LENGTH, DATA, 0, 0 );
			tc.expectError( AssertionError.class );
			bf.write( (int) NEW_MAX_LENGTH, DATA, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "At fail limit throws AppException",
		threadsafe = false
	)
	public void tm_02485FF19( Test.Case tc ) {
		// Fail limit is 5 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile(); 
			ByteFile bf4 = new ByteFile(); 
			ByteFile bf5 = new ByteFile(); 
			ByteFile bf6 = new ByteFile(); 
		) {
			int position = (int) this.NEW_MAX_LENGTH - this.DATA.length;
			bf1.write( position, this.DATA );
			bf2.write( position, this.DATA );
			bf3.write( position, this.DATA );
			bf4.write( position, this.DATA );
			bf5.write( position, this.DATA );
			tc.expectError( AppRuntime.class );
			bf6.write( 0, DATA, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "At warn limit issues warning",
		threadsafe = false
	)
	public void tm_05F5C8C0C( Test.Case tc ) {
		// Warn limit is 2 * max length
		try ( 
			ByteFile bf1 = new ByteFile(); 
			ByteFile bf2 = new ByteFile(); 
			ByteFile bf3 = new ByteFile() 
		) {
			Consumer<Fault> listener = f -> tc.assertPass();
			tc.afterThis( () -> Fault.removeListener( listener ) );
			int position = (int) this.NEW_MAX_LENGTH - DATA.length;
			bf1.write( position, DATA );
			bf2.write( position, DATA );
			Fault.addListener( listener );
			bf3.write( 0, DATA, 0, 1 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Can recover bytes written at position zero" 
	)
	public void tm_035DED900( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 0, DATA, 0, 6 );
			tc.assertEqual( Arrays.copyOfRange( DATA, 0, 6 ), bf.read( 0, 6 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Can recover bytes written at positive position" 
	)
	public void tm_04034FDF1( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 92, DATA, 0, 8 );
			tc.assertEqual( Arrays.copyOfRange( DATA, 0, 8 ), bf.read( 92, 8 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Can recover bytes written with positive offset" 
	)
	public void tm_0DE30DB2E( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 92, DATA, 3, 8 );
			tc.assertEqual( Arrays.copyOfRange( DATA, 3, 11 ), bf.read( 92, 8 ) );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Does not increase length if position + count <= length" 
	)
	public void tm_0A0F7045F( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 145, DATA );
			int length = bf.getLength();
			bf.write( 112, DATA, 4, 11 );
			tc.assertEqual( length, bf.getLength() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Does not increase total bytes if position + count <= length",
		threadsafe = false
	)
	public void tm_0723A2AC2( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 145, DATA );
			long total = this.getTotalBytes();
			bf.write( 112, DATA, 4, 11 );
			tc.assertEqual( total, this.getTotalBytes() );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Increases length if position + count > length"
	)
	public void tm_0EEDE24DF( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 145, DATA, 4, 11 );
			int length = bf.getLength();
			bf.write( 182, DATA, 4, 11 );
			tc.assertTrue( bf.getLength() > length );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Increases total bytes if position + count > length",
		threadsafe = false
	)
	public void tm_05E542706( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 145, DATA, 4, 11 );
			long total = this.getTotalBytes();
			bf.write( 167, DATA, 4, 11 );
			tc.assertTrue( this.getTotalBytes() > total );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for disposed" 
	)
	public void tm_07F76D987( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( 145, DATA, 4, 11 );
			bf.dispose();
			tc.expectError( AssertionError.class );
			bf.write( 145, DATA, 4, 11 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for negative count" 
	)
	public void tm_0C92B23C6( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 10, DATA, 5, -20 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for negative offset" 
	)
	public void tm_0455779D2( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 10, DATA, -5, 20 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for negative position" 
	)
	public void tm_0C6A38FE8( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( -10, DATA, 5, 20 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for null source" 
	)
	public void tm_0630542C8( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 10, null, 5, 20 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for offset + count > source.length" 
	)
	public void tm_0A99ADC46( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 10, DATA, 3, DATA.length );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for offset greater or equal to length of source" 
	)
	public void tm_0EC35FC6D( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			tc.expectError( AssertionError.class );
			bf.write( 10, DATA, DATA.length, 0 );
		}
	}
		
	@Test.Impl( 
		member = "method: void ByteFile.write(int, byte[], int, int)", 
		description = "Throws AssertionError for position + count > MAX_LENGTH",
		threadsafe = false
	)
	public void tm_056D8DE34( Test.Case tc ) {
		try ( ByteFile bf = new ByteFile() ) {
			bf.write( (int) NEW_MAX_LENGTH, DATA, 0, 0 );
			tc.expectError( AssertionError.class );
			bf.write( (int) NEW_MAX_LENGTH, DATA, 0, 1 );
		}
	}
	
	
	

	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( ByteFile.class )
			.concurrent( true )
			.showDetails( true )
			.print();
		//*/
		
		//* Toggle package results
		Test.evalPackage( ByteFile.class )
			.concurrent( true )
			.showDetails( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
}
