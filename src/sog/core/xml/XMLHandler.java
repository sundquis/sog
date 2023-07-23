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
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

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

import sog.core.App;
import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Strings;
import sog.core.Test;
import sog.util.StreamReader;

/**
 * Provides default no-op implementations of the SAX xml parsing call-backs, and minimizes
 * application contact with the SAX api.
 */
@Test.Subject( value = "test.", threadsafe = false )
public class XMLHandler implements ContentHandler, ErrorHandler, DeclHandler, LexicalHandler {

	
	private final InputSource source;
	private Locator locator = null;
	
	private boolean showContentEvents = false;
	private boolean showErrorEvents = false;
	private boolean showLexicalEvents = false;
	private boolean showDeclarationEvents = false;

	/**
	 * Construct a handler that will process xml from the given {@code InputSource}.
	 * 
	 * @param source
	 */
	@Test.Decl( "Throws AssertionError for null source" )
	public XMLHandler( InputSource source ) {
		this.source = Assert.nonNull( source );
	}
	
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
	 * Construct a handler that will process xml from the given stream of strings.
	 * @param stream
	 */
	@Test.Decl( "Throws AssertionError for null stream" )
	public XMLHandler( Stream<String> stream ) {
		this( new StreamReader( Assert.nonNull( stream ) ) );
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
	 * Construct a handler that will process xml from the given {@code Path},
	 * an absolute Path to the xml file. The SystemId is set to the root
	 * location, so relative paths in the file will be properly resolved.
	 * 
	 * @param reader
	 */
	@Test.Decl( "Throws AssertionError for null path" )
	@Test.Decl( "Throws AppRuntime if the file is missing" )
	@Test.Decl( "External DTD is read" )
	public XMLHandler( Path path ) {
		try {
			this.source = new InputSource( Files.newInputStream( Assert.nonNull( path ) ) );
			this.source.setSystemId( path.toUri().toString() );
		} catch ( IOException ex ) {
			throw new AppRuntime( ex );
		}
	}
	

	@Test.Decl( "Enables feedback for content events" )
	@Test.Decl( "Returns this XMLHandler instance to allow chaining" )
	public XMLHandler showContentEvents() {
		this.showContentEvents = true;
		return this;
	}

	@Test.Decl( "Enables feedback for error events" )
	@Test.Decl( "Returns this XMLHandler instance to allow chaining" )
	public XMLHandler showErrorEvents() {
		this.showErrorEvents = true;
		return this;
	}

	@Test.Decl( "Enables feedback for lexical (DTD) events" )
	@Test.Decl( "Returns this XMLHandler instance to allow chaining" )
	public XMLHandler showLexicalEvents() {
		this.showLexicalEvents = true;
		return this;
	}

	@Test.Decl( "Enables feedback for declaration events" )
	@Test.Decl( "Returns this XMLHandler instance to allow chaining" )
	public XMLHandler showDeclarationEvents() {
		this.showDeclarationEvents = true;
		return this;
	}


	/**
	 * Parse the document provided by the ({@code InputSource}
	 * This {@code XMLHandler} will receive parsing events
	 */
	@Test.Decl( "Throws AppRuntime when IOException encountered reading xml" )
	public void parse() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating( true );
			factory.setNamespaceAware( false );

			SAXParser saxp = factory.newSAXParser();
			
			XMLReader reader = saxp.getXMLReader();
			reader.setContentHandler( this );
			reader.setErrorHandler( this );
			reader.setProperty( "http://xml.org/sax/properties/declaration-handler", this );
			reader.setProperty( "http://xml.org/sax/properties/lexical-handler", this );
			reader.parse( this.source );
		} catch ( ParserConfigurationException e ) {
			Fatal.error( "Failed to configure parser", e );
		} catch ( SAXException e ) {
			Fatal.error( "Unknown SAXException", e );
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
		
		private final String publicId;
		private final String systemId;
		private final int line;
		private final int col;
		
		private Location( Locator locator ) {
			this.publicId = (locator == null || locator.getPublicId() == null) ? "unknown" : locator.getPublicId();
			this.systemId = (locator == null || locator.getPublicId() == null) ? "unknown" : locator.getSystemId();
			this.line = locator == null ? -1 : locator.getLineNumber();
			this.col = locator == null ? -1 : locator.getColumnNumber();
		}
		
		@Test.Decl( "Agrees with value supplied to the InputSource" )
		public String getPublicId() {
			return this.publicId;
		}
		
		@Test.Decl( "Agrees with value supplied to the InputSource" )
		public String getSystemId() {
			return this.systemId;
		}
		
		@Test.Decl( "Greater or equal to zero while parsing" )
		@Test.Decl( "Return is -1 when unknown" )
		public int getLineNumber() { return this.line; }
		
		@Test.Decl( "Greater or equal to zero while parsing" )
		@Test.Decl( "Return is -1 when unknown" )
		public int getColumnNumber() { return this.col; }
		
		@Override 
		@Test.Decl( "Indicates row and column while parsing" )
		public String toString() { return "(" + this.line + ", " + this.col + ")"; }

		@Test.Decl( "False while parsing" )
		public boolean unknown() {
			return (this.line == -1) || (this.col == -1);
		}
	}

	/**
	 * In verbose mode, callbacks use this to report parsing details.
	 * Detail objects are assumed to be in pairs: (label, details)
	 * 
	 * @param details
	 */
	@Test.Decl( "Throws AssertionError for null details" )
	@Test.Decl( "Throws AssertionError for odd number of details" )
	@Test.Decl( "Detail objects may be null" )
	@Test.Decl( "Details treated as (label, detail) pairs" )
	public void out( Object... details ) {
		Assert.nonNull( details );
		Assert.isTrue( 2 * (details.length/2) == details.length, "Odd number of details provided" );
		System.err.println( ">>> " + this.getLocation() );
		for ( int i = 0; i < details.length; ) {
			System.err.println( "\t" + Strings.toString( details[i++] ) + ": " + Strings.toString( details[i++] ) );
		}
	}


	
	// Content Handler

	@Override
	@Test.Decl( "Parser registers non-null locator" )
	@Test.Decl( "Called before startDocument()" )
	public void setDocumentLocator( Locator locator ) {
		this.locator = locator;
		if ( this.showContentEvents ) {
			this.out( "Parser set Locator", this.getLocation().getSystemId() );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void startDocument() throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Document started", this.getLocation().getSystemId() );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	@Test.Decl( "Called after startDocument()" )
	@Test.Decl( "Location is unknown" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void endDocument() throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Document complete", this.getLocation().getSystemId() );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate prefix mapping" )
	public void startPrefixMapping( String prefix, String uri ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Prefix mapping started", this.locator.getSystemId(), "Prefix", prefix, "URI", uri );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate prefix mapping" )
	public void endPrefixMapping( String prefix ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Prefix mapping complete", this.locator.getSystemId(), "Prefix", prefix );
		}
	}

	/**
	 * Convenience method to convert between SAX-based {@code Attributes} structure
	 * and a {@code java.util.Map}
	 * 
	 * @param atts
	 * @return
	 */
	@Test.Decl( "Empty map returned when no attributes" )
	@Test.Decl( "All keys are present" )
	@Test.Decl( "Values are correct" )
	public static Map<String, String> attributesToMap( Attributes atts ) {
		Map<String, String> result = atts.getLength() > 0 ? new TreeMap<>() : Collections.emptyMap();
		
		for ( int i = 0; i < atts.getLength(); i++ ) {
			result.put( atts.getQName(i), atts.getValue(i) );
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	@Test.Decl( "qName is not empty" )
	@Test.Decl( "Attributes is not null" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
		this.startElement( qName,  attributesToMap( atts ) );
		if ( this.showContentEvents ) {
			this.out( "Element started", qName, "Local name", localName, "URI", uri, "Has attrubutes", (atts != null && atts.getLength() > 0) );
		}
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
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void startElement( String name, Map<String, String> attributes ) {
		if ( this.showContentEvents ) {
			this.out( name + " attributes", attributes );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "uri can be empty" )
	@Test.Decl( "localName can be empty" )
	@Test.Decl( "qName is not empty" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void endElement( String uri, String localName, String qName ) throws SAXException {
		this.endElement( qName );
		if ( this.showContentEvents ) {
			this.out( "Element complete", qName );
		}
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
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void characters( char[] ch, int start, int length ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Character data found", length );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Ignorable whitespace found", length );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Target is correct" )
	@Test.Decl( "Data is correct" )
	@Test.Decl( "Feedback provided if showContentEvents() has been called" )
	public void processingInstruction( String target, String data ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Processing instruction", " ", "Target", target, "Data", data );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate" )
	public void skippedEntity( String name ) throws SAXException {
		if ( this.showContentEvents ) {
			this.out( "Skipped entity", name );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Decl( "Unable to trigger..." )
	public void warning( SAXParseException exception ) {
		if ( this.showErrorEvents ) {
			this.out( "SAX Warning", exception );
			App.get().getLocationMatching( exception, "^sog.*|^test.*" ).map( s -> "\t" + s ).forEach( System.err::println );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Decl( "Trigered for missing doctype" )
	@Test.Decl( "Trigered when element does not match declared type" )
	@Test.Decl( "Trigered for undeclared elements" )
	@Test.Decl( "Trigered for undeclared attributes" )
	@Test.Decl( "Trigered for missing requiured attribute" )
	@Test.Decl( "Trigered for changing #FIXED attribute" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showErrorEvents() has been called" )
	@Test.Decl( "Prints error message and stack trace" )
	public void error( SAXParseException exception ) {
		if ( this.showErrorEvents ) {
			this.out( "SAX Error", exception );
			App.get().getLocationMatching( exception, "^sog.*|^test.*" ).map( s -> "\t" + s ).forEach( System.err::println );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	@Test.Decl( "Trigered for illegal DTD structure" )
	@Test.Decl( "Trigered for undeclared entity" )
	@Test.Decl( "AppRuntime is thrown after signaling" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Prints fatal message and stack trace" )
	@Test.Decl( "Feedback provided if showErrorEvents() has been called" )
	public void fatalError( SAXParseException exception ) throws AppRuntime {
		if ( this.showErrorEvents ) {
			this.out( "SAX Fatal Error", exception );
			App.get().getLocationMatching( exception, "^sog.*|^test.*" ).map( s -> "\t" + s ).forEach( System.err::println );
		}
		throw new AppRuntime( exception );
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#elementDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is not empty" )
	@Test.Decl( "Model is correct" )
	@Test.Decl( "Feedback provided if showDeclarationEvents() has been called" )
	public void elementDecl( String name, String model ) throws SAXException {
		if ( this.showDeclarationEvents ) {
			this.out( "Element declaration", " ", "Name", name, "Model", model );
		}
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
	@Test.Decl( "Feedback provided if showDeclarationEvents() has been called" )
	public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
		if ( this.showDeclarationEvents ) {
			this.out( "Attribute declaration", " ", "eName", eName, "aName", aName, "Type", type, "Mode", mode, "Value", value );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#internalEntityDecl(java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is correct" )
	@Test.Decl( "Value is correct" )
	@Test.Decl( "Feedback provided if showDeclarationEvents() has been called" )
	public void internalEntityDecl( String name, String value ) throws SAXException {
		if ( this.showDeclarationEvents ) {
			this.out( "Entity declaration", " ", "Name", name, "Value", value );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#externalEntityDecl(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Skip( "FIXME: Investigate" )
	public void externalEntityDecl( String name, String publicId, String systemId ) throws SAXException {
		if ( this.showDeclarationEvents ) {
			this.out( "External entity declaration", " ", "Name", name, "PublicId", publicId, "SystemId", systemId );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is root element" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void startDTD( String name, String publicId, String systemId ) throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "DTD started", name, "PublicId", publicId, "SystemId", systemId );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endDTD()
	 */
	@Override
	@Test.Decl( "Called after startDTD()" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void endDTD() throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "DTD complete", " " );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
	 */
	@Override
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is correct" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void startEntity( String name ) throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "Entity started", name );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
	 */
	@Override
	@Test.Decl( "Called after startEntity()" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Name is consistent" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void endEntity( String name ) throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "Entity complete", name );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	@Override
	@Test.Decl( "Parser signals start of CDATA AFTER characters" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void startCDATA() throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "CDATA started", " " );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	@Override
	@Test.Decl( "Called after startCDATA()" )
	@Test.Decl( "Parser signals end of CDATA BEFORE last characters call" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void endCDATA() throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "CDATA complete", " " );
		}
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
	 */
	@Override
	@Test.Decl( "Parser signals comments" )
	@Test.Decl( "Location is identified" )
	@Test.Decl( "Feedback provided if showLexicalEvents() has been called" )
	public void comment( char[] ch, int start, int length ) throws SAXException {
		if ( this.showLexicalEvents ) {
			this.out( "Comment", new String( ch ) );
		}
	}

		
	
}
