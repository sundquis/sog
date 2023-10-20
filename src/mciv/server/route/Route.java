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

import mciv.server.Server;
import sog.core.Test;
import sog.util.Commented;
import sog.util.Macro;
import sog.util.json.JsonReader;
import sog.util.json.JSON;


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
		Root,
		Administration,
		Authorization
	}
	
	
	// This template goes in each concrete route.
	
	/* <API>
	 * <hr>
	 * <h2 id="${path}"><a href="http:/${host}${path}">${path}</a></h2>
	 * <pre>
	 * DESCRIPTION:
	 *   Insert description of potential state transitions.
	 * 	
	 * REQUEST BODY:
	 *   ${Request}
	 * 	
	 * RESPONSE BODY:
	 *   ${Response}
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
	 *   
	 * @param exchange
	 * @param requestBody
	 * @param params TODO
	 * @return
	 * @throws Exception
	 */
	public abstract Response getResponse( HttpExchange exchange, String requestBody, Params params ) throws Exception;
	
	/* Corresponds to turn phase and package. */
	public abstract Category getCategory();
	
	/* Approximate sequence in turn phase. */
	public abstract int getSequence();
	
	/* End point for the route. */
	public abstract String getPath();
	
	/**
	 * The default implementation gives an empty request object.
	 * Routes override by calling super.getRequestAPI() and then adding custom structure.
	 * 
	 * @return
	 */
	public API getRequestAPI() {
		return API.obj( "Request", "The JSON Request object" );
	}
	
	/**
	 * The default implementation gives an empty response object.
	 * Routes override by calling super.getResponseAPI() and then adding custom structure.
	 * 
	 * @return
	 */
	public API getResponseAPI() {
		return API.obj( "Response", "The JSON Response object" );
	}
		
			
	@Override
	public void handle( HttpExchange exchange ) {
		this.executionCount++;
		this.remoteVisit( exchange.getRemoteAddress().getAddress().toString() );
		
		Response response = null;
		String requestBody = null;
		Params params = null;
		
		try {
			//requestBody = new JsonReader( exchange.getRequestBody() ).readObject().toJSON();
			requestBody = new String( exchange.getRequestBody().readAllBytes() );
			params = new Params( exchange.getRequestURI().getQuery() );
			
			response = this.getResponse( exchange, requestBody, params );
			
			if ( exchange.getRequestHeaders().containsKey( "Origin" ) ) {
				String origin = exchange.getRequestHeaders().getFirst( "Origin" );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Origin", origin );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Methods", "POST, GET" );
				exchange.getResponseHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );
			}
			
			if ( this.isLogging() ) {
				Log.get().accept( exchange, requestBody, response.getLength(), params.toString() );
			}

			exchange.sendResponseHeaders( 200, response.getLength() );
			response.getBody().transferTo( exchange.getResponseBody() );
		} catch ( Exception ex ) {
			this.sendErrorResponse( exchange, ex, requestBody, params );
		} finally {
			exchange.close();
			if ( response.afterClose() != null ) {
				response.afterClose().exec();
			}
		}
	}
	
	private void sendErrorResponse( HttpExchange exchange, Exception ex, String requestBody, Params params ) {
		try {
			this.errorCount++;
			Error.get().accept( ex, exchange.getRequestURI().toString() );
			
			String error = JSON.obj()
				.add( "status", JSON.num( 400 ) )
				.add( "type", JSON.str( ex.getClass().toString() ) )
				.add( "message", JSON.str( ex.getMessage() ) )
				.add( "URL_parameters", JSON.str( params.toString() ) )
				.add( "request_body", JSON.str( requestBody ) )
				.toJSON();
			exchange.getResponseHeaders().add( "Content-Type", "application/json" );
			exchange.sendResponseHeaders( 200, error.getBytes().length );
			exchange.getResponseBody().write( error.getBytes() );
		} catch ( IOException e ) {
			// Graceful response failed, log another error
			this.errorCount++;
			Error.get().accept( e, exchange.getRequestURI().toString() );
		}
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
		Macro mapper = new Macro()
			.expand( "path", this.getPath() )
			.expand( "host", Server.SOCKET_ADDRESS )
			.expand( "Request", this.getRequestAPI().getAPI() )
			.expand( "Response", this.getResponseAPI().getAPI() );
		
		return this.getTaggedLines( "API" ).flatMap( mapper );
	}
	
	protected Stream<String> getTaggedLines( String tag ) {
		try {
			return new Commented( this.getClass() ).getTaggedBlock( tag );
		} catch ( IOException e ) {
			Error.get().accept( e, "Tagged lines for " + this.getPath() );
			return Stream.of();
		}
	}
	
	protected Stream<String> getCommentedLines( String label ) {
		try {
			return new Commented( this.getClass() ).getCommentedLines( label );
		} catch ( IOException e ) {
			Error.get().accept( e, "Commented lines for " + this.getPath() );
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
