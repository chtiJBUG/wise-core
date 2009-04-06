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

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.xml.ws.WebServiceClient;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.WSService;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.consumer.WSConsumer;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.utils.JavaUtils;

/**
 * This is the Wise core, i.e. the JAX-WS client that handles wsdl retrieval & parsing, invocations, etc.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * @since
 */
@ThreadSafe
public class WSDynamicClientImpl implements WSDynamicClient {

    private static final long serialVersionUID = -7185945063107035243L;
    private final Logger log = Logger.getLogger(WSDynamicClientImpl.class);

    @GuardedBy( "this" )
    private URLClassLoader classLoader;
    private final String userName;
    private final String password;

    private final CopyOnWriteArrayList<String> classNames = new CopyOnWriteArrayList<String>();
    private final Map<String, WSService> servicesMap = Collections.synchronizedMap(new HashMap<String, WSService>());

    private final WiseConfig config;

    public WSDynamicClientImpl( WSDynamicClientBuilder builder ) throws WiseRuntimeException, MCKernelUnavailableException {
        this(builder, WSConsumer.getInstance(builder.getConfig()));
    }

    public WSDynamicClientImpl( WSDynamicClientBuilder builder,
                                WSConsumer consumer ) throws WiseRuntimeException {
        config = builder.getConfig();
        userName = builder.getUserName();
        password = builder.getPassword();
        final String symbolicName = this.getSymlicName(builder.getWsdlURL());
        File outputDir = new File(builder.getTmpDir() + "/" + symbolicName + "/");
        File sourceDir = new File(builder.getTmpDir() + "/src/" + symbolicName + "/");

        try {
            classNames.addAll(consumer.importObjectFromWsdl(builder.getWsdlURL(),
                                                            outputDir,
                                                            sourceDir,
                                                            builder.getTargetPackage(),
                                                            builder.getBindingFiles(),
                                                            builder.getCatelogFile()));
        } catch (MalformedURLException e) {
            throw new WiseRuntimeException("Problem consumig wsdl:" + builder.getWsdlURL(), e);
        }
        this.initClassLoader(outputDir);
        this.processServices();
    }

    /**
     * @param outputDir
     * @throws WiseRuntimeException
     */

    private void initClassLoader( File outputDir ) throws WiseRuntimeException {
        try {

            // we need a custom classloader pointing the temp dir
            // in order to load the generated classes on the fly
            this.setClassLoader(new URLClassLoader(new URL[] {outputDir.toURL(),}, Thread.currentThread().getContextClassLoader()));
            ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();

            try {
                Thread.currentThread().setContextClassLoader(this.getClassLoader());
                JavaUtils.loadJavaType("com.sun.xml.ws.spi.ProviderImpl", this.getClassLoader());
            } finally {
                // restore the original classloader
                Thread.currentThread().setContextClassLoader(oldLoader);
            }
        } catch (Exception e) {
            throw new WiseRuntimeException("Error occurred while setting up classloader for generated class in directory: "
                                           + outputDir, e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSDynamicClient#processServices()
     */
    public Map<String, WSService> processServices() throws IllegalStateException {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(this.getClassLoader());
            for (String className : classNames) {
                try {
                    Class clazz = JavaUtils.loadJavaType(className, this.getClassLoader());
                    Annotation annotation = clazz.getAnnotation(WebServiceClient.class);
                    if (annotation != null) {
                        WSService service = new WSServiceImpl(clazz, this.getClassLoader(), clazz.newInstance(), userName,
                                                              password);
                        servicesMap.put(((WebServiceClient)annotation).name(), service);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Error during loading/instanciating class:" + className
                                                    + " with exception message: " + e.getMessage());
                }
            }
        } finally {
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
        return servicesMap;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSDynamicClient#processServices()
     */
    public WSMethod getWSMethod( String serviceName,
                                 String portName,
                                 String operationName ) {
        // if (servicesMap.size() == 0) {
        // processServices();
        // }
        WSService wsService = servicesMap.get(serviceName);
        return wsService.processEndpoints().get(portName).getWSMethods().get(operationName);
    }

    public synchronized final URLClassLoader getClassLoader() {
        return classLoader;
    }

    public synchronized final void setClassLoader( URLClassLoader classLoader ) {
        this.classLoader = classLoader;
    }

    private String getSymlicName( String wsdlURL ) {
        return DigestUtils.md5Hex(wsdlURL);
    }

}
