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
import sog.core.TestOrig;
import sog.core.TestCase;
import sog.core.TestContainer;

/**
 * @author sundquis
 *
 */
public class SoftStringTest implements TestContainer {

	@Override
	public Class<?> subjectClass() {
		return SoftString.class;
	}
	
	private SoftString soft;
	
	private int ORIG_THRESHOLD;
	private int threshold = 5;
	
	@Override
	public Procedure beforeAll() {
		return () -> {
			this.ORIG_THRESHOLD = this.getSubjectField( null,  "THRESHOLD",  0 );
			this.setSubjectField( null,  "THRESHOLD",  this.threshold );
		};
	}
	
	@Override
	public Procedure afterAll() {
		return () -> this.setSubjectField( null,  "THRESHOLD",  this.ORIG_THRESHOLD );
	};
	
	@Override
	public Procedure afterEach() {
		return () -> this.soft = null;
	};

	
	@TestOrig.Impl( src = "public SoftString(String)", desc = "Can construct empty" )
	public void SoftString_CanConstructEmpty( TestCase tc ) {
		soft = new SoftString( "" );
		tc.assertEqual( "",  soft.toString() );
	}

	@TestOrig.Impl( src = "public SoftString(String)", desc = "Can construct long strings" )
	public void SoftString_CanConstructLongStrings( TestCase tc ) {
		String arg = Strings.rightJustify( "42",  this.threshold,  '.' );
		soft = new SoftString( arg );
		tc.assertEqual( arg,  soft.toString() );
	}

	@TestOrig.Impl( src = "public SoftString(String)", desc = "Can construct short strings" )
	public void SoftString_CanConstructShortStrings( TestCase tc ) {
		String arg = "42";
		soft = new SoftString( arg );
		tc.assertEqual( arg,  soft.toString() );
	}

	@TestOrig.Impl( src = "public SoftString(String)", desc = "Throws assertion error for null strings" )
	public void SoftString_ThrowsAssertionErrorForNullStrings( TestCase tc ) {
		tc.expectError( AssertionError.class );
		soft = new SoftString( null );
	}
	
	@TestOrig.Impl( src = "public String SoftString.toString()", desc = "Stress test correct value", weight = 10 )
	public void toString_StressTestCorrectValue( TestCase tc ) throws IOException {
		
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

	
	@TestOrig.Impl( src = "public int SoftString.compareTo(SoftString)", desc = "Can sort large collections" )
	public void compareTo_CanSortLargeCollections( TestCase tc ) throws IOException {
		
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
	
	@TestOrig.Impl( src = "public boolean SoftString.equals(Object)", desc = "Consistent with compare" )
	public void equals_ConsistentWithCompare( TestCase tc ) throws IOException {
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
	
	@TestOrig.Impl( src = "public boolean SoftString.equals(Object)", desc = "Sample cases equal" )
	public void equals_SampleCasesEqual( TestCase tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertEqual( new SoftString( args1[i] ), new SoftString( args2[i] ) );
		}
	}

	@TestOrig.Impl( src = "public boolean SoftString.equals(Object)", desc = "Sample cases not equal" )
	public void equals_SampleCasesNotEqual( TestCase tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertFalse( new SoftString( args1[i] ).equals( new SoftString( args3[i] ) ) );
		}
	}

	@TestOrig.Impl( src = "public int SoftString.hashCode()", desc = "Sample cases equal" )
	public void hashCode_SampleCasesEqual( TestCase tc ) {
		for ( int i = 0; i < args1.length; i++ ) {
			tc.assertEqual( new SoftString( args1[i] ).hashCode(), new SoftString( args2[i] ).hashCode() );
		}
	}
	
	@TestOrig.Impl( src = "public SoftString(String)", desc = "Strings longer than threshold are soft" )
	public void SoftString_StringsLongerThanThresholdOrSoft( TestCase tc ) {
		String big = Strings.rightJustify( "",  this.threshold,  '.' );
		SoftString soft = new SoftString( big );
		tc.assertTrue( this.getSubjectField( soft,  "hard",  "" ) == null );
	}
	
	@TestOrig.Impl( src = "public SoftString(String)", desc = "Correct value after collection" )
	public void SoftString_CorrectValueAfterCollection( TestCase tc ) {
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
	
	

	// Test implementations

	public static void main(String[] args) {

		System.out.println();

		new TestOrig(SoftStringTest.class);
		TestOrig.printResults();

		System.out.println("\nDone!");

	}
}
