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
package it.javalinux.wise.core.client.impl.reflection;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import it.javalinux.wise.core.client.WSMethod;
import java.util.Map;
import javax.jws.WebMethod;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class WSEndpointImplTest {

    @WebMethod
    public void method1() {

    }

    @WebMethod( )
    public void method2() {

    }

    @Test
    public void getWSMethodsShouldReturnAnnotatedMethods() {
        WSEndpointImpl endpoint = new WSEndpointImpl();
        endpoint.setUnderlyingObjectClass(this.getClass());
        Map<String, WSMethod> wsMethods = endpoint.getWSMethods();
        assertThat(wsMethods.size(), is(2));
        assertThat(wsMethods.keySet(), hasItem("method1"));
        assertThat(wsMethods.keySet(), hasItem("method2"));
    }
    
    
}
