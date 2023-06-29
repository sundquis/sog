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
package sog.core.test;

import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;

/**
 * Defines the unique key identifier associated with TestDecl and TestImpl
 * Defines the member naming policy for test methods.
 * 
 * In the context of a single subject class, the member name and test description must
 * uniquely identify a single test case. These strings are used to form a
 * unique key for mapping.
 */
@Test.Subject( "test." )
public abstract class TestIdentifier {

	/* The member name, as determined by TestMember, of a member in a subject class */
	private final String memberName;
	
	/* The description given in a Test.Decl annotation */
	private final String description;
	
	
	@Test.Decl( "Throws AssertionError for empty member name" )
	@Test.Decl( "Throws AssertionError for null member name" )
	@Test.Decl( "Throws AssertionError for empty description" )
	@Test.Decl( "Throws AssertionError for null description" )
	protected TestIdentifier( String memberName, String description ) {
		this.memberName = Assert.nonEmpty( memberName );
		this.description = Assert.nonEmpty( description );
	}
	
	@Test.Decl( "Return is non-empty" )
	@Test.Decl( "Return is consistent with value supplied to constructor" )
	final public String getMemberName() {
		return this.memberName;
	}
	
	@Test.Decl( "Return is non-empty" )
	@Test.Decl( "Return is consistent with value supplied to constructor" )
	final public String getDescription() {
		return this.description;
	}

	/**
	 * The unique key assigned to the given (name, description) pair. The mapping from pairs
	 * to key value must be an injection, and the key should be user friendly as it is
	 * included in the toString() representation of TestDecl and TestImpl instances.
	 * 
	 * @return
	 */
	@Test.Decl( "Return is non-empty" )
	@Test.Decl( "If two pairs are equal then keys are equal" )
	@Test.Decl( "If two pairs are not equal then keys are not equal" )
	final public String getKey() {
		return "[" + this.getMemberName() + ", \"" + this.getDescription() + "\"]";
	}
	
	@Override
	@Test.Decl( "Return is non-empty" )
	public String toString() {
		return this.getKey();
	}

	/**
	 * The generated method name for the container's test method implementation.
	 * 
	 * The name should be short and unique for (name, description) pairs, but in a container
	 * the test method names can be changed so a name collision is not fatal. It is not necessary
	 * for the method name to display the (name, description) pair since test methods are
	 * annotated with Test.Impl which displays the (name, description) information.
	 * 
	 * @return
	 */
	@Test.Decl( "Return is non-empty" )
	final public String getMethodName() {
		return "tm_" + Strings.rightJustify( Integer.toHexString( this.getKey().hashCode() ).toUpperCase(),  9,  '0' );
	}
		
}
