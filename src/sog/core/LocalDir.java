/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/*
 * This should be reworked to use java.nio.Path
 */
public class LocalDir {

	/**
	 * The enumerated type for possible extensions.
	 */
	public static enum Type {

		/** No system type defined. */
		PLAIN( "plain" ),

		/** File used to store configuration information */
		PROPERTY( "xml" ),

		/** Temporary file*/
		TEMPORARY( "tmp" ),

		/** File contains comma separated values */
		CSV( "csv" ),

		/** File contains generic binary data */
		DATA( "dat" ),

		/** File contains serialized objects */
		SERIAL( "ser" ),

		/** Text file */
		TEXT( "txt" ),

		/** Java source file */
		SRC( "java" ),

		/** HTML file */
		HTML( "html" ),
		
		/** An xml file */
		XML( "xml" );
		
		private String extension;
		
		Type( String ext ) {
			this.extension = "." + ext;
		}
		
		public String getExtension() {
			return this.extension;
		}
		
	}

	private Path path;
	
	private boolean createMissingDirs;

	public LocalDir( boolean createMissingDirs ) {
		this.createMissingDirs = createMissingDirs;
		this.path = Assert.rwDirectory( App.get().root() );
	}
	
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
	@Test.Decl( "Appending null subdir throws assertion error")
	@Test.Decl( "Appending empty subdir throws assertion error")
	public LocalDir sub( String subDir ) {
		Assert.nonEmpty( subDir );
		this.path = this.path.resolve( subDir );
		if ( !Files.exists( this.path ) && this.createMissingDirs ) {
			try {
				Files.createDirectory( this.path );
			} catch ( IOException e ) {
				throw new AppException( e );
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
	@Test.Decl( "throws Assertion Error if name is null" )
	@Test.Decl( "throws Assertion Error if name is empty" )
	@Test.Decl( "throws Assertion Error if type is null" )
	public Path getFile( String name, Type type ) {
		Assert.nonEmpty( name );
		Assert.nonNull( type );
		return this.path.resolve( name + type.getExtension() );
	}
	
	/** Retrieve a temporary file that will be deleted when the JVM exits */
	@Test.Decl( "Temporary file exists" )
	@Test.Decl( "Temporary file is readable" )
	@Test.Decl( "Temporary file is writeable" )
	@Test.Decl( "Throws arretion error if prefix is null" )
	@Test.Decl( "Throws arretion error if prefix is empty" )
	@Test.Decl( "Throws arretion error if prefix is short" )
	public File getTmpFile( String prefix ) {
		Assert.nonEmpty( prefix );
		Assert.isTrue( prefix.length() >= 3 );
		
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
