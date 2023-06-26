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

/**
 * The base application exception type
 *
 * Depends on:
 * 		NONE
 */
@Test.Subject( "test." )
public class AppException extends RuntimeException {

	private static final long serialVersionUID = -2314875945481995828L;

	/** Constructs an exception with empty detail message. */
	@Test.Skip( "No testing required" )
	public AppException() {
		super();
	}

	/** Constructs an exception with specified detail message. */
	@Test.Decl( "Throws AsserionError for null message" )
	@Test.Decl( "Throws AsserionError for empty message" )
	public AppException( String msg ) {
		super( Assert.nonEmpty( msg ) );
	}

	/** Constructs an exception with specified cause. */
	@Test.Decl( "Throws AsserionError for null cause" )
	public AppException( Throwable cause ) {
		super( Assert.nonNull( cause ) );
	}

	/** Constructs an exception with specified detail message and cause. */
	@Test.Decl( "Throws AsserionError for null message" )
	@Test.Decl( "Throws AsserionError for empty message" )
	@Test.Decl( "Throws AsserionError for null cause" )
	public AppException( String msg, Throwable cause ) {
		super( Assert.nonEmpty( msg ), Assert.nonNull( cause ) );
	}
	
}