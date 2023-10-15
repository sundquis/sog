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

package sog.util.json;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import sog.core.App;
import sog.core.Test;
import sog.util.FixedWidth;
import sog.util.IndentWriter;
import sog.util.Printable;
import sog.util.StringLineBuilder;

/**
 * 
 */
@Test.Subject( "test." )
public class API {
	
	
	private final String name;
	
	private final String doc;
	
	private final List<Member> members;
	
	public API( String name, String doc ) {
		this.name = name;
		this.doc = doc;
		this.members = new ArrayList<>();
	}
	
	public Member member( String name, String doc ) {
		return new Member( name, doc );
	}
	
	private FixedWidth getFormatter() {
		FixedWidth result = new FixedWidth();
		
		int maxName = this.members.stream().map( Member::getName ).map( String::length )
				.max( Comparator.naturalOrder() ).get();
		
		return result;
	}
	
	@Override
	public String toString() {
		StringLineBuilder slb = new StringLineBuilder();
		
		slb.append( "//" ).appendln( this.doc );
		slb.append( this.name ).appendln( ": {" );
		this.members.forEach( slb::appendln );
		slb.appendln( "}" );
		
		return slb.toString();
	}

	
	
	public class Member {
		private final String name;
		private final String doc;
		private String type;
		
		private Member( String name, String doc ) {
			this.name = name;
			this.doc = doc;
			this.type = "";
		}
		
		private String getName() { return this.name; }
		
		private String getType() { return this.type; }
		
		private String getDoc() { return this.doc; }
		
		private API type( String type ) {
			this.type = type;
			return API.this;
		}
		
		public API str() {
			return this.type( "str" );
		}
		
		public API num() {
			return this.type( "int" );
		}
		
		public API dec() {
			return this.type( "dec" );
		}
		
		public API bool() {
			return this.type( "bool" );
		}
		
		public String format( FixedWidth fw ) {
			return fw.format( this.name, this.type, this.doc );
		}
	}
	
	
	public static void main( String[] args ) {
		API api = new API( "Response", "The JSON Response object" )
			.member( "foo", "The doc for foo" ).str()
			.member( "anInt", "Doc for an int" ).num()
			.member( "aDEc", "Doc for a ecimal" ).dec()
			.member( "done?", "Doc for the boolean done" ).bool();
		
		System.out.println( api.toString() );
		App.get().done();
	}

}
