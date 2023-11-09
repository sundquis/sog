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

package sog.core.json.model;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.Writer;

import sog.core.Test;
import sog.core.json.PrimitiveWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class ModelWriter extends PrimitiveWriter {
	
	public ModelWriter( BufferedWriter buf ) {
		super( buf );
	}
	
	public ModelWriter( Writer writer ) {
		super( writer );
	}
	
	public ModelWriter( OutputStream out ) {
		super( out );
	}
	
	

}
