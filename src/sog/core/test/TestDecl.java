/**
 * Copyright (C) 2017 -- 2021 by TS Sundquist
 * *** *** * 
 * All rights reserved.
 * 
 */
package sog.core.test;

import java.util.LinkedList;
import java.util.List;

import sog.core.SoftString;
import sog.core.Test;

/**
 * 
 */
public class TestDecl {
	

	private final String member;
	private final String description;
	private int implCount = 0;

	public TestDecl( TestMember testMember, String description ) {
		this.member = testMember.toString();
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "(" + this.member + ", " + this.description + ")";
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( other == null || !TestDecl.class.equals( other.getClass() ) ) {
			return false;
		} else {
			return this.toString().equals( other.toString() );
		}
	}

}
