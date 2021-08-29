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

package test.sog.core;


import sog.core.Switch;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class SwitchTest extends Test.Container {
	
	public SwitchTest() {
		super( Switch.class );
	}
	
	
	
	// TEST CASES
	
	
	
	

	public static void main( String[] args ) {
		Test.eval( Switch.class );
		//Test.evalPackage( Switch.class );
	}
}
