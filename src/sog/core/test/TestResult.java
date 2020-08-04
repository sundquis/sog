/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import sog.core.Assert;
import sog.core.Test;
import sog.util.Fault;
import sog.util.IndentWriter;

/**
 * 
 */
public class TestResult extends Result implements Consumer<Fault> {
	
	private final IndentWriter err;
	
	/**
	 * Empty set of classes.
	 */
	public TestResult() {
		super( "TESTS: " + new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" ).format( new Date() ) );
		
		this.err = new IndentWriter( System.err );
		Fault.addListener( this );
	}
	
	
	@Override
	public void init() {}
	
	
	public void addSubject( Class<?> subjectClass ) {
		Assert.nonNull( subjectClass );
		
		if ( Container.class.isAssignableFrom( subjectClass ) ) {
			new Fault( "Ignoring container class", subjectClass ).toss();
			return;
		}
		this.addChild( new ClassResult( subjectClass ) );
	}

		
	@Override
	public void accept( Fault fault ) {
		fault.print( err );
	}



	
	
	// FIXME: Convert to proper testing
	@Test.Case( "1" )
	@Test.Case( "2" )
	@Test.Case( "3" )
	public static void fooStatic() {}
	public static int fooIntStatic() { return 42; }
	
	@Test.Case( "1" )
	public static int fooIntStatic(int x) { return x; }
	
	public void bar() {}
	public String label;
	
	@Test.Skip
	public class Inner {
		public void bar() {}
		protected int ft;
	}
	
	public static class MyContainer extends Container {

		public Class<?> subjectClass() {
			return TestResult.class;
		}
		
		@Test.Impl( description = "The description", member = "Made_up_member_name" )
		public void foo() {}
		
	}
	
	public static void main( String[] args ) {
		System.out.println( ">>> Hello world!" );
		
		TestResult res = new TestResult( );
		res.addSubject( TestResult.class );
		res.addSubject( Container.class );
		res.print();

		System.out.println( ">>> Done!" );
	}





}
