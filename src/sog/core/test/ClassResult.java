/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.Assert;
import sog.core.Test;
import sog.util.Fault;

/**
 * 
 */
public class ClassResult extends Result {
	
	private final Class<?> subjectClass;
	private final Test.Skip skip;
	private Container container;
	private Map<String, Map<String, Method>> containerMethods;
	
	/**
	 * @param subjectClass
	 */
	public ClassResult( Class<?> subjectClass ) {
		super( Assert.nonNull( subjectClass ).getSimpleName() );
		
		this.subjectClass = subjectClass;
		this.skip = subjectClass.getDeclaredAnnotation( Test.Skip.class );
		this.container = null;
		this.containerMethods = null;
	}
	
	
	public void init() {
		if ( this.skip != null ) {
			new Fault( "Skipping:", this.subjectClass, this.skip.value() ).toss();
			return;
		}

		this.getContainer();
		if ( this.container == null ) {
			return;
		}
		
		this.loadContainer();
		this.scan( this.subjectClass );
		this.warnOrphans();
	}
	
	
	/**
	 * If {@code this.subjectClass} represents a {@code Container} then a Fault is generated.
	 * 
	 * Otherwise, a set of well-known locations is examined for a container class.
	 * 
	 * 1. A public static inner class of {@code this.subjectClass} implementing Container.
	 * 2. A class in the "parallel" package obtained by replacing the root package name-element with "test".
	 * 
	 * If a legal {@code Container} instance cannot be created a Fault is generated.
	 */
	private void getContainer() {
		Class<?> containerClass = null;
		
		containerClass = Arrays.stream( this.subjectClass.getDeclaredClasses() )
			.filter( c -> Container.class.isAssignableFrom( c ) )
			.findFirst()
			.orElse( null );
		
		if( containerClass == null ) {
			String[] comps = this.subjectClass.getName().split( "\\." );
			comps[0] = "test";
			try {
				containerClass = Class.forName( String.join( ".",  comps ) );
			} catch ( ClassNotFoundException e ) {
				new Fault( "No container for subject", this.subjectClass ).toss();
				return;
			}
		}
		
		try {
			this.container = (Container) containerClass.getDeclaredConstructor().newInstance();
		} catch ( Exception e ) {
			new Fault( "Unable to construct Container", e ).toss();
			this.container = null;
			return;
		}
		
		if ( !this.subjectClass.equals( this.container.subjectClass() ) ) {
			new Fault( "Bad container", 
				"this.subject = ", this.subjectClass, 
				"container.subject = ", this.container.subjectClass() ).toss();
			this.container = null;
			return;
		}
		
	}
	
	
	private void loadContainer() {
		Function<Method, String> key1 = m -> m.getDeclaredAnnotation( Test.Impl.class ).member();
		Function<Method, String> key2 = m -> m.getDeclaredAnnotation( Test.Impl.class ).description();
		this.containerMethods = Arrays.stream( this.container.getClass().getDeclaredMethods() )
			.filter( m -> m.getDeclaredAnnotation( Test.Impl.class ) != null )
			.collect( Collectors.groupingBy( key1, Collectors.toMap( key2, Function.identity() ) ) );
	}
	
	
	private void add( AnnotatedElement elt ) {
		this.addChild( new MemberResult( elt, this.container, this.containerMethods.remove( elt.toString() ) ) );
	}
	
	
	private void scan( Class<?> subject ) {
		// The class itself is treated as a "member"
		this.add( subject );
		
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
	
	private void warnOrphan( String memberName ) {
		new Fault( "Subject class no longer contains member", this.subjectClass, memberName ).toss();
	}
	
	private void warnOrphans() {
		this.containerMethods.keySet().stream().forEach( this::warnOrphan );
	}

}
