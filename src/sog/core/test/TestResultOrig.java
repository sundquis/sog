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
import sog.core.TestOrig;

/**
 * @author sundquis
 *
 */
@TestOrig.Skip
public class TestResultOrig extends ResultOrig {
	
	public TestResultOrig() {
		super( "TESTS: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
	}
	
	public ClassResultOrig addClass( Class<?> clazz ) {
		Assert.nonNull( clazz );
		ClassResultOrig result = new ClassResultOrig( clazz );
		return (ClassResultOrig) this.addChild( result );
	}
	
	/**
	 * @see sog.core.test.ResultOrig#showResults()
	 */
	@Override
	public boolean showResults() {
		return ResultOrig.SHOW_GLOBAL_RESULTS;
	}
	
}
