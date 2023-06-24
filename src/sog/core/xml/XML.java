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

package sog.core.xml;


import sog.core.Assert;
import sog.core.Property;
import sog.core.Test;

/**
 * Static help with xml
 */
@Test.Subject( "test." )
public class XML {

	private static XML instance = null;

	/** Retrieve the singleton instance */
	@Test.Decl( "Is not null" )
	public static XML get() {
		if ( XML.instance == null ) {
			synchronized ( XML.class ) {
				if ( XML.instance == null ) {
					XML.instance = new XML();
				}
			}
		}
		
		return Assert.nonNull( XML.instance );
	}
	
	private final String declaration;
	
	private XML() {
		this.declaration = Property.getText( "declaration" );
	}
	
	@Test.Decl( "Not empty" )
	@Test.Decl( "Starts correct" )
	@Test.Decl( "Specifies version" )
	@Test.Decl( "Specifies encoding" )
	public String getDeclaration() {
		return this.declaration;
	}

	
}
