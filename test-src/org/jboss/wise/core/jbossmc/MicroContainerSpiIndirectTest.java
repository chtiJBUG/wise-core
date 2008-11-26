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

package org.jboss.wise.core.jbossmc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.jbossmc.beans.WiseClientConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * This class aims to test {@link MicroContainerSpi} using {@link WSDynamicClientBuilder} as spi provided class It doesn't provide
 * test for {@link WSDynamicClientBuilder}, but just MC aspects
 * 
 * @author stefano.maestri@javalinux.it
 */
public class MicroContainerSpiIndirectTest {

    WiseClientConfiguration config;

    @Before
    public void before() throws Exception {
        WSDynamicClientFactory factory = MicroContainerSpi.getKernelProvidedImplementation(BeansNames.WSDynamicClientFactory.name(),
                                                                                           WSDynamicClientFactory.class);

        config = factory.getConfig();
    }

    @Test
    public void shouldGetTmpDirByIOCFromWiseConfiguration() throws Exception {
        assertThat(config.getDefaultTmpDeployDir(), equalTo("temp"));
    }

    @Test
    public void shouldGetUserNameFomItsMCConfiguration() throws Exception {
        assertThat(config.getDefaultUserName(), equalTo("foo"));
    }

    @Test
    public void shouldPermitOverridPasswordGotByMC() throws Exception {
        config.setDefaultPassword("newPWD");
        assertThat(config.getDefaultPassword(), equalTo("newPWD"));
    }

}
