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
package org.jboss.wise.core.wsextensions.impl.jbosswsnative;

import static org.hamcrest.core.IsAnything.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.jbossmc.BeansNames;
import org.jboss.wise.core.jbossmc.MicroContainerSpi;
import org.jboss.ws.core.StubExt;
import org.jboss.ws.extensions.addressing.jaxws.WSAddressingClientHandler;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionEnablerDelegateTest {

    /**
     * Test method for
     * {@link org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate#visitMTOM(org.jboss.wise.core.client.WSEndpoint)}
     * .
     */
    @Test
    public void visitMTOMShouldSetMTOMOnBiding() {
        ReflectionEnablerDelegate delegate = new ReflectionEnablerDelegate();
        WSEndpoint endpoint = mock(WSEndpoint.class);
        BindingProvider bindingProvider = mock(BindingProvider.class);
        SOAPBinding binding = mock(SOAPBinding.class);
        stub(endpoint.getUnderlyingObjectInstance()).toReturn(bindingProvider);
        stub(bindingProvider.getBinding()).toReturn(binding);
        delegate.visitMTOM(endpoint);
        verify(binding).setMTOMEnabled(true);
    }

    /**
     * Test method for
     * {@link org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate#visitWSAddressing(org.jboss.wise.core.client.WSEndpoint)}
     * .
     */
    @Test
    public void visitWSAddressingShouldAddRightHandler() {
        ReflectionEnablerDelegate delegate = new ReflectionEnablerDelegate();
        WSEndpoint endpoint = mock(WSEndpoint.class);
        delegate.visitWSAddressing(endpoint);
        verify(endpoint).addHandler(argThat(any(WSAddressingClientHandler.class)));
    }

    /**
     * Test method for
     * {@link org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate#visitWSRM(org.jboss.wise.core.client.WSEndpoint)}
     * .
     */
    @Test( expected = UnsupportedOperationException.class )
    public void visitWSRMShouldThrowUnsupportedOperationException() {
        ReflectionEnablerDelegate delegate = new ReflectionEnablerDelegate();
        WSEndpoint endpoint = mock(WSEndpoint.class);
        delegate.visitWSRM(endpoint);

    }

    /**
     * Test method for
     * {@link org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate#visitWSSecurity(org.jboss.wise.core.client.WSEndpoint)}
     * .
     */
    @Test
    public void visitWSSecurityShouldSetConfig() {
        ReflectionEnablerDelegate delegate = MicroContainerSpi.getKernelProvidedImplementation(BeansNames.EnablerDelegate.name(),
                                                                                               ReflectionEnablerDelegate.class);

        WSEndpoint endpoint = mock(WSEndpoint.class);
        StubExt stub = mock(StubExt.class);
        stub(endpoint.getUnderlyingObjectInstance()).toReturn(stub);
        delegate.visitWSSecurity(endpoint);
        verify(stub).setSecurityConfig(anyString());
        verify(stub).setConfigName(anyString());

    }

}