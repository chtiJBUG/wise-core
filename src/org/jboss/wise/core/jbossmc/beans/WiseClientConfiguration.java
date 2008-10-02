/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package org.jboss.wise.core.jbossmc.beans;


/**
 * @author stefano.maestri@javalinux.it
 */

public class WiseClientConfiguration {

    private String defaultTmpDeployDir = "/tmp";

    private String defaultTargetPackage = "it.javalinux.wise";

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

    public final String getDefaultTargetPackage() {
        return defaultTargetPackage;
    }

    public WiseClientConfiguration() {

    }

    public final void setDefaultTmpDeployDir( String defaultTmpDeployDir ) {
        this.defaultTmpDeployDir = defaultTmpDeployDir;
    }

    public final void setDefaultTargetPackage( String defaultTargetPackage ) {
        this.defaultTargetPackage = defaultTargetPackage;
    }
    
    /**
     * Set the log4j configuraton file url
     * @param url log4j configuraiton url 
     */
    public final void setLogConfig(String url) {
        this.logConfig = url;
    }
    
    /**
     * @return log4j configuration url
     */
    public String getLogConfig() {
        return logConfig;
    }

}
