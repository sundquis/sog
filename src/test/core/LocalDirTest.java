/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import java.nio.file.Files;

import sog.core.LocalDir;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class LocalDirTest extends Test.Container {
	
	public LocalDirTest() {
		super( LocalDir.class );
	}

	// Test implementations

	@Test.Impl( member = "public Path LocalDir.getDir()", description = "Exists" )
	public void getDir_Exists( Test.Case tc ) {
		tc.assertTrue( Files.exists( new LocalDir().getDir() ) );
	}

	@Test.Impl( member = "public Path LocalDir.getDir()", description = "Is directory" )
	public void getDir_IsDirectory( Test.Case tc ) {
		tc.assertTrue( Files.isDirectory( new LocalDir().getDir() ) );
	}

	@Test.Impl( member = "public Path LocalDir.getDir()", description = "Is readable" )
	public void getDir_IsReadable( Test.Case tc ) {
		tc.assertTrue( Files.isReadable( new LocalDir().getDir() ) );
	}

	@Test.Impl( member = "public Path LocalDir.getFile(String, LocalDir.Type)", description = "throws Assertion Error if name is null" )
	public void getFile_ThrowsAssertionErrorIfNameIsNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( null, LocalDir.Type.PLAIN );
	}

	@Test.Impl( member = "public Path LocalDir.getFile(String, LocalDir.Type)", description = "throws Assertion Error if name is empty" )
	public void getFile_ThrowsAssertionErrorIfNameIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( "", LocalDir.Type.PLAIN );
	}

	@Test.Impl( member = "public Path LocalDir.getFile(String, LocalDir.Type)", description = "throws Assertion Error if type is null" )
	public void getFile_ThrowsAssertionErrorIfTypeIsNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( "Foo", null );
	}
	
	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Temporary file exists" )
	public void getTmpFile_TemporaryFileExists( Test.Case tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).exists() );
	}

	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Temporary file is readable" )
	public void getTmpFile_TemporaryFileIsReadable( Test.Case tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).canRead() );
	}

	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Temporary file is writeable" )
	public void getTmpFile_TemporaryFileIsWriteable( Test.Case tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).canWrite() );
	}

	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Throws arretion error if prefix is empty" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsEmpty( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "" );
	}

	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Throws arretion error if prefix is short" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsShort( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "AB" );
	}

	@Test.Impl( member = "public File LocalDir.getTmpFile(String)", description = "Throws arretion error if prefix is null" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsNull( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( null );
	}

	@Test.Impl( member = "public LocalDir LocalDir.sub(String)", description = "Appending empty subdir throws assertion error" )
	public void sub_AppendingEmptySubdirThrowsAssertionError( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "" );
	}

	@Test.Impl( member = "public LocalDir LocalDir.sub(String)", description = "Appending null subdir throws assertion error" )
	public void sub_AppendingNullSubdirThrowsAssertionError( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( null );
	}
	
	
	
}
