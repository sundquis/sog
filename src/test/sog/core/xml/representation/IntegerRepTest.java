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
import sog.core.xml.representation.IntegerRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class IntegerRepTest extends Test.Container {
	
	public IntegerRepTest() {
		super( IntegerRep.class );
	}
	
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: IntegerRep(Type[])", 
    	description = "Array of component types is ignored" 
    )
    public void tm_038316EBE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Integer IntegerRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_057AC26C0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Integer IntegerRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_0108D6533( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Integer IntegerRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces the original instance" 
    )
    public void tm_02A36B8DA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String IntegerRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_0DE21D037( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String IntegerRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_09E0E577A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void IntegerRep.toXML(Integer, XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_01321A282( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void IntegerRep.toXML(Integer, XMLWriter)", 
    	description = "Throws AssertionError for null element" 
    )
    public void tm_0937C70EA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void IntegerRep.toXML(Integer, XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0028D615B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( IntegerRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( IntegerRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
