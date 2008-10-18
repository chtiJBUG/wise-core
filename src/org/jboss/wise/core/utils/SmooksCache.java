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

package org.jboss.wise.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.milyn.Smooks;

/**
 * Provide a cache of smooks objects internally used bye wise-core
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
public class SmooksCache {

    private static SmooksCache me;

    private final Map<String, Smooks> cache = new ConcurrentHashMap<String, Smooks>();

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
