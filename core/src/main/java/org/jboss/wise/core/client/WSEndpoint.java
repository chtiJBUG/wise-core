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

import java.util.List;
import java.util.Map;
import javax.xml.ws.handler.Handler;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.wsextensions.WSExtensionEnabler;

/**
 * This represent a Endpoint(Port) and has utility methods to edit username, password, endpoint address, attach handlers
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * @author <a href="ema@redhat.com">Jim Ma</a>
 */
@ThreadSafe
public interface WSEndpoint {
    public Object createInstance();

    public String getWsdlName();

    @GuardedBy( "this" )
    public void setUsername( String username );

    @GuardedBy( "this" )
    public void setPassword( String password );

    public Class getUnderlyingObjectClass();

    @GuardedBy( "this" )
    public String getName();

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

    /**
     * @return classLoader used to load JAXWS generated object see also {@link #getUnderlyingObjectClass()}
     */
    public ClassLoader getClassLoader();

    /**
     * Use this method to add WSExtension you would enable on this endpoint. Of course extension have to be enabled before you cal
     * method associated to action you are going to invoke. Not necessary before you build WSMethods object associated to this
     * endpoint {@link #getWSMethods()} see also {@link WSExtensionEnabler} for more information on how to enable WSExtensions
     * 
     * @param enabler it is an implementation of {@link WSExtensionEnabler}
     */
    @GuardedBy( "this" )
    public void addWSExtension( WSExtensionEnabler enabler );

    /**
     * @return handlers
     */
    @GuardedBy( "this" )
    public List<Handler<?>> getHandlers();

    /**
     * @return extensions
     */
    @GuardedBy( "this" )
    public List<WSExtensionEnabler> getExtensions();

    @GuardedBy( "this" )
    public String getTargetUrl();

    @GuardedBy( "this" )
    public String getUsername();

    @GuardedBy( "this" )
    public String getPassword();

    @GuardedBy( "this" )
    public void setTargetUrl( String targetUrl );

}
