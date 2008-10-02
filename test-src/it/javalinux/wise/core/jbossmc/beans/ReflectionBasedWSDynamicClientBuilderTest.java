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
package it.javalinux.wise.core.jbossmc.beans;

import it.javalinux.wise.core.client.builder.WSDynamicClientBuilder;
import it.javalinux.wise.core.client.factories.WSDynamicClientFactory;
import it.javalinux.wise.core.client.impl.reflection.builder.ReflectionBasedWSDynamicClientBuilder;
import it.javalinux.wise.core.jbossmc.BeansNames;
import it.javalinux.wise.core.jbossmc.MicroContainerSpi;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test {@link ReflectionBasedWSDynamicClientBuilder}
 * 
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionBasedWSDynamicClientBuilderTest {

    WSDynamicClientBuilder builder;

    @Before
    public void before() throws Exception {
        WSDynamicClientFactory factory = MicroContainerSpi.getKernelProvidedImplementation(BeansNames.WSDynamicClientFactory.name(),
                                                                                           WSDynamicClientFactory.class);
        builder = factory.createBuilder();
    }

    @Test( expected = IllegalStateException.class )
    public void shouldThrowExceptionIfTrimmedWsdlIsNotSetted() throws Exception {
        builder.build();
    }

    @Test( expected = IllegalStateException.class )
    public void shouldThrowExceptionIfWsdlIsNull() throws Exception {
        builder.wsdlURL(null);
        builder.build();
    }

    @Test( expected = IllegalStateException.class )
    public void shouldThrowExceptionIfTrimmedWsdlLengthIsZero() throws Exception {
        builder.wsdlURL(" ");
        builder.build();
    }

    // @Test
    // public void shouldThrowExceptionIfTrimmedSymbolicNameIsZero() throws Exception {
    // WSDynamicClientBuilder builder =
    // MicroContainerSpi.getKernelProvidedImplementation(BeansNames.WSDynamicClientBuilder.name(),
    // WSDynamicClientBuilder.class);
    // WSDynamicClientBuilder.class);
    // builder.wsdlURL("aa");
    // builder.symbolicName(" ");
    // builder.build();
    // }
    //    
}
