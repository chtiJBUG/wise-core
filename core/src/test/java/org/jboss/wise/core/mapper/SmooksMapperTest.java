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

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jboss.wise.core.mapper.mappingObject.ExternalObject;
import org.jboss.wise.core.mapper.mappingObject.InternalObject;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class SmooksMapperTest {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Test
    public void shouldMapComplexObjectModel() throws Exception {
        WiseMapper mapper = new SmooksMapper("./smooks/smooks-config.xml", "target/smooks-report/report.html");
        Map<String, Object> originalObjects = new HashMap<String, Object>();
        ExternalObject external = new ExternalObject();
        InternalObject internal = new InternalObject();
        internal.setNumber(Integer.valueOf(1));
        internal.setText("fooText");
        external.setInternal(internal);
        originalObjects.put("external", external);
        Map<String, Object> results;
        results = mapper.applyMapping(originalObjects);
        Integer integerResult = (Integer)results.get("complexObject").getClass().getMethod("getNumberField", null).invoke(results.get("complexObject"),
                                                                                                                          null);
        String stringResult = (String)results.get("complexObject").getClass().getMethod("getTextField", null).invoke(results.get("complexObject"),
                                                                                                                     null);
        assertThat(integerResult, equalTo(internal.getNumber()));
        assertThat(stringResult, equalTo("fooText"));

    }

    @Test
    public void shouldMapObjectContainingXMLGregorianCalendarField() throws Exception {
        WiseMapper mapper = new SmooksMapper("./smooks/smooks-config-XMLGregorianCalendar.xml");
        Map<String, Object> originalObjects = new HashMap<String, Object>();
        ExternalObject external = new ExternalObject();
        String dateString = "2007-03-07T04:27:00";
        Date date = (new SimpleDateFormat(DEFAULT_DATE_FORMAT)).parse(dateString);
        external.setDate(date);
        originalObjects.put("external", external);
        Map<String, Object> results;
        results = mapper.applyMapping(originalObjects);
        long returnedTime = ((XMLGregorianCalendar)results.get("complexObject").getClass().getMethod("getDateField", null).invoke(results.get("complexObject"),
                                                                                                                                  null)).toGregorianCalendar().getTimeInMillis();
        assertThat(returnedTime, is(date.getTime()));

    }

    @Test
    public void shouldMapToSingleInput() throws Exception {
        WiseMapper mapper = new SmooksMapper("./smooks/smooks-single-input.xml");
        Map<String, Object> originalObjects = new HashMap<String, Object>();
        InternalObject internal = new InternalObject();
        internal.setNumber(Integer.valueOf(1));
        internal.setText("fooText");
        originalObjects.put("internal", internal);
        Map<String, Object> results;
        results = mapper.applyMapping(originalObjects);
        assertThat(results, hasEntry("textInput", (Object)"fooText"));

    }
}