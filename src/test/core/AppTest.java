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
import sog.core.Procedure;
import sog.core.Property;
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class AppTest implements TestContainer {

		@Override
		public Class<?> subjectClass() {
			return App.class;
		}

		public Path mySource;
		
		public Path myDir;
		
		public Procedure beforeAll() {
			return new Procedure() {
				public void exec () {
					mySource = App.get().sourceFile( AppTest.class );
					myDir = App.get().sourceDir( AppTest.class );
				}
			};
		}

		
		@TestOrig.Impl( src = "public App App.get()", desc = "Is not null" )
		public void get_IsNotNull( TestCase tc ) {
			tc.notNull( App.get() );
		}

		@TestOrig.Impl( src = "public String App.description()", desc = "Is not empty" )
		public void description_IsNotEmpty( TestCase tc ) {
			tc.assertFalse( App.get().description().isEmpty() );
		}

		@TestOrig.Impl( src = "public void App.run()", desc = "Calls terminate on shutdown" )
		public void run_CallsTerminateOnShutdown( TestCase tc ) {
			// TOGGLE
			/* */ tc.addMessage( "Manually tested" ).pass(); /*
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
			tc.pass();
			// */
		}

		@TestOrig.Impl( src = "public Path App.root()", desc = "Is not null" )
		public void root_IsNotNull( TestCase tc ) {
			tc.notNull( App.get().root() );
		}

		@TestOrig.Impl( src = "public Path App.root()", desc = "Is readable" )
		public void root_IsReadable( TestCase tc ) {
			tc.assertTrue( Files.isReadable( App.get().root() ) );
		}

		@TestOrig.Impl( src = "public Path App.root()", desc = "Is wrieable" )
		public void root_IsWrieable( TestCase tc ) {
			tc.assertTrue( Files.isWritable( App.get().root() ) );
		}
		
		@TestOrig.Impl( src = "public String App.description()", desc = "Is not null" )
		public void description_IsNotNull( TestCase tc ) {
			tc.notNull( App.get().description() );
		}

		@TestOrig.Impl( src = "public List App.sourceDirs()", desc = "Is not empty" )
		public void sourceDirs_IsNotEmpty( TestCase tc ) {
			tc.assertTrue( App.get().sourceDirs().size() > 0 );
		}

		@TestOrig.Impl( src = "public List App.sourceDirs()", desc = "Is not null" )
		public void sourceDirs_IsNotNull( TestCase tc ) {
			tc.notNull( App.get().sourceDirs() );
		}
		
		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns container for anonymous class" )
		public void sourceFile_ReturnsContainerForAnonymousClass( TestCase tc ) {
			Object anon = new Object(){};
			tc.assertEqual( mySource, App.get().sourceFile( anon.getClass() ) );
		}

		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns container for local class" )
		public void sourceFile_ReturnsContainerForLocalClass( TestCase tc ) {
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
		
		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns container for nested anonymous class" )
		public void sourceFile_ReturnsContainerForNestedAnonymousClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.anon.getClass() ) );
		}

		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)",
			desc = "Returns container for nested class" )
		public void sourceFile_ReturnsContainerForNestedClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.getClass() ) );
		}
		
		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns container for nested local class" )
		public void sourceFile_ReturnsContainerForNestedLocalClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.get().getClass() ) );
		}

		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns container for nested nested class" )
		public void sourceFile_ReturnsContainerForNestedNestedClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( mySource, App.get().sourceFile( n.inner.getClass() ) );
		}

		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns non null" )
		public void sourceFile_ReturnsNonNull( TestCase tc ) {
			tc.notNull( App.get().sourceFile( Property.class ) );
		}
					
		@TestOrig.Impl( 
			src = "public Path App.sourceFile(Class)", 
			desc = "Returns readable" )
		public void sourceFile_ReturnsReadable( TestCase tc ) {
			tc.assertTrue( Files.isReadable( App.get().sourceFile( ByteFile.class ) ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceFile(Class)", desc = "Throws App Excetion for missing source file" )
		public void sourceFile_ThrowsAppExcetionForMissingSourceFile( TestCase tc ) {
			tc.expectError( AppException.class );
			App.get().sourceFile( Object.class );
		}

		@TestOrig.Impl( src = "public Path App.sourceFile(Class)", desc = "Throws assertion error for null class" )
		public void sourceFile_ThrowsAssertionErrorForNullClass( TestCase tc ) {
			tc.expectError( AssertionError.class );
			App.get().sourceFile( null );
		}
					
		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for anonymous class" )
		public void sourceDir_ReturnsContainerForAnonymousClass( TestCase tc ) {
			Object anon = new Object() {};
			tc.assertEqual( myDir, App.get().sourceDir( anon.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for local class" )
		public void sourceDir_ReturnsContainerForLocalClass( TestCase tc ) {
			class Local{};
			Local local = new Local();
			tc.assertEqual( myDir, App.get().sourceDir( local.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for nested anonymous class" )
		public void sourceDir_ReturnsContainerForNestedAnonymousClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.anon.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for nested class" )
		public void sourceDir_ReturnsContainerForNestedClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for nested local class" )
		public void sourceDir_ReturnsContainerForNestedLocalClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.get().getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns container for nested nested class" )
		public void sourceDir_ReturnsContainerForNestedNestedClass( TestCase tc ) {
			Nested n = new Nested();
			tc.assertEqual( myDir, App.get().sourceDir( n.inner.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns directory" )
		public void sourceDir_ReturnsDirectory( TestCase tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isDirectory( App.get().sourceDir( obj.getClass()) ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns non null" )
		public void sourceDir_ReturnsNonNull( TestCase tc ) {
			Object obj = new Object() {};
			tc.notNull( App.get().sourceDir( obj.getClass() ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns readable" )
		public void sourceDir_ReturnsReadable( TestCase tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isReadable( App.get().sourceDir( obj.getClass()) ) );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Throws App Excetion for missing source dir" )
		public void sourceDir_ThrowsAppExcetionForMissingSourceDir( TestCase tc ) {
			tc.expectError( AppException.class );
			App.get().sourceDir( Object.class );
		}

		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Throws assertion error for null class" )
		public void sourceDir_ThrowsAssertionErrorForNullClass( TestCase tc ) {
			tc.expectError( AssertionError.class );
			App.get().sourceDir( (Class<?>) null );
		}
					
		@TestOrig.Impl( src = "public Path App.sourceDir(Class)", desc = "Returns writeable" )
		public void sourceDir_ReturnsWriteable( TestCase tc ) {
			Object obj = new Object() {};
			tc.assertTrue( Files.isWritable( App.get().sourceDir( obj.getClass()) ) );
		}

		@TestOrig.Impl( src = "public void App.terminateOnShutdown(App.OnShutdown)", desc = "Throws assertion error for null" )
		public void terminateOnShutdown_ThrowsAssertionErrorForNull( TestCase tc ) {
			tc.expectError( AssertionError.class );
			App.get().terminateOnShutdown( null );
		}
		
		@TestOrig.Impl( src = "public void App.terminateOnShutdown(App.OnShutdown)", desc = "Registers hook" )
		public void terminateOnShutdown_RegistersHook( TestCase tc ) {
			List<App.OnShutdown> witness = new ArrayList<App.OnShutdown>();
			List<?> hooks = this.getSubjectField( App.get(), "objectsForShutdown", witness );
			int size = hooks.size();
			App.OnShutdown hook = new App.OnShutdown() { public void terminate() {} };
			App.get().terminateOnShutdown( hook );
			tc.assertEqual( size + 1,  hooks.size() );
		}

		
		
	public static void main(String[] args) {

		System.out.println();

		new TestOrig(AppTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
