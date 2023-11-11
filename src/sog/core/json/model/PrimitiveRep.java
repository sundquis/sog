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

package sog.core.json.model;

import java.io.IOException;
import java.util.Date;

import sog.core.Test;
import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Model.Rep;

/**
 * 
 */
@Test.Subject( "test." )
public enum PrimitiveRep {

	DATE( Date.class, new Rep<Date>() {

		@Override public Date read( PrimitiveReader reader ) throws IOException, JsonException {
			return new Date( reader.readNumber().longValueExact() );
		}

		@Override public void write( Date t, PrimitiveWriter writer ) throws IOException {
			writer.writeNumber( t.getTime() );
		}
		
	}),
	
	LONG( Long.class, new Rep<Long>() {

			@Override
			public Long read( PrimitiveReader reader ) throws IOException, JsonException {
				return reader.readNumber().longValueExact();
			}

			@Override
			public void write( Long t, PrimitiveWriter writer ) throws IOException {
				writer.writeNumber( t );
			}
			
	}),
	
	BOOLEAN( Boolean.class, new Rep<Boolean>() {

			@Override
			public Boolean read( PrimitiveReader reader ) throws IOException, JsonException {
				return reader.readBoolean();
			}

			@Override
			public void write( Boolean t, PrimitiveWriter writer ) throws IOException {
				writer.writeBoolean( t );
			}
			
	}),
	
	DOUBLE( Double.class, new Rep<Double>() {

			@Override
			public Double read( PrimitiveReader reader ) throws IOException, JsonException {
				return reader.readNumber().doubleValue();
			}

			@Override
			public void write( Double t, PrimitiveWriter writer ) throws IOException {
				writer.writeNumber( t );
			}
			
	}),
	
	INTEGER( Integer.class, new Rep<Integer>() {

			@Override
			public Integer read( PrimitiveReader reader ) throws IOException, JsonException {
				return reader.readNumber().intValueExact();
			}

			@Override
			public void write( Integer t, PrimitiveWriter writer ) throws IOException {
				writer.writeNumber( t );
			}
			
	}),
	
	STRING( String.class, new Rep<String>() {
			
			@Override public String read( PrimitiveReader reader ) throws IOException, JsonException {
				return reader.readString();
			}

			@Override public void write( String t, PrimitiveWriter writer ) throws IOException {
				writer.writeString( t );
			}

	})
	
	;
	
	private Class<?> rawType;
	private Rep<?> rep;
	
	private PrimitiveRep( Class<?> rawType, Rep<?> rep ) {
		this.rawType = rawType;
		this.rep = rep;
	}
	
	public Class<?> getRawType() { return this.rawType; }
	
	public Rep<?> getRep() { return this.rep; }


}
