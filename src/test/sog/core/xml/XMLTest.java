/**
 * Copyright (C) 2021
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
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String XML.getDeclaration()", 
			description = "starts correct" 
		)
		public void tm_0640FE05B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: XML XML.get()", 
			description = "Is not null" 
		)
		public void tm_038ADA1AC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( XML.class );
		//Test.evalPackage( XML.class );
	}
}
