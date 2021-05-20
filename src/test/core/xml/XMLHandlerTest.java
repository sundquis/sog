/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import sog.core.AppException;
import sog.core.Strings;
import sog.core.Test;
import sog.core.xml.XMLHandler;
import sog.util.Commented;
import sog.util.StreamReader;

/**
 * @author sundquis
 *
 */
public class XMLHandlerTest extends Test.Container {

	// Override event handler of interest, using accept() to assign the result
	private static abstract class Adapter<T> extends XMLHandler implements Supplier<T>, Consumer<T> {

		private static final Commented comment = new Commented() {};
		
		private static Reader getReader( String label ) {
			try {
				return new StreamReader( Adapter.comment.getCommentedLines( label ) );
			} catch ( IOException e ) {
				throw new AppException( e );
			}
		}
		
		private T result = null;
		
		private Adapter( String label ) {
			super( Adapter.getReader( label ) );
		}

		@Override public void accept( T result ) { this.result = result; }
		
		@Override public T get() { this.parse(); return this.result; }
		
		
		// Controls diagnostic feedback
		private static final boolean FEEDBACK = true;
		
		private void out( Object ... msg ) {
			if ( !FEEDBACK ) { return; }
			
			String message = ">>> " + this.getLocation() + " IN " + Test.Container.getFileLocation();
			for ( int i = 0; i < msg.length; i++ ) {
				message += ", " + Strings.toString( msg[i] );
			}
			System.out.println( message );
		}
		
		@Override public void warning( SAXParseException exception ) throws SAXException { 
			out( "WARN", exception );
		}

		@Override public void error( SAXParseException exception ) throws SAXException { 
			out( "ERROR", exception.getMessage() ); 
		}

		@Override public void fatalError( SAXParseException exception ) throws SAXException { 
			out( "FATAL", exception ); 
		}
		
	}
	
	// Test implementations
	
	
	// ATTRIBUTE	<?xml version="1.0" encoding="UTF-8"?>
	// ATTRIBUTE	<!DOCTYPE root [
	// ATTRIBUTE		<!ELEMENT 	root 	(child)* >
	// ATTRIBUTE		<!ATTLIST 	root
	// ATTRIBUTE			name	CDATA	#REQUIRED
	// ATTRIBUTE			age		CDATA	#IMPLIED
	// ATTRIBUTE			id		CDATA	#FIXED		"42"
	// ATTRIBUTE			father	(newton|leibnitz)	"leibnitz"
	// ATTRIBUTE		>
	// ATTRIBUTE		<!ELEMENT	child	EMPTY>
	// ATTRIBUTE	]>
	// ATTRIBUTE	
	// ATTRIBUTE	<root name="My name" age="56" father="newton">
	// ATTRIBUTE		<child />
	// ATTRIBUTE	</root>
	
	@Test.Impl( member = "public Map XMLHandler.attributesToMap(Attributes)", description = "Empty map returned when no attributes" )
	public void attributesToMap_EmptyMapReturnedWhenNoAttributes( Test.Case tc ) {
		Map<String, String> result = new Adapter<Map<String, String>>( "ATTRIBUTE" ) {
			@Override public void startElement( String name, Map<String, String> attributes ) {
				if ( name.equals("child") ) { this.accept( attributes ); }
			}
		}.get();
		tc.assertEqual( 0,  result.size() );
	}

	@Test.Impl( member = "public Map XMLHandler.attributesToMap(Attributes)", description = "Result is not null" )
	public void attributesToMap_ResultIsNotNull( Test.Case tc ) {
		Map<String, String> result = new Adapter<Map<String, String>>( "ATTRIBUTE" ) {
			@Override public void startElement( String name, Map<String, String> attributes ) {
				if ( "child".equals( name ) ) { this.accept( attributes ); }
			}
		}.get();
		tc.notNull( result );
	}
	
	@Test.Impl( member = "public void XMLHandler.startElement(String, String, String, Attributes)", description = "Attributes is not null" )
	public void startElement_AttributesIsNotNull( Test.Case tc ) {
		tc.notNull( new Adapter<Object>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				if ( "child".equals( qName ) ) { this.accept( atts ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Value correct for fixed attribute" )
	public void startElement_ValueCorrectForFixedAttribute( Test.Case tc ) {
		tc.assertEqual( "42", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("id") ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Value correct for implied attribute" )
	public void startElement_ValueCorrectForImpliedAttribute( Test.Case tc ) {
		tc.assertEqual( "56", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("age") ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Value correct for required attribute" )
	public void startElement_ValueCorrectForRequiredAttribute( Test.Case tc ) {
		tc.assertEqual( "My name", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("name") ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Value correct for enumerated" )
	public void startElement_ValueCorrectForFixedEnumerated( Test.Case tc ) {
		tc.assertEqual( "newton", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("father") ); }
			}
		}.get() );
	}

	
	
	
	// CONSTRUCTOR	
	
	@Test.Impl( member = "public XMLHandler(InputSource)", description = "Throws assertion error for null source" )
	public void XMLHandler_ThrowsAssertionErrorForNullSource( Test.Case tc ) {
		InputSource source = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( source );
	}

	@Test.Impl( member = "public XMLHandler(InputStream)", description = "Throws assertion error for null stream" )
	public void XMLHandler_ThrowsAssertionErrorForNullStream( Test.Case tc ) {
		InputStream stream = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( stream );
	}

	@Test.Impl( member = "public XMLHandler(Path)", description = "Throws NoSuchFileException if the file is missing" )
	public void XMLHandler_ThrowsNosuchfileexceptionIfTheFileIsMissing( Test.Case tc ) throws IOException {
		tc.expectError( NoSuchFileException.class );
		new XMLHandler( Paths.get( "BOGUS") );
	}

	@Test.Impl( member = "public XMLHandler(Path)", description = "Throws assertion error for null path" )
	public void XMLHandler_ThrowsAssertionErrorForNullPath( Test.Case tc ) throws IOException {
		Path path = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( path );
	}

	@Test.Impl( member = "public XMLHandler(Reader)", description = "Throws assertion error for null reader" )
	public void XMLHandler_ThrowsAssertionErrorForNullReader( Test.Case tc ) {
		Reader reader = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( reader );
	}
	
	
	
	
	// LOCATION	<!DOCTYPE root [
	// LOCATION	<!ELEMENT root EMPTY >
	// LOCATION	]>
	// LOCATION	<root></root>

	@Test.Impl( member = "public XMLHandler.Location XMLHandler.getLocation()", description = "Is not null" )
	public void getLocation_IsNotNull( Test.Case tc ) {
		tc.notNull( new Adapter<Object>( "LOCATION" ) {}.getLocation() );
	}

	@Test.Impl( member = "public XMLHandler.Location XMLHandler.getLocation()", description = "Location is unknown after parsing" )
	public void getLocation_LocationIsUnknownAfterParsing( Test.Case tc ) {
		Adapter<Object> a = new Adapter<Object>( "LOCATION" ) {};
		a.parse();
		tc.assertEqual( "(-1, -1)",  a.getLocation().toString() );
	}

	@Test.Impl( member = "public XMLHandler.Location XMLHandler.getLocation()", description = "Location is unknown before parsing" )
	public void getLocation_LocationIsUnknownBeforeParsing( Test.Case tc ) {
		Adapter<Object> a = new Adapter<Object>( "LOCATION" ) {};
		tc.assertEqual( "(-1, -1)",  a.getLocation().toString() );
	}
	
	
	
	
	// CONTENT	<?xml version="1.0" encoding="UTF-8"?>
	// CONTENT	<!DOCTYPE root [
	// CONTENT		<!ELEMENT 	root 	(child)* >
	// CONTENT		<!ELEMENT	child	(#PCDATA) >
	// CONTENT		<!ENTITY	HGTG	"The answer to the ultimate question..." >
	// CONTENT	]>
	// CONTENT	<root>    <?annotation This is an annotation?>
	// CONTENT		<child><![CDATA[Unparsed <data>data</data>]]></child>
	// CONTENT		<child>&HGTG;</child>
	// CONTENT		<child>Some text.</child>
	// CONTENT		<!-- Comment -->
	// CONTENT	</root>

	@Test.Impl( member = "public void XMLHandler.characters(char[], int, int)", description = "Location is identified" )
	public void characters_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(10, 21)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				this.accept( this.getLocation().toString() ); // Overwrites. Exit with last location
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.characters(char[], int, int)", description = "Parser uses to signal content" )
	public void characters_ParserUsesToSignalContent( Test.Case tc ) {
		tc.assertEqual( 3, new Adapter<Integer>( "CONTENT" ) {
			int count = 0;
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				this.count++; this.accept( count );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.comment(char[], int, int)", description = "Location is identified" )
	public void comment_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(11, 18)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void comment( char[] ch, int start, int length ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.comment(char[], int, int)", description = "Parser signals comments" )
	public void comment_ParserSignalsComments( Test.Case tc ) {
		tc.assertEqual( " Comment ", new Adapter<String>( "CONTENT" ) {
			@Override
			public void comment( char[] ch, int start, int length ) throws SAXException {
				this.accept( new String( ch, start, length  ) );
			}
		}.get() );
	}
	
	@Test.Impl( member = "public void XMLHandler.startCDATA()", description = "Location is identified" )
	public void startCDATA_LocationIsIdentified( Test.Case tc ) {
		// Thought this would be column 49 based on other locations. 
		// Location data is heuristic 
		tc.assertEqual( "(8, 47)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void startCDATA() throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startCDATA()", description = "Parser signals start of CDATA" )
	public void startCDATA_ParserSignalsStartOfCdata( Test.Case tc ) {
		tc.assertEqual( "Unparsed <data>data</data>", new Adapter<String>( "CONTENT" ) {
			boolean ready = false;
			@Override
			public void startCDATA() throws SAXException {
				this.ready = true;
			}
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				if ( this.ready ) {
					this.accept( new String( ch, start, length ) );
					this.ready = false;
				}
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endCDATA()", description = "Called after startCDATA()" )
	public void endCDATA_CalledAfterStartcdata( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			boolean startEncountered = false;
			@Override public void startCDATA() { this.startEncountered = true; }
			@Override public void endCDATA() { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endCDATA()", description = "Location is identified" )
	public void endCDATA_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(8, 47)", new Adapter<String>( "CONTENT" ) {
			@Override public void endCDATA() throws SAXException { 
				this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startEntity(String)", description = "Location is identified" )
	public void startEntity_LocationIsIdentified( Test.Case tc ) {
		// Hmmm... It appears the location of an entity is not correctly identified
		// Should be (9, 17) or (9, 15)
		// The {@code name} arg is correct: "HGTG"
		tc.assertEqual( "(1, 1)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void startEntity( String name ) throws SAXException { 
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startEntity(String)", description = "Name is correct" )
	public void startEntity_NameIsCorrect( Test.Case tc ) {
		tc.assertEqual( "HGTG", new Adapter<String>( "CONTENT" ) {
			@Override public void startEntity( String name ) throws SAXException { this.accept( name ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startEntity(String)", description = "Parser signals use of entity" )
	public void startEntity_ParserSignalsUseOfEntity( Test.Case tc ) {
		tc.assertEqual( "The answer to the ultimate question...", new Adapter<String>( "CONTENT" ) {
			boolean ready = false;
			@Override
			public void startEntity( String name ) throws SAXException {
				this.ready = true;
			}
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				if ( this.ready ) {
					this.accept( new String( ch, start, length ) );
					this.ready = false;
				}
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endEntity(String)", description = "Called after startEntity()" )
	public void endEntity_CalledAfterStartentity( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			boolean startEncountered = false;
			@Override public void startEntity( String name ) { this.startEncountered = true; }
			@Override public void endEntity( String name ) { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endEntity(String)", description = "Location is identified" )
	public void endEntity_LocationIsIdentified( Test.Case tc ) {
		// Curiouser and curiouser...
		// Column 39 is one past the end of the first row
		// The {@code name} arg is correct: "HGTG"
		tc.assertEqual( "(1, 39)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void endEntity( String name ) throws SAXException { 
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endEntity(String)", description = "Name is consistent" )
	public void endEntity_NameIsConsistent( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			String startName = null;
			@Override public void startEntity( String name ) { this.startName = name; }
			@Override public void endEntity( String name ) { this.accept( this.startName.equals( name ) ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.ignorableWhitespace(char[], int, int)", description = "Location is identified" )
	public void ignorableWhitespace_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(7, 12)", new Adapter<String>( "CONTENT" ) {
			boolean foundFirst = false;
			@Override
			public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
				if( !this.foundFirst ) { 
					this.accept( this.getLocation().toString() );
					this.foundFirst = true;
				}
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.processingInstruction(String, String)", description = "Data is correct" )
	public void processingInstruction_DataIsCorrect( Test.Case tc ) {
		tc.assertEqual( "This is an annotation",  new Adapter<String>( "CONTENT" ) {
			@Override
			public void processingInstruction( String target, String data ) throws SAXException {
				this.accept( data );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.processingInstruction(String, String)", description = "Location is identified" )
	public void processingInstruction_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(7, 47)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void processingInstruction( String target, String data ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.processingInstruction(String, String)", description = "Target is correct" )
	public void processingInstruction_TargetIsCorrect( Test.Case tc ) {
		tc.assertEqual( "annotation",  new Adapter<String>( "CONTENT" ) {
			@Override
			public void processingInstruction( String target, String data ) throws SAXException {
				this.accept( target );
			}
		}.get() );
	}

	
	
	
	// Note: Only exercising CDATA here
	
	// ELEMENT	<?xml version="1.0" encoding="UTF-8"?>
	// ELEMENT	<!DOCTYPE root [
	// ELEMENT		<!ELEMENT 	root 	(A | B)* >
	// ELEMENT		<!ELEMENT	A	(A | B)* >
	// ELEMENT		<!ELEMENT	B	(A | B)* >
	// ELEMENT	]>
	// ELEMENT	
	// ELEMENT	<root>
	// ELEMENT		<A>
	// ELEMENT			<B></B>
	// ELEMENT			<A>
	// ELEMENT				<B>
	// ELEMENT				</B>
	// ELEMENT			</A>
	// ELEMENT		</A>
	// ELEMENT		<B>
	// ELEMENT			<A>
	// ELEMENT			</A>
	// ELEMENT			<B></B>
	// ELEMENT		</B>
	// ELEMENT	</root>

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Location is identified" )
	public void startElement_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(8, 7)",  new Adapter<String>( "ELEMENT" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( this.getLocation().toString() ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, Map)", description = "Name is not empty" )
	public void startElement_NameIsNotEmpty( Test.Case tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				tc.notEmpty( name );
			}
		}.get();
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, String, String, Attributes)", description = "Parser signals start of element processing" )
	public void startElement_ParserSignalsStartOfElementProcessing( Test.Case tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
	}

	@Test.Impl( member = "public void XMLHandler.startElement(String, String, String, Attributes)", description = "qName is not null" )
	public void startElement_QnameIsNotNull( Test.Case tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
	}
	
	@Test.Impl( member = "public void XMLHandler.endElement(String)", description = "Location is identified" )
	public void endElement_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(21, 8)",  new Adapter<String>( "ELEMENT" ) {
			@Override
			public void endElement( String name ) {
				if ( "root".equals( name ) ) { this.accept( this.getLocation().toString() ); }
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endElement(String)", description = "Name is not empty" )
	public void endElement_NameIsNotEmpty( Test.Case tc ) {
		new Adapter<Boolean>( "ELEMENT" ) {
			@Override
			public void endElement( String name ) {
				tc.notEmpty( name );
			}
		}.get();
	}

	@Test.Impl( member = "public void XMLHandler.endElement(String)", description = "Elements closed in LIFO order" )
	public void endElement_ElementsClosedInLifoOrder( Test.Case tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			Stack<String> names = new Stack<>();
			@Override public void startElement( String name, Map<String, String> attributes ) { this.names.push( name ); }
			@Override public void endElement( String name ) { tc.assertEqual( name,  names.pop() ); }
		}.get();
	}
	
	@Test.Impl( member = "public void XMLHandler.endElement(String, String, String)", description = "qName is not empty" )
	public void endElement_QnameIsNotEmpty( Test.Case tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void endElement( String uri, String localName, String qName ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
	}


	
	
	
	// STRUCTURE	<!DOCTYPE	root	[
	// STRUCTURE		<!ELEMENT	root	ANY >
	// STRUCTURE	]>
	// STRUCTURE	
	// STRUCTURE	<root>
	// STRUCTURE	</root>

	@Test.Impl( member = "public void XMLHandler.setDocumentLocator(Locator)", description = "Called before startDocument()" )
	public void setDocumentLocator_CalledBeforeStartdocument( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean locatorSet = false;
			@Override public void setDocumentLocator( Locator locator ) { this.locatorSet = true; }
			@Override public void startDocument() throws SAXException { this.accept( this.locatorSet ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.setDocumentLocator(Locator)", description = "Parser registers non-null locator" )
	public void setDocumentLocator_ParserRegistersNonNullLocator( Test.Case tc ) {
		new Adapter<Object>( "STRUCTURE" ) {
			@Override public void setDocumentLocator( Locator locator ) { tc.notNull( locator ); }
		};
	}
	
	@Test.Impl( member = "public void XMLHandler.startDocument()", description = "Location is identified" )
	public void startDocument_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(1, 1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void startDocument() throws SAXException { 
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endDocument()", description = "Called after startDocument()" )
	public void endDocument_CalledAfterStartdocument( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean startEncountered = false;
			@Override public void startDocument() throws SAXException { this.startEncountered = true; }
			@Override public void endDocument() throws SAXException { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endDocument()", description = "Location is identified" )
	public void endDocument_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(-1, -1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void endDocument() throws SAXException { this.accept( this.getLocation().toString() ); }
		}.get() );
	}
	
	@Test.Impl( member = "public void XMLHandler.startDTD(String, String, String)", description = "Location is identified" )
	public void startDTD_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(1, 16)",  new Adapter<String>( "STRUCTURE" ) {
			@Override
			public void startDTD( String name, String publicId, String systemId ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.startDTD(String, String, String)", description = "Name is root element" )
	public void startDTD_NameIsRootElement( Test.Case tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "STRUCTURE" ) {
			@Override
			public void startDTD( String name, String publicId, String systemId ) throws SAXException {
				this.accept( name );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endDTD()", description = "Called after startDTD()" )
	public void endDTD_CalledAfterStartdtd( Test.Case tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean startEncountered = false;
			@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException { this.startEncountered = true; }
			@Override public void endDTD() throws SAXException { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.endDTD()", description = "Location is identified" )
	public void endDTD_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(3, 1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void endDTD() throws SAXException { this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	
	
	
	// ERRORS

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Location is identified" )
	public void error_LocationIsIdentified( Test.Case tc ) {
		// ERR-POS	<!DOCTYPE	root [ <!ELEMENT root EMPTY > ]>
		// ERR-POS	<root> </root> <!-- Err line 2 column ??? -->
		tc.assertEqual( "(2, 15)",  new Adapter<String>( "ERR-POS" ) {
			@Override public void error( SAXParseException exception ) { this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered for changing #FIXED attribute" )
	public void error_TrigeredForChangingFixedAttribute( Test.Case tc ) {
		// ERR-FIXED	<!DOCTYPE root [ <!ELEMENT root EMPTY> <!ATTLIST root id CDATA #FIXED "42"> ]>
		// ERR-FIXED	<root id="43" />
		tc.notNull( new Adapter<SAXException>( "ERR-FIXED" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered for missing DTD" )
	public void error_TrigeredForMissingDtd( Test.Case tc ) {
		// ERR-NO-DTD	<root></root>
		tc.notNull( new Adapter<SAXException>( "ERR-NO-DTD" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered when element does not match declared type" )
	public void error_TrigeredWhenElementDoesNotMatchDeclaredType( Test.Case tc ) {
		// ERR-MISMATCH	<!DOCTYPE root [ <!ELEMENT root (child)*> <!ELEMENT child ANY> ]>
		// ERR-MISMATCH	<root>Illegal</root>
		tc.notNull( new Adapter<SAXException>( "ERR-MISMATCH" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}
	
	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered for missing requiured attribute" )
	public void error_TrigeredForMissingRequiuredAttribute( Test.Case tc ) {
		// ERR-REQUIRED	<!DOCTYPE root [ <!ELEMENT root ANY> <!ATTLIST root id CDATA #REQUIRED> ]>
		// ERR-REQUIRED	<root />
		tc.notNull( new Adapter<SAXException>( "ERR-REQUIRED" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered for undeclared attributes" )
	public void error_TrigeredForUndeclaredAttributes( Test.Case tc ) {
		// ERR-UNDECLARED-ATT	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
		// ERR-UNDECLARED-ATT	<root not="allowed"></root>
		tc.notNull( new Adapter<SAXException>( "ERR-UNDECLARED-ATT" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.error(SAXParseException)", description = "Trigered for undeclared elements" )
	public void error_TrigeredForUndeclaredElements( Test.Case tc ) {
		// ERR-UNDECLARED-ELT	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
		// ERR-UNDECLARED-ELT	<root><illegal></illegal></root>
		tc.notNull( new Adapter<SAXException>( "ERR-UNDECLARED-ELT" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.fatalError(SAXParseException)", description = "Excpetion is thrown after signaling" )
	public void fatalError_ExcpetionIsThrownAfterSignaling( Test.Case tc ) {
		// FATAL-THROW	<!DOGTYPE woof [ <!ELEMENT woof CFATA EMPTU> ]>
		Adapter<String> adapter = new Adapter<String>( "FATAL-THROW" ) {
			String msg = "";
			@Override public void fatalError( SAXParseException exception ) { this.msg = "Got error"; }
			@Override public String get() { return this.msg; }
		};
		try {
			adapter.parse();
			tc.addMessage( "Expected SAXParseException" ).fail();
		} catch ( AppException e ) {
			tc.assertEqual( "Got error", adapter.get() );
			tc.assertEqual( SAXParseException.class, e.getCause().getClass() );
		}
	}

	@Test.Impl( member = "public void XMLHandler.fatalError(SAXParseException)", description = "Location is identified" )
	public void fatalError_LocationIsIdentified( Test.Case tc ) {
		Adapter<String> adapter = new Adapter<String>( "FATAL-THROW" ) {
			String result = "";
			@Override public void fatalError( SAXParseException exception ) { this.result = this.getLocation().toString(); }
			@Override public String get() { return this.result; }
		};
		try {
			adapter.parse();
			tc.addMessage( "Expected SAXParseException" ).fail();
		} catch ( AppException e ) {
			tc.assertEqual( "(1, 3)",  adapter.get() );
		}
	}

	@Test.Impl( member = "public void XMLHandler.fatalError(SAXParseException)", description = "Trigered for illegal DTD structure" )
	public void fatalError_TrigeredForIllegalDtdStructure( Test.Case tc ) {
		// FATAL-MALFORMED	<!DOCTYPE root [ <!ELEMENT root ANY> <!ATTLIST root att CDATA> ]>
		// FATAL-MALFORMED	<root>Attribute missing type</root>
		Adapter<String> adapter = new Adapter<String>( "FATAL-MALFORMED" ) {
			String msg = "";
			@Override public void fatalError( SAXParseException exception ) { this.msg = "Got error"; }
			@Override public String get() { return this.msg; }
		};
		try {
			adapter.parse();
			tc.addMessage( "Expected SAXParseException" ).fail();
		} catch ( AppException e ) {
			tc.assertEqual( "Got error",  adapter.get() );
		}
	}

	@Test.Impl( member = "public void XMLHandler.fatalError(SAXParseException)", description = "Trigered for undeclared entity" )
	public void fatalError_TrigeredForUndeclaredEntity( Test.Case tc ) {
		// ERR-UNDECLARED-ENT	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
		// ERR-UNDECLARED-ENT	<root>&undeclared;</root>
		Adapter<String> adapter = new Adapter<String>( "ERR-UNDECLARED-ENT" ) {
			String msg = "";
			@Override public void fatalError( SAXParseException exception ) { this.msg = "Got error"; }
			@Override public String get() { return this.msg; }
		};
		try {
			adapter.parse();
			tc.addMessage( "Expected SAXParseException" ).fail();
		} catch ( AppException e ) {
			tc.assertEqual( "Got error",  adapter.get() );
		}
	}
	
	
	
	
	// DTD	<!DOCTYPE root [
	// DTD		<!ENTITY pf "proof">
	// DTD		<!ELEMENT root ANY>
	// DTD		<!ATTLIST root age CDATA #FIXED "45">
	// DTD	]>
	// DTD	<root>&pf;</root>

	@Test.Impl( member = "public void XMLHandler.internalEntityDecl(String, String)", description = "Location is identified" )
	public void internalEntityDecl_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(2, 22)",  new Adapter<String>( "DTD" ) {
			@Override
			public void internalEntityDecl( String name, String value ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.internalEntityDecl(String, String)", description = "Name is correct" )
	public void internalEntityDecl_NameIsCorrect( Test.Case tc ) {
		tc.assertEqual( "pf",  new Adapter<String>( "DTD" ) {
			@Override public void internalEntityDecl( String name, String value ) throws SAXException { 
				this.accept( name );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.internalEntityDecl(String, String)", description = "Value is correct" )
	public void internalEntityDecl_ValueIsCorrect( Test.Case tc ) {
		tc.assertEqual( "proof",  new Adapter<String>( "DTD" ) {
			@Override public void internalEntityDecl( String name, String value ) throws SAXException { 
				this.accept( value );
			}
		}.get() );
	}
	
	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Location is identified" )
	public void attributeDecl_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(4, 38)",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Attribute name is not empty" )
	public void attributeDecl_AttributeNameIsNotEmpty( Test.Case tc ) {
		tc.assertEqual( "age",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( aName );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Element name is not empty" )
	public void attributeDecl_ElementNameIsNotEmpty( Test.Case tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( eName );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Mode is correct" )
	public void attributeDecl_ModeIsCorrect( Test.Case tc ) {
		tc.assertEqual( "#FIXED",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( mode );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Type is correct" )
	public void attributeDecl_TypeIsCorrect( Test.Case tc ) {
		tc.assertEqual( "CDATA",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( type );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.attributeDecl(String, String, String, String, String)", description = "Value is correct when supplied in DTD" )
	public void attributeDecl_ValueIsCorrectWhenSuppliedInDtd( Test.Case tc ) {
		tc.assertEqual( "45",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( value );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.elementDecl(String, String)", description = "Location is identified" )
	public void elementDecl_LocationIsIdentified( Test.Case tc ) {
		tc.assertEqual( "(3, 21)",  new Adapter<String>( "DTD" ) {
			@Override
			public void elementDecl( String name, String model ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( member = "public void XMLHandler.elementDecl(String, String)", description = "Name is not empty" )
	public void elementDecl_NameIsNotEmpty( Test.Case tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "DTD" ) {
			@Override
			public void elementDecl( String name, String model ) throws SAXException {
				this.accept( name );
			}
		}.get() );
	}

	

	
}
