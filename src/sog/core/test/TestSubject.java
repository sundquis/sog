/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Test;

/**
 * 
 */
public class TestSubject {
	
	private final Class<?> subjectClass;
	private final TestContainer container;
	
	public TestSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass ); 
		this.container = TestContainer.get( subjectClass );
	}
	
	
	public String evaluate() {
		this.scanSubject();
		this.execTestMethods();

		return this.toString();
	}
	
	private Map<String, Method> containerMethod = null;
	
	// FIXME
	public void loadContainer() {
		if ( Test.Implementation.class.isAssignableFrom( this.subjectClass ) ) {
			Msg.info( "Skipping Container" ).addDetail( "Subject class", this.subjectClass );
		}
		
//		Test.Subject testContainer = this.subjectClass.getDeclaredAnnotation( Test.Subject.class );
//		if ( testContainer == null ) {
//			Msg.error( "No container" ).addDetail( "Subject class", this.subjectClass );
//		}
		
//		try {
//			Class<?> clazz = testContainer.container();
//			this.container = (TestContainer) clazz.getDeclaredConstructor().newInstance();
//		} catch ( Exception e ) {
//			return Msg.error( "Unable to construct Container", null, e, "Subject = " + this.subjectClass, "Container = " + testContainer );
//		} 
	}
	
	public void loadMethods() {
		this.containerMethod = Arrays.stream( this.container.getClass().getDeclaredMethods() )
			.collect( Collectors.toMap( this::methodKey, Function.identity() ) );
	}
	
	private String methodKey( Method method ) {
		Test.Impl impl = method.getDeclaredAnnotation( Test.Impl.class );
		return impl == null ? "NONE" : this.methodKey( impl.member(), impl.description() );
	}
	
	private String methodKey( String member, String description ) {
		return member + "#" + description;
	}
	
	private void scanSubject() {
		this.scanClass( this.subjectClass );
	}
	
	private void scanClass( Class<?> clazz ) {
		Arrays.stream( clazz.getDeclaredConstructors() ).forEach( this::addTestCases );
		Arrays.stream( clazz.getDeclaredFields() ).forEach( this::addTestCases );
		Arrays.stream( clazz.getDeclaredMethods() ).forEach( this::addTestCases );
		
		Arrays.stream( clazz.getDeclaredClasses() ).forEach( this::scanClass );
	}

	private void addTestCases( Constructor<?> constructor ) {
		if ( constructor.isSynthetic() ) return;
		
		addTestCases( 
			getSimpleName( constructor ), 
			constructor.getDeclaredAnnotationsByType( Test.Decl.class ),
			Policy.get().required( constructor ) );
	}
	
	private void addTestCases( Field field ) {
		if ( field.isSynthetic() ) return;
		
		addTestCases( 
			getSimpleName( field ), 
			field.getDeclaredAnnotationsByType( Test.Decl.class ),
			Policy.get().required( field ) );
	}
	
	private void addTestCases( Method method ) {
		if ( method.isSynthetic() || "main".equals( method.getName() ) ) return;
		
		addTestCases( 
			getSimpleName( method ), 
			method.getDeclaredAnnotationsByType( Test.Decl.class ),
			Policy.get().required( method ) );
	}
	
	private void addTestCases( String memberName, Test.Decl[] cases, boolean required ) {
		if ( cases != null && cases.length > 0 ) {
			Arrays.stream( cases ).forEach( c -> this.addTestMethod( memberName, c.value() ) );
		} else {
			if ( required ) {
				Msg.error( "Untested member" )
					.addDetail( "Subject", this.subjectClass )
					.addDetail( "Member", memberName );
			} else {
				Msg.info( "Skipping member" )
					.addDetail( "Subject", this.subjectClass )
					.addDetail( "Member", memberName );
			}
		}
	}
	
	private void addTestMethod( String member, String description ) {
		Method method = this.containerMethod.get( this.methodKey( member, description ) );
		if ( method == null ) {
			Msg.stub( member, description );
		} else {
			Fatal.unimplemented("Enque test method");
		}
	}
	
	// TODO
	// define class that accepts testMethod and implements Test, enqueue
	// execMethods runs the queue then tabulates results
	// check for orphaned test methods
	// toString reports results
	// check on static/instance for helper methods
			
	// Naming policy for member classes
	private static String getSimpleName( Class<?> clazz ) {
		return (clazz.isMemberClass() ? getSimpleName(clazz.getEnclosingClass()) + "." : "") + clazz.getSimpleName();
	}
	
	// Naming policy for constructors
	private static String getSimpleName( Constructor<?> constructor ) {
		return new StringBuilder()
			.append( getSimpleName( constructor.getDeclaringClass() ) )
			.append( "(" )
			.append( Arrays.stream( constructor.getParameterTypes() )
				.map( TestSubject::getSimpleName )
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
				.map( TestSubject::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	public void execTestMethods() {
		Fatal.unimplemented("");
	}
	
	@Override
	public String toString() {
		return "UNIMPLEMENTED";
	}
	


}
