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

package sog.core;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * which defaults to "system.xml" but can be changed by setting the JVM arg system.name.
 * 
 * To avoid circular class-loading dependencies this class should not have any dependencies on other 
 * core classes. It does depend on the XMLHandler class.
 */
@Test.Subject( "test." )
public class Property extends XMLHandler {
	

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
		
		return stringValue == null ? defaultValue : parser.fromString( stringValue );
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
        
        // Convert name of anonymous class to be the enclosing class, so strip off the $nn..."
        String className = stackTrace[2].getClassName().replaceAll( "\\$\\d+[_a-zA-Z$]*$", "" );
        
        return Assert.nonEmpty( className );
    }
 

	private static Property instance = null;
	
	
	private static Property getInstance() {
		if ( Property.instance == null ) {
			synchronized ( Property.class ) {
				if ( Property.instance == null ) {
					Property.makeInstance();
				}
			}
		}
		
		return Assert.nonNull( Property.instance );
	}
	

	private static final String ERR_MSG = ""
		+ "\n\nThe system property 'system.dir' must be set to an absolute path to the directory continaing\n"
		+ "the properties file. Typically this is set as a JVM arg, for example:\n"
		+ "\t-Dsystem.dir=/home/user/apps/sog/\n";
	
	
	@Test.Decl( "Prints instructions when system property file not found" )
	private static void makeInstance() {
		String systemDir = System.getProperty( "system.dir" );
		if ( systemDir == null || systemDir.isEmpty() ) {
			Fatal.error( ERR_MSG );
		}
		String systemName = System.getProperty( "system.name", "system.xml" );
		
		Path path = Paths.get( systemDir, systemName );
		Assert.readableFile( path );
		
		try {
			Property.instance = new Property( path );
		} catch ( IOException ex ) {
			Fatal.error( "Unable to open system properties", ex );
		}
	}
	
	
	
	

	/* Holds property values */
	private final Map<String, String> values = new TreeMap<>();
	
	/* Holds text values */
	private final Map<String, String> text = new TreeMap<>();

	/* Used during the initial parse */
	private String currentClassName = null;
	
	/* Used during the initial parse */
	private String currentTextKey = null;
	
	/* Used during the initial parse */
	private StringBuilder buf = new StringBuilder();
	
	private Property( Path path ) throws IOException {
		super( path );
		
		this.parse();
	}

	/*
	 * This method overrides the noop implementation inherited from XMLHandler.
	 * 
	 * The DTD specifies that there are three types of elements that can be encountered.
	 * 
	 * The top-level elements are <code>class</code> elements that contain nested configuration information.
	 * When a <code>class</code> element is encountered we record the class name (<code>fullname</code>)
	 * to be used to generate keys.
	 * 
	 * A nested <code>property</code> element signals a <code>(name, value)</code> association, which is
	 * stored in the <code>properties</code> map.
	 * 
	 * A nested <code>text</code> element can have content (CDATA). To prepare for character data, we
	 * record the key that will be used to store the data, and clear out the buffer that will be
	 * used to store characters.
	 */
	@Override
	@Test.Decl("Throws AssertionError for empty name" )
	@Test.Decl("Throws AssertionError for null attributes" )
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
			this.values.put( key, value );
		}
		
		if ( name.equals( "text" ) ) {
			String propName = Assert.nonEmpty( attributes.get( "name" ) );
			this.currentTextKey = this.currentClassName + "." + propName;
			this.buf.setLength( 0 );
		}
	}

	/*
	 * The end of a <code>text</code> element signals that the character data is complete, so
	 * we capture and store the contents of the buffer.
	 */
	@Override
	@Test.Decl( "Text elements terminated" )
	public void endElement( String name ) {
		Assert.nonEmpty( name );
		
		if ( name.equals( "text" ) ) {
			Assert.nonEmpty( this.currentTextKey );
			
			Property.this.text.put( this.currentTextKey,  this.buf.toString() );
		}
	}

	/*
	 * Character data gets appended to the buffer.
	 */
	@Override
	@Test.Decl( "Characters added" )
	public void characters( char[] ch, int start, int length ) {
		this.buf.append( ch, start, length );
	}


	private String getStringValue( String key ) {
		return this.values.get( key );
	}
	
	private String getTextValue( String key ) {
		return this.text.get( key );
	}


}
