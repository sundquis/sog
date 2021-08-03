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

package test.sog.core.test.foo;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( ".TEST" )
public class C2 {

	@Test.Decl( "fail w = 3" ) public C2() {}
	
	@Test.Decl( "pass w = 4" ) public void m() {}

	@Test.Skip( "container" )
	public static class TEST extends Test.Container implements Sleep {
		TEST() { super( C2.class ); }

		@Test.Impl( member = "constructor: C2()", description = "fail w = 3", weight = 3 )
		public void tm_0A4759247( Test.Case tc ) { this.sleep( 3L ); tc.assertTrue( false ); }
		
		@Test.Impl( member = "method: void C2.m()", description = "pass w = 4", weight = 4 )
		public void tm_01EF180A9( Test.Case tc ) { this.sleep( 4L ); tc.assertTrue( true ); }
	}
}
