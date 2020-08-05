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
	private Container container;
	private Map<String, Map<String, Method>> containerMethods;
	
	/**
	 * @param subjectClass
	 */
	public ClassResult( Class<?> subjectClass, Container container ) {
		super( Assert.nonNull( subjectClass ).getSimpleName() );
		this.container = Assert.nonNull( container );
		
		this.subjectClass = subjectClass;
		this.containerMethods = null;
	}
	
	
	public void load() {
		//this.loadContainer();
		//this.scan( this.subjectClass );
		//this.warnOrphans();
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
