/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * 
 */
@Test.Subject( ".SomMember" )
//@Test.Skip( "Skip this class" )
public class TestResult extends Result {
	
	public static TestResult forSubject( Class<?> subjectClass ) {
		TestResult result = new TestResult( Assert.nonNull( subjectClass ).getName() );
		
		if ( !result.loadSubject( subjectClass ) ) { return result; }
		
		if ( !result.scanSubject() ) { return result; }
		
		if ( !result.loadContainer()  ) { return result; }
		
		
		return result;
	}




	/** The subject class for which this TestResult holds results. Set by loadSubject(). */
	private Class<?> subjectClass;
	
	/**
	 * Identifies the location of the Test.Container holding the test implementations for this
	 * subject class. Read from the Test.Subject annotation. The value determines the location 
	 * according to the following rules:
	 *   1. If the string starts with a dot (.Member) it is treated as the name of a member Test.Container class
	 *   2. If the string ends with a dot (package.) it is prepended to the subject full class name
	 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
	 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container class
	 */
	private String containerLocation;

	/**
	 * Errors represent fatal misconfigurations of test information. When encountered
	 * the framework fails fast and marks all passed tests as failed. Errors
	 * are reported by the print( IndentWriter ) method.
	 */
	private final List<String> errors = new ArrayList<String>();
	
	/**
	 * When Test.Skip is used to bypass testing of a class or member an item is added
	 * to this list. Skips are reported by the print( IndentWriter ) method.
	 */
	private final List<String> skips = new ArrayList<String>();
	
	private final Map<String, TestCase> caseMap = new TreeMap<String, TestCase>();
	
	/** The Test.Container holding implementations of test methods. Constructed by loadContainer() */
	private Test.Container container;
	
	
	public TestResult( String label ) {
		super( label );
	}
	
	
	
	private void addError( int xxx, Object... details ) {
		int pass = this.getPassCount();
		this.incPassCount( -pass );
		this.incFailCount( pass );
		this.errors.add( Arrays.stream( details ).map( Strings::toString ).collect( Collectors.joining() ) );
	}
	
	private class Err {
		private Err addDetail( String description, Object target ) {
			TestResult.this.errors.add( "    " + description + ": " + target.toString() );
			return this;
		}
	}
	
	private Err addError( String description, Object target ) {
		this.errors.add( description +": " + target );
		return new Err();
	}
	
	private void addSkip( Object target, String reason ) {
		this.skips.add(  target.toString() + ": " + reason );
	}
		
	
	/** Return true if processing should continue */
	private boolean loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
		if ( subjectClass.isSynthetic() || subjectClass.getEnclosingClass() != null ) {
			this.addError( "Subject class is not a top-level class", subjectClass );
			return false;
		}
		
		Test.Skip skip = this.subjectClass.getDeclaredAnnotation( Test.Skip.class );
		Test.Subject subj = this.subjectClass.getDeclaredAnnotation( Test.Subject.class );
		
		boolean isSkipped = skip != null;
		boolean isSubject = subj != null;
		
		if ( isSkipped ) {
			if ( isSubject ) {
				this.addError( "Subject is also marked to be skipped", this.subjectClass );
			} else {
				this.addSkip( this.subjectClass, skip.value() );
			}
			return false;
		} else {
			if ( isSubject ) {
				this.containerLocation = subj.value();
				return true;
			} else {
				this.addError( "Subject class is not annotated", this.subjectClass );
				return false;
			}
		}
	}
	
	
	private boolean scanSubject() {
		this.scanClass( this.subjectClass ).forEach( this::scanMember );
		
		return this.errors.size() == 0;
	}
	
	private Stream<TestDecl> scanClass( Class<?> clazz ) {
		return Stream.concat(
			Stream.concat( 
				Arrays.stream( clazz.getDeclaredConstructors() ).map( TestDecl::new ),
				Arrays.stream( clazz.getDeclaredFields() ).map( TestDecl::new )
			), 
			Stream.concat( 
				Arrays.stream( clazz.getDeclaredMethods() ).map( TestDecl::new ),
				Arrays.stream( clazz.getDeclaredClasses() ).flatMap( this::scanClass )
			)
		);
	}
	
	private void scanMember( TestDecl member ) {
		if ( member.isSkipped() ) {
			if ( member.hasDecls() ) {
				this.addError( "Member is marked for skipping", member)
					.addDetail( "Has test declarations", member.getDecls().length );
			} else {
				this.addSkip( member, member.getSkipReason() );
			}
		} else {
			if ( member.hasDecls() ) {
				this.addCases( member.toString(), member.getDecls() );
			} else {
				if ( member.isRequired() ) {
					this.addError( "Untested member required by the current policy", member );
				}
			}
		}
	}
	

	private void addCases( String memberName, Test.Decl[] decls ) {
		for ( Test.Decl decl : decls ) {
			//if ( !this.caseMap.put(  , value ))
		}
	}
	
	

	/*
	 *   1. If the string starts with a dot (.Member) it is treated as the name of a member Test.Container class
	 *   2. If the string ends with a dot (package.) it is prepended to the subject full class name
	 *   3. If the string contains no dots (ClassName) it is treated as the name of a class in the same package
	 *   4. Otherwise, the string is treated as the fully qualified name of a Test.Container class
	 */
	private boolean loadContainer() {
		String containerClassName = "";
		
		if ( this.containerLocation.startsWith( "." ) ) {
			containerClassName = this.subjectClass.getName() + this.containerLocation.replaceAll( "\\.", "\\$" );
		} else if ( this.containerLocation.endsWith( "." ) ) {
			containerClassName = this.containerLocation + this.subjectClass.getName();
		} else if ( !this.containerLocation.contains( "." ) ) {
			containerClassName = this.subjectClass.getPackageName() + "." + this.containerLocation;
		} else {
			containerClassName = this.containerLocation;
		}
		
		try {
			Class<?> clazz = Class.forName( containerClassName );
			this.container = (Test.Container) clazz.getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			this.addError( "Cannot construct container", this.subjectClass )
				.addDetail( "Container name", containerClassName )
				.addDetail( "Exception", e );
			return false;
		}
		
		if ( !this.container.getSubjectClass().equals( this.subjectClass ) ) {
			this.addError( "Container names the wrong subject", this.container.getClass() )
				.addDetail( "Should be", this.subjectClass )
				.addDetail( "Got", this.container.getSubjectClass() );
			return false;
		}
		
		return true;
	}
	

	@Override
	public void print( IndentWriter out ) {
		out.println( this );

		out.increaseIndent();

		if ( this.errors.size() > 0 ) {
			out.println( "ERRORS:" );
			out.increaseIndent();
			this.errors.stream().forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.skips.size() > 0 ) {
			out.println( "SKIPS:" );
			out.increaseIndent();
			this.skips.stream().forEach( out::println );
			out.decreaseIndent();
		}

		out.decreaseIndent();
	}
	
	
	
	public static void main( String[] args ) {
		Test.eval( TestResult.class );
		
		
		System.out.println( "Done!" );
	}
	
	@Test.Subject( ".Nested.Inner" )
	//@Test.Subject( "test.one." )
	//@Test.Subject( "SomeContainer" )
	private static class Foo2 {
		private static class Nested{
			private static class Inner extends Test.Container {
				@Test.Skip( "test" )
				public Inner() {
					super( Foo2.class );
				}
				
			}
		}
		
	}
	
	//@Test.Skip( "Just because" )
	@Test.Subject( ".FooMemberClass" )
	private static class Foo {
		
		private static String myString = "Hi";
		public static String fieldViolation = "foo";
		private Foo( String s, int i) {}
		void string(int i, double d, Foo[] foos, Stream<Foo> stream) {}
		
		@Test.Skip( "With decls"  )
		@Test.Decl( "Test 1" )
		@Test.Decl( "Test 2"  )
		void run() {}
		
		@Test.Skip( "Good skip"  )
		void run2() {}
		
		void main() {}
		
		class Nested {}
		
		@Test.Decl( "Duplicate declaration" )
		@Test.Decl( "Duplicate declaration" )
		public Foo(int i) {}
		
		
	}
	

}
