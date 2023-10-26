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


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Strings represented by soft references backed by byte arrays.
 * 
 * For applications that need a large number of long Strings. A SoftString uses a collectible
 * soft reference to the String (the payload) and a reference to ByteFile location that
 * maintains the String across collections.
 * 
 * NOT thread safe
 */
@Test.Subject( "test." )
public class SoftString implements Comparable<SoftString> {

	/* Where the persistent data resides */
	private static class Location {

		private static final List<ByteFile> BYTE_FILES = new ArrayList<ByteFile>();
		
		private final int fileIndex;
		private final int offset;
		private final int length;
				
		private Location( String s ) {
			byte[] data = s.getBytes();

			synchronized ( Location.BYTE_FILES ) {
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
		}
	
		@Override
		@Test.Decl( "Consistent with constructed value" )
		public String toString() {
			return new String( BYTE_FILES.get( this.fileIndex ).read( this.offset, this.length ) );
		}
	}

	
	/* Configurable minimum string length for soft references */
	private static Integer THRESHOLD = Property.get( "threshold", 50, Parser.INTEGER );

	
	
	private final String hard;
	
	private final Location location;
	
	private SoftReference<String> soft;

	/**
	 * Create string reference; if the length is less than {@code THRESHOLD} use a
	 * hard reference, otherwise use a soft reference backed by disk storage.
	 *  
	 * @param s
	 * 		The string to represent
	 */
	@Test.Decl( "Throws assertion error for null strings" )
	@Test.Decl( "Can construct empty" )
	@Test.Decl( "Can construct short strings" )
	@Test.Decl( "Can construct long strings" )
	@Test.Decl( "Strings longer or equal to threshold are soft" )
	public SoftString( String s ) {
		Assert.nonNull( s );
		
		if ( s.length() < SoftString.THRESHOLD ) {
			this.hard = s;
			this.location = null;
			this.soft = null;
		} else {
			this.hard = null;
			this.location = new Location( s );
			this.soft = new SoftReference<String>( s );
		}
	}
	
	@Override
	@Test.Decl( "Stress test correct value" )
	@Test.Decl( "Result is not null" )
	@Test.Decl( "Consistent with constructed value" )
	@Test.Decl( "Correct value after collection" )
	public String toString() {
		String result = null;
		
		if ( this.location == null ) {
			result = this.hard;
		} else {
			result = this.soft.get();
			if ( result == null ) {  // value has been collected
				result = this.location.toString();
				this.soft = new SoftReference<>( result );
			}
		}
		
		return Assert.nonNull( result );
	}
	
	@Override
	@Test.Decl( "Can sort large collections" )
	@Test.Decl( "Consistent with String.compareTo()" )
	public int compareTo( SoftString other ) {
		return this.toString().compareTo( other.toString() );
	}
	
	@Override
	@Test.Decl( "SampleAction cases equal" )
	@Test.Decl( "SampleAction cases not equal" )
	@Test.Decl( "If equals then compareTo is zero" )
	@Test.Decl( "If compareTo is zero then equals" )
	public boolean equals( Object other ) {
		boolean result = false;
		
		if ( other != null && this.getClass().equals( other.getClass() ) ) {
			result = this.toString().equals( other.toString() );
		}

		return result;
	}
	
	@Override
	@Test.Decl( "If equals then hashCodes are the same" )
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	
}
