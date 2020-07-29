/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.nio.file.Files;
import java.nio.file.Path;

@Test.Skip
/**
 * @author sundquis
 * 
 * Static helper methods for testing normal conditions
 * All failures use Assert.fail() as a common failure exit
 * Methods return object being tested
 * 
 * Depends on:
 * 		NONE
 *
 */
public class Assert {
	
	/**
	 * Not intended to be instantiated.
	 */
	private Assert() {}

	public static <T> T nonNull( T obj ) {
		if ( obj == null ) {
			Assert.fail();
		}
		return obj;
	}
	
	public static String nonEmpty( String s ) {
		if ( s == null || s.isEmpty() ) {
			Assert.fail();
		}
		return s;
	}
	
	public static String boundedString( String s, int minLength, int maxLength ) {
		if ( s == null || s.length() < minLength || s.length() > maxLength ) {
			Assert.fail();
		}
		return s;
	}
	
	public static Path readableDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Path writeableDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isWritable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Path rwDirectory( Path p ) {
		if ( p == null || !Files.exists(p) || !Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Path readableFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Path writeableFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isWritable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Path rwFile( Path p ) {
		if ( p == null || !Files.exists(p) || Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p) ) {
			Assert.fail();
		}
		return p;
	}
	
	public static Integer nonZero( Integer n ) {
		if ( n == 0 ) {
			Assert.fail();
		}
		return n;
	}
	
	public static Integer positive( Integer n ) {
		if ( n <= 0 ) {
			Assert.fail();
		}
		return n;		
	}
	
	public static Integer nonNeg( Integer n ) {
		if ( n < 0 ) {
			Assert.fail();
		}
		return n;		
	}
	
	public static Integer lessThan( Integer n, Integer max ) {
		if ( n >= max ) {
			Assert.fail();
		}
		return n;
	}
	
	public static boolean isTrue( boolean predicate ) {
		if ( ! predicate ) {
			Assert.fail();
		}
		return predicate;
	}

	
	private static void fail() {
		throw new AssertionError();
	}
	


}
