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
import java.util.Set;
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
public class TestResult extends Test.Result {
	
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
	
	/** scanMember( ... ) converts a legal TestMember instance into a TestDecl. */
	private final Set<TestDecl> decls = new HashSet<TestDecl>();
	
	/** The Test.Container holding implementations of test methods. Constructed by loadContainer() */
	private Test.Container container;
	
	
	public TestResult( String label ) {
		super( label );
	}
	
	
	
	private void addError( Object... details ) {
		int pass = this.getPassCount();
		this.incPassCount( -pass );
		this.incFailCount( pass );
		this.errors.add( Arrays.stream( details ).map( Strings::toString ).collect( Collectors.joining() ) );
	}
	
	private void addSkip( Object... details ) {
		this.skips.add( Arrays.stream( details ).map( Strings::toString ).collect( Collectors.joining() ) );
	}
		
	
	/** Return true if processing should continue */
	private boolean loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
		if ( subjectClass.isSynthetic() || subjectClass.getEnclosingClass() != null ) {
			this.addError( "Subject class ", subjectClass, " is not a top-level class." );
			return false;
		}
		
		Test.Skip skip = this.subjectClass.getDeclaredAnnotation( Test.Skip.class );
		Test.Subject subj = this.subjectClass.getDeclaredAnnotation( Test.Subject.class );
		
		boolean isSkipped = skip != null;
		boolean isSubject = subj != null;
		
		if ( isSkipped ) {
			if ( isSubject ) {
				this.addError( "Subject ", this.subjectClass, " is marked to be skipped." );
			} else {
				this.addSkip( "Skipping ", this.subjectClass, ": ", skip.value() );
			}
			return false;
		} else {
			if ( isSubject ) {
				this.containerLocation = subj.value();
				return true;
			} else {
				this.addError( this.subjectClass, " is not marked as a subject class." );
				return false;
			}
		}
	}
	
	
	private boolean scanSubject() {
		this.scanClass( this.subjectClass ).forEach( this::scanMember );
		
		return this.errors.size() == 0;
	}
	
	private Stream<TestMember> scanClass( Class<?> clazz ) {
		return Stream.concat(
			Stream.concat( 
				Arrays.stream( clazz.getDeclaredConstructors() ).map( TestMember::new ),
				Arrays.stream( clazz.getDeclaredFields() ).map( TestMember::new )
			), 
			Stream.concat( 
				Arrays.stream( clazz.getDeclaredMethods() ).map( TestMember::new ),
				Arrays.stream( clazz.getDeclaredClasses() ).flatMap( this::scanClass )
			)
		);
	}
	
	private void scanMember( TestMember member ) {
		if ( member.isSkipped() ) {
			if ( member.hasDecls() ) {
				this.addError( "Member ", member, " is marked for skipping and has ", member.getDecls().length, " declarations."  );
			} else {
				this.addSkip( "Skipping member ", member, ": ", member.getSkipReason() );
			}
		} else {
			if ( member.hasDecls() ) {
				this.addCases( member.toString(), member.getDecls() );
			} else {
				if ( member.isRequired() ) {
					this.addError( "Untested member ", member, " is required by the current policy." );
				}
			}
		}
	}
	

	private void addCases( String memberName, Test.Decl[] decls ) {
		
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
			this.addError( "Cannot construct container for ", this.subjectClass,
				", container name: ", containerClassName, ", exception: ", e );
			return false;
		}
		
		if ( !this.container.getSubjectClass().equals( this.subjectClass ) ) {
			this.addError( "Container ", this.container.getClass(), " names the wrong subject: ", 
				"Should be ", this.subjectClass, ", got: ", this.container.getSubjectClass() );
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
