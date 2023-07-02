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

package test.sog.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import sog.core.Objects;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
@Test.Subject( "test." )
public class ObjectsTest extends Test.Container {

	public ObjectsTest() {
		super ( Objects.class );
	}
	
	private final Object[] arrayA1 = new Object[] { 1, 2, 7, 42 };
	private final Object[] arrayA2 = new Object[] { 1, 2, 7, 42 };
	private final Object[] arrayB1 = new Object[] { 1L, 2L, 7L, 42L };

	private final Collection<Object> collA1 = List.of( 1, 2, 7, 42 );
	private final Collection<Object> collA2 = List.of( 1, 2, 7, 42 );
	private final Collection<Object> collB1 = List.of( 1L, 2L, 7L, 42L );

	private final Object[] deepArrayA1 = new Object[] { arrayA1, arrayA2, arrayB1, collA1, collA2, "Hello", false };
	private final Object[] deepArrayA2 = new Object[] { arrayA2, arrayA1, arrayB1, collA2, collA1, "Hell" + "o", !true };
	private final Object[] deepArrayB1 = new Object[] { arrayA1, arrayA1, arrayB1, collA1, collB1, "hello", true };
	
	private final Collection<Object> deepCollA1 = List.of( arrayA1, arrayA2, arrayB1, collA1, collA2, "Hello", false );
	private final Collection<Object> deepCollA2 = List.of( arrayA2, arrayA1, arrayB1, collA2, collA1, "Hell" + "o", !true );
	private final Collection<Object> deepCollB1 = List.of( arrayA1, arrayA1, arrayB1, collA1, collB1, "hello", true );

	public static class EqList extends ArrayList<String> {
		
		private static final long serialVersionUID = 0L;
		
		@Override public boolean equals( Object other ) { return true; }

	}
	

	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Is symmetric" 
	)
	public void tm_0CFD48003( Test.Case tc ) {
		tc.assertTrue( 
			Objects.deepArrayEquals( deepArrayA1, deepArrayA2 ) == Objects.deepArrayEquals( deepArrayA2, deepArrayA1 )
		);
		tc.assertTrue( 
			Objects.deepArrayEquals( deepArrayA1, deepArrayB1 ) == Objects.deepArrayEquals( deepArrayB1, deepArrayA1 )
		);
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Sample cases deep equal" 
	)
	public void tm_02A278907( Test.Case tc ) {
		Object[] array1 = new Object[] { deepArrayA1, deepCollA1, arrayA2, collA2, 7 };
		Object[] array2 = new Object[] { deepArrayA2, deepCollA2, arrayA1, collA1, 7+0 };
		tc.assertTrue( Objects.deepArrayEquals( array1, array2 ) );
		Object[] array3 = new Object[] { array1, array2 };
		Object[] array4 = new Object[] { array2, array1 };
		tc.assertTrue( Objects.deepArrayEquals( array3, array4 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Sample cases deep not equal" 
	)
	public void tm_0BEA760BA( Test.Case tc ) {
		Object[] array1 = new Object[] { deepArrayA1, deepCollA1, deepCollB1, collA2, 7 };
		Object[] array2 = new Object[] { deepCollA1, deepArrayA1, deepCollB1, collA1, 7+0 };
		tc.assertFalse( Objects.deepArrayEquals( array1, array2 ) );
		Object[] array3 = new Object[] { array1, array2 };
		Object[] array4 = new Object[] { array2, array1 };
		tc.assertFalse( Objects.deepArrayEquals( array3, array4 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array first arg" 
	)
	public void tm_0DD5FDCCE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( deepCollA1, deepArrayA1 );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array second arg" 
	)
	public void tm_06BBB1758( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( deepArrayA1, deepCollA1 );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "null equals null" 
	)
	public void tm_0F0061151( Test.Case tc ) {
		tc.assertTrue( Objects.deepArrayEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepArrayEquals(Object, Object)", 
		description = "null not equal non null" 
	)
	public void tm_0897D89C0( Test.Case tc ) {
		tc.assertFalse( Objects.deepArrayEquals( null, deepArrayA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Is symmetric" 
	)
	public void tm_06C02EE9C( Test.Case tc ) {
		tc.assertTrue( 
			Objects.deepCollectionEquals( deepCollA1, deepCollA2 ) == Objects.deepCollectionEquals( deepCollA2, deepCollA1 )
		);
		tc.assertTrue( 
			Objects.deepCollectionEquals( deepCollA1, deepCollB1 ) == Objects.deepCollectionEquals( deepCollB1, deepCollA1 )
		);
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Sample cases deep equal" 
	)
	public void tm_0B37430CE( Test.Case tc ) {
		Collection<Object> coll1 = List.of( deepArrayA1, deepCollA1, arrayA2, collA2, 7 );
		Collection<Object> coll2 = List.of( deepArrayA2, deepCollA2, arrayA1, collA1, 7+0 );
		tc.assertTrue( Objects.deepCollectionEquals( coll1, coll2 ) );
		Collection<Object> coll3 = List.of( coll1, coll2 );
		Collection<Object> coll4 = List.of( coll2, coll1 );
		tc.assertTrue( Objects.deepCollectionEquals( coll3, coll4 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Sample cases deep not equal" 
	)
	public void tm_0FE3CCD01( Test.Case tc ) {
		Collection<Object> coll1 = List.of( deepArrayA1, deepCollA2, arrayA2, collA2, 7 );
		Collection<Object> coll2 = List.of( deepCollA1, deepArrayA2, arrayA1, collA1, 7+0 );
		tc.assertFalse( Objects.deepCollectionEquals( coll1, coll2 ) );
		Collection<Object> coll3 = List.of( coll1, coll2 );
		Collection<Object> coll4 = List.of( coll2, coll1 );
		tc.assertFalse( Objects.deepCollectionEquals( coll3, coll4 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "null equals null" 
	)
	public void tm_0DBB98B6A( Test.Case tc ) {
		tc.assertTrue( Objects.deepCollectionEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "null not equal non null" 
	)
	public void tm_012CA3187( Test.Case tc ) {
		tc.assertFalse( Objects.deepCollectionEquals( null, deepCollA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "Array not equal collection" 
	)
	public void tm_0A72C8BF9( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( deepArrayA1, deepCollA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "Is symmetric" 
	)
	public void tm_0420452DC( Test.Case tc ) {
		Object array1 = new Object[] { deepArrayA1, deepCollA1, arrayA2, collA2, 7 };
		Object array2 = new Object[] { deepArrayA2, deepCollA2, arrayA1, collA1, 7+0 };
		tc.assertTrue( Objects.deepEquals( array1, array2 ) );
		Object array3 = new Object[] { array1, array2 };
		Object array4 = new Object[] { array2, array1 };
		tc.assertTrue( Objects.deepEquals( array3, array4 ) );
		
		Object coll1 = List.of( deepArrayA1, deepCollA1, arrayA2, collA2, 7 );
		Object coll2 = List.of( deepArrayA2, deepCollA2, arrayA1, collA1, 7+0 );
		tc.assertTrue( Objects.deepEquals( coll1, coll2 ) );
		Object coll3 = List.of( coll1, coll2 );
		Object coll4 = List.of( coll2, coll1 );
		tc.assertTrue( Objects.deepEquals( coll3, coll4 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "Object not equal array" 
	)
	public void tm_060A9955A( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( 1,  arrayA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "Object not equal collection" 
	)
	public void tm_0F0A5BE93( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( 2, collA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "null equals null" 
	)
	public void tm_04DEECFAA( Test.Case tc ) {
		tc.assertTrue( Objects.deepEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.deepEquals(Object, Object)", 
		description = "null not equal non null" 
	)
	public void tm_0732BA547( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( null, 2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.equals(Object, Object)", 
		description = "Is symmetric" 
	)
	public void tm_04F1C5F68( Test.Case tc ) {
		Object[] args = new Object[] { 
			null, 42, arrayA1, arrayA2, arrayA1, collA1, collA2, collA1 };
		for ( int i = 0; i < args.length - 1; i++ ) {
			for ( int j = i + 1; j < args.length; j++ ) {
				tc.assertEqual( Objects.equals( args[i], args[j] ), Objects.equals( args[j],  args[i] ) );
			}
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.equals(Object, Object)", 
		description = "Sample cases for equals" 
	)
	public void tm_0638962F8( Test.Case tc ) {
		Object[] args1 = new Object[] {
			arrayA1, arrayB1, collA2, collB1, "Hello", Set.of( 1, 2, 3)
		};
		Object[] args2 = new Object[] {
			arrayA1, arrayB1, collA2, collB1, "Hel" + "lo", Set.of( 1, 2, 3)
		};
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertTrue( Objects.equals( args1[i], args2[i] ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.equals(Object, Object)", 
		description = "Sample cases for not equals" 
	)
	public void tm_0B893A6A5( Test.Case tc ) {
		Object[] args1 = new Object[] {
			arrayA1, arrayB1, collA2, collA1, "Hello ", Set.of( 2, 3)
		};
		Object[] args2 = new Object[] {
			arrayA2, arrayA1, collB1, collB1, "hel" + "lo", Set.of( 1, 2, 3)
		};
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertFalse( Objects.equals( args1[i], args2[i] ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.equals(Object, Object)", 
		description = "null equals null" 
	)
	public void tm_0BFD5B636( Test.Case tc ) {
		tc.assertTrue( Objects.equals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.equals(Object, Object)", 
		description = "null not equal non null" 
	)
	public void tm_04FFA893B( Test.Case tc ) {
		tc.assertFalse( Objects.equals( null, new Object() ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Is symmetric" 
	)
	public void tm_01451A903( Test.Case tc ) {
		Object[] args = new Object[] { arrayA1, arrayA2, arrayB1, deepArrayA1, deepArrayA2 };
		for ( int i = 0; i < args.length - 1; i++ ) {
			for ( int j = i + 1; j < args.length; j++ ) {
				tc.assertEqual( 
					Objects.shallowArrayEquals( args[i], args[j] ), 
					Objects.shallowArrayEquals( args[j], args[i] ) 
				);
			}
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow but not deep" 
	)
	public void tm_02B19A06B( Test.Case tc ) {
		List<String> list1 = new EqList();
		list1.add( "A" );
		list1.add( "B" );

		List<String> list2 = new EqList();
		list2.add( "B" );
		list2.add( "A" );
		
		// Now list1.equals( list 2 ) but not equal component-wise

		Object arg1 = new Object[] { list1, list2, "42", 42 };
		Object arg2 = new Object[] { list2, list1, "42", 42 };
		tc.assertTrue( Objects.shallowArrayEquals( arg1, arg2 ) );
		tc.assertFalse( Objects.deepArrayEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow equal" 
	)
	public void tm_08476B897( Test.Case tc ) {
		tc.assertTrue( Objects.shallowArrayEquals( arrayA1, arrayA2 ) );
		Object[] arg1 = new Object[] { arrayA1, arrayB1, "Hello" };
		Object[] arg2 = new Object[] { arrayA1, arrayB1, "Hello" };
		tc.assertTrue( Objects.shallowArrayEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow not equal" 
	)
	public void tm_0F7B4484A( Test.Case tc ) {
		tc.assertFalse( Objects.shallowArrayEquals( arrayA1, arrayB1 ) );
		Object[] arg1 = new Object[] { arrayA1, collA1 };
		Object[] arg2 = new Object[] { arrayA2, collA2 };
		tc.assertFalse( Objects.shallowArrayEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array first arg" 
	)
	public void tm_07952C5CE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( collA1, arrayA1 );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array second arg" 
	)
	public void tm_04E254E58( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( arrayA1, collA1 );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "null equals null" 
	)
	public void tm_0EFC6BA51( Test.Case tc ) {
		tc.assertTrue( Objects.shallowArrayEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "null not equal non null" 
	)
	public void tm_0F85CC0C0( Test.Case tc ) {
		tc.assertFalse( Objects.shallowArrayEquals( null, arrayA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Is symmetric" 
	)
	public void tm_09FF0659C( Test.Case tc ) {
		Collection<?>[] args = new Collection<?>[] { collA1, collA2, collB1, deepCollA1, deepCollA2 };
		for ( int i = 0; i < args.length - 1; i++ ) {
			for ( int j = i + 1; j < args.length; j++ ) {
				tc.assertEqual( 
					Objects.shallowCollectionEquals( args[i], args[j] ), 
					Objects.shallowCollectionEquals( args[j], args[i] ) 
				);
			}
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow but not deep" 
	)
	public void tm_04934ED72( Test.Case tc ) {
		List<String> list1 = new EqList();
		list1.add( "A" );
		list1.add( "B" );

		List<String> list2 = new EqList();
		list2.add( "B" );
		list2.add( "A" );
		
		// Now list1.equals( list 2 ) but not equal component-wise

		Collection<Object> arg1 = List.of( list1, list2, "42", 42 );
		Collection<Object> arg2 = List.of( list2, list1, "42", 42 );
		tc.assertTrue( Objects.shallowCollectionEquals( arg1, arg2 ) );
		tc.assertFalse( Objects.deepCollectionEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow equal" 
	)
	public void tm_039BEF570( Test.Case tc ) {
		tc.assertTrue( Objects.shallowCollectionEquals( collA1, collA2 ) );
		Collection<Object> arg1 = List.of( arrayA1, arrayB1, "Hello" );
		Collection<Object> arg2 = List.of( arrayA1, arrayB1, "Hello" );
		tc.assertTrue( Objects.shallowCollectionEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow not equal" 
	)
	public void tm_0227070A3( Test.Case tc ) {
		tc.assertFalse( Objects.shallowCollectionEquals( collA1, collB1 ) );
		Collection<Object> arg1 = List.of( arrayA1, collA1 );
		Collection<Object> arg2 = List.of( arrayA2, collA2 );
		tc.assertFalse( Objects.shallowCollectionEquals( arg1, arg2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "null equals null" 
	)
	public void tm_0DE13826A( Test.Case tc ) {
		tc.assertTrue( Objects.shallowCollectionEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "null not equal non null" 
	)
	public void tm_0AC9C5A87( Test.Case tc ) {
		tc.assertFalse( Objects.shallowCollectionEquals( null, collA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "Array not equal collection" 
	)
	public void tm_0E26D82F9( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( arrayA1, collA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "Is symmetric" 
	)
	public void tm_02C3189DC( Test.Case tc ) {
		Object[] args = new Object[] { arrayA1, arrayA2, arrayB1, collA1, collA2, collB1, deepArrayA1, deepArrayA2, deepCollA1, deepCollA2 };
		for ( int i = 0; i < args.length - 1; i++ ) {
			for ( int j = i + 1; j < args.length; j++ ) {
				tc.assertEqual( 
					Objects.shallowEquals( args[i], args[j] ), 
					Objects.shallowEquals( args[j], args[i] ) 
				);
			}
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "Object not equal array" 
	)
	public void tm_058FE0C5A( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( "42", arrayA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "Object not equal collection" 
	)
	public void tm_01D83A793( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( "42", collA1 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "null equals null" 
	)
	public void tm_060A886AA( Test.Case tc ) {
		tc.assertTrue( Objects.shallowEquals( null, null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Objects.shallowEquals(Object, Object)", 
		description = "null not equal non null" 
	)
	public void tm_085660E47( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( null, "42" ) );
	}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Objects.class ).showDetails( true ).print();
	}
}
