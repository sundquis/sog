/**
 * Copyright (C) 2021
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

package sog.core;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sog.core.xml.XMLHandler;

/**
 * Usage:
 * 		public T myT = Property.get( name, default, parser );
 * 
 * This class is one of the lowest level classes in the application framework. Any client call to 
 * App or Property results in a call to XMLHandler to load the root system xml configuration file. 
 * 
 * The location of the configuration file is determined here based on the required environment variable 
 * "system.dir", which must name an absolute path to the directory containing the configuration file.
 * It is typically set as a JVM arg, for example:
 * 		-Dsystem.dir="/.../"
 * 
 * The name of the configuration file is determined by the optional environment variable "system.xml"
 * which defaults to "system.xml" but can be overridden. 
 * 
 * To avoid circular class-loading dependencies this class should not have any dependencies on other 
 * core classes. It does depend on the XMLHandler class.
 */
@Test.Subject( "test." )
public class Property {
	
	// FIXME:
	// Revert. Replace parsers, Property is a standalone singleton
	// Reads System properties
	// Provides properties statically including app.root
	
	/** Retrieve a configurable property from the system property file */
	@Test.Decl( "Throws AssertionError for null name" )
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Throws AssertionError for null parser" )
	@Test.Decl( "Retrieves properties for top level classes" )
	@Test.Decl( "Retrieves properties for nested classes" )
	@Test.Decl( "Retrieves properties for double nested classes" )
	@Test.Decl( "Retrieves properties for anonymous classes" )
	@Test.Decl( "Retrieves properties for local classes" )
	@Test.Decl( "Prints declaration for missing property" )
	@Test.Decl( "Last value for repeated elements" )
	@Test.Decl( "Uses default for missing" )
	public static <T> T get( String name, T defaultValue, Parser<T> parser ) {
		Assert.nonEmpty( name );
		Assert.nonNull( parser );

		String className = Property.getClassName();
		String key = className + "." + name;
		String stringValue = Property.getInstance().getStringValue( key );

		if ( stringValue == null ) {
			System.err.println( "WARNING: Property not found:" );
			System.err.println( "<class fullname=\"" + className + "\">" );
			System.err.println( "<property name=\"" + name + "\" value=\"" + defaultValue + "\" />" );
			System.err.println( "</class>" );
		}
		
		return stringValue == null ? defaultValue : parser.value( stringValue );
	}
	
	/** Retrieve a configurable block of text from the system property file */
	@Test.Decl( "Throws AssertionError for null name" )
	@Test.Decl( "Throws AssertionError for empty name" )
	@Test.Decl( "Retrieves text for top level classes" )
	@Test.Decl( "Retrieves text for nested classes" )
	@Test.Decl( "Retrieves text for double nested classes" )
	@Test.Decl( "Retrieves text for anonymous classes" )
	@Test.Decl( "Retrieves text for local classes" )
	@Test.Decl( "Prints declaration for missing property" )
	@Test.Decl( "Can retrieve empty" )
	@Test.Decl( "Can use property name" )
	@Test.Decl( "Last value for multiple elements" )
	public static String getText( String name ) {
		Assert.nonEmpty( name );
		
		String className = Property.getClassName();
		String key = className + "." + name;
		String value = Property.getInstance().getTextValue( key );
		
		if ( value == null ) {
			value = "";
			System.err.println( "WARNING: Text not found:" );
			System.err.println( "<class fullname=\"" + className + "\">" );
			System.err.println( "<text name=\"" + name + "\">Text value</text>" );
			System.err.println( "</class>" );
		}
		
		return value;
	}
	
    private static String getClassName() {
        // FRAGILE:
        StackTraceElement[] stackTrace = (new Exception()).getStackTrace();
        // Stack:
        //              Property.getClassName
        //              Property.get/getText
        //              <class declaring a property>
        Assert.isTrue( stackTrace.length > 2 );
        
        // This original regular expression was not documented and seems wrong
        //String className = stackTrace[2].getClassName().replaceAll( "\\D*\\d+[_a-zA-Z]*$", "" );

        // Replaced with expression that matches inner class names that contain "$n"
        String className = stackTrace[2].getClassName().replaceAll( "\\$\\d+[_a-zA-Z$]*$", "" );
        return Assert.nonEmpty( className );
}
	
	

	

	// MOVE to constructor, non-static, with checks
	
	// The system.dir property must be defined, for example as a JVM arg:
	//     -Dsystem.dir=...
	// The property must be an absolute path to the directory serving as the root for the application
	public static final String SYSTEM_DIR = System.getProperty( "system.dir", "FATAL" );

	private static final String SYSTEM_NAME = "system.xml";

	
	
	private boolean loaded = false;
	
	private Map<String, String> values = null;
	
	private Map<String, String> text = null;

	private Property() {}
	
	
	// move to constructor
	private void ensureLoaded() {
		if ( ! this.loaded ) {
			synchronized ( Property.class ) {
				if ( ! this.loaded ) {
					this.values = new TreeMap<>();
					this.text = new TreeMap<>();
					this.load();
					this.loaded = true;
				}
			}
		}
	}

	private String getStringValue( String key ) {
		this.ensureLoaded();
		
		return this.values.get( key );
	}
	
	private String getTextValue( String key ) {
		this.ensureLoaded();
		
		return this.text.get( key );
	}

	private void load() {
		Path path = Paths.get( Property.SYSTEM_DIR, Property.SYSTEM_NAME );
		Assert.readableFile( path );

		try {
			XMLHandler handler = new SystemHandler( path );
			handler.parse();
		} catch ( IOException e ) {
			Fatal.impossible( "Assert should make this impossible",  e );
		}
	}
	
	// Tied to the structure defined in system.dtd
	@Test.Skip( "FIXME" )
	private class SystemHandler extends XMLHandler {
		
		private String currentClassName = null;
		
		private String currentTextKey = null;
		
		private StringBuilder buf = null;

		public SystemHandler( Path path ) throws IOException {
			super( path );
			this.buf = new StringBuilder();
		}
		
		@Override
		public void startElement( String name, Map<String, String> attributes ) {
			Assert.nonEmpty( name );
			Assert.nonNull( attributes );

			if ( name.equals( "class" ) ) {
				this.currentClassName = Assert.nonEmpty( attributes.get( "fullname" ) );
			}
			
			if ( name.equals( "property" ) ) {
				String propName = Assert.nonEmpty( attributes.get( "name" ) );
				String key = this.currentClassName + "." + propName;
				String value = Assert.nonEmpty( attributes.get( "value" ) );
				Property.this.values.put( key,  value );
			}
			
			if ( name.equals( "text" ) ) {
				String propName = Assert.nonEmpty( attributes.get( "name" ) );
				this.currentTextKey = this.currentClassName + "." + propName;
				this.buf.setLength( 0 );
			}
		}
		
		@Override
		public void endElement( String name ) {
			Assert.nonEmpty( name );
			
			if ( name.equals( "text" ) ) {
				Assert.nonEmpty( this.currentTextKey );
				
				Property.this.text.put( this.currentTextKey,  this.buf.toString() );
			}
		}

		@Override
		public void characters( char[] ch, int start, int length ) {
			this.buf.append( ch, start, length );
		}
	}
	


}
