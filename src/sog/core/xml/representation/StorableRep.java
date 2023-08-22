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

package sog.core.xml.representation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import sog.core.AppRuntime;
import sog.core.Fatal;
import sog.core.Storable.Data;
import sog.core.Test;
import sog.core.xml.XMLReader;
import sog.core.xml.XMLRepresentation;
import sog.core.xml.XMLWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class StorableRep<T> extends XMLRepresentation<T> {

	private final Class<T> targetType;
	
	private final Map<String, Field> dataFields;
	
	private final ListRep<String> nameRep;
	
	public StorableRep( Class<T> targetType ) {
		this.targetType = targetType;
		this.dataFields = Arrays.stream( this.targetType.getDeclaredFields() )
			.filter( f -> f.getAnnotation( Data.class ) != null )
			.collect( Collectors.toMap( Field::getName, Function.identity() ) );
		this.nameRep = new ListRep<>( String.class );
	}


	@Override
	public String getName() {
		return "storable";
	}

	@Override
	public T fromXML( XMLReader in ) {
		T result;
		
		try {
			result = this.targetType.getDeclaredConstructor().newInstance();
		} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException | NoSuchMethodException | SecurityException e ) {
			throw new AppRuntime( "Storable classes must support a public, no-arg constructor.", e );
		}
		
		in.readOpenTag( this.getName() );

		in.readOpenTag( "class" );
		in.readContent();	// Maybe should assert same as targetType?
		in.readCloseTag( "class" );
		
		List<String> names = this.nameRep.fromXML( in );
		final T instance = result;
		names.stream()
			.map( this.dataFields::get )
			.map( f -> new FieldRep( f, instance ) )
			.forEach( f -> f.fromXML( in ) );
		
		in.readCloseTag( this.getName() );
		
		return result;
	}

	@Override
	public void toXML( T t, XMLWriter out ) {
		out.writeOpenTag( this.getName() );
		
		out.writeTag( "class", this.targetType.getName() );

		List<FieldRep> fieldsWithData = this.dataFields.values()
			.stream().map( f -> new FieldRep( f, t ) )
			.filter( FieldRep::hasData )
			.collect( Collectors.toList() );
		List<String> names = fieldsWithData.stream().map( FieldRep::getName ).collect( Collectors.toList() );
		this.nameRep.toXML( names, out );
		fieldsWithData.stream().forEach( f -> f.toXML( out ) );
		
		out.writeCloseTag( this.getName() );
	}
	
	private class FieldRep {

		private Field field;
		
		private XMLRepresentation<Object> rep;
		
		private T instance;
		
		private Object value;
		
		private FieldRep( Field field, T instance ) {
			this.field = field;
			this.field.setAccessible( true );
			this.rep = XMLRepresentation.forType( this.field.getGenericType() );
			this.instance = instance;
		}
		
		private boolean hasData() {
			try {
				this.value = this.field.get( instance );
			} catch ( IllegalArgumentException | IllegalAccessException e ) {
				Fatal.impossible( "Unable to get Storable Data field", e );
			}
			return this.value != null;
		}
		
		private String getName() {
			return this.field.getName();
		}
		
		private void toXML( XMLWriter out ) {
			this.rep.toXML( this.value, out );
		}
		
		private void fromXML( XMLReader in ) {
			try {
				this.field.set( this.instance, this.rep.fromXML( in ) );
			} catch ( IllegalArgumentException | IllegalAccessException e ) {
				Fatal.impossible( "Unable to set Storable Data field", e );
			}
		}
	}

}
