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

package test.sog.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import sog.core.App;
import sog.core.AppRuntime;
import sog.core.LocalDir;
import sog.core.Test;
import sog.core.xml.XML;
import sog.core.xml.XMLHandler;
import sog.util.Commented;
import sog.util.Macro;

/**
 * Test cases reveal the behavior of the SAX parsing framework.
 * 
 * Cases showing interesting/unexpected/useful behavior are marked with a "LESSON-LEARNED" comment.
 * Cases illustrating a useful technique are marked with a "PROOF-OF-CONCEPT" comment.
 */
@Test.Skip( "Container" )
public class XMLHandlerTest extends Test.Container {

	
	private final Commented source = new Commented( XMLHandlerTest.class );
	
	private final Macro mapper = new Macro().expand( "xml", XML.get().getDeclaration() );
	
	
	public XMLHandlerTest() {
		super( XMLHandler.class );
	}
	
	
	private static class VAR<T> {

		private T result = null;
		
		private VAR() {}
		
		private VAR( T init ) { this.result = init; }

		public T get() { return this.result; }

		public void set( T t ) { this.result = t; }
		
	}
	
	
	private Stream<String> getLines( String tag ) {
		try {
			return this.source.getTaggedBlock( tag ).flatMap( this.mapper );
		} catch ( IOException e ) {
			throw new AppRuntime( e );
		}
	}

	
	// TEST CASES:

	// LESSON LEARNED
	// For various reasons, these tests cannot be run concurrently.
	// See the comments for
	//     	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
	//		description = "All keys are present" 

	
    @Test.Impl( 
    	member = "constructor: XMLHandler(InputSource)", 
    	description = "Throws AssertionError for null source" 
    )
    public void tm_0962A2397( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	InputSource source = null;
    	new XMLHandler( source );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(InputStream)", 
    	description = "Throws AssertionError for null stream" 
    )
    public void tm_0D0691137( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	InputStream stream = null;
    	new XMLHandler( stream );
    }
    
    
    
    
    /* <EXT_XML>
     * <?xml version = "1.0" encoding = "UTF-8" ?>
     * <!DOCTYPE	root	SYSTEM	"TEST.dtd" >
     * <root/>
     */
    
    /* <EXT_DTD>
     * <!ELEMENT	root	EMPTY>
     */
    @Test.Impl( 
    	member = "constructor: XMLHandler(Path)", 
    	description = "External DTD is read"
    )
    public void tm_00380BA00( Test.Case tc ) throws IOException {
    	Path xmlPath = new LocalDir().sub( "tmp" ).sub( "system" ).getFile( "TEST", LocalDir.Type.XML );
    	Path dtdPath = new LocalDir().sub( "tmp" ).sub( "system" ).getFile( "TEST", LocalDir.Type.DTD );

		this.source.writeTaggedBlock( "EXT_XML", xmlPath );
		this.source.writeTaggedBlock( "EXT_DTD", dtdPath );
		
		final VAR<Boolean> docComplete = new VAR<>( false );
		final VAR<Boolean> dtdComplete = new VAR<>( false );
		final VAR<Boolean> noWarn = new VAR<>( true );
		final VAR<Boolean> noError= new VAR<>( true );
		final VAR<Boolean> noFatal = new VAR<>( true );
		
    	new XMLHandler( xmlPath ) {
    		@Override public void endDocument() throws SAXException { 
    			super.endDocument();
    			docComplete.set( true ); 
    		}
    		@Override public void endDTD() throws SAXException { 
    			super.endDTD();
    			dtdComplete.set( true ); 
    		}
    		@Override public void warning( SAXParseException exception ) { 
    			super.warning( exception );
    			noWarn.set( false ); 
    		}
    		@Override public void error( SAXParseException exception ) { 
    			super.error( exception );
    			noError.set( false ); 
    		}
    		@Override public void fatalError( SAXParseException exception ) throws AppRuntime { 
    			super.fatalError( exception );
    			noFatal.set( false ); 
    		}
    	}.parse();
    	
    	tc.assertTrue( docComplete.get() );
    	tc.assertTrue( dtdComplete.get() );
    	tc.assertTrue( noWarn.get() );
    	tc.assertTrue( noError.get() );
    	tc.assertTrue( noFatal.get() );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(Path)", 
    	description = "Throws AppRuntime if the file is missing" 
    )
    public void tm_02CA1C52D( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	new XMLHandler( Path.of( "bogus" ) );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(Path)", 
    	description = "Throws AssertionError for null path" 
    )
    public void tm_00B3EEBA1( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Path path = null;
    	new XMLHandler( path );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(Reader)", 
    	description = "Throws AssertionError for null reader" 
    )
    public void tm_02FDE7D21( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Reader reader = null;
    	new XMLHandler( reader );
    }
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(Stream)", 
    	description = "Throws AssertionError for null stream" 
    )
    public void tm_07E27CDC1( Test.Case tc ) {
    	tc.expectError( AssertionError.class );
    	Stream<String> stream = null;
    	new XMLHandler( stream );
    }
    
    
    
    /* <attributesToMap>
     * ${ xml }
     * <!DOCTYPE	root [
     * 	<!ELEMENT	root	(A, B) >
     * 	<!ELEMENT	A		EMPTY>
     * 	<!ATTLIST	A		attr1		CDATA	#IMPLIED
     * 						attr2		CDATA	#IMPLIED
     * 						attr3		CDATA	#IMPLIED
     * 	>
     * 	<!ELEMENT	B		EMPTY>
     * ]>
     * <root>
     * 	<A  attr1 = "1"  attr2 = "2"  attr3 = "3" />
     * 	<B/>
     * </root>
     */
    @Test.Impl( 
    	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
    	description = "All keys are present"
    )
    public void tm_0CEFD09EC( Test.Case tc ) {
    	VAR<Map<String, String>> keyToVal = new VAR<>();
    	
    	// DIFFICULT LESSON LEARNED:
    	// SAX reuses structures. The Attributes instance returned is the same instance with it's properties reset.
    	// We can't use VAR<Attributes> since the values of the contained Attributes will have the properties
    	// of the last element encountered. Could have kept a copy, or overridden the VAR.accept() to
    	// make the test assertion. Decided to simply convert to a Map
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			super.startElement( uri, localName, qName, atts );
    			if ( qName.equals( "A" ) ) { keyToVal.set( XMLHandler.attributesToMap( atts ) ); }
    		}
    	}.parse();
    	
    	Stream.of( "attr1", "attr2", "attr3" ).forEach( s -> tc.assertTrue( keyToVal.get().keySet().contains( s ) ) );
    }
    
    @Test.Impl( 
    	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
    	description = "Values are correct"
    )
    public void tm_056FCB76A( Test.Case tc ) {
    	VAR<Map<String, String>> keyToVal = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			super.startElement( uri, localName, qName, atts );
    			if ( qName.equals( "A" ) ) { keyToVal.set( XMLHandler.attributesToMap( atts ) ); }
    		}
    	}.parse();
    	
    	tc.assertEqual( 
    		List.of( "1", "2", "3" ), 
    		Stream.of( "attr1", "attr2", "attr3" ).map( keyToVal.get()::get ).collect( Collectors.toList() )
    	);
	}
        
    @Test.Impl( 
    	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
    	description = "Empty map returned when no attributes" 
    )
    public void tm_01F066856( Test.Case tc ) {
    	VAR<Map<String, String>> keyToVal = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			super.startElement( uri, localName, qName, atts );
    			if ( qName.equals( "B" ) ) { keyToVal.set( XMLHandler.attributesToMap( atts ) ); }
    		}
    	}.parse();
    	
    	tc.assertTrue( keyToVal.get().size() == 0 );
    }
    

    /* <Location>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT root EMPTY>
     *	]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.getPublicId()", 
    	description = "Agrees with value supplied to the InputSource" 
    )
    public void tm_090C629A6( Test.Case tc ) {
    	VAR<String> thePublicId = new VAR<>();
    	
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void setDocumentLocator( Locator locator ) {
    			super.setDocumentLocator( locator );
    			thePublicId.set( locator.getPublicId() );
    		}
    	};

    	String publicId = "my-pub-id";		// For publicId, string can be anything
    	InputSource source = this.getSubjectField( handler, "source", null );
    	source.setPublicId( publicId );

    	handler.parse();
    	tc.assertEqual( publicId, thePublicId.get() );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.getSystemId()", 
    	description = "Agrees with value supplied to the InputSource" 
    )
    public void tm_07B51C1E5( Test.Case tc ) {
    	VAR<String> theSystemId = new VAR<>();
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void setDocumentLocator( Locator locator ) {
    			super.setDocumentLocator( locator );
    			theSystemId.set( locator.getSystemId() );
    		}
    	};

    	// LESSON LEARNED
    	// If the systemId has not been previously set (for example, reading a stream) and
    	// and then set to a non-fully resolved URL, it is interpreted as a relative
    	// URL and gets resolved against a value determined by some environment variable.
    	// For example, setSystemId( "my-sis-id" ) results in
    	// 		file:///home/sundquis/book/Dropbox/java/projects/SOG/my-sys-id
    	String systemId = "file:///home";
    	InputSource source = this.getSubjectField( handler, "source", null );
    	source.setSystemId( systemId );

    	handler.parse();
    	tc.assertEqual( systemId, theSystemId.get() );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.toString()", 
    	description = "Indicates row and column while parsing"
    )
    public void tm_03405A7FD( Test.Case tc ) {
    	VAR<String> thePosition = new VAR<>();
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override 	public void startDocument() throws SAXException {
    			super.startDocument();
    			thePosition.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(1, 1)", thePosition.get() );
    }
    
    
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showContentEvents()", 
    	description = "Enables feedback for content events" 
    )
    public void tm_09A29E715( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events, including:" );
    	tc.addMessage( "Parser set Locator" );
    	tc.addMessage( "Document started" );
    	tc.addMessage( "JsonValue started" );
    	tc.addMessage( "JsonValue complete" );
    	tc.addMessage( "Document complete" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showContentEvents()", 
    	description = "Returns this XMLHandler instance to allow chaining" 
    )
    public void tm_0CFC338A5( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	tc.assertEqual( handler, handler.showContentEvents() );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showDeclarationEvents()", 
    	description = "Enables feedback for declaration events" 
    )
    public void tm_027E14B35( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see declaration events, including:" );
    	tc.addMessage( "JsonValue declaration" );
    	tc.addMessage( "Name: root" );
    	tc.addMessage( "Model: EMPTY" );
    	handler.showDeclarationEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showDeclarationEvents()", 
    	description = "Returns this XMLHandler instance to allow chaining" 
    )
    public void tm_0795C4C66( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	tc.assertEqual( handler, handler.showDeclarationEvents() );
    }
    
    /* <showErrorEvents>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root (A) > ]>
     *	<root>
     *		<A/>
     *	</groot>
     */
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showErrorEvents()", 
    	description = "Enables feedback for error events" 
    )
    public void tm_023C1CCF5( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "showErrorEvents" ) );
    	// TOGGLE
    	/* */ tc.expectError( AppRuntime.class ); handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see error events, including:" );
    	tc.addMessage( "SAX Error: org.xml.sax.SAXParseException; ... JsonValue type \"A\" must be declared." );
    	tc.addMessage( "SAX Fatal Error: org.xml.sax.SAXParseException; ... The element type \"root\" must be terminated by the matching end-tag \"</root>\"." );
    	handler.showErrorEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showErrorEvents()", 
    	description = "Returns this XMLHandler instance to allow chaining" 
    )
    public void tm_0983A36B4( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	tc.assertEqual( handler, handler.showErrorEvents() );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showLexicalEvents()", 
    	description = "Enables feedback for lexical (DTD) events" 
    )
    public void tm_0C9173858( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events, including:" );
    	tc.addMessage( "DTD started: root" );
    	tc.addMessage( "DTD complete" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showLexicalEvents()", 
    	description = "Returns this XMLHandler instance to allow chaining" 
    )
    public void tm_091F12050( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	tc.assertEqual( handler, handler.showDeclarationEvents() );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Is not null" 
    )
    public void tm_0132A429A( Test.Case tc ) {
    	tc.assertNonNull( new XMLHandler( this.getLines( "Location" ) ).getLocation() );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Location is unknown after parsing" 
    )
    public void tm_0E72E765B( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	handler.parse();
    	tc.assertTrue( handler.getLocation().unknown() );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Location is unknown before parsing" 
    )
    public void tm_0EDFB4DD6( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Location" ) );
    	tc.assertTrue( handler.getLocation().unknown() );
    }
    
    @Test.Impl( 
    	member = "method: boolean XMLHandler.Location.unknown()", 
    	description = "False while parsing" 
    )
    public void tm_0C41A489B( Test.Case tc ) {
    	VAR<Boolean> theUnknown = new VAR<>();
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void startDocument() throws SAXException {
    			super.startDocument();
    			theUnknown.set( this.getLocation().unknown() );
    		}
    	}.parse();
    	tc.assertEqual( false, theUnknown.get() );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getColumnNumber()", 
    	description = "Greater or equal to zero while parsing" 
    )
    public void tm_09D21EF6F( Test.Case tc ) {
    	VAR<Integer> theStartDocumentColumn = new VAR<>();
    	VAR<Integer> theStartElementColumn = new VAR<>();
    	VAR<Integer> theEndElementColumn = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void startDocument() throws SAXException { 
    			super.startDocument();
    			theStartDocumentColumn.set( this.getLocation().getColumnNumber() );
    		}
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theStartElementColumn.set( this.getLocation().getColumnNumber() );
    		}
    		@Override public void endElement( String uri, String localName, String qName ) throws SAXException {
    			super.endElement( uri, localName, qName );
    			theEndElementColumn.set( this.getLocation().getColumnNumber() );
    		}
    	}.parse();
    	
    	tc.assertTrue( theStartDocumentColumn.get() > 0 );
    	tc.assertTrue( theStartElementColumn.get() > 0 );
    	tc.assertTrue( theEndElementColumn.get() > 0 );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getColumnNumber()", 
    	description = "Return is -1 when unknown" 
    )
    public void tm_0B830214C( Test.Case tc ) {
    	VAR<Integer> theColumn = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void endDocument() throws SAXException { 
    			super.endDocument();
    			theColumn.set( this.getLocation().getColumnNumber() );
    		}
    	}.parse();
    	
    	tc.assertEqual( -1, theColumn.get() );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getLineNumber()", 
    	description = "Greater or equal to zero while parsing" 
    )
    public void tm_0604FC4D1( Test.Case tc ) {
    	VAR<Integer> theStartDocumentLine = new VAR<>();
    	VAR<Integer> theStartElementLine = new VAR<>();
    	VAR<Integer> theEndElementLine = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void startDocument() throws SAXException { 
    			super.startDocument();
    			theStartDocumentLine.set( this.getLocation().getLineNumber() );
    		}
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theStartElementLine.set( this.getLocation().getLineNumber() );
    		}
    		@Override public void endElement( String uri, String localName, String qName ) throws SAXException {
    			super.endElement( uri, localName, qName );
    			theEndElementLine.set( this.getLocation().getLineNumber() );
    		}
    	}.parse();
    	
    	tc.assertTrue( theStartDocumentLine.get() > 0 );
    	tc.assertTrue( theStartElementLine.get() > 0 );
    	tc.assertTrue( theEndElementLine.get() > 0 );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getLineNumber()", 
    	description = "Return is -1 when unknown" 
    )
    public void tm_0830934AA( Test.Case tc ) {
    	VAR<Integer> theLine = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void endDocument() throws SAXException { 
    			super.endDocument();
    			theLine.set( this.getLocation().getLineNumber() );
    		}
    	}.parse();
    	
    	tc.assertEqual( -1, theLine.get() );
    }
    
    /* <attributeDecl: Basic>
     *	${ xml }
     *	<!DOCTYPE	root	[
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	attr	CDATA	#IMPLIED>
     *	]>
     *	<root  attr = "42"/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Attribute name is not empty" 
    )
    public void tm_0D9B6E779( Test.Case tc ) {
    	VAR<String> theAttributeName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributeDecl: Basic" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
				theAttributeName.set( aName );
    		}
    	}.parse();

    	tc.assertNotEmpty( theAttributeName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "JsonValue name is not empty" 
    )
    public void tm_0879EB099( Test.Case tc ) {
    	VAR<String> theElementName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributeDecl: Basic" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
				theElementName.set( eName );
    		}
    	}.parse();

    	tc.assertNotEmpty( theElementName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_087E189D9( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "attributeDecl: Basic" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "JsonValue declaration" );
    	tc.addMessage( "Attribute declaration" );
    	handler.showDeclarationEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Location is identified" 
    )
    public void tm_009A360BE( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributeDecl: Basic" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
    			if ( "attr".equals( aName ) ) {
    				theLocation.set( this.getLocation().toString() );    				
    			}
    		}
    	}.parse();

    	tc.assertEqual( "(4, 36)", theLocation.get() );
    }

    
    
    /* <attributeDecl: Mode>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	implied		CDATA		#IMPLIED
     *							required	CDATA		#REQUIRED
     *							fixed		CDATA		#FIXED		"42"
     *							default		CDATA		"42"
     *		>
     *	]>
     *	<root required = "val"/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Mode is correct" 
    )
    public void tm_0FAF7DB4F( Test.Case tc ) {
    	final Map<String, String> aNameToMode = new HashMap<>();

    	new XMLHandler( this.getLines( "attributeDecl: Mode" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
				aNameToMode.put( aName, mode );
    		}
    	}.parse();

    	tc.assertEqual( 
    		List.of( "#IMPLIED", "#REQUIRED", "#FIXED" ),
    		Stream.of( "implied", "required", "fixed" ).map( aNameToMode::get ).collect( Collectors.toList() )
    	);
    	tc.assertIsNull( aNameToMode.get( "default" ) );
    }

    /* <attributeDecl: Type>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	cdata		CDATA		#IMPLIED
     *							nmtoken		NMTOKEN		#IMPLIED
     *							nmtokens	NMTOKENS	#IMPLIED
     *							id			ID			#IMPLIED
     *							idref		IDREF		#IMPLIED
     *							idrefs		IDREFS		#IMPLIED
     *							entity		ENTITY		#IMPLIED
     *							entities	ENTITIES	#IMPLIED
     *							enum		(1|2|3)		#IMPLIED
     *		>
     *	]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Type is correct" 
    )
    public void tm_035989318( Test.Case tc ) {
    	final Map<String, String> aNameToType = new HashMap<>();

    	new XMLHandler( this.getLines( "attributeDecl: Type" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
				aNameToType.put( aName, type );
    		}
    	}.parse();

    	// TODO: Investigate Notations to see if they should be added.
		Stream.of( "cdata", "nmtoken", "nmtokens", "id", "idref", "idrefs", "entity", "entities" )
			.forEach( (String s) -> tc.assertEqual( s.toUpperCase(), aNameToType.get( s ) ) );
		tc.assertEqual( "(1|2|3)", aNameToType.get( "enum" ) );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Value is correct when supplied in DTD" 
    )
    public void tm_080BE7B30( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "attributeDecl: Mode" ) ) {
    		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
    			super.attributeDecl( eName, aName, type, mode, value );
    			if ( "fixed".equals( aName ) ) {
    				theValue.set( value );
    			}
    		}
    	}.parse();
    	
    	tc.assertEqual( "42", theValue.get() );
    }
    
    
    
    /* <characters>
     *	${ xml }
     *	<!DOCTYPE	root	[
     *		<!ELEMENT	root	(#PCDATA)>
     *	]>
     *	<root>
     *		Some character data.
     *		Another line of data.
     *	</root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "All content registered before endElement" 
    )
    public void tm_0CD2DE44F( Test.Case tc ) {
    	StringBuilder buffer = new StringBuilder();
    	VAR<String> theResult = new VAR<>();
    	
    	new XMLHandler( this.getLines( "characters" ) ) {
    		@Override public void characters( char[] ch, int start, int length ) throws SAXException {
    			super.characters( ch, start, length );
    			buffer.append( ch, start, length );
    		}
    		@Override public void endElement( String name ) {
    			super.endElement( name );
    			theResult.set( buffer.toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "\n\tSome character data.\n\tAnother line of data.\n", theResult.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0AE2AC765( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "characters" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Character data found" );
    	tc.addMessage( "JsonValue complete" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_07AD6ABCB( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "characters" ) ) {
    		@Override public void characters( char[] ch, int start, int length ) throws SAXException {
    			super.characters( ch, start, length );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();

    	tc.assertNotEmpty( theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Parser uses to signal content" 
    )
    public void tm_0292924E4( Test.Case tc ) {
    	tc.addMessage( "Remove this case?" );
    	tc.assertPass();
    }
    
    
    
    /* <comment>
     *	${ xml }
     *	<!DOCTYPE	root [ <!ELEMENT root EMPTY> ]>
     *	<!--Comment before-->
     *	<root/>
     *	<!--Comment after-->
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_027A703BD( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "comment" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
		tc.addMessage( "Should see parsing events including:" );
		tc.addMessage( "Comment:  Comment before" );
		tc.addMessage( "Comment:  Comment after" );
		handler.showLexicalEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_04E7CE238( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "comment"  ) ) {
    		@Override public void comment( char[] ch, int start, int length ) throws SAXException {
    			super.comment( ch, start, length );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertNotEmpty( theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Parser signals comments"
    )
    public void tm_0F8C0BB4E( Test.Case tc ) {
    	VAR<Boolean> needComment = new VAR<>( true );
    	StringBuilder theComment = new StringBuilder();
    	
    	new XMLHandler( this.getLines( "comment" ) ) {
    		@Override public void comment( char[] ch, int start, int length ) throws SAXException {
    			super.comment( ch, start, length );
    			if ( needComment.get() ) {
    				theComment.append( ch, start, length );
    				needComment.set( false );
    			}
    		}
    	}.parse();
    	
    	tc.assertEqual( "Comment before", theComment.toString() );
    }
    
    

    /* <ElementDecl>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *	]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_001DFE38A( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "ElementDecl" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "JsonValue declaration" );
    	tc.addMessage( "Name: root" );
    	tc.addMessage( "Model: EMPTY" );
    	handler.showDeclarationEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0092D172F( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "ElementDecl" ) ) {
    		@Override public void elementDecl( String name, String model ) throws SAXException {
    			super.elementDecl( name, model );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(3, 23)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Model is correct" 
    )
    public void tm_0E0D2F914( Test.Case tc ) {
    	VAR<String> theModel = new VAR<>();
    	
    	new XMLHandler( this.getLines( "ElementDecl" ) ) {
    		@Override public void elementDecl( String name, String model ) throws SAXException {
    			super.elementDecl( name, model );
    			theModel.set( model );
    		}
    	}.parse();
    	
    	tc.assertEqual( "EMPTY", theModel.get() );
    }

    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Name is not empty" 
    )
    public void tm_079D955AC( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "ElementDecl" ) ) {
    		@Override public void elementDecl( String name, String model ) throws SAXException {
    			super.elementDecl( name, model );
    			theName.set( name );
    		}
    	}.parse();

    	tc.assertNotEmpty( theName.get() );
    }
    
    
    /* <CDATA>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	(#PCDATA) >
     *	]>
     *	<root>
     *		<![CDATA[<foo>Unparsed</foo>]]>
     *	</root>
     */
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Called after startCDATA()" 
    )
    public void tm_04AE85F04( Test.Case tc ) {
    	VAR<Boolean> startCDATACalled = new VAR<>( false );
    	VAR<Boolean> endedAfter = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "CDATA" ) ) {
    		@Override public void startCDATA() throws SAXException {
    			super.startCDATA();
    			startCDATACalled.set( true );
    		}
    		@Override public void endCDATA() throws SAXException {
    			super.endCDATA();
    			if ( startCDATACalled.get() ) {
    				endedAfter.set( true );
    			}
    		}
    	}.parse();

    	tc.assertTrue( endedAfter.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_0B493F730( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "CDATA" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "CDATA complete" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Location is identified" 
    )
    public void tm_029EBA26B( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "CDATA" ) ) {
    		@Override public void endCDATA() throws SAXException {
    			super.endCDATA();
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();

    	tc.assertEqual( "(6, 33)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Parser signals end of CDATA BEFORE last characters call" 
    )
    public void tm_0130C0094( Test.Case tc ) {
    	// LESSON LEARNED:
    	// This was an unexpected sequence of events...
    	VAR<Boolean> endCDATACalled = new VAR<>( false );
    	VAR<Boolean> charactersCalledAfter = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "CDATA" ) ) {
    		@Override public void endCDATA() throws SAXException {
    			super.endCDATA();
    			endCDATACalled.set( true );
    		}
    		@Override public void characters( char[] ch, int start, int length ) throws SAXException {
    			super.characters( ch, start, length );
    			if ( endCDATACalled.get() ) {
    				charactersCalledAfter.set( true );
    			}
    		}
    	}.parse();

    	tc.assertTrue( charactersCalledAfter.get() );
    }
    
    
    
    /* <DTD>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT root EMPTY>
     *	]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Called after startDTD()" 
    )
    public void tm_07A017C12( Test.Case tc ) {
    	VAR<Boolean> startDTDCalled = new VAR<>( false );
    	VAR<Boolean> endDTDCalledAfter = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "DTD" ) ) {
    		@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException {
    			super.startDTD( name, publicId, systemId );
    			startDTDCalled.set( true );
    		}
    		@Override public void endDTD() throws SAXException {
    			super.endDTD();
    			if ( startDTDCalled.get() ) {
    				endDTDCalledAfter.set( true );
    			}
    		}
    	}.parse();

    	tc.assertTrue( startDTDCalled.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_0F7D04189( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "DTD" ) );
    	// TOGGLE:
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "DTD complete" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "Verify messages" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Location is identified" 
    )
    public void tm_0F9095304( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "DTD" ) ) {
    		@Override public void endDTD() throws SAXException {
    			super.endDTD();
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(4, 1)", theLocation.get() );  // Atypical location to fire this event.
    }

    
    /* <Document>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root EMPTY> ]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Called after startDocument()" 
    )
    public void tm_0717CDC9A( Test.Case tc ) {
    	VAR<Boolean> startCalled = new VAR<>( true );
    	VAR<Boolean> endCalledAfter = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Document" ) ) {
    		@Override public void startDocument() throws SAXException {
    			super.startDocument();
    			startCalled.set( true );
    		}
    		@Override public void endDocument() throws SAXException {
    			super.endDocument();
    			if ( startCalled.get() ) {
    				endCalledAfter.set( true );
    			}
    		}
    	}.parse();
    	
    	tc.assertTrue( endCalledAfter.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0D4080063( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Document" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including: " );
    	tc.addMessage( "Document started" );
    	tc.addMessage( "Document complete" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "Check messages above" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Location is unknown"
    )
    public void tm_0B93B8449( Test.Case tc ) {
    	VAR<Boolean> locationUnknown = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Document" ) ) {
    		@Override public void endDocument() throws SAXException {
    			super.endDocument();
    			locationUnknown.set( this.getLocation().unknown() );
    		}
    	}.parse();

    	tc.assertTrue( locationUnknown.get() );
    }

    
    
    /* <JsonValue>
     *	${ xml }
     *	<!DOCTYPE A [
     *		<!ELEMENT	A	(B)>
     *		<!ELEMENT	B	(C)>
     *		<!ELEMENT	C	EMPTY>
     *	]>
     *	<A>
     *		<B>
     *			<C/>
     *		</B>
     *	</A>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Elements closed in LIFO order" 
    )
    public void tm_0DD1B1E73( Test.Case tc ) {
    	List<String> closed = new ArrayList<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String name ) {
    			super.endElement( name );
    			closed.add( name );
    		}
    	}.parse();

    	tc.assertEqual( List.of( "C", "B", "A" ), closed );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Location is identified" 
    )
    public void tm_03EF39A0D( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String name ) {
    			super.endElement( name );
    			if ( theLocation.get() == null ) {
    				theLocation.set( this.getLocation().toString() );
    			}
    		}
    	}.parse();
    	
    	tc.assertEqual( "(9, 7)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Name is not empty" 
    )
    public void tm_01BE8000E( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String name ) {
    			super.endElement( name );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertNotEmpty( theName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0F41011F1( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "JsonValue" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "JsonValue complete: C" );
    	tc.addMessage( "JsonValue complete: B" );
    	tc.addMessage( "JsonValue complete: A" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "Check messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "localName can be empty" 
    )
    public void tm_0A1159725( Test.Case tc ) {
    	VAR<String> theLocalName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String uri, String localName, String qName ) throws SAXException {
    			// Don't want super here.
    			theLocalName.set( localName );
    		}
    	}.parse();

    	tc.assertEqual( "", theLocalName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "qName is not empty" 
    )
    public void tm_026D3575F( Test.Case tc ) {
    	VAR<String> theQName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String uri, String localName, String qName ) throws SAXException {
    			// Don't want super here.
    			theQName.set( qName );
    		}
    	}.parse();

    	tc.assertNotEmpty( theQName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "uri can be empty" 
    )
    public void tm_06A6DB40F( Test.Case tc ) {
    	VAR<String> theUri = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void endElement( String uri, String localName, String qName ) throws SAXException {
    			// Don't want super here.
    			theUri.set( uri );
    		}
    	}.parse();

    	tc.assertEqual( "", theUri.get() );
    }

    
    
    
    /* <Entity>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	ANY>
     *		<!ENTITY	hgtg	"The answer to the ultimate question...">
     *	]>
     *	<root>&hgtg; of life the universe and everything.</root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Called after startEntity()" 
    )
    public void tm_0C0B373CB( Test.Case tc ) {
    	VAR<Boolean> startCalled = new VAR<>( false );
    	VAR<Boolean> endCalledAfter = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void startEntity( String name ) throws SAXException {
    			super.startEntity( name );
    			startCalled.set( true );
    		}
    		@Override public void endEntity( String name ) throws SAXException {
    			super.endEntity( name );
    			if ( startCalled.get() ) {
    				endCalledAfter.set( true );
    			}
    		}
    	}.parse();
    	
    	tc.assertTrue( endCalledAfter.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_07B02E777( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Entity" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing messages including:" );
    	tc.addMessage( "Entity started: hgtg" );
    	tc.addMessage( "Entity complete: hgtg" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "Check messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Location is identified" 
    )
    public void tm_06E79D472( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void endEntity( String name ) throws SAXException {
    			super.endEntity( name );
    			if ( "hgtg".equals( name ) ) {
        			theLocation.set( this.getLocation().toString() );
    			}
    		}
    	}.parse();
    	
    	//tc.assertEqual( "(4, 57)", theLocation.get() );
    	// LESSON LEARNED:
    	// The parser reports (1, 39), which cannot be correct.
    	tc.assertPass();
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Name is consistent" 
    )
    public void tm_06878B7FF( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void endEntity( String name ) throws SAXException {
    			super.endEntity( name );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertEqual( "hgtg", theName.get() );
    }

    
    

    /*	<Error: Change #FIXED>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	fixed	CDATA	#FIXED	"42">
     *	]>
     *	<root fixed = "0"/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Feedback provided if showErrorEvents() has been called" 
    )
    public void tm_0BA4B01C4( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Error: Change #FIXED" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "SAX Error" );
    	handler.showErrorEvents().parse();
    	tc.assertFail( "Check messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Location is identified" 
    )
    public void tm_0D36F725B( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Error: Change #FIXED" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(6, 20)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for changing #FIXED attribute" 
    )
    public void tm_07BDBFE86( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Change #FIXED" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Error: Missing doctype>
     *	${ xml }
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for missing doctype" 
    )
    public void tm_02BA18DA0( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Missing doctype" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Error: Missing #REQUIRED>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	required	CDATA	#REQUIRED>
     *	]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for missing requiured attribute" 
    )
    public void tm_0594E8016( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Missing #REQUIRED" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Error: Undeclared attribute>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
     *	<root a = "A"></root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for undeclared attributes" 
    )
    public void tm_0372E572A( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Undeclared attribute" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Error: Undeclared element>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
     *	<root><bad></bad></root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for undeclared elements" 
    )
    public void tm_028D7ECCA( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Undeclared element" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Error: Wrong type>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	(child)*>
     *		<!ELEMENT	child	ANY>
     *	]>
     *	<root>Wrong</root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered when element does not match declared type" 
    )
    public void tm_0FA312C98( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Error: Wrong type" ) ) {
    		@Override public void error( SAXParseException exception ) {
    			super.error( exception );
    			triggered.set( true );
    		}
    	}.parse();
    	
    	tc.assertTrue( triggered.get() );
    }

    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Prints error message and stack trace" 
    )
    public void tm_0B507F16E( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Error: Wrong type" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see localized stack trace containing:" );
    	tc.addMessage( "sog.core.xml ... in parse" );
    	tc.addMessage( "test.sog.core.xml ... tm_0B507F16E" );
    	tc.addMessage( "sog.core.test ... in run" );
    	tc.addMessage( "..." );
    	tc.addMessage( "test.sog.core.xml ... in main" );
    	handler.showErrorEvents().parse();
    	tc.assertFail( "Check messages above." );
    	/* */
    }


    
    /*	<Fatal: Illegal DTD>
     *	${ xml }
     *	<!DOCTYPE root [ <!ATLAS	root	garbage	DCATA	#IMPLIED> ]>
     *	<root/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Prints fatal message and stack trace" 
    )
    public void tm_0B2A96188( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	XMLHandler handler = new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) );
    	// TOGGLE
    	/* */ handler.parse(); /*
    	tc.addMessage( "Should see localized stack trace including:" );
    	tc.addMessage( "sog.core.xml ... in parse" );
    	tc.addMessage( "test.sog.core.xml ... in tm_0B2A96188" );
    	tc.addMessage( "sog.core.test ... in run" );
    	tc.addMessage( "..." );
    	tc.addMessage( "test.sog.core.xml ... in main" );
    	tc.assertFail( "Check above messages." );
    	handler.showErrorEvents().parse();
    	/* */
    }

    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "AppRuntime is thrown after signaling" 
    )
    public void tm_0D0852470( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) ).parse();
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Feedback provided if showErrorEvents() has been called" 
    )
    public void tm_010038662( Test.Case tc ) {
    	tc.expectError( AppRuntime.class );
    	XMLHandler handler = new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) );
    	// TOGGLE
    	/* */ handler.parse(); /*
    	tc.addMessage( "Should parsing messages including:" );
    	tc.addMessage( "SAX Fatal Error: org.xml.sax.SAXParseException" );
    	tc.assertFail( "Check above messages." );
    	handler.showErrorEvents().parse();
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Location is identified" 
    )
    public void tm_086F1EEF9( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();

    	// LESSON LEARNERD:
    	// Very tricky timing.
    	// 1. SAXParser.parse() generates a fatal error
    	// 2. fatalError(..) callback executes, but we have overriden the super.fatalError so it does not throw the AppRuntime
    	// 3. SaxParser could not "configure parser" so it throws the SAXException
    	// 4. This is caught in XMLHandler.parse() and wrapped as an AppRuntime
    	// If we call tc.expectException( AppRuntime.class ) then the case passes because of the expected
    	// error, but we do not hit the desired assertion that the callback set the location.
    	try {
    		new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) ) {
    			@Override public void fatalError( SAXParseException exception ) throws AppRuntime {
    				theLocation.set( this.getLocation().toString() );
    			}
    		}.parse();
    	} catch ( AppRuntime e ) {}

		tc.assertEqual( "(2, 20)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Trigered for illegal DTD structure" 
    )
    public void tm_09C315DA1( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );

    	try {
        	new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) ) {
        		@Override public void fatalError( SAXParseException exception ) throws AppRuntime {
        			triggered.set( true );
        		}
        	}.parse();
    	} catch ( Throwable t ) {}
    	
    	tc.assertTrue( triggered.get() );
    }

    /*	<Fatal: Undeclared entity
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
     *	<root>&undeclared;</root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Trigered for undeclared entity" 
    )
    public void tm_017BA6954( Test.Case tc ) {
    	VAR<Boolean> triggered = new VAR<>( false );

    	try {
        	new XMLHandler( this.getLines( "Fatal: Illegal DTD" ) ) {
        		@Override public void fatalError( SAXParseException exception ) throws AppRuntime {
        			triggered.set( true );
        		}
        	}.parse();
    	} catch ( Throwable t ) {}
    	
    	tc.assertTrue( triggered.get() );
    }
    
    

    
    /*	<Ignorable whitespace>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	(child)>
     *		<!ELEMENT	child	EMPTY>
     *	]>
     *	<root>      <child/></root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.ignorableWhitespace(char[], int, int)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0F61FECE7( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Ignorable whitespace" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Ignorable whitespace found: 6" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.ignorableWhitespace(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_04BE3D1CD( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Ignorable whitespace" ) ) {
    		@Override public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
    			super.ignorableWhitespace( ch, start, length );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(6, 14)", theLocation.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_04C5E3244( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Entity" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Entity declaration" );
    	handler.showDeclarationEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0A01AE169( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void internalEntityDecl( String name, String value ) throws SAXException {
    			super.internalEntityDecl( name, value );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(4, 57)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Name is correct" 
    )
    public void tm_0829D6D1C( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void internalEntityDecl( String name, String value ) throws SAXException {
    			super.internalEntityDecl( name, value );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertEqual( "hgtg", theName.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Value is correct" 
    )
    public void tm_084975586( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void internalEntityDecl( String name, String value ) throws SAXException {
    			super.internalEntityDecl( name, value );
    			theValue.set( value );
    		}
    	}.parse();
    	
    	tc.assertEqual( "The answer to the ultimate question...", theValue.get() );
    }


    
    /*	<Out>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
     *	<root></root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Detail objects may be null" 
    )
    public void tm_04FCB3A1A( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Out" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see:" );
    	tc.addMessage( ">>> (-1, -1)" );
    	tc.addMessage( "	null: null" );
    	handler.out( null, null );
    	tc.addMessage( "See messages above" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Details treated as (label, detail) pairs" 
    )
    public void tm_0330950D4( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Out" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see:" );
    	tc.addMessage( "	Label: Detail" );
    	handler.out( "Label", "Detail" );
    	tc.addMessage( "See messages above" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Throws AssertionError for null details" 
    )
    public void tm_09F6D9B93( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Out" ) );
    	tc.expectError( AssertionError.class );
    	Object[] details = null;
    	handler.out( details );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Throws AssertionError for odd number of details" 
    )
    public void tm_00832F26B( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Out" ) );
    	tc.expectError( AssertionError.class );
    	handler.out( "1", "2", "3" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.parse()", 
    	description = "Throws AppRuntime when IOException encountered reading xml" 
    )
    public void tm_0BEE9A734( Test.Case tc ) {
    	// Have never encountered this case.
    	// If this occurs, try to reproduce and capture the test case here.
    	tc.assertPass();
    }
    
    
    
    
    /*	<Annotation>
     *	${ xml }
     *	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
     *	<?MyAnnotation The contents of the annotation, no quotes.?>
     *	<root></root>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Data is correct" 
    )
    public void tm_003E2BBCC( Test.Case tc ) {
    	VAR<String> theData = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Annotation" ) ) {
    		@Override public void processingInstruction( String target, String data ) throws SAXException {
    			super.processingInstruction( target, data );
    			theData.set( data );
    		}
    	}.parse();
    	
    	tc.assertEqual( "The contents of the annotation, no quotes.", theData.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0D61F04F4( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Annotation" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Processing instruction" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_07A7BFD1A( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Annotation" ) ) {
    		@Override public void processingInstruction( String target, String data ) throws SAXException {
    			super.processingInstruction( target, data );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(3, 60)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Target is correct" 
    )
    public void tm_0AF485B85( Test.Case tc ) {
    	VAR<String> theTarget = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Annotation" ) ) {
    		@Override public void processingInstruction( String target, String data ) throws SAXException {
    			super.processingInstruction( target, data );
    			theTarget.set( target );
    		}
    	}.parse();
    	
    	tc.assertEqual( "MyAnnotation", theTarget.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.setDocumentLocator(Locator)", 
    	description = "Called before startDocument()" 
    )
    public void tm_0B81629FE( Test.Case tc ) {
    	VAR<Boolean> startDocumentCalled = new VAR<>( false );
    	VAR<Boolean> locatorSetBefore = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void startDocument() throws SAXException {
    			super.startDocument();
    			startDocumentCalled.set( true );
    		}
    		@Override public void setDocumentLocator( Locator locator ) {
    			super.setDocumentLocator( locator );
    			if ( !startDocumentCalled.get() ) {
    				locatorSetBefore.set( true );
    			}
    		}
    	}.parse();
    	
    	tc.assertTrue( locatorSetBefore.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.setDocumentLocator(Locator)", 
    	description = "Parser registers non-null locator" 
    )
    public void tm_074D500CC( Test.Case tc ) {
    	VAR<Boolean> locatorNotNull = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "Location" ) ) {
    		@Override public void setDocumentLocator( Locator locator ) {
    			super.setDocumentLocator( locator );
    			locatorNotNull.set( locator != null );
    		}
    	}.parse();
    	
    	tc.assertTrue( locatorNotNull.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_05A79D777( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "CDATA" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "CDATA started" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Location is identified" 
    )
    public void tm_0A6ACC472( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "CDATA" ) ) {
    		@Override public void startCDATA() throws SAXException {
    			super.startCDATA();
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(6, 33)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Parser signals start of CDATA AFTER characters" 
    )
    public void tm_013B6230D( Test.Case tc ) {
    	VAR<Boolean> charactersCalled = new VAR<>( false );
    	VAR<Boolean> CDATACalledBefore = new VAR<>( false );
    	
    	new XMLHandler( this.getLines( "CDATA" ) ) {
    		@Override public void startCDATA() throws SAXException {
    			super.startCDATA();
    			if ( !charactersCalled.get() ) {
    				CDATACalledBefore.set( true );
    			}
    		}
    		@Override public void characters( char[] ch, int start, int length ) throws SAXException {
    			super.characters( ch, start, length );
    			charactersCalled.set( true );
    		}
    	}.parse();

    	// LESSON LEARNED
    	// Unexpected sequence of events
    	tc.assertFalse( CDATACalledBefore.get() );
    }
    
    
    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_06FA2D30B( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "DTD" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "DTD started: root" );
    	tc.addMessage( "PublicId: null" );
    	tc.addMessage( "SystemId: null" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0C70EE506( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "DTD" ) ) {
    		@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException {
    			super.startDTD( name, publicId, systemId );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(2, 16)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Name is root element" 
    )
    public void tm_0B9A9F53F( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "DTD" ) ) {
    		@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException {
    			super.startDTD( name, publicId, systemId );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertEqual( "root", theName.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDocument()", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_08DF666BC( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Document" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Document started: unknown" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDocument()", 
    	description = "Location is identified" 
    )
    public void tm_0885250E2( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Document" ) ) {
    		@Override public void startDocument() throws SAXException {
    			super.startDocument();
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(1, 1)", theLocation.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_06CCE369A( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "JsonValue" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "JsonValue started: A" );
    	tc.addMessage( "JsonValue started: B" );
    	tc.addMessage( "JsonValue started: C" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "See messages above" );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Location is identified" 
    )
    public void tm_0911D7840( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	tc.assertEqual( "(9, 7)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Name is not empty" 
    )
    public void tm_01BE2CDFB( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "JsonValue" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertNotEmpty( theName.get() );
    }

    
    /*	<Attribute>
     *	${ xml }
     *	<!DOCTYPE root [
     *		<!ELEMENT	root	EMPTY>
     *		<!ATTLIST	root	enumerated	(1|2|42)	#IMPLIED
     *							fixed		CDATA		#FIXED		"42"
     *							implied		CDATA		#IMPLIED
     *							required	CDATA		#REQUIRED>
     *	]>
     *	<root  enumerated = "42"  implied = "42"  required = "42"/>
     */
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for enumerated attribute" 
    )
    public void tm_0DCD9C552( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theValue.set( attributes.get( "enumerated" ) );
    		}
    	}.parse();
    	
    	tc.assertEqual( "42", theValue.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for fixed attribute" 
    )
    public void tm_0196155B0( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theValue.set( attributes.get( "fixed" ) );
    		}
    	}.parse();
    	
    	tc.assertEqual( "42", theValue.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for implied attribute" 
    )
    public void tm_06EC8C3A4( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theValue.set( attributes.get( "implied" ) );
    		}
    	}.parse();
    	
    	tc.assertEqual( "42", theValue.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for required attribute" 
    )
    public void tm_0A95C5EB1( Test.Case tc ) {
    	VAR<String> theValue = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String name, Map<String, String> attributes ) {
    			super.startElement( name, attributes );
    			theValue.set( attributes.get( "required" ) );
    		}
    	}.parse();
    	
    	tc.assertEqual( "42", theValue.get() );
    }

    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "Attributes is not null" 
    )
    public void tm_0CE1E9284( Test.Case tc ) {
    	VAR<Attributes> theAttributes = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			super.startElement( uri, localName, qName, atts );
    			theAttributes.set( atts );
    		}
    	}.parse();

    	tc.assertNonNull( theAttributes.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0329069E3( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Attribute" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "JsonValue started: root" );
    	tc.addMessage( "Local name:" );
    	tc.addMessage( "URI:" );
    	tc.addMessage( "Has attrubutes: true" );
    	handler.showContentEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "qName is not empty" 
    )
    public void tm_05FCB54D1( Test.Case tc ) {
    	VAR<String> theQName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Attribute" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			super.startElement( uri, localName, qName, atts );
    			theQName.set( qName );
    		}
    	}.parse();

    	tc.assertNotEmpty( theQName.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_030347950( Test.Case tc ) {
    	XMLHandler handler = new XMLHandler( this.getLines( "Entity" ) );
    	// TOGGLE
    	/* */ handler.parse(); tc.assertPass(); /*
    	tc.addMessage( "Should see parsing events including:" );
    	tc.addMessage( "Entity started: hgtg" );
    	handler.showLexicalEvents().parse();
    	tc.assertFail( "See messages above." );
    	/* */
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Location is identified" 
    )
    public void tm_005B6AC8B( Test.Case tc ) {
    	VAR<String> theLocation = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void startEntity( String name ) throws SAXException {
    			super.startEntity( name );
    			theLocation.set( this.getLocation().toString() );
    		}
    	}.parse();
    	
    	// LESSON LEARNED
    	// Strange location
    	tc.assertEqual( "(1, 1)", theLocation.get() );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Name is correct" 
    )
    public void tm_0E1FD043A( Test.Case tc ) {
    	VAR<String> theName = new VAR<>();
    	
    	new XMLHandler( this.getLines( "Entity" ) ) {
    		@Override public void startEntity( String name ) throws SAXException {
    			super.startEntity( name );
    			theName.set( name );
    		}
    	}.parse();
    	
    	tc.assertEqual( "hgtg", theName.get() );
    }

    
    
    @Test.Impl( 
    	member = "method: void XMLHandler.warning(SAXParseException)", 
    	description = "Unable to trigger..." 
    )
    public void tm_032218917( Test.Case tc ) {
    	tc.addMessage( "If warnings occur, add case" );
    	tc.assertPass();
    }	


	public static void main( String[] args ) {
		//* Toggle class results
		Test.eval( XMLHandler.class )
			.concurrent( false )
			.showDetails( true )
			.showProgress( false )
			.print();
		//*/
		
		/* Toggle package results
		sog.util.Concurrent.safeModeOff();
		Test.evalPackage( XMLHandler.class )
			.concurrent( true )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
		
		App.get().done();
	}
}
