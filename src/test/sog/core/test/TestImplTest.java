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
package test.sog.core.test;

import sog.core.Test;
import sog.core.test.TestImpl;

/**
 * 
 */
public class TestImplTest extends Test.Container {

	public TestImplTest() {
		super( TestImpl.class );
	}
	
	
    @Test.Impl( 
        	member = "method: Method TestImpl.getMethod()", 
        	description = "Return is consistent with value supplied to factory" 
        )
        public void tm_024D0F4AC( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: Method TestImpl.getMethod()", 
        	description = "Return is non-null" 
        )
        public void tm_0FD9DAEBD( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: TestImpl TestImpl.forMethod(Method)", 
        	description = "Constructor only called with non-null arguments" 
        )
        public void tm_019BCD4E9( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: TestImpl TestImpl.forMethod(Method)", 
        	description = "Result is non-null for test methods" 
        )
        public void tm_09ADC2AA8( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: TestImpl TestImpl.forMethod(Method)", 
        	description = "Result is null for non-test methods" 
        )
        public void tm_0E704F0A8( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: TestImpl TestImpl.forMethod(Method)", 
        	description = "Throws AssertionError for null method" 
        )
        public void tm_011C175C3( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: int TestImpl.getPriority()", 
        	description = "Value is consistent with configured value" 
        )
        public void tm_04DE60A21( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: int TestImpl.getWeight()", 
        	description = "Value is consistent with configured value" 
        )
        public void tm_02D048235( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
        
        @Test.Impl( 
        	member = "method: long TestImpl.getTimeout()", 
        	description = "Value is consistent with configured value" 
        )
        public void tm_00E245875( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" ).fail();
        }
	
	
	
	
	public static void main( String[] args ) {
		Test.eval( TestImpl.class );
		//Test.evalPackage( TestImpl.class );
	}
}
