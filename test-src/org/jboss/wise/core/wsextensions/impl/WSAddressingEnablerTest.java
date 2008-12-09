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
package org.jboss.wise.core.wsextensions.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.jboss.wise.core.client.impl.reflection.WSEndpointImpl;
import org.jboss.wise.core.wsextensions.WSExtensionEnabler;
import org.jboss.wise.core.wsextensions.WSExtensionVisitor;
import org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionWSExtensionVisitor;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class WSAddressingEnablerTest {

    private final WSExtensionEnabler enabler = new WSAddressingEnabler();

    @Test
    public void shouldFindVisitorImplWithIOC() {
        assertThat(enabler.getVisitor(), is(ReflectionWSExtensionVisitor.class));
    }

    @Test
    public void shouldDelegateToVisitor() {
        WSExtensionVisitor visitor = mock(WSExtensionVisitor.class);
        WSEndpointImpl ep = mock(WSEndpointImpl.class);
        enabler.setVisitor(visitor);
        enabler.enable(ep);
        verify(visitor).visitWSAddressing(ep);

    }
}
