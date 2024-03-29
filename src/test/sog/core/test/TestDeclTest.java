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

import java.io.ByteArrayOutputStream;

import sog.core.Test;
import sog.core.test.TestDecl;
import sog.core.test.TestImpl;
import sog.util.IndentWriter;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestDeclTest extends Test.Container {

	public TestDeclTest() {
		super( TestDecl.class );
	}
	
	private static final String MEMBER_NAME = "MemebrName";
	private static final String DESCRIPTION = "Description";
	
	private TestDecl getDecl() {
		return new TestDecl( MEMBER_NAME, DESCRIPTION );
	}
	
	private TestImpl getImpl() {
		return TestImpl.forMethod( SomeContainer.class.getDeclaredMethods()[0] );
	}
	
	public class SomeContainer {
		@Test.Impl( member = MEMBER_NAME, description = DESCRIPTION )
		public void method() {}
	}
	


    @Test.Impl( 
    	member = "constructor: TestDecl(String, String)", 
    	description = "Throws AssertionError for emtpy description" 
    )
    public void tm_0744E823D( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestDecl( "name", "" );
    }
        
    @Test.Impl( 
    	member = "constructor: TestDecl(String, String)", 
    	description = "Throws AssertionError for emtpy member name" 
    )
    public void tm_02A1D6E92( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestDecl( "", "description" );
    }
        
    @Test.Impl( 
    	member = "constructor: TestDecl(String, String)", 
    	description = "Throws AssertionError for null description" 
    )
    public void tm_07C7C1B57( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestDecl( "name", null );
    }
        
    @Test.Impl( 
    	member = "constructor: TestDecl(String, String)", 
    	description = "Throws AssertionError for null member name" 
    )
    public void tm_0324B07AC( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new TestDecl( null, "description" );
    }
        
	@Test.Impl( 
		member = "method: boolean TestDecl.setImpl(TestImpl)", 
		description = "First call returns true"
	)
	public void tm_01E20C91E( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		tc.assertTrue( decl.setImpl( impl ) );
	}

	@Test.Impl( 
		member = "method: boolean TestDecl.setImpl(TestImpl)", 
		description = "Second call returns false" 
	)
	public void tm_00BBFC671( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		decl.setImpl( impl );
		tc.assertFalse( decl.setImpl( impl ) );
	}

	@Test.Impl( 
		member = "method: boolean TestDecl.setImpl(TestImpl)", 
		description = "Throws AssertionError for null TestImpl" 
	)
	public void tm_0E190932E( Test.Case tc ) {
		TestDecl decl = this.getDecl();
		tc.expectError( AssertionError.class );
		decl.setImpl( null );
	}

	@Test.Impl( 
		member = "method: boolean TestDecl.unimplemented()", 
		description = "After setImpl returns false" 
	)
	public void tm_03388949E( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		decl.setImpl( impl );
		tc.assertFalse( decl.unimplemented() );
	}

	@Test.Impl( 
		member = "method: boolean TestDecl.unimplemented()", 
		description = "Before setImpl returns true" 
	)
	public void tm_07DEBD7E4( Test.Case tc ) {
		TestDecl decl = this.getDecl();
		tc.assertTrue( decl.unimplemented() );
	}

	@Test.Impl( 
		member = "method: void TestDecl.print(IndentWriter)", 
		description = "Output includes Test.Impl" 
	)
	public void tm_0F238E18B( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		decl.setImpl( impl );
		decl.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( "@Test.Impl" ) );
	}

	@Test.Impl( 
		member = "method: void TestDecl.print(IndentWriter)", 
		description = "Output includes description" 
	)
	public void tm_0F3CCC7CB( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		decl.setImpl( impl );
		decl.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( DESCRIPTION ) );
	}

	@Test.Impl( 
		member = "method: void TestDecl.print(IndentWriter)", 
		description = "Output includes member name" 
	)
	public void tm_0A99BB420( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		decl.setImpl( impl );
		decl.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( MEMBER_NAME ) );
	}

	@Test.Impl( 
		member = "method: void TestDecl.print(IndentWriter)", 
		description = "Output includes method name" 
	)
	public void tm_01FDC4239( Test.Case tc ) {
		TestDecl decl = this.getDecl(); TestImpl impl = this.getImpl();
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		decl.setImpl( impl );
		decl.print( new IndentWriter( sos ) );
		tc.assertTrue( sos.toString().contains( decl.getMethodName() ) );
	}

	@Test.Impl( 
		member = "method: void TestDecl.print(IndentWriter)", 
		description = "Throws AssertionError for null IndentWriter" 
	)
	public void tm_071B0803F( Test.Case tc ) {
		TestDecl decl = this.getDecl();
		tc.expectError( AssertionError.class );
		decl.print( null );
	}

	
	
	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( TestDecl.class )
			.concurrent( true )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		Test.evalPackage( TestDecl.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
	}
	
}
