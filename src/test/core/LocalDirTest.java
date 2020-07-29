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
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class LocalDirTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return LocalDir.class;
	}

	// Test implementations

	@Test.Impl( src = "public Path LocalDir.getDir()", desc = "Exists" )
	public void getDir_Exists( TestCase tc ) {
		tc.assertTrue( Files.exists( new LocalDir().getDir() ) );
	}

	@Test.Impl( src = "public Path LocalDir.getDir()", desc = "Is directory" )
	public void getDir_IsDirectory( TestCase tc ) {
		tc.assertTrue( Files.isDirectory( new LocalDir().getDir() ) );
	}

	@Test.Impl( src = "public Path LocalDir.getDir()", desc = "Is readable" )
	public void getDir_IsReadable( TestCase tc ) {
		tc.assertTrue( Files.isReadable( new LocalDir().getDir() ) );
	}

	@Test.Impl( src = "public Path LocalDir.getFile(String, LocalDir.Type)", desc = "throws Assertion Error if name is null" )
	public void getFile_ThrowsAssertionErrorIfNameIsNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( null, LocalDir.Type.PLAIN );
	}

	@Test.Impl( src = "public Path LocalDir.getFile(String, LocalDir.Type)", desc = "throws Assertion Error if name is empty" )
	public void getFile_ThrowsAssertionErrorIfNameIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( "", LocalDir.Type.PLAIN );
	}

	@Test.Impl( src = "public Path LocalDir.getFile(String, LocalDir.Type)", desc = "throws Assertion Error if type is null" )
	public void getFile_ThrowsAssertionErrorIfTypeIsNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().getFile( "Foo", null );
	}
	
	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Temporary file exists" )
	public void getTmpFile_TemporaryFileExists( TestCase tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).exists() );
	}

	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Temporary file is readable" )
	public void getTmpFile_TemporaryFileIsReadable( TestCase tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).canRead() );
	}

	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Temporary file is writeable" )
	public void getTmpFile_TemporaryFileIsWriteable( TestCase tc ) {
		tc.assertTrue( new LocalDir().sub( "tmp" ).getTmpFile( "Foo" ).canWrite() );
	}

	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Throws arretion error if prefix is empty" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsEmpty( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "" );
	}

	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Throws arretion error if prefix is short" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsShort( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "AB" );
	}

	@Test.Impl( src = "public File LocalDir.getTmpFile(String)", desc = "Throws arretion error if prefix is null" )
	public void getTmpFile_ThrowsArretionErrorIfPrefixIsNull( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( null );
	}

	@Test.Impl( src = "public LocalDir LocalDir.sub(String)", desc = "Appending empty subdir throws assertion error" )
	public void sub_AppendingEmptySubdirThrowsAssertionError( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "" );
	}

	@Test.Impl( src = "public LocalDir LocalDir.sub(String)", desc = "Appending null subdir throws assertion error" )
	public void sub_AppendingNullSubdirThrowsAssertionError( TestCase tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( null );
	}
	
	
	
	
	public static void main(String[] args) {

		System.out.println();

		new Test(LocalDirTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
