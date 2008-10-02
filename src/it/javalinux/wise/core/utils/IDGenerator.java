/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package it.javalinux.wise.core.utils;

/**
 * Generates unique ID numbers, almost like
 * a database sequence.
 * 
 * @author alessio.soldano@javalinux.it
 * @since 25-Apr-2005
 */
public final class IDGenerator {
	
	private static long counter = 0;
	
	
	public static final synchronized long nextVal() {
		if (counter>=Long.MAX_VALUE-1) {
			counter = 0;
		}
		return counter++;
	}
}
