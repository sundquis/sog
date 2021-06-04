/**
 * Copyright (C) 2021
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
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sog.core.Test;
import sog.util.Protection;

/**
 * 
 */
@Test.Subject( ".TC" )
public class Policy implements Protection {

	
	// Require non-private executable, public fields
	public static final Policy DEFAULT = new Policy() {
		@Override public boolean requirePrivateConstructor() { return false; }
		@Override public boolean requireProtectedField() { return false; }
		@Override public boolean requirePackageField() { return false; }
		@Override public boolean requirePrivateField() { return false; }
		@Override public boolean requirePrivateMethod() { return false; }		
	};
	
	
	// All non-private
	public static final Policy STRICT = new Policy() {
		@Override public boolean requirePrivateConstructor() { return false; }
		@Override public boolean requirePrivateField() { return false; }
		@Override public boolean requirePrivateMethod() { return false; }		
	};
	
	
	// Require nothing
	public static final Policy EMPTY = new Policy() {
		@Override public boolean requirePublicConstructor() { return false; }
		@Override public boolean requirePackageConstructor() { return false; }
		@Override public boolean requireProtectedConstructor() { return false; }
		@Override public boolean requirePrivateConstructor() { return false; }
		@Override public boolean requirePublicField() { return false; }
		@Override public boolean requirePackageField() { return false; }
		@Override public boolean requireProtectedField() { return false; }
		@Override public boolean requirePrivateField() { return false; }
		@Override public boolean requirePublicMethod() { return false; }
		@Override public boolean requirePackageMethod() { return false; }
		@Override public boolean requireProtectedMethod() { return false; }
		@Override public boolean requirePrivateMethod() { return false; }
	};
	
	
	// Require everyhting
	public static final Policy ALL = new Policy();
	
	
	
	
	private static Policy current = Policy.ALL;
	
	public static Policy get() {
		return Policy.current;
	}
	
	public static void set( Policy policy ) {
		Policy.current = policy;
	}
	
	
	
	private Policy() {}
	
	
	
	public boolean requirePublicConstructor() { return true; }
	
	public boolean requireProtectedConstructor() { return true; }

	public boolean requirePackageConstructor() { return true; }

	public boolean requirePrivateConstructor() { return true; }

	public boolean requirePublicField() { return true; }
	
	public boolean requireProtectedField() { return true; }

	public boolean requirePackageField() { return true; }

	public boolean requirePrivateField() { return true; }

	public boolean requirePublicMethod() { return true; }
	
	public boolean requireProtectedMethod() { return true; }

	public boolean requirePackageMethod() { return true; }

	public boolean requirePrivateMethod() { return true; }
	
	
	
	final public boolean required( Constructor<?> constructor ) {
		Protection.Level level = this.getProtectionLevel( constructor );

		switch ( level ) {
			case PUBLIC:
				return this.requirePublicConstructor();
			case PACKAGE:
				return this.requirePackageConstructor();
			case PROTECTED:
				return this.requireProtectedConstructor();
			case PRIVATE:
				return this.requirePrivateConstructor();
			default:
				return false;
		}
	}

	final public boolean required( Field field ) {
		Protection.Level level = this.getProtectionLevel( field );
		
		switch ( level ) {
			case PUBLIC:
				return this.requirePublicField();
			case PACKAGE:
				return this.requirePackageField();
			case PROTECTED:
				return this.requireProtectedField();
			case PRIVATE:
				return this.requirePrivateField();
			default:
				return false;
		}
	}

	final public boolean required( Method method ) {
		Protection.Level level = this.getProtectionLevel( method );
		
		switch ( level ) {
			case PUBLIC:
				return this.requirePublicMethod();
			case PACKAGE:
				return this.requirePackageMethod();
			case PROTECTED:
				return this.requireProtectedMethod();
			case PRIVATE:
				return this.requirePrivateMethod();
			default:
				return false;
		}
	}
	
	
	
	public static void main( String[] args ) {
		Test.eval();
	}
	
	
	@Test.Skip( "Test container" )
	public static class TC extends Test.Container {
		public TC() {
			super( Policy.class );
		}
	}

}
