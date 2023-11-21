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

package sog.core.json.model.rep;

import java.lang.reflect.Field;

import sog.core.Fatal;
import sog.core.Test;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Entity;
import sog.core.json.model.Member;
import sog.core.json.model.Model;
import sog.core.json.model.ModelException;
import sog.core.json.model.ModelRep;

/**
 * 
 */
@Test.Subject( "test." )
public class MemberRep<T> implements Comparable<MemberRep<T>> {


	private final Field field;
	
	private ModelRep<T> rep = null;
	
	private int order = 0;
	
	public MemberRep( Field field ) {
		this.field = field;
		this.field.setAccessible( true );
	}
	
	public boolean isEntity() {
		return Entity.class.isAssignableFrom( this.field.getType() );
	}
	
	public boolean isMember() {
		Member member = this.field.getDeclaredAnnotation( Member.class );
		if ( member == null ) {
			return false;
		} else {
			this.order = member.order();
			return true;
		}
	}
	
	@SuppressWarnings( "unchecked" )
	public void setRep() throws ModelException {
		this.rep = (ModelRep<T>) Model.get().repForType( this.field.getGenericType() );
	}
	
	public String getName() {
		return this.field.getName();
	}

	public Object getValue( Object instance ) {
		try {
			return this.field.get( instance );
		} catch ( Exception ex ) {
			Fatal.error( "Instance: " + instance + " does not declare field: " + this.field, ex );
			return null;
		}
	}
	
	public void readAndSet( PrimitiveReader reader, Object instance ) throws ModelException {
		try {
			this.field.set( instance, this.rep.read( reader ) );
		} catch ( Exception ex ) {
			throw new ModelException( "Error setting member: " + this.field );
		}
	}
	
	@SuppressWarnings( "unchecked" )
	public boolean write( boolean first, PrimitiveWriter writer, Object instance ) throws ModelException {
		try {
			Object value = this.field.get( instance );
			if ( value == null ) {
				return first;
			}
			if ( !first ) {
				writer.append( ',' );
			}
			writer.writeString( this.getName() );
			writer.append( ':' );
			this.rep.write( (T) value, writer );
			return false;
		} catch ( Exception ex ) {
			throw new ModelException( "Error getting member value: " + this.field );
		}
	}

	@Override
	public int compareTo( MemberRep<T> other ) {
		return this.order - other.order;
	}
	

}
