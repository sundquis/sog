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

package sog.util.json;

import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
@Test.Subject( "test." )
public class JsonParseException extends Exception {
	
	private static final long serialVersionUID = 5207115341201377011L;

	public JsonParseException( String msg, int column ) {
		super( "Column " + column + ": " + msg );
	}


}
