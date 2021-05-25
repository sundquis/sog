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
public class TestResult extends Test.Result {
	
	public static TestResult forSubject( Class<?> subjectClass ) {
		String label = Assert.nonNull( subjectClass ).getName();
		TestResult result = new TestResult( label );
		
		if ( !result.loadSubject( subjectClass ) ) { return result; }
		
		if ( !result.scanSubject() ) { return result; }
		
		
		return result;
	}



	
	private final List<String> errors = new ArrayList<String>();
	private final List<String> skips = new ArrayList<String>();
	
	private final List<TestMember> members = new ArrayList<TestMember>();
	private final Set<TestDecl> decls = new HashSet<TestDecl>();
	
	private Class<?> subjectClass;
	private String containerLocation;

	public TestResult( String label ) {
		super( label );
	}
	
	
	private void addError( Object... details ) {
		this.errors.add( Arrays.stream( details ).map( Strings::toString ).collect( Collectors.joining() ) );
	}
	
	private void addSkip( Object... details ) {
		this.skips.add( Arrays.stream( details ).map( Strings::toString ).collect( Collectors.joining() ) );
	}
		
	
	/** Return true if processing should continue */
	private boolean loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
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
				this.addError( this.subjectClass, " is unmarked." );
				return false;
			}
		}
	}
	
	
	private boolean scanSubject() {
		this.scanClass( this.subjectClass );
		this.members.stream().forEach( this::scanMember );
		
		return this.errors.size() == 0;
	}
	
	private void scanClass( Class<?> clazz ) {
		Arrays.stream( clazz.getDeclaredConstructors() ).map( TestMember::build ).forEach( this.members::add );
		Arrays.stream( clazz.getDeclaredFields() ).map( TestMember::build ).forEach( this.members::add );
		Arrays.stream( clazz.getDeclaredMethods() ).map( TestMember::build ).forEach( this.members::add );
		
		Arrays.stream( clazz.getDeclaredClasses() ).forEach( this::scanClass );
	}
	
	private void scanMember( TestMember member ) {
		if ( member.isSkipped() ) {
			if ( member.hasDecls() ) {
				this.addError( "Member ", member, " is marked for skipping and has ", member.getDecls().length, " declarations."  );
			} else {
				this.addSkip( "Skipping ", member, ": ", member.getSkipReason() );
			}
		} else {
			if ( member.hasDecls() ) {
				for ( Test.Decl decl : member.getDecls() ) {
					if ( !this.decls.add( new TestDecl( member, decl.value() ) ) ) {
						this.addError( "Duplicate test declaration: ", member, ": ", decl.value() );
					}
				}
			} else {
				if ( member.isRequired() ) {
					this.addError( "Untested member ", member, " is required by the current policy." );
				}
			}
		}
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
		Test.eval( Foo.class );
		
		System.out.println( "Done!" );
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
