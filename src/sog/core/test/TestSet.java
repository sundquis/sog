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
package sog.core.test;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;
import sog.util.Concurrent;
import sog.util.IndentWriter;

/**
 * 	Responsibilities:
 * 		Aggregate results for multiple classes into a single Result.
 * 
 * 	Structure:
 * 		Extends Result to represent the results after running the test case.
 * 		Holds a SortedSet of TestSubject instances sorted by classname.
 * 		
 * 	Services:
 *  	Static helper methods for assembling sets of results by package or directory tree.
 */
@Test.Subject( "test." )
public class TestSet extends Result {
	

	private static Concurrent concurrent = null;

	/* All TestSet instances share a common concurrent */
	private static Concurrent getConcurrent() {
		if ( TestSet.concurrent == null ) {
			synchronized( TestSet.class ) {
				if ( TestSet.concurrent == null ) {
					TestSet.concurrent = new Concurrent( "TestSet", 6 );
				}
			}
		}
		return Assert.nonNull( TestSet.concurrent );
	}
	
	
	
	private long elapsedTime = 0L;
	
	private int passCount = 0;
	
	private int failCount = 0;
	
	private boolean hasRun = false;
	
	private final List<String> skippedClasses = new ArrayList<String>();

	private final SortedSet<TestSubject> testSubjects;
	
	/**
	 * Constructs an empty TestSet. The required label identifies the context of the set of test results
	 * The various "add" methods aggregate tests for various categories of classes.
	 * 
	 * @param label
	 */
	@Test.Decl( "Throws AssertionError for empty label" )
	@Test.Decl( "Throws AssertionError for null label" )
	public TestSet( String label ) {
		super( Assert.nonEmpty( label ) );

		this.testSubjects = new TreeSet<TestSubject>();
	}


	@Override
	@Test.Decl( "Value is the total elapsed time for all tests" )
	public long getElapsedTime() {
		return this.elapsedTime;
	}

	@Override
	@Test.Decl( "Value is the sum of all passing weights for all tests" )
	public int getPassCount() {
		return this.passCount;
	}


	@Override
	@Test.Decl( "Value is the sum of all failing weights for all tests" )
	public int getFailCount() {
		return this.failCount;
	}
	
	private void addSkippedClass( String className, String reason ) {
		this.skippedClasses.add( className + ": " + reason );
	}

	@Override
	protected void run() {
		if ( this.hasRun ) { return; }
		
		Function<TestSubject, TestSubject> mapper = (ts) -> { ts.run(); return ts; };
		
		if ( Result.concurrentSets() ) {
			TestSet.getConcurrent()
				.map( mapper, this.testSubjects.stream() )
				.forEach( this::addResult );
		} else {
			this.testSubjects.stream().map( mapper ).forEach( this::addResult );
		}

		this.hasRun = true;
	}
	
	private void addResult( Result result ) {
		this.elapsedTime += result.getElapsedTime();
		this.passCount += result.getPassCount();
		this.failCount += result.getFailCount();
	}
	

	/**
	 * Add results corresponding to the given subject class.
	 * 
	 * @param clazz
	 * @return		This TestSet instance to allow chaining.
	 */
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Adds one TestSubject if not skipped" )
	@Test.Decl( "Skipped classes not added" )
	@Test.Decl( "Return is this TestSet instance to allow chaining" )
	public TestSet addClass( Class<?> clazz ) {
		Test.Skip skip = Assert.nonNull( clazz ).getDeclaredAnnotation( Test.Skip.class );
		if ( skip == null ) {
			this.testSubjects.add( TestSubject.forSubject( clazz ) );
		} else {
			this.addSkippedClass( TestMember.getSimpleName( clazz ), skip.value() );
		}
		return this;
	}
	

	/**
	 * Add results corresponding to the named class.
	 * 
	 * @param className
	 * @return		This TestSet instance to allow chaining.
	 */
	@Test.Decl( "Throws AssertionError for empty class name" )
	@Test.Decl( "Throws AssertionError for null class name" )
	@Test.Decl( "Records error message if class is not found" )
	@Test.Decl( "Adds one TestSubject" )
	@Test.Decl( "Return is this TestSet instance to allow chaining" )
	public TestSet addClass( String className ) {
		try {
			Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass( Assert.nonEmpty( className ) );
			this.addClass( clazz );
		} catch ( ClassNotFoundException ex ) {
			this.addSkippedClass( className, ex.getMessage() );
		}
		
		return this;
	}
	

	/**
	 * Add all results corresponding to all classes in the given stream.
	 * 
	 * @param classnames
	 * @return		This TestSet instance to allow chaining.
	 */
	@Test.Decl( "Throws AssertionError for null class names stream" )
	@Test.Decl( "Adds one TestSubject for each valid class name" )
	@Test.Decl( "Return is this TestSet instance to allow chaining" )
	public TestSet addClasses( Stream<String> classnames ) {
		Assert.nonNull( classnames ).forEach( this::addClass );
		
		return this;
	}
	

	/**
	 * Construct a set of test results corresponding to all subject classes in the
	 * source code directory.
	 * 
	 * @return		The newly constructed TestSet
	 */
	@Test.Decl( "Aggregates TestSubject instances for every class under every source directory" )
	@Test.Decl( "Return is not null" )
	public static TestSet forAllSourceDirs() {
		final TestSet set = new TestSet( "ALL:\t" 
			+ new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
		
		Consumer<Path> action = p -> { set.addClasses( App.get().classesUnderDir( p ) ); };
		App.get().sourceDirs().forEach( action );

		return set;
	}
	

	/**
	 * Construct a set of test results corresponding to all subject classes in the given source directory.
	 * 
	 * @param sourceDir
	 * @return		The newly constructed TestSet
	 */
	@Test.Decl( "Throws AssertionError for null source path" )
	@Test.Decl( "Aggregates TestSubject instances for every class under the given source directory" )
	@Test.Decl( "Return is not null" )
	public static TestSet forSourceDir( Path sourceDir ) {
		TestSet set = new TestSet( "DIR:\t" + Assert.nonNull( sourceDir ) );
		
		set.addClasses( App.get().classesUnderDir( sourceDir ) );
		
		return set;
	}
	
	/**
	 * Construct a set of test results corresponding to all packages and sub-packages relative
	 * to the given subdirectory of a source directory.
	 * 
	 * @param sourceDir
	 * @param sub
	 * @return		The newly constructed TestSet
	 */
	@Test.Decl( "Throws AssertionError for null source directory" )
	@Test.Decl( "Throws AssertionError for null sub-directory" )
	@Test.Decl( "Aggregates TestSubject instances for every class under the given directory" )
	@Test.Decl( "Return is not null" )
	public static TestSet forPackages( Path sourceDir, Path sub ) {
		TestSet set = new TestSet( "PKGS:\t" + Assert.nonNull( sub ) );
		
		set.addClasses( App.get().classesUnderDir( Assert.nonNull( sourceDir ), sub ) );
		
		return set;
	}

	/**
	 * Construct a set of test results corresponding to subject classes in the same package
	 * as the given class.
	 * 
	 * @param clazz
	 * @return		The newly constructed TestSet
	 */
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Aggregates TestSubject instances for every class in the same package as the given class" )
	@Test.Decl( "Return is not null" )
	public static TestSet forPackage( Class<?> clazz ) {
		TestSet set = new TestSet( "PKG:\t" + Assert.nonNull( clazz ).getPackageName() );

		set.addClasses( App.get().classesInPackage( clazz ) );
		
		return set;
	}

	
	/**
	 * Used to show detailed results.
	 */
	@Override
	@Test.Decl( "Throws AssertionError for null writer" )
	@Test.Decl( "Includes summary for each TestSubject" )
	@Test.Decl( "Includes messages for each bad classname" )
	@Test.Decl( "Includes messages for each skipped class" )
	@Test.Decl( "Results are printed in alphabetaical order" )
	public void print( IndentWriter out ) {
		if ( !this.hasRun ) {
			this.run();
		}
		
		// Mandatory summary
		Assert.nonNull( out ).println().println( this.toString() );

		// Always show list of skipped classes
		if ( this.skippedClasses.size() > 0 ) {
			out.increaseIndent();
			out.println().println( "SKIPPED CLASSES:" );
			out.increaseIndent();
			this.skippedClasses.forEach( out::println );
			out.decreaseIndent();
			out.decreaseIndent();
		}

		// If showDetails is false, print subject summaries and be done
		if ( !Result.showDetails() ) {
			out.println().increaseIndent();
			this.testSubjects.stream().map( Object::toString ).forEach( out::println );
			out.decreaseIndent();
			return;
		}

		// If showDetails is true, each TestSubject adds its details
		this.testSubjects.stream().forEach( out::println );
	}

	
}
