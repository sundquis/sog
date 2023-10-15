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

package sog.util.json;

import java.util.List;
import java.util.Map;

import sog.core.Test;

/**
 * Static types and helpers
 */
@Test.Subject( "test." )
public class JSON {
	
	private JSON() {}

	
	public interface JElement {
		
		/**
		 * Produce the canonical JSON string representation for the element.
		 * See: "https://www.json.org/json-en.html"
		 * 
		 * @return
		 */
		public String toJSON();
		
		/** Attempt to cast to JObject */
		public JObject toJObject() throws IllegalCast;
		
		/** Attempt to cast to JAray */
		public JArray toJArray() throws IllegalCast;
		
		/** Attempt to cast to JObject */
		public JString toJString() throws IllegalCast;
		
		/** Attempt to cast to JObject */
		public JNumber toJNumber() throws IllegalCast;
		
		/** Attempt to cast to JObject */
		public JBoolean toJBoolean() throws IllegalCast;
		
	}

	
	
	public static interface JObject extends JElement {
		
		public JObject add( JString key, JElement value );
		
		public JObject add( String key, JElement value );
		
		public Map<String, JElement> toJavaMap();
		
	}
	
	public static JObject obj() {
		return new JObjectImpl();
	}
	
	
	
	public static interface JArray extends JElement {
		
		public JArray add( JElement element );
		
		public List<JElement> toJavaList();
		
	}
	
	public static JArray arr() {
		return new JArrayImpl();
	}
	
	
	
	public static interface JString extends JElement {
		
		public String toJavaString();
		
	}
	
	public static JString str( String s ) {
		return new JStringImpl( s );
	}
	
	
	
	public static interface JNumber extends JElement {
		
		public Integer toJavaInteger();
		
		public Float toJavaFloat();
		
		public Double toJavaDouble();
		
		public Number toJavaNumber();
		
	}
	
	public static JNumber num( int num ) {
		return new JNumberImpl( num, 0, 0 );
	}
		
	public static JNumber dec( int num, int frac ) {
		return new JNumberImpl( num, frac, 0 );
	}
	
	public static JNumber exp( int num, int frac, int exp ) {
		return new JNumberImpl( num, frac, exp );
	}
	
	
	
	public static interface JBoolean extends JElement {
		public Boolean toJavaObject();
	}
	
	public static final JBoolean TRUE = new JBooleanImpl( true );
	
	public static final JBoolean FALSE = new JBooleanImpl( false );
	
	
	
	public static interface JNull extends JObject, JArray, JString, JNumber, JBoolean {}
	
	public static final JNull NULL = new JNullImpl();
	
	
}
