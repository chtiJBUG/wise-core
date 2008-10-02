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
package org.jboss.wise.core.utils;

// $Id: IOUtils.java 4018 2007-07-27 06:31:03Z thomas.diesler@jboss.com $

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * IO utilites
 * 
 * @author stefano.maestri@javalinux.it class imported from JBossWS. Kept
 *         methods useful for Wise
 */
public final class IOUtils {
    // Hide the constructor
    private IOUtils() {
    }

    public static Writer getCharsetFileWriter(File file, String charset) throws IOException {
	return new OutputStreamWriter(new FileOutputStream(file), charset);
    }

    /**
     * True if the given type name is the source notation of a primitive or
     * array of which.
     * 
     * @param outs
     * @param ins
     * @throws IOException
     */
    public static void copyStream(OutputStream outs, InputStream ins) throws IOException {
	byte[] bytes = new byte[1024];
	int r = ins.read(bytes);
	while (r > 0) {
	    outs.write(bytes, 0, r);
	    r = ins.read(bytes);
	}
    }

}
