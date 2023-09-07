/**
 * Copyright (C) 2021, 2023
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */
package sog.util;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public interface Protection {

	@Test.Skip( "Enumerated constants" )
	public static enum Level {
		PUBLIC,
		PROTECTED,
		PACKAGE,
		PRIVATE
	}

	
	@Test.Decl( "Throws AssertionError for null member" )
	@Test.Decl( "Correct for public member" )
	@Test.Decl( "Correct for protected member" )
	@Test.Decl( "Correct for package member" )
	@Test.Decl( "Correct for private member" )
	default Level getProtectionLevel( Member member ) {
		return this.getProtectionLevel( Assert.nonNull( member.getModifiers() ) );
	}
	
	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Correct for public class" )
	@Test.Decl( "Correct for protected class" )
	@Test.Decl( "Correct for package class" )
	@Test.Decl( "Correct for private class" )
	default Level getProtectionLevel( Class<?> clazz ) {
		return this.getProtectionLevel( Assert.nonNull( clazz.getModifiers() ) );
	}
	
	
	
	@Test.Skip( "No additional testing" )
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
	
	

	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Correct for public class" )
	@Test.Decl( "Correct for non-public class" )
	default boolean hasPublicProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PUBLIC;
	}

	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Correct for protected class" )
	@Test.Decl( "Correct for non-protected class" )
	default boolean hasProtectedProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PROTECTED;
	}

	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Correct for package class" )
	@Test.Decl( "Correct for non-package class" )
	default boolean hasPackageProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PACKAGE;
	}

	@Test.Decl( "Throws AssertionError for null class" )
	@Test.Decl( "Correct for private class" )
	@Test.Decl( "Correct for non-private class" )
	default boolean hasPrivateProtection( Class<?> clazz ) {
		return this.getProtectionLevel( clazz ) == Level.PRIVATE;
	}
	
	

	@Test.Decl( "Throws AssertionError for null member" )
	@Test.Decl( "Correct for public member" )
	@Test.Decl( "Correct for non-public member" )
	default boolean hasPublicProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PUBLIC;
	}

	@Test.Decl( "Throws AssertionError for null member" )
	@Test.Decl( "Correct for protected member" )
	@Test.Decl( "Correct for non-protected member" )
	default boolean hasProtectedProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PROTECTED;
	}

	@Test.Decl( "Throws AssertionError for null member" )
	@Test.Decl( "Correct for package member" )
	@Test.Decl( "Correct for non-package member" )
	default boolean hasPackageProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PACKAGE;
	}

	@Test.Decl( "Throws AssertionError for null member" )
	@Test.Decl( "Correct for private member" )
	@Test.Decl( "Correct for non-private member" )
	default boolean hasPrivateProtection( Member member ) {
		return this.getProtectionLevel( member ) == Level.PRIVATE;
	}

}
