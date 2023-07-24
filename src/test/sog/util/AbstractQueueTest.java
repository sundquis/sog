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

import java.util.function.Consumer;
import java.util.function.Supplier;

import sog.core.Test;
import sog.util.AbstractQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class AbstractQueueTest extends Test.Container {

	
	public AbstractQueueTest() {
		super( AbstractQueue.class );
	}

	Queue<String> getEmptyQueue() {
		return new AbstractQueue<>() {

			@Override protected boolean putImpl( String elt ) { return false; }

			@Override protected String getImpl() { return null; }

			@Override public boolean isEmpty() { return true; }
			
		};
	}

	Queue<String> getQueue( Supplier<String> getImpl, Consumer<String> putImpl ) {
		return new AbstractQueue<>() {

			@Override protected boolean putImpl( String elt ) {
				if ( putImpl != null ) { putImpl.accept( elt ); }
				return true;
			}

			@Override protected String getImpl() { 
				return getImpl == null ? null : getImpl.get(); 
			}

			@Override public boolean isEmpty() { return false; }
			
		};
	}
	
	// TESTS
	
    @Test.Impl( 
    	member = "constructor: AbstractQueue()", 
    	description = "Queues are created open" 
    )
    public void tm_065BC3384( Test.Case tc ) {
		tc.assertTrue( this.getEmptyQueue().isOpen() );
    }
    
    @Test.Impl( 
    	member = "method: void AbstractQueue.close()", 
    	description = "Can close if open" 
    )
    public void tm_0E5D12532( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
		tc.assertTrue( q.isOpen() );
		q.close();
		tc.assertTrue( q.isClosed() );
    }
    
    @Test.Impl( 
    	member = "method: void AbstractQueue.close()", 
    	description = "Close on terminated ignored" 
    )
    public void tm_02524CF0B( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
		q.terminate();
		tc.assertTrue( q.isTerminated() );
		q.close();
		tc.assertTrue( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: void AbstractQueue.terminate()", 
    	description = "Can terminate if closed" 
    )
    public void tm_0792F99A6( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
		q.close();
		tc.assertTrue( q.isClosed() );
		q.terminate();
		tc.assertTrue( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: void AbstractQueue.terminate()", 
    	description = "Can terminate if open" 
    )
    public void tm_02086C2C4( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
		tc.assertTrue( q.isOpen() );
		q.terminate();
		tc.assertTrue( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: Object AbstractQueue.get()", 
    	description = "Consistent with getImpl when CLOSED" 
    )
    public void tm_01E709462( Test.Case tc ) {
    	Supplier<String> getImpl = () -> "Value";
    	Queue<String> q = this.getQueue( getImpl, null );
    	q.close();
    	tc.assertEqual( getImpl.get(), q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object AbstractQueue.get()", 
    	description = "Consistent with getImpl when OPEN and non-empty" 
    )
    public void tm_0704E01E4( Test.Case tc ) {
    	Supplier<String> getImpl = () -> "Value";
    	Queue<String> q = this.getQueue( getImpl, null );
    	tc.assertEqual( getImpl.get(), q.get() );
    }
    
    @Test.Impl( 
    	member = "method: Object AbstractQueue.get()", 
    	description = "Returns null when TERMINATED" 
    )
    public void tm_056C5279D( Test.Case tc ) {
    	Supplier<String> getImpl = () -> "Value";
    	Queue<String> q = this.getQueue( getImpl, null );
    	q.terminate();
    	tc.assertIsNull( q.get() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isClosed()", 
    	description = "False after terminate()" 
    )
    public void tm_0349629FE( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.terminate();
    	tc.assertFalse( q.isClosed() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isClosed()", 
    	description = "False when constructed" 
    )
    public void tm_06DC0F086( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	tc.assertFalse( q.isClosed() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isClosed()", 
    	description = "True after close()" 
    )
    public void tm_069BD757C( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.close();
    	tc.assertTrue( q.isClosed() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isOpen()", 
    	description = "False after close()" 
    )
    public void tm_0007C9EB3( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.close();
    	tc.assertFalse( q.isOpen() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isOpen()", 
    	description = "False after terminate()" 
    )
    public void tm_09C174B1C( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.terminate();
    	tc.assertFalse( q.isOpen() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isOpen()", 
    	description = "True when constructed" 
    )
    public void tm_0FC7DCCDD( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	tc.assertTrue( q.isOpen() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isTerminated()", 
    	description = "False after close()" 
    )
    public void tm_0B9BECB0C( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.close();
    	tc.assertFalse( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isTerminated()", 
    	description = "False when constructed" 
    )
    public void tm_06ECEFCAF( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	tc.assertFalse( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.isTerminated()", 
    	description = "True after terminate()" 
    )
    public void tm_0391BA18E( Test.Case tc ) {
    	Queue<String> q = this.getEmptyQueue();
    	q.terminate();
    	tc.assertTrue( q.isTerminated() );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.put(Object)", 
    	description = "Returns false when CLOSED" 
    )
    public void tm_08C5AF99D( Test.Case tc ) {
    	Consumer<String> putImpl = (String s) -> {};
    	Queue<String> q = this.getQueue( null, putImpl );
    	q.close();
    	tc.assertFalse( q.put( "Hi" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.put(Object)", 
    	description = "Returns false when TERMINATED" 
    )
    public void tm_045B28194( Test.Case tc ) {
    	Consumer<String> putImpl = (String s) -> {};
    	Queue<String> q = this.getQueue( null, putImpl );
    	q.terminate();
    	tc.assertFalse( q.put( "Hi" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean AbstractQueue.put(Object)", 
    	description = "Returns true when OPEN" 
    )
    public void tm_02FFD76AC( Test.Case tc ) {
    	Consumer<String> putImpl = (String s) -> {};
    	Queue<String> q = this.getQueue( null, putImpl );
    	tc.assertTrue( q.put( "Hi" ) );
    }



    
	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( AbstractQueue.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( AbstractQueue.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
	
	
}
