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
package test.sog.core.test;

import sog.core.Test;
import sog.core.test.TestResultSet;

/**
 * 
 */
public class TestResultSetTest extends Test.Container {

	public TestResultSetTest() {
		super( TestResultSet.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: TestResultSet(String)", 
		description = "Default has verbose = false" 
	)
	public void tm_017656908( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" );
	}
		
		@Test.Impl( 
			member = "constructor: TestResultSet(String, boolean)", 
			description = "Throws AssertionError for empty label" 
		)
		public void tm_0E5BF9170( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: TestResultSet(String, boolean)", 
			description = "Throws AssertionError for null label" 
		)
		public void tm_0A1C6DF82( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(Class)", 
			description = "Adds one TestResult" 
		)
		public void tm_02334FB7C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(Class)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_0337F4643( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(Class)", 
			description = "Throws AssertionError for null class" 
		)
		public void tm_0FAD19EEE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(String)", 
			description = "Adds one TestResult" 
		)
		public void tm_0A82CCD55( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(String)", 
			description = "Records error message if class is not found" 
		)
		public void tm_0B778BFB9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(String)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_077F1B3DC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(String)", 
			description = "Throws AssertionError for empty class name" 
		)
		public void tm_0D15DAF7C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClass(String)", 
			description = "Throws AssertionError for null class name" 
		)
		public void tm_0D0425EEC( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
			description = "Adds one TestResult for each valid class name" 
		)
		public void tm_022AD7F13( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_02A4BB5FB( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addClasses(Stream)", 
			description = "Throws AssertionError for null class names stream" 
		)
		public void tm_0830D4A18( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addResult(Result)", 
			description = "Elapsed time reflects new total" 
		)
		public void tm_06E3B48EE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addResult(Result)", 
			description = "Fail count reflects new total" 
		)
		public void tm_0BDAFF652( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addResult(Result)", 
			description = "Pass count reflects new total" 
		)
		public void tm_0C19DE97F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.addResult(Result)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_03C567283( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forAllSourceDirs()", 
			description = "Aggregates TestResult instances for every class under every source directory" 
		)
		public void tm_004D9EEC5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forAllSourceDirs()", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_0C565278F( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forPackage(Class)", 
			description = "Aggregates TestResult instances for every class in the same package as the given class" 
		)
		public void tm_0A1A84C84( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forPackage(Class)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_037C6939D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forPackage(Class)", 
			description = "Throws AssertionError for null class" 
		)
		public void tm_0A01E3E54( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
			description = "Aggregates TestResult instances for every class under the given source directory" 
		)
		public void tm_0D63BED47( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_0491CBEDA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.forSourceDir(Path)", 
			description = "Throws AssertionError for null source path" 
		)
		public void tm_08EB90389( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: TestResultSet TestResultSet.setVerbose(boolean)", 
			description = "Return is this TestResultSet instance" 
		)
		public void tm_07A9EAA2A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int TestResultSet.getFailCount()", 
			description = "Value is the sum of all failing weights for all tests" 
		)
		public void tm_06A537EE4( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int TestResultSet.getPassCount()", 
			description = "Value is the sum of all passing weights for all tests" 
		)
		public void tm_0584C9B7E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: long TestResultSet.getElapsedTime()", 
			description = "Value is the total elapsed time for all tests" 
		)
		public void tm_0ED41C40C( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void TestResultSet.print(IndentWriter)", 
			description = "If verbose is true includes details from each TestResult" 
		)
		public void tm_04519880D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void TestResultSet.print(IndentWriter)", 
			description = "Includes messages for each bad classname" 
		)
		public void tm_067F458BE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void TestResultSet.print(IndentWriter)", 
			description = "Includes summary for each TestResult" 
		)
		public void tm_06F277C47( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void TestResultSet.print(IndentWriter)", 
			description = "Results are printed in alphabetaical order" 
		)
		public void tm_0119BF2CF( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: void TestResultSet.print(IndentWriter)", 
			description = "Throws AssertionError for null writer" 
		)
		public void tm_06C10F0A5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}

	
	
	

	public static void main( String[] args ) {
		Test.eval( TestResultSet.class );
		//Test.evalPackage( TestResultSet.class );
	}
}
