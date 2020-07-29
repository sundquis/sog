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
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
//import org.xml.sax.helpers.XMLReaderFactory;

import sog.core.Fatal;
import sog.core.Strings;
import sog.core.Test;
import sog.util.Commented;
import sog.util.StreamReader;

/**
 * Demo showing parser configuration, handler implementations, usage examples, and lessons learned (comments)
 * 
 * @author sundquis
 *
 */
@Test.Skip
public class XMLDEMO implements ContentHandler, DTDHandler, ErrorHandler, DeclHandler, LexicalHandler {
	
	private InputSource source;
	private static Locator locator = null;
	
	public XMLDEMO( InputSource source ) {
		this.source = source;
	}
	
	public XMLDEMO( Reader reader ) {
		this( new InputSource( reader ) );
	}
	
	public XMLDEMO( InputStream stream ) {
		this( new InputSource( stream ) );
	}
	
	public XMLDEMO( Path path ) throws IOException {
		this( Files.newInputStream( path ) );
	}
	
	public XMLDEMO( Stream<String> stream ) {
		this( new StreamReader( stream ) );
	}
	
	public void parse() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating( true );
			// If using schema turn on. DTD does not work well (at all) with namespace
			factory.setNamespaceAware( false );

			SAXParser saxp = factory.newSAXParser();
			
			XMLReader reader = saxp.getXMLReader();
			reader.setContentHandler( this ); // Need it
			reader.setDTDHandler( this ); // No current use
			reader.setErrorHandler( this ); // Need it
			reader.setProperty( "http://xml.org/sax/properties/declaration-handler",  this ); // Inside DTD
			reader.setProperty( "http://xml.org/sax/properties/lexical-handler",  this ); // CDATA, comments, entity, DTD
			reader.parse(source);
			
			// TODO: investigate xml-string property while parsing
			
		} catch (ParserConfigurationException e) { // Setting up factory
			e.printStackTrace();
		} catch (SAXException e) { // Setting up factory
			e.printStackTrace();
		} catch (IOException e) { // parse
			e.printStackTrace();
		}
	}
	
	// Exploring configuration options
	public void config() {
		try {
			out( "" );
			SAXParserFactory factory = SAXParserFactory.newInstance();
			out( "Factory", factory );
	
			out( "" );
			SAXParser saxp = factory.newSAXParser();
			out( "SAX Parser", saxp );
			out( "Parser", saxp.getParser() );
			out( "Namespace aware", saxp.isNamespaceAware() );
			out( "Is validating", saxp.isValidating() );
			out( "Schema", saxp.getSchema() );
			out( "Decl Handler", saxp.getProperty( "http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", saxp.getProperty( "http://xml.org/sax/properties/lexical-handler" ) );

			out( "" );
			XMLReader reader = saxp.getXMLReader();
			out( "XMLReader", reader );
			out( "Decl Handler", reader.getProperty("http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", reader.getProperty("http://xml.org/sax/properties/lexical-handler" ) );
			out( "DTD Handler", reader.getDTDHandler() );
			out( "Content Handler", reader.getContentHandler() );
			out( "Error Handler", reader.getErrorHandler() );
			out( "Unconfigured parser done" );
			
			out( "" );
			out( "" );
			factory = SAXParserFactory.newInstance();
			factory.setValidating( true );
			factory.setNamespaceAware( true );
			out( "Factory", factory );
	
			out( "" );
			saxp = factory.newSAXParser();
			out( "SAX Parser", saxp );
			out( "Parser", saxp.getParser() );
			out( "Namespace aware", saxp.isNamespaceAware() );
			out( "Is validating", saxp.isValidating() );
			out( "Schema", saxp.getSchema() );
			out( "Decl Handler", saxp.getProperty( "http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", saxp.getProperty( "http://xml.org/sax/properties/lexical-handler" ) );

			out( "" );
			reader = saxp.getXMLReader();
			reader.setContentHandler( this );
			reader.setDTDHandler( this );
			reader.setErrorHandler( this );
			reader.setProperty( "http://xml.org/sax/properties/declaration-handler",  this );
			reader.setProperty( "http://xml.org/sax/properties/lexical-handler",  this );
			out( "XMLReader", reader );
			out( "Decl Handler", reader.getProperty("http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", reader.getProperty("http://xml.org/sax/properties/lexical-handler" ) );
			out( "DTD Handler", reader.getDTDHandler() );
			out( "Content Handler", reader.getContentHandler() );
			out( "Error Handler", reader.getErrorHandler() );
			out( "Configured parser done" );
			/*
			out( "" );
			out( "" );
			reader = XMLReaderFactory.createXMLReader();
			out( "XMLReader", reader );
			out( "Decl Handler", reader.getProperty("http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", reader.getProperty("http://xml.org/sax/properties/lexical-handler" ) );
			out( "DTD Handler", reader.getDTDHandler() );
			out( "Content Handler", reader.getContentHandler() );
			out( "Error Handler", reader.getErrorHandler() );
			
			out( "" );
			reader.setContentHandler( this );
			reader.setDTDHandler( this );
			reader.setErrorHandler( this );
			reader.setProperty( "http://xml.org/sax/properties/declaration-handler",  this );
			reader.setProperty( "http://xml.org/sax/properties/lexical-handler",  this );
			out( "Decl Handler", reader.getProperty("http://xml.org/sax/properties/declaration-handler" ) );
			out( "Lex Handler", reader.getProperty("http://xml.org/sax/properties/lexical-handler" ) );
			out( "DTD Handler", reader.getDTDHandler() );
			out( "Content Handler", reader.getContentHandler() );
			out( "Error Handler", reader.getErrorHandler() );
			*/
			
		} catch ( Exception e ) {
			Fatal.error( "SAX parsing failed", e );
		}
	}
	
	
	private static void out( String msg, Object ... args ) {
		System.out.println( ">>> " + msg + (args.length > 0 ? ": " + Strings.toString( args ) : "") );
	}

	private static void out( int n, String msg, Object ... args ) {
		String pos = locator == null ? "(unknown)" : ("(" + locator.getLineNumber() + ", " + locator.getColumnNumber()) + ")";
		out( pos + " " + n + ". " + msg, args );
	}
	
	private static void out( Attributes atts ) {
		for ( int i = 0; i < atts.getLength(); i++ ) {
			String s = "    "
			// URI
					+ atts.getURI(i)
			// LocalName
					+ ", " + atts.getLocalName(i)
			// QName
					+ ", " + atts.getQName(i)
			// Type
					+ ", " + atts.getType(i)
			// Value
					+ ", " + atts.getValue(i);
			System.out.println( s );
		}
	}

	public static void main(String[] args) throws IOException {

		System.out.println();
		
		new XMLDEMO( (new Commented() {}).getCommentedLines( "XML" ) ).parse();
		
		// NS-EX	<root xmlns="app:/s/c/p">
		
		// XML	<?xml version="1.0" encoding="UTF-8"?>
		// XML	<!DOCTYPE root [
		// XML	<!ENTITY tss "Thomas Scott Sundquist">
		// XML	<!ELEMENT root (child)* >
		// XML	<!-- A comment in the DTD -->
		// XML	<!ELEMENT child (#PCDATA) >
		// XML	<?indtd a="b" c="d" no validation error but no event fired?>
		// XML	<!ATTLIST child
		// XML		k1	CDATA	#REQUIRED
		// XML		k2	CDATA	#IMPLIED
		// XML		k3	CDATA	#FIXED	"foo"
		// XML	>
		// XML	]>
		// XML	<?between a="b" c="d" ?>
		// XML	
		// XML	<!-- A comment before the doc -->
		// XML	<root>
		// XML	<?indoc a="b" c="d" ?>
		// XML	<child k1="v1" />
		// XML	<child k1 = "v1">Text inside a child element with <![CDATA[Some unparsed <tag>tagged</tag> data]]></child>
		// XML	<child k1="v1" k2="v2">&tss;</child>
		// XML	<!-- A comment in the doc -->
		// XML	<child k1="v1" k2="v2" />
		// XML	</root>
		// XML	

		System.out.println("\nDone!");

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		out( 1, "setDocumentLocator" );
		XMLDEMO.locator = locator;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		out( 2, "startDocument" );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		out( 3, "endDocument" );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		// With namespaceAware = true have the following cases for uri, local, qName
		// No namespace declared: "", local = qName
		// Default ns, no prefix: uri, local = qName
		// Declared ns, no prefix: "", local = qName
		// Declared ns with prefix: uri, local, pefix:local
		out( 4, "startElement", uri, localName, qName );
		out( atts );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		out( 5, "endElement", uri, localName, qName );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		out( 6, "characters" );
		out( new String(ch, start, length) );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		out( 7, "ignorableWhitespace" );
		out( new String(ch, start, length) );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		out( 8, "processingInstruction", target, data );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.DTDHandler#notationDecl(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void notationDecl(String name, String publicId, String systemId) throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.DTDHandler#unparsedEntityDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
			throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	@Override
	public void warning(SAXParseException exception) throws SAXException {
		// Failed to get warning using illegal identifiers (xml...)
		out( 100, "warning" );
		exception.printStackTrace();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	@Override
	public void error(SAXParseException exception) throws SAXException {
		// Errors reported for: 
		// undeclared elements or attributes
		// DTD structure
		// missing required attr
		// Changing value of #FIXED
		// Missing DTD
		out( 101, "error" );
		exception.printStackTrace();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		// Fatal reported for:
		// bad DTD structure
		// undeclared entity (when validating)
		out( 102, "fatalError" );
		exception.printStackTrace();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#elementDecl(java.lang.String, java.lang.String)
	 */
	@Override
	public void elementDecl(String name, String model) throws SAXException {
		out( 10, "elementDecl", name, model );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
		out( 11, "attributeDecl", eName, aName, type, mode, value );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#internalEntityDecl(java.lang.String, java.lang.String)
	 */
	@Override
	public void internalEntityDecl(String name, String value) throws SAXException {
		out( 12, "internalEntityDecl", name, value );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.DeclHandler#externalEntityDecl(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		out( 13, "startDTD", name, publicId, systemId );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endDTD()
	 */
	@Override
	public void endDTD() throws SAXException {
		out( 14, "endDTD" );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
	 */
	@Override
	public void startEntity(String name) throws SAXException {
		out( 15, "startEntity", name );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
	 */
	@Override
	public void endEntity(String name) throws SAXException {
		out( 16, "endEntity", name );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
	 */
	@Override
	public void startCDATA() throws SAXException {
		out( 17, "startCDATA" );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
	 */
	@Override
	public void endCDATA() throws SAXException {
		out( 18, "endCDATA" );
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
	 */
	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		out( 19, "comment", new String(ch, start, length) );
	}
	
}
