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

package test.sog.core.test.bar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( ".TEST" )
public class ConcurrentTests {

	@Test.Skip( "For testing" )
	public ConcurrentTests() {}
	
	@Test.Decl( "Test 0" )
	@Test.Decl( "Test 1" )
	@Test.Decl( "Test 2" )
	@Test.Decl( "Test 3" )
	@Test.Decl( "Test 4" )
	@Test.Decl( "Test 5" )
	@Test.Decl( "Test 6" )
	@Test.Decl( "Test 7" )
	@Test.Decl( "Test 8" )
	@Test.Decl( "Test 9" )
	public void method1() {}
	
	@Test.Decl( "Test 0" )
	@Test.Decl( "Test 1" )
	@Test.Decl( "Test 2" )
	@Test.Decl( "Test 3" )
	@Test.Decl( "Test 4" )
	@Test.Decl( "Test 5" )
	@Test.Decl( "Test 6" )
	@Test.Decl( "Test 7" )
	@Test.Decl( "Test 8" )
	@Test.Decl( "Test 9" )
	public void method2() {}

	@Test.Skip( "Container" )
	public static class TEST extends Test.Container {

		public TEST() {
			super( ConcurrentTests.class );
		}
		
		private static Set<Thread> implThreads = Collections.synchronizedSet( new HashSet<>() );
		
		public static Set<Thread> getThreads() {
			return ConcurrentTests.TEST.implThreads;
		}
		
		
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 0" )
        public void tm_048AAE768( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
            
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 1" )
        public void tm_048AAEB29( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 2" )
        public void tm_048AAEEEA( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 3" )
        public void tm_048AAF2AB( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 4" )
        public void tm_048AAF66C( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 5" )
        public void tm_048AAFA2D( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 6" )
        public void tm_048AAFDEE( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 7" )
        public void tm_048AB01AF( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 8" )
        public void tm_048AB0570( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method1()", description = "Test 9" )
        public void tm_048AB0931( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 0" )
        public void tm_02A88B107( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 1" )
        public void tm_02A88B4C8( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 2" )
        public void tm_02A88B889( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 3" )
        public void tm_02A88BC4A( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 4" )
        public void tm_02A88C00B( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 5" )
        public void tm_02A88C3CC( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 6" )
        public void tm_02A88C78D( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 7" )
        public void tm_02A88CB4E( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 8" )
        public void tm_02A88CF0F( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }
        
        @Test.Impl( member = "method: void ConcurrentTests.method2()", description = "Test 9" )
        public void tm_02A88D2D0( Test.Case tc ) { implThreads.add( Thread.currentThread() ); tc.assertPass(); }

	}
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		//Test.eval( ConcurrentTests.class ).concurrentSubjects( false ).showDetails( true ).print();
		//*/

		/* Toggle package results
		// Some tests can fail with multiple threads due to exceeding specified resource limits.
		Test.evalPackage( ConcurrentTests.class )
			.concurrentSets( false )
			.concurrentSubjects( false )
			.showDetails( true )
			.print();
		//*/
		
		//ConcurrentTests.TEST.getThreads().forEach( System.out::println );
	}
}
