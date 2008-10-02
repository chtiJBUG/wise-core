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
package it.javalinux.wise.core.client.impl.reflection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import it.javalinux.wise.core.mapper.WiseMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author stefano.maestri@javalinux.it
 */
public class InvocationResultImplTest {

    private InvocationResultImpl results = null;

    @Test
    public void shoudReturnAnOriginaObjectsEmptyMapIfNameIsNull() throws Exception {
        results = new InvocationResultImpl(null, new Long(1), null);
        Map<String, Object> mappedResult = results.getMappedResult(null, null);
        assertThat(((Map)mappedResult.get("results")).isEmpty(), is(true));
    }

    @Test
    public void shoudReturnAnOriginaObjectsEmptyMapIfNameIsEmptyString() throws Exception {
        results = new InvocationResultImpl(" ", new Long(1), null);
        Map<String, Object> mappedResult = results.getMappedResult(null, null);
        assertThat(((Map)mappedResult.get("results")).isEmpty(), is(true));
    }

    @Test
    public void shouldReturnOriginalObjectIfMapperIsNull() throws Exception {
        results = new InvocationResultImpl("result", new Long(1), null);
        Map<String, Object> mappedResult = results.getMappedResult(null, null);
        assertThat((Long)((Map)mappedResult.get("results")).get("result"), equalTo(new Long(1)));

    }

    @Test
    public void shouldApplyMapping() throws Exception {
        results = new InvocationResultImpl("result", new Long(1), null);
        WiseMapper mapper = mock(WiseMapper.class);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("result", new Long(2));
        stub(mapper.applyMapping(anyObject())).toReturn(map);
        Map<String, Object> mappedResult = results.getMappedResult(mapper, null);
        assertThat((Long)((Map)mappedResult).get("result"), equalTo(new Long(2)));

    }

    @Test
    public void shouldReturnInputObjectAndOriginalObjectIfMapperIsNull() throws Exception {
        results = new InvocationResultImpl("result", new Long(1), null);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("origKey", "origValue");
        Map<String, Object> mappedResult = results.getMappedResult(null, inputMap);
        assertThat((Long)((Map)mappedResult.get("results")).get("result"), equalTo(new Long(1)));
        assertThat((String)mappedResult.get("origKey"), equalTo("origValue"));

    }

    @Test
    public void shouldApplyMappingAndReturnIputMap() throws Exception {
        results = new InvocationResultImpl("result", new Long(1), null);
        WiseMapper mapper = mock(WiseMapper.class);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("result", new Long(2));
        stub(mapper.applyMapping(anyObject())).toReturn(map);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("origKey", "origValue");
        Map<String, Object> mappedResult = results.getMappedResult(null, inputMap);
        assertThat((Long)((Map)mappedResult.get("results")).get("result"), equalTo(new Long(1)));
        assertThat((String)mappedResult.get("origKey"), equalTo("origValue"));

    }

}
