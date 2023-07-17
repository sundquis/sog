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
import java.util.function.Function;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public interface Commented {
	
	public static String getRegExp( String tag ) {
		return "^\\s*//\\s*" + tag + "[ \\t]?";
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
	default Stream<String> getCommentedLines( String label ) throws IOException {
		return Files.lines( App.get().sourceFile( this.getClass() ) )
			.filter( s -> s.matches( getRegExp( label + ".*" ) ) )
			.map( s -> s.replaceFirst( getRegExp( label ),  "" ) ); 
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
	default void writeLines( String label, Path file, Function<String, Stream<String>> mapper ) throws IOException {
		// WARNING: Forming an Iterable that will only only work once
		Stream<String> lines = this.getCommentedLines( label );
		Iterable<CharSequence> iter = () -> lines.flatMap( mapper ).map( CharSequence.class::cast ).iterator();

		Files.write( file, iter, 
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
	}

	default void writeLines( String label, Path file ) throws IOException {
		this.writeLines( label, file, (String s) -> Stream.of( s ) );
	}




}
