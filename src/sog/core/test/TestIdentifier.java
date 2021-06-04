/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Test;

/**
 * In the context of a single subject class, the member name and test description must
 * uniquely identify a single test case. These strings are used to form a
 * unique key for mapping.
 */
public interface TestIdentifier {
	
	public String getMemberName();
	
	public String getDescription();
		
	@Test.Decl( "FIXME" )
	public default String getKey() {
		return this.getMemberName() + "#" + this.getDescription();
	}

}
