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
import sog.core.xml.representation.MapRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class MapRepTest extends Test.Container {
	
	public MapRepTest() {
		super( MapRep.class );
	}
	
	
	
	// TEST CASES:

    @Test.Impl( 
    	member = "constructor: MapRep(Type[])", 
    	description = "Throws AssertionError for null array of component types" 
    )
    public void tm_03EDEC307( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: MapRep(Type[])", 
    	description = "Throws AssertionError if not exactly two components" 
    )
    public void tm_082C6E83F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Map MapRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_0B5BDDCFC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Map MapRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_0310A7C6F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Map MapRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces the original instance" 
    )
    public void tm_0F1EC7716( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String MapRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_0CCAACFD9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String MapRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_0258ABC9C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void MapRep.toXML(Map, XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_00026C542( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void MapRep.toXML(Map, XMLWriter)", 
    	description = "Throws AssertionError for null key" 
    )
    public void tm_084D1E7CD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void MapRep.toXML(Map, XMLWriter)", 
    	description = "Throws AssertionError for null map" 
    )
    public void tm_084EC238A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void MapRep.toXML(Map, XMLWriter)", 
    	description = "Throws AssertionError for null value" 
    )
    public void tm_0D676FD9F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void MapRep.toXML(Map, XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0DE68569B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }


	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( MapRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( MapRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
