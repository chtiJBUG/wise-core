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

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.standalone.StandaloneBootstrap;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.jboss.wise.core.consumer.impl.jbosswsnative.WSImportImpl;
import org.jboss.wise.core.exception.MCKernelUnavailableException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.jbossmc.beans.ReflectionWSDynamicClientFactory;
import org.jboss.wise.core.jbossmc.beans.WiseClientConfiguration;
import org.jboss.wise.core.wsextensions.impl.jbosswsnative.NativeSecurityConfig;
import org.jboss.wise.core.wsextensions.impl.jbosswsnative.ReflectionEnablerDelegate;

/**
 * @author stefano.maestri@javalinux.it
 */
public final class MicroContainerSpi {

    private MicroContainerSpi() {
    }

    public static <T> T getImplementation( BeansNames beanName,
                                           Class<T> clazz,
                                           WiseConfig config ) throws WiseRuntimeException {
        if (config == null) {
            try {
                return getKernelProvidedImplementation(beanName, clazz);
            } catch (MCKernelUnavailableException mke) {
                throw new WiseRuntimeException(mke);
            }
        }

        if (config instanceof WiseJBWSRefletctionConfig) {
            return getJBWSReflectionImplementation(beanName, clazz, (WiseJBWSRefletctionConfig)config);
        }

        throw new WiseRuntimeException("Not valid static configuration");

    }

    private static <T> T getKernelProvidedImplementation( BeansNames beanName,
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
            throw new MCKernelUnavailableException("Kernel is null");
        }
        KernelRegistry registry = kernel.getRegistry();
        KernelRegistryEntry entry = registry.getEntry(beanName.name());
        return (T)entry.getTarget();
    }

    private static Kernel bootstrap() throws Exception {
        StandaloneBootstrap bootstrap = new StandaloneBootstrap(null);
        bootstrap.run();
        KernelUtil.setKernelStatic(bootstrap.getKernel());
        return KernelUtil.getKernel();
    }

    private static <T> T getJBWSReflectionImplementation( BeansNames beanName,
                                                          Class<T> clazz,
                                                          WiseJBWSRefletctionConfig config ) {
        switch (beanName) {
            case EnablerDelegate:
                ReflectionEnablerDelegate enablerDelegate = new ReflectionEnablerDelegate();
                enablerDelegate.setDefaultSecurityConfig(new NativeSecurityConfig(config.getConfigFileURL(),
                                                                                  config.getConfigName()));
                return (T)enablerDelegate;
            case WSDynamicClientFactory:
                return (T)new ReflectionWSDynamicClientFactory(config);
            case WiseClientConfiguration:
                return (T)new WiseClientConfiguration(config.getTmpDir());
            case WSConsumer:
                return (T)new WSImportImpl(config.isKeepSource(), config.isVerbose());
            default:
                throw new WiseRuntimeException("Not valid static configuration for " + beanName);
        }

    }
}
