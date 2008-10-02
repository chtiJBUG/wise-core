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
package org.jboss.wise.core.jbossmc;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.standalone.StandaloneBootstrap;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.wise.core.exception.MCKernelUnavailableException;

/**
 * @author oracle
 */
public class MicroContainerSpi {
    public static <T> T getKernelProvidedImplementation( String beanName,
                                                         Class<T> clazz ) throws MCKernelUnavailableException {
        Kernel kernel = KernelUtil.getKernel();
        if (kernel == null) {
            try {
                kernel = bootstrap();
            } catch (Exception e) {
                e.printStackTrace();
                throw new MCKernelUnavailableException("Failed to bootstrap standalone Kernel", e);
            }
        }
        if (kernel == null) {
            throw new MCKernelUnavailableException("Kernl is null");
        }
        KernelRegistry registry = kernel.getRegistry();
        KernelRegistryEntry entry = registry.getEntry(beanName);
        return (T)entry.getTarget();
    }

    private static Kernel bootstrap() throws Exception {
        StandaloneBootstrap bootstrap = new StandaloneBootstrap(null);
        bootstrap.run();
        KernelUtil.setKernelStatic(bootstrap.getKernel());
        return KernelUtil.getKernel();
    }

}
