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


import sog.core.Assert;
import sog.core.Property;
import sog.core.Test;

/**
 * Static help with xml
 */
@Test.Subject( "test." )
public class XML {

	private static XML instance = null;

	/** Retrieve the singleton instance */
	@Test.Decl( "Is not null" )
	public static XML get() {
		if ( XML.instance == null ) {
			synchronized ( XML.class ) {
				if ( XML.instance == null ) {
					XML.instance = new XML();
				}
			}
		}
		
		return Assert.nonNull( XML.instance );
	}
	
	private final String declaration;
	
	private XML() {
		this.declaration = Property.getText( "declaration" );
	}
	
	@Test.Decl( "Not empty" )
	@Test.Decl( "Starts correct" )
	@Test.Decl( "Specifies version" )
	@Test.Decl( "Specifies encoding" )
	public String getDeclaration() {
		return this.declaration;
	}
	
	
	public enum Entity {
		
		@Test.Skip( "Enumerated value" )
		AMP( "&", "&amp;"),
		
		@Test.Skip( "Enumerated value" )
		LT( "<", "&lt;" );
		
		private final String entity;
		private final String replacement;
		
		private Entity( String entity, String replacement ) {
			this.entity = entity;
			this.replacement = replacement;
		}

		@Test.Decl( "Throws AssertionError for null string" )
		@Test.Decl( "Ampersand is encoded" )
		@Test.Decl( "Less than is encoded" )
		public static String encode( String s ) {
			String result = Assert.nonNull( s );
			
			for ( Entity e : Entity.values() ) {
				result = result.replace( e.entity, e.replacement );
			}
			
			return result;
		}
		
		@Test.Decl( "Throws AssertionError for null string" )
		@Test.Decl( "Ampersand is decoded" )
		@Test.Decl( "Less than is decoded" )
		public static String decode( String s ) {
			String result = Assert.nonNull( s );
			
			for ( Entity e : Entity.values() ) {
				result = result.replace( e.replacement, e.entity );
			}
			
			return result;
		}
	}


	

	@Test.Skip( "Simple static functions" )
	public interface Helpers {
		
		default public String tagStart( String tag ) {
			return "<" + tag + ">";
		}
		
		default public String tagEnd( String tag ) {
			return "</" + tag + ">";
		}
		
		default public String encodeEntities( String s ) {
			return Entity.encode( s );
		}
		
		default public String decodeEntities( String s ) {
			return Entity.decode( s );
		}
		
	}

	
}
