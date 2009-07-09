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

package org.jboss.wise.core.client.builder;

import java.io.File;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.List;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.exception.WiseRuntimeException;

/**
 * {@link WSDynamicClientBuilder} is an interface to define builder for various
 * kind of implementation of WiseDynamicClient Indirect build permit to easy
 * inject different implementation of {@link WSDynamicClient}.
 * 
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
public interface WSDynamicClientBuilder {

    /**
     * 
     * @return {@link WSDynamicClient}
     * @throws IllegalStateException
     * @throws ConnectException
     * @throws WiseRuntimeException
     */
    public WSDynamicClient build() throws IllegalStateException, ConnectException, WiseRuntimeException;

    /**
     * 
     * @param wsdlURL
     * @return {@link WSDynamicClient}
     */
    public WSDynamicClientBuilder wsdlURL(String wsdlURL);

    /**
     * 
     * @param userName
     * @return {@link WSDynamicClient}
     */
    public WSDynamicClientBuilder userName(String userName);

    public WSDynamicClientBuilder password(String password);

    public WSDynamicClientBuilder tmpDir(String tmpDir);

    public WSDynamicClientBuilder targetPackage(String targetPackage);

    public WSDynamicClientBuilder bindingFiles(List<File> bindings);

    public WSDynamicClientBuilder catalogFile(File catelog);

    public WSDynamicClientBuilder securityConfigUrl(String url);

    public WSDynamicClientBuilder securityConfigName(String name);

    public WSDynamicClientBuilder keepSource(boolean bool);

    public WSDynamicClientBuilder verbose(boolean bool);

    public WSDynamicClientBuilder messageStream(PrintStream messageStream);

    public WSDynamicClientBuilder maxThreadPoolSize(int maxThreadPoolSize);

    public String getWsdlURL();

    public String getUserName();

    public String getPassword();

    public String getTmpDir();

    public String getTargetPackage();

    public List<File> getBindingFiles();

    public File getCatalogFile();

    public String getSecurityConfigFileURL();

    public String getSecurityConfigName();

    public boolean isKeepSource();

    public boolean isVerbose();

    public PrintStream getMessageStream();

    public String getNormalizedWsdlUrl();

    public String getClientSpecificTmpDir();

    public int getMaxThreadPoolSize();

}
