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

import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;
import org.jboss.wise.core.handlers.LoggingHandler;
import wise_samples.mtom.MTOM;
import wise_samples.mtom.MTOMWSService;

/**
 * @author oracle
 */
public class MTOMStandardClient {

    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception {
        try {
            // WSDynamicClient client =
            // WSDynamicClientFactory.getInstance().getClient("http://127.0.0.1:8080/MTOMSample/MTOMWS?wsdl");
            // WSMethod method = client.getWSMethod("MTOMWSService", "MTOMPort", "sayHello");
            MTOMWSService service = new MTOMWSService(new URL("http://127.0.0.1:8080/MTOMSample/MTOMWS?wsdl"), new QName("http://wise_samples/MTOM","MTOMWSService"));
            MTOM port = service.getMTOMPort();
            ((SOAPBinding)((BindingProvider)port).getBinding()).setMTOMEnabled(true);
            List<Handler> handlerChain = ((BindingProvider)port).getBinding().getHandlerChain();
            handlerChain.add(new LoggingHandler());
            ((BindingProvider)port).getBinding().setHandlerChain(handlerChain);
            byte[] bytes = port.sayHello("SpiderMan");
            if (bytes != null ) {
                System.out.println(bytes.length);
                System.out.println(bytes);
            } else {
                System.out.println("RECEIVED NULL!!");
            }
            
            wise_samples.mtom.Foo foo = port.sayHelloFoo("SuperMan");
            
            System.out.println(foo.getDataHandler());

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
