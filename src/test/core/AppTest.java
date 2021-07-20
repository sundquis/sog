/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;




import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import sog.core.App;
import sog.core.AppException;
import sog.core.ByteFile;
import sog.core.Property;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class AppTest extends Test.Container {


	public Path mySource; 
	
	public Path myDir;
	
	public AppTest() {
		super( App.class );
		this.mySource = App.get().sourceFile( AppTest.class );
		this.myDir = App.get().sourceDir( AppTest.class );
	}


		
		@Test.Impl( member = "public App App.get()", description = "Is not null" )
		public void get_IsNotNull( Test.Case tc ) {
			tc.assertNonNull( App.get() );
		}

		@Test.Impl( member = "public String App.description()", description = "Is not empty" )
		public void description_IsNotEmpty( Test.Case tc ) {
			tc.assertFalse( App.get().description().isEmpty() );
		}

		@Test.Impl( member = "public void App.run()", description = "Calls terminate on shutdown" )
		public void run_CallsTerminateOnShutdown( Test.Case tc ) {
			// TOGGLE
			//* */ tc.addMessage( "Manually tested" ); /*
			// SHOULD SEE:
			//		Terminating [0] Thread[ ... ]
			//		...
			//		Terminating [9] Thread[ ... ]
			class OS implements App.OnShutdown {
				int id;
				OS(int id) {this.id = id;}
				public void terminate() {
					System.out.println("Terminating [" + id + "] " + Thread.currentThread() );
				}
				
			};
			for ( int i = 0; i < 10; i++ ) {
				App.get().terminateOnShutdown( new OS(i) );
			}
			//tc.pass();
			// */
		}

		@Test.Impl( member = "public Path App.root()", description = "Is not null" )
		public void root_IsNotNull( Test.Case tc ) {
			tc.assertNonNull( App.get().root() );
		}

		@Test.Impl( member = "public Path App.root()", description = "Is readable" )
		public void root_IsReadable( Test.Case tc ) {
			tc.assertTrue( Files.isReadable( App.get().root() ) );
		}

		@Test.Impl( member = "public Path App.root()", description = "Is wrieable" )
		public void root_IsWrieable( Test.Case tc ) {
			tc.assertTrue( Files.isWritable( App.get().root() ) );
		}
		
		@Test.Impl( member = "public String App.description()", description = "Is not null" )
		public void description_IsNotNull( Test.Case tc ) {
			tc.assertNonNull( App.get().description() );
		}

		@Test.Impl( member = "public List App.sourceDirs()", description = "Is not empty" )
		public void sourceDirs_IsNotEmpty( Test.Case tc ) {
			tc.assertTrue( App.get().sourceDirs().size() > 0 );
		}

		@Test.Impl( member = "public List App.sourceDirs()", description = "Is not null" )
		public void sourceDirs_IsNotNull( Test.Case tc ) {
			tc.assertNonNull( App.get().sourceDirs() );
		}
		
		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns container for anonymous class" )
		public void sourceFile_ReturnsContainerForAnonymousClass( Test.Case tc ) {
			Object anon = new Object(){};
			tc.assertEqual( mySource, App.get().sourceFile( anon.getClass() ) );
		}

		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns container for local class" )
		public void sourceFile_ReturnsContainerForLocalClass( Test.Case tc ) {
			class Local {}
			Local local = new Local();
			tc.assertEqual( mySource, App.get().sourceFile( local.getClass() ) );
		}

		public static class Nested {
			private class Inner {}
			
			private Inner inner = new Inner();
			
			private Object anon = new Object() {};
			
			private Object get() { return new Object(){}; }
		}
		
		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns container for nested anonymous class" )
		public void sourceFile_ReturnsContainerForNestedAnonymousClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.anon.getClass() ) );
		}

		@Test.Impl( 
			member = "public Path App.sourceFile(Class)",
			description = "Returns container for nested class" )
		public void sourceFile_ReturnsContainerForNestedClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.getClass() ) );
		}
		
		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns container for nested local class" )
		public void sourceFile_ReturnsContainerForNestedLocalClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.get().getClass() ) );
		}

		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns container for nested nested class" )
		public void sourceFile_ReturnsContainerForNestedNestedClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.inner.getClass() ) );
		}

		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns non null" )
		public void sourceFile_ReturnsNonNull( Test.Case tc ) {
			tc.assertNonNull( App.get().sourceFile( Property.class ) );
		}
					
		@Test.Impl( 
			member = "public Path App.sourceFile(Class)", 
			description = "Returns readable" )
		public void sourceFile_ReturnsReadable( Test.Case tc ) {
			tc.assertTrue( Files.isReadable( App.get().sourceFile( ByteFile.class ) ) );
		}

		@Test.Impl( member = "public Path App.sourceFile(Class)", description = "Throws App Excetion for missing source file" )
		public void sourceFile_ThrowsAppExcetionForMissingSourceFile( Test.Case tc ) {
			tc.expectError( AppException.class );
			App.get().sourceFile( Object.class );
		}

		@Test.Impl( member = "public Path App.sourceFile(Class)", description = "Throws assertion error for null class" )
		public void sourceFile_ThrowsAssertionErrorForNullClass( Test.Case tc ) {
			tc.expectError( AssertionError.class );
			App.get().sourceFile( null );
		}
					
		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for anonymous class" )
		public void sourceDir_ReturnsContainerForAnonymousClass( Test.Case tc ) {
			Object anon = new Object() {};
			tc.assertEqual( myDir, App.get().sourceDir( anon.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for local class" )
		public void sourceDir_ReturnsContainerForLocalClass( Test.Case tc ) {
			class Local{};
			Local local = new Local();
			tc.assertEqual( myDir, App.get().sourceDir( local.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for nested anonymous class" )
		public void sourceDir_ReturnsContainerForNestedAnonymousClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.anon.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for nested class" )
		public void sourceDir_ReturnsContainerForNestedClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for nested local class" )
		public void sourceDir_ReturnsContainerForNestedLocalClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.get().getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns container for nested nested class" )
		public void sourceDir_ReturnsContainerForNestedNestedClass( Test.Case tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.inner.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns directory" )
		public void sourceDir_ReturnsDirectory( Test.Case tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isDirectory( App.get().sourceDir( obj.getClass()) ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns non null" )
		public void sourceDir_ReturnsNonNull( Test.Case tc ) {
			Object obj = new Object() {};
			tc.assertNonNull( App.get().sourceDir( obj.getClass() ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns readable" )
		public void sourceDir_ReturnsReadable( Test.Case tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isReadable( App.get().sourceDir( obj.getClass()) ) );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Throws App Excetion for missing source dir" )
		public void sourceDir_ThrowsAppExcetionForMissingSourceDir( Test.Case tc ) {
			tc.expectError( AppException.class );
			App.get().sourceDir( Object.class );
		}

		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Throws assertion error for null class" )
		public void sourceDir_ThrowsAssertionErrorForNullClass( Test.Case tc ) {
			tc.expectError( AssertionError.class );
			App.get().sourceDir( (Class<?>) null );
		}
					
		@Test.Impl( member = "public Path App.sourceDir(Class)", description = "Returns writeable" )
		public void sourceDir_ReturnsWriteable( Test.Case tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isWritable( App.get().sourceDir( obj.getClass()) ) );
		}

		@Test.Impl( member = "public void App.terminateOnShutdown(App.OnShutdown)", description = "Throws assertion error for null" )
		public void terminateOnShutdown_ThrowsAssertionErrorForNull( Test.Case tc ) {
			tc.expectError( AssertionError.class );
			App.get().terminateOnShutdown( null );
		}
		
		@Test.Impl( member = "public void App.terminateOnShutdown(App.OnShutdown)", description = "Registers hook" )
		public void terminateOnShutdown_RegistersHook( Test.Case tc ) {
			List<App.OnShutdown> witness = new ArrayList<App.OnShutdown>();
			List<?> hooks = this.getSubjectField( App.get(), "objectsForShutdown", witness );
			int size = hooks.size();
			App.OnShutdown hook = new App.OnShutdown() { public void terminate() {} };
			App.get().terminateOnShutdown( hook );
			tc.assertEqual( size + 1,  hooks.size() );
		}

		
		
}
