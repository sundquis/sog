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

package test.sog.core.xml.representation;

import sog.core.App;
import sog.core.Test;
import sog.core.xml.representation.ArrayRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class ArrayRepTest extends Test.Container {
	
	public ArrayRepTest() {
		super( ArrayRep.class );
	}
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "constructor: ArrayRep(Class)", 
    	description = "Throws AssertionError for null component types" 
    )
    public void tm_0D2D621B4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object[] ArrayRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_0684D4E02( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object[] ArrayRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_07D81C3F5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object[] ArrayRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces the original instance" 
    )
    public void tm_085751C1C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String ArrayRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_09120317C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String ArrayRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_06B00FA7F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ArrayRep.toXML(Object[], XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_0B5CA4602( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ArrayRep.toXML(Object[], XMLWriter)", 
    	description = "Throws AssertionError for null array" 
    )
    public void tm_01F8C0547( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ArrayRep.toXML(Object[], XMLWriter)", 
    	description = "Throws AssertionError for null element" 
    )
    public void tm_0B23CB46A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ArrayRep.toXML(Object[], XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0B0F6ADDB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( ArrayRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( ArrayRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
