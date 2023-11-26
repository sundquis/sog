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
public class BooleanRepresentationProvider implements RepresentationProvider {
	
	public BooleanRepresentationProvider() {}

	@Override
	public Class<?> getRawType() {
		return Boolean.class;
	}

	@Override
	public Representation<?> getRepresentationFor( Type... params ) {
		return BooleanRepresentationProvider.INSTANCE;
	}

	
	private static final Representation<Boolean> INSTANCE = new Representation<>() {

		@Override
		public Boolean read( PrimitiveReader reader ) throws IOException {
			return reader.readBoolean();
		}

		@Override
		public void write( Boolean element, PrimitiveWriter writer ) throws IOException {
			writer.writeBoolean( element );
		}
		
	};

}
