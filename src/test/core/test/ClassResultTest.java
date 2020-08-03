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
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.test.ClassResultOrig;

/**
 * @author sundquis
 *
 */
public class ClassResultTest implements TestContainer {

	@Override public Class<?> subjectClass() { return ClassResultOrig.class; }

	// Test implementations

	
	private ClassResultOrig instance;

	@Override public Procedure beforeEach() { return () -> this.instance = new ClassResultOrig( TestOrig.class ); }

	@Override public Procedure afterEach() { return () -> this.instance = null; }

	
	@TestOrig.Impl( src = "public MemberResult ClassResult.addMember(Class)", desc = "Throws assertion error for null class" )
	public void addMember_ThrowsAssertionErrorForNullClass( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Class<?> clazz = null;
		instance.addMember( clazz );
	}

	@TestOrig.Impl( src = "public MemberResult ClassResult.addMember(Constructor)", desc = "Throws assertion error for null constructor" )
	public void addMember_ThrowsAssertionErrorForNullConstructor( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> constructor = null;
		instance.addMember( constructor );
	}

	@TestOrig.Impl( src = "public MemberResult ClassResult.addMember(Field)", desc = "Throws assertion error for null field" )
	public void addMember_ThrowsAssertionErrorForNullField( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Field field = null;
		instance.addMember( field );
	}

	@TestOrig.Impl( src = "public MemberResult ClassResult.addMember(Method)", desc = "Throws assertion error for null method" )
	public void addMember_ThrowsAssertionErrorForNullMethod( TestCase tc ) {
		tc.expectError( AssertionError.class );
		Method method = null;
		instance.addMember( method );
	}

	
	
	
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(ClassResultTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
