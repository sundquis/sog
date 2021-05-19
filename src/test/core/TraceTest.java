/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import sog.core.AppException;
import sog.core.Fatal;
import sog.core.Trace;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class TraceTest extends Test.Implementation {

	// Test implementations

	// If a test gets the path via Trace.close() then the test should delete the path
	private void delete( Path path ) {
		if ( path != null ) {
			try {
				Files.deleteIfExists( path );
			} catch ( IOException e ) {
				Fatal.warning( "Unable to delete trace file: " + e.getMessage() );
			}
		}
	}


	// 1. Run this first:
	@Test.Impl( member = "public boolean Trace.isEnabled()", description = "Enabled when initialized", priority = -3 )
	public void isEnabled_EnabledWhenInitialized( Test.Case tc ) {
		tc.assertTrue( Trace.isEnabled() );
	}

	// 2. Next check warn limit
	@Test.Impl( member = "public void Trace.write(String)", description = "Warning issued when count exceeds warn limit", priority = -2 )
	public void write_WarningIssuedWhenCountExceedsWarnLimit( Test.Case tc ) {
		// TOGGLE
		/* */	tc.addMessage( "Manually tested" );	/*
		int orig = this.getSubjectField( null,  "WARN_LIMIT",  0 );
		this.setSubjectField( null,  "WARN_LIMIT",  10 );
		Trace t = new Trace( "Test" );
		for ( int i = 0; i < 9; i++ ) {
			t.write( "" + i );
		}
		this.setSubjectField( null,  "WARN_LIMIT",  orig );
		tc.addMessage( "SHOULD SEE:" );
		tc.addMessage( "sundquis.core.AppException: WARNING: Trace file ..." );
		tc.fail();
		// */
	}

	// 3. Next check limits
	@Test.Impl( member = "public void Trace.write(String)", description = "Fatal error issued when count exceeds fail limit", priority = -1 )
	public void write_FatalErrorIssuedWhenCountExceedsFailLimit( Test.Case tc ) throws IOException {
		int orig = this.getSubjectField( null,  "FAIL_LIMIT",  0 );
		tc.afterThis( () -> this.setSubjectField( null,  "FAIL_LIMIT",  orig ) );
		this.setSubjectField( null,  "FAIL_LIMIT",  20 );
		Trace t = new Trace( "Test" );
		for ( int i = 0; i < 18; i++ ) {
			t.write( "" + i );
		}
		tc.expectError( AppException.class );
		t.write( "Too many" );
	}

	public static class Agent extends Thread {
		
		private Trace trace;
		
		Agent( String topic ) {
			this.trace = new Trace( topic );
		}
		
		@Override
		public void run() {
			for ( int i = 0; i < 10; i++ ) {
				for( int j = 0; j < 10; j++ ) {
					this.trace.write(  "O = " + i + ", I = " + j );
				}
				Thread.yield();
			}
		}
		
		Thread init() {
			this.start();
			return this;
		}
	}
	
	@Test.Impl( member = "public void Trace.write(String)", description = "Multi thread stress test" )
	public void write_MultiThreadStressTest( Test.Case tc ) throws IOException {
		// Get a clean trace file
		this.delete( Trace.close() );

		ArrayList<Thread> agents = new ArrayList<Thread>();
		for ( int i = 0; i < 10; i++ ) {
			agents.add( new Agent( "Thread " + i ).init() );
		}
		
		for ( Thread thread : agents ) {
			try {
				thread.join();
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}

		Path path = Trace.close();
		// First line header + 1000 = 10 * 10 * 10 messages
		tc.assertEqual( 1001,  Files.readAllLines( path ).size() );
		this.delete( path );
	}

	@Test.Impl( member = "public void Trace.write(String)", description = "Throws assertion exception for null message" )
	public void write_ThrowsAssertionExceptionForNullMessage( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Trace( "TEST" ).write( null );
	}

	@Test.Impl( member = "public void Trace.write(String)", description = "Throws assetrion exception for empty message" )
	public void write_ThrowsAssetrionExceptionForEmptyMessage( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Trace( "TEST" ).write( "" );
	}
	
	@Test.Impl( member = "public void Trace.write(String)", description = "Message printed if echo enabled" )
	public void write_MessagePrintedIfEchoEnabled( Test.Case tc ) {
		// TOGGLE
		/* */	tc.addMessage( "Manually tested" );	/*
		// SHOULD SEE:
		// SEQ NO TOPIC           THREAD     CLASS NAME                     METHOD                    MESSAGE
		//	    1 TEST            main       test.core.TraceTest            write_MessagePrintedIfEch FOO
		new Trace( "TEST", true ).write( "FOO" );
		tc.pass();
		// */
	}

	@Test.Impl( member = "public Trace(String, boolean)", description = "Throws assertion error for empty topic" )
	public void Trace_ThrowsAssertionErrorForEmptyTopic( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new Trace( "" );
	}
	
	@Test.Impl( member = "public Path Trace.close()", description = "Returned file contains messages if written and enabled" )
	public void close_ReturnedFileContainsMessagesIfWrittenAndEnabled( Test.Case tc ) throws IOException {
		String uniqueMessage = "Foo " + System.currentTimeMillis();
		Trace t = new Trace( "Test" );
		t.write( uniqueMessage );
		Path path = Trace.close();
		tc.assertTrue( Files.lines( path ).anyMatch( s -> s.contains( uniqueMessage ) ) );
		this.delete( path );
	}

	@Test.Impl( member = "public Path Trace.close()", description = "File does not exist if no messages written" )
	public void close_FileDoesNotExistIfNoMessagesWritten( Test.Case tc ) {
		Path path = Trace.close();
		tc.assertFalse( Files.exists( path ) );
		this.delete( path );
	}

	@Test.Impl( member = "public Path Trace.close()", description = "Returns readable file if written and enabled" )
	public void close_ReturnsReadableFileIfWrittenAndEnabled( Test.Case tc ) {
		new Trace( "Test" ).write( "Foo" );
		Path path = Trace.close();
		tc.assertTrue( Files.isReadable( path ) );
		this.delete( path );
	}

	@Test.Impl( member = "public Trace(String)", description = "Default does not echo messages" )
	public void Trace_DefaultDoesNotEchoMessages( Test.Case tc ) {
		tc.addMessage( "Manually verified" );
	}

	@Test.Impl( member = "public void Trace.disable()", description = "Not enabled after" )
	public void disable_NotEnabledAfter( Test.Case tc ) {
		Trace.disable();
		tc.assertFalse( Trace.isEnabled() );
		Trace.enable();  // Other tests require enabled
	}

	@Test.Impl( member = "public void Trace.enable()", description = "Is enabled after" )
	public void enable_IsEnabledAfter( Test.Case tc ) {
		Trace.disable();
		tc.assertFalse( Trace.isEnabled() );
		Trace.enable();
		tc.assertTrue( Trace.isEnabled() );
	}

	@Test.Impl( member = "public Path Trace.close()", description = "Opens new handler with different file" )
	public void close_OpensNewHandlerWithDifferentFile( Test.Case tc ) {
		Path path1 = Trace.close();
		Path path2 = Trace.close();
		tc.assertFalse( path1.equals( path2 ) );
		this.delete( path1 );
		this.delete( path2 );
	}

	@Test.Impl( member = "public Path Trace.close()", description = "Returns non null when disabled" )
	public void close_ReturnsNonNullWhenDisabled( Test.Case tc ) {
		Trace.disable();
		Path path = Trace.close();
		tc.notNull( path );
		Trace.enable();
		this.delete( path );;
	}

	@Test.Impl( member = "public Path Trace.close()", description = "Returns non null when enabled" )
	public void close_ReturnsNonNullWhenEnabled( Test.Case tc ) {
		Trace.enable();
		Path path = Trace.close();
		tc.notNull( path );
		Trace.enable();
		this.delete( path );;
	}
	
	@Test.Impl( member = "public String Trace.toString()", description = "Identifies state when disabled" )
	public void toString_IdentifiesStateWhenDisabled( Test.Case tc ) {
		Trace trace = new Trace( "Test" );
		Trace.disable();
		tc.assertTrue( trace.toString().contains( "DISABLED" ) );
		Trace.enable();
	}

	@Test.Impl( member = "public String Trace.toString()", description = "Indicates topic when enabled" )
	public void toString_IndicatesTopicWhenEnabled( Test.Case tc ) {
		String topic = "Some topic string";
		Trace trace = new Trace( topic );
		tc.assertTrue( trace.toString().contains( topic ) );
	}


	@Test.Impl( member = "public void Trace.disable()", description = "Messages ignored" )
	public void disable_MessagesIgnored( Test.Case tc ) throws IOException {
		String includedMessage = "INCLUDED " + System.currentTimeMillis();
		String excludedMessage = "EXCLUDED " + System.currentTimeMillis();
		Trace trace = new Trace( "Test" );
		trace.write( includedMessage );
		trace.write( includedMessage );
		trace.write( includedMessage );
		Trace.disable();
		trace.write( excludedMessage );
		Trace.enable();
		trace.write( includedMessage );
		Path path = Trace.close();
		tc.assertEqual( 0L,  Files.lines( path ).filter( s -> s.contains(excludedMessage) ).count() );
		tc.assertEqual( 4L,  Files.lines( path ).filter( s -> s.contains(includedMessage) ).count() );
		this.delete( path );
	}

	@Test.Impl( member = "public void Trace.enable()", description = "Same file used when re-enabled without close" )
	public void enable_SameFileUsedWhenReEnabledWithoutClose( Test.Case tc ) {
		Trace trace = new Trace( "Test" );
		String before = trace.toString();
		trace.write( "before" );
		Trace.disable();
		trace.write( "ignored" );
		Trace.enable();
		trace.write( "enabled" );
		String after = trace.toString();
		tc.assertEqual( before, after );
	}
	
	@Test.Impl( member = "public Path Trace.close()", description = "File does not exist if disabled messages written" )
	public void close_FileDoesNotExistIfDisabledMessagesWritten( Test.Case tc ) {
		this.delete( Trace.close() );
		Trace trace = new Trace( "Ignored" );
		Trace.disable();
		trace.write( "msg" );
		Trace.enable();
		tc.assertFalse( Files.exists( Trace.close() ) );  // If this test fails there is an undeleted trace file
	}

	@Test.Impl( member = "public String Trace.toString()", description = "Indicates state when enabled" )
	public void toString_IndicatesStateWhenEnabled( Test.Case tc ) {
		Trace trace = new Trace( "Test" );
		tc.assertTrue( trace.toString().contains( "ENABLED" ) );
	}
	
	@Test.Impl( member = "public Path Trace.close()", description = "Can write after close" )
	public void close_CanWriteAfterClose( Test.Case tc ) throws IOException {
		Trace t = new Trace( "Test" );
		t.write( "Before close" );
		Path p1 = Trace.close();
		t.write( "After close" );
		Path p2 = Trace.close();
		Thread.yield();
		tc.assertTrue( Files.lines(p1).anyMatch( s -> s.contains( "Before close" ) ) );
		tc.assertTrue( Files.lines(p2).anyMatch( s -> s.contains( "After close" ) ) );
		this.delete(p1);
		this.delete(p2);
	}

	
}
