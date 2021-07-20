/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.io.IOException;

import sog.core.Test;
import sog.util.Commented;

/**
 * @author sundquis
 *
 */
public class CommentedTest extends Test.Container implements Commented {
	
	public CommentedTest() {
		super( Commented.class );
	}
	

	// Test implementations

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Empty label returns end-of-line comments" )
	public void getCommentedLines_EmptyLabelReturnsEndOfLineComments( Test.Case tc ) throws IOException {
		tc.assertEqual( "Test implementations",  this.getCommentedLines("").findFirst().get() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Lines returned in order" )
	public void getCommentedLines_LinesReturnedInOrder( Test.Case tc ) throws IOException {
		// ORDERED	1
		// ORDERED	2
		tc.assertEqual( new Object[] {"1",  "2"}, this.getCommentedLines("ORDERED").toArray() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Multiple labels allowed" )
	public void getCommentedLines_MultipleLabelsAllowed( Test.Case tc ) throws IOException {
		// Another label	FOO
		tc.assertEqual( "FOO",  this.getCommentedLines("Another label").findFirst().get() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Return is empty when no label matches" )
	public void getCommentedLines_ReturnIsEmptyWhenNoLabelMatches( Test.Case tc ) throws IOException {
		tc.assertFalse( this.getCommentedLines("Bogus label").findAny().isPresent() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Return is not null" )
	public void getCommentedLines_ReturnIsNotNull( Test.Case tc ) throws IOException {
		tc.assertNonNull( this.getCommentedLines("Bogus tag") );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Returned line can be empty" )
	public void getCommentedLines_ReturnedLineCanBeEmpty( Test.Case tc ) throws IOException {
		// EMPTY	
		tc.assertEqual( "",  this.getCommentedLines("EMPTY").findFirst().get() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Labels can be interspersed" )
	public void getCommentedLines_TagsCanBeInterspersed( Test.Case tc ) throws IOException {
		//LABEL-1	one
		//LABEL-2	two
		//LABEL-1	one
		//LABEL-2	two
		tc.assertEqual( new Object[] {"one",  "one"}, this.getCommentedLines("LABEL-1").toArray() );
		tc.assertEqual( new Object[] {"two",  "two"}, this.getCommentedLines("LABEL-2").toArray() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Ignores optional tab after label" )
	public void getCommentedLines_IgnoresOptionalTabAfterLabel( Test.Case tc ) throws IOException {
		//	LABEL-FOLLWED-BY-TAB	content
		tc.assertEqual( "content",  this.getCommentedLines("LABEL-FOLLWED-BY-TAB").findFirst().get() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "One space or tab after label ignored" )
	public void getCommentedLines_OneSpaceOrTabAfterLabelIgnored( Test.Case tc ) throws IOException {
		//	LABEL-FOLLWED-BY-SPACE content
		tc.assertEqual( "content",  this.getCommentedLines("LABEL-FOLLWED-BY-SPACE").findFirst().get() );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "White space before label is optional" )
	public void getCommentedLines_WhiteSpaceBeforeLabelIsOptional( Test.Case tc ) throws IOException {
		//	 	 	WHITE-SPACE	content
		//  		WHITE-SPACE	content
		//          WHITE-SPACE	content
		tc.assertTrue( this.getCommentedLines("WHITE-SPACE").allMatch( s -> s.equals("content") ) );
	}

	@Test.Impl( member = "public Stream Commented.getCommentedLines(String)", description = "Works with anonymous classes" )
	public void getCommentedLines_WorksWithAnonymousClasses( Test.Case tc ) throws IOException {
		//	ANON	anonymous content
		tc.assertEqual( "anonymous content",  new Commented() {}.getCommentedLines( "ANON" ).findFirst().get() );
	}
	
	
}
