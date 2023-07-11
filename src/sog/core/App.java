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
package sog.core;


import java.io.IOException;
import java.lang.StackWalker.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Singleton used to expose access to application level resources:
 * 		Description of the current application
 * 		Date and time the application started
 * 		Location of the root (home) directory of the application
 * 		Services related to locating source files and directories
 * 		Set of classes in a package, directory, or directory tree
 * 		Services related to cleanup code to be run at shutdown
 * 		Location services for providing links to code location
 * 		Methods for returning calling class or method
 * 		
 * 
 */
@Test.Subject( "test." )
public class App implements Runnable {
	
	private static App instance = null;
	
	/** Retrieve the singleton instance */
	@Test.Decl( "Is not null" )
	@Test.Decl( "Value is unique" )
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
	
	private final String startDateTime;
	
	private App() {
		this.root = Assert.rwDirectory( Property.get( "root", null, Path::of ) );
		this.description = Assert.nonEmpty( Property.get( "description",  "<none>",  Parser.STRING ) );
		this.sourceDirs = Property.get( "source.dirs", List.of(), Parser.LIST )
			.stream()
			.map( Path::of )
			.collect( Collectors.toList() );
		Assert.isTrue( ! this.sourceDirs.isEmpty() );
		this.objectsForShutdown = new LinkedList<OnShutdown>();
		Runtime.getRuntime().addShutdownHook( new Thread( this ) );
		
		DateFormat fmt = new SimpleDateFormat( "yyyy.MM.dd#HH.mm.ss" );
		this.startDateTime = fmt.format( new Date() );
	}

	/** Root directory for all application resources */
	@Test.Decl( "Is not null" )
	@Test.Decl( "Is readable" )
	@Test.Decl( "Is writeable" )
	public Path root() {
		return this.root;
	}

	/** A brief description of the application. */
	@Test.Decl( "Is not empty" )
	@Test.Decl( "Is not null" )
	public String description() {
		return this.description;
	}

	/** Date and time that the application was started. */
	@Test.Decl( "Return is non-empty" )
	@Test.Decl( "Return indicates the date that the application started" )
	@Test.Decl( "Return indicates the time that the application started" )
	public String startDateTime() {
		return this.startDateTime;
	}
	
	/** A list of path instances locating the roots of the source directories. */
	@Test.Decl( "Is not empty" )
	@Test.Decl( "Is not null" )
	public List<Path> sourceDirs() {
		return this.sourceDirs;
	}
	
	/**
	 * Find the directory that is the root of the source code tree for the given non-null class.
	 * The class cannot be a secondary class, but can be local or nested.
	 * 
	 * @param clazz	The non-null class object.
	 * @return	The non-null Path to the source directory.
	 */
	@Test.Decl( "Throws assertion error for null class" )
	@Test.Decl( "Throws AppException for missing source dir" )
	@Test.Decl( "Throws AppException for secondary class" )
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
		Class<?> encl = Assert.nonNull( clazz );
		while ( encl.getEnclosingClass() != null ) {
			encl = encl.getEnclosingClass();
		}

		String[] components = encl.getName().split( "\\." );
		components[components.length-1] += ".java";
		Path relName = Path.of( "", components );

		return this.sourceDirs().stream()
			.filter( p -> Files.exists( p.resolve( relName ) ) )
			.findAny()
			.orElseGet( () -> { Fatal.error( "No source directory for " + clazz ); return null; } );
	}

	/**
	 * Find the source file where the given non-null class is defined.
	 * The class cannot be a secondary class, but can be local or nested.
	 * 
	 * @param clazz	The non-null class object.
	 * @return	The non-null Path to the source file.
	 */
	@Test.Decl( "Throws assertion error for null class" )
	@Test.Decl( "Throws AppException for missing source file" )
	@Test.Decl( "Throws AppException for secondary class" )
	@Test.Decl( "Returns non null" )
	@Test.Decl( "Returns readable" )
	@Test.Decl( "Returns container for nested class" )
	@Test.Decl( "Returns container for nested nested class" )
	@Test.Decl( "Returns container for local class" )
	@Test.Decl( "Returns container for anonymous class" )
	@Test.Decl( "Returns container for nested local class" )
	@Test.Decl( "Returns container for nested anonymous class" )
	public Path sourceFile( Class<?> clazz ) {
		Class<?> encl = Assert.nonNull( clazz );
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
	

	/**
	 * A predicate for identifying java source files.
	 */
	@Test.Decl( "True for every java source file" )
	@Test.Decl( "False for null paths" )
	@Test.Decl( "False for empty paths" )
	@Test.Decl( "False for directories" )
	@Test.Decl( "False for non-java files" )
	public final Predicate<Path> SOURCE_FILE = p -> 
		p != null && Files.isRegularFile( p ) && p.getFileName().toString().endsWith( ".java" );

	/**
	 * Produce the fully qualified class name corresponding to the path, relative
	 * to a source code directory, of a java source file.
	 * 
	 * @param relativePath	The non-null relative path identifying a java source file.
	 * @return	The corresponding class name.
	 */
	@Test.Decl( "Throws AssertionError for null realtivePath" )
	@Test.Decl( "Throws AssertionError if not a java source file" )
	@Test.Decl( "Agrees with classname of top-level classes" )
	public String relativePathToClassname( Path relativePath ) {
		Assert.nonNull( relativePath );
		String result = StreamSupport.stream( relativePath.spliterator(), false )
			.map( Object::toString )
			.collect( Collectors.joining( "." ) );
		Assert.isTrue( result.endsWith( ".java" ) );
		return result.replace( ".java",  "" );
	}
		

	/**
	 * Return a stream of fully qualified classnames for the top-level classes in the
	 * same package as the given class.
	 * 
	 * @param clazz
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are non-empty" )
	@Test.Decl( "Elements are classnames for classes in the package of the given class" )
	@Test.Decl( "One element for each class in the package" )
	@Test.Decl( "Secondary classes are not included" )
	@Test.Decl( "Non-source files are excluded" )
	public Stream<String> classesInPackage( Class<?> clazz ) {
		Assert.nonNull( clazz );
		
		final Path sourceDir = this.sourceDir( clazz );
		final Path packageDir = this.sourceFile( clazz ).getParent();
		
		try {
			return Files.list( packageDir )
				.filter( this.SOURCE_FILE )
				.map( sourceDir::relativize )
				.map( this::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	

	/**
	 * Return a stream of fully qualified class names for all classes in the given source directory or
	 * its sub-directories.
	 * 
	 * @param sourceDir
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null class source directory" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are non-empty" )
	@Test.Decl( "Elements are classnames for classes in the directory or sub-directories of the given directory" )
	@Test.Decl( "One element for each class under the given directory" )
	@Test.Decl( "Non-source files are excluded" )
	@Test.Decl( "Secondary classes are not included" )
	public Stream<String> classesUnderDir( Path sourceDir ) {
		try {
			return Files.walk( Assert.nonNull( sourceDir ) )
				.filter( this.SOURCE_FILE )
				.map( sourceDir::relativize )
				.map( this::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	

	/**
	 * Return a stream of fully qualified class names for all classes in the given subdirectory 
	 * of a source directory, or its sub-directories.
	 * 
	 * @param sourceDir
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null class source directory" )
	@Test.Decl( "Throws AssertionError for null sub-directory" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are non-empty" )
	@Test.Decl( "Elements are classnames for classes in the directory or sub-directories of the given directory" )
	@Test.Decl( "Non-source files are excluded" )
	@Test.Decl( "Secondary classes are not included" )
	public Stream<String> classesUnderDir( Path sourceDir, Path sub ) {
		try {
			return Files.walk( Assert.nonNull( sourceDir ).resolve( Assert.nonNull( sub ) ) )
				.filter( this.SOURCE_FILE )
				.map( sourceDir::relativize )
				.map( this::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}

	
	/** For objects that require clean-up before shutdown. */
	@FunctionalInterface
	public static interface OnShutdown {
		public void terminate();
	}

	/** Register for shutdown termination */
	@Test.Decl( "Throws assertion error for null" )
	public void terminateOnShutdown( OnShutdown os ) {
		this.objectsForShutdown.add( Assert.nonNull( os ) );
	}

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

	/**
	 * The string representations of instances contain links to the file and line number
	 * corresponding to an execution point.
	 */
	public static class Location {
		
		private final String className;
		private final String methodName;
		private final String fileName;
		private final int lineNo;

		@Test.Decl( "Throws AssertionError for null frame" )
		public Location( StackWalker.StackFrame sf ) {
			Assert.nonNull( sf );
			
			this.className = sf.getClassName();
			this.methodName = sf.getMethodName();
			this.fileName = sf.getFileName();
			this.lineNo = sf.getLineNumber();
		}
		
		@Test.Decl( "Throws AssertionError for null element" )
		public Location( StackTraceElement ste ) {
			Assert.nonNull( ste );
			
			this.className = ste.getClassName();
			this.methodName = ste.getMethodName();
			this.fileName = ste.getFileName();
			this.lineNo = ste.getLineNumber();
		}
		
		@Override
		@Test.Decl( "Return is non-empty" )
		public String toString() {
			String[] comps = this.className.split("\\.");
			//comps[comps.length-1] = ".";  // This stopped working 7/25/21
			//return String.join(".",  comps) + "(" + this.fileName + ":" + this.lineNo + ") in " + this.methodName;
			comps[comps.length-1] = " ";
			return String.join(".",  comps) + "(" + this.fileName + ":" + this.lineNo + ") in " + this.methodName;
		}
	}

	
	/**
	 * Return a stream of file locations corresponding to the stack of an executing program.
	 */
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are file links" )
	@Test.Decl( "Links work for secondary classes" )
	@Test.Decl( "Elements correspond to the calling stack" )
	public Stream<String> getLocation() {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.map( Location::new )
			.map( Location::toString )
			.collect( Collectors.toList() )
		).stream();
	}
	
	
	
	/**
	 * Return a stream of file locations corresponding to the portion of the stack of an executing 
	 * program that corresponds to classes with the given prefix.
	 */
	@Test.Decl( "Throws AssertionError for null prefix" )
	@Test.Decl( "Throws AssertionError for empty prefix" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are file links" )
	@Test.Decl( "Links work for secondary classes" )
	@Test.Decl( "Elements correspond to the calling stack" )
	@Test.Decl( "Elements have classes matching the given class name prefix" )
	public Stream<String> getLocation( String prefix ) {
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.filter( sf -> sf.getClassName().startsWith( Assert.nonEmpty( prefix ) ) )
			.map( Location::new )
			.map( Location::toString )
			.collect( Collectors.toList() )
		).stream();
	}
	
	

	/**
	 * Return a stream of file locations corresponding to the stack trace elements of the
	 * given Throwable object.
	 */
	@Test.Decl( "Throws AssertionError for null Throwable" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Return can be empty" )
	@Test.Decl( "Elements are file links" )
	@Test.Decl( "Links work for secondary classes" )
	@Test.Decl( "Elements correspond to the stack trace" )
	@Test.Decl( "Elements have classes matching the given class name prefix" )
	@Test.Decl( "Throws AssertionError for null prefix" )
	@Test.Decl( "Prefix can be empty" )
	public Stream<String> getLocation( Throwable th, String prefix ) {
		Assert.nonNull( prefix );
		return Arrays.stream( Assert.nonNull( th ).getStackTrace() )
			.filter( (ste) -> ste.getClassName().startsWith( prefix ) )
			.map( Location::new )
			.map( Location::toString );
	}
	

	/**
	 * Return a stream of file locations corresponding to the stack trace elements of the
	 * given Throwable object.
	 */
	@Test.Decl( "Throws AssertionError for null Throwable" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are file links" )
	@Test.Decl( "Links work for secondary classes" )
	@Test.Decl( "Elements correspond to the stack trace" )
	public Stream<String> getLocation( Throwable th ) {
		return this.getLocation( th, "" );
	}
	

	/**
	 * Return the fully qualified class name for the class containing the executing method.
	 * 
	 * @param offset
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for negative offset" )
	@Test.Decl( "Throws AppExcpetion for offset larger than stack depth" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is correct for offset = 0" )
	@Test.Decl( "Return is correct for offset = 1" )
	@Test.Decl( "Return is correct for offset = 2" )
	@Test.Decl( "Returns class for nested class" )
	@Test.Decl( "Returns class for nested nested class" )
	@Test.Decl( "Returns class for local class" )
	@Test.Decl( "Returns class for anonymous class" )
	@Test.Decl( "Returns class for secondary class" )
	public Class<?> getCallingClass( int offset ) {
		Assert.nonNeg( offset );
		
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.map( StackWalker.StackFrame::getDeclaringClass )
			.skip( offset )
			.findFirst()
			.orElseThrow( () -> new AppException( "Offset (" + offset + ") larger than depth of stack." ) )
		);
	}

	
	/**
	 * Return the fully name of the executing method.
	 * 
	 * @param offset
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for negative offset" )
	@Test.Decl( "Throws AppExcpetion for offset larger than stack depth" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is correct for offset = 0" )
	@Test.Decl( "Return is correct for offset = 1" )
	@Test.Decl( "Return is correct for offset = 2" )
	@Test.Decl( "Returns method for nested class" )
	@Test.Decl( "Returns method for nested nested class" )
	@Test.Decl( "Returns method for local class" )
	@Test.Decl( "Returns method for anonymous class" )
	@Test.Decl( "Returns method for secondary class" )
	public String getCallingMethod( int offset ) {
		Assert.nonNeg( offset );
		
		return StackWalker.getInstance( Option.RETAIN_CLASS_REFERENCE ).walk( s -> s
			.map(  StackWalker.StackFrame::getMethodName )
			.skip( offset )
			.findFirst()
			.orElseThrow( () -> new AppException( "Offset (" + offset + ") larger than depth of stack." ) )
		);
	}
	

	
}
