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

import java.util.HashMap;
import java.util.Map;

import sog.core.Strings;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLTag {

	private final Map<String, String> attributes;
	
	private final String name;
	
	private boolean empty;
	
	public XMLTag( String name ) {
		this.attributes = new HashMap<>();
		this.name = name;
		this.empty = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAttribute( String name ) {
		if ( this.attributes.containsKey( name ) ) {
			return this.attributes.get( name );
		}
		throw new XMLRuntime( "Element " + this.getName() + " does not have attribute " + name );
	}
	
	public boolean isEmpty() {
		return this.empty;
	}
	
	public XMLTag addAttribute( String name, String value ) {
		this.attributes.put( name, value );
		return this;
	}
	
	public XMLTag markEmpty() {
		this.empty = true;
		return this;
	}
	
	@Override
	public String toString() {
		return "<" + this.name + " " + Strings.toString( this.attributes ) + (this.isEmpty() ? "/" : "") + ">";
	}

	
	
}
