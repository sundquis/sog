/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import sog.core.Assert;

/**
 * 
 */
public class CaseResult extends Result {

	public CaseResult( String description ) {
		super( Assert.nonNull( description ) );
	}

}
