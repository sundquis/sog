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

package mciv.server.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Params {

	
	private final List<Entry<String, String>> entries;

	/**
	 * The constructor expects a URI.getQuery() string of parameters.
	 * 
	 * @param query
	 */
	public Params( String query ) {
		this.entries = new ArrayList<>();
		
		if ( query != null ) {
			String[] args = query.split( "&" );
			for ( String arg : args ) {
				String[] pair = arg.split( "=" );
				if ( pair.length == 1 ) {
					this.entries.add( Map.entry( pair[0], "" ) );
				} else if ( pair.length == 2 ) {
					this.entries.add( Map.entry( pair[0], pair[1] ) );
				} else {
					this.entries.add( Map.entry( "ERROR", arg ) );
				}
			}
		}
	}
	
	public List<Entry<String,String>> getEntries() {
		return this.entries;
	}

	/**
	 * Find the first Entry with matching key and attempt to parse the corresponding
	 * value as an integer. If not Entry matches, return the default value.
	 * 
	 * @param key
	 * @param dflt
	 * @return
	 * @throws NumberFormatException
	 */
	public int getInt( String key, int defaultValue ) throws NumberFormatException {
		String s = this.entries.stream()
			.filter( e -> e.getKey().equals( key ) )
			.map( Entry::getValue )
			.findFirst()
			.orElse( null );
		
		if ( s == null ) {
			return defaultValue;
		} else {
			return Integer.parseInt( s );
		}
	}
	
	@Override
	public String toString() {
		return "{" + this.entries.stream().map( this::entryToString ).collect( Collectors.joining( ", " ) ) + "}";
	}
	
	private String entryToString( Entry<String,String> entry ) {
		return "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"";
	}
	

}
