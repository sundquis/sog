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

package test.sog.util;

import sog.core.App;
import sog.core.Test;
import sog.util.Protection;

/**
 * 
 */
@Test.Skip( "Container" )
public class ProtectionTest extends Test.Container {
	
	public ProtectionTest() {
		super( Protection.class );
	}
	
	
	
	// TEST CASES:
	
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Class)", 
    	description = "Correct for package class" 
    )
    public void tm_0DFA6849C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Class)", 
    	description = "Correct for private class" 
    )
    public void tm_032CFE8D9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Class)", 
    	description = "Correct for protected class" 
    )
    public void tm_0F8C13F44( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Class)", 
    	description = "Correct for public class" 
    )
    public void tm_0356AFE59( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_00DE46488( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Member)", 
    	description = "Correct for package member" 
    )
    public void tm_09E09F6A8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Member)", 
    	description = "Correct for private member" 
    )
    public void tm_0B00D1A0B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Member)", 
    	description = "Correct for protected member" 
    )
    public void tm_0DB439A00( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Member)", 
    	description = "Correct for public member" 
    )
    public void tm_005AEC5D3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Protection.Level Protection.getProtectionLevel(Member)", 
    	description = "Throws AssertionError for null member" 
    )
    public void tm_02012EF84( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Class)", 
    	description = "Correct for non-package class" 
    )
    public void tm_0F4B8C977( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Class)", 
    	description = "Correct for package class" 
    )
    public void tm_010AA3777( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_046F5EACD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Member)", 
    	description = "Correct for non-package member" 
    )
    public void tm_011D623C3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Member)", 
    	description = "Correct for package member" 
    )
    public void tm_09CEC5EC3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPackageProtection(Member)", 
    	description = "Throws AssertionError for null member" 
    )
    public void tm_05ADBF889( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Class)", 
    	description = "Correct for non-private class" 
    )
    public void tm_0C0976557( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Class)", 
    	description = "Correct for private class" 
    )
    public void tm_0B8C41CD7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_00DA465CA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Member)", 
    	description = "Correct for non-private member" 
    )
    public void tm_0441F2209( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Member)", 
    	description = "Correct for private member" 
    )
    public void tm_089C44689( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPrivateProtection(Member)", 
    	description = "Throws AssertionError for null member" 
    )
    public void tm_02FD7A846( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Class)", 
    	description = "Correct for non-protected class" 
    )
    public void tm_0447AB737( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Class)", 
    	description = "Correct for protected class" 
    )
    public void tm_0B6E15137( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_0F5FDD3F5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Member)", 
    	description = "Correct for non-protected member" 
    )
    public void tm_0C995D433( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Member)", 
    	description = "Correct for protected member" 
    )
    public void tm_091A6FB33( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasProtectedProtection(Member)", 
    	description = "Throws AssertionError for null member" 
    )
    public void tm_0678E37B1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Class)", 
    	description = "Correct for non-public class" 
    )
    public void tm_06ECA8D5F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Class)", 
    	description = "Correct for public class" 
    )
    public void tm_0D29D27DF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Class)", 
    	description = "Throws AssertionError for null class" 
    )
    public void tm_044B6750E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Member)", 
    	description = "Correct for non-public member" 
    )
    public void tm_0DBEC9859( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Member)", 
    	description = "Correct for public member" 
    )
    public void tm_01EFCA5D9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean Protection.hasPublicProtection(Member)", 
    	description = "Throws AssertionError for null member" 
    )
    public void tm_0EAA2F68A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	
	

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Protection.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Protection.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

}
