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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import sog.core.App;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class JSON {
	
	private JSON() {}

	public interface Element {
		/**
		 * Produce the canonical JSON string representation for the element.
		 * See: "https://www.json.org/json-en.html"
		 * 
		 * @return
		 */
		@Override public String toString();
	}
	
	
	public static class Member {
		
		private final JString key;
		private final Element value;
		
		public Member( JString key, Element value ) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.key.toString() + ": " + this.value.toString();
		}
	}

	
	public static class JObject implements Element {
		
		private final List<Member> members;
		
		public JObject() {
			this.members = new LinkedList<>();
		}
		
		public JObject add( JString key, Element value ) {
			this.members.add( new Member( key, value ) );
			return this;
		}
		
		public JObject add( String key, Element value ) {
			this.members.add( new Member( JSON.str( key ), value ) );
			return this;
		}
		
		@Override 
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( '{' );
			sb.append( this.members.stream().map( Member::toString ).collect( Collectors.joining( ", " ) ) );
			sb.append( '}' );
			return sb.toString();
		}
	}

	
	public static JObject obj() {
		return new JObject();
	}

	
	public static class JArray implements Element {
		
		private final List<Element> elements;

		public JArray() {
			this.elements = new LinkedList<>();
		}		
		
		public JArray add( Element element ) {
			this.elements.add( element );
			return this;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append( '[' );
			sb.append( this.elements.stream().map( Element::toString ).collect( Collectors.joining( ", " ) ) );
			sb.append( ']' );
			return sb.toString();
		}
	}
	
	
	public static JArray arr() {
		return new JArray();
	}

	
	public static final Element TRUE = new Element() {
		@Override public String toString() { return "true"; }
	};
	
	
	public static final Element FALSE = new Element() {
		@Override public String toString() { return "false"; }
	};
	
	
	public static final Element NULL = new Element() {
		@Override public String toString() { return "null"; }
	};
	
	
	public static class JString implements Element {
		private final String value;
		
		public JString( String value ) { this.value = value; }
		
		@Override 
		public String toString() { 
			return "\"" + this.value + "\""; 
		}
	}

	
	public static JString str( String s ) {
		return new JString(s );
	}
	
	
	public static class JNumber implements Element {
	
		private final int integer;
		private final int fraction;
		private final int exponent;
		
		public JNumber( int integer, int fraction, int exponent ) {
			this.integer = integer;
			this.fraction = fraction;
			this.exponent = exponent;
		}
		
		@Override
		public String toString() {
			return "" + this.integer 
				+ (this.fraction > 0 ? "." + this.fraction : "") 
				+ (this.exponent == 0 ? "" : "E" + this.exponent );
		}
	}

	
	public static JNumber num( int num ) {
		return new JNumber( num, 0, 0 );
	}
	
	
	public static JNumber dec( int num, int frac ) {
		return new JNumber( num, frac, 0 );
	}
	
	
	public static JNumber exp( int num, int frac, int exp ) {
		return new JNumber( num, frac, exp );
	}
	
	
	
	
	public static void main( String[] args ) {
		Element elt = JSON.obj()
			.add( "status", JSON.num(-1) )
			.add( "data", JSON.obj()
				.add( "seq", arr().add( FALSE ).add( NULL ).add( TRUE ).add( str( "foo" ) ).add( num(42) ) )
			);
		
		System.out.println( elt.toString() );
		
		App.get().done();
	}

}
