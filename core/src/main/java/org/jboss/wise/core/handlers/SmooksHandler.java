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

package org.jboss.wise.core.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.utils.SmooksCache;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.javabean.BeanAccessor;
import org.milyn.resource.URIResourceLocator;

/**
 * A SOAPHandler extension. It apply smooks transformation on soap message. Transformation can also use freemarker, using provided
 * javaBeans map to get values It can apply transformation only on inbound message, outbound ones or both, depending on
 * setInBoundHandlingEnabled(boolean) and setOutBoundHandlingEnabled(boolean) methods
 * 
 * @see #setInBoundHandlingEnabled(boolean)
 * @see #setOutBoundHandlingEnabled(boolean)
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
@ThreadSafe
public class SmooksHandler implements SOAPHandler<SOAPMessageContext> {

    private final String smooksResource;

    private final Map beansMap;

    @GuardedBy( "this" )
    private boolean outBoundHandlingEnabled = true;

    @GuardedBy( "this" )
    private boolean inBoundHandlingEnabled = true;

    /**
     * @param resource URI of smooks config file
     * @param beans used for smooks BeanAccessor
     */
    public SmooksHandler( String resource,
                          Map beans ) {
        this.smooksResource = resource;
        this.beansMap = beans;
    }

    public Set getHeaders() {
        return null;
    }

    public void close( MessageContext arg0 ) {

    }

    public boolean handleFault( SOAPMessageContext arg0 ) {
        return false;
    }

    public boolean handleMessage( SOAPMessageContext smc ) {
        SOAPMessage message = smc.getMessage();
        Boolean outboundProperty = (Boolean)smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty == true && this.isOutBoundHandlingEnabled() == false) {
            return false;
        }
        if (outboundProperty == false && this.isInBoundHandlingEnabled() == false) {
            return false;
        }
        if (smooksResource != null) {
            try {
                smc.setMessage(applySmooksTransformation(message));
            } catch (Exception e) {
                return false;
            }
            return true;

        } else {
            return false;
        }

    }

    SOAPMessage applySmooksTransformation( SOAPMessage message ) throws Exception {
        ByteArrayOutputStream outStream = null;
        ByteArrayInputStream inStream = null;

        try {

            Smooks smooks = SmooksCache.getInstance().get(smooksResource);
            if (smooks == null) {
                smooks = new Smooks();
                smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smooksResource));
                SmooksCache.getInstance().put(smooksResource, smooks);
            }
            ExecutionContext executionContext = smooks.createExecutionContext();
            StringWriter transResult = new StringWriter();

            BeanAccessor.getBeans(executionContext).putAll(this.beansMap);
            StringWriter buffer;
            outStream = new ByteArrayOutputStream();
            message.writeTo(outStream);
            outStream.flush();
            inStream = new ByteArrayInputStream(outStream.toByteArray());
            smooks.filter(new StreamSource(inStream), new StreamResult(transResult), executionContext);
            inStream.close();
            inStream = new ByteArrayInputStream(transResult.toString().getBytes());
            SOAPMessage message2 = MessageFactory.newInstance().createMessage(message.getMimeHeaders(), inStream);
            return message2;
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {
                // nop
            }
            try {
                outStream.close();
            } catch (Exception e) {
                // nop
            }
        }
    }

    public synchronized boolean isOutBoundHandlingEnabled() {
        return outBoundHandlingEnabled;
    }

    /**
     * @param outBoundHandlingEnabled if true smooks transformation are applied to outBound message
     */
    public synchronized void setOutBoundHandlingEnabled( boolean outBoundHandlingEnabled ) {
        this.outBoundHandlingEnabled = outBoundHandlingEnabled;
    }

    public synchronized boolean isInBoundHandlingEnabled() {
        return inBoundHandlingEnabled;
    }

    /**
     * @param inBoundHandlingEnabled if true smooks transformation are applied to inBound message
     */
    public synchronized void setInBoundHandlingEnabled( boolean inBoundHandlingEnabled ) {
        this.inBoundHandlingEnabled = inBoundHandlingEnabled;
    }
}