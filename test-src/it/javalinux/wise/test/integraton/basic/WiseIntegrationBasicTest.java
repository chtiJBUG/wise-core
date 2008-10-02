package it.javalinux.wise.test.integraton.basic;

import org.junit.Assert;
import it.javalinux.wise.core.client.InvocationResult;
import it.javalinux.wise.core.client.WSDynamicClient;
import it.javalinux.wise.core.client.WSMethod;
import it.javalinux.wise.core.client.factories.WSDynamicClientFactory;
import it.javalinux.wise.core.test.WiseTest;
import java.net.URL;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class WiseIntegrationBasicTest extends WiseTest {
    private URL warUrl = null;
    @Before
    public void setUp() throws Exception  {       
        warUrl = this.getWarUrl("basic.war"); 
        deployWS(warUrl);
  
    }
    @Test
    public void testDeploy() throws Exception {       
        URL wsdURL = new URL(getServerHostAndPort() + "/basic/HelloWorld?wsdl");
        
        WSDynamicClientFactory factory =  WSDynamicClientFactory.getInstance();
        WSDynamicClient client = factory.getClient(wsdURL.toString());
        WSMethod method = client.getWSMethod("HelloService", "HelloWorldBeanPort", "echo");
        Map<String, Object> args = new java.util.HashMap<String, Object>();
        args.put("arg0", "from-wise-client");
        InvocationResult result = method.invoke(args, null);
        Map<String, Object> res = result.getMappedResult(null, null);
        Map<String, Object> test = (Map<String, Object>)res.get("results");
        Assert.assertEquals("from-wise-client", test.get("result"));        
    }

    
    @After
    public void tearDown() throws Exception  {  
        undeployWS(warUrl);  
    }
}
