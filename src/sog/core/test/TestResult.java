/**
 * Copyright (C) 2021
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
package sog.core.test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * 		Responsibilities: 
 * 			Given a subject class, assembles and executes the set of test cases associated with the subject.
 * 			Defines error reporting logic for mis-configured tests.
 * 		Structure:
 * 			Extends Result.
 * 			Holds the subject class and container name.
 * 			Holds lists of errors and skips.
 * 			Holds collections for declared tests and their corresponding test cases.
 * 		Services: 
 * 			Public static factory method builds the Result: Given a subject class, examine the class
 * 				for test declarations, match them with corresponding implementations in a container,
 * 				and run the tests.
 * 			Implements Printable.print( ... ) to include details on errors, skips, stubs, and failed cases.
 * 
 */
@Test.Subject( "test." )
public class TestResult extends Result {
	

	/**
	 * Builder to construct an instance representing the results of evaluating the tests associated
	 * with the given subject class.
	 * 
	 * @param subjectClass
	 * @return
	 */
	// General Assertions;
	@Test.Decl( "Throws AssertionError for null subject" )
	@Test.Decl( "Return is not null" )

	// Properties from loadSubject()
	@Test.Decl( "Has an error message if subject is not a top-level class" )
	@Test.Decl( "Has an error message if subject is marked as subject and marked skipped" )
	@Test.Decl( "Has an error message if subject is not marked as subject and not marked skipped" )
	@Test.Decl( "Has a skip message if subject is marked as skipped and not marked as subject" )
	@Test.Decl( "If subject is marked as skipped and not marked as subject there are no test cases" )
	@Test.Decl( "Has an error message if the container location in the Test.Subject annotation is empty" )
	@Test.Decl( "If not marked as skipped and has valid subject annotation then container location is not empty" )
	
	// Properties from scanSubject()
	@Test.Decl( "Subject fields are scanned for test obligations" )
	@Test.Decl( "Subject constructors are scanned for test obligations" )
	@Test.Decl( "Subject methods are scanned for test obligations" )
	@Test.Decl( "Subject member classes are recursively scanned" )
	@Test.Decl( "Members of skipped member classes are ignored" )
	@Test.Decl( "Has a skip message if member is marked as skipped and does not have test declarations" )
	@Test.Decl( "Has an error message if a member is marked as skipped and has test declaration(s)" )
	@Test.Decl( "Has an error message for non-skipped members that do not have declarations and are required by the current policy" )
	@Test.Decl( "Has an error message if a memeber has two identical test declarations" )
	@Test.Decl( "Each valid test declaration is recorded in the declaration mapping" )

	// Properties from loadContainer()
	@Test.Decl( "If the container location starts with a dot a member class test container is used" )
	@Test.Decl( "if the container location ends with a dot a test container class in a parallel package is used" )
	@Test.Decl( "If a parallel test container class is used the classname has 'Test' appended" )
	@Test.Decl( "If the container location has no dots a test container class in the same package is used" )
	@Test.Decl( "If the container location has internal dots the named test container class is used" )
	@Test.Decl( "Has an error message if an instance of the test container can not be constructed" )
	@Test.Decl( "Has an error if the test container does not name the correct subject class" )
	
	// Properties from scanContainer()
	@Test.Decl( "Test container class is scanned for test method implementations" )
	@Test.Decl( "Test container methods without Test.Impl annotations are ignored" )
	@Test.Decl( "Has an error message for test method implementations without a corresponding test declaration" )
	@Test.Decl( "Has an error message if one test declaration has multiple test method implementations" )
	@Test.Decl( "Each valid test method implementation has a corresponding TestCase saved" )
	
	// Properties from runTests()
	@Test.Decl( "Unimplemented test declarations count as test failures" )
	@Test.Decl( "If there are any errors no test cases are run" )
	@Test.Decl( "If there are any errors all test cases are counted as failures" )
	@Test.Decl( "If there are no errors all test cases are run" )
	@Test.Decl( "If a test container was constructed afterAll is called after all cases have run" )
	public static TestResult forSubject( Class<?> subjectClass ) {
		TestResult result = new TestResult( Assert.nonNull( subjectClass ).getName() );
		
		result.loadSubject( subjectClass );
		result.scanSubject();

		result.loadContainer();
		result.scanContainer();
		
		result.runTests();
		
		return result;
	}




	/* The subject class for which this TestResult holds results. Set by loadSubject(). */
	private Class<?> subjectClass;
	
	/*
	 * Identifies the location of the Test.Container holding the test implementations for this
	 * subject class. Read from the Test.Subject annotation. The value determines the location 
	 * according to the following rules:
	 *   1. If the string starts with a dot (.Member) it is treated as the name of a member Test.Container class
	 *   2. If the string ends with a dot (package.) it is prepended to the subject full class name,
	 *   	creating a parallel test package. To prevent name confusion in test code, in this case
	 *   	the name of the container class has suffix "Test" appended.
	 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
	 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container class
	 */
	private String containerLocation;

	/*
	 * Errors represent fatal misconfigurations of test information. When encountered the framework marks 
	 * all tests as failed. Errors are reported by the print( IndentWriter ) method.
	 */
	private final List<Err> errors = new ArrayList<Err>();
	
	/*
	 * When Test.Skip is used to bypass testing of a class or member an item is added
	 * to this list. Skips are reported by the print( IndentWriter ) method.
	 */
	private final List<String> skips = new ArrayList<String>();
	
	/* 
	 * A map of all test declarations encountered when scanning the subject class, keyed by the
	 * unique identifier determined by TestIdentifier. These are later matched with the test
	 * implementations encountered when scanning the container. 
	 */
	private final Map<String, TestDecl> declMap = new TreeMap<String, TestDecl>();
	
	/*
	 * Unmatched test declarations do not yet have test implementations. These will count as test
	 * failures, and will generate test method stubs in the Printable.print(..) output. This
	 * list is generated by runTests()
	 */
	private List<TestDecl> unimplemented = null;
	
	/* The Test.Container holding implementations of test methods. Constructed by loadContainer() */
	private Test.Container container;

	/*
	 * Set of cases generated by scanContainer(). Cases are matched against the previously encountered
	 * declarations from scanSubject(). The set is ordered, first by the configured priority (if given)
	 * then alphabetically by member name and description.
	 */
	private final SortedSet<TestCase> testCases = new TreeSet<TestCase>();
	
	/* The total weight of all passing test cases */
	private int passCount = 0;
	
	/* The total weight of all failing test cases */
	private int failCount = 0;
	
	/* The total elapsed time for running all test cases */
	private long elapsedTime = 0L;
	
	
	/* Instances are obtained using the public static builder. */
	private TestResult( String label ) {
		super( label );
	}
	
	
	
	private class Err implements Printable {
		
		private final Throwable error;
		private final String description;
		private final Object[] details;
		
		private Err( Throwable error, String description, Object... details ) {
			this.error = error;
			this.description = description;
			this.details = details;
		}

		@Override
		@Test.Skip( "See addError()" )
		public void print( IndentWriter out ) {
			out.println( this.description );
			out.increaseIndent();
			Arrays.stream( this.details ).forEach( out::println );
			if ( this.error != null ) {
				out.println( this.error );
			}
			out.decreaseIndent();
		}
		
	}
	
	/**
	 * Record information about a fatal error by adding a number of String messages to List<Sting> messages.
	 * 
	 * @param error - An optional Throwable associated with the error
	 * @param description - Brief String description of the error
	 * @param details - Any number of additional Objects providing details
	 */
	@Test.Decl( "If error is null no location information is printed" )
	@Test.Decl( "If error is not null location information is printed" )
	@Test.Decl( "If error is not null includes information on cause(s)" )
	@Test.Decl( "Error messages include description" )
	@Test.Decl( "Detail objects are included in message" )
	private void addError( Throwable error, String description, Object... details ) {
		this.errors.add( new Err( error, description, details ) );
	}

	@Test.Decl( "Skip message includes the source member" )
	@Test.Decl( "Skip message includes the reason" )
	private void addSkip( Object source, String reason ) {
		this.skips.add( Strings.toString( source ) + ": " + reason );
	}
		
	
	private void loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
		if ( subjectClass.isSynthetic() || subjectClass.getEnclosingClass() != null ) {
			this.addError( null, "Subject class is not a top-level class", subjectClass );
		}
		
		Test.Skip skip = this.subjectClass.getDeclaredAnnotation( Test.Skip.class );
		Test.Subject subj = this.subjectClass.getDeclaredAnnotation( Test.Subject.class );
		
		boolean isSkipped = skip != null;
		boolean isSubject = subj != null;
		
		if ( isSkipped ) {
			if ( isSubject ) {
				this.addError( null, "Subject is also marked to be skipped", this.subjectClass );
			} else {
				// The recursive method scanClass() adds the skip message
				// this.addSkip( this.subjectClass, skip.value() );
			}
		} else {
			if ( isSubject ) {
				if ( subj.value().isEmpty() ) {
					this.addError( null, "Subject annotation has empty container location", this.subjectClass );
				} else {
					this.containerLocation = subj.value();
				}
			} else {
				this.addError( null, "Subject class is not annotated", this.subjectClass );
			}
		}
	}
	
	
	private void scanSubject() {
		this.scanClass( this.subjectClass ).forEach( this::scanMember );
	}
	
	private Stream<TestMember> scanClass( Class<?> clazz ) {
		Stream<TestMember> result = null;
		Test.Skip skip = clazz.getDeclaredAnnotation( Test.Skip.class );
		
		if ( skip == null ) {
			result = Stream.concat(
				Stream.concat( 
					Arrays.stream( clazz.getDeclaredConstructors() ).map( TestMember::new ),
					Arrays.stream( clazz.getDeclaredFields() ).map( TestMember::new )
				), 
				Stream.concat( 
					Arrays.stream( clazz.getDeclaredMethods() ).map( TestMember::new ),
					Arrays.stream( clazz.getDeclaredClasses() ).flatMap( this::scanClass )
				)
			);
		} else {
			result = Stream.of();
			this.addSkip( clazz, skip.value() );
		}
		
		return result;
	}
	
	private void scanMember( TestMember member ) {
		if ( member.isSkipped() ) {
			if ( member.hasDecls() ) {
				this.addError( null, "Member has declarations and is marked for skipping", member );
			} else {
				this.addSkip( member, member.getSkipReason() );
			}
		} else {
			if ( member.hasDecls() ) {
				member.getDecls().forEach( this::addDecl );
			} else {
				if ( member.isRequired() ) {
					this.addError( null, "Untested member required by the current policy", member );
				}
			}
		}
	}

	private void addDecl( TestDecl decl ) {
		if ( this.declMap.containsKey( decl.getKey() ) ) {
			this.addError( null, "Duplicate declaration", decl, 
				"Member", decl.getMemberName(), "Description", decl.getDescription() );
		} else {
			this.declMap.put( decl.getKey(), decl );
		}
	}

	
	

	/*
	 *   1. If the string starts with a dot (.Member) it is treated as the name of a member Test.Container class
	 *   2. If the string ends with a dot (package.) it is prepended to the subject full class name,
	 *   	creating a parallel test package. To prevent name confusion in test code, in this case
	 *   	the name of the container class has suffix "Test" appended.
	 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
	 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container class
	 */
	private void loadContainer() {
		if ( this.containerLocation == null ) {
			return;
		}
		
		String containerClassName = "";
		
		if ( this.containerLocation.startsWith( "." ) ) {
			containerClassName = this.subjectClass.getName() + this.containerLocation.replaceAll( "\\.", "\\$" );
		} else if ( this.containerLocation.endsWith( "." ) ) {
			containerClassName = this.containerLocation + this.subjectClass.getName() + "Test";
		} else if ( !this.containerLocation.contains( "." ) ) {
			containerClassName = this.subjectClass.getPackageName() + "." + this.containerLocation;
		} else {
			containerClassName = this.containerLocation;
		}
		
		try {
			Class<?> clazz = Class.forName( containerClassName );
			Constructor<?> cons = clazz.getDeclaredConstructor();
			cons.setAccessible( true );
			this.container = (Test.Container) cons.newInstance();
		} catch ( Throwable e ) {
			this.addError( e, "Cannot construct container", this.subjectClass, "Container name", containerClassName );
			return;
		}
		
		if ( !this.container.getSubjectClass().equals( this.subjectClass ) ) {
			this.addError( null, "Container names the wrong subject", this.container.getClass(), 
				"Should be", this.subjectClass, "Got", this.container.getSubjectClass() );
		}
	}
	

	private void scanContainer() {
		if ( this.container == null ) {
			return;
		}
		
		Arrays.stream(  this.container.getClass().getDeclaredMethods() )
			.map( TestImpl::forMethod )
			.filter( TestImpl::isValid )
			.forEach( this::addImpl );
	}
	
	private void addImpl( TestImpl impl ) {
		TestDecl decl = this.declMap.get( impl.getKey() );
		if ( decl == null ) {
			this.addError( null, "Orphaned test implementation", this.containerLocation, 
				"Member", impl.getMemberName(), "Description", impl.getDescription() );		
		} else {
			if ( decl.setImpl( impl ) ) {
				this.testCases.add( new TestCase( impl, this.container ) );
			} else {
				this.addError( null, "Duplicate test implementation", this.containerLocation,
					"Member", impl.getMemberName(), "Description", impl.getDescription() );
			}
		}		
	}
	
	
	private void runTests() {
		this.unimplemented = this.declMap.values().stream().filter( TestDecl::unimplemented )
			.collect( Collectors.toList() );
		this.failCount += this.unimplemented.size();

		final boolean noErrors = this.errors.size() == 0;
		Consumer<TestCase> process = tc -> {
			if ( noErrors ) {
				tc.run();
			}
			this.elapsedTime += tc.getElapsedTime();
			this.passCount += tc.getPassCount();
			this.failCount += tc.getFailCount();
		};
		
		this.testCases.forEach( process );

		if ( this.container != null ) {
			this.container.afterAll().exec();
		}
	}
	

	@Override
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Includes global summary statistics" )
	@Test.Decl( "If there are errors then details are included" )
	@Test.Decl( "If no errors then result details are included" )
	@Test.Decl( "Includes details on members that have been skipped" )
	@Test.Decl( "Includes stubs for unimplemented methods" )
	public void print( IndentWriter out ) {
		out.println( this.toString() );

		out.increaseIndent();
		
		if ( this.errors.size() > 0 ) {
			out.println();
			out.println( "ERRORS:" );
			out.increaseIndent();
			this.errors.forEach( out::println );
			out.decreaseIndent();
		} else {
			out.println();
			out.println( "RESULTS:" );
			out.increaseIndent();
			this.testCases.forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.skips.size() > 0 ) {
			out.println();
			out.println( "SKIPS:" );
			out.increaseIndent();
			this.skips.forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.unimplemented.size() > 0 ) {
			out.println();
			out.println( "STUBS:" );
			out.increaseIndent();
			this.unimplemented.forEach( out::println );
			out.decreaseIndent();
		}

		out.decreaseIndent();
	}
	
	@Override
	@Test.Decl( "Reported time is the sum of the times of all test cases" )
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	@Override
	@Test.Decl( "Return is the sum of the weights of all passing cases" )
	public int getPassCount() {
		return this.passCount;
	}

	@Override
	@Test.Decl( "Return is the sum of the weights of all failing cases" )
	public int getFailCount() {
		return this.failCount;
	}

}
