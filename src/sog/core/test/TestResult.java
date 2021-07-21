/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

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

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestResult extends Result {
	
	public static TestResult forSubject( Class<?> subjectClass ) {
		TestResult result = new TestResult( Assert.nonNull( subjectClass ).getName() );
		
		result.loadSubject( subjectClass );
		result.scanSubject();

		result.loadContainer();
		result.scanContainer();
		
		result.runTests();
		
		return result;
	}




	/** The subject class for which this TestResult holds results. Set by loadSubject(). */
	private Class<?> subjectClass;
	
	/**
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
	
	/**  */
	private final Map<String, TestDecl> declMap = new TreeMap<String, TestDecl>();
	
	/** The Test.Container holding implementations of test methods. Constructed by loadContainer() */
	private Test.Container container;
	
	private final SortedSet<TestCase> testCases = new TreeSet<TestCase>();
	
	private int passCount = 0;
	
	private int failCount = 0;
	
	private long elapsedTime = 0L;
	
	
	private TestResult( String label ) {
		super( label );
	}
	
	
	
	private class Err {
		private Err addDetail( String description, Object target ) {
			TestResult.this.errors.add( "    " + description + ": " + target.toString() );
			return this;
		}
		private Err addDetail( String description, Throwable t ) {
			this.addDetail( description, t.toString() );
			App.get().getLocation( t ).map( s -> "        " + s ).forEach( TestResult.this.errors::add );
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
		
	

	private void loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
		if ( subjectClass.isSynthetic() || subjectClass.getEnclosingClass() != null ) {
			this.addError( "Subject class is not a top-level class", subjectClass );
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
		} else {
			if ( isSubject ) {
				this.containerLocation = subj.value();
			} else {
				this.addError( "Subject class is not annotated", this.subjectClass );
			}
		}
	}
	
	
	private void scanSubject() {
		this.scanClass( this.subjectClass ).forEach( this::scanMember );
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
				this.addError( "Member has declarations and is marked for skipping", member);
			} else {
				this.addSkip( member, member.getSkipReason() );
			}
		} else {
			if ( member.hasDecls() ) {
				member.getDecls().forEach( this::addDecl );
			} else {
				if ( member.isRequired() ) {
					this.addError( "Untested member required by the current policy", member );
				}
			}
		}
	}

	private void addDecl( TestDecl decl ) {
		if ( this.declMap.containsKey( decl.getKey() ) ) {
			this.addError( "Duplicate declaration", "" )
				.addDetail( "Member", decl.getMemberName() )
				.addDetail( "Description", decl.getDescription() );
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
			this.container = (Test.Container) clazz.getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			this.addError( "Cannot construct container", this.subjectClass )
				.addDetail( "Container name", containerClassName )
				.addDetail( "Exception", e );
			return;
		}
		
		if ( !this.container.getSubjectClass().equals( this.subjectClass ) ) {
			this.addError( "Container names the wrong subject", this.container.getClass() )
				.addDetail( "Should be", this.subjectClass )
				.addDetail( "Got", this.container.getSubjectClass() );
		}
	}
	
	
	private void scanContainer() {
		if ( this.container == null ) {
			return;
		}
		
		Arrays.stream(  this.container.getClass().getDeclaredMethods() )
			.map( TestImpl::forMethod )
			.filter( ti -> ti != null )
			.forEach( this::addImpl );
	}
	
	private void addImpl( TestImpl impl ) {
		TestDecl decl = this.declMap.get( impl.getKey() );
		if ( decl == null ) {
			this.addError( "Orphaned test implementation", this.containerLocation )
				.addDetail( "Member", impl.getMemberName() )
				.addDetail( "Description", impl.getDescription() );		
		} else {
			if ( decl.setImpl( impl ) ) {
				this.testCases.add( new TestCase( impl, this.container ) );
			} else {
				this.addError( "Duplicate test implementation", this.containerLocation )
					.addDetail( "Member", impl.getMemberName() )
					.addDetail( "Description", impl.getDescription() );
			}
		}		
	}
	
	
	private void runTests() {
		if ( this.errors.size() > 0 ) {
			return;
		}
		
		Consumer<TestCase> process = tc -> {
			tc.run();
			this.elapsedTime += tc.getElapsedTime();
			this.passCount += tc.getPassCount();
			this.failCount += tc.getFailCount();
		};
		this.testCases.forEach( process );

		this.container.afterAll().exec();
	}
	

	@Override
	public void print( IndentWriter out ) {
		out.println( this );

		out.increaseIndent();
		
		if ( this.errors.size() > 0 ) {
			out.println();
			out.println( "ERRORS:" );
			out.increaseIndent();
			this.errors.stream().forEach( out::println );
			out.decreaseIndent();
		} else {
			out.println();
			out.println( "RESULTS:" );
			out.increaseIndent();
			this.testCases.stream().forEach( tc -> tc.print( out ) );
			out.decreaseIndent();
		}
		
		if ( this.skips.size() > 0 ) {
			out.println();
			out.println( "SKIPS:" );
			out.increaseIndent();
			this.skips.stream().forEach( out::println );
			out.decreaseIndent();
		}
		
		List<TestDecl> decls = this.declMap.values().stream()
			.filter( TestDecl::unimplemented ).collect( Collectors.toList() );
		if ( decls.size() > 0 ) {
			out.println();
			out.println( "STUBS:" );
			out.increaseIndent();
			decls.stream().forEach( d -> d.print( out ) );
			out.decreaseIndent();
		}

		out.decreaseIndent();
	}
	
	@Override
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	@Override
	public int getPassCount() {
		return this.passCount;
	}

	@Override
	public int getFailCount() {
		return this.failCount;
	}


	
	
	
	public static void main( String[] args ) {
		Test.eval( TestResult.class );

		System.out.println( "Done!" );
	}
	
	


}
