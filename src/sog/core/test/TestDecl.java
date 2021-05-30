/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Assert;
import sog.core.Test;

/**
 * Represents a single test declaration as specified by a Test.Decl annotation
 */
public class TestDecl implements TestIdentifier {

	
	private final String memberName;
	
	private final String description;
	
	private TestImpl impl = null;


	public TestDecl( String memberName, String description ) {
		this.memberName = Assert.nonEmpty( memberName );
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
	
	public TestImpl setImpl( TestImpl impl ) {
		TestImpl result = this.impl;
		this.impl = impl;
		return result;
	}

}
