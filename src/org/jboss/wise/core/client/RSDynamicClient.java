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

package org.jboss.wise.core.client;

import java.security.InvalidParameterException;
import java.util.Map;
import net.jcip.annotations.Immutable;
import org.jboss.wise.core.mapper.JAXBMapper;
import org.jboss.wise.core.mapper.SmooksMapper;
import org.jboss.wise.core.mapper.WiseMapper;

/**
 * This is the Wise core class responsible to invoke the JAX-RS tools endpoint. It is used directly to represent a JAXRS service
 * and invoke it.
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 */
@Immutable
public interface RSDynamicClient {

    /**
     * JAXRS HTTP Method supported by Wise
     * 
     * @author stefano.maestri@javalinux.it
     */
    public enum HttpMethod {
        POST,
        GET,
        DELETE,
        ADD;
    }

    /**
     * @return the JAXRS resource URI to be called.
     */
    public String getResourceURI();

    /**
     * @return Usename used for HTTP authentication
     */
    public String getUser();

    /**
     * @return Password used for HTTP authentication
     */
    public String getPassword();

    /**
     * @return {@link HttpMethod} used to call the resource ({@link #getResourceURI()})
     */
    public HttpMethod getHttpMethod();

    /**
     * @return content type used to call the resource ({@link #getResourceURI()})
     */
    public String getContentType();

    /**
     * Invoke JAXRS service.
     * 
     * @param inputObjects it's a Map containing objects to call the services. They can be pojo mapped to stream using a
     *        {@link SmooksMapper}, JAXB annotated object marshaled using a {@link JAXBMapper}, a standard key/value pair with
     *        keys "ContentType" and "JAXRSStream". In the last case ContentType represent the content type of te input that have
     *        to be converted to the content type needed by service call if possible. If it isn't possible an
     *        {@link InvalidParameterException} is thrown
     * @param mapper {@link WiseMapper} used to map inputObject to stream representation used to call the service
     * @return {@link InvocationResult} implementation representing the result of JAXRS service
     */
    public InvocationResult invoke( Map<String, Object> inputObjects,
                                    WiseMapper mapper );

}
