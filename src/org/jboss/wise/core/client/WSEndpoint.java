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
package org.jboss.wise.core.client;

import java.util.Map;
import javax.xml.ws.handler.Handler;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * This represent a WebEndpoint and has utility methods to edit username, password, endpoint address, attach handlers
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
@ThreadSafe
public interface WSEndpoint {

    public Object getUnderlyingObjectInstance();

    public String getWsdlName();

    @GuardedBy( "this" )
    public void setUsername( String username );

    @GuardedBy( "this" )
    public void setPassword( String password );

    public Class getUnderlyingObjectClass();

    public String getMethodName();

    /**
     * Add an Handler to this endpoint. Handler will apply on all endpoint method called
     * 
     * @see #getWSMethods()
     * @param handler
     */
    @GuardedBy( "this" )
    public void addHandler( Handler handler );

    /**
     * Create the webmethods' map and it back. This maps would be used by clients to get a method to call and invoke it All calls
     * of this method apply all handlers added with {@link #addHandler(Handler)} method
     * 
     * @return The list of WebMethod names
     */
    public Map<String, WSMethod> getWSMethods();

    public ClassLoader getClassLoader();

    @GuardedBy( "this" )
    public void enableMTOM();

}
