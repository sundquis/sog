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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Fatal;
import sog.core.Test;
import sog.core.App;
import sog.core.App.OnShutdown;
import sog.core.xml.XML.DataManager;

@Test.Subject( "test." )
public class XMLSimpleDataManager implements DataManager, OnShutdown {

	private static DataManager instance = null;

	public static DataManager get() {
		if ( XMLSimpleDataManager.instance == null ) {
			synchronized( XMLSimpleDataManager.class ) {
				if ( XMLSimpleDataManager.instance == null ) {
					XMLSimpleDataManager.instance = new XMLSimpleDataManager();
				}
			}
		}
		
		return Assert.nonNull( XMLSimpleDataManager.instance );
	}
	
	
	
	private final Map<Object, XMLClass> loadedObjects;
	
	private final Object lock;
	
	private XMLSimpleDataManager() {
		this.loadedObjects = new HashMap<>();
		this.lock = new Object() {};
		App.get().terminateOnShutdown( this );
	}
	

	@Override
	public void load( Object obj ) throws IOException {
		Assert.nonNull( obj );
		
		XMLClass xmlClass = null;
		synchronized ( this.lock ) {
			if ( this.loadedObjects.containsKey( obj ) ) {
				throw new AppRuntime( "Object for class " + obj.getClass() + " already loaded" );
			}
			xmlClass = new XMLClass( obj );
			this.loadedObjects.put( obj, xmlClass );
		}
		
		// TODO:
		// Find the file and load
	}

	@Override
	public void store( Object obj ) throws IOException {
		Assert.nonNull( obj );
		
		XMLClass xmlClass = null;
		synchronized( this.lock ) {
			xmlClass = loadedObjects.get( Assert.nonNull( obj ) );
			if ( xmlClass == null ) {
				throw new AppRuntime( "Object not loaded" );
			}
		}
		
		xmlClass.write();
	}

	@Override
	public void copy( Object obj ) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void terminate() {
		Consumer<XMLClass> action = (XMLClass xmlClass) -> {
			try {
				xmlClass.write();
			} catch ( IOException e ) {
				Fatal.error( "Unable to save persistent data for " + xmlClass.toString(), e );
			}
		};
		this.loadedObjects.values().stream().forEach( action );
	}
	
}