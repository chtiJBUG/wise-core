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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.jws.WebMethod;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.wsextensions.WSExtensionEnabler;

/**
 * This represent a WebEndpoint and has utility methods to edit username, password, endpoint address, attach handlers
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * @since 09-Sep-2007
 */
@ThreadSafe
public class WSEndpointImpl implements WSEndpoint {

    @GuardedBy( "this" )
    private String name;

    @GuardedBy( "this" )
    private Object underlyingObjectInstance;

    @GuardedBy( "this" )
    private Class underlyingObjectClass;

    @GuardedBy( "this" )
    private String methodName;

    @GuardedBy( "this" )
    ClassLoader classLoader;

    private final Map<String, WSMethod> wsMethods = Collections.synchronizedMap(new TreeMap<String, WSMethod>());

    public WSEndpointImpl() {
        this.wsMethods.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSEndpoint#getWsdlName()
     */
    public String getWsdlName() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSEndpoint#getUnderlyingObjectInstance()
     */
    public synchronized Object getUnderlyingObjectInstance() {
        return underlyingObjectInstance;
    }

    public synchronized void setUnderlyingObjectInstance( Object instance ) {
        this.underlyingObjectInstance = instance;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName( String name ) {
        this.name = name;
    }

    public synchronized String getUrl() {
        return (String)(((BindingProvider)underlyingObjectInstance).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
    }

    public synchronized void setUrl( String url ) {
        ((BindingProvider)underlyingObjectInstance).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }

    public synchronized String getUsername() {
        return (String)(((BindingProvider)underlyingObjectInstance).getRequestContext().get(BindingProvider.USERNAME_PROPERTY));
    }

    /**
     * Set username used for Basic HTTP auth in calling ws
     * 
     * @param username
     */
    public synchronized void setUsername( String username ) {
        ((BindingProvider)underlyingObjectInstance).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
    }

    public synchronized String getPassword() {
        return (String)(((BindingProvider)underlyingObjectInstance).getRequestContext().get(BindingProvider.PASSWORD_PROPERTY));
    }

    /**
     * Set password used for Basic HTTP auth in calling ws
     * 
     * @param password
     */
    public synchronized void setPassword( String password ) {
        ((BindingProvider)underlyingObjectInstance).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
    }

    public synchronized Class getUnderlyingObjectClass() {
        return underlyingObjectClass;
    }

    public synchronized void setUnderlyingObjectClass( Class clazz ) {
        this.underlyingObjectClass = clazz;
    }

    public synchronized String getMethodName() {
        return methodName;
    }

    public synchronized void setMethodName( String methodName ) {
        this.methodName = methodName;
    }

    /**
     * Add an Handler to this endpoint. Handler will apply on all endpoint method called
     * 
     * @see #getWSMethods()
     * @param handler
     */
    public synchronized void addHandler( Handler handler ) {
        List<Handler> handlerChain = ((BindingProvider)underlyingObjectInstance).getBinding().getHandlerChain();
        handlerChain.add(handler);
        ((BindingProvider)underlyingObjectInstance).getBinding().setHandlerChain(handlerChain);
    }

    /**
     * Create the webmethods' map and it back. This maps would be used by clients to get a method to call and invoke it All calls
     * of this method apply all handlers added with {@link #addHandler(Handler)} method
     * 
     * @return The list of WebMethod names
     */
    public synchronized Map<String, WSMethod> getWSMethods() {
        if (wsMethods.size() > 0) {
            return wsMethods;
        }
        for (Method method : this.getUnderlyingObjectClass().getMethods()) {
            WebMethod annotation = method.getAnnotation(WebMethod.class);
            if (annotation != null) {
                wsMethods.put(method.getName(), new WSMethodImpl(method, this));
            }
        }
        return wsMethods;
    }

    public synchronized ClassLoader getClassLoader() {
        return classLoader;
    }

    public synchronized void setClassLoader( ClassLoader classLoader ) {
        this.classLoader = classLoader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSEndpoint#addWSExtension(org.jboss.wise.core.wsextensions.WSExtensionEnabler)
     */
    public void addWSExtension( WSExtensionEnabler enabler ) {
        enabler.enable(this);
    }

}
