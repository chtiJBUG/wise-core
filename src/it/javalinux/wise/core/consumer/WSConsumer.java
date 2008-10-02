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
package it.javalinux.wise.core.consumer;

import it.javalinux.wise.core.exception.MCKernelUnavailableException;
import it.javalinux.wise.core.exception.WiseRuntimeException;
import it.javalinux.wise.core.jbossmc.BeansNames;
import it.javalinux.wise.core.jbossmc.MicroContainerSpi;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author stefano.maestri@javalinux.it
 */
public abstract class WSConsumer {

    private boolean keepSource;
    private boolean verbose;
    private List<String> additionalJar;

    public abstract List<String> importObjectFromWsdl( String wsdlURL,
                                                       File outputDir,
                                                       File sourceDir,
                                                       String targetPackage,
                                                       List<File> bindingFiles,
                                                       File catelog ) throws MalformedURLException, WiseRuntimeException;

    /**
     * Not a real singleton jus return right instance from MC
     * 
     * @return an instance of concrete class extending {@link #WSConsumer()}
     * @throws MCKernelUnavailableException
     */
    public static synchronized WSConsumer getInstance() throws MCKernelUnavailableException {
        return MicroContainerSpi.getKernelProvidedImplementation(BeansNames.WSConsumer.name(), WSConsumer.class);
    }

    /**
     * @return verbose
     */
    public final boolean isVerbose() {
        return verbose;
    }

    /**
     * @param verbose Sets verbose to the specified value.
     */
    public final void setVerbose( boolean verbose ) {
        this.verbose = verbose;
    }

    /**
     * @return keepSource
     */
    public final boolean isKeepSource() {
        return keepSource;
    }

    /**
     * @param keepSource Sets keepSource to the specified value.
     */
    public final void setKeepSource( boolean keepSource ) {
        this.keepSource = keepSource;
    }

    /*
     * Gets an array containing the generated class names
     *
     * @param outputDir 
     * @return the List of of generated className qualifiedName
     */
    public List<String> getClassNames( File outputDir,
                                       String targetPackage ) throws WiseRuntimeException {
        if (targetPackage == null || targetPackage.trim().length() == 0) {
            return this.getClassNames(outputDir);
        }

        List<String> classNames = new LinkedList<String>();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept( File dir,
                                   String name ) {
                return name.endsWith(".class");
            }
        };
        File scanDir = new File(outputDir.getAbsolutePath() + "/" + targetPackage.replaceAll("\\.", "/") + "/");
        String[] children = scanDir.list(filter);
        for (int i = 0; children != null && i < children.length; i++) {
            classNames.add(targetPackage + "." + children[i].substring(0, children[i].length() - 6));
        }
        if (classNames.size() == 0) {
            throw new WiseRuntimeException("No classs foung in dir " + outputDir + "for targetPackage " + targetPackage);
        }
        return classNames;

    }

    public List<String> getClassNames( File outputDir ) throws WiseRuntimeException {
        List<String> classNames = this.getClassNames(outputDir, outputDir);
        if (classNames.size() == 0) {
            throw new WiseRuntimeException("No classs foung in dir " + outputDir + "for targetPackage unspecified");
        }
        return classNames;
    }

    private List<String> getClassNames( File outputDir,
                                        File parentDir ) {
        LinkedList<String> classNames = new LinkedList<String>();
        File[] files = parentDir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                classNames.addAll(this.getClassNames(outputDir, file));
            } else {
                String className = file.getPath();
                if (className.endsWith(".class")) {
                    className = className.substring(outputDir.getPath().length() + 1, className.length() - 6);
                    className = className.replace(File.separatorChar, '.');
                    classNames.add(className);
                }
            }

        }

        return classNames;
    }

}
