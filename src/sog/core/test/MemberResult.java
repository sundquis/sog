/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
public class MemberResult extends Result {
	
	private final Test.Skip skip;

	public MemberResult( AnnotatedElement member ) {
		super( Assert.nonNull( member ).toString() );
		
		this.skip = member.getDeclaredAnnotation( Test.Skip.class );
		
		Arrays.stream( member.getDeclaredAnnotationsByType( Test.Case.class ) )
			.forEach( c -> this.addChild( new CaseResult(c.value() ) ) );
	}

}
