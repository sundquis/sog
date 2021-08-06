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



import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.SortedMap;
import java.util.TreeMap;

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

		SoftRef( K key, V value, ReferenceQueue<V> rq ) {
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

	/** Useful for diagnostics. */
	private boolean collected;
	
	/** Construct */
	@Test.Decl( "Null Builder throws Assertion Error" )
	public Cache( Builder<K, V> builder ) {
		this.builder = Assert.nonNull( builder );
		this.rq = new ReferenceQueue<V>();
		this.map = new TreeMap<K, SoftRef<K, V>>();
		this.collected = false;
	}

	/**
	 * True if the reference queue has provided a soft reference.
	 * This can only happen if get() is called after a garbage collection.
	 * 
	 * @return
	 */
	@Test.Decl( "False at creation" )
	public boolean collected() {
		return collected;
	}
	
	/**
	 * Return the value corresponding to the given key. If the value is not currently
	 * held the associated builder is used to construct an instance.
	 * 
	 * @param key
	 * @return
	 * @throws AppException		If the builder is unable to construct the value.
	 */
	@Test.Decl( "Null key throws Assertion Error" )
	@Test.Decl( "Values are not null" )
	@Test.Decl( "From empty cache returns valid object" )
	@Test.Decl( "Stored uncolllectable object returns same object" )
	@Test.Decl( "Get stress test" )
	@Test.Decl( "Multi thread stress test" )
	public V get( K key ) throws AppException {
		Assert.nonNull( key );
		V value = null;

		synchronized ( this.map ) {
			SoftRef<K, V> sr = this.map.get( key );
			value = (sr == null) ? null : sr.get();
			
			this.flushQueue();

			if ( value == null ) {
				value = this.builder.make( key );
				this.map.put( key, new SoftRef<K, V>( key, value, this.rq ) );
			}
		}

		return Assert.nonNull( value );
	}

	/**
	 * Remove all associations.
	 */
	@Test.Decl( "Then get() retrieves distinct instance" )
	@Test.Decl( "Then get() retrieves equivalent value" )
	@Test.Decl( "Cache empty after" )
	public void flush() {
		synchronized ( this.map ) {
			this.map.clear();
		}
		
	}

	/**
	 * Queue contains references whose referents have been collected.
	 * Remove these keys from the map.
	 * 
	 * Only called when lock on map is held.
	 */
	@SuppressWarnings("unchecked")
	private void flushQueue() {
		SoftRef<K, V> sr = null;
		while ( (sr = (SoftRef<K, V>) this.rq.poll()) != null ) {
			this.map.remove( sr.key );
			this.collected = true;
		}
	}
	
	/**
	 * Current number of cached values.
	 * 
	 * @return
	 */
	@Test.Decl( "Created empty" )
	@Test.Decl( "Not empty after get" )
	public int size() {
		return this.map.size();
	}

	@Override
	@Test.Decl( "Result is not null" )
	@Test.Decl( "Result is not empty" )
	public String toString() {
		return "Cache(" + this.size() + " keys)";
	}
	
	
}
