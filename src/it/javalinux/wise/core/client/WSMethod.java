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

import it.javalinux.wise.core.exception.InvocationException;
import it.javalinux.wise.core.exception.MappingException;
import it.javalinux.wise.core.mapper.WiseMapper;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;

/**
 * Represent a webservice operation invocation attached to a specific endpoint.
 * 
 * @author stefano.maestri@javalinux.it
 * @since 23-Aug-2007
 */
@ThreadSafe
public interface WSMethod {

    /**
     * Invokes this method with the provided arguments applying provided mapper
     * 
     * @param args the arguments to call operation. It could be a generic Object to be passed to provided mapper. If mapper is
     *        null args must be a Map<String, Object>. This Map have to contain entries for all needed parameters, keys have to
     *        reflect operation parameter name as defined in wsdl. Keys which names are not defined in wsdls will be simply
     *        ignored. Implementation will take care values nullability will reflect "nillable" properties defined in wsdl. order
     *        isn't important since WSMethod implementation will take care of reorder parameters in right position to make
     *        operation call. If it isn't a Map<String, Object> or keys don't contain all parameters name an
     *        {@link IllegalArgumentException} is thrown.
     * @param mapper if null no mappings are applied method will be invoked using args directly. in this case the keys of the map
     *        gotta be the parameters names as defined in wsdl/wsconsume generated classes
     * @return return an {@link InvocationResult} object populated with returned values (implementation will process both directed
     *         returned values and OUT parameters as defined in wsdl)
     * @throws InvocationException
     * @throws IllegalArgumentException
     * @throws MappingException
     */
    public InvocationResult invoke( Object args,
                                    WiseMapper mapper ) throws InvocationException, IllegalArgumentException, MappingException;

    /**
     * Gets the map of {@link WebParameter} for the webserice method represented by instance of this type
     * 
     * @return a Map<String, Object> representing valid webparameters where keys contain symbolic names as defined by wsdl. It
     *         may be null in case of selected operation haven't parameter.
     */
    public Map<String, ? extends WebParameter> getWebParams();

    /**
     * @return true if operation is defined as OneWay in wsdl
     */
    public boolean isOneWay();

    /**
     * @return the endpoint on which this method is attached.
     */
    public WSEndpoint getEndpoint();

    /**
     * @param endpoint set the endpoint on which this method is attached.
     */
    public void setEndpoint( WSEndpoint endpoint );
}
