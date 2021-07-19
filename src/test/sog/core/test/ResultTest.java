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

import sog.core.App;
import sog.core.Test;
import sog.core.test.Result;
import sog.util.IndentWriter;

/**
 * 
 */
public class ResultTest extends Test.Container {

	public ResultTest() {
		super( Result.class );
	}
	
	
	static class MyResult extends Result {
		protected MyResult( String label ) { super( label ); }
		@Override public long getElapsedTime() { return 1000L; }
		@Override public int getPassCount() { return 2000; }
		@Override public int getFailCount() { return 3000; }
		@Override public void print( IndentWriter out ) {}
	}
	
	

	@Test.Impl( member = "constructor: Result(String)", description = "Label is included in the toString() value" )
	public void Result_LabelIsIncludedInTheTostringValue( Test.Case tc ) {
		String label = "LABEL_XXX";
		MyResult r = new MyResult( label );
		tc.assertTrue( r.toString().startsWith( label ) );
	}

    @Test.Impl( member = "constructor: Result(String)", description = "Throws AssertionError for empty label" )
	public void Result_ThrowsAssertionerrorForEmptyLabel( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new MyResult( "" );
	}

	@Test.Impl( member = "method: String Result.toString()", description = "Includes elapsed time" )
	public void toString_IncludesElapsedTime( Test.Case tc ) {
		MyResult res = new MyResult( "foo" );
		tc.assertTrue( res.toString().contains( "1.0s" ) );
	}

	@Test.Impl( member = "method: String Result.toString()", description = "Includes the fail count" )
	public void toString_IncludesTheFailCount( Test.Case tc ) {
		MyResult res = new MyResult( "foo" );
		tc.assertTrue( res.toString().contains( "F = 3000" ) );
	}

	@Test.Impl( member = "method: String Result.toString()", description = "Includes the pass count" )
	public void toString_IncludesThePassCount( Test.Case tc ) {
		MyResult res = new MyResult( "foo" );
		tc.assertTrue( res.toString().contains( "P = 2000" ) );
	}

	@Test.Impl( member = "method: String Result.toString()", description = "Includes the total count" )
	public void toString_IncludesTheTotalCount( Test.Case tc ) {
		MyResult res = new MyResult( "foo" );
		tc.assertTrue( res.toString().contains( "Count = 5000" ) );
	}
	
	
	
	public static void main( String[] args ) {
		Test.eval( Result.class );
	}
        
}
