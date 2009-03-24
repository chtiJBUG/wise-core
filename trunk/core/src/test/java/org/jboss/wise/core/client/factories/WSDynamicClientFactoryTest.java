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

package org.jboss.wise.core.client.factories;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.hamcrest.core.IsNull;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.jbossmc.beans.ReflectionWSDynamicClientFactory;
import org.junit.Test;

public class WSDynamicClientFactoryTest {

    @Test
    public void testLogger() throws Exception {
        WSDynamicClientFactory factory = WSDynamicClientFactory.getInstance();
        assertThat(factory == null, is(false));
        assertThat(Logger.getRootLogger().getAllAppenders() instanceof NullEnumeration, is(false));
    }

    @Test
    public void twoInvocationOfNewInstanceShouldReturnDifferentObject() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", false);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        assertThat(factory, not(is(WSDynamicClientFactory.newInstance(config))));
    }

    @Test( expected = WiseRuntimeException.class )
    public void newInstanceShouldThrowWiseRuntimeExceptionForNonValidConfig() throws Exception {
        WiseConfig config = mock(WiseConfig.class);
        when(config.isCacheEnabled()).thenReturn(false);
        when(config.getTmpDir()).thenReturn("./target/temp");
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
    }

    @Test
    public void newInstanceShouldReturnRightClassAccordingToConfig() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", false);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        assertThat(factory, instanceOf(ReflectionWSDynamicClientFactory.class));
        assertThat(((ReflectionWSDynamicClientFactory)factory).getWiseConfig(), is(config));
    }

    @Test
    public void prepareBuilderShouldSetAllFieldsToBuilder() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", false);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        List<File> bindings = mock(List.class);
        File catalog = mock(File.class);
        WSDynamicClientBuilder builder = factory.prepareBuilder("url", "userName", "password", "targetPackage", bindings, catalog);
        assertThat(builder.getWsdlURL(), equalTo("url"));
        assertThat(builder.getUserName(), equalTo("userName"));
        assertThat(builder.getPassword(), equalTo("password"));
        assertThat(builder.getTargetPackage(), equalTo("targetPackage"));
        assertThat(builder.getBindingFiles(), equalTo(bindings));
        assertThat(builder.getCatelogFile(), equalTo(catalog));
        assertThat(builder.getTmpDir(), equalTo("./target/temp"));

    }

    @Test
    public void prepareBuilderShouldSetUserAndPasswordNullWhenEmptyWithNewInstance() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", false);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        List<File> bindings = mock(List.class);
        File catalog = mock(File.class);
        WSDynamicClientBuilder builder = factory.prepareBuilder("", "", "", "targetPackage", bindings, catalog);
        assertThat(builder.getUserName(), new IsNull<String>());
        assertThat(builder.getPassword(), new IsNull<String>());
    }

    @Test
    public void getJAXWSClientShouldUseCacheIfWiseConfigRequireThat() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", true);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("./hello_world.wsdl");
        WSDynamicClient client = factory.getJAXWSClient(wsdURL.toExternalForm());
        assertThat(client, is(factory.getJAXWSClient(wsdURL.toExternalForm())));
    }

    @Test
    public void getJAXWSClientShouldNotUseCacheIfWiseConfigRequireThat() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "./target/temp", false);
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("./hello_world.wsdl");
        WSDynamicClient client = factory.getJAXWSClient(wsdURL.toExternalForm());
        assertThat(client, not(is(factory.getJAXWSClient(wsdURL.toExternalForm()))));
    }
}
