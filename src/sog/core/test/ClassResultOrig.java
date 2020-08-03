/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sog.core.Assert;
import sog.core.TestOrig;

/**
 * Represents the results for all tested components of a subject class
 *
 */
public class ClassResultOrig extends ResultOrig {

	
	public ClassResultOrig( Class<?> clazz ) {
		super( clazz.getCanonicalName() );
	}
	

	@TestOrig.Decl( "Throws assertion error for null class" )
	public MemberResultOrig addMember( Class<?> c ) {
		Assert.nonNull( c );
		MemberResultOrig result = new MemberResultOrig( c );
		return (MemberResultOrig) this.addChild( result );
	}

	@TestOrig.Decl( "Throws assertion error for null constructor" )
	public MemberResultOrig addMember( Constructor<?> c ) {
		Assert.nonNull( c );
		MemberResultOrig result = new MemberResultOrig( c );
		return (MemberResultOrig) this.addChild( result );
	}

	@TestOrig.Decl( "Throws assertion error for null method" )
	public MemberResultOrig addMember( Method m ) {
		Assert.nonNull( m );
		MemberResultOrig result = new MemberResultOrig( m );
		return (MemberResultOrig) this.addChild( result );
	}

	@TestOrig.Decl( "Throws assertion error for null field" )
	public MemberResultOrig addMember( Field f ) {
		Assert.nonNull( f );
		MemberResultOrig result = new MemberResultOrig( f );
		return (MemberResultOrig) this.addChild( result );
	}
	
	@Override
	@TestOrig.Skip
	public boolean showResults() {
		return ResultOrig.SHOW_CLASS_RESULTS && this.getTotalCount() > 0;
	}
	
	
		
}
