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

import java.lang.reflect.Field;

import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Entity;
import sog.core.json.model.Member;
import sog.core.json.model.ModelException;
import sog.core.json.model.Representation;
import sog.core.json.model.Representations;

/**
 * 
 */
@Test.Subject( "test." )
public class StructureMember<T> {

	private final Field field;
	
	private Representation<T> fieldRep = null;
	
	@SuppressWarnings( "unchecked" )
	public StructureMember( Field field ) {
		if ( Entity.class.isAssignableFrom( field.getType() ) ) {
			throw new ModelException( "Structure declares Entity member: " + field );
		}
		
		this.field = field;
		this.field.setAccessible( true );
		if ( this.field.getDeclaredAnnotation( Member.class ) != null ) {
			this.fieldRep = (Representation<T>) Representations.get().forField( this.field );
		}
	}
	
	public boolean isMember() {
		return this.fieldRep != null;
	}
	
	public String getName() {
		return this.field.getName();
	}

	public Object getValue( Object instance ) {
		try {
			return this.field.get( instance );
		} catch ( Exception ex ) {
			throw new ModelException( "Instance: " + instance + " does not declare field: " + this.field, ex );
		}
	}
	
	public void readAndSet( PrimitiveReader reader, Object instance ) throws ModelException {
		try {
			this.field.set( instance, this.fieldRep.read( reader ) );
		} catch ( Exception ex ) {
			throw new ModelException( "Error setting member: " + this.field );
		}
	}
	
	@SuppressWarnings( "unchecked" )
	public boolean write( boolean first, PrimitiveWriter writer, Object instance ) throws ModelException {
		try {
			T value = (T) this.field.get( instance );
			if ( value == null ) {
				return first;
			}
			if ( !first ) {
				writer.append( ',' );
			}
			writer.writeString( this.getName() );
			writer.append( ':' );
			this.fieldRep.write( value, writer );
			return false;
		} catch ( Exception ex ) {
			throw new ModelException( "Error getting member value: " + this.field );
		}
	}


}
