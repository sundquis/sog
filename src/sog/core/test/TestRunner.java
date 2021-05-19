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
import java.util.Arrays;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class TestRunner {

	
	/*
	 *  Test all classes in the source directory identified by the sequence of name components
	 */
	public static void testSourceDir( String ... components ) {
		Path sourcePath = Paths.get( "/", components );
		try {
			Files.walk( sourcePath )
				.filter( p -> Files.isRegularFile( p ) )
				.map( p -> sourcePath.relativize( p ) )
				.map( Strings::relativePathToClassname )
				.forEach( TestRunner::testClassName );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	/*
	 *  Test all classes in the package identified by the sequence of name components
	 */
	public static void testPackage( String ... components ) {
		Path packagePath = Paths.get( "", components );
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
				.forEach( TestRunner::testClassName );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	/*
	 *  Test all classes in the package identified by the sequence of name components or any sub-package
	 */
	public static void testPackages( String ... components ) {
		Path packagePath = Paths.get( "", components );
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
				.forEach( TestRunner::testClassName );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	/*
	 * Test the named class if it's a Container or declares any inner Containers
	 */
	public static void testClassName( String className ) {
		Assert.nonEmpty( className );
		
		Class<?> clazz = null;
		try {
			clazz = ClassLoader.getSystemClassLoader().loadClass( className );
		} catch ( ClassNotFoundException ex ) {
			ex.printStackTrace();
		}
		
		testClass( clazz );
		Arrays.stream( clazz.getDeclaredClasses() ).forEach( TestRunner::testClass );
	}
	
	//@SuppressWarnings("unchecked")
	public static void testClass( Class<?> clazz ) { 
		if ( clazz != null && !clazz.isInterface()  && Test.Implementation.class.isAssignableFrom( clazz ) ) {
			//new TestOrig( (Class<TestContainer>) clazz );
		}
	}
	
	

	
	
	
	

	public static void main(String[] args) {

		System.out.println();
		
		// TESTS
		//
		//Test.verbose();
		//Test.summaryOnly();
		//Test.noWarnings();
		//Test.noStubs();

		//testPackage( "test", "util" );
		//testPackages( "test", "persist" );
		//testPackage( "test", "persist", "model" );
		testSourceDir( "home", "sundquis", "book", "sog", "src" );


		System.out.println("\nDone!");

	}
}
