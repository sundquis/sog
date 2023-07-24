/**
 * Copyright (C) 2021, 2023
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
package test.sog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.AppRuntime;
import sog.core.LocalDir;
import sog.core.LocalDir.Type;
import sog.core.Test;
import sog.util.Commented;

/**
 * @author sundquis
 *
 */
@Test.Subject( "test." )
public class CommentedTest extends Test.Container {
	
	private final Commented commented = new Commented( CommentedTest.class );
	
	private final Path source = App.get().sourceFile( CommentedTest.class );
	
	public CommentedTest() {
		super( Commented.class );
	}
	

	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Can be a local class" 
    )
    public void tm_0BA2354C0( Test.Case tc ) {
    	class Local {}
    	Commented c = new Commented( Local.class );
    	tc.assertEqual( this.source, this.getSubjectField( c, "source", null ) );
    }

    class Member {}
    
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Can be a member class" 
    )
    public void tm_0D287682B( Test.Case tc ) {
    	Commented c = new Commented( Member.class );
    	tc.assertEqual( this.source, this.getSubjectField( c, "source", null ) );
    }
    
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Can NOT be a secondary class" 
    )
    public void tm_03ADDBCC9( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	new Commented( Secondary.class );
    }

    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Can be a top-level class" 
    )
    public void tm_0226A9061( Test.Case tc ) {
    	Commented c = new Commented( CommentedTest.class );
    	tc.assertEqual( this.source, this.getSubjectField( c, "source", null ) );
    }
    
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Can be an anonymous class" 
    )
    public void tm_0F345CCAC( Test.Case tc ) {
    	Commented c = new Commented( new Object() {}.getClass() );
    	tc.assertEqual( this.source, this.getSubjectField( c, "source", null ) );
    }
    
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Throws AppRuntime when the source file is not found" 
    )
    public void tm_0672A2D6A( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	new Commented( Object.class );
    }
    
    @Test.Impl( 
    	member = "constructor: Commented(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_0CE4D60A6( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Commented( null );
    }
    
    @Test.Impl( 
    	member = "field: String Commented.taggedBlockEnd", 
    	description = "End-of-block character sequence may be preceded by any whitespace" 
    )
    public void tm_052237D6A( Test.Case tc ) {
    	tc.assertTrue( "*/".matches( Commented.taggedBlockEnd ) );
    	tc.assertTrue( " */".matches( Commented.taggedBlockEnd ) );
    	tc.assertTrue( "\t*/".matches( Commented.taggedBlockEnd ) );
    	tc.assertTrue( "\t \t  */".matches( Commented.taggedBlockEnd ) );
    }
    
    @Test.Impl( 
    	member = "field: String Commented.taggedBlockPrefix", 
    	description = "Matches * followed by space or tab" 
    )
    public void tm_087CCD2DE( Test.Case tc ) {
    	tc.assertTrue( "* ".matches( Commented.taggedBlockPrefix ) );
    	tc.assertTrue( "*\t".matches( Commented.taggedBlockPrefix ) );
    }
    
    @Test.Impl( 
    	member = "field: String Commented.taggedBlockPrefix", 
    	description = "Matches * preceded by any whitespace" 
    )
    public void tm_0BE3A286B( Test.Case tc ) {
    	tc.assertTrue( "*".matches( Commented.taggedBlockPrefix ) );
    	tc.assertTrue( " *".matches( Commented.taggedBlockPrefix ) );
    	tc.assertTrue( "\t*".matches( Commented.taggedBlockPrefix ) );
    	tc.assertTrue( " \t\t *".matches( Commented.taggedBlockPrefix ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Empty label ignores single-line comments that do not start with space or tab" 
    )
    public void tm_05E8E2B0E( Test.Case tc ) throws IOException {
    	//Not included in the single-line comments
    	tc.assertEqual( 0L, this.commented.getCommentedLines( "" )
    		.filter( s -> "Not included in the single-line comments".equals( s ) ).count() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Empty label returns single-line comments that start with space or tab" 
    )
    public void tm_050F65664( Test.Case tc ) throws IOException {
    	// This line included in single-line comments
    	//	This line included in single-line comments
    	tc.assertEqual( 2L, this.commented.getCommentedLines( "" )
    		.filter( s -> "This line included in single-line comments".equals( s ) ).count() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Labels can be interspersed" 
    )
    public void tm_0C54FA9C8( Test.Case tc ) throws IOException {
    	// LABEL-1	1
    	// LABEL-2	1
    	// LABEL-1	2
    	// LABEL-2	2
    	tc.assertEqual( List.of( "1", "2" ), 
    		this.commented.getCommentedLines( "LABEL-1" ).collect( Collectors.toList() ) );
    	tc.assertEqual( List.of( "1", "2" ), 
        		this.commented.getCommentedLines( "LABEL-2" ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Lines returned in order" 
    )
    public void tm_0FE561829( Test.Case tc ) throws IOException {
    	// ORDERED	1
    	// ORDERED	2
    	// ORDERED	3
    	tc.assertEqual( List.of( "1", "2", "3" ), 
        	this.commented.getCommentedLines( "ORDERED" ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Prefix is removed" 
    )
    public void tm_0CB1903FE( Test.Case tc ) throws IOException {
    	// THIS-PREFIX-REMOVED	Remaining content
    	tc.assertEqual( "Remaining content", 
    		this.commented.getCommentedLines( "THIS-PREFIX-REMOVED" ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Return is empty when no label matches" 
    )
    public void tm_0055BCFFB( Test.Case tc ) throws IOException {
    	tc.assertEqual( 0L, this.commented.getCommentedLines( "bogus_label_matches_nothing" ).count() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Return is not null" 
    )
    public void tm_01095018A( Test.Case tc ) throws IOException {
    	tc.assertNonNull( this.commented.getCommentedLines( "bogus_label_matches_nothing" ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Return stream is not terminated" 
    )
    public void tm_075506F1C( Test.Case tc ) throws IOException {
    	this.commented.getCommentedLines( "bogus_label_matches_nothing" ).count();
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Returned line can be empty" 
    )
    public void tm_04A99686B( Test.Case tc ) throws IOException {
    	/* The following label has a single tab character following it */
    	// EMPTY-LINE	
    	tc.assertEqual( "", this.commented.getCommentedLines( "EMPTY-LINE" ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "Throws IllegalArgumentException for illegal label" 
    )
    public void tm_063FEB468( Test.Case tc ) throws IOException {
    	tc.expectError( IllegalArgumentException.class );
    	this.commented.getCommentedLines( "No spaces" ).count();
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getCommentedLines(String)", 
    	description = "White space before label is optional" 
    )
    public void tm_0C302210A( Test.Case tc ) throws IOException {
    	//THIS-WORKS	This works
    	tc.assertEqual( "This works", this.commented.getCommentedLines( "THIS-WORKS" ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Empty tag matches the diamond, <>" 
    )
    public void tm_0D5090BEB( Test.Case tc ) throws IOException {
    	/* <>
    	 * Diamond matched
    	 */
    	tc.assertEqual( "Diamond matched", this.commented.getTaggedBlock( "" ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Ignores optional tab after prefix" 
    )
    public void tm_0288CD498( Test.Case tc ) throws IOException {
    	/*	<Ignores optional tab after prefix>
    	 *	Just this text
    	 */
    	tc.assertEqual( "Just this text", 
    		this.commented.getTaggedBlock( "Ignores optional tab after prefix" ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Lines returned in order" 
    )
    public void tm_0621CB0B9( Test.Case tc ) throws IOException {
    	/*	<ORDERED>
    	 * 1
    	 * 2
    	 * 3
    	 */
    	tc.assertEqual( List.of( "1", "2", "3" ),
    		this.commented.getTaggedBlock( "ORDERED" ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "One-line block comments ignored" 
    )
    public void tm_09FC512B4( Test.Case tc ) throws IOException {
    	/* <One-line block comments ignored> This is ignored */
    	/* <One-line block comments ignored>
    	 * This is included
    	 */
    	tc.assertEqual( "This is included", 
    		this.commented.getTaggedBlock( "One-line block comments ignored" ).findAny().get() );
    }

    /*	<Empty Stream>
     */
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Return is not null" 
    )
    public void tm_021154EFA( Test.Case tc ) throws IOException {
    	tc.assertNonNull( this.commented.getTaggedBlock( "Empty Stream" ) );
    }

    /*	<Empty lines>
     * 
     * 
     */
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Returned lines can be empty" 
    )
    public void tm_026B2D988( Test.Case tc ) throws IOException {
    	tc.assertEqual( List.of( "", "" ),
    		this.commented.getTaggedBlock( "Empty lines" ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Returned stream is empty for trivial block" 
    )
    public void tm_0E21721A5( Test.Case tc ) throws IOException {
    	tc.assertEqual( 0L, this.commented.getTaggedBlock( "Empty Stream" ).count() );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Returned stream is not terminated" 
    )
    public void tm_09C25B0ED( Test.Case tc ) throws IOException {
    	this.commented.getTaggedBlock( "Empty Stream" ).count();
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Throws AppRuntime if multiple block comments found for the given tag" 
    )
    public void tm_0D0C0B7FD( Test.Case tc ) throws IOException {
    	/*	<Repeated block>
    	 */
    	/*	<Repeated block>
    	 */
    	tc.expectError( AppRuntime.class );
    	this.commented.getTaggedBlock( "Repeated block" ).count();
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Throws AppRuntime if no block comment found for the given tag" 
    )
    public void tm_0552F89C3( Test.Case tc ) throws IOException {
    	tc.expectError( AppRuntime.class );
    	this.commented.getTaggedBlock( "bogus tag, matches nothing" );
    }
    
    @Test.Impl( 
    	member = "method: Stream Commented.getTaggedBlock(String)", 
    	description = "Throws IllegalArgumentException for illegal tag" 
    )
    public void tm_084EE765E( Test.Case tc ) throws IOException {
    	tc.expectError( IllegalArgumentException.class );
    	this.commented.getTaggedBlock( "pi > 3" );
    }
    
    @Test.Impl( 
    	member = "method: String Commented.labeledLineExpr(String)", 
    	description = "Throws IllegalArgumentException for illegal label" 
    )
    public void tm_06FF7341E( Test.Case tc ) {
    	tc.expectError( IllegalArgumentException.class );
    	Commented.labeledLineExpr( "no spaces" );
    }
    
    @Test.Impl( 
    	member = "method: String Commented.taggedBlockStart(String)", 
    	description = "Throws IllegalArgumentException for illegal tag" 
    )
    public void tm_0A34D4B43( Test.Case tc ) {
    	tc.expectError( IllegalArgumentException.class );
    	Commented.taggedBlockStart( "3 < pi" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalBlockTag(String)", 
    	description = "Tag  can be empty" 
    )
    public void tm_023000AF3( Test.Case tc ) {
    	tc.assertTrue( Commented.legalBlockTag( "" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalBlockTag(String)", 
    	description = "Tag  cannot contain <" 
    )
    public void tm_02AE87DC6( Test.Case tc ) {
    	tc.assertFalse( Commented.legalBlockTag( "abc < def" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalBlockTag(String)", 
    	description = "Tag  cannot contain >" 
    )
    public void tm_02AE88548( Test.Case tc ) {
    	tc.assertFalse( Commented.legalBlockTag( "xyz > def" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalBlockTag(String)", 
    	description = "Tag can contain special characters" 
    )
    public void tm_07314F8B0( Test.Case tc ) {
    	tc.assertTrue( Commented.legalBlockTag( ": @ # -" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalBlockTag(String)", 
    	description = "Tag cannot be null" 
    )
    public void tm_057DA420A( Test.Case tc ) {
    	tc.assertFalse( Commented.legalBlockTag( null ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalLineLabel(String)", 
    	description = "Label can be empty" 
    )
    public void tm_024F58654( Test.Case tc ) {
    	tc.assertTrue( Commented.legalBlockTag( "" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalLineLabel(String)", 
    	description = "Label can contain special characters" 
    )
    public void tm_01C0F06A1( Test.Case tc ) {
    	tc.assertTrue( Commented.legalLineLabel( ":@#-" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalLineLabel(String)", 
    	description = "Label cannot be null" 
    )
    public void tm_0F8E851FB( Test.Case tc ) {
    	tc.assertFalse( Commented.legalLineLabel( null ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalLineLabel(String)", 
    	description = "Label cannot contain spaces" 
    )
    public void tm_0BC92B878( Test.Case tc ) {
    	tc.assertFalse( Commented.legalLineLabel( "no spaces" ) );
    }
    
    @Test.Impl( 
    	member = "method: boolean Commented.legalLineLabel(String)", 
    	description = "Label cannot contain tabs" 
    )
    public void tm_0059BAF89( Test.Case tc ) {
    	tc.assertFalse( Commented.legalLineLabel( "no\ttabs" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "If file does not exist it is created" 
    )
    public void tm_040E4F8E3( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLCreated", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	if ( dest.toFile().exists() ) {
    		Files.delete( dest );
    	}
    	tc.assertFalse( dest.toFile().exists() );
    	// WCL-CREATED	File created
    	Function<String, Stream<String>> mapper = (s) -> Stream.of(  s  );
    	this.commented.writeCommentedLines( "WCL-CREATED", mapper, dest );
    	tc.assertTrue( dest.toFile().exists() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "If the file exists, the contents are replaced with the new lines" 
    )
    public void tm_0FDB027BD( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLReplaced", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of(  s  );

    	// WCL-ORIGINAL	Some content
    	this.commented.writeCommentedLines( "WCL-ORIGINAL", mapper, dest );
    	tc.assertEqual( "Some content", Files.lines( dest ).findAny().get() );

    	// WCL-REPLACED New content
    	this.commented.writeCommentedLines( "WCL-REPLACED", mapper, dest );
    	tc.assertEqual( "New content", Files.lines( dest ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "New lines have been mapped" 
    )
    public void tm_007E86C48( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLMapped", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( "A", "B", "C" );

    	// WCL-MAPPED	Some content
    	this.commented.writeCommentedLines( "WCL-MAPPED", mapper, dest );
    	tc.assertEqual( List.of( "A", "B", "C" ), Files.lines( dest ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "New lines have prefix removed" 
    )
    public void tm_0B3A52E27( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLPrefix", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );

    	// WCL-PREFIX	Some content
    	this.commented.writeCommentedLines( "WCL-PREFIX", mapper, dest );
    	tc.assertEqual( "Some content", Files.lines( dest ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "Throws IllegalArgumentException for illegal label" 
    )
    public void tm_0C373410A( Test.Case tc ) throws IOException {
    	tc.expectError( IllegalArgumentException.class );
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLIllegal", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );
    	
    	this.commented.writeCommentedLines( "illegal label", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "Throws AssertionError for null destination" 
    )
    public void tm_07B5DAACB( Test.Case tc ) throws IOException {
    	tc.expectError( AssertionError.class );
    	Path dest = null;
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );
    	// WCL-NULL-DEST		
    	this.commented.writeCommentedLines( "WCL-NULL-DEST", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Function, Path)", 
    	description = "Throws AssertionError for null mapper" 
    )
    public void tm_0FF23F77A( Test.Case tc ) throws IOException {
    	tc.expectError( AssertionError.class );
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLIllegal", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = null;
    	
    	// WCL-NULL-MAPPER		
    	this.commented.writeCommentedLines( "WCL-NULL-MAPPER", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeCommentedLines(String, Path)", 
    	description = "Default uses trivial mapper" 
    )
    public void tm_02C482E17( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WCLDefault", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	
    	// WCL-DEFAULT	No mapping
    	this.commented.writeCommentedLines( "WCL-DEFAULT", dest );
    	tc.assertEqual( "No mapping", Files.lines( dest ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "If file does not exist it is created" 
    )
    public void tm_005F3456D( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBCreated", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	if ( dest.toFile().exists() ) {
    		Files.delete( dest );
    	}
    	tc.assertFalse( dest.toFile().exists() );
    	Function<String, Stream<String>> mapper = (s) -> Stream.of(  s  );

    	/*	<WTB Created>
    	 * A line
    	 */
    	this.commented.writeTaggedBlock( "WTB Created", mapper, dest );
    	tc.assertTrue( dest.toFile().exists() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "If the file exists, the contents are replaced with the new lines" 
    )
    public void tm_0BEA5A147( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBReplaced", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of(  s  );

    	/*	<WTB Original>
    	 * The original content
    	 */
    	this.commented.writeTaggedBlock( "WTB Original", mapper, dest );
    	tc.assertEqual( "The original content", Files.lines( dest ).findAny().get() );

    	/*	<WTB Replaced>
    	 * The new content
    	 */
    	this.commented.writeTaggedBlock( "WTB Replaced", mapper, dest );
    	tc.assertEqual( "The new content", Files.lines( dest ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "New lines have been mapped" 
    )
    public void tm_08AC8FD52( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBMapped", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( "A", "B", "C" );

    	/*	<WTB Mapped>
    	 * 	A line of content
    	 */
    	this.commented.writeTaggedBlock( "WTB Mapped", mapper, dest );
    	tc.assertEqual( List.of( "A", "B", "C" ), Files.lines( dest ).collect( Collectors.toList() ) );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "New lines have prefix removed" 
    )
    public void tm_006B388DD( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBPrefix", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );

    	/*	<WTB Prefix>
    	 *	Some content
    	 */
    	this.commented.writeTaggedBlock( "WTB Prefix", mapper, dest );
    	tc.assertEqual( "Some content", Files.lines( dest ).findAny().get() );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "Throws IllegalArgumentException for illegal tag" 
    )
    public void tm_03E131FA6( Test.Case tc ) throws IOException {
    	tc.expectError( IllegalArgumentException.class );
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBIllegal", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );
    	
    	this.commented.writeTaggedBlock( "illegal < tag", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "Throws AssertionError for null destination" 
    )
    public void tm_09B9EE7D5( Test.Case tc ) throws IOException {
    	tc.expectError( AssertionError.class );
    	Path dest = null;
    	Function<String, Stream<String>> mapper = (s) -> Stream.of( s );

    	/*	<WTB Null destination>
    	 */
    	this.commented.writeTaggedBlock( "WTB Null destination", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Function, Path)", 
    	description = "Throws AssertionError for null mapper" 
    )
    public void tm_0DBDF3C30( Test.Case tc ) throws IOException {
    	tc.expectError( AssertionError.class );
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBIllegal", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	Function<String, Stream<String>> mapper = null;
    	
    	// WCL-NULL-MAPPER		
    	/*	<WTB Null mapper>
    	 */
    	this.commented.writeTaggedBlock( "WTB Null mapper", mapper, dest );
    }
    
    @Test.Impl( 
    	member = "method: void Commented.writeTaggedBlock(String, Path)", 
    	description = "Default uses trivial mapper" 
    )
    public void tm_097B069CD( Test.Case tc ) throws IOException {
    	Path dest = new LocalDir().sub( "tmp" ).getFile( "WTBDefault", Type.TEMPORARY );
    	dest.toFile().deleteOnExit();
    	
    	/*	<WTB Default>
    	 *	No mapping
    	 */
    	this.commented.writeTaggedBlock( "WTB Default", dest );
    	tc.assertEqual( "No mapping", Files.lines( dest ).findAny().get() );
    }
	

    
	
	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( Commented.class )
			.concurrent( true )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Commented.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}

	
}

class Secondary {}