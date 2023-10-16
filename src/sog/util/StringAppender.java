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

package sog.util;

import java.io.PrintWriter;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
@FunctionalInterface
public interface StringAppender extends AutoCloseable {
	
	public StringAppender append( String s );
	
	public default StringAppender appendln( String s ) {
		return this.append( s ).append( System.lineSeparator() );
	}
	
	public default StringAppender appendln() {
		return this.append( System.lineSeparator() );
	}
	
	/* Subclasses override as needed. */
	public default void close() {}
	
	
	public static StringAppender wrap( final PrintWriter out ) {
		return new StringAppender() {

			@Override
			public StringAppender append( String s ) {
				out.print( s );
				out.flush();
				return this;
			}
			
			@Override
			public void close() {
				out.flush();
				out.close();
			}
			
		};
	}
	
	
	public static StringAppender wrap( final StringBuilder buf ) {
		return new StringAppender() {

			@Override
			public StringAppender append( String s ) {
				buf.append( s );
				return this;
			}
			
			@Override
			public String toString() {
				return buf.toString();
			}
			
		};
	}
	

}
