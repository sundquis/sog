/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package sog.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
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
import sog.core.TestOrig;

/**
 * @author sundquis
 *
 */
public class XMLHandler implements ContentHandler, ErrorHandler, DeclHandler, LexicalHandler {

	private final InputSource source;
	private Locator locator;

	/**
	 * Construct a handler that will process xml from the given {@code InputSource}.
	 * 
	 * @param source
	 */
	@TestOrig.Decl( "Throws assertion error for null source" )
	public XMLHandler( InputSource source ) {
		this.source = Assert.nonNull( source );
	}

	/**
	 * Construct a handler that will process xml from the given {@code Reader}.
	 * 
	 * @param reader
	 */
	@TestOrig.Decl( "Throws assertion error for null reader" )
	public XMLHandler( Reader reader ) {
		this( new InputSource( Assert.nonNull( reader ) ) );
	}

	/**
	 * Construct a handler that will process xml from the given {@code InputStream}.
	 * 
	 * @param reader
	 */
	@TestOrig.Decl( "Throws assertion error for null stream" )
	public XMLHandler( InputStream stream ) {
		this( new InputSource( Assert.nonNull( stream ) ) );
	}
	
	/**
	 * Construct a handler that will process xml from the given {@code Path}.
	 * 
	 * @param reader
	 */
	@TestOrig.Decl( "Throws assertion error for null path" )
	@TestOrig.Decl( "Throws NoSuchFileException if the file is missing" )
	public XMLHandler( Path path ) throws IOException {
		this( Files.newInputStream( Assert.nonNull( path ) ) );
	}

	
	
	/**
	 * Parse the document provided by the ({@code InputSource}
	 * This {@code XMLHandler} will receive parsing events
	 */
	@TestOrig.Skip( "Extensively exercised in other tests" )
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
			e.printStackTrace();
		}
	}

	/**
	 * Get the current location of a parsing event. Used in a event callback to get
	 * an approximation to the location in the xml file. If the row and/or
	 * column is not known the value -1 is used.
	 * 
	 * @return
	 */
	@TestOrig.Decl( "Is not null" )
	@TestOrig.Decl( "Location is unknown before parsing" )
	@TestOrig.Decl( "Location is unknown after parsing" )
	public Location getLocation() {
		return new Location();
	}

	@TestOrig.Skip( "Tested extensively in event-based tests" )
	public class Location {
		
		private final int line;
		private final int col;
		
		private Location() {
			this.line = XMLHandler.this.locator == null ? -1 : XMLHandler.this.locator.getLineNumber();
			this.col = XMLHandler.this.locator == null ? -1 : XMLHandler.this.locator.getColumnNumber();
		}
		
		public int getLineNumber() { return this.line; }
		public int getColumnNumber() { return this.col; }
		@Override public String toString() { return "(" + this.line + ", " + this.col + ")"; }
	}


	
	// Content Handler

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	@TestOrig.Decl( "Parser registers non-null locator" )
	@TestOrig.Decl( "Called before startDocument()" )
	public void setDocumentLocator( Locator locator ) {
		this.locator = locator;
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	public void startDocument() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	@TestOrig.Decl( "Called after startDocument()" )
	@TestOrig.Decl( "Location is identified" )
	public void endDocument() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Skip( "Namespaces are disabled" )
	public void startPrefixMapping( String prefix, String uri ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	@TestOrig.Skip( "Namespaces are disabled" )
	public void endPrefixMapping( String prefix ) throws SAXException {
	}

	/**
	 * Convenience method to convert between SAX-based {@code Attributes} structure
	 * and a {@code java.util.Map}
	 * 
	 * @param atts
	 * @return
	 */
	@TestOrig.Decl( "Result is not null" )
	@TestOrig.Decl( "Empty map returned when no attributes" )
	public static Map<String, String> attributesToMap( Attributes atts ) {
		Map<String, String> result = atts.getLength() > 0 ? new TreeMap<>() : XMLHandler.EMPTY_MAP;
		
		for ( int i = 0; i < atts.getLength(); i++ ) {
			result.put( atts.getQName(i),  atts.getValue(i) );
		}
		
		return result;
	}
	
	private static Map<String, String> EMPTY_MAP = new TreeMap<>();

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	@TestOrig.Decl( "Parser signals start of element processing" )
	@TestOrig.Decl( "qName is not null" )
	@TestOrig.Decl( "Attributes is not null" )
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
	@TestOrig.Decl( "Name is not empty" )
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Value correct for required attribute" )
	@TestOrig.Decl( "Value correct for implied attribute" )
	@TestOrig.Decl( "Value correct for fixed attribute" )
	@TestOrig.Decl( "Value correct for enumerated" )
	public void startElement( String name, Map<String, String> attributes ) {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "qName is not empty" )
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
	@TestOrig.Decl( "Name is not empty" )
	@TestOrig.Decl( "Elements closed in LIFO order" )
	@TestOrig.Decl( "Location is identified" )
	public void endElement( String name ) {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	@TestOrig.Decl( "Parser uses to signal content" )
	@TestOrig.Decl( "Location is identified" )
	public void characters( char[] ch, int start, int length ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Target is correct" )
	@TestOrig.Decl( "Data is correct" )
	public void processingInstruction( String target, String data ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	@TestOrig.Skip( "Failed to trigger this event" )
	public void skippedEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	@TestOrig.Skip( "Failed to trigger this event" )
	public void warning( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	@TestOrig.Decl( "Trigered for missing DTD" )
	@TestOrig.Decl( "Trigered when element does not match declared type" )
	@TestOrig.Decl( "Trigered for undeclared elements" )
	@TestOrig.Decl( "Trigered for undeclared attributes" )
	@TestOrig.Decl( "Trigered for missing requiured attribute" )
	@TestOrig.Decl( "Trigered for changing #FIXED attribute" )
	@TestOrig.Decl( "Location is identified" )
	public void error( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	@TestOrig.Decl( "Trigered for illegal DTD structure" )
	@TestOrig.Decl( "Trigered for undeclared entity" )
	@TestOrig.Decl( "Excpetion is thrown after signaling" )
	@TestOrig.Decl( "Location is identified" )
	public void fatalError( SAXParseException exception ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#elementDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Name is not empty" )
	public void elementDecl( String name, String model ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Element name is not empty" )
	@TestOrig.Decl( "Attribute name is not empty" )
	@TestOrig.Decl( "Type is correct" )
	@TestOrig.Decl( "Mode is correct" )
	@TestOrig.Decl( "Value is correct when supplied in DTD" )
	public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#internalEntityDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Name is correct" )
	@TestOrig.Decl( "Value is correct" )
	public void internalEntityDecl( String name, String value ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#externalEntityDecl(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Skip( "Not using external entities" )
	public void externalEntityDecl( String name, String publicId, String systemId ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Name is root element" )
	public void startDTD( String name, String publicId, String systemId ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endDTD()
	 */
	@Override
	@TestOrig.Decl( "Called after startDTD()" )
	@TestOrig.Decl( "Location is identified" )
	public void endDTD() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Parser signals use of entity" )
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Name is correct" )
	public void startEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
	 */
	@Override
	@TestOrig.Decl( "Called after startEntity()" )
	@TestOrig.Decl( "Location is identified" )
	@TestOrig.Decl( "Name is consistent" )
	public void endEntity( String name ) throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	@Override
	@TestOrig.Decl( "Parser signals start of CDATA" )
	@TestOrig.Decl( "Location is identified" )
	public void startCDATA() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	@Override
	@TestOrig.Decl( "Called after startCDATA()" )
	@TestOrig.Decl( "Location is identified" )
	public void endCDATA() throws SAXException {
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
	 */
	@Override
	@TestOrig.Decl( "Parser signals comments" )
	@TestOrig.Decl( "Location is identified" )
	public void comment( char[] ch, int start, int length ) throws SAXException {
	}

		
	
}
