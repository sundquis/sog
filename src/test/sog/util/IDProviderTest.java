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
import sog.util.IDProvider;

/**
 * @author sundquis
 *
 */
@Test.Skip( "Container" )
public class IDProviderTest extends Test.Container {
	
	public IDProviderTest() {
		super( IDProvider.class );
	}
	
	
	
	// TEST CASES:
	

//	@Test.Impl( member = "public int IDProvider.get(String)", description = "Case sensitive" )
//	public void get_CaseSensitive( Test.Case tc ) {
//		String[] args = { "hello", "world", "foo", "bar", "hello2", "world2" };
//		for ( String arg : args ) {
//			String cap = arg.substring(0,1).toUpperCase() + arg.substring(1);
//			tc.assertTrue( IDProvider.get(arg) != IDProvider.get(cap) );
//		}
//	}
//
//	@Test.Impl( member = "public int IDProvider.get(String)", description = "Short names distinct" )
//	public void get_ShortNamesDistinct( Test.Case tc ) {
//		String lower = "abcdefghijklmnopqrstuvwxyz";
//		String upper = lower.toUpperCase();
//		String other = "._0123456789!@#$%^&*()<>?;:";
//		String data = lower + upper + other;
//		Map<Integer, String> results = new TreeMap<>();
//		String arg;
//		int id;
//		for ( int i = 0; i < data.length(); i++ ) {
//			for ( int j = 0; j < data.length(); j++ ) {
//				arg = "" + data.charAt(i) + data.charAt(j);
//				id = IDProvider.get( arg );
//				if ( results.containsKey( id ) ) {
//					tc.assertEqual( arg,  results.get( id ) );
//				}
//				results.put( id,  arg );
//			}
//		}
//	}
//	
//	@Test.Impl( member = "public int IDProvider.get(String)", description = "Throws assertion error for empty name" )
//	public void get_ThrowsAssertionErrorForEmptyName( Test.Case tc ) {
//		tc.expectError( AssertionError.class );
//		IDProvider.get("");
//	}
//	
//	@Test.Impl( member = "public int IDProvider.get(String)", description = "Long strings have id" )
//	public void get_LongStringsHaveId( Test.Case tc ) {
//		IDProvider.get( "123456789012345678901234567890123456789012345678901234567890" );
//	}
//	
//	@Test.Impl( member = "public int IDProvider.get(String)", description = "No collision dict test" )
//	public void get_NoCollisionDictTest( Test.Case tc ) throws Exception {
//		File file = new File( "/usr/share/dict/american-english" );
//		try ( BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {
//			Map<Integer, String> results = new TreeMap<>();
//			String line;
//			while ( (line = br.readLine()) != null ) {
//				int id = IDProvider.get( line );
//				if ( results.containsKey( id ) ) {
//					tc.assertEqual( line,  results.get( id ) );
//				}
//				results.put( id, line );
//			}
//		} 
//	}
//	
//	@Test.Impl( member = "public int IDProvider.get(String)", description = "Qualified name stress test" )
//	public void get_QualifiedNameStressTest( Test.Case tc ) throws Exception {
//		File file = new File( "/usr/share/dict/american-english" );
//		try ( BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {
//			Map<Integer, String> results = new TreeMap<>();
//			String line = "";
//			String name = "";
//			int counter = 0;
//			while ( (line = br.readLine()) != null ) {
//				counter++;
//				if ( counter % 15 == 0 ) {
//					name = line;
//				}
//				if ( counter % 15 == 5 ) {
//					name = name + "." + line;
//				}
//				if ( counter % 15 == 10 ) {
//					name = name + "." + line;
//					int id = IDProvider.get( name );
//					if ( results.containsKey( id ) ) {
//						tc.assertEqual( name,  results.get( id ) );
//					}
//					results.put( id, name );
//				}
//			}
//		} 
//	}

    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "Case sensitive" 
    )
    public void tm_0300B0318( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "Long strings have id" 
    )
    public void tm_0DB6C1F03( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "No collision dict test" 
    )
    public void tm_04618EAA1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "Qualified name stress test" 
    )
    public void tm_08CA6DD9F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "Short names distinct" 
    )
    public void tm_0A89C74AA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int IDProvider.get(String)", 
    	description = "Throws assertion error for empty name" 
    )
    public void tm_06C14FD82( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }

        

	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( IDProvider.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( IDProvider.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/

		App.get().done();
	}
	
	
	
}
