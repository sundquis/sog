/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import sog.core.Objects;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class ObjectsTestOLD extends Test.Container {
	
	public ObjectsTestOLD() {
		super( Objects.class );
	}

	// Test implementations

	// deepArrayEquals
	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Is symmetric" )
	public void deepArrayEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Sample cases deep equal" )
	public void deepArrayEquals_SampleCasesDeepEquals( Test.Case tc ) {
		Object[] o1 = {
			42,
			Arrays.asList( 1, 2, 3 ),
			new boolean[] { true, false },
			new Object[] { Arrays.asList( 'a', 'b', 'c' ) },
			"A string"
		};
		
		Object[] o2 = {
			42,
			Arrays.asList( 1, 2, 3 ),
			new Boolean[] { Boolean.TRUE, Boolean.FALSE },
			new Object[] { Arrays.asList( 'a', 'b', 'c' ) },
			"A" + " string"
		};
		
		tc.assertTrue( Objects.deepArrayEquals( o1,  o2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Sample cases deep not equal" )
	public void deepArrayEquals_SampleCasesDeepNotEqual( Test.Case tc ) {
		Object[] o1 = {
				42,
				Arrays.asList( 1, 2, 3 ),
				new boolean[] { true, false },
				new Object[] { Arrays.asList( 'a', 'b', 'c' ) },
				"A string"
			};
			
			Object[] o2 = {
				42,
				Arrays.asList( 1, 2, 3 ),
				new Boolean[] { Boolean.TRUE, Boolean.FALSE },
				new Object[] { Arrays.asList( 'c', 'b', 'a' ) },  // Transposition
				"A" + " string"
			};
			
			tc.assertFalse( Objects.deepArrayEquals( o1,  o2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array first arg" )
	public void deepArrayEquals_ThrowsAssertionErrorForNonArrayFirstArg( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( "foo", new String[] {"foo" } );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array second arg" )
	public void deepArrayEquals_ThrowsAssertionErrorForNonArraySecondArg( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( new String[] {"foo" }, "foo" );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "null equals null" )
	public void deepArrayEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.deepArrayEquals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepArrayEquals(Object, Object)", 
		description = "null not equal non null" )
	public void deepArrayEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.deepArrayEquals( null,  new int[] {1} ) );
	}


	
	// deepCollectionEquals
	@Test.Impl( 
		member = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Is symmetric" )
	public void deepCollectionEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Sample cases deep equal" )
	public void deepCollectionEquals_SampleCasesDeepEqual( Test.Case tc ) {
		Collection<Object> c1 = Arrays.asList(
			42,
			Arrays.asList( 1, 2, 3 ),
			new boolean[] { true, false },
			"A string",
			new Object[] { Arrays.asList( 'a', 'b', 'c' ) }
		);
		
		Collection<Object> c2 = Arrays.asList(
			42,
			Arrays.asList( 1, 2, 3 ),
			new Boolean[] { Boolean.TRUE, Boolean.FALSE },
			"A" + " string",
			new Object[] { Arrays.asList( 'a', 'b', 'c' ) }
		);
		
		tc.assertTrue( Objects.deepCollectionEquals( c1, c2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "Sample cases deep not equal" )
	public void deepCollectionEquals_SampleCasesDeepNotEqual( Test.Case tc ) {
		Collection<Object> c1 = Arrays.asList(
			42,
			Arrays.asList( 1, 2, 3 ),
			new boolean[] { true, false },
			"A string",
			new Object[] { Arrays.asList( 'a', 'b', 'c' ) }
		);
		
		Collection<Object> c2 = Arrays.asList(
			42,
			Arrays.asList( 1, 2, 3 ),
			new Boolean[] { Boolean.TRUE, Boolean.FALSE },
			"A" + " string",
			new Object[] { Arrays.asList( 'c', 'b', 'a' ) }  // Transposition
		);
		
		tc.assertFalse( Objects.deepCollectionEquals( c1, c2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		description = "null equals null" )
	public void deepCollectionEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.deepCollectionEquals( null,  null ) );
	}

	@Test.Impl( member = "public boolean Objects.deepCollectionEquals(Collection, Collection)", description = "null not equal non null" )
	public void deepCollectionEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.deepCollectionEquals( null, new ArrayList<String>() ) );
	}

	
	// deepEquals
	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "Array not equal collection" )
	public void deepEquasl_ArrayNotEqualCollection( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( new String[] { "a" },  Arrays.asList( "a" ) ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "Is symmetric" )
	public void deepEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "Object not equal array" )
	public void deepEquals_ObjectNotEqualArray( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( "A",  new String[] { "A" } ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "Object not equal collection" )
	public void deepEquals_ObjectNotEqualCollection( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( "A", Arrays.asList( "A" ) ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "null equals null" )
	public void deepEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.deepEquals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.deepEquals(Object, Object)", 
		description = "null not equal non null" )
	public void deepEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.deepEquals( null,  "null" ) );
	}

	
	// equals
	@Test.Impl( 
		member = "public boolean Objects.equals(Object, Object)", 
		description = "Is symmetric" )
	public void equals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.equals(Object, Object)", 
		description = "Sample cases for equals" )
	public void equals_SampleCasesForEquals( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.equals(Object, Object)", 
		description = "Sample cases for not equals" )
	public void equals_SampleCasesForNotEquals( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl(
		member = "public boolean Objects.equals(Object, Object)", 
		description = "null equals null" )
	public void equals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.equals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.equals(Object, Object)", 
		description = "null not equal non null" )
	public void equals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.equals( null,  "foo" ) );
	}

	
	// shallowArrayEquals
	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Is symmetric" )
	public void shallowArrayEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow but not deep" )
	public void shallowArrayEquals_SampleCasesShallowButNotDeep( Test.Case tc ) {
		
		@SuppressWarnings("serial")
		class EqList extends ArrayList<String> {
			@Override public boolean equals( Object other ) { return true; }
		}

		// Build two lists with
		// l1.equals( l2 ) but not component-wise...
		List<String> l1 = new EqList();
		l1.add( "A" );
		l1.add( "B" );
		List<String> l2 = new EqList();
		l2.add( "B" );
		l2.add( "A" );
		
		Object[] o1 = {
			42,
			l1,
			"A string"
		};
		
		Object[] o2 = {
			42,
			l2,
			"A" + " string"
		};
		
		tc.assertFalse( Objects.deepArrayEquals( o1,  o2 ) );
		tc.assertTrue( Objects.shallowArrayEquals( o1,  o2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow equal" )
	public void shallowArrayEquals_SampleCasesShallowEqual( Test.Case tc ) {
		@SuppressWarnings("serial")
		class EqList extends ArrayList<String> {
			@Override public boolean equals( Object other ) { return true; }
		}

		// Build two lists with
		// l1.equals( l2 ) but not component-wise...
		List<String> l1 = new EqList();
		l1.add( "A" );
		l1.add( "B" );
		List<String> l2 = new EqList();
		l2.add( "B" );
		l2.add( "A" );

		Object[] o1 = {
				42,
				"A string",
				l1
			};
			
			Object[] o2 = {
				42,
				"A" + " string",
				l2
			};
			
			tc.assertTrue( Objects.shallowArrayEquals( o1,  o2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Sample cases shallow not equal" )
	public void shallowArrayEquals_SampleCasesShallowNotEqual( Test.Case tc ) {
		Object[] o1 = {
				42,
				"A string",
				new int[] { 1, 2, 3 }
			};
			
			Object[] o2 = {
				42,
				"A" + " string",
				new int[] { 1, 2, 3 }
			};
			
			tc.assertFalse( Objects.shallowArrayEquals( o1,  o2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array first arg" )
	public void shallowArrayEquals_ThrowsAssertionErrorForNonArrayFirstArg( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( 42,  new int[] {42} );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "Throws Assertion error for non array second arg" )
	public void shallowArrayEquals_ThrowsAssertionErrorForNonArraySecondArg( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( new int[] {42}, 42 );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "null equals null" )
	public void shallowArrayEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.shallowArrayEquals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		description = "null not equal non null" )
	public void shallowArrayEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.shallowArrayEquals( null,  new int[]{} ) );
	}

	
	// shallowCollectionEquals
	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Is symmetric" )
	public void shallowCollectionEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow but not deep" )
	public void shallowCollectionEquals_SampleCasesShallowButNotDeep( Test.Case tc ) {
		
		@SuppressWarnings("serial")
		class EqList extends ArrayList<String> {
			@Override public boolean equals( Object other ) { return true; }
		}

		// Build two lists with
		// l1.equals( l2 ) but not component-wise...
		List<String> l1 = new EqList();
		l1.add( "A" );
		l1.add( "B" );
		List<String> l2 = new EqList();
		l2.add( "B" );
		l2.add( "A" );
		
		Collection<Object> c1 = Arrays.asList(
			42,
			l1,
			"A string"
		);
		
		Collection<Object> c2 = Arrays.asList(
			42,
			l2,
			"A" + " string"
		);
		
		tc.assertFalse( Objects.deepCollectionEquals( c1,  c2 ) );
		tc.assertTrue( Objects.shallowCollectionEquals( c1,  c2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow equal" )
	public void shallowCollectionEquals_SampleCasesShallowEqual( Test.Case tc ) {
		@SuppressWarnings("serial")
		class EqList extends ArrayList<String> {
			@Override public boolean equals( Object other ) { return true; }
		}

		// Build two lists with
		// l1.equals( l2 ) but not component-wise...
		List<String> l1 = new EqList();
		l1.add( "A" );
		l1.add( "B" );
		List<String> l2 = new EqList();
		l2.add( "B" );
		l2.add( "A" );
		
		Collection<Object> c1 = Arrays.asList(
			42,
			"A string",
			l1
		);
		
		Collection<Object> c2 = Arrays.asList(
			42,
			"A" + " string",
			l2
		);
			
		tc.assertTrue( Objects.shallowCollectionEquals( c1,  c2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "Sample cases shallow not equal" )
	public void shallowCollectionEquals_SampleCasesShallowNotEqual( Test.Case tc ) {
		Collection<Object> c1 = Arrays.asList(
			42,
			"A string",
			new int[] {}
		);
		
		Collection<Object> c2 = Arrays.asList(
			42,
			"A" + " string",
			new int[] {}
		);
			
		tc.assertFalse( Objects.shallowCollectionEquals( c1,  c2 ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "null equals null" )
	public void shallowCollectionEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.shallowCollectionEquals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		description = "null not equal non null" )
	public void shallowCollectionEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.shallowCollectionEquals( null,  new ArrayList<String>() ) );
	}

	
	// shallowEquals
	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "Array not equal collection" )
	public void shallowEquals_ArrayNotEqualCollection( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( new int[] {1},  Arrays.asList( 1 ) ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "Is symmetric" )
	public void shallowEquals_IsSymmetric( Test.Case tc ) {
		//tc.addMessage( "Manually verified" );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "Object not equal array" )
	public void shallowEquals_ObjectNotEqualArray( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( 42, new int[] {42} ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "Object not equal collection" )
	public void shallowEquals_ObjectNotEqualCollection( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( 42,  Arrays.asList( 42 ) ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "null equals null" )
	public void shallowEquals_NullEqualsNull( Test.Case tc ) {
		tc.assertTrue( Objects.shallowEquals( null,  null ) );
	}

	@Test.Impl( 
		member = "public boolean Objects.shallowEquals(Object, Object)", 
		description = "null not equal non null" )
	public void shallowEquals_NullNotEqualNonNull( Test.Case tc ) {
		tc.assertFalse( Objects.shallowEquals( null,  42) );
	}

	
}
