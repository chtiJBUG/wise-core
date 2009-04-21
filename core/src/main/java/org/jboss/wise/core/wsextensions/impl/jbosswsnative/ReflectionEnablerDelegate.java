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
package org.jboss.wise.core.wsextensions.impl.jbosswsnative;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.apache.xerces.parsers.SecurityConfiguration;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.wsextensions.EnablerDelegate;
import org.jboss.ws.core.StubExt;
import org.jboss.ws.extensions.addressing.jaxws.WSAddressingClientHandler;

/**
 * It is an implementation of {@link EnablerDelegate} providing operation needed to enable extension on jbossws-native stack using
 * reflection to access generated classes.
 * 
 * @author stefano.maestri@javalinux.it
 * @author alessio.soldano@jboss.com
 */
@Immutable
public class ReflectionEnablerDelegate implements EnablerDelegate {

    private final String configFileURL;
    private final String configName;

    /**
     * @param configFileURL
     * @param configName
     */
    public ReflectionEnablerDelegate( String configFileURL,
                                      String configName ) {
        super();
        this.configFileURL = configFileURL;
        this.configName = configName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.EnablerDelegate#visitMTOM(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitMTOM( WSEndpoint endpoint ) throws UnsupportedOperationException {
        ((SOAPBinding)((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding()).setMTOMEnabled(true);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.EnablerDelegate#visitWSAddressing(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSAddressing( WSEndpoint endpoint ) throws UnsupportedOperationException {
        endpoint.addHandler(new WSAddressingClientHandler());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.EnablerDelegate#visitWSRM(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSRM( WSEndpoint endpoint ) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void visitWSSecurity( WSEndpoint endpoint ) throws UnsupportedOperationException, IllegalStateException {

        if (configFileURL == null || configFileURL.length() == 0 || configName == null || configName.length() == 0) {
            throw new IllegalStateException("configFileURL and configName should not be null");
        }

        List<Handler> origHandlerChain = ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().getHandlerChain();
        ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().setHandlerChain(new LinkedList<Handler>());

        if (endpoint.getUnderlyingObjectInstance() instanceof StubExt) {
            URL configFile = getClass().getClassLoader().getResource(configFileURL);
            if (configFile == null) {
                throw new IllegalStateException("Cannot find file: " + configFileURL);
            }
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setSecurityConfig(configFile.toExternalForm());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setConfigName(configName);
        }
        List<Handler> handlerChain = ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().getHandlerChain();
        handlerChain.addAll(origHandlerChain);
        ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().setHandlerChain(handlerChain);

    }

}
