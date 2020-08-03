/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Strings represented by soft references to byte arrays.
 * 
 * NOT thread safe
 */
public class SoftString implements Comparable<SoftString> {

	// Where the persistent data resides
	private static class Location {

		private static final List<ByteFile> BYTE_FILES = new ArrayList<ByteFile>();
		
		private final int fileIndex;
		private final int offset;
		private final int length;
		
		private Location( String s ) {
			byte[] data = s.getBytes();
			
			ByteFile file = null;
			int i = 0;
			while ( file == null && i < BYTE_FILES.size() ) {
				if ( BYTE_FILES.get( i ).canAppend( data.length ) ) {
					file = BYTE_FILES.get( i );
				} else {
					i++;
				}
			}
			if ( file == null ) {
				file = new ByteFile();
				BYTE_FILES.add( file );
			}
			
			this.fileIndex = i;
			this.offset = file.append( data );
			this.length = data.length;
		}
		
		private String get() {
			return new String( BYTE_FILES.get( this.fileIndex ).read( this.offset, this.length ) );
		}
	}

	
	// Configurable minimum string length for soft references
	private static Integer THRESHOLD = Property.get( "threshold", 50, Property.INTEGER );

	
	
	private final String hard;
	
	private final Location location;
	
	private SoftReference<String> ref;

	/**
	 * Create string reference; if the length is less than {@code THRESHOLD} use a
	 * hard reference, otherwise use a soft reference backed by disk storage.
	 *  
	 * @param s
	 * 		The string to represent
	 * @param threshold
	 * 		Strings this length or longer are held via a soft reference and can be GC'd
	 */
	@TestOrig.Decl( "Throws assertion error for null strings" )
	@TestOrig.Decl( "Can construct empty" )
	@TestOrig.Decl( "Can construct short strings" )
	@TestOrig.Decl( "Can construct long strings" )
	@TestOrig.Decl( "Correct value after collection" )
	@TestOrig.Decl( "Strings longer than threshold are soft" )
	public SoftString( String s ) {
		Assert.nonNull( s );
		
		if ( s.length() < SoftString.THRESHOLD ) {
			this.hard = s;
			this.location = null;
			this.ref = null;
		} else {
			this.hard = null;
			this.location = new Location( s );
			this.ref = new SoftReference<String>( s );
		}
	}
	
	@Override
	@TestOrig.Decl( "Stress test correct value" )
	public String toString() {
		String result;
		
		if ( this.location == null ) {
			result = this.hard;
		} else {
			result = this.ref.get();
			if ( result == null ) {  // value has been collected
				result = this.location.get();
				this.ref = new SoftReference<>( result );
			}
		}
		
		return result;
	}
	
	@Override
	@TestOrig.Decl( "Can sort large collections" )
	public int compareTo( SoftString other ) {
		return this.toString().compareTo( other.toString() );
	}
	
	@Override
	@TestOrig.Decl( "Sample cases equal" )
	@TestOrig.Decl( "Sample cases not equal" )
	@TestOrig.Decl( "Consistent with compare" )
	public boolean equals( Object other ) {
		boolean result;
		if ( other == null || !SoftString.class.equals( other.getClass() ) ) {
			result = false;
		} else {
			result = this.compareTo( (SoftString)other ) == 0;
		}
		return result;
	}
	
	@Override
	@TestOrig.Decl( "Sample cases equal" )
	public int hashCode() {
		return this.toString().hashCode();
	}

	
	
	
}
