/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.Arrays;

import sog.core.Assert;

/**
 * 
 */
public class ClassResult extends Result {
	
	/**
	 * @param subject
	 */
	public ClassResult( Class<?> subject ) {
		super( Assert.nonNull( subject).getSimpleName() );
		
		this.scan( subject );
	}
	
	private void scan( Class<?> subject ) {
		// The class itself is treated as a "member"
		this.addChild( new MemberResult( subject ) );
		
		// Constructors
		Arrays.stream( subject.getDeclaredConstructors() )
			.filter( c -> !c.isSynthetic() )
			.forEach( c -> this.addChild( new MemberResult(c) ) );

		// Fields
		Arrays.stream( subject.getDeclaredFields() )
			.filter( f -> !f.isSynthetic() )
			.forEach( f -> this.addChild( new MemberResult(f) ) );

		// Methods
		Arrays.stream( subject.getDeclaredMethods() )
			.filter( m -> !m.isSynthetic() )
			.filter( m -> !"main".equals( m.getName() ) )
			.forEach( m -> this.addChild( new MemberResult(m) ) );
		
		// Member classes
		Arrays.stream( subject.getDeclaredClasses() ).forEach( this::scan );
	}

}
