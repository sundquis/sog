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

package sog.core.xml;

import sog.core.Test;

@Test.Subject( "test." )
public abstract class XMLRepresentation<T> {
		
		protected XMLRepresentation() {}
		
		public abstract T fromString( String rep );
		
		public abstract String toString( T t );
		
//		protected T toThenFrom( T t ) {
//			return this.fromXML( this.toXML( t ) );
//		}
//		
//		protected String fromThenTo( String xml ) {
//			return this.toXML( this.fromXML( xml ) );
//		}
		
		protected String wrapEntities( String s ) {
			return s.replaceAll( "&", "&amp;" )
					.replaceAll( "<", "&lt;" )
					.replaceAll( ">", "&gt;" )
					.replaceAll( "\"", "&quot;" )
					.replaceAll( "'", "&apos;" );
		}
		
		protected String unwrapEntities( String s ) {
			return s.replaceAll( "&lt;", "<" )
					.replaceAll( "&gt;", ">" )
					.replaceAll( "&amp;", "&" )
					.replaceAll( "&quot;", "\"" )
					.replaceAll( "&apos;", "'" );
		}
		
//		protected void testMappings( T t ) {
//			System.out.println( "\n>>> ORIG: " + t );
//			System.out.println( ">>> XML:  " + this.toXML( t ) );
//			System.out.println( ">>> COMP: " + this.fromXML( this.toXML( t ) ) );
//		}
		
	}