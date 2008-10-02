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
package org.jboss.wise.core.exception;

/**
 * This exception is for connection/authentication failure issues.
 * 
 * @author alessio.soldano@javalinux.it
 */
public class WiseConnectionException extends Exception {

    private static final long serialVersionUID = -4622247006256827035L;

    public WiseConnectionException( String message ) {
        super(message);
    }

    public WiseConnectionException( String message,
                                    Throwable cause ) {
        super(message, cause);
    }

    public WiseConnectionException( Throwable cause ) {
        super(cause);
    }
}
