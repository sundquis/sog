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
import sog.core.xml.representation.StorableRep;

/**
 * 
 */
@Test.Skip( "Container" )
public class StorableRepTest extends Test.Container {
	
	public StorableRepTest() {
		super( StorableRep.class );
	}
	
	
	
	// TEST CASSES:
	
    @Test.Impl( 
    	member = "constructor: StorableRep(Class)", 
    	description = "Throws AssertionError for null target type" 
    )
    public void tm_0816BB0B8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object StorableRep.fromXML(XMLReader)", 
    	description = "Fields annotated with Storable.Data are initialized" 
    )
    public void tm_076D830DC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object StorableRep.fromXML(XMLReader)", 
    	description = "Throws AppRuntime if target type cannot be constructed" 
    )
    public void tm_086B771E3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object StorableRep.fromXML(XMLReader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_0F62149C3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object StorableRep.fromXML(XMLReader)", 
    	description = "Throws XMLRuntime for malformed content" 
    )
    public void tm_0E645D376( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Object StorableRep.fromXML(XMLReader)", 
    	description = "Write followed by read produces instance equivalent to the original instance" 
    )
    public void tm_0FC2DCFA9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StorableRep.getName()", 
    	description = "Result does not contain entity characters" 
    )
    public void tm_0F881CC9D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String StorableRep.getName()", 
    	description = "Result is not empty" 
    )
    public void tm_0691FA460( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StorableRep.toXML(Object, XMLWriter)", 
    	description = "Fields annotated with Storable.Data are stored" 
    )
    public void tm_03CB015B2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StorableRep.toXML(Object, XMLWriter)", 
    	description = "Null fields are ignored" 
    )
    public void tm_00B965B3C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StorableRep.toXML(Object, XMLWriter)", 
    	description = "Read followed by write produces an equivalent representation" 
    )
    public void tm_01EF502AB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StorableRep.toXML(Object, XMLWriter)", 
    	description = "Throws AssertionError for null instance" 
    )
    public void tm_0BACCF1F4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void StorableRep.toXML(Object, XMLWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0F6C6BB52( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( StorableRep.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( StorableRep.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
