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

import it.javalinux.wise.core.exception.MappingException;
import it.javalinux.wise.core.mapper.WiseMapper;
import java.util.Map;
import net.jcip.annotations.Immutable;

/**
 * Holds the webservice's invocation result's data. Normally this data are kept in Map<String, Object>, but implementor are free
 * to change internal data structure. Anyway it return a Map<String, Object> with webservice's call results, eventually applying a
 * mapping to custom object using a WiseMapper passed to {@link #getMappedResult(WiseMapper, Map)} methods
 * 
 * @author stefano.maestri@javalinux.it
 * @since 29-07-2007
 */
@Immutable
public interface InvocationResult {

    /**
     * Apply WiseMapper provided with to returned Object as defined in wsdl/wiseconsume generated objects. If mapper parameter is
     * null, no mapping are applied and original object are returned.
     * 
     * @param mapper a WiseMapper used to map JAX-WS generated object returned by method call to arbitrary custom object model. It
     *        could be null to don't apply any kind of mappings
     * @param inputMap It's the map of input object used to give them together with output. It's useful when they are needed by
     *        wise's client in same classLoader used by smooks (i.e when wise is used to enrich set of objects like in ESB action
     *        pipeline)
     * @return a Map<String, Object> containing the result of ws calls eventually mapped using WiseMapper provided
     * @throws MappingException rethrown exception got from provided {@link WiseMapper}
     */

    public Map<String, Object> getMappedResult( WiseMapper mapper,
                                                Map<String, Object> inputMap ) throws MappingException;

}
