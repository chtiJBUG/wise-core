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
package it.javalinux.wise.core.jbossmc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import it.javalinux.wise.core.client.builder.WSDynamicClientBuilder;
import it.javalinux.wise.core.client.factories.WSDynamicClientFactory;
import it.javalinux.wise.core.jbossmc.BeansNames;
import it.javalinux.wise.core.jbossmc.MicroContainerSpi;
import it.javalinux.wise.core.jbossmc.beans.WiseClientConfiguration;
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
        assertThat(config.getDefaultTmpDeployDir(), equalTo("/home/oracle/temp"));
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
