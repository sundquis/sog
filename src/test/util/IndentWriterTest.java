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
import sog.util.IndentWriter;

/**
 * @author sundquis
 *
 */
public class IndentWriterTest extends Test.Container {

	// Test implementations
	
	
	
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	
	public Procedure beforeEach() {
		return () -> { baos.reset(); };
	}


	// Test implementations

	@Test.Impl( member = "public IndentWriter(OutputStream)", description = "Default indent" )
	public void IndentWriter_DefaultIndent( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos );
		out.increaseIndent();
		out.println( "FOO" );
		tc.assertEqual( "    FOO\n",  this.baos.toString() );
	}

	@Test.Impl( member = "public void IndentWriter.println(String)", description = "Before increase no prefix" )
	public void println_BeforeIncreaseNoPrefix( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos );
		out.println( "FOO" );
		tc.assertEqual( "FOO\n",  this.baos.toString() );
	}
	
	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Indent can be empty" )
	public void IndentWriter_IndentCanBeEmpty( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, "" );
		out.println( "FOO" );
	}

	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Increase empty indent is noop" )
	public void increaseIndent_IncreaseEmptyIndentIsNoop( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, "" );
		out.println( "FOO" );
		String result = this.baos.toString();
		this.baos.reset();
		out.increaseIndent();
		out.println( "FOO" );
		tc.assertEqual( result,  this.baos.toString() );
	}
	
	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Throws assertion error for null indent" )
	public void IndentWriter_ThrowsAssertionErrorForNullIndent( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new IndentWriter( this.baos, null );
	}

	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Throws assertion error for null stream" )
	public void IndentWriter_ThrowsAssertionErrorForNullStream( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new IndentWriter( null, "" );
	}

	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Can decrease after increase" )
	public void decreaseIndent_CanDecreaseAfterIncrease( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.increaseIndent();
		out.decreaseIndent();
		out.increaseIndent();
		out.increaseIndent();
		out.decreaseIndent();
		out.decreaseIndent();
	}

	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Illegal state for decrease before increase" )
	public void decreaseIndent_IllegalStateForDecreaseBeforeIncrease( Test.Case tc ) {
		tc.expectError( IllegalStateException.class );
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.decreaseIndent();
	}

	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Illegal state for more decreases than increases" )
	public void decreaseIndent_IllegalStateForMoreDecreasesThanIncreases( Test.Case tc ) {
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

	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Can increase indent" )
	public void increaseIndent_CanIncreaseIndent( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, ">>>" );
		out.increaseIndent();
	}

	@Test.Impl( member = "public void IndentWriter.println(String)", description = "Prints prefix" )
	public void println_PrintsPrefix( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
		out.increaseIndent();
		out.println( "FOO" );
		String result = this.baos.toString();
		tc.assertTrue( result.startsWith( "XXXXFOO" ) );
	}
	
	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Increase indent increases indent" )
	public void increaseIndent_IncreaseIndentIncreasesIndent( Test.Case tc ) {
		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
		out.increaseIndent();
		out.increaseIndent();
		out.println( "FOO" );
		String result = this.baos.toString();
		tc.assertTrue( result.startsWith( "XXXXXXXXFOO" ) );
	}
	
	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Custom indent removed" )
	public void decreaseIndent_CustomIndentRemoved( Test.Case tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}

	@Test.Impl( member = "public void IndentWriter.increaseIndent(String)", description = "Custom indent used" )
	public void increaseIndent_CustomIndentUsed( Test.Case tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}

	@Test.Impl( member = "public void IndentWriter.increaseIndent(String)", description = "Custom indent used after default" )
	public void increaseIndent_CustomIndentUsedAfterDefault( Test.Case tc ) {
		tc.addMessage( "Unimplemented" ).fail();
	}
	
	
}
