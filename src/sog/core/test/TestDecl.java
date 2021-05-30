/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import sog.core.Test;

/**
 * 
 */
public class TestDecl {

	
	// Naming policy for member classes
	private static String getSimpleName( Class<?> clazz ) {
		return (clazz.isMemberClass() ? getSimpleName(clazz.getEnclosingClass()) + "." : "") + clazz.getSimpleName();
	}
	
	// Naming policy for constructors
	private static String getSimpleName( Constructor<?> constructor ) {
		return new StringBuilder()
			.append( "constructor: " )
			.append( getSimpleName( constructor.getDeclaringClass() ) )
			.append( "(" )
			.append( Arrays.stream( constructor.getParameterTypes() )
				.map( TestDecl::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	// Naming policy for member fields
	private static String getSimpleName( Field field ) {
		return new StringBuilder()
			.append( "field: " )
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
			.append( "method: " )
			.append( getSimpleName( method.getReturnType() ) )
			.append( " " )
			.append( getSimpleName( method.getDeclaringClass() ) )
			.append( "." )
			.append( method.getName() )
			.append( "(" )
			.append( Arrays.stream( method.getParameterTypes() )
				.map( TestDecl::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	
	
	
	private final String memberName;
	private final Member member;
	private final Test.Skip skip;
	private final Test.Decl[] decls;
	private boolean isRequired;
	
	private TestDecl( String memberName, Member member, AnnotatedElement element, boolean isRequired ) {
		this.memberName = memberName;
		this.member = member;
		this.skip = element.getDeclaredAnnotation( Test.Skip.class );
		this.decls = element.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = isRequired;
	}
	
	public TestDecl( Constructor<?> constructor ) {
		this( TestDecl.getSimpleName( constructor ), constructor, constructor, Policy.get().required( constructor ) );
	}
	
	public TestDecl( Field field ) {
		this( TestDecl.getSimpleName( field ), field, field, Policy.get().required( field ) );
	}
	
	public TestDecl( Method method ) {
		this( TestDecl.getSimpleName( method ), method, method, Policy.get().required( method ) );
	}
	
		
	
	public boolean isSkipped() { 
		return this.skip != null || this.member.isSynthetic();
	}
	
	public String getSkipReason() { 
		return (this.skip != null) ? this.skip.value() : "Synthetic";
	}
	
	public boolean isRequired() { 
		return this.isRequired; 
	}
	
	public boolean hasDecls() { 
		return this.decls.length > 0; 
	}
	
	public Test.Decl[] getDecls() { 
		return this.decls; 
	}
	

	
	
	@Override
	public String toString() {
		return this.memberName;
	}

}
