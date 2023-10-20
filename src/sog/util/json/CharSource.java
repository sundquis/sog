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

package sog.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class CharSource implements AutoCloseable {

	
	
	protected CharSource() {}
	
	public abstract boolean hasChar();

	public abstract char curChar() throws IOException;

	public abstract void advance() throws IOException;
	
	@Override
	public abstract void close() throws IOException;

	
	
	public static CharSource forInputStream( InputStream input ) {
		return new InputStreamCharSource( input );
	}
	
	private static class InputStreamCharSource extends CharSource {
		
		private final InputStream input;
		
		private final BufferedReader buf;
		
		private InputStreamCharSource( InputStream input ) {
			this.input = input;
			this.buf = new BufferedReader( new InputStreamReader( input, Charset.forName( "UTF-8" ) ) );
		}

		@Override
		public boolean hasChar() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public char curChar() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void advance() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}
		
	}
	

}
