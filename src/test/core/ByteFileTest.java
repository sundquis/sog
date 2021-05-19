/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;

import sog.core.AppException;
import sog.core.ByteFile;
import sog.core.Procedure;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class ByteFileTest extends Test.Implementation {


	private byte[] BYTES = "A fairly long string that we use for testing".getBytes();
	private byte[] BUF;
	private ByteFile bf;
	
	private long maxLength;
	private long warnLimit;
	private long failLimit;
	
	@Override
	public Procedure beforeAll() {
		return () -> {
			this.maxLength = this.getSubjectField( null,  "MAX_LENGTH", 0L );
			this.warnLimit = this.getSubjectField( null,  "WARN_LIMIT", 0L );
			this.failLimit = this.getSubjectField( null,  "FAIL_LIMIT", 0L );
		};
	}
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			bf = new ByteFile(); 
			BUF = new byte[100]; 
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.setMaxLength( this.maxLength );
			this.setWarnLimit( this.warnLimit );
			this.setFailLimit( this.failLimit );
			bf.dispose();
			bf = null;
			BUF = null;
		};
	}
	
	public long getTotalBytes() {
		return this.getSubjectField( null,  "TOTAL_BYTES",  0L );
	}
	
	public void setMaxLength( long maxLength ) {
		this.setSubjectField( null,  "MAX_LENGTH", maxLength );
	}
	
	public void setWarnLimit( long warnLimit ) {
		this.setSubjectField( null,  "WARN_LIMIT", warnLimit );
	}
	
	public void setFailLimit( long failLimit ) {
		this.setSubjectField( null,  "FAIL_LIMIT", failLimit );
	}

	
	
	@Test.Impl( member = "public ByteFile()", description = "Creates empty file" )
	public void ByteFile_CreatesEmptyFile( Test.Case tc ) {
		tc.assertEqual( 0, bf.length() );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "At fail limit throws AppException" )
	public void write_AtFailLimitThrowsAppexception( Test.Case tc ) {
		this.setFailLimit( 200L );
		tc.expectError( AppException.class );
		bf.write( 190,  BYTES, 0, 20 );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "At warn limit issues warning" )
	public void write_AtWarnLimitIssuesWarning( Test.Case tc ) {
		// TOGGLE
		/* */	tc.addMessage( "Manually tested" );	/*
		// SHOULD SEE: "WARNING: Total bytes stored exceeds 100" ...
		this.setWarnLimit( 100L );
		bf.write( 90,  BYTES, 0, 12 );
		tc.assertTrue( bf.length() > 100 );
		// */
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Beyond max length throws AssertionError" )
	public void write_BeyondMaxLengthThrowsAssertionerror( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.write( (int) this.maxLength - 10,  BYTES, 0, 20 );
	}
	
	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Increases length" )
	public void write_IncreasesLength( Test.Case tc ) {
		int len = bf.length();
		bf.write( len + 10,  BYTES, 0, 20 );
		tc.assertEqual( bf.length(), len + 30 );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Throws AssertionError for illegal count" )
	public void write_ThrowsAssertionerrorForIllegalCount( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.write( 10,  BYTES, 0, 100);
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Throws AssertionError for negative offset" )
	public void write_ThrowsAssertionerrorForNegativeOffset( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.write( 0,  BYTES, -1, 5 );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Throws AssertionError for offset too big" )
	public void write_ThrowsAssertionerrorForOffsetTooBig( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.write( 0,  BYTES, 0, 100 );
	}
	
	@Test.Impl( member = "public int ByteFile.append(byte[], int, int)", description = "Increases length by count" )
	public void add_IncreasesLengthByCount( Test.Case tc ) {
		bf.write( 42,  BYTES );
		int len = bf.length();
		bf.append( BYTES, 5, 10 );
		tc.assertEqual( bf.length(), len + 10 );
	}
	
	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Is consistent with write" , weight = 5 )
	public void read_IsConsistentWithWrite( Test.Case tc ) {
		String[] args = {
			"The first string.",
			"Four score and seven years ago..",
			"A long time ago, in a alaxy far, far away...",
			"In a hole in, in the ground, there lived a Hobbit.",
			"The answer to the ultimate question of life, the universe, and everything."
		};
		int[] positions = { 13, 117, 319, 523, 729 };
		for ( int i = 0; i < 5; i++ ) {
			bf.write( positions[i],  args[i].getBytes(), 0, 10 );
		}
		boolean pass = true;
		for ( int i = 0; i < 5; i++ ) {
			bf.read( positions[i],  BUF, 0, 10 );
			pass &= args[i].substring(0,  10).equals( new String( BUF, 0, 10 ) );
		}
		tc.assertTrue( pass );
	}
	
	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for count too big" )
	public void read_ThrowsAssertionerrorForCountTooBig( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( 0, BUF, 0, 100 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for negative count" )
	public void read_ThrowsAssertionerrorForNegativeCount( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( 0, BUF, 0, -1 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for negative offset" )
	public void read_ThrowsAssertionerrorForNegativeOffset( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( 0, BUF, -1, 100 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for offset too big" )
	public void read_ThrowsAssertionerrorForOffsetTooBig( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( 0, BUF, 100, 1 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError on read past EOF" )
	public void read_ThrowsAssertionerrorOnReadPastEof( Test.Case tc ) {
		bf.append( BYTES );
		tc.expectError( AssertionError.class );
		bf.read( BYTES.length - 5, BUF, 0, 6 );
	}
				
	@Test.Impl( member = "public byte[] ByteFile.read(int, int)", description = "Is consistent with write", weight = 5 )
	public void read2_IsConsistentWithWrite( Test.Case tc ) {
		String[] args = {
			"The first string.",
			"Four score and seven years ago..",
			"A long time ago, in a alaxy far, far away...",
			"In a hole in, in the ground, there lived a Hobbit.",
			"The answer to the ultimate question of life, the universe, and everything."
		};
		int[] positions = { 13, 117, 319, 523, 729 };
		for ( int i = 0; i < 5; i++ ) {
			bf.write( 1000 + positions[i],  args[i].getBytes() );
		}
		byte[] b;
		boolean pass = true;
		for ( int i = 0; i < 5; i++ ) {
			b = bf.read( 1000 + positions[i],  args[i].length() );
			pass &= args[i].equals( new String( b ) );
		}
		tc.assertTrue( pass );
	}

	@Test.Impl( member = "public String ByteFile.toString()", description = "Indicates length" )
	public void toString_IndicatesLength( Test.Case tc ) {
		bf.append( "12345".getBytes() );
		tc.assertEqual( 5, bf.length() );
	}

	@Test.Impl( member = "public boolean ByteFile.canAppend(int)", description = "False for large count", weight = 2 )
	public void canAppend_FalseForLargeCount( Test.Case tc ) {
		this.setMaxLength( 1000L );
		tc.assertFalse( bf.canAppend( 1001) );
		bf.write( 500,  BYTES );
		tc.assertFalse( bf.canAppend( 1001) );
	}
	
	@Test.Impl( member = "public boolean ByteFile.canAppend(int)", description = "True for small count", weight = 2 )
	public void canAppend_TrueForSmallCount( Test.Case tc ) {
		this.setMaxLength( 1000L );
		tc.assertTrue( bf.canAppend( 1000 ) );
		tc.assertTrue( bf.canAppend( 800 ) );
	}
	
	@Test.Impl( member = "public boolean ByteFile.isOpen()", description = "False after dispose" )
	public void isOpen_FalseAfterDispose( Test.Case tc ) {
		bf.dispose();
		tc.assertFalse( bf.isOpen() );
	}
	
	@Test.Impl( member = "public boolean ByteFile.isOpen()", description = "True for new" )
	public void isOpen_TrueForNew( Test.Case tc ) {
		tc.assertTrue( bf.isOpen() );
	}

	@Test.Impl( member = "public void ByteFile.dispose()", description = "Releases resources" )
	public void dispose_ReleasesResources( Test.Case tc ) {
		ByteFile other = new ByteFile();
		other.append( BYTES );
		long total1 = this.getTotalBytes();
		bf.append( BYTES );
		long total2 = this.getTotalBytes();
		bf.dispose();
		long total3 = this.getTotalBytes();
		tc.assertFalse( total1 == total2 );
		tc.assertEqual( total1, total3 );
	}
				
	@Test.Impl( member = "public int ByteFile.append(byte[])", description = "Increases length by src length" )
	public void add_IncreasesLengthBySrcLength( Test.Case tc ) {
		int len = bf.length();
		bf.append( BYTES );
		tc.assertEqual( len + BYTES.length, bf.length() );
	}
				
	@Test.Impl( member = "public int ByteFile.append(byte[])", description = "Increases total bytes by count" )
	public void add_IncreasesTotalBytesByCount( Test.Case tc ) {
		long total1 = this.getTotalBytes();
		bf.append( BYTES );
		bf.append( BYTES );
		long total2 = this.getTotalBytes();
		tc.assertEqual( total1 + 2 * BYTES.length, total2 );
	}
	
	@Test.Impl( member = "public int ByteFile.append(byte[], int, int)", description = "Increases total bytes by count" )
	public void add2_IncreasesTotalBytesByCount( Test.Case tc ) {
		long total1 = this.getTotalBytes();
		bf.append( BYTES,  0, 12 );
		bf.append( BYTES,  0, 14 );
		bf.append( BYTES,  0, 16 );
		long total2 = this.getTotalBytes();
		tc.assertEqual( total1 + 42, total2 );
	}
	
	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Increases total bytes" )
	public void write_IncreasesTotalBytes( Test.Case tc ) {
		long total1 = this.getTotalBytes();
		bf.write( 110, BYTES, 0, 20 );
		long total2 = this.getTotalBytes();
		tc.assertEqual( total1 + 130, total2 );
	}
	
	@Test.Impl( member = "public ByteFile()", description = "Creates open file" )
	public void ByteFile_CreatesOpenFile( Test.Case tc ) {
		tc.assertTrue( bf.isOpen() );
	}

	@Test.Impl( member = "public boolean ByteFile.canAppend(int)", description = "False for disposed" )
	public void canAppend_FalseForDisposed( Test.Case tc ) {
		bf.append( BYTES );
		tc.assertTrue( bf.canAppend( 10 ) );
		bf.dispose();
		tc.assertFalse( bf.canAppend( 10 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canRead(int, int)", description = "False for disposed" )
	public void canRead_FalseForDisposed( Test.Case tc ) {
		bf.append( BYTES );
		tc.assertTrue( bf.canRead( 10, 10 ) );
		bf.dispose();
		tc.assertFalse( bf.canRead( 10, 10 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canRead(int, int)", description = "False for large count" )
	public void canRead_FalseForLargeCount( Test.Case tc ) {
		bf.append( BYTES );
		tc.assertFalse( bf.canRead( 0,  100 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canRead(int, int)", description = "False for large position" )
	public void canRead_FalseForLargePosition( Test.Case tc ) {
		bf.append( BYTES );
		tc.assertFalse( bf.canRead( 100,  0 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canRead(int, int)", description = "True for small count" )
	public void canRead_TrueForSmallCount( Test.Case tc ) {
		bf.append( BYTES );
		tc.assertTrue( bf.canRead( 10,  10 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canWrite(int, int)", description = "False for disposed" )
	public void canWrite_FalseForDisposed( Test.Case tc ) {
		bf.write( 10,  BYTES );
		tc.assertTrue( bf.canWrite( 20,  20 ) );
		bf.dispose();
		tc.assertFalse( bf.canWrite( 20,  20 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canWrite(int, int)", description = "False for large count" )
	public void canWrite_FalseForLargeCount( Test.Case tc ) {
		tc.assertFalse( bf.canWrite( 0,  (int) this.maxLength + 1 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canWrite(int, int)", description = "False for large position" )
	public void canWrite_FalseForLargePosition( Test.Case tc ) {
		tc.assertTrue( bf.canWrite( (int) this.maxLength, 0 ) );
		tc.assertFalse( bf.canWrite( (int) this.maxLength + 1, 0 ) );
	}

	@Test.Impl( member = "public boolean ByteFile.canWrite(int, int)", description = "True for small count" )
	public void canWrite_TrueForSmallCount( Test.Case tc ) {
		tc.assertTrue( bf.canWrite( 0,  (int) this.maxLength ) );
	}

	@Test.Impl( member = "public int ByteFile.length()", description = "Length increases" )
	public void length_LengthIncreases( Test.Case tc ) {
		bf.write( 20, BYTES, 2, 10 );
		int length = bf.length();
		bf.append( BYTES );
		tc.assertTrue( bf.length() > length );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for disposed" )
	public void read_ThrowsAssertionerrorForDisposed( Test.Case tc ) {
		bf.append( BYTES );
		bf.read( 0,  BYTES, 0, 10 );
		bf.dispose();
		tc.expectError( AssertionError.class );
		bf.read( 0,  BYTES, 0, 10 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for negative position" )
	public void read_ThrowsAssertionerrorForNegativePosition( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( -2,  BYTES, 0, 10 );
	}

	@Test.Impl( member = "public void ByteFile.read(int, byte[], int, int)", description = "Throws AssertionError for null destination" )
	public void read_ThrowsAssertionerrorForNullDestination( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.read( 0,  null, 0, 10 );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Same length for small write" )
	public void write_SameLengthForSmallWrite( Test.Case tc ) {
		bf.append( BYTES );
		bf.append( BYTES );
		int length = bf.length();
		bf.write( 10,  BYTES, 0, 10 );
		tc.assertEqual( length,  bf.length() );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Throws AssertionError for disposed" )
	public void write_ThrowsAssertionerrorForDisposed( Test.Case tc ) {
		bf.write( 0,  BYTES, 0, 10 );
		tc.expectError( AssertionError.class );
		bf.dispose();
		bf.write( 0,  BYTES, 0, 10 );
	}

	@Test.Impl( member = "public void ByteFile.write(int, byte[], int, int)", description = "Throws AssertionError for null source" )
	public void write_ThrowsAssertionerrorForNullSource( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		bf.write( 0,  null, 0, 10 );
	}

	
	
}
