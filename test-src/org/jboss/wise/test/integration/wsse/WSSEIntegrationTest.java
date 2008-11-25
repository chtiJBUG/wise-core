package org.jboss.wise.test.integration.wsse;

import java.io.File;
import java.net.URL;
import java.util.Map;
import junit.framework.Assert;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.WSService;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.test.WiseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSSEIntegrationTest extends WiseTest {
    private URL warUrl = null;
    @Before
    public void setUp() throws Exception  {       
        warUrl = this.getWarUrl("wsse.war");
        deployWS(warUrl);
  
    }
    @Test
    public void testDeploy() throws Exception {
        WSDynamicClientFactory factory =  WSDynamicClientFactory.getInstance();       
        URL wsdURL = new URL(getServerHostAndPort() + "/wsse/HelloWorld?wsdl");
        WSDynamicClient client = factory.getClient(wsdURL.toString());
        WSService wsService = client.processServices().values().iterator().next();
        WSEndpoint wsEndpoint = wsService.processEndpoints().values().iterator().next();
        URL configFile = getClass().getClassLoader().getResource("integration/test/wsse/WEB-INF/jboss-wsse-client.xml");
        File file = new File(configFile.getFile());
        URL tmp = file.toURL();
        
        URL keystore = getClass().getClassLoader().getResource("integration/test/wsse/WEB-INF/wsse.keystore");
        URL trustStore = getClass().getClassLoader().getResource("integration/test/wsse/WEB-INF/wsse.truststore");

       
        System.setProperty("org.jboss.ws.wsse.keyStore", keystore.getFile());
        System.setProperty("org.jboss.ws.wsse.trustStore", trustStore.getFile());
        System.setProperty("org.jboss.ws.wsse.keyStorePassword", "jbossws");
        System.setProperty("org.jboss.ws.wsse.trustStorePassword", "jbossws");
        System.setProperty("org.jboss.ws.wsse.keyStoreType", "jks");
        System.setProperty("org.jboss.ws.wsse.trustStoreType", "jks");
        
        
        
        wsEndpoint.setSecurityConfig(configFile.toExternalForm());
        
        WSMethod method = wsEndpoint.getWSMethods().values().iterator().next();
        Map<String, Object> args = new java.util.HashMap<String, Object>();
        args.put("user", "test");
        InvocationResult result = method.invoke(args, null);
        Map<String, Object> results = (Map<String, Object>) result.getMappedResult(null, null).get("results");
        Assert.assertEquals("Hello WSSecurity", results.get("result"));
        
    }

    
    @After
    public void tearDown() throws Exception  {  
        undeployWS(warUrl);  
    }
}
