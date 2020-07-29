/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sog.core.Procedure;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.test.ClassResult;

/**
 * @author sundquis
 *
 */
public class ClassResultTest implements TestContainer {

	@Override public Class<?> subjectClass() { return ClassResult.class; }

	// Test implementations

	
	private ClassResult instance;

	@Override public Procedure beforeEach() { return () -> this.instance = new ClassResult( Test.class ); }

	@Override public Procedure afterEach() { return () -> this.instance = null; }

	
	@Test.Impl( src = "public MemberResult ClassResult.addMember(Class)", desc = "Throws assertion error for null class" )
	public void addMember_ThrowsAssertionErrorForNullClass( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Class<?> clazz = null;
		instance.addMember( clazz );
	}

	@Test.Impl( src = "public MemberResult ClassResult.addMember(Constructor)", desc = "Throws assertion error for null constructor" )
	public void addMember_ThrowsAssertionErrorForNullConstructor( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> constructor = null;
		instance.addMember( constructor );
	}

	@Test.Impl( src = "public MemberResult ClassResult.addMember(Field)", desc = "Throws assertion error for null field" )
	public void addMember_ThrowsAssertionErrorForNullField( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Field field = null;
		instance.addMember( field );
	}

	@Test.Impl( src = "public MemberResult ClassResult.addMember(Method)", desc = "Throws assertion error for null method" )
	public void addMember_ThrowsAssertionErrorForNullMethod( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Method method = null;
		instance.addMember( method );
	}

	
	
	
	public static void main(String[] args) {

		System.out.println();

		new Test(ClassResultTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
