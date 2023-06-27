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

import java.util.List;

/**
 * @author sundquis
 *
 */
@FunctionalInterface
@Test.Subject("test." )
public interface Procedure {

	/**
	 * No arguments, no return, no Exception.
	 */
	public void exec();

	
	@Test.Decl( "A non-null Procedure that does nothing" )
	public static final Procedure NOOP = new Procedure() { public void exec() {} };

	/**
	 * Form a composite {@code Procedure} that first executes this {@code Procedure} and then
	 * executes the given other {@code Procedure}.
	 *  
	 * @param after
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null Procedure after" )
	@Test.Decl( "Result executes given after Procedure following execution of this" )
	default public Procedure andThen( Procedure after ) {
		return () -> { this.exec(); Assert.nonNull( after ).exec(); };
	}
	
	/**
	 * Form a composite {@code Procedure} that first executes this {@code Procedure} and then
	 * executes the given list of additional {@code Procedure}s.
	 *  
	 * @param after
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null list of Procedure more" )
	@Test.Decl( "Result executes given list in order following execution of this" )
	default public Procedure andThen( List<Procedure> more ) {
		return () -> { this.exec(); Assert.nonNull( more ).forEach( Procedure::exec ); };
	}
	
}
