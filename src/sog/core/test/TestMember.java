/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
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
	private final boolean isRequired;
	private final boolean isSynthetic;
	private final boolean isMain;
	private final boolean isAbstract;
	private final boolean isEnumSpecial;
	
	public TestMember( Constructor<?> constructor ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( constructor ) );
		this.subject = constructor.getDeclaringClass().getSimpleName();
		this.skip = constructor.getDeclaredAnnotation( Test.Skip.class );
		this.decls = constructor.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( constructor );
		this.isSynthetic = constructor.isSynthetic();
		this.isMain= false;
		this.isAbstract = false;
		this.isEnumSpecial = this.isEnumConstructor( constructor );
	}
	
	private boolean isEnumConstructor( Constructor<?> constructor ) {
		return Enum.class.isAssignableFrom( constructor.getDeclaringClass() )
			&& constructor.getParameterCount() == 2
			&& String.class.equals( constructor.getParameterTypes()[0] )
			&& int.class.equals( constructor.getParameterTypes()[1] );
	}
	
	public TestMember( Field field ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( field ) );
		this.subject = field.getName();
		this.skip = field.getDeclaredAnnotation( Test.Skip.class );
		this.decls = field.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( field );
		this.isSynthetic = field.isSynthetic();
		this.isMain= false;
		this.isAbstract = false;
		this.isEnumSpecial = false;
	}
	
	public TestMember( Method method ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( method ) );
		this.subject = method.getName();
		this.skip = method.getDeclaredAnnotation( Test.Skip.class );
		this.decls = method.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( method );
		this.isSynthetic = method.isSynthetic();
		this.isMain= "main".equals( method.getName() );
		this.isAbstract = Modifier.isAbstract( method.getModifiers() );
		this.isEnumSpecial = this.isEnumValues( method ) || this.isEnumValueOf( method );
	}
	
	private boolean isEnumValueOf( Method method ) {
		return Enum.class.isAssignableFrom( method.getDeclaringClass() )
			&& "valueOf".equals(  method.getName() ) 
			&& method.getParameterCount() == 1
			&& String.class.equals( method.getParameterTypes()[0] );
	}
	
	private boolean isEnumValues( Method method ) {
		return Enum.class.isAssignableFrom( method.getDeclaringClass() )
			&& "values".equals(  method.getName() ) 
			&& method.getParameterCount() == 0;
	}
	
		
	
	public boolean isSkipped() { 
		return this.skip != null || this.isSynthetic || this.isMain || this.isAbstract || this.isEnumSpecial;
	}
	
	public String getSkipReason() { 
		return this.skip != null ? this.skip.value() : 
			this.isSynthetic ? "Synthetic" : 
				this.isMain ? "main() method" : 
					this.isAbstract ? "Abstract method" : "Enum Special";
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
