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
import sog.core.xml.representation.StringRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class StringRepTest extends Test.Container {
	
	public StringRepTest() {
		super( StringRep.class );
	}
	
	
	
	// TEST CASES:

    @Test.Impl( 
    	member = "constructor: StringRep(Type[])", 
    	description = "Array of component types is ignored" 
    )
    public void tm_049642861( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StringRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_03797C524( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StringRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_0A40AFA97( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StringRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces the original instance" 
    )
    public void tm_02D810F3E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StringRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_09C7384CA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StringRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_0C5E1BC4D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StringRep.toXML(String, XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_0BA60B4C6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StringRep.toXML(String, XMLWriter)", 
    	description = "Throws AssertionError for null element" 
    )
    public void tm_093B28E2E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StringRep.toXML(String, XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0B83C8B97( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( StringRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( StringRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
