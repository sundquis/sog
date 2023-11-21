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

package test.json;

import java.util.Date;

import sog.core.Test;
import sog.core.json.model.Entity;
import sog.core.json.model.Member;
import sog.core.json.model.PKey;

/**
 * 
 */
@Test.Subject( "test." )
public class Person implements Entity {

	public static class KEY extends PKey<Person> {
		
		@Member( order = 3 ) private String email;
		@Member( order = 2 ) private Date date;
		@Member( order = 1 ) private Boolean experienced;
		
		public KEY( String email, Date date, Boolean experienced ) {
			this.email = email;
			this.date = date;
			this.experienced = experienced;
		}
		
	}
	
	
	private final KEY key;
	
	@Member private String name;
	
	@Member private Integer age;
	
	private Person( KEY key ) {
		this.key = key;
	}
	
	public String getEmail() {
		return this.key.email;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge( int age ) {
		this.age = age;
	}


}
