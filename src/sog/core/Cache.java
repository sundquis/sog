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
package sog.core;



import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A Cache implementation built on java SoftReferences.
 * 
 * Elements are stored as (key, value) pairs. The keys (type parameter K) are Comparable
 * and values (type parameter V) are retrieved from a map using soft references. When a softly
 * held value is garbage-collected the association is removed from the map and if subsequently
 * needed, the missing value is reconstructed on demand by the associated Builder.
 */
@Test.Subject( "test." )
public final class Cache<K extends Comparable<K>, V> {
	

	/**
	 * When the value corresponding to a key is not found in the cache
	 * we use a Builder to make a value instance. The key must
	 * contain all the information needed by the builder
	 * to create the object. A Builder must never make null objects.
	 */
	@FunctionalInterface
	public interface Builder<K, V> {
		public V make( K key ) throws AppException;
	}

	/** A SoftReference that also holds the key */
	private static final class SoftRef<K, V> extends SoftReference<V> {

		private final K key;

		private SoftRef( K key, V value, ReferenceQueue<V> rq ) {
			super( value, rq );
			this.key = Assert.nonNull( key );
		}
	}


	/** The builder that creates objects for this cache. **/
	private final Builder<K, V> builder;

	/** Maintains hard references to SoftReferece objects whose referent has been collected */
	private final ReferenceQueue<V> rq;

	/** The contents of the cache are stored here. **/
	private final SortedMap<K, SoftRef<K, V>> map;

	/** The number of Values that have been garbage collected. */
	private int collected;
	
	/** Construct */
	@Test.Decl( "Throws AssertionError for null builder" )
	public Cache( Builder<K, V> builder ) {
		this.builder = Assert.nonNull( builder );
		this.rq = new ReferenceQueue<V>();
		this.map = new TreeMap<K, SoftRef<K, V>>();
		this.collected = 0;
	}

	/**
	 * Return the value corresponding to the given key. If the value is not currently
	 * held, the associated builder is used to construct an instance.
	 * 
	 * @param key
	 * @return
	 * @throws AppException		If the builder is unable to construct the value.
	 */
	@SuppressWarnings( "unchecked" )
	@Test.Decl( "Throws AssertionError for null key" )
	@Test.Decl( "Throws AssertionError if Builder produces null" )
	@Test.Decl( "Throws AppException if Builder throws exception" )
	@Test.Decl( "From empty cache returns valid object" )
	@Test.Decl( "After clear() produces equivalent object" )
	@Test.Decl( "After clear() produces non-identical object" )
	@Test.Decl( "Stored uncolllectable object returns identical object" )
	@Test.Decl( "After garbage collection produces valid object" )
	@Test.Decl( "Produces identical values for identical keys before collection" )
	@Test.Decl( "Produces identical values for identical keys after collection" )
	@Test.Decl( "Values before and after collection are equivalent" )
	@Test.Decl( "Values before and after collection are not identical" )
	public V get( K key ) throws AppException {
		Assert.nonNull( key );
		V value = null;

		synchronized ( this.map ) {
			// Attempt to find a SoftRef for the given key and its associated Value
			SoftRef<K, V> sr = this.map.get( key );
			value = (sr == null) ? null : sr.get();

			// If value == null there are a number of possible cases:
			//		This is the first request for the desired SofRef and it has never been added to the map.
			// 		The map does not contain a SoftRef for the key because it was removed.
			//		The SoftRef is in the map but its value was collected before sr.get() completed.

			// If value != null we now hold a strong reference to it, it cannot be collected,
			// and the map holds the association
			
			// Before continuing we poll the reference queue to see if collection has occurred.
			// If it has we can remove the associations from the map.
			while ( (sr = (SoftRef<K, V>) this.rq.poll()) != null ) {
				this.map.remove( sr.key );
				this.collected++;
			}

			if ( value == null ) {
				try {
					value = this.builder.make( key );
				} catch ( Throwable t ) {
					throw new AppException( t );
				}
				this.map.put( key, new SoftRef<K, V>( key, Assert.nonNull( value ), this.rq ) );
			}
		}

		return value;
	}
	

	@Test.Decl( "Idempotent" )
	public void clear() {
		this.map.clear();
	}
	
	/**
	 * The number of Value instances that have been collected.
	 * 
	 * @return
	 */
	@Test.Decl( "Zero at creation" )
	@Test.Decl( "Unchanged after clear()" )
	@Test.Decl( "Greater than zero when the size of the map decreases" )
	@Test.Decl( "collected() + size() is invariant across collections" )
	public int collected() {
		return this.collected;
	}
	
	/**
	 * Current number of cached values.
	 * 
	 * @return
	 */
	@Test.Decl( "Created empty" )
	@Test.Decl( "Zero after clear()" )
	@Test.Decl( "Not empty after get" )
	public int size() {
		return this.map.size();
	}

	@Override
	@Test.Decl( "Result is not empty" )
	public String toString() {
		return "Cache(" + this.size() + " key" + (this.size() == 1 ? "" : "s") + ")";
	}

	
}
