/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
public class Validate {
	
	private static final List<String> errMessages = new LinkedList<String>();
	
	private static final List<String> warnMessages = new LinkedList<String>();

	
	public static void printErrors() {
		Validate.errMessages.forEach( System.err::println );
	}
	
	
	public static void printWarnings() {
		Validate.warnMessages.forEach( System.out::println );
	}
	
	
	public static void print() {
		Validate.printErrors();
		Validate.printWarnings();
	}

	
	public static void err( String description, String... details ) {
		Assert.nonEmpty( description );
		
		Validate.errMessages.add( "" );
		Validate.errMessages.add( "ERROR: " + description );
		
		Validate.errMessages.add( "    Location: " );
		App.get().getFrames( "sog",  10 ).stream()
			.map( f -> "        (" + f.getFileName() + ":" + f.getLineNumber() + ")" )
			.forEach( Validate.errMessages::add );
		
		Validate.errMessages.add( "    Details:" );
		Arrays.stream( details ).forEach( s -> Validate.errMessages.add( "        " + s ) );
		Validate.errMessages.add( "" );
	}
	
	
	
	public static void warn( String description, String... details) {
		Assert.nonEmpty( description );
		
		Validate.warnMessages.add( "" );
		Validate.warnMessages.add( "WARNING: " + description );
		
		Exception e = new Exception();
		Validate.warnMessages.add( "    Location: " );
		Function<StackTraceElement, String> location = 
			ste -> { return "        (" + ste.getFileName() + ":" + ste.getLineNumber() + ")"; };
		Consumer<StackTraceElement> write = ste -> { Validate.warnMessages.add( location.apply(ste) ); };
		Arrays.stream( e.getStackTrace() ).limit( 5 ).forEach( write );
		
		Validate.warnMessages.add( "    Details:" );
		Arrays.stream( details ).forEach( s -> Validate.warnMessages.add( "        " + s ) );
		Validate.warnMessages.add( "" );
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
				"Container class = " + container.value(), "Skip = " + skip.value() );
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

		// ELSE: container != null && skip == null
		Container result = null;
		try {
			Class<?> clazz = Class.forName( container.value() );
			result = (Container) clazz.getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			Validate.err( "Unable to construct Container", e.toString(), container.value() );
			return null;
		}
		
		if ( !subjectClass.equals( result.subjectClass() ) ) {
			Validate.err( "Bad container", subjectClass.getName(), result.subjectClass().getName() );
			return null;
		}
		
		return result;
	}

}
