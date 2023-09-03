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

package test.sog.core;

import sog.core.App;
import sog.core.Stored;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class StoredTest extends Test.Container {
	
	public StoredTest() {
		super( Stored.class );
	}
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "method: Object Stored.get()", 
    	description = "Result is not null" 
    )
    public void tm_0D4B0B265( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Initial vlue is consistent with final value of previous JVM execution" 
    )
    public void tm_0E034F14C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Result agrees with the given initial value the first time the Stored instance is retrieved" 
    )
    public void tm_033AB0E48( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Result is not null" 
    )
    public void tm_093EB7A4D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Result is the same instance if previously retrieved" 
    )
    public void tm_0635AF64C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_050C89CA2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Throws AssertionError for null initial value" 
    )
    public void tm_093929960( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Throws AssertionError if calling class is anonymous" 
    )
    public void tm_009C37EE7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Throws AssertionError if field is not defined in the calling class" 
    )
    public void tm_0B842D490( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Throws AssertionError if named field is not a Stored instance" 
    )
    public void tm_03FF62FE9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Stored Stored.get(String, Object)", 
    	description = "Value is stored in an xml data file when JVM shuts down" 
    )
    public void tm_041AC28F3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Stored.toString()", 
    	description = "Uses natural string representation of the stored value" 
    )
    public void tm_09F37A67B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Stored.set(Object)", 
    	description = "Throws AssertionError for null value" 
    )
    public void tm_007B2781C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Stored.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Stored.class )
			.concurrent( false )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
