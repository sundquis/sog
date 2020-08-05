/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Method;

import sog.core.Assert;

/**
 * 
 */
public class CaseResult extends Result {
	
	private final Container container;
	private final Method method;

	public CaseResult( String description, Container container, Method method ) {
		super( Assert.nonNull( description ) );
		
		this.container = container;
		this.method = method;
	}
	
	public void load() {
		// execute
System.out.println( ">>> Executing: " + method );
	}

}
