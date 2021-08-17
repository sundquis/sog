/**
 * Copyright (C) 2021
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Singleton used to expose access to application level resources:
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
		
	private App() {
		String rootDirName = Assert.nonEmpty( Property.get( "root", null, Property.STRING ) );
		this.root = Assert.rwDirectory( Path.of( rootDirName ) );
		this.description = Assert.nonEmpty( Property.get( "description",  "<none>",  Property.STRING ) );
		this.sourceDirs = Property.get( "source.dirs", List.of(), Property.LIST )
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
		Assert.nonNull( clazz );

		Class<?> encl = clazz;
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
	
	
	@Test.Decl( "True for every java source file" )
	@Test.Decl( "False for null paths" )
	@Test.Decl( "False for empty paths" )
	@Test.Decl( "False for directories" )
	@Test.Decl( "False for non-java files" )
	public static final Predicate<Path> SOURCE_FILE = p -> 
		p != null && Files.isRegularFile( p ) && p.getFileName().toString().endsWith( ".java" );

	
	private String relativePathToClassname( Path relativePath ) {
		String result = StreamSupport.stream( relativePath.spliterator(), false )
			.map( Object::toString )
			.collect( Collectors.joining( "." ) );
		Assert.isTrue( result.endsWith( ".java" ) );
		return result.replace( ".java",  "" );
	}
		
		
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Return is non-null" )
	@Test.Decl( "Return is not terminated" )
	@Test.Decl( "Elements are non-empty" )
	@Test.Decl( "Elements are classnames for classes in the package of the given class" )
	@Test.Decl( "One element for each class in the package" )
	@Test.Decl( "Secondary classes are not included" )
	@Test.Decl( "Non-source files are excluded" )
	public Stream<String> classesInPackage( Class<?> clazz ) {
		final Path sourceDir = this.sourceDir( Assert.nonNull( clazz ) );
		final Path packageDir = this.sourceFile( clazz ).getParent();
		
		try {
			return Files.list( packageDir )
				.filter( App.SOURCE_FILE )
				.map( sourceDir::relativize )
				.map( this::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	
	
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
				.filter( App.SOURCE_FILE )
				.map( sourceDir::relativize )
				.map( this::relativePathToClassname );
		} catch ( IOException e ) {
			throw new AppException( e );
		}
	}
	

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
				.filter( App.SOURCE_FILE )
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
	@Test.Decl( "Elements are file links" )
	@Test.Decl( "Links work for secondary classes" )
	@Test.Decl( "Elements correspond to the stack trace" )
	public Stream<String> getLocation( Throwable th ) {
		return Arrays.stream( Assert.nonNull( th ).getStackTrace() )
			.map( Location::new )
			.map( Location::toString );
	}
	

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
	
	
	
	public static void main( String[] args ) {
		//App.get().classesUnderPackage( App.class ).forEach( System.out::println );
		//Arrays.stream( Package.getPackages() ).forEach( System.out::println );
		App.get().classesUnderDir( App.get().sourceDir( App.class ), Path.of( "" ) ).forEach( System.out::println );
		System.out.println( "Done!" );
	}
	
	
}
