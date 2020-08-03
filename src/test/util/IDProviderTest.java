/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.IDProvider;

/**
 * @author sundquis
 *
 */
public class IDProviderTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return IDProvider.class;
	}

	// Test implementations
	
	

	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "Case sensitive" )
	public void get_CaseSensitive( TestCase tc ) {
		String[] args = { "hello", "world", "foo", "bar", "hello2", "world2" };
		for ( String arg : args ) {
			String cap = arg.substring(0,1).toUpperCase() + arg.substring(1);
			tc.assertTrue( IDProvider.get(arg) != IDProvider.get(cap) );
		}
	}

	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "Short names distinct" )
	public void get_ShortNamesDistinct( TestCase tc ) {
		String lower = "abcdefghijklmnopqrstuvwxyz";
		String upper = lower.toUpperCase();
		String other = "._0123456789!@#$%^&*()<>?;:";
		String data = lower + upper + other;
		Map<Integer, String> results = new TreeMap<>();
		String arg;
		int id;
		for ( int i = 0; i < data.length(); i++ ) {
			for ( int j = 0; j < data.length(); j++ ) {
				arg = "" + data.charAt(i) + data.charAt(j);
				id = IDProvider.get( arg );
				if ( results.containsKey( id ) ) {
					tc.assertEqual( arg,  results.get( id ) );
				}
				results.put( id,  arg );
			}
		}
		tc.pass();
	}
	
	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "Throws assertion error for empty name" )
	public void get_ThrowsAssertionErrorForEmptyName( TestCase tc ) {
		tc.expectError( AssertionError.class );
		IDProvider.get("");
	}
	
	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "Long strings have id" )
	public void get_LongStringsHaveId( TestCase tc ) {
		IDProvider.get( "123456789012345678901234567890123456789012345678901234567890" );
		tc.pass();
	}
	
	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "No collision dict test" )
	public void get_NoCollisionDictTest( TestCase tc ) throws Exception {
		File file = new File( "/usr/share/dict/american-english" );
		try ( BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {
			Map<Integer, String> results = new TreeMap<>();
			String line;
			while ( (line = br.readLine()) != null ) {
				int id = IDProvider.get( line );
				if ( results.containsKey( id ) ) {
					tc.assertEqual( line,  results.get( id ) );
				}
				results.put( id, line );
			}
		} 
		tc.pass();
	}
	
	@TestOrig.Impl( src = "public int IDProvider.get(String)", desc = "Qualified name stress test" )
	public void get_QualifiedNameStressTest( TestCase tc ) throws Exception {
		File file = new File( "/usr/share/dict/american-english" );
		try ( BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {
			Map<Integer, String> results = new TreeMap<>();
			String line = "";
			String name = "";
			int counter = 0;
			while ( (line = br.readLine()) != null ) {
				counter++;
				if ( counter % 15 == 0 ) {
					name = line;
				}
				if ( counter % 15 == 5 ) {
					name = name + "." + line;
				}
				if ( counter % 15 == 10 ) {
					name = name + "." + line;
					int id = IDProvider.get( name );
					if ( results.containsKey( id ) ) {
						tc.assertEqual( name,  results.get( id ) );
					}
					results.put( id, name );
				}
			}
		} 
		tc.pass();
	}



	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new TestOrig(IDProviderTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
