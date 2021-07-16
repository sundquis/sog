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
public class TestImpl extends TestIdentifier {
	
	
	public static TestImpl forMethod( Method method ) {
		TestImpl result = null;
		
		Test.Impl impl = method.getDeclaredAnnotation( Test.Impl.class );
		if ( impl != null ) {
			result = new TestImpl( impl.member(), impl.description(), method, 
				impl.priority(), impl.weight(), impl.timeout() );
		}
		
		return result;
	}

	
	private final Method method;
	
	private final int priority;
	
	private final int weight;

	@SuppressWarnings( "unused" )
	private final long timeout;
	
	private TestImpl( String memberName, String description, Method method, int priority, int weight, long timeout ) {
		super( memberName, description );

		this.method = Assert.nonNull( method );
		this.priority = priority;
		this.weight = weight;
		this.timeout = timeout;
	}
	
	
	
	public Method getMethod() {
		return this.method;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public int getWeight() {
		return this.weight;
	}

}
