/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.util.ArrayList;

import sog.core.Fatal;
import sog.core.Procedure;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.FifoQueue;
import sog.util.MultiQueue;
import sog.util.PriorityQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class MultiQueueTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return MultiQueue.class;
	}

	// Test implementations
	
	
	private Queue<String> fm;
	private Queue<String> pm;
	private Queue<String> fifo;
	private Queue<String> priority;

	@Override
	public Procedure beforeEach() {
		return () -> {
			this.fifo = new FifoQueue<String>();
			this.priority = new PriorityQueue<String>();
			this.fm = new MultiQueue<String>( fifo );
			this.pm = new MultiQueue<String>( priority );
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.fifo = null;
			this.priority = null;
			this.fm = null;
			this.pm = null;
		};
	}
	
	private static class Agent extends Thread {
		private Queue<String> queue;
		private String results;
		Agent( Queue<String> queue ) { this.queue = queue; this.results = ""; }
		String getResults() { return this.results; }
		@Override public void run() {
			String r;
			while ( (r = this.queue.get()) != null ) {
				this.results = r;
			}
			this.results = "Z";
		}
	}

	private static class Record {
		int counter = 0;
		void inc() { this.counter++; }
		int get() { return this.counter; }
	}
	
	private static class Agent2 extends Thread {
		private Queue<Record> in;
		private Queue<Record> out;
		private int index;
		private int count = 0;
		Agent2( int index, Queue<Record> in, Queue<Record> out ) {
			this.index = index;
			this.in = in; 
			this.out = out; 
		}
		@Override public void run() {
			Record r;
			while ( (r = this.in.get()) != null ) {
				r.inc();
				count = r.get();
				Thread.yield();
				if ( count != r.get() ) {
					Fatal.error( "Concurrency error" );
				}
				out.put( r );
			}
			out.close();
		}
		Agent2 init() { this.start(); return this; }
		@Override public String toString() {
			return this.index + " [" + count + "]" ; 
		}
	}
	
	

	@TestOrig.Impl( src = "MultiQueue", desc = "If backed by FIFO queue elements retrieved in FIFO order" )
	public void MultiQueue_IfBackedByFifoQueueElementsRetrievedInFifoOrder( TestCase tc ) {
		this.fm.put( "B" );
		this.fm.put( "A" );
		tc.assertEqual( "B",  this.fm.get() );
		tc.assertEqual( "A",  this.fm.get() );
	}

	@TestOrig.Impl( src = "MultiQueue", desc = "If backed by priority queue elements retrieved in priority order" )
	public void MultiQueue_IfBackedByPriorityQueueElementsRetrievedInPriorityOrder( TestCase tc ) {
		this.pm.put( "B" );
		this.pm.put( "A" );
		tc.assertEqual( "A",  this.pm.get() );
		tc.assertEqual( "B",  this.pm.get() );
	}

	@TestOrig.Impl( src = "MultiQueue", desc = "Multi thread stress test" )
	public void MultiQueue_MultiThreadStressTest( TestCase tc ) throws InterruptedException {
		ArrayList<Agent2> threads = new ArrayList<Agent2>();

		Queue<Record> first = new MultiQueue<Record>( new FifoQueue<Record>() );
		Queue<Record> prev = first;
		Queue<Record> next;
		for ( int i = 0; i < 5; i++ ) {
			next = new MultiQueue<Record>( new FifoQueue<Record>() );
			threads.add( new Agent2( i, prev, next ).init() );
			prev = next;
		}
		threads.add( new Agent2( 5, prev, first ).init() );
		
		for ( int i = 0; i < 100; i++ ) {
			first.put( new Record() );
		}
		Thread.sleep( 1000 );
		first.close();
		
		for ( Thread t : threads ) {
			t.join();
		}
		
		tc.pass();
	}

	@TestOrig.Impl( src = "public MultiQueue(Queue)", desc = "is closed if queue is closed" )
	public void MultiQueue_IsClosedIfQueueIsClosed( TestCase tc ) {
		fifo.close();
		priority.close();
		tc.assertTrue( fm.isClosed() );
		tc.assertTrue( pm.isClosed() );
	}

	@TestOrig.Impl( src = "public MultiQueue(Queue)", desc = "is empty if queue is empty" )
	public void MultiQueue_IsEmptyIfQueueIsEmpty( TestCase tc ) {
		tc.assertTrue( fifo.isEmpty() );
		tc.assertTrue( priority.isEmpty() );
		tc.assertTrue( fm.isEmpty() );
		tc.assertTrue( pm.isEmpty() );
	}

	@TestOrig.Impl( src = "public MultiQueue(Queue)", desc = "is non empty if queue is non empty" )
	public void MultiQueue_IsNonEmptyIfQueueIsNonEmpty( TestCase tc ) {
		fifo.put( "A" );
		priority.put( "A" );
		tc.assertFalse( fifo.isEmpty() );
		tc.assertFalse( priority.isEmpty() );
		tc.assertFalse( fm.isEmpty() );
		tc.assertFalse( pm.isEmpty() );
	}

	@TestOrig.Impl( src = "public MultiQueue(Queue)", desc = "is open if queue is open" )
	public void MultiQueue_IsOpenIfQueueIsOpen( TestCase tc ) {
		tc.assertTrue( fifo.isOpen() );
		tc.assertTrue( priority.isOpen() );
		tc.assertTrue( fm.isOpen() );
		tc.assertTrue( pm.isOpen() );
	}

	@TestOrig.Impl( src = "public MultiQueue(Queue)", desc = "is terminated if queue is terminated" )
	public void MultiQueue_IsTerminatedIfQueueIsTerminated( TestCase tc ) {
		this.fifo.terminate();
		this.priority.terminate();
		tc.assertTrue( fifo.isTerminated() );
		tc.assertTrue( priority.isTerminated() );
		tc.assertTrue( fm.isTerminated() );
		tc.assertTrue( pm.isTerminated() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on closed empty returns null" )
	public void get_GetOnClosedEmptyReturnsNull( TestCase tc ) {
		this.fifo.close();
		this.priority.close();
		tc.assertTrue( fifo.isEmpty() );
		tc.assertTrue( priority.isEmpty() );
		tc.assertTrue( fifo.isClosed() );
		tc.assertTrue( priority.isClosed() );
		tc.isNull( fm.get() );
		tc.isNull( pm.get() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on closed non empty returns non null" )
	public void get_GetOnClosedNonEmptyReturnsNonNull( TestCase tc ) {
		this.fifo.put( "A" );
		this.priority.put( "B" );
		this.fifo.close();
		this.priority.close();
		tc.assertFalse( fifo.isEmpty() );
		tc.assertFalse( priority.isEmpty() );
		tc.assertTrue( fifo.isClosed() );
		tc.assertTrue( priority.isClosed() );
		tc.notNull( fm.get() );
		tc.notNull( pm.get() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on open empty blocks awaiting notification" )
	public void get_GetOnOpenEmptyBlocksAwaitingNotification( TestCase tc ) throws InterruptedException {
		tc.assertTrue( this.fm.isEmpty() );
		tc.assertTrue( this.pm.isEmpty() );
		Agent fma = new Agent( this.fm );
		Agent pma = new Agent( this.pm );
		fma.start();
		pma.start();
		tc.assertEqual( "",  fma.getResults() );
		tc.assertEqual( "",  pma.getResults() );

		this.fm.put( "A" );
		this.pm.put( "A" );
		Thread.sleep( 20 );
		tc.assertEqual( "A",  fma.getResults() );
		tc.assertEqual( "A",  pma.getResults() );
		
		this.fm.put( "B" );
		this.pm.put( "B" );
		Thread.sleep( 20 );
		tc.assertEqual( "B",  fma.getResults() );
		tc.assertEqual( "B",  pma.getResults() );
		
		this.fm.close();
		this.pm.close();
		Thread.sleep( 20 );
		tc.assertEqual( "Z",  fma.getResults() );
		tc.assertEqual( "Z",  pma.getResults() );
		
		tc.assertFalse( fma.isAlive() );
		tc.assertFalse( pma.isAlive() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on open non empty returns non null" )
	public void get_GetOnOpenNonEmptyReturnsNonNull( TestCase tc ) {
		this.fifo.put( "A" );
		this.priority.put( "B" );
		tc.assertFalse( fifo.isEmpty() );
		tc.assertFalse( priority.isEmpty() );
		tc.assertTrue( fifo.isOpen() );
		tc.assertTrue( priority.isOpen() );
		tc.notNull( fm.get() );
		tc.notNull( pm.get() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on terminated empty returns null" )
	public void get_GetOnTerminatedEmptyReturnsNull( TestCase tc ) {
		this.fifo.terminate();
		this.priority.terminate();
		tc.assertTrue( fifo.isEmpty() );
		tc.assertTrue( priority.isEmpty() );
		tc.assertTrue( fifo.isTerminated() );
		tc.assertTrue( priority.isTerminated() );
		tc.isNull( fm.get() );
		tc.isNull( pm.get() );
	}

	@TestOrig.Impl( src = "public Object MultiQueue.get()", desc = "Get on terminated non empty returns null" )
	public void get_GetOnTerminatedNonEmptyReturnsNull( TestCase tc ) {
		this.fifo.put( "A" );
		this.priority.put( "B" );
		this.fifo.terminate();
		this.priority.terminate();
		tc.assertFalse( fifo.isEmpty() );
		tc.assertFalse( priority.isEmpty() );
		tc.assertTrue( fifo.isTerminated() );
		tc.assertTrue( priority.isTerminated() );
		tc.isNull( fm.get() );
		tc.isNull( pm.get() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isClosed()", desc = "is closed if queue is closed" )
	public void isClosed_IsClosedIfQueueIsClosed( TestCase tc ) {
		this.fifo.close();
		this.priority.close();
		tc.assertTrue( fifo.isClosed() );
		tc.assertTrue( priority.isClosed() );
		tc.assertTrue( fm.isClosed() );
		tc.assertTrue( pm.isClosed() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isEmpty()", desc = "Put on empty is not empty" )
	public void isEmpty_PutOnEmptyIsNotEmpty( TestCase tc ) {
		tc.assertTrue( fm.isEmpty() );
		tc.assertTrue( pm.isEmpty() );
		this.fm.put( "A" );
		this.pm.put( "A" );
		tc.assertFalse( fm.isEmpty() );
		tc.assertFalse( pm.isEmpty() );			
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isEmpty()", desc = "Put on non empty is not empty" )
	public void isEmpty_PutOnNonEmptyIsNotEmpty( TestCase tc ) {
		this.fm.put( "A" );
		this.pm.put( "A" );
		tc.assertFalse( fm.isEmpty() );
		tc.assertFalse( pm.isEmpty() );			
		this.fm.put( "B" );
		this.pm.put( "B" );
		tc.assertFalse( fm.isEmpty() );
		tc.assertFalse( pm.isEmpty() );			
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isEmpty()", desc = "Put then get on empty is empty" )
	public void isEmpty_PutThenGetOnEmptyIsEmpty( TestCase tc ) {
		tc.assertTrue( fm.isEmpty() );
		tc.assertTrue( pm.isEmpty() );
		this.fm.put( "A" );
		this.pm.put( "A" );
		this.fm.get();
		this.pm.get();
		tc.assertTrue( fm.isEmpty() );
		tc.assertTrue( pm.isEmpty() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isEmpty()", desc = "Put then put then get is not empty" )
	public void isEmpty_PutThenPutThenGetIsNotEmpty( TestCase tc ) {
		tc.assertTrue( fm.isEmpty() );
		tc.assertTrue( pm.isEmpty() );
		this.fm.put( "A" );
		this.pm.put( "A" );
		this.fm.put( "B" );
		this.pm.put( "B" );
		this.fm.get();
		this.pm.get();
		tc.assertFalse( fm.isEmpty() );
		tc.assertFalse( pm.isEmpty() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isOpen()", desc = "is open if queue is open" )
	public void isOpen_IsOpenIfQueueIsOpen( TestCase tc ) {
		tc.assertTrue( this.fifo.isOpen() );
		tc.assertTrue( this.pm.isOpen() );
		tc.assertTrue( this.fm.isOpen() );
		tc.assertTrue( this.pm.isOpen() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.isTerminated()", desc = "is terminated if queue is terminated" )
	public void isTerminated_IsTerminatedIfQueueIsTerminated( TestCase tc ) {
		this.fifo.terminate();
		this.pm.terminate();
		tc.assertTrue( this.fifo.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.put(Object)", desc = "Put on closed is ignored" )
	public void put_PutOnClosedIsIgnored( TestCase tc ) {
		this.fm.put( "A" );
		this.pm.put( "B" );
		this.fm.close();
		this.pm.close();
		tc.assertTrue( this.fm.isClosed() );
		tc.assertTrue( this.pm.isClosed() );
		tc.assertFalse( this.fm.put( "B" ) );
		tc.assertFalse( this.pm.put( "A" ) );
		tc.assertEqual( "A",  this.fm.get() );
		tc.assertEqual( "B",  this.pm.get() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.put(Object)", desc = "Put on open is accepted" )
	public void put_PutOnOpenIsAccepted( TestCase tc ) {
		tc.assertTrue( this.fm.isOpen() );
		tc.assertTrue( this.pm.isOpen() );
		tc.assertTrue( this.fm.put( "B" ) );
		tc.assertTrue( this.pm.put( "A" ) );
		tc.assertEqual( "B",  this.fm.get() );
		tc.assertEqual( "A",  this.pm.get() );
	}

	@TestOrig.Impl( src = "public boolean MultiQueue.put(Object)", desc = "Put on terminated is ignored" )
	public void put_PutOnTerminatedIsIgnored( TestCase tc ) {
		this.fm.put( "A" );
		this.pm.put( "B" );
		this.fm.terminate();
		this.pm.terminate();
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
		tc.assertFalse( this.fm.put( "B" ) );
		tc.assertFalse( this.pm.put( "A" ) );
		tc.isNull( this.fm.get() );
		tc.isNull( this.pm.get() );
	}

	@TestOrig.Impl( src = "public void MultiQueue.close()", desc = "Can close if open" )
	public void close_CanCloseIfOpen( TestCase tc ) {
		tc.assertTrue( this.fm.isOpen() );
		tc.assertTrue( this.pm.isOpen() );
		this.fm.close();
		this.pm.close();
		tc.assertTrue( this.fm.isClosed() );
		tc.assertTrue( this.pm.isClosed() );
	}

	@TestOrig.Impl( src = "public void MultiQueue.close()", desc = "Close on terminated ignored" )
	public void close_CloseOnTerminatedIgnored( TestCase tc ) {
		this.fm.terminate();
		this.pm.terminate();
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
		this.fm.close();
		this.pm.close();
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
	}

	@TestOrig.Impl( src = "public void MultiQueue.terminate()", desc = "Can terminate if closed" )
	public void terminate_CanTerminateIfClosed( TestCase tc ) {
		this.fm.close();
		this.pm.close();
		tc.assertTrue( this.fm.isClosed() );
		tc.assertTrue( this.pm.isClosed() );
		this.fm.terminate();
		this.pm.terminate();
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
	}

	@TestOrig.Impl( src = "public void MultiQueue.terminate()", desc = "Can terminate if open" )
	public void terminate_CanTerminateIfOpen( TestCase tc ) {
		tc.assertTrue( this.fm.isOpen() );
		tc.assertTrue( this.pm.isOpen() );
		this.fm.terminate();
		this.pm.terminate();
		tc.assertTrue( this.fm.isTerminated() );
		tc.assertTrue( this.pm.isTerminated() );
	}


	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new TestOrig(MultiQueueTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
