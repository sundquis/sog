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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import sog.core.Test;
import sog.core.json.JsonException;
import sog.core.json.PrimitiveReader;
import sog.core.json.PrimitiveWriter;
import sog.core.json.model.Entity;
import sog.core.json.model.ModelException;
import sog.core.json.model.ModelRep;

/**
 * 
 */
@Test.Subject( "test." )
public class EntityRep<E extends Entity> implements ModelRep<E> {
	
	/*
	 * We only need one instance per concrete Structure class. Save instances here:
	 */
	private static final Map<String, EntityRep<?>> typeToRep = new TreeMap<>();
	
	@SuppressWarnings( "unchecked" )
	public static synchronized <F extends Entity> EntityRep<F> forClass( Class<F> clazz ) {
		String typeName = clazz.getCanonicalName();
		EntityRep<F> result = (EntityRep<F>) EntityRep.typeToRep.get( typeName );
		if ( result == null ) {
			result = new EntityRep<>( clazz );
			EntityRep.typeToRep.put( typeName, result );
		}
		return result;
	}
	
	
	private final Class<E> entityClass;
	
	private EntityRep( Class<E> entityClass ) {
		this.entityClass = entityClass;
	}

	@Override
	public E read( PrimitiveReader reader ) throws IOException, JsonException, ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write( E t, PrimitiveWriter writer ) throws IOException, ModelException {
		// TODO Auto-generated method stub
		
	}


}
