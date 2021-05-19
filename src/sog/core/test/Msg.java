/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sog.core.App;
import sog.core.Assert;
import sog.core.Strings;
import sog.util.IndentWriter;
import sog.util.Printable;

/**
 * 
 */
public class Msg implements Printable, Comparable<Msg> {
	

	private static final SortedSet<Msg> INSTANCES = new TreeSet<Msg>();
	
	private static int SEQNO = 0;
	
	private static enum Severity {
		INFO,
		WARNING,
		STUB,
		ERROR,
	}
	
	
	
	// Print >= given severity
	public static void print( Severity severity ) {
		IndentWriter out =  new IndentWriter( System.err );
		Msg.INSTANCES.stream()
			.filter( m -> m.severity.ordinal() >= severity.ordinal() )
			.forEach( m -> m.print( out ) );
	}

	// default all
	public static void print() {
		Msg.print( Severity.INFO );
	}
	
	
	public static Msg info( String description ) {
		return new Msg( Msg.Severity.INFO, description );
	}

	
	public static Msg warning( String description ) {
		return new Msg( Msg.Severity.WARNING, description );
	}
	
	
	public static void stub( String member, String description ) {
		new Msg( Msg.Severity.STUB, description )
			.addDetail( "FIXME" )
			.addDetail( "STUB FOR" )
			.addDetail( member )
			.addDetail( description );
	}
	
	
	public static Msg error( String description ) {
		return new Msg( Msg.Severity.ERROR, description );
	}

	
	
	private final Severity severity;
	private final int seqno;
	private final String description;
	private final List<String> location;
	private Throwable cause;
	private final List<String> details;
	
	private Msg( Severity severity, String description ) {
		this.severity = Assert.nonNull( severity );
		this.seqno = Msg.SEQNO++;
		this.description = Assert.nonEmpty( description );
		
		this.location = new ArrayList<String>();
		this.cause = null;
		this.details = new ArrayList<String>();
	}
	
	
	public Msg addLocation( int skip, int count ) {
		App.get().getLocation().skip( skip ).limit( count ).forEach( this.location::add );
		return this;
	}
	
		
	public Msg setCause( Throwable cause ) {
		this.cause = Assert.nonNull( cause );
		return this;
	}
	

	public Msg addDetail( Object obj ) {
		this.details.add( Strings.toString( Assert.nonNull( obj ) ) );
		return this;
	}
	
	
	public Msg addDetail( String label, Object obj ) {
		return this.addDetail( label + " = " + Strings.toString( obj ) );
	}
	
	

	
	@Override
	public void print( IndentWriter out ) {
		Assert.nonNull( out );

		out.println();
		out.println( this.severity.toString() + ": " + this.description );
		out.increaseIndent();
		
		if ( this.location.size() > 0 ) {
			out.println( "LOCATION:" );
			out.increaseIndent();
			this.location.forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.cause != null ) {
			out.println( "CAUSE: " + this.cause.toString() );
			out.increaseIndent();
			App.get().getLocation( this.cause ).forEach( out::println );
			out.decreaseIndent();
		}
		
		if ( this.details.size() > 0 ) {
			out.println( "DETAILS:" );
			out.increaseIndent();
			this.details.forEach( out::println );
			out.decreaseIndent();
		}
				
		out.decreaseIndent();
	}

	@Override
	public int compareTo( Msg other ) {
		return this.severity == other.severity ? this.seqno - other.seqno : other.severity.ordinal() - this.severity.ordinal();
	}

}
