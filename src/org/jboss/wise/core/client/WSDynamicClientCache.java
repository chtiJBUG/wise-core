/*
 * JBoss, Home of Professional Open Source Copyright 2006, JBoss Inc., and
 * individual contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of individual
 * contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */

package org.jboss.wise.core.client;

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
