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


import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sog.core.Assert;
import sog.core.Property;
import sog.core.Test;
import sog.core.xml.data.XMLSimpleDataManager;

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
	

	@Documented
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD, ElementType.TYPE } )
	/**
	 * A marker annotation indicating that the target class or field can be represented
	 * as XML data and stored.
	 */
	public @interface Data {}
	
	/**
	 * Operations pertaining to persistent data stored as xml.
	 */
	public interface DataManager {
		
		/**
		 * Retrieve stored persistent values for the given non-null object.
		 * 
		 * The given object becomes the manager...
		 * Read xml...
		 * 
		 * @param obj
		 */
		public void load( Object obj ) throws IOException;
		
		/**
		 * Store an xml representation of the persistent data for the given non-null object.
		 * 
		 * Fields marked XML.Data
		 * Only if obj called load
		 * 
		 * @param obj
		 */
		public void store( Object obj ) throws IOException;
		
		/**
		 * Same as load without store capability
		 * @param obj
		 */
		public void copy( Object obj ) throws IOException;
		
	}
	
	public DataManager dataManager() {
		return XMLSimpleDataManager.get();
	}
	
	
	public static void main( String[] args ) {
		String orig = "&lt;class&gt;";
		String replaced = orig.replaceAll( "", "" );
		System.out.println( ">>> ORIGINAL: " + orig );
		System.out.println( ">>> REPLACED: " + replaced );
		
		System.out.println( "\nDone!" );
	}
	
}
