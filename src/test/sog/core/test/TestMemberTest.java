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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.Policy;
import sog.core.test.TestIdentifier;
import sog.core.test.TestMember;
import sog.util.Pair;
import sog.util.Printable;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestMemberTest extends Test.Container {

	private final Map<Integer, Constructor<?>> CONSTRUCTORS;
	
	private final Map<String, Field> FIELDS;
	
	private final Map<String, Method> METHODS;
	
	private final Policy ORIGINAL_POLICY;
	
	public TestMemberTest() {
		super( TestMember.class );
		
		this.CONSTRUCTORS = Arrays.stream( MySubject.class.getDeclaredConstructors() )
			.collect( Collectors.toMap( Constructor::getParameterCount, Function.identity() ) );
		
		this.FIELDS = Arrays.stream( MySubject.class.getDeclaredFields() )
			.collect( Collectors.toMap( Field::getName, Function.identity() ) );
		
		this.METHODS = Arrays.stream( MySubject.class.getDeclaredMethods() )
			.collect( Collectors.toMap( Method::getName, Function.identity() ) );
		
		this.ORIGINAL_POLICY = Policy.get();
	}
	
	@Override
	public Procedure afterEach() {
		return () -> {
			Policy.set( this.ORIGINAL_POLICY );
		};
	}

	/* Get a constructor by the number of arguments */
	public Constructor<?> getConstructor( int argumentCount ) {
		return Assert.nonNull( this.CONSTRUCTORS.get( argumentCount ), "Missing constructor: " + argumentCount );
	}
	
	/* Get a field by name. */
	public Field getField( String name ) {
		return Assert.nonNull( this.FIELDS.get( name ), "Missing field: " + name );
	}
	
	/* Get a method by name. */
	public Method getMethod( String name ) {
		return Assert.nonNull( this.METHODS.get( name ), "Missing method: " + name );
	}
	
	public String getName( Class<?> clazz ) {
		return this.evalSubjectMethod( null, "getSimpleName", "", clazz );
	}
	
	public String getName( Constructor<?> constructor ) {
		return this.evalSubjectMethod( null, "getSimpleName", "", constructor );
	}
	
	public String getName( Field field ) {
		return this.evalSubjectMethod( null, "getSimpleName", "", field );
	}
	
	public String getName( Method method ) {
		return this.evalSubjectMethod( null, "getSimpleName", "", method );
	}
		
	
	
	
	public abstract static class MySubject {
		
		public enum EmptyEnum {}
		
		public enum CustomEnum {
			A(null, 0), 
			B(null, 0);
			
			CustomEnum( String s, int i ) {}
			public CustomEnum[] values( int i) { return null; }
			public CustomEnum valueOf( String s, int i ) { return null; }
		}
		
		public MySubject() {}

		@Test.Decl( "Constructor 1" )
		protected MySubject( String s ) {}
		
		@Test.Decl( "Constructor 2" )
		MySubject( String s, Integer i ) {}

		@Test.Decl( "Constructor 3" )
		private MySubject( Test t, Integer i, List<String> args ) {}

		
		public static final int FIELD1_DECL_COUNT = 4;

		public int field0 = 0;
		
		@Test.Decl( "Description 1" )
		@Test.Decl( "Description 2" )
		@Test.Decl( "Description 3" )
		@Test.Decl( "Description 4" )
		protected String field1;
		
		@Test.Decl( "Field 2" )
		Integer field2;
		
		@Test.Decl( "Field 3" )
		private Class<?> field3;
		
		public void method0() {}
		
		@Test.Decl( "method1, void, String" )
		protected void method1( String s ) {}
		
		@Test.Decl( "method2, Integer, List, Double" )
		Integer method2( List<String> list, Double d ) { return null;}
		
		@Test.Decl( "method3, boolean, int" )
		private boolean method3( int x ) { return false; }
		
		@Test.Decl( "method4, TestMember, String, TestMember, Class" )
		public TestMember method4( String s, TestMember p, Class<?> c ) { return null; }
		
		public static final String SKIP_REASON = "Reason for skiping this member";
		@Test.Skip( SKIP_REASON )
		public String skippedField = "Skipped";

		public abstract void abstractMethod();
		
		public static void main( String[] args ) {}
		
		private class Outer {
			private String myString;
			private Outer( String s ) { this.myString = s; }
			
			@SuppressWarnings( "unused" )
			private class Inner {
				Inner() { new Outer("hi"); Outer.this.myString = "hi"; }
				String foo() { return Outer.this.myString; }
			}
		}
		
	}
	

	
	
	
	// TEST METHODS
	
	@Test.Impl( 
		member = "constructor: TestMember(Constructor)", 
		description = "Throws AssertionError for null constructor" 
	)
	public void tm_0EBC43962( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> c = null;
		new TestMember( c );
	}
		
	@Test.Impl( 
		member = "constructor: TestMember(Field)", 
		description = "Throws AssertionError for null field" 
	)
	public void tm_0823CEBE2( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Field f = null;
		new TestMember( f );
	}
		
	@Test.Impl( 
		member = "constructor: TestMember(Method)", 
		description = "Throws AssertionError for null method" 
	)
	public void tm_08BBD693A( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Method m = null;
		new TestMember( m );
	}
		
	@Test.Impl( 
		member = "method: Stream TestMember.getDecls()", 
		description = "Each TestDecl has the correct member name" 
	)
	public void tm_0F6BAFFA6( Test.Case tc ) {
		final TestMember tm = new TestMember( this.getField( "field1" ) );
		tm.getDecls().map( TestIdentifier::getMemberName )
			.forEach( s -> tc.assertEqual( tm.toString(), s ) );
	}
		
	@Test.Impl( 
		member = "method: Stream TestMember.getDecls()", 
		description = "Returns one TestDecl for each Test.Decl" 
	)
	public void tm_098316090( Test.Case tc ) {
		final TestMember tm = new TestMember( this.getField( "field1" ) );
		tc.assertEqual( MySubject.FIELD1_DECL_COUNT, (int) tm.getDecls().count() );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Class)", 
		description = "Name is non-empty" 
	)
	public void tm_0C93EED34( Test.Case tc ) {
		Stream.of( Test.Case.class, Object.class, Printable.class, List.of(1).getClass() )
			.map( this::getName ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Class)", 
		description = "Name should be short and descriptive" 
	)
	public void tm_07F846BE8( Test.Case tc ) {
		tc.addMessage( "Visual inspection of the following names:" );
		tc.addMessage( "=========================================" );
		Stream.of( Test.Case.class, Object.class, Printable.class, List.of(1).getClass() )
			.map( this::getName ).forEach( tc::addMessage );
		// TOGGLE:
		//* */ tc.assertTrue( false ); /*
		tc.assertTrue( true );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Class)", 
		description = "Names for nested classes indicate enclosure" 
	)
	public void tm_092A54F47( Test.Case tc ) {
		class Local {
			class Nested {
				class Inner {}
			}
		}
		String name = this.getName( Local.Nested.Inner.class );
		tc.assertTrue( name.contains( "Local" ) );
		tc.assertTrue( name.contains( "Nested" ) );
		tc.assertTrue( name.contains( "Inner" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Name includes information about arguments" 
	)
	public void tm_068CBE115( Test.Case tc ) {
		Constructor<?> c = this.getConstructor( 1 ); // String
		String name = this.getName( c );
		tc.assertTrue( name.contains( "String" ) );
		
		c = this.getConstructor( 2 ); // String, Integer
		name = this.getName( c );
		tc.assertTrue( name.contains( "String" ) );
		tc.assertTrue( name.contains( "Integer" ) );

		c = this.getConstructor( 3 ); // Test, Integer, List<String>
		name = this.getName( c );
		tc.assertTrue( name.contains( "Test" ) );
		tc.assertTrue( name.contains( "Integer" ) );
		tc.assertTrue( name.contains( "List" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Name is non-empty" 
	)
	public void tm_07EBAFCF2( Test.Case tc ) {
		this.CONSTRUCTORS.values().stream().map( this::getName ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Name should be short and descriptive" 
	)
	public void tm_0E635176A( Test.Case tc ) {
		tc.addMessage( "Visual inspection of constructor names:" );
		tc.addMessage( "=======================================" );
		this.CONSTRUCTORS.values().stream().map( this::getName ).forEach( tc::addMessage );
		// TOGGLE:
		//* */ tc.assertTrue( false ); /*
		tc.assertTrue( true );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Field)", 
		description = "Name includes information about type" 
	)
	public void tm_0B134AB71( Test.Case tc ) {
		Field f = this.getField( "field1" ); // String
		String name = this.getName( f );
		tc.assertTrue( name.contains( "String" ) );
		
		f = this.getField( "field2" ); // Integer
		name = this.getName( f );
		tc.assertTrue( name.contains( "Integer" ) );
		
		
		f = this.getField( "field3" ); // Class<?>
		name = this.getName( f );
		tc.assertTrue( name.contains( "Class" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Field)", 
		description = "Name is non-empty" 
	)
	public void tm_095BAA372( Test.Case tc ) {
		this.FIELDS.values().stream().map( this::getName ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Field)", 
		description = "Name should be short and descriptive" 
	)
	public void tm_05AC2E0EA( Test.Case tc ) {
		tc.addMessage( "Visual inspection of field names:" );
		tc.addMessage( "=================================" );
		this.FIELDS.values().stream().map( this::getName ).forEach( tc::addMessage );
		// TOGGLE:
		//* */ tc.assertTrue( false ); /*
		tc.assertTrue( true );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Method)", 
		description = "Name includes information about arguments" 
	)
	public void tm_0BD7490FA( Test.Case tc ) {
		Method method = this.getMethod( "method1" );  // String
		String name = this.getName( method );
		tc.assertTrue( name.contains( "String" ) );
		
		method = this.getMethod( "method2" );  // List, Double
		name = this.getName( method );
		tc.assertTrue( name.contains( "List" ) );
		tc.assertTrue( name.contains( "Double" ) );
		
		method = this.getMethod( "method3" );  // int
		name = this.getName( method );
		tc.assertTrue( name.contains( "int" ) );
		
		method = this.getMethod( "method4" );  // String, TestMember, Class
		name = this.getName( method );
		tc.assertTrue( name.contains( "String" ) );
		tc.assertTrue( name.contains( "TestMember" ) );
		tc.assertTrue( name.contains( "Class" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Method)", 
		description = "Name includes information about return type" 
	)
	public void tm_0A3722C8E( Test.Case tc ) {
		Method method = this.getMethod( "method1" );  // void
		String name = this.getName( method );
		tc.assertTrue( name.contains( "void" ) );
		
		method = this.getMethod( "method2" );  // Integer
		name = this.getName( method );
		tc.assertTrue( name.contains( "Integer" ) );
		
		method = this.getMethod( "method3" );  // boolean
		name = this.getName( method );
		tc.assertTrue( name.contains( "boolean" ) );
		
		method = this.getMethod( "method4" );  // TestMember
		name = this.getName( method );
		tc.assertTrue( name.contains( "TestMember" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Method)", 
		description = "Return is non-empty" 
	)
	public void tm_01FDE0692( Test.Case tc ) {
		this.METHODS.values().stream().map( this::getName ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Method)", 
		description = "Return should be short and descriptive" 
	)
	public void tm_069E169CA( Test.Case tc ) {
		tc.addMessage( "Visual inspection of method names:" );
		tc.addMessage( "=================================" );
		this.METHODS.values().stream().map( this::getName ).forEach( tc::addMessage );
		// TOGGLE:
		//* */ tc.assertTrue( false ); /*
		tc.assertTrue( true );
		/* */
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSkipReason()", 
		description = "Configured reason reported for skipped elements" 
	)
	public void tm_0A9A7D743( Test.Case tc ) {
		TestMember tm = new TestMember( this.getField( "skippedField" ) );
		tc.assertEqual( MySubject.SKIP_REASON, tm.getSkipReason() );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSkipReason()", 
		description = "Descriptive reason given for abstract methods" 
	)
	public void tm_00D535A68( Test.Case tc ) {
		TestMember tm = new TestMember( this.getMethod( "abstractMethod" ) );
		tc.assertTrue( tm.getSkipReason().contains( "bstract" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSkipReason()", 
		description = "Descriptive reason given for enum generated members" 
	)
	public void tm_0AD65E6DD( final Test.Case tc ) {
		Consumer<TestMember> process = tm -> {
			tc.assertTrue( tm.isSkipped() );
			tc.assertNotEmpty( tm.getSkipReason() );
		};
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredConstructors() ).map( TestMember::new ).forEach( process );
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredMethods() ).map( TestMember::new ).forEach( process );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSkipReason()", 
		description = "Descriptive reason given for main method" 
	)
	public void tm_08947DECA( Test.Case tc ) {
		TestMember tm = new TestMember( this.getMethod( "main" ) );
		tc.assertNonNull( tm.getSkipReason() );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSkipReason()", 
		description = "Descriptive reason given for synthetic members" 
	)
	public void tm_0ABE7AE20( Test.Case tc ) {
		Consumer<TestMember> process = tm -> {
			tc.assertTrue( tm.isSkipped() );
			tc.assertNotEmpty( tm.getSkipReason() );
		};
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredFields() ).map( TestMember::new ).forEach( process );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.toString()", 
		description = "Return is not empty" 
	)
	public void tm_002DCEBC9( Test.Case tc ) {
		this.FIELDS.values().stream().map(  TestMember::new ).map( TestMember::toString ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.hasDecls()", 
		description = "Constructors: True iff member has one or more Test.Decl" 
	)
	public void tm_0CC36B001( Test.Case tc ) {
		TestMember tm = new TestMember( this.getConstructor( 0 ) );
		tc.assertFalse( tm.hasDecls() );
		
		tm = new TestMember( this.getConstructor( 1 ) );
		tc.assertTrue( tm.hasDecls() );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.hasDecls()", 
		description = "Fields: True iff member has one or more Test.Decl" 
	)
	public void tm_0001D4081( Test.Case tc ) {
		TestMember tm = new TestMember( this.getField( "field0" ) );
		tc.assertFalse( tm.hasDecls() );
		
		tm = new TestMember( this.getField( "field1" ) );
		tc.assertTrue( tm.hasDecls() );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.hasDecls()", 
		description = "Methods: True iff member has one or more Test.Decl" 
	)
	public void tm_060042314( Test.Case tc ) {
		TestMember tm = new TestMember( this.getMethod( "method0" ) );
		tc.assertFalse( tm.hasDecls() );
		
		tm = new TestMember( this.getMethod( "method1" ) );
		tc.assertTrue( tm.hasDecls() );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isEnumConstructor(Constructor)", 
		description = "False for custom enum constructors" 
	)
	public void tm_00959B896( Test.Case tc ) {
		Function<Constructor<?>, Boolean> process = c -> {
			TestMember tm = new TestMember( c );
			return this.evalSubjectMethod( tm, "isEnumConstructor", null, c );
		};
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredConstructors() )
			.map( process ).forEach( tc::assertTrue );
		Arrays.stream( MySubject.CustomEnum.class.getDeclaredConstructors() )
			.map( process ).forEach( tc::assertFalse );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isEnumValueOf(Method)", 
		description = "False for custom valueOf methods" 
	)
	public void tm_06F952BF3( Test.Case tc ) {
		Predicate<Method> custom = m -> {
			TestMember tm = new TestMember( m );
			return !this.evalSubjectMethod( tm, "isEnumValueOf", Boolean.FALSE, m );
		};
		tc.assertNonNull( Arrays.stream( MySubject.CustomEnum.class.getDeclaredMethods() )
			.filter( m -> m.getName().equals( "valueOf" ) ).filter( custom ).findFirst().orElse( null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isEnumValues(Method)", 
		description = "False for custom values methods" 
	)
	public void tm_0FC64EFF1( Test.Case tc ) {
		Predicate<Method> custom = m -> {
			TestMember tm = new TestMember( m );
			return !this.evalSubjectMethod( tm, "isEnumValues", Boolean.FALSE, m );
		};
		tc.assertNonNull( Arrays.stream( MySubject.CustomEnum.class.getDeclaredMethods() )
			.filter( m -> m.getName().equals( "values" ) ).filter( custom ).findFirst().orElse( null ) );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isRequired()", 
		description = "Constructors: True iff required by Policy" 
	)
	public void tm_0CE2B8A43( Test.Case tc ) {
		Policy.set(  Policy.PUBLIC );
		tc.assertTrue( new TestMember( this.getConstructor( 0 ) ).isRequired() );
		Stream.of( 1, 2, 3 ).map( this::getConstructor )
			.map( TestMember::new ).map( TestMember::isRequired ).forEach( tc::assertFalse );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isRequired()", 
		description = "Fields: True iff required by Policy" 
	)
	public void tm_03C5F4643( Test.Case tc ) {
		Policy.set( Policy.PROTECTED );
		tc.assertTrue( new TestMember( this.getField( "field1" ) ).isRequired() );
		Stream.of( 0, 2, 3 ).map( x -> "field" + x ).map( this::getField )
			.map( TestMember::new ).map( TestMember::isRequired ).forEach( tc::assertFalse );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isRequired()", 
		description = "Methods: True iff required by Policy" 
	)
	public void tm_062F94DA2( Test.Case tc ) {
		Policy.set( Policy.PACKAGE );
		tc.assertTrue( new TestMember( this.getMethod( "method2" ) ).isRequired() );
		Stream.of( 0, 1, 3 ).map( x -> "method" + x ).map( this::getMethod )
			.map(  TestMember::new ).map( TestMember::isRequired ).forEach( tc::assertFalse );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for abstract methods" 
	)
	public void tm_0EF0DA029( Test.Case tc ) {
		tc.assertTrue( new TestMember( this.getMethod( "abstractMethod" ) ).isSkipped() );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for enum constructor" 
	)
	public void tm_02F29DC10( Test.Case tc ) {
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredConstructors() )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for enum valueOf method" 
	)
	public void tm_097A17B19( Test.Case tc ) {
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredMethods() ).filter( m -> m.getName().equals( "valueOf" ) )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for enum values method" 
	)
	public void tm_08D3F05B5( Test.Case tc ) {
		Arrays.stream( MySubject.EmptyEnum.class.getDeclaredMethods() ).filter( m -> m.getName().equals( "values" ) )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for methods named main" 
	)
	public void tm_052B090E3( Test.Case tc ) {
		Arrays.stream( MySubject.class.getDeclaredMethods() ).filter( m -> m.getName().equals( "main" ) )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for synthetic constructors" 
	)
	public void tm_011816FA9( Test.Case tc ) {
		tc.addMessage( "Unable to create synthetic constructor?" );
		tc.assertTrue( true );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for synthetic fields" 
	)
	public void tm_0106AB269( Test.Case tc ) {
		Arrays.stream( MySubject.Outer.Inner.class.getDeclaredFields() )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True for synthetic methods" 
	)
	public void tm_07E8560D8( Test.Case tc ) {
		Arrays.stream( MySubject.Outer.class.getDeclaredMethods() )
			.map( TestMember::new ).map( TestMember::isSkipped ).forEach( tc::assertTrue );
	}
		
	@Test.Impl( 
		member = "method: boolean TestMember.isSkipped()", 
		description = "True if annotated with Test.Skip" 
	)
	public void tm_084045A84( Test.Case tc ) {
		tc.assertTrue( new TestMember( this.getField( "skippedField" ) ).isSkipped() );
	}
	
	
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_07734BC8B( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Class<?> c = null;
		TestMember.getSimpleName( c );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Throws AssertionError for null constructor" 
	)
	public void tm_0AC59028F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Constructor<?> c = null;
		TestMember.getSimpleName( c );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Field)", 
		description = "Throws AssertionError for null field" 
	)
	public void tm_0EC3C508F( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Field f = null;
		TestMember.getSimpleName( f );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Method)", 
		description = "Throws AssertionError for null method" 
	)
	public void tm_0737656A7( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Method m = null;
		TestMember.getSimpleName( m );
	}
	
	private static class InnerStatic {
		private static class Inner2 {}
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Array types handled correctly" 
	)
	public void tm_051271F45( Test.Case tc ) {
		Stream.of(
			new Pair<Object, String>( new String[] {}, "String[]" ),
			new Pair<Object, String>( new TestMember[][] {}, "TestMember[][]" ),
			new Pair<Object, String>( new InnerStatic[] {}, "TestMemberTest.InnerStatic[]" ),
			new Pair<Object, String>( new InnerStatic.Inner2[][][] {}, "TestMemberTest.InnerStatic.Inner2[][][]" )
		).forEach( p -> tc.assertEqual( p.getY(), TestMember.getTypeName( p.getX().getClass() ) ) );
	}

	private class InnerInstance {}
	
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Instance member classes show containing class name" 
	)
	public void tm_04890588D( Test.Case tc ) {
		InnerInstance ii = new InnerInstance();
		tc.assertEqual( "TestMemberTest.InnerInstance", TestMember.getTypeName( ii.getClass() ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Package suppressed for application classes" 
	)
	public void tm_025A7ABC3( Test.Case tc ) {
		Object obj = this;
		String packageName = obj.getClass().getPackageName();
		tc.assertTrue( obj.getClass().getName().startsWith( packageName ) );
		tc.assertFalse( TestMember.getTypeName( obj.getClass() ).startsWith( packageName ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Package suppressed for java.lang classes" 
	)
	public void tm_0E54364AD( Test.Case tc ) {
		Object obj = "";
		String packageName = obj.getClass().getPackageName();
		tc.assertTrue( obj.getClass().getName().startsWith( packageName ) );
		tc.assertFalse( TestMember.getTypeName( obj.getClass() ).startsWith( packageName ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Package suppressed for library classes" 
	)
	public void tm_011534A6E( Test.Case tc ) {
		Object obj = new java.util.Date();
		String packageName = obj.getClass().getPackageName();
		tc.assertTrue( obj.getClass().getName().startsWith( packageName ) );
		tc.assertFalse( TestMember.getTypeName( obj.getClass() ).startsWith( packageName ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Package suppressed for member classes" 
	)
	public void tm_0945C8669( Test.Case tc ) {
		Object obj = new InnerInstance();
		String packageName = obj.getClass().getPackageName();
		tc.assertTrue( obj.getClass().getName().startsWith( packageName ) );
		tc.assertFalse( TestMember.getTypeName( obj.getClass() ).startsWith( packageName ) );
	}

	void myFooMethod() {}
	int myFooMethod( int n ) { return 42; }
	
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Primitive types handled correctly" 
	)
	public void tm_0356D69D3( Test.Case tc ) throws NoSuchMethodException, SecurityException {
		tc.assertEqual( "void", TestMember.getTypeName( this.getClass().getDeclaredMethod( "myFooMethod" ).getReturnType() ) );
		tc.assertEqual( "int", TestMember.getTypeName( this.getClass().getDeclaredMethod( "myFooMethod", int.class ).getReturnType() ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Static member classes show containing class name" 
	)
	public void tm_091C8B3A6( Test.Case tc ) {
		tc.assertEqual( "TestMemberTest.InnerStatic", TestMember.getTypeName( InnerStatic.class ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getTypeName(Class)", 
		description = "Throws AssertionError for null class" 
	)
	public void tm_0A3B4BA53( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestMember.getTypeName( null );
	}

	
	
	

	public static void main( String[] args ) {
		Test.eval( TestMember.class ).showDetails( true ).print();
	}
}
