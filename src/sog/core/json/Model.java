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

package sog.core.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Model.Structure marker interface
 * 
 * Structure instances have annotated Member fields.
 * 
 * Member fields can be
 * + Primitive
 * + Non-Entity Structure
 * + List<above>
 * 
 * Entity is persistent Structure
 * 
 * Entity instances can have references to Entity
 * 
 */
public class Model {
	
	private Model() {}
	
	public static interface Structure {}
	
	public static interface Entity extends Structure {}
	
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD } )
	@Documented
	public @interface Member {}
	
	
}
