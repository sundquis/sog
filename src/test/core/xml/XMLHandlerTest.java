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
import sog.core.TestCase;
import sog.core.TestContainer;
import sog.core.xml.XMLHandler;
import sog.util.Commented;
import sog.util.StreamReader;

/**
 * @author sundquis
 *
 */
public class XMLHandlerTest implements TestContainer {

	@Override public Class<?> subjectClass() { return XMLHandler.class; }

	
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
			
			String message = ">>> " + this.getLocation() + " IN " + TestContainer.location();
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
	
	@Test.Impl( src = "public Map XMLHandler.attributesToMap(Attributes)", desc = "Empty map returned when no attributes" )
	public void attributesToMap_EmptyMapReturnedWhenNoAttributes( TestCase tc ) {
		Map<String, String> result = new Adapter<Map<String, String>>( "ATTRIBUTE" ) {
			@Override public void startElement( String name, Map<String, String> attributes ) {
				if ( name.equals("child") ) { this.accept( attributes ); }
			}
		}.get();
		tc.assertEqual( 0,  result.size() );
	}

	@Test.Impl( src = "public Map XMLHandler.attributesToMap(Attributes)", desc = "Result is not null" )
	public void attributesToMap_ResultIsNotNull( TestCase tc ) {
		Map<String, String> result = new Adapter<Map<String, String>>( "ATTRIBUTE" ) {
			@Override public void startElement( String name, Map<String, String> attributes ) {
				if ( "child".equals( name ) ) { this.accept( attributes ); }
			}
		}.get();
		tc.notNull( result );
	}
	
	@Test.Impl( src = "public void XMLHandler.startElement(String, String, String, Attributes)", desc = "Attributes is not null" )
	public void startElement_AttributesIsNotNull( TestCase tc ) {
		tc.notNull( new Adapter<Object>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				if ( "child".equals( qName ) ) { this.accept( atts ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Value correct for fixed attribute" )
	public void startElement_ValueCorrectForFixedAttribute( TestCase tc ) {
		tc.assertEqual( "42", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("id") ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Value correct for implied attribute" )
	public void startElement_ValueCorrectForImpliedAttribute( TestCase tc ) {
		tc.assertEqual( "56", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("age") ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Value correct for required attribute" )
	public void startElement_ValueCorrectForRequiredAttribute( TestCase tc ) {
		tc.assertEqual( "My name", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("name") ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Value correct for enumerated" )
	public void startElement_ValueCorrectForFixedEnumerated( TestCase tc ) {
		tc.assertEqual( "newton", new Adapter<String>( "ATTRIBUTE" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( attributes.get("father") ); }
			}
		}.get() );
	}

	
	
	
	// CONSTRUCTOR	
	
	@Test.Impl( src = "public XMLHandler(InputSource)", desc = "Throws assertion error for null source" )
	public void XMLHandler_ThrowsAssertionErrorForNullSource( TestCase tc ) {
		InputSource source = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( source );
	}

	@Test.Impl( src = "public XMLHandler(InputStream)", desc = "Throws assertion error for null stream" )
	public void XMLHandler_ThrowsAssertionErrorForNullStream( TestCase tc ) {
		InputStream stream = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( stream );
	}

	@Test.Impl( src = "public XMLHandler(Path)", desc = "Throws NoSuchFileException if the file is missing" )
	public void XMLHandler_ThrowsNosuchfileexceptionIfTheFileIsMissing( TestCase tc ) throws IOException {
		tc.expectError( NoSuchFileException.class );
		new XMLHandler( Paths.get( "BOGUS") );
	}

	@Test.Impl( src = "public XMLHandler(Path)", desc = "Throws assertion error for null path" )
	public void XMLHandler_ThrowsAssertionErrorForNullPath( TestCase tc ) throws IOException {
		Path path = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( path );
	}

	@Test.Impl( src = "public XMLHandler(Reader)", desc = "Throws assertion error for null reader" )
	public void XMLHandler_ThrowsAssertionErrorForNullReader( TestCase tc ) {
		Reader reader = null;
		tc.expectError( AssertionError.class );
		new XMLHandler( reader );
	}
	
	
	
	
	// LOCATION	<!DOCTYPE root [
	// LOCATION	<!ELEMENT root EMPTY >
	// LOCATION	]>
	// LOCATION	<root></root>

	@Test.Impl( src = "public XMLHandler.Location XMLHandler.getLocation()", desc = "Is not null" )
	public void getLocation_IsNotNull( TestCase tc ) {
		tc.notNull( new Adapter<Object>( "LOCATION" ) {}.getLocation() );
	}

	@Test.Impl( src = "public XMLHandler.Location XMLHandler.getLocation()", desc = "Location is unknown after parsing" )
	public void getLocation_LocationIsUnknownAfterParsing( TestCase tc ) {
		Adapter<Object> a = new Adapter<Object>( "LOCATION" ) {};
		a.parse();
		tc.assertEqual( "(-1, -1)",  a.getLocation().toString() );
	}

	@Test.Impl( src = "public XMLHandler.Location XMLHandler.getLocation()", desc = "Location is unknown before parsing" )
	public void getLocation_LocationIsUnknownBeforeParsing( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.characters(char[], int, int)", desc = "Location is identified" )
	public void characters_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(10, 21)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				this.accept( this.getLocation().toString() ); // Overwrites. Exit with last location
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.characters(char[], int, int)", desc = "Parser uses to signal content" )
	public void characters_ParserUsesToSignalContent( TestCase tc ) {
		tc.assertEqual( 3, new Adapter<Integer>( "CONTENT" ) {
			int count = 0;
			@Override
			public void characters( char[] ch, int start, int length ) throws SAXException {
				this.count++; this.accept( count );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.comment(char[], int, int)", desc = "Location is identified" )
	public void comment_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(11, 18)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void comment( char[] ch, int start, int length ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.comment(char[], int, int)", desc = "Parser signals comments" )
	public void comment_ParserSignalsComments( TestCase tc ) {
		tc.assertEqual( " Comment ", new Adapter<String>( "CONTENT" ) {
			@Override
			public void comment( char[] ch, int start, int length ) throws SAXException {
				this.accept( new String( ch, start, length  ) );
			}
		}.get() );
	}
	
	@Test.Impl( src = "public void XMLHandler.startCDATA()", desc = "Location is identified" )
	public void startCDATA_LocationIsIdentified( TestCase tc ) {
		// Thought this would be column 49 based on other locations. 
		// Location data is heuristic 
		tc.assertEqual( "(8, 47)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void startCDATA() throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startCDATA()", desc = "Parser signals start of CDATA" )
	public void startCDATA_ParserSignalsStartOfCdata( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.endCDATA()", desc = "Called after startCDATA()" )
	public void endCDATA_CalledAfterStartcdata( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			boolean startEncountered = false;
			@Override public void startCDATA() { this.startEncountered = true; }
			@Override public void endCDATA() { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endCDATA()", desc = "Location is identified" )
	public void endCDATA_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(8, 47)", new Adapter<String>( "CONTENT" ) {
			@Override public void endCDATA() throws SAXException { 
				this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startEntity(String)", desc = "Location is identified" )
	public void startEntity_LocationIsIdentified( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.startEntity(String)", desc = "Name is correct" )
	public void startEntity_NameIsCorrect( TestCase tc ) {
		tc.assertEqual( "HGTG", new Adapter<String>( "CONTENT" ) {
			@Override public void startEntity( String name ) throws SAXException { this.accept( name ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startEntity(String)", desc = "Parser signals use of entity" )
	public void startEntity_ParserSignalsUseOfEntity( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.endEntity(String)", desc = "Called after startEntity()" )
	public void endEntity_CalledAfterStartentity( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			boolean startEncountered = false;
			@Override public void startEntity( String name ) { this.startEncountered = true; }
			@Override public void endEntity( String name ) { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endEntity(String)", desc = "Location is identified" )
	public void endEntity_LocationIsIdentified( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.endEntity(String)", desc = "Name is consistent" )
	public void endEntity_NameIsConsistent( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "CONTENT" ) {
			String startName = null;
			@Override public void startEntity( String name ) { this.startName = name; }
			@Override public void endEntity( String name ) { this.accept( this.startName.equals( name ) ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.ignorableWhitespace(char[], int, int)", desc = "Location is identified" )
	public void ignorableWhitespace_LocationIsIdentified( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.processingInstruction(String, String)", desc = "Data is correct" )
	public void processingInstruction_DataIsCorrect( TestCase tc ) {
		tc.assertEqual( "This is an annotation",  new Adapter<String>( "CONTENT" ) {
			@Override
			public void processingInstruction( String target, String data ) throws SAXException {
				this.accept( data );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.processingInstruction(String, String)", desc = "Location is identified" )
	public void processingInstruction_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(7, 47)", new Adapter<String>( "CONTENT" ) {
			@Override
			public void processingInstruction( String target, String data ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.processingInstruction(String, String)", desc = "Target is correct" )
	public void processingInstruction_TargetIsCorrect( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Location is identified" )
	public void startElement_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(8, 7)",  new Adapter<String>( "ELEMENT" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				if ( "root".equals( name ) ) { this.accept( this.getLocation().toString() ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, Map)", desc = "Name is not empty" )
	public void startElement_NameIsNotEmpty( TestCase tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String name, Map<String, String> attributes ) {
				tc.notEmpty( name );
			}
		}.get();
		tc.pass();
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, String, String, Attributes)", desc = "Parser signals start of element processing" )
	public void startElement_ParserSignalsStartOfElementProcessing( TestCase tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
		tc.pass();
	}

	@Test.Impl( src = "public void XMLHandler.startElement(String, String, String, Attributes)", desc = "qName is not null" )
	public void startElement_QnameIsNotNull( TestCase tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
		tc.pass();
	}
	
	@Test.Impl( src = "public void XMLHandler.endElement(String)", desc = "Location is identified" )
	public void endElement_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(21, 8)",  new Adapter<String>( "ELEMENT" ) {
			@Override
			public void endElement( String name ) {
				if ( "root".equals( name ) ) { this.accept( this.getLocation().toString() ); }
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endElement(String)", desc = "Name is not empty" )
	public void endElement_NameIsNotEmpty( TestCase tc ) {
		new Adapter<Boolean>( "ELEMENT" ) {
			@Override
			public void endElement( String name ) {
				tc.notEmpty( name );
			}
		}.get();
		tc.pass();
	}

	@Test.Impl( src = "public void XMLHandler.endElement(String)", desc = "Elements closed in LIFO order" )
	public void endElement_ElementsClosedInLifoOrder( TestCase tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			Stack<String> names = new Stack<>();
			@Override public void startElement( String name, Map<String, String> attributes ) { this.names.push( name ); }
			@Override public void endElement( String name ) { tc.assertEqual( name,  names.pop() ); }
		}.get();
		tc.pass();
	}
	
	@Test.Impl( src = "public void XMLHandler.endElement(String, String, String)", desc = "qName is not empty" )
	public void endElement_QnameIsNotEmpty( TestCase tc ) {
		new Adapter<Object>( "ELEMENT" ) {
			@Override
			public void endElement( String uri, String localName, String qName ) throws SAXException {
				tc.notEmpty( qName );
			}
		}.get();
		tc.pass();
	}


	
	
	
	// STRUCTURE	<!DOCTYPE	root	[
	// STRUCTURE		<!ELEMENT	root	ANY >
	// STRUCTURE	]>
	// STRUCTURE	
	// STRUCTURE	<root>
	// STRUCTURE	</root>

	@Test.Impl( src = "public void XMLHandler.setDocumentLocator(Locator)", desc = "Called before startDocument()" )
	public void setDocumentLocator_CalledBeforeStartdocument( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean locatorSet = false;
			@Override public void setDocumentLocator( Locator locator ) { this.locatorSet = true; }
			@Override public void startDocument() throws SAXException { this.accept( this.locatorSet ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.setDocumentLocator(Locator)", desc = "Parser registers non-null locator" )
	public void setDocumentLocator_ParserRegistersNonNullLocator( TestCase tc ) {
		new Adapter<Object>( "STRUCTURE" ) {
			@Override public void setDocumentLocator( Locator locator ) { tc.notNull( locator ); }
		};
		tc.pass();
	}
	
	@Test.Impl( src = "public void XMLHandler.startDocument()", desc = "Location is identified" )
	public void startDocument_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(1, 1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void startDocument() throws SAXException { 
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endDocument()", desc = "Called after startDocument()" )
	public void endDocument_CalledAfterStartdocument( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean startEncountered = false;
			@Override public void startDocument() throws SAXException { this.startEncountered = true; }
			@Override public void endDocument() throws SAXException { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endDocument()", desc = "Location is identified" )
	public void endDocument_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(-1, -1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void endDocument() throws SAXException { this.accept( this.getLocation().toString() ); }
		}.get() );
	}
	
	@Test.Impl( src = "public void XMLHandler.startDTD(String, String, String)", desc = "Location is identified" )
	public void startDTD_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(1, 16)",  new Adapter<String>( "STRUCTURE" ) {
			@Override
			public void startDTD( String name, String publicId, String systemId ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.startDTD(String, String, String)", desc = "Name is root element" )
	public void startDTD_NameIsRootElement( TestCase tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "STRUCTURE" ) {
			@Override
			public void startDTD( String name, String publicId, String systemId ) throws SAXException {
				this.accept( name );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endDTD()", desc = "Called after startDTD()" )
	public void endDTD_CalledAfterStartdtd( TestCase tc ) {
		tc.assertTrue( new Adapter<Boolean>( "STRUCTURE" ) {
			boolean startEncountered = false;
			@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException { this.startEncountered = true; }
			@Override public void endDTD() throws SAXException { this.accept( this.startEncountered ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.endDTD()", desc = "Location is identified" )
	public void endDTD_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(3, 1)",  new Adapter<String>( "STRUCTURE" ) {
			@Override public void endDTD() throws SAXException { this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	
	
	
	// ERRORS

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Location is identified" )
	public void error_LocationIsIdentified( TestCase tc ) {
		// ERR-POS	<!DOCTYPE	root [ <!ELEMENT root EMPTY > ]>
		// ERR-POS	<root> </root> <!-- Err line 2 column ??? -->
		tc.assertEqual( "(2, 15)",  new Adapter<String>( "ERR-POS" ) {
			@Override public void error( SAXParseException exception ) { this.accept( this.getLocation().toString() ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered for changing #FIXED attribute" )
	public void error_TrigeredForChangingFixedAttribute( TestCase tc ) {
		// ERR-FIXED	<!DOCTYPE root [ <!ELEMENT root EMPTY> <!ATTLIST root id CDATA #FIXED "42"> ]>
		// ERR-FIXED	<root id="43" />
		tc.notNull( new Adapter<SAXException>( "ERR-FIXED" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered for missing DTD" )
	public void error_TrigeredForMissingDtd( TestCase tc ) {
		// ERR-NO-DTD	<root></root>
		tc.notNull( new Adapter<SAXException>( "ERR-NO-DTD" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered when element does not match declared type" )
	public void error_TrigeredWhenElementDoesNotMatchDeclaredType( TestCase tc ) {
		// ERR-MISMATCH	<!DOCTYPE root [ <!ELEMENT root (child)*> <!ELEMENT child ANY> ]>
		// ERR-MISMATCH	<root>Illegal</root>
		tc.notNull( new Adapter<SAXException>( "ERR-MISMATCH" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}
	
	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered for missing requiured attribute" )
	public void error_TrigeredForMissingRequiuredAttribute( TestCase tc ) {
		// ERR-REQUIRED	<!DOCTYPE root [ <!ELEMENT root ANY> <!ATTLIST root id CDATA #REQUIRED> ]>
		// ERR-REQUIRED	<root />
		tc.notNull( new Adapter<SAXException>( "ERR-REQUIRED" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered for undeclared attributes" )
	public void error_TrigeredForUndeclaredAttributes( TestCase tc ) {
		// ERR-UNDECLARED-ATT	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
		// ERR-UNDECLARED-ATT	<root not="allowed"></root>
		tc.notNull( new Adapter<SAXException>( "ERR-UNDECLARED-ATT" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.error(SAXParseException)", desc = "Trigered for undeclared elements" )
	public void error_TrigeredForUndeclaredElements( TestCase tc ) {
		// ERR-UNDECLARED-ELT	<!DOCTYPE root [ <!ELEMENT root ANY> ]>
		// ERR-UNDECLARED-ELT	<root><illegal></illegal></root>
		tc.notNull( new Adapter<SAXException>( "ERR-UNDECLARED-ELT" ) {
			@Override public void error( SAXParseException exception ) { this.accept( exception ); }
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.fatalError(SAXParseException)", desc = "Excpetion is thrown after signaling" )
	public void fatalError_ExcpetionIsThrownAfterSignaling( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.fatalError(SAXParseException)", desc = "Location is identified" )
	public void fatalError_LocationIsIdentified( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.fatalError(SAXParseException)", desc = "Trigered for illegal DTD structure" )
	public void fatalError_TrigeredForIllegalDtdStructure( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.fatalError(SAXParseException)", desc = "Trigered for undeclared entity" )
	public void fatalError_TrigeredForUndeclaredEntity( TestCase tc ) {
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

	@Test.Impl( src = "public void XMLHandler.internalEntityDecl(String, String)", desc = "Location is identified" )
	public void internalEntityDecl_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(2, 22)",  new Adapter<String>( "DTD" ) {
			@Override
			public void internalEntityDecl( String name, String value ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.internalEntityDecl(String, String)", desc = "Name is correct" )
	public void internalEntityDecl_NameIsCorrect( TestCase tc ) {
		tc.assertEqual( "pf",  new Adapter<String>( "DTD" ) {
			@Override public void internalEntityDecl( String name, String value ) throws SAXException { 
				this.accept( name );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.internalEntityDecl(String, String)", desc = "Value is correct" )
	public void internalEntityDecl_ValueIsCorrect( TestCase tc ) {
		tc.assertEqual( "proof",  new Adapter<String>( "DTD" ) {
			@Override public void internalEntityDecl( String name, String value ) throws SAXException { 
				this.accept( value );
			}
		}.get() );
	}
	
	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Location is identified" )
	public void attributeDecl_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(4, 38)",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Attribute name is not empty" )
	public void attributeDecl_AttributeNameIsNotEmpty( TestCase tc ) {
		tc.assertEqual( "age",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( aName );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Element name is not empty" )
	public void attributeDecl_ElementNameIsNotEmpty( TestCase tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( eName );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Mode is correct" )
	public void attributeDecl_ModeIsCorrect( TestCase tc ) {
		tc.assertEqual( "#FIXED",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( mode );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Type is correct" )
	public void attributeDecl_TypeIsCorrect( TestCase tc ) {
		tc.assertEqual( "CDATA",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( type );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.attributeDecl(String, String, String, String, String)", desc = "Value is correct when supplied in DTD" )
	public void attributeDecl_ValueIsCorrectWhenSuppliedInDtd( TestCase tc ) {
		tc.assertEqual( "45",  new Adapter<String>( "DTD" ) {
			@Override
			public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException {
				this.accept( value );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.elementDecl(String, String)", desc = "Location is identified" )
	public void elementDecl_LocationIsIdentified( TestCase tc ) {
		tc.assertEqual( "(3, 21)",  new Adapter<String>( "DTD" ) {
			@Override
			public void elementDecl( String name, String model ) throws SAXException {
				this.accept( this.getLocation().toString() );
			}
		}.get() );
	}

	@Test.Impl( src = "public void XMLHandler.elementDecl(String, String)", desc = "Name is not empty" )
	public void elementDecl_NameIsNotEmpty( TestCase tc ) {
		tc.assertEqual( "root",  new Adapter<String>( "DTD" ) {
			@Override
			public void elementDecl( String name, String model ) throws SAXException {
				this.accept( name );
			}
		}.get() );
	}

	

	
	
	public static void main(String[] args) {

		System.out.println();

		new Test(XMLHandlerTest.class);
		Test.printResults();

		System.out.println("\nDone!");

	}
}
