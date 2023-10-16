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
import sog.core.Test;

/**
 * Extract comments from source files. 
 * This allows the embedding of text content that is linked to a class with the source code 
 * for the class, similar to the unix/linux here-document.
 * 
 * There are two types of comments:
 * 
 * Labeled Comment Lines: Zero or more lines, located anywhere in the source file, with a
 * labeled single-line comment.
 * 		// LABEL	The content to extract
 * 		// LABEL	As many lines as needed
 * 		// LABEL	Each line includes the label
 * 		// Other comments
 * 		// LABEL	Not required to be contiguous
 * The LABEL cannot contain whitespace, and must be followed be a space or tab.
 * 
 * 
 * Tagged Comment Blocks: Exactly one multi-line comment block labeled with a given <TAG>.
 * 		/* <TAG>
 * 		 * The content to extract
 * 		 * 
 * 		 * The entire block is returned after removing the whitespace and leading *
 * 		 * If a second block exists an exception is thrown
 * 		 * /
 * The TAG cannot contain "<" or ">".
 * 
 * In both cases, the extraction methods below may fail if LABEL or TAG contain regular
 * expression character sequences, but this is not checked currently.
 * 
 * @author sundquis
 *
 */
@Test.Subject( "test." )
public class Commented {
	
	
	private final Path source;

	/**
	 * Construct a Commented instance that will extract text from the source file for 
	 * the given class.
	 * 
	 * @param clazz
	 */
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Can be a top-level class" )
	@Test.Decl( "Can be a member class" )
	@Test.Decl( "Can NOT be a secondary class" )
	@Test.Decl( "Can be a local class" )
	@Test.Decl( "Can be an anonymous class" )
	@Test.Decl( "Throws AppRuntime when the source file is not found" )
	public Commented( Class<?> clazz ) {
		this.source = App.get().sourceFile( Assert.nonNull( clazz ) );
	}


	/**
	 * Predicate defining the allowed labels for labeled single-line comments.
	 * 
	 * @param label
	 * @return
	 */
	@Test.Decl( "Label cannot be null" )
	@Test.Decl( "Label can be empty" )
	@Test.Decl( "Label cannot contain spaces" )
	@Test.Decl( "Label cannot contain tabs" )
	@Test.Decl( "Label can contain special characters" )
	public static boolean legalLineLabel( String label ) {
		return label != null && label.matches( "\\S*" );
	}
	
	/**
	 * The regular expression used to identify labeled one-line comments. It looks for:
	 * 		^		Beginning of the line
	 * 		\\s*	Optional whitespace
	 * 		//		One-line comment character sequence
	 * 		\\s*	Optional whitespace
	 * 		label	The given label
	 * 		[ \\t]	One space or tab
	 * 
	 * The returned lines have this prefix removed.
	 * 
	 * @param label	Can only contain word characters, [a-zA-Z_0-9]
	 * @return
	 */
	@Test.Decl( "Throws IllegalArgumentException for illegal label" )
	public static String labeledLineExpr( String label ) {
		if ( !Commented.legalLineLabel( label ) ) {
			throw new IllegalArgumentException( "Label cannot contain white space." ); 
		}
		return "^\\s*//\\s*" + label + "[ \\t]";
	}
	
	/**
	 * Returns a non-terminated stream containing labeled comments from the source file for this class
	 * Labeled comments have:
	 * 		<optional white space>//<optional white space><label><one tab or space>Content
	 * 
	 * @param label	Cannot contain whitespace
	 * @return
	 * @throws IOException
	 */
	@Test.Decl( "Return is not null" )
	@Test.Decl( "Return stream is not terminated" )
	@Test.Decl( "Return is empty when no label matches" )
	@Test.Decl( "Empty label returns single-line comments that start with space or tab" )
	@Test.Decl( "Empty label ignores single-line comments that do not start with space or tab" )
	@Test.Decl( "Returned line can be empty" )
	@Test.Decl( "Lines returned in order" )
	@Test.Decl( "Labels can be interspersed" )
	@Test.Decl( "Prefix is removed" )
	@Test.Decl( "White space before label is optional" )
	@Test.Decl( "Throws IllegalArgumentException for illegal label" )
	public Stream<String> getCommentedLines( String label ) throws IOException {
		return Files.lines( this.source )
			.filter( s -> s.matches( Commented.labeledLineExpr( label ) + ".*" ) )
			.map( s -> s.replaceFirst( Commented.labeledLineExpr( label ),  "" ) ); 
	}
	

	
	/**
	 * Predicate defining the allowed labels for labeled single-line comments.
	 * 
	 * @param label
	 * @return
	 */
	@Test.Decl( "Tag cannot be null" )
	@Test.Decl( "Tag  can be empty" )
	@Test.Decl( "Tag  cannot contain <" )
	@Test.Decl( "Tag  cannot contain >" )
	@Test.Decl( "Tag can contain special characters" )
	public static boolean legalBlockTag( String tag ) {
		return tag != null && tag.matches( "[^<>]*" );
	}

	
	/**
	 * The regular expression used to identify a tagged (non-javadoc) block comment. It looks for:
	 * 		^		Beginning of the line
	 * 		\\s*	Optional whitespace
	 * 		/\\*	Begin comment character sequence
	 * 		\\s*	Optional whitespace
	 * 		<tag>	The tag
	 * 		\\s*	Trailing whitespace
	 * 
	 * For example, this matches:
	 * 		/* <my-tag>
	 * But this does not
	 * 		/* <my-tag> other stuff * /
	 * 
	 * @param tag	Cannot contain "<" or ">" and SHOULD NOT contain regular expression character sequences (not checked)
	 * @return
	 */
	@Test.Decl( "Throws IllegalArgumentException for illegal tag" )
	public static String taggedBlockStart( String tag ) {
		if ( ! Commented.legalBlockTag( tag ) ) {
			throw new IllegalArgumentException( "Tag cannot contain < or >." );
		}

		return "^\\s*/\\*\\s*<" + tag + ">\\s*";
	}

	/**
	 * If the first line of a comment block matches, the subsequent lines up to but not including the
	 * closing "* /" end comment line are added to the return stream after removing 
	 * 		<Start><optional white space>*<one optional tab or space>
	 */
	@Test.Decl( "Matches * followed by space or tab" )
	@Test.Decl( "Matches * preceded by any whitespace" )
	public static final String taggedBlockPrefix = "^\\s*\\*[ \\t]?";

	/**
	 * Indicates the end of the comment block.
	 */
	@Test.Decl( "End-of-block character sequence may be preceded by any whitespace" )
	public static final String taggedBlockEnd = "^\\s*\\*/.*";

	
	/**
	 * Returns a non-terminated stream containing the lines in a tagged comment block from the
	 * source file for this class.
	 * 
	 * @param tag		Cannot contain "<" or ">"
	 * @return
	 * @throws IOException
	 */
	@Test.Decl( "Throws IllegalArgumentException for illegal tag" )
	@Test.Decl( "Throws AppRuntime if no block comment found for the given tag" )
	@Test.Decl( "Throws AppRuntime if multiple block comments found for the given tag" )
	@Test.Decl( "Return is not null" )
	@Test.Decl( "Returned stream is not terminated" )
	@Test.Decl( "Returned stream is empty for trivial block" )
	@Test.Decl( "Empty tag matches the diamond, <>" )
	@Test.Decl( "Returned lines can be empty" )
	@Test.Decl( "Lines returned in order" )
	@Test.Decl( "Ignores optional tab after prefix" )
	@Test.Decl( "One-line block comments ignored" )
	public Stream<String> getTaggedBlock( final String tag ) throws IOException {
		return StreamSupport.stream( new Spliterator<String>() {
			
			Stream<String> stream = null;
			Iterator<String> iterator = null;
			final String startTag = Commented.taggedBlockStart( tag );
			
			{	// Initialization: Look for the tagged block comment
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
					throw new AppRuntime( "Comment block for tag <" + tag + "> not found" );
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

			@Override public Spliterator<String> trySplit() { return null; }

			@Override public long estimateSize() { return Long.MAX_VALUE; }

			@Override public int characteristics() { return ORDERED | NONNULL | IMMUTABLE; }
			
		}, false );
	}

	
	
	/*
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
		Files.write( 
			Assert.nonNull( dest ), 
			new IterableIterator<CharSequence>( lines
				.flatMap( Assert.nonNull( mapper ) )
				.map( CharSequence.class::cast )
				.iterator() 
			), 
			StandardOpenOption.CREATE, 
			StandardOpenOption.TRUNCATE_EXISTING, 
			StandardOpenOption.WRITE 
		);
	}
	
	

	/**
	 * A filtering operation that:
	 * 		1. Extracts the commented lines in this Commented source file.
	 * 		2. Applies the mapping (usually a Macro).
	 * 		3. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param label		The non-null label
	 * @param mapper	The mapper to be applied to the extracted lines
	 * @param dest		The path to the destination output file
	 * @throws IOException
	 */
	@Test.Decl( "Throws IllegalArgumentException for illegal label" )
	@Test.Decl( "Throws AssertionError for null mapper" )
	@Test.Decl( "Throws AssertionError for null destination" )
	@Test.Decl( "If file does not exist it is created" )
	@Test.Decl( "If the file exists, the contents are replaced with the new lines" )
	@Test.Decl( "New lines have been mapped" )
	@Test.Decl( "New lines have prefix removed" )
	public void writeCommentedLines( String label, Function<String, Stream<String>> mapper, Path dest ) throws IOException {
		this.writeLines( this.getCommentedLines( label ), mapper, dest );
	}

	
	/**
	 * A filtering operation that:
	 * 		1. Extracts the commented lines in this Commented source file.
	 * 		2. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param label		The non-null label
	 * @param dest		The path to the destination output file
	 * @throws IOException
	 */
	@Test.Decl( "Default uses trivial mapper" )
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
	 * @param tag		The non-null tag identifying a block comment
	 * @param mapper	The mapper to be applied to the extracted lines
	 * @param dest		The path to the destination output file
	 * @throws IOException
	 */
	@Test.Decl( "Throws IllegalArgumentException for illegal tag" )
	@Test.Decl( "Throws AssertionError for null mapper" )
	@Test.Decl( "Throws AssertionError for null destination" )
	@Test.Decl( "If file does not exist it is created" )
	@Test.Decl( "If the file exists, the contents are replaced with the new lines" )
	@Test.Decl( "New lines have been mapped" )
	@Test.Decl( "New lines have prefix removed" )
	public void writeTaggedBlock( String tag, Function<String, Stream<String>> mapper, Path dest ) throws IOException {
		this.writeLines( this.getTaggedBlock( tag ), mapper, dest );
	}

	/**
	 * A filtering operation that:
	 * 		1. Extracts the tagged block in this Commented source file.
	 * 		2. Writes the mapped lines to a destination file.
	 * If the file exists, the contents are replaced with the new lines.
	 * If the file does not exist it is created.
	 * 
	 * @param tag		The non-null tag identifying a block comment
	 * @param dest		The path to the destination output file
	 * @throws IOException
	 */
	@Test.Decl( "Default uses trivial mapper" )
	public void writeTaggedBlock( String tag, Path dest ) throws IOException {
		this.writeTaggedBlock( tag, (String s) -> Stream.of( s ), dest );
	}


}
