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

package sog.core.xml.data;

import java.lang.reflect.Field;

import sog.core.Test;
import sog.core.xml.XML;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLField {
	
	private final Field field;
	
	XMLField( Field field ) {
		this.field = field;
	}

	boolean persistent() {
		return this.field.getAnnotation( XML.Data.class ) != null;
	}
	
	String getName() {
		return this.field.getName();
	}

}
