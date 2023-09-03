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
import sog.core.xml.XMLReader;

/**
 * 
 */
@Test.Skip( "Container" )
public class XMLReaderTest extends Test.Container {
	
	public XMLReaderTest() {
		super( XMLReader.class );
	}
	
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: XMLReader(Path)", 
    	description = "Throws AssertionError for null path" 
    )
    public void tm_01D40C8BE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLReader.getLocation()", 
    	description = "Location includes the current column number" 
    )
    public void tm_0C67925AB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLReader.getLocation()", 
    	description = "Location includes the current line number" 
    )
    public void tm_0026B9E0D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLReader.readContent()", 
    	description = "Return can be empty" 
    )
    public void tm_0B304BF00( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLReader.readContent()", 
    	description = "Return can contain entities" 
    )
    public void tm_0B1D4811D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLReader.readContent()", 
    	description = "Return can contain whitespace" 
    )
    public void tm_088550C99( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readCloseTag(String)", 
    	description = "Advances reader beyond the end of tag" 
    )
    public void tm_0A2F835B9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readCloseTag(String)", 
    	description = "Leading whitespace is skipped" 
    )
    public void tm_02C7FA734( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readCloseTag(String)", 
    	description = "Throws AppRuntime if reader does not hold a close tag with the given name" 
    )
    public void tm_014E6AF9E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readCloseTag(String)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_0783B3921( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readOpenTag(String)", 
    	description = "Advances reader beyond the end of tag" 
    )
    public void tm_072F40383( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readOpenTag(String)", 
    	description = "Leading whitespace is skipped" 
    )
    public void tm_01DA2DEFE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readOpenTag(String)", 
    	description = "Throws AppRuntime if reader does not hold an open tag with the given name" 
    )
    public void tm_0B3B5044C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLReader XMLReader.readOpenTag(String)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_0E2098A17( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLReader.close()", 
    	description = "Characters not available after close" 
    )
    public void tm_0F49F4C88( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLReader.close()", 
    	description = "Idempotent" 
    )
    public void tm_00D50D5A7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XMLReader.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XMLReader.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		App.get().done();
	}

}
