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

package org.jboss.wise.core.client.factories;

import java.net.URL;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.impl.reflection.builder.ReflectionBasedWSDynamicClientBuilder;
import org.jboss.wise.core.client.jaxrs.RSDynamicClient;
import org.jboss.wise.core.client.jaxrs.impl.RSDynamicClientImpl;
import org.jboss.wise.core.exception.MCKernelUnavailableException;

/**
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
@ThreadSafe
public abstract class WSDynamicClientFactory {
    private static final String WISE_LOG_CONFIG = "wise-log4j.xml";

    public static void initLog4j( String configFile ) throws MCKernelUnavailableException {
        synchronized (WSDynamicClientFactory.class) {
            // If the logger has not been configured
            if (Logger.getRootLogger().getAllAppenders() instanceof NullEnumeration) {
                // Use default log configuraiton to startup jbossmc
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                URL url = cl.getResource("META-INF/" + WISE_LOG_CONFIG);
                if (url != null) {
                    DOMConfigurator.configure(url);
                }
            }

            if (configFile != null) {
                if (configFile.endsWith(".xml")) {
                    DOMConfigurator.configure(configFile);
                } else {
                    PropertyConfigurator.configure(configFile);
                }
            }

        }

    }

    public static WSDynamicClientBuilder getJAXWSClientBuilder() {
        // TODO: spi
        return new ReflectionBasedWSDynamicClientBuilder();
    }

    /**
     * Return an instance of RSDynamicClient taken from cache if possible, generate and initialise if not.
     * 
     * @param endpointURL
     * @param produceMediaTypes
     * @param consumeMediaTypes
     * @param httpMethod
     * @param userName
     * @param password
     * @return an instance of {@link RSDynamicClient} already initialized, ready to be called
     */
    public static RSDynamicClient getJAXRSClient( String endpointURL,
                                                  RSDynamicClient.HttpMethod httpMethod,
                                                  String produceMediaTypes,
                                                  String consumeMediaTypes,
                                                  String userName,
                                                  String password ) {
        RSDynamicClient client = new RSDynamicClientImpl(endpointURL, produceMediaTypes, consumeMediaTypes, httpMethod);
        return client;
    }

    /**
     * Return an instance of RSDynamicClient taken from cache if possible, generate and initialise if not.
     * 
     * @param endpointURL
     * @param produceMediaTypes
     * @param consumeMediaTypes
     * @param httpMethod
     * @return an instance of {@link RSDynamicClient} already initialized, ready to be called
     */
    public static RSDynamicClient getJAXRSClient( String endpointURL,
                                                  RSDynamicClient.HttpMethod httpMethod,
                                                  String produceMediaTypes,
                                                  String consumeMediaTypes ) {
        return getJAXRSClient(endpointURL, httpMethod, produceMediaTypes, consumeMediaTypes, null, null);

    }

}
