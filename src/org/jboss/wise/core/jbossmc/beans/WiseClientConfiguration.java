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

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
public class WiseClientConfiguration {
    @GuardedBy( "this" )
    private String defaultTmpDeployDir = "tmp";
    @GuardedBy( "this" )
    private String defaultUserName = null;
    @GuardedBy( "this" )
    private String defaultPassword = null;
    @GuardedBy( "this" )
    private String logConfig;

    public WiseClientConfiguration() {
        super();
    }

    public WiseClientConfiguration( String tmpDir ) {
        super();
        this.defaultTmpDeployDir = tmpDir;
    }

    /**
     * @return defaultTmpDeployDir
     */
    public synchronized final String getDefaultTmpDeployDir() {
        return defaultTmpDeployDir;
    }

    /**
     * @param defaultTmpDeployDir Sets defaultTmpDeployDir to the specified value.
     */
    public synchronized final void setDefaultTmpDeployDir( String defaultTmpDeployDir ) {
        this.defaultTmpDeployDir = defaultTmpDeployDir;
    }

    /**
     * @return defaultUserName
     */
    public synchronized final String getDefaultUserName() {
        return defaultUserName;
    }

    /**
     * @param defaultUserName Sets defaultUserName to the specified value.
     */
    public synchronized final void setDefaultUserName( String defaultUserName ) {
        this.defaultUserName = defaultUserName;
    }

    /**
     * @return defaultPassword
     */
    public synchronized final String getDefaultPassword() {
        return defaultPassword;
    }

    /**
     * @param defaultPassword Sets defaultPassword to the specified value.
     */
    public synchronized final void setDefaultPassword( String defaultPassword ) {
        this.defaultPassword = defaultPassword;
    }

    /**
     * @return logConfig
     */
    public synchronized final String getLogConfig() {
        return logConfig;
    }

    /**
     * @param logConfig Sets logConfig to the specified value.
     */
    public synchronized final void setLogConfig( String logConfig ) {
        this.logConfig = logConfig;
    }

}
