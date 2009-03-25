package org.jboss.wise.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;

public class HelloWorldServlet extends HttpServlet {
    @Override
    public void doGet( HttpServletRequest theRequest,
                       HttpServletResponse theResponse ) throws IOException {
        PrintWriter pw = theResponse.getWriter();

        theResponse.setContentType("text/html");

        String name = theRequest.getParameter("NAME");

        pw.print("<html><head/><body><h1>WS return: " + invokeWS(name) + "</h1></body></html>");
    }

    public String invokeWS( String name ) {
        try {
            WSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXWSClient("http://127.0.0.1:8080/HelloWorld/HelloWorldServletWS?wsdl");
            WSMethod method = client.getWSMethod("HelloWorldServletWSService", "HelloWorldPort", "sayHello");
            HashMap<String, Object> requestMap = new HashMap<String, Object>();
            requestMap.put("toWhom", name);
            InvocationResult result = method.invoke(requestMap, null);
            return result.getMapRequestAndResult(null, null).toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }
}
