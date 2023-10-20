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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import sog.core.Test;
import sog.util.json.JSON.JArray;
import sog.util.json.JSON.JBoolean;
import sog.util.json.JSON.JElement;
import sog.util.json.JSON.JNumber;
import sog.util.json.JSON.JObject;
import sog.util.json.JSON.JString;

/**
 * 
 */
@Test.Subject( "test." )
public class JObjectImpl implements JSON.JObject {
	
	
	private static class JMember {
		
		private final JString key;
		private final JElement value;
		
		private JMember( JString key, JElement value ) {
			this.key = key;
			this.value = value;
		}
		
		private String toJSON() {
			return this.key.toJSON() + ": " + this.value.toJSON();
		}

		// Used only when constructing the Java map where keys are java strings
		private String getKey() {
			return this.key.toJavaString();
		}
		
		private JElement getValue() {
			return this.value;
		}
		
	}
	
	private final List<JMember> members;
	
	JObjectImpl() {
		this.members = new ArrayList<>();
	}
	
	@Override
	public JObject add( JString key, JElement value ) {
		this.members.add( new JMember( key, value ) );
		return this;
	}

	@Override
	public JObject add( String key, JElement value ) {
		this.members.add( new JMember( JSON.str( key ), value ) );
		return this;
	}

	@Override
	public Map<String, JElement> toJavaMap() {
		final Map<String, JElement> result = new TreeMap<>();
		// Currently, we silently ignore repeated keys, and keep the last encountered value
		this.members.stream().forEach( m -> result.put( m.getKey(), m.getValue() ) );
		return result;
	}
	
	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append( '{' );
		sb.append( this.members.stream().map( JMember::toJSON ).collect( Collectors.joining( ", " ) ) );
		sb.append( '}' );
		return sb.toString();
	}

	@Override
	public JObject toJObject() throws JsonIllegalCast {
		return this;
	}

	@Override
	public JArray toJArray() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Object", "JSON Array" );
	}

	@Override
	public JString toJString() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Object", "JSON String" );
	}

	@Override
	public JNumber toJNumber() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Object", "JSON Number" );
	}

	@Override
	public JBoolean toJBoolean() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Object", "JSON Boolean" );
	}

}
