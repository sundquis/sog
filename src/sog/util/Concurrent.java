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
import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Parser;
import sog.core.Procedure;
import sog.core.Property;
import sog.core.Test;

/**
 * Provides services for executing functions concurrently by a pool of Wroker threads.
 */
@Test.Subject( "test." )
public class Concurrent implements App.OnShutdown {

	
	/*
	 * When true, framework checks for potential deadlock.
	 * 
	 * If the thread requesting a service is one of the Worker threads the call could result
	 * in deadlock. This can happen if the implementation of a concurrent function or procedure
	 * uses the same Concurrent instance to execute code.
	 */
	private static boolean safeMode = Property.get( "safeMode", true, Parser.BOOLEAN );
	
	/**
	 * Safe mode is on by default. This allows the testing framework to temporarily suppress during testing.
	 * Deprecation warning serves as reminder to remove.
	 */
	@Deprecated
	@Test.Skip( "Deprecated" )
	public static void safeModeOff() {
		Concurrent.safeMode = false;
	}


	
	private final String label;
	
	private final Queue<Procedure> procedures;
	
	private final List<Worker> workers;

	@Test.Decl( "Throws AssertionError for null or empty label" )
	@Test.Decl( "Throws AssertionError for non-positive thread count" )
	@Test.Decl( "Terminates workers on shutdown" )
	public Concurrent( String label, int threadCount ) {
		Assert.positive( threadCount );
		
		this.label = Assert.nonEmpty( label );
		this.procedures = new MultiQueue<>( new FifoQueue<>() );
		this.workers = Stream.generate( Worker::new ).limit( threadCount ).map( Worker::init ).collect( Collectors.toList() );

		App.get().terminateOnShutdown( this );
	}
	
	private void addProcedure( Procedure p ) {
		if ( !this.procedures.isOpen() ) {
			throw new AppRuntime( "This Concurrent instance has been terminated and is not accpeting rquests" );
		}
		
		if ( Concurrent.safeMode ) {
			Thread caller = this.workers.stream()
				.filter( Thread.currentThread()::equals ).findFirst().orElse( null );
			if ( caller != null ) {
				System.err.println( "WARNING: Potential deadlock identified. In Concurrent, calling thread is a Worker thread: " + caller );
				App.get().getLocationMatching( "^sog.*|^test.*" ).map( s -> "\t" + s ).forEach( System.err::println );
			}
		}
		
		this.procedures.put( p );
	}


	/**
	 * Closes the request queue and waits for Worker threads to complete their evaluations.
	 */
	@Override
	@Test.Decl( "No effect if Concurrent already terminated" )
	@Test.Decl( "Throws AppRuntime for requests after terminated" )
	@Test.Decl( "Worker threads are stopped after terminate()" )
	public void terminate() {
		if ( this.procedures == null || this.procedures.isClosed() ) {
			return;
		}
		this.procedures.close();
		this.workers.forEach( Worker::quietJoin );
	}
	
	
	
	
	/**
	 * Returns a blocking Supplier that can be used to get the result.
	 * 
	 * This enqueues a request to evaluate the given function with the given argument.
	 * The call returns immediately with a Supplier that can be used the retrieve the result.
	 * The Supplier.get() call blocks until a Worker thread completes the evaluation
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param function		The function to evaluate
	 * @param t				The argument
	 * @return				Blocking Supplier with the retrievable result
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	public <T, R> Supplier<R> applyGetLater( Function<T, R> function, T t ) {
		Assert.nonNull( function );
		return new FunctionProcedure<T, R>( function, t ).add();
	}
	
	/**
	 * Evaluate the given function with the given argument using a Worker thread.
	 * This call blocks until a Worker thread completes the evaluation.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param function		The function to evaluate
	 * @param t				The argument
	 * @return				The result of the function application
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	public <T, R> R apply( Function<T, R> function, T t ) {
		Assert.nonNull( function );
		return this.applyGetLater( function, t ).get();
	}

	/**
	 * Returns a function that will be evaluated by a Worker thread by providing a blocking supplier.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param function		The function to evaluate
	 * @return				A function that evaluates concurrently
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	public <T, R> Function<T, Supplier<R>> wrapGetLater( Function<T, R> function ) {
		Assert.nonNull( function );
		return (t) -> { return this.applyGetLater( function, t ); };
	}

	/**
	 * Returns a blocking function that is evaluated by a Worker thread
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param function		The function to evaluate
	 * @return				A function that evaluates concurrently
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	public <T, R> Function<T, R> wrap( Function<T, R> function ) {
		Assert.nonNull( function );
		return (t) -> { return this.apply( function, t ); };
	}
	
	/**
	 * This blocks until all values in the input stream have been processed be a Worker thread.
	 * The evaluations may occur in any order, but the results in the output stream are presented 
	 * in the same order as the inputs.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param function		The function to evaluate
	 * @param stream		The sequence of inputs for function applications
	 * @return				The stream of results
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Throws AssertionError for null stream" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Call blocks until all results are ready" )
	@Test.Decl( "Results are presented in the order of the original steram of inputs" )
	@Test.Decl( "Results are consistent with the given function and arguments" )
	public <T, R> Stream<R> map( Function<T, R> function, Stream<T> stream ) {
		Assert.nonNull( function );
		Assert.nonNull( stream );
		return stream
			.map( this.wrapGetLater( function ) )
			.collect( Collectors.toList() ).stream()	// Forces all concurrent procedures to be enqueued
			.map( Supplier::get );						// Block until all procedures are complete
	}

	
	
	/**
	 * Returns a blocking Supplier that can be used to get the result.
	 * If the evaluation of the function raises an exception the Supplier.get() call will throw it.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param <E>			The type of the exception thrown by the function
	 * @param function		The function to evaluate
	 * @param t				The argument for the function application
	 * @return				A blocking supplier with the result or exception
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	@Test.Decl( "If the function application throws a checked exception it is thrown by the Supplier" )
	@Test.Decl( "If the function application throws an unchecked excpetion it is thrown by the Supplier" )
	public <T, R, E extends Exception> SupplierWithException<R, E> applyGetLater( FunctionWithException<T, R, E> function, T t ) {
		Assert.nonNull( function );
		return new FunctionWithExceptionProcedure<T, R, E>( function, t ).add();
	}
	
	/**
	 * This call blocks until a Worker thread completes the evaluation.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param <E>			The type of the exception thrown by the function
	 * @param function		The function to evaluate
	 * @param t				The argument for the function application
	 * @return				The result of the function application
	 * @throws E			If the function application raises an exception
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	@Test.Decl( "If the function application throws a checked exception it is thrown" )
	@Test.Decl( "If the function application throws an unchecked excpetion it is thrown" )
	public <T, R, E extends Exception> R apply( FunctionWithException<T, R, E> function, T t ) throws E {
		Assert.nonNull( function );
		return this.applyGetLater( function, t ).get();
	}

	/**
	 * Returns a function that will be evaluated by a Worker thread by providing a blocking supplier.
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param <E>			The type of the exception thrown by the function
	 * @param function		The function to evaluate
	 * @return				A function that evaluates concurrently
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	@Test.Decl( "If the function application throws a checked exception it is thrown by the Supplier" )
	@Test.Decl( "If the function application throws an unchecked excpetion it is thrown by the Supplier" )
	public <T, R, E extends Exception> Function<T, SupplierWithException<R, E>> wrapGetLater( FunctionWithException<T, R, E> function ) {
		Assert.nonNull( function );
		return (t) -> { return this.applyGetLater( function, t ); };
	}

	/**
	 * Returns a blocking function that is evaluated by a Worker thread
	 * 
	 * @param <T>			The argument type of the function
	 * @param <R>			The return type of the function
	 * @param <E>			The type of the exception thrown by the function
	 * @param function		The function to evaluate
	 * @return				A function that evaluates concurrently
	 */
	@Test.Decl( "Throws AssertionError for null function" )
	@Test.Decl( "Function is evaluated by a Worker thread" )
	@Test.Decl( "Result is consistent with given function and argument" )
	@Test.Decl( "If the function application throws a checked exception it is thrown" )
	@Test.Decl( "If the function application throws an unchecked excpetion it is thrown" )
	public <T, R, E extends Exception> FunctionWithException<T, R, E> wrap( FunctionWithException<T, R, E> function ) {
		Assert.nonNull( function );
		return (t) -> { return this.apply( function, t ); };
	}

	
	
	@Override 
	@Test.Decl( "Includes label" )
	@Test.Decl( "Reports when procedures are pending" )
	@Test.Decl( "Reports number of Worker threads" )
	public String toString() {
		return "Concurrent(" + this.label 
			+ ", " + (this.procedures.isEmpty() ? "no procedures" : "has procedures")
			+ ", " + this.workers.size() + " workers)";
	}


	
	private class Worker extends Thread {
		
		@Override
		@Test.Decl( "Worker evaluates procedures" )
		public  void run() {
			Procedure proc;
			while ( (proc = Concurrent.this.procedures.get()) != null ) {
				proc.exec();
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
	
	
	@Test.Skip( "Abstract base class" )
	private abstract class BaseProcedure<S extends BaseProcedure<S>> implements Procedure {
		
		private boolean completed = false;
		
		protected abstract S getThis();
		
		protected abstract void execImpl();

		protected S add() {
			Concurrent.this.addProcedure( this );
			return this.getThis();
		}
	
		/* Called by a Worker thread */
		@Override
		public void exec() {
			this.execImpl();
			this.complete();
		}
		
		/* Called by a Worker thread */
		private synchronized void complete() {
			this.completed = true;
			this.notifyAll();
		}
		
		/*
		 * Concrete subclasses wait until this is true before presenting results. For example
		 * 
		 * 		protected synchronized Result get() {
		 * 			while ( true ) {
		 * 				if ( this.isCompleted() ) {
		 * 					return this.result;
		 * 				} else {
		 * 					try { this.wait(); } catch ( InterruptedException ex ) {}
		 * 				}
		 * 			}
		 * 		}
		 */
		protected boolean isCompleted() {
			return this.completed;
		}

	}
	
	/*
	 * Holds the function and argument to be evaluated by a Worker thread.
	 * The Supplier.get() method blocks until the result has been calculated.
	 */
	@Test.Skip( "Covered by cases in Concurrent" )
	private class FunctionProcedure<T, R> extends BaseProcedure<FunctionProcedure<T, R>> implements Supplier<R> {
		
		private final Function<T, R> function;
		
		private final T arg;
		
		private R result;
		
		private FunctionProcedure( Function<T, R> function, T arg ) {
			this.function = function;
			this.arg = arg;
			this.result = null;
		}
		
		@Override
		protected FunctionProcedure<T, R> getThis() {
			return this;
		}
		
		@Override
		protected void execImpl() {
			this.result = this.function.apply( this.arg );
		}

		@Override
		@Test.Decl( "Blocks until evaluation is complete" )
		@Test.Decl( "Result agrees with value of the function" )
		public synchronized R get() {
			while  ( true ) {
				if ( this.isCompleted() ) { 
					return this.result; 
				}
				
				try {
					this.wait();
				} catch ( InterruptedException ex ) {}
			}
		}

	}

	@Test.Skip( "Covered by cases in Concurrent" )
	private class FunctionWithExceptionProcedure<T, R, E extends Exception> 
		extends BaseProcedure<FunctionWithExceptionProcedure<T, R, E>> 
		implements SupplierWithException<R, E> {
		
		private final FunctionWithException<T, R, E> function;
		
		private final T arg;
		
		private R result;
		
		private E checked;
		
		private RuntimeException runtime;
		
		private Error error;		
		private FunctionWithExceptionProcedure( FunctionWithException<T, R, E> function, T arg ) {
			this.function = function;
			this.arg = arg;
			this.result = null;
			this.checked = null;
			this.runtime = null;
			this.error = null;
		}
		
		@Override
		protected FunctionWithExceptionProcedure<T, R, E> getThis() {
			return this;
		}
		
		@SuppressWarnings( "unchecked" )
		@Override
		protected void execImpl() {
			try {
				this.result = this.function.apply( this.arg );
			} catch ( RuntimeException ex ) {
				this.runtime = ex;
			} catch( Error ex ) {
				this.error = ex;
			} catch ( Exception ex ) {
				this.checked = (E) ex;
			}
		}

		@Override
		@Test.Decl( "Blocks until evaluation is complete" )
		@Test.Decl( "Result is agrees with value of the function" )
		public synchronized R get() throws E {
			while  ( true ) {
				if ( this.isCompleted() ) { 
					if ( this.checked != null ) {
						throw this.checked;
					}
					if ( this.runtime != null ) {
						throw this.runtime;
					}
					if ( this.error != null ) {
						throw this.error;
					}
					return this.result; 
				}
				
				try {
					this.wait();
				} catch ( InterruptedException ex ) {}
			}
		}

	}

	
}

