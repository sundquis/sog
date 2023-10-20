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
public class JArrayImpl implements JArray {
	
	private final List<JElement> elements;
	
	JArrayImpl() {
		this.elements = new ArrayList<>();
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append( '[' );
		sb.append( this.elements.stream().map( JElement::toJSON ).collect( Collectors.joining( ", " ) ) );
		sb.append( ']' );
		return sb.toString();
	}

	@Override
	public JArray add( JElement element ) {
		this.elements.add(  element );
		return this;
	}

	@Override
	public List<JElement> toJavaList() {
		return this.elements;
	}

	@Override
	public JObject toJObject() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Array", "JSON Object" );
	}

	@Override
	public JArray toJArray() throws JsonIllegalCast {
		return this;
	}

	@Override
	public JString toJString() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Array", "JSON String" );
	}

	@Override
	public JNumber toJNumber() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Array", "JSON Number" );
	}

	@Override
	public JBoolean toJBoolean() throws JsonIllegalCast {
		throw new JsonIllegalCast( "JSON Array", "JSON Boolean" );
	}

}
