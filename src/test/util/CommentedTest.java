/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.io.IOException;

import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.Commented;

/**
 * @author sundquis
 *
 */
public class CommentedTest implements TestContainer, Commented {

	@Override
	public Class<?> subjectClass() {
		return Commented.class;
	}

	// Test implementations

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Empty label returns end-of-line comments" )
	public void getCommentedLines_EmptyLabelReturnsEndOfLineComments( TestCase tc ) throws IOException {
		tc.assertEqual( "Test implementations",  this.getCommentedLines("").findFirst().get() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Lines returned in order" )
	public void getCommentedLines_LinesReturnedInOrder( TestCase tc ) throws IOException {
		// ORDERED	1
		// ORDERED	2
		tc.assertEqual( new Object[] {"1",  "2"}, this.getCommentedLines("ORDERED").toArray() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Multiple labels allowed" )
	public void getCommentedLines_MultipleLabelsAllowed( TestCase tc ) throws IOException {
		// Another label	FOO
		tc.assertEqual( "FOO",  this.getCommentedLines("Another label").findFirst().get() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Return is empty when no label matches" )
	public void getCommentedLines_ReturnIsEmptyWhenNoLabelMatches( TestCase tc ) throws IOException {
		tc.assertFalse( this.getCommentedLines("Bogus label").findAny().isPresent() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Return is not null" )
	public void getCommentedLines_ReturnIsNotNull( TestCase tc ) throws IOException {
		tc.notNull( this.getCommentedLines("Bogus tag") );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Returned line can be empty" )
	public void getCommentedLines_ReturnedLineCanBeEmpty( TestCase tc ) throws IOException {
		// EMPTY	
		tc.assertEqual( "",  this.getCommentedLines("EMPTY").findFirst().get() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Labels can be interspersed" )
	public void getCommentedLines_TagsCanBeInterspersed( TestCase tc ) throws IOException {
		//LABEL-1	one
		//LABEL-2	two
		//LABEL-1	one
		//LABEL-2	two
		tc.assertEqual( new Object[] {"one",  "one"}, this.getCommentedLines("LABEL-1").toArray() );
		tc.assertEqual( new Object[] {"two",  "two"}, this.getCommentedLines("LABEL-2").toArray() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Ignores optional tab after label" )
	public void getCommentedLines_IgnoresOptionalTabAfterLabel( TestCase tc ) throws IOException {
		//	LABEL-FOLLWED-BY-TAB	content
		tc.assertEqual( "content",  this.getCommentedLines("LABEL-FOLLWED-BY-TAB").findFirst().get() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "One space or tab after label ignored" )
	public void getCommentedLines_OneSpaceOrTabAfterLabelIgnored( TestCase tc ) throws IOException {
		//	LABEL-FOLLWED-BY-SPACE content
		tc.assertEqual( "content",  this.getCommentedLines("LABEL-FOLLWED-BY-SPACE").findFirst().get() );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "White space before label is optional" )
	public void getCommentedLines_WhiteSpaceBeforeLabelIsOptional( TestCase tc ) throws IOException {
		//	 	 	WHITE-SPACE	content
		//  		WHITE-SPACE	content
		//          WHITE-SPACE	content
		tc.assertTrue( this.getCommentedLines("WHITE-SPACE").allMatch( s -> s.equals("content") ) );
	}

	@TestOrig.Impl( src = "public Stream Commented.getCommentedLines(String)", desc = "Works with anonymous classes" )
	public void getCommentedLines_WorksWithAnonymousClasses( TestCase tc ) throws IOException {
		//	ANON	anonymous content
		tc.assertEqual( "anonymous content",  new Commented() {}.getCommentedLines( "ANON" ).findFirst().get() );
	}
	
	
	
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(CommentedTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
