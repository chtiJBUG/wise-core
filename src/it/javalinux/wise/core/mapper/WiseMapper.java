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
package it.javalinux.wise.core.mapper;

import it.javalinux.wise.core.exception.MappingException;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;

/**
 * Interface must be implemented by any mapper defined to be used with wise-core
 * 
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
public interface WiseMapper {

    /**
     * apply this mapping to original object
     * 
     * @param originalObjects
     * @return the mapped object in a Map<String,Object>. Keys of this map normally represent symbolic name of mapped Object
     * @throws MappingException
     */
    public Map<String, Object> applyMapping( Object originalObjects ) throws MappingException;

}
