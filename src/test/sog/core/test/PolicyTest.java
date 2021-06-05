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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import sog.core.Test;
import sog.core.test.Policy;

/**
 * Test implementations.
 */
public class PolicyTest extends Test.Container {


	public PolicyTest() {
		super( sog.core.test.Policy.class );
	}

	
	// Class with public constructor, field, and method
	static class PublicClass {
		public PublicClass() {}
		public int i;
		public void get() {}
		public static final Constructor<?> CONSTRUCTOR = PublicClass.class.getDeclaredConstructors()[0];
		public static final Field FIELD = PublicClass.class.getDeclaredFields()[0];
		public static final Method METHOD = PublicClass.class.getDeclaredMethods()[0];
	}
	
	// Class with protected constructor, field, and method
	static class ProtectedClass {
		protected ProtectedClass() {}
		protected int i;
		protected void get() {}
		public static final Constructor<?> CONSTRUCTOR = ProtectedClass.class.getDeclaredConstructors()[0];
		public static final Field FIELD = ProtectedClass.class.getDeclaredFields()[0];
		public static final Method METHOD = ProtectedClass.class.getDeclaredMethods()[0];
	}
	
	// Class with package constructor, field, and method
	static class PackageClass {
		PackageClass() {}
		int i;
		void get() {}
		public static final Constructor<?> CONSTRUCTOR = PackageClass.class.getDeclaredConstructors()[0];
		public static final Field FIELD = PackageClass.class.getDeclaredFields()[0];
		public static final Method METHOD = PackageClass.class.getDeclaredMethods()[0];
	}
	
	// Class with private constructor, field, and method
	static class PrivateClass {
		private PrivateClass() {}
		@SuppressWarnings( "unused" ) private int i;
		@SuppressWarnings( "unused" ) private void get() {}
		public static final Constructor<?> CONSTRUCTOR = PrivateClass.class.getDeclaredConstructors()[0];
		public static final Field FIELD = PrivateClass.class.getDeclaredFields()[0];
		public static final Method METHOD = PrivateClass.class.getDeclaredMethods()[0];
	}
	
	
	
	// Test Cases
	

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Non-private executables are required", weight = 6 )
	public void DEFAULT_NonPrivateExecutablesAreRequired( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Non-public fields are exempt", weight = 3 )
	public void DEFAULT_NonPublicFieldsAreExempt( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.FIELD  ) );
		tc.assertFalse( Policy.get().required( PackageClass.FIELD  ) );
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Private executables are exempt", weight = 2 )
	public void DEFAULT_PrivateExecutablesAreExempt( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertFalse( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PrivateClass.METHOD ) );
	}

	@Test.Impl( member = "field: Policy Policy.DEFAULT", description = "Public fields are required" )
	public void DEFAULT_PublicFieldsAreRequired( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
	}
        
	@Test.Impl( member = "field: Policy Policy.ALL", description = "Constructors are required", weight = 4 )
	public void ALL_ConstructorsAreRequired( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
	}

	@Test.Impl( member = "field: Policy Policy.ALL", description = "Fields are required", weight = 4 )
	public void ALL_FieldsAreRequired( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.FIELD ) );
	}

	@Test.Impl( member = "field: Policy Policy.ALL", description = "Methods are required", weight = 4 )
	public void ALL_MethodsAreRequired( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.METHOD ) );
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Constructors are not required", weight = 4 )
	public void NONE_ConstructorsAreNotRequired( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Fields are not required", weight = 4 )
	public void NONE_FieldsAreNotRequired( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.FIELD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PackageClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
	}

	@Test.Impl( member = "field: Policy Policy.NONE", description = "Methods are not required", weight = 4 )
	public void NONE_MethodsAreNotRequired( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.METHOD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PackageClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.METHOD ) );
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private constructors are required", weight = 3 )
	public void STRICT_NonPrivateConstructorsAreRequired( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private fields are required", weight = 3 )
	public void STRICT_NonPrivateFieldsAreRequired( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
	}

	@Test.Impl( member = "field: Policy Policy.STRICT", description = "Non-private methods are required", weight = 3 )
	public void STRICT_NonPrivateMethodsAreRequired( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
	}
        
	@Test.Impl( member = "field: Map Policy.INSTANCES", description = "Map include ALL" )
	public void INSTANCES_MapIncludeAll( Test.Case tc ) {
		Map<String, Policy> map = null;
		map = this.getSubjectField( null, "INSTANCES", map );
		tc.assertTrue( map.containsKey( "ALL" ) );
	}

	@Test.Impl( member = "field: Map Policy.INSTANCES", description = "Map inlcudes DEFAULT" )
	public void INSTANCES_MapInlcudesDefault( Test.Case tc ) {
		Map<String, Policy> map = null;
		map = this.getSubjectField( null, "INSTANCES", map );
		tc.assertTrue( map.containsKey( "DEFAULT" ) );
	}

	@Test.Impl( member = "field: Policy Policy.currentPolicy", description = "Current Policy is not null" )
	public void currentPolicy_CurrentPolicyIsNoNull( Test.Case tc ) {
		Policy policy = null;
		policy = this.getSubjectField( null, "currentPolicy", policy );
		tc.notNull( policy );
	}

	@Test.Impl( member = "method: Policy Policy.get()", description = "Return is consistent with previous set(Policy)", weight = 5 )
	public void get_ReturnIsConsistentWithPreviousSetPolicy( Test.Case tc ) {
		for ( Policy p : Policy.values() ) {
			Policy.set( p );
			tc.assertEqual( p, Policy.get() );
		}
	}

	@Test.Impl( member = "method: Policy Policy.get()", description = "Return is not null" )
	public void get_ReturnIsNotNull( Test.Case tc ) {
		tc.notNull( Policy.get() );
	}

	@Test.Impl( member = "method: void Policy.set(Policy)", description = "Throws AssertionError for null policy" )
	public void set_ThrowsAssertionerrorForNullPolicy( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Policy.set( null );
	}

	@Test.Impl( member = "method: boolean Policy.required(Constructor)", description = "Results correct for constructors", weight = 8 )
	public void required_ResultsCorrectForConstructors( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
	}

	@Test.Impl( member = "method: boolean Policy.required(Constructor)", description = "Throws AssertionError for null constructor" )
	public void required_ThrowsAssertionerrorForNullConstructor( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> c = null;
		Policy.get().required( c );
	}

	@Test.Impl( member = "method: boolean Policy.required(Field)", description = "Results correct for fields", weight = 8 )
	public void required_ResultsCorrectForFields( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.FIELD ) );
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.FIELD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PackageClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
	}

	@Test.Impl( member = "method: boolean Policy.required(Field)", description = "Throws AssertionError for null field" )
	public void required_ThrowsAssertionerrorForNullField( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Field f = null;
		Policy.get().required( f );
	}

	@Test.Impl( member = "method: boolean Policy.required(Method)", description = "Results correct for methods", weight = 8 )
	public void required_ResultsCorrectForMethods( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.METHOD ) );
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.METHOD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PackageClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.METHOD ) );
	}

	@Test.Impl( member = "method: boolean Policy.required(Method)", description = "Throws AssertionError for null method" )
	public void required_ThrowsAssertionerrorForNullMethod( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Method m = null;
		Policy.get().required( m );
	}
        
	@Test.Impl( member = "field: Policy Policy.VERY_STRICT", description = "Constructors are required", weight = 4 )
	public void VERY_STRICT_ConstructorsAreRequired( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
	}

	@Test.Impl( member = "field: Policy Policy.VERY_STRICT", description = "Methods are required", weight = 4 )
	public void VERY_STRICT_MethodsAreRequired( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.METHOD ) );
	}

	@Test.Impl( member = "field: Policy Policy.VERY_STRICT", description = "Non-private fields are required", weight = 4 )
	public void VERY_STRICT_NonPrivateFieldsAreRequired( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageConstructor()", description = "True for Policy CONSTRUCTOR" )
	public void requirePackageConstructor_TrueForPolicyConstructor( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePackageConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageConstructor()", description = "True for Policy PACKAGE" )
	public void requirePackageConstructor_TrueForPolicyPackage( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageField()", description = "True for Policy FIELD" )
	public void requirePackageField_TrueForPolicyField( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePackageField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageField()", description = "True for Policy PACKAGE" )
	public void requirePackageField_TrueForPolicyPackage( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageMethod()", description = "True for Policy METHOD" )
	public void requirePackageMethod_TrueForPolicyMethod( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePackageMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePackageMethod()", description = "True for Policy PACKAGE" )
	public void requirePackageMethod_TrueForPolicyPackage( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateConstructor()", description = "True for Policy CONSTRUCTOR" )
	public void requirePrivateConstructor_TrueForPolicyConstructor( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePrivateConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateConstructor()", description = "True for Policy PRIVATE" )
	public void requirePrivateConstructor_TrueForPolicyPrivate( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateField()", description = "True for Policy FIELD" )
	public void requirePrivateField_TrueForPolicyField( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePrivateField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateField()", description = "True for Policy PRIVATE" )
	public void requirePrivateField_TrueForPolicyPrivate( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateMethod()", description = "True for Policy METHOD" )
	public void requirePrivateMethod_TrueForPolicyMethod( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePrivateMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePrivateMethod()", description = "True for Policy PRIVATE" )
	public void requirePrivateMethod_TrueForPolicyPrivate( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedConstructor()", description = "True for Policy CONSTRUCTOR" )
	public void requireProtectedConstructor_TrueForPolicyConstructor( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requireProtectedConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedConstructor()", description = "True for Policy PROTECTED" )
	public void requireProtectedConstructor_TrueForPolicyProtected( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedField()", description = "True for Policy FIELD" )
	public void requireProtectedField_TrueForPolicyField( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requireProtectedField() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedField()", description = "True for Policy PROTECTED" )
	public void requireProtectedField_TrueForPolicyProtected( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedField() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedMethod()", description = "True for Policy METHOD" )
	public void requireProtectedMethod_TrueForPolicyMethod( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requireProtectedMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requireProtectedMethod()", description = "True for Policy PROTECTED" )
	public void requireProtectedMethod_TrueForPolicyProtected( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicConstructor()", description = "True for Policy CONSTRUCTOR" )
	public void requirePublicConstructor_TrueForPolicyConstructor( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePublicConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicConstructor()", description = "True for Policy PUBLIC" )
	public void requirePublicConstructor_TrueForPolicyPublic( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicConstructor() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicField()", description = "True for Policy FIELD" )
	public void requirePublicField_TrueForPolicyField( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePublicField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicField()", description = "True for Policy PUBLIC" )
	public void requirePublicField_TrueForPolicyPublic( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicField() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicMethod()", description = "True for Policy METHOD" )
	public void requirePublicMethod_TrueForPolicyMethod( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePublicMethod() );
	}

	@Test.Impl( member = "method: boolean Policy.requirePublicMethod()", description = "True for Policy PUBLIC" )
	public void requirePublicMethod_TrueForPolicyPublic( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicMethod() );
	}
	
	
	public static void main( String[] args ) {
		Test.eval( Policy.class );
	}
        
}
