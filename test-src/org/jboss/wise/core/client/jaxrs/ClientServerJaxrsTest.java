package org.jboss.wise.core.client.jaxrs;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.testutil.common.AbstractClientServerTestBase;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientServerJaxrsTest extends AbstractClientServerTestBase {
    
    @BeforeClass
    public static void startServers() throws Exception {
        assertTrue("server did not launch correctly",
                   launchServer(JaxrsServer.class));
    }
    
    @Test    
    public void testGetBook() throws Exception {
        RSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books/123", RSDynamicClient.HttpMethod.GET, null, "application/xml");
        InvocationResult result = client.invoke();
        String response = (String)result.getResult().get("result");
        
        String expected = getStringFromInputStream(
              getClass().getResourceAsStream("resources/expected_get_book123.txt"));            

        assertEquals(response, expected);
    }
    
    @Test
    public void testAddBook() throws Exception {
        RSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books", RSDynamicClient.HttpMethod.POST, "application/xml", "application/xml");

        InputStream request = getClass().getResourceAsStream("resources/add_book.txt"); 
        InvocationResult result = client.invoke(request, null);

        String response = (String)result.getResult().get("result");
        System.out.println("-------------" + response);
        
        String expected = getStringFromInputStream(
              getClass().getResourceAsStream("resources/expected_add_book.txt"));            

        assertEquals(response, expected);
    }
    
    @Test
    public void testUpdateBook() throws Exception {
        RSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books", RSDynamicClient.HttpMethod.PUT, "application/xml", "application/xml");

        InputStream request = getClass().getResourceAsStream("resources/update_book.txt"); 
        InvocationResult result = client.invoke(request, null);

        String response = (String)result.getResult().get("result");

        //TODO: Verify the status code is 200
        
        
        //verify result
        client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books/123", RSDynamicClient.HttpMethod.GET, null, "application/xml");
        result = client.invoke();
        response = (String)result.getResult().get("result");

     
        String expected = getStringFromInputStream(
              getClass().getResourceAsStream("resources/expected_update_book.txt"));            

        assertEquals(response, expected);    
        
        // Roll back changes:        
        client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books", RSDynamicClient.HttpMethod.PUT, "application/xml", "application/xml");
        request = getClass().getResourceAsStream("resources/expected_get_book123.txt"); 
        result = client.invoke(request, null);
    }   
    
    @Test
    public void testDeleteBook() throws Exception {
        RSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXRSClient("http://localhost:9080/bookstore/books/123", RSDynamicClient.HttpMethod.DELETE, "application/xml", "application/xml");

        InvocationResult result = client.invoke();
        //Verify the status code
    }
    
    private String getStringFromInputStream(InputStream in) throws Exception {        
        CachedOutputStream bos = new CachedOutputStream();
        IOUtils.copy(in, bos);
        in.close();
        bos.close();
        return bos.getOut().toString();        
    }

}
