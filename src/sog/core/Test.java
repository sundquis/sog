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
 */
public interface Test {

	/**
	 * Subject classes register a container that holds test methods
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.TYPE )
	public @interface Container {
		/**
		 * Name of the test container class
		 */
		String value();
	}
	
	/**
	 * Annotate class elements (containing class, member class, constructor, field, method)
	 * with {@code Test.Case} to declare a test case pertaining to the element.
	 */
	@Repeatable( Test.Cases.class )
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD } )
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
	@Target( { ElementType.CONSTRUCTOR , ElementType.FIELD, ElementType.METHOD } )
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
		
		/** Used to scale the relative importance of the test case */
		int weight() default 1;
		
	}
	
	
	
	
	/**
	 * Only displayed for non-passing cases
	 * 
	 * @param message
	 * 		Failure message
	 * @return
	 * 		this Test
	 */
	public Test addMessage( String message );


	/**
	 * Type of Throwable that subsequent code is expected to throw
	 * 
	 * @param expectedError
	 * 		Throwable type
	 * @return
	 * 		this Test
	 */
	public Test expectError( Class<? extends Throwable> expectedError );


	/**
	 * Procedure to call after the current method (in a Container) completes
	 * Will be called even if the method throws an exception.
	 * 
	 * @param callAfter
	 * @return
	 * 		this Test
	 */
	public Test afterThis( Procedure callAfter );

	
	/**
	 * Mark the current case as successful.
	 * 
	 * @return
	 * 		this Test
	 */
	public Test pass();


	/**
	 * Mark the current case as failed.
	 * 
	 * @return
	 * 		this Test
	 */
	public Test fail();
	

	/**
	 * Assert that the given object is not null.
	 * 
	 * @param obj
	 * @return
	 * 		this Test
	 */
	public Test notNull( Object obj );
	
	/**
	 * Assert that the given string is non-null and not empty
	 * 
	 * @param s
	 * @return
	 * 		this Test
	 */
	public Test notEmpty( String s );
	
	
	/**
	 * Assert that the given object is not null.
	 * 
	 * @param obj
	 * @return
	 * 		this Test
	 */
	public Test isNull( Object obj );
	
	
	/**
	 * Conditionally mark the case as passed/failed.
	 *  
	 * @param passIfTrue
	 * @return
	 * 		this Test
	 */
	public Test assertTrue( boolean passIfTrue );
	
	
	/**
	 * Conditionally mark the case as passed/failed.
	 *  
	 * @param passIfFalse
	 * @return
	 * 		this Test
	 */
	public Test assertFalse( boolean passIfFalse);
	
	
	/**
	 * Test for equality using Object.equals().
	 * If T is a compound type (array or collection) then the components are shallowly tested.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 * 		this Test
	 */
	public <T> Test assertEqual( T expected, T actual );
	
	

}
