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

import sog.core.App;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Error {
	
	private static final Error INSTANCE = new Error();
	
	public static Error get() {
		return Error.INSTANCE;
	}
	
	
	public static class Entry {
		
	}
	
	private Error() {}
	
	public void accept( Exception ex ) {
		// FIXME
		App.get().msg( ex );
		App.get().getLocationMatching( ex, "^sog.*|^mciv.*"  ).forEach( System.out::println );
		//ex.printStackTrace();
	}


}
