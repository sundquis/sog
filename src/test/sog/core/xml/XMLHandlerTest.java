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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sog.core.AppRuntime;
import sog.core.LocalDir;
import sog.core.Strings;
import sog.core.Test;
import sog.core.xml.XMLHandler;
import sog.util.Commented;
import sog.util.Macro;
import sog.util.StreamReader;

/**
 * Test cases reveal the behavior of the SAX parsing framework.
 * 
 * Cases showing interesting/unexpected/useful behavior are marked with a "LESSON-LEARNED" comment.
 * Cases illustrating a useful technique are marked with a "PROOF-OF-CONCEPT" comment.
 */
@Test.Skip( "Container" )
public class XMLHandlerTest extends Test.Container implements Commented {
	
	public XMLHandlerTest() {
		super( XMLHandler.class );
	}
	
	
	private static class CS<T> implements Consumer<T>, Supplier<T> {

		private T result = null;
		
		private CS() {}
		
		private CS( T init ) { this.result = init; }

		@Override public T get() { return this.result; }

		@Override public void accept( T t ) { this.result = t; }
		
	}
	
	
	private Stream<String> getLines( String label ) {
		try {
			return this.getCommentedLines( label );
		} catch ( IOException e ) {
			throw new AppRuntime( e );
		}
	}

	
	// TEST CASES:
	
	
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
    
    // EXT-XML	<?xml version = "1.0" encoding = "UTF-8" ?>
    // EXT-XML	<!DOCTYPE	root	SYSTEM	"TEST.dtd" >
    // EXT-XML	<root/>
    
    // EXT-DTD	<!ELEMENT	root	EMPTY>
    
    @Test.Impl( 
    	member = "constructor: XMLHandler(Path)", 
    	description = "External DTD is read" 
    )
    public void tm_00380BA00( Test.Case tc ) throws IOException {
    	Path xmlPath = new LocalDir().sub( "tmp" ).sub( "system" ).getFile( "TEST", LocalDir.Type.XML );
    	Path dtdPath = new LocalDir().sub( "tmp" ).sub( "system" ).getFile( "TEST", LocalDir.Type.DTD );
    	
		this.writeLines( "EXT-XML", xmlPath );
		this.writeLines( "EXT-DTD", dtdPath );
		
		final CS<Boolean> docComplete = new CS<>( false );
		final CS<Boolean> dtdComplete = new CS<>( false );
		final CS<Boolean> noWarn = new CS<>( true );
		final CS<Boolean> noError= new CS<>( true );
		final CS<Boolean> noFatal = new CS<>( true );
		
    	new XMLHandler( xmlPath ) {
    		@Override public void endDocument() throws SAXException { docComplete.accept( true ); }
    		@Override public void endDTD() throws SAXException { dtdComplete.accept( true ); }
    		@Override public void warning( SAXParseException exception ) throws SAXException { noWarn.accept( false ); }
    		@Override public void error( SAXParseException exception ) throws SAXException { noError.accept( false ); }
    		@Override public void fatalError( SAXParseException exception ) throws SAXException { noFatal.accept( false ); }
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
    
    // attributesToMap	<?xml version = "1.0" encoding = "UTF-8" ?>
    // attributesToMap	<!DOCTYPE root [
    // attributesToMap		<!ELEMENT	root	(A,B) >
    // attributesToMap		<!ELEMENT	A		EMPTY >
    // attributesToMap		<!ATTLIST	A
    // attributesToMap			attr1	CDATA	#IMPLIED
    // attributesToMap			attr2	CDATA	#IMPLIED
    // attributesToMap			attr3	CDATA	#IMPLIED
    // attributesToMap		>
    // attributesToMap		<!ELEMENT	B		EMPTY >
    // attributesToMap	]>
    // attributesToMap	<root>
    // attributesToMap		<A attr1 = "1" attr2 = "2" attr3 = "3" />
    // attributesToMap		<B />
    // attributesToMap	</root>
    // attributesToMap	
    // attributesToMap	
    // attributesToMap	
    // attributesToMap	
    // attributesToMap	
    
    @Test.Impl( 
    	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
    	description = "All keys are present" 
    )
    public void tm_0CEFD09EC( Test.Case tc ) {
    	CS<Map<String, String>> keyToVal = new CS<>();
    	
    	// DIFFICULT LESSON LEARNED:
    	// SAX reuses structures. The Attributes instance returned is the same instance with it's properties reset.
    	// We can't use CS<Attributes> since the values of the contained Attributes will have the properties
    	// of the last element encountered. Could have kept a copy, or overridden the CS.accept() to
    	// make the test assertion. Decided to simply convert to a Map
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			if ( qName.equals( "A" ) ) { keyToVal.accept( XMLHandler.attributesToMap( atts ) ); }
    		}
    	}.parse();
    	
    	Stream.of( "attr1", "attr2", "attr3" ).forEach( s -> tc.assertTrue( keyToVal.get().keySet().contains( s ) ) );
    }
    
    @Test.Impl( 
    	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
    	description = "Values are correct" 
    )
    public void tm_056FCB76A( Test.Case tc ) {
    	CS<Map<String, String>> keyToVal = new CS<>();
    	
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			if ( qName.equals( "A" ) ) { keyToVal.accept( XMLHandler.attributesToMap( atts ) ); }
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
    	CS<Map<String, String>> keyToVal = new CS<>();
    	
    	new XMLHandler( this.getLines( "attributesToMap" ) ) {
    		@Override public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
    			if ( qName.equals( "B" ) ) { keyToVal.accept( XMLHandler.attributesToMap( atts ) ); }
    		}
    	}.parse();
    	
    	tc.assertTrue( keyToVal.get().size() == 0 );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.getPublicId()", 
    	description = "Must be set if using public resources" 
    )
    public void tm_090C629A6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.getSystemId()", 
    	description = "Can be set when using a file" 
    )
    public void tm_07B51C1E5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.toString()", 
    	description = "Indicates row and column while parsing" 
    )
    public void tm_03405A7FD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: String XMLHandler.Location.toString()", 
    	description = "Not empty" 
    )
    public void tm_0B48C6125( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showContentEvents()", 
    	description = "Enables feedback for content events" 
    )
    public void tm_09A29E715( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showContentEvents()", 
    	description = "Returns this XMLHadler instance to allow chaining" 
    )
    public void tm_0CFC338A5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showDeclarationEvents()", 
    	description = "Enables feedback for declaration events" 
    )
    public void tm_027E14B35( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showDeclarationEvents()", 
    	description = "Returns this XMLHadler instance to allow chaining" 
    )
    public void tm_0795C4C66( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showErrorEvents()", 
    	description = "Enables feedback for error events" 
    )
    public void tm_023C1CCF5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showErrorEvents()", 
    	description = "Returns this XMLHadler instance to allow chaining" 
    )
    public void tm_0983A36B4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showLexicalEvents()", 
    	description = "Enables feedback for lexical (DTD) events" 
    )
    public void tm_0C9173858( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler XMLHandler.showLexicalEvents()", 
    	description = "Returns this XMLHadler instance to allow chaining" 
    )
    public void tm_091F12050( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Is not null" 
    )
    public void tm_0132A429A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Location is unknown after parsing" 
    )
    public void tm_0E72E765B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: XMLHandler.Location XMLHandler.getLocation()", 
    	description = "Location is unknown before parsing" 
    )
    public void tm_0EDFB4DD6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean XMLHandler.Location.unknown()", 
    	description = "True when line or column unknown" 
    )
    public void tm_0C41A489B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getColumnNumber()", 
    	description = "Greater or equal to zero while parsing" 
    )
    public void tm_09D21EF6F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getColumnNumber()", 
    	description = "Return is -1 when unknown" 
    )
    public void tm_0B830214C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getLineNumber()", 
    	description = "Greater or equal to zero while parsing" 
    )
    public void tm_0604FC4D1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int XMLHandler.Location.getLineNumber()", 
    	description = "Return is -1 when unknown" 
    )
    public void tm_0830934AA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Attribute name is not empty" 
    )
    public void tm_0D9B6E779( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Element name is not empty" 
    )
    public void tm_0879EB099( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_087E189D9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Location is identified" 
    )
    public void tm_009A360BE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Mode is correct" 
    )
    public void tm_0FAF7DB4F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Type is correct" 
    )
    public void tm_035989318( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.attributeDecl(String, String, String, String, String)", 
    	description = "Value is correct when supplied in DTD" 
    )
    public void tm_080BE7B30( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "All content registered before endElement" 
    )
    public void tm_0CD2DE44F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0AE2AC765( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_07AD6ABCB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.characters(char[], int, int)", 
    	description = "Parser uses to signal content" 
    )
    public void tm_0292924E4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_027A703BD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_04E7CE238( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.comment(char[], int, int)", 
    	description = "Parser signals comments" 
    )
    public void tm_0F8C0BB4E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_001DFE38A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0092D172F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.elementDecl(String, String)", 
    	description = "Name is not empty" 
    )
    public void tm_079D955AC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Called after startCDATA()" 
    )
    public void tm_04AE85F04( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_0B493F730( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Location is identified" 
    )
    public void tm_029EBA26B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endCDATA()", 
    	description = "Parser signals end of CDATA after characters" 
    )
    public void tm_0130C0094( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Called after startDTD()" 
    )
    public void tm_07A017C12( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_0F7D04189( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDTD()", 
    	description = "Location is identified" 
    )
    public void tm_0F9095304( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Called after startDocument()" 
    )
    public void tm_0717CDC9A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0D4080063( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endDocument()", 
    	description = "Location is identified" 
    )
    public void tm_0B93B8449( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Elements closed in LIFO order" 
    )
    public void tm_0DD1B1E73( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Location is identified" 
    )
    public void tm_03EF39A0D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String)", 
    	description = "Name is not empty" 
    )
    public void tm_01BE8000E( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0F41011F1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "localName is not empty" 
    )
    public void tm_0A1159725( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "qName is not empty" 
    )
    public void tm_026D3575F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endElement(String, String, String)", 
    	description = "uri is not empty" 
    )
    public void tm_06A6DB40F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Called after startEntity()" 
    )
    public void tm_0C0B373CB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_07B02E777( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Location is identified" 
    )
    public void tm_06E79D472( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.endEntity(String)", 
    	description = "Name is consistent" 
    )
    public void tm_06878B7FF( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Feedback provided if showErrorEvents() has been called" 
    )
    public void tm_0BA4B01C4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Location is identified" 
    )
    public void tm_0D36F725B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for changing #FIXED attribute" 
    )
    public void tm_07BDBFE86( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for missing DTD" 
    )
    public void tm_02BA18DA0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for missing requiured attribute" 
    )
    public void tm_0594E8016( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for undeclared attributes" 
    )
    public void tm_0372E572A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered for undeclared elements" 
    )
    public void tm_028D7ECCA( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.error(SAXParseException)", 
    	description = "Trigered when element does not match declared type" 
    )
    public void tm_0FA312C98( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Excpetion is thrown after signaling" 
    )
    public void tm_0D0852470( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Feedback provided if showErrorEvents() has been called" 
    )
    public void tm_010038662( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Location is identified" 
    )
    public void tm_086F1EEF9( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Trigered for illegal DTD structure" 
    )
    public void tm_09C315DA1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.fatalError(SAXParseException)", 
    	description = "Trigered for undeclared entity" 
    )
    public void tm_017BA6954( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.ignorableWhitespace(char[], int, int)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0F61FECE7( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.ignorableWhitespace(char[], int, int)", 
    	description = "Location is identified" 
    )
    public void tm_04BE3D1CD( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Feedback provided if showDeclarationEvents() has been called" 
    )
    public void tm_04C5E3244( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0A01AE169( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Name is correct" 
    )
    public void tm_0829D6D1C( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.internalEntityDecl(String, String)", 
    	description = "Value is correct" 
    )
    public void tm_084975586( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Detail objects may be null" 
    )
    public void tm_04FCB3A1A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Details treated as (label, detail) pairs" 
    )
    public void tm_0330950D4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Throws AssertionError for null details" 
    )
    public void tm_09F6D9B93( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.out(Object[])", 
    	description = "Throws AssertionError for odd number of details" 
    )
    public void tm_00832F26B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.parse()", 
    	description = "Throws AppRuntime when IOException encountered reading xml" 
    )
    public void tm_0BEE9A734( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Data is correct" 
    )
    public void tm_003E2BBCC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0D61F04F4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Location is identified" 
    )
    public void tm_07A7BFD1A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.processingInstruction(String, String)", 
    	description = "Target is correct" 
    )
    public void tm_0AF485B85( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.setDocumentLocator(Locator)", 
    	description = "Called before startDocument()" 
    )
    public void tm_0B81629FE( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.setDocumentLocator(Locator)", 
    	description = "Parser registers non-null locator" 
    )
    public void tm_074D500CC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_05A79D777( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Location is identified" 
    )
    public void tm_0A6ACC472( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startCDATA()", 
    	description = "Parser signals start of CDATA before characters" 
    )
    public void tm_013B6230D( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_06FA2D30B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Location is identified" 
    )
    public void tm_0C70EE506( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDTD(String, String, String)", 
    	description = "Name is root element" 
    )
    public void tm_0B9A9F53F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDocument()", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_08DF666BC( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startDocument()", 
    	description = "Location is identified" 
    )
    public void tm_0885250E2( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_06CCE369A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Location is identified" 
    )
    public void tm_0911D7840( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Name is not empty" 
    )
    public void tm_01BE2CDFB( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for enumerated attribute" 
    )
    public void tm_0DCD9C552( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for fixed attribute" 
    )
    public void tm_0196155B0( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for implied attribute" 
    )
    public void tm_06EC8C3A4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, Map)", 
    	description = "Value correct for required attribute" 
    )
    public void tm_0A95C5EB1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "Attributes is not null" 
    )
    public void tm_0CE1E9284( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "Feedback provided if showContentEvents() has been called" 
    )
    public void tm_0329069E3( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "Parser signals start of element processing" 
    )
    public void tm_0538C4F2F( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "localName is not empty" 
    )
    public void tm_07A0D8B97( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "qName is not empty" 
    )
    public void tm_05FCB54D1( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startElement(String, String, String, Attributes)", 
    	description = "uri is not empty" 
    )
    public void tm_087CA6601( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Feedback provided if showLexicalEvents() has been called" 
    )
    public void tm_030347950( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Location is identified" 
    )
    public void tm_005B6AC8B( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Name is correct" 
    )
    public void tm_0E1FD043A( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.startEntity(String)", 
    	description = "Parser signals use of entity" 
    )
    public void tm_068491C42( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.warning(SAXParseException)", 
    	description = "Feedback provided if showErrorEvents() has been called" 
    )
    public void tm_095950498( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: void XMLHandler.warning(SAXParseException)", 
    	description = "Unable to trigger..." 
    )
    public void tm_032218917( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
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
	}
}
