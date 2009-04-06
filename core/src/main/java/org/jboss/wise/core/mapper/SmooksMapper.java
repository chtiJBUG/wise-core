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

package org.jboss.wise.core.mapper;

import java.io.IOException;
import java.util.Map;
import javax.xml.transform.Source;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.jboss.wise.core.config.WiseConfig;
import org.jboss.wise.core.exception.MappingException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wise.core.utils.SmooksCache;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.container.plugin.PayloadProcessor;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.payload.JavaResult;
import org.milyn.resource.URIResourceLocator;
import org.xml.sax.SAXException;

/**
 * A WiseMapper based on smooks
 * 
 * @author stefano.maestri@javalinux.it
 */
@ThreadSafe
@Immutable
public class SmooksMapper implements WiseMapper {

    private final Logger log = Logger.getLogger(SmooksMapper.class);

    private final Smooks smooks;
    private final String smooksReport;

    /**
     * Create this mapper using passed resource
     * 
     * @param smooksResource URI of smooks resource to use
     */
    public SmooksMapper( String smooksResource ) {
        this(smooksResource, null, null);
    }

    /**
     * Create this mapper using passed resource and passed smooks html report to generate. A SmooksMapper created with this
     * constructor will create an html smooks report useful for debug.
     * 
     * @param smooksResource URI of smooks resource to use
     * @param smooksReport the URI of smooks html report to generate.
     */
    public SmooksMapper( String smooksResource,
                         String smooksReport ) {
        this(smooksResource, smooksReport, null);
    }

    /**
     * Create this mapper using passed resource
     * 
     * @param config
     * @param smooksResource URI of smooks resource to use
     */
    public SmooksMapper( String smooksResource,
                         WiseConfig config ) {
        this(smooksResource, null, config);
    }

    /**
     * Create this mapper using passed resource and passed smooks html report to generate. A SmooksMapper created with this
     * constructor will create an html smooks report useful for debug.
     * 
     * @param config
     * @param smooksResource URI of smooks resource to use
     * @param smooksReport the URI of smooks html report to generate.
     */
    public SmooksMapper( String smooksResource,
                         String smooksReport,
                         WiseConfig config ) {
        try {
            smooks = initSmooks(smooksResource, config);
            this.smooksReport = smooksReport;
        } catch (Exception e) {
            throw new WiseRuntimeException("failde to create SmooksMapper", e);
        }
    }

    // Package protected for test purpose
    /*package*/Smooks initSmooks( String smookResources,
                                   WiseConfig config ) throws IllegalArgumentException, SAXException, IOException {
        Smooks smooks = null;
        if (config == null || config.isCacheEnabled()) {
            smooks = SmooksCache.getInstance().get(smookResources);
            if (smooks == null) {
                smooks = new Smooks();
                smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smookResources));
                SmooksCache.getInstance().put(smookResources, smooks);
            }
        } else {
            smooks = new Smooks();
            smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smookResources));
        }
        return smooks;

    }

    // package protected for test purpose
    /*package*/ExecutionContext initExecutionContext( String smooksReport ) {
        ExecutionContext executionContext = smooks.createExecutionContext();
        if (smooksReport != null) {
            try {
                executionContext.setEventListener(new HtmlReportGenerator(smooksReport));
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error during loading/instanciating Html report generator (" + smooksReport
                              + ") with exception message: " + e.getMessage());
                    log.info("Wise will continue without it");

                }
            }
        }
        return executionContext;

    }

    /**
     * apply this mapping to original object
     * 
     * @param originalObjects
     * @return Map returned is typically used to invoke webservice operations. To do this, beanids defined in smooks config (and
     *         used here as Map's keys) have to be the parameters names as defined in wsdl/wsconsume generated classes
     * @throws MappingException
     */
    public Map<String, Object> applyMapping( Object originalObjects ) throws MappingException {
        ExecutionContext executionContext = initExecutionContext(smooksReport);
        Source source;
        JavaResult result = new JavaResult();
        // Configure the execution context to generate a report...
        org.milyn.container.plugin.PayloadProcessor payloadProcessor = new PayloadProcessor(
                                                                                            smooks,
                                                                                            org.milyn.container.plugin.ResultType.JAVA);
        Map<String, Object> returnMap = (Map<String, Object>)payloadProcessor.process(originalObjects, executionContext);
        // workaround when we should use smooks to extract a single value
        // Have a look to SmooksMapperTest.shouldMapToSingleInput() for an example of use
        if (returnMap.size() == 1 && returnMap.get("singleInput") != null) {
            returnMap = (Map<String, Object>)returnMap.get("singleInput");
        }
        return returnMap;

    }

}
