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
import java.io.Reader;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sog.core.AppException;
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
public class XMLHandlerTest extends Test.Container {
	
	public XMLHandlerTest() {
		super( XMLHandler.class );
	}
	
	
	/* 
	 * PROOF-OF-CONCEPT:
	 * Test case paradigm - Define a Consumer/Producer class to be used by each test case.
	 * Particularly useful when testing a Handler.
	 */
	
	/*
	 * Test cases create anonymous subclasses by overriding the call-back of interest, using
	 * Consumer.accept(..) to save the result. The constructor requires the label for the 
	 * commented lines defining the document to parse. Supplier.get() returns the result.
	 */
	public static class Adapter<T> extends XMLHandler implements Consumer<T>, Supplier<T> {
		
		private static final Commented COMMENT = new Commented() {};
		
		private static Reader getReader( String label ) {
			return Adapter.getReader( label, s -> Stream.of( s ) );
		}

		/* PROOF-OF-CONCEPT: How to use a Macro as a mapper to perform substitutions on a Stream of lines. */
		private static Reader getReader( String label, Function<String, Stream<String>> mapper ) {
			try {
				return new StreamReader( Adapter.COMMENT.getCommentedLines( label ).flatMap( mapper ) );
			} catch ( IOException e ) {
				throw new AppException( e );
			}
		}
		
		private T result = null;
		private boolean verbose = false;
		
		private Adapter( String label ) {
			super( Adapter.getReader( label ) );
		}
		
		private Adapter( String label, Macro filter ) {
			super( Adapter.getReader( label, filter ) );
		}
		
		private Adapter( Path path ) throws IOException {
			super( path );
		}

		/*
		 * When testing a method of XMLHandler, create an anonymous Adapter that overrides
		 * the method. The implementation calls accept to store the result of the test
		 */
		@Override 
		public void accept( T result ) { this.result = result; }

		/*
		 * USAGE:
		 * ReturnType result = new Adapter<ReturnType>( "XML Label" ) {
		 * 		@Override public void someSAXMethod( ... ) {
		 * 			...
		 * 			this.accept( result );
		 * 		}
		 * }.get();
		 */
		@Override 
		public T get() { this.parse(); return this.result; }

		// details are assumed to be in pairs, (label, value)
		public void out( boolean show, Object... details ) {
			if ( !show ) return;
			
			System.err.println();
			System.err.println( ">>> " + this.getLocation() );
			for ( int i = 0; i < details.length; ) {
				System.err.println( "\t" + Strings.toString( details[i++] ) + " = " + Strings.toString( details[i++] ) );
			}
		}
		
		public Adapter<T> verbose( boolean v ) {
			this.verbose = v;
			return this;
		}
		
		public Adapter<T> verbose() {
			return this.verbose( true );
		}
		
		// For diagnostics. User Adapter.verbose() to get detailed processing diagnostics
		
		@Override public void startDocument() throws SAXException { this.out( this.verbose, "startDocument", "" ); }
		@Override public void endDocument() throws SAXException { this.out( this.verbose, "endDocument", "" ); }
		@Override public void startElement( String name, Map<String, String> attributes ) { this.out(  this.verbose, "startElement", name ); }
		@Override public void endElement( String name ) { this.out(  this.verbose, "endElement", name ); }
		@Override public void characters( char[] ch, int start, int length ) throws SAXException { this.out(  this.verbose, "characters", "" + length ); }
		@Override public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException { this.out(  this.verbose, "ignoreableWhitespace", "" + length ); }
		@Override public void warning( SAXParseException exception ) throws SAXException { this.out( this.verbose, "warning", exception.getMessage() ); }
		@Override public void error( SAXParseException exception ) throws SAXException { this.out( this.verbose, "error", exception.getMessage() ); }
		@Override public void fatalError( SAXParseException exception ) throws SAXException { this.out( this.verbose, "fatalError", exception.getMessage() ); }
		@Override public void elementDecl( String name, String model ) throws SAXException { this.out(  this.verbose, "elementDecl", name ); }
		@Override public void attributeDecl( String eName, String aName, String type, String mode, String value ) throws SAXException { this.out(  this.verbose, "attributeDecl", aName ); }
		@Override public void startDTD( String name, String publicId, String systemId ) throws SAXException { this.out(  this.verbose, "startDTD", name, "publicId", publicId, "systemId", systemId  ); }
		@Override public void endDTD() throws SAXException { this.out(  this.verbose, "endDTD", "" ); }
		@Override public void startCDATA() throws SAXException { this.out(  false, "startCDATA", "" ); }
		@Override public void endCDATA() throws SAXException { this.out(  false, "andCDATA", "" ); }
		
	}
	
	
	
	
	// TEST CASES
	
    @Test.Impl( 
        	member = "constructor: XMLHandler(InputSource)", 
        	description = "Throws AssertionError for null source" 
        )
        public void tm_0962A2397( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "constructor: XMLHandler(InputStream)", 
        	description = "Throws AssertionError for null stream" 
        )
        public void tm_0D0691137( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "constructor: XMLHandler(Path)", 
        	description = "External DTD is read" 
        )
        public void tm_00380BA00( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "constructor: XMLHandler(Path)", 
        	description = "Throws AssertionError for null path" 
        )
        public void tm_00B3EEBA1( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "constructor: XMLHandler(Path)", 
        	description = "Throws NoSuchFileException if the file is missing" 
        )
        public void tm_09953EC0B( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "constructor: XMLHandler(Reader)", 
        	description = "Throws AssertionError for null reader" 
        )
        public void tm_02FDE7D21( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
        	description = "All keys are present" 
        )
        public void tm_0CEFD09EC( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
        	description = "Empty map returned when no attributes" 
        )
        public void tm_01F066856( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
        	description = "Result is not null" 
        )
        public void tm_09F68DA71( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: Map XMLHandler.attributesToMap(Attributes)", 
        	description = "Values are correct" 
        )
        public void tm_056FCB76A( Test.Case tc ) {
        	tc.addMessage( "GENERATED STUB" );
        }
        
        @Test.Impl( 
        	member = "method: String XMLHandler.Location.getPublicId()", 
        	description = "Must be set if using a public resources" 
        )
        public void tm_0D8DDB085( Test.Case tc ) {
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
        	description = "Location is identified" 
        )
        public void tm_04BE3D1CD( Test.Case tc ) {
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
        	member = "method: void XMLHandler.parse()", 
        	description = "Throws AppException when IOException encountered reading xml" 
        )
        public void tm_01EE9781D( Test.Case tc ) {
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
        	description = "Location is identified" 
        )
        public void tm_0885250E2( Test.Case tc ) {
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
			.concurrent( false )
			.showDetails( false )
			.showProgress( true )
			.print();
		//*/
	}
}
