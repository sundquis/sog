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
package test.sog.util;

import sog.core.Test;
import sog.util.FifoQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class FifoQueueTest extends Test.Container {

	public FifoQueueTest() {
		super( FifoQueue.class );
	}
	

	private Queue<String> getQueue() {
		return new FifoQueue<>();
	}

	
	// TEST CASES

	
    @Test.Impl( 
    	member = "constructor: FifoQueue()", 
    	description = "FifoQueues are created empty" 
    )
    public void tm_03F696CBF( Test.Case tc ) {
    	tc.assertTrue( this.getQueue().isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on closed empty returns null" 
    )
    public void tm_0BDDCDE4E( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.close();
    	tc.assertTrue( q.isClosed() );
    	tc.assertTrue( q.isEmpty() );
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on closed non empty returns non null" 
    )
    public void tm_094801CF4( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	q.close();
    	tc.assertTrue( q.isClosed() );
    	tc.assertFalse( q.isEmpty() );
    	tc.assertNonNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on open empty returns null" 
    )
    public void tm_055AB9A30( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	tc.assertTrue( q.isOpen() );
    	tc.assertTrue( q.isEmpty() );
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on open non empty returns non null" 
    )
    public void tm_05FCDD6D6( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	tc.assertTrue( q.isOpen() );
    	q.put( "A" );
    	tc.assertFalse( q.isEmpty() );
    	tc.assertNonNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on terminated empty returns null" 
    )
    public void tm_07123C5F7( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.terminate();
    	tc.assertTrue( q.isTerminated() );
    	tc.assertTrue( q.isEmpty() );
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Get on terminated non empty returns null" 
    )
    public void tm_0534172EA( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	q.terminate();
    	tc.assertTrue( q.isTerminated() );
    	tc.assertFalse( q.isEmpty() );
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.isEmpty()", 
    	description = "Put on empty is not empty" 
    )
    public void tm_059CBA43B( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	tc.assertTrue( q.isEmpty() );
    	q.put( "A" );
    	tc.assertFalse( q.isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.isEmpty()", 
    	description = "Put on non empty is not empty" 
    )
    public void tm_0CC43422E( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	tc.assertFalse( q.isEmpty() );
    	q.put( "B" );
    	tc.assertFalse( q.isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.isEmpty()", 
    	description = "Put then get on empty is empty" 
    )
    public void tm_0C540A9B7( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	tc.assertTrue( q.isEmpty() );
    	q.put( "A" );
    	q.get();
    	tc.assertTrue( q.isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.isEmpty()", 
    	description = "Put then put then get is not empty" 
    )
    public void tm_04BE5A8BC( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	q.put( "B" );
    	q.get();
    	tc.assertFalse( q.isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.putImpl(Object)", 
    	description = "Put on closed is ignored" 
    )
    public void tm_0F3EE777B( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	q.close();
    	tc.assertTrue( q.isClosed() );
    	tc.assertFalse( q.put( "B" ) );
    	tc.assertEqual( "A", q.get() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.putImpl(Object)", 
    	description = "Put on open is accepted" 
    )
    public void tm_0EFF8B5D2( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	tc.assertTrue( q.isOpen() );
    	tc.assertTrue( q.put( "A" ) );
    	tc.assertEqual( "A", q.get() );
    }
    
    @Test.Impl( 
    	member = "method: boolean FifoQueue.putImpl(Object)", 
    	description = "Put on terminated is ignored" 
    )
    public void tm_0BED87424( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.terminate();
    	tc.assertTrue( q.isTerminated() );
    	tc.assertFalse( q.put( "A" ) );
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object FifoQueue.getImpl()", 
    	description = "Elements retrieved in FIFO order" 
    )
    public void tm_0FC5F958E( Test.Case tc ) {
    	Queue<String> q = this.getQueue();
    	q.put( "A" );
    	q.put( "B" );
    	q.put( "C" );
    	tc.assertEqual( "A", q.get() );
    	q.put( "D" );
    	tc.assertEqual( "B", q.get() );
    	tc.assertEqual( "C", q.get() );
    	tc.assertEqual( "D", q.get() );
    }


	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( FifoQueue.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( FifoQueue.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
	
	
}
