/**
 * Copyright (C) 2017, 2018, 2019, 2020, 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.lang.annotation.Repeatable;
import java.lang.StackWalker.Option;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.Predicate;

import sog.core.test.TestContainer;

/**
 * 	SUBJECT
 * 		A class that is the subject of one or more tests.
 * 		A class identifies as a subject using Test.Subject, which indicates the test container.
 * 		Container name conventions:
 * 			.MemberName		Search for a member class Test.Container
 * 			packages.		Prepend packages to full subject class name
 * 			ClassName		Look in the same package for ClassName
 * 			Otherwise		Name is a fully qualified class name
 * 		Declares test cases using Test.Decl on members
 * 		Test declarations serve as additional documentation on behavior
 * 		Policy specifies which members should have cases declared
 * 		A class may also use Test.Skip to declare no testing is required
 * 
 * 	CONTAINER
 * 		A class that holds test method implementations
 * 		Extends the abstract base class Test.Container
 * 		Specified by the subjects' Test.Subject annotation
 * 		Framework generates stubs for test methods and marks with Test.Impl annotations
 * 		Implementations use Test.Case interface
 * 
 * 	CASE
 * 		Each test method implementation gets an instance of Test.Case to record results
 * 		Implemented by TestCase
 * 
 * 	RESULT
 * 		Test.Result aggregates the results of all test cases for an individual class
 * 		Implemented by TestResult
 * 		Creates TestCase instances
 * 
 */
public class Test {
	
	
	/**
	 * Classes and/or members can be marked as not requiring testing.
	 * The test policy is not applied, and no messages are generated.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD } )
	@Documented
	public @interface Skip {
		/** Optional description, for example explanation of alternate testing */
		String value() default "No testing required.";
	}

	
	
	/**
	 * Subject classes use to identify as a class needing testing.
	 * The value identifies the location of the 
	 * Test.Container that holds test method implementations.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.TYPE )
	@Documented
	public @interface Subject {
		/**
		 * The value determines the location according to the following rules:
		 *   1. If the string starts with a dot (.Member) it is treated as the name of a member class Test.Container
		 *   2. If the string ends with a dot (package.) it is prepended to the subject full classname
		 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
		 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container
		 */
		String value(); 
	}

	/**
	 * Annotate class members (constructor, field, method)
	 * with {@code Test.Decl} to declare a test case pertaining to the element.
	 */
	@Repeatable( Test.Decls.class )
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD } )
	@Documented
	public @interface Decl {
		/**
		 * A description of the test. 
		 * 
		 * Members may have multiple {@code Test.Decl} annotations but the descriptions for each member's 
		 * tests must be unique. Descriptions serve to further document the member's properties,
		 * but descriptions should be brief. 
		 */
		String value();
	}
	
	
	/**
	 * Container for repeated {@code Test.Decl} annotations.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD } )
	@Documented
	public @interface Decls {
		Decl[] value();
	}



	
	/**
	 * Container classes that hold test method implementations extend this abstract base class.
	 */
	public static abstract class Container {
		
		protected Container() {}
		
		/** The named class must be annotated as a subject naming this Test.Container */
		protected abstract Class<?> getSubjectClass();
		
		/** The procedure to be called before any method invocation occurs. */
		public Procedure beforeAll() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called before each method invocation. */
		public Procedure beforeEach() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called after each method invocation. */
		public Procedure afterEach() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called after all method invocations have completed. */
		public Procedure afterAll() {
			return Procedure.NOOP;
		}

		/** Set the value of a subject's field. */
		public void setSubjectField( Object subject, String fieldName, Object fieldValue ) {
			try {
				Field field = this.getSubjectClass().getDeclaredField( fieldName );
				field.setAccessible( true );
				field.set( subject, fieldValue );
			} catch ( Exception e ) {
				throw new AppException( e );
			} 
		}

		/** Retrieve the value of a subject's field. */
		@SuppressWarnings("unchecked")
		public <T> T getSubjectField( Object subject, String fieldName, T witness ) {
			T result = null;
			try {
				Field field = this.getSubjectClass().getDeclaredField( fieldName );
				field.setAccessible( true );
				result = (T) field.get( subject );
			} catch ( Exception e ) {
				throw new AppException( e );
			} 
			return result;
		}
		
		/** Calling location in a concrete Container. */
		public static String getFileLocation() {
			Predicate<StackWalker.StackFrame> sfp = sf -> 
				Test.Container.class.isAssignableFrom( sf.getDeclaringClass() ) 
				&& !sf.getDeclaringClass().equals( Test.Container.class );
			StackWalker.StackFrame sf = StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
				s -> s.filter( sfp ).findFirst().orElse( null )
			);
			
			return sf == null ? "UNKNOWN" : new App.Location( sf ).toString();
		}
		
	}
	
	/**
	 * Marker for test method implementations. These are generated by the test framework and generally
	 * should not be hand coded. In particular the {@code member} and {@code description} fields are 
	 * generated to correspond to a test declaration.
	 * 
	 * The optional numeric fields may be included to adjust features of the test.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.METHOD )
	public @interface Impl {

		/** Do not edit. Must match the corresponding declaration */
		String member();
		
		/** Do not edit. Must match the corresponding declaration */
		String description();
		
		/** Used to determine order that a class's test cases should be executed */
		int priority() default 0;

		/** CURRENTLY NOT USED */
		long timeout() default 0L;
		
		/** Used to scale the relative importance of the test case */
		int weight() default 1;
		
	}
	
	
	public interface Case {
		
		/**
		 * Only displayed for non-passing cases
		 * 
		 * @param message
		 * 		Failure message
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case addMessage( String message );


		/**
		 * Type of Throwable that subsequent code is expected to throw
		 * 
		 * @param expectedError
		 * 		Throwable tpye
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case expectError( Class<? extends Throwable> expectedError );


		/**
		 * Procedure to call after the current method (in a Test.Container) completes
		 * Will be called even if the method throws an exception.
		 * 
		 * @param callafter
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case afterThis( Procedure callafter );

		
		/**
		 * Mark the current case as failed.
		 * 
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case fail();
		

		/**
		 * Mark the current case as passed.
		 * 
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case pass();
		

		/**
		 * Assert that the given object is not null.
		 * 
		 * @param obj
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case notNull( Object obj );
		
		/**
		 * Assert that the given string is non-null and not empty
		 * 
		 * @param s
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case notEmpty( String s );
		
		
		/**
		 * Assert that the given object is not null.
		 * 
		 * @param obj
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case isNull( Object obj );
		
		
		/**
		 * Conditionally mark the case as passed/failed.
		 *  
		 * @param passIfTrue
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertTrue( boolean passIfTrue );
		
		
		/**
		 * Conditionally mark the case as passed/failed.
		 *  
		 * @param passIfFalse
		 * @return
		 * 		this TestCase
		 */
		public Test.Case assertFalse( boolean passIfFalse );
		
		
		/**
		 * Test for equality using Object.equals().
		 * If T is a compound type (array or collection) then the components are shallowly tested.
		 * 
		 * @param expected
		 * @param actual
		 * @return
		 * 		this Test.Case
		 */
		public <T> Test.Case assertEqual( T expected, T actual );
		
	}
	
	
	

	public static String eval( Class<?> subjectClass ) {
		return TestContainer.eval( subjectClass );
	}
	
	// FIXME: Needs testing
	public static String eval() {
		return Test.eval( App.get().getCallingClass( 2 ) );
	}
	
}
