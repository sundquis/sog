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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;

import sog.core.Test;
import sog.core.json.Model;
import sog.core.json.Model.Structure;
import sog.core.json.PrimitiveWriter;

/**
 * 
 */
@Test.Subject( "test." )
public class ModelWriter extends PrimitiveWriter {
	
	public ModelWriter( BufferedWriter buf ) {
		super( buf );
	}
	
	public ModelWriter( Writer writer ) {
		super( writer );
	}
	
	public ModelWriter( OutputStream out ) {
		super( out );
	}

	
	
	public void writeStructure( Structure s ) throws IOException {
		this.append( '{' );

		boolean first = true;
		Field[] fields = s.getClass().getDeclaredFields();
		for ( Field field : fields ) {
			Member member = new Member( s, field );
			if ( member.hasData() ) {
				if ( first ) {
					first = false;
				} else {
					this.append( ',' );
				}
				
			}
		}
		
		this.append( '}' );
	}
	
	private class Member {
		
		private final Structure instance;
		
		private final Field field;
		
		private Object value;
		
		Member( Structure instance, Field field ) {
			this.instance = instance;
			this.field = field;
		}
		
		boolean hasData() {
			if ( this.field.getDeclaredAnnotation( Model.Member.class ) == null ) {
				return false;
			}
			
			try {
				this.value = this.field.get( this.instance );
			} catch ( IllegalArgumentException | IllegalAccessException e ) {
				this.value = null;
			}
			return this.value != null;
		}
		
	}

}
