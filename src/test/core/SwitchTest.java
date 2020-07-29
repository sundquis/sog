/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;


import java.util.function.Function;

import sog.core.AppException;
import sog.core.Procedure;
import sog.core.Switch;
import sog.core.Test;
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class SwitchTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Switch.class;
	}

	// Test implementations
	
	public static enum State {
		OPEN,
		CLOSED,
		MURKY
	}
	
	private static Function<String, String> ID = (s) -> s;
	
	private Switch<State, String, String> sw;
	
	public Procedure beforeEach() {
		return () -> this.sw = new Switch<State, String, String>();
	}
	
	public Procedure afterEach() {
		return () -> this.sw = null;
	}
	
	
	// apply
	@Test.Impl( 
		src = "public Object Switch.apply(Object, Object)", 
		desc = "Throws App exception when handler raises exception" )
	public void apply_ThrowsAppExceptionWhenHandlerRaisesException( TestCase tc ) {
		tc.expectError( AppException.class );
		this.sw.addCase( State.OPEN, (s) -> s + 1/0 );
		this.sw.apply( State.OPEN, "Foo" );
	}
		

	@Test.Impl( 
		src = "public Object Switch.apply(Object, Object)", 
		desc = "Throws App exception when no handler found" )
	public void apply_ThrowsAppExceptionWhenNoHandlerFound( TestCase tc ) {
		tc.expectError( AppException.class );
		this.sw.addCase( State.OPEN, ID ).addCase( State.CLOSED, ID );
		tc.assertEqual( "Foo",  this.sw.apply( State.OPEN, "Foo" ) );
		tc.assertEqual( "Bar",  this.sw.apply( State.CLOSED, "Bar" ) );
		this.sw.apply( State.MURKY, "Foo" );
	}

	@Test.Impl( 
		src = "public Object Switch.apply(Object, Object)", 
		desc = "Throws assertion error for null key" )
	public void apply_ThrowsAssertionErrorForNullKey( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( State.OPEN, ID ).addCase( State.CLOSED, ID );
		this.sw.apply( null, "Foo" );
	}

	
	// addCase
	@Test.Impl( 
		src = "public Switch Switch.addCase(Object, Function)", 
		desc = "Returns non null" )
	public void addCase_ReturnsNonNull( TestCase tc ) {
		tc.notNull( this.sw.addCase( State.OPEN, ID ) );
	}

	@Test.Impl( 
		src = "public Switch Switch.addCase(Object, Function)", 
		desc = "Returns this Switch instance" )
	public void addCase_ReturnsThisSwitchInstance( TestCase tc ) {
		tc.assertEqual( this.sw, this.sw.addCase( State.OPEN, ID ) );
	}

	@Test.Impl( 
		src = "public Switch Switch.addCase(Object, Function)", 
		desc = "Throws assertion error for null handler" )
	public void addCase_ThrowsAssertionErrorForNullHandler( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( State.OPEN, null );
	}

	@Test.Impl( 
		src = "public Switch Switch.addCase(Object, Function)", 
		desc = "Throws assertion error for null key" )
	public void addCase_ThrowsAssertionErrorForNullKey( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( null, ID );
	}

	
	// addDefault
	@Test.Impl( 
		src = "public Switch Switch.addDefault(Function)", 
		desc = "Returns non null" )
	public void addDefault_ReturnsNonNull( TestCase tc ) {
		tc.notNull( this.sw.addDefault( ID ) );
	}

	@Test.Impl( 
		src = "public Switch Switch.addDefault(Function)", 
		desc = "Returns this Switch instance" )
	public void addDefault_ReturnsThisSwitchInstance( TestCase tc ) {
		tc.assertEqual( this.sw, this.sw.addDefault( ID ) );
	}

	@Test.Impl( 
		src = "public Switch Switch.addDefault(Function)", 
		desc = "Throws assertion error for null handler" )
	public void addDefault_ThrowsAssertionErrorForNullHandler( TestCase tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addDefault( null );
	}
	
	

	public static void main(String[] args) {

		System.out.println();

		new Test(SwitchTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
