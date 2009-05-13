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
package org.jboss.wise.core.client.impl.wsdlResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import sun.misc.BASE64Encoder;

import org.apache.commons.lang.StringUtils;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.ws.core.utils.JarUrlConnection;

/**
 * 
 * @author alessio.soldano@jboss.com
 * @since 13-May-2009
 * 
 */
public class Connection {

    private String usernamePassword = null;

    public Connection(String username, String password) {
	//BASIC Auth support; further auth not supported yet
	if (StringUtils.trimToNull(username) != null && StringUtils.trimToNull(password) != null) {
	    this.usernamePassword = new StringBuffer(username).append(":").append(password).toString();
	}
    }

    /**
     * Open an inputStream from the given URL.
     * 
     */
    public InputStream open(URL url) throws IOException {
	String protocol = url.getProtocol();
	if ("http".equalsIgnoreCase(protocol)) {
        	return getInputStream(openAndInitConnection(url));
	} else if ("https".equalsIgnoreCase(protocol)) {
	    throw new WiseRuntimeException("https not supported yet");
	} else if ("jar".equalsIgnoreCase(protocol)) {
	    return new JarUrlConnection(url).getInputStream();
	} else {
	    return url.openStream();
	}
    }

    HttpURLConnection openAndInitConnection(URL url) throws WiseRuntimeException {
	HttpURLConnection conn;
	try {
	    conn = (HttpURLConnection) url.openConnection();
	} catch (IOException e) {
	    throw new WiseRuntimeException(e);
	}
	return this.initConnection(conn);
    }
    
    HttpURLConnection initConnection(HttpURLConnection conn) throws WiseRuntimeException {
	try {

	    conn.setDoOutput(false);
	    conn.setDoInput(true);
	    conn.setUseCaches(false);
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
	    // set Connection close, otherwise we get a keep-alive
	    // connection that gives us fragmented answers.
	    conn.setRequestProperty("Connection", "close");
	    // BASIC AUTH
	    if (usernamePassword != null && usernamePassword.length() != 0) {
		conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode(usernamePassword.getBytes()));
	    }
	    return conn;
	} catch (Exception e) {
	    throw new WiseRuntimeException(e);
	}
    }
    
    InputStream getInputStream(HttpURLConnection conn) throws WiseRuntimeException {
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
}
