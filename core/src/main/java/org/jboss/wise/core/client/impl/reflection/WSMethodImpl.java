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

package org.jboss.wise.core.client.impl.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.jws.Oneway;
import javax.jws.WebParam;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSEndpoint;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.exception.InvocationException;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.mapper.WiseMapper;

/**
 * Represent a webservice operation invocation
 * 
 * @author stefano.maestri@javalinux.it
 * @since 23-Aug-2007
 */
@ThreadSafe
public class WSMethodImpl implements WSMethod {

    @GuardedBy( "this" )
    private Method method;

    @GuardedBy( "this" )
    private WSEndpoint endpoint;

    private final Map<String, WebParameterImpl> parameters = Collections.synchronizedMap(new HashMap<String, WebParameterImpl>());

    public WSMethodImpl( Method method,
                         WSEndpoint endpoint ) throws IllegalArgumentException {
        if (method == null || endpoint == null) {
            throw new IllegalArgumentException();
        }
        this.method = method;
        this.endpoint = endpoint;
        this.initWebParams();
    }

    /*
     * Invokes this method with the provided arguments
     * 
     * @param args @return @throws WiseException If an unknowtn exception is received
     */
    InvocationResultImpl invoke( Map<String, Object> args ) throws InvocationException, IllegalArgumentException {
        Method methodPointer = null;
        InvocationResultImpl result = null;
        Map<String, Object> emptyHolder = Collections.emptyMap();

        try {
            Object epInstance = this.getEndpoint().getUnderlyingObjectInstance();
            methodPointer = epInstance.getClass().getMethod(this.getMethod().getName(), this.getMethod().getParameterTypes());
            if (isOneWay()) {
                synchronized (this) {
                    methodPointer.invoke(epInstance, this.getParmeterInRightPositionArray(args));
                }
                result = new InvocationResultImpl(null, null, emptyHolder);
            } else {
                synchronized (this) {
                    result = new InvocationResultImpl("result", methodPointer.invoke(epInstance,
                                                                                     this.getParmeterInRightPositionArray(args)),
                                                      getHoldersResult(args));
                }
            }
        } catch (InvocationTargetException ite) {
            System.out.print("error invoking:" + this.getMethod());
            System.out.print("error invoking:" + args.values().toArray());
            if (methodPointer != null && methodPointer.getExceptionTypes() != null) {
                for (int i = 0; i < methodPointer.getExceptionTypes().length; i++) {
                    if (ite.getCause().getClass().isAssignableFrom(methodPointer.getExceptionTypes()[i])) {
                        result = new InvocationResultImpl("exception", ite.getCause(), emptyHolder);
                        return result;
                    }
                }
            }
            throw new InvocationException("Unknown exception received: " + ite.getMessage(), ite);
        } catch (Exception e) {
            throw new InvocationException("Generic Error during method invocation!", e);
        }
        return result;
    }

    /**
     * Invokes this method with the provided arguments applying provided mapper
     * 
     * @param args
     * @param mapper if null no mappings are applied method will be invoked using args directly. in this case the keys of the map
     *        gotta be the parameters names as defined in wsdl/wsconsume generated classes
     * @return {@link InvocationResultImpl}
     * @throws InvocationException
     * @throws IllegalArgumentException
     * @throws MappingException
     */
    public InvocationResultImpl invoke( Object args,
                                        WiseMapper mapper )
        throws InvocationException, IllegalArgumentException, MappingException {
        if (mapper == null) {
            return this.invoke((Map<String, Object>)args);
        }
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Map<String, Object> mappingResults;
        try {
            Thread.currentThread().setContextClassLoader(this.getEndpoint().getClassLoader());
            mappingResults = mapper.applyMapping(args);
        } finally {
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
        return this.invoke(mappingResults);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.wise.core.client.WSMethod#invoke(java.lang.Object)
     */
    public InvocationResult invoke( Object args ) throws InvocationException, IllegalArgumentException, MappingException {
        return this.invoke(args, null);
    }

    /**
     * Gets the map of WebParameters for a selected method
     * 
     * @return a map representing valide webparameters
     */
    public Map<String, WebParameterImpl> getWebParams() {
        return parameters;
    }

    private void initWebParams() {
        Method method = this.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        Type[] methodparameterTypes = method.getGenericParameterTypes();
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof WebParam) {
                    WebParam webParaAnno = (WebParam)annotations[i][j];
                    WebParameterImpl parameter = new WebParameterImpl(methodparameterTypes[i], webParaAnno.name(), i,
                                                                      webParaAnno.mode());
                    parameters.put(parameter.getName(), parameter);
                    break;
                }
            }
        }

    }

    /* 
     * package protected method, for test purpose
     */
    /*package*/Object[] getParmeterInRightPositionArray( Map<String, Object> originalParams ) {
        Map<String, WebParameterImpl> webParams = this.getWebParams();
        Object[] arrayToReturn = new Object[webParams.size()];
        Arrays.fill(arrayToReturn, null);

        for (String key : originalParams.keySet()) {
            if (webParams.get(key) != null) {
                WebParameterImpl webPara = webParams.get(key);
                int position = webParams.get(key).getPosition();
                arrayToReturn[position] = originalParams.get(key);
            }

        }
        return arrayToReturn;
    }

    /* 
     * package protected method, for test purpose
     */
    Map<String, Object> getHoldersResult( Map<String, Object> paras ) {
        Map<String, Object> holders = new HashMap<String, Object>();
        Map<String, WebParameterImpl> webParams = this.getWebParams();

        for (String key : paras.keySet()) {
            WebParameterImpl wisePara = webParams.get(key);
            if (wisePara != null && (wisePara.getMode() == WebParam.Mode.INOUT || wisePara.getMode() == WebParam.Mode.OUT)) {
                holders.put(key, paras.get(key));
            }
        }
        return holders;
    }

    public synchronized boolean isOneWay() {
        return method.getAnnotation(Oneway.class) != null;
    }

    public synchronized Method getMethod() {
        return method;
    }

    public synchronized void setMethod( Method method ) {
        this.method = method;
    }

    public synchronized WSEndpoint getEndpoint() {
        return endpoint;
    }

    public synchronized void setEndpoint( WSEndpoint endpoint ) {
        this.endpoint = endpoint;
    }

}
