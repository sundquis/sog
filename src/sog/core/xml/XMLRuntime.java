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

import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLRuntime extends AppRuntime {
	
	
	private static final long serialVersionUID = 6778552297315218478L;

	
	@Test.Skip( "No testing required" )
	public XMLRuntime() {
		super();
	}
	
	
	/** Constructs an exception with specified detail message. */
	@Test.Decl( "Throws AsserionError for null message" )
	@Test.Decl( "Throws AsserionError for empty message" )
	public XMLRuntime( String msg ) {
		super( Assert.nonEmpty( msg ) );
	}


	/** Constructs an exception with specified detail message and cause. */
	@Test.Decl( "Throws AsserionError for null message" )
	@Test.Decl( "Throws AsserionError for empty message" )
	@Test.Decl( "Throws AsserionError for null cause" )
	public XMLRuntime( String msg, Throwable cause ) {
		super( Assert.nonEmpty( msg ), Assert.nonNull( cause ) );
	}
	

}
