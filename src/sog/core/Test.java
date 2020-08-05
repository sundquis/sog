/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */

package sog.core;

import java.lang.annotation.Repeatable;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation types used by the testing framework.
 */
public @interface Test {
	
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.TYPE )
	public @interface Container {
		Class<? extends sog.core.test.Container> clazz();
	}
	
	/**
	 * Annotate class elements (containing class, member class, constructor, field, method)
	 * with {@code Test.Case} to declare a test case pertaining to the element.
	 */
	@Repeatable( Test.Cases.class )
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD, ElementType.TYPE } )
	public @interface Case {
		/**
		 * A description of the test. 
		 * 
		 * Members may have multiple {@code Test.Case} annotations but the descriptions for each member's 
		 * tests must be unique. Descriptions serve to further document the member's properties,
		 * but descriptions should be brief. 
		 */
		String value();
	}
	
	
	/**
	 * Container for repeated {@code Test.Case} annotations.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD, ElementType.TYPE } )
	public @interface Cases {
		Case[] value();
	}


	/**
	 * Used to indicate that the member (and contained members of a class) should not
	 * be scanned for test cases. Can be used to mark simple components that do not require
	 * testing or components that are externally tested.
	 * 
	 * The optional string {@code value} can be used to describe alternate testing
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD, ElementType.TYPE } )
	public @interface Skip {
		String value() default "No test";
	}
	

	/**
	 * Marker for test implementation methods. These are generated by the test framework and generally
	 * should not be hand coded. In particular the {@code src} and {@code desc} fields are generated to 
	 * correspond to a test declaration.
	 * 
	 * The optional numeric fields may be included to adjust features of the test.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.METHOD )
	public @interface Impl {

		/** Do not edit. Must match the corresponding declaration */
		String member();
		
		/** Do not edit. Must match the corresponding declaration */
		String description();

		/** CURRENTLY NOT USED */
		long timeout() default 0L;
		
		/** Used to control the order of execution of test methods */
		int priority() default 0;
		
		/** Used to scale the relative importance of the test case */
		int weight() default 1;
		
	}
	

}
