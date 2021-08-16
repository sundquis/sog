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

import sog.core.Objects;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
@Test.Subject( "test." )
public class ObjectsTest extends Test.Container {

	public ObjectsTest() {
		super (Objects.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "Is symmetric" 
		)
		public void tm_0CFD48003( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "Sample cases deep equal" 
		)
		public void tm_02A278907( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "Sample cases deep not equal" 
		)
		public void tm_0BEA760BA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "Throws Assertion error for non array first arg" 
		)
		public void tm_0DD5FDCCE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "Throws Assertion error for non array second arg" 
		)
		public void tm_06BBB1758( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "null equals null" 
		)
		public void tm_0F0061151( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
			description = "null not equal non null" 
		)
		public void tm_0897D89C0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
			description = "Is symmetric" 
		)
		public void tm_06C02EE9C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
			description = "Sample cases deep equal" 
		)
		public void tm_0B37430CE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
			description = "Sample cases deep not equal" 
		)
		public void tm_0FE3CCD01( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
			description = "null equals null" 
		)
		public void tm_0DBB98B6A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
			description = "null not equal non null" 
		)
		public void tm_012CA3187( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "Array not equal collection" 
		)
		public void tm_0A72C8BF9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "Is symmetric" 
		)
		public void tm_0420452DC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "Object not equal array" 
		)
		public void tm_060A9955A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "Object not equal collection" 
		)
		public void tm_0F0A5BE93( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "null equals null" 
		)
		public void tm_04DEECFAA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.deepEquals(Object, Object)", 
			description = "null not equal non null" 
		)
		public void tm_0732BA547( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.equals(Object, Object)", 
			description = "Is symmetric" 
		)
		public void tm_04F1C5F68( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.equals(Object, Object)", 
			description = "Sample cases for equals" 
		)
		public void tm_0638962F8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.equals(Object, Object)", 
			description = "Sample cases for not equals" 
		)
		public void tm_0B893A6A5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.equals(Object, Object)", 
			description = "null equals null" 
		)
		public void tm_0BFD5B636( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.equals(Object, Object)", 
			description = "null not equal non null" 
		)
		public void tm_04FFA893B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Is symmetric" 
		)
		public void tm_01451A903( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Sample cases shallow but not deep" 
		)
		public void tm_02B19A06B( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Sample cases shallow equal" 
		)
		public void tm_08476B897( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Sample cases shallow not equal" 
		)
		public void tm_0F7B4484A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Throws Assertion error for non array first arg" 
		)
		public void tm_07952C5CE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "Throws Assertion error for non array second arg" 
		)
		public void tm_04E254E58( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "null equals null" 
		)
		public void tm_0EFC6BA51( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
			description = "null not equal non null" 
		)
		public void tm_0F85CC0C0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "Is symmetric" 
		)
		public void tm_09FF0659C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "Sample cases shallow but not deep" 
		)
		public void tm_04934ED72( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "Sample cases shallow equal" 
		)
		public void tm_039BEF570( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "Sample cases shallow not equal" 
		)
		public void tm_0227070A3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "null equals null" 
		)
		public void tm_0DE13826A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
			description = "null not equal non null" 
		)
		public void tm_0AC9C5A87( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "Array not equal collection" 
		)
		public void tm_0E26D82F9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "Is symmetric" 
		)
		public void tm_02C3189DC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "Object not equal array" 
		)
		public void tm_058FE0C5A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "Object not equal collection" 
		)
		public void tm_01D83A793( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "null equals null" 
		)
		public void tm_060A886AA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean Objects.shallowEquals(Object, Object)", 
			description = "null not equal non null" 
		)
		public void tm_085660E47( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Objects.class );
		//Test.evalPackage( Objects.class );
	}
}
