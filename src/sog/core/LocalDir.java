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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Represents a directory under the root of the application.
 * 
 * Services:
 * 		Append a new subdirectory.
 * 		Retrieve a Path representing the current directory.
 * 		Retrieve a file in the current directory with the specified extension.
 * 		Retrieve a temporary file.
 */
@Test.Subject( "test." )
public class LocalDir {

	/**
	 * The enumerated type for possible extensions. Add new types as needed.
	 */
	public static enum Type {

		/** No system type defined. */
		@Test.Skip( "Enumerated constant" )
		PLAIN( "plain" ),

		/** File used to store configuration information */
		@Test.Skip( "Enumerated constant" )
		PROPERTY( "xml" ),

		/** Temporary file*/
		@Test.Skip( "Enumerated constant" )
		TEMPORARY( "tmp" ),

		/** File contains comma separated values */
		@Test.Skip( "Enumerated constant" )
		CSV( "csv" ),

		/** File contains generic binary data */
		@Test.Skip( "Enumerated constant" )
		DATA( "dat" ),

		/** File contains serialized objects */
		@Test.Skip( "Enumerated constant" )
		SERIAL( "ser" ),

		/** Text file */
		@Test.Skip( "Enumerated constant" )
		TEXT( "txt" ),

		/** Java source file */
		@Test.Skip( "Enumerated constant" ) 		
		SRC( "java" ),

		/** HTML file */
		@Test.Skip( "Enumerated constant" ) 		
		HTML( "html" ),
		
		/** An xml file */
		@Test.Skip( "Enumerated constant" ) 		
		XML( "xml" ),
		
		/** A document type definition file */
		@Test.Skip( "Enumerated constant" )
		DTD( "dtd");
		
		
		private String extension;
		
		private Type( String ext ) {
			this.extension = "." + ext;
		}

		@Test.Decl( "Starts with dot" )
		public String getExtension() {
			return this.extension;
		}
		
	}

	private Path path;
	
	private boolean createMissingDirs;

	@Test.Decl( "Directory exists" )
	@Test.Decl( "Directory is readable" )
	@Test.Decl( "Directory is writeable" )
	public LocalDir( boolean createMissingDirs ) {
		this.createMissingDirs = createMissingDirs;
		this.path = Assert.rwDirectory( App.get().root() );
	}

	@Test.Decl( "Directory exists" )
	@Test.Decl( "Directory is readable" )
	@Test.Decl( "Directory is writeable" )
	@Test.Decl( "Default creates missing directories" )
	public LocalDir() {
		this( true );
	}

	/**
	 * Append a subdirectory to the current directory path.
	 *
	 * @param subDir
	 *	    The non-empty name of the sub-directory to be appended.
	 *
	 * @return
	 *      This LocalDir instance.
	 */
	@Test.Decl( "Throws AssertionError for null subdir name" )
	@Test.Decl( "Throws AssertionError for empty subdir name" )
	@Test.Decl( "Throws AppRuntime if missing directory cannot be created" )
	@Test.Decl( "Throws AssertionError if resulting directory does not exist" )
	@Test.Decl( "Throws AssertionError if resulting directory is not readable" )
	@Test.Decl( "Return is this LocalDir instance" )
	public LocalDir sub( String subDir ) {
		this.path = this.path.resolve( Assert.nonEmpty( subDir ) );
		if ( !Files.exists( this.path ) && this.createMissingDirs ) {
			try {
				Files.createDirectory( this.path );
			} catch ( IOException e ) {
				throw new AppRuntime( e );
			}
		}

		Assert.readableDirectory( this.path );
		return this;
	}

	/**
	 * Retrieves the directory currently represented by this LocalDir
	 * 
	 * @return
	 *      The non-null Path instance representing the readable directory
	 */
	@Test.Decl( "Is directory" )
	@Test.Decl( "Exists" )
	@Test.Decl( "Is readable")
	public Path getDir() {
		return this.path;
	}

	/**
	 * Retrieve the named file of the given type in the current directory.
	 */
	@Test.Decl( "Throws AssertionError if name is null" )
	@Test.Decl( "Throws AssertionError if name is empty" )
	@Test.Decl( "Throws AssertionError if type is null" )
	public Path getFile( String name, Type type ) {
		return this.path.resolve( Assert.nonEmpty( name ) + Assert.nonNull( type ).getExtension() );
	}
	
	/** Retrieve a temporary file that will be deleted when the JVM exits */
	@Test.Decl( "Temporary file exists" )
	@Test.Decl( "Temporary file is readable" )
	@Test.Decl( "Temporary file is writeable" )
	@Test.Decl( "Throws AssertionError if prefix is null" )
	@Test.Decl( "Throws AssertionError if prefix is empty" )
	@Test.Decl( "Throws AssertionError if prefix is shorter than 3 characters" )
	@Test.Decl( "Throws AssertionError if prefix is longer than 10 characters" )
	public File getTmpFile( String prefix ) {
		Assert.nonEmpty( prefix );
		Assert.boundedString( prefix, 3, 10 );
		
		File f = null;
		try {
			f = File.createTempFile( prefix, Type.TEMPORARY.getExtension(), this.path.toFile() );
		} catch ( IOException e ) {
			Fatal.error( "Framework requires access to teporary files.", e );
		}
		f.deleteOnExit();
		
		return f;
	}
	
	
	
}
