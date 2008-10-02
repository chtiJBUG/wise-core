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
package it.javalinux.wise.test.mocks;

import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebEndpoint;

/**
 * @author stefano.maestri@javalinux.it
 */
@WebServiceClient( name = "ServiceName1" )
public class Service1 {
    @WebEndpoint(name = "Port1")
    public PortType1 getSoapProviderPort() {
        return null;
    }
}