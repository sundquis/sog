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

import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.TestIdentifier;

/**
 * 
 */
public class TestIdentifierTest extends Test.Container {

	public TestIdentifierTest() {
		super( TestIdentifier.class );
	}
	
	private static final String NAME = "The member name";
	private static final String DESC  ="The description of a test";
	
	private TestIdentifier id;
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.id = new TestIdentifier( NAME, DESC ) {};
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.id = null;
		};
	}
	
	
    @Test.Impl( 
    	member = "constructor: TestIdentifier(String, String)", 
    	description = "Throws AssertionError for empty description" 
    )
    public void tm_097062DF4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestIdentifier( NAME, "" ) {};
    }
        
    @Test.Impl( 
    	member = "constructor: TestIdentifier(String, String)", 
    	description = "Throws AssertionError for empty member name" 
    )
    public void tm_04CD51A49( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestIdentifier( "", DESC ) {};
    }
        
    @Test.Impl( 
    	member = "constructor: TestIdentifier(String, String)", 
    	description = "Throws AssertionError for null description" 
    )
    public void tm_0323FA938( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestIdentifier( NAME, null ) {};
    }
        
    @Test.Impl( 
    	member = "constructor: TestIdentifier(String, String)", 
    	description = "Throws AssertionError for null member name" 
    )
    public void tm_0E80E958D( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestIdentifier( null, DESC ) {};
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getDescription()", 
    	description = "Return is consistent with value supplied to constructor" 
    )
    public void tm_02952996B( Test.Case tc ) {
    	tc.assertEqual( DESC, this.id.getDescription() );
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getDescription()", 
    	description = "Return is non-empty" 
    )
    public void tm_0ADDB51FC( Test.Case tc ) {
    	tc.notEmpty( this.id.getDescription() );
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getKey()", 
    	description = "If two pairs are equal then keys are equal" ,
    	weight = 4
    )
    public void tm_0B8708571( Test.Case tc ) {
    	String[] names = new String[] {
    		"First", "Another name", "Special # $ _ ! % ^ & characters", "Numbers 1, 2, 3, 4, 5, 6, 7, 8, 9, 0"	
    	};
    	String[] descriptions = new String[] {
    		"A description of a test case",
    		"Another, but slightly longer description of a different test case",
    		"A description, that for some reason contains !@#$%^&* characters",
    		"A description containing 1234567890 numbers"
    	};
    	TestIdentifier id1, id2;
    	for ( int i = 0; i < 4; i++ ) {
    		id1 = new TestIdentifier( names[i], descriptions[i] ) {};
    		id2 = new TestIdentifier( names[i], descriptions[i] ) {};
    		tc.assertEqual( id1.getKey(), id2.getKey() );
    	}
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getKey()", 
    	description = "If two pairs are not equal then keys are not equal",
    	weight = 4
    )
    public void tm_0E768CFD7( Test.Case tc ) {
    	String[] names = new String[] {
    		"First", "Another name", "Special # $ _ ! % ^ & characters", "Numbers 1, 2, 3, 4, 5, 6, 7, 8, 9, 0"	
    	};
    	String[] descriptions = new String[] {
    		"A description of a test case",
    		"Another, but slightly longer description of a different test case",
    		"A description, that for some reason contains !@#$%^&* characters",
    		"A description containing 1234567890 numbers"
    	};
    	TestIdentifier id1, id2;
    	for ( int i = 0; i < 4; i++ ) {
    		id1 = new TestIdentifier( names[i], descriptions[i] ) {};
    		id2 = new TestIdentifier( "X" + names[i], descriptions[i] ) {};
    		tc.assertFalse( id1.getKey().equals( id2.getKey() ) );

    		id1 = new TestIdentifier( names[i], descriptions[i] ) {};
    		id2 = new TestIdentifier( names[i] + "X", descriptions[i] ) {};
    		tc.assertFalse( id1.getKey().equals( id2.getKey() ) );
    		
    		id1 = new TestIdentifier( names[i], descriptions[i] ) {};
    		id2 = new TestIdentifier( names[i], "X" + descriptions[i] ) {};
    		tc.assertFalse( id1.getKey().equals( id2.getKey() ) );
    		
    		id1 = new TestIdentifier( names[i], descriptions[i] ) {};
    		id2 = new TestIdentifier( names[i], descriptions[i] + "X" ) {};
    		tc.assertFalse( id1.getKey().equals( id2.getKey() ) );
    	}
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getKey()", 
    	description = "Return is non-empty" 
    )
    public void tm_06238B85F( Test.Case tc ) {
    	String[] names = new String[] {
    		"First", "Another name", "Special # $ _ ! % ^ & characters", "Numbers 1, 2, 3, 4, 5, 6, 7, 8, 9, 0"	
    	};
    	String[] descriptions = new String[] {
    		"A description of a test case",
    		"Another, but slightly longer description of a different test case",
    		"A description, that for some reason contains !@#$%^&* characters",
    		"A description containing 1234567890 numbers"
    	};
    	TestIdentifier id;
    	for ( int i = 0; i < 4; i++ ) {
    		id = new TestIdentifier( names[i], descriptions[i] ) {};
    		tc.notEmpty( id.getKey() );
    	}
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getMemberName()", 
    	description = "Return is consistent with value supplied to constructor" 
    )
    public void tm_03656892A( Test.Case tc ) {
    	tc.assertEqual( NAME, this.id.getMemberName() );
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getMemberName()", 
    	description = "Return is non-empty" 
    )
    public void tm_083C7B53B( Test.Case tc ) {
    	tc.notEmpty( this.id.getMemberName() );
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.getMethodName()", 
    	description = "Return is non-empty" 
    )
    public void tm_07EEE5B02( Test.Case tc ) {
    	tc.notEmpty( this.id.getMethodName() );
    }
        
    @Test.Impl( 
    	member = "method: String TestIdentifier.toString()", 
    	description = "Return is non-empty" 
    )
    public void tm_000A913E2( Test.Case tc ) {
    	tc.notEmpty( this.id.toString() );
    }

	
	public static void main( String[] args ) {
		//Test.eval( TestIdentifier.class );
		Test.evalPackage( TestIdentifier.class );
	}
	
}
