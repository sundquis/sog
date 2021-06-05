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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Test;

/**
 * 
 */
public class TestMember {

	
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
				.map( TestMember::getSimpleName )
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
				.map( TestMember::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	
	
	
	private final String memberName;
	private final String subject;
	private final Test.Skip skip;
	private final Test.Decl[] decls;
	private final boolean isSynthetic;
	private final boolean isMain;
	private final boolean isRequired;
	private final boolean isAbstract;
	
	private TestMember( String memberName, String subject, AnnotatedElement element, 
			boolean isSynthetic, boolean isRequired ) {
		this( memberName, subject, element, isSynthetic, false, isRequired, false );
	}
	
	private TestMember( String memberName, String subject, AnnotatedElement element, 
			boolean isSynthetic, boolean isMain, boolean isRequired, boolean isAbstract ) {
		this.memberName = memberName;
		this.subject = subject;
		this.skip = element.getDeclaredAnnotation( Test.Skip.class );
		this.decls = element.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isSynthetic = isSynthetic;
		this.isMain = isMain;
		this.isRequired = isRequired;
		this.isAbstract = isAbstract;
	}
	
	public TestMember( Constructor<?> constructor ) {
		this( TestMember.getSimpleName( constructor ), constructor.getDeclaringClass().getSimpleName(), 
				constructor, constructor.isSynthetic(), Policy.get().required( constructor ) );
	}
	
	public TestMember( Field field ) {
		this( TestMember.getSimpleName( field ), field.getName(), 
				field, field.isSynthetic(), Policy.get().required( field ) );
	}
	
	public TestMember( Method method ) {
		this( TestMember.getSimpleName( method ), method.getName(),
			method, method.isSynthetic(), "main".equals( method.getName() ), 
			Policy.get().required( method ), Modifier.isAbstract( method.getModifiers() ) );
	}
	
		
	
	public boolean isSkipped() { 
		return this.skip != null || this.isSynthetic || this.isMain || this.isAbstract;
	}
	
	public String getSkipReason() { 
		return this.skip != null ? this.skip.value() : 
			this.isSynthetic ? "Synthetic" : 
				this.isMain ? "main() method" : "Abstract method";
	}
	
	public boolean isRequired() { 
		return this.isRequired; 
	}
	
	public boolean hasDecls() { 
		return this.decls.length > 0; 
	}
	
	public Stream<TestDecl> getDecls() { 
		return Arrays.stream( this.decls ).map( d -> new TestDecl( TestMember.this.memberName, TestMember.this.subject, d.value() ) );
	}
	

	
	
	@Override
	public String toString() {
		return this.memberName;
	}

}
