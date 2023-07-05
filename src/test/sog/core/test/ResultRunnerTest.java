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

package test.sog.core.test;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import sog.core.Test;
import sog.core.test.Result;
import sog.core.test.ResultRunner;
import sog.util.FifoQueue;
import sog.util.IndentWriter;
import sog.util.PriorityQueue;
import sog.util.Queue;

/**
 * 
 */
@Test.Skip( "Container" )
public class ResultRunnerTest extends Test.Container {
	
	public ResultRunnerTest() {
		super( ResultRunner.class );
	}
	
	public class ConcurrentResult extends Result implements Consumer<ConcurrentResult>, Comparable<ConcurrentResult> {
		
		public ConcurrentResult() { super( "For Testing" ); }

		private String rrt;
		
		@Override protected void run() {
			this.rrt = Thread.currentThread().toString();
		}
		
		public String getRRThread() { return this.rrt; }

		@Override public long getElapsedTime() { return 0; }
		@Override public int getPassCount() { return 0; }
		@Override public int getFailCount() { return 0; }
		@Override public void print( IndentWriter out ) {}
		@Override public boolean concurrentSubjects() { return true; }
		@Override public boolean concurrentSets() { return true; }

		private Result after;
		@Override public void accept( ConcurrentResult r ) { this.after = r; }
		public Result getResultAfter() { return this.after; }

		@Override public int compareTo( ConcurrentResult o ) { return this.toString().compareTo( o.toString() ); }
	}
	
	public ResultRunner<ConcurrentResult> getResultRunner() {
    	ConcurrentResult r = new ConcurrentResult();
    	SortedSet<ConcurrentResult> set = new TreeSet<ConcurrentResult>( Set.of(r) );
    	PriorityQueue<ConcurrentResult> q = new PriorityQueue<ConcurrentResult>( set );
		return new ResultRunner<ConcurrentResult>( q, r );
	}
	
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: ResultRunner(Queue, Consumer)", 
    	description = "Throws AssertionError for null result consumer" 
    )
    public void tm_00290A843( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Queue<Result> queue = new FifoQueue<Result>();
    	Consumer<Result> con = null;
    	new ResultRunner<Result>( queue, con );
    }
        
    @Test.Impl( 
    	member = "constructor: ResultRunner(Queue, Consumer)", 
    	description = "Throws AssertionError for null result queue" 
    )
    public void tm_04320293A( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Queue<Result> queue = null;
    	Consumer<Result> con = r -> {};
    	new ResultRunner<Result>( queue, con );
    }
        
    @Test.Impl( 
    	member = "method: ResultRunner ResultRunner.init()", 
    	description = "Result runner gets started" 
    )
    public void tm_076EF9BF3( Test.Case tc ) {
    	ConcurrentResult r = new ConcurrentResult();
    	SortedSet<Result> set = new TreeSet<Result>( Set.of(r) );
    	this.evalSubjectMethod( null, "run", null, set, r, Boolean.TRUE );
    	tc.assertNotEqual( Thread.currentThread().toString(), r.getRRThread() );
    }
        
    @Test.Impl( 
    	member = "method: ResultRunner ResultRunner.init()", 
    	description = "This Result runner return to allow chaining" 
    )
    public void tm_0C37E8D2A( Test.Case tc ) {
    	ResultRunner<ConcurrentResult> rr = this.getResultRunner();
    	tc.assertEqual( rr, rr.init() );
    }
        
    @Test.Impl( 
    	member = "method: void ResultRunner.quietJoin()", 
    	description = "Prints message if interrupted" 
    )
    public void tm_0E1B89967( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run()", 
        	description = "All result queue elements consumed" 
        )
        public void tm_07856FC92( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run(SortedSet, Consumer, boolean)", 
        	description = "All threads started before joining" 
        )
        public void tm_050C185A5( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run(SortedSet, Consumer, boolean)", 
        	description = "Configured number of threads used when concurent is true" 
        )
        public void tm_039F3999C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run(SortedSet, Consumer, boolean)", 
        	description = "Single thread used when concurrent is false" 
        )
        public void tm_087FC0732( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run(SortedSet, Consumer, boolean)", 
        	description = "Throws AsserionError for null set of tests" 
        )
        public void tm_0BA15F08C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: void ResultRunner.run(SortedSet, Consumer, boolean)", 
        	description = "Throws AssertionError for null consumer" 
        )
        public void tm_0AA64D14E( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
	
	

	public static void main( String[] args ) {
		Test.eval( ResultRunner.class ).showDetails( true ).print();
	}
	
}
