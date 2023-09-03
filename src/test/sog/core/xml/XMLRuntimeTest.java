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

package test.sog.core.xml;

import sog.core.App;
import sog.core.Test;
import sog.core.xml.XMLRuntime;

/**
 * 
 */
@Test.Skip( "Container" )
public class XMLRuntimeTest extends Test.Container {
	
	public XMLRuntimeTest() {
		super( XMLRuntime.class );
	}
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: XMLRuntime(String)", 
    	description = "Throws AsserionError for empty message" 
    )
    public void tm_0427C1EAB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLRuntime(String)", 
    	description = "Throws AsserionError for null message" 
    )
    public void tm_05C17E8ED( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLRuntime(String, Throwable)", 
    	description = "Throws AsserionError for empty message" 
    )
    public void tm_0CC728E51( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLRuntime(String, Throwable)", 
    	description = "Throws AsserionError for null cause" 
    )
    public void tm_0F6DEFDA9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLRuntime(String, Throwable)", 
    	description = "Throws AsserionError for null message" 
    )
    public void tm_0BB61EC87( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XMLRuntime.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XMLRuntime.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		App.get().done();
	}

}
