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

package org.jboss.wise.core.client.factory;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.jbossmc.beans.ReflectionWSDynamicClientFactory;
import org.junit.Test;

public class WSDynamicClientFactoryTest {

    @Test
    public void testLogger() throws Exception {
        WSDynamicClientFactory factory = WSDynamicClientFactory.getInstance();
        assertThat(factory == null, is(false));
        assertThat(Logger.getRootLogger().getAllAppenders() instanceof NullEnumeration, is(false));
    }

    @Test
    public void twoInvocationOfNewInstanceShouldReturnDifferentObject() {
        WiseConfig config = mock(WiseJBWSRefletctionConfig.class);
        when(config.getTmpDir()).thenReturn("./tmp");
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        assertThat(factory, not(is(WSDynamicClientFactory.newInstance(config))));
    }

    @Test( expected = WiseRuntimeException.class )
    public void newInstanceShouldThrowWiseRuntimeExceptionForNonValidConfig() {
        WiseConfig config = mock(WiseConfig.class);
        when(config.getTmpDir()).thenReturn("./tmp");
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
    }

    @Test
    public void neInstanceShouldReturnRightClassAccordingToConfig() {
        WiseConfig config = mock(WiseJBWSRefletctionConfig.class);
        when(config.getTmpDir()).thenReturn("./tmp");
        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);
        assertThat(factory, instanceOf(ReflectionWSDynamicClientFactory.class));
        assertThat(((ReflectionWSDynamicClientFactory)factory).getWiseConfig(), is(config));
    }
}
