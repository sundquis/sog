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
import sog.util.Timed;

/**
 * 
 */
@Test.Skip( "Container" )
public class TimedTest extends Test.Container {
	
	public TimedTest() {
		super( Timed.class );
	}
	
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "method: String Timed.format()", 
    	description = "Times less than one microsecond use nanoseconds" 
    )
    public void tm_04F5FD467( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.format()", 
    	description = "Times less than one millisecond use microseconds" 
    )
    public void tm_04F0806CC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.format()", 
    	description = "Times less than one second use milliseconds" 
    )
    public void tm_086763014( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.format()", 
    	description = "Times more than one second use seconds" 
    )
    public void tm_046453D8F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.formatMicro()", 
    	description = "Prints elapsed time in microseconds" 
    )
    public void tm_01060A3B8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.formatMilli()", 
    	description = "Prints elapsed time in milliseconds" 
    )
    public void tm_09D1E6298( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.formatNano()", 
    	description = "Prints elapsed time in nanoseconds" 
    )
    public void tm_07263D13E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Timed.formatSecond()", 
    	description = "Prints elapsed time in seconds" 
    )
    public void tm_0BA7D2672( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Timed.Func Timed.wrap(Function)", 
    	description = "Return is a non-null Function that is Timed" 
    )
    public void tm_00F941480( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Timed.Proc Timed.wrap(Procedure)", 
    	description = "Return is a non-null Procedure that is Timed" 
    )
    public void tm_0B7A3D016( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Timed.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Timed.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}


}
