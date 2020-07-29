/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import sog.core.Assert;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
@Test.Skip
public class TestResult extends Result {
	
	public TestResult() {
		super( "TESTS: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
	}
	
	public ClassResult addClass( Class<?> clazz ) {
		Assert.nonNull( clazz );
		ClassResult result = new ClassResult( clazz );
		return (ClassResult) this.addChild( result );
	}
	
	/**
	 * @see sog.core.test.Result#showResults()
	 */
	@Override
	public boolean showResults() {
		return Result.SHOW_GLOBAL_RESULTS;
	}
	
}
