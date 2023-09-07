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
@Test.Subject( "test." )
public interface XStream<T> {
	
	/*
	 */
	/**
	 * Returns an XStream where any of the elements of this XStream that match
	 * the given predicate have been consumed by the given action.
	 * 
	 * @param condition		Predicate specifying the elements to consume.
	 * @param action		The action used to consume the specified elements.
	 * @return				The resulting XStream containing any remaining elements.
	 */
    public XStream<T> forSome( Predicate<? super T> condition, Consumer<? super T> action );

    
    /**
     * Abstraction combining the condition and action associated with processing
     * some of the elements of an XStream.
     */
	public interface Case<T> {
		
		/**
		 * @return		Predicate defining the elements belonging to this Case.
		 */
		public Predicate<? super T> condition();

		/**
		 * @return		The action used to consume elements belonging to this Case.
		 */
		public Consumer<? super T> action();
		
	}
	
	/**
	 * Alternate form using the Case abstraction.
	 * @param c
	 * @return
	 */
	@Test.Decl( "Elements matching the condition have been consumed" )
	@Test.Decl( "Given action is applied to consumed elements" )
    default public XStream<T> forSome( Case<T> c ) {
    	return this.forSome( c.condition(), c.action() );
    }

	
	
	/* The interface of XStream is intended to mirror the Stream interface: */
    
	public XStream<T> filter( Predicate<? super T> predicate );

	public <R> XStream<R> map( Function<? super T, ? extends R> mapper );
	
	public <R> XStream<R> flatMap( Function<? super T, ? extends Stream<? extends R>> mapper );

	public XStream<T> peek( Consumer<? super T> action );
	
	public void forEach( Consumer<? super T> action );

	public Stream<T> toStream();
	
	

	/**
	 * Adapt the given Stream by wrapping it in an XStream instance.
	 * 
	 * @param <T>			Type parameter of the given Stream and resulting XStream elements.
	 * @param stream		Stream to wrap.
	 * @return				XStream that defers operations to the wrapped Stream.
	 */
	@Test.Decl( "Has elements if given Stream has elements" )
	@Test.Decl( "Is terminated if gievn Stream is terminated" )
	@Test.Decl( "Adapted forSome consumes matching elements" )
	@Test.Decl( "Adapted forSome applies action to matching elements" )
	@Test.Decl( "Adapted filter operation is applied to underlying Stream" )
	@Test.Decl( "Adapted map operation is applied to underlying Stream" )
	@Test.Decl( "Adapted flatMap operation is applied to underlying Stream" )
	@Test.Decl( "Adapted peek operation is applied to underlying Stream" )
	@Test.Decl( "Adapted forEach operation is applied to underlying Stream" )
	@Test.Decl( "Additional operations can be applied to original Stream using toStream" )
	public static <T> XStream<T> adapt( Stream<T> stream ) {
		
		return new XStream<T>() {

			@Override
			public XStream<T> forSome( Predicate<? super T> condition, Consumer<? super T> action ) {
				Predicate<T> p1 = t -> { action.accept(t); return false; };
				Predicate<T> p2 = t -> condition.test(t) ? p1.test(t) : true;
				return XStream.adapt( stream.filter( p2 ) );
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
