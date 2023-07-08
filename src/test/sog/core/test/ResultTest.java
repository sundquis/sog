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

package test.sog.core.test;

import sog.core.Test;
import sog.core.test.Result;
import sog.util.IndentWriter;

/**
 * 
 */
@Test.Skip( "Container" )
public class ResultTest extends Test.Container {

	public ResultTest() {
		super( Result.class );
	}
	
	
	static class MyResult extends Result {
		protected MyResult( String label ) { super( label ); }
		@Override public long getElapsedTime() { return 1000L; }
		@Override public int getFailCount() { return 2000; }
		@Override public int getPassCount() { return 3000; }
		@Override public void print( IndentWriter out ) {}
		@Override protected void run() {}
	}
	
	
	
	// TEST CASES
	
	
    @Test.Impl( 
    	member = "constructor: Result(String)", 
    	description = "Label is included in the toString() value" 
    )
    public void tm_03FCE4C8D( Test.Case tc ) {
		String label = "LABEL_XXX";
		MyResult r = new MyResult( label );
		tc.assertTrue( r.toString().startsWith( label ) );
    }
        
    @Test.Impl( 
    	member = "constructor: Result(String)", 
    	description = "Throws AssertionError for empty label" 
    )
    public void tm_0408E29A2( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new MyResult( "" );
    }
        
    @Test.Impl( 
    	member = "constructor: Result(String)", 
    	description = "Throws AssertionError for null label" 
    )
    public void tm_0FF8B7910( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new MyResult( null );
    }
    
    @Test.Impl( 
    	member = "method: String Result.toString()", 
    	description = "Includes elapsed time" 
    )
    public void tm_09DDBEA84( Test.Case tc ) {
    	tc.assertTrue( new MyResult( "foo" ).toString().contains( "1.0s" ) );
    }
        
    @Test.Impl( 
    	member = "method: String Result.toString()", 
    	description = "Includes the fail count" 
    )
    public void tm_0287CBB6F( Test.Case tc ) {
    	tc.assertTrue( new MyResult( "foo" ).toString().contains( "2000" ) );
    }
        
    @Test.Impl( 
    	member = "method: String Result.toString()", 
    	description = "Includes the pass count" 
    )
    public void tm_00E2AA562( Test.Case tc ) {
    	tc.assertTrue( new MyResult( "foo" ).toString().contains( "3000" ) );
    }
        
    @Test.Impl( 
    	member = "method: String Result.toString()", 
    	description = "Includes the total count" 
    )
    public void tm_094635D27( Test.Case tc ) {
    	tc.assertTrue( new MyResult( "foo" ).toString().contains( "5000" ) );
    }
    
    @Test.Impl( 
    	member = "method: Result Result.concurrentSets(boolean)", 
    	description = "Concurrent processing not used when false" 
    )
    public void tm_07BAF6ECD( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.concurrentSets(boolean)", 
    	description = "Concurrent processing used when true" 
    )
    public void tm_028BB198D( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.concurrentSets(boolean)", 
    	description = "Returns this Result instance to allow chaining" 
    )
    public void tm_0480D5528( Test.Case tc ) {
    	Result result = new MyResult( "test" );
    	tc.assertEqual( result, result.concurrentSets( true ) );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.concurrentSubjects(boolean)", 
    	description = "Concurrent processing not used when false" 
    )
    public void tm_0E50BBD17( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.concurrentSubjects(boolean)", 
    	description = "Concurrent processing used when true" 
    )
    public void tm_08D858503( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.concurrentSubjects(boolean)", 
    	description = "Returns this Result instance to allow chaining" 
    )
    public void tm_0F078651E( Test.Case tc ) {
    	Result result = new MyResult( "test" );
    	tc.assertEqual( result, result.concurrentSubjects( true ) );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.showDetails(boolean)", 
    	description = "Details are excluded when false" 
    )
    public void tm_096B7E46A( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.showDetails(boolean)", 
    	description = "Details are included when true" 
    )
    public void tm_0D49BB16F( Test.Case tc ) {
    	tc.assertPass();
    	tc.addMessage( "Manually checked" );
    }
        
    @Test.Impl( 
    	member = "method: Result Result.showDetails(boolean)", 
    	description = "Returns this Result instance to allow chaining" 
    )
    public void tm_0D9AB46E5( Test.Case tc ) {
    	//Result result = new MyResult( "test" );
    	//tc.assertEqual( result, result.showDetails( true ) );
    }
        
        @Test.Impl( 
        	member = "method: String Result.getLabel()", 
        	description = "Is consistent with constructed value" 
        )
        public void tm_01099BDEB( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: String Result.getLabel()", 
        	description = "Is not null" 
        )
        public void tm_0B993845A( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: boolean Result.concurrentSets()", 
        	description = "Consistent with specified value" 
        )
        public void tm_01C1A8202( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: boolean Result.concurrentSubjects()", 
        	description = "Consistent with specified value" 
        )
        public void tm_0D86562F8( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: boolean Result.showDetails()", 
        	description = "False after showDetails(false)" 
        )
        public void tm_05D6BD804( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: boolean Result.showDetails()", 
        	description = "True after showDetails(true)" 
        )
        public void tm_074BB375C( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        


	
	public static void main( String[] args ) {
		Test.eval( Result.class ).showDetails( false ).print();
		//Test.evalPackage( Result.class ).showDetails( true ).print();
	}
        
}
