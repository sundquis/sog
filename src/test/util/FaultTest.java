/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.util;

import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class FaultTest extends Test.Container {

	// Test implementations
	
	
	@Test.Impl( member = "public Fault Fault.addSource(String)", description = "Returns this" )
	public void addSource_ReturnsThis( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public Fault Fault.addSource(String)", description = "Source is appended to previous sources" )
	public void addSource_SourceIsAppendedToPreviousSources( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public Fault(String, Object[])", description = "Fault location is recorded" )
	public void Fault_FaultLocationIsRecorded( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public Fault(String, Object[])", description = "Throws assertion error for enpty string" )
	public void Fault_ThrowsAssertionErrorForEnptyString( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public String Fault.toString()", description = "Returns non-empty description" )
	public void toString_ReturnsNonEmptyDescription( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.addListener(Consumer)", description = "Subsequent faults are deleivered to listener" )
	public void addListener_SubsequentFaultsAreDeleiveredToListener( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.print(IndentWriter)", description = "All provided sources printed" )
	public void print_AllProvidedSourcesPrinted( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.print(IndentWriter)", description = "Description printed" )
	public void print_DescriptionPrinted( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.print(IndentWriter)", description = "Fault location printed" )
	public void print_FaultLocationPrinted( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.print(IndentWriter)", description = "Model location printed when possible" )
	public void print_ModelLocationPrintedWhenPossible( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.removeListener(Consumer)", description = "Subsequent faults are deleivered to listener" )
	public void removeListener_SubsequentFaultsAreDeleiveredToListener( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "public void Fault.toss()", description = "Fault is deleiverd to listeners" )
	public void toss_FaultIsDeleiverdToListeners( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}


}
