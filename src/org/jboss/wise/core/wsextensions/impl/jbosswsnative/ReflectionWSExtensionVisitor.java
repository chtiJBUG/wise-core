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
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.wsextensions.WSExtensionVisitor;
import org.jboss.ws.core.StubExt;

/**
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionWSExtensionVisitor implements WSExtensionVisitor {

    Map<String, NativeSecurityConfig> securityConfigMap;

    NativeSecurityConfig defaultSecurityConfig;

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionVisitor#visitMTOM(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitMTOM( WSEndpoint endpoint ) throws UnsupportedOperationException {
        ((SOAPBinding)((BindingProvider)endpoint.getUnderlyingObjectInstance()).getBinding()).setMTOMEnabled(true);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionVisitor#visitWSAddressing(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSAddressing( WSEndpoint endpoint ) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionVisitor#visitWSRM(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSRM( WSEndpoint endpoint ) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionVisitor#visitWSSecurity(org.jboss.wise.core.client.WSEndpoint)
     */
    public void visitWSSecurity( WSEndpoint endpoint ) throws UnsupportedOperationException {

        NativeSecurityConfig securityConfig = securityConfigMap.get(endpoint.getMethodName());

        if (securityConfig == null) {
            securityConfig = defaultSecurityConfig;
        }

        String keystoreLocation = securityConfig.getKeystoreLocation() != null ? securityConfig.getKeystoreLocation() : defaultSecurityConfig.getKeystoreLocation();
        String trustStoreLocation = securityConfig.getTrustStoreLocation() != null ? securityConfig.getTrustStoreLocation() : defaultSecurityConfig.getTrustStoreLocation();

        URL keystore = getClass().getClassLoader().getResource(keystoreLocation);
        URL trustStore = getClass().getClassLoader().getResource(trustStoreLocation);

        System.setProperty("org.jboss.ws.wsse.keyStore", keystore.getFile());
        System.setProperty("org.jboss.ws.wsse.trustStore", trustStore.getFile());
        System.setProperty("org.jboss.ws.wsse.keyStorePassword", "jbossws");
        System.setProperty("org.jboss.ws.wsse.trustStorePassword", "jbossws");
        System.setProperty("org.jboss.ws.wsse.keyStoreType", "jks");
        System.setProperty("org.jboss.ws.wsse.trustStoreType", "jks");

        if (endpoint.getUnderlyingObjectInstance() instanceof StubExt) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA+" + securityConfig.getConfigFileURL());
            URL configFile = getClass().getClassLoader().getResource(securityConfig.getConfigFileURL());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setSecurityConfig(configFile.toExternalForm());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setConfigName(securityConfig.getConfigName());
        }

    }

    /**
     * @return securityConfigMap
     */
    public final Map<String, NativeSecurityConfig> getSecurityConfigMap() {
        return securityConfigMap;
    }

    /**
     * @param securityConfigMap Sets securityConfigMap to the specified value.
     */
    public final void setSecurityConfigMap( Map<String, NativeSecurityConfig> securityConfigMap ) {
        this.securityConfigMap = securityConfigMap;
    }

    /**
     * @return defaultSecurityConfig
     */
    public final NativeSecurityConfig getDefaultSecurityConfig() {
        return defaultSecurityConfig;
    }

    /**
     * @param defaultSecurityConfig Sets defaultSecurityConfig to the specified value.
     */
    public final void setDefaultSecurityConfig( NativeSecurityConfig defaultSecurityConfig ) {
        this.defaultSecurityConfig = defaultSecurityConfig;
    }
}
