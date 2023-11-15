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

package sog.core.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class PrimitiveWriter implements AutoCloseable {
	
	private final BufferedWriter buf;
	
	public PrimitiveWriter( BufferedWriter buf ) {
		this.buf = Assert.nonNull( buf );
	}
	
	public PrimitiveWriter( Writer writer ) {
		this( new BufferedWriter( Assert.nonNull( writer ) ) );
	}
	
	public PrimitiveWriter( OutputStream out ) {
		this( new OutputStreamWriter( out, Charset.forName( "UTF-8" ) ) );
	}
	
	public PrimitiveWriter( Path path ) throws IOException {
		this( Files.newBufferedWriter( path, 
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE ) );
	}
	
	
	public void append( char c ) throws IOException {
		this.buf.append( c );
	}
	
	
	public void writeString( String javaString ) throws IOException {
		this.buf.append( '"' );

		int start = 0;
		int current = 0;
		char c = 0;
		int length = javaString.length();
		
		while ( current < length ) {
			c = javaString.charAt( current );
			switch (c) {
			case '\\':
			case '"':
			case '/':
				this.buf.append( javaString, start, current ).append( '\\' );
				start = current++;
				break;
			case '\b':
				this.buf.append( javaString, start, current ).append( "\\b" );
				start = ++current;
				break;
			case '\f':
				this.buf.append( javaString, start, current ).append( "\\f" );
				start = ++current;
				break;
			case '\n':
				this.buf.append( javaString, start, current ).append( "\\n" );
				start = ++current;
				break;
			case '\r':
				this.buf.append( javaString, start, current ).append( "\\r" );
				start = ++current;
				break;
			case '\t':
				this.buf.append( javaString, start, current ).append( "\\t" );
				start = ++current;
				break;
			default:
				current++;
			}
		}
		
		this.buf.append( javaString, start, length ).append( '"' );
	}

	
	public void writeNumber( Number number ) throws IOException {
		this.buf.append( number.toString() );
	}


	public void writeBoolean( Boolean bool ) throws IOException {
		this.buf.append( bool ? JSON.TRUE.toString() : JSON.FALSE.toString() );
	}

	
	public void writeNull() throws IOException {
		this.buf.append( JSON.NULL.toString() );
	}
	
	
	public void flush() throws IOException {
		this.buf.flush();
	}
	

	@Override
	public void close() throws IOException {
		this.buf.flush();
		this.buf.close();
	}
	

}
