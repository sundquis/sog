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

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import sog.core.Strings;
import sog.core.Test;

import sog.util.Fault;
import sog.util.IndentWriter;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class FaultTest extends Test.Container {
	
	public FaultTest() {
		super( Fault.class );
	}
	
	
	private class FaultData {
		
		private final Fault fault;
		private final ByteArrayOutputStream sos;
		private final IndentWriter out;
		private final Object source;
		
		private FaultData( String description ) {
			this.source = new Object() {};
			this.fault = new Fault( this.source, description );
			this.sos = new ByteArrayOutputStream();
			this.out = new IndentWriter( this.sos );
		}
		
		private Fault getFault() { return this.fault; }
		
		private Object getSource() { return this.source; }
		
		private String print() { 
			this.fault.print( this.out );
			return this.sos.toString();
		}		
	}
	
	
	private static Consumer<Fault> getListener() {
		return new Consumer<>() {
    		String accepted = "";
			@Override public void accept( Fault fault ) { this.accepted += fault.toString(); }
    		@Override public String toString() { return this.accepted; }
    	};

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
    	FaultData fd = new FaultData( "descr" );
    	List<Integer> list = List.of( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 );
    	fd.getFault().addDetail( list );
    	tc.assertTrue( fd.print().contains( Strings.toString( list ) ) );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Detail is appended to previous details" 
    )
    public void tm_0412E937F( Test.Case tc ) {
    	FaultData fd = new FaultData( "sequential" );
    	String detail1 = "The first detail";
    	fd.getFault().addDetail( detail1 );
    	String detail2 = "The second message";
    	fd.getFault().addDetail( detail2 );
    	String details = fd.print();
    	tc.assertTrue( details.indexOf( detail1 ) < details.indexOf( detail2 ) );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Return is this Fault instance" 
    )
    public void tm_010465E44( Test.Case tc ) {
    	FaultData fd = new FaultData( "Chaining" );
    	tc.assertEqual( fd.getFault(), fd.getFault().addDetail( "detail" ) );
    }
    
    @Test.Impl( 
    	member = "method: Fault Fault.addDetail(Object)", 
    	description = "Throws AssertionError for null detail" 
    )
    public void tm_0650C6DE1( Test.Case tc ) {
    	FaultData fd = new FaultData( "Description" );
    	tc.expectError( AssertionError.class );
    	fd.getFault().addDetail( null );
    }
    
    @Test.Impl( 
    	member = "method: String Fault.toString()", 
    	description = "Returns non-empty description" 
    )
    public void tm_03EB994FF( Test.Case tc ) {
    	String description = "This is the description message.";
    	FaultData fd = new FaultData( description );
    	tc.assertTrue( fd.getFault().toString().contains( description ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Subsequent faults for given source are deleivered to listener" 
    )
    public void tm_06FD45E03( Test.Case tc ) {
    	Consumer<Fault> listener = FaultTest.getListener();
    	Object source = new Object();

    	new Fault( source, "Before" ).toss();
    	Fault.addListener( source, listener );
    	new Fault( source, "After 1" ).toss();
    	new Fault( source, "After 2" ).toss();

    	String results = listener.toString();
    	tc.assertFalse( results.contains( "Before" ) );
    	tc.assertTrue( results.contains( "After 1" ) );
    	tc.assertTrue( results.contains( "After 2" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Subsequent faults for other sources are ignored" 
    )
    public void tm_000B17977( Test.Case tc ) {
    	Consumer<Fault> listener = FaultTest.getListener();
    	Object source = new Object();

    	Fault.addListener( source, listener );
    	new Fault( source, "My source" ).toss();
    	new Fault( new Object() {}, "Other source" ).toss();
    	String results = listener.toString();
    	
    	tc.assertTrue( results.contains( "My source" ) );
    	tc.assertFalse( results.contains( "Other source" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_04BF0AFB3( Test.Case tc ) {
    	Consumer<Fault> listener = null;
    	tc.expectError( AssertionError.class );
    	Fault.addListener( new Object() {}, listener );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.addListener(Object, Consumer)", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_01FEC265A( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Fault.addListener( null, FaultTest.getListener() );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "All other listeners are ignored" 
    )
    public void tm_09504E73D( Test.Case tc ) {
    	FaultData fd = new FaultData( "Delivery" );
    	
    	Consumer<Fault> listener1 = FaultTest.getListener();
    	Consumer<Fault> listener2 = FaultTest.getListener();
    	Consumer<Fault> listener3 = FaultTest.getListener();

    	Fault.addListener( fd.getSource(), listener1 );
    	Fault.addListener( fd.getSource(), listener2 );
    	Fault.addListener( fd.getSource(), listener3 );
    	
    	fd.getFault().deliver( listener3 );
    	
    	tc.assertTrue( listener1.toString().isEmpty() );
    	tc.assertTrue( listener2.toString().isEmpty() );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "Fault is delivered to given listener" 
    )
    public void tm_016C466B7( Test.Case tc ) {
    	FaultData fd = new FaultData( "Individual Delivery" );
    	Consumer<Fault> listener = FaultTest.getListener();
    	fd.getFault().deliver( listener );
    	tc.assertTrue( listener.toString().contains( "Individual Delivery" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.deliver(Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_058FA2396( Test.Case tc ) {
    	FaultData fd = new FaultData( "null" );
    	tc.expectError( AssertionError.class );
    	fd.getFault().deliver( null );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "All details printed" 
    )
    public void tm_0BF452975( Test.Case tc ) {
    	FaultData fd = new FaultData( "Details" );
    	fd.getFault().addDetail( List.of(1, 2, 3) );
    	fd.getFault().addDetail( Set.of("A") );
    	fd.getFault().addDetail( "Additional details" );
    	String results  = fd.print();
    	tc.assertTrue( results.contains( "{1, 2, 3}" ) );
    	tc.assertTrue( results.contains( "{A}" ) );
    	tc.assertTrue( results.contains( "Additional details" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Description printed" 
    )
    public void tm_01BB1636E( Test.Case tc ) {
    	String description = "The detailed description";
    	FaultData fd = new FaultData( description );
    	tc.assertTrue( fd.print().contains( description ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Fault location printed" 
    )
    public void tm_0D286E8AF( Test.Case tc ) {
    	FaultData fd = new FaultData( "Location" );
    	tc.assertTrue( fd.print().contains( "tm_0D286E8AF" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Source is printed" 
    )
    public void tm_0B5603DC1( Test.Case tc ) {
    	FaultData fd = new FaultData( "Source" );
    	tc.assertTrue( fd.print().contains( fd.getSource().toString() ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.print(IndentWriter)", 
    	description = "Throws AssertionError for null writer" 
    )
    public void tm_0D7AF15F4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new Fault( new Object() {}, "descr" ).print( null );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Subsequent faults for given source are not deleivered to listener" 
    )
    public void tm_00CB353FF( Test.Case tc ) {
    	Object source = new Object() {};
    	Consumer<Fault> listener = FaultTest.getListener();
    	
    	Fault.addListener( source, listener );
    	new Fault( source, "Attached" ).toss();
    	
    	Fault.removeListener( source, listener );
    	new Fault( source, "Removed" ).toss();

    	String results = listener.toString();
    	tc.assertTrue( results.contains( "Attached" ) );
    	tc.assertFalse( results.contains( "Removed" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Throws AssertionError for null listener" 
    )
    public void tm_0E7460FC2( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Fault.removeListener( new Object(), null );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.removeListener(Object, Consumer)", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_049E83E29( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Fault.removeListener( null, FaultTest.getListener() );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.toss()", 
    	description = "Fault is delivered to each registered listener" 
    )
    public void tm_099A5CDBB( Test.Case tc ) {
    	FaultData fd = new FaultData( "Multiple listeners" );
    	Object source = fd.getSource();

    	Consumer<Fault> listener1 = FaultTest.getListener();
    	Consumer<Fault> listener2 = FaultTest.getListener();
    	Consumer<Fault> listener3 = FaultTest.getListener();
    	
    	Fault.addListener( source, listener1 );
    	Fault.addListener( source, listener2 );
    	Fault.addListener( source, listener3 );
    	
    	fd.getFault().toss();
    	
    	tc.assertTrue( listener1.toString().contains( "Multiple listeners" ) );
    	tc.assertTrue( listener2.toString().contains( "Multiple listeners" ) );
    	tc.assertTrue( listener3.toString().contains( "Multiple listeners" ) );
    }
    
    @Test.Impl( 
    	member = "method: void Fault.toss()", 
    	description = "Listeners registered for other sources are ignored" 
    )
    public void tm_0E75C08B1( Test.Case tc ) {
    	FaultData fd = new FaultData( "Other sources" );

    	Consumer<Fault> listener1 = FaultTest.getListener();
    	Consumer<Fault> listener2 = FaultTest.getListener();
    	Consumer<Fault> listener3 = FaultTest.getListener();
    	
    	Fault.addListener( new Object() {}, listener1 );
    	Fault.addListener( new Object() {}, listener2 );
    	Fault.addListener( new Object() {}, listener3 );
    	
    	fd.getFault().toss();
    	
    	tc.assertTrue( listener1.toString().isEmpty() );
    	tc.assertTrue( listener2.toString().isEmpty() );
    	tc.assertTrue( listener3.toString().isEmpty() );
    }
	
	
	
	
	public static void main( String[] args ) {
		/* Toggle class results
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
