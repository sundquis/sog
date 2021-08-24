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

import sog.core.SoftString;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class SoftStringTest extends Test.Container {
	
	public SoftStringTest() {
		super( SoftString.class );
	}
	
	
	
	// TEST CASES
	
	
	
	@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Can construct empty" 
		)
		public void tm_096E5EE31( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Can construct long strings" 
		)
		public void tm_00BADCA10( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Can construct short strings" 
		)
		public void tm_0ABC71682( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Correct value after collection" 
		)
		public void tm_024B1A4E0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Strings longer than threshold are soft" 
		)
		public void tm_0D154678A( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "constructor: SoftString(String)", 
			description = "Throws assertion error for null strings" 
		)
		public void tm_06146F0C6( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String SoftString.toString()", 
			description = "Result is not null" 
		)
		public void tm_004B0AE5D( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String SoftString.toString()", 
			description = "Stress test correct value" 
		)
		public void tm_0E92F44B9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean SoftString.equals(Object)", 
			description = "Consistent with compare" 
		)
		public void tm_0CB2A9AC0( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean SoftString.equals(Object)", 
			description = "Sample cases equal" 
		)
		public void tm_04C1E0530( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean SoftString.equals(Object)", 
			description = "Sample cases not equal" 
		)
		public void tm_0CB41A063( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int SoftString.compareTo(SoftString)", 
			description = "Can sort large collections" 
		)
		public void tm_03B0327E1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: int SoftString.hashCode()", 
			description = "Sample cases equal" 
		)
		public void tm_0B5F2E8CE( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}


	public static void main( String[] args ) {
		Test.eval( SoftString.class );
		//Test.evalPackage( SoftString.class );
	}
}
