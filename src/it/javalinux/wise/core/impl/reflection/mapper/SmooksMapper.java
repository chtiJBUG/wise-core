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
package it.javalinux.wise.core.impl.reflection.mapper;

import it.javalinux.wise.core.exception.MappingException;
import it.javalinux.wise.core.mapper.WiseMapper;
import it.javalinux.wise.core.utils.SmooksCache;
import java.io.IOException;
import java.util.Map;
import javax.xml.transform.Source;
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
public class SmooksMapper implements WiseMapper {

    private String smooksResource;

    private String smooksReport = null;

    /**
     * Create this mapper using passed resource
     * 
     * @param smooksResource URI of smooks resource to use
     */
    public SmooksMapper( String smooksResource ) {
        this.smooksResource = smooksResource;
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
        this.smooksResource = smooksResource;
        this.smooksReport = smooksReport;
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

        Smooks smooks = SmooksCache.getInstance().get(smooksResource);
        if (smooks == null) {
            smooks = new Smooks();
            try {
                smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smooksResource));
            } catch (IllegalArgumentException e) {
                throw new MappingException("Failed to add smooks resource to smooks cahe", e);
            } catch (SAXException e) {
                throw new MappingException("Failed to add smooks resource to smooks cahe", e);
            } catch (IOException e) {
                throw new MappingException("Failed to add smooks resource to smooks cahe", e);
            }
            SmooksCache.getInstance().put(smooksResource, smooks);
        }

        ExecutionContext executionContext = smooks.createExecutionContext();
        Source source;
        JavaResult result = new JavaResult();
        // Configure the execution context to generate a report...
        if (this.getSmooksReport() != null) {
            try {
                executionContext.setEventListener(new HtmlReportGenerator(this.getSmooksReport()));
            } catch (IOException e) {
                // TODO: add logging here!
                // we ignore this excepetion and go on without HtmlReportGenerator
                // nut at least we need to log it
            }
        }
        org.milyn.container.plugin.PayloadProcessor payloadProcessor = new PayloadProcessor(
                                                                                            smooks,
                                                                                            org.milyn.container.plugin.ResultType.JAVA);
        // smooks should return a map
        // TODO: verify with some unit tests
        Map<String, Object> returnMap = (Map<String, Object>)payloadProcessor.process(originalObjects, executionContext);
        // workaround when we would to use smooks to extract a single value
        // TODO: provide an example/unitTest of real use (take from Stefano's real world dwh application)
        if (returnMap.size() == 1 && returnMap.get("singleInput") != null) {
            returnMap = (Map<String, Object>)returnMap.get("singleInput");
        }
        return returnMap;

    }

    public String getSmooksResource() {
        return smooksResource;
    }

    public void setSmooksResource( String smooksResource ) {
        this.smooksResource = smooksResource;
    }

    public String getSmooksReport() {
        return smooksReport;
    }

    public void setSmooksReport( String smooksReport ) {
        this.smooksReport = smooksReport;
    }

}
