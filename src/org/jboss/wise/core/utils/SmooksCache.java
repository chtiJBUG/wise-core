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
package org.jboss.wise.core.utils;

import java.util.HashMap;
import java.util.Map;
import org.milyn.Smooks;

/**
 * Provide a cache of smooks objects internally used bye wise-core
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
public class SmooksCache {

    private static SmooksCache me;

    private final Map<String, Smooks> cache = new HashMap<String, Smooks>();

    public Smooks get( Object key ) {
        return cache.get(key);
    }

    public Smooks put( String key,
                       Smooks value ) {
        return cache.put(key, value);
    }

    public Smooks remove( String key ) {
        return cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    private SmooksCache() {

    }

    public static synchronized SmooksCache getInstance() {
        if (me == null) {
            me = new SmooksCache();
        }
        return me;
    }

    public void clearAll() {
        cache.clear();
    }
}
