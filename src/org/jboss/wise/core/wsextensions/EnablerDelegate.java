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
package org.jboss.wise.core.wsextensions;

import org.jboss.wise.core.client.WSEndpoint;

/**
 * It is an interface defining a visitor pattern which is used by {@link WSExtensionEnabler} to delegate the effective work needed
 * to enable single extensions. The idea is to inject a {@link EnablerDelegate} implementations using IOC (aka jboss-beans.xml
 * file) having the right implementation of extension enabling for current used JAX-WS stack and/or access type to generated
 * classes (reflection vs javassist). It is in fact the way used by wise-core to decouple enabling of extension and
 * operation/configurations needed to do so. You can always provide your own implementation of {@link EnablerDelegate} if you
 * need something different during enabling of Extensions.
 * 
 * @author stefano.maestri@javalinux.it
 */
public interface EnablerDelegate {

    public void visitWSSecurity( WSEndpoint endpoint ) throws UnsupportedOperationException;

    public void visitWSRM( WSEndpoint endpoint ) throws UnsupportedOperationException;

    public void visitWSAddressing( WSEndpoint endpoint ) throws UnsupportedOperationException;

    public void visitMTOM( WSEndpoint endpoint ) throws UnsupportedOperationException;
}
