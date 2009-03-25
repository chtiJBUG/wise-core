/*
 * JBoss, Home of Professional Open Source Copyright 2006, JBoss Inc., and
 * individual contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of individual
 * contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */

package org.jboss.wise.samples;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.BindingType;

@WebService( name = "MTOM", targetNamespace = "http://wise_samples/MTOM" )
@SOAPBinding( style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.BARE )
@BindingType( value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true" )
public class MTOMWS {
    @WebMethod
    @WebResult( name = "result" )
    @XmlMimeType( "text/plain" )
    public DataHandler sayHello( @WebParam( name = "toWhom" ) String toWhom ) {

        String greeting = "Hello World Greeting for '" + toWhom + "' on " + new java.util.Date();

        return new DataHandler("<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello>" + greeting + "</hello>", "text/plain");

    }
}