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
	
	/** Return true if the impl was not previously set */
	public boolean setImpl( TestImpl impl ) {
		boolean result = this.impl == null;
		this.impl = Assert.nonNull( impl );
		return result;
	}

}
