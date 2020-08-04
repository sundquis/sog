/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.StackWalker.Option;
import java.lang.reflect.Field;
import java.util.function.Predicate;

import sog.core.AppException;
import sog.core.Procedure;

/**
 * 
 */
public abstract class Container {
	
	/**
	 * All concrete containers must define a no-arg constructor.
	 */
	protected Container() {}
	
	/**
	 * The target class for which the container holds test cases
	 */
	public abstract Class<?> subjectClass();

	
	
	/**
	 * The procedure to be called before any method invocation occurs.
	 */
	protected Procedure beforeAll() {
		return Procedure.NOOP;
	}
	
	
	/**
	 * The procedure to be called before each method invocation
	 */
	protected Procedure beforeEach() {
		return Procedure.NOOP;
	}
	

	
	/**
	 * The procedure to be called after each method invocation
	 */
	protected Procedure afterEach() {
		return Procedure.NOOP;
	}

	
	/**
	 * The procedure to be called after all method invocations have completed
	 */
	protected Procedure afterAll() {
		return Procedure.NOOP;
	}

	protected void setSubjectField( Object subject, String  fieldName, Object fieldValue ) {
		try {
			Field field = subjectClass().getDeclaredField( fieldName );
			field.setAccessible( true );
			field.set( subject, fieldValue );
		} catch ( Exception e ) {
			throw new AppException( e );
		} 
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getSubjectField( Object subject, String fieldName, T witness ) {
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
	
	
	protected String getFileLocation() {
		Predicate<StackWalker.StackFrame> sfp = sf -> 
			Container.class.isAssignableFrom( sf.getDeclaringClass() ) 
			&& !sf.getDeclaringClass().equals( Container.class );
		StackWalker.StackFrame sf = StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
			s -> s.filter( sfp ).findFirst().get()
		);
		
		return sf == null ? "UNKNOWN" : "(" + sf.getFileName() + ":" + sf.getLineNumber() + ")";
	}
	
}
