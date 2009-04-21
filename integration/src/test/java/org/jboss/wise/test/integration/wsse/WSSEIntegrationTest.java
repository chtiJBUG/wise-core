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
package org.jboss.wise.test.integration.wsse;

import java.net.URL;
import java.util.Map;

import junit.framework.Assert;

import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.handlers.LoggingHandler;
import org.jboss.wise.core.test.WiseTest;
import org.jboss.wise.core.wsextensions.impl.WSSecurityEnabler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSSEIntegrationTest extends WiseTest {
    private URL warUrl = null;

    @Before
    public void setUp() throws Exception {
	warUrl = this.getClass().getClassLoader().getResource("wsse.war");
	deployWS(warUrl);

    }

    @Test
    public void shouldRunWithoutMK() throws Exception {
	URL wsdlURL = new URL(getServerHostAndPort() + "/wsse/HelloWorld?wsdl");

	WSDynamicClientBuilder clientBuilder = WSDynamicClientFactory.getJAXWSClientBuilder();
	WSDynamicClient client = clientBuilder.tmpDir("target/temp/wise").verbose(true).keepSource(true)
		.securityConfigUrl("WEB-INF/wsaandwsse/jboss-wsse-client.xml").securityConfigName("Standard WSSecurity Client")
		.wsdlURL(wsdlURL.toString()).build();

	WSMethod method = client.getWSMethod("HelloService", "HelloImplPort", "echoUserType");
	WSEndpoint wsEndpoint = method.getEndpoint();

	wsEndpoint.addHandler(new LoggingHandler());

	wsEndpoint.addWSExtension(new WSSecurityEnabler(client));

	Map<String, Object> args = new java.util.HashMap<String, Object>();
	args.put("user", "test");
	InvocationResult result = method.invoke(args, null);
	Map<String, Object> results = (Map<String, Object>) result.getMapRequestAndResult(null, null).get("results");
	Assert.assertEquals("Hello WSSecurity", results.get("result"));
    }

    @After
    public void tearDown() throws Exception {
	undeployWS(warUrl);
    }
}
