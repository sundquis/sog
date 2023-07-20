/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import sog.core.App;
import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.LocalDir;
import sog.core.LocalDir.Type;
import sog.core.Test;

/**
 * Extract comments from source files. Two types of comments:
 * 
 * Labeled Comment Lines
 * 		// LABEL	The content to extract
 * 		// LABEL	As many lines as needed
 * 		// LABEL	Each line includes the label
 * 
 * 
 * Tagged Comment Blocks
 * 		/* <TAG>
 * 		 * The content to extract
 * 		 * Multiple lines
 * 		 * /
 * 
 * For tagged, only whitespace is allowed after <TAG>, so no single line comments.
 * 
 * @author sundquis
 *
 */
@Test.Subject( "test." )
public class Commented {
	
	
	private final Path source;
	
	public Commented( Class<?> clazz ) {
		this.source = App.get().sourceFile( Assert.nonNull( clazz ) );
	}

	

	public static String labeledLineExpr( String label ) {
		return "^\\s*//\\s*" + label + "[ \\t]?";
	}
	
	/**
	 * Returns a non-terminated stream containing labeled comments from the source file for this class
	 * Labeled comments have:
	 * 		<optional white space>//<optional white space><label><one optional tab or space>Content
	 * 
	 * @param label
	 * @return
	 * @throws IOException
	 */
	@Test.Decl( "Return is not null" )
	@Test.Decl( "Return is empty when no label matches" )
	@Test.Decl( "Empty label returns end-of-line comments" )
	@Test.Decl( "Returned line can be empty" )
	@Test.Decl( "Lines returned in order" )
	@Test.Decl( "Multiple labels allowed" )
	@Test.Decl( "Labels can be interspersed" )
	@Test.Decl( "Ignores optional tab after label" )
	@Test.Decl( "White space before label is optional" )
	@Test.Decl( "One space or tab after label ignored" )
	@Test.Decl( "Works with anonymous classes" )
	public Stream<String> getCommentedLines( String label ) throws IOException {
		return Files.lines( this.source )
			.filter( s -> s.matches( labeledLineExpr( label + ".*" ) ) )
			.map( s -> s.replaceFirst( labeledLineExpr( label ),  "" ) ); 
	}
	

	
	/**
	 * Matches a non-javadoc comment block that starts with a specified comment tag of the form
	 * 		<start><optional white space>/*<optional white space><tag><optional white space>
	 * 
	 * For example, this matches:
	 * 		/* <my-tag>
	 * But this does not
	 * 		/* <my-tag> other stuff * /
	 * 
	 * @param tag
	 * @return
	 */
	public static String taggedBlockStart( String tag ) {
		return "^\\s*/\\*\\s*<" + tag + ">\\s*";
	}
	
	/**
	 * If the first line of a comment block matches, the subsequent lines up to but not including the
	 * closing "* /" end comment line are added to the return stream after removing 
	 * 		<Start><optional white space>*<one optional tab or space>
	 */
	public static final String taggedBlockPrefix = "^\\s*\\*[ \\t]?";

	/**
	 * Indicates the end of the comment block.
	 */
	public static final String taggedBlockEnd = "^\\s*\\*/.*";

	/**
	 * Returns a non-terminated stream containing the lines in a tagged comment block from the
	 * source file for this class.
	 * 
	 * @param tag
	 * @return
	 * @throws IOException
	 */
	// TODO:
	// Check tag for legal characters.
	public Stream<String> getTaggedBlock( final String tag ) throws IOException {
		return StreamSupport.stream( new Spliterator<String>() {
			
			Stream<String> stream = null;
			Iterator<String> iterator = null;
			final String startTag = Commented.taggedBlockStart( tag );
			
			{
				stream = Files.lines( Commented.this.source );
				iterator = stream.iterator();
				boolean searching = true;

				while ( searching && iterator.hasNext() ) {
					if ( iterator.next().matches( startTag ) ) {
						searching = false;
					}
				}
				
				// If searching is still true we ran out of lines
				if ( searching ) {
					this.close();
				}
				
				// If searching is false there must be at least one more line in the stream,
				// from the end-of-comment line, because we do not allow a one-line comment block.
			}
			
			@Override
			public boolean tryAdvance( Consumer<? super String> action ) {
				String line = null;
				if ( this.iterator != null && this.iterator.hasNext() ) {
					line = this.iterator.next();
					if ( line.matches( Commented.taggedBlockEnd ) ) {
						this.checkForDuplicateTag();
						this.close();
						return false;
					} else {
						action.accept( line.replaceFirst( Commented.taggedBlockPrefix, "" ) );
						return true;
					}
				} else {
					this.close();
					return false;
				}
			}
			
			void checkForDuplicateTag() {
				while ( this.iterator.hasNext() ) {
					if ( this.iterator.next().matches( startTag ) ) {
						this.close();
						throw new AppRuntime( "Duplicate start tags found: " + tag );
					}
				}
			}
			
			void close() {
				if ( this.stream != null ) {
					this.stream.close();
					this.stream = null;
					this.iterator = null;
				}
			}

			@Override
			public Spliterator<String> trySplit() {
				return null;
			}

			@Override
			public long estimateSize() {
				return Long.MAX_VALUE;
			}

			@Override
			public int characteristics() {
				return ORDERED | NONNULL | IMMUTABLE;
			}
			
		}, false );
	}

	
	/**
	 * A filtering operation that:
	 * 		1. Applies the mapping (usually a Macro) to each line.
	 * 		2. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param lines
	 * @param mapper
	 * @param dest
	 * @throws IOException 
	 */
	private void writeLines( Stream<String> lines, Function<String, Stream<String>> mapper, Path dest ) throws IOException {
		Files.write( dest, new IterableIterator<CharSequence>( lines.flatMap( mapper ).map( CharSequence.class::cast ).iterator() ), 
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
	}
	
	

	/**
	 * A filtering operation that:
	 * 		1. Extracts the commented lines in this Commented source file.
	 * 		2. Applies the mapping (usually a Macro).
	 * 		3. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param label
	 * @param file
	 * @param mapper
	 * @param options
	 * @throws IOException
	 */
	public void writeCommentedLines( String label, Function<String, Stream<String>> mapper, Path dest ) throws IOException {
		this.writeLines( this.getCommentedLines( label ), mapper, dest );
	}

	
	public void writeCommentedLines( String label, Path dest ) throws IOException {
		this.writeCommentedLines( label, (String s) -> Stream.of( s ), dest );
	}


	
	
	/**
	 * A filtering operation that:
	 * 		1. Extracts the tagged block in this Commented source file.
	 * 		2. Applies the mapping (usually a Macro).
	 * 		3. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param label
	 * @param file
	 * @param mapper
	 * @param options
	 * @throws IOException
	 */
	public void writeTaggedBlock( String tag, Function<String, Stream<String>> mapper, Path dest ) throws IOException {
		this.writeLines( this.getTaggedBlock( tag ), mapper, dest );
	}

	public void writeTaggedBlock( String tag, Path dest ) throws IOException {
		this.writeTaggedBlock( tag, (String s) -> Stream.of( s ), dest );
	}

	/* <foo> this is ignored */

	/* <foo> also ignored
	 * ignored
	 */
	
	/* <foo>  	
	 * A
	 * B
	 * C
	 */
	
	/* <foo>
	 * Second occurrence ignored... should report error?
	 */
	
	/*<bar>
	 * Allowed
	   Allowed, but then extra white space
	 */
    
	/* <EXT-XML>
     * <?xml version = "1.0" encoding = "UTF-8" ?>
     * <!DOCTYPE	root	SYSTEM	"TEST.dtd" >
     * <root/>
     */
	
    /* <EXT-DTD>
     * <!ELEMENT	root	EMPTY>
     */

	public static void main( String[] args ) throws IOException {
		new Commented(Commented.class ).getTaggedBlock( "bar" ).map( s -> ">>> " + s ).forEach( System.out::println );
		Path destXml = new LocalDir().sub( "tmp" ).getFile( "Commented", Type.XML );
		Path destDtd = new LocalDir().sub( "tmp" ).getFile( "Commented", Type.DTD );
		new Commented(Commented.class ).writeTaggedBlock( "EXT-XML", destXml );
		new Commented(Commented.class ).writeTaggedBlock( "EXT-DTD", destDtd );
		try {
			new Commented( Commented.class ).getTaggedBlock( "foo" ).map( s -> ">>> " + s ).forEach( System.out::println );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
		
		System.out.println("Done!");
	}


}
