package it.javalinux.wise.core.client.factory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import it.javalinux.wise.core.client.factories.WSDynamicClientFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.junit.Test;

public class WSDynamicClientFactoryTest {

    @Test
    public void testLogger() throws Exception {
        WSDynamicClientFactory factory =  WSDynamicClientFactory.getInstance();        
        assertThat(factory == null , is(false));
        assertThat(Logger.getRootLogger().getAllAppenders() instanceof NullEnumeration , is(false));
    }
}
