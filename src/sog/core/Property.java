/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import sog.core.xml.XMLHandler;

/**
 * Usage:
 * 		public T myT = Property.get( name, default, parser );
 * 
 * An application must define the single System property: "system.dir"
 * This must be a path to a directory containing the xml property file "system.xml"
 * 
 */
public class Property {

	// FOR DEVELOPMENT ONLY
	private static final String DEFAULT_SYSTEM_DIR = "/home/sundquis/book/Dropbox/java/projects/DEV";
	
	private static final String SYSTEM_DIR = System.getProperty( "system.dir", DEFAULT_SYSTEM_DIR );
	
	private static final String SYSTEM_NAME = "system.xml";

	private static final Property INSTANCE = new Property();
	
	
	/** Retrieve a configurable property from the system property file */
	@Test.Decl( "Throws assertion error for null name" )
	@Test.Decl( "Throws assertion error for empty name" )
	@Test.Decl( "throws assertion error for null parser" )
	@Test.Decl( "Retrieves properties for top level classes" )
	@Test.Decl( "Retrieves properties for nested classes" )
	@Test.Decl( "Retrieves properties for double nested classes" )
	@Test.Decl( "Throws assertion error for anonymous classes" )
	@Test.Decl( "Throws assertion error for local classs" )
	@Test.Decl( "Prints declaration for missing property" )
	@Test.Decl( "Last value for multiple elements" )
	@Test.Decl( "Uses default for missing" )
	@Test.Decl( "Throws exception for malformed integer" )
	@Test.Decl( "Throws exception for malformed long" )
	public static <T> T get( String name, T defaultValue, Function<String, T> parser ) {
		Assert.nonEmpty( name );
		Assert.nonNull( parser );

		String className = Property.getClassName();
		String key = className + "." + name;
		String stringValue = Property.INSTANCE.getStringValue( key );
		
		if ( stringValue == null ) {
			System.err.println( "WARNING: Property not found:" );
			System.err.println( "<class fullname=\"" + className + "\">" );
			System.err.println( "<property name=\"" + name + "\" value=\"" + defaultValue + "\" />" );
			System.err.println( "</class>" );
		}
		
		return stringValue == null ? defaultValue : parser.apply( stringValue );
	}
	
	/** Retrieve a configurable block of text from the system property file */
	@Test.Decl( "Throws assertion error for null name" )
	@Test.Decl( "Throws assertion error for empty name" )
	@Test.Decl( "Retrieves text for top level classes" )
	@Test.Decl( "Retrieves text for nested classes" )
	@Test.Decl( "Retrieves text for double nested classes" )
	@Test.Decl( "Throws assertion error for anonymous classes" )
	@Test.Decl( "Throws assertion error for local classs" )
	@Test.Decl( "Prints declaration for missing property" )
	@Test.Decl( "Can retrieve empty" )
	@Test.Decl( "Can use property name" )
	@Test.Decl( "Last value for multiple elements" )
	public static String getText( String name ) {
		Assert.nonEmpty( name );
		
		String className = Property.getClassName();
		String key = className + "." + name;
		String value = Property.INSTANCE.getTextValue( key );
		
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
		//		Property.getClassName
		//		Property.get/getText
		//		<class declaring a property>
		Assert.isTrue( stackTrace.length > 2 );
		String className = stackTrace[2].getClassName().replaceAll( "\\D*\\d+[_a-zA-Z]*$", "" );
		return Assert.nonEmpty( className );
	}
	
	// Convenience parsers. Add as needed.
	@Test.Skip
	public static final Function<String, Integer> INTEGER = (s) -> Integer.parseInt(s);

	@Test.Skip
	public static final Function<String, Long> LONG = (s) -> Long.parseLong(s);
	
	@Test.Skip
	public static final Function<String, Boolean> BOOLEAN = (s) -> Boolean.parseBoolean(s);
	
	@Test.Skip
	public static final Function<String, String> STRING = (s) -> s;

	@Test.Decl( "Collection of common cases" )
	@Test.Decl( "Array of length one allowed" )
	@Test.Decl( "Empty array allowed" )
	@Test.Decl( "White space after comman ignored" )
	public static final Function<String, String[]> CSV = (s) -> s.split( ",\\s*" );
	
	@Test.Skip
	public static final Function<String, List<String>> LIST = (s) -> Arrays.asList( CSV.apply(s));
	

	

	
	private boolean loaded = false;
	
	private Map<String, String> values = null;
	
	private Map<String, String> text = null;

	private Property() {}
	
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
	private class SystemHandler extends XMLHandler {
		
		private String currentClassName = null;
		
		private String currentTextKey = null;
		
		private StringBuilder buf = null;

		public SystemHandler( Path path ) throws IOException {
			super( path );
			this.buf = new StringBuilder();
		}
		
		@Test.Skip
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
		
		@Test.Skip
		@Override
		public void endElement( String name ) {
			Assert.nonEmpty( name );
			
			if ( name.equals( "text" ) ) {
				Assert.nonEmpty( this.currentTextKey );
				
				Property.this.text.put( this.currentTextKey,  this.buf.toString() );
			}
		}

		@Test.Skip
		@Override
		public void characters( char[] ch, int start, int length ) {
			this.buf.append( ch, start, length );
		}
	}
	

	
		

}
