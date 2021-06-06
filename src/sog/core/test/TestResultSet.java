/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * @author sundquis
 *
 */
public class TestResultSet extends Result {
	
	private long elapsedTime = 0L;
	
	private int passCount = 0;
	
	private int failCount = 0;

	private final List<Result> results = new ArrayList<Result>();
	
	public TestResultSet( String label ) {
		super( Assert.nonEmpty( label ) );
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

	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out ).println( this );
		
		out.increaseIndent();
		this.results.forEach( out::println );
		out.decreaseIndent();
	}
	

	public TestResultSet addResult( Result result ) {
		this.results.add( Assert.nonNull( result ) );
		this.elapsedTime += result.getElapsedTime();
		this.passCount += result.getPassCount();
		this.failCount += result.getFailCount();
		return this;
	}
	
	
	public TestResultSet addClass( Class<?> clazz ) {
		return this.addResult( TestResult.forSubject( Assert.nonNull( clazz ) ) );
	}
	
	
	public TestResultSet addClass( String className ) {
		Assert.nonEmpty( className );
		
		Class<?> clazz = null;
		try {
			clazz = ClassLoader.getSystemClassLoader().loadClass( className );
		} catch ( ClassNotFoundException ex ) {
			ex.printStackTrace();
		}
		return this.addClass( clazz );
	}
	
	
	public TestResultSet addClasses( Class<?>... clazzes ) {
		Arrays.stream( clazzes ).forEach( this::addClass );
		return this;
	}

	
	
	

	public static void testSourceDir( String ... components ) {
		Path sourcePath = Paths.get( "/", components );
		TestResultSet result = new TestResultSet( "DIR: " + sourcePath );
		
		try {
			Files.walk( sourcePath )
				.filter( p -> Files.isRegularFile( p ) )
				.map( p -> sourcePath.relativize( p ) )
				.map( Strings::relativePathToClassname )
				.forEach( result::addClass );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		result.print( new IndentWriter( System.out ) );
	}
	
	/*
	 *  Test all classes in the package identified by the sequence of name components
	 */
	public static void testPackage( String ... components ) {
		Path packagePath = Paths.get( "", components );
		TestResultSet result = new TestResultSet( "PACKAGE: " + Strings.relativePathToPackage( packagePath ) );

		Path sourcePath = App.get().sourceDirs().stream()
			.filter( p -> Files.exists( p.resolve( packagePath ) ) )
			.findAny()
			.orElse( null );
		Assert.nonNull( sourcePath );
		Path packageDir = sourcePath.resolve( packagePath );

		try {
			Files.list( packageDir )
				.filter( p -> Files.isRegularFile( p ) )
				.map( p -> sourcePath.relativize( p ) )
				.map( Strings::relativePathToClassname )
				.forEach( result::addClass );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		result.print( new IndentWriter( System.out ) );
	}

	/*
	 *  Test all classes in the package identified by the sequence of name components or any sub-package
	 */
	public static void testPackages( String ... components ) {
		Path packagePath = Paths.get( "", components );
		TestResultSet result = new TestResultSet( "PACKAGES: " + Strings.relativePathToPackage( packagePath ) );

		Path sourcePath = App.get().sourceDirs().stream()
			.filter( p -> Files.exists( p.resolve( packagePath ) ) )
			.findAny()
			.orElse( null );
		Assert.nonNull( sourcePath );
		Path packageDir = sourcePath.resolve( packagePath );
		
		try {
			Files.walk( packageDir )
				.filter( p -> Files.isRegularFile( p ) )
				.map( p -> sourcePath.relativize( p ) )
				.map( Strings::relativePathToClassname )
				.forEach( result::addClass );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		result.print( new IndentWriter( System.out ) );
	}

	
	
	
	
	

	public static void main(String[] args) {

		System.out.println();
		
		// TESTS
		//

		//testPackage( "sog", "core" );
		testPackages( "sog", "core" );
		//testSourceDir( "home", "sundquis", "book", "sog", "src" );


		System.out.println("\nDone!");

	}

}
