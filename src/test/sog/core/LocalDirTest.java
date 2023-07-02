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

package test.sog.core;

import java.nio.file.Files;

import sog.core.AppException;
import sog.core.LocalDir;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class LocalDirTest extends Test.Container {
	
	public LocalDirTest() {
		super( LocalDir.class );
	}
	
	
	public boolean createMissingDirs( LocalDir dir ) {
		return this.getSubjectField( dir, "createMissingDirs", null );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: LocalDir()", 
		description = "Default creates missing directories" 
	)
	public void tm_0C0A3215E( Test.Case tc ) {
		LocalDir dir = new LocalDir();
		tc.assertTrue( this.createMissingDirs( dir ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir()", 
		description = "Directory exists" 
	)
	public void tm_05B3C3150( Test.Case tc ) {
		LocalDir dir = new LocalDir();
		tc.assertTrue( Files.exists( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir()", 
		description = "Directory is readable" 
	)
	public void tm_04C402D48( Test.Case tc ) {
		LocalDir dir = new LocalDir();
		tc.assertTrue( Files.isReadable( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir()", 
		description = "Directory is writeable" 
	)
	public void tm_07D894AB7( Test.Case tc ) {
		LocalDir dir = new LocalDir();
		tc.assertTrue( Files.isWritable( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir(boolean)", 
		description = "Directory exists" 
	)
	public void tm_05D07AC96( Test.Case tc ) {
		LocalDir dir = new LocalDir( false );
		tc.assertTrue( Files.exists( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir(boolean)", 
		description = "Directory is readable" 
	)
	public void tm_00CDCCDC2( Test.Case tc ) {
		LocalDir dir = new LocalDir( false );
		tc.assertTrue( Files.isReadable( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "constructor: LocalDir(boolean)", 
		description = "Directory is writeable" 
	)
	public void tm_0D080B97D( Test.Case tc ) {
		LocalDir dir = new LocalDir( false );
		tc.assertTrue( Files.isWritable( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Temporary file exists" 
	)
	public void tm_075A0E455( Test.Case tc ) {
		LocalDir tmp = new LocalDir().sub( "tmp" );
		tc.assertTrue( Files.exists( tmp.getTmpFile( "FOO" ).toPath() ) );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Temporary file is readable" 
	)
	public void tm_08C5B9563( Test.Case tc ) {
		LocalDir tmp = new LocalDir().sub( "tmp" );
		tc.assertTrue( Files.isReadable( tmp.getTmpFile( "FOO" ).toPath() ) );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Temporary file is writeable" 
	)
	public void tm_040DAE5FC( Test.Case tc ) {
		LocalDir tmp = new LocalDir().sub( "tmp" );
		tc.assertTrue( Files.isWritable( tmp.getTmpFile( "FOO" ).toPath() ) );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Throws AssertionError if prefix is empty" 
	)
	public void tm_0B9D5E45B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "" );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Throws AssertionError if prefix is longer than 10 characters" 
	)
	public void tm_010459A31( Test.Case tc ) {
		new LocalDir().sub( "tmp" ).getTmpFile( "0123456789" );
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "0123456789X" );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Throws AssertionError if prefix is null" 
	)
	public void tm_015C9040F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( null );
	}
		
	@Test.Impl( 
		member = "method: File LocalDir.getTmpFile(String)", 
		description = "Throws AssertionError if prefix is shorter than 3 characters" 
	)
	public void tm_0CCA16D6D( Test.Case tc ) {
		new LocalDir().sub( "tmp" ).getTmpFile( "012" );
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "tmp" ).getTmpFile( "01" );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Return is this LocalDir instance" 
	)
	public void tm_0BF3BF740( Test.Case tc ) {
		LocalDir dir = new LocalDir( true ).sub( "tmp" ).sub( "A" ).sub( "B" );
		tc.assertEqual( dir, dir.sub( "C" ) );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Throws AppException if missing directory cannot be created" 
	)
	public void tm_0ECFA8620( Test.Case tc ) {
		LocalDir dir = new LocalDir( true ).sub( "tmp" ).sub( "assert" ).sub( "unwriteable_dir" );
		tc.expectError( AppException.class );
		dir.sub( "foo" );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Throws AssertionError for empty subdir name" 
	)
	public void tm_0012D099A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( "" );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Throws AssertionError for null subdir name" 
	)
	public void tm_04432426C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir().sub( null );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Throws AssertionError if resulting directory does not exist" 
	)
	public void tm_01A8EB09A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new LocalDir( false ).sub( "tmp" ).sub( "foo" );
	}
		
	@Test.Impl( 
		member = "method: LocalDir LocalDir.sub(String)", 
		description = "Throws AssertionError if resulting directory is not readable" 
	)
	public void tm_0C33206B2( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" );
		tc.expectError( AssertionError.class );
		dir.sub( "unreadable_dir" );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getDir()", 
		description = "Exists" 
	)
	public void tm_0D3ADF3FA( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.assertTrue( Files.exists( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getDir()", 
		description = "Is directory" 
	)
	public void tm_0EB06C195( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.assertTrue( Files.isDirectory( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getDir()", 
		description = "Is readable" 
	)
	public void tm_0A0AF84DE( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.assertTrue( Files.isReadable( dir.getDir() ) );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getFile(String, LocalDir.Type)", 
		description = "Throws AssertionError if name is empty" 
	)
	public void tm_0178E2088( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.expectError( AssertionError.class );
		dir.getFile( "", LocalDir.Type.CSV );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getFile(String, LocalDir.Type)", 
		description = "Throws AssertionError if name is null" 
	)
	public void tm_0A5320E42( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.expectError( AssertionError.class );
		dir.getFile( null, LocalDir.Type.CSV );
	}
		
	@Test.Impl( 
		member = "method: Path LocalDir.getFile(String, LocalDir.Type)", 
		description = "Throws AssertionError if type is null" 
	)
	public void tm_063C25431( Test.Case tc ) {
		LocalDir dir = new LocalDir( false ).sub( "tmp" ).sub( "assert" ).sub( "readable_dir" );
		tc.expectError( AssertionError.class );
		dir.getFile( "file", null );
	}
		
	@Test.Impl( 
		member = "method: String LocalDir.Type.getExtension()", 
		description = "Starts with dot" 
	)
	public void tm_04B4A99AA( Test.Case tc ) {
		tc.assertTrue( LocalDir.Type.DATA.getExtension().startsWith( "." ) );
	}
	
	
	

	public static void main( String[] args ) {
		Test.eval( LocalDir.class ).showDetails( true ).print();
	}
}
