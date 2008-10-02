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
package org.jboss.wise.core.client.factories;

import java.io.File;
import java.net.URL;
import java.util.List;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSDynamicClientCache;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.WiseConnectionException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.jbossmc.BeansNames;
import org.jboss.wise.core.jbossmc.MicroContainerSpi;
import org.jboss.wise.core.jbossmc.beans.WiseClientConfiguration;
import org.jboss.wise.core.utils.SmooksCache;

/**
 * WSDynamicClientFactory is a singleton containing a WSDynamicClient cache. It's able to create WSDynamicCient objects, and init
 * then using WiseProperties using wise-core.properties find in classpath as default. This default properties may be overridden
 * using setWiseProperties method.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
@ThreadSafe
public abstract class WSDynamicClientFactory {
    private static final String WISE_LOG_CONFIG = "wise-log4j.xml";
    private WiseClientConfiguration config;
    private static boolean initialized;

    private static Logger log;

    /**
     * Provide a method to clean up cached thing when Wise is integrated with JBoss or other container
     */
    public static void initialise() {
        SmooksCache.getInstance().clearAll();
        WSDynamicClientCache.getInstace().clearCache();
    }

    public static WSDynamicClientFactory getInstance() throws MCKernelUnavailableException {
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

        }
        WSDynamicClientFactory factory = MicroContainerSpi.getKernelProvidedImplementation(BeansNames.WSDynamicClientFactory.name(),
                                                                                           WSDynamicClientFactory.class);
        // if the log config file is specified, configure it

        synchronized (WSDynamicClientFactory.class) {
            if (!initialized && factory.config.getLogConfig() != null) {
                String configFile = factory.config.getLogConfig();
                if (configFile.endsWith(".xml")) {
                    DOMConfigurator.configure(configFile);
                } else {
                    PropertyConfigurator.configure(configFile);
                }
                initialized = true;
            }
            if (log == null) {
                log = Logger.getLogger(WSDynamicClientFactory.class);
            }

        }
        return factory;
    }

    public WSDynamicClient getClient( String wsdlURL )
        throws IllegalStateException, WiseConnectionException, WiseRuntimeException {
        return this.getClient(wsdlURL, null, null, null, null, null);
    }

    /**
     * Return an instance of WSDynamicClient taken from cache if possible, generate and initialise if not.
     * 
     * @param wsdlURL The URL to retrive wsdl of webservice called
     * @param userName we support HTTP BASIC Auth protected wsdls: this is username used for authentication
     * @param password we support HTTP BASIC Auth protected wsdls: this is password used for authentication
     * @return an instance of WSDynamicClient already initialized, ready to call endpoints
     * @throws IllegalStateException
     * @throws WiseConnectionException thrown in case wsdl isn't accessible at given URL
     * @throws WiseRuntimeException
     */
    public WSDynamicClient getClient( String wsdlURL,
                                      String userName,
                                      String password )
        throws IllegalStateException, WiseConnectionException, WiseRuntimeException {
        return this.getClient(wsdlURL, userName, password, null, null, null);
    }

    public WSDynamicClient getClient( String wsdlURL,
                                      List<File> bindings,
                                      File catelog ) throws IllegalStateException, WiseConnectionException, WiseRuntimeException {
        return this.getClient(wsdlURL, null, null, null, bindings, catelog);
    }

    public WSDynamicClient getClient( String wsdlURL,
                                      String userName,
                                      String password,
                                      String targetPackage,
                                      List<File> bindings,
                                      File catelog ) throws IllegalStateException, WiseConnectionException, WiseRuntimeException {
        WSDynamicClientBuilder builder = this.createBuilder();
        builder.tmpDir(config.getDefaultTmpDeployDir()).bindingFiles(bindings).catelogFile(catelog).wsdlURL(wsdlURL);
        builder.targetPackage(targetPackage);
        if (userName == null) {
            userName = config.getDefaultUserName();
        }
        if (password == null) {
            password = config.getDefaultPassword();
        }
        builder.userName(userName).password(password);
        WSDynamicClient client = WSDynamicClientCache.getInstace().get(wsdlURL);
        if (client == null) {
            client = builder.build();
            WSDynamicClientCache.getInstace().addToCache(wsdlURL, client);
        }
        log.debug("Create WSDynamicClient successfully");
        return client;
    }

    public final WiseClientConfiguration getConfig() {
        return config;
    }

    public final void setConfig( WiseClientConfiguration config ) {
        this.config = config;
    }

    public abstract WSDynamicClientBuilder createBuilder();
}
