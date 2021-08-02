/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;


import java.io.IOException;
import java.lang.StackWalker.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author sundquis
 * 
 * Singleton used to expose access to application level resources:
 *
 */
public class App implements Runnable {
	
	private static App instance = null;
	
	/** Retrieve the singleton instance */
	@Test.Decl( "Is not null" )
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
		String rootDirName = Assert.nonEmpty( Property.get( "root", null, Property.STRING ) );
		this.root = Assert.rwDirectory( Path.of( rootDirName ) );
		this.description = Assert.nonEmpty( Property.get( "description",  "<none>",  Property.STRING ) );
		this.sourceDirs = Property.get( "source.dirs", Collections.<String>emptyList(), Property.LIST )
			.stream()
			.map( Path::of )
			.collect( Collectors.toList() );
		Assert.isTrue( ! this.sourceDirs.isEmpty() );

		this.objectsForShutdown = new LinkedList<OnShutdown>();
		Runtime.getRuntime().addShutdownHook( new Thread( this ) );
	}

	/** Root directory for all application resources */
	@Test.Decl( "Is not null" )
	@Test.Decl( "Is readable" )
	@Test.Decl( "Is wrieable" )
	public Path root() {
		return this.root;
	}

	@Test.Decl( "Is not empty" )
	@Test.Decl( "Is not null" )
	public String description() {
		return this.description;
	}
	
	@Test.Decl( "Is not empty" )
	@Test.Decl( "Is not null" )
	public List<Path> sourceDirs() {
		return this.sourceDirs;
	}
	
	@Test.Decl( "Throws assertion error for null class" )
	@Test.Decl( "Throws App Excetion for missing source dir" )
	@Test.Decl( "Returns non null" )
	@Test.Decl( "Returns readable" )
	@Test.Decl( "Returns writeable" )
	@Test.Decl( "Returns directory" )
	@Test.Decl( "Returns container for nested class" )
	@Test.Decl( "Returns container for nested nested class" )
	@Test.Decl( "Returns container for local class" )
	@Test.Decl( "Returns container for anonymous class" )
	@Test.Decl( "Returns container for nested local class" )
	@Test.Decl( "Returns container for nested anonymous class" )
	public Path sourceDir( Class<?> clazz ) {
		Assert.nonNull( clazz );

		Class<?> encl = clazz;
		while ( encl.getEnclosingClass() != null ) {
			encl = encl.getEnclosingClass();
		}

		String[] components = encl.getName().split( "\\." );
		components[components.length-1] += ".java";
		Path relName = Path.of( "",  components );

		return this.sourceDirs().stream()
			.filter( p -> Files.exists( p.resolve( relName ) ) )
			.findAny()
			.orElseGet( () -> { Fatal.error( "No source directory for " + clazz ); return null; } );
	}
		
	@Test.Decl( "Throws assertion error for null class" )
	@Test.Decl( "Throws App Excetion for missing source file" )
	@Test.Decl( "Returns non null" )
	@Test.Decl( "Returns readable" )
	@Test.Decl( "Returns container for nested class" )
	@Test.Decl( "Returns container for nested nested class" )
	@Test.Decl( "Returns container for local class" )
	@Test.Decl( "Returns container for anonymous class" )
	@Test.Decl( "Returns container for nested local class" )
	@Test.Decl( "Returns container for nested anonymous class" )
	public Path sourceFile( Class<?> clazz ) {
		Assert.nonNull( clazz );

		Class<?> encl = clazz;
		while ( encl.getEnclosingClass() != null ) {
			encl = encl.getEnclosingClass();
		}

		String[] components = encl.getName().split( "\\." );
		components[components.length-1] += ".java";
		Path relName = Path.of( "",  components );

		return this.sourceDirs().stream()
			.map( p -> p.resolve( relName ) )
			.filter( p -> Files.exists( p ) )
			.findAny()
			.orElseGet( () -> { Fatal.error( "No source file for " + clazz ); return null; } );
	}
	
	
	public Stream<String> classesInPackage( Class<?> clazz ) {
		final Path sourceDir = this.sourceDir( clazz );
		final Path packageDir = this.sourceFile( clazz ).getParent();
		
		try {
			return Files.list( packageDir )
				.filter( Files::isRegularFile )
				.map( sourceDir::relativize )
				.map( Strings::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	
	
	public Stream<String> classesUnderDir( Path sourceDir ) {
		try {
			return Files.walk( sourceDir )
				.filter( Files::isRegularFile )
				.map( sourceDir::relativize )
				.map( Strings::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	
	
	
	/** For objects that require clean-up before shutdown. */
	public static interface OnShutdown {
		public void terminate();
	}

	/** Register for shutdown termination */
	@Test.Decl( "Throws assertion error for null" )
	@Test.Decl( "Registers hook" )
	public void terminateOnShutdown( OnShutdown os ) {
		this.objectsForShutdown.add( Assert.nonNull( os ) );
	}

	/** @see java.lang.Runnable#run() */
	@Override
	@Test.Decl( "Calls terminate on shutdown" )
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
	
	// FIXME: Test
	public static class Location {
		
		private final String className;
		private final String methodName;
		private final String fileName;
		private final int lineNo;
		
		public Location( StackWalker.StackFrame sf ) {
			Assert.nonNull( sf );
			
			this.className = sf.getClassName();
			this.methodName = sf.getMethodName();
			this.fileName = sf.getFileName();
			this.lineNo = sf.getLineNumber();
		}
		
		public Location( StackTraceElement ste ) {
			Assert.nonNull( ste );
			
			this.className = ste.getClassName();
			this.methodName = ste.getMethodName();
			this.fileName = ste.getFileName();
			this.lineNo = ste.getLineNumber();
		}
		
		@Override
		public String toString() {
			String[] comps = this.className.split("\\.");
			//comps[comps.length-1] = ".";  // This stopped working 7/25/21
			//return String.join(".",  comps) + "(" + this.fileName + ":" + this.lineNo + ") in " + this.methodName;
			comps[comps.length-1] = " ";
			return String.join(".",  comps) + "(" + this.fileName + ":" + this.lineNo + ") in " + this.methodName;
		}
	}

	// FIXME: Document
	/**
	 * Return a file name and line number pointer to the calling location of an executing program.
	 * If the calling stack does not have the requested depth, an AppException is thrown.
	 */
	public Stream<String> getLocation() {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.map( Location::new )
			.map( Location::toString )
			.collect( Collectors.toList() )
		).stream();
	}
	
	
	
	public Stream<String> getLocation( String prefix ) {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.filter( sf -> sf.getClassName().startsWith( prefix ) )
			.map( Location::new )
			.map( Location::toString )
			.collect( Collectors.toList() )
		).stream();
	}
	
	

	public Stream<String> getLocation( Throwable th ) {
		return Arrays.stream( th.getStackTrace() )
			.map( Location::new )
			.map( Location::toString );
	}
	

	public Class<?> getCallingClass( int offset ) {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
			s -> s.skip(offset).findFirst().get()
		).getDeclaringClass();
	}
	
	
	public String getCallingMethod( int offset ) {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk(
			s -> s.skip( offset ).findFirst().get()
		).getMethodName();
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
		//Other.a();
		
		App.get().classesUnderDir( Path.of( "/", "home", "sundquis", "book", "sog", "src" ) ).forEach( System.out::println );
		//App.get().classesInPackage( test.sog.core.test.TestResultTest.class ).forEach( System.out::println );
	}

	
}
