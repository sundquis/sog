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

package mciv.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import mciv.server.route.Registrar;
import sog.core.App;
import sog.core.AppRuntime;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Server {

	// FIXME: Make port changeable
	
	public static final String SOCKET_ADDRESS = "/23.88.147.138:1104";
	//public static final String SOCKET_ADDRESS = "/localhost:1104";
	
	private static final int MCIV_PORT = 1104;

	private static final Server INSTANCE = new Server();
	
	public static Server get() {
		return Server.INSTANCE;
	}
	
	
	
	private final HttpServer httpServer;
	
	private final ExecutorService exec;
	
	private final Date started;
	
	
	private Server() {
		try {
			this.httpServer = HttpServer.create();
		} catch ( IOException e ) {
			throw new AppRuntime( e );
		}
		this.exec = Executors.newCachedThreadPool();
		this.started = new Date();
	}
	
	public String getStartTime() {
		return this.started.toString();
	}
	
	public String getUpTime() {
		return (int) (new Date().getTime() - this.started.getTime()) / 1000 / 60 / 60 + " hours.";
	}
	
	
	public void bind() throws IOException {
		this.httpServer.bind( new InetSocketAddress( MCIV_PORT ), 0 );
		this.httpServer.setExecutor( exec );
	}
	
	public void load() {
		Registrar.get().getNewRoutes()
			.forEach( r -> { this.httpServer.createContext( r.getPath(), r ); } );
	}
	
	public void start() {
		this.httpServer.start();
	}

	
	public void stop( int delay ) {
		this.exec.shutdown();
		this.exec.shutdownNow();
		this.httpServer.stop( delay );
	}

	
	public static void main( String[] args ) {
		try {
			Server.get().bind();
			Server.get().load();
			Server.get().start();
			App.get().msg( "Server started." );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}
