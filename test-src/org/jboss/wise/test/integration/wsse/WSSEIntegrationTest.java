package org.jboss.wise.test.integration.wsse;

import java.net.URL;
import java.util.Map;
import junit.framework.Assert;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
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
        warUrl = this.getWarUrl("wsse.war");
        deployWS(warUrl);

    }

    @Test
    public void testDeploy() throws Exception {
        URL wsdURL = new URL(getServerHostAndPort() + "/wsse/HelloWorld?wsdl");
        WSDynamicClientFactory factory = WSDynamicClientFactory.getInstance();
        WSDynamicClient client = factory.getClient(wsdURL.toString());
        WSMethod method = client.getWSMethod("HelloService", "HelloImplPort", "echoUserType");
        WSEndpoint wsEndpoint = method.getEndpoint();

        wsEndpoint.addHandler(new LoggingHandler());

        wsEndpoint.addWSExtension(new WSSecurityEnabler());

        Map<String, Object> args = new java.util.HashMap<String, Object>();
        args.put("user", "test");
        InvocationResult result = method.invoke(args, null);
        Map<String, Object> results = (Map<String, Object>)result.getMappedResult(null, null).get("results");
        Assert.assertEquals("Hello WSSecurity", results.get("result"));
    }

    @After
    public void tearDown() throws Exception {
        undeployWS(warUrl);
    }
}
