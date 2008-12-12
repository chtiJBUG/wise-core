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
import org.jboss.wise.core.wsextensions.EnablerDelegate;
import org.jboss.ws.core.StubExt;

/**
 * It is an implementation of {@link EnablerDelegate} providing operation needed to enable extension on jbossws-native stack
 * using reflection to access generated classes.
 * 
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionEnablerDelegate implements EnablerDelegate {

    Map<String, NativeSecurityConfig> securityConfigMap;

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
        throw new UnsupportedOperationException("Not yet implemented");
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
            URL configFile = getClass().getClassLoader().getResource(securityConfig.getConfigFileURL());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setSecurityConfig(configFile.toExternalForm());
            ((StubExt)endpoint.getUnderlyingObjectInstance()).setConfigName(securityConfig.getConfigName());
        }

    }

    /**
     * get security config Map<String, {@link NativeSecurityConfig}> where keys are {@link WSEndpoint} names. Intended to be used
     * for IOC (jboss-beans.xml)
     * 
     * @return securityConfigMap
     */
    public final Map<String, NativeSecurityConfig> getSecurityConfigMap() {
        return securityConfigMap;
    }

    /**
     * set security config Map<String, {@link NativeSecurityConfig}> where keys are {@link WSEndpoint} names. Intended to be used
     * for IOC (jboss-beans.xml)
     * 
     * @param securityConfigMap Sets securityConfigMap to the specified value.
     */
    public final void setSecurityConfigMap( Map<String, NativeSecurityConfig> securityConfigMap ) {
        this.securityConfigMap = securityConfigMap;
    }

    /**
     * get default s {@link NativeSecurityConfig} used when {@link WSEndpoint} on which this visitor is enabling extensions isn't
     * find in {@link #getSecurityConfigMap()} Intended to be used for IOC (jboss-beans.xml)
     * 
     * @return defaultSecurityConfig
     */
    public final NativeSecurityConfig getDefaultSecurityConfig() {
        return defaultSecurityConfig;
    }

    /**
     * set default s {@link NativeSecurityConfig} used when {@link WSEndpoint} on which this visitor is enabling extensions isn't
     * find in {@link #getSecurityConfigMap()} Intended to be used for IOC (jboss-beans.xml)
     * 
     * @param defaultSecurityConfig Sets defaultSecurityConfig to the specified value.
     */
    public final void setDefaultSecurityConfig( NativeSecurityConfig defaultSecurityConfig ) {
        this.defaultSecurityConfig = defaultSecurityConfig;
    }
}
