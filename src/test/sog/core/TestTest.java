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
package test.sog.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import sog.core.AppException;
import sog.core.Procedure;
import sog.core.Test;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestTest extends Test.Container {

	
	public final Test.Container container;
	
	public TestTest() {
		super( Test.class );
		
		class MyContainer extends Test.Container { MyContainer() { super( TestTest.class ); } }
		this.container = new MyContainer();
	}
	
	
	// TEST CASES
	
    @Test.Impl( 
    	member = "constructor: Test.Container(Class)", 
    	description = "Throws AssertionError for null subject" 
    )
    public void tm_0B658BD4B( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	class MyContainer extends Test.Container {
    		MyContainer() { super( null ); }
    	}
    	new MyContainer();
    }
        
    @Test.Impl( 
    	member = "method: Class Test.Container.getSubjectClass()", 
    	description = "Return is not null" 
    )
    public void tm_04079313F( Test.Case tc ) {
    	tc.assertNonNull( this.getSubjectClass() );
    }
        
    @Test.Impl( 
    	member = "method: Class Test.Container.getSubjectClass()", 
    	description = "Value is consistent with constructed value" 
    )
    public void tm_0296FEE99( Test.Case tc ) {
    	tc.assertEqual( Test.class, this.getSubjectClass() );
    }

    public int instanceMethodMixed( String s, int i ) { return s.hashCode() & i; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates instance methods with mixed arguments" 
    )
    public void tm_0A12B3F13( Test.Case tc ) {
    	tc.assertEqual(
    		this.instanceMethodMixed( "Hello World!", 42 ), 
    		this.container.evalSubjectMethod( this, "instanceMethodMixed", 0, "Hello World!", 42 ) 
    	);
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws AppException for no matching method" 
    )
    public void tm_059D1554A( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.evalSubjectMethod( this, "FOO", 0 );
    }

    public int instanceMethodObjects( String s, Object[] objs, List<Integer> ints ) { 
    	return s.hashCode() + objs.length + ints.stream().mapToInt( i -> i ).sum();
    }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates instance methods with object arguments" 
    )
    public void tm_0468D2E69( Test.Case tc ) {
    	tc.assertEqual( 
    		this.instanceMethodObjects( "hi", new Object[] {1, "A", null}, List.of( 0, 1, 2 ) ), 
    		this.container.evalSubjectMethod( this, "instanceMethodObjects", 0, "hi", new Object[] {1, "A", null}, List.of( 0, 1, 2 ) )
    	);
    }

    public int overloadedInstanceMethods( String s, Integer i ) { return 1; }
    public int overloadedInstanceMethods( String s, Class<?> c ) { return 2; }
    public int overloadedInstanceMethods( Class<?> c, Integer i ) { return 3; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates overloaded instance methods",
    	weight = 3
    )
    public void tm_036BAB4B1( Test.Case tc ) {
    	tc.assertEqual( 
    		this.overloadedInstanceMethods( "hi", 42 ), 
    		this.container.evalSubjectMethod( this, "overloadedInstanceMethods", 0, "hi", 42 )
    	);
    	
    	tc.assertEqual( 
    		this.overloadedInstanceMethods( "hi", Object.class ), 
    		this.container.evalSubjectMethod( this, "overloadedInstanceMethods", 0, "hi", Object.class )
        );
    	
    	tc.assertEqual( 
    		this.overloadedInstanceMethods( String.class, 42 ), 
    		this.container.evalSubjectMethod( this, "overloadedInstanceMethods", 0, String.class, 42 )
        );
    }
        
    public static int overloadedStaticMethods( String s, Integer i ) { return 11; }
    public static int overloadedStaticMethods( String s, Class<?> c ) { return 12; }
    public static int overloadedStaticMethods( Class<?> c, Integer i ) { return 13; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates overloaded static methods",
    	weight = 3
    )
    public void tm_029AB9B0A( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.overloadedStaticMethods( "hi", 42 ), 
    		this.container.evalSubjectMethod( null, "overloadedStaticMethods", 0, "hi", 42 )
    	);
    	
    	tc.assertEqual( 
    		TestTest.overloadedStaticMethods( "hi", Object.class ), 
    		this.container.evalSubjectMethod( null, "overloadedStaticMethods", 0, "hi", Object.class )
        );
    	
    	tc.assertEqual( 
    		TestTest.overloadedStaticMethods( String.class, 42 ), 
    		this.container.evalSubjectMethod( null, "overloadedStaticMethods", 0, String.class, 42 )
        );
    }

    public String instanceMethodPrimitive( char c, int i, boolean b ) { return "" + c + "#" + i + "#" + b; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates instance methods with primitive arguments"
    )
    public void tm_06E26B27F( Test.Case tc ) {
    	tc.assertEqual( 
    		this.instanceMethodPrimitive( '$', 42, false ), 
    		this.container.evalSubjectMethod( this, "instanceMethodPrimitive", "", '$', 42, false )
        );
    }

    public List<String> noargInstanceMethod() { return List.of( "A", "B", "C" ); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates no-arg instance methods"
    )
    public void tm_0E7F9B2A0( Test.Case tc ) {
    	tc.assertEqual( 
    		this.noargInstanceMethod(), 
    		this.container.evalSubjectMethod( this, "noargInstanceMethod", null )
        );
    }
        
    public static List<Integer> noargStaticMethod() { return List.of( 2, 3, 4, 5 ); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates no-arg static methods" 
    )
    public void tm_0E91F48B9( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.noargStaticMethod(), 
    		this.container.evalSubjectMethod( null, "noargStaticMethod", null )
        );
    }

    String packageInstanceMethod() { return "foo"; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates package instance methods" 
    )
    public void tm_0832AEBD4( Test.Case tc ) {
    	tc.assertEqual( 
			this.packageInstanceMethod(), 
			this.container.evalSubjectMethod( this, "packageInstanceMethod", null )
        );
    }

    static Integer packageStaticMethod() { return 1331; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates package static methods" 
    )
    public void tm_061F53EED( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.packageStaticMethod(), 
    		this.container.evalSubjectMethod( null, "packageStaticMethod", null )
    	);
    }

    private String privateInstanceMethod() { return "42"; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates private instance methods" 
    )
    public void tm_0064F6F77( Test.Case tc ) {
    	tc.assertEqual( 
    		this.privateInstanceMethod(), 
    		this.container.evalSubjectMethod( this, "privateInstanceMethod", null )
    	);
    }

    private static int privateStaticMethod() { return -42; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates private static methods" 
    )
    public void tm_0729C4F50( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.privateStaticMethod(),
    		this.container.evalSubjectMethod( null, "privateStaticMethod", null )
    	);
    }
        
    protected Class<?> protectedInstanceMethod() { return Object.class; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates protected instance methods" 
    )
    public void tm_01F7BD56C( Test.Case tc ) {
    	tc.assertEqual( 
    		this.protectedInstanceMethod(), 
    		this.container.evalSubjectMethod( this, "protectedInstanceMethod", null )
    	);
    }

    protected static String protectedStaticMethod() { return "jhdsakja"; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates protected static methods" 
    )
    public void tm_0D023AE85( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.protectedStaticMethod(), 
    		this.container.evalSubjectMethod( null, "protectedStaticMethod", null )
    	);
    }

    public boolean publicInstanceMethod() { return true; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates public instance methods" 
    )
    public void tm_0EC7F6A21( Test.Case tc ) {
    	tc.assertEqual( 
    		this.publicInstanceMethod(), 
    		this.container.evalSubjectMethod( this, "publicInstanceMethod", null )
    	);
    }

    public static Map<String, Integer> publicStaticMethod() { return Map.of(); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates public static methods" 
    )
    public void tm_0E6FEEC7A( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.publicStaticMethod(), 
    		this.container.evalSubjectMethod( null, "publicStaticMethod", null )
    	);
    }

    public static String staticMethodMixed( String s, long l, Method m ) { return s + l + m; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates static methods with mixed arguments" 
    )
    public void tm_0C7B1B41A( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.staticMethodMixed( "hey", 0x1331L, null ), 
    		this.container.evalSubjectMethod( null, "staticMethodMixed", null, "hey", 0x1331L, null )
    	);
    }

    public static int staticMethodObjects( Integer i1, String s, Integer i2 ) { return i1 * i2 - s.hashCode(); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates static methods with object arguments" 
    )
    public void tm_0F0D55A42( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.staticMethodObjects( 29, "1 3 3 1", 57 ), 
    		this.container.evalSubjectMethod( null, "staticMethodObjects", null, 29, "1 3 3 1", 57 )
    	);
    }

    public static boolean staticMethodPrimitive( int i, char c ) { return i == c; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates static methods with primitive arguments" 
    )
    public void tm_052CD4C06( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.staticMethodPrimitive( 345, '#' ), 
    		this.container.evalSubjectMethod( null, "staticMethodPrimitive", null, 345, '#' )
    	);
    }

    public int returnsInt() { return 42; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws ClassCastException for wrong witness type" 
    )
    public void tm_091E6C138( Test.Case tc ) {
    	tc.expectError( ClassCastException.class );
    	tc.addMessage( this.container.evalSubjectMethod( this, "returnsInt", "" ) );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws AppExcpetion for null subject and non-static method" 
    )
    public void tm_0B37C3216( Test.Case tc ) {
    	this.container.evalSubjectMethod( this, "returnsInt", null );
    	tc.expectError( AppException.class );
    	this.container.evalSubjectMethod( null, "returnsInt", null );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws AssertionError for empty method name" 
    )
    public void tm_08E07798C( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.evalSubjectMethod( this, "", null );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws AssertionError for null method name" 
    )
    public void tm_08D129792( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.evalSubjectMethod( this, null, null );
    }

    private int voidInstanceValue = 0;
    private void voidInstanceMethod( int i ) { this.voidInstanceValue = i; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates void instance methods" 
    )
    public void tm_0864A9716( Test.Case tc ) {
    	this.voidInstanceMethod( 0 ); // To avoid warning about unused
		this.container.evalSubjectMethod( this, "voidInstanceMethod", null, 42 );
    	tc.assertEqual( 42, this.voidInstanceValue );
    }

    private static int voidStaticValue = 0;
    private static void voidStaticMethod( int i ) { TestTest.voidStaticValue = i; }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Evaluates void static methods" 
    )
    public void tm_01C2AD2AF( Test.Case tc ) {
    	TestTest.voidStaticMethod( 0 );
    	this.container.evalSubjectMethod( this, "voidStaticMethod", null, 42 );
    	tc.assertEqual( 42, TestTest.voidStaticValue );
    }
        
	public static class SomeError extends Error { private static final long serialVersionUID = 0L; }
	public void throwsSomeError() { throw new SomeError(); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws Error generated by method" 
    )
    public void tm_09864CD9B( Test.Case tc ) {
    	tc.expectError( SomeError.class );
    	this.container.evalSubjectMethod( this, "throwsSomeError", null );
    }
        
	public static class SomeRuntimeException extends RuntimeException { private static final long serialVersionUID = 0L; }
	public static void throwsSomeRuntimeException() { throw new SomeRuntimeException(); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Throws RuntimeException generated by method" 
    )
    public void tm_0B2BA344C( Test.Case tc ) {
    	tc.expectError( SomeRuntimeException.class );
    	this.container.evalSubjectMethod( this, "throwsSomeRuntimeException", null );
    }

	public static class SomeException extends Exception { private static final long serialVersionUID = 0L; }
    public void throwsSomeException() throws SomeException { throw new SomeException(); }
    @Test.Impl( 
    	member = "method: Object Test.Container.evalSubjectMethod(Object, String, Object, Object[])", 
    	description = "Wraps Exception generated by method in AppException" 
    )
    public void tm_012CA8323( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.evalSubjectMethod( this, "throwsSomeException", null );
    }

    int packageInstanceValue = 42;
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets package instance values" 
    )
    public void tm_02D269DD5( Test.Case tc ) {
    	tc.assertEqual( 
    		this.packageInstanceValue, 
    		this.container.getSubjectField( this, "packageInstanceValue", null ) 
    	);
    }

    static String packageStaticValue = "hi";
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets package static values" 
    )
    public void tm_0C3E3675C( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.packageStaticValue, 
    		this.container.getSubjectField( null, "packageStaticValue", null ) 
    	);
    }
        
    private int privateInstanceValue = 43;
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets private instance values" 
    )
    public void tm_0316199D2( Test.Case tc ) {
    	tc.assertEqual( 
    		this.privateInstanceValue, 
    		this.container.getSubjectField( this, "privateInstanceValue", null ) 
    	);
    }
        
    private static String privateStaticValue = "hi again";
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets private static values" 
    )
    public void tm_0EDB73E99( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.privateStaticValue, 
    		this.container.getSubjectField( null, "privateStaticValue", null )
    	);
    }

    protected boolean protectedInstanceValue = true;
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets protected instance values" 
    )
    public void tm_0A495AD7D( Test.Case tc ) {
    	tc.assertEqual( 
    		this.protectedInstanceValue, 
    		this.container.getSubjectField( this, "protectedInstanceValue", null ) 
    	);
    }

    protected static boolean protectedStaticValue = false;
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets protected static values" 
    )
    public void tm_02FA24104( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.protectedStaticValue, 
    		this.container.getSubjectField( null, "protectedStaticValue", null )
    	);
    }

    public List<String> publicInstanceValue = List.of( "Hello", "World" );
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets public instance values" 
    )
    public void tm_0C463328A( Test.Case tc ) {
    	tc.assertEqual( 
    		this.publicInstanceValue, 
    		this.container.getSubjectField( this, "publicInstanceValue", null ) 
    	);
    }

    public static List<Integer> publicStaticValue = List.of( 1, 2, 3 );
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Gets public static values" 
    )
    public void tm_0DF37A551( Test.Case tc ) {
    	tc.assertEqual( 
    		TestTest.publicStaticValue, 
    		this.container.getSubjectField( null, "publicStaticValue", null )
    	);
    }

    public String wrongWitnessValue = "foo";
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Throws ClassCastException for wrong witness type" 
    )
    public void tm_0BA4BF740( Test.Case tc ) {
    	tc.expectError( ClassCastException.class );
    	Integer value = null;
    	value = this.container.getSubjectField( this, "wrongWitnessValue", value );
    }

    public int nullSubjectInstance = 42;
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Throws AppExcpetion for null subject and non-static field" 
    )
    public void tm_0B2085CC3( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.getSubjectField( null, "nullSubjectInstance", 1 );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Throws AssertionError for empty field name" 
    )
    public void tm_06D112E2D( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.getSubjectField( this, "", null );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Throws AssertionError for null field name" 
    )
    public void tm_066F9EA57( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.getSubjectField( this, null, null );
    }
        
    @Test.Impl( 
    	member = "method: Object Test.Container.getSubjectField(Object, String, Object)", 
    	description = "Throws AppExcpetion for missing field" 
    )
    public void tm_000C7C5A5( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.getSubjectField( this, "bogusFieldName", null );
    }
        
    @Test.Impl( 
    	member = "method: Procedure Test.Container.afterAll()", 
    	description = "Default is NOOP" 
    )
    public void tm_0E4ABBA26( Test.Case tc ) {
    	class SomeContainer extends Test.Container { public SomeContainer() { super( Test.class ); } };
    	Test.Container c = new SomeContainer();
    	tc.assertEqual( Procedure.NOOP, c.afterAll() );
    }
        
    @Test.Impl( 
    	member = "method: Procedure Test.Container.afterEach()", 
    	description = "Default is NOOP" 
    )
    public void tm_0BEEDAD8A( Test.Case tc ) {
    	class SomeContainer extends Test.Container { public SomeContainer() { super( Test.class ); } };
    	Test.Container c = new SomeContainer();
    	tc.assertEqual( Procedure.NOOP, c.afterEach() );
    }
        
    @Test.Impl( 
    	member = "method: Procedure Test.Container.beforeEach()", 
    	description = "Default is NOOP" 
    )
    public void tm_0B88FCD61( Test.Case tc ) {
    	class SomeContainer extends Test.Container { public SomeContainer() { super( Test.class ); } };
    	Test.Container c = new SomeContainer();
    	tc.assertEqual( Procedure.NOOP, c.beforeEach() );
    }

    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Throws AppExcpetion for missing field" 
    )
    public void tm_09CA99D9C( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.setSubjectField( this, "bogusFieldName", null );
    }

    int incompatibleType = 1;
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Throws AppExcpetion for setting incompatible type" 
    )
    public void tm_066F7E58D( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.setSubjectField( this, "incompatibleType", "Hello" );
    }

    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets package instance values" 
    )
    public void tm_02AC6AF72( Test.Case tc ) {
    	int value = 3465245;
    	this.container.setSubjectField( this, "packageInstanceValue", value );
    	tc.assertEqual( value, this.packageInstanceValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets package static values" 
    )
    public void tm_06AA53C39( Test.Case tc ) {
    	String value = "137954";
    	this.container.setSubjectField( null, "packageStaticValue", value );
    	tc.assertEqual( value, TestTest.packageStaticValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets private instance values" 
    )
    public void tm_02F01AB6F( Test.Case tc ) {
    	int value = 321097;
    	this.container.setSubjectField( this, "privateInstanceValue", value );
    	tc.assertEqual( value, this.privateInstanceValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets private static values" 
    )
    public void tm_094791376( Test.Case tc ) {
    	String value = "Hello wurld.";
    	this.container.setSubjectField( null, "privateStaticValue", value );
    	tc.assertEqual( value, TestTest.privateStaticValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets protected instance values" 
    )
    public void tm_0BA77CBDA( Test.Case tc ) {
    	boolean value = true;
    	this.container.setSubjectField( this, "protectedInstanceValue", value );
    	tc.assertEqual( value, this.protectedInstanceValue );
    	value = false;
    	this.container.setSubjectField( this, "protectedInstanceValue", value );
    	tc.assertEqual( value, this.protectedInstanceValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets protected static values" 
    )
    public void tm_02D4252A1( Test.Case tc ) {
    	boolean value = true;
    	this.container.setSubjectField( null, "protectedStaticValue", value );
    	tc.assertEqual( value, TestTest.protectedStaticValue );
    	value = false;
    	this.container.setSubjectField( null, "protectedStaticValue", value );
    	tc.assertEqual( value, TestTest.protectedStaticValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets public instance values" 
    )
    public void tm_0F5DBF94D( Test.Case tc ) {
    	List<String> value = List.of( "a", "b", "...", "z" );
    	this.container.setSubjectField( this, "publicInstanceValue", value );
    	tc.assertEqual( value, this.publicInstanceValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Sets public static values" 
    )
    public void tm_026A940D4( Test.Case tc ) {
    	List<Integer> value = List.of(1, 2, 42 );
    	this.container.setSubjectField( null, "publicStaticValue", value );
    	tc.assertEqual( value, TestTest.publicStaticValue );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Throws AppExcpetion for null subject and non-static field" 
    )
    public void tm_0312D933A( Test.Case tc ) {
    	tc.expectError( AppException.class );
    	this.container.setSubjectField( null, "privateInstanceValue", "" );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Throws AssertionError for empty field name" 
    )
    public void tm_013C832A4( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.setSubjectField( this, "", null );
    }
        
    @Test.Impl( 
    	member = "method: void Test.Container.setSubjectField(Object, String, Object)", 
    	description = "Throws AssertionError for null field name" 
    )
    public void tm_095A4FB00( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	this.container.setSubjectField( this, null, null );
    }
        
    @Test.Impl( 
    	member = "method: void Test.eval(Class)", 
    	description = "Throws AssertionError for null subject" 
    )
    public void tm_05FAEFD2F( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Test.eval( null );
    }

    @Test.Impl( 
    	member = "method: void Test.evalPackage(Class)", 
    	description = "Throws AssertionError for null subject" 
    )
    public void tm_0FEED7639( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Test.evalPackage( null );
    }

	@Test.Impl( 
		member = "method: void Test.evalPackage(Class, boolean)", 
		description = "Throws AssertionError for null subject" 
	)
	public void tm_0CCC0451D( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Test.evalPackage( null );
	}
		
	@Test.Impl( 
		member = "method: void Test.evalPackage(Class, boolean)", 
		description = "When verbose is true details for each class TestResult are given" 
	)
	public void tm_06BBE429E( Test.Case tc ) {
		tc.addMessage( "Manually verified." );
		tc.assertTrue( true );
	}
	
	@Test.Impl( 
		member = "method: void Test.evalDir(Class, String[])", 
		description = "Throws AppException for components not on source path" 
	)
	public void tm_04AA69422( Test.Case tc ) {
		tc.expectError( AppException.class );
		Test.evalDir( sog.core.Test.class, "sog", "foo" );
	}
		
	@Test.Impl( 
		member = "method: void Test.evalDir(Class, String[])", 
		description = "Throws AssertionError for null subject class" 
	)
	public void tm_0B372F19F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Test.evalDir( null, "sog" );
	}


		

	public static void main( String[] args ) {
		//Test.eval( Test.class );
		//Test.evalPackage( Test.class );
		Test.evalDir( Test.class, "sog" );
	}
}
