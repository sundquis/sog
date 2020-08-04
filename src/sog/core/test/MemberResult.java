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

import sog.core.Assert;
import sog.core.Test;
import sog.util.Fault;

/**
 * 
 */
public class MemberResult extends Result {
	
	
	private final AnnotatedElement member;
	private final Container container;
	private final Map<String, Method> methods;
	private final Test.Skip skip;

	public MemberResult( AnnotatedElement member, Container container, Map<String, Method> methods ) {
		super( Assert.nonNull( member ).toString() );
		
		this.member = member;
		this.container = container;
		this.methods = methods;
		this.skip = member.getDeclaredAnnotation( Test.Skip.class );
	}
	
	private void add( Test.Case tc ) {
		String description = tc.value();
		Method method = this.methods.remove( description );
		if ( method == null ) {
			new Fault( "No implementation for declared test case", this.member, description ).toss();
		}
		
		this.addChild( new CaseResult( description, this.container, method ));
	}
	
	public void init() {
		Arrays.stream( member.getDeclaredAnnotationsByType( Test.Case.class ) ).forEach( this::add );
		this.warnOrphans();
	}

	private void warnOrphan( String description ) {
		new Fault( "Orphaned test implementation", this.member, description ).toss();
	}
	
	private void warnOrphans() {
		if ( this.methods == null ) {
			return;
		}
		
		this.methods.keySet().stream().forEach( this::warnOrphan );
	}
	
}
