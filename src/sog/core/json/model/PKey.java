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

import java.util.Objects;

import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Test;
import sog.core.json.model.representation.StructureMember;
import sog.core.json.model.representation.StructureRepresentationProvider;

/**
 * 
 */
@Test.Subject( "test." )
public abstract class PKey<E extends Entity> implements Structure, Comparable<PKey<E>> {
	
	private ID<E> myID = null;
	
	//private StructureRepresentationProvider<PKey<E>> rep = null;
	
	private int hashCode = 0;
	
	@SuppressWarnings( "unchecked" )
	protected PKey() {
		try {
			//this.rep = (StructureRepresentationProvider<PKey<E>>) StructureRepresentationProvider.forType( this.getClass() );
		} catch ( ModelException ex ) {
			Fatal.error( "No representation of PKey: " + this.getClass(), ex );
		}
	}
	
	ID<E> getID() {
		return this.myID;
	}
	
	void setID( ID<E> id ) {
		Assert.isNull( this.myID );
		this.myID = id;
	}
	
	/**
	 * WARNING: If the concrete PKey<E> class does not define any Member fields then
	 * there is only one PKey<E> instance, an empty key, equal to all other keys.
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public int compareTo( PKey<E> other ) {
		int result = 0;
//		for ( StructureMember<?> member : this.rep.getMembers() ) {
//			Object thisValue = member.getValue( this );
//			Object otherValue = member.getValue( other );
//			if ( thisValue instanceof Comparable c1 ) {
//				if ( otherValue instanceof Comparable c2 ) {
//					result = c1.compareTo( c2 );
//				} else {
//					Fatal.impossible( "Two values of the same field have the same type." );
//				}
//			} else {
//				result = this.hashCode() - other.hashCode();
//			}
//			
//			if ( result != 0 ) {
//				break;
//			}
//		}
		
		return result;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public boolean equals( Object other ) {	
		if ( other == null ) return false;
		if ( this == other ) return true;
		if ( ! this.getClass().equals( other.getClass() ) ) return false;
		
		return this.compareTo( (PKey<E>) other ) == 0;
	}

	@Override
	public int hashCode() {
		if ( this.hashCode == 0 ) {
			//this.hashCode = Objects.hash( this.rep.getMembers().stream().map( (mr) -> mr.getValue( this ) ).toArray() );
		}
		return this.hashCode;
	}

	
	
}
