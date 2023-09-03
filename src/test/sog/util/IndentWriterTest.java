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
import sog.util.IndentWriter;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class IndentWriterTest extends Test.Container {
	
	public IndentWriterTest() {
		super( IndentWriter.class );
	}
	
	
	
	// TEST CASES:
	

	
//	private final ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//	
//	public Procedure beforeEach() {
//		return () -> { baos.reset(); };
//	}
//
//
//	// Test implementations
//
//	@Test.Impl( member = "public IndentWriter(OutputStream)", description = "Default indent" )
//	public void IndentWriter_DefaultIndent( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos );
//		out.increaseIndent();
//		out.println( "FOO" );
//		tc.assertEqual( "    FOO\n",  this.baos.toString() );
//	}
//
//	@Test.Impl( member = "public void IndentWriter.println(String)", description = "Before increase no prefix" )
//	public void println_BeforeIncreaseNoPrefix( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos );
//		out.println( "FOO" );
//		tc.assertEqual( "FOO\n",  this.baos.toString() );
//	}
//	
//	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Indent can be empty" )
//	public void IndentWriter_IndentCanBeEmpty( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, "" );
//		out.println( "FOO" );
//	}
//
//	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Increase empty indent is noop" )
//	public void increaseIndent_IncreaseEmptyIndentIsNoop( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, "" );
//		out.println( "FOO" );
//		String result = this.baos.toString();
//		this.baos.reset();
//		out.increaseIndent();
//		out.println( "FOO" );
//		tc.assertEqual( result,  this.baos.toString() );
//	}
//	
//	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Throws assertion error for null indent" )
//	public void IndentWriter_ThrowsAssertionErrorForNullIndent( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		new IndentWriter( this.baos, null );
//	}
//
//	@Test.Impl( member = "public IndentWriter(OutputStream, String)", description = "Throws assertion error for null stream" )
//	public void IndentWriter_ThrowsAssertionErrorForNullStream( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		new IndentWriter( null, "" );
//	}
//
//	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Can decrease after increase" )
//	public void decreaseIndent_CanDecreaseAfterIncrease( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, ">>>" );
//		out.increaseIndent();
//		out.decreaseIndent();
//		out.increaseIndent();
//		out.increaseIndent();
//		out.decreaseIndent();
//		out.decreaseIndent();
//	}
//
//	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Illegal state for decrease before increase" )
//	public void decreaseIndent_IllegalStateForDecreaseBeforeIncrease( Test.Case tc ) {
//		tc.expectError( IllegalStateException.class );
//		IndentWriter out = new IndentWriter( this.baos, ">>>" );
//		out.decreaseIndent();
//	}
//
//	@Test.Impl( member = "public void IndentWriter.decreaseIndent()", description = "Illegal state for more decreases than increases" )
//	public void decreaseIndent_IllegalStateForMoreDecreasesThanIncreases( Test.Case tc ) {
//		tc.expectError( IllegalStateException.class );
//		IndentWriter out = new IndentWriter( this.baos, ">>>" );
//		try {
//			out.increaseIndent();
//			out.decreaseIndent();
//		} catch ( Exception e ) {
//			// Ignore
//		}
//		out.decreaseIndent();
//	}
//
//	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Can increase indent" )
//	public void increaseIndent_CanIncreaseIndent( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, ">>>" );
//		out.increaseIndent();
//	}
//
//	@Test.Impl( member = "public void IndentWriter.println(String)", description = "Prints prefix" )
//	public void println_PrintsPrefix( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
//		out.increaseIndent();
//		out.println( "FOO" );
//		String result = this.baos.toString();
//		tc.assertTrue( result.startsWith( "XXXXFOO" ) );
//	}
//	
//	@Test.Impl( member = "public void IndentWriter.increaseIndent()", description = "Increase indent increases indent" )
//	public void increaseIndent_IncreaseIndentIncreasesIndent( Test.Case tc ) {
//		IndentWriter out = new IndentWriter( this.baos, "XXXX" );
//		out.increaseIndent();
//		out.increaseIndent();
//		out.println( "FOO" );
//		String result = this.baos.toString();
//		tc.assertTrue( result.startsWith( "XXXXXXXXFOO" ) );
//	}

    @Test.Impl( 
    	member = "constructor: IndentWriter()", 
    	description = "No-arg uses both defaults" 
    )
    public void tm_0486AB245( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: IndentWriter(OutputStream)", 
    	description = "Default indent" 
    )
    public void tm_0E26FC4D0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: IndentWriter(OutputStream, String)", 
    	description = "Indent can be empty" 
    )
    public void tm_0FE271080( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: IndentWriter(OutputStream, String)", 
    	description = "Throws assertion error for null indent" 
    )
    public void tm_0B120DD0F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: IndentWriter(OutputStream, String)", 
    	description = "Throws assertion error for null stream" 
    )
    public void tm_0152FD6E3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "constructor: IndentWriter(String)", 
    	description = "Default OutputStream" 
    )
    public void tm_020B2AB55( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.close()", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_06724718E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.close()", 
    	description = "Write fails after close" 
    )
    public void tm_0AD1F2A3E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.decreaseIndent()", 
    	description = "Can decrease after increase" 
    )
    public void tm_00F336D20( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.decreaseIndent()", 
    	description = "Custom indent removed" 
    )
    public void tm_02588D823( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.decreaseIndent()", 
    	description = "Illegal state for decrease before increase" 
    )
    public void tm_07B52193F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.decreaseIndent()", 
    	description = "Illegal state for more decreases than increases" 
    )
    public void tm_0B491AEA0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.decreaseIndent()", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_0B663743C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent()", 
    	description = "Can increase indent" 
    )
    public void tm_06F4FB3C6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent()", 
    	description = "Increase empty indent is noop" 
    )
    public void tm_00AE17341( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent()", 
    	description = "Increase indent increases indent" 
    )
    public void tm_0753E805B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent()", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_0AE62F618( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent(String)", 
    	description = "Custom indent used after default" 
    )
    public void tm_0DC11A75A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent(String)", 
    	description = "Custom indent used" 
    )
    public void tm_06F3D563D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.increaseIndent(String)", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_07E028CC9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable)", 
    	description = "Default shows all locations" 
    )
    public void tm_00FB1B28C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable)", 
    	description = "Default shows elements from all classes" 
    )
    public void tm_0BB9DBA67( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable)", 
    	description = "Details of the error are included" 
    )
    public void tm_0F23E27D0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable)", 
    	description = "Throws AssertionError for null error" 
    )
    public void tm_0B1D1F02D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "Details of the error are included" 
    )
    public void tm_0A5057D8B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "Elements have classes matching the given regexp" 
    )
    public void tm_0C6E5F746( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_0B9FCF873( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "Throws Assertion Error for null error" 
    )
    public void tm_01C0072E0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "Throws AssertionError for null regexp" 
    )
    public void tm_0BB3BF915( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.printErr(Throwable, String)", 
    	description = "regexp can be empty" 
    )
    public void tm_082E2C7E8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println()", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_02D2D3B77( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(Object)", 
    	description = "Null object is allowed" 
    )
    public void tm_0A89D8B62( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(Object)", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_0B0E7FE16( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(Printable)", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_01636E22C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(String)", 
    	description = "Before increase no prefix" 
    )
    public void tm_092C05D70( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(String)", 
    	description = "Prints prefix" 
    )
    public void tm_024A4ADC8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.println(String)", 
    	description = "Return this IndentWriter to allow chaining" 
    )
    public void tm_0ECB50EE8( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: IndentWriter IndentWriter.stringIndentWriter()", 
    	description = "After toString write calls are ignored" 
    )
    public void tm_00B78E5B2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }



	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( IndentWriter.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( IndentWriter.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}

	
	
}
