/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import sog.core.Test;

/**
 * If Stream had been parameterized over its implementing type, as BaseStream is, then
 * XStream could extend Stream and we could use a wrapper. My current understanding
 * is that Stream can't be extended this way...
 * 
 * Intended use pattern:
 * 		Create and configure a Stream
 * 		Construct XStream adapter
 * 		Mutate (process) XStream: filter, map, flatMap, peek, and new method forSome( ... )
 * 		toStream()
 * 		Terminate using Stream interface
 * 
 * Since XStream is frequently used with forEach it is provided as a terminal operation
 * 
 * Can extend by adding other Stream methods as necessary
 * 
 * @author sundquis
 *
 */
@Test.Skip("FIXME")
public interface XStream<T> {
	
	/*
	 * Returns a stream where any of the elements of this stream that match
	 * the given predicate have been consumed by the given action.
	 */
    public XStream<T> forSome( Predicate<? super T> condition, Consumer<? super T> action );
    
	public interface Case<T> {
		
		public Predicate<? super T> when();
		
		public Consumer<? super T> process();
		
	}
	
	/*
	 * Returns a stream where any of the elements of this stream that match
	 * the given predicate have been consumed by the given action.
	 */
    public XStream<T> forSome( Case<T> c );
    
    // Copied from java.util.Stream
    
	public XStream<T> filter( Predicate<? super T> predicate );

	public <R> XStream<R> map( Function<? super T, ? extends R> mapper );
	
	public <R> XStream<R> flatMap( Function<? super T, ? extends Stream<? extends R>> mapper );

	public XStream<T> peek( Consumer<? super T> action );
	
	public void forEach( Consumer<? super T> action );

	public Stream<T> toStream();
	
	
	
	public static <T> XStream<T> adapt( Stream<T> stream ) {
		
		return new XStream<T>() {

			@Override
			public XStream<T> forSome( Predicate<? super T> condition, Consumer<? super T> action ) {
				Predicate<T> p1 = t -> { action.accept(t); return false; };
				Predicate<T> p2 = t -> condition.test(t) ? p1.test(t) : true;
				return XStream.adapt( stream.filter( p2 ) );
			}

			@Override
			public XStream<T> forSome( Case<T> c ) {
				return forSome( c.when(), c.process() );
			}

			@Override
			public XStream<T> filter( Predicate<? super T> predicate) {
				return XStream.adapt( stream.filter( predicate ) );
			}

			@Override
			public <R> XStream<R> map( Function<? super T, ? extends R> mapper ) {
				return XStream.adapt( stream.map( mapper ) );
			}

			@Override
			public <R> XStream<R> flatMap( Function<? super T, ? extends Stream<? extends R>> mapper ) {
				return XStream.adapt( stream.flatMap( mapper ) );
			}

			@Override
			public XStream<T> peek( Consumer<? super T> action ) {
				return XStream.adapt( stream.peek( action ) );
			}

			@Override
			public void forEach( Consumer<? super T> action ) {
				stream.forEach( action );
			}
			
			@Override
			public Stream<T> toStream() {
				return stream;
			}

		};
		
	}
	
}
