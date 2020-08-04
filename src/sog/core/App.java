/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;


import java.lang.StackWalker.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sundquis
 * 
 * Singleton used to expose access to application level resources:
 *
 */
public class App implements Runnable {
	
	private static App instance = null;
	
	/** Retrieve the singleton instance */
	@TestOrig.Decl( "Is not null" )
	public static App get() {
		if ( App.instance == null ) {
			synchronized ( App.class ) {
				if ( App.instance == null ) {
					App.instance = new App();
				}
			}
		}
		return Assert.nonNull( App.instance );
	}

	private final Path root;
	
	private final String description;
	
	private final List<Path> sourceDirs;
	
	private final List<OnShutdown> objectsForShutdown;
		
	private App() {
		String rootDirName = Property.get( "root", null, Property.STRING );
		Assert.nonEmpty( rootDirName );
		this.root = Assert.rwDirectory( Paths.get( rootDirName ) );

		this.description = Assert.nonEmpty( Property.get( "description",  "<none>",  Property.STRING ) );
		
		this.sourceDirs = Property.get( "source.dirs", Collections.<String>emptyList(), Property.LIST )
			.stream()
			.map( Paths::get )
			.collect( Collectors.toList() );
		Assert.isTrue( ! this.sourceDirs.isEmpty() );
		
		this.objectsForShutdown = new LinkedList<OnShutdown>();
		Runtime.getRuntime().addShutdownHook( new Thread( this ) );
	}

	/** Root directory for all application resources */
	@TestOrig.Decl( "Is not null" )
	@TestOrig.Decl( "Is readable" )
	@TestOrig.Decl( "Is wrieable" )
	public Path root() {
		return this.root;
	}

	@TestOrig.Decl( "Is not empty" )
	@TestOrig.Decl( "Is not null" )
	public String description() {
		return this.description;
	}
	
	@TestOrig.Decl( "Is not empty" )
	@TestOrig.Decl( "Is not null" )
	public List<Path> sourceDirs() {
		return this.sourceDirs;
	}
	
	@TestOrig.Decl( "Throws assertion error for null class" )
	@TestOrig.Decl( "Throws App Excetion for missing source dir" )
	@TestOrig.Decl( "Returns non null" )
	@TestOrig.Decl( "Returns readable" )
	@TestOrig.Decl( "Returns writeable" )
	@TestOrig.Decl( "Returns directory" )
	@TestOrig.Decl( "Returns container for nested class" )
	@TestOrig.Decl( "Returns container for nested nested class" )
	@TestOrig.Decl( "Returns container for local class" )
	@TestOrig.Decl( "Returns container for anonymous class" )
	@TestOrig.Decl( "Returns container for nested local class" )
	@TestOrig.Decl( "Returns container for nested anonymous class" )
	public Path sourceDir( Class<?> clazz ) {
		Assert.nonNull( clazz );

		Class<?> encl = clazz;
		while ( encl.getEnclosingClass() != null ) {
			encl = encl.getEnclosingClass();
		}

		String[] components = encl.getName().split( "\\." );
		components[components.length-1] += ".java";
		Path relName = Paths.get( "",  components );

		return this.sourceDirs().stream()
			.filter( p -> Files.exists( p.resolve( relName ) ) )
			.findAny()
			.orElseGet( () -> { Fatal.error( "No source directory for " + clazz ); return null; } );
	}
	
	@TestOrig.Decl( "Throws assertion error for null class" )
	@TestOrig.Decl( "Throws App Excetion for missing source file" )
	@TestOrig.Decl( "Returns non null" )
	@TestOrig.Decl( "Returns readable" )
	@TestOrig.Decl( "Returns container for nested class" )
	@TestOrig.Decl( "Returns container for nested nested class" )
	@TestOrig.Decl( "Returns container for local class" )
	@TestOrig.Decl( "Returns container for anonymous class" )
	@TestOrig.Decl( "Returns container for nested local class" )
	@TestOrig.Decl( "Returns container for nested anonymous class" )
	public Path sourceFile( Class<?> clazz ) {
		Assert.nonNull( clazz );

		Class<?> encl = clazz;
		while ( encl.getEnclosingClass() != null ) {
			encl = encl.getEnclosingClass();
		}

		String[] components = encl.getName().split( "\\." );
		components[components.length-1] += ".java";
		Path relName = Paths.get( "",  components );

		return this.sourceDirs().stream()
			.map( p -> p.resolve( relName ) )
			.filter( p -> Files.exists( p ) )
			.findAny()
			.orElseGet( () -> { Fatal.error( "No source file for " + clazz ); return null; } );
	}
	
	/** For objects that require clean-up before shutdown. */
	@TestOrig.Skip
	public static interface OnShutdown {
		public void terminate();
	}

	/** Register for shutdown termination */
	@TestOrig.Decl( "Throws assertion error for null" )
	@TestOrig.Decl( "Registers hook" )
	public void terminateOnShutdown( OnShutdown os ) {
		this.objectsForShutdown.add( Assert.nonNull( os ) );
	}

	/** @see java.lang.Runnable#run() */
	@Override
	@TestOrig.Decl( "Calls terminate on shutdown" )
	public void run() {
		for ( OnShutdown os : this.objectsForShutdown ) {
			try {
				os.terminate();
			} catch ( Throwable th ) {
				System.out.println( "Exception during shutdown: " + th.getMessage() );
				th.printStackTrace();
			}
		}
	}

	/**
	 * Return a file name and line number pointer to the calling location of an executing program.
	 * If the calling stack does not have the requested depth, an AppException is thrown.
	 * 
	 * @param offset, the position on the calling stack. The call to getLocation has offset 0
	 * @return (fileName:lineNo) link to calling location
	 */
	public String getFileLocation( int offset ) {
		return this.getFileLocation( offset,  1 );
	}
	
	public String getFileLocation( int offset, int count ) {
		return StackWalker.getInstance().walk( s -> s
			.skip( offset )
			.limit( count )
			.map( sf -> "(" + sf.getFileName() + ":" + sf.getLineNumber() + ")" )
			.collect( Collectors.joining(", ") )
		);
	}

	public Class<?> getCallingClass( int offset ) {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
			s -> s.skip(offset).findFirst().get()
		).getDeclaringClass();
	}
	
	
	
	// FIXME: Convert to proper testing
	public static class Inner {
		public static void a() {
			System.out.println( App.get().getCallingClass(0));
			System.out.println( App.get().getCallingClass(1));
			System.out.println( App.get().getCallingClass(2));
			System.out.println( App.get().getCallingClass(3));
		}
	}
	
	public static class Other {
		public static void a() { Inner.a(); }
	}
	
	public static void main( String[] args ) {
		Other.a();
	}

	
}
