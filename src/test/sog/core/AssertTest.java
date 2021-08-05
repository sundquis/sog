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
import java.nio.file.Path;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class AssertTest extends Test.Container {
	
	private final String ERR_MSG = "Custom diagnostic error message.";
	
	private final Path READABLE_FILE = App.get().root().resolve( Path.of( "example", "readable_file" ) );
	private final Path UNREADABLE_FILE = App.get().root().resolve( Path.of( "example", "unreadable_file" ) );
	private final Path WRITEABLE_FILE = App.get().root().resolve( Path.of( "example", "writeable_file" ) );
	private final Path UNWRITEABLE_FILE = App.get().root().resolve( Path.of( "example", "unwriteable_file" ) );

	private final Path READABLE_DIR = App.get().root().resolve( Path.of( "example", "readable_dir" ) );
	private final Path UNREADABLE_DIR = App.get().root().resolve( Path.of( "example", "unreadable_dir" ) );
	private final Path WRITEABLE_DIR = App.get().root().resolve( Path.of( "example", "writeable_dir" ) );
	private final Path UNWRITEABLE_DIR = App.get().root().resolve( Path.of( "example", "unwriteable_dir" ) );
	
	private final Path DOES_NOT_EXIST = App.get().root().resolve( Path.of( "example", "DOES_NOT_EXIST" ) );
	
	public AssertTest() {
		super( Assert.class );
		
		assert Files.exists( this.READABLE_FILE );
		assert Files.exists( this.UNREADABLE_FILE );
		assert Files.exists( this.WRITEABLE_FILE );
		assert Files.exists( this.UNWRITEABLE_FILE );
		
		assert Files.exists( this.READABLE_DIR );
		assert Files.exists( this.UNREADABLE_DIR );
		assert Files.exists( this.WRITEABLE_DIR );
		assert Files.exists( this.UNWRITEABLE_DIR );
	}
	

	public void assertNotEmpty( Test.Case tc, String msg, boolean printMsg ) {
		if ( printMsg ) {
			System.out.println( ">>> " + msg );
		}
		tc.assertNotEmpty( msg );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0ACE6F73C( Test.Case tc ) {
		try {
			Assert.greaterThan( 10, 42 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer)", 
		description = "Returns integer if it is greater than the minimum" 
	)
	public void tm_037CEBEA4( Test.Case tc ) {
		tc.assertEqual( 42, Assert.greaterThan( 42, 10 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer)", 
		description = "Throws AssertionError if integer is less than or equal to the minimum" 
	)
	public void tm_0A1D45BCC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.greaterThan( 10, 42 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_08307A237( Test.Case tc ) {
		try {
			Assert.greaterThan( 10, 42, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer, String)", 
		description = "Returns integer if it is greater than the minimum" 
	)
	public void tm_064552B5F( Test.Case tc ) {
		tc.assertEqual( 42, Assert.greaterThan( 42, 10, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.greaterThan(Integer, Integer, String)", 
		description = "Throws AssertionError if integer is less than or equal to the minimum" 
	)
	public void tm_00AA81D07( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.greaterThan( 10, 42, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_09149DE63( Test.Case tc ) {
		try {
			Assert.lessThan( 10, 1 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer)", 
		description = "Returns integer if it is less than the minimum" 
	)
	public void tm_0EF6DB138( Test.Case tc ) {
		tc.assertEqual( 1, Assert.lessThan( 1, 10 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer)", 
		description = "Throws AssertionError if integer is greater than or equal to the minimum" 
	)
	public void tm_061259678( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.lessThan( 10, 1 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_03C00725E( Test.Case tc ) {
		try {
			Assert.lessThan( 10, 1, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer, String)", 
		description = "Returns integer if it is less than the minimum" 
	)
	public void tm_076BD6D5D( Test.Case tc ) {
		tc.assertEqual( 1, Assert.lessThan( 1, 10, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThan(Integer, Integer, String)", 
		description = "Throws AssertionError if integer is greater than or equal to the minimum" 
	)
	public void tm_024B9BE5D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.lessThan( 10, 1, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_05C01725C( Test.Case tc ) {
		try {
			Assert.nonNeg( -1 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer)", 
		description = "Returns integer if it is nonnegative" 
	)
	public void tm_042BB291A( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonNeg( 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer)", 
		description = "Throws AssertionError if integer is negative" 
	)
	public void tm_050E4C8EA( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonNeg( -1 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0E204FD57( Test.Case tc ) {
		try {
			Assert.nonNeg( -1, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer, String)", 
		description = "Returns integer if it is nonnegative" 
	)
	public void tm_0EB14C87F( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonNeg( 1, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonNeg(Integer, String)", 
		description = "Throws AssertionError if integer is negative" 
	)
	public void tm_0902F334F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonNeg( -1, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0627371D2( Test.Case tc ) {
		try {
			Assert.nonZero( 0 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer)", 
		description = "Returns integer if it is not zero" 
	)
	public void tm_0F3B99C09( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonZero( 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer)", 
		description = "Throws AssertionError if integer is zero" 
	)
	public void tm_01E1B07E7( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonZero( 0 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0699326CD( Test.Case tc ) {
		try {
			Assert.nonZero( 0, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer, String)", 
		description = "Returns integer if it is not zero" 
	)
	public void tm_0A6FE68C4( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonZero( 1, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.nonZero(Integer, String)", 
		description = "Throws AssertionError if integer is zero" 
	)
	public void tm_0479482CC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonZero( 0, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0695165A6( Test.Case tc ) {
		try {
			Assert.positive( 0 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer)", 
		description = "Returns integer if it is positive" 
	)
	public void tm_08613F201( Test.Case tc ) {
		tc.assertEqual( 1, Assert.positive( 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer)", 
		description = "Throws AssertionError if integer is not positive" 
	)
	public void tm_0834A4311( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.positive( 0 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0017A06A1( Test.Case tc ) {
		try {
			Assert.positive( 0, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer, String)", 
		description = "Returns integer if it is positive" 
	)
	public void tm_02300AABC( Test.Case tc ) {
		tc.assertEqual( 1, Assert.positive( 1, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.positive(Integer, String)", 
		description = "Throws AssertionError if integer is not positive" 
	)
	public void tm_08DE39CF6( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.positive( 0, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object)", 
		description = "Includes diagnostic message" 
	)
	public void tm_04628C9AA( Test.Case tc ) {
		try {
			Assert.equal( 1, 2 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object)", 
		description = "Returns first argument if objects are equal" 
	)
	public void tm_02BF61E76( Test.Case tc ) {
		tc.assertEqual( 1, Assert.equal( 1, 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object)", 
		description = "Throws AssertionError if objects are not equal" 
	)
	public void tm_07ACC7022( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.equal( 1, 2 );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_023BF26A5( Test.Case tc ) {
		try {
			Assert.equal( 1, 2, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object, String)", 
		description = "Returns first argument if objects are equal" 
	)
	public void tm_03FE12571( Test.Case tc ) {
		tc.assertEqual( 1, Assert.equal( 1, 1, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.equal(Object, Object, String)", 
		description = "Throws AssertionError if objects are not equal" 
	)
	public void tm_05E2DC347( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.equal( 1, 2, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0FBF9872C( Test.Case tc ) {
		try {
			Assert.isNull( 1 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object)", 
		description = "Returns object if null" 
	)
	public void tm_076183013( Test.Case tc ) {
		tc.assertEqual( null, Assert.isNull( null ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object)", 
		description = "Throws AssertionError if object is not null" 
	)
	public void tm_045A46BAE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.isNull( 1 );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0DA434227( Test.Case tc ) {
		try {
			Assert.isNull( 1, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object, String)", 
		description = "Returns object if null" 
	)
	public void tm_07B9E4438( Test.Case tc ) {
		tc.assertEqual( null, Assert.isNull( null, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.isNull(Object, String)", 
		description = "Throws AssertionError if object is not null" 
	)
	public void tm_0F3C6D0A9( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.isNull( 1, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0F3E8D21D( Test.Case tc ) {
		try {
			Assert.nonNull( null );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object)", 
		description = "Returns object if not null" 
	)
	public void tm_0BA496C6F( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonNeg( 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object)", 
		description = "Throws AssertionError if object is null" 
	)
	public void tm_07EDBF2B2( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonNull( null );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0CFB50C18( Test.Case tc ) {
		try {
			Assert.nonNull( null, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object, String)", 
		description = "Returns object if not null" 
	)
	public void tm_0C1608714( Test.Case tc ) {
		tc.assertEqual( 1, Assert.nonNeg( 1, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Object Assert.nonNull(Object, String)", 
		description = "Throws AssertionError if object is null" 
	)
	public void tm_00F920C2D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonNull( null, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[])", 
		description = "Includes diagnostic message" 
	)
	public void tm_0679D079B( Test.Case tc ) {
		try {
			Assert.nonEmpty( new Object[] {} );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[])", 
		description = "Returns array if not empty" 
	)
	public void tm_0EC7BC99F( Test.Case tc ) {
		tc.assertEqual( new String[] {"A","B"}, Assert.nonNull( new String[] {"A","B"} ) );
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[])", 
		description = "Throws AssertionError if array is empty" 
	)
	public void tm_0F9F0A892( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonEmpty( new Object[] {} );
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[], String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_07412E396( Test.Case tc ) {
		try {
			Assert.nonEmpty( new Object[] {}, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[], String)", 
		description = "Returns array if not empty" 
	)
	public void tm_0162D0244( Test.Case tc ) {
		tc.assertEqual( new String[] {"A","B"}, Assert.nonNull( new String[] {"A","B"}, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Object[] Assert.nonEmpty(Object[], String)", 
		description = "Throws AssertionError if array is empty" 
	)
	public void tm_0EFED640D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonEmpty( new Object[] {}, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_0D5E0677A( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.readableDirectory( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.READABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.UNREADABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path)", 
		description = "Returns path if it represents a readable directory" 
	)
	public void tm_0950CF440( Test.Case tc ) {
		tc.assertEqual( this.READABLE_DIR, Assert.readableDirectory( this.READABLE_DIR ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path)", 
		description = "Throws AssertionError if path does not represent a readable directory" 
	)
	public void tm_0A6EA86C0( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.readableDirectory( this.UNREADABLE_DIR );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path, String)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_07C23F475( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.readableDirectory( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.READABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableDirectory( this.UNREADABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path, String)", 
		description = "Returns path if it represents a readable directory" 
	)
	public void tm_08312FCE5( Test.Case tc ) {
		tc.assertEqual( this.READABLE_DIR, Assert.readableDirectory( this.READABLE_DIR, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableDirectory(Path, String)", 
		description = "Throws AssertionError if path does not represent a readable directory" 
	)
	public void tm_05926A9FB( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.readableDirectory( this.UNREADABLE_DIR, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_05EFA4A27( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.readableFile( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.READABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.UNREADABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path)", 
		description = "Returns path if it represents a readable file" 
	)
	public void tm_0A1FC44CC( Test.Case tc ) {
		tc.assertEqual( this.READABLE_FILE, Assert.readableFile( this.READABLE_FILE ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path)", 
		description = "Throws AssertionError if path does not represent a readable file" 
	)
	public void tm_0DDE2E652( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.readableFile( this.UNREADABLE_FILE );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path, String)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_0787ADA22( Test.Case tc ) {
		boolean verbose  = false;
		try {
			Assert.readableFile( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.READABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.readableFile( this.UNREADABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path, String)", 
		description = "Returns path if it represents a readable file" 
	)
	public void tm_02E24EC07( Test.Case tc ) {
		tc.assertEqual( this.READABLE_FILE, Assert.readableFile( this.READABLE_FILE, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.readableFile(Path, String)", 
		description = "Throws AssertionError if path does not represent a readable file" 
	)
	public void tm_045DF4737( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.readableFile( this.UNREADABLE_FILE, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path)", 
		description = "Includes diagnostic message",
		weight = 5
	)
	public void tm_00F6FDD45( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.rwDirectory( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.READABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.UNREADABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.UNWRITEABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path)", 
		description = "Returns path if it represents a readable and writeable directory" 
	)
	public void tm_01C116DE5( Test.Case tc ) {
		tc.assertEqual( this.READABLE_DIR, Assert.rwDirectory( this.READABLE_DIR ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path)", 
		description = "Throws AssertionError if path does not represent a readable and writeable directory" 
	)
	public void tm_01AA45C1B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.rwDirectory( this.UNREADABLE_DIR );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path, String)", 
		description = "Includes diagnostic message",
		weight = 5
	)
	public void tm_08FB26F40( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.rwDirectory( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.READABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.UNREADABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwDirectory( this.UNWRITEABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path, String)", 
		description = "Returns path if it represents a readable and writeable directory" 
	)
	public void tm_033C50CCA( Test.Case tc ) {
		tc.assertEqual( this.READABLE_DIR, Assert.rwDirectory( this.READABLE_DIR, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwDirectory(Path, String)", 
		description = "Throws AssertionError if path does not represent a readable and writeable directory" 
	)
	public void tm_09E24C116( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.rwDirectory( this.UNREADABLE_DIR, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path)", 
		description = "Includes diagnostic message",
		weight = 5
	)
	public void tm_052A5217C( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.rwFile( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.READABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.UNREADABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.UNWRITEABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path)", 
		description = "Returns path if it represents a readable and writeable file" 
	)
	public void tm_08836CED1( Test.Case tc ) {
		tc.assertEqual( this.READABLE_FILE, Assert.rwFile( this.READABLE_FILE ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path)", 
		description = "Throws AssertionError if path does not represent a readable and writeable file" 
	)
	public void tm_00FC0150D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.rwFile( this.UNWRITEABLE_FILE );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path, String)", 
		description = "Includes diagnostic message",
		weight = 5
	)
	public void tm_0D6178C77( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.rwFile( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.READABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.UNREADABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.rwFile( this.UNWRITEABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path, String)", 
		description = "Returns path if it represents a readable and writeable file" 
	)
	public void tm_07A068DCC( Test.Case tc ) {
		tc.assertEqual( this.READABLE_FILE, Assert.rwFile( this.READABLE_FILE, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.rwFile(Path, String)", 
		description = "Throws AssertionError if path does not represent a readable and writeable file" 
	)
	public void tm_00E58C632( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.rwFile( this.UNWRITEABLE_FILE, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_00F8CF80F( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.writeableDirectory( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.WRITEABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.UNWRITEABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path)", 
		description = "Returns path if it represents a writeable directory" 
	)
	public void tm_07D47398E( Test.Case tc ) {
		tc.assertEqual( this.WRITEABLE_DIR, Assert.writeableDirectory( this.WRITEABLE_DIR ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path)", 
		description = "Throws AssertionError if path does not represent a writeable directory" 
	)
	public void tm_0F7B559C4( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.writeableDirectory( this.UNWRITEABLE_DIR );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path, String)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_01415200A( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.writeableDirectory( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.WRITEABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableDirectory( this.UNWRITEABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path, String)", 
		description = "Returns path if it represents a writeable directory" 
	)
	public void tm_060E5E089( Test.Case tc ) {
		tc.assertEqual( this.WRITEABLE_DIR, Assert.writeableDirectory( this.WRITEABLE_DIR, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableDirectory(Path, String)", 
		description = "Throws AssertionError if path does not represent a writeable directory" 
	)
	public void tm_0900622E9( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.writeableDirectory( this.UNWRITEABLE_DIR, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_00D80F072( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.writeableFile( null );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.DOES_NOT_EXIST );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.WRITEABLE_DIR );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.UNWRITEABLE_FILE );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path)", 
		description = "Returns path if it represents a writeable file" 
	)
	public void tm_0B228E40E( Test.Case tc ) {
		tc.assertEqual( this.WRITEABLE_FILE, Assert.writeableFile( this.WRITEABLE_FILE ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path)", 
		description = "Throws AssertionError if path does not represent a writeable file" 
	)
	public void tm_07B93A17E( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.writeableFile( this.UNWRITEABLE_FILE );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path, String)", 
		description = "Includes diagnostic message",
		weight = 4
	)
	public void tm_01108056D( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.writeableFile( null, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.DOES_NOT_EXIST, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.WRITEABLE_DIR, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.writeableFile( this.UNWRITEABLE_FILE, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path, String)", 
		description = "Returns path if it represents a writeable file" 
	)
	public void tm_09BAE7F33( Test.Case tc ) {
		tc.assertEqual( this.WRITEABLE_FILE, Assert.writeableFile( this.WRITEABLE_FILE, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Path Assert.writeableFile(Path, String)", 
		description = "Throws AssertionError if path does not represent a writeable file" 
	)
	public void tm_0C91E2239( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.writeableFile( this.UNWRITEABLE_FILE, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int)", 
		description = "Includes diagnostic message" 
	)
	public void tm_048F30673( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.boundedString( "foo", 1, 2 );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.boundedString( "foo", 4, 5 );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int)", 
		description = "Returns string if length is in bounds" 
	)
	public void tm_02B4ABB13( Test.Case tc ) {
		tc.assertEqual( "foo", Assert.boundedString( "foo", 3, 3 ) );
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int)", 
		description = "Throws AssertionError if string length is out of bounds" 
	)
	public void tm_06A3F0D2D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.boundedString( "", 1, 2 );
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_030488A6E( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.boundedString( "foo", 1, 2, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			Assert.boundedString( "foo", 4, 5, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int, String)", 
		description = "Returns string if length is in bounds" 
	)
	public void tm_0B013C14E( Test.Case tc ) {
		tc.assertEqual( "foo", Assert.boundedString( "foo", 3, 3, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: String Assert.boundedString(String, int, int, String)", 
		description = "Throws AssertionError if string length is out of bounds" 
	)
	public void tm_013571AA8( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.boundedString( "", 1, 0, ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_010E66ADB( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.nonEmpty( "" );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			String s = null;
			Assert.nonEmpty( s );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String)", 
		description = "Returns string if not empty" 
	)
	public void tm_0C0A3E5A7( Test.Case tc ) {
		tc.assertEqual( "foo", Assert.nonEmpty( "foo" ) );
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String)", 
		description = "Throws AssertionError if string is empty" 
	)
	public void tm_0BC6D4208( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonEmpty( "" );
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_04C6506D6( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.nonEmpty( "", ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
		try {
			String s = null;
			Assert.nonEmpty( s, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String, String)", 
		description = "Returns string if not empty" 
	)
	public void tm_0FC2281A2( Test.Case tc ) {
		tc.assertEqual( "foo", Assert.nonEmpty( "foo", ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: String Assert.nonEmpty(String, String)", 
		description = "Throws AssertionError if string is empty" 
	)
	public void tm_05B3735ED( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.nonEmpty( "", ERR_MSG );
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean)", 
		description = "Includes diagnostic message" 
	)
	public void tm_03CABBB93( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.isTrue( false );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean)", 
		description = "Returns predicate if it is true" 
	)
	public void tm_0502281FC( Test.Case tc ) {
		tc.assertEqual( true, Assert.isTrue( true ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean)", 
		description = "Throws AssertionError if predicate is false" 
	)
	public void tm_0C7A87042( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.isTrue( false );
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0D70A1F8E( Test.Case tc ) {
		boolean verbose = false;
		try {
			Assert.isTrue( false, ERR_MSG );
			tc.assertFail( "Should not get here" );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), verbose );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean, String)", 
		description = "Returns predicate if it is true" 
	)
	public void tm_00C687077( Test.Case tc ) {
		tc.assertEqual( true, Assert.isTrue( true, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: boolean Assert.isTrue(boolean, String)", 
		description = "Throws AssertionError if predicate is false" 
	)
	public void tm_0AD6D7E3D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.isTrue( false, ERR_MSG );
	}
	
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer)", 
		description = "Includes diagnostic message" 
	)
	public void tm_06687C858( Test.Case tc ) {
		try {
			Assert.lessThanOrEqual( 42, 41 );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer)", 
		description = "Returns integer if it is less than or equal to the minimum" 
	)
	public void tm_03CD62D07( Test.Case tc ) {
		tc.assertEqual( 42, Assert.lessThanOrEqual( 42, 42 ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer)", 
		description = "Throws AssertionError if integer is greater than the minimum" 
	)
	public void tm_0EAEE84BF( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.lessThanOrEqual( 42, 1 );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer, String)", 
		description = "Includes diagnostic message" 
	)
	public void tm_0B2C39753( Test.Case tc ) {
		try {
			Assert.lessThanOrEqual( 42, 41, ERR_MSG );
		} catch ( AssertionError ae ) {
			this.assertNotEmpty( tc, ae.getMessage(), false );
		}
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer, String)", 
		description = "Returns integer if it is less than or equal to the minimum" 
	)
	public void tm_083EF3EAC( Test.Case tc ) {
		tc.assertEqual( 42, Assert.lessThanOrEqual( 42, 42, ERR_MSG ) );
	}
		
	@Test.Impl( 
		member = "method: Integer Assert.lessThanOrEqual(Integer, Integer, String)", 
		description = "Throws AssertionError if integer is greater than the minimum" 
	)
	public void tm_0D009C124( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Assert.lessThanOrEqual( 42, 1, ERR_MSG );
	}

	
	
	

	public static void main( String[] args ) {
		Test.eval( Assert.class );
		//Test.evalPackage( Assert.class );
	}
}
