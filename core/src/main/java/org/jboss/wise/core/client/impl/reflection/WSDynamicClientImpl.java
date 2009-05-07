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
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.wise.core.client.SpiLoader;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.WSService;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.consumer.WSConsumer;
import org.jboss.wise.core.consumer.impl.jbosswsnative.WSImportImpl;
import org.jboss.wise.core.exception.ResourceNotAvailableException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.utils.JavaUtils;
import org.jboss.wise.core.wsextensions.EnablerDelegate;
import org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate;
import org.milyn.Smooks;

/**
 * This is the Wise core, i.e. the JAX-WS client that handles wsdl retrieval &
 * parsing, invocations, etc.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * @since
 */
@ThreadSafe
public class WSDynamicClientImpl implements WSDynamicClient {

    private static final long serialVersionUID = -7185945063107035243L;

    private final Logger log = Logger.getLogger(WSDynamicClientImpl.class);

    @GuardedBy("this")
    private URLClassLoader classLoader;

    private final String userName;

    private final String password;

    private final EnablerDelegate wsExtensionEnablerDelegate;

    private final CopyOnWriteArrayList<String> classNames = new CopyOnWriteArrayList<String>();

    private final Map<String, WSService> servicesMap = Collections.synchronizedMap(new HashMap<String, WSService>());

    private final Smooks smooksInstance;

    private final String tmpDir;

    /**
     * @param builder
     * @return consumer
     */
    private static WSConsumer createConsumer(WSDynamicClientBuilder builder) {
	WSConsumer consumer = (WSConsumer) SpiLoader
		.loadService("org.jboss.wise.consumer.WSConsumer", "org.jboss.wise.core.consumer.impl.jbosswsnative.WSImportImpl");
	return consumer;
    }

    public WSDynamicClientImpl(WSDynamicClientBuilder builder) throws WiseRuntimeException {
	this(builder, createConsumer(builder), new Smooks());
    }

    protected WSDynamicClientImpl(WSDynamicClientBuilder builder, WSConsumer consumer) throws WiseRuntimeException {
	this(builder, consumer, new Smooks());
    }

    protected WSDynamicClientImpl(WSDynamicClientBuilder builder, WSConsumer consumer, Smooks smooks) throws WiseRuntimeException {
	this.smooksInstance = smooks;
	userName = builder.getUserName();
	password = builder.getPassword();
	wsExtensionEnablerDelegate = new ReflectionEnablerDelegate(builder.getSecurityConfigFileURL(), builder
		.getSecurityConfigName());
	this.tmpDir = builder.getClientSpecificTmpDir();
	File outputDir = new File(tmpDir + "/classes/");
	File sourceDir = new File(tmpDir + "/src/");

	try {
	    classNames.addAll(consumer.importObjectFromWsdl(builder.getNormalizedWsdlUrl(), outputDir, sourceDir, builder
		    .getTargetPackage(), builder.getBindingFiles(), builder.getMessageStream(), builder.getCatalogFile()));
	} catch (MalformedURLException e) {
	    throw new WiseRuntimeException("Problem consuming wsdl:" + builder.getWsdlURL(), e);
	}
	this.initClassLoader(outputDir);

	this.processServices();
    }

    /**
     * @param outputDir
     * @throws WiseRuntimeException
     */

    private synchronized void initClassLoader(File outputDir) throws WiseRuntimeException {
	try {

	    // we need a custom classloader pointing the temp dir
	    // in order to load the generated classes on the fly
	    this.setClassLoader(new URLClassLoader(new URL[] { outputDir.toURL(), }, Thread.currentThread()
		    .getContextClassLoader()));
	    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();

	    try {
		Thread.currentThread().setContextClassLoader(this.getClassLoader());
		JavaUtils.loadJavaType("com.sun.xml.ws.spi.ProviderImpl", this.getClassLoader());
	    } finally {
		// restore the original classloader
		Thread.currentThread().setContextClassLoader(oldLoader);
	    }
	} catch (Exception e) {
	    throw new WiseRuntimeException(
		    "Error occurred while setting up classloader for generated class in directory: " + outputDir, e);
	}
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSDynamicClient#processServices()
     */
    public synchronized Map<String, WSService> processServices() throws IllegalStateException {
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
			servicesMap.put(((WebServiceClient) annotation).name(), service);
		    }
		} catch (Exception e) {
		    throw new IllegalStateException(
			    "Error during loading/instanciating class:" + className + " with exception message: " + e
				    .getMessage());
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
     * @see org.jboss.wise.core.client.WSDynamicClient#getWSMethod()
     */
    public WSMethod getWSMethod(String serviceName, String portName, String operationName) throws ResourceNotAvailableException {
	WSService wsService = servicesMap.get(serviceName);
	if (wsService == null) {
	    throw new ResourceNotAvailableException("Cannot find requested service: " + serviceName);
	}
	WSEndpoint wsEndpoint = wsService.processEndpoints().get(portName);
	if (wsEndpoint == null) {
	    throw new ResourceNotAvailableException("Cannot find requested endpoint (port): " + portName);
	}
	WSMethod wsMethod = wsEndpoint.getWSMethods().get(operationName);
	if (wsMethod == null) {
	    throw new ResourceNotAvailableException("Cannot find requested method (operation): " + operationName);
	}
	return wsMethod;
    }

    public synchronized final URLClassLoader getClassLoader() {
	return classLoader;
    }

    public synchronized final void setClassLoader(URLClassLoader classLoader) {
	this.classLoader = classLoader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSDynamicClient#getWSExtensionEnablerDelegate()
     */
    public EnablerDelegate getWSExtensionEnablerDelegate() {
	return wsExtensionEnablerDelegate;
    }

    /**
     * @return smooksInstance
     */
    public Smooks getSmooksInstance() {
	return smooksInstance;
    }

    public synchronized void close() {
	smooksInstance.close();
	try {
	    FileUtils.forceDelete(new File(tmpDir));
	} catch (IOException e) {
	    Logger.getLogger(WSDynamicClientImpl.class).info("unable to remove tmpDir:" + tmpDir);
	}
    }

    /**
     * @return tmpDir
     */
    public synchronized String getTmpDir() {
	return tmpDir;
    }

}
