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
package sog.core.test;

import java.io.IOException;

import sog.core.AppException;
import sog.core.Assert;
import sog.core.Test;
import sog.util.Commented;
import sog.util.IndentWriter;
import sog.util.Macro;
import sog.util.Printable;

/**
 * Represents a single test declaration as specified by a Test.Decl annotation on a member in a subject class. 
 * Maintains the template code for test method stubs.
 * Knows when the TestDecl has been matched with a corresponding TestImpl. 
 */
@Test.Subject( "test." )
public class TestDecl extends TestIdentifier implements Commented, Printable {

	
	private TestImpl impl = null;

	@Test.Decl( "Throws AssertionError for null member name" )
	@Test.Decl( "Throws AssertionError for emtpy member name" )
	@Test.Decl( "Throws AssertionError for null description" )
	@Test.Decl( "Throws AssertionError for emtpy description" )
	public TestDecl( String memberName, String description ) {
		super( memberName, description );
	}

	/** Return true if the impl was not previously set */
	@Test.Decl( "Throws AssertionError for null TestImpl" )
	@Test.Decl( "First call returns true" )
	@Test.Decl( "Second call returns false" )
	public boolean setImpl( TestImpl impl ) {
		boolean result = this.impl == null;
		this.impl = Assert.nonNull( impl );
		return result;
	}

	@Test.Decl( "Before setImpl returns true" )
	@Test.Decl( "After setImpl returns false" )
	public boolean unimplemented() {
		return this.impl == null;
	}

	//	STUB	
	//	STUB	@Test.Impl( 
	//	STUB		member = "${memberName}", 
	//	STUB		description = "${description}" 
	//	STUB	)
	//	STUB	public void ${methodName}( Test.Case tc ) {
	//	STUB		tc.addMessage( "GENERATED STUB" ).fail();
	//	STUB	}

	@Override
	@Test.Decl( "Throws AssertionError for null IndentWriter" )
	@Test.Decl( "Output includes Test.Impl" )
	@Test.Decl( "Output includes member name" )
	@Test.Decl( "Output includes description" )
	@Test.Decl( "Output includes method name" )
	public void print( IndentWriter out ) {
		Assert.nonNull( out );
		Macro macro = new Macro()
			.expand( "memberName", this.getMemberName() )
			.expand( "description", this.getDescription() )
			.expand( "methodName", this.getMethodName() );
		try {
			this.getCommentedLines( "STUB" ).flatMap( macro ).forEach( out::println );
		} catch ( IOException ex ) {
			throw new AppException( ex );
		}
	}

}
