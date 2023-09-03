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
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Skip( "Container" )
public class XMLWriterTest extends Test.Container {
	
	public XMLWriterTest() {
		super( XMLWriter.class );
	}
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: XMLWriter(Path)", 
    	description = "Throws AssertionError for null path" 
    )
    public void tm_0F673206E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLWriter(Path)", 
    	description = "Throws IOExcpetion for error openening file" 
    )
    public void tm_0AD0C2B2A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.close()", 
    	description = "Idempotent" 
    )
    public void tm_0FE9B63F7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeCloseTag(String)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_05713C402( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeOpenTag(String)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_0D0739F16( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeTag(String, String)", 
    	description = "Content can be empty" 
    )
    public void tm_0A83087DC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeTag(String, String)", 
    	description = "Content can contain whitespace" 
    )
    public void tm_0E3F07275( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeTag(String, String)", 
    	description = "Entities in content are encoded" 
    )
    public void tm_038E13300( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeTag(String, String)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_0B8BFB291( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLWriter.writeTag(String, String)", 
    	description = "Throws AssertionError for null content" 
    )
    public void tm_02A9D2D73( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XMLWriter.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XMLWriter.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		App.get().done();
	}

}
