package org.jboss.wise.test.integration.wsse;

import javax.jws.WebService;
import org.jboss.ws.annotation.EndpointConfig;
@WebService( endpointInterface = "org.jboss.wise.test.integration.wsse.Hello", targetNamespace = "http://org.jboss/wise/wssecurity", serviceName = "HelloService" )
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
public class HelloImpl implements Hello {
    public String echoUserType(String user) {
         return "Hello WSSecurity";
    }
}
