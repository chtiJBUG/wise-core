package org.jboss.wise.test.mocks;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "PortType", targetNamespace = "http://www.javalinux.it")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PortType1 {
    @WebMethod(action = "http://www.javalinux.it/action")
    @WebResult(name = "out", targetNamespace = "http://www.javalinux.it", partName = "out")
    public String testMethod(
        @WebParam(name = "in", targetNamespace = "http://www.javalinux.it", partName = "in")
        String in);

}