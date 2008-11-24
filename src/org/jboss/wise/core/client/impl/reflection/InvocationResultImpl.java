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

package org.jboss.wise.core.client.impl.reflection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.mapper.WiseMapper;

/**
 * Holds the webservice's invocation result's data. Can apply a mapping to custom object using a WiseMapper passed to
 * {@link #getMappedResult(WiseMapper,Map)} methods
 * 
 * @author stefano.maestri@javalinux.it
 */
@Immutable
@ThreadSafe
public class InvocationResultImpl implements InvocationResult {

    private final Map<String, Object> originalObjects;

    /**
     * @param name
     * @param value
     * @param results
     */
    public InvocationResultImpl( String name,
                                 Object value,
                                 Map<String, Object> results ) {

        this.originalObjects = new HashMap<String, Object>();
        if (results == null) {
            results = Collections.EMPTY_MAP;
        }
        this.originalObjects.putAll(results);
        if (name != null && name.trim().length() != 0) {
            this.originalObjects.put(name, value);
        }
    }

    /**
     * Apply WiseMapper provided with to returned Object as defined in wsdl/wiseconsume generated objects
     * 
     * @param mapper a WiseMapper used to map JAX-WS generated object returned by method call to arbitrary custom object model
     * @param inputMap It's the map of input object used to give them together with output. It's useful when they are needed by
     *        wise's client in same classLoader used by smooks (i.e when wise is used to enrich set of objects like in ESB action
     *        pipeline)
     * @return a Map<String, Object> containing the result od ws calls eventually remapped using WiseMapper provided
     * @throws MappingException
     */
    // TODO: demostrate with unit test how it can be used for message enrichement in same class loader and
    // integrating input and output in same object model
    public Map<String, Object> getMappedResult( WiseMapper mapper,
                                                Map<String, Object> inputMap ) throws MappingException {

        if (inputMap == null) {
            inputMap = new HashMap<String, Object>();
        }
        inputMap.put("results", originalObjects);
        Map<String, Object> mappedResult = new HashMap<String, Object>();
        if (mapper != null) {
            mappedResult.putAll(mapper.applyMapping(inputMap));
        } else {
            mappedResult.putAll(inputMap);
        }
        return mappedResult;

    }

}
