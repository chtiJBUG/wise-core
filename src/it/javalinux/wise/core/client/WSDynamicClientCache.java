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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stefano.maestri@javalinux.it
 */
public class WSDynamicClientCache {
    private static WSDynamicClientCache me = null;

    private final Map<String, WSDynamicClient> clientsMap = Collections.synchronizedMap(new HashMap<String, WSDynamicClient>());

    private WSDynamicClientCache() {
    }

    /**
     * This method return the singleton instance
     * 
     * @return an Instance of this singleton class
     */
    public static synchronized WSDynamicClientCache getInstace() {
        if (me == null) {
            me = new WSDynamicClientCache();
        }

        return me;
    }

    /**
     * Remove all clients from cache Map
     */
    public void clearCache() {
        this.clientsMap.clear();
    }

    public WSDynamicClient get( String key ) {
        return clientsMap.get(key);
    }

    public WSDynamicClient addToCache( String key,
                                       WSDynamicClient value ) {
        return clientsMap.put(key, value);
    }

    public WSDynamicClient removeFromCache( String key ) {
        return clientsMap.remove(key);
    }

}
