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
package it.javalinux.wise.core.client;

import java.lang.reflect.Type;
import javax.jws.WebParam;
import net.jcip.annotations.Immutable;

/**
 * Holds single parameter's data required for an invocation
 * 
 * @author stefano.maestri@javalinux.it
 * @since 23-Aug-2007
 */
@Immutable
public interface WebParameter {

    public String getName();

    public Type getType();

    public int getPosition();

    public Enum<WebParam.Mode> getMode();

}
