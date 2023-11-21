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

import sog.core.Fatal;
import sog.core.Test;

/**
 * JSON Model Manager
 * 
 * TODO:
 * Code generation get/set from Member
 * "Compiler"?
 * + Verify PKey instance
 */
@Test.Subject( "test." )
public class JMM {
	
	private static JMM INSTANCE = null;
	
	public static JMM getManager() {
		if ( JMM.INSTANCE == null ) {
			synchronized ( JMM.class ) {
				if ( JMM.INSTANCE == null ) {
					JMM.INSTANCE = new JMM();
				}
			}
		}
		
		return JMM.INSTANCE;
	}
	
	
	private JMM() {}
	
	public <E extends Entity> E findEntity( PKey<E> key ) {
		Fatal.unimplemented( "" );
		return null;
	}
	
	public <E extends Entity> E getEntity( ID<E> id ) {
		Fatal.unimplemented( "" );
		return null;
	}


}
