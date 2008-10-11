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
package org.jboss.wise.smooks.decoders;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.javabean.DecodeType;
import org.milyn.javabean.decoders.DateDecoder;

/**
 * {@link javax.xml.datatype.Duration} data decoder. <p/> Decodes the supplied
 * string into a {@link javax.xml.datatype.Duration} String value is supposed to
 * be millisecs representing this Duaration
 */
@DecodeType(Duration.class)
public class DurationDecoder extends DateDecoder implements DataDecoder {

    @Override
    public Object decode(String data) throws DataDecodeException {
	Object result = null;
	try {
	    result = DatatypeFactory.newInstance().newDuration(Long.parseLong(data));
	} catch (DatatypeConfigurationException e) {
	    throw new DataDecodeException("Error decoding Duration data value '" + data, e);
	}

	return result;
    }

}
