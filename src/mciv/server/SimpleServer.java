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

package mciv.server;

import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
@Test.Subject( "test." )
public class SimpleServer {
	public SimpleServer() {
	}

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( SimpleServer.class ).concurrent( false ).showDetails( true ).print();
		//*/

		/* Toggle package results
		Test.evalPackage( SimpleServer.class )
			.concurrent( false )
			.showDetails( true )
			.print();
		//*/

		System.out.println( "\nDone!" );
	}

}
