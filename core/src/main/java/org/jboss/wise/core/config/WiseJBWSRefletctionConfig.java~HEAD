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
package org.jboss.wise.core.config;

/**
 * Concrete implementation of {@link WiseConfig} which set up a Reflection based implementation of Wise based on JBossWS native
 * JAX-WS stack
 * 
 * @author stefano.maestri@javalinux.it
 */
public class WiseJBWSRefletctionConfig implements WiseConfig {

    private final String securityConfigFileURL;
    private final String securityConfigName;
    private final boolean keepSource;
    private final boolean verbose;
    private final String tmpDir;
    private final boolean cacheEnabled;

    /**
     * @param securityConfigFileURL
     * @param securityConfigName
     * @param keepSource
     * @param verbose
     * @param tmpDir
     * @param cacheEnabled
     */
    public WiseJBWSRefletctionConfig( String securityConfigFileURL,
                                      String securityConfigName,
                                      boolean keepSource,
                                      boolean verbose,
                                      String tmpDir,
                                      boolean cacheEnabled ) {
        super();
        this.securityConfigFileURL = securityConfigFileURL;
        this.securityConfigName = securityConfigName;
        this.keepSource = keepSource;
        this.verbose = verbose;
        this.tmpDir = tmpDir;
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * @return securityConfigFileURL
     */
    public String getSecurityConfigFileURL() {
        return securityConfigFileURL;
    }

    /**
     * @return securityConfigName
     */
    public String getSecurityConfigName() {
        return securityConfigName;
    }

    /**
     * @return keepSource
     */
    public boolean isKeepSource() {
        return keepSource;
    }

    /**
     * @return verbose
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * @return tmpDir
     */
    public String getTmpDir() {
        return tmpDir;
    }

    /**
     * @return cacheEnabled
     */
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

}
