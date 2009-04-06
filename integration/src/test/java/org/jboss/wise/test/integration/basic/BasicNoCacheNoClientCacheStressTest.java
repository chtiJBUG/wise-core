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

package org.jboss.wise.test.integration.basic;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.jboss.wise.core.client.InvocationResult;
import org.jboss.wise.core.client.WSDynamicClient;
import org.jboss.wise.core.client.WSMethod;
import org.jboss.wise.core.client.factories.WSDynamicClientFactory;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.config.WiseJBWSRefletctionConfig;
import org.jboss.wise.core.test.WiseTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicNoCacheNoClientCacheStressTest extends WiseTest {

    private URL warUrl = null;

    // limit thread for expensive operations
    private static final int THREADS = Integer.valueOf(System.getProperty("wise.stress.expensive.threads"));
    private static final int THREAD_POOL_SIZE = Integer.valueOf(System.getProperty("wise.stress.expensive.threadPoolSize"));

    @Before
    public void setUp() throws Exception {
        warUrl = this.getClass().getClassLoader().getResource("basic.war");
        deployWS(warUrl);

    }

    @Test
    public void shouldRunWithoutMKNoCacheStressTest() throws Exception {
        System.out.println("running NOMK with THREADS=" + THREADS);
        WiseConfig config = new WiseJBWSRefletctionConfig(null, null, false, false, "target/temp/wise", false);

        URL wsdlURL = new URL(getServerHostAndPort() + "/basic/HelloWorld?wsdl");

        WSDynamicClientFactory factory = WSDynamicClientFactory.newInstance(config);

        ExecutorService es = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        FutureTask<String>[] tasks = new FutureTask[THREADS];
        for (int i = 0; i < THREADS; i++) {
            tasks[i] = new FutureTask<String>(new NoMKCallableTest(factory, wsdlURL.toString(), i));
            es.submit(tasks[i]);

        }
        for (int i = 0; i < THREADS; i++) {
            String result = tasks[i].get();
            System.out.println(i + ") " + result);
            Assert.assertEquals("from-wise-client thread #" + i, result);

        }
        es.shutdown();

    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

    public class NoMKCallableTest implements Callable<String> {

        private final WSDynamicClientFactory factory;
        private final int count;
        private final String wsdlURLString;

        public NoMKCallableTest( WSDynamicClientFactory factory,
                                 String wsdlURLString,
                                 int count ) {
            this.factory = factory;
            this.wsdlURLString = wsdlURLString;
            this.count = count;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.concurrent.Callable#call()
         */
        public String call() throws Exception {
            WSDynamicClient client = factory.getJAXWSClient(wsdlURLString);
            WSMethod method = client.getWSMethod("HelloService", "HelloWorldBeanPort", "echo");
            Map<String, Object> args = new java.util.HashMap<String, Object>();
            args.put("arg0", "from-wise-client thread #" + count);
            InvocationResult result = method.invoke(args, null);
            Map<String, Object> res = result.getMapRequestAndResult(null, null);
            Map<String, Object> test = (Map<String, Object>)res.get("results");
            return (String)test.get("result");

        }
    }

}
