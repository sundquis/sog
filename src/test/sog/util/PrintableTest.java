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
import sog.util.Printable;

/**
 * 
 */
@Test.Skip( "Container" )
public class PrintableTest extends Test.Container {
	
	public PrintableTest() {
		super( Printable.class );
	}
	
	
	
	// TEST CASES

    @Test.Impl( 
    	member = "method: void Printable.print()", 
    	description = "Default uses System.out" 
    )
    public void tm_050556F8E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Printable.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Printable.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}


}
