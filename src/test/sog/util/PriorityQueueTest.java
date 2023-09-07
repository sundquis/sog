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


import sog.core.App;
import sog.core.Test;
import sog.util.PriorityQueue;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class PriorityQueueTest extends Test.Container {

	public PriorityQueueTest() {
		super( PriorityQueue.class );
	}
	
//	private Queue<String> queue;
//	
//
//	@Override
//	public Procedure beforeEach() {
//		return new Procedure() {
//			public void exec() {
//				queue = new PriorityQueue<String>();
//			}
//		};
//	}
//	
//	@Override
//	public Procedure afterEach() {
//		return new Procedure() {
//			public void exec() {
//				queue = null;
//			}
//		};
//	}
//	
//
//	@Test.Impl( member = "PriorityQueue", description = "Elements retrieved in comparable order" )
//	public void PriorityQueue_ElementsRetrievedInComparableOrder( Test.Case tc ) {
//		this.queue.put( "C" );
//		this.queue.put( "B2" );
//		this.queue.put( "A" );
//		this.queue.put( "B1" );
//		tc.assertEqual( "A",  this.queue.get() );
//		tc.assertEqual( "B1",  this.queue.get() );
//		tc.assertEqual( "B2",  this.queue.get() );
//		tc.assertEqual( "C",  this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on closed empty returns null" )
//	public void getImpl_GetOnClosedEmptyReturnsNull( Test.Case tc ) {
//		this.queue.close();
//		tc.assertTrue( this.queue.isClosed() );
//		tc.assertTrue( this.queue.isEmpty() );
//		tc.assertIsNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on closed non empty returns non null" )
//	public void getImpl_GetOnClosedNonEmptyReturnsNonNull( Test.Case tc ) {
//		this.queue.put( "A" );
//		this.queue.close();
//		tc.assertTrue( this.queue.isClosed() );
//		tc.assertFalse( this.queue.isEmpty() );
//		tc.assertNonNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on open empty returns null" )
//	public void getImpl_GetOnOpenEmptyReturnsNull( Test.Case tc ) {
//		tc.assertTrue( this.queue.isOpen() );
//		tc.assertTrue( this.queue.isEmpty() );
//		tc.assertIsNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on open non empty returns non null" )
//	public void getImpl_GetOnOpenNonEmptyReturnsNonNull( Test.Case tc ) {
//		this.queue.put( "A" );
//		tc.assertTrue( this.queue.isOpen() );
//		tc.assertFalse( this.queue.isEmpty() );
//		tc.assertNonNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on terminated empty returns null" )
//	public void getImpl_GetOnTerminatedEmptyReturnsNull( Test.Case tc ) {
//		this.queue.terminate();
//		tc.assertTrue( this.queue.isTerminated() );
//		tc.assertTrue( this.queue.isEmpty() );
//		tc.assertIsNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected Comparable PriorityQueue.getImpl()", description = "Get on terminated non empty returns null" )
//	public void getImpl_GetOnTerminatedNonEmptyReturnsNull( Test.Case tc ) {
//		this.queue.put( "A" );
//		this.queue.terminate();
//		tc.assertTrue( this.queue.isTerminated() );
//		tc.assertFalse( this.queue.isEmpty() );
//		tc.assertIsNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on closed is ignored" )
//	public void putImpl_PutOnClosedIsIgnored( Test.Case tc ) {
//		this.queue.put( "B" );
//		this.queue.close();
//		tc.assertTrue( this.queue.isClosed() );
//		tc.assertFalse( this.queue.put( "A" ) );
//		tc.assertEqual( "B", this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on open is accepted" )
//	public void putImpl_PutOnOpenIsAccepted( Test.Case tc ) {
//		tc.assertTrue( this.queue.isOpen() );
//		tc.assertTrue( this.queue.put( "B" ) );
//		tc.assertEqual( "B", this.queue.get() );
//	}
//
//	@Test.Impl( member = "protected boolean PriorityQueue.putImpl(Comparable)", description = "Put on terminated is ignored" )
//	public void putImpl_PutOnTerminatedIsIgnored( Test.Case tc ) {
//		this.queue.terminate();
//		tc.assertTrue( this.queue.isTerminated() );
//		tc.assertFalse( this.queue.put( "A" ) );
//		tc.assertIsNull( this.queue.get() );
//	}
//
//	@Test.Impl( member = "public PriorityQueue()", description = "Can be created empty" )
//	public void PriorityQueue_CanBeCreatedEmpty( Test.Case tc ) {
//		tc.assertTrue ( this.queue.isEmpty() );
//	}
//
//	@Test.Impl( member = "public PriorityQueue(SortedSet)", description = "Can be created non empty" )
//	public void PriorityQueue_CanBeCreatedNonEmpty( Test.Case tc ) {
//		TreeSet<String> set = new TreeSet<String>();
//		set.add( "B" );
//		set.add( "A" );
//		queue = new PriorityQueue<String>( set );
//		tc.assertFalse( queue.isEmpty() );
//		tc.assertEqual( "A",  queue.get() );
//	}
//
//	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put on empty is not empty" )
//	public void isEmpty_PutOnEmptyIsNotEmpty( Test.Case tc ) {
//		tc.assertTrue( this.queue.isEmpty() );
//		this.queue.put( "A" );
//		tc.assertFalse( this.queue.isEmpty() );
//	}
//
//	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put on non empty is not empty" )
//	public void isEmpty_PutOnNonEmptyIsNotEmpty( Test.Case tc ) {
//		this.queue.put( "A" );
//		tc.assertFalse( this.queue.isEmpty() );
//		this.queue.put( "B" );
//		tc.assertFalse( this.queue.isEmpty() );
//	}
//
//	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put then get on empty is empty" )
//	public void isEmpty_PutThenGetOnEmptyIsEmpty( Test.Case tc ) {
//		tc.assertTrue( this.queue.isEmpty() );
//		this.queue.put( "A" );
//		this.queue.get();
//		tc.assertTrue( this.queue.isEmpty() );
//	}
//
//	@Test.Impl( member = "public boolean PriorityQueue.isEmpty()", description = "Put then put then get is not empty" )
//	public void isEmpty_PutThenPutThenGetIsNotEmpty( Test.Case tc ) {
//		tc.assertTrue( this.queue.isEmpty() );
//		this.queue.put( "A" );
//		this.queue.put( "B" );
//		this.queue.get();
//		tc.assertFalse( this.queue.isEmpty() );
//	}
	
	
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "constructor: PriorityQueue()", 
    	description = "Can be created empty" 
    )
    public void tm_0DBD6D408( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: PriorityQueue(SortedSet)", 
    	description = "Can be created non empty" 
    )
    public void tm_0F73D643A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on closed empty returns null" 
    )
    public void tm_0A36140EB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on closed non empty returns non null" 
    )
    public void tm_0D67A9291( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on open empty returns null" 
    )
    public void tm_08332000D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on open non empty returns non null" 
    )
    public void tm_0A6330FB3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on terminated empty returns null" 
    )
    public void tm_0624B9214( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Comparable PriorityQueue.getImpl()", 
    	description = "Get on terminated non empty returns null" 
    )
    public void tm_0953BE887( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.isEmpty()", 
    	description = "Put on empty is not empty" 
    )
    public void tm_0187E2B63( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.isEmpty()", 
    	description = "Put on non empty is not empty" 
    )
    public void tm_0528DF556( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.isEmpty()", 
    	description = "Put then get on empty is empty" 
    )
    public void tm_0084C5B8F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.isEmpty()", 
    	description = "Put then put then get is not empty" 
    )
    public void tm_0DB94AE94( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.putImpl(Comparable)", 
    	description = "Put on closed is ignored" 
    )
    public void tm_05DCFE3AE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.putImpl(Comparable)", 
    	description = "Put on open is accepted" 
    )
    public void tm_024EF773F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean PriorityQueue.putImpl(Comparable)", 
    	description = "Put on terminated is ignored" 
    )
    public void tm_044F28ED7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	

	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( PriorityQueue.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( PriorityQueue.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

	
	
}
