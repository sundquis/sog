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

import sog.core.Fatal;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class FatalTest extends Test.Container {
	
	public FatalTest() {
		super( Fatal.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: void Fatal.error(String)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_0A177EC2E( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.error( "" );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.error(String, Throwable)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_05F9142B0( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.error( "", new Exception() );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.error(String, Throwable)", 
		description = "Throws AssertionError for null cause" 
	)
	public void tm_0B1BEAC15( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.error( "Help", null );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.impossible(String)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_026303D19( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.impossible( "" );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.impossible(String, Throwable)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_0CB99C0E5( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.impossible( "", new Exception() );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.impossible(String, Throwable)", 
		description = "Throws AssertionError for null cause" 
	)
	public void tm_0C410788A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.impossible( "Help", null );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.unimplemented(String)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_04AF178CD( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.unimplemented( "" );
	}
		
	@Test.Impl( 
		member = "method: void Fatal.warning(String)", 
		description = "Throws AssertionError for empty detail message" 
	)
	public void tm_02618F51A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Fatal.warning( "" );
	}
	
	
	

	public static void main( String[] args ) {
		//Test.eval( Fatal.class );
		//Test.evalPackage( Fatal.class );
		Test.evalAll();
	}
}
