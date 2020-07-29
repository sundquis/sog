/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.io.ByteArrayOutputStream;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.IndentWriter;

/**
 * @author sundquis
 *
 */
public class IndentWriterTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return IndentWriter.class;
	}

	// Test implementations
	
	
	
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	
	public Procedure beforeEach() {
		return () -> { baos.reset(); };
	}


	// Test implementations

	@Test.Impl( src = "public IndentWriter(OutputStream)", desc = "Default indent" )
	public void IndentWriter_DefaultIndent( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos );
		out.increaseIndent();
		out.println( "FOO" );
		tc.assertEqual( "    FOO\n",  this.baos.toString() );
	}

	@Test.Impl( src = "public void IndentWriter.println(String)", desc = "Before increase no prefix" )
	public void println_BeforeIncreaseNoPrefix( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos );
		out.println( "FOO" );
		tc.assertEqual( "FOO\n",  this.baos.toString() );
	}
	
	@Test.Impl( src = "public IndentWriter(OutputStream, String)", desc = "Indent can be empty" )
	public void IndentWriter_IndentCanBeEmpty( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, "" );
		out.println( "FOO" );
		tc.pass();
	}

	@Test.Impl( src = "public void IndentWriter.increaseIndent()", desc = "Increase empty indent is noop" )
	public void increaseIndent_IncreaseEmptyIndentIsNoop( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, "" );
		out.println( "FOO" );
		String result = this.baos.toString();
		this.baos.reset();
		out.increaseIndent();
		out.println( "FOO" );
		tc.assertEqual( result,  this.baos.toString() );
	}
	
	@Test.Impl( src = "public IndentWriter(OutputStream, String)", desc = "Throws assertion error for null indent" )
	public void IndentWriter_ThrowsAssertionErrorForNullIndent( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new IndentWriter( this.baos, null );
	}

	@Test.Impl( src = "public IndentWriter(OutputStream, String)", desc = "Throws assertion error for null stream" )
	public void IndentWriter_ThrowsAssertionErrorForNullStream( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new IndentWriter( null, "" );
	}

	@Test.Impl( src = "public void IndentWriter.decreaseIndent()", desc = "Can decrease after increase" )
	public void decreaseIndent_CanDecreaseAfterIncrease( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.increaseIndent();
		out.decreaseIndent();
		out.increaseIndent();
		out.increaseIndent();
		out.decreaseIndent();
		out.decreaseIndent();
		tc.pass();
	}

	@Test.Impl( src = "public void IndentWriter.decreaseIndent()", desc = "Illegal state for decrease before increase" )
	public void decreaseIndent_IllegalStateForDecreaseBeforeIncrease( TestCase tc ) {
		tc.expectError( IllegalStateException.class );
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.decreaseIndent();
	}

	@Test.Impl( src = "public void IndentWriter.decreaseIndent()", desc = "Illegal state for more decreases than increases" )
	public void decreaseIndent_IllegalStateForMoreDecreasesThanIncreases( TestCase tc ) {
		tc.expectError( IllegalStateException.class );
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		try {
			out.increaseIndent();
			out.decreaseIndent();
		} catch ( Exception e ) {
			// Ignore
		}
		out.decreaseIndent();
	}

	@Test.Impl( src = "public void IndentWriter.increaseIndent()", desc = "Can increase indent" )
	public void increaseIndent_CanIncreaseIndent( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.increaseIndent();
		tc.pass();
	}

	@Test.Impl( src = "public void IndentWriter.println(String)", desc = "Prints prefix" )
	public void println_PrintsPrefix( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
		out.increaseIndent();
		out.println( "FOO" );
		String result = this.baos.toString();
		tc.assertTrue( result.startsWith( "XXXXFOO" ) );
	}
	
	@Test.Impl( src = "public void IndentWriter.increaseIndent()", desc = "Increase indent increases indent" )
	public void increaseIndent_IncreaseIndentIncreasesIndent( TestCase tc ) {
		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
		out.increaseIndent();
		out.increaseIndent();
		out.println( "FOO" );
		String result = this.baos.toString();
		tc.assertTrue( result.startsWith( "XXXXXXXXFOO" ) );
	}
	
	@Test.Impl( src = "public void IndentWriter.decreaseIndent()", desc = "Custom indent removed" )
	public void decreaseIndent_CustomIndentRemoved( TestCase tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}

	@Test.Impl( src = "public void IndentWriter.increaseIndent(String)", desc = "Custom indent used" )
	public void increaseIndent_CustomIndentUsed( TestCase tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}

	@Test.Impl( src = "public void IndentWriter.increaseIndent(String)", desc = "Custom indent used after default" )
	public void increaseIndent_CustomIndentUsedAfterDefault( TestCase tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}
	
	

	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new Test(IndentWriterTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
