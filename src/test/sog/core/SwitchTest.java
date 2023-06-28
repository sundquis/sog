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
import sog.core.Procedure;
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
	
	private Switch<Type, Integer, String> sw;
	
	@Override
	public Procedure beforeEach() {
		return () -> {
			this.sw = new Switch<>();
		};
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			this.sw = null;
		};
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: Switch()", 
		description = "Default handler returns null" 
	)
	public void tm_065ACB3A8( Test.Case tc ) {
		tc.assertIsNull( this.sw.apply( Type.A, 1 ) );
		tc.assertIsNull( this.sw.apply( Type.B, 2 ) );
		tc.assertIsNull( this.sw.apply( Type.C, 3 ) );
	}
		
	@Test.Impl( 
		member = "method: Object Switch.apply(Object, Object)", 
		description = "Throws AppException when handler raises exception" 
	)
	public void tm_00BEB3662( Test.Case tc ) {
		this.sw.addCase( Type.A, n -> { return "A"; } )
			.addCase( Type.B, n -> { return "B" + 1/n; } );

		this.sw.apply( Type.A, 0 );
		tc.expectError( AppException.class );
		this.sw.apply( Type.B, 0 );
	}
		
	@Test.Impl( 
		member = "method: Object Switch.apply(Object, Object)", 
		description = "Throws AssertionError for null key" 
	)
	public void tm_060AEDB6B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.apply( null, 1 );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Replaces previously added case" 
	)
	public void tm_03EDF885D( Test.Case tc ) {
		this.sw.addCase( Type.A, n -> { return "orig"; } );
		tc.assertEqual( "orig", this.sw.apply( Type.A, 0 ) );
		this.sw.addCase( Type.A, n -> { return "replace"; } );
		tc.assertEqual( "replace", this.sw.apply( Type.A, 0 ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Returns this Switch instance" 
	)
	public void tm_066A336EE( Test.Case tc ) {
		tc.assertEqual( this.sw, this.sw.addCase( Type.A, n -> { return null; } ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Throws AssertionError for null handler" 
	)
	public void tm_065C4CEC1( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( Type.A, null );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addCase(Object, Function)", 
		description = "Throws AssertionError for null key" 
	)
	public void tm_01BC7C296( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( null, n -> { return "fi"; } );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Replaces previously set default" 
	)
	public void tm_0CDE0172E( Test.Case tc ) {
		this.sw.addDefault( n -> { return "orig"; } );
		tc.assertEqual( "orig", this.sw.apply( Type.A, 0 ) );
		this.sw.addDefault( n -> { return "replace"; } );
		tc.assertEqual( "replace", this.sw.apply( Type.A, 0 ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Returns this Switch instance" 
	)
	public void tm_0469437EC( Test.Case tc ) {
		tc.assertEqual( this.sw, this.sw.addDefault( n -> { return null; } ) );
	}
		
	@Test.Impl( 
		member = "method: Switch Switch.addDefault(Function)", 
		description = "Throws AssertionError for null handler" 
	)
	public void tm_0875F2A3F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addDefault( null );
	}
	
	
	

	public static void main( String[] args ) {
		Test.eval( Switch.class );
		//Test.evalPackage( Switch.class );
	}
}
