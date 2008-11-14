package org.jboss.wise.samples;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;

public class Foo {
    private DataHandler dataHandler;
    
    public Foo( DataHandler dataHandler ) {
        this.dataHandler = dataHandler;
    }
    
    public Foo()
    {
        
    }
    
    @XmlMimeType("text/plain")
    public DataHandler getDataHandler() {
       return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
       this.dataHandler = dataHandler;
    }

}
