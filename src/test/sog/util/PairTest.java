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
import sog.util.Pair;

/**
 * 
 */
@Test.Skip( "Container" )
public class PairTest extends Test.Container {
	
	public PairTest() {
		super( Pair.class );
	}
	
	
	
	// TEST CASES

    @Test.Impl( 
    	member = "constructor: Pair(Object, Object)", 
    	description = "First coordinate can be null" 
    )
    public void tm_00CC2F217( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: Pair(Object, Object)", 
    	description = "Second coordinate can be null" 
    )
    public void tm_0FFB0CF13( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Pair.getX()", 
    	description = "Returns original value" 
    )
    public void tm_0FE1F08D0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object Pair.getY()", 
    	description = "Returns original value" 
    )
    public void tm_0FECBB46F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Pair.toString()", 
    	description = "Uses canonical string representations for first coordinate" 
    )
    public void tm_05E7378DD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Pair.toString()", 
    	description = "Uses canonical string representations for second coordinate" 
    )
    public void tm_0EE6DF1E5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Pair.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Pair.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}


}
