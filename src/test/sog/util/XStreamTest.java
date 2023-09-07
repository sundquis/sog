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

import sog.util.XStream;


/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class XStreamTest extends Test.Container {

	public XStreamTest() {
		super( XStream.class );
	}
	
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted filter operation is applied to underlying Stream" 
    )
    public void tm_046C708DB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted flatMap operation is applied to underlying Stream" 
    )
    public void tm_0E1D65B72( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted forEach operation is applied to underlying Stream" 
    )
    public void tm_0DE36D439( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted forSome applies action to matching elements" 
    )
    public void tm_0B6686B79( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted forSome consumes matching elements" 
    )
    public void tm_04EDB4C59( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted map operation is applied to underlying Stream" 
    )
    public void tm_0EC501B2B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Adapted peek operation is applied to underlying Stream" 
    )
    public void tm_0EDC1D8BE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Additional operations can be applied to original Stream using toStream" 
    )
    public void tm_00ECB459E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Has elements if given Stream has elements" 
    )
    public void tm_0B7B6370A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.adapt(Stream)", 
    	description = "Is terminated if gievn Stream is terminated" 
    )
    public void tm_0A06B67EA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.forSome(XStream.Case)", 
    	description = "Elements matching the condition have been consumed" 
    )
    public void tm_051A64CE3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XStream XStream.forSome(XStream.Case)", 
    	description = "Given action is applied to consumed elements" 
    )
    public void tm_0553A70CD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XStream.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XStream.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}


}
