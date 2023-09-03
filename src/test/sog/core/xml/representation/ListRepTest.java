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
import sog.core.xml.representation.ListRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class ListRepTest extends Test.Container {
	
	public ListRepTest() {
		super( ListRep.class );
	}
	
	
	
	// TEST CASES:

    @Test.Impl( 
    	member = "constructor: ListRep(Type[])", 
    	description = "Throws AssertionError for null array of component types" 
    )
    public void tm_03386BE1B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: ListRep(Type[])", 
    	description = "Throws AssertionError if not exactly one component" 
    )
    public void tm_0C19C87D0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: List ListRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_08DD73C04( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: List ListRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_068483977( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: List ListRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces the original instance" 
    )
    public void tm_006B6C61E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String ListRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_0EB14D0DD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String ListRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_091CB58A0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ListRep.toXML(List, XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_0E509F02C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ListRep.toXML(List, XMLWriter)", 
    	description = "Throws AppRuntime if an IOException occurs" 
    )
    public void tm_0A9C37C97( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ListRep.toXML(List, XMLWriter)", 
    	description = "Throws AssertionError for null element" 
    )
    public void tm_031865A14( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void ListRep.toXML(List, XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0206CAAF1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( ListRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( ListRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

	App.get().done();
	}

}
