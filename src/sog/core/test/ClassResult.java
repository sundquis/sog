/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Test;
import sog.util.Protection;

/**
 * 
 */
public class ClassResult extends Result implements Protection {
	
	private final Class<?> subjectClass;
	private final Container container;
	private final Map<String, Map<String, Method>> containerMethods;
	
	/**
	 * @param subjectClass
	 */
	public ClassResult( Class<?> subjectClass, Container container ) {
		super( "CLASS: " + Assert.nonNull( subjectClass ).getName() );
		
		this.subjectClass = subjectClass;
		this.container = Assert.nonNull( container );

		Function<Method, String> key1 = m -> m.getDeclaredAnnotation( Test.Impl.class ).member();
		Function<Method, String> key2 = m -> m.getDeclaredAnnotation( Test.Impl.class ).description();
		this.containerMethods = Arrays.stream( container.getClass().getDeclaredMethods() )
			.filter( m -> m.getDeclaredAnnotation( Test.Impl.class ) != null )
			.collect( Collectors.groupingBy( key1, Collectors.toMap( key2, Function.identity() ) ) );
	}
	
	
	@Override
	public void load() {
		this.scan( this.subjectClass );
		this.warnOrphans();
	}
	
	
	private void scan( Class<?> subject ) {
		// Silently skip member containers
		if ( Container.class.isAssignableFrom( subject ) ) {
			return;
		}
		
		// Determine if the entire (member) class is skipped; issue warnings/errors
		Test.Skip skip = subject.getDeclaredAnnotation( Test.Skip.class );
		if ( skip != null ) {
			if ( this.hasPublicProtection( subject ) ) {
				Msg.error( "Skipping public member class", "Subject = " + subject, "Skip = " + skip.value() );
			}
			if ( this.hasProtectedProtection( subject ) || this.hasPackageProtection( subject ) ) {
				Msg.warning( "Skipping member class", "Subject = " + subject, "Skip = " + skip.value() );
			}
			// Silently skip private member classes
			return;
		}
		
		// Constructors
		Arrays.stream( subject.getDeclaredConstructors() )
			.filter( c -> !c.isSynthetic() )
			.forEach( this::add );

		// Fields
		Arrays.stream( subject.getDeclaredFields() )
			.filter( f -> !f.isSynthetic() )
			.forEach( this::add );

		// Methods
		Arrays.stream( subject.getDeclaredMethods() )
			.filter( m -> !m.isSynthetic() )
			.filter( m -> !"main".equals( m.getName() ) )
			.forEach( this::add );
		
		// Member classes
		Arrays.stream( subject.getDeclaredClasses() ).forEach( this::scan );
	}
	
	private void add( Constructor<?> c ) {
		this.add( "CONST: " + c.toString(), c, c);
	}
	
	private void add( Field f ) {
		this.add( "FIELD: " + f.toString(), f, f );
	}
	
	private void add( Method m ) {
		this.add( "METHOD: " + m.toString(), m, m );
	}
	
	private void add( String name, AnnotatedElement element, Member member ) {
		Test.Skip testSkip = element.getDeclaredAnnotation( Test.Skip.class );
		Test.Case[] testCases = element.getDeclaredAnnotationsByType( Test.Case.class );
		boolean skip = testSkip != null;
		boolean test = testCases.length > 0;
		
		if ( skip && test ) {
			Msg.error( "Inconsistent meta-data", "Member = " + name, "Has " + testCases.length + " test cases", "Skip = " + testSkip.value() );
			return;
		}
		
		if ( !skip && !test ) {
			if ( this.hasPublicProtection( member ) ) {
				Msg.error( "No test cases on public member", "Member = " + name );
			}
			if ( this.hasProtectedProtection( member ) || this.hasPackageProtection( member ) ) {
				Msg.warning( "Unstated testing policy", "Member = " + name );
			}
			// Silently ignore private
			return;
		}
		
		if ( skip && !test ) {
			if ( this.hasPublicProtection( member ) ) {
				Msg.warning( "Skiping public member", "Member = " + name );
			}
			// Silently ignore other levels when Skip is declared
			return;
		}
		
		// ELSE: !skip && test
		this.addChild( new MemberResult( name, testCases, this.container, this.containerMethods.remove( name ) ) );
	}	
	
	
	private void warnOrphan( String memberName ) {
		Msg.error( "Subject class no longer contains member", "Subject = " + this.subjectClass, "Container = " + this.container.getClass().getName(), "Member = " + memberName );
	}
	
	private void warnOrphans() {
		this.containerMethods.keySet().stream().forEach( this::warnOrphan );
	}


}
