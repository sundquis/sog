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
//@Test.Skip( "Foo" )
@Test.Container( "test.core.test.TestResult" )
public class TestResult extends Result {
	
	private static String defaultName() {
		return "TEST: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() );
	}
	
	
	
	private final Collection<Class<?>> subjectClasses;
	
	public TestResult( String name, Collection<Class<?>> subjectClasses ) {
		super( name );
		this.subjectClasses = Assert.nonNull( subjectClasses );
	}
	
	public TestResult( Collection<Class<?>> subjectClasses ) {
		this( TestResult.defaultName(), subjectClasses );
	}
	
	@Override
	public void load() {
		this.subjectClasses.forEach( this::addSubject );
	}
	
	
	public void addSubject( Class<?> subjectClass ) {
		Assert.nonNull( subjectClass );
		
		if ( subjectClass.getEnclosingClass() != null ) {
			Msg.error( "Subject must be a top-level class", "Subject = " + subjectClass.getName() );
			return;
		}
		
		if ( Container.class.isAssignableFrom( subjectClass ) ) {
			Msg.error( "Subject cannot be a container", "Subject = " + subjectClass.getName() );
			return;
		}
		
		Test.Container testContainer = subjectClass.getDeclaredAnnotation( Test.Container.class );
		Test.Skip testSkip = subjectClass.getDeclaredAnnotation( Test.Skip.class );
		boolean test = testContainer != null;
		boolean skip = testSkip != null;
		
		if ( test && skip ) {
			Msg.error( "Inconsistent meta-data", "Subject = " + subjectClass.getName(),
				"Container class = " + testContainer.value(), "Skip = " + testSkip.value() );
			return;
		}
		
		if ( !test && !skip ) {
			Msg.error( "No container declared for subject", "Subject = " + subjectClass.getName() );
			return;
		}
		
		if ( !test && skip ) {
			Msg.warning( "Skipping:", "Subject = " + subjectClass.toString(), "Skip = " + testSkip.value() );
			return;
		}

		// ELSE: test && !skip
		Container container = null;
		try {
			Class<?> clazz = Class.forName( testContainer.value() );
			container = (Container) clazz.getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			Msg.error( e, "Unable to construct Container", "Container = " + testContainer.value() );
			return;
		}
		
		if ( !subjectClass.equals( container.subjectClass() ) ) {
			Msg.error( "Bad container", "Subject class = " + subjectClass.getName(), 
				"Container class = " + container.subjectClass().getName() );
			return;
		}
		
		this.addChild( new ClassResult( subjectClass, container ) );
	}


		

	
	public static String stringField = "foo";

	
	private static class Inner extends Container {
		@Override
		public Class<?> subjectClass() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	@Test.Skip( "Err: Skip this public member class" )
	public static class SkipPublic {}
	
	@Test.Skip( "Warn: Skip this protected member class" )
	protected static class SkipProtected {}
	
	@Test.Skip( "Warn: Skip this package member class" )
	static class SkipPackage {}
	
	@Test.Skip( "Ignore: Should not see this message" )
	private static class SkipPrivate {}
	
	@Test.Case( "The description for the foo method")
	private static void foo( String s ) {}

	@Test.Impl( member = "METHOD: private static void sog.core.test.TestResult.foo(java.lang.String)", description = "The description for the foo method" )
	public void XXX( Test test ) {
	test.addMessage( "UNIMPLEMENTED" ).fail();
	}

	
	public static void main( String[] args ) {
		System.out.println( ">>> Hello world!" );
		
		TestResult res = new TestResult( Arrays.asList( Inner.class, Container.class, TestResult.class ) );
		res.load();
		res.print();
		Msg.print();
		
		System.out.println( ">>> Done!" );
	}








}
