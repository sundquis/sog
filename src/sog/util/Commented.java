/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.io.IOException;
import java.nio.file.Files;
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
	 * 		<optional white space>//<optional white space><label><optional tab or space>Content
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

}
