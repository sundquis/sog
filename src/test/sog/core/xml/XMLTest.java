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

import sog.core.Test;
import sog.core.xml.XML;

/**
 * 
 */
@Test.Skip( "Container" )
public class XMLTest extends Test.Container {
	
	public XMLTest() {
		super( XML.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: String XML.getDeclaration()", 
		description = "Not empty" 
	)
	public void tm_033B21BF6( Test.Case tc ) {
		tc.assertNotEmpty( XML.get().getDeclaration() );
	}
		
	@Test.Impl( 
		member = "method: String XML.getDeclaration()", 
		description = "Starts correct" 
	)
	public void tm_0640FE05B( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().toLowerCase().startsWith( "<?xml" ) );
	}
		
	@Test.Impl( 
		member = "method: XML XML.get()", 
		description = "Is not null" 
	)
	public void tm_038ADA1AC( Test.Case tc ) {
		tc.assertNonNull( XML.get() );
	}
	
	@Test.Impl( 
		member = "method: String XML.getDeclaration()", 
		description = "Specifies encoding" 
	)
	public void tm_0F49CDAB4( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().toLowerCase().contains( "encoding" ) );
	}
		
	@Test.Impl( 
		member = "method: String XML.getDeclaration()", 
		description = "Specifies version" 
	)
	public void tm_0D5FDCCED( Test.Case tc ) {
		tc.assertTrue( XML.get().getDeclaration().toLowerCase().contains( "version" ) );
	}
	
	

	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( XML.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XML.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
	}
}
