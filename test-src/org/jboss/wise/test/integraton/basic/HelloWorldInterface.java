package org.jboss.wise.test.integraton.basic;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://www.javalinux.it/helloworld")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface HelloWorldInterface
{
   String echo(String input);
}
