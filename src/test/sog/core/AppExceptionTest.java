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

import sog.core.AppException;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class AppExceptionTest extends Test.Container {
	
	public AppExceptionTest() {
		super( AppException.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: AppException(String)", 
		description = "Throws AsserionError for empty message" 
	)
	public void tm_0CE4F475E( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new AppException( "" );
	}
	
	@Test.Impl( 
		member = "constructor: AppException(String)", 
		description = "Throws AsserionError for null message" 
	)
	public void tm_0C3B35DDA( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		String msg = null;
		new AppException( msg );
	}
	
	@Test.Impl( 
		member = "constructor: AppException(String, Throwable)", 
		description = "Throws AsserionError for empty message" 
	)
	public void tm_0D1CD07BE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		String msg = "";
		Throwable th = new Throwable();
		new AppException( msg, th );
	}
	
	@Test.Impl( 
		member = "constructor: AppException(String, Throwable)", 
		description = "Throws AsserionError for null cause" 
	)
	public void tm_05CE3C35C( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		String msg = "hi";
		Throwable th = null;
		new AppException( msg, th );
	}
	
	@Test.Impl( 
		member = "constructor: AppException(String, Throwable)", 
		description = "Throws AsserionError for null message" 
	)
	public void tm_0B34C117A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		String msg = null;
		Throwable th = new Throwable();
		new AppException( msg, th );
	}
	
	@Test.Impl( 
		member = "constructor: AppException(Throwable)", 
		description = "Throws AsserionError for null cause" 
	)
	public void tm_066DF6237( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Throwable th = null;
		new AppException( th );
	}
	
	
	

	public static void main( String[] args ) {
		Test.eval( AppException.class );
		//Test.evalPackage( AppException.class );
	}
}
