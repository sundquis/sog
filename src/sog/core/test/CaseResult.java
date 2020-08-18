/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Method;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
public class CaseResult extends Result {
	
	private final Test.Case tc;
	private final Container container;
	private final Method method;
	
	private boolean pass = true;
	private long time = 0L;

	public CaseResult( Test.Case tc, Container container, Method method ) {
		super( Assert.nonEmpty( tc.value() ) );
		
		this.tc = tc;
		this.container = Assert.nonNull( container );
		this.method = method;
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
	}

	

}
