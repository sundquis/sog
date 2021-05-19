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

/**
 * @author sundquis
 *
 */
public class SwitchTest extends Test.Implementation {

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
		member = "public Object Switch.apply(Object, Object)", 
		description = "Throws App exception when handler raises exception" )
	public void apply_ThrowsAppExceptionWhenHandlerRaisesException( Test.Case tc ) {
		tc.expectError( AppException.class );
		this.sw.addCase( State.OPEN, (s) -> s + 1/0 );
		this.sw.apply( State.OPEN, "Foo" );
	}
		

	@Test.Impl( 
		member = "public Object Switch.apply(Object, Object)", 
		description = "Throws App exception when no handler found" )
	public void apply_ThrowsAppExceptionWhenNoHandlerFound( Test.Case tc ) {
		tc.expectError( AppException.class );
		this.sw.addCase( State.OPEN, ID ).addCase( State.CLOSED, ID );
		tc.assertEqual( "Foo",  this.sw.apply( State.OPEN, "Foo" ) );
		tc.assertEqual( "Bar",  this.sw.apply( State.CLOSED, "Bar" ) );
		this.sw.apply( State.MURKY, "Foo" );
	}

	@Test.Impl( 
		member = "public Object Switch.apply(Object, Object)", 
		description = "Throws assertion error for null key" )
	public void apply_ThrowsAssertionErrorForNullKey( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( State.OPEN, ID ).addCase( State.CLOSED, ID );
		this.sw.apply( null, "Foo" );
	}

	
	// addCase
	@Test.Impl( 
		member = "public Switch Switch.addCase(Object, Function)", 
		description = "Returns non null" )
	public void addCase_ReturnsNonNull( Test.Case tc ) {
		tc.notNull( this.sw.addCase( State.OPEN, ID ) );
	}

	@Test.Impl( 
		member = "public Switch Switch.addCase(Object, Function)", 
		description = "Returns this Switch instance" )
	public void addCase_ReturnsThisSwitchInstance( Test.Case tc ) {
		tc.assertEqual( this.sw, this.sw.addCase( State.OPEN, ID ) );
	}

	@Test.Impl( 
		member = "public Switch Switch.addCase(Object, Function)", 
		description = "Throws assertion error for null handler" )
	public void addCase_ThrowsAssertionErrorForNullHandler( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( State.OPEN, null );
	}

	@Test.Impl( 
		member = "public Switch Switch.addCase(Object, Function)", 
		description = "Throws assertion error for null key" )
	public void addCase_ThrowsAssertionErrorForNullKey( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addCase( null, ID );
	}

	
	// addDefault
	@Test.Impl( 
		member = "public Switch Switch.addDefault(Function)", 
		description = "Returns non null" )
	public void addDefault_ReturnsNonNull( Test.Case tc ) {
		tc.notNull( this.sw.addDefault( ID ) );
	}

	@Test.Impl( 
		member = "public Switch Switch.addDefault(Function)", 
		description = "Returns this Switch instance" )
	public void addDefault_ReturnsThisSwitchInstance( Test.Case tc ) {
		tc.assertEqual( this.sw, this.sw.addDefault( ID ) );
	}

	@Test.Impl( 
		member = "public Switch Switch.addDefault(Function)", 
		description = "Throws assertion error for null handler" )
	public void addDefault_ThrowsAssertionErrorForNullHandler( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		this.sw.addDefault( null );
	}
	
	
}
