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
import sog.core.Test;

/**
 * Represents the results for all tested components of a subject class
 *
 */
public class ClassResult extends Result {

	
	public ClassResult( Class<?> clazz ) {
		super( clazz.getCanonicalName() );
	}
	

	@Test.Decl( "Throws assertion error for null class" )
	public MemberResult addMember( Class<?> c ) {
		Assert.nonNull( c );
		MemberResult result = new MemberResult( c );
		return (MemberResult) this.addChild( result );
	}

	@Test.Decl( "Throws assertion error for null constructor" )
	public MemberResult addMember( Constructor<?> c ) {
		Assert.nonNull( c );
		MemberResult result = new MemberResult( c );
		return (MemberResult) this.addChild( result );
	}

	@Test.Decl( "Throws assertion error for null method" )
	public MemberResult addMember( Method m ) {
		Assert.nonNull( m );
		MemberResult result = new MemberResult( m );
		return (MemberResult) this.addChild( result );
	}

	@Test.Decl( "Throws assertion error for null field" )
	public MemberResult addMember( Field f ) {
		Assert.nonNull( f );
		MemberResult result = new MemberResult( f );
		return (MemberResult) this.addChild( result );
	}
	
	@Override
	@Test.Skip
	public boolean showResults() {
		return Result.SHOW_CLASS_RESULTS && this.getTotalCount() > 0;
	}
	
	
		
}
