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

package test.sog.core;


import sog.core.AppException;
import sog.core.Switch;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class SwitchTest extends Test.Container {
	
	public SwitchTest() {
		super( Switch.class );
	}
	
	
	private enum Type {
		A, B, C
	}
	
	private Switch<Type, Integer, String> getSwitch() {
		return new Switch<>();
	}
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: Switch()", 
		description = "Default handler returns null" 
	)
	public void tm_065ACB3A8( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.assertIsNull( sw.apply( Type.A, 1 ) );
		tc.assertIsNull( sw.apply( Type.B, 2 ) );
		tc.assertIsNull( sw.apply( Type.C, 3 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Switch.apply(Object, Object)", 
		description = "Throws AppException when handler raises exception" 
	)
	public void tm_00BEB3662( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		sw.addCase( Type.A, n -> { return "A"; } )
			.addCase( Type.B, n -> { return "B" + 1/n; } );

		sw.apply( Type.A, 0 );
		tc.expectError( AppException.class );
		sw.apply( Type.B, 0 );
	}
		
	@Test.Impl( 
		member = "method: Object Switch.apply(Object, Object)", 
		description = "Throws AssertionError for null key" 
	)
	public void tm_060AEDB6B( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.expectError( AssertionError.class );
		sw.apply( null, 1 );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Replaces previously added case" 
	)
	public void tm_03EDF885D( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		sw.addCase( Type.A, n -> { return "orig"; } );
		tc.assertEqual( "orig", sw.apply( Type.A, 0 ) );
		sw.addCase( Type.A, n -> { return "replace"; } );
		tc.assertEqual( "replace", sw.apply( Type.A, 0 ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Returns this Switch instance" 
	)
	public void tm_066A336EE( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.assertEqual( sw, sw.addCase( Type.A, n -> { return null; } ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Throws AssertionError for null handler" 
	)
	public void tm_065C4CEC1( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.expectError( AssertionError.class );
		sw.addCase( Type.A, null );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Throws AssertionError for null key" 
	)
	public void tm_01BC7C296( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.expectError( AssertionError.class );
		sw.addCase( null, n -> { return "fi"; } );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Replaces previously set default" 
	)
	public void tm_0CDE0172E( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		sw.addDefault( n -> { return "orig"; } );
		tc.assertEqual( "orig", sw.apply( Type.A, 0 ) );
		sw.addDefault( n -> { return "replace"; } );
		tc.assertEqual( "replace", sw.apply( Type.A, 0 ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Returns this Switch instance" 
	)
	public void tm_0469437EC( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.assertEqual( sw, sw.addDefault( n -> { return null; } ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Throws AssertionError for null handler" 
	)
	public void tm_0875F2A3F( Test.Case tc ) {
		Switch<Type, Integer, String> sw = this.getSwitch();
		tc.expectError( AssertionError.class );
		sw.addDefault( null );
	}
	
	
	

	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( Switch.class )
			.concurrent( true )
			.showDetails( true )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( Switch.class )
			.concurrent( false )
			.showDetails( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
}
