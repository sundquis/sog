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

package test.sog.core;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import sog.core.App;
import sog.core.SoftString;
import sog.core.Strings;
import sog.core.Test;
import test.sog.core.test.TestCaseTest;

/**
 * 
 */
@Test.Skip( "Container" )
public class SoftStringTest extends Test.Container {
	
	private final int THRESHOLD;
	private final String LONG_STRING;
	private final String SHORT_STRING;
	private final String[] ARGS;
	
	public SoftStringTest() {
		super( SoftString.class );
		
		this.THRESHOLD = this.getSubjectField( null, "THRESHOLD", null );
		this.LONG_STRING = Strings.leftJustify( "Hello", THRESHOLD, '_' )
			+ Strings.rightJustify( "world.", THRESHOLD, '_' );
		this.SHORT_STRING = Strings.leftJustify( "FOO", THRESHOLD/2, '_' );
		
		this.ARGS = new String[] {
			this.LONG_STRING,
			this.SHORT_STRING,
			Strings.leftJustify( "Some random string", this.THRESHOLD, '#' ),
			"abcdefghijklmnopqrstuvwxyz!@#$%^&*()0123456789,<.>/?;:'\\|ABCDEFGHIJKLMNOPQRSTUVWXYZ\"",
			""
		};
	}
	
	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "constructor: SoftString(String)", 
		description = "Can construct empty" 
	)
	public void tm_096E5EE31( Test.Case tc ) {
		tc.assertEqual( "", new SoftString( "" ).toString() );
	}
		
	@Test.Impl( 
		member = "constructor: SoftString(String)", 
		description = "Can construct long strings" 
	)
	public void tm_00BADCA10( Test.Case tc ) {
		tc.assertEqual( this.LONG_STRING, new SoftString( this.LONG_STRING ).toString() );
	}
		
	@Test.Impl( 
		member = "constructor: SoftString(String)", 
		description = "Can construct short strings" 
	)
	public void tm_0ABC71682( Test.Case tc ) {
		tc.assertEqual( this.SHORT_STRING, new SoftString( this.SHORT_STRING ).toString() );
	}
		
	@Test.Impl( 
		member = "constructor: SoftString(String)", 
		description = "Strings longer than threshold are soft" 
	)
	public void tm_0D154678A( Test.Case tc ) {
		SoftString ss = new SoftString( this.LONG_STRING );
		tc.assertIsNull( this.getSubjectField( ss, "hard", "" ) );
		SoftReference<String> sr = null;
		tc.assertNonNull( this.getSubjectField( ss, "soft", sr ) );
	}
		
	@Test.Impl( 
		member = "constructor: SoftString(String)", 
		description = "Throws assertion error for null strings" 
	)
	public void tm_06146F0C6( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		new SoftString( null );
	}
		
	@Test.Impl( 
		member = "method: String SoftString.Location.toString()", 
		description = "Consistent with constructed value" 
	)
	public void tm_072BB8E48( Test.Case tc ) {
		SoftString ss = new SoftString( this.LONG_STRING );
		tc.assertEqual( this.LONG_STRING, this.getSubjectField( ss, "location", null ).toString() );
	}
		
	@Test.Impl( 
		member = "method: String SoftString.toString()", 
		description = "Consistent with constructed value" 
	)
	public void tm_0D0D4921B( Test.Case tc ) {
		for ( int i = 0; i < this.ARGS.length; i++ ) {
			tc.assertEqual( this.ARGS[i], new SoftString( this.ARGS[i] ).toString() );
		}
	}
		
	@Test.Impl( 
		member = "method: String SoftString.toString()", 
		description = "Correct value after collection" 
	)
	public void tm_08A459B1D( Test.Case tc ) {
		List<SoftString> strings = new ArrayList<SoftString>();
		ReferenceQueue<String> rq = new ReferenceQueue<>();
		
		while ( rq.poll() == null ) {
			// String stored in byte file, length > threshold so uses a soft reference
			SoftString soft = new SoftString( this.LONG_STRING );
			strings.add( soft );
			
			// Replace its string soft ref with bogus large soft string ref
			// This one uses our reference queue so we can detect when collection occurs
			this.setSubjectField( soft,  "soft", new SoftReference<String>( new String( new byte[100_000] ), rq ) );
		}

		// GC has run. No guarantee about what was collected. SHOULD be one of our bogus soft refs
		// If a bogus soft ref was collected, the SoftString will retrieve the correct value from the Location
		tc.assertTrue( strings.stream().map( Object::toString ).anyMatch( this.LONG_STRING::equals ) );
	}
		
	@Test.Impl( 
		member = "method: String SoftString.toString()", 
		description = "Result is not null" 
	)
	public void tm_004B0AE5D( Test.Case tc ) {
		Arrays.stream( this.ARGS ).map( SoftString::new ).map( SoftString::toString ).forEach( tc::assertNonNull );
	}
		
	@Test.Impl( 
		member = "method: String SoftString.toString()", 
		description = "Stress test correct value" 
	)
	public void tm_0E92F44B9( Test.Case tc ) throws IOException {
		// find a big file...
		// find . -type f -printf '%s %p\n' | sort -nr | less
		// Here I use the source for TestCaseTest
		
		Object[] lines1 = Files.lines( App.get().sourceFile( TestCaseTest.class ) )
			.map( SoftString::new )
			.map( SoftString::toString )
			.toArray();
		
		Object[] lines2 = Files.lines( App.get().sourceFile( TestCaseTest.class ) )
			.map( SoftString::new )
			.map( SoftString::toString )
			.toArray();
		
		tc.assertTrue( Objects.deepEquals( lines1,  lines2 ) );
	}
		
	@Test.Impl( 
		member = "method: boolean SoftString.equals(Object)", 
		description = "If compareTo is zero then equals" 
	)
	public void tm_05D8B58CC( Test.Case tc ) {
		// compareTo != 0 OR equals
		for ( int i = 0; i < this.ARGS.length; i++ ) {
			for ( int j = 0; j < this.ARGS.length; j++ ) {
				SoftString ss1 = new SoftString( this.ARGS[i] );
				SoftString ss2 = new SoftString( this.ARGS[2] );
				tc.assertTrue( ss1.compareTo( ss2 ) != 0 || ss1.equals( ss2 ) );
			}
		}
	}
		
	@Test.Impl( 
		member = "method: boolean SoftString.equals(Object)", 
		description = "If equals then compareTo is zero" 
	)
	public void tm_011F43208( Test.Case tc ) {
		// ! equals OR compareTo == 0
		for ( int i = 0; i < this.ARGS.length; i++ ) {
			for ( int j = 0; j < this.ARGS.length; j++ ) {
				SoftString ss1 = new SoftString( this.ARGS[i] );
				SoftString ss2 = new SoftString( this.ARGS[2] );
				tc.assertTrue( ! ss1.equals( ss2 ) || ss1.compareTo( ss2 ) == 0 );
			}
		}
	}
		
	private String[] args1 = {
		"accustom accustomed accustoming anatomic anatomical",
		"automata automate automated automates automatic automatically",
		"automatics automating automation automaton automatons automobile",
		"bottoming bottomless bottoms custom customarily customary",
		"customizing customs diatom diatoms dichotomies dichotomy",
		"entomologist entomologists entomology epitome epitomes epitomize",
		"kleptomaniac kleptomaniacs lobotomies lobotomy mastectomies mastectomy",
		"ptomaines semiautomatic semiautomatics stomach stomachache stomachaches",
		"stomps streptomycin subatomic symptom symptomatic symptoms",
		"tomatoes tomb tombed tombing tomboy tomboys",
		"tomes tomfooleries tomfoolery tomorrow toms",
		"tonsillectomy tracheotomy unaccustomed vasectomy"
	};
	
	private String[] args2 = {
		"accustom" + " accustomed accustoming anatomic anatomical",
		"automata automate" + " automated automates automatic automatically",
		"automatics automating automation" + " automaton automatons automobile",
		"bottoming bottomless bottoms custom customarily" + " customary",
		"custom" + "izing customs diatom diatoms dichotomies dichotomy",
		"entomologis" + "t entomologists entomology epitome epitomes epitomize",
		"kleptomaniac kle" + "ptomaniacs lobotomies lobotomy mastectomies mastectomy",
		"ptomaines semiautomat"  + "ic semiautomatics stomach stomachache stomachaches",
		"stomps streptomycin subatom" + "ic symptom symptomatic symptoms",
		"tomatoes tomb tombed tombing tom" + "boy tomboys",
		"tomes tomfooleries tomfoolery tomorro" + "w toms",
		"tonsillectomy tracheotomy unaccustomed vas" + "ectomy"
	};
	
	private String[] args3 = {  // extra spaces
		"accustom  accustomed accustoming anatomic anatomical",
		"automata automate  automated automates automatic automatically",
		"automatics automating  automation automaton automatons automobile",
		"bottoming bottomless bottoms  custom customarily customary",
		"customizing customs diatom diatoms  dichotomies dichotomy",
		"entomologist entomologists entomology  epitome epitomes epitomize",
		"kleptomaniac kleptomaniacs lobotomies lobotomy  mastectomies mastectomy",
		"ptomaines semiautomatic semiautomatics stomach  stomachache stomachaches",
		"stomps streptomycin subatomic symptom symptomatic  symptoms",
		"tomatoes  tomb tombed tombing tomboy tomboys",
		"tomes tomfooleries  tomfoolery tomorrow toms",
		"tonsillectomy tracheotomy  unaccustomed vasectomy"
	};

	@Test.Impl( 
		member = "method: boolean SoftString.equals(Object)", 
		description = "Sample cases equal" 
	)
	public void tm_04C1E0530( Test.Case tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertEqual( new SoftString( args1[i] ), new SoftString( args2[i] ) );
		}
	}
		
	@Test.Impl( 
		member = "method: boolean SoftString.equals(Object)", 
		description = "Sample cases not equal" 
	)
	public void tm_0CB41A063( Test.Case tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertNotEqual( new SoftString( args1[i] ), new SoftString( args3[i] ) );
		}
	}
		
	@Test.Impl( 
		member = "method: int SoftString.compareTo(SoftString)", 
		description = "Can sort large collections" 
	)
	public void tm_03B0327E1( Test.Case tc ) throws IOException {
		// find a big file...
		// find . -type f -printf '%s %p\n' | sort -nr | less
		// Here I use the source for TestCaseTest

		List<SoftString> lines = new LinkedList<SoftString>();

		// Execution time very sensitive to multiplicity
		int stressMultiplicty = 20;
		for ( int i = 0; i < stressMultiplicty; i++ ) {
			Files.lines( App.get().sourceFile( TestCaseTest.class ) )
				.map( SoftString::new )
				.forEach( lines::add );			
		}
		
		Collections.sort( lines );
		Collections.reverse( lines );

		int pos = 0;
		while ( pos < lines.size() ) {
			int offset = 1;
			while ( offset < stressMultiplicty ) {
				tc.assertEqual( lines.get( pos ), lines.get( pos + offset ) );
				offset += 1;
			}
			pos += offset;
		}
	}
		
	@Test.Impl( 
		member = "method: int SoftString.compareTo(SoftString)", 
		description = "Consistent with String.compareTo()" 
	)
	public void tm_045BF11F3( Test.Case tc ) {
		for ( int i = 0; i < this.ARGS.length; i++ ) {
			for ( int j = 0; j < this.ARGS.length; j++ ) {
				tc.assertEqual( this.ARGS[i].compareTo( this.ARGS[j] ), 
					new SoftString( this.ARGS[i] ).compareTo( new SoftString( this.ARGS[j] ) ) );
			}
		}
	}
		
	@Test.Impl( 
		member = "method: int SoftString.hashCode()", 
		description = "If equals then hashCodes are the same" 
	)
	public void tm_00D32EC0D( Test.Case tc ) {
		// !equals OR hasCode == hashCode
		for ( int i = 0; i < this.ARGS.length; i++ ) {
			for ( int j = 0; j < this.ARGS.length; j++ ) {
				SoftString ss1 = new SoftString( this.ARGS[i] );
				SoftString ss2 = new SoftString( this.ARGS[2] );
				tc.assertTrue( ! ss1.equals( ss2 ) || ss1.hashCode() == ss2.hashCode() );
			}
		}
	}
	
	


	public static void main( String[] args ) {
		Test.eval( SoftString.class ).showDetails( true ).print();
	}
}
