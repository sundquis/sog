/**
 * Copyright (C) 2021, 2023
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */

package sog.core;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.stream.Stream;

import sog.core.test.TestSubject;
import sog.core.test.TestSet;

/**
 * All classes import Test as the single public access to the testing framework.
 * 
 * Resources fall into two categories, SUBJECT and CONTAINER. Classes intending to be tested, 
 * "subject classes", use annotations to declare test cases. The framework generates method stubs 
 * for each declared test case, which are implemented in a "container class." 
 * 
 * 
 *	SUBJECT: A class that is the subject of one or more tests.
 * 
 * 		@Test.Subject( container )
 * 			A class identifies itself as a subject by using the Test.Subject annotation, 
 * 			which indicates the test container class using the following naming conventions:
 * 				.MemberName		Search for a member Test.Container class in the subject.
 * 				packagePrefix.	Prepend packagePrefix to the full subject class name and append "Test" to the class name.
 * 				ClassName		Look in the same package as the subject for ClassName.
 * 				Otherwise		The name, which must contain a ".", is a fully qualified class name.
 * 			Each valid subject is represented by a sog.core.test.TestSubject instance.
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
 * 			associated with the member. Each Test.Decl annotation is represented by a
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
 * 			location identifier that can be used for debugging.
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
 * 			+ For private/package members requiring tests, either provide well-documented
 * 				Test.Skip annotation together with description of how obligations are met through
 * 				alternate testing, OR, arrange for testing through an internal member container
 * 				so that the private members can be accessed via a nest-mate.
 * 			+ Use @Test.Decl( description ) annotations to describe test cases.
 * 			+ Rerun main to confirm no errors due to untested members.
 * 			+ Review SKIPS and fix any ERRORS.
 * 
 * 		In the container class:
 * 			+ import sog.core.Test;
 * 			+ public static void main( String[] args ) { Test.eval( SubjectClass.class ); }
 * 			+ Run main.
 * 			+ Copy stubs into container body.
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
 * 			Responsibilities: 
 * 				Defines and enforces the policy regarding which class members should be flagged 
 * 				as needing test validation.
 * 			Structure: 
 * 				An enumeration of all defined policies. 
 * 				Each policy is defined by the status of the 12 types of members: constructors, fields, 
 * 				and methods from the four protection levels.
 * 			Services: 
 * 				Convenience methods for determining if a given member requires testing.
 * 				
 * 		sog.core.test.Result
 * 			Responsibilities: 
 * 				Base class for all test results. 
 * 				Defines the standard output format for test results including the success rate, 
 * 				time, and pass/fail counts.
 * 			Structure: 
 * 				Holds the required label identifying the test.
 * 			Services: 
 * 				The toString() implementation is taken to be the canonical format for test results.	
 * 
 * 		sog.core.test.TestCase
 * 			Responsibilities: 
 * 				Represents one test case as determined by a TestImpl instance.
 * 				After the test case has been executed, represents the results of the test case.
 * 				Allows a test method to interact with the testing framework and records results.
 * 			Structure:
 * 				Extends Result to represent the results after running the test case.
 * 				Holds a TestContainer and TestImpl.
 * 				Implements Test.Case and is given to the TestImpl test method to interact with the testing framework.
 * 				Implements Runnable by executing the given test method and recording results.
 * 				Implements Comparable by using the priority (if any) of the given TestImpl.
 * 			Services: 
 * 				Implements Printable.print( ... ) to include details for failing test cases.
 * 
 * 		sog.core.test.TestDecl
 * 			Responsibilities: 
 * 				Represents a single test declaration as determined by a @Test.Decl annotation
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
 * 			Responsibilities: 
 * 				Defines the unique key identifier associated with TestDecl and TestImpl
 * 				Defines the member naming policy for test methods.
 * 			Structure:
 * 				Abstract base class for TestDecl and TestImpl.
 * 				Holds the member name and test case description. This pair must be unique across the test
 * 				cases in a single subject class.
 * 			Services:
 * 				public String getKey(): the unique identifier.
 * 				public String getMethodName(): the generated test method name.
 * 
 * 		sog.core.test.TestImpl
 * 			Responsibilities: 
 * 				Holds information about a single test case.
 * 			Structure: 
 * 				Extends TestIdentifier, the common base class for TestDecl and TestImpl.
 * 				Holds the executable test method corresponding to this case.
 * 			Services: 
 * 				Static factory for constructing from a given method, or return null if not a test method.
 * 
 * 		sog.core.test.TestMember
 * 			Responsibilities: 
 * 				Represents a single member in a subject class.
 * 				Defines the naming policy for classes, constructors, fields, and methods.
 * 				Provides logic for handling special cases such as synthetic members.
 * 			Structure:
 * 				Constructors (one for each type of member) determine and record basic properties.
 * 			Services:
 * 				public boolean isSkipped()
 * 				public boolean ieRequired()
 * 				public Stream<TestDec> getDecls()
 * 
 * 		sog.core.test.TestSubject
 * 			Responsibilities: 
 * 				Given a subject class, assembles and executes the set of test cases associated with the subject.
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
 * 		sog.core.test.TestSet
 * 			Responsibilities:
 * 				Aggregate results for multiple classes into a single Result.
 * 			Structure:
 * 				Extends Result to represent the results after running the test case.
 * 				Holds a Set of TestSubject instance sorted by classname.
 * 			Services:
 *  			Mutator to set the verbosity level.
 *  			Static helper methods for assembling sets of results by package or directory tree.
 */
@Test.Subject( "test." )
public class Test {
	
	/**
	 * Not intended to be instantiated.
	 * 
	 * The class defines the components of the testing framework.
	 */
	private Test() {}
	
	
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
	 * Subject classes use this annotation to identify as a class needing testing. The value identifies 
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

		@Test.Decl( "Throws AssertionError for null subject" )
		protected Container( Class<?> subjectClass ) {
			this.subjectClass = Assert.nonNull( subjectClass );
		}
		
		/** The named class must be annotated as a subject naming this Test.Container */
		@Test.Decl( "Return is not null" )
		@Test.Decl( "Value is consistent with constructed value" )
		public Class<?> getSubjectClass() {
			return this.subjectClass;
		}
		
		/** 
		 * The procedure to be called before any method invocations.
		 * 
		 * Note: Formerly initialization code was placed in the Container constructor, but this
		 * constructor is called while tests are being assembled (before tests are run) and
		 * was interfering with the Test framework. Any remaining code in constructors should
		 * be moved to the beforeAll Procedure.
		 */
		@Test.Decl( "Default is NOOP" )
		public Procedure beforeAll() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called before each method invocation. */
		@Test.Decl( "Default is NOOP" )
		public Procedure beforeEach() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called after each method invocation. */
		@Test.Decl( "Default is NOOP" )
		public Procedure afterEach() {
			return Procedure.NOOP;
		}
		
		/** The procedure to be called after all method invocations have completed. */
		@Test.Decl( "Default is NOOP" )
		public Procedure afterAll() {
			return Procedure.NOOP;
		}

		/** Set the value of a subject's field. */
		@Test.Decl( "Throws AppExcpetion for null subject and non-static field" )
		@Test.Decl( "Throws AssertionError for null field name" )
		@Test.Decl( "Throws AssertionError for empty field name" )
		@Test.Decl( "Throws AppExcpetion for missing field" )
		@Test.Decl( "Throws AppExcpetion for setting incompatible type" )
		@Test.Decl( "Sets public instance values" )
		@Test.Decl( "Sets protected instance values" )
		@Test.Decl( "Sets package instance values" )
		@Test.Decl( "Sets private instance values" )
		@Test.Decl( "Sets public static values" )
		@Test.Decl( "Sets protected static values" )
		@Test.Decl( "Sets package static values" )
		@Test.Decl( "Sets private static values" )
		public void setSubjectField( Object subject, String fieldName, Object fieldValue ) {
			try {
				Field field = this.getSubjectClass().getDeclaredField( Assert.nonEmpty( fieldName ) );
				field.setAccessible( true );
				field.set( subject, fieldValue );
			} catch ( NoSuchFieldException e ) {
				throw new AppException( "Bad field name: " + fieldName, e );
			} catch ( IllegalAccessException e ) {
				Fatal.impossible( "After setAccessible should not happen?", e );
			} catch ( NullPointerException e ) {
				throw new AppException( "Instance field requires non-null subject", e );
			} catch ( IllegalArgumentException e ) {
				throw new AppException( "Wrong type value supplied to " + fieldName, e );
			}
		}

		/** Retrieve the value of a subject's field. */
		@SuppressWarnings("unchecked")
		@Test.Decl( "Throws AppExcpetion for null subject and non-static field" )
		@Test.Decl( "Throws AssertionError for null field name" )
		@Test.Decl( "Throws AssertionError for empty field name" )
		@Test.Decl( "Throws ClassCastException for wrong witness type" )
		@Test.Decl( "Throws AppExcpetion for missing field" )
		@Test.Decl( "Gets public instance values" )
		@Test.Decl( "Gets protected instance values" )
		@Test.Decl( "Gets package instance values" )
		@Test.Decl( "Gets private instance values" )
		@Test.Decl( "Gets public static values" )
		@Test.Decl( "Gets protected static values" )
		@Test.Decl( "Gets package static values" )
		@Test.Decl( "Gets private static values" )
		public <T> T getSubjectField( Object subject, String fieldName, T witness ) {
			T result = null;

			try {
				Field field = this.getSubjectClass().getDeclaredField( Assert.nonEmpty( fieldName ) );
				field.setAccessible( true );
				result = (T) field.get( subject );
			} catch ( NoSuchFieldException e ) {
				throw new AppException( "Bad field name: " + fieldName, e );
			} catch ( IllegalAccessException e ) {
				Fatal.impossible( "After setAccessible should not happen?", e );
			} catch ( NullPointerException e ) {
				throw new AppException( "Instance field requires non-null subject", e );
			}
			
			return result;
		}
		
		@SuppressWarnings( "unchecked" )
		@Test.Decl( "Throws AppExcpetion for null subject and non-static method" )
		@Test.Decl( "Throws AssertionError for null method name" )
		@Test.Decl( "Throws AssertionError for empty method name" )
		@Test.Decl( "Throws AppException for no matching method" )
		@Test.Decl( "Throws ClassCastException for wrong witness type" )
		@Test.Decl( "Throws Error generated by method")
		@Test.Decl( "Throws RuntimeException generated by method")
		@Test.Decl( "Wraps Exception generated by method in AppException")
		@Test.Decl( "Evaluates public instance methods" )
		@Test.Decl( "Evaluates protected instance methods" )
		@Test.Decl( "Evaluates package instance methods" )
		@Test.Decl( "Evaluates private instance methods" )
		@Test.Decl( "Evaluates public static methods" )
		@Test.Decl( "Evaluates protected static methods" )
		@Test.Decl( "Evaluates package static methods" )
		@Test.Decl( "Evaluates private static methods" )
		@Test.Decl( "Evaluates no-arg instance methods" )
		@Test.Decl( "Evaluates no-arg static methods" )
		@Test.Decl( "Evaluates void instance methods" )
		@Test.Decl( "Evaluates void static methods" )
		@Test.Decl( "Evaluates instance methods with object arguments" )
		@Test.Decl( "Evaluates instance methods with primitive arguments" )
		@Test.Decl( "Evaluates instance methods with mixed arguments" )
		@Test.Decl( "Evaluates static methods with object arguments" )
		@Test.Decl( "Evaluates static methods with primitive arguments" )
		@Test.Decl( "Evaluates static methods with mixed arguments" )
		@Test.Decl( "Evaluates overloaded instance methods" )
		@Test.Decl( "Evaluates overloaded static methods" )
		public <T> T evalSubjectMethod( Object subject, String methodName, T returnWitness, Object... args ) {
			Assert.nonEmpty( methodName );
			Assert.nonNull( args );
			
			Iterator<Method> methods = Stream.of( this.getSubjectClass().getDeclaredMethods() )
				.filter( m -> m.getName().equals( methodName ) )
				.filter( m -> m.getParameterCount() == args.length )
				.iterator();

			boolean searching = true;
			Method method = null;
			T result = null;
			
			// Need to better understand reflecting for methods by parameter types...
			// This looks for the first matching, which might not be the most specific match
			while ( searching && methods.hasNext() ) {
				method = methods.next();
				method.setAccessible( true );
				try {
					result = (T) method.invoke( subject, args );
					searching = false;
				} catch ( IllegalAccessException | IllegalArgumentException e ) {
				} catch ( NullPointerException e ) {
					throw new AppException( "Instance method requires non-null subject", e );
				} catch ( InvocationTargetException e ) {
					// this means we found and executed a method, but it raised an exception
					Throwable t = e.getCause();
					if ( t instanceof Error ) {
						throw (Error) t;
					} else if ( t instanceof RuntimeException ) {
						throw (RuntimeException) t;
					} else {
						throw new AppException( t );
					}
				}
			}
			
			if ( searching ) {
				throw new AppException( "No matching method: " + methodName );
			}
						
			return result;
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

		/** Used to determine if cases should be run concurrently */
		boolean threadsafe() default true;
		
	}
	
	
	/**
	 * Test.Container classes contain methods annotated with Test.Impl. These methods are passed 
	 * an instance of Test.Case to describe the results of the test case. All methods
	 * declare a return of Test.Case which is intended to "return this" to allow chaining.
	 */
	public interface Case {
		
		
		/**
		 * Procedure to call after the current method (in a Test.Container) completes.
		 * Will be called even if the method throws an exception.
		 * 
		 * @param callafter
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case afterThis( Procedure callafter );

		
		/**
		 * Only displayed for non-passing cases. 
		 * 
		 * @param message
		 * 		Failure message
		 * @return
		 * 		this Test.Case
		 */
		// Can be used with the following "toggle block" pattern to manually check cases via visual inspection
		// 
		//		// TOGGLE:
		//		//* */ tc.assertPass(); /*
		//		tc.assertFail( "Explanation" );
		//		...
		//		/* */
		// 
		// When the first line starts "/* */" the first assertion is live, causing the case to pass.
		// When the first line starts "//* */" the second assertion is live, causing the case to fail.
		// Messages should be used to describe how to determine (manually) that the case passes.
		public Test.Case addMessage( String message );


		/**
		 * Type of Throwable that subsequent code is expected to throw. The Test.Case
		 * fails if the expected Throwable is not thrown.
		 * 
		 * @param expectedError
		 * 		Throwable type
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case expectError( Class<? extends Throwable> expectedError );


		/**
		 * Assert that the given object is not null.
		 * 
		 * @param obj
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertNonNull( Object obj );
		
		/**
		 * Assert that the given object is null.
		 * 
		 * @param obj
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertIsNull( Object obj );
		
		
		/**
		 * Assert that the given string is non-null and not empty
		 * 
		 * @param s
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertNotEmpty( String s );
		
		
		/**
		 * Unconditionally mark the case as passed.
		 *  
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertPass();
		
		
		/**
		 * Unconditionally mark the case as failed.
		 *  
		 * @param message
		 * 		Description of the failure
		 * @return
		 * 		this Test.Case
		 */
		public Test.Case assertFail( String message );
		
		
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
		
		/**
		 * Test for non-equality using Object.equals().
		 * If T is a compound type (array or collection) then the components are shallowly tested.
		 * 
		 * @param expected
		 * @param actual
		 * @return
		 * 		this Test.Case
		 */
		public <T> Test.Case assertNotEqual( T expected, T actual );
		
	}
	
	
	
	/** Convenience method to evaluate and print results for one subject class. */
	@Test.Decl( "Throws AssertionError for null subject" )
	public static TestSubject eval( Class<?> subject ) {
		return TestSubject.forSubject( Assert.nonNull( subject ) );
	}
	
	/** Convenience method to evaluate and print results for the package containing the given subject class. */
	@Test.Decl( "Throws AssertionError for null subject" )
	public static TestSet evalPackage( Class<?> subject ) {
		return TestSet.forPackage( Assert.nonNull( subject ) );
	}
	
		
}
