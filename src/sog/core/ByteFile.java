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
package sog.core;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Implement byte array behavior for large arrays, up to 1 billion bytes.
 * 
 * Not thread-safe
 */
@Test.Subject( "test." )
public class ByteFile implements AutoCloseable {
	
	/* For defining lengths in terms of 1 gig */
	private static long GB = 1_000_000_000L;

	/* Configurable max data file length in bytes (1 GB default) */
	private static long MAX_LENGTH = Property.get( "maxLength", 1L * GB, Parser.LONG );

	/*
	 * Configurable data usage limit.
	 * When the total (across all ByteFlie instances) data usage reaches this limit, calls to
	 * add additional bytes trigger warning messages.
	 */
	private static long WARN_LIMIT = Property.get( "warnLimit", 2L * GB, Parser.LONG );
	
	/*
	 * Configurable data usage limit.
	 * When the total (across all ByteFlie instances) data usage reaches this limit, the nex
	 * call to add additional bytes triggers an Error.
	 */
	private static long FAIL_LIMIT = Property.get( "failLimit", 5L * GB, Parser.LONG );
	

	/*
	 * The total (across all ByteFil instances) number of bytes being stored.
	 * Used to trigger warnings and Errors
	 */
	private static volatile long TOTAL_BYTES = 0L;

	/* Adjust the total number of bytes used and trigger warning or error if needed. */
	private static void newBytes( int count ) {
		TOTAL_BYTES += (long) count;
		if ( TOTAL_BYTES > WARN_LIMIT ) {
			Fatal.warning( "Total bytes stored exceeds " + WARN_LIMIT );
		}
		if ( TOTAL_BYTES > FAIL_LIMIT ) {
			Fatal.error( "Total bytes stored exceeds " + FAIL_LIMIT );
		}
	}
	
	
	
	
	/* Temporary file holding the data as raw bytes. */
	private File file;
	
	/* Current length of the file in bytes. */
	private int length;
	
	/** 
	 * Construct an empty {@code ByteFile} backed by a temporary file.
	 * The temporary file is automatically deleted when the JVM exists.
	 * 
	 * @throws IOException
	 * 		If the temporary file cannot be constructed.
	 */
	@Test.Decl( "Creates empty file" )
	@Test.Decl( "Creates open file" )
	public ByteFile() {
		this.file = new LocalDir().sub( "tmp" ).getTmpFile( "BYTES" );
		this.length = 0;
	}
	
	/**
	 * The current length in bytes
	 * @return
	 */
	@Test.Decl( "Length increases with write" )
	@Test.Decl( "Length is non-negative" )
	@Test.Decl( "Length is at most MAX_LENGTH" )
	public int length() {
		return this.length;
	}
	
	/**
	 * Determine if this {@code ByteFile} can write {@code count} bytes at {@code position}.
	 * 
	 * @param position
	 * @param count
	 * @return
	 */
	@Test.Decl( "True for small count" )
	@Test.Decl( "False for large count" )
	@Test.Decl( "False for large position" )
	@Test.Decl( "False for disposed" )
	public boolean canWrite( int position, int count ) {
		return this.isOpen() && (long) position + count <= MAX_LENGTH;
	}

	/**
	 * Write data from the source buffer into this {@code ByteFile}
	 * 
	 * @param position
	 * 		Start location in this ByteFile for the source bytes
	 * @param src
	 * 		Bytes to be written
	 * @param offset
	 * 		Start location in {@code src} of data 
	 * @param count
	 * 		Number of bytes to write
	 * @throws IOException
	 * 		If {@code RandomAccessFile} operations fail or if the maximum length is exceeded.
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for negative position" )
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for negative offset" )
	@Test.Decl( "Throws AssertionError for offset greater or equal to length of source" )
	@Test.Decl( "Throws AssertionError for negative count" )
	@Test.Decl( "Throws AssertionError for offset + count > source.length" )
	@Test.Decl( "Throws AssertionError for position + count > MAX_LENGTH" )
	@Test.Decl( "At warn limit issues warning" )
	@Test.Decl( "At fail limit throws AppException" )
	@Test.Decl( "Increases length if position + count > length" )
	@Test.Decl( "Does not increase length if position + count <= length" )
	@Test.Decl( "Increases total bytes if position + count > length" )
	@Test.Decl( "Does not increase total bytes if position + count <= length" )
	@Test.Decl( "Can recover bytes written at position zero" )
	@Test.Decl( "Can recover bytes written at positive position" )
	@Test.Decl( "Can recover bytes written with positive offset" )
	public void write( int position, byte[] src, int offset, int count ) {
		Assert.isTrue( this.isOpen() );
		Assert.nonNeg( position );
		Assert.nonNull( src );
		Assert.nonNeg( offset );
		Assert.lessThan( offset, src.length );
		Assert.lessThanOrEqual( offset + count, src.length );
		Assert.nonNeg( count );
		
		if ( count == 0 ) {
			return;
		}
		
		int oldLength = this.length;
		int newLength = Math.max( this.length, position + count );
		Assert.isTrue( (long) newLength <= ByteFile.MAX_LENGTH );
		
		try ( RandomAccessFile raf = new RandomAccessFile( this.file, "rw" ) ) {
			Assert.isTrue( (long) oldLength == raf.length() );
			raf.seek( (long) position );
			raf.write( src, offset, count );
			Assert.isTrue( (long) newLength == raf.length() );
			this.length = newLength;
			ByteFile.newBytes( newLength - oldLength );
		} catch ( FileNotFoundException e ) {
			Fatal.impossible( "Framework should ensure tmp files exist." );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}

	/**
	 * Write the entire contents of the buffer into this {@code ByteFile}.
	 * 
	 * @param position
	 * 		Start location in this ByteFile for the source bytes
	 * @param src
	 * 		Bytes to be written
	 * @throws IOException
	 * 		If {@code RandomAccessFile} operations fail or if the maximum length is exceeded.
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for negative position" )
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for position + source.length > MAX_LENGTH" )
	@Test.Decl( "At warn limit issues warning" )
	@Test.Decl( "At fail limit throws AppException" )
	@Test.Decl( "Increases length if position + source.length > length" )
	@Test.Decl( "Does not increase length if position + source.length <= length" )
	@Test.Decl( "Increases total bytes if position + source.length > length" )
	@Test.Decl( "Does not increase total bytes if position + source.length <= length" )
	@Test.Decl( "Can recover bytes written at position zero" )
	@Test.Decl( "Can recover bytes written at positive position" )
	public void write( int position, byte[] src ) {
		this.write( position, Assert.nonNull( src ), 0, src.length );
	}
	
	/**
	 * Determine if this {@code ByteFile} can hold {@code count} new bytes.
	 * 
	 * @param count
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for negative count" )
	@Test.Decl( "True for small count" )
	@Test.Decl( "False for large count" )
	@Test.Decl( "False for disposed" )
	public boolean canAppend( int count ) {
		Assert.nonNeg( count );
		return this.isOpen() && (long) this.length + count <= MAX_LENGTH;
	}

	/**
	 * Add to the end of this {@code ByteFile}
	 * 
	 * @param src
	 * @param offset
	 * @param count
	 * @return
	 * 		The position in this {@code ByteFile} where the write starts
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for negative offset" )
	@Test.Decl( "Throws AssertionError for offset greater or equal to length of source" )
	@Test.Decl( "Throws AssertionError for negative count" )
	@Test.Decl( "Throws AssertionError for offset + count > source.length" )
	@Test.Decl( "Throws AssertionError for length + count > MAX_LENGTH" )
	@Test.Decl( "At warn limit issues warning" )
	@Test.Decl( "At fail limit throws AppException" )
	@Test.Decl( "Increases length by count" )
	@Test.Decl( "Increases total bytes by count" )
	@Test.Decl( "Can recover bytes written with zero offset" )
	@Test.Decl( "Can recover bytes written with positive offset" )
	public int append( byte[] src, int offset, int count ) {
		int position = this.length;
		this.write( position, Assert.nonNull( src ), Assert.nonNeg( offset ), Assert.nonNeg( count ) );
		return position;
	}
	
	/**
	 * Add to the end of this {@code ByteFile}
	 * 
	 * @param src
	 * @return
	 * 		The position in this {@code ByteFile} where the write starts
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for length + source.length > MAX_LENGTH" )
	@Test.Decl( "At warn limit issues warning" )
	@Test.Decl( "At fail limit throws AppException" )
	@Test.Decl( "Increases length by source.length" )
	@Test.Decl( "Increases total bytes by source.length" )
	@Test.Decl( "Can recover bytes" )
	public int append( byte[] src ) {
		return this.append( Assert.nonNull( src ), 0, src.length );
	}
	
	/**
	 * Determine if this {@code ByteFile} can read {@code count} bytes at {@code position}.
	 * 
	 * @param position
	 * @param count
	 * @return
	 */
	@Test.Decl( "True for small count" )
	@Test.Decl( "False for large count" )
	@Test.Decl( "False for large position" )
	@Test.Decl( "False for disposed" )
	public boolean canRead( int position, int count ) {
		return this.isOpen() && (long) position + count <= this.length;
	}

	/**
	 * Read data from this {@code ByteFile} into the destination buffer.
	 * 
	 * @param position
	 * 		Location in this {@code ByteFile} to start reading.
	 * @param dest
	 * 		Buffer to read into.
	 * @param offset
	 * 		Start position in the buffer.
	 * @param count
	 * 		Number of bytes to read.
	 * @throws IOException
	 * 		If {@code RandomAccessFile} operations fail or if the maximum length is exceeded.
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for negative position" )
	@Test.Decl( "Throws AssertionError for null destination" )
	@Test.Decl( "Throws AssertionError for negative offset" )
	@Test.Decl( "Throws AssertionError for offset greater than length of destination" )
	@Test.Decl( "Throws AssertionError for offset + count > destination.length" )
	@Test.Decl( "Throws AssertionError for negative count" )
	@Test.Decl( "Throws AssertionError for position + count > length" )
	@Test.Decl( "Read is idempotent" )
	@Test.Decl( "Read is consistent with write" )
	public void read( int position, byte[] dest, int offset, int count ) {
		Assert.isTrue( this.isOpen() );
		Assert.nonNull( dest );
		Assert.nonNeg( position );
		Assert.lessThanOrEqual( position + count, this.length );  // Read beyond end of file
		Assert.nonNeg( offset );
		Assert.lessThanOrEqual( offset + count, dest.length );
		Assert.nonNeg( count );
		
		if ( count == 0 ) {
			return;
		}
		
		try ( RandomAccessFile raf = new RandomAccessFile( this.file, "r" ) ) {
			raf.seek( position );
			raf.read( dest, offset, count );
		} catch ( FileNotFoundException e ) {
			Fatal.impossible( "Framework should ensure tmp files exist." );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	
	/**
	 * Construct a new {@code byte} array and read into it.
	 * 
	 * @param position
	 * 		Location in this {@code ByteFile} to start reading.
	 * @param count
	 * 		Number of bytes to read.
	 * @return
	 * 		The newly constructed byte array
	 */
	@Test.Decl( "Throws AssertionError for disposed" )
	@Test.Decl( "Throws AssertionError for negative position" )
	@Test.Decl( "Throws AssertionError for negative count" )
	@Test.Decl( "Throws AssertionError for position + count > length" )
	@Test.Decl( "Read is idempotent" )
	@Test.Decl( "Read is consistent with write" )
	public byte[] read( int position, int count ) {
		byte[] result = new byte[ Assert.nonNeg( count ) ];
		this.read( position, result, 0, count );
		return result;
	}
	/**
	 * @return
	 * 		The current length of the file in bytes
	 */
	@Test.Decl( "Agrees with length of underlying physical file" )
	public int getLength() {
		return this.length;
	}
	
	/** Determine if this {@code ByteFile} can accept read/write requests */
	@Test.Decl( "True for new" )
	@Test.Decl( "False after dispose" )
	public boolean isOpen() {
		return this.file != null;
	}
	
	/** Close this {@code ByteFile} and release resources. */
	@Test.Decl( "Releases resources" )
	@Test.Decl( "Idempotent" )
	public void dispose() {
		if ( this.file != null && this.file.delete() ) {
			ByteFile.newBytes( -1 * this.length );
		}
		this.file = null;
	}

	@Override
	@Test.Decl( "Indicates length" )
	public String toString() {
		return ( this.isOpen() ? this.file.getAbsolutePath() : "CLOSED" ) + "(Length = " + this.length  + ")";
	}

	@Override
	@Test.Decl( "Idempotent" )
	public void close() {
		this.dispose();
	}
	

}
