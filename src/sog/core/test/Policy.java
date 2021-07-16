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
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Property;
import sog.core.Test;
import sog.util.Protection;

/**
 * This class has the responsibility of defining and enforcing the policy regarding which
 * class members should be flagged as needing test validation. A Policy is defined by the twelve
 * boolean methods, four each (corresponding to the four protection levels) for constructors,
 * fields, and methods. The test framework generates an error whenever it encounters an untested member 
 * that is required by the current policy. Errors can be eliminated by relaxing the policy,
 * declaring one or more tests, or marking the member with the Test.Skip annotation.
 * 
 * Policies are defined using bit strings. The leftmost four bits define the policy for constructors,
 * the next four bits define the policy for fields, and the rightmost four define the policy for
 * methods. Each set of four bits defines (1 = required) the policy for
 * 		public members
 * 		protected members
 * 		package members
 * 		private members
 */
@Test.Subject( "test." )
public enum Policy implements Protection {
	
	/** The default policy requires all executables except private, and public fields. */
	@Test.Decl( "Non-private executables are required" )
	@Test.Decl( "Private executables are exempt" )
	@Test.Decl( "Public fields are required" )
	@Test.Decl( "Non-public fields are exempt" )
	DEFAULT( 0B0_1110_1000_1110 ),

	/** Require all non-private members. */
	@Test.Decl( "Non-private constructors are required" )
	@Test.Decl( "Non-private fields are required" )
	@Test.Decl( "Non-private methods are required" )
	STRICT( 0B0_1110_1110_1110 ),
	
	/** Require everything except private fields. */
	@Test.Decl( "Constructors are required" )
	@Test.Decl( "Non-private fields are required" )
	@Test.Decl( "Methods are required" )
	VERY_STRICT( 0B0_1111_1110_1111 ),
	
	/** No testing required. */
	@Test.Decl( "Constructors are not required" )
	@Test.Decl( "Fields are not required" )
	@Test.Decl( "Methods are not required" )
	NONE( 0 ),
	
	@Test.Skip( "Manually checked" ) PUBLIC( 		0B0_1000_1000_1000 ),
	@Test.Skip( "Manually checked" ) PROTECTED( 	0B0_0100_0100_0100 ),
	@Test.Skip( "Manually checked" ) PACKAGE( 		0B0_0010_0010_0010 ),
	@Test.Skip( "Manually checked" ) PRIVATE( 		0B0_0001_0001_0001 ),
	@Test.Skip( "Manually checked" ) CONSTRUCTOR( 	0B0_1111_0000_0000 ),
	@Test.Skip( "Manually checked" ) FIELD( 		0B0_0000_1111_0000 ),
	@Test.Skip( "Manually checked" ) METHOD( 		0B0_0000_0000_1111 ),
	
	/** Require all 12 types of members. */
	@Test.Decl( "Constructors are required" )
	@Test.Decl( "Fields are required" )
	@Test.Decl( "Methods are required" )
	ALL( 0B0_1111_1111_1111 );
		

	private final int code;
	
	@Test.Skip( "No testing required" )
	private Policy( int code ) {
		this.code = code;
	}
	
	@Test.Decl( "True for Policy PUBLIC" )
	@Test.Decl( "True for Policy CONSTRUCTOR" )
	public boolean requirePublicConstructor() {
		int bit = 0B0_1000_0000_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PROTECTED" )
	@Test.Decl( "True for Policy CONSTRUCTOR" )
	public boolean requireProtectedConstructor() {
		int bit = 0B0_0100_0000_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PACKAGE" )
	@Test.Decl( "True for Policy CONSTRUCTOR" )
	public boolean requirePackageConstructor() {
		int bit = 0B0_0010_0000_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PRIVATE" )
	@Test.Decl( "True for Policy CONSTRUCTOR" )
	public boolean requirePrivateConstructor() {
		int bit = 0B0_0001_0000_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PUBLIC" )
	@Test.Decl( "True for Policy FIELD" )
	public boolean requirePublicField() {
		int bit = 0B0_0000_1000_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PROTECTED" )
	@Test.Decl( "True for Policy FIELD" )
	public boolean requireProtectedField() {
		int bit = 0B0_0000_0100_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PACKAGE" )
	@Test.Decl( "True for Policy FIELD" )
	public boolean requirePackageField() {
		int bit = 0B0_0000_0010_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PRIVATE" )
	@Test.Decl( "True for Policy FIELD" )
	public boolean requirePrivateField() {
		int bit = 0B0_0000_0001_0000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PUBLIC" )
	@Test.Decl( "True for Policy METHOD" )
	public boolean requirePublicMethod() {
		int bit = 0B0_0000_0000_1000;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PROTECTED" )
	@Test.Decl( "True for Policy METHOD" )
	public boolean requireProtectedMethod() {
		int bit = 0B0_0000_0000_0100;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PACKAGE" )
	@Test.Decl( "True for Policy METHOD" )
	public boolean requirePackageMethod() {
		int bit = 0B0_0000_0000_0010;
		return (this.code & bit) == bit;
	}

	@Test.Decl( "True for Policy PRIVATE" )
	@Test.Decl( "True for Policy METHOD" )
	public boolean requirePrivateMethod() {
		int bit = 0B0_0000_0000_0001;
		return (this.code & bit) == bit;
	}
	
	
	/** Allows Policy instances to be set by name as a Property. */
	@Test.Decl( "Map inlcudes DEFAULT" )
	@Test.Decl( "Map include ALL" )
	private final static Map<String, Policy> INSTANCES = Arrays.stream( Policy.values() )
		.collect( Collectors.toMap( Policy::name, Function.identity() ) );
		
	/** The current Policy being enforced. */
	@Test.Decl( "Current Policy is not null" )
	private static Policy currentPolicy = Property.get( "current.policy", Policy.DEFAULT, Policy.INSTANCES::get );

	/** Public access to the current Policy. */
	@Test.Decl( "Return is not null" )
	@Test.Decl( "Return is consistent with previous set(Policy)" )
	public static Policy get() {
		return Policy.currentPolicy;
	}
	
	/**
	 * Set the global testing Policy.
	 * 
	 * @param policy
	 */
	@Test.Decl( "Throws AssertionError for null policy" )
	public static void set( Policy policy ) {
		Policy.currentPolicy = Assert.nonNull( policy );
	}
	

	/**
	 * Determine if testing of the given Constructor is required by the current Policy.
	 * 
	 * @param constructor
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null constructor" )
	@Test.Decl( "Results correct for constructors" )
	final public boolean required( Constructor<?> constructor ) {
		Protection.Level level = this.getProtectionLevel( Assert.nonNull( constructor ) );

		switch ( level ) {
			case PUBLIC:
				return this.requirePublicConstructor();
			case PACKAGE:
				return this.requirePackageConstructor();
			case PROTECTED:
				return this.requireProtectedConstructor();
			case PRIVATE:
				return this.requirePrivateConstructor();
			default:
				return false;
		}
	}

	/**
	 * Determine if testing of the given Field is required by the current Policy.
	 * 
	 * @param field
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null field" )
	@Test.Decl( "Results correct for fields" )
	final public boolean required( Field field ) {
		Protection.Level level = this.getProtectionLevel( Assert.nonNull( field ) );
		
		switch ( level ) {
			case PUBLIC:
				return this.requirePublicField();
			case PACKAGE:
				return this.requirePackageField();
			case PROTECTED:
				return this.requireProtectedField();
			case PRIVATE:
				return this.requirePrivateField();
			default:
				return false;
		}
	}

	/**
	 * Determine if testing of the given Method is required by the current Policy.
	 * 
	 * @param method
	 * @return
	 */
	@Test.Decl( "Throws AssertionError for null method" )
	@Test.Decl( "Results correct for methods" )
	final public boolean required( Method method ) {
		Protection.Level level = this.getProtectionLevel( Assert.nonNull( method ) );
		
		switch ( level ) {
			case PUBLIC:
				return this.requirePublicMethod();
			case PACKAGE:
				return this.requirePackageMethod();
			case PROTECTED:
				return this.requireProtectedMethod();
			case PRIVATE:
				return this.requirePrivateMethod();
			default:
				return false;
		}
	}
	
	
	

}
