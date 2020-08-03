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
import sog.core.TestOrig;

/**
 * @author sundquis
 *
 */
public interface Commented {
	
	@TestOrig.Skip
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
	@TestOrig.Decl( "Return is not null" )
	@TestOrig.Decl( "Return is empty when no label matches" )
	@TestOrig.Decl( "Empty label returns end-of-line comments" )
	@TestOrig.Decl( "Returned line can be empty" )
	@TestOrig.Decl( "Lines returned in order" )
	@TestOrig.Decl( "Multiple labels allowed" )
	@TestOrig.Decl( "Labels can be interspersed" )
	@TestOrig.Decl( "Ignores optional tab after label" )
	@TestOrig.Decl( "White space before label is optional" )
	@TestOrig.Decl( "One space or tab after label ignored" )
	@TestOrig.Decl( "Works with anonymous classes" )
	default Stream<String> getCommentedLines( String label ) throws IOException {
		return Files.lines( App.get().sourceFile( this.getClass() ) )
			.filter( s -> s.matches( getRegExp( label + ".*" ) ) )
			.map( s -> s.replaceFirst( getRegExp( label ),  "" ) ); 
	}

}
