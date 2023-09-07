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
 * A FunctionalInterface for objects that have an inherent processing time associated with them.
 * 
 * Default methods provide formatting for the elapsed time using various scales.
 */
@Test.Subject( "test." )
@FunctionalInterface
public interface Timed {

	/**
	 * Return the processing time associated with this instance.
	 * @return
	 */
	public long getNano();
	
	/**
	 * Return the processing time formatted in nanoseconds.
	 * 
	 * @return
	 */
	@Test.Decl( "Prints elapsed time in nanoseconds" )
	default public String formatNano() {
		return String.format( "%d ns", this.getNano() );
	}
	
	/**
	 * Return the processing time formatted in microseconds.
	 * 
	 * @return
	 */
	@Test.Decl( "Prints elapsed time in microseconds" )
	default public String formatMicro() {
		return String.format( "%.1f \u03BCs", this.getNano() / 1000.0 );
	}

	/**
	 * Return the processing time formatted in milliseconds.
	 * 
	 * @return
	 */
	@Test.Decl( "Prints elapsed time in milliseconds" )
	default public String formatMilli() {
		return String.format( "%.1f ms", this.getNano() / 1000000.0 );
	}

	/**
	 * Return the processing time formatted in seconds.
	 * 
	 * @return
	 */
	@Test.Decl( "Prints elapsed time in seconds" )
	default public String formatSecond() {
		return String.format( "%.1f s", this.getNano() / 1000000000.0 );
	}

	/**
	 * Return the processing time formatted in scale-appropriate form.
	 * 
	 * @return
	 */
	@Test.Decl( "Times less than one microsecond use nanoseconds" )
	@Test.Decl( "Times less than one millisecond use microseconds" )
	@Test.Decl( "Times less than one second use milliseconds" )
	@Test.Decl( "Times more than one second use seconds" )
	default public String format() {
		return this.getNano() < 1000 ? this.formatNano()
			: this.getNano() < 1_000_000 ? this.formatMicro()
				: this.getNano() < 1_000_000_000 ? this.formatMilli()
					: this.formatSecond();
	}
	
	

	/**
	 * Marker interface for a Procedure that is also Timed.
	 */
	public interface Proc extends Timed, Procedure {}

	/**
	 * Wrap the given Procedure in a Timed.Proc. The associated processing time is the
	 * execution time of the given Procedure.
	 * 
	 * @param procedure
	 * @return
	 */
	@Test.Decl( "Return is a non-null Procedure that is Timed" )
	public static Proc wrap( final Procedure procedure ) {
		
		return new Proc() {
			
			private long nano = 0L;

			@Override 
			@Test.Decl( "Processing time reflects the execution time" )
			public long getNano() { return this.nano; }

			@Override 
			@Test.Decl( "Given Procedure.exec() is called" )
			public void exec() {
				long start = System.nanoTime();
				procedure.exec();
				this.nano = System.nanoTime() - start;
			}
			
		};
	}
	
	

	/**
	 * Marker interface for a Function that is also Timed.
	 */
	public interface Func<T, R> extends Timed, Function<T, R> {}
	
	/**
	 * Wrap the given Function in a Timed.Func. The associated processing time is the
	 * execution time of the given Function.
	 * 
	 * @param procedure
	 * @return
	 */
	@Test.Decl( "Return is a non-null Function that is Timed" )
	public static <T, R> Timed.Func<T, R> wrap( final Function<T, R> function ) {
		
		return new Func<T, R>() {
			
			private long nano = 0L;

			@Override 
			@Test.Decl( "Processing time reflects the execution time" )
			public long getNano() { return this.nano; }

			@Override 
			@Test.Decl( "Given Function.apply() is called" )
			public R apply( T t ) {
				long start = System.nanoTime();
				R result = function.apply( t );
				this.nano = System.nanoTime() - start;
				return result;
			}
			
		};
	}
	


}
