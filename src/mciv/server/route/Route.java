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

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sog.core.Strings;
import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;


/**
 * Base class for all application routes.
 * 
 * Concrete subclasses are required to support a no-arg constructor so that the Registrar can discover
 * and construct instances.
 * 
 * Concrete routes are located in sub-packages under mciv.server.route so that the Registrar can find them.
 */
@Test.Subject( "test." )
public abstract class Route implements HttpHandler, Comparable<Route> {
	

	/*
	 * Each Route is assigned to a category.
	 * Categories correspond roughly to turn phases and are implemented by Routes in a corresponding
	 * sub-package of mciv.server.route.
	 */
	public static enum Category {
		Administration,
		Authorization
	}
	
	
	// This template goes in each concrete route.
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}">${path}</h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Insert description of potential state transitions.
	 * 	
	 * REQUEST BODY:
	 *   Response: {
	 *   }
	 * 	
	 * RESPONSE BODY:
	 *   Response: {
	 *     "status": int( <values enumerated in mciv.server.route.Codes> ),
	 *     "data": Data,
	 *     "error": Error
	 *   }
	 * 
	 *   Where Data: {
	 *   }
	 * 
	 *   Where Error: [ str ]
	 * 
	 * EXCEPTIONS:
	 *   Description of non-programmatic exceptions.
	 *   Exceptions have status codes enumerated in mciv.server.route.Status
	 *   The application must recover from these.
	 * 
	 * </pre>
	 * <a href="#">Top</a>
	 * 
	 */

	
	private boolean logging;
	
	private int executionCount;
	
	private int errorCount;
	
	private final static Map<String, Integer> remoteCounts = new TreeMap<>();
	
	private final static Comparator<Map.Entry<String, Integer>> remoteComparator = new Comparator<>() {
		@Override
		public int compare( Entry<String, Integer> o1, Entry<String, Integer> o2 ) {
			int count = o2.getValue() - o1.getValue();
			return count == 0 ? o1.getKey().compareTo( o2.getKey() ) : count;
		}
		
	};
	
	protected Route() {
		this.logging = true;
		this.executionCount = 0;
		this.errorCount = 0;
	}

	/**
	 * Perform all necessary state transitions and prepare the response body.
	 * If response is stringified JSON, the implementation must set the header 
	 *   "Content-Type: application/json"
	 *   
	 * @param exchange
	 * @param requestBody
	 * @param params TODO
	 * @return
	 * @throws Exception
	 */
	public abstract Response getResponse( HttpExchange exchange, String requestBody, Map<String, String> params ) throws Exception;
	
	/* Corresponds to turn phase and package. */
	public abstract Category getCategory();
	
	/* Approximate sequence in turn phase. */
	public abstract int getSequence();
	
	/* End point for the route. */
	public abstract String getPath();
		
			
	@Override
	public void handle( HttpExchange exchange ) {
		this.executionCount++;
		this.remoteVisit( exchange.getRemoteAddress().getAddress().toString() );
		
		Response response = null;
		try {
			String requestBody = new String( exchange.getRequestBody().readAllBytes() );
			Map<String, String> params = this.getParams( exchange.getRequestURI().getQuery() );
			
			response = this.getResponse( exchange, requestBody, params );
			String responseBody = response.getBody();
			
			if ( exchange.getRequestHeaders().containsKey( "Origin" ) ) {
				String origin = exchange.getRequestHeaders().getFirst( "Origin" );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", origin );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "POST, GET" );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );
			}
			
			if ( this.isLogging() ) {
				Log.get().accept( exchange, requestBody, responseBody, Strings.toString( params ) );
			}

			exchange.sendResponseHeaders( 200, responseBody.getBytes().length );
			exchange.getResponseBody().write( responseBody.getBytes() );
		} catch ( Exception ex ) {
			this.errorCount++;
			Error.get().accept( ex );
		} finally {
			exchange.close();
			if ( response.afterClose() != null ) {
				response.afterClose().exec();
			}
		}
	}
	
	/* 
	 * Only handles case with simple queries of the form 
	 *     ?key1=value1&key2=value2&...
	 *     
	 * Keys and values can be URL-encoded.
	 */
	public Map<String, String> getParams( String query ) {
		Map<String, String> params = new TreeMap<>();
		
		if ( query != null ) {
			String[] args = query.split( "&" );
			for ( String arg : args ) {
				String[] pair = arg.split( "=" );
				if ( pair.length == 1 ) {
					params.put( pair[0], "true" );
				} else if ( pair.length == 2 ) {
					params.put( pair[0], pair[1] );
				}
			}
		}
		
		return params;
	}
	
	public void remoteVisit( String remote ) {
		Integer count = Route.remoteCounts.get( remote );
		int newCount = count == null ? 1 : count.intValue() + 1;
		Route.remoteCounts.put( remote, newCount );
	}
	
	public Stream<Map.Entry<String, Integer>> getRemoteEntries() {
		SortedSet<Map.Entry<String, Integer>> entries = new TreeSet<>( Route.remoteComparator );
		entries.addAll( Route.remoteCounts.entrySet() );
		return entries.stream();
	}

	
	public Stream<String> getDocunmentation() {
		return this.getTaggedLines( "API" ).flatMap(  new Macro().expand( "path", this.getPath() ) );
	}
	
	protected Stream<String> getTaggedLines( String tag ) {
		try {
			return new Commented( this.getClass() ).getTaggedBlock( tag );
		} catch ( IOException e ) {
			Error.get().accept( e );
			return Stream.of();
		}
	}
	
	protected Stream<String> getCommentedLines( String label ) {
		try {
			return new Commented( this.getClass() ).getCommentedLines( label );
		} catch ( IOException e ) {
			Error.get().accept( e );
			return Stream.of();
		}
	}
		
	public boolean isLogging() {
		return this.logging;
	}
	
	public void setLogging( boolean logging ) {
		this.logging = logging;
	}
	
	@Override
	public int compareTo( Route other ) {
		int cat = this.getCategory().compareTo( other.getCategory() );
		int seq = this.getSequence() - other.getSequence();
		int path = this.getPath().compareTo( other.getPath() );
		return cat != 0 ? cat : seq != 0 ? seq : path;
	}
	
	@Override
	public String toString() {
		return this.getPath();
	}
	
	public int getExecutionCount() {
		return this.executionCount;
	}
	
	public int getErrorCount() {
		return this.errorCount;
	}
	
}
