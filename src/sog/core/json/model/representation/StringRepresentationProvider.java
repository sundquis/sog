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

package sog.core.json.model.representation;

import java.io.IOException;
import java.lang.reflect.Type;

import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Representation;
import sog.core.json.model.RepresentationProvider;

/**
 * 
 */
@Test.Subject( "test." )
public final class StringRepresentationProvider implements RepresentationProvider {
	
	public StringRepresentationProvider() {}
	
	@Override
	public Class<?> getRawType() {
		return String.class;
	}

	@Override
	public Representation<String> getRepresentationFor( Type... params ) {
		return StringRepresentationProvider.INSTANCE;
	}

	private static final Representation<String> INSTANCE = new Representation<>() {

		@Override
		public String read( PrimitiveReader reader ) throws IOException {
			return reader.readString();
		}

		@Override
		public void write( String element, PrimitiveWriter writer ) throws IOException {
			writer.writeString( element );
		}
		
	};

}
