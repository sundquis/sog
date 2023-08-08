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

package sog.core.xml.data;



import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import sog.core.App;
import sog.core.Assert;
import sog.core.LocalDir;
import sog.core.Test;
import sog.core.xml.XML;

/**
 * 
 */
@Test.Subject( "test." )
public class XMLClass {
	
	private final Object instance;
	
	private final Class<?> targetClass;
	
	private final Path dataFile;
	
	XMLClass( Object obj ) {
		Assert.nonNull( obj );
		Assert.isNull( obj.getClass().getEnclosingClass(), "Can only use persistent values with top-level classes." );
		
		this.instance = obj;
		this.targetClass = obj.getClass();
		LocalDir dir = new LocalDir().sub( "data" );
		Arrays.stream( this.targetClass.getPackageName().split( "\\." ) ).forEach( dir::sub );
		this.dataFile = dir.getFile( this.targetClass.getSimpleName(), LocalDir.Type.XML );
	}
	
	void write() throws IOException {
		try ( XMLWriter out = new XMLWriter( this.dataFile ) ) {
			out.declaration().startClass( this.targetClass );
			
			Arrays.stream( this.targetClass.getDeclaredFields() )
				.map( XMLField::new )
				.filter( XMLField::persistent )
				.forEach( f -> this.writeField( out, f ) );
			
			out.endClass();
		}
	}
	
	private void writeField( XMLWriter out, XMLField field ) {
		out.startField( field );
	}
	
	@Override
	public String toString() {
		return "XMLClass( " + this.targetClass + ")";
	}

	
	public static void main( String[] args ) {
		try {
			new XMLClass( new Secondary() ).write();
		} catch ( Throwable e ) {
			e.printStackTrace();
		}
		
		System.out.println( "\nDone!" );
	}

}

class Secondary {
	private String ignoreThis;

	@XML.Data private String privateString;
	@XML.Data private Integer privateInteger;
	@XML.Data protected String protectedString;
	@XML.Data protected Integer protectedInteger;
	@XML.Data String packageString;
	@XML.Data Integer packageInteger;
	
}