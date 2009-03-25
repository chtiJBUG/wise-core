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

package org.jboss.wise.core.client.impl.reflection.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.impl.reflection.WSDynamicClientImpl;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.utils.IDGenerator;
import org.jboss.wise.core.utils.IOUtils;
import sun.misc.BASE64Encoder;

/**
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
public class ReflectionBasedWSDynamicClientBuilder implements WSDynamicClientBuilder {
    private static Logger logger = Logger.getLogger(ReflectionBasedWSDynamicClientBuilder.class);
    @GuardedBy( "this" )
    private String wsdlURL;
    @GuardedBy( "this" )
    private String userName;
    @GuardedBy( "this" )
    private String password;
    @GuardedBy( "this" )
    private String tmpDir;
    @GuardedBy( "this" )
    private String targetPackage;
    @GuardedBy( "this" )
    private List<File> bindingFiles = null;
    @GuardedBy( "this" )
    private File catelog = null;

    private final WiseConfig config;

    /**
     * @param config
     */
    public ReflectionBasedWSDynamicClientBuilder( WiseConfig config ) {
        super();
        this.config = config;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#build()
     */
    public WSDynamicClient build() throws IllegalStateException, ConnectException, WiseRuntimeException {
        if (this.getWsdlURL() != null && this.getWsdlURL().startsWith("http://")) {
            this.setWsdlURL(this.transferWSDL(getUserNameAndPasswordForBasicAuthentication()));
        }
        logger.debug("Get usable WSDL :" + wsdlURL);

        if (this.getWsdlURL() == null || this.getWsdlURL().trim().length() == 0) {
            throw new IllegalStateException("wsdlURL cannot be null");
        }

        try {
            return new WSDynamicClientImpl(this);
        } catch (MCKernelUnavailableException e) {
            throw new WiseRuntimeException(
                                           "Problem consumig wsdl caused by not founded WSConsumer instance in MC. Check your jboss-beans.xml file");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getWsdlURL()
     */
    public synchronized final String getWsdlURL() {
        return wsdlURL;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#wsdlURL(java.lang.String)
     */
    public synchronized final WSDynamicClientBuilder wsdlURL( String wsdlURL ) {
        this.wsdlURL = wsdlURL;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getUserName()
     */
    public synchronized final String getUserName() {
        return userName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#userName(java.lang.String)
     */
    public synchronized WSDynamicClientBuilder userName( String userName ) {
        this.userName = userName;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getPassword()
     */
    public synchronized final String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#password(java.lang.String)
     */
    public synchronized WSDynamicClientBuilder password( String password ) {
        this.password = password;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getTmpDir()
     */
    public synchronized final String getTmpDir() {
        return tmpDir;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getTargetPackage()
     */
    public synchronized final String getTargetPackage() {
        return targetPackage;
    }

    public synchronized final List<File> getBindingFiles() {
        return this.bindingFiles;
    }

    public synchronized final File getCatelogFile() {
        return this.catelog;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #wsdlURL(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param wsdlURL
     */
    public synchronized final void setWsdlURL( String wsdlURL ) {
        this.wsdlURL = wsdlURL;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #userName(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param userName
     */
    public synchronized final void setUserName( String userName ) {
        this.userName = userName;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #password(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param password
     */
    public synchronized final void setPassword( String password ) {
        this.password = password;
    }

    public synchronized final void setBindingFiles( List<File> bindings ) {
        this.bindingFiles = bindings;
    }

    public synchronized final void setCatelogFile( File catelog ) {
        this.catelog = catelog;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#targetPackage(java.lang.String)
     */
    public synchronized WSDynamicClientBuilder targetPackage( String targetPackage ) {
        this.targetPackage = targetPackage;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#tmpDir(java.lang.String)
     */
    public synchronized WSDynamicClientBuilder tmpDir( String tmpDir ) {
        this.tmpDir = tmpDir;
        return this;
    }

    public synchronized WSDynamicClientBuilder bindingFiles( List<File> bindings ) {
        this.bindingFiles = bindings;
        return this;
    }

    public synchronized WSDynamicClientBuilder catalogFile( File catelogFile ) {
        catelog = catelogFile;
        return this;
    }

    synchronized String getUserNameAndPasswordForBasicAuthentication() {
        if (StringUtils.trimToNull(userName) == null || StringUtils.trimToNull(password) == null) {
            return null;
        } else {
            return new StringBuffer(userName).append(":").append(password).toString();
        }
    }

    /*
     * Downloads the wsdl.
     * 
     * @throws WiseConnectionException If the wsdl cannot be retrieved
     */
    private String transferWSDL( String usernameAndPassword ) throws WiseRuntimeException {
        try {
            return this.transferWSDL(usernameAndPassword, IOUtils.newInstance());
        } catch (IOException e) {
            throw new WiseRuntimeException(e);
        }

    }

    /*
     * Downloads the wsdl.
     * 
     * @throws WiseConnectionException If the wsdl cannot be retrieved
     */
    String transferWSDL( String usernameAndPassword,
                         IOUtils ioUtils ) throws IOException, WiseRuntimeException {
        HttpURLConnection conn = openAndInitConnection(usernameAndPassword, new URL(this.getWsdlURL()));
        File file = new File(this.getTmpDir(), new StringBuffer("Wise").append(IDGenerator.nextVal()).append(".xml").toString());
        ioUtils.copyStreamAndClose(new FileOutputStream(file), getWsdlInputStream(conn));
        return file.getPath();

    }

    /**
     * It opens an input stream for passed {@link HttpConnection}. Note that callers should take care of resource closing.
     * 
     * @param conn
     * @return an InputStream if httconn went well, or throw an exception
     * @throws WiseRuntimeException
     */
    InputStream getWsdlInputStream( HttpURLConnection conn ) throws WiseRuntimeException {
        try {
            InputStream is = null;
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
            } else {

                throw new ConnectException("Remote server's response is an error: " + conn.getResponseCode());
            }
            return is;
        } catch (Exception e) {
            throw new WiseRuntimeException(e);
        }

    }

    /**
     * @param usernameAndPassword
     * @param url
     * @return a connection prepared to download the wsdl
     * @throws WiseRuntimeException
     */
    HttpURLConnection openAndInitConnection( String usernameAndPassword,
                                             URL url ) throws WiseRuntimeException {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new WiseRuntimeException(e);
        }
        return this.initConnection(usernameAndPassword, conn);
    }

    /**
     * @param usernameAndPassword
     * @param conn
     * @return a connection prepared to download the wsdl
     * @throws WiseRuntimeException
     */
    HttpURLConnection initConnection( String usernameAndPassword,
                                      HttpURLConnection conn ) throws WiseRuntimeException {
        try {

            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept",
                                    "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            // set Connection close, otherwise we get a keep-alive
            // connection
            // that gives us fragmented answers.
            conn.setRequestProperty("Connection", "close");
            // BASIC AUTH
            if (usernameAndPassword != null && usernameAndPassword.length() != 0) {
                conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode(usernameAndPassword.getBytes()));
            }
            return conn;
        } catch (Exception e) {
            throw new WiseRuntimeException(e);
        }
    }

    /**
     * @return config
     */
    public synchronized final WiseConfig getConfig() {
        return config;
    }

}