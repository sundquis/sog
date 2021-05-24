/*
 * Copyright (C) 2017-18 by TS Sundquist
 * 
 * All rights reserved.
 * 
 */

package test.core;



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
import sog.core.Procedure;
import sog.core.SoftString;
import sog.core.Strings;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
public class SoftStringTest extends Test.Container {

	private SoftString soft;
	
	private int ORIG_THRESHOLD;
	private int threshold = 5;
	
	public SoftStringTest() {
		super( SoftString.class );
		
		this.ORIG_THRESHOLD = this.getSubjectField( null,  "THRESHOLD",  0 );
		this.setSubjectField( null,  "THRESHOLD",  this.threshold );
	}
	

	@Override
	public Procedure afterAll() {
		return () -> this.setSubjectField( null,  "THRESHOLD",  this.ORIG_THRESHOLD );
	};
	
	@Override
	public Procedure afterEach() {
		return () -> this.soft = null;
	};

	
	@Test.Impl( member = "public SoftString(String)", description = "Can construct empty" )
	public void SoftString_CanConstructEmpty( Test.Case tc ) {
		soft = new SoftString( "" );
		tc.assertEqual( "",  soft.toString() );
	}

	@Test.Impl( member = "public SoftString(String)", description = "Can construct long strings" )
	public void SoftString_CanConstructLongStrings( Test.Case tc ) {
		String arg = Strings.rightJustify( "42",  this.threshold,  '.' );
		soft = new SoftString( arg );
		tc.assertEqual( arg,  soft.toString() );
	}

	@Test.Impl( member = "public SoftString(String)", description = "Can construct short strings" )
	public void SoftString_CanConstructShortStrings( Test.Case tc ) {
		String arg = "42";
		soft = new SoftString( arg );
		tc.assertEqual( arg,  soft.toString() );
	}

	@Test.Impl( member = "public SoftString(String)", description = "Throws assertion error for null strings" )
	public void SoftString_ThrowsAssertionErrorForNullStrings( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		soft = new SoftString( null );
	}
	
	@Test.Impl( member = "public String SoftString.toString()", description = "Stress test correct value", weight = 10 )
	public void toString_StressTestCorrectValue( Test.Case tc ) throws IOException {
		
		// find a big file...
		// find . -type f -printf '%s %p\n' | sort -nr | less
		// Here I use the source for ObjectsTest
		
		Object[] lines1 = Files.lines( App.get().sourceFile( ObjectsTest.class ) )
			.map( SoftString::new )
			.toArray();
		
		Object[] lines2 = Files.lines( App.get().sourceFile( ObjectsTest.class ) )
			.map( SoftString::new )
			.toArray();
		
		tc.assertTrue( Objects.deepEquals( lines1,  lines2 ) );
	}

	
	@Test.Impl( member = "public int SoftString.compareTo(SoftString)", description = "Can sort large collections" )
	public void compareTo_CanSortLargeCollections( Test.Case tc ) throws IOException {
		
		// find a big file...
		// find . -type f -printf '%s %p\n' | sort -nr | less
		// Here I use the source for ObjectsTest

		List<SoftString> lines = new LinkedList<SoftString>();
		
		int stressMultiplicty = 100;
		for ( int i = 0; i < stressMultiplicty; i++ ) {
			Files.lines( App.get().sourceFile( ObjectsTest.class ) )
				.map( SoftString::new )
				.forEach( lines::add );			
		}
		
		Collections.sort( lines );
		Collections.reverse( lines );

		boolean match = true;
		for ( int i = 0; i < stressMultiplicty; i++ ) {
			match &= lines.get( i ).toString().equals( "}" ); 
		}
		
		tc.assertTrue( match );
	}
	
	@Test.Impl( member = "public boolean SoftString.equals(Object)", description = "Consistent with compare" )
	public void equals_ConsistentWithCompare( Test.Case tc ) throws IOException {
		// find a big file...
		// find . -type f -printf '%s %p\n' | sort -nr | less
		// Here I use the source for ObjectsTest
		
		Object[] lines1 = Files.lines( App.get().sourceFile( ObjectsTest.class ) )
			.map( SoftString::new )
			.toArray();
		
		Object[] lines2 = Files.lines( App.get().sourceFile( ObjectsTest.class ) )
			.map( SoftString::new )
			.toArray();
		
		Arrays.sort( lines1 );
		Arrays.sort( lines2 );
		
		boolean match = true;
		for ( int i = 0; i < lines1.length; i++ ) {
			match &= lines1[i].equals( lines2[i] );
		}
		
		tc.assertTrue( match );
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
	
	@Test.Impl( member = "public boolean SoftString.equals(Object)", description = "Sample cases equal" )
	public void equals_SampleCasesEqual( Test.Case tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertEqual( new SoftString( args1[i] ), new SoftString( args2[i] ) );
		}
	}

	@Test.Impl( member = "public boolean SoftString.equals(Object)", description = "Sample cases not equal" )
	public void equals_SampleCasesNotEqual( Test.Case tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertFalse( new SoftString( args1[i] ).equals( new SoftString( args3[i] ) ) );
		}
	}

	@Test.Impl( member = "public int SoftString.hashCode()", description = "Sample cases equal" )
	public void hashCode_SampleCasesEqual( Test.Case tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertEqual( new SoftString( args1[i] ).hashCode(), new SoftString( args2[i] ).hashCode() );
		}
	}
	
	@Test.Impl( member = "public SoftString(String)", description = "Strings longer than threshold are soft" )
	public void SoftString_StringsLongerThanThresholdOrSoft( Test.Case tc ) {
		String big = Strings.rightJustify( "",  this.threshold,  '.' );
		SoftString soft = new SoftString( big );
		tc.assertTrue( this.getSubjectField( soft,  "hard",  "" ) == null );
	}
	
	@Test.Impl( member = "public SoftString(String)", description = "Correct value after collection" )
	public void SoftString_CorrectValueAfterCollection( Test.Case tc ) {
		String value = Strings.rightJustify( "",  this.threshold,  '.' );
		List<SoftString> strings = new ArrayList<>();
		ReferenceQueue<String> rq = new ReferenceQueue<>();
		
		while ( rq.poll() == null ) {
			// String stored in byte file, length = threshold
			SoftString soft = new SoftString( value );
			strings.add( soft );
			
			// Replace its string ref with bogus large string ref, length = 1000000
			this.setSubjectField( soft,  "ref", new SoftReference<String>( new String( new byte[1000000] ), rq ) );
		}

		// GC has run. No guarantee over what was collected. SHOULD be one of our bogus soft refs
		tc.assertTrue( strings.stream().anyMatch( s -> s.toString().equals( value ) ) );
		
		// Verified that sometimes, not all have been collected
		//tc.assertTrue( strings.stream().allMatch( s -> s.toString().equals( value ) ) );
	}
	
	

}
