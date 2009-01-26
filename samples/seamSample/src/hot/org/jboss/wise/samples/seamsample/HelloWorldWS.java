package org.jboss.wise.samples.seamsample;

import javax.ejb.Local;

@Local
public interface HelloWorldWS
{
    // seam-gen method
    public void helloWorldWS();

    // add additional interface methods here

}
