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

package org.jboss.wise.test.integration.smooks;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.jboss.wise.core.mapper.SmooksMapper;
import org.jboss.wise.core.test.WiseTest;
import org.jboss.wise.test.integration.smooks.pojo.clientside.ExternalObject;
import org.jboss.wise.test.integration.smooks.pojo.clientside.InternalObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WiseIntegrationSmooksTest extends WiseTest {
    private URL warUrl = null;

    @Before
    public void setUp() throws Exception {
        warUrl = this.getClass().getClassLoader().getResource("smooks.war");
        deployWS(warUrl);

    }

    @Test
    public void shouldRunWithMK() throws Exception {
        WSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXWSClient(getServerHostAndPort()
                                                                                     + "/smooks/ComplexWS?wsdl");
        WSMethod method = client.getWSMethod("ComplexWSService", "ComplexWSPort", "ping");
        InternalObject internal = new InternalObject();
        internal.setNumber(new Integer(1));
        internal.setText("aa");
        ExternalObject external = new ExternalObject();
        external.setDate(new Date(0));
        external.setInternal(internal);
        // without smooks debug infos
        InvocationResult result = method.invoke(external,
                                                new SmooksMapper("META-INF/smooks/smooks-config-XMLGregorianCalendar.xml"));
        Map<String, Object> resultMap = result.getMappedResult(new SmooksMapper("META-INF/smooks/smooks-response-config.xml"));
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getInternal(), equalTo(internal));
        // just verifying not null, ignoring all annoyance of java TZ
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getDate(), notNullValue());
    }

    @Test
    public void shouldRunWithoutMK() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, true, true, "target/temp/wise", true);
        WSDynamicClient client = WSDynamicClientFactory.newInstance(config).getJAXWSClient(getServerHostAndPort()
                                                                                           + "/smooks/ComplexWS?wsdl");
        WSMethod method = client.getWSMethod("ComplexWSService", "ComplexWSPort", "ping");
        InternalObject internal = new InternalObject();
        internal.setNumber(new Integer(1));
        internal.setText("aa");
        ExternalObject external = new ExternalObject();
        external.setDate(new Date(0));
        external.setInternal(internal);
        // without smooks debug infos
        InvocationResult result = method.invoke(external,
                                                new SmooksMapper("META-INF/smooks/smooks-config-XMLGregorianCalendar.xml"));
        Map<String, Object> resultMap = result.getMappedResult(new SmooksMapper("META-INF/smooks/smooks-response-config.xml"));
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getInternal(), equalTo(internal));
        // just verifying not null, ignoring all annoyance of java TZ
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getDate(), notNullValue());
    }

    // @Test
    public void shouldRunWithoutMKNoCache() throws Exception {
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, true, true, "target/temp/wise", false);
        WSDynamicClient client = WSDynamicClientFactory.newInstance(config).getJAXWSClient(getServerHostAndPort()
                                                                                           + "/smooks/ComplexWS?wsdl");
        WSMethod method = client.getWSMethod("ComplexWSService", "ComplexWSPort", "ping");
        InternalObject internal = new InternalObject();
        internal.setNumber(new Integer(1));
        internal.setText("aa");
        ExternalObject external = new ExternalObject();
        external.setDate(new Date(0));
        external.setInternal(internal);
        // without smooks debug infos
        InvocationResult result = method.invoke(external,
                                                new SmooksMapper("META-INF/smooks/smooks-config-XMLGregorianCalendar.xml"));
        Map<String, Object> resultMap = result.getMappedResult(new SmooksMapper("META-INF/smooks/smooks-response-config.xml"));
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getInternal(), equalTo(internal));
        // just verifying not null, ignoring all annoyance of java TZ
        assertThat(((ExternalObject)resultMap.get("ExternalObject")).getDate(), notNullValue());
    }

    @After
    public void tearDown() throws Exception {
        undeployWS(warUrl);
    }
}
