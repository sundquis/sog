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

package sog.core.xml.representation;


import sog.core.Test;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class StringRep extends XMLRepresentation<String> {
	
	@Override
	public String fromXML( XMLReader in ) {
		return in.readTag( "String" );
	}

	@Override
	public void toXML( String t, XMLWriter out ) {
		out.writeTag( "String", t );
	}
	


}
