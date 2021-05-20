/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.StackWalker.Option;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import sog.core.App;
import sog.core.AppException;
import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Procedure;
import sog.core.Test;

/**
 * 
 */
public class TestContainer {
	
	
	public static String eval( Class<?> subjectClass ) {
		Fatal.unimplemented("");
		return null;
	}
	
	

	public static TestContainer get( Class<?> subjectClass ) {
		TestContainer result = new TestContainer();
		
		
		if ( subjectClass.isSynthetic() || subjectClass.getEnclosingClass() != null ) {
			Msg.warning( "Subject class must be a top-level class" ).addDetail( "Subject Class", subjectClass );
			return result;
		}
		
//		Test.Subject subject = subjectClass.getDeclaredAnnotation( Test.Subject.class );
//		if ( subject == null ) {
//			Msg.error( "Missing Test.Subject annotation" ).addDetail( "Subject Class", subjectClass );
//			return result;
//		}
		
		
//		try {
//		Class<?> clazz = testContainer.container();
//		this.container = (TestContainer) clazz.getDeclaredConstructor().newInstance();
//	} catch ( Exception e ) {
//		return Msg.error( "Unable to construct Container", null, e, "Subject = " + this.subjectClass, "Container = " + testContainer );
//	} 
		
		// Construct container
		// add TestCase s
		
		return result;
	}
	
	// Naming policy for member classes
	private static String getSimpleName( Class<?> clazz ) {
		return (clazz.isMemberClass() ? getSimpleName(clazz.getEnclosingClass()) + "." : "") + clazz.getSimpleName();
	}
	
	protected void setSubjectClass( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
	}
	
	// Naming policy for constructors
	private static String getSimpleName( Constructor<?> constructor ) {
		return new StringBuilder()
			.append( getSimpleName( constructor.getDeclaringClass() ) )
			.append( "(" )
			.append( Arrays.stream( constructor.getParameterTypes() )
				.map( TestContainer::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	// Naming policy for member fields
	private static String getSimpleName( Field field ) {
		return new StringBuilder()
			.append( getSimpleName( field.getType() ) )
			.append( " " )
			.append( getSimpleName( field.getDeclaringClass() ) )
			.append( "." )
			.append( field.getName() )
			.toString();
	}
	
	// Naming policy for member methods
	private static String getSimpleName( Method method ) {
		return new StringBuilder()
			.append( getSimpleName( method.getReturnType() ) )
			.append( " " )
			.append( getSimpleName( method.getDeclaringClass() ) )
			.append( "." )
			.append( method.getName() )
			.append( "(" )
			.append( Arrays.stream( method.getParameterTypes() )
				.map( TestContainer::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	

	
	
	private Class<?> subjectClass;
	
	private final Map<String, TestCase> testCases;

	private Test.Container container;

	// Created empty
	private TestContainer() {
		this.testCases = new TreeMap<String, TestCase>();
	}
	
	private void setContainer( Test.Container container ) {
		//this.container = Assert.nonNull( container );
		// load methods -> cases
	}
	
	
	public TestCase getCases( Constructor<?> constructor, Test.Decl decl ) {
		return this.getCases( TestContainer.getSimpleName( constructor ), decl.value() );
	}

	public TestCase getCases( Field field, Test.Decl decl ) {
		return this.getCases( TestContainer.getSimpleName( field ), decl.value() );
	}

	public TestCase getCases( Method method, Test.Decl decl ) {
		return this.getCases( TestContainer.getSimpleName( method ), decl.value() );
	}
	
	private TestCase getCases( String memberName, String description ) {
		// if missing: Stub member, description
		// remove as found, warn remainders
		return null;
	}

}
