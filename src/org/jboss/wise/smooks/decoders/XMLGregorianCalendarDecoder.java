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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.javabean.DecodeType;
import org.milyn.javabean.decoders.DateDecoder;

/**
 * {@link javax.xml.datatype.XMLGregorianCalendar} data decoder. <p/> Decodes the supplied string into a
 * {@link javax.xml.datatype.XMLGregorianCalendar} value based on the supplied "{@link java.text.SimpleDateFormat format}"
 * parameter, or the default (see below). <p/> The default date format used is "<i>yyyy-MM-dd'T'HH:mm:ss</i>" (see
 * {@link SimpleDateFormat}). This format is based on the <a
 * href="http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#isoformats">ISO 8601</a> standard as used by the XML Schema type "<a
 * href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>". <p/> This decoder is synchronized on its underlying
 * {@link SimpleDateFormat} instance.
 * 
 * @author <a href="mailto:stefano.maestri@javalinux.it">stefano.maestri@javalinux.it</a>
 */
@DecodeType( XMLGregorianCalendar.class )
public class XMLGregorianCalendarDecoder extends DateDecoder implements DataDecoder {

    @Override
    public Object decode( String data ) throws DataDecodeException {
        Date date = (Date)super.decode(data);

        Object result = null;
        try {
            GregorianCalendar gregCal = new GregorianCalendar();
            gregCal.setTime(date);
            result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal);
        } catch (DatatypeConfigurationException e) {
            throw new DataDecodeException("Error decoding XMLGregorianCalendar data value '" + data + "' with decode format '"
                                          + format + "'.", e);
        }
        return result;
    }

}
