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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import javax.xml.ws.WebEndpoint;
import net.jcip.annotations.Immutable;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSService;
import org.jboss.wise.core.client.impl.reflection.WSEndpointImpl;
import org.jboss.wise.core.client.impl.reflection.WSServiceImpl;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
@Immutable
public class WSServiceImplTest {

    @WebEndpoint( name = "EndPoint1" )
    public String getEndPointForTest1() {
        return " ";
    }

    @WebEndpoint( name = "EndPoint2" )
    public Integer getEndPointForTes2() {
        return Integer.valueOf(3);
    }

    @Test
    public void shouldProcessEndPoint() throws Exception {
        URLClassLoader loader = new URLClassLoader(new URL[] {}, Thread.currentThread().getContextClassLoader());
        WSService service = new WSServiceImpl(WSServiceImplTest.class, loader, this, null, null);
        Map<String, WSEndpoint> endpoints = service.processEndpoints();
        assertThat(endpoints.keySet(), hasItem("EndPoint1"));
        assertThat(endpoints.keySet(), hasItem("EndPoint2"));

        WSEndpointImpl endPoint1 = (WSEndpointImpl)endpoints.get("EndPoint1");
        assertThat((URLClassLoader)endPoint1.getClassLoader(), is(loader));
        assertThat(endPoint1.getUnderlyingObjectClass().getCanonicalName(), equalTo(String.class.getCanonicalName()));
        assertThat((String)endPoint1.getUnderlyingObjectInstance(), equalTo(" "));
        WSEndpointImpl endPoint2 = (WSEndpointImpl)endpoints.get("EndPoint2");
        assertThat((URLClassLoader)endPoint2.getClassLoader(), is(loader));
        assertThat(endPoint2.getUnderlyingObjectClass().getCanonicalName(), equalTo(Integer.class.getCanonicalName()));
        assertThat((Integer)endPoint2.getUnderlyingObjectInstance(), equalTo(Integer.valueOf(3)));

    }
}
