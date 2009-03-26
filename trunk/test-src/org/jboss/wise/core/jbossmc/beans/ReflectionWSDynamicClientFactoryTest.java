/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.wise.core.jbossmc.beans;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.junit.Test;

/**
 * @author Stefano Maestri stefano.maestri@javallinux.it
 */
public class ReflectionWSDynamicClientFactoryTest {

    /**
     * Test method for {@link org.jboss.wise.core.jbossmc.beans.ReflectionWSDynamicClientFactory#isCacheEnabled()}.
     */
    @Test
    public void isCacheEnabledShouldReturnPassedValueThroughConfig() {
        WiseConfig config = mock(WiseJBWSRefletctionConfig.class);
        when(config.isCacheEnabled()).thenReturn(true).thenReturn(false);
        ReflectionWSDynamicClientFactory factory = new ReflectionWSDynamicClientFactory(config);
        assertThat(factory.isCacheEnabled(), is(true));
        assertThat(factory.isCacheEnabled(), is(false));
    }

    @Test
    public void isCacheEnabledShouldReturnTrueForConfigNull() {
        ReflectionWSDynamicClientFactory factory = new ReflectionWSDynamicClientFactory(null);
        assertThat(factory.isCacheEnabled(), is(true));
    }

}
