/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
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
public class TestImpl implements TestIdentifier {

	
	private final Method method;
	
	private final Test.Impl impl;

	public TestImpl( Method method ) {
		this.method = Assert.nonNull( method );
		this.impl = method.getDeclaredAnnotation( Test.Impl.class );
	}
	
	public boolean hasImpl() {
		return this.impl != null;
	}
	
	
	
	
	public Method getMethod() {
		return this.method;
	}
	
	@Override
	public String toString() {
		return this.getMemberName() + " # " + this.getDescription();
	}

	@Override
	public String getMemberName() {
		return this.impl.member();
	}

	@Override
	public String getDescription() {
		return this.impl.description();
	}
	
	public int getPriority() {
		return this.impl.priority();
	}
	
	public int getWeight() {
		return this.impl.weight();
	}

}
