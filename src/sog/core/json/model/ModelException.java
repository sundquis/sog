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

import sog.core.Test;

/**
 * Indicates error with definition of model classes.
 */
@Test.Subject( "test." )
public class ModelException extends RuntimeException {
	
	private static final long serialVersionUID = 5084657946546956407L;

	public ModelException( String msg ) {
		super( msg );
	}

	public ModelException( Exception ex ) {
		super( ex );
	}

	public ModelException( String msg, Exception ex ) {
		super( msg, ex );
	}


}
