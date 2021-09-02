/**
 * Copyright (C) 2021
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

package sog.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import sog.core.App.OnShutdown;
import sog.util.FifoQueue;
import sog.util.FixedWidth;
import sog.util.MultiQueue;
import sog.util.Queue;

/**
 * A thread-safe tracing service that writes formatted messages to a file and optionally echoes
 * to standard output.
 * 
 * Trace instances are created with a "topic" and produce messages pertaining to that topic.
 * The messages also include information about the current thread, calling class and method
 * (from the stack trace) in addition to the diagnostic message. A Trace instance formats the 
 * message and then enqueues the message with the current MsgHandler.
 * 
 * MsgHandler instances maintain a worker thread that reads the queue for messages, transfers
 * messages to a buffer, then writes the buffer to a particular file. The size of the buffer
 * is a configurable property, as are the limits (warn and fail) on the number of messages
 * that can be written to the given file. When constructed, a MsgHandler registers to be
 * terminated when the application shuts down, and then the MsgHandler starts the worker thread
 * that listens for messages.
 * 
 * Static methods allow tracing to be disabled and enabled, and allow for the current tracing
 * file to be closed and replaced with a new file.
 */
@Test.Subject( "test." )
public class Trace {


	
	/* Subdirectory of <root> holding trace files. */
	private static String TRACE_DIR_NAME = Property.get( "dir.name",  "trace",  Property.STRING );
	
	/* Issue warning when this many lines have been written to a single file. */
	private static Integer WARN_LIMIT = Property.get( "warn.limit",  1000000,  Property.INTEGER );

	/* Stop tracing when this many lines have been written to a single file. */
	private static Integer FAIL_LIMIT = Property.get( "fail.limit",  100000000,  Property.INTEGER );
	
	/* Number of lines to hold in buffer before writing to file. */
	private static Integer MAX_BUFFER_SIZE = Property.get( "max.buffer.size",  100,  Property.INTEGER );

	
	/*
	 * 
	 */
	private static class MsgHandler implements Runnable, OnShutdown, Consumer<String> {
		
		private static int fileNo = 0;
		
		

		// Client write calls add messages to the queue. MsgHandler thread processes
		private final Queue<String> entries;
		
		// Only accessed by handler; a buffer between queued messages and file write
		private final List<String> buffer;
		
		// Current number of messages
		private volatile int lineCount;
		
		// Messages are written here
		private final Path traceFile;
		
		// Read the message queue, move to buffer, empty buffer to file
		private final Thread worker;
		
		private MsgHandler() {
			// Register for shutdown. Close the queue, process existing messages
			App.get().terminateOnShutdown( this );
			
			this.entries = new MultiQueue<String>( new FifoQueue<String>() );
			this.buffer = new LinkedList<String>();
			
			String seq = Strings.rightJustify( "" + Trace.MsgHandler.fileNo++, 4, '0' );
			String filename = App.get().startDateTime() + "#" + seq;
			LocalDir dir = new LocalDir( true );
			dir.sub( Trace.TRACE_DIR_NAME );
			this.traceFile = dir.getFile( filename,  LocalDir.Type.TEXT );
			this.lineCount = 0;
			
			this.worker = new Thread( this );
			this.worker.setDaemon( true );
			this.worker.start();
		}
		
		/**
		 * @see sog.core.App.OnShutdown#terminate()
		 */
		@Override
		@Test.Skip( "Closing the queue interrupts the waiting worker thread" )
		public void terminate() {
			this.entries.close();
			try {
				this.worker.join();
			} catch ( InterruptedException e ) {}
		}
		
		@Test.Decl( "Returns path to file containing messages on this trace instance" )
		@Test.Decl( "Return is not null" )
		public Path getPath() {
			return this.traceFile;
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		@Test.Decl( "Throws AppExcpetion when started from a non-worker thread" )
		public void run() {
			if ( Thread.currentThread() != this.worker ) {
				throw new AppException( "Cannot start externally." );
			}

			String msg = null;
			while ( (msg = this.entries.get()) != null ) {
				buffer.add( msg );
				if ( this.buffer.size() > Trace.MAX_BUFFER_SIZE ) {
					this.emptyBuffer();
				}
			}
			this.emptyBuffer();
		}
		
		private void emptyBuffer() {
			if ( this.buffer.size() == 0 ) {
				return;
			}
			
			try {
				Files.write( this.traceFile, this.buffer, StandardOpenOption.APPEND, StandardOpenOption.CREATE );
			} catch ( IOException e ) {
				Fatal.error( "Unable to write trace file.", e );
			} finally {
				this.buffer.clear();
			}
		}
		
		/**
		 * @see java.util.function.Consumer#accept(java.lang.Object)
		 */
		@Override
		@Test.Decl( "Throws AssertionError for null message" )
		@Test.Decl( "Issues global warning when message exceeds line count warn limit" )
		@Test.Decl( "Throws AppException when message exceeds line count fail limit" )
		public synchronized void accept( String msg ) {
			if ( this.lineCount == 0 ) {
				this.entries.put( Trace.HEADER );
				this.lineCount++;
			}
			
			this.entries.put( msg );
			this.lineCount++;
			
			if ( this.lineCount >= Trace.WARN_LIMIT ) {
				Fatal.warning( "Trace file " + this.traceFile + " line count is " + this.lineCount  );
			}
			if ( this.lineCount >= Trace.FAIL_LIMIT ) {
				Fatal.error( "Trace file " + this.traceFile + " line count exceeds limit" );
			}
		}
		
		@Override
		@Test.Decl( "Return is non-empty" )
		public String toString() {
			return this.traceFile.toString();
		}
		
	}

	private final static Object mutex = new Object() {};
	
	// The no-op consumer when messages are disabled
	private static final Consumer<String> DISABLED = (s) -> {};

	private static MsgHandler ENABLED = new MsgHandler();
	
	private static Consumer<String> currentHandler = Trace.ENABLED;

	
	/**
	 * Report the current tracing status.
	 * @return
	 */
	@Test.Decl( "Enabled when initialized" )
	public static boolean isEnabled() {
		return Trace.currentHandler != Trace.DISABLED;
	}

	/**
	 * When enabled, trace messages are written to a trace file.
	 */
	@Test.Decl( "Is enabled after" )
	@Test.Decl( "Same file used when re-enabled without close" )
	public static void enable() {
		synchronized ( mutex ) {
			Trace.currentHandler = Trace.ENABLED;
		}
	}
	
	/**
	 * When disabled, messages are not written to file. Messages may still be echoed to
	 * standard out.
	 */
	@Test.Decl( "Not enabled after" )
	@Test.Decl( "Messages ignored" )
	public static void disable() {
		synchronized ( mutex ) {
			Trace.currentHandler = Trace.DISABLED;
		}
	}
	
	/**
	 * Close the current Trace file and return the path to the file.
	 * Independent of the enabled status. When disabled, closes the handler that would be used if re-enabled.
	 * A new handler is constructed to handle subsequent writes.
	 * If no messages were written while enabled then this file does not exist.
	 * 
	 * @return
	 */
	@Test.Decl( "Returns non null when disabled" )
	@Test.Decl( "Returns non null when enabled" )
	@Test.Decl( "File does not exist if no messages written" )
	@Test.Decl( "File does not exist if disabled messages written" )
	@Test.Decl( "Returns readable file if written and enabled" )
	@Test.Decl( "Returned file contains messages if written and enabled" )
	@Test.Decl( "Opens new handler with different file" )
	@Test.Decl( "Can write after close" )
	public static Path close() {
		synchronized ( mutex ) {
			MsgHandler current = Trace.ENABLED;
			Trace.ENABLED = new MsgHandler();
			if ( Trace.isEnabled() ) {
				Trace.currentHandler = Trace.ENABLED;
			}
	
			current.terminate();
			return current.getPath();
		}
	}
	

	
	
	
	private String topic;
	private int seqNo;
	private boolean echoMessages;
	
	@Test.Decl( "Throws assertion error for empty topic" )
	public Trace( String topic, boolean echoMessages ) {
		this.topic = Assert.nonEmpty( topic );
		this.seqNo = 1;
		this.echoMessages = echoMessages;
		
		if ( echoMessages ) {
			System.out.println( Trace.HEADER );
		}
	}

	@Test.Decl( "Default does not echo messages" )
	public Trace( String topic ) {
		this( topic, false );
	}

	
	private static final FixedWidth formatter = new FixedWidth()
		.defaultFieldSeparator( " " )
		.right( 5,  ' ' )
		.left( 10,  ' ' )
		.left( 10,  ' ' )
		.left( 25,  ' ' )
		.left( 25,  ' ' )
		.left( 50,  ' ' );
	
	private static final String HEADER = Trace.formatter.format(
		"SEQ NO", "TOPIC", "THREAD", "CLASS NAME", "METHOD", "MESSAGE" );

	@Test.Decl( "Throws AssertionError for null message" )
	@Test.Decl( "Throws AssertionError for empty message" )
	@Test.Decl( "Multi thread stress test" )
	@Test.Decl( "Message printed if echo enabled" )
	@Test.Decl( "Warning issued when count exceeds warn limit" )
	@Test.Decl( "Throws AppException when count exceeds fail limit")
	public void write( String message ) {
		Assert.nonEmpty( message );
		StackTraceElement ste = (new Exception()).getStackTrace()[1];
		String threadName = Thread.currentThread().getName();
		String className = ste.getClassName();
		String methodName = ste.getMethodName();
		
		String msg = Trace.formatter.format(
			"" + seqNo++, this.topic, threadName, className, methodName, message );
		
		if ( echoMessages ) {
			System.out.println( msg );
		}
		
		Trace.currentHandler.accept( msg );
	}
	
	@Override
	@Test.Decl( "Indicates topic when enabled" )
	@Test.Decl( "Indicates state when enabled" )
	@Test.Decl( "Identifies state when disabled" )
	public String toString() {
		return "Trace(" + this.topic + ", " + (Trace.isEnabled() ? "ENABLED" : "DISABLED") + ")";
	}
	
	
}
