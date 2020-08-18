/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.util;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * 
 */
public interface Protection {
	
	public static enum Level {
		PUBLIC,
		PROTECTED,
		PACKAGE,
		PRIVATE
	}
	
	default Level getProtectionLevel( Member member ) {
		return this.getProtectionLevel( member.getModifiers() );
	}
	
	default Level getProtectionLevel( Class<?> clazz ) {
		return this.getProtectionLevel( clazz.getModifiers() );
	}
	
	default Level getProtectionLevel( int mod ) {
		if ( Modifier.isPublic( mod ) ) {
			return Level.PUBLIC;
		} else if ( Modifier.isProtected( mod ) ) {
			return Level.PROTECTED;
		} else if ( Modifier.isPrivate( mod ) ) {
			return Level.PRIVATE;
		} else {
			return Level.PACKAGE;
		}
	}
	
	default boolean hasPublicProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PUBLIC;
	}

	default boolean hasProtectedProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PROTECTED;
	}

	default boolean hasPackageProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PACKAGE;
	}

	default boolean hasPrivateProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PRIVATE;
	}

	default boolean hasPublicProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PUBLIC;
	}

	default boolean hasProtectedProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PROTECTED;
	}

	default boolean hasPackageProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PACKAGE;
	}

	default boolean hasPrivateProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PRIVATE;
	}

}
