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
package it.javalinux.wise.core.client.impl.reflection;

import it.javalinux.wise.core.client.WSEndpoint;
import it.javalinux.wise.core.client.WSService;
import it.javalinux.wise.core.exception.WiseRuntimeException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.WebEndpoint;
import net.jcip.annotations.Immutable;

/**
 * @author stefano.maestri@javalinux.it
 */
@Immutable
public class WSServiceImpl implements WSService {

    private final Class serviceClass;
    private final URLClassLoader classLoader;
    private final Object service;
    private final String userName;
    private final String password;
    private Map<String, WSEndpoint> endpoints = Collections.synchronizedMap(new HashMap<String, WSEndpoint>());
    

    /**
     * @param serviceClass
     * @param classLoader
     * @param service
     * @param userName
     * @param password
     */
    public WSServiceImpl( Class serviceClass,
                          URLClassLoader classLoader,
                          Object service,
                          String userName,
                          String password ) {
        super();
        this.serviceClass = serviceClass;
        this.classLoader = classLoader;
        this.service = service;
        this.userName = userName;
        this.password = password;
        endpoints.clear();
    }

    /**
     * Create the endpoints' map and gives it back.
     * 
     * @return The Map of WSEndpoint with symbolic names as keys
     */
    public Map<String, WSEndpoint> processEndpoints() {
        if (endpoints.size() > 0 ) {
            return endpoints;
        }
        
        for (Method method : this.getServiceClass().getMethods()) {
            WebEndpoint annotation = method.getAnnotation(WebEndpoint.class);
            if (annotation != null) {
                WSEndpoint ep;
                try {
                    ep = this.getWiseEndpoint(method);
                    endpoints.put(annotation.name(), ep);
                } catch (WiseRuntimeException e) {
                    e.printStackTrace();
                }

            }
        }
        return endpoints;
    }

    private WSEndpoint getWiseEndpoint( Method method ) throws WiseRuntimeException {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        WSEndpointImpl ep = new WSEndpointImpl();
        try {
            Thread.currentThread().setContextClassLoader(this.getClassLoader());
            ep.setClassLoader(this.getClassLoader());
            ep.setUnderlyingObjectInstance(this.getServiceClass().getMethod(method.getName(), method.getParameterTypes()).invoke(this.getService(),
                                                                                                                                 (Object[])null));
            ep.setUnderlyingObjectClass(this.getServiceClass().getMethod(method.getName(), method.getParameterTypes()).getReturnType());
            if (userName != null && password != null) {
                ep.setUsername(userName);
                ep.setPassword(password);
            }

        } catch (Exception e) {
            throw new WiseRuntimeException("Error while reading an endpoint!", e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
        return ep;
    }

    @SuppressWarnings( "unchecked" )
    private synchronized final Class getServiceClass() {
        return serviceClass;
    }

    public synchronized final URLClassLoader getClassLoader() {
        return classLoader;
    }

    private synchronized final Object getService() {
        return service;
    }

}
