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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
				.map( TestMember::getSimpleName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	private static boolean isSkipped( Member m, AnnotatedElement e ) {
		Test.Skip skip = e.getDeclaredAnnotation( Test.Skip.class );		
		return skip != null || m.isSynthetic() || "main".equals( m.getName() );
	}
	
	private static String skipReason( Member m, AnnotatedElement e ) {
		Test.Skip skip = e.getDeclaredAnnotation( Test.Skip.class );				
		return (skip != null) ? skip.value() :
			m.isSynthetic() ? "Synthetic" :
				"main".equals(  m.getName() ) ? "main" : "";
	}

	
	public static TestMember build( Constructor<?> constructor ) {
		return new TestMember( 
			TestMember.getSimpleName( constructor ),
			TestMember.isSkipped( constructor, constructor ),
			TestMember.skipReason( constructor, constructor ),
			Policy.get().required( constructor ),
			constructor.getDeclaredAnnotationsByType( Test.Decl.class )
		);
	}

	
	public static TestMember build( Field field ) {
		return new TestMember( 
			TestMember.getSimpleName( field ),
			TestMember.isSkipped( field, field ),
			TestMember.skipReason( field, field ),
			Policy.get().required( field ),
			field.getDeclaredAnnotationsByType( Test.Decl.class )
		);
	}

	
	public static TestMember build( Method method ) {
		return new TestMember( 
			TestMember.getSimpleName( method ),
			TestMember.isSkipped( method, method ),
			TestMember.skipReason( method, method ),
			Policy.get().required( method ),
			method.getDeclaredAnnotationsByType( Test.Decl.class )
		);
	}

	
	
	
	private final String name;
	private final boolean isSkipped;
	private final String skipReason;
	private final boolean isRequired;
	private final Test.Decl[] decls;
	
	private TestMember( String name, boolean isSkipped, String skipReason, boolean isRequired, Test.Decl[] decls ) {
		this.name = name;
		this.isSkipped = isSkipped;
		this.skipReason = skipReason;
		this.isRequired = isRequired;
		this.decls = decls;
	}
	
		
	
	public boolean isSkipped() { return this.isSkipped; }
	
	public String getSkipReason() { return this.skipReason; }
	
	public boolean isRequired() { return this.isRequired; }
	
	public boolean hasDecls() { return this.decls.length > 0; }
	
	public Test.Decl[] getDecls() { return this.decls; }
	

	
	
	@Override
	public String toString() { return this.name; }

}
