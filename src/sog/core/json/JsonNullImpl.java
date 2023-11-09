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

package sog.core.json;

import java.util.List;
import java.util.Map;

import sog.core.Test;
import sog.core.json.JSON.JsonNull;
import sog.core.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public final class JsonNullImpl implements JsonNull {
	
	static final JsonNullImpl NULL = new JsonNullImpl();
	
	private JsonNullImpl() {}

	
	
	
	@Override
	public Map<String, JsonValue> toJavaMap() {
		return null;
	}
	
	@Override
	public List<JsonValue> toJavaList() {
		return null;
	}
	
	@Override
	public String toString() {
		return "null";
	}


}
