/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * 
 */
public class Msg implements Printable {
	
	
	private static final List<Msg> ERRORS = new ArrayList<Msg>();
	private static final List<Msg> WARNINGS = new ArrayList<Msg>();
	
	public static void error( Exception cause, String description, Object ... details ) {
		Msg.ERRORS.add( new Msg( "ERROR", description, cause, details ) );
	}

	public static void error( String description, Object ... details ) {
		Msg.ERRORS.add( new Msg( "ERROR", description, null, details ) );
	}

	public static void warning( Exception cause, String description, Object ... details ) {
		Msg.WARNINGS.add( new Msg( "WARNING", description, cause, details ) );
	}
	
	public static void warning( String description, Object ... details ) {
		Msg.WARNINGS.add( new Msg( "WARNING", description, null, details ) );
	}
	
	public static void printErrors() {
		IndentWriter err = new IndentWriter( System.err );
		Msg.ERRORS.forEach( e -> e.print( err ) );
		System.err.flush();
	}

	public static void printWarnings() {
		IndentWriter out = new IndentWriter( System.out );
		Msg.WARNINGS.forEach( w -> w.print( out ) );
		System.out.flush();
	}
	
	public static void print() {
		Msg.printErrors();
		Msg.printWarnings();
	}

	
	private final String category;
	private final String description;
	private final Exception cause;
	private final List<String> details;
	private final List<String> location;
	
	/**
	 * 
	 */
	private Msg( String category, String description, Exception cause, Object ... details) {
		this.category = Assert.nonEmpty( category );
		this.description = Assert.nonEmpty( description );
		this.cause = cause;
		this.details = Arrays.stream( details ).map( Strings::toString ).collect( Collectors.toList() );

		// FIXME: Adjust limit()
		this.location = App.get().getLocation( "sog" ).stream().skip(3).collect( Collectors.toList() );
	}

	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out );
		
		out.println( this.category + ": " + this.description );
		out.increaseIndent();
		
		if ( this.cause != null ) {
			out.println( "CAUSE: " + this.cause.toString() );
			out.increaseIndent();
			App.get().getLocation( this.cause ).forEach( out::println );
			out.decreaseIndent();
		}
		
		out.println( "DETAILS:" );
		out.increaseIndent();
		this.details.forEach( out::println );
		out.decreaseIndent();
		
		out.println( "LOCATION:" );
		out.increaseIndent();
		this.location.forEach( out::println );
		out.decreaseIndent();
		
		out.decreaseIndent();
		out.println( "" );
	}

}
