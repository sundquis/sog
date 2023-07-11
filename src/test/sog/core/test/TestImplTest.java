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
package test.sog.core.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Test;
import sog.core.test.TestImpl;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestImplTest extends Test.Container {
	
	private final Map<String, Method> methods;

	public TestImplTest() {
		super( TestImpl.class );
		
		this.methods = Arrays.stream( MyContainer.class.getDeclaredMethods() )
			.collect( Collectors.toMap( Method::getName, Function.identity() ) );
	}
	
	public Method getMethod( String name ) {
		return Assert.nonNull( this.methods.get( name ), "No method: " + name );
	}
	
	
	public static class MyContainer extends Test.Container {
		
		public static final String MEMBER_NAME = "This is the member name";
		public static final String DESCRIPTION = "This is the description";
		public static final int WEIGHT = 123;
		public static final int PRIORITY = 7;
		public static final long TIMEOUT = 57L;
		
		public MyContainer() {
			super( TestImpl.class );
		}
		
		@Test.Impl(  member = MEMBER_NAME, description = DESCRIPTION, weight = WEIGHT, priority = PRIORITY, timeout = TIMEOUT )
		public void testMethod( Test.Case tc ) { }
		
		public void nonTestMethod( Test.Case tc ) { }
		
	}
	
	
	// TEST METHODS

	@Test.Impl( 
		member = "method: Method TestImpl.getMethod()", 
		description = "Return is consistent with value supplied to factory" 
	)
	public void tm_024D0F4AC( Test.Case tc ) {
		Method method = this.getMethod( "testMethod" );
		tc.assertEqual( method,  TestImpl.forMethod( method ).getMethod() );
	}
		
	@Test.Impl( 
		member = "method: Method TestImpl.getMethod()", 
		description = "Return is non-null" 
	)
	public void tm_0FD9DAEBD( Test.Case tc ) {
		Method method = this.getMethod( "testMethod" );
		tc.assertNonNull( TestImpl.forMethod( method ).getMethod() );
	}
		
	@Test.Impl( 
		member = "method: TestImpl TestImpl.forMethod(Method)", 
		description = "Result is non-null for non-test methods" 
	)
	public void tm_0AA15ED28( Test.Case tc ) {
		Method nonTest = this.getMethod( "nonTestMethod" );
		tc.assertNonNull( TestImpl.forMethod( nonTest ) );
	}
		
	@Test.Impl( 
		member = "method: TestImpl TestImpl.forMethod(Method)", 
		description = "Result is non-null for test methods" 
	)
	public void tm_09ADC2AA8( Test.Case tc ) {
		Method test = this.getMethod( "testMethod" );
		tc.assertNonNull( TestImpl.forMethod( test ) );
	}
		
	@Test.Impl( 
		member = "method: TestImpl TestImpl.forMethod(Method)", 
		description = "Throws AssertionError for null method" 
	)
	public void tm_011C175C3( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestImpl.forMethod( null );
	}
		
	@Test.Impl( 
		member = "method: boolean TestImpl.isValid()", 
		description = "TestImpl from Test.Impl method is valid" 
	)
	public void tm_0562B1765( Test.Case tc ) {
		Method test = this.getMethod( "testMethod" );
		tc.assertTrue( TestImpl.forMethod( test ).isValid() );
	}
		
	@Test.Impl( 
		member = "method: boolean TestImpl.isValid()", 
		description = "TestImpl from non Test.Impl method is not valid" 
	)
	public void tm_0581ECA45( Test.Case tc ) {
		Method nonTest = this.getMethod( "nonTestMethod" );
		tc.assertFalse( TestImpl.forMethod( nonTest ).isValid() );
	}
		
	@Test.Impl( 
		member = "method: int TestImpl.getPriority()", 
		description = "Value is consistent with configured value" 
	)
	public void tm_04DE60A21( Test.Case tc ) {
		Method test = this.getMethod( "testMethod" );
		TestImpl impl = TestImpl.forMethod( test );
		tc.assertEqual( MyContainer.PRIORITY, impl.getPriority() );
	}
		
	@Test.Impl( 
		member = "method: int TestImpl.getWeight()", 
		description = "Value is consistent with configured value" 
	)
	public void tm_02D048235( Test.Case tc ) {
		Method test = this.getMethod( "testMethod" );
		TestImpl impl = TestImpl.forMethod( test );
		tc.assertEqual( MyContainer.WEIGHT, impl.getWeight() );
	}
		
	@Test.Impl( 
		member = "method: long TestImpl.getTimeout()", 
		description = "Value is consistent with configured value" 
	)
	public void tm_00E245875( Test.Case tc ) {
		Method test = this.getMethod( "testMethod" );
		TestImpl impl = TestImpl.forMethod( test );
		tc.assertEqual( MyContainer.TIMEOUT, impl.getTimeout() );
	}
	
	
	
	
	
	public static void main( String[] args ) {
	}
}
