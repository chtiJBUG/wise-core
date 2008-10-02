package org.jboss.wise.test.integraton.basic;

import javax.jws.WebService;
@WebService( endpointInterface = "org.jboss.wise.test.integraton.basic.HelloWorldInterface", targetNamespace = "http://www.javalinux.it/helloworld", serviceName = "HelloService" )
public class HelloWorldBean implements HelloWorldInterface {
    public String echo( String input ) {
        return input;
    }
}
