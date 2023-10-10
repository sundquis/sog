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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import mciv.server.route.Registrar;
import sog.core.Test;

/**
 * 
 */
@Test.Subject( "test." )
public class Server {
	
	private static final int MCIV_PORT = 1104;

	private static Server server;
	
	
	
	private final HttpServer httpServer;
	
	private final ExecutorService exec;
	
	
	private Server() throws IOException {
		this.httpServer = HttpServer.create( new InetSocketAddress( MCIV_PORT ), 0 );
		this.exec = Executors.newCachedThreadPool();
		this.httpServer.setExecutor( exec );
		
		Registrar.get().getRoutes().forEach( r -> httpServer.createContext( r.getPath(), r ) );
		this.httpServer.start();
	}

	
	public static void stop( int delay ) {
		Server.server.exec.shutdown();
		Server.server.exec.shutdownNow();
		Server.server.httpServer.stop( delay );
	}

	
	public static void main( String[] args ) {
		try {
			Server.server = new Server();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}
