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

import java.util.HashMap;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.exception.InvocationException;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.exception.WiseConnectionException;
import org.jboss.wise.core.exception.WiseRuntimeException;

/**
 * @author oracle
 */
public class HelloWorldClient {

    /**
     * @param args
     */
    public static void main( String[] args ) {
        try {
            WSDynamicClient client = WSDynamicClientFactory.getInstance().getClient("http://127.0.0.1:8080/HelloWorld/HelloWorldWS?wsdl");
            WSMethod method = client.getWSMethod("HelloWorldWSService", "HelloWorldPort", "sayHello");
            HashMap<String, Object> requestMap = new HashMap<String, Object>();
            requestMap.put("toWhom", "SpiderMan");
            InvocationResult result = method.invoke(requestMap, null);
            System.out.println(result.getMappedResult(null, null));
            System.out.println(result.getMappedResult(null, requestMap));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (WiseConnectionException e) {
            e.printStackTrace();
        } catch (WiseRuntimeException e) {
            e.printStackTrace();
        } catch (MCKernelUnavailableException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationException e) {
            e.printStackTrace();
        } catch (MappingException e) {
            e.printStackTrace();
        }
    }

}
