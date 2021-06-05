/**
 * Copyright (C) 2021
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */

package test.sog.core.test;

import sog.core.Test;

/**
 * Test implementations.
 */
public class Policy extends Test.Container {

	public Policy() {
		super( sog.core.test.Policy.class );
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Non-private executables are required" )
	public void DEFAULT_NonPrivateExecutablesAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Non-public fields are exempt" )
	public void DEFAULT_NonPublicFieldsAreExempt( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Private executables are exempt" )
	public void DEFAULT_PrivateExecutablesAreExempt( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Public fields are required" )
	public void DEFAULT_PublicFieldsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}
        
	@Test.Impl( member = "field: Policy Policy.ALL", description = "Constructors are required" )
	public void ALL_ConstructorsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.ALL", description = "Fields are required" )
	public void ALL_FieldsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.ALL", description = "Methods are required" )
	public void ALL_MethodsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Constructors are not required" )
	public void NONE_ConstructorsAreNotRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Fields are not required" )
	public void NONE_FieldsAreNotRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Methods are not required" )
	public void NONE_MethodsAreNotRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private constructors are required" )
	public void STRICT_NonPrivateConstructorsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private fields are required" )
	public void STRICT_NonPrivateFieldsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private methods are required" )
	public void STRICT_NonPrivateMethodsAreRequired( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}
        
	@Test.Impl( member = "field: Map Policy.INSTANCES", description = "Map include ALL" )
	public void INSTANCES_MapIncludeAll( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Map Policy.INSTANCES", description = "Map inlcudes DEFAULT" )
	public void INSTANCES_MapInlcudesDefault( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "field: Policy Policy.currentPolicy", description = "Current Policy is no null" )
	public void currentPolicy_CurrentPolicyIsNoNull( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Policy Policy.get()", description = "Return is consistent with previous set(Policy)" )
	public void get_ReturnIsConsistentWithPreviousSetPolicy( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: Policy Policy.get()", description = "Return is not null" )
	public void get_ReturnIsNotNull( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void Policy.set(Policy)", description = "Changes current policy" )
	public void set_ChangesCurrentPolicy( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: void Policy.set(Policy)", description = "Throws AssertionError for null policy" )
	public void set_ThrowsAssertionerrorForNullPolicy( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Constructor)", description = "Results correct for constructors" )
	public void required_ResultsCorrectForConstructors( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Constructor)", description = "Throws AssertionError for null constructor" )
	public void required_ThrowsAssertionerrorForNullConstructor( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Field)", description = "Results correct for fields" )
	public void required_ResultsCorrectForFields( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Field)", description = "Throws AssertionError for null field" )
	public void required_ThrowsAssertionerrorForNullField( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Method)", description = "Results correct for methods" )
	public void required_ResultsCorrectForMethods( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}

	@Test.Impl( member = "method: boolean Policy.required(Method)", description = "Throws AssertionError for null method" )
	public void required_ThrowsAssertionerrorForNullMethod( Test.Case tc ) {
		tc.addMessage( "GENERATED STUB" ).fail();
	}
        
}
