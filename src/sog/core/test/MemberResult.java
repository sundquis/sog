/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
public class MemberResult extends Result {
	
	private final Test.Case[] cases;
	private final Container container;
	private final Map<String, Method> methods;

	public MemberResult( String name, Test.Case[] cases, Container container, Map<String, Method> methods ) {
		super( Assert.nonEmpty( name ) );
		
		this.cases = Assert.nonEmpty( cases );
		this.container = Assert.nonNull( container );
		this.methods = methods;
	}
	
	public void load() {
		Arrays.stream( this.cases ).forEach( this::add );
		this.warnOrphans();
	}

	private void add( Test.Case tc ) {
		Method method = this.methods == null ? null : this.methods.remove( tc.value() );
		if ( method == null ) {
			Msg.stub( this.toString(), tc.value() );
		}
		
		this.addChild( new CaseResult( tc, this.container, method ));
	}
	
	private void warnOrphan( String description ) {
		Msg.error( "Orphaned test implementation", "Container = " + this.container.getClass().getName(), "Description = " + description  );
	}
	
	private void warnOrphans() {
		if ( this.methods == null ) {
			return;
		}
		
		this.methods.keySet().stream().forEach( this::warnOrphan );
	}

	
}
