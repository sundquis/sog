/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.Arrays;

import sog.core.Assert;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * 
 */
public class Validate {

	private static final IndentWriter err = new IndentWriter( System.err );
	
	private static final IndentWriter warn = new IndentWriter( System.out );
	
	
	public static void err( String description, String... details) {
		Assert.nonEmpty( description );
		
		// FIXME Capture frame data
		
		Validate.err.println( description );
		
		Validate.err.increaseIndent();
		Arrays.stream( details ).forEach( Validate.err::println );
		Validate.err.decreaseIndent();
	}
	
	
	public static void warn( String description, String... details) {
		Assert.nonEmpty( description );
		
		// FIXME Capture frame data and save messages to print later
		
		Validate.warn.println( description );
		
		Validate.warn.increaseIndent();
		Arrays.stream( details ).forEach( Validate.warn::println );
		Validate.warn.decreaseIndent();
	}

	
	/**
	 * Valid subject class must pass a number of checks. If any fail the return is null
	 * signaling that the class is skipped as a subject class.
	 * 
	 * 
	 * @param subjectClass
	 * @return
	 */
	public static Container getContainer( Class<?> subjectClass ) {
		Assert.nonNull( subjectClass );
		
		if ( subjectClass.getEnclosingClass() != null ) {
			Validate.err( "Subject must be a top-level class", subjectClass.getName() );
			return null;
		}
		
		if ( Container.class.isAssignableFrom( subjectClass ) ) {
			Validate.err( "Subbject cannot be a container", subjectClass.getName() );
			return null;
		}
		
		Test.Container container = subjectClass.getDeclaredAnnotation( Test.Container.class );
		Test.Skip skip = subjectClass.getDeclaredAnnotation( Test.Skip.class );
		
		if ( container != null && skip != null ) {
			Validate.err( "Inconsistent meta-data", "Subject = " + subjectClass.getName(),
				"Container class = " + container.clazz().getName(), "Skip = " + skip.value() );
			return null;
		}
		
		if ( container == null && skip == null ) {
			Validate.err( "No container declared for subject", subjectClass.getName() );
			return null;
		}
		
		if ( container == null && skip != null ) {
			Validate.warn( "Skipping:", subjectClass.toString() );
			return null;
		}

		Container result = null;
		try {
			result = (Container) container.clazz().getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			Validate.err( "Unable to construct Container", container.clazz().getName(), e.getMessage() );
			return null;
		}
		
		if ( !subjectClass.equals( result.subjectClass() ) ) {
			Validate.err( "Bad container", subjectClass.getName(), result.subjectClass().getName() );
			return null;
		}
		
		return result;
	}

}
