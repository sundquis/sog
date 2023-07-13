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

package sog.util;

import java.util.function.Function;

import sog.core.Procedure;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
@FunctionalInterface
public interface Timed {

	public long getNano();
	
	default public String formatNano() {
		return String.format( "%d ns", this.getNano() );
	}
	
	default public String formatMicro() {
		return String.format( "%.1f \u03BCs", this.getNano() / 1000.0 );
	}
	
	default public String formatMilli() {
		return String.format( "%.1f ms", this.getNano() / 1000000.0 );
	}
	
	default public String formatSecond() {
		return String.format( "%.1f s", this.getNano() / 1000000000.0 );
	}
	
	default public String format() {
		return this.getNano() < 1000 ? this.formatNano()
			: this.getNano() < 1_000_000 ? this.formatMicro()
				: this.getNano() < 1_000_000_000 ? this.formatMilli()
					: this.formatSecond();
	}
	
	
	
	public interface Proc extends Timed, Procedure {}
	
	public static Proc wrap( final Procedure procedure ) {
		return new Proc() {
			
			private long nano = 0L;

			@Override public long getNano() { return this.nano; }

			@Override public void exec() {
				long start = System.nanoTime();
				procedure.exec();
				this.nano = System.nanoTime() - start;
			}
			
		};
	}
	
	
	
	public interface Func<T, R> extends Timed, Function<T, R> {}
	
	public static <T, R> Timed.Func<T, R> wrap( final Function<T, R> function ) {
		return new Func<T, R>() {
			
			private long nano = 0L;

			@Override public long getNano() { return this.nano; }

			@Override public R apply( T t ) {
				long start = System.nanoTime();
				R result = function.apply( t );
				this.nano = System.nanoTime() - start;
				return result;
			}
			
		};
	}
	
	public static void main( String[] args ) {
		/* Toggle class results
		Test.eval( Timed.class )
			.concurrent( false )
			.showDetails( true )
			.print();
		//*/
		
		Procedure p = () -> {
			try { Thread.sleep( 0L, 9 ); } catch ( InterruptedException e ) {}
		};
		Timed.Proc tp = Timed.wrap( p );
		tp.exec();
		System.out.println( "Nano: " + tp.formatNano() );
		System.out.println( "Micro: " + tp.formatMicro() );
		System.out.println( "Milli: " + tp.formatMilli() );
		System.out.println( "Seconds: " + tp.formatSecond() );
		System.out.println( "Time: " + tp.format() );
		
		/* Toggle package results
		Test.evalPackage( Timed.class )
			.concurrent( false )
			.showDetails( true )
			.print();
		//*/

		System.out.println( "\nDone!" );
	}

}
