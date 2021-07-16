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
 * In the context of a single subject class, the member name and test description must
 * uniquely identify a single test case. These strings are used to form a
 * unique key for mapping.
 */
@Test.Subject( ".test" )
public abstract class TestIdentifier {
	
	private final String memberName;
	private final String description;
	
	protected TestIdentifier( String memberName, String description ) {
		this.memberName = Assert.nonEmpty( memberName );
		this.description = Assert.nonEmpty( description );
	}
	
	final public String getMemberName() {
		return this.memberName;
	}
	
	final public String getDescription() {
		return this.description;
	}
	
	final public String getKey() {
		return this.getMemberName() + " # " + this.getDescription();
	}
	
	@Override
	public String toString() {
		return this.getKey();
	}

	final public String getMethodName() {
		return "M_" + this.getKey().hashCode();
	}
		
}
