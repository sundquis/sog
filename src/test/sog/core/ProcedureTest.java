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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Procedure;
import sog.core.Test;

@Test.Skip( "Container" )
public class ProcedureTest extends Test.Container {
	
	public ProcedureTest() {
		super( Procedure.class );
	}
	
	
	
	// TEST CASES
	
	@Test.Impl( 
		member = "field: Procedure Procedure.NOOP", 
		description = "A non-null Procedure that does nothing" 
	)
	public void tm_08D8EF8D2( Test.Case tc ) {
		tc.assertNonNull( Procedure.NOOP );
		Procedure.NOOP.exec();
	}
		
	@Test.Impl( 
		member = "method: Procedure Procedure.andThen(List)", 
		description = "Result executes given list in order following execution of this" 
	)
	public void tm_035B7CFDD( Test.Case tc ) {
		final List<Integer> results = new ArrayList<Integer>();
		class Proc implements Procedure {
			int n;
			Proc( int n ) { this.n = n; }
			@Override public void exec() { results.add( n ); }
		}
		List<Procedure> more = Stream.of( 1, 2, 3 ).map( Proc::new ).collect( Collectors.toList() );
		Procedure proc = new Proc( 0 );
		proc.andThen( more ).exec();
		tc.assertEqual( List.of( 0, 1, 2, 3 ), results );
	}
		
	@Test.Impl( 
		member = "method: Procedure Procedure.andThen(List)", 
		description = "Throws AssertionError for null list of Procedure more" 
	)
	public void tm_0716FE7BE( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		List<Procedure> more = null;
		Procedure.NOOP.andThen( more ).exec();
	}
		
	@Test.Impl( 
		member = "method: Procedure Procedure.andThen(Procedure)", 
		description = "Result executes given after Procedure following execution of this" 
	)
	public void tm_0C594791E( Test.Case tc ) {
		final List<Integer> results = new ArrayList<Integer>();
		class Proc implements Procedure {
			int n;
			Proc( int n ) { this.n = n; }
			@Override public void exec() { results.add( n ); }
		}
		Procedure proc = new Proc( 0 );
		Procedure after = new Proc( 1 );
		proc.andThen( after ).exec();
		tc.assertEqual( List.of( 0, 1 ), results );
	}
		
	@Test.Impl( 
		member = "method: Procedure Procedure.andThen(Procedure)", 
		description = "Throws AssertionError for null Procedure after" 
	)
	public void tm_08B1082A9( Test.Case tc ) {
		tc.expectError( AssertionError.class );
		Procedure after = null;
		Procedure.NOOP.andThen( after ).exec();
	}

	
	

	public static void main( String[] args ) {
		Test.eval( Procedure.class );
		//Test.evalPackage( Procedure.class );
		//Test.evalDir( Procedure.class, "sog" );
	}
}
