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

package test.sog.core.xml;

import sog.core.App;
import sog.core.Test;
import sog.core.xml.XMLRepresentation;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLRepresentationTest extends Test.Container {
	
	public XMLRepresentationTest() {
		super( XMLRepresentation.class );
	}
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forClass(Class, Type[])", 
    	description = "Throws AppRuntime when no representation for the target type has been registered" 
    )
    public void tm_0DBCAFA86( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forClass(Class, Type[])", 
    	description = "Throws AssertionError for null target type" 
    )
    public void tm_06701C905( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Throws AppRuntime for a type without registered representation" 
    )
    public void tm_0A812EFC0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Throws AssertionError for null type" 
    )
    public void tm_0FE37F504( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Throws ClassCastException for improper type" 
    )
    public void tm_0AB819609( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Returns representation for Storable instances" 
    )
    public void tm_044026178( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Returns representation for arrays" 
    )
    public void tm_0936C6856( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Returns representation for lists" 
    )
    public void tm_09E79BD8F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Returns representation for maps" 
    )
    public void tm_016E386B3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLRepresentation XMLRepresentation.forType(Type)", 
    	description = "Returns representation for primitive types" 
    )
    public void tm_022BEB87A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	

    
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XMLRepresentation.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XMLRepresentation.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		App.get().done();
	}

}
