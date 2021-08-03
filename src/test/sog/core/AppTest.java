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
import java.util.function.Consumer;
import java.util.stream.Stream;

import sog.core.App;
import sog.core.AppException;
import sog.core.Test;
import sog.core.test.TestCase;

/**
 * 
 */
@Test.Skip( "Container" )
public class AppTest extends Test.Container{

	public AppTest() {
		super( App.class );
	}
	
	
	
	// TEST CASSES:
	
	@Test.Impl( 
		member = "constructor: App.Location(StackTraceElement)", 
		description = "Throws AssertionError for null element" 
	)
	public void tm_0F5367C3F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		StackTraceElement ste = null;
		new App.Location( ste );
	}
		
	@Test.Impl( 
		member = "constructor: App.Location(StackWalker.StackFrame)", 
		description = "Throws AssertionError for null frame" 
	)
	public void tm_0D87952A8( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		StackWalker.StackFrame sf = null;
		new App.Location( sf );
	}
		
	@Test.Impl( 
		member = "method: App App.get()", 
		description = "Is not null" 
	)
	public void tm_08153A1C0( Test.Case tc ) {
		tc.assertNonNull( App.get() );
	}
		
	@Test.Impl( 
		member = "method: App App.get()", 
		description = "Value is unique" 
	)
	public void tm_05173EF6E( Test.Case tc ) {
		App app = App.get();
		Stream.of( 1, 2, 3 ).forEach( x -> tc.assertEqual( app, App.get() ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Return is correct for offset = 0" 
	)
	public void tm_0231E8DAD( Test.Case tc ) {
		tc.assertEqual( App.class, App.get().getCallingClass( 0 ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Return is correct for offset = 1" 
	)
	public void tm_0231E916E( Test.Case tc ) {
		tc.assertEqual( AppTest.class, App.get().getCallingClass( 1 ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Return is correct for offset = 2" 
	)
	public void tm_0231E952F( Test.Case tc ) {
		tc.assertEqual( TestCase.class, App.get().getCallingClass( 2 ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Return is non-null" 
	)
	public void tm_076098647( Test.Case tc ) {
		tc.assertNonNull( App.get().getCallingClass( 3 ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Returns class for anonymous class" 
	)
	public void tm_086079A85( Test.Case tc ) {
		String c = new Object() { 
			@Override public String toString() { return App.get().getCallingClass( 1 ).getName(); }
		}.toString();
		tc.assertTrue( c.startsWith( "test.sog.core.AppTest" ) );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Returns class for local class" 
	)
	public void tm_0A3C59203( Test.Case tc ) {
		class Local { @Override public String toString() { return App.get().getCallingClass( 1 ).getName(); } }
		tc.assertEqual( Local.class.getName(), new Local().toString() );
	}

	static class Nested {
		static Object anon = new Object() {};
		
		@Override public String toString() { return App.get().getCallingClass( 1 ).getName(); }
		
		static class Inner {
			@Override public String toString() { return App.get().getCallingClass( 1 ).getName(); }
		}
	}
	
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Returns class for nested class" 
	)
	public void tm_0898AFCA5( Test.Case tc ) {
		tc.assertEqual( Nested.class.getName(), new Nested().toString() );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Returns class for nested nested class" 
	)
	public void tm_0C1B22CD8( Test.Case tc ) {
		tc.assertEqual( Nested.Inner.class.getName(), new Nested.Inner().toString() );
	}

	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Returns class for secondary class" 
	)
	public void tm_0E2C9A08C( Test.Case tc ) {
		tc.assertEqual( SecondClass.class.getName(), new SecondClass().toString() );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Throws AppExcpetion for offset larger than stack depth" 
	)
	public void tm_01A20702E( Test.Case tc ) {
		tc.expectError( AppException.class );
		App.get().getCallingClass( 42 );
	}
		
	@Test.Impl( 
		member = "method: Class App.getCallingClass(int)", 
		description = "Throws AssertionError for negative offset" 
	)
	public void tm_0ED4E39BC( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		App.get().getCallingClass( -1 );
	}
		
	@Test.Impl( 
		member = "method: List App.sourceDirs()", 
		description = "Is not empty" 
	)
	public void tm_0505615E4( Test.Case tc ) {
		tc.assertTrue( App.get().sourceDirs().size() > 0 );
	}
		
	@Test.Impl( 
		member = "method: List App.sourceDirs()", 
		description = "Is not null" 
	)
	public void tm_02B27FD66( Test.Case tc ) {
		tc.assertNonNull( App.get().sourceDirs() );
	}
		
	@Test.Impl( 
		member = "method: Path App.root()", 
		description = "Is not null" 
	)
	public void tm_0E15831A0( Test.Case tc ) {
		tc.assertNonNull( App.get().root() );
	}
		
	@Test.Impl( 
		member = "method: Path App.root()", 
		description = "Is readable" 
	)
	public void tm_028082FDC( Test.Case tc ) {
		tc.assertTrue( Files.isReadable( App.get().root() ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.root()", 
		description = "Is wrieable" 
	)
	public void tm_0E8E0AABD( Test.Case tc ) {
		tc.assertTrue( Files.isWritable( App.get().root() ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for anonymous class" 
	)
	public void tm_00C1974CC( Test.Case tc ) {
		tc.assertEqual( 
			App.get().sourceDir( AppTest.class ), 
			App.get().sourceDir( new Object() {}.getClass() )
		);
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for local class" 
	)
	public void tm_00EF5A7CA( Test.Case tc ) {
		class Local {}
		tc.assertEqual( App.get().sourceDir( AppTest.class ), App.get().sourceDir( Local.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for nested anonymous class" 
	)
	public void tm_062E1F14B( Test.Case tc ) {
		tc.assertEqual( App.get().sourceDir( AppTest.class ), App.get().sourceDir( Nested.anon.getClass() ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for nested class" 
	)
	public void tm_0845D9FBE( Test.Case tc ) {
		tc.assertEqual( App.get().sourceDir( AppTest.class ), App.get().sourceDir( Nested.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for nested local class" 
	)
	public void tm_0E0363BC9( Test.Case tc ) {
		class Local { class Inner {} }
		tc.assertEqual( App.get().sourceDir( AppTest.class ), App.get().sourceDir( Local.Inner.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns container for nested nested class" 
	)
	public void tm_0DB2F8B9F( Test.Case tc ) {
		tc.assertEqual( App.get().sourceDir( AppTest.class ), App.get().sourceDir( Nested.Inner.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns directory" 
	)
	public void tm_0F2CB182A( Test.Case tc ) {
		tc.assertTrue( Files.isDirectory( App.get().sourceDir( AppTest.class ) ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns non null" 
	)
	public void tm_0DA747C33( Test.Case tc ) {
		tc.assertNonNull( App.get().sourceDir( AppTest.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns readable" 
	)
	public void tm_0906B87A9( Test.Case tc ) {
		tc.assertTrue( Files.isReadable( App.get().sourceDir( AppTest.class ) ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Returns writeable" 
	)
	public void tm_0BEC93C76( Test.Case tc ) {
		tc.assertTrue( Files.isWritable( App.get().sourceDir( AppTest.class ) ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Throws AppException for missing source dir" 
	)
	public void tm_095D6573A( Test.Case tc ) {
		tc.expectError( AppException.class );
		App.get().sourceDir( Object.class );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Throws AppException for secondary class" 
	)
	public void tm_0221E47B0( Test.Case tc ) {
		tc.expectError( AppException.class );
		App.get().sourceDir( SecondClass.class );
	}

	@Test.Impl( 
		member = "method: Path App.sourceDir(Class)", 
		description = "Throws assertion error for null class" 
	)
	public void tm_0C849B019( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		App.get().sourceDir( null );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for anonymous class" 
	)
	public void tm_03828B09B( Test.Case tc ) {
		tc.assertEqual( 
			App.get().sourceFile( AppTest.class ), 
			App.get().sourceFile( new Object() {}.getClass() )
		);
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for local class" 
	)
	public void tm_05EE12319( Test.Case tc ) {
		class Local {}
		tc.assertEqual( App.get().sourceFile( AppTest.class ), App.get().sourceFile( Local.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for nested anonymous class" 
	)
	public void tm_03C5D9E9C( Test.Case tc ) {
		tc.assertEqual( App.get().sourceFile( AppTest.class ), App.get().sourceFile( Nested.anon.getClass() ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for nested class" 
	)
	public void tm_031E18E4F( Test.Case tc ) {
		tc.assertEqual( App.get().sourceFile( AppTest.class ), App.get().sourceFile( Nested.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for nested local class" 
	)
	public void tm_020F7399A( Test.Case tc ) {
		class Local { class Inner {} }
		tc.assertEqual( App.get().sourceFile( AppTest.class ), App.get().sourceFile( Local.Inner.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns container for nested nested class" 
	)
	public void tm_0B28E47EE( Test.Case tc ) {
		tc.assertEqual( App.get().sourceFile( AppTest.class ), App.get().sourceFile( Nested.Inner.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns non null" 
	)
	public void tm_09C269D04( Test.Case tc ) {
		tc.assertNonNull( App.get().sourceFile( AppTest.class ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Returns readable" 
	)
	public void tm_0521DA87A( Test.Case tc ) {
		tc.assertTrue( Files.isReadable( App.get().sourceFile( AppTest.class ) ) );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Throws AppException for missing source file" 
	)
	public void tm_0A2FD6054( Test.Case tc ) {
		tc.expectError( AppException.class );
		App.get().sourceFile( Object.class );
	}
		
	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Throws AppException for secondary class" 
	)
	public void tm_0874DCBBF( Test.Case tc ) {
		tc.expectError( AppException.class );
		App.get().sourceFile( SecondClass.class );
	}

	@Test.Impl( 
		member = "method: Path App.sourceFile(Class)", 
		description = "Throws assertion error for null class" 
	)
	public void tm_0F458EBE8( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		App.get().sourceFile( null );
	}
		
	@Test.Impl( 
		member = "method: Stream App.classesInPackage(Class)", 
		description = "Elements are files in the package directory of the given class" 
	)
	public void tm_0A7F8D3CC( Test.Case tc ) {
		final String prefix = "test.sog.core.foo.";
		final Consumer<String> check = s -> tc.assertTrue( s.startsWith( prefix ) );
		App.get().classesInPackage( test.sog.core.foo.A.class ).forEach( check );
	}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Elements are non-empty" 
		)
		public void tm_07FBD890E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Java source files correspond to class names" 
		)
		public void tm_023EF2D9C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Non-source files are included" 
		)
		public void tm_00B4E5956( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Return is non-null" 
		)
		public void tm_0EC260903( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Return is not terminated" 
		)
		public void tm_0C9AAAB0C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesInPackage(Class)", 
			description = "Throws AssertionError for null class" 
		)
		public void tm_01D3F1A53( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Elements are files in the package directory of the given class or one of its sub-directories" 
		)
		public void tm_0901BD867( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Elements are non-empty" 
		)
		public void tm_07EE2BBF5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Java source files correspond to class names" 
		)
		public void tm_0FF08A115( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Non-source files are included" 
		)
		public void tm_05F7C648F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Return is non-null" 
		)
		public void tm_0B42EC76A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Return is not terminated" 
		)
		public void tm_0944EC033( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.classesUnderDir(Path)", 
			description = "Throws AssertionError for null class source directory" 
		)
		public void tm_0C71BBCA4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation()", 
			description = "Elements are file links" 
		)
		public void tm_0E6EE3978( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation()", 
			description = "Elements correspond to the calling stack" 
		)
		public void tm_0D91F7968( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation()", 
			description = "Links fail for secondary classes" 
		)
		public void tm_021F60730( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

		@Test.Impl( 
			member = "method: Stream App.getLocation()", 
			description = "Return is non-null" 
		)
		public void tm_000E15555( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation()", 
			description = "Return is not terminated" 
		)
		public void tm_0717BF1DE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Elements are file links" 
		)
		public void tm_01CB2DCE7( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Elements correspond to the calling stack" 
		)
		public void tm_0821641D9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Elements have classes matching the given class name prefix" 
		)
		public void tm_0F8318AAC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Links fail for secondary classes" 
		)
		public void tm_0D790D0A1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Return is non-null" 
		)
		public void tm_024320886( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Return is not terminated" 
		)
		public void tm_0F44BBC4F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Throws AssertionError for empty prefix" 
		)
		public void tm_0E83961BC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(String)", 
			description = "Throws AssertionError for null prefix" 
		)
		public void tm_0CF000FCA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Elements are file links" 
		)
		public void tm_0901581E8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Elements correspond to the stack trace" 
		)
		public void tm_0DF0FD879( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Links fail for secondary classes" 
		)
		public void tm_05CA13CC0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Return is non-null" 
		)
		public void tm_0CC8A86E5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Return is not terminated" 
		)
		public void tm_0ED3DB76E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: Stream App.getLocation(Throwable)", 
			description = "Throws AssertionError for null Throwable" 
		)
		public void tm_03BC3785D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.Location.toString()", 
			description = "Return includes a file link" 
		)
		public void tm_0D2E6BB13( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.Location.toString()", 
			description = "Return is non-empty" 
		)
		public void tm_00F7C8960( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.description()", 
			description = "Is not empty" 
		)
		public void tm_06C24E9B0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.description()", 
			description = "Is not null" 
		)
		public void tm_01B89801A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Return is correct for offset = 0" 
		)
		public void tm_04A7A3DC3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Return is correct for offset = 1" 
		)
		public void tm_04A7A4184( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Return is correct for offset = 2" 
		)
		public void tm_04A7A4545( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Return is non-null" 
		)
		public void tm_0EB4D74DD( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Returns method for anonymous class" 
		)
		public void tm_05EFFE93C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Returns method for local class" 
		)
		public void tm_018A7D43A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Returns method for nested class" 
		)
		public void tm_0B0F1014E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Returns method for nested nested class" 
		)
		public void tm_0B3E6480F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Returns method for secondary class" 
		)
		public void tm_0BBC1EF43( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Throws AppExcpetion for offset larger than stack depth" 
		)
		public void tm_0F4DB4BC4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String App.getCallingMethod(int)", 
			description = "Throws AssertionError for negative offset" 
		)
		public void tm_058684266( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void App.run()", 
			description = "Calls terminate on shutdown" 
		)
		public void tm_0C4CE178F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void App.terminateOnShutdown(App.OnShutdown)", 
			description = "Registers hook" 
		)
		public void tm_08E7828DE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void App.terminateOnShutdown(App.OnShutdown)", 
			description = "Throws assertion error for null" 
		)
		public void tm_03710D592( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	

	public static void main( String[] args ) {
		Test.eval( App.class );
		//Test.evalPackage( App.class );
	}
}

class SecondClass {
	@Override public String toString() { return App.get().getCallingClass( 1 ).getName(); }
}
