/**
 */

package sog.util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class StringOutputStream extends OutputStream {

	private final List<Byte> bytes = new ArrayList<Byte>();
	private String value = null;
	
	public StringOutputStream() {}

	@Override
	public void write( int b ) {
		if ( this.value != null ) {
			return;
		}
		this.bytes.add( (byte) b );
	}
	
	@Override
	public void close() {
		if ( this.value == null ) {
			byte[] data = new byte[this.bytes.size()];
			for ( int i = 0; i < data.length; i++ ) {
				data[i] = this.bytes.get( i );
			}
			this.value = new String( data );
		}
	}
	
	@Override
	public void flush() {}
	
	@Override
	public String toString() {
		this.close();
		return this.value;
	}
	
	public void reset() {
		this.value = null;
		this.bytes.clear();
	}
	

	
	public static void main( String[] args ) {
		StringOutputStream sos = new StringOutputStream();
		IndentWriter iw = new IndentWriter( sos );
		iw.println( "one" );
		iw.println( "two" );
		iw.println( "three" );
		System.out.println( sos );
		iw.println( "four" );
		System.out.println( sos );
	}
	
}
