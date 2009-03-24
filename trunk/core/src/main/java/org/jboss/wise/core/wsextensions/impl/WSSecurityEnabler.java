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

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.jbossmc.BeansNames;
import org.jboss.wise.core.jbossmc.MicroContainerSpi;
import org.jboss.wise.core.wsextensions.EnablerDelegate;
import org.jboss.wise.core.wsextensions.WSExtensionEnabler;

/**
 * It is the enabler for WS-Security extension
 * 
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
@Immutable
public class WSSecurityEnabler implements WSExtensionEnabler {

    private final WiseConfig config;

    private final EnablerDelegate delegate;

    public WSSecurityEnabler() {
        this.config = null;
        delegate = MicroContainerSpi.getImplementation(BeansNames.EnablerDelegate, EnablerDelegate.class, config);
    }

    public WSSecurityEnabler( WiseConfig config ) {
        this.config = config;
        delegate = MicroContainerSpi.getImplementation(BeansNames.EnablerDelegate, EnablerDelegate.class, config);
    }

    protected WSSecurityEnabler( WiseConfig config,
                                 EnablerDelegate delegate ) {
        this.config = config;
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionEnabler#enable(WSEndpoint)
     */
    public void enable( WSEndpoint endpoint ) throws UnsupportedOperationException {
        delegate.visitWSSecurity(endpoint);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionEnabler#getConfig()
     */
    public final WiseConfig getConfig() {
        return config;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.wsextensions.WSExtensionEnabler#getDelegate()
     */
    public EnablerDelegate getDelegate() {
        return delegate;
    }

}
