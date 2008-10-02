package org.jboss.wise.samples;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService( name = "HelloWorldJDK6", targetNamespace = "http://wise_samples/helloworld" )
public class HelloWorldWSJDK6 {
    @WebMethod
    @WebResult( name = "result" )
    public String sayHello( @WebParam( name = "toWhom" ) String toWhom ) {

        String greeting = "Hello World Greeting for '" + toWhom + "' on " + new java.util.Date();

        return greeting;

    }
}
