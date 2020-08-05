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

		


	
	
	// FIXME: Convert to proper testing
	@Test.Case( "1" )
	@Test.Case( "2" )
	@Test.Case( "3" )
	public static void fooStatic() {}
	public static int fooIntStatic() { return 42; }
	
	@Test.Case( "1" )
	public static int fooIntStatic(int x) { return x; }
	
	public void bar() {}
	public String label;
	
	public class Inner {
		public void bar() {}
		protected int ft;
	}
	
	public static class MyContainer extends Container {

		public Class<?> subjectClass() {
			return TestResult.class;
		}
		
		@Test.Impl( description = "The description", member = "Made_up_member_name" )
		public void foo() {}
		
	}
	
	public static void main( String[] args ) {
		System.out.println( ">>> Hello world!" );
		
		TestResult res = new TestResult( Arrays.asList( TestResult.class, Container.class ) );
		res.print();
		
		System.out.println( ">>> Done!" );
	}





}
