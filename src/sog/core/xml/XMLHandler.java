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

package sog.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Property;
import sog.core.Test;

/**
 * Provides default no-op implementations of the SAX xml parsing call-backs, and minimizes
 * application contact with the SAX api.
 */
@Test.Subject( "test." )
public class XMLHandler implements ContentHandler, ErrorHandler, DeclHandler, LexicalHandler {

	
	private final InputSource source;
	private Locator locator;

	/**
	 * Construct a handler that will process xml from the given {@code InputSource}.
	 * 
	 * @param source
	 */
	@Test.Decl( "Throws AssertionError for null source" )
	public XMLHandler( InputSource source ) {
		this.source = Assert.nonNull( source );
	}
	
	// FIXME
	// Move Property.SYSTEM_DIR here
	// Set publicId here to SYSTEM_DIR
	// Alter constructor from path. If relative then use SYSTEM_DIR

	/**
	 * Construct a handler that will process xml from the given {@code Reader}.
	 * 
	 * @param reader
	 */
	@Test.Decl( "Throws AssertionError for null reader" )
	public XMLHandler( Reader reader ) {
		this( new InputSource( Assert.nonNull( reader ) ) );
	}

	/**
	 * Construct a handler that will process xml from the given {@code InputStream}.
	 * 
	 * @param reader
	 */
	@Test.Decl( "Throws AssertionError for null stream" )
	public XMLHandler( InputStream stream ) {
		this( new InputSource( Assert.nonNull( stream ) ) );
	}
	
	/**
	 * Construct a handler that will process xml from the given {@code Path}.
	 * 
	 * @param reader
	 */
	@Test.Decl( "Throws AssertionError for null path" )
	@Test.Decl( "Throws NoSuchFileException if the file is missing" )
	public XMLHandler( Path path ) throws IOException {
		this( Files.newInputStream( Assert.nonNull( path ) ) );
		//this.source.setSystemId( "file://localhost" + path.toString() );
		//this.source.setSystemId( "FOO" );
		//System.out.println(">>> Property.SYSTEM = " + Property.SYSTEM_DIR);
		this.source.setSystemId( Property.SYSTEM_DIR + "/" );
	} //"file://localhost" + 

	// FIXME:
	// additional constructor with two paths, one a root dir set to the SystemId
	
	
	/**
	 * Parse the document provided by the ({@code InputSource}
	 * This {@code XMLHandler} will receive parsing events
	 */
	@Test.Decl( "Throws AppException when IOException encountered reading xml" )
	public void parse() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating( true );
			factory.setNamespaceAware( false );

			SAXParser saxp = factory.newSAXParser();
			
			XMLReader reader = saxp.getXMLReader();
			reader.setContentHandler( this );
			reader.setErrorHandler( this );
			reader.setProperty( "http://xml.org/sax/properties/declaration-handler",  this );
			reader.setProperty( "http://xml.org/sax/properties/lexical-handler",  this );
			reader.parse( this.source );
		} catch ( ParserConfigurationException e ) {
			Fatal.error( "Failed to configure parser", e );
		} catch ( SAXException e ) {
			Fatal.error( "Failed to configure parser", e );
		} catch ( IOException e ) { // parse
			Fatal.error( "Error while parsing source", e );
		}
	}

	/**
	 * Get the current location of a parsing event. Used in a event callback to get
	 * an approximation to the location in the xml file. If the row and/or
	 * column is not known the value -1 is used.
	 * 
	 * @return
	 */
	@Test.Decl( "Is not null" )
	@Test.Decl( "Location is unknown before parsing" )
	@Test.Decl( "Location is unknown after parsing" )
	public Location getLocation() {
		return new Location( this.locator );
	}

	/**
	 * SAX sets the Locator while parsing. This class creates a snapshot of the current 
	 * location information, if known.
	 */
	public class Location {
		
		private String publicId = "unknown";
		private String systemId = "unknown";
		private int line = -1;
		private int col = -1;
		
		private Location( Locator locator ) {
			if ( locator != null ) {
				if ( locator.getPublicId() != null ) {
					this.publicId = locator.getPublicId();
				}
				if ( locator.getSystemId() != null ) {
					this.systemId = locator.getSystemId();
				}
				this.line = locator.getLineNumber();
				this.col = locator.getColumnNumber();
			}
		}
		
		@Test.Decl( "Is not empty" )
		@Test.Decl( "Apparently always unknown for SAX parser?" )
		public String getPublicId() {
			return this.publicId;
		}
		
		@Test.Decl( "Is not empty" )
		@Test.Decl( "Apparently always unknown for SAX parser?" )
		public String getSystemId() {
			return this.systemId;
		}
		
		@Test.Decl( "Greater or equal to zero while parsing" )
		@Test.Decl( "Return is -1 when unknown" )
		public int getLineNumber() { return this.line; }
		
		@Test.Decl( "Greater or equal to zero while parsing" )
		@Test.Decl( "Return is -1 when unknown" )
		public int getColumnNumber() { return this.col; }
		
		@Test.Decl( "Not empty" )
		@Test.Decl( "Indicates row and column while parsing" )
		@Override public String toString() { return "(" + this.line + ", " + this.col + ")"; }
	}


	
	// Content Handler

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	@Test.Decl( "Parser registers non-null locator" )
	@Test.Decl( "Called before startDocument()" )
	public void setDocumentLocator( Locator locator ) {
		this.locator = locator;
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	@Test.Decl( "Location is identified" )
	public void startDocument() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	@Test.Decl( "Called after startDocument()" )
	@Test.Decl( "Location is identified" )
	public void endDocument() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate prefix mapping" )
	public void startPrefixMapping( String prefix, String uri ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate prefix mapping" )
	public void endPrefixMapping( String prefix ) throws SAXException {
	}

	/**
	 * Convenience method to convert between SAX-based {@code Attributes} structure
	 * and a {@code java.util.Map}
	 * 
	 * @param atts
	 * @return
	 */
	@Test.Decl( "Result is not null" )
	@Test.Decl( "Empty map returned when no attributes" )
	@Test.Decl( "All keys are present" )
	@Test.Decl( "Values are correct" )
	public static Map<String, String> attributesToMap( Attributes atts ) {
		Map<String, String> result = atts.getLength() > 0 ? new TreeMap<>() : Collections.emptyMap();
		
		for ( int i = 0; i < atts.getLength(); i++ ) {
			result.put( atts.getQName(i),  atts.getValue(i) );
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	@Test.Decl( "Parser signals start of element processing" )
	@Test.Decl( "uri is not empty" )
	@Test.Decl( "localName is not empty" )
	@Test.Decl( "qName is not empty" )
	@Test.Decl( "Attributes is not null" )
	public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
		this.startElement( qName,  attributesToMap( atts ) );
	}

	/**
	 * Convenience call-back to replace SAX {@code Attributes} with a {@code Map}.
	 * Default implementation re-routes 
	 * {@code org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)} to this method.
	 * CAUTION: If {@code org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)}
	 * is overridden then {@code XMLHandler#startElement(String, Map)} will not be called (unless super() is used.)
	 * If namespaces are used then {@code name} is the prefix-qualified name {@code qName}.
	 * 
	 * @param name
	 * @param attributes
	 */
	@Test.Decl( "Name is not empty" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Value correct for required attribute" )
	@Test.Decl( "Value correct for implied attribute" )
	@Test.Decl( "Value correct for fixed attribute" )
	@Test.Decl( "Value correct for enumerated attribute" )
	public void startElement( String name, Map<String, String> attributes ) {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "uri is not empty" )
	@Test.Decl( "localName is not empty" )
	@Test.Decl( "qName is not empty" )
	public void endElement( String uri, String localName, String qName ) throws SAXException {
		this.endElement( qName );
	}
	
	/**
	 * Convenience call-back to be consistent with the name choice used by
	 * {@code XMLHandler#startElement(String, Map)}.
	 * Default implementation re-routes 
	 * {@code org.xml.sax.ContentHandler#endElement(String, String, String)} to this method.
	 * CAUTION: If {@code org.xml.sax.ContentHandler#endElement(String, String, String)}
	 * is overridden then {@code XMLHandler#endElement(String)} will not be called (unless super() is used.)
	 * If namespaces are used then {@code name} is the prefix-qualified name {@code qName}.
	 * 
	 * @param name
	 */
	@Test.Decl( "Name is not empty" )
	@Test.Decl( "Elements closed in LIFO order" )
	@Test.Decl( "Location is identified" )
	public void endElement( String name ) {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	@Test.Decl( "Parser uses to signal content" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "All content registered before endElement" )
	public void characters( char[] ch, int start, int length ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Target is correct" )
	@Test.Decl( "Data is correct" )
	public void processingInstruction( String target, String data ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate" )
	public void skippedEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Skip( "Unable to trigger..." )
	public void warning( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Decl( "Trigered for missing DTD" )
	@Test.Decl( "Trigered when element does not match declared type" )
	@Test.Decl( "Trigered for undeclared elements" )
	@Test.Decl( "Trigered for undeclared attributes" )
	@Test.Decl( "Trigered for missing requiured attribute" )
	@Test.Decl( "Trigered for changing #FIXED attribute" )
	@Test.Decl( "Location is identified" )
	public void error( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Decl( "Trigered for illegal DTD structure" )
	@Test.Decl( "Trigered for undeclared entity" )
	@Test.Decl( "Excpetion is thrown after signaling" )
	@Test.Decl( "Location is identified" )
	public void fatalError( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#elementDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is not empty" )
	public void elementDecl( String name, String model ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Element name is not empty" )
	@Test.Decl( "Attribute name is not empty" )
	@Test.Decl( "Type is correct" )
	@Test.Decl( "Mode is correct" )
	@Test.Decl( "Value is correct when supplied in DTD" )
	public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#internalEntityDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is correct" )
	@Test.Decl( "Value is correct" )
	public void internalEntityDecl( String name, String value ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#externalEntityDecl(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate" )
	public void externalEntityDecl( String name, String publicId, String systemId ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is root element" )
	public void startDTD( String name, String publicId, String systemId ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endDTD()
	 */
	@Override
	@Test.Decl( "Called after startDTD()" )
	@Test.Decl( "Location is identified" )
	public void endDTD() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
	 */
	@Override
	@Test.Decl( "Parser signals use of entity" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is correct" )
	public void startEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
	 */
	@Override
	@Test.Decl( "Called after startEntity()" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is consistent" )
	public void endEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	@Override
	@Test.Decl( "Parser signals start of CDATA before characters" )
	@Test.Decl( "Location is identified" )
	public void startCDATA() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	@Override
	@Test.Decl( "Called after startCDATA()" )
	@Test.Decl( "Parser signals end of CDATA after characters" )
	@Test.Decl( "Location is identified" )
	public void endCDATA() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
	 */
	@Override
	@Test.Decl( "Parser signals comments" )
	@Test.Decl( "Location is identified" )
	public void comment( char[] ch, int start, int length ) throws SAXException {
	}

		
	
}
