/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.util;

import java.util.function.Function;


/**
 * @author sundquis
 *
 */
@FunctionalInterface
public interface Operator<T> extends Function<T, T> {
	
	@Override T apply( T t );

}
