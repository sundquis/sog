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
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Assert;
import sog.core.Test;

/**
 * Responsibilities: 
 * 		Represents a single member in a subject class.
 * 		Defines the naming policy for classes, constructors, fields, and methods.
 * 		Provides logic for handling special cases such as synthetic members.
 * 	Structure:
 * 		Constructors (one for each type of member) determine and record basic properties.
 */
@Test.Subject( "test." )
public class TestMember {


	/* Naming policy for classes suppresses package information. */
	@Test.Decl( "Package suppressed for java.lang classes" )
	@Test.Decl( "Package suppressed for library classes" )
	@Test.Decl( "Package suppressed for application classes" )
	@Test.Decl( "Package suppressed for member classes" )
	@Test.Decl( "Primitive types handled correctly" )
	@Test.Decl( "Array types handled correctly" )
	@Test.Decl( "Static member classes show containing class name" )
	@Test.Decl( "Instance member classes show containing class name" )
	@Test.Decl( "Throws AssertionError for null class" )
	public static String getTypeName( Class<?> clazz ) {
		Assert.nonNull( clazz );
		if ( clazz.isArray() ) {
			return TestMember.getTypeName( clazz.getComponentType() ) + "[]";
		} else {
			return (clazz.isMemberClass() ? getTypeName( clazz.getEnclosingClass() ) + "." : "") + clazz.getSimpleName();
		}
	}
	
	/* Naming policy for member classes. */
	@Test.Decl( "Name is non-empty" )
	@Test.Decl( "Name should be short and descriptive" )
	@Test.Decl( "Names for nested classes indicate enclosure" )
	@Test.Decl( "Throws AssertionError for null class" )
	public static String getSimpleName( Class<?> clazz ) {
		Assert.nonNull( clazz );
		return "class: " + getTypeName( clazz );
	}
	
	/* Naming policy for constructors. */
	@Test.Decl( "Name is non-empty" )
	@Test.Decl( "Name should be short and descriptive" )
	@Test.Decl( "Name includes information about arguments" )
	@Test.Decl( "Throws AssertionError for null constructor" )
	public static String getSimpleName( Constructor<?> constructor ) {
		Assert.nonNull( constructor );
		return new StringBuilder()
			.append( "constructor: " )
			.append( getTypeName( constructor.getDeclaringClass() ) )
			.append( "(" )
			.append( Arrays.stream( constructor.getParameterTypes() )
				.map( TestMember::getTypeName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	/* Naming policy for member fields. */
	@Test.Decl( "Name is non-empty" )
	@Test.Decl( "Name should be short and descriptive" )
	@Test.Decl( "Name includes information about type" )
	@Test.Decl( "Throws AssertionError for null field" )
	public static String getSimpleName( Field field ) {
		Assert.nonNull( field );
		return new StringBuilder()
			.append( "field: " )
			.append( getTypeName( field.getType() ) )
			.append( " " )
			.append( getTypeName( field.getDeclaringClass() ) )
			.append( "." )
			.append( field.getName() )
			.toString();
	}
	
	/* Naming policy for member methods. */
	@Test.Decl( "Return is non-empty" )
	@Test.Decl( "Return should be short and descriptive" )
	@Test.Decl( "Name includes information about arguments" )
	@Test.Decl( "Name includes information about return type" )
	@Test.Decl( "Throws AssertionError for null method" )
	public static String getSimpleName( Method method ) {
		Assert.nonNull( method );
		return new StringBuilder()
			.append( "method: " )
			.append( getTypeName( method.getReturnType() ) )
			.append( " " )
			.append( getTypeName( method.getDeclaringClass() ) )
			.append( "." )
			.append( method.getName() )
			.append( "(" )
			.append( Arrays.stream( method.getParameterTypes() )
				.map( TestMember::getTypeName )
				.collect( Collectors.joining( ", " ) ) )
			.append( ")" )
			.toString();
	}
	
	
	
	
	private final String memberName;
	
	private final Test.Skip skip;
	
	private final Test.Decl[] decls;
	
	private final boolean isRequired;
	
	private final boolean isSynthetic;
	
	private final boolean isMain;
	
	private final boolean isAbstract;
	
	private final boolean isEnumSpecial;

	
	@Test.Decl( "Throws AssertionError for null constructor" )
	public TestMember( Constructor<?> constructor ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( constructor ) );
		this.skip = constructor.getDeclaredAnnotation( Test.Skip.class );
		this.decls = constructor.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( constructor );
		this.isSynthetic = constructor.isSynthetic();
		this.isMain= false;
		this.isAbstract = false;
		this.isEnumSpecial = this.isEnumConstructor( constructor );
	}

	@Test.Decl( "False for custom enum constructors" )
	private boolean isEnumConstructor( Constructor<?> constructor ) {
		return Enum.class.isAssignableFrom( constructor.getDeclaringClass() )
			&& constructor.getParameterCount() == 2
			&& String.class.equals( constructor.getParameterTypes()[0] )
			&& int.class.equals( constructor.getParameterTypes()[1] );
	}
	
	@Test.Decl( "Throws AssertionError for null field" )
	public TestMember( Field field ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( field ) );
		this.skip = field.getDeclaredAnnotation( Test.Skip.class );
		this.decls = field.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( field );
		this.isSynthetic = field.isSynthetic();
		this.isMain= false;
		this.isAbstract = false;
		this.isEnumSpecial = false;
	}
	
	@Test.Decl( "Throws AssertionError for null method" )
	public TestMember( Method method ) {
		this.memberName = TestMember.getSimpleName( Assert.nonNull( method ) );
		this.skip = method.getDeclaredAnnotation( Test.Skip.class );
		this.decls = method.getDeclaredAnnotationsByType( Test.Decl.class );
		this.isRequired = Policy.get().required( method );
		this.isSynthetic = method.isSynthetic();
		this.isMain= "main".equals( method.getName() );
		this.isAbstract = Modifier.isAbstract( method.getModifiers() );
		this.isEnumSpecial = this.isEnumValues( method ) || this.isEnumValueOf( method );
	}

	@Test.Decl( "False for custom valueOf methods" )
	private boolean isEnumValueOf( Method method ) {
		return Enum.class.isAssignableFrom( method.getDeclaringClass() )
			&& "valueOf".equals(  method.getName() ) 
			&& method.getParameterCount() == 1
			&& String.class.equals( method.getParameterTypes()[0] );
	}
	
	@Test.Decl( "False for custom values methods" )
	private boolean isEnumValues( Method method ) {
		return Enum.class.isAssignableFrom( method.getDeclaringClass() )
			&& "values".equals(  method.getName() ) 
			&& method.getParameterCount() == 0;
	}
	
		
	@Test.Decl( "True if annotated with Test.Skip" )
	@Test.Decl( "True for synthetic constructors" )
	@Test.Decl( "True for synthetic fields" )
	@Test.Decl( "True for synthetic methods" )
	@Test.Decl( "True for methods named main" )
	@Test.Decl( "True for abstract methods" )
	@Test.Decl( "True for enum constructor" )
	@Test.Decl( "True for enum values method" )
	@Test.Decl( "True for enum valueOf method" )
	public boolean isSkipped() { 
		return this.skip != null || this.isSynthetic || this.isMain || this.isAbstract || this.isEnumSpecial;
	}

	@Test.Decl( "Configured reason reported for skipped elements" )
	@Test.Decl( "Descriptive reason given for synthetic members" )
	@Test.Decl( "Descriptive reason given for main method" )
	@Test.Decl( "Descriptive reason given for abstract methods" )
	@Test.Decl( "Descriptive reason given for enum generated members" )
	public String getSkipReason() { 
		return this.skip != null ? this.skip.value() : 
			this.isSynthetic ? "Synthetic" : 
				this.isMain ? "main() method" : 
					this.isAbstract ? "Abstract method" : 
						this.isEnumSpecial ? "Enum Special" : null;
	}

	@Test.Decl( "Constructors: True iff required by Policy" )
	@Test.Decl( "Fields: True iff required by Policy" )
	@Test.Decl( "Methods: True iff required by Policy" )
	public boolean isRequired() {
		return this.isRequired; 
	}
	
	@Test.Decl( "Constructors: True iff member has one or more Test.Decl" )
	@Test.Decl( "Fields: True iff member has one or more Test.Decl" )
	@Test.Decl( "Methods: True iff member has one or more Test.Decl" )
	public boolean hasDecls() { 
		return this.decls.length > 0; 
	}

	@Test.Decl( "Returns one TestDecl for each Test.Decl" )
	@Test.Decl( "Each TestDecl has the correct member name" )
	public Stream<TestDecl> getDecls() { 
		return Arrays.stream( this.decls ).map( d -> new TestDecl( TestMember.this.memberName, d.value() ) );
	}
	
	
	@Override
	@Test.Decl( "Return is not empty" )
	public String toString() {
		return this.memberName;
	}


}
