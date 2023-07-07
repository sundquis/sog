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

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Evaluator implements App.OnShutdown {
	

	private final String label;
	
	private final Queue<Request<?, ?>> requests;
	
	private final List<Worker> workers;

	@Test.Decl( "Throws AssertionError for null or empty label" )
	@Test.Decl( "Throws AssertionError for non-positive thread count" )
	@Test.Decl( "Evaluation requests are accepted" )
	@Test.Decl( "Starts correct number of agent threads" )
	@Test.Decl( "Terminates workers on shutdown" )
	public Evaluator( String label, int threadCount ) {
		this.label = Assert.nonEmpty( label );
		this.requests = new MultiQueue<>( new FifoQueue<>() );
		this.workers = Stream.generate( Worker::new ).limit( threadCount ).map( Worker::init ).collect( Collectors.toList() );

		App.get().terminateOnShutdown( this );
	}
	

	public <T, R> Function<T, Supplier<R>> wrap( Function<T, R> function ) {
		return (t) -> {
			return new Request<T, R>( function, t ).add();
		};
	}
	
	public <T, R> Function<T, Supplier<R>> wrap( Function<T, R> function, boolean concurrent ) {
		return concurrent ? this.wrap( function ) : (t) -> { return () -> function.apply( t ); };
	}
	
	public <T, R> Stream<R> apply( Function<T, R> function, Stream<T> stream, boolean concurrent ) {
		return stream
			.map( this.wrap( function, concurrent ) )
			.collect( Collectors.toList() ).stream()	// Forces all concurrent requests to be enqueued
			.map( Supplier::get );
	}
	
	public <T, R> Stream<R> apply( Function<T, R> function, Stream<T> stream ) {
		return this.apply( function, stream, true );
	}

	
	
	@Override
	@Test.Decl( "No effect if Evaluator already terminated" )
	@Test.Decl( "Requests are not acceppted after terminate()" )
	@Test.Decl( "Worker threads are stopped after terminate()" )
	public void terminate() {
		if ( this.requests == null || this.requests.isClosed() ) {
			return;
		}
		this.requests.close();
		this.workers.forEach( Worker::quietJoin );
	}
	
	
	@Override 
	@Test.Decl( "Includes constructed label" )
	@Test.Decl( "Reports when requests are pending" )
	@Test.Decl( "Reports number of Worker threads" )
	public String toString() {
		return "Evaluator(" + this.label 
			+ ", " + (this.requests.isEmpty() ? "no requests" : "has requests")
			+ ", " + this.workers.size() + " workers)";
	}


	
	private class Worker extends Thread {
		
		@Override
		@Test.Decl( "Worker accepts requests" )
		@Test.Decl( "Worker evaluates requests" )
		public  void run() {
			Request<?, ?> request;
			while ( (request = Evaluator.this.requests.get()) != null ) {
				request.eval();
			}
		}
		
		private Worker init() {
			this.setDaemon( true );
			this.start();
			return this;
		}
		
		private void quietJoin() {
			try {
				this.join();
			} catch ( InterruptedException ex ) {
				App.get().getLocation( ex ).map( s -> ">>> " + s ).forEach( System.out::println );
			}
		}
	}
	
	
	
	
	/*
	 * Holds the function and argument to be evaluated by a Worker thread.
	 * The Supplier.get() method blocks until the result has been calculated.
	 */
	private class Request<T, R> implements Supplier<R> {
		
		private final Function<T, R> function;
		
		private final T arg;
		
		private R result;
		
		private boolean completed;
		
		private Request( Function<T, R> function, T arg ) {
			this.function = function;
			this.arg = arg;
			this.result = null;
			this.completed = false;
		}
		
		private Request<T, R> add() {
			Evaluator.this.requests.put( this );
			return this;
		}
		
		private void eval() {
			this.result = this.function.apply( this.arg );
			this.complete();
		}
		
		private synchronized void complete() {
			this.completed = true;
			this.notifyAll();
		}

		@Override
		@Test.Decl( "Blocks until evaluation is complete" )
		@Test.Decl( "Result is agrees with value of the function" )
		public synchronized R get() {
			while  ( true ) {
				if ( this.completed ) { 
					return this.result; 
				}
				
				try {
					this.wait();
				} catch ( InterruptedException ex ) {}
			}
		}

	}

	

	@FunctionalInterface
	public static interface FunctionE<T, R, E extends Throwable> {
		public R apply( T t ) throws E;
	}
	
	@FunctionalInterface
	public static interface SupplierE<R, E extends Throwable> {
		public R get() throws E;
	}


}
