/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
public class TestResult extends Result {
	
	private final Collection<Class<?>> subjectClasses;
	
	/**
	 * Empty set of classes.
	 */
	public TestResult( Collection<Class<?>> subjectClasses ) {
		super( "TESTS: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
		Assert.nonNull( subjectClasses );
		
		this.subjectClasses = subjectClasses;
	}
	
	
	@Override
	public void load() {
		this.subjectClasses.forEach( this::addSubject );
	}
	
	
	public void addSubject( Class<?> subjectClass ) {
		Assert.nonNull( subjectClass );
		
		Container container = Validate.getContainer( subjectClass );
		if ( container != null ) {
			this.addChild( new ClassResult( subjectClass, container ) );
		}
	}

		


	
	
	public static void main( String[] args ) {
		System.out.println( ">>> Hello world!" );
		
		TestResult res = new TestResult( Arrays.asList( TestResult.class ) );
		res.load();
		res.print();
		
		System.out.println( ">>> Done!" );
	}





}
