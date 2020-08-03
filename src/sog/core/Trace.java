/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import sog.core.App.OnShutdown;
import sog.util.FifoQueue;
import sog.util.FixedWidth;
import sog.util.MultiQueue;
import sog.util.Queue;

/**
 * @author sundquis
 *
 */
public class Trace {
	
	// Subdirectory of <root> holding trace files
	private static String TRACE_DIR_NAME = Property.get( "dir.name",  "trace",  Property.STRING );
	
	// Issue warning when this many lines written
	private static Integer WARN_LIMIT = Property.get( "warn.limit",  1000000,  Property.INTEGER );

	// Stop tracing when this many lines written
	private static Integer FAIL_LIMIT = Property.get( "fail.limit",  100000000,  Property.INTEGER );
	
	// Number of lines to hold in buffer before writing to file
	private static Integer MAX_BUFFER_SIZE = Property.get( "max.buffer.size",  100,  Property.INTEGER );

	
	

	@TestOrig.Skip
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
		
		MsgHandler() {
			this.entries = new MultiQueue<String>( new FifoQueue<String>() );
			this.buffer = new LinkedList<String>();
			
			// Register for shutdown. Close the queue, process existing messages
			App.get().terminateOnShutdown( this );
			
			this.lineCount = 0;
			
			DateFormat fmt = new SimpleDateFormat( "yyyy.MM.dd" );
			String filename = fmt.format( new Date() ) + "." + fileNo++;
			LocalDir dir = new LocalDir( true );
			dir.sub( Trace.TRACE_DIR_NAME );
			this.traceFile = dir.getFile( filename,  LocalDir.Type.TEXT );
			
			this.worker = new Thread( this );
			this.worker.setDaemon( true );
			this.worker.start();
		}
		
		/**
		 * @see sog.core.App.OnShutdown#terminate()
		 */
		@Override
		public void terminate() {
			this.entries.close();
			try {
				this.worker.join();
			} catch ( InterruptedException e ) {}
		}
		
		public Path getPath() {
			return this.traceFile;
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
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
		public String toString() {
			return this.traceFile.toString();
		}
		
	}

	private final static Object mutex = new Object() {};
	
	// The no-op consumer when messages are disabled
	private static final Consumer<String> DISABLED = (s) -> {};

	private static MsgHandler ENABLED = new MsgHandler();
	
	private static Consumer<String> currentHandler = Trace.ENABLED;
	
	private static Consumer<String> getHandler() {
		synchronized ( mutex ) {
			return Trace.currentHandler;
		}
	}

	/**
	 * Report the current tracing status.
	 * @return
	 */
	@TestOrig.Decl( "Enabled when initialized" )
	public static boolean isEnabled() {
		return Trace.currentHandler != Trace.DISABLED;
	}

	/**
	 * When enabled, trace messages are written to a trace file.
	 */
	@TestOrig.Decl( "Is enabled after" )
	@TestOrig.Decl( "Same file used when re-enabled without close" )
	public static void enable() {
		synchronized ( mutex ) {
			Trace.currentHandler = Trace.ENABLED;
		}
	}
	
	/**
	 * When disabled, messages are not written to file. Messages may still be echoed to
	 * standard out.
	 */
	@TestOrig.Decl( "Not enabled after" )
	@TestOrig.Decl( "Messages ignored" )
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
	@TestOrig.Decl( "Returns non null when disabled" )
	@TestOrig.Decl( "Returns non null when enabled" )
	@TestOrig.Decl( "File does not exist if no messages written" )
	@TestOrig.Decl( "File does not exist if disabled messages written" )
	@TestOrig.Decl( "Returns readable file if written and enabled" )
	@TestOrig.Decl( "Returned file contains messages if written and enabled" )
	@TestOrig.Decl( "Opens new handler with different file" )
	@TestOrig.Decl( "Can write after close" )
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
	
	@TestOrig.Decl( "Throws assertion error for empty topic" )
	public Trace( String topic, boolean echoMessages ) {
		this.topic = Assert.nonEmpty( topic );
		this.seqNo = 1;
		this.echoMessages = echoMessages;
		
		if ( echoMessages ) {
			System.out.println( Trace.HEADER );
		}
	}

	@TestOrig.Decl( "Default does not echo messages" )
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

	@TestOrig.Decl( "Throws assertion exception for null message" )
	@TestOrig.Decl( "Throws assetrion exception for empty message" )
	@TestOrig.Decl( "Multi thread stress test" )
	@TestOrig.Decl( "Message printed if echo enabled" )
	@TestOrig.Decl( "Warning issued when count exceeds warn limit" )
	@TestOrig.Decl( "Fatal error issued when count exceeds fail limit")
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
		
		Trace.getHandler().accept( msg );
	}
	
	@Override
	@TestOrig.Decl( "Indicates topic when enabled" )
	@TestOrig.Decl( "Indicates state when enabled" )
	@TestOrig.Decl( "Identifies state when disabled" )
	public String toString() {
		return "Trace(" + this.topic + ", " + (Trace.isEnabled() ? "ENABLED" : "DISABLED") + ")";
	}
	
	
}
