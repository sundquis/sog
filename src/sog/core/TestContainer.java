/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.lang.reflect.Field;

/**
 * 
 * Classes that contain test cases for a subject class implement this interface.
 * 
 */
@TestOrig.Skip
public interface TestContainer {
	
	/**
	 * The target class for which the container holds test cases
	 */
	public Class<?> subjectClass();

	
	
	/**
	 * The procedure to be called before any method invocation occurs.
	 */
	default public Procedure beforeAll() {
		return Procedure.NOOP;
	}
	
	
	/**
	 * The procedure to be called before each method invocation
	 */
	default public Procedure beforeEach() {
		return Procedure.NOOP;
	}
	

	
	/**
	 * The procedure to be called after each method invocation
	 */
	default public Procedure afterEach() {
		return Procedure.NOOP;
	}

	
	/**
	 * The procedure to be called after all method invocations have completed
	 */
	default public Procedure afterAll() {
		return Procedure.NOOP;
	}
	
	default public void setSubjectField( Object subject, String  fieldName, Object fieldValue ) {
		try {
			Field field = subjectClass().getDeclaredField( fieldName );
			field.setAccessible( true );
			field.set( subject, fieldValue );
		} catch ( Exception e ) {
			throw new AppException( e );
		} 
	}
	
	@SuppressWarnings("unchecked")
	default public <T> T getSubjectField( Object subject, String fieldName, T witness ) {
		T result = null;
		try {
			Field field = subjectClass().getDeclaredField( fieldName );
			field.setAccessible( true );
			result = (T) field.get( subject );
		} catch ( Exception e ) {
			throw new AppException( e );
		} 
		return result;
	}
	
	
	@TestOrig.Skip( "May need testing. Appears correct" )
	public static String location() {
		StackTraceElement[] stes = (new Exception()).getStackTrace();
		int index = 0;
		int line = -1;
		String fileName = null;
		while ( fileName == null && index < stes.length ) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName( stes[index].getClassName() );
			} catch (ClassNotFoundException e) {
				return null;
			}
			if ( TestContainer.class.isAssignableFrom( clazz ) && ! clazz.equals( TestContainer.class ) ) {
				line = stes[index].getLineNumber();
				fileName = stes[index].getFileName();
			} else {
				index++;
			}
		}
		
		return fileName == null ? null : "(" + fileName + ":" + line + ")";
	}

	
	
}