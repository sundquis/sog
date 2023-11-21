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

package sog.core.json.model;

import java.util.SortedMap;
import java.util.TreeMap;

import sog.core.App;
import sog.core.Assert;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class ID<E extends Entity> {

	private final static class State extends Singleton {
		
		@Member private SortedMap<String, Integer> classToCID = new TreeMap<>();
		
		@Member private Integer nextCID = 130_691_232;  // 42^5
		
		@Member private Long nextIID = 717_368_321_110_468_608L;  // 42^11
		
		private long nextIID() {
			long result = this.nextIID;
			this.nextIID += 130691237;  // Prime after 42^5
			return result;
		}
		
		private int getCID( String entityClassName ) {
			Integer result = this.classToCID.get( entityClassName );
			if ( result == null ) {
				result = this.nextCID;
				this.nextCID += 74093;  // Prime after 42^3
				this.classToCID.put( entityClassName, result );
			}
			return result;
		}
		
	}
	
	private final static State state = Singleton.getInstance( State.class );
	
	
	final static synchronized <F extends Entity> ID<F> createID( Class<F> entityClass ) {
		int cid = ID.state.getCID( Assert.nonNull( entityClass ).getCanonicalName() );
		long iid = ID.state.nextIID();
		return new ID<>( cid, iid );
	}
	
	
	
	private final int cid;
	
	private final long iid;
	
	private ID( int cid, long iid ) {
		this.cid = cid;
		this.iid = iid;
	}
	
	@Override
	public String toString() {
		return Integer.toHexString( this.cid ) + ":" + Long.toHexString( this.iid );
	}
	
	
	
	private static class SomeEntity implements Entity {}
	private static class AnotherEntity implements Entity {}
	private static class ThirdEntity implements Entity {}

	public static void main( String[] args ) {
//		App.get().msg( "CID: " + ID.state.nextCID );
//		App.get().msg( "IID: " + ID.state.nextIID );
//		App.get().msg( sog.core.Strings.toString( ID.state.classToCID ) );
		
		App.get().msg( ID.createID( SomeEntity.class  ) );
		App.get().msg( ID.createID( SomeEntity.class  ) );
		App.get().msg( ID.createID( AnotherEntity.class  ) );
		App.get().msg( ID.createID( SomeEntity.class  ) );
		App.get().msg( ID.createID( AnotherEntity.class  ) );
		App.get().msg( ID.createID( ThirdEntity.class  ) );
		
//		App.get().msg( "ID: " + id.toString() );
//		App.get().msg( "CID: " + ID.state.nextCID );
//		App.get().msg( "IID: " + ID.state.nextIID );
//		App.get().msg( sog.core.Strings.toString( ID.state.classToCID ) );
		
//		int x = Integer.MAX_VALUE + 1;
//		App.get().msg( "Max = " + Integer.toHexString( Integer.MAX_VALUE ) );
//		App.get().msg( "Min = " + Integer.toHexString( Integer.MIN_VALUE ) );
//		App.get().msg( "max + 1 = " + Integer.toHexString( x ) );
		App.get().done();
	}

}
