/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package it.javalinux.wise.core.handlers;

import it.javalinux.wise.core.client.factories.WSDynamicClientFactory;
import java.io.PrintStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This simple SOAPHandler will output the contents of incoming and outgoing
 * messages. Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this
 * is an outgoing or incoming message. Write a brief message to the print stream
 * and output the message. 
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private PrintStream outputStream = System.out;

    /**
     * Default constructor using default System.out PrintStream to print message
     */
    public LoggingHandler() {
        

    }

    /**
     * Constructor for custom PrintStream outputter
     * 
     * @param outStream
     *                the PrintStream to use to print messages.
     */
    public LoggingHandler(PrintStream outStream) {
	this.outputStream = outStream;
    }

    public Set<QName> getHeaders() {
	return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
	logToSystemOut(smc);
	return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
	logToSystemOut(smc);
	return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
    }

    private void logToSystemOut(SOAPMessageContext smc) {
	Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

	if (outboundProperty.booleanValue()) {
	    outputStream.println("\nOutbound message:");
	} else {
	    outputStream.println("\nInbound message:");
	}
	SOAPMessage message = smc.getMessage();
	try {
	    message.writeTo(outputStream);
	    outputStream.println(""); // just to add a newline
	} catch (Exception e) {
	    outputStream.println("Exception in handler: " + e);
	}
    }

    /**
     * 
     * @param outputStream
     *                custom PrintStream outputter
     */
    public void setOutputStream(PrintStream outputStream) {
	this.outputStream = outputStream;
    }
}
