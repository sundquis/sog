/**
 * Copyright (C) 2021
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

import sog.util.Fault;

/**
 * A class used to signal fatal runtime conditions.
 * 
 * These (with the exception of warning) throw AppException.
 * Contract violations (illegal arguments, etc.) use the Assert class and throw AssertionError
 */
@Test.Subject( "test." )
public final class Fatal {

	/**
	 * Not intended to be instantiated.
	 */
	private Fatal() {}

	/**
	 * Indicates that an unimplemented block of code has been reached and
	 * includes a detail message in the exception.
	 *
	 * @param detail	Brief message describing or outlining needed implementation.
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	public static void unimplemented( String detail ) {
		fail( "Unimplemented: " + Assert.nonEmpty( detail ) );
	}


	/**
	 * Indicates that a fatal error has occurred and includes a detail
	 * message in the exception and a throwable cause.
	 *
	 * @param detail
	 *      A string detail message to include in the exception message.
	 *
	 * @param cause
	 *      The exception that caused the fatal error.
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	@Test.Decl( "Throws AssertionError for null cause" )
	public static void error( String detail, Throwable cause ) {
		fail( "Error: " + Assert.nonEmpty( detail ), Assert.nonNull( cause ) );
	}
	
	/**
	 * Indicates that a fatal error has occurred and includes a detail
	 * message in the exception.
	 *
	 * @param detail
	 *      A string detail message to include in the exception message.
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	public static void error( String detail ) {
		fail( "Error: " + Assert.nonEmpty( detail ) );
	}
	
	/**
	 * Toss a fault indicating a serious condition.
	 * To receive notifications register a listener:
	 * 		Fault.addListener( Consumer<Fault> listener )
	 * 
	 * @param detail
	 *      A string detail message to include in the exception message.
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	public static void warning( String detail ) {
		new Fault( "WARNING: " + Assert.nonEmpty( detail ) ).toss();
	}

	/**
	 * Indicates that a state believed to be impossible has been reached
	 *
	 * @param detail
	 *      A string detail message to include in the exception message.
	 *      
	 * @param cause
	 * 		Throwable cause linked to the failure
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	@Test.Decl( "Throws AssertionError for null cause" )
	public static void impossible( String detail, Throwable cause ) {
		fail( "Error: " + Assert.nonEmpty( detail ), Assert.nonNull( cause ) );
	}
	
	/**
	 * Indicates that a state believed to be impossible has been reached
	 *
	 * @param detail
	 *      A string detail message to include in the exception message.
	 */
	@Test.Decl( "Throws AssertionError for empty detail message" )
	public static void impossible( String detail ) {
		fail( "Error: " + Assert.nonEmpty( detail ) );
	}
	

	
	private static void fail( String msg ) {
		throw new AppException( msg );
	}
	
	private static void fail( String msg, Throwable cause ) {
		throw new AppException( msg, cause );
	}

}
