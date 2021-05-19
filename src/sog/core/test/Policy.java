/**
 * Copyright (C) 2017, 2018, 2019, 2020 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sog.util.Protection;

/**
 * 
 */
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
	
	
	
	
	private static Policy current = Policy.DEFAULT;
	
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

}
