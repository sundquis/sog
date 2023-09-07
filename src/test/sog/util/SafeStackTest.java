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
package test.sog.util;

import sog.core.App;
import sog.core.Test;
import sog.util.SafeStack;

/**
 * 
 */
@Test.Skip( "Container" )
public class SafeStackTest extends Test.Container {
	
	public SafeStackTest() {
		super( SafeStack.class );
	}
	
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "constructor: SafeStack()", 
    	description = "Default returns null when empty" 
    )
    public void tm_0DE0537A8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: SafeStack(Supplier)", 
    	description = "Throws AssertionError for null supplier" 
    )
    public void tm_0868A555F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.peek()", 
    	description = "Configured supplier used before elements have been added" 
    )
    public void tm_099BCC026( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.peek()", 
    	description = "Configured supplier used when empty" 
    )
    public void tm_0B0D473B3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.peek()", 
    	description = "Previously added element retained" 
    )
    public void tm_06DA4873F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.peek()", 
    	description = "Previously added element returned" 
    )
    public void tm_09EFCC5EA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.pop()", 
    	description = "Configured supplier used before elements have been added" 
    )
    public void tm_0D75E5794( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.pop()", 
    	description = "Configured supplier used when empty" 
    )
    public void tm_04BB75385( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.pop()", 
    	description = "Previously added element not retained" 
    )
    public void tm_028B3C5FE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object SafeStack.pop()", 
    	description = "Previously added element returned" 
    )
    public void tm_09E59723C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean SafeStack.isEmpty()", 
    	description = "False after push" 
    )
    public void tm_0B0B7590D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean SafeStack.isEmpty()", 
    	description = "False after replace" 
    )
    public void tm_0BE76B337( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean SafeStack.isEmpty()", 
    	description = "True when constructed" 
    )
    public void tm_0C761B826( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.clear()", 
    	description = "Previously added elements are not available" 
    )
    public void tm_09A949A41( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.clear()", 
    	description = "Stack is empty after" 
    )
    public void tm_0A7F02983( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.push(Object)", 
    	description = "New element retained" 
    )
    public void tm_0E648B878( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.push(Object)", 
    	description = "Null element allowed" 
    )
    public void tm_0D0D6881B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.push(Object)", 
    	description = "Previously added element retained" 
    )
    public void tm_07AC80C2A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.replace(Object)", 
    	description = "New element retained" 
    )
    public void tm_0E9939BB8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.replace(Object)", 
    	description = "Null element allowed" 
    )
    public void tm_0D4216B5B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void SafeStack.replace(Object)", 
    	description = "Previously added element not retained" 
    )
    public void tm_009AAAED7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

        
        
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( SafeStack.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( SafeStack.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}


}
