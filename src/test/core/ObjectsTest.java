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
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class ObjectsTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Objects.class;
	}

	// Test implementations

	// deepArrayEquals
	@Test.Impl( 
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "Is symmetric" )
	public void deepArrayEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "Sample cases deep equal" )
	public void deepArrayEquals_SampleCasesDeepEquals( TestCase tc ) {
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
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "Sample cases deep not equal" )
	public void deepArrayEquals_SampleCasesDeepNotEqual( TestCase tc ) {
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
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "Throws Assertion error for non array first arg" )
	public void deepArrayEquals_ThrowsAssertionErrorForNonArrayFirstArg( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( "foo", new String[] {"foo" } );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "Throws Assertion error for non array second arg" )
	public void deepArrayEquals_ThrowsAssertionErrorForNonArraySecondArg( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Objects.deepArrayEquals( new String[] {"foo" }, "foo" );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "null equals null" )
	public void deepArrayEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.deepArrayEquals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepArrayEquals(Object, Object)", 
		desc = "null not equal non null" )
	public void deepArrayEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.deepArrayEquals( null,  new int[] {1} ) );
	}


	
	// deepCollectionEquals
	@Test.Impl( 
		src = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		desc = "Is symmetric" )
	public void deepCollectionEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		desc = "Sample cases deep equal" )
	public void deepCollectionEquals_SampleCasesDeepEqual( TestCase tc ) {
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
		src = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		desc = "Sample cases deep not equal" )
	public void deepCollectionEquals_SampleCasesDeepNotEqual( TestCase tc ) {
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
		src = "public boolean Objects.deepCollectionEquals(Collection, Collection)", 
		desc = "null equals null" )
	public void deepCollectionEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.deepCollectionEquals( null,  null ) );
	}

	@Test.Impl( src = "public boolean Objects.deepCollectionEquals(Collection, Collection)", desc = "null not equal non null" )
	public void deepCollectionEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.deepCollectionEquals( null, new ArrayList<String>() ) );
	}

	
	// deepEquals
	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "Array not equal collection" )
	public void deepEquasl_ArrayNotEqualCollection( TestCase tc ) {
		tc.assertFalse( Objects.deepEquals( new String[] { "a" },  Arrays.asList( "a" ) ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "Is symmetric" )
	public void deepEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "Object not equal array" )
	public void deepEquals_ObjectNotEqualArray( TestCase tc ) {
		tc.assertFalse( Objects.deepEquals( "A",  new String[] { "A" } ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "Object not equal collection" )
	public void deepEquals_ObjectNotEqualCollection( TestCase tc ) {
		tc.assertFalse( Objects.deepEquals( "A", Arrays.asList( "A" ) ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "null equals null" )
	public void deepEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.deepEquals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.deepEquals(Object, Object)", 
		desc = "null not equal non null" )
	public void deepEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.deepEquals( null,  "null" ) );
	}

	
	// equals
	@Test.Impl( 
		src = "public boolean Objects.equals(Object, Object)", 
		desc = "Is symmetric" )
	public void equals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.equals(Object, Object)", 
		desc = "Sample cases for equals" )
	public void equals_SampleCasesForEquals( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.equals(Object, Object)", 
		desc = "Sample cases for not equals" )
	public void equals_SampleCasesForNotEquals( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl(
		src = "public boolean Objects.equals(Object, Object)", 
		desc = "null equals null" )
	public void equals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.equals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.equals(Object, Object)", 
		desc = "null not equal non null" )
	public void equals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.equals( null,  "foo" ) );
	}

	
	// shallowArrayEquals
	@Test.Impl( 
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Is symmetric" )
	public void shallowArrayEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Sample cases shallow but not deep" )
	public void shallowArrayEquals_SampleCasesShallowButNotDeep( TestCase tc ) {
		
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
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Sample cases shallow equal" )
	public void shallowArrayEquals_SampleCasesShallowEqual( TestCase tc ) {
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
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Sample cases shallow not equal" )
	public void shallowArrayEquals_SampleCasesShallowNotEqual( TestCase tc ) {
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
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Throws Assertion error for non array first arg" )
	public void shallowArrayEquals_ThrowsAssertionErrorForNonArrayFirstArg( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( 42,  new int[] {42} );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "Throws Assertion error for non array second arg" )
	public void shallowArrayEquals_ThrowsAssertionErrorForNonArraySecondArg( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Objects.shallowArrayEquals( new int[] {42}, 42 );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "null equals null" )
	public void shallowArrayEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.shallowArrayEquals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowArrayEquals(Object, Object)", 
		desc = "null not equal non null" )
	public void shallowArrayEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.shallowArrayEquals( null,  new int[]{} ) );
	}

	
	// shallowCollectionEquals
	@Test.Impl( 
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "Is symmetric" )
	public void shallowCollectionEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "Sample cases shallow but not deep" )
	public void shallowCollectionEquals_SampleCasesShallowButNotDeep( TestCase tc ) {
		
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
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "Sample cases shallow equal" )
	public void shallowCollectionEquals_SampleCasesShallowEqual( TestCase tc ) {
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
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "Sample cases shallow not equal" )
	public void shallowCollectionEquals_SampleCasesShallowNotEqual( TestCase tc ) {
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
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "null equals null" )
	public void shallowCollectionEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.shallowCollectionEquals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowCollectionEquals(Collection, Collection)", 
		desc = "null not equal non null" )
	public void shallowCollectionEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.shallowCollectionEquals( null,  new ArrayList<String>() ) );
	}

	
	// shallowEquals
	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "Array not equal collection" )
	public void shallowEquals_ArrayNotEqualCollection( TestCase tc ) {
		tc.assertFalse( Objects.shallowEquals( new int[] {1},  Arrays.asList( 1 ) ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "Is symmetric" )
	public void shallowEquals_IsSymmetric( TestCase tc ) {
		tc.addMessage( "Manually verified" ).pass();
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "Object not equal array" )
	public void shallowEquals_ObjectNotEqualArray( TestCase tc ) {
		tc.assertFalse( Objects.shallowEquals( 42, new int[] {42} ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "Object not equal collection" )
	public void shallowEquals_ObjectNotEqualCollection( TestCase tc ) {
		tc.assertFalse( Objects.shallowEquals( 42,  Arrays.asList( 42 ) ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "null equals null" )
	public void shallowEquals_NullEqualsNull( TestCase tc ) {
		tc.assertTrue( Objects.shallowEquals( null,  null ) );
	}

	@Test.Impl( 
		src = "public boolean Objects.shallowEquals(Object, Object)", 
		desc = "null not equal non null" )
	public void shallowEquals_NullNotEqualNonNull( TestCase tc ) {
		tc.assertFalse( Objects.shallowEquals( null,  42) );
	}

	
	
	public static void main(String[] args) {

		System.out.println();

		new Test(ObjectsTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
