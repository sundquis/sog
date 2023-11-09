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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.Test;
import sog.util.FixedWidth;
import sog.util.StringAppender;

/**
 * Contains builder methods to construct documentation for JSON request/response objects.
 * 
 * Supported JSON types:
 *   + Objects with member values of any of these supported types
 *   + Arrays of any one supported type
 *   + Strings
 *   + Numeric values expressible as a java int (num) or a java float (dec)
 *   + Boolean
 *   + Enumerated strings or numeric types.
 */
@Test.Subject( "test." )
public class API implements Comparable<API> {


	/**
	 * Entry point to the fluent interface used to construct API documentation.
	 * 
	 * Conventionally, the type follows java class naming conventions.
	 * 
	 * @param type
	 * @param doc
	 * @return
	 */
	public static API obj( String type, String doc ) {
		API result = new API( type, doc );
		result.objects.add( result );
		return result;
	}
	

	

	/* Equality is based on the type name. Must be unique for all contained object types. */
	private final String type;
	
	/* The short documentation string for the API. */
	private final String doc;

	/* The (key, value) pairs. */
	private final List<Member> members;

	/* When the value of a Model is another JSON Object API we add it to this list. */
	private final List<API> objects;
	
	/* Used for formatting members. */
	private FixedWidth fw;

	/* Use the static builder method to construct. */
	private API( String type, String doc ) {
		this.type = type;
		this.doc = doc;
		this.members = new ArrayList<>();
		this.objects = new ArrayList<>();
		this.fw = null;
	}

	
	private API addMember( Member member ) {
		this.members.add( member );
		return this;
	}
	
	private API addObject( API contained ) {
		if ( ! this.objects.contains( contained ) ) {
			this.objects.add( contained );
		}
		return this;
	}
	
	private FixedWidth getFormatter() {
		if ( this.fw == null ) {
			int maxKey = this.members.stream().map( Member::getKey )
				.map( String::length ).max( Integer::compareTo ).orElseGet( () -> 4 );
			
			int maxType = this.members.stream().map( Member::getType )
				.map( String::length ).max( Integer::compareTo ).orElseGet( () -> 4 );
			
			int maxDoc = this.members.stream().map( Member::getDoc )
				.map( String::length ).max( Integer::compareTo ).orElseGet( () -> 4 );

			this.fw = new FixedWidth()
				.sep( "  " )
				.left( "Key", maxKey, ' ' )
				.sep( " : " )
				.left( "Type", maxType,' ' )
				.sep( "  // " )
				.left( "Doc", maxDoc, ' ' );
		}
		
		return this.fw;
	}
	
	@Override
	public int compareTo( API other ) {
		return this.type.compareTo( other.type );
	}
	
	@Override
	public int hashCode() {
		return this.type.hashCode();
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( other != null && other instanceof API ) {
			return this.type.equals( ((API)other).type );
		} else {
			return false;
		}
	}

	/**
	 * Part of the fluent interface. The returned Model has mutators to assign the 
	 * type of the value.
	 * 
	 * @param key
	 * @param doc
	 * @return
	 */
	public Member member( String key, String  doc ) {
		return new Member( key, doc );
	}

	/**
	 * Generates the string representation and appends it to the given sink.
	 * This is the documentation for the top-level JSON object. Each contained object
	 * generates documentation using the getObj method.
	 * 
	 * @param out
	 */
	public void getAPI( StringAppender out ) {
		boolean first = true;
		for ( API api : this.objects ) {
			if ( first ) {
				first = false;
			} else {
				out.appendln();
				out.append( "Where ");
			}
			api.getObj( out );
		}
	}
	
	public List<String> getAPI() {
		List<String> list = new ArrayList<>();
		this.getAPI( StringAppender.wrap( list ) );
		return list;
	}

	private void getObj( StringAppender out ) {
		out.appendln( this.type + ": { // " + this.doc );
		
		this.members.stream().map( Member::format ).forEach( out::appendln );
		
		out.appendln( "}" );
	}

	
	
	
	public class Member {
		
		private final String key;
		
		private final String doc;
		
		private String type;
		
		private int arrayDepth;
		
		private Member( String key, String doc ) {
			this.key = "\"" + key + "\"";
			this.doc = doc;
			this.type = "unknown";
			this.arrayDepth = 0;
		}
		
		private String getKey() { return this.key; }
		
		private String getDoc() { return this.doc; }
		
		private String getType() { return this.type; }
		
		private String format() {
			return API.this.getFormatter().format( this.key, this.type, this.doc ); 
		}
		
		private API setType( String type ) {
			this.type = type + "[]".repeat( this.arrayDepth );
			return API.this.addMember( this );
		}
		
		public Member arrayOf() {
			this.arrayDepth++;
			return this;
		}
		
		public API string( String... values ) {
			return this.setType( "str" + this.toType( values )); 
		}
		
		public API integer( Integer... values ) { 
			return this.setType( "int" + this.toType( values )); 
		}
		
		private <T> String toType( T[] values ) {
			if ( values == null || values.length == 0 ) {
				return "";
			}
			
			if ( values.length > 5 ) {
				return "(" + values[0] + " | "+ values[1] + " | "+ values[2] + " | ... | " + values[values.length-1] + ")";
			} else {
				return "(" + Stream.of( values ).map( Object::toString ).collect( Collectors.joining( " | " ) ) + ")";
			}
		}
		
		public API decimal() { 
			return this.setType( "dec" ); 
		}
		
		public API bool() { 
			return this.setType( "bool" ); 
		}
		
		public API obj( API contained ) {
			this.setType( contained.type );
			return API.this.addObject( contained );
		}
		
		
		
	}

	
	public static void main( String[] args ) {
		API child = API.obj( "Game", "Game state" )
			.member( "id", "The ganme id" ).integer()
			.member( "cur", "The current player" ).string( )
			.member( "A ridiculously long key name", "Foo" ).arrayOf().arrayOf().arrayOf().arrayOf().integer()
			;
		
		API obj = API.obj( "Response", "The JSON Response object" )
			.member( "foo", "The doc for foo" ).string()
			.member( "anInt", "Doc for an int" ).integer()
			.member( "aDEc", "Doc for a decimal" ).decimal()
			.member( "done?", "Doc for the boolean done" ).bool()
			.member( "ids", "Doc for list of ids" ).arrayOf().string()
			.member( "multi", "Rectangular boolean array" ).arrayOf().arrayOf().bool()
			.member( "enum", "A, B, C, ..., Z" ).string( "A", "B", "C", "D", "E", "F", "G", "Z" )
			.member( "int_enum", "0..10" ).integer( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 )
			.member( "enum_array", "Status array" ).arrayOf().string( "ON", "OFF" )
			.member( "game", "The Game" ).obj( child )
			;
		
		child.member( "parent", "Recursive" ).obj( obj );
		
		//PrintWriter out = new PrintWriter( System.out, true );
		//StringBuilder buf = new StringBuilder();

		//obj.getAPI( StringAppender.wrap( buf ) );
		//List<String> results = new ArrayList<>();
		//obj.getAPI( StringAppender.wrap( results ) );
		//results.forEach( System.out::println );
		//System.out.println( buf.toString() );
		obj.getAPI().forEach( System.out::println );
		
		sog.core.App.get().done();
	}

}
