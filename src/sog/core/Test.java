/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
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

import sog.util.IndentWriter;
import sog.core.test.TestResult;
import sog.core.test.TestResultSet;

/**
 * All classes import Test as the single public access to the testing framework.
 * 
 * Resources fall into two categories. Classes intending to be tested, "subject classes", use annotations to
 * declare test cases. The framework generates method stubs for each declared test case, which are
 * implemented in a "container class." 
 * 
 * 
 *	SUBJECT: A class that is the subject of one or more tests.
 * 
 * 		@Test.Subject( container )
 * 			A class identifies itself as a subject by using the Test.Subject annotation, 
 * 			which indicates the test container class using the following naming conventions:
 * 				.MemberName		Search for a member Test.Container class in the subject.
 * 				packagePrefix.	Prepend packagePrefix to the full subject class name and append "Test".
 * 				ClassName		Look in the same package as the subject for ClassName.
 * 				Otherwise		The name, which must contain a ".", is a fully qualified class name.
 * 			Each valid subject is represented by a sog.core.test.TestResult instance.
 * 			Members in a subject are represented by sog.core.test.TestMember instances.
 * 
 * 		@Test.Skip( rationale )
 * 			A subject may declare itself or any of its members as being exempt from testing by using 
 * 			this annotation. The given rationale should explain why the member is not being tested.
 * 			Certain members (such as the "public static void main" method, synthetic members, and 
 * 			Enum special methods) are automatically skipped. Non-skipped members generate testing 
 * 			obligations as specified by the sog.core.test.Policy.
 * 
 * 		@Test.Decl( description )
 * 			A subject class declares test cases using Test.Decl annotations on members. The declaration
 * 			gives a description which serves as additional documentation of behavior and properties
 * 			associated with the member. Each Test.Decl annotation is represented by an
 * 			sog.core.test.TestDecl instance.
 * 
 * 		@Test.Decls
 * 			The required container annotation allowing declaration annotations to be repeated.
 * 
 * 		public static void eval()
 * 			A subject can use this entry point to evaluate its own test cases. The results are 
 * 			automatically printed.
 * 				<code>public static void main( String[] args ) { Test.eval(); }</code>
 * 
 * 
 * 
 *	CONTAINER: A class that holds test method implementations for a specified subject class.
 * 
 * 		class Test.Container
 * 			The abstract base class for all concrete container classes. The constructor requires
 * 			the Class object for the corresponding subject. The framework checks for consistency
 * 			between the subject class given in the constructor and the subject's identification
 * 			of container in its Test.Subject annotation. The base class provides default
 * 			noop implementations of the procedures run before and after each test case. Concrete
 * 			containers override these as necessary. In addition, the base class provides convenience
 * 			methods for getting and setting values on a subject, and for producing a file
 * 			location identifier that can used for debugging.
 * 
 * 		@Test.Impl
 * 			Methods in the container with this annotation are the implementations of tests declared
 * 			in the corresponding subject class. Each subject Test.Decl should have a corresponding
 * 			container Test.Impl method, and the pair is identified with a single Test.Case. The
 * 			correspondence with the subject declaration is through the Test.Impl "method" and
 * 			"description" attributes. These values are generated by the framework and should not be 
 * 			altered. The framework also generates a method stub that should be copied into the
 * 			container. The generated method names can be changed if needed or desired. Test.Impl 
 * 			also has the three attributes "priority", "timeout", and "weight" which can be used to 
 * 			refine the behavior of the test. Each Test.Impl annotation is represented by a
 * 			sog.core.test.TestImpl instance.
 * 
 * 		interface Test.Case
 * 			The Test.Case interface specifies how test methods interact with the framework to report 
 * 			results. The signature of each Test.Impl test method includes a reference to a
 * 			Test.Case instance. Each (Test.Decl, Test.Impl) pair corresponds to a Test.Case 
 * 			that is implemented by a sog.core.test.TestCase instance. 
 * 
 * 
 * 
 * 	WORKFLOW
 * 
 * 		In the subject class:
 * 			+ import sog.core.Test;
 * 			+ For non-tested class, annotate with @Test.Skip( reason ). Done.
 * 			+ @Test.Subject( container )
 * 			+ public static void main( String[] args ) { Test.eval(); }
 * 			+ Run main.
 * 			+ Confirm error regarding missing container.
 * 			+ Create stub container class.
 * 			+ Rerun main to confirm correct configuration with container.
 * 			+ Consult Policy or review errors for untested members that require testing.
 * 			+ Use @Test.Decl( description ) annotations to describe test cases.
 * 			+ Rerun main to confirm no errors due to untested members.
 * 			+ Review SKIPS and fix any ERRORS.
 * 
 * 		In the container class:
 * 			+ import sog.core.Test;
 * 			+ public static void main( String[] args ) { Test.eval( SubjectClass.class ); }
 * 			+ Run main.
 * 			+ Copy subs into container body.
 * 			+ Rerun main to confirm no ERRORS, no STUBS, and all cases fail.
 * 			+ Implement test methods, run, and debug subject code.
 * 			+ Goal: 100% pass rate, no ERRORS, no STUBS, reasonable SKIPS.
 * 			+ Run tests for the entire package using Test.evalPackage( SubjectClass.class )
 * 
 * 
 * 
 * 
 * 	FRAMEWORK COMPONENTS: Classes in sog.core.test
 * 
 * 		sog.core.test.Policy
 * 			Responsibilities: This class has the responsibility of defining and enforcing the policy 
 * 				regarding which class members should be flagged as needing test validation.
 * 			Structure: An enumeration of all defined policies. Each policy is defined by the status 
 * 				of the 12 types of members: constructors, fields, and methods from the four
 * 				protection levels.
 * 			Services: Convenience methods for determining if a given method is required.
 * 				public boolean required( ... )
 * 				
 * 		sog.core.test.Result
 * 			Responsibilities: Base class for all test results. This class defines the standard output
 * 				format for test results including the success rate, time, and pass/fail counts.
 * 			Structure: Holds the required label identifying the test.
 * 			Services: The toString() implementation is taken to be the canonical format for test results.	
 * 
 * 		sog.core.test.TestCase
 * 			Responsibilities: Represents one test case as determined by a TestImpl instance.
 * 				Allows a test method to interact with the testing framework and records results.
 * 			Structure:
 * 				Extends Result to represent the results after running the test case.
 * 				Holds a TestContainer and TestImpl.
 * 				Implements Test.Case and is given to the TestImpl test method to interact with the testing framework.
 * 				Implements Runnable be executing the given test method and recording results.
 * 				Implements Comparable by using the priority (if any) of the given TestImpl.
 * 			Services: Implements Printable.print( ... ) to include details for failing test cases.
 * 
 * 		sog.core.test.TestDecl
 * 			Responsibilities: Represents a single test declaration as determined by a @Test.Decl annotation
 * 				on a member in a subject class. 
 * 				Maintains the template code for test method stubs.
 * 				Knows when the TestDecl has been matched with a corresponding TestImpl. 
 * 			Structure:
 * 				Extends TestIdentifier, the common base class for TestDecl and TestImpl.
 * 				Holds onto the corresponding TestImpl instance.
 * 				Implements Commented by maintaining the template for stub code.
 * 				Implements Printable by printing the stub code.
 * 			Services:
 * 				public boolean unimplemented() to detect declared cases that do not have test method implementations.
 * 
 * 		sog.core.test.TestIdentifier
 * 			Responsibilities: Define the unique key identifier associated with TestDecl and TestImpl, and define
 * 				the member naming policy for test methods.
 * 			Structure:
 * 				Abstract base class for TestDecl and TestImpl.
 * 				Holds the member name and test case description. This pair must be unique across the test
 * 				cases in a single subject class.
 * 			Services:
 * 				public String getKey(): the unique identifier.
 * 				public String getMethodName(): the generated test method name.
 * 
 * 		sog.core.test.TestImpl
 * 			Responsibilities: Hold information about a single test case.
 * 			Structure: 
 * 				Extends TestIdentifier, the common base class for TestDecl and TestImpl.
 * 				Holds the executable test method corresponding to this case.
 * 			Services: Static factory for constructing from a given method, or return null if not a test method.
 * 
 * 		sog.core.test.TestMember
 * 			Responsibilities: Represents a single member in a subject class.
 * 				Define the naming policy for classes, constructors, fields, and methods.
 * 				Provide logic for handling special cases such as synthetic members.
 * 			Structure:
 * 				Constructors (one for each type of member) determine and record basic properties.
 * 			Services:
 * 				public boolean isSkipped()
 * 				public boolean ieRequired()
 * 				public Stream<TestDec> getDecls()
 * 
 * 		sog.core.test.TestResult
 * 			Responsibilities: Given a subject class, assemble and execute the set of test cases associated
 * 				with the subject.
 * 				Defines error reporting logic for mis-configured tests.
 * 			Structure:
 * 				Extends Result.
 * 				Holds the subject class and container name.
 * 				Holds lists of errors and skips.
 * 				Holds collections for declared tests and their corresponding test cases.
 * 			Services: 
 * 				Public static factory method builds the Result: Given a subject class, examine the class
 * 					for test declarations, match them with corresponding implementations in a container,
 * 					and run the tests.
 * 				Implements Printable.print( ... ) to include details on errors, skips, stubs, and failed cases.
 * 
 * 		sog.core.test.TestResultSet
 * 			Responsibilities: FIXME
 * 			Structure: FIXME
 * 			Services: FIXME
 * 
 * 
 * 		
 */
public class Test {
	
	
	/**
	 * Classes and/or members can be marked as not requiring testing.
	 * The test policy is not applied, but a reminder message is generated.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE } )
	@Documented
	public @interface Skip {
		/** Optional description, for example explanation of alternate testing */
		String value() default "No testing required.";
	}

	
	
	/**
	 * Subject classes use to identify as a class needing testing. The value identifies 
	 * the location of the Test.Container that holds test method implementations.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.TYPE )
	@Documented
	public @interface Subject {
		/**
		 * The value determines the location according to the following rules:
		 *   1. If the string starts with a dot (.Member) it is treated as the name of a member Test.Container class
		 *   2. If the string ends with a dot (package.) it is prepended to the subject full class name
		 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
		 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container class
		 */
		String value(); 
	}

	/**
	 * Annotate class members (constructor, field, method)
	 * with {@code Test.Decl} to declare a test case pertaining to the element.
	 */
	@Repeatable( Test.Decls.class )
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD } )
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
	@Target( { ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD } )
	@Documented
	public @interface Decls {
		Decl[] value();
	}



	
	/**
	 * Container classes that hold test method implementations extend this abstract base class.
	 */
	public static abstract class Container {
		
		private final Class<?> subjectClass;
		
		protected Container( Class<?> subjectClass ) {
			this.subjectClass = subjectClass;
		}
		
		/** The named class must be annotated as a subject naming this Test.Container */
		public Class<?> getSubjectClass() {
			return this.subjectClass;
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
		public String getFileLocation() {
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
	
	
	/**
	 * Test.Container classes contain methods annotated with Test.Impl. These methods are passed 
	 * an instance of Test.Case to describe the results of the test case. All methods
	 * declare a return of Test.Case which is intended to "return this" to allow chaining.
	 */
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
		 * 		Throwable type
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
		 * Test for equality using Objobject.equals().
		 * If T is a compound type (array or collection) then the components are shallowly tested.
		 * 
		 * @param expected
		 * @param actual
		 * @return
		 * 		this Test.Case
		 */
		public <T> Test.Case assertEqual( T expected, T actual );
		
	}
	
	
	
	/** Convenience method to evaluate and print results for one subject class */
	public static void eval( Class<?> subjectClass ) {
		TestResult.forSubject( subjectClass ).print( new IndentWriter( System.err ) );
	}
	
	/** Convenience method to evaluate and print results for the calling class class */
	public static void eval() {
		Test.eval( App.get().getCallingClass( 2 ) );
	}
	
	/** Convenience method to evaluate and print results for the package containing the given subject */
	public static void evalPackage( Class<?> subjectClass ) {
		TestResultSet.testPackage( subjectClass );
	}
	
	
}
