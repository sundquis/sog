/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.stream.Stream;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestResult extends Test.Result {
	
	public static TestResult forSubject( Class<?> subjectClass ) {
		String label = Assert.nonNull( subjectClass ).getName();
		TestResult result = new TestResult( label );
		
		if ( !result.loadSubject( subjectClass ) ) { return result; }
		
		
		return result;
	}



	private Class<?> subjectClass;
	
	public TestResult( String label ) {
		super( label );
	}
	
	
	public boolean loadSubject( Class<?> subjectClass ) {
		this.subjectClass = Assert.nonNull( subjectClass );
		
		return true;
	}
	

	@Override
	public void print( IndentWriter out ) {
		out.println( this );

		out.increaseIndent();
		out.println( "DETAILS" );
		out.decreaseIndent();
	}
	
	
	
	public static void main( String[] args ) {
		Test.eval();
		
		System.out.println( "Done!" );
	}
	

}
