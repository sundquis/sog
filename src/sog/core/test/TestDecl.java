/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.io.IOException;

import sog.core.AppException;
import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;
import sog.util.Commented;
import sog.util.IndentWriter;
import sog.util.Macro;
import sog.util.Printable;

/**
 * Represents a single test declaration as specified by a Test.Decl annotation
 */
@Test.Skip( "FIXME" )
public class TestDecl implements TestIdentifier, Commented, Printable {

	
	private final String memberName;
		
	private final String description;
	
	private final String methodName;
	
	private TestImpl impl = null;


	public TestDecl( String memberName, String subject, String description ) {
		this.memberName = Assert.nonEmpty( memberName );
		this.methodName = subject + "_" + Strings.toCamelCase( description );
		this.description = Assert.nonEmpty( description );
	}

	@Override
	public String getMemberName() {
		return this.memberName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	/** Return true if the impl was not previously set */
	public boolean setImpl( TestImpl impl ) {
		boolean result = this.impl == null;
		this.impl = Assert.nonNull( impl );
		return result;
	}
	
	public boolean unimplemented() {
		return this.impl == null;
	}

	//	STUB	
	//	STUB	@Test.Impl( 
	//	STUB		member = "${memberName}", 
	//	STUB		description = "${description}" 
	//	STUB	)
	//	STUB	public void ${methodName}( Test.Case tc ) {
	//	STUB		tc.addMessage( "GENERATED STUB" ).fail();
	//	STUB	}

	@Override
	public void print( IndentWriter out ) {
		Macro macro = new Macro()
			.expand( "memberName", this.memberName )
			.expand( "description", this.description )
			.expand( "methodName", this.methodName );
		try {
			this.getCommentedLines( "STUB" ).flatMap( macro ).forEach( out::println );
		} catch ( IOException ex ) {
			throw new AppException( ex );
		}
	}

}
