/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.wise.core.client.impl.reflection.builder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.impl.reflection.WSDynamicClientImpl;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.WiseConnectionException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.utils.IDGenerator;
import org.jboss.wise.core.utils.IOUtils;
import sun.misc.BASE64Encoder;

/**
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionBasedWSDynamicClientBuilder implements WSDynamicClientBuilder {
    private static Logger logger = Logger.getLogger(ReflectionBasedWSDynamicClientBuilder.class);
    private String wsdlURL;
    private String userName;
    private String password;
    private String tmpDir;
    private String targetPackage;
    private List<File> bindingFiles = null;
    private File catelog = null;

    /**
     * {@inheritDoc}
     * 
     * @throws WiseRuntimeException
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#build()
     */
    public WSDynamicClient build() throws IllegalStateException, WiseConnectionException, WiseRuntimeException {
        if (wsdlURL != null && wsdlURL.startsWith("http://")) {
            wsdlURL = this.getUsableWSDL();
        }
        logger.debug("Get usable WSDL :" + wsdlURL);

        if (wsdlURL == null || wsdlURL.trim().length() == 0) {
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
    public synchronized final WSDynamicClientBuilder userName( String userName ) {
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
    public final WSDynamicClientBuilder password( String password ) {
        this.password = password;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getTmpDir()
     */
    public final String getTmpDir() {
        return tmpDir;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#getTargetPackage()
     */
    public final String getTargetPackage() {
        return targetPackage;
    }

    public final List<File> getBindingFiles() {
        return this.bindingFiles;
    }

    public final File getCatelogFile() {
        return this.catelog;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #wsdlURL(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param wsdlURL
     */
    public final void setWsdlURL( String wsdlURL ) {
        this.wsdlURL = wsdlURL;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #userName(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param userName
     */
    public final void setUserName( String userName ) {
        this.userName = userName;
    }

    /**
     * setter method used only by MC. Even if they are usable also by code is much more convenient to use intrface's defined
     * method {@link #password(String)} since it return current instance and permit concatenation like
     * instace.wsdlURL(wsdl).symbolycName(name);
     * 
     * @param password
     */
    public final void setPassword( String password ) {
        this.password = password;
    }

    public final void setBindingFiles( List<File> bindings ) {
        this.bindingFiles = bindings;
    }

    public final void setCatelogFile( File catelog ) {
        this.catelog = catelog;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#targetPackage(java.lang.String)
     */
    public WSDynamicClientBuilder targetPackage( String targetPackage ) {
        this.targetPackage = targetPackage;
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.builder.WSDynamicClientBuilder#tmpDir(java.lang.String)
     */
    public WSDynamicClientBuilder tmpDir( String tmpDir ) {
        this.tmpDir = tmpDir;
        File tmp = new File(tmpDir);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        return this;
    }

    public WSDynamicClientBuilder bindingFiles( List<File> bindings ) {
        this.bindingFiles = bindings;
        return this;
    }

    public WSDynamicClientBuilder catelogFile( File catelogFile ) {
        catelog = catelogFile;
        return this;
    }

    /**
     * Gets a WSDL given its url and userName/password if needed.
     * 
     * @return The path to the temp file containing the requested wsdl
     * @throws WiseConnectionException If an error occurs while downloading the wsdl
     */
    // TODO: move to super class
    private String getUsableWSDL() throws WiseConnectionException {
        if (StringUtils.trimToNull(userName) == null || StringUtils.trimToNull(password) == null) {
            return this.wsdlURL;
        } else {
            return this.transferWSDL(new StringBuffer(userName).append(":").append(password).toString());
        }
    }

    /*
     * Downloads the wsdl.
     * 
     * @throws WiseConnectionException If the wsdl cannot be retrieved
     */
    private String transferWSDL( String usernameAndPassword ) throws WiseConnectionException {
        String filePath = null;
        try {
            URL endpoint = new URL(wsdlURL);
            // Create the connection
            HttpURLConnection conn = (HttpURLConnection)endpoint.openConnection();
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
            if (this.password != null) {
                conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode(usernameAndPassword.getBytes()));
            }
            // Read response
            InputStream is = null;
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                StringWriter sw = new StringWriter();
                char[] buf = new char[200];
                int read = 0;
                while (read != -1) {
                    read = isr.read(buf);
                    sw.write(buf);
                }
                throw new WiseConnectionException("Remote server's response is an error: " + sw.toString());
            }
            // saving file
            File file = new File(tmpDir, new StringBuffer("Wise").append(IDGenerator.nextVal()).append(".xml").toString());
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copyStream(fos, is);
            fos.close();
            is.close();
            filePath = file.getPath();
        } catch (WiseConnectionException wce) {
            throw wce;
        } catch (Exception e) {
            logger.error("Failed to download wsdl from URL : " + wsdlURL);
            throw new WiseConnectionException("Wsdl download failed!", e);
        }
        return filePath;
    }

}