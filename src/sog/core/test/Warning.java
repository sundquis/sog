/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import sog.core.Test;
import sog.util.IndentWriter;

/**
 * Register a warning encountered while processing tests.
 * 
 * Instances represent non-serious defects that may be ignored, as opposed to errors, which
 * must be fixed.
 * 
 * Error and Warning Policy:
 * 
 * On any type of member (Class, Constructor, Field, Method) if Test.Case is present:
 * 		Case, !Impl: Error - Unimplemented (generates a stub)
 * 		Case,  Impl, Fail: Error - Test Fail
 * 		Case,  Impl, Pass: Ok
 * 
 * The strictest policy is applied to public methods:
 * 		!Case, !Skip: Error - Untested
 * 		!Case,  Skip: Warning - Skipping public method...
 * 
 * For protected or package methods:
 * 		!Case, !Skip: Warning - Unstated test policy
 * 		!Case,  Skip: Ok
 * 
 * For private methods, no annotation is required:
 * 		!Case, !Skip: Ok
 * 
 * For public classes, constructors, or fields the policy is the same as protected methods:
 * 		!Case, !Skip: Warning - Unstated test policy
 * 		!Case,  Skip: Ok
 * 
 * For other protection levels, no annotation is required:
 * 		!Case, !Skip: Ok
 */
public class Warning {
	
	
	// Class
	public static boolean isSkipped( AnnotatedElement elt, Class<?> c ) {
		Test.Skip skip = Warning.getSkipAnnotation( elt );
		if ( skip == null ) {
			return false;
		} else {
			if ( Modifier.isPublic( c.getModifiers() ) ) {
				Warning.log( "Unstated test policy",  "Untested public class", c.getName(), skip.value() );
			}
			return true;
		}
	}
	
	// Constructor
	public static boolean isSkipped( AnnotatedElement elt, Constructor<?> c ) {
		Test.Skip skip = Warning.getSkipAnnotation( elt );
		if ( skip == null ) {
			return false;
		} else {
			if ( Modifier.isPublic( c.getModifiers() ) ) {
				Warning.log( "Unstated test policy",  "Untested public constructor", c.getName(), skip.value() );
			}
			return true;
		}
	}

	// Field
	public static boolean isSkipped( AnnotatedElement elt, Field f ) {
		Test.Skip skip = Warning.getSkipAnnotation( elt );
		if ( skip == null ) {
			return false;
		} else {
			if ( Modifier.isPublic( f.getModifiers() ) ) {
				Warning.log( "Unstated test policy",  "Untested public field", f.getName(), skip.value() );
			}
			return true;
		}
	}

	// Method
	public static boolean isSkipped( AnnotatedElement elt, Method m ) {
		Test.Skip skip = Warning.getSkipAnnotation( elt );
		if ( skip == null ) {
			return false;
		} else {
			if ( Modifier.isPublic( m.getModifiers() ) ) {
				Warning.log( "Unstated test policy",  "Untested public method", m.getName(), skip.value() );
			}
			return true;
		}
	}

	public static Test.Skip getSkipAnnotation( AnnotatedElement elt ) {
		return elt.getDeclaredAnnotation( Test.Skip.class );
	}

	
	// FIXME: Save a list of warnings, print later.
	private static final IndentWriter warning = new IndentWriter( System.out );

	/**
	 * 
	 */
	public static void log( String description, String... details ) {
		Warning.warning.println( ">>> " + description );
		Arrays.stream( details ).forEach( s -> Warning.warning.println( ">>>     " + s ) );
	}

}
