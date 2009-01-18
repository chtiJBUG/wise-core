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

package org.jboss.wise.core.consumer.impl.jbosswsnative;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jboss.wise.core.consumer.WSConsumer;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.jboss.wsf.spi.tools.WSContractConsumer;

/**
 * @author stefano.maestri@javalinux.it
 */
public class WSImportImpl extends WSConsumer {

    private final String[] needeJars = {"jbossws-native-jaxws.jar", "jbossws-native-jaxws-ext.jar", "jaxb-api.jar",
        "jaxb-impl.jar"};

    ProviderChanger providerChanger = new ProviderChanger();

    @Override
    public List<String> importObjectFromWsdl( String wsdlURL,
                                              File outputDir,
                                              File sourceDir,
                                              String targetPackage,
                                              List<File> bindingFiles,
                                              File catelog ) throws MalformedURLException, WiseRuntimeException {
        try {
            // NEEDED for WISE-36 issue
            providerChanger.changeProvider();
            WSContractConsumer wsImporter = WSContractConsumer.newInstance(Thread.currentThread().getContextClassLoader());

            if (targetPackage != null && targetPackage.trim().length() > 0) {
                wsImporter.setTargetPackage(targetPackage);
            }

            wsImporter.setGenerateSource(true);
            wsImporter.setOutputDirectory(outputDir);
            wsImporter.setSourceDirectory(sourceDir);

            wsImporter.setMessageStream(System.out);
            wsImporter.setAdditionalCompilerClassPath(defineAdditionalCompilerClassPath());

            if (bindingFiles != null && bindingFiles.size() > 0) {
                wsImporter.setBindingFiles(bindingFiles);
            }

            if (catelog != null) {
                wsImporter.setCatalog(catelog);
            }

            wsImporter.consume(wsdlURL);
            return this.getClassNames(outputDir, targetPackage);
        } finally {
            // NEEDED for WISE-36 issue
            providerChanger.restoreDefaultProvider();

        }

    }

    /*
    * This is used load libraries required by tests and usually not available
    * when running out of container.
    *
    * @return A list of paths
    */
    List<String> defineAdditionalCompilerClassPath() throws WiseRuntimeException {
        List<String> cp = new LinkedList<String>();
        for (String jar : needeJars) {
            try {
                cp.add(Thread.currentThread().getContextClassLoader().getResource(jar).getPath());
            } catch (NullPointerException npe) {
                Logger.getLogger(this.getClass()).debug("Didnt't find jar needed by wsImport API:" + jar);
            }

        }
        return cp;
    }

    public class ProviderChanger {

        private final String defaultProvider = "org.jboss.ws.core.jaxws.spi.ProviderImpl";

        public void changeProvider() {
            System.setProperty("javax.xml.ws.spi.Provider", "com.sun.xml.ws.spi.ProviderImpl");
        }

        public void restoreDefaultProvider() {
            System.setProperty("javax.xml.ws.spi.Provider", defaultProvider);
        }
    }
}
