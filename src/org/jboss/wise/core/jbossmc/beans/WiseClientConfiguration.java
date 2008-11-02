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

package org.jboss.wise.core.jbossmc.beans;

/**
 * @author stefano.maestri@javalinux.it
 */

public class WiseClientConfiguration {

    private String defaultTmpDeployDir = "tmp";

    private String defaultUserName;

    private String defaultPassword;

    private String logConfig;

    public final String getDefaultUserName() {
        return defaultUserName;
    }

    public final void setDefaultUserName( String defaultUserName ) {
        this.defaultUserName = defaultUserName;
    }

    public final String getDefaultPassword() {
        return defaultPassword;
    }

    public final void setDefaultPassword( String defaultPassword ) {
        this.defaultPassword = defaultPassword;
    }

    public final String getDefaultTmpDeployDir() {
        return defaultTmpDeployDir;
    }

    public WiseClientConfiguration() {

    }

    public final void setDefaultTmpDeployDir( String defaultTmpDeployDir ) {
        this.defaultTmpDeployDir = defaultTmpDeployDir;
    }

    /**
     * Set the log4j configuraton file url
     * 
     * @param url log4j configuraiton url
     */
    public final void setLogConfig( String url ) {
        this.logConfig = url;
    }

    /**
     * @return log4j configuration url
     */
    public String getLogConfig() {
        return logConfig;
    }

}
