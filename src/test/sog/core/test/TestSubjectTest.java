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
package test.sog.core.test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import sog.core.App;
import sog.core.Procedure;
import sog.core.Test;
import sog.core.test.Policy;
import sog.core.test.TestCase;
import sog.core.test.TestDecl;
import sog.core.test.TestSubject;
import sog.util.IndentWriter;
import sog.util.StringOutputStream;

/**
 * 
 */
@Test.Skip( "Container" )
public class TestSubjectTest extends Test.Container {
	
	private final Policy originalPolicy;

	public TestSubjectTest() {
		super( TestSubject.class );
		this.originalPolicy = Policy.get();
		//Policy.set( Policy.ALL );		// WTF?
	}
	
	@Override
	public Procedure afterAll() {
		return () -> { Policy.set( this.originalPolicy ); };
	}
	
	public int declarationCount( TestSubject tr ) {
		Map<String, TestDecl> declMap = null;
		return this.getSubjectField( tr, "declMap", declMap ).size();
	}
	
	public int testCaseCount( TestSubject tr ) {
		Set<TestCase> testCases = null;
		return this.getSubjectField( tr, "testCases", testCases ).size();
	}
	
	public void print( TestSubject tr ) {
		tr.print( new IndentWriter( System.out, "\t" ) );
	}
	
	public String skipMessages( TestSubject tr ) {
		List<String> skips = null;
		return this.getSubjectField( tr, "skips", skips ).stream().collect( Collectors.joining( ", " ) );
	}
	
	public String messages( TestSubject tr ) {
		StringOutputStream sos = new StringOutputStream();
		tr.print( new IndentWriter( sos, "" ) );
		return sos.toString();
	}
	
	public String containerLocation( TestSubject tr ) {
		return this.getSubjectField( tr, "containerLocation", "" );
	}
	
	public Test.Container getContainer( TestSubject tr ) {
		return this.getSubjectField( tr, "container", null );
	}

	
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Each valid test declaration is recorded in the declaration mapping" 
	)
	public void tm_06E84278C( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertEqual( 9, this.declarationCount( tr ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Each valid test method implementation has a corresponding TestCase saved" 
	)
	public void tm_0D219DD38( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertEqual( 9, this.testCaseCount( tr ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has a skip message if member is marked as skipped and does not have test declarations",
		weight = 4
	)
	public void tm_0DFF844E5( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		String skipMessages = this.skipMessages( tr );
		tc.assertTrue( skipMessages.contains( "Skip member class for testing" ) );
		tc.assertTrue( skipMessages.contains( "Skip constructor for testing" ) );
		tc.assertTrue( skipMessages.contains( "Skip field for testing" ) );
		tc.assertTrue( skipMessages.contains( "Skip method for testing" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has a skip message if subject is marked as skipped and not marked as subject" 
	)
	public void tm_0276B3961( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SkippedSubject.class );
		String skipMessages = this.skipMessages( tr );
		tc.assertTrue( skipMessages.contains( "Skip this subject" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error if the test container does not name the correct subject class" 
	)
	public void tm_06466FC21( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ContainerNamesTheWrongSubject.class );
		tc.assertTrue( this.messages( tr ).contains( "Container names the wrong subject" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message for non-skipped members that do not have declarations and are required by the current policy" 
	)
	public void tm_082C22D5C( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( UntestedMemberRequiredByTheCurrentPolicy.class );
		tc.assertTrue( this.messages( tr ).contains( "Untested member required by the current policy" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message for test method implementations without a corresponding test declaration" 
	)
	public void tm_0550BC710( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( OrphanedTestImplementation.class );
		tc.assertTrue( this.messages( tr ).contains( "Orphaned test implementation" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if a member is marked as skipped and has test declaration(s)" 
	)
	public void tm_09AD22058( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( MemberHasDeclarationsAndAsMarkedForSkipping.class );
		tc.assertTrue( this.messages( tr ).contains( "Member has declarations and is marked for skipping" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if a memeber has two identical test declarations" 
	)
	public void tm_00DC5F430( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TwoIdenticalTestDeclarations.class );
		tc.assertTrue( this.messages( tr ).contains( "Duplicate declaration" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if an instance of the test container can not be constructed" 
	)
	public void tm_068B56D8B( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( BadContainer.class  );
		tc.assertTrue( this.messages( tr ).contains( "Cannot construct container" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if one test declaration has multiple test method implementations" 
	)
	public void tm_08C2EB022( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TwoTestImplementations.class );
		tc.assertTrue( this.messages( tr ).contains( "Duplicate test implementation" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if subject is marked as subject and marked skipped" 
	)
	public void tm_002F2CD43( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SubjectAndSkipped.class );
		tc.assertTrue( this.messages( tr ).contains( "Subject is also marked to be skipped" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if subject is not a top-level class" 
	)
	public void tm_0D820B0FE( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( NotTopLevel.Inner.class );
		tc.assertTrue( this.messages( tr ).contains( "Subject class is not a top-level class" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if subject is not marked as subject and not marked skipped" 
	)
	public void tm_0863481DD( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SubjectNotAnnotated.class );
		tc.assertTrue( this.messages( tr ).contains( "Subject class is not annotated" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Has an error message if the container location in the Test.Subject annotation is empty" 
	)
	public void tm_0FAF3F88D( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( EmptyContainerLocation.class );
		tc.assertTrue( this.messages( tr ).contains( "Subject annotation has empty container location" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If a parallel test container class is used the classname has 'Test' appended" 
	)
	public void tm_067B2BCB1( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ParallelTestContainer.class );
		tc.assertTrue( this.messages( tr ).contains( "ParallelTestContainerTest" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.run()", 
		description = "If there are no errors afterAll is called after all cases have run" 
	)
	public void tm_0C883E562( Test.Case tc ) {
		TestSubject.forSubject( AfterAllCalled.class );
		tc.assertTrue( AfterAllCalled.TEST.executed );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If not marked as skipped and has valid subject annotation then container location is not empty" 
	)
	public void tm_08BCB74C6( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertTrue( !this.containerLocation( tr ).isEmpty() );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If subject is marked as skipped and not marked as subject there are no test cases" 
	)
	public void tm_05A5284F2( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SkippedSubject.class );
		tc.assertEqual( 0, this.testCaseCount( tr ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If the container location has internal dots the named test container class is used" 
	)
	public void tm_065FF5F4A( Test.Case tc ) {
		TestSubject.forSubject( SubjectWithSpecificContainer.class );
		tc.assertTrue( SpecificContainer.found );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If the container location has no dots a test container class in the same package is used" 
	)
	public void tm_0E1E1B12D( Test.Case tc ) {
		TestSubject.forSubject( SubjectWithSamePackageContainer.class );
		tc.assertTrue( SamePackageContainer.found );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "If the container location starts with a dot a member class test container is used" 
	)
	public void tm_0F3E8A40D( Test.Case tc ) {
		TestSubject.forSubject( SubjectWithMemberContainer.class );
		tc.assertTrue( SubjectWithMemberContainer.Member.found );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.run()", 
		description = "If there are any errors all test cases are counted as failures" 
	)
	public void tm_0CE25B846( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( AllFailWithErrors.class );
		tc.assertTrue( tr.getFailCount() > 0 );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.run()", 
		description = "If there are any errors no test cases are run" 
	)
	public void tm_0CADCECD0( Test.Case tc ) {
		TestSubject.forSubject( AllFailWithErrors.class );
		tc.assertFalse( AllFailWithErrors.TEST.executed );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.run()", 
		description = "If there are no errors all test cases are run" 
	)
	public void tm_08A2F76A7( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertEqual( "9", this.getContainer( tr ).toString() );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Members of skipped member classes are ignored" 
	)
	public void tm_088C372EB( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SkippedMemberClassesIgnored.class );
		tc.assertTrue( tr.getPassCount() > 0 );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Return is not null" 
	)
	public void tm_0FD93921D( Test.Case tc ) {
		tc.assertNonNull( TestSubject.forSubject( Object.class ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Subject constructors are scanned for test obligations" 
	)
	public void tm_0E5F0A2C0( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TestObligations.class );
		// FRAGILE. Could try to retrieve and access individual Err instances.
		tc.assertTrue( this.messages( tr ).contains( "constructor: TestObligations()" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Subject fields are scanned for test obligations" 
	)
	public void tm_0282D26C0( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TestObligations.class );
		// FRAGILE. Could try to retrieve and access individual Err instances.
		tc.assertTrue( this.messages( tr ).contains( "field: int TestObligations.testableField" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Subject member classes are recursively scanned" 
	)
	public void tm_07D880F1A( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TestObligations.class );
		// FRAGILE. Could try to retrieve and access individual Err instances.
		tc.assertTrue( this.messages( tr ).contains( "field: int TestObligations.Inner.testableInnerField" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Subject methods are scanned for test obligations" 
	)
	public void tm_05FC179DF( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TestObligations.class );
		// FRAGILE. Could try to retrieve and access individual Err instances.
		tc.assertTrue( this.messages( tr ).contains( "method: void TestObligations.testableMethod" ) );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Test container class is scanned for test method implementations" 
	)
	public void tm_04818361B( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertEqual( "9", this.getContainer( tr ).toString() );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Test container methods without Test.Impl annotations are ignored" 
	)
	public void tm_03E79E116( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertEqual( "9", this.getContainer( tr ).toString() );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "Throws AssertionError for null subject" 
	)
	public void tm_05A0636B4( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSubject.forSubject( null );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.run()", 
		description = "Unimplemented test declarations count as test failures" 
	)
	public void tm_05E38A64D( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( UnimplementedDeclarations.class );
		tc.assertEqual( 3, tr.getFailCount() );
	}
		
	@Test.Impl( 
		member = "method: TestSubject TestSubject.forSubject(Class)", 
		description = "if the container location ends with a dot a test container class in a parallel package is used" 
	)
	public void tm_090A089C9( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( TestSubject.class );
		String containerLocation = this.containerLocation( tr );
		tc.assertTrue( containerLocation.endsWith( "." ) );
		String containerClassname = this.getContainer( tr ).getClass().getName();
		tc.assertTrue( containerClassname.startsWith( containerLocation ) );
		tc.assertTrue( containerClassname.endsWith( TestSubject.class.getName() + "Test" ) );
	}
		
	@Test.Impl( 
		member = "method: int TestSubject.getFailCount()", 
		description = "Return is the sum of the weights of all failing cases" 
	)
	public void tm_0F73C8FE7( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( FailCount.class );
		tc.assertEqual( 6, tr.getFailCount() );
	}
		
	@Test.Impl( 
		member = "method: int TestSubject.getPassCount()", 
		description = "Return is the sum of the weights of all passing cases" 
	)
	public void tm_0EAD3A681( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( PassCount.class );
		tc.assertEqual( 6, tr.getPassCount() );
	}
		
	@Test.Impl( 
		member = "method: long TestSubject.getElapsedTime()", 
		description = "Reported time is the sum of the times of all test cases" 
	)
	public void tm_03A708E7D( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ElapsdedTime.class );
		tc.assertTrue( tr.getElapsedTime() >= 60L );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addError(Throwable, String, Object[])", 
		description = "Detail objects are included in message" 
	)
	public void tm_0CFB722F7( Test.Case tc ) {
		Object detail = new Object() {};
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		this.evalSubjectMethod( tr, "addError", null, new Exception(), "Message", new Object[] { detail } );
		tc.assertTrue( this.messages( tr ).contains( detail.toString() ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addError(Throwable, String, Object[])", 
		description = "Error messages include description" 
	)
	public void tm_0E40BFC36( Test.Case tc ) {
		Object detail = new Object() {};
		String message = "A really strange message";
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		this.evalSubjectMethod( tr, "addError", null, new Exception(), message, new Object[] { detail } );
		tc.assertTrue( this.messages( tr ).contains( message ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addError(Throwable, String, Object[])", 
		description = "If error is not null includes information on cause(s)" 
	)
	public void tm_00804D5A6( Test.Case tc ) {
		Object detail = new Object() {};
		String message = "A really strange message";
		String err1 = "First error message";
		String err2 = "Second error message";
		Exception e1 = new Exception( err1 );
		Exception e2 = new Exception( err2, e1 );
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		this.evalSubjectMethod( tr, "addError", null, e2, message, new Object[] { detail } );
		tc.assertTrue( this.messages( tr ).contains( err1 ) );
		tc.assertTrue( this.messages( tr ).contains( err2 ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addError(Throwable, String, Object[])", 
		description = "If error is not null location information is printed" 
	)
	public void tm_0B12B5572( Test.Case tc ) {
		Object detail = new Object() {};
		String message = "A really strange message";
		String err = "First error message in exception";
		Exception e = new Exception( err );
		String location = App.get().getLocation( e ).findFirst().get();
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		this.evalSubjectMethod( tr, "addError", null, e, message, new Object[] { detail } );
		tc.assertTrue( this.messages( tr ).contains( location ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addSkip(Object, String)", 
		description = "Skip message includes the reason" 
	)
	public void tm_0A8E65AEF( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		String message = "Reason to skip somoe memeber";
		this.evalSubjectMethod( tr, "addSkip", null, new Object[] { null, message} );
		tc.assertTrue( this.skipMessages( tr ).contains( message ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.addSkip(Object, String)", 
		description = "Skip message includes the source member" 
	)
	public void tm_0F7103BCA( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		Object source = new Object() {};
		String message = "Reason to skip somoe memeber";
		this.evalSubjectMethod( tr, "addSkip", null, new Object[] { source, message} );
		tc.assertTrue( this.skipMessages( tr ).contains( source.toString() ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "If no errors then result details are included" 
	)
	public void tm_01937DAA6( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertTrue( this.messages( tr ).contains( "RESULTS" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "If there are errors then details are included" 
	)
	public void tm_074B4DD86( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( AllFailWithErrors.class );
		tc.assertTrue( this.messages( tr ).contains( "ERRORS" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "Includes details on members that have been skipped" 
	)
	public void tm_023D7CCFF( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( SkippedMemberClassesIgnored.class );
		tc.assertTrue( this.messages( tr ).contains( "SKIPS" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "Includes global summary statistics" 
	)
	public void tm_0C9B26B94( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tc.assertTrue( this.messages( tr ).contains( tr.toString() ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "Includes stubs for unimplemented methods" 
	)
	public void tm_0F59F5383( Test.Case tc ) {
		TestSubject tr = TestSubject.forSubject( UnimplementedDeclarations.class );
		tc.assertTrue( this.messages( tr ).contains( "STUBS" ) );
	}
		
	@Test.Impl( 
		member = "method: void TestSubject.print(IndentWriter)", 
		description = "Throws AssertionError for null writer" 
	)
	public void tm_0A4F4F335( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		TestSubject tr = TestSubject.forSubject( ValidSubject.class );
		tr.print( null );
	}
	
    @Test.Impl( 
    	member = "method: void TestSubject.run()", 
    	description = "If there are no errors beforeAll is called before any cases have run" 
    )
    public void tm_0ACCB12F5( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean TestSubject.equals(Object)", 
    	description = "If compareTo not zero then not equal" 
    )
    public void tm_01554D9E6( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean TestSubject.equals(Object)", 
    	description = "If compareTo zero then equal" 
    )
    public void tm_0CCE3E640( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: boolean TestSubject.noErorrs()", 
    	description = "False after error" 
    )
    public void tm_0328F7955( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int TestSubject.compareTo(TestSubject)", 
    	description = "Alphabetic by subject classname" 
    )
    public void tm_0114C74A4( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
    
    @Test.Impl( 
    	member = "method: int TestSubject.hashCode()", 
    	description = "If equal then same hashCode" 
    )
    public void tm_0E7104431( Test.Case tc ) {
    	tc.addMessage( "GENERATED STUB" );
    }
		

	public static void main( String[] args ) {
	}
	
	
}

@Test.Subject( ".TEST" )
class ValidSubject {

	@Test.Skip( "Skip field for testing" ) public boolean field0 = false;
	@Test.Decl( "Test description" ) public int field1 = 1;
	@Test.Decl( "Test description" ) public String field2 = "hi";
	@Test.Decl( "Test description" ) public List<Integer> field3 = List.of(1, 2, 3);
	
	@Test.Skip( "Skip constructor for testing" ) ValidSubject() {}
	@Test.Decl( "Test description" ) ValidSubject( int i ) {}
	@Test.Decl( "Test description" ) ValidSubject( int i, String s ) {}
	@Test.Decl( "Test description" ) ValidSubject( int i, String s, List<Integer> l ) {}

	@Test.Skip( "Skip method for testing" ) public void method0() {}
	@Test.Decl( "Test description" ) public void method1() {}
	@Test.Decl( "Test description" ) public void method2() {}
	@Test.Decl( "Test description" ) public void method3() {}
	
	@Test.Skip( "Skip member class for testing" )
	public static class TEST extends Test.Container {
		public int testCaseCount = 0;
		
		TEST() { super( ValidSubject.class ); }
		
		@Override public String toString() { return "" + this.testCaseCount; }
		
		// This is ignored
		public void tm_0( TestCase tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "constructor: ValidSubject(int)", description = "Test description" )
		public void tm_1( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "constructor: ValidSubject(int, String)", description = "Test description" )
		public void tm_2( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "constructor: ValidSubject(int, String, List)", description = "Test description" )
		public void tm_3( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "field: List ValidSubject.field3", description = "Test description" )
		public void tm_4( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "field: String ValidSubject.field2", description = "Test description" )
		public void tm_5( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "field: int ValidSubject.field1", description = "Test description" )
		public void tm_6( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "method: void ValidSubject.method1()", description = "Test description" )
		public void tm_7( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "method: void ValidSubject.method2()", description = "Test description" )
		public void tm_8( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }

		@Test.Impl( member = "method: void ValidSubject.method3()", description = "Test description" )
		public void tm_9( Test.Case tc ) { this.testCaseCount++; tc.addMessage( "GENERATED STUB" ); }
	}
}

@Test.Skip( "Skip this subject" )
class SkippedSubject{
	public void needsTest() {}
}

@Test.Subject( ".TEST" )
class ContainerNamesTheWrongSubject {
	public static class TEST extends Test.Container {
		TEST() { super(SkippedSubject.class); }
	}
}

class UntestedMemberRequiredByTheCurrentPolicy {
	public int i = 0;
}

@Test.Subject( ".TEST" )
class OrphanedTestImplementation {
	@Test.Skip( "skip" ) public static class TEST extends Test.Container {
		TEST() { super( OrphanedTestImplementation.class ); }
		@Test.Impl( member = "member", description = "description" ) public void orphan( Test.Case tc ) {}
	}
}

class MemberHasDeclarationsAndAsMarkedForSkipping {
	@Test.Decl( "A test" ) @Test.Skip( "skip" ) public int i = 0;
}

class TwoIdenticalTestDeclarations {
	@Test.Decl( "test" ) @Test.Decl( "test" ) public int i = 0;
}

@Test.Subject( ".TEST" )
class BadContainer {
	public static class TEST extends Test.Container {
		TEST( int i ) { super( BadContainer.class ); }
	}
}

@Test.Subject( ".TEST" )
class TwoTestImplementations {
	@Test.Decl( "test" ) TwoTestImplementations() {}
	@Test.Skip( "skip" ) public static class TEST extends Test.Container {
		TEST() { super( TwoTestImplementations.class ); }
		@Test.Impl( member = "constructor: TwoTestImplementations()", description = "test" )
		public void tm_impl1( Test.Case tc ) { tc.addMessage( "GENERATED STUB" ); }
		@Test.Impl( member = "constructor: TwoTestImplementations()", description = "test" )
		public void tm_impl2( Test.Case tc ) { tc.addMessage( "GENERATED STUB" ); }
	}
}

@Test.Subject( ".TEST" )
@Test.Skip( "skip" )
class SubjectAndSkipped {}

class NotTopLevel {
	public static class Inner {}
}

class SubjectNotAnnotated {}

@Test.Subject( "" )
class EmptyContainerLocation {}

@Test.Subject( "foo." )
class ParallelTestContainer {}

@Test.Subject( ".TEST" )
class AfterAllCalled {
	@Test.Decl( "test" ) AfterAllCalled() {}
	@Test.Skip( "container" ) 
	public static class TEST extends Test.Container {
		static boolean executed = false;

		TEST() { super( AfterAllCalled.class ); }
		
		@Override public Procedure afterAll() { return () -> { TEST.executed = true;}; }
		
		@Test.Impl( member = "constructor: AfterAllCalled()", description = "test" )
		public void tm_0D7053F73( Test.Case tc ) { tc.assertTrue( false ); }
	}
}

@Test.Subject( "test.sog.core.test.SpecificContainer" )
class SubjectWithSpecificContainer {}

class SpecificContainer extends Test.Container {
	public static boolean found = false;
	SpecificContainer() { super( SubjectWithSpecificContainer.class ); SpecificContainer.found = true; }
}

@Test.Subject( "SamePackageContainer" )
class SubjectWithSamePackageContainer {}

class SamePackageContainer extends Test.Container {
	public static boolean found = false;
	SamePackageContainer() { super( SubjectWithSamePackageContainer.class ); SamePackageContainer.found = true; }
}

@Test.Subject( ".Member" )
class SubjectWithMemberContainer {
	public static class Member extends Test.Container {
		public static boolean found = false;
		public Member() { super( SubjectWithMemberContainer.class ); Member.found = true; }
	}
}

@Test.Subject( ".TEST" )
class AllFailWithErrors {
	@Test.Decl( "test 1" ) @Test.Decl( "Test 2" ) public void tested() {}
	public void untested() {}
	
	@Test.Skip( "container" )
	public static class TEST extends Test.Container {
		public static boolean executed = false;
		
		public TEST() { super( AllFailWithErrors.class ); }
		
		@Test.Impl( member = "method: void AllFailWithErrors.tested()", description = "Test 2" )
		public void tm_00603D9D3( Test.Case tc ) { TEST.executed = true; tc.assertTrue( true ); }
			
		@Test.Impl( member = "method: void AllFailWithErrors.tested()", description = "test 1" )
		public void tm_0022971F2( Test.Case tc ) { TEST.executed = true; tc.assertTrue( true ); }
	}
}

@Test.Subject( ".TEST" )
class SkippedMemberClassesIgnored {
	@Test.Skip( "skip" ) public static class Member { public void testable() {} }

	@Test.Decl( "test" ) SkippedMemberClassesIgnored() {}
	
	@Test.Skip( "container" )
	public static class TEST extends Test.Container {
		TEST() { super( SkippedMemberClassesIgnored.class ); }

		@Test.Impl( member = "constructor: SkippedMemberClassesIgnored()", description = "test" )
		public void tm_0DBCF47C9( Test.Case tc ) { tc.assertTrue( true ); }
	}
}

class TestObligations {

	public TestObligations() {}
	
	public int testableField = 0;
	
	public void testableMethod() {}
	
	public static class Inner {
		public int testableInnerField = 0;
	}
	
}

class UnimplementedDeclarations {
	@Test.Decl( "test" ) UnimplementedDeclarations() {}
	@Test.Decl( "test" ) public int i = 0;
	@Test.Decl( "test" ) public void m() {}
}

@Test.Subject( ".TEST" )
class FailCount {

	@Test.Decl( "test" ) public FailCount() {}
	@Test.Decl( "test" ) public void m1() {}
	@Test.Decl( "test" ) public void m2() {}
	
	@Test.Skip( "container" )
	public static class TEST extends Test.Container {
		TEST() { super( FailCount.class ); }

		@Test.Impl( member = "constructor: FailCount()", description = "test" )
		public void tm_0A72809EE( Test.Case tc ) { tc.assertTrue( false ); }
		
		@Test.Impl( member = "method: void FailCount.m1()", description = "test", weight = 2 )
		public void tm_029F3244F( Test.Case tc ) { tc.assertTrue( false ); }
			
		@Test.Impl( member = "method: void FailCount.m2()", description = "test", weight = 3 )
		public void tm_031A4C9AE( Test.Case tc ) { tc.assertTrue( false ); }
	}
}

@Test.Subject( ".TEST" )
class PassCount {

	@Test.Decl( "test" ) public PassCount() {}
	@Test.Decl( "test" ) public void m1() {}
	@Test.Decl( "test" ) public void m2() {}
	
	@Test.Skip( "container" )
	public static class TEST extends Test.Container {
		TEST() { super( PassCount.class ); }

		@Test.Impl( member = "constructor: PassCount()", description = "test" )
		public void tm_0A72809EE( Test.Case tc ) { tc.assertTrue( true ); }
		
		@Test.Impl( member = "method: void PassCount.m1()", description = "test", weight = 2 )
		public void tm_029F3244F( Test.Case tc ) { tc.assertTrue( true ); }
			
		@Test.Impl( member = "method: void PassCount.m2()", description = "test", weight = 3 )
		public void tm_031A4C9AE( Test.Case tc ) { tc.assertTrue( true ); }
	}
}

@Test.Subject( ".TEST" )
class ElapsdedTime {

	@Test.Decl( "test" ) public ElapsdedTime() {}
	@Test.Decl( "test" ) public void m1() {}
	@Test.Decl( "test" ) public void m2() {}
	
	@Test.Skip( "container" )
	public static class TEST extends Test.Container {
		TEST() { super( ElapsdedTime.class ); }

		@Test.Impl( member = "constructor: ElapsdedTime()", description = "test" )
		public void tm_03F5EA4E2( Test.Case tc ) { 
			try { Thread.sleep( 10L ); } catch ( InterruptedException e ) {}
			tc.addMessage( "GENERATED STUB" ); 
		}
			
		@Test.Impl( member = "method: void ElapsdedTime.m1()", description = "test" )
		public void tm_0D96C1045( Test.Case tc ) {
			try { Thread.sleep( 20L ); } catch ( InterruptedException e ) {}
			tc.addMessage( "GENERATED STUB" );
		}
			
		@Test.Impl( member = "method: void ElapsdedTime.m2()", description = "test" )
		public void tm_0E11DB5A4( Test.Case tc ) {
			try { Thread.sleep( 30L ); } catch ( InterruptedException e ) {}
			tc.addMessage( "GENERATED STUB" );
		}
	}
}