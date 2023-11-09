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
import java.util.HashMap;
import java.util.Map;

import sog.core.Test;
import sog.core.json.JsonException;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class Rep<T> {
	
	@SuppressWarnings( "unchecked" )
	public static <S> Rep<S> forClass( Class<S> clazz ) throws ModelException {
		Rep<S> result = (Rep<S>) CLASS_TO_REP.get( clazz );
		if ( result == null ) {
			throw new ModelException( "No representation registered for " + clazz );
		}
		return result;
	}

	private static final Map<Class<?>, Rep<?>> CLASS_TO_REP = new HashMap<>();
	
	private static void register( Rep<?> rep ) {
		CLASS_TO_REP.put( rep.getTargetType(), rep );
	}

	/*
	 * Use private member classes to implement Rep's then register them here.
	 */
	static {
		Rep.register( new StringRep() );
		Rep.register( new IntegerRep() );
		Rep.register( new DoubleRep() );
		Rep.register( new BooleanRep() );
		Rep.register( new LongRep() );
		Rep.register( new DateRep() );
	}

	

	public abstract Class<T> getTargetType();
	
	public abstract T read( ModelReader reader ) throws IOException, JsonException;
	
	public abstract void write( T t, ModelWriter writer ) throws IOException;

	
	
	private static class DateRep extends Rep<Date> {

		@Override public Class<Date> getTargetType() {
			return Date.class;
		}

		@Override
		public Date read( ModelReader reader ) throws IOException, JsonException {
			return new Date( reader.readNumber().longValueExact() );
		}

		@Override
		public void write( Date t, ModelWriter writer ) throws IOException {
			writer.writeNumber( t.getTime() );
		}
		
	}
	
	
	private static class LongRep extends Rep<Long> {

		@Override public Class<Long> getTargetType() {
			return Long.class;
		}

		@Override
		public Long read( ModelReader reader ) throws IOException, JsonException {
			return reader.readNumber().longValueExact();
		}

		@Override
		public void write( Long t, ModelWriter writer ) throws IOException {
			writer.writeNumber( t );
		}
		
	}
	
	
	private static class BooleanRep extends Rep<Boolean> {

		@Override public Class<Boolean> getTargetType() {
			return Boolean.class;
		}

		@Override
		public Boolean read( ModelReader reader ) throws IOException, JsonException {
			return reader.readBoolean();
		}

		@Override
		public void write( Boolean t, ModelWriter writer ) throws IOException {
			writer.writeBoolean( t );
		}
		
	}
	
	
	private static class DoubleRep extends Rep<Double> {

		@Override public Class<Double> getTargetType() {
			return Double.class;
		}

		@Override
		public Double read( ModelReader reader ) throws IOException, JsonException {
			return reader.readNumber().doubleValue();
		}

		@Override
		public void write( Double t, ModelWriter writer ) throws IOException {
			writer.writeNumber( t );
		}
		
	}
	
	
	private static class IntegerRep extends Rep<Integer> {

		@Override public Class<Integer> getTargetType() {
			return Integer.class;
		}

		@Override
		public Integer read( ModelReader reader ) throws IOException, JsonException {
			return reader.readNumber().intValueExact();
		}

		@Override
		public void write( Integer t, ModelWriter writer ) throws IOException {
			writer.writeNumber( t );
		}
		
	}

	
	private static class StringRep extends Rep<String> {

		@Override public Class<String> getTargetType() {
			return String.class;
		}
		
		@Override public String read( ModelReader reader ) throws IOException, JsonException {
			return reader.readString();
		}

		@Override public void write( String t, ModelWriter writer ) throws IOException {
			writer.writeString( t );
		}

	}
	
	
}
