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

package org.jboss.wise.core.client.jaxrs.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.impl.reflection.InvocationResultImpl;
import org.jboss.wise.core.client.jaxrs.RSDynamicClient;
import org.jboss.wise.core.mapper.WiseMapper;


/*
 * TODO:
 * 1. return headers
 * 2. Support MultipartRequestEntity
 * 3. Support HttpClient properties
 * 4. Support setting headers
 * 5. Return the result in formats other than InputStream
 * 6. Exception handling
 * 7. Using jax-rs providers
 */
@ThreadSafe
public class RSDynamicClientImpl implements RSDynamicClient {
    private final Logger log = Logger.getLogger(RSDynamicClientImpl.class);

    private String resourceURI;
    private String user;
    private String password;
    private String produceMediaTypes;
    private String consumeMediaTypes;
    private HttpMethod httpMethod;
    private HttpClient httpClient;
    private Map<String, String> requestHeaders = new HashMap<String, String>();


    /**
     * Invoke JAXRS service.
     *
     * @param resourceURI
     * @param produceMediaTypes: default to "* / *"
     * @param consumeMediaTypes: default to "* / *"
     * @param httpMethod
     */
    public RSDynamicClientImpl(String resourceURI, String produceMediaTypes, String consumeMediaTypes, HttpMethod httpMethod) {
        this.resourceURI = resourceURI;
        this.produceMediaTypes = produceMediaTypes;
        this.consumeMediaTypes = consumeMediaTypes;
        this.httpMethod = httpMethod;

        this.httpClient = new HttpClient();
    }

    public RSDynamicClientImpl(String resourceURI, String produceMediaTypes, String consumeMediaTypes, HttpMethod httpMethod, Map<String, String> requestHeaders) {
        this.resourceURI = resourceURI;
        this.produceMediaTypes = produceMediaTypes;
        this.consumeMediaTypes = consumeMediaTypes;
        this.httpMethod = httpMethod;
        this.requestHeaders = requestHeaders;

        this.httpClient = new HttpClient();
    }

    public String getResourceURI(){
        return resourceURI;
    }

    public String getUser(){
        return user;
    }

    public String getPassword(){
        return password;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getProduceMediaTypes() {
        return produceMediaTypes;
    }

    public String getConsumeMediaTypes() {
        return consumeMediaTypes;
    }

    public InvocationResult invoke(Map<String, Object> inputObjects,
                                    WiseMapper mapper) {
        //NOT SUPPORTED
        //transform inputObjects to String or InputStream using WiseMapper?
        return null;
    }

    public InvocationResult invoke(InputStream request,
                                    WiseMapper mapper) {
        return invoke(new InputStreamRequestEntity(request), mapper);
    }

    public InvocationResult invoke(String request, WiseMapper mapper) {
        RequestEntity requestEntity = null;
        try {
            requestEntity = new StringRequestEntity(request, produceMediaTypes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO:
        }
        return invoke(requestEntity, mapper);
    }

    public InvocationResult invoke(byte[] request,
                                    WiseMapper mapper) {
        return invoke(new ByteArrayRequestEntity(request), mapper);
    }

    public InvocationResult invoke(File request,
                                    WiseMapper mapper ) {
        return invoke(new FileRequestEntity(request, produceMediaTypes), mapper);
    }

    public InvocationResult invoke() {
        RequestEntity requestEntity = null;
        return invoke(requestEntity, null);
    }

    public InvocationResult invoke(RequestEntity requestEntity, WiseMapper mapper) {
        InvocationResult result = null;
        Map<String, Object> responseHolder = new HashMap<String, Object>();

        if (HttpMethod.GET == httpMethod) {
            GetMethod get = new GetMethod(resourceURI);
            setRequestHeaders(get);

            try {
                int statusCode = httpClient.executeMethod(get);
                //TODO: Use InputStream
                String response = get.getResponseBodyAsString();
                responseHolder.put(InvocationResult.STATUS, new Integer(statusCode));

                result = new InvocationResultImpl(InvocationResult.RESPONSE, response, responseHolder);

                // System.out.print(response);
            } catch(IOException e) {
                //TODO:
            } finally {
                get.releaseConnection();
            }
        } else if (HttpMethod.POST == httpMethod) {
            PostMethod post = new PostMethod(resourceURI);
            setRequestHeaders(post);

            post.setRequestEntity(requestEntity);

            try {
                int statusCode = httpClient.executeMethod(post);
                String response = post.getResponseBodyAsString();
                responseHolder.put(InvocationResult.STATUS, new Integer(statusCode));

                result = new InvocationResultImpl(InvocationResult.RESPONSE, response, responseHolder);

                // System.out.print(response);
            } catch(IOException e) {
                //TODO:
            } finally {
                post.releaseConnection();
            }
        } else if (HttpMethod.PUT == httpMethod) {
            PutMethod put = new PutMethod(resourceURI);
            setRequestHeaders(put);

            put.setRequestEntity(requestEntity);

            try {
                int statusCode = httpClient.executeMethod(put);
                String response = put.getResponseBodyAsString();
                responseHolder.put(InvocationResult.STATUS, new Integer(statusCode));

                result = new InvocationResultImpl(InvocationResult.RESPONSE, response, responseHolder);

                // System.out.print(response);
            } catch(IOException e) {
                //TODO:
            } finally {
                put.releaseConnection();
            }
        } else if (HttpMethod.DELETE == httpMethod) {
            DeleteMethod delete = new DeleteMethod(resourceURI);
            setRequestHeaders(delete);

            try {
                int statusCode = httpClient.executeMethod(delete);
                String response = delete.getResponseBodyAsString();
                responseHolder.put(InvocationResult.STATUS, new Integer(statusCode));

                result = new InvocationResultImpl(InvocationResult.RESPONSE, response, responseHolder);

                // System.out.print(response);
            } catch(IOException e) {
                //TODO:
            } finally {
                delete.releaseConnection();
            }
        }

        return result;
    }

    private void setRequestHeaders(HttpMethodBase method) {
        if (produceMediaTypes != null) {
            method.setRequestHeader("Content-Type", produceMediaTypes);
        }

        if (consumeMediaTypes != null) {
            method.setRequestHeader("Accept", consumeMediaTypes);
        }

        for (String headerName : requestHeaders.keySet()) {
            String headerValue = requestHeaders.get(headerName);

            method.setRequestHeader(headerName, headerValue);
        }
    }

}
