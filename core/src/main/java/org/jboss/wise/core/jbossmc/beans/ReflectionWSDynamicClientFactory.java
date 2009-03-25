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

package org.jboss.wise.core.jbossmc.beans;

import org.jboss.wise.core.client.builder.WSDynamicClientBuilder;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.client.impl.reflection.builder.ReflectionBasedWSDynamicClientBuilder;
import org.jboss.wise.core.config.WiseConfig;

/**
 * @author stefano.maestri@javalinux.it
 */
public class ReflectionWSDynamicClientFactory extends WSDynamicClientFactory {

    private final WiseConfig wiseConfig;

    public ReflectionWSDynamicClientFactory() {
        super();
        this.wiseConfig = null;
    }

    /**
     * @param wiseConfig
     */
    public ReflectionWSDynamicClientFactory( WiseConfig wiseConfig ) {
        super();
        this.wiseConfig = wiseConfig;
        if (wiseConfig != null) {
            super.config = new WiseClientConfiguration(wiseConfig.getTmpDir());
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.factories.WSDynamicClientFactory#createBuilder()
     */
    @Override
    public WSDynamicClientBuilder createBuilder() {
        ReflectionBasedWSDynamicClientBuilder builder = new ReflectionBasedWSDynamicClientBuilder(wiseConfig);
        return builder;
    }

    public WiseConfig getWiseConfig() {
        return wiseConfig;
    }

    @Override
    public boolean isCacheEnabled() {
        return wiseConfig == null ? true : wiseConfig.isCacheEnabled();
    }

}