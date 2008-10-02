package org.jboss.wise.core.consumer.impl.metro;

import java.io.File;
import java.net.URL;
import org.jboss.wise.core.consumer.impl.metro.MetroWSConsumer;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.junit.Test;

public class MetroWSConsumerTest {
    @Test( expected = WiseRuntimeException.class )
    public void shouldThrowsWiseRuntimeExceptionIMetroHomeNotSet() throws Exception {
        MetroWSConsumer consumer = new MetroWSConsumer();
        // consumer.setMetroHome("/home/jimma/java/metro/1.2");
        URL url = Thread.currentThread().getContextClassLoader().getResource(".");
        File outputDir = new File(url.getFile());
        URL wsdURL = Thread.currentThread().getContextClassLoader().getResource("../test-resources/AddNumbers.wsdl");
        consumer.importObjectFromWsdl(wsdURL.toExternalForm(), outputDir, outputDir, "org.jimma", null, null);

        // Uncomment it when Metro jars are available
        // File codeGenDir = new File(outputDir, "org/jimma/");
        // Assert.assertEquals(18, codeGenDir.list().length);

    }

}
