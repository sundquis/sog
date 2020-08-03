/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
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
public class ByteFile  {
	
	private static long GB = 1000L * 1000L * 1000L;

	// Configurable max data file length in bytes (1 GB default)
	private static long MAX_LENGTH = Property.get( "maxLength", 1 * GB, Property.LONG );

	// Configurable data usage limit to issue warning (2 GB default)
	private static long WARN_LIMIT = Property.get( "warnLimit", 2 * GB, Property.LONG );
	
	// Configurable data usage limit to shut down ByteFile (5 GB default)
	private static long FAIL_LIMIT = Property.get( "failLimit", 5 * GB, Property.LONG );
	

	// Used to monitor total disk usage and signal warning or failure
	private static volatile long TOTAL_BYTES = 0L;

	private static void newBytes( int count ) {
		TOTAL_BYTES += (long) count;
		if ( TOTAL_BYTES > WARN_LIMIT ) {
			Fatal.warning( "Total bytes stored exceeds " + WARN_LIMIT );
		}
		if ( TOTAL_BYTES > FAIL_LIMIT ) {
			Fatal.error( "Total bytes stored exceeds " + FAIL_LIMIT );
		}
	}
	
	
	
	
	// Temporary file holding data
	private File file;
	
	// Current length in bytes
	private int length;
	
	/** 
	 * Construct an empty {@code ByteFile} backed by a temporary file.
	 * The temporary file is automatically deleted when the JVM exists.
	 * 
	 * @throws IOException
	 * 		If the temporary file cannot be constructed.
	 */
	@TestOrig.Decl( "Creates empty file" )
	@TestOrig.Decl( "Creates open file" )
	public ByteFile() {
		this.file = new LocalDir().sub( "tmp" ).getTmpFile( "BYTES" );
		this.length = 0;
	}
	
	/**
	 * The current length in bytes
	 * @return
	 */
	@TestOrig.Decl( "Length increases" )
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
	@TestOrig.Decl( "True for small count" )
	@TestOrig.Decl( "False for large count" )
	@TestOrig.Decl( "False for large position" )
	@TestOrig.Decl( "False for disposed" )
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
	@TestOrig.Decl( "Increases length" )
	@TestOrig.Decl( "Same length for small write" )
	@TestOrig.Decl( "Beyond max length throws AssertionError" )
	@TestOrig.Decl( "At warn limit issues warning" )
	@TestOrig.Decl( "At fail limit throws AppException" )
	@TestOrig.Decl( "Throws AssertionError for disposed" )
	@TestOrig.Decl( "Throws AssertionError for null source" )
	@TestOrig.Decl( "Throws AssertionError for negative offset" )
	@TestOrig.Decl( "Throws AssertionError for offset too big" )
	@TestOrig.Decl( "Throws AssertionError for illegal count" )
	@TestOrig.Decl( "Increases total bytes" )
	public void write( int position, byte[] src, int offset, int count ) {
		Assert.isTrue( this.isOpen() );
		Assert.nonNull( src );
		Assert.isTrue( offset >= 0 );
		Assert.isTrue( offset < src.length );
		Assert.isTrue( offset + count <= src.length );
		
		int oldLength = this.length;
		int newLength = Math.max( this.length, position + count );
		Assert.isTrue( (long) newLength <= MAX_LENGTH );
		
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
	@TestOrig.Skip
	public void write( int position, byte[] src ) {
		this.write( position, src, 0, src.length );
	}
	
	/**
	 * Determine if this {@code ByteFile} can hold {@code count} new bytes.
	 * 
	 * @param count
	 * @return
	 */
	@TestOrig.Decl( "True for small count" )
	@TestOrig.Decl( "False for large count" )
	@TestOrig.Decl( "False for disposed" )
	public boolean canAppend( int count ) {
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
	@TestOrig.Decl( "Increases length by count" )
	@TestOrig.Decl( "Increases total bytes by count" )
	public int append( byte[] src, int offset, int count ) {
		int position = this.length;
		this.write( position, src, offset, count );
		return position;
	}
	
	/**
	 * Add to the end of this {@code ByteFile}
	 * 
	 * @param src
	 * @return
	 * 		The position in this {@code ByteFile} where the write starts
	 */
	@TestOrig.Decl( "Increases length by src length" )
	@TestOrig.Decl( "Increases total bytes by count" )
	public int append( byte[] src ) {
		return this.append( src, 0, src.length );
	}
	
	/**
	 * Determine if this {@code ByteFile} can erad {@code count} bytes at {@code position}.
	 * 
	 * @param position
	 * @param count
	 * @return
	 */
	@TestOrig.Decl( "True for small count" )
	@TestOrig.Decl( "False for large count" )
	@TestOrig.Decl( "False for large position" )
	@TestOrig.Decl( "False for disposed" )
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
	@TestOrig.Decl( "Throws AssertionError for disposed" )
	@TestOrig.Decl( "Throws AssertionError for null destination" )
	@TestOrig.Decl( "Throws AssertionError for negative position" )
	@TestOrig.Decl( "Throws AssertionError on read past EOF" )
	@TestOrig.Decl( "Throws AssertionError for negative offset" )
	@TestOrig.Decl( "Throws AssertionError for offset too big" )
	@TestOrig.Decl( "Throws AssertionError for count too big" )
	@TestOrig.Decl( "Throws AssertionError for negative count" )
	@TestOrig.Decl( "Is consistent with write" )
	public void read( int position, byte[] dest, int offset, int count ) {
		Assert.isTrue( this.isOpen() );
		Assert.nonNull( dest );
		Assert.isTrue( position >= 0 );
		Assert.isTrue( position + count <= this.length );  // Read beyond end of file
		Assert.isTrue( offset >= 0 );
		Assert.isTrue( offset + count <= dest.length );
		Assert.isTrue( count >= 0 );
		
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
	@TestOrig.Decl( "Is consistent with write" )
	public byte[] read( int position, int count ) {
		byte[] result = new byte[ count ];
		this.read( position, result, 0, count );
		return result;
	}
	
	/** Determine if this {@code ByteFile} can accept read/write requests */
	@TestOrig.Decl( "True for new" )
	@TestOrig.Decl( "False after dispose" )
	public boolean isOpen() {
		return this.file != null;
	}
	
	/** Close this {@code ByteFile} and release resources. */
	@TestOrig.Decl( "Releases resources" )
	public void dispose() {
		if ( this.file != null && this.file.delete() ) {
			ByteFile.newBytes( -1 * this.length );
		}
		this.file = null;
	}

	@Override
	@TestOrig.Decl( "Indicates length" )
	public String toString() {
		return this.file.getAbsolutePath() + "(Length = " + this.length + ")";
	}
	

}
