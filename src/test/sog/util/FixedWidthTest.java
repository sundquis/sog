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

import sog.core.Strings;
import sog.core.Test;
import sog.util.FixedWidth;

/**
 * 
 */
@Test.Skip( "Container" )
public class FixedWidthTest extends Test.Container {
	
	public FixedWidthTest() {
		super( FixedWidth.class );
	}
	
	

	
	// TEST CASES
	
	
    @Test.Impl( 
    	member = "constructor: FixedWidth()", 
    	description = "Empty formatter produces empty formatted string" 
    )
    public void tm_067EDD111( Test.Case tc ) {
    	tc.assertEqual( "", new FixedWidth().format( ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "Formatting strategy is applied to the corresponding column" 
    )
    public void tm_005ABB2F8( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.field( "1", 4, '+', (String s, int w, char c) -> "1111" )
    		.field( "2", 6, ' ', (String s, int w, char c) -> "222222" );
    	tc.assertEqual( "1111222222", fw.format( 1, 2 ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "If width is zero, the length of the column name determines the width" 
    )
    public void tm_09865B36C( Test.Case tc ) {
    	String name = "The long column name";
    	FixedWidth fw = new FixedWidth()
    		.field( name, 0, ' ', Strings::leftJustify );
    	tc.assertEqual( name.length(), fw.width() );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "Returns this FixedWidth instance to allow chaining" 
    )
    public void tm_0A9125D83( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth();
    	tc.assertEqual( fw, fw.field( "nam", 0, ' ', Strings::leftJustify ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "String representations formed using Strings.toString(...)" 
    )
    public void tm_04D32FF29( Test.Case tc ) {
    	List<Object> list = List.of( 1, "A", new Object() );
    	Object[] array = new Object[] { 2, "B", new Object() };
    	FixedWidth fw = new FixedWidth()
    		.left( "1", 40, '_' )
    		.left( "2", 40, '_' );
    	String row = fw.format( list, array );
    	tc.assertTrue( row.contains( Strings.toString( list ) ) );
    	tc.assertTrue( row.contains( Strings.toString( array ) ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_0307476C4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().field( "", 4, ' ', Strings::leftJustify );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.field(String, int, char, FixedWidth.Field)", 
    	description = "Throws AssertionError for null formatter" 
    )
    public void tm_0B1E39877( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().field( "name", 4, ' ', null );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Appends left justified field" 
    )
    public void tm_060EF5F4A( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "LEFT", 4, '+' );
    	tc.assertEqual( "1+++", fw.format( 1 ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Padding character is used" 
    )
    public void tm_0EC0AF220( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
        	.left( "LEFT", 4, '+' );
    	tc.assertTrue( fw.format( 1 ).contains( "+" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Returns this FixedWidth instance to allow chaining" 
    )
    public void tm_0563E877E( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth();
    	tc.assertEqual( fw, fw.left( "n", 4, ' ' ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_03291E3FF( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().left( "", 4, ' ' );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Throws AssertionError for negative width" 
    )
    public void tm_091AAE820( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().left( "xx", -1, ' ' );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Truncated for long" 
    )
    public void tm_0FFA98798( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "A", 4, ' ' );
    	tc.assertEqual( "This", fw.format( "This field is too long" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.left(String, int, char)", 
    	description = "Width is correct for short" 
    )
    public void tm_071DA6156( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "A", 10, ' ' );
    	tc.assertEqual( 10, fw.format( "short" ).length() );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Appends right justified field" 
    )
    public void tm_00FF1F88C( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.right( "RIGHT", 4, '+' );
    	tc.assertEqual( "+++1", fw.format( 1 ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Padding character is used" 
    )
    public void tm_0FEA3C48B( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.right( "RIGHT", 4, '+' );
    	tc.assertTrue( fw.format( 1 ).contains( "+" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Returns this FixedWidth instance to allow chaining" 
    )
    public void tm_0EAABB373( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth();
    	tc.assertEqual( fw, fw.right( "R", 4, ' ') );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Throws AssertionError for empty name" 
    )
    public void tm_001E1F0B4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().right( "", 4, ' ' );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Throws AssertionError for negative width" 
    )
    public void tm_01A916FD5( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().right( "xx", -1, ' ' );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Truncated for long" 
    )
    public void tm_0555BC78D( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.right( "name", 4, '_' );
    	tc.assertEqual( "long", fw.format( "This is too long" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.right(String, int, char)", 
    	description = "Width is correct for short" 
    )
    public void tm_0B25BDC4B( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.right( "nmae", 10, ' ' );
    	tc.assertEqual( 10, fw.format( "A" ).length() );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.sep(String)", 
    	description = "Included in formatted string for each row" 
    )
    public void tm_0B5DB5AF1( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 4, '=' ).sep( "<>" ).right( "R", 4, '=' );
    	tc.assertEqual( "(===<>===)", fw.format( "(", ")" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.sep(String)", 
    	description = "Multiple consecutive separators allowed" 
    )
    public void tm_04BB99747( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 4, '=' ).sep( "<>" ).sep( "<>" ).right( "R", 4, '=' );
    	tc.assertEqual( "(===<><>===)", fw.format( "(", ")" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.sep(String)", 
    	description = "Returns this FixedWidth instance to allow chaining" 
    )
    public void tm_0A32822E6( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth();
    	tc.assertEqual( fw, fw.sep( "+" ) );
    }
    
    @Test.Impl( 
    	member = "method: FixedWidth FixedWidth.sep(String)", 
    	description = "Throws AssertionError for empty separator" 
    )
    public void tm_00008C19F( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	new FixedWidth().sep( "" );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Fields are separated as specified" 
    )
    public void tm_019DCA587( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 1, ' ' ).sep( "1" ).left( "L", 1, ' ' ).sep( "2" ).right( "R", 1, ' ' );
    	tc.assertEqual( "A1B2C", fw.format( "A", "B", "C" ) );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Fields can be added after formatting" 
    )
    public void tm_013651872( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 4, ' ' );
    	tc.assertEqual( "A   ", fw.format( "A" ) );
    	fw.sep( "+" ).right( "R", 4, ' ' );
    	tc.assertEqual( "A   +   B", fw.format( "A", "B" ) );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Fields formatted in the order specified" 
    )
    public void tm_0C321D2F2( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.field( "N", 4, ' ', (s, w, c) -> "XXXX" ).field( "N", 4, ' ', (s, w, c) -> "YYYY" );
    	tc.assertEqual( "XXXXYYYY", fw.format( 1, 2 ) );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Null fields are allowed" 
    )
    public void tm_0BF1ECCC1( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 8, '_' );
    	Object[] args = new Object[] { null };
    	tc.assertEqual( "null____", fw.format( args ) );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Throws IllegalArgumentException for too few fields" 
    )
    public void tm_0D5AC38C8( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 4, ' ' ).right( "R", 4, ' ' );
    	tc.expectError( IllegalArgumentException.class );
    	fw.format( 1 );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.format(Object[])", 
    	description = "Throws IllegalArgumentException for too many fields" 
    )
    public void tm_0FDDFB6C9( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 4, ' ' ).right( "R", 4, ' ' );
    	tc.expectError( IllegalArgumentException.class );
    	fw.format( 1, 2, 3 );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.header()", 
    	description = "Includes configured column names" 
    )
    public void tm_0E267CDD7( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "LEFT", 0, ' ' ).right( "RIGHT", 0, ' ' );
    	tc.assertEqual( "LEFTRIGHT", fw.header() );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.header()", 
    	description = "Includes configured separators" 
    )
    public void tm_0F65CB587( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "LEFT", 0, ' ' ).sep( "<>" ).sep( "()" ).right( "RIGHT", 0, ' ' );
    	tc.assertEqual( "LEFT<>()RIGHT", fw.header() );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.header()", 
    	description = "Length of the header equals width()" 
    )
    public void tm_0A8706BA5( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "LEFT", 0, ' ' ).sep( "<>" ).sep( "()" ).right( "RIGHT", 0, ' ' );
    	tc.assertEqual( fw.width(), fw.header().length() );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.toString()", 
    	description = "Includes field count" 
    )
    public void tm_05F9D67E1( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
        	.left( "LEFT", 0, ' ' ).sep( "<>" ).sep( "()" ).right( "RIGHT", 0, ' ' );
    	tc.assertTrue( fw.toString().contains( "2 fields" ) );
    }
    
    @Test.Impl( 
    	member = "method: String FixedWidth.toString()", 
    	description = "Includes length" 
    )
    public void tm_0F1255484( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
        	.left( "LEFT", 0, ' ' ).sep( "<>" ).sep( "()" ).right( "RIGHT", 0, ' ' );
    	tc.assertTrue( fw.toString().contains( "width = " + fw.width() ) );
    }
    
    @Test.Impl( 
    	member = "method: int FixedWidth.width()", 
    	description = "Increases by field width when field appended" 
    )
    public void tm_06B74AF36( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 5, ' ' ).sep( "<>" ).right( "R", 6, ' ' );
    	int width = fw.width();
    	tc.assertEqual( width + 3, fw.left( "L", 3, ' ' ).width() );
    }
    
    @Test.Impl( 
    	member = "method: int FixedWidth.width()", 
    	description = "Increses by separator length when separator appended" 
    )
    public void tm_05088203F( Test.Case tc ) {
    	FixedWidth fw = new FixedWidth()
    		.left( "L", 5, ' ' ).sep( "<>" ).right( "R", 6, ' ' );
    	int width = fw.width();
    	tc.assertEqual( width + 2, fw.sep( "--" ).width() );
    }
    
    @Test.Impl( 
    	member = "method: int FixedWidth.width()", 
    	description = "Width is zero when constructed" 
    )
    public void tm_081CA4057( Test.Case tc ) {
    	tc.assertEqual( 0, new FixedWidth().width() );
    }
	

    

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( FixedWidth.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		//sog.util.Concurrent.safeModeOff();
		Test.evalPackage( FixedWidth.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		System.out.println( "\nDone!" );
	}
	
	

}
