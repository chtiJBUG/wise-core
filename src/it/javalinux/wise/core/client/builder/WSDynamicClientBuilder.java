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
package it.javalinux.wise.core.client.builder;

import java.io.File;
import java.util.List;
import it.javalinux.wise.core.client.WSDynamicClient;
import it.javalinux.wise.core.exception.WiseConnectionException;
import it.javalinux.wise.core.exception.WiseRuntimeException;

/**
 * @author stefano.maestri@javalinux.it
 */
public interface WSDynamicClientBuilder {

    public WSDynamicClient build() throws IllegalStateException, WiseConnectionException, WiseRuntimeException;

    public String getWsdlURL();

    public WSDynamicClientBuilder wsdlURL( String wsdlURL );

    public String getUserName();

    public WSDynamicClientBuilder userName( String userName );

    public String getPassword();

    public WSDynamicClientBuilder password( String password );

    public WSDynamicClientBuilder tmpDir( String tmpDir );

    public WSDynamicClientBuilder targetPackage( String targetPackage );
    
    public WSDynamicClientBuilder bindingFiles(List<File> bindings);
    
    public WSDynamicClientBuilder catelogFile(File catelog);

    public String getTmpDir();

    public String getTargetPackage();
    
    public List<File> getBindingFiles();
    
    public File getCatelogFile();

}
