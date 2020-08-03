/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.util.Fault;

/**
 * @author sundquis
 *
 */
public class FaultTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return Fault.class;
	}

	// Test implementations
	
	
	@TestOrig.Impl( src = "public Fault Fault.addSource(String)", desc = "Returns this" )
	public void addSource_ReturnsThis( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public Fault Fault.addSource(String)", desc = "Source is appended to previous sources" )
	public void addSource_SourceIsAppendedToPreviousSources( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public Fault(String, Object[])", desc = "Fault location is recorded" )
	public void Fault_FaultLocationIsRecorded( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public Fault(String, Object[])", desc = "Throws assertion error for enpty string" )
	public void Fault_ThrowsAssertionErrorForEnptyString( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public String Fault.toString()", desc = "Returns non-empty description" )
	public void toString_ReturnsNonEmptyDescription( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.addListener(Consumer)", desc = "Subsequent faults are deleivered to listener" )
	public void addListener_SubsequentFaultsAreDeleiveredToListener( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.print(IndentWriter)", desc = "All provided sources printed" )
	public void print_AllProvidedSourcesPrinted( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.print(IndentWriter)", desc = "Description printed" )
	public void print_DescriptionPrinted( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.print(IndentWriter)", desc = "Fault location printed" )
	public void print_FaultLocationPrinted( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.print(IndentWriter)", desc = "Model location printed when possible" )
	public void print_ModelLocationPrintedWhenPossible( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.removeListener(Consumer)", desc = "Subsequent faults are deleivered to listener" )
	public void removeListener_SubsequentFaultsAreDeleiveredToListener( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@TestOrig.Impl( src = "public void Fault.toss()", desc = "Fault is deleiverd to listeners" )
	public void toss_FaultIsDeleiverdToListeners( TestCase tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}



	public static void main(String[] args) {

		System.out.println();

		//Test.verbose();
		new TestOrig(FaultTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
