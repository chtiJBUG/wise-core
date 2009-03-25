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
import net.jcip.annotations.ThreadSafe;
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
@ThreadSafe
public class ReflectionEnablerDelegate implements EnablerDelegate {

    Map<String, NativeSecurityConfig> securityConfigMap = Collections.synchronizedMap(new HashMap<String, NativeSecurityConfig>());

    NativeSecurityConfig defaultSecurityConfig;

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

    /**
     * {@inheritDoc} Note it uses {@link NativeSecurityConfig} associated to passed endpoint with {@link #getSecurityConfigMap()}.
     * If there isn't specific configuration associated to this endpoint {@link #getDefaultSecurityConfig()} is used. Note also if
     * specific configuration doesn't define keystoreLocation or truststoreLocation they are taken form
     * {@link #defaultSecurityConfig}.
     * 
     * @see org.jboss.wise.core.wsextensions.EnablerDelegate#visitWSSecurity(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSSecurity( WSEndpoint endpoint ) throws UnsupportedOperationException, IllegalStateException {

        NativeSecurityConfig securityConfig = getSecurityConfigMap() != null ? getSecurityConfigMap().get(endpoint.getName()) : null;

        if (securityConfig == null) {
            securityConfig = this.getDefaultSecurityConfig();
        }

        if (securityConfig == null) {
            throw new IllegalStateException("Configure at least a default NativeSecurityConfig for WSSE in jboss-beans.xml");
        }

        List<Handler> origHandlerChain = ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().getHandlerChain();
        ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().setHandlerChain(new LinkedList<Handler>());

        if (endpoint.getUnderlyingObjectInstance() instanceof StubExt) {
            URL configFile = getClass().getClassLoader().getResource(securityConfig.getConfigFileURL());
            if (configFile == null) {
                throw new IllegalStateException("Cannot find file: " + securityConfig.getConfigFileURL());
            }
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setSecurityConfig(configFile.toExternalForm());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setConfigName(securityConfig.getConfigName());
        }
        List<Handler> handlerChain = ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().getHandlerChain();
        handlerChain.addAll(origHandlerChain);
        ((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding().setHandlerChain(handlerChain);

    }

    /**
     * get security config Map<String, {@link NativeSecurityConfig}> where keys are {@link WSEndpoint} names. Intended to be used
     * for IOC (jboss-beans.xml)
     * 
     * @return securityConfigMap
     */
    public synchronized final Map<String, NativeSecurityConfig> getSecurityConfigMap() {
        return securityConfigMap;
    }

    /**
     * set security config Map<String, {@link NativeSecurityConfig}> where keys are {@link WSEndpoint} names. Intended to be used
     * for IOC (jboss-beans.xml)
     * 
     * @param securityConfigMap Sets securityConfigMap to the specified value.
     */
    public synchronized final void setSecurityConfigMap( Map<String, NativeSecurityConfig> securityConfigMap ) {
        this.securityConfigMap.clear();
        this.securityConfigMap.putAll(securityConfigMap);
    }

    /**
     * get default s {@link NativeSecurityConfig} used when {@link WSEndpoint} on which this visitor is enabling extensions isn't
     * find in {@link #getSecurityConfigMap()} Intended to be used for IOC (jboss-beans.xml)
     * 
     * @return defaultSecurityConfig
     */
    public synchronized final NativeSecurityConfig getDefaultSecurityConfig() {
        return defaultSecurityConfig;
    }

    /**
     * set default s {@link NativeSecurityConfig} used when {@link WSEndpoint} on which this visitor is enabling extensions isn't
     * find in {@link #getSecurityConfigMap()} Intended to be used for IOC (jboss-beans.xml)
     * 
     * @param defaultSecurityConfig Sets defaultSecurityConfig to the specified value.
     */
    public synchronized final void setDefaultSecurityConfig( NativeSecurityConfig defaultSecurityConfig ) {
        this.defaultSecurityConfig = defaultSecurityConfig;
    }
}