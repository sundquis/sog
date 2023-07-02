/**
 * Copyright (C) 2021, 2023
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

import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.Policy;

/**
 * Test implementations.
 */
@Test.Skip( "Container" )
public class PolicyTest extends Test.Container {


	private final Policy ORIGINAL;
	
	public PolicyTest() {
		super( Policy.class );
		this.ORIGINAL = Policy.get();
	}
	
	
	@Override
	public Procedure afterAll() {
		return () -> {
			Policy.set( this.ORIGINAL );
		};
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
	
	
    @Test.Impl( 
    	member = "field: Map Policy.INSTANCES", 
    	description = "Map include ALL"
    )
    public void tm_06D1C64FD( Test.Case tc ) {
		Map<String, Policy> map = null;
		map = this.getSubjectField( null, "INSTANCES", map );
		tc.assertTrue( map.containsKey( "ALL" ) );
    }

    @Test.Impl( 
    	member = "field: Map Policy.INSTANCES", 
    	description = "Map inlcudes DEFAULT" 
    )
    public void tm_012F32CBC( Test.Case tc ) {
		Map<String, Policy> map = null;
		map = this.getSubjectField( null, "INSTANCES", map );
		tc.assertTrue( map.containsKey( "DEFAULT" ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.ALL", 
    	description = "Constructors are required",
    	weight = 4
    )
    public void tm_01D124A8D( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.ALL", 
    	description = "Fields are required",
    	weight = 4
    )
    public void tm_0EB1ECB8D( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.FIELD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.ALL", 
    	description = "Methods are required",
    	weight = 4
    )
    public void tm_0196C7634( Test.Case tc ) {
		Policy.set( Policy.ALL );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.DEFAULT", 
    	description = "Non-private executables are required",
    	weight = 6
    )
    public void tm_034231D3A( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.DEFAULT", 
    	description = "Non-public fields are exempt",
    	weight = 3
    )
    public void tm_0FFA1C9F6( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.FIELD  ) );
		tc.assertFalse( Policy.get().required( PackageClass.FIELD  ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.DEFAULT", 
    	description = "Private executables are exempt",
    	weight = 2
    )
    public void tm_09160167A( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertFalse( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PrivateClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.DEFAULT", 
    	description = "Public fields are required" 
    )
    public void tm_0570F03B6( Test.Case tc ) {
		Policy.set( Policy.DEFAULT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.NONE", 
    	description = "Constructors are not required",
    	weight = 4
    )
    public void tm_07058AF37( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertFalse( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.NONE", 
    	description = "Fields are not required",
    	weight = 4
    )
    public void tm_0D44AC1F7( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.FIELD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PackageClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.NONE", 
    	description = "Methods are not required",
    	weight = 4
    )
    public void tm_03AB837C4( Test.Case tc ) {
		Policy.set( Policy.NONE );
		tc.assertFalse( Policy.get().required( PublicClass.METHOD ) );
		tc.assertFalse( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PackageClass.METHOD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.STRICT", 
    	description = "Non-private constructors are required",
    	weight = 3
    )
    public void tm_08B95DDBE( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.STRICT", 
    	description = "Non-private fields are required",
    	weight = 3
    )
    public void tm_09BD8D77E( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.STRICT", 
    	description = "Non-private methods are required",
    	weight = 3
    )
    public void tm_07FF3E863( Test.Case tc ) {
		Policy.set( Policy.STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.VERY_STRICT", 
    	description = "Constructors are required",
    	weight = 4
    )
    public void tm_0D8F4763E( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PackageClass.CONSTRUCTOR ) );
		tc.assertTrue( Policy.get().required( PrivateClass.CONSTRUCTOR ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.VERY_STRICT", 
    	description = "Methods are required",
    	weight = 4
    )
    public void tm_00EF7FFE3( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.METHOD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PackageClass.METHOD ) );
		tc.assertTrue( Policy.get().required( PrivateClass.METHOD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.VERY_STRICT", 
    	description = "Non-private fields are required",
    	weight = 4
    )
    public void tm_0B6C714A1( Test.Case tc ) {
		Policy.set( Policy.VERY_STRICT );
		tc.assertTrue( Policy.get().required( PublicClass.FIELD ) );
		tc.assertTrue( Policy.get().required( ProtectedClass.FIELD ) );
		tc.assertTrue( Policy.get().required( PackageClass.FIELD ) );
		tc.assertFalse( Policy.get().required( PrivateClass.FIELD ) );
    }
        
    @Test.Impl( 
    	member = "field: Policy Policy.currentPolicy", 
    	description = "Current Policy is not null" 
    )
    public void tm_018641FB4( Test.Case tc ) {
		Policy policy = null;
		policy = this.getSubjectField( null, "currentPolicy", policy );
		tc.assertNonNull( policy );
    }
        
    @Test.Impl( 
    	member = "method: Policy Policy.get()", 
    	description = "Return is consistent with previous set(Policy)",
    	weight = 5
    )
    public void tm_0B57EECFC( Test.Case tc ) {
		for ( Policy p : Policy.values() ) {
			Policy.set( p );
			tc.assertEqual( p, Policy.get() );
		}
    }
        
    @Test.Impl( 
    	member = "method: Policy Policy.get()", 
    	description = "Return is not null" 
    )
    public void tm_0628D8B7C( Test.Case tc ) {
		tc.assertNonNull( Policy.get() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageConstructor()", 
    	description = "True for Policy CONSTRUCTOR" 
    )
    public void tm_0E77F521A( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePackageConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageConstructor()", 
    	description = "True for Policy PACKAGE" 
    )
    public void tm_064AAADE6( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageField()", 
    	description = "True for Policy FIELD" 
    )
    public void tm_007A9499A( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePackageField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageField()", 
    	description = "True for Policy PACKAGE" 
    )
    public void tm_0E28520A6( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageMethod()", 
    	description = "True for Policy METHOD" 
    )
    public void tm_068086B4A( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePackageMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePackageMethod()", 
    	description = "True for Policy PACKAGE" 
    )
    public void tm_0375EC2B3( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( Policy.get().requirePackageMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateConstructor()", 
    	description = "True for Policy CONSTRUCTOR" 
    )
    public void tm_0FFD3BFFD( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePrivateConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateConstructor()", 
    	description = "True for Policy PRIVATE" 
    )
    public void tm_009F41B46( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateField()", 
    	description = "True for Policy FIELD" 
    )
    public void tm_0B9E133FD( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePrivateField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateField()", 
    	description = "True for Policy PRIVATE" 
    )
    public void tm_01C909446( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateMethod()", 
    	description = "True for Policy METHOD" 
    )
    public void tm_06BEF48ED( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePrivateMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePrivateMethod()", 
    	description = "True for Policy PRIVATE" 
    )
    public void tm_0E6782F6D( Test.Case tc ) {
		Policy.set( Policy.PRIVATE );
		tc.assertTrue( Policy.get().requirePrivateMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedConstructor()", 
    	description = "True for Policy CONSTRUCTOR" 
    )
    public void tm_0FF080EF2( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requireProtectedConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedConstructor()", 
    	description = "True for Policy PROTECTED" 
    )
    public void tm_08521A4E6( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedField()", 
    	description = "True for Policy FIELD" 
    )
    public void tm_00F848A72( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requireProtectedField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedField()", 
    	description = "True for Policy PROTECTED" 
    )
    public void tm_02A8769A6( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedMethod()", 
    	description = "True for Policy METHOD" 
    )
    public void tm_0E616D622( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requireProtectedMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requireProtectedMethod()", 
    	description = "True for Policy PROTECTED" 
    )
    public void tm_0CB252DC3( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( Policy.get().requireProtectedMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicConstructor()", 
    	description = "True for Policy CONSTRUCTOR" 
    )
    public void tm_089CB5C71( Test.Case tc ) {
		Policy.set( Policy.CONSTRUCTOR );
		tc.assertTrue( Policy.get().requirePublicConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicConstructor()", 
    	description = "True for Policy PUBLIC" 
    )
    public void tm_0007D0888( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicConstructor() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicField()", 
    	description = "True for Policy FIELD" 
    )
    public void tm_020A8DE71( Test.Case tc ) {
		Policy.set( Policy.FIELD );
		tc.assertTrue( Policy.get().requirePublicField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicField()", 
    	description = "True for Policy PUBLIC" 
    )
    public void tm_078EDA888( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicField() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicMethod()", 
    	description = "True for Policy METHOD" 
    )
    public void tm_03F762661( Test.Case tc ) {
		Policy.set( Policy.METHOD );
		tc.assertTrue( Policy.get().requirePublicMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.requirePublicMethod()", 
    	description = "True for Policy PUBLIC" 
    )
    public void tm_0A6FBC5E9( Test.Case tc ) {
		Policy.set( Policy.PUBLIC );
		tc.assertTrue( Policy.get().requirePublicMethod() );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Constructor)", 
    	description = "Results correct for constructors",
    	weight = 8
    )
    public void tm_01E342FA3( Test.Case tc ) {
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
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Constructor)", 
    	description = "Throws AssertionError for null constructor" 
    )
    public void tm_0BDBC56F2( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> c = null;
		Policy.get().required( c );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Field)", 
    	description = "Results correct for fields",
    	weight = 8
    )
    public void tm_089AF65A3( Test.Case tc ) {
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
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Field)", 
    	description = "Throws AssertionError for null field" 
    )
    public void tm_0BDC8E172( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Field f = null;
		Policy.get().required( f );
    }
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Method)", 
    	description = "Results correct for methods",
    	weight = 8
    )
    public void tm_0D8359329( Test.Case tc ) {
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
        
    @Test.Impl( 
    	member = "method: boolean Policy.required(Method)", 
    	description = "Throws AssertionError for null method" 
    )
    public void tm_014223ACA( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Method m = null;
		Policy.get().required( m );
    }
        
    @Test.Impl( 
    	member = "method: void Policy.set(Policy)", 
    	description = "Throws AssertionError for null policy" 
    )
    public void tm_0F472BD39( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Policy.set( null );
    }

	
	
	public static void main( String[] args ) {
		Test.eval( Policy.class ).showDetails( true ).print();
	}

}
