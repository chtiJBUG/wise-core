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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class WSImportImplTest {

    @Test
    public void parseHelloGreetingWSDLShouldWorkWithoutPackage() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/hello_world.wsdl");
        WSImportImpl importer = new WSImportImpl();
        importer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, null, null, null);
    }

    @Test
    public void importObjectFromWsdlShouldSetProviderProperties() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/hello_world.wsdl");
        WSImportImpl.ProviderChanger providerChanger = mock(WSImportImpl.ProviderChanger.class);
        WSImportImpl importer = new WSImportImpl(providerChanger);
        importer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, null, null, null);
        verify(providerChanger).changeProvider();
        verify(providerChanger).restoreDefaultProvider();

    }

    @Test
    public void importObjectFromWsdlShouldRestoreDefaultProviderProperties() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/hello_world.wsdl");
        WSImportImpl importer = new WSImportImpl();
        String defaultProvider = System.getProperty("javax.xml.ws.spi.Provider");
        importer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, null, null, null);
        assertThat(System.getProperty("javax.xml.ws.spi.Provider"), equalTo(defaultProvider));

    }

    @Test( )
    public void parseHelloWSDLWithBindingFile() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL bindingURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/jaxws-binding.xml");
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/hello_world.wsdl");
        File bindFile = new File(bindingURL.getFile());
        List<File> bindings = new java.util.ArrayList<File>();
        bindings.add(bindFile);
        WSImportImpl importer = new WSImportImpl();
        importer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, null, bindings, null);
        File generatedClass = new File(url.getFile(), "org/mytest");
        assertTrue(generatedClass.exists());
    }

    @Test( expected = WiseRuntimeException.class )
    public void parseHelloGreetingWSDLShouldFailWithPackageAndNoBindingsForNameDuplication() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/hello_world.wsdl");
        WSImportImpl importer = new WSImportImpl();
        importer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, "org.jboss.wise", null, null);
    }

    @Test( )
    public void getClassNamesShouldFindFooClassPlaceHolder() throws Exception {
        WSImportImpl importer = new WSImportImpl();
        URL url = Thread.currentThread().getContextClassLoader().getResource("../test-resources/placeHolderClasses/");
        File file = new File(url.getFile());
        List<String> list = importer.getClassNames(file);
        assertThat(list.size(), is(1));
        assertThat(list, hasItem("org.jboss.foo.Foo"));

    }
}
