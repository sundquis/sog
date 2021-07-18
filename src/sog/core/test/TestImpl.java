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

import java.lang.reflect.Method;

import sog.core.Assert;
import sog.core.Test;

/**
 * Holds information about a single test case.
 */
@Test.Subject( "test." )
public class TestImpl extends TestIdentifier {
	

	/**
	 * A public factory method for constructing a TestImpl corresponding to the given method.
	 * If the method does not have a Test.Impl annotation then the return is null.
	 * 
	 * @param method
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null method" )
	@Test.Decl( "Result is null for non-test methods" )
	@Test.Decl( "Result is non-null for test methods" )
	@Test.Decl( "Constructor only called with non-null arguments" )
	public static TestImpl forMethod( Method method ) {
		TestImpl result = null;
		
		Test.Impl impl = Assert.nonNull( method ).getDeclaredAnnotation( Test.Impl.class );
		if ( impl != null ) {
			result = new TestImpl( impl, method );
		}
		
		return result;
	}

	

	/* Holds the test case information */
	private final Test.Impl impl;
	
	/* The executable test method */
	private final Method method;

	@Test.Skip( "Factory guarantees impl and method are non-null" )
	private TestImpl( Test.Impl impl, Method method ) {
		super( impl.member(), impl.description() );
		
		this.impl = impl;
		this.method = method;
	}
	
	
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is consistent with value supplied to factory" )
	public Method getMethod() {
		return this.method;
	}

	@Test.Decl( "Value is consistent with configured value" )
	public int getPriority() {
		return this.impl.priority();
	}
	
	@Test.Decl( "Value is consistent with configured value" )
	public int getWeight() {
		return this.impl.weight();
	}
	
	@Test.Decl( "Value is consistent with configured value" )
	public long getTimeout() {
		return this.impl.timeout();
	}

}
