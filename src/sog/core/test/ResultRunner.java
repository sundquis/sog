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

package sog.core.test;

import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;
import sog.util.FifoQueue;
import sog.util.MultiQueue;
import sog.util.Queue;

/**
 * 
 */
@Test.Subject( "test." )
public class ResultRunner<R extends Result> extends Thread {
	
	static <R extends Result> void run( SortedSet<R> tests, Consumer<R> consumer, int threads ) {
		final Queue<R> results = new MultiQueue<R>( new FifoQueue<R>() );
		tests.forEach( results::put );
		results.close();
		
		Stream.generate( () -> new ResultRunner<R>( results, consumer ) )
			.limit( threads )
			.map( ResultRunner::init )
			.collect( Collectors.toList() )
			.forEach( ResultRunner::quietJoin );
	}

	private final Queue<R> results;
	
	private final Consumer<R> consumer;

	@Test.Decl( "Throws AssertionError for null result queue" )
	@Test.Decl( "Throws AssertionError for null result consumer" )
	public ResultRunner( Queue<R> results, Consumer<R> consumer ) {
		this.results = Assert.nonNull( results );
		this.consumer = Assert.nonNull( consumer );
	}

	@Test.Decl( "This Result runner return to allow chaining" )
	@Test.Decl( "Result runner gets started" )
	public ResultRunner<R> init() {
		this.start();
		return this;
	}
	
	@Override
	@Test.Decl( "All result queue elements consumed" )
	public void run() {
		R result = null;
		while ( (result = this.results.get()) != null ) {
			result.run();
			this.consumer.accept( result );
		}
	}

	@Test.Decl( "Prints message if interrupted" )
	public void quietJoin() {
		try {
			this.join();
		} catch ( InterruptedException ex ) {
			App.get().getLocation( ex ).map( s -> ">>> " + s ).forEach( System.out::println );
		}
	}

}
