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

import java.util.List;
import java.util.Map;

import sog.core.Strings;
import sog.core.Test;

import sog.util.Fault;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class FaultTest extends Test.Container {
	
	public FaultTest() {
		super( Fault.class );
	}
	
	
	private static class StringIndentWriter extends IndentWriter {
		
		final StringOutputStream sos;
		
		private StringIndentWriter( StringOutputStream sos ) {
			super( sos );
			this.sos = sos;
		}
		
		@Override
		public String toString() {
			String result = this.sos.toString();
			this.sos.reset();
			return result;
		}
		
	}
	
	private StringIndentWriter getWriter() {
		return new StringIndentWriter( new StringOutputStream() );
	}

	

	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: Fault(Object, String, Object[])", 
    	description = "Throws AssertionError for enpty description" 
    )
    public void tm_053046F5C( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Fault( new Object(), "" );
    }
    
    @Test.Impl( 
    	member = "constructor: Fault(Object, String, Object[])", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_0B0F29068( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Fault( null, "description" );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Detail converted using Strings.toString()" 
    )
    public void tm_098B1A867( Test.Case tc ) {
    	Fault fault = new Fault( new Object(), "My description" );
    	List<Integer> list = List.of( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 );
    	fault.addDetail( list );
    	Map<Integer, String> map = Map.of( 1, "A", 2, "B", 3, "C", 4, "D", 5, "E" );
    	fault.addDetail( map );
    	IndentWriter out = this.getWriter();
    	fault.print( out );
    	tc.assertEqual( Strings.toString( list ), out.toString() );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Detail is appended to previous details" 
    )
    public void tm_0412E937F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Return is this Fault instance" 
    )
    public void tm_010465E44( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Throws AssertionError for null detail" 
    )
    public void tm_0650C6DE1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String Fault.toString()", 
    	description = "Returns non-empty description" 
    )
    public void tm_03EB994FF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Subsequent faults for given source are deleivered to listener" 
    )
    public void tm_06FD45E03( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Subsequent faults for other sources are ignored" 
    )
    public void tm_000B17977( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_04BF0AFB3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_01FEC265A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "All other listeners are ignored" 
    )
    public void tm_09504E73D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "Fault is delivered to given listener" 
    )
    public void tm_016C466B7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_058FA2396( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "All details printed" 
    )
    public void tm_0BF452975( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Description printed" 
    )
    public void tm_01BB1636E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Fault location printed" 
    )
    public void tm_0D286E8AF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Source is printed" 
    )
    public void tm_0B5603DC1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0D7AF15F4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Subsequent faults for given source are not deleivered to listener" 
    )
    public void tm_00CB353FF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_0E7460FC2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_049E83E29( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.toss()", 
    	description = "Fault is delivered to each registered listener" 
    )
    public void tm_099A5CDBB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.toss()", 
    	description = "Listeners registered for other sources are ignored" 
    )
    public void tm_0E75C08B1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
	
	
	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( Fault.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( Fault.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
	

}
