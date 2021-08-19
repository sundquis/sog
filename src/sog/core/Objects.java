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

package sog.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Compare java.util.Objects
 */
@Test.Subject( "test." )
public class Objects {
	
	// Static functions only
	private Objects() {}
	
	
	/*
	 * Replication of java.util.Objects
	 * Included for completeness
	 */
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Sample cases for equals" )
	@Test.Decl( "Sample cases for not equals" )
	public static boolean equals( Object o1, Object o2 ) {
		if ( o1 == null || o2 == null ) {
			return o1 == o2;
		}
		
		return o1.equals( o2 );
	}

	/*
	 * If objects are collections or arrays compare components using equal()
	 * Otherwise use equal()
	 */
	@SuppressWarnings("unchecked")
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Object not equal array" )
	@Test.Decl( "Object not equal collection" )
	@Test.Decl( "Array not equal collection" )
	public static boolean shallowEquals( Object o1, Object o2 ) {
		if ( o1 == null || o2 == null ) {
			return o1 == o2;
		}

		if ( o1.getClass().isArray() ) {
			if ( o2.getClass().isArray() ) {
				return shallowArrayEquals( o1, o2 );
			} else {
				return false;
			}
		}
		
		if ( Collection.class.isAssignableFrom( o1.getClass() ) ) {
			if ( Collection.class.isAssignableFrom( o2.getClass() ) ) {
				return shallowCollectionEquals( (Collection<Object>) o1,  (Collection<Object>) o2 );
			} else {
				return false;
			}
		}
		
		return o1.equals( o2 );		
	}
	
	/*
	 * If objects are collections or arrays compare components using deepEequal()
	 * Otherwise use equal()
	 */
	@SuppressWarnings("unchecked")
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Object not equal array" )
	@Test.Decl( "Object not equal collection" )
	@Test.Decl( "Array not equal collection" )
	public static boolean deepEquals( Object o1, Object o2 ) {
		if ( o1 == null || o2 == null ) {
			return o1 == o2;
		}

		if ( o1.getClass().isArray() ) {
			if ( o2.getClass().isArray() ) {
				return deepArrayEquals( o1, o2 );
			} else {
				return false;
			}
		}
		
		if ( Collection.class.isAssignableFrom( o1.getClass() ) ) {
			if ( Collection.class.isAssignableFrom( o2.getClass() ) ) {
				return deepCollectionEquals( (Collection<Object>) o1,  (Collection<Object>) o2 );
			} else {
				return false;
			}
		}
		
		return o1.equals( o2 );		
	}

	
	/*
	 * Shallow check for equality of two arrays
	 */
	@Test.Decl( "Throws Assertion error for non array first arg" )
	@Test.Decl( "Throws Assertion error for non array second arg" )
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Sample cases shallow equal" )
	@Test.Decl( "Sample cases shallow not equal" )
	@Test.Decl( "Sample cases shallow but not deep" )
	public static boolean shallowArrayEquals( Object a1, Object a2 ) {
		if ( a1 == null || a2 == null ) {
			return a1 == a2;
		}
		
		Assert.isTrue( a1.getClass().isArray() );
		Assert.isTrue( a2.getClass().isArray() );
		
		int length = Array.getLength( a1 );
		if ( length != Array.getLength( a2 ) ) {
			return false;
		}
		
		boolean areEqual = true;
		int index = 0;
		while ( areEqual && index < length ) {
			areEqual = equals( Array.get( a1,  index ), Array.get( a2,  index ) );
			index++;
		}
		
		return areEqual;
	}

	
	/*
	 * Deep check for equality of two arrays
	 */
	@Test.Decl( "Throws Assertion error for non array first arg" )
	@Test.Decl( "Throws Assertion error for non array second arg" )
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Sample cases deep equal" )
	@Test.Decl( "Sample cases deep not equal" )
	public static boolean deepArrayEquals( Object a1, Object a2 ) {
		if ( a1 == null || a2 == null ) {
			return a1 == a2;
		}

		Assert.isTrue( a1.getClass().isArray() );
		Assert.isTrue( a2.getClass().isArray() );
		
		int length = Array.getLength( a1 );
		if ( length != Array.getLength( a2 ) ) {
			return false;
		}
		
		boolean areEqual = true;
		int index = 0;
		while ( areEqual && index < length ) {
			areEqual = deepEquals( Array.get( a1,  index ), Array.get( a2,  index ) );
			index++;
		}
		
		return areEqual;
	}
	
	
	/*
	 * Shallow check for equality of two Collection objects
	 * Examines elements in the order presented by the iterators.
	 */
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Sample cases shallow equal" )
	@Test.Decl( "Sample cases shallow not equal" )
	@Test.Decl( "Sample cases shallow but not deep" )
	public static boolean shallowCollectionEquals( Collection<?> c1, Collection<?> c2 ) {
		if ( c1 == null || c2 == null ) {
			return c1 == c2;
		}
		
		int size = c1.size();
		if ( size != c2.size() ) {
			return false;
		}
		
		boolean areEqual = true;
		Iterator<?> iter1 = c1.iterator();
		Iterator<?> iter2 = c2.iterator();
		while ( areEqual && iter1.hasNext() ) {
			areEqual = equals( iter1.next(), iter2.next() );
		}
		
		return areEqual;
	}

	
	/*
	 * Deep check for equality of two Collection objects
	 * Examines elements in the order presented by the iterators.
	 */
	@Test.Decl( "null equals null" )
	@Test.Decl( "null not equal non null" )
	@Test.Decl( "Is symmetric" )
	@Test.Decl( "Sample cases deep equal" )
	@Test.Decl( "Sample cases deep not equal" )
	public static boolean deepCollectionEquals( Collection<?> c1, Collection<?> c2 ) {
		if ( c1 == null || c2 == null ) {
			return c1 == c2;
		}
		
		int size = c1.size();
		if ( size != c2.size() ) {
			return false;
		}
		
		boolean areEqual = true;
		Iterator<?> iter1 = c1.iterator();
		Iterator<?> iter2 = c2.iterator();
		while ( areEqual && iter1.hasNext() ) {
			areEqual = deepEquals( iter1.next(), iter2.next() );
		}
		
		return areEqual;
	}

	
		
}
