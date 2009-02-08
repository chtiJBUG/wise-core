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
 * @author stefano.maestri@javalinux.it
 */
public class WiseJBWSRefletctionConfig implements WiseConfig {

    private final String configFileURL;
    private final String configName;
    private final boolean keepSource;
    private final boolean verbose;
    private final String tmpDir;

    /**
     * @param configFileURL
     * @param configName
     * @param keepSource
     * @param verbose
     * @param tmpDir
     */
    public WiseJBWSRefletctionConfig( String configFileURL,
                                      String configName,
                                      boolean keepSource,
                                      boolean verbose,
                                      String tmpDir ) {
        super();
        this.configFileURL = configFileURL;
        this.configName = configName;
        this.keepSource = keepSource;
        this.verbose = verbose;
        this.tmpDir = tmpDir;
    }

    /**
     * @return configFileURL
     */
    public final String getConfigFileURL() {
        return configFileURL;
    }

    /**
     * @return configName
     */
    public final String getConfigName() {
        return configName;
    }

    /**
     * @return keepSource
     */
    public final boolean isKeepSource() {
        return keepSource;
    }

    /**
     * @return verbose
     */
    public final boolean isVerbose() {
        return verbose;
    }

    /**
     * @return tmpDir
     */
    public final String getTmpDir() {
        return tmpDir;
    }

}
