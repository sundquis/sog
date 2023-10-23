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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sog.core.App;
import sog.core.Test;
import sog.util.json.JSON.JsonArray;
import sog.util.json.JSON.JsonValue;

/**
 * 
 */
@Test.Subject( "test." )
public class JsonArrayImpl extends JsonValueImpl implements JsonArray {
	
	private final List<JsonValue> values;
	
	JsonArrayImpl() {
		this.values = new ArrayList<>();
	}

	@Override
	public JsonArray add( JsonValue element ) {
		this.values.add(  element );
		return this;
	}

	@Override
	public List<JsonValue> toJavaList() {
		return this.values;
	}
	
	@Override
	public String toString() {
		return this.toStringImpl();
	}
	
	@Override
	protected void write( BufferedWriter writer ) throws IOException {
		writer.append( '[' );
		boolean first = true;
		for ( JsonValue value : this.values ) {
			if ( first ) {
				first = false;
			} else {
				App.get().msg( "appending ," );
				writer.append( ',' );
				App.get().msg( "appended ," );
			}
			value.write( writer );
		}
		writer.append( ']' );
	}

}
