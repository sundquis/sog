/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import sog.core.Assert;
import sog.core.Test;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestResult extends Result {
	
	/**
	 * Construct a test for a single {@code Class}.
	 */
	public TestResult( Class<?> subject ) {
		super( "TESTS: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
		
		this.addChild( new ClassResult( Assert.nonNull( subject ) ) );
	}

	@Test.Case( "1" )
	@Test.Case( "2" )
	@Test.Case( "3" )
	public static void fooStatic() {}
	public static int fooIntStatic() { return 42; }
	public static int fooIntStatic(int x) { return x; }
	
	public void bar() {}
	public String label;
	public class Inner {
		public void bar() {}
		protected int ft;
	}
	
	public static void main( String[] args ) {
		System.out.println( ">>> Hello world!" );
		
		TestResult res = new TestResult( TestResult.class );
		IndentWriter out = new IndentWriter( System.out );
		res.print( out );

		System.out.println( ">>> Done!" );
	}

}
