package org.jboss.wise.samples.seamsample;

import java.util.HashMap;
import javax.ejb.Stateless;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;

@Stateless
@Name( "helloWorldWS" )
public class HelloWorldWSBean implements HelloWorldWS {
    @Logger
    private Log log;

    @In
    StatusMessages statusMessages;

    public void helloWorldWS() {
        // implement your business logic here
        log.info("helloWorldWS.helloWorldWS() action begin call");
        try {
            WSDynamicClient client = WSDynamicClientFactory.getInstance().getJAXWSClient("http://127.0.0.1:8080/HelloWorld/HelloWorldServletWS?wsdl");
            WSMethod method = client.getWSMethod("HelloWorldServletWSService", "HelloWorldPort", "sayHello");
            HashMap<String, Object> requestMap = new HashMap<String, Object>();
            requestMap.put("toWhom", "SpiderMan");
            InvocationResult result = method.invoke(requestMap, null);
            statusMessages.add(result.getMapRequestAndResult(null, null).toString());
            statusMessages.add(result.getMapRequestAndResult(null, requestMap).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("helloWorldWS.helloWorldWS() action end call");

    }

    // add additional action methods

}
