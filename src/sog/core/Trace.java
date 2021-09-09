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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * 
 * FIXME: FixedWidth results is so much truncation and onformation loss; this should be replaced
 * with a new CSV formatter.
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
	
	

	/* Produces the fixed width formatted message. */
	private static final FixedWidth FORMATTER = new FixedWidth()
		.right( "SEQ NO", 6, '0' ).sep( " " )
		.left( "SOURCE", 10, ' ' ).sep( " " )
		.left( "THREAD", 20,  ' ' ).sep( " " )
		.left( "CLASS NAME", 25,  ' ' ).sep( " " )
		.left( "METHOD", 20,  ' ' ).sep( " " )
		.left( "MESSAGE", 50,  ' ' );
	
	/* Global counter for all messages. */
	private static volatile int seqNo = 0;

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
	@Test.Decl( "Multi-thread stress test" )
	public static void write( Object source, String message, PrintWriter out ) {
		if ( !Trace.enabled ) {
			return;
		}
		
		StackTraceElement ste = (new Exception()).getStackTrace()[1];
		String entry = Trace.FORMATTER.format( 
			Trace.seqNo++,
			source,
			Thread.currentThread(),
			ste.getClassName(),
			ste.getMethodName(),
			message
		);
		Trace.INSTANCE.entries.put( entry );
		if ( out != null ) {
			out.println( entry );
		}
	}
	
	@Test.Decl( "Throws AssertionError for null source" )
	@Test.Decl( "Throws AssertionError for empty message" )
	@Test.Decl( "Message is written to file before application exits" )
	@Test.Decl( "Trace message includes details on the source object" )
	@Test.Decl( "Trace message includes the given message" )
	@Test.Decl( "Trace message includes details on the calling thread" )
	@Test.Decl( "Trace message includes details on the calling class" )
	@Test.Decl( "Trace message includes details on the calling method" )
	public static void write( Object source, String message ) {
		Trace.write( source, message, null );
	}

	
	
	private static volatile boolean enabled = true;
	
	@Test.Decl( "After enable(true) message are logged in the trace file" )
	@Test.Decl( "After enable(false) messages are ignored" )
	@Test.Decl( "Is idempotent" )
	@Test.Decl( "Does not affect pending messages" )
	public static void enable( boolean enable ) {
		Trace.enabled = enable;
	}
	
	@Test.Decl( "After enable(true) returns true" )
	@Test.Decl( "After enable(false) returns false" )
	public static boolean isEnabled() {
		return Trace.enabled;
	}

	
	
	/* The singleton instance. */
	private static final Trace INSTANCE = new Trace();

	

	/* Client write( message ) calls add messages to the queue. Trace worker thread processes them. */
	private final Queue<String> entries;
	
	
	// All remaining state is only accessed by the Trace worker thread.
	
	/* A buffer between queued messages and file write. */
	private final List<String> buffer;
	
	/* Number of messages written to the current trace file. */
	private int lineCount;
	
	/* The number of Trace files that have been created.  */
	private int fileCount;
	
	/* Read the message queue, move to buffer, empty buffer to file. */
	private final Thread worker;
	
	
	private Trace() {
		App.get().terminateOnShutdown( this );
		
		this.entries = new MultiQueue<String>( new FifoQueue<String>() );
		this.buffer = new ArrayList<String>();
		this.lineCount = 0;
		this.fileCount = 0;
		
		this.worker = new Thread( this );
		this.worker.setDaemon( true );
		this.worker.start();
	}
	
	
	@Override
	@Test.Decl( "Pending messages are written to the trace file" )
	@Test.Decl( "After termination, messages are ignored" )
	public void terminate() {
		Trace.write( "Trace", "Terminating: " + new Date() );
		this.entries.close();
		try {
			this.worker.join( 2000L );
		} catch ( InterruptedException e ) {}
	}
	
	
	@Override
	@Test.Decl( "Throws AppException when called from an external thread" )
	@Test.Decl( "Inserts header for new files" )
	public void run() {
		if ( Thread.currentThread() != this.worker ) {
			throw new AppException( "Cannot start externally." );
		}

		String msg = null;
		while ( (msg = this.entries.get()) != null ) {
			if ( this.lineCount == 0 ) {
				this.buffer.add( Trace.FORMATTER.header() );
			}
			this.buffer.add( msg );
			if ( this.buffer.size() > Trace.BUFFER_LIMIT ) {
				this.emptyBuffer();
			}
		}
		
		this.emptyBuffer();
	}
	
	
	private void emptyBuffer() {
		String filename = 
			App.get().startDateTime() + "#" + Strings.rightJustify( "" + this.fileCount, 4, '0' );
		
		Path traceFile = new LocalDir( true )
			.sub( Trace.TRACE_DIR_NAME )
			.getFile( filename, LocalDir.Type.TEXT );
		
		try {
			Files.write( traceFile, this.buffer, StandardOpenOption.APPEND, StandardOpenOption.CREATE );
		} catch ( IOException e ) {
			Fatal.error( "Unable to write trace file.", e );
		} 
		
		this.lineCount += this.buffer.size();
		this.buffer.clear();
		
		if ( this.lineCount > Trace.LINE_LIMIT ) {
			this.lineCount = 0;
			this.fileCount++;
		}
		
		if ( this.fileCount > Trace.FILE_LIMIT ) {
			Fatal.error( "Maximum number of Trace files exceeded." );
		}
		
	}
	
	
	
}
