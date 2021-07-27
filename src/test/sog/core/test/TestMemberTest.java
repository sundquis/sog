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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Test;
import sog.core.test.TestIdentifier;
import sog.core.test.TestMember;
import sog.util.Printable;

/**
 * 
 */
public class TestMemberTest extends Test.Container {

	private final Map<String, Field> FIELDS;
	
	private final Map<Integer, Constructor<?>> CONSTRUCTORS;
	
	public TestMemberTest() {
		super( TestMember.class );
		
		this.FIELDS = Arrays.stream( MySubject.class.getDeclaredFields() )
			.collect( Collectors.toMap( Field::getName, Function.identity() ) );
		this.CONSTRUCTORS = Arrays.stream( MySubject.class.getDeclaredConstructors() )
			.collect( Collectors.toMap( Constructor::getParameterCount, Function.identity() ) );
	}

	public Field getField( String name ) {
		return Assert.nonNull( this.FIELDS.get( name ), "Missing field: " + name );
	}
	
	public Constructor<?> getConstructor( int argumentCount ) {
		return Assert.nonNull( this.CONSTRUCTORS.get( argumentCount ), "Missing constructor: " + argumentCount );
	}
	
	
	
	
	
	public static class MySubject {

		@Test.Decl( "Constructor 1" )
		public MySubject( String s ) {}
		
		@Test.Decl( "Constructor 2" )
		public MySubject( String s, Integer i ) {}

		@Test.Decl( "Constructor 3" )
		public MySubject( Test t, Integer i, List<String> args ) {}

		
		public static final int FIELD1_DECL_COUNT = 4;
		
		@Test.Decl( "Description 1" )
		@Test.Decl( "Description 2" )
		@Test.Decl( "Description 3" )
		@Test.Decl( "Description 4" )
		public String field1;
		
		@Test.Decl( "Field 2" )
		public Integer field2;
		
		@Test.Decl( "Field 3" )
		public Class<?> field3;
		
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
		Function<Class<?>, String> mapper = c ->
			this.evalSubjectMethod( null, "getSimpleName", "", c );
		Stream.of( Test.Case.class, Object.class, Printable.class, List.of(1).getClass() )
			.map( mapper ).forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Class)", 
		description = "Name should be short and descriptive" 
	)
	public void tm_07F846BE8( Test.Case tc ) {
		tc.addMessage( "Visual inspection of the following names:" );
		tc.addMessage( "=========================================" );
		Function<Class<?>, String> mapper = c ->
			this.evalSubjectMethod( null, "getSimpleName", "", c );
		Stream.of( Test.Case.class, Object.class, Printable.class, List.of(1).getClass() )
			.map( mapper ).forEach( tc::addMessage );
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
		String name = this.evalSubjectMethod( null, "getSimpleName", "", Local.Nested.Inner.class );
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
		String name = this.evalSubjectMethod( null, "getSimpleName", "", c );
		tc.assertTrue( name.contains( "String" ) );
		
		c = this.getConstructor( 2 ); // String, Integer
		name = this.evalSubjectMethod( null, "getSimpleName", "", c );
		tc.assertTrue( name.contains( "String" ) );
		tc.assertTrue( name.contains( "Integer" ) );

		c = this.getConstructor( 3 ); // Test, Integer, List<String>
		name = this.evalSubjectMethod( null, "getSimpleName", "", c );
		tc.assertTrue( name.contains( "Test" ) );
		tc.assertTrue( name.contains( "Integer" ) );
		tc.assertTrue( name.contains( "List" ) );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Name is non-empty" 
	)
	public void tm_07EBAFCF2( Test.Case tc ) {
		Function<Constructor<?>, String> mapper = c -> this.evalSubjectMethod( null, "getSimpleName", "", c );
		Stream.of( 1, 2, 3 ).map( this::getConstructor ).map( mapper )
			.forEach( tc::assertNotEmpty );
	}
		
	@Test.Impl( 
		member = "method: String TestMember.getSimpleName(Constructor)", 
		description = "Name should be short and descriptive" 
	)
	public void tm_0E635176A( Test.Case tc ) {
		tc.addMessage( "Visual inspection of constructor names:" );
		tc.addMessage( "=======================================" );
		Function<Constructor<?>, String> mapper = c ->
			this.evalSubjectMethod( null, "getSimpleName", "", c );
		Stream.of( 1, 2, 3 ).map( this::getConstructor ).map( mapper )
			.forEach( tc::addMessage );
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
		String name = this.evalSubjectMethod( null, "getSimpleName", "", f );
		tc.assertTrue( name.contains( "String" ) );
		
		f = this.getField( "field2" ); // Integer
		name = this.evalSubjectMethod( null, "getSimpleName", "", f );
		tc.assertTrue( name.contains( "Integer" ) );
		
		
		f = this.getField( "field3" ); // Class<?>
		name = this.evalSubjectMethod( null, "getSimpleName", "", f );
		tc.assertTrue( name.contains( "Class" ) );
	}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Field)", 
			description = "Name is non-empty" 
		)
		public void tm_095BAA372( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Field)", 
			description = "Name should be short and descriptive" 
		)
		public void tm_05AC2E0EA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Method)", 
			description = "Name includes information about arguments" 
		)
		public void tm_0BD7490FA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Method)", 
			description = "Name includes information about return type" 
		)
		public void tm_0A3722C8E( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Method)", 
			description = "Return is non-empty" 
		)
		public void tm_01FDE0692( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSimpleName(Method)", 
			description = "Return should be short and descriptive" 
		)
		public void tm_069E169CA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSkipReason()", 
			description = "Configured reason reported for skipped elements" 
		)
		public void tm_0A9A7D743( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSkipReason()", 
			description = "Descriptive reason given for abstract methods" 
		)
		public void tm_00D535A68( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSkipReason()", 
			description = "Descriptive reason given for enum generated members" 
		)
		public void tm_0AD65E6DD( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSkipReason()", 
			description = "Descriptive reason given for main method" 
		)
		public void tm_08947DECA( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.getSkipReason()", 
			description = "Descriptive reason given for synthetic members" 
		)
		public void tm_0ABE7AE20( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: String TestMember.toString()", 
			description = "Return is not empty" 
		)
		public void tm_002DCEBC9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.hasDecls()", 
			description = "Constructors: True iff member has one or more Test.Decl" 
		)
		public void tm_0CC36B001( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.hasDecls()", 
			description = "Fields: True iff member has one or more Test.Decl" 
		)
		public void tm_0001D4081( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.hasDecls()", 
			description = "Methods: True iff member has one or more Test.Decl" 
		)
		public void tm_060042314( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isEnumConstructor(Constructor)", 
			description = "False for custom enum constructors" 
		)
		public void tm_00959B896( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isEnumValueOf(Method)", 
			description = "False for custom valueOf methods" 
		)
		public void tm_06F952BF3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isEnumValues(Method)", 
			description = "False for custom values methods" 
		)
		public void tm_0FC64EFF1( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isRequired()", 
			description = "Constructors: True iff required by Policy" 
		)
		public void tm_0CE2B8A43( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isRequired()", 
			description = "Fields: True iff required by Policy" 
		)
		public void tm_03C5F4643( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isRequired()", 
			description = "Methods: True iff required by Policy" 
		)
		public void tm_062F94DA2( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for abstract methods" 
		)
		public void tm_0EF0DA029( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for enum constructor" 
		)
		public void tm_02F29DC10( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for enum valueOf method" 
		)
		public void tm_097A17B19( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for enum values method" 
		)
		public void tm_08D3F05B5( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for methods named main" 
		)
		public void tm_052B090E3( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for synthetic constructors" 
		)
		public void tm_011816FA9( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for synthetic fields" 
		)
		public void tm_0106AB269( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True for synthetic methods" 
		)
		public void tm_07E8560D8( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
		
		@Test.Impl( 
			member = "method: boolean TestMember.isSkipped()", 
			description = "True if annotated with Test.Skip" 
		)
		public void tm_084045A84( Test.Case tc ) {
			tc.addMessage( "GENERATED STUB" );
		}
	
	
	
	

	public static void main( String[] args ) {
		Test.eval( TestMember.class );
		// Test.evalPackage( TestMember.class );
	}
}
