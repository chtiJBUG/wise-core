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
package org.jboss.wise.core.client.impl.reflection;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.WSService;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.impl.reflection.WSDynamicClientImpl;
import org.jboss.wise.core.client.impl.reflection.builder.ReflectionBasedWSDynamicClientBuilder;
import org.jboss.wise.core.consumer.WSConsumer;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the Wise core, i.e. the JAX-WS client that handles wsdl retrieval & parsing, invocations, etc.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * @since
 */
@ThreadSafe
public class WSDynamicClientImplTest {

    WSDynamicClientBuilder builder;

    @Before
    public void before() {
        builder = new ReflectionBasedWSDynamicClientBuilder();
        builder.wsdlURL("foo").tmpDir("/tmp").targetPackage("org.jboss.wise.test.mocks");
    }

    @Test
    public void shouldInitClassLoader() throws Exception {
        WSConsumer consumerMock = mock(WSConsumer.class);

        stub(consumerMock.importObjectFromWsdl(anyString(), (File)anyObject(), (File)anyObject(), anyString(), (List)anyObject(), (File)anyObject())).toReturn(new LinkedList<String>());
        WSDynamicClientImpl client = new WSDynamicClientImpl(builder, consumerMock);
        File expectedOutPutDir = new File("/tmp/" + DigestUtils.md5Hex("foo"));
        assertThat(client.getClassLoader().getURLs().length, is(1));
        assertThat(client.getClassLoader().getURLs()[0], equalTo(expectedOutPutDir.toURL()));
    }

    @Test
    public void shouldProcessServices() throws Exception {
        WSConsumer consumerMock = mock(WSConsumer.class);
        String[] classes = {"org.jboss.wise.test.mocks.Service1", "org.jboss.wise.test.mocks.Service2"};

        stub(consumerMock.importObjectFromWsdl(anyString(), (File)anyObject(), (File)anyObject(), anyString(), (List)anyObject(), (File)anyObject())).toReturn(Arrays.asList(classes));
        WSDynamicClientImpl client = new WSDynamicClientImpl(builder, consumerMock);

        Map<String, WSService> services = client.processServices();
        assertThat(services.size(), is(2));
        assertThat(services.keySet(), hasItem("ServiceName1"));
        assertThat(services.keySet(), hasItem("ServiceName2"));
    }

    @Test
    public void testGetMethod() throws Exception {
        WSConsumer consumerMock = mock(WSConsumer.class);
        String[] classes = {"org.jboss.wise.test.mocks.Service1", "org.jboss.wise.test.mocks.Service2"};

        stub(consumerMock.importObjectFromWsdl(anyString(), (File)anyObject(), (File)anyObject(), anyString(), (List)anyObject(), (File)anyObject())).toReturn(Arrays.asList(classes));
        WSDynamicClientImpl client = new WSDynamicClientImpl(builder, consumerMock);

        WSMethod wsMethod = client.getWSMethod("ServiceName1", "Port1", "testMethod");
        assertNotNull("Should get WsMethod through getWSMethod api", wsMethod);

    }

}
