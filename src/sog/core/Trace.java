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
import java.io.PrintWriter;
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
 * to a PrintWriter.
 * 
 * The singleton Trace instance holds a queue of pending messages. Trace is Runnable, and backs
 * an instance worker thread that polls the queue for new messages and transfers them to a buffer.
 * When the buffer is full it writes messages to a file. When the file is full it opens a new
 * file for subsequent messages.
 * 
 * Messages are formatted and contain a "source" as well as information about the current thread, 
 * calling class and method (from the stack trace) in addition to the diagnostic message. 
 * 
 * Static methods allow tracing to be disabled and enabled, and allow for the Trace facility to be
 * completely terminated.
 */
@Test.Subject( "test." )
public class Trace implements Runnable, OnShutdown {

	
	
	/* Subdirectory of <root> holding trace files. */
	private static final String TRACE_DIR_NAME = Property.get( "dir.name",  "trace",  Property.STRING );

	/* After this many messages have been added the buffer is written to the current trace file. */
	private static final Integer BUFFER_LIMIT = Property.get( "buffer.limit", 100, Property.INTEGER );
	
	/* After this many lines have been written to a trace file a new file is opened. */
	private static final Integer LINE_LIMIT = Property.get( "line.limit", 10_000, Property.INTEGER );
	
	/* After this many files have been written a Fatal.error is triggered. */
	private static final Integer FILE_LIMIT = Property.get( "file.limit", 100, Property.INTEGER );
	

	/* The singleton instance. */
	private static final Trace INSTANCE = new Trace();

	
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for empty message" )
	@Test.Decl( "Message is written to file before application exits" )
	@Test.Decl( "Trace message includes details on the source object" )
	@Test.Decl( "Trace message includes the given message" )
	@Test.Decl( "Trace message includes details on the calling thread" )
	@Test.Decl( "Trace message includes details on the calling class" )
	@Test.Decl( "Trace message includes details on the calling method" )
	public static void write( Object source, String message ) {
		Fatal.unimplemented( "" );
	}

	
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for empty message" )
	@Test.Decl( "Throws AssertionError for null PrintWriter" )
	@Test.Decl( "Message is written to file before application exits" )
	@Test.Decl( "Message is echoed to the given PrintWriter immediately" )
	@Test.Decl( "Trace message includes details on the source object" )
	@Test.Decl( "Trace message includes the given message" )
	@Test.Decl( "Trace message includes details on the calling thread" )
	@Test.Decl( "Trace message includes details on the calling class" )
	@Test.Decl( "Trace message includes details on the calling method" )
	public static void write( Object source, String message, PrintWriter out ) {
		Fatal.unimplemented( "" );
	}
	
	
	@Test.Decl( "After enable(true) message are logged in the trace file" )
	@Test.Decl( "After enable(false) messages are ignored" )
	@Test.Decl( "Is idempotent" )
	@Test.Decl( "Does not affect pending messages" )
	public static void enable( boolean enable ) {
		Fatal.unimplemented( "" );
	}
	
	
	@Test.Decl( "After enable(true) returns true" )
	@Test.Decl( "After enable(false) returns false" )
	public static boolean isEnabled() {
		Fatal.unimplemented( "" );
		return false;
	}

	
	/*
	 * The thread that processes enqueued messages. The MsgHandler moves messages to a buffer
	 * and then, when the buffer is full, writes the messages to a trace file.
	 */
	private static class MsgHandler implements Runnable, OnShutdown, Consumer<String> {
		
		private static int fileNo = 0;
		
		

		/* Client write( message ) calls add messages to the queue. MsgHandler thread processes them. */
		private final Queue<String> entries;
		
		/* Only accessed by handler; a buffer between queued messages and file write. */
		private final List<String> buffer;
		
		/* Current number of messages written to the trace file. */
		private volatile int lineCount;
		
		/* Messages are written here. */
		private final Path traceFile;
		
		/* Read the message queue, move to buffer, empty buffer to file. */
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
				this.worker.join( 2000L );
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

	


	/* When a message is added */
	private final FixedWidth formatter;
	
	private final String header;
	
	/* Client write( message ) calls add messages to the queue. Trace worker thread processes them. */
	private final Queue<String> entries;
	
	/* Only accessed by the Trace worker; a buffer between queued messages and file write. */
	private final List<String> buffer;
	
	/* Number of messages written to the current trace file. */
	private volatile int lineNumber;
	
	/* Trace files are  */
	private int fileNumber;
	
	/* Messages are written here. */
	private final Path traceFile;
	
	/* Read the message queue, move to buffer, empty buffer to file. */
	private final Thread worker;
	
	
	private Trace() {
		this.formatter = new FixedWidth()
			.defaultFieldSeparator( " " )
			.right( 7,  ' ' )
			.left( 10,  ' ' )
			.left( 10,  ' ' )
			.left( 25,  ' ' )
			.left( 25,  ' ' )
			.left( 50,  ' ' );
			
		this.header = this.formatter.format( "LINE NO", "SOURCE", "THREAD", "CLASS NAME", "METHOD", "MESSAGE" );
	}
	
	
	// FIXME: Remove
	public Trace( String s ) { this(); }
	
	// FIXME: Remove
	public static Path close() { return null; }
	
	// FIXME: Remove
	public Trace( String s, boolean b ) { this(); }
	
	// FIXME: Remove
	public static void enable() {}

	// FIXME: Remove
	public static void disable() {}

	// FIXME: Remove
	public boolean echoEnabled() { return false; }





	 

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
